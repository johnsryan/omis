/*
 * OMIS - Officer Management Information System
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
package omis.caseload.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.caseload.dao.OfficerCaseAssignmentDao;
import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;

/**
 * Officer case assignment delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentDelegate {

	/* DAOs */
	private final OfficerCaseAssignmentDao officerCaseAssignmentDao;
	
	/* Instance factory. */
	private final InstanceFactory<OfficerCaseAssignment> 
					officerCaseAssignmentInstanceFactory;
	
	/* Helpers. */
	private final AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates an implementation of officer case assignment delegate with 
	 * the specified data access object, instance factory and audit component 
	 * retriever.
	 * 
	 * @param officerCaseAssignmentDao officer case assignment data access 
	 * object
	 * @param officerCaseAssignmentInstanceFactory officer case assignment 
	 * instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public OfficerCaseAssignmentDelegate(
			final OfficerCaseAssignmentDao officerCaseAssignmentDao,
			final InstanceFactory<OfficerCaseAssignment> 
					officerCaseAssignmentInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.officerCaseAssignmentDao = officerCaseAssignmentDao;
		this.officerCaseAssignmentInstanceFactory = 
				officerCaseAssignmentInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new officer case assignment.
	 * 
	 * @param offender offender
	 * @param officer officer
	 * @param startDate start date
	 * @param endDate end date
	 * @param supervisionOffice supervision office
	 * @param supervisionLevel supervision level category
	 * @return officer case assignment
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	public OfficerCaseAssignment create(final Offender offender, 
			final UserAccount officer, final Date startDate, final Date endDate,
			final Location supervisionOffice, 
			final SupervisionLevelCategory supervisionLevel) 
					throws DuplicateEntityFoundException, DateConflictException {
		if (this.officerCaseAssignmentDao.find(offender, officer, startDate, 
				endDate) != null) {
			throw new DuplicateEntityFoundException(
					"Officer case assignment already exists.");
		}
		if (!this.officerCaseAssignmentDao.findWithinDateRange(offender, 
				startDate, endDate).isEmpty()) {
			throw new DateConflictException(
					"Officer case assignment already exists within date range.");
		}
		OfficerCaseAssignment officerCaseAssignment = this
				.officerCaseAssignmentInstanceFactory.createInstance();
		officerCaseAssignment.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		populateOfficerCaseAssignment(officerCaseAssignment, offender, officer, 
				startDate, endDate, supervisionOffice, supervisionLevel);
		return this.officerCaseAssignmentDao.makePersistent(
				officerCaseAssignment);
	}
	
	/**
	 * Updates an existing officer case assignment.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 * @param offender offender
	 * @param officer officer
	 * @param startDate start date
	 * @param endDate end date
	 * @param supervisionOffice supervision office
	 * @param supervisionLevel supervision level category
	 * @return officer case assignment
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 * @throws DateConflictException if date conflict exists
	 */
	public OfficerCaseAssignment update(
			final OfficerCaseAssignment officerCaseAssignment, 
			final Offender offender, final UserAccount officer, 
			final Date startDate, final Date endDate,
			final Location supervisionOffice, 
			final SupervisionLevelCategory supervisionLevel) 
					throws DuplicateEntityFoundException, DateConflictException {
		if (this.officerCaseAssignmentDao.findExcluding(offender, officer, 
				startDate, endDate, officerCaseAssignment) != null) {
			throw new DuplicateEntityFoundException(
					"Officer case assignment already exists.");
		}
		if (!this.officerCaseAssignmentDao.findWithinDateRangeExcluding(
				offender, startDate, endDate, officerCaseAssignment).isEmpty()) {
			throw new DateConflictException(
					"Officer case assignment already exists within date range.");
		}
		populateOfficerCaseAssignment(officerCaseAssignment, offender, officer, 
				startDate, endDate, supervisionOffice, supervisionLevel);
		return this.officerCaseAssignmentDao.makePersistent(
				officerCaseAssignment);
	}

	/**
	 * Removes the specified officer case assignment.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 */
	public void remove(final OfficerCaseAssignment officerCaseAssignment) {
		this.officerCaseAssignmentDao.makeTransient(officerCaseAssignment);
	}
	
	// Populates an officer case assignment
	private void populateOfficerCaseAssignment(
			final OfficerCaseAssignment officerCaseAssignment,
			final Offender offender, final UserAccount officer, 
			final Date startDate, final Date endDate, 
			final Location supervisionOffice,
			final SupervisionLevelCategory supervisionLevel) {
		officerCaseAssignment.setOffender(offender);
		officerCaseAssignment.setOfficer(officer);
		officerCaseAssignment.setSupervisionLevel(supervisionLevel);
		officerCaseAssignment.setSupervisionOffice(supervisionOffice);
		officerCaseAssignment.setDateRange(new DateRange(startDate, endDate));
		officerCaseAssignment.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}