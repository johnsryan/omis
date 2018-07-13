/* 
* OMIS - Offender Management Information System 
* Copyright (C) 2011 - 2017 State of Montana 
* 
* This program is free software: you can redistribute it and/or modify 
* it under the terms of the GNU General Public License as published by 
* the Free Software Foundation, either version 3 of the License, or 
* (at your option) any later version. 
* 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
* GNU General Public License for more details. 
* 
* You should have received a copy of the GNU General Public License 
* along with this program.  If not, see <http://www.gnu.org/licenses/>. 
*/
package omis.visitation.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.visitation.dao.VisitationAssociationDao;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.exception.VisitationExistsException;

/**
 * Visitation association delegate.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (May 11, 2015)
 * @since OMIS 3.0
 */
public class VisitationAssociationDelegate {

	/* Data access objects. */
	
	private VisitationAssociationDao visitationAssociationDao;
	
	/* Instance factories. */
	
	private InstanceFactory<VisitationAssociation> 
	visitationAssociationInstanceFactory;
	
	/* Component retrievers. */
	
	private AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates a visitation association service implementation delegate.
	 * 
	 * @param visitationAssociationDao
	 * @param visitationAssociationInstanceFactory
	 * @param auditComponentRetriever
	 */
	public VisitationAssociationDelegate(
			final VisitationAssociationDao visitationAssociationDao,
			final InstanceFactory<VisitationAssociation>
			visitationAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.visitationAssociationDao = visitationAssociationDao;
		this.visitationAssociationInstanceFactory 
		= visitationAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Management methods. */
	
	/**
	 * Creates a new visitation association.
	 * 
	 * @param relationship relationship
	 * @param category visitation association category
	 * @param approval visitation approval
	 * @param startDate start date
	 * @param endDate end date
	 * @param flags visitation association flags
	 * @param notes notes
	 * @param guardianship guardianship
	 * @return newly created visitation association
	 * @throws VisitationExistsException thrown when a duplicate visitation
	 * association is found
	 * @throws DateConflictException thrown when a visitation association with
	 * the specified relationship overlaps the specified start or end date.
	 */
	public VisitationAssociation create(final Relationship relationship,
			final VisitationAssociationCategory category,
			final VisitationApproval approval, final Date startDate,
			final Date endDate, final VisitationAssociationFlags flags,
			final String notes, final String guardianship)
		throws VisitationExistsException, DateConflictException {
		if (this.visitationAssociationDao.find(relationship, startDate)
				!= null) {
			throw new VisitationExistsException(
					"Visitation already exists");
		}
		if (this.visitationAssociationDao.findDateRangeOverLap(
				relationship, new DateRange(startDate, endDate)) > 0) {
			throw new DateConflictException(
					"Conflicting visitation association date range found");
		}
		VisitationAssociation association = 
				this.visitationAssociationInstanceFactory.createInstance();
		association.setRelationship(relationship);
		association.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		this.populateVisitationAssociation(association, category,
				approval, startDate, endDate, flags, notes, guardianship);
		return this.visitationAssociationDao.makePersistent(association);
	}
	
	/**
	 * Updates the specified visitation association.
	 * 
	 * @param association visitation association
	 * @param category visitation association category
	 * @param approval visitation approval
	 * @param startDate start date
	 * @param endDate end date
	 * @param flags visitation association flags
	 * @param notes notes
	 * @param guardianship guardianship
	 * @return updated visitation association
	 * @throws DuplicateEntityFoundException thrown when a duplicate visitation
	 * association is found
	 * @throws DateConflictException thrown when a visitation association is
	 * found that overlaps the specified start date and end date with the same
	 * relationship as the specified association, excluding the specified
	 * association
	 */
	public VisitationAssociation update(final VisitationAssociation association,
			final VisitationAssociationCategory category,
			final VisitationApproval approval, final Date startDate,
			final Date endDate, final VisitationAssociationFlags flags, 
			final String notes, final String guardianship)
		throws DuplicateEntityFoundException, DateConflictException {
		if (this.visitationAssociationDao.findExcluding(association, 
				association.getRelationship(), startDate) != null)  {
			throw new DuplicateEntityFoundException(
					"Duplicate visitation association found.");
		}
		if (this.visitationAssociationDao.findDateRangeOverLapExcluding(
				association.getRelationship(),
				new DateRange(startDate, endDate), association) > 0) {
			throw new DateConflictException(
					"Conflicting visitation association date range found");
		}
		this.populateVisitationAssociation(association, category, approval,
				startDate, endDate, flags, notes, guardianship);
		return this.visitationAssociationDao.makePersistent(association);
	}
	
	/**
	 * Removes the specified visitation association.
	 * 
	 * @param association visitation association
	 */
	public void remove(final VisitationAssociation association) {
		this.visitationAssociationDao.makeTransient(association);
	}
	
	/**
	 * Sets the end date of the visitation association.
	 * 
	 * @param association visitation association
	 * @param date date to dissociate the visitation association
	 * @return dissociated visitation association
	 */
	public VisitationAssociation dissociate(
			final VisitationAssociation association, final Date date) {
		association.getDateRange().setEndDate(date);
		return this.visitationAssociationDao.makePersistent(association);
	}
	
	/**
	 * Return a list of all visitation associations with the specified offender
	 * that are either approved or have special visit privilege.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitation associations
	 */
	public List<VisitationAssociation> 
			findApprovedVisitationAssociationsByOffender(
					final Offender offender, final Date date) {
		return this.visitationAssociationDao
				.findApprovedVisitationAssociationsByOffender(offender, date);
	}
	
	/**
	 * Return a list of all visitation association with the specified offender
	 * that have special visit privileges.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return list of visitation associations
	 */
	public List<VisitationAssociation> 
			findSpecialVisitVisitationAssociationsByOffender(
					final Offender offender, final Date date) {
		return this.visitationAssociationDao
				.findSpecialVisitVisitationAssociationsByOffender(offender, 
						date);
	}
	
	/**
	 * Returns all visitation associations for the specified person.
	 * 
	 * @param person person
	 * @return
	 */
	public List<VisitationAssociation>
		findByPerson(final Person person) {
		return this.visitationAssociationDao.findByPerson(person);
	}
	
	/**
	 * Find out how many existing visitationAssociation whose date range has 
	 * overlap with the input parameter "dateRange"
	 * 
	 * @param dateRange date range
	 * @param relationship relationship
	 * @return count of overlap
	 */
	public long countForOverlapBetweenDate(final Relationship relationship,
		final DateRange dateRange) {
		long overlaps = this.visitationAssociationDao.findDateRangeOverLap(
			relationship, dateRange);
		return overlaps;
	}
	
	/**
	 * Returns count of visitation associations by relationship.
	 * 
	 * @param relationship relationship by which to count by relationships
	 * @return count of visitation associations by relationship
	 */
	public long countByRelationship(final Relationship relationship) {
		return this.visitationAssociationDao.countByRelationship(relationship);
	}
	
	/**
	 * Removes visitation associations by relationship.
	 * 
	 * @param relationship relationship by which to remove visitation
	 * associations
	 * @return number of visitation associations removed
	 */
	public int removeByRelationship(final Relationship relationship) {
		return this.visitationAssociationDao.removeByRelationship(relationship);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates the specified visitation association.
	 * 
	 * @param association visitation association
	 * @param category visitation association category
	 * @param approval visitation approval
	 * @param startDate start date
	 * @param endDate end date
	 * @param flags visitation association flags
	 * @param notes notes
	 * @param guardianship guardianship
	 * @return populated visitation association
	 */
	private VisitationAssociation populateVisitationAssociation(
			final VisitationAssociation association,
			final VisitationAssociationCategory category,
			final VisitationApproval approval, 
			final Date startDate, final Date endDate, 
			final VisitationAssociationFlags flags, final String notes,
			final String guardianship) {
		association.setCategory(category);
		association.setApproval(approval);
		association.setDateRange(new DateRange(startDate, endDate));
		association.setFlags(flags);
		association.setNotes(notes);
		association.setGuardianship(guardianship);
		association.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return association;
	}
}