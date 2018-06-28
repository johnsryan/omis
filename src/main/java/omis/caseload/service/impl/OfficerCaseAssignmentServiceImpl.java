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
package omis.caseload.service.impl;

import java.util.Date;
import java.util.List;

import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.caseload.service.OfficerCaseAssignmentService;
import omis.caseload.service.delegate.OfficerCaseAssignmentDelegate;
import omis.caseload.service.delegate.SupervisionLevelCategoryDelegate;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;

/**
 * Implementation of officer case assignment service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public class OfficerCaseAssignmentServiceImpl 
		implements OfficerCaseAssignmentService {
	
	private final OfficerCaseAssignmentDelegate officerCaseAssignmentDelegate;
	
	private final SupervisionLevelCategoryDelegate 
			supervisionLevelCategoryDelegate;
	
	private final LocationDelegate locationDelegate;
	
	private final SupervisoryOrganizationDelegate 
			supervisoryOrganizationDelegate;
	
	private final UserAccountDelegate userAccountDelegate;

	/**
	 * Instantiates an officer case assignment service implementation with the 
	 * specified delegates.
	 * 
	 * @param officerCaseAssignmentDelegate officer case assignment delegate
	 * @param supervisionLevelCategoryDelegate supervision level category 
	 * delegate
	 * @param locationDelegate location delegate
	 * @param supervisoryOrganizationDelegate supervisory organization delegate
	 * @param userAccountDelegate user account delegate
	 */
	public OfficerCaseAssignmentServiceImpl(
			final OfficerCaseAssignmentDelegate officerCaseAssignmentDelegate,
			final SupervisionLevelCategoryDelegate 
					supervisionLevelCategoryDelegate,
			final LocationDelegate locationDelegate,
			final SupervisoryOrganizationDelegate 
					supervisoryOrganizationDelegate,
			final UserAccountDelegate userAccountDelegate) {
		this.officerCaseAssignmentDelegate = officerCaseAssignmentDelegate;
		this.supervisionLevelCategoryDelegate = 
				supervisionLevelCategoryDelegate;
		this.locationDelegate = locationDelegate;
		this.supervisoryOrganizationDelegate = supervisoryOrganizationDelegate;
		this.userAccountDelegate = userAccountDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public OfficerCaseAssignment createOfficerCaseAssignment(
			final Offender offender, final UserAccount officer, 
			final Date startDate, final Date endDate, 
			final Location supervisionOffice, 
			final SupervisionLevelCategory supervisionLevel) 
					throws DuplicateEntityFoundException, DateConflictException {
		return this.officerCaseAssignmentDelegate.create(offender, officer, 
				startDate, endDate, supervisionOffice, supervisionLevel);
	}

	/** {@inheritDoc} */
	@Override
	public OfficerCaseAssignment updateOfficerCaseAssignment(
			final OfficerCaseAssignment officerCaseAssignment,
			final UserAccount officer, final Date startDate, 
			final  Date endDate, final Location supervisionOffice, 
			final SupervisionLevelCategory supervisionLevel) 
					throws DuplicateEntityFoundException, DateConflictException {
		return this.officerCaseAssignmentDelegate.update(officerCaseAssignment, 
				officerCaseAssignment.getOffender(), officer, startDate, 
				endDate, supervisionOffice, supervisionLevel);
	}

	/** {@inheritDoc} */
	@Override
	public void removeOfficerCaseAssignment(
			final OfficerCaseAssignment officerCaseAssignment) {
		this.officerCaseAssignmentDelegate.remove(officerCaseAssignment);
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisionLevelCategory> findSupervisionLevelCategories() {
		return this.supervisionLevelCategoryDelegate.findValid();
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findSupervisoryOrganizationLocations() {
		return this.locationDelegate.findByOrganizations(this
				.supervisoryOrganizationDelegate.findAll().toArray(
						new SupervisoryOrganization[0]));
	}

	/** {@inheritDoc} */
	@Override
	public UserAccount findUserAccountByUsername(final String username) {
		return this.userAccountDelegate.findByUsername(username);
	}
}