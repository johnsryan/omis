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
package omis.family.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.family.dao.FamilyAssociationDao;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;

/**
 * Family association service implementation delegate.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @author Stephen Abson
 * @version 0.1.1 (June 2, 2015)
 * @since OMIS 3.0
 */
public class FamilyAssociationDelegate {

	/* Data access objects. */
	
	private FamilyAssociationDao familyAssociationDao;
	
	/* Instance factories. */
	
	private InstanceFactory<FamilyAssociation> familyAssociationInstanceFactory;
	
	/* Component retrievers. */
	
	private AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	/**
	 * Instantiates a family association service implementation delegate with
	 * the specified data access object and instance factory.
	 * 
	 * @param familyAssociationDao family association data access object
	 * @param familyAssociationInstanceFactory family association instance
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public FamilyAssociationDelegate(
			final FamilyAssociationDao familyAssociationDao,
			final InstanceFactory<FamilyAssociation> 
			familyAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.familyAssociationDao = familyAssociationDao;
		this.familyAssociationInstanceFactory = 
				familyAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Management methods. */
	
	/**
	 * Creates a family association for the specified relationship.
	 * 
	 * @param category family association category
	 * @param marriageDate marriage date
	 * @param divorceDate divorce date
	 * @param flags family association flags
	 * @param relationship relationship
	 * @param dateRange date range
	 * @return created family association
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws FamilyAssociationConflictException 
	 * family association conflict exception
	 */
	public FamilyAssociation create(final DateRange dateRange, 
		final FamilyAssociationCategory category,
		final FamilyAssociationFlags flags, 
		final Date marriageDate, 
		final Date divorceDate, 
		final Relationship relationship)
		throws FamilyAssociationExistsException, 
		FamilyAssociationConflictException {
		if (this.familyAssociationDao.find(relationship, dateRange) != null) {
			throw new FamilyAssociationExistsException(
					"Duplicate family association found");
		}
		
		if (this.familyAssociationDao.findConflicting(relationship, 
				dateRange) > 0) {
			throw new FamilyAssociationConflictException(
				"Overlapped family association found");
		}
				
		FamilyAssociation association = this.familyAssociationInstanceFactory
			.createInstance();
		association.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		this.populateFamilyAssociation(association, category, flags,
			relationship, dateRange, marriageDate, divorceDate);
		
		return this.familyAssociationDao.makePersistent(association);
	}
	
	/**
	 * Updates the specified family association.
	 * 
	 * @param association family association
	 * @param dateRange date range
	 * @param category family association category
	 * @param flags family association flags
	 * @param marriageDate marriage date
	 * @param divorceDate divorce date
	 * @return update family association
	 * @throws FamilyAssociationExistsException family association exists exception
	 * @throws FamilyAssociationConflictException 
	 * family association conflict exception
	 */
	public FamilyAssociation update(final FamilyAssociation association, 
		final DateRange dateRange, final FamilyAssociationCategory category, 
		final FamilyAssociationFlags flags, final Date marriageDate, 
		final Date divorceDate) throws FamilyAssociationExistsException,
		FamilyAssociationConflictException {
		if (this.familyAssociationDao.findExcluding(association,
			association.getRelationship(), dateRange) != null) {
			throw new FamilyAssociationExistsException(
					"Duplicate family association found");
		}
		
		if (this.familyAssociationDao.findConflictingExcluding(
			association.getRelationship(), dateRange, association) > 0) {
			throw new FamilyAssociationConflictException(
				"Overlapped family association found");
		}
		
		association.setCategory(category);
		association.setDateRange(dateRange);
		association.setFlags(flags);
//		association.setRelationship(relationship);
		association.setDivorceDate(divorceDate);
		association.setMarriageDate(marriageDate);
		association.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.familyAssociationDao.makePersistent(association);
	}
	
	/**
	 * Removes the specified family association.
	 * 
	 * @param associaiton family association
	 */
	public void remove(final FamilyAssociation associaiton) {
		this.familyAssociationDao.makeTransient(associaiton);
	}
	
	/**
	 * Find if there is existing familyAssociation whose date range has overlap 
	 * with the input parameter "dateRange".
	 * 
	 * @param dateRange date range
	 * @param relationship relationship
	 * @return count of overlap
	 */
	public long findDateRangeOverlap(final Relationship relationship,
		final DateRange dateRange) {
		long overlaps = this.familyAssociationDao.findDateRangeOverLap(
			relationship, dateRange);
		return overlaps;
	}
	
	/**
	 * Find specific offender's family associations.
	 * 
	 * @param offender offender
	 * @return A list of family associations
	 */
	public List<FamilyAssociation> findByOffender(final Offender offender) {
		return this.familyAssociationDao.findByOffender(offender);
	}

	/**
	 * Finds all family associations for the specified person.
	 * 
	 * @param person person
	 * @return list of family associations
	 */
	public List<FamilyAssociation> findByPerson(final Person person) {
		return this.familyAssociationDao.findByPerson(person);
	}
	
	/**
	 * Returns count of family associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of family associations by relationship
	 */
	public long countByRelationship(final Relationship relationship) {
		return this.familyAssociationDao.countByRelationship(relationship);
	}
	
	/**
	 * Removes family associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return number of family associations removed by relationship
	 */
	public int removeByRelationship(final Relationship relationship) {
		return this.familyAssociationDao.removeByRelationship(relationship);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates the specified family association with the specified properties,
	 * and an update signature.
	 * 
	 * @param association family association
	 * @param category family association category
	 * @param flags family association flags
	 * @param relationship relationship
	 * @param dateRange date range
	 * @param divorceDate divorce date
	 * @param marriageDate marriage date
	 * @return populated family association
	 */
	private FamilyAssociation populateFamilyAssociation(
			final FamilyAssociation association,
			final FamilyAssociationCategory category,
			final FamilyAssociationFlags flags, final Relationship relationship,
			final DateRange dateRange,
			final Date marriageDate, 
			final Date divorceDate) {
		association.setCategory(category);
		association.setDateRange(dateRange);
		association.setFlags(flags);
		association.setRelationship(relationship);
		association.setDivorceDate(divorceDate);
		association.setMarriageDate(marriageDate);
		association.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return association;
	}
}