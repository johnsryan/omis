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
package omis.supervision.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.location.dao.LocationDao;
import omis.location.domain.Location;
import omis.locationterm.dao.LocationTermDao;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;
import omis.region.dao.StateDao;
import omis.region.domain.State;
import omis.supervision.dao.CorrectionalStatusDao;
import omis.supervision.dao.CorrectionalStatusTermDao;
import omis.supervision.dao.PlacementTermChangeReasonDao;
import omis.supervision.dao.PlacementTermDao;
import omis.supervision.dao.SupervisoryOrganizationDao;
import omis.supervision.dao.SupervisoryOrganizationTermDao;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.PlacementTermNote;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermExistsAfterException;
import omis.supervision.exception.PlacementTermExistsBeforeException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.PlacementTermLockedException;
import omis.supervision.exception.PlacementTermNoteExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.service.PlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermNoteDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.util.DateManipulator;

/**
 * Implementation of service for placement terms.
 * 
 * @author Stephen Abson
 * @author Jason Nelson
 * @version 0.1.0 (Oct 9, 2013)
 * @since OMIS 3.0
 */
public class PlacementTermServiceImpl
		implements PlacementTermService {

	private final InstanceFactory<PlacementTerm> placementTermInstanceFactory;
	
	private final PlacementTermDao placementTermDao;
	
	private final CorrectionalStatusDao correctionalStatusDao;

	private final InstanceFactory<CorrectionalStatusTerm>
	correctionalStatusTermInstanceFactory;
	
	private final CorrectionalStatusTermDao correctionalStatusTermDao;

	private final SupervisoryOrganizationDao supervisoryOrganizationDao;

	private final InstanceFactory<SupervisoryOrganizationTerm>
	supervisoryOrganizationTermInstanceFactory;
	
	private final SupervisoryOrganizationTermDao supervisoryOrganizationTermDao;

	private final PlacementTermChangeReasonDao placementTermChangeReasonDao;
	
	private final StateDao stateDao;
	
	private final LocationDao locationDao;
	
	private final LocationTermDao locationTermDao;
	
	private final PlacementTermNoteDelegate placementTermNoteDelegate;
	
	private final CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	private final SupervisoryOrganizationTermDelegate
	supervisoryOrganizationTermDelegate;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Instantiates an implementation of service for placement terms.
	 * 
	 * @param placementTermInstanceFactory instance factory for placement terms
	 * @param placementTermDao data access object for placement terms
	 * @param correctionalStatusDao data access object for correctional statuses
	 * @param correctionalStatusTermDao data access object for correctional 
	 * status terms
	 * @param supervisoryOrganizationTermDao data access object for
	 * supervisory organization terms
	 * @param supervisoryOrganizationDao data access object for supervisory
	 * organizations
	 * @param placementTermChangeReasonDao data access object for placement
	 * term change reasons
	 * @param staffAssignmentDao data access object for staff assignments
	 * @param stateDao data access object for States
	 * @param locationDao data access object for locations
	 * @param locationTermDao data access object for location terms
	 * @param placementTermNoteDelegate delegate for notes for placement terms
	 * @param correctionalStatusTermDelegate delegate for correctional status
	 * terms
	 * @param supervisoryOrganizationTermDelegate delegate for supervisory
	 * organization terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public PlacementTermServiceImpl(
			final InstanceFactory<PlacementTerm>
			placementTermInstanceFactory,
			final PlacementTermDao placementTermDao,
			final CorrectionalStatusDao correctionalStatusDao,
			final InstanceFactory<CorrectionalStatusTerm>
			correctionalStatusTermInstanceFactory,
			final CorrectionalStatusTermDao correctionalStatusTermDao,
			final SupervisoryOrganizationDao supervisoryOrganizationDao,
			final InstanceFactory<SupervisoryOrganizationTerm>
			supervisoryOrganizationTermInstanceFactory,
			final SupervisoryOrganizationTermDao supervisoryOrganizationTermDao,
			final PlacementTermChangeReasonDao placementTermChangeReasonDao,
			final StateDao stateDao,
			final LocationDao locationDao,
			final LocationTermDao locationTermDao,
			final PlacementTermNoteDelegate placementTermNoteDelegate,
			final CorrectionalStatusTermDelegate correctionalStatusTermDelegate,
			final SupervisoryOrganizationTermDelegate
			supervisoryOrganizationTermDelegate,
			final AuditComponentRetriever auditComponentRetriever) {
		this.placementTermInstanceFactory = placementTermInstanceFactory;
		this.placementTermDao = placementTermDao;
		this.correctionalStatusDao = correctionalStatusDao;
		this.correctionalStatusTermInstanceFactory
			= correctionalStatusTermInstanceFactory;
		this.correctionalStatusTermDao = correctionalStatusTermDao;
		this.supervisoryOrganizationDao = supervisoryOrganizationDao;
		this.supervisoryOrganizationTermInstanceFactory	
			= supervisoryOrganizationTermInstanceFactory;
		this.supervisoryOrganizationTermDao = supervisoryOrganizationTermDao;
		this.placementTermChangeReasonDao = placementTermChangeReasonDao;
		this.stateDao = stateDao;
		this.locationDao = locationDao;
		this.locationTermDao = locationTermDao;
		this.placementTermNoteDelegate = placementTermNoteDelegate;
		this.correctionalStatusTermDelegate = correctionalStatusTermDelegate;
		this.supervisoryOrganizationTermDelegate
			= supervisoryOrganizationTermDelegate;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTerm> findByOffender(final Offender offender) {
		return this.placementTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findByOffenderOnDate(
			final Offender offender, final Date date) {
		return this.placementTermDao.findForOffenderOnDate(offender, date);
	}
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm create(final Offender offender,
			final SupervisoryOrganization supervisoryOrganization,
			final CorrectionalStatus correctionalStatus,
			final DateRange dateRange,
			final PlacementTermChangeReason startChangeReason,
			final PlacementTermChangeReason endChangeReason)
					throws PlacementTermExistsException,
						CorrectionalStatusTermConflictException,
						SupervisoryOrganizationTermConflictException,
						PlacementTermConflictException {
		
		// Determines a date range
		final Date startDate = DateRange.getStartDate(dateRange);
		final Date endDate = DateRange.getEndDate(dateRange);
		
		// Prevents dates from being equal unless both are null
		if (startDate != null && startDate.equals(endDate)) {
			throw new IllegalArgumentException(
					"Start and end date cannot be equal");
		}
		
		// Checks for duplicate, throw exception if found
		if (this.placementTermDao.find(offender, startDate, endDate,
				correctionalStatus, supervisoryOrganization) != null) {
			throw new PlacementTermExistsException("Placement term exists");
		}
		
		// Adjusts end date of placement term on start date and start date
		// of placement term on end date if existing
		PlacementTerm startPlacementTerm = this.placementTermDao
				.findForOffenderOnDate(offender, startDate);
		PlacementTerm endPlacementTerm = this.placementTermDao
				.findForOffenderOnDate(offender, endDate);
		if (startPlacementTerm != null) {
			if (startPlacementTerm.equals(endPlacementTerm)) {
				
				// TODO: Implement splitting of placement terms - SA
				throw new UnsupportedOperationException(
						"Don\'t know how to split placement terms");
			}
			if (startDate == null && (startPlacementTerm.getDateRange()
					== null || startPlacementTerm.getDateRange().getEndDate()
					== null)) {
				throw new PlacementTermConflictException(
						"Conflict with existing placement term");
			}
			startPlacementTerm.setDateRange(
					DateRange.adjustEndDate(startPlacementTerm.getDateRange(),
							startDate));
			startPlacementTerm.setUpdateSignature(this.createUpdateSignature());
			this.placementTermDao.makePersistent(startPlacementTerm);
		}
		if (endPlacementTerm != null) {
			if (endDate == null && (endPlacementTerm.getDateRange() == null
					|| endPlacementTerm.getDateRange().getStartDate()
					== null)) {
				throw new PlacementTermConflictException(
						"Conflict with existing placement term");
			}
			endPlacementTerm.setDateRange(DateRange.adjustStartDate(
					endPlacementTerm.getDateRange(), endDate));
			endPlacementTerm.setUpdateSignature(this.createUpdateSignature());
			this.placementTermDao.makePersistent(endPlacementTerm);
		}
		
		// Throws exception if placement term conflicts with existing
		// placement terms
		long placementTermCountBetweenDates;
		if (startPlacementTerm == null && endPlacementTerm == null) {
			placementTermCountBetweenDates
				= this.placementTermDao.countForOffenderBetweenDates(
					offender, startDate, endDate);
		} else {
			PlacementTerm[] excludedPlacementTerms;
			if (startPlacementTerm != null && endPlacementTerm != null) {
				excludedPlacementTerms = new PlacementTerm[] {
					startPlacementTerm, endPlacementTerm
				};
			} else if (startPlacementTerm != null) {
				excludedPlacementTerms = new PlacementTerm[] {
					startPlacementTerm
				};
			} else if (endPlacementTerm != null) {
				excludedPlacementTerms = new PlacementTerm[] {
					endPlacementTerm
				};
			} else {
				
				// Logically impossible
				throw new AssertionError();
			}
			placementTermCountBetweenDates
				= this.placementTermDao.countForOffenderBetweenDatesExcluding(
					offender, startDate, endDate, excludedPlacementTerms);
		}
		if (placementTermCountBetweenDates > 0) {
			throw new PlacementTermConflictException(
					"Conflict with existing placement term");
		}

		// Determines correctional status term. Use existing if the correctional
		// status matches that of the placement term - otherwise end existing
		// and create a new one
		CorrectionalStatusTerm correctionalStatusTerm;
		CorrectionalStatusTerm startCorrectionalStatusTerm
			= this.correctionalStatusTermDao.findForOffenderOnDate(
					offender, startDate);
		CorrectionalStatusTerm endCorrectionalStatusTerm
			= this.correctionalStatusTermDao.findForOffenderOnDate(
					offender, endDate);
		if ((startCorrectionalStatusTerm != null
				&& startCorrectionalStatusTerm.equals(
						endCorrectionalStatusTerm))
				&& correctionalStatus.equals(
						startCorrectionalStatusTerm.getCorrectionalStatus())) {
			
			// Nothing needs to be done
			correctionalStatusTerm = startCorrectionalStatusTerm;
		} else if ((startCorrectionalStatusTerm != null
				&& startCorrectionalStatusTerm.equals(
						endCorrectionalStatusTerm))
				&& !correctionalStatus.equals(
						startCorrectionalStatusTerm.getCorrectionalStatus())) {
			
			// TODO: Implement splitting of correctional status terms - SA
			throw new UnsupportedOperationException(
					"Don\'t know how to split correctional status terms");
		} else {
			if (startCorrectionalStatusTerm != null) {
				if (startCorrectionalStatusTerm.getCorrectionalStatus()
						.equals(correctionalStatus)) {
					
					// Use starting correctional status term if matching
					// Adjust end date of starting correctional status to
					// if before end date of placement term
					correctionalStatusTerm = startCorrectionalStatusTerm;
					if (startCorrectionalStatusTerm.getDateRange() != null
							&& startCorrectionalStatusTerm.getDateRange()
								.getEndDate() != null) {
						if (endDate == null || (endDate != null
								&& startCorrectionalStatusTerm.getDateRange()
									.getEndDate().getTime()
										< endDate.getTime())) {
							startCorrectionalStatusTerm.setDateRange(
									DateRange.adjustEndDate(
											startCorrectionalStatusTerm
												.getDateRange(), endDate));
							startCorrectionalStatusTerm.setUpdateSignature(
									this.createUpdateSignature());
							this.correctionalStatusTermDao
								.makePersistent(startCorrectionalStatusTerm);
						}
					}
				} else {
					
					// End starting correctional status and start new one
					startCorrectionalStatusTerm.setDateRange(
							DateRange.adjustEndDate(
									startCorrectionalStatusTerm.getDateRange(),
									startDate));
					startCorrectionalStatusTerm.setUpdateSignature(
							this.createUpdateSignature());
					this.correctionalStatusTermDao
						.makePersistent(startCorrectionalStatusTerm);
					correctionalStatusTerm = this.createCorrectionalStatusTerm(
							offender, correctionalStatus, dateRange);
				}
			} else {
				
				// Use end correctional status term if available and has a
				// matching correctional status
				// Adjust start date if necessary
				if (endCorrectionalStatusTerm != null
						&& correctionalStatus.equals(
								endCorrectionalStatusTerm
									.getCorrectionalStatus())) {
					correctionalStatusTerm = endCorrectionalStatusTerm;
					if (endCorrectionalStatusTerm.getDateRange() != null
							&& endCorrectionalStatusTerm.getDateRange()
								.getStartDate() != null) {
						if (startDate == null || (startDate != null
								&&endCorrectionalStatusTerm.getDateRange()
									.getStartDate().getTime()
										> startDate.getTime())) {
							endCorrectionalStatusTerm.setDateRange(
									DateRange.adjustStartDate(
										endCorrectionalStatusTerm
											.getDateRange(), startDate));
							endCorrectionalStatusTerm.setUpdateSignature(
									this.createUpdateSignature());
							this.correctionalStatusTermDao
								.makePersistent(endCorrectionalStatusTerm);
						}
					}
				} else {
					
					// Start new correctional status
					correctionalStatusTerm = this.createCorrectionalStatusTerm(
						offender, correctionalStatus, dateRange);
				}
			}
			
			// Start ending correctional status term on end date of placement
			// term
			if (endCorrectionalStatusTerm != null) {
				if (!correctionalStatus.equals(
						endCorrectionalStatusTerm.getCorrectionalStatus())) {
					endCorrectionalStatusTerm.setDateRange(
							DateRange.adjustStartDate(
									endCorrectionalStatusTerm.getDateRange(),
									endDate));
					endCorrectionalStatusTerm.setUpdateSignature(
							this.createUpdateSignature());
					this.correctionalStatusTermDao.makePersistent(
							endCorrectionalStatusTerm);
				}
			}
		}
		
		// Ensures no other correctional status terms exist during range
		if (this.correctionalStatusTermDelegate
				.countForOffenderBetweenDatesExcluding(
					offender, startDate, endDate, startCorrectionalStatusTerm,
					endCorrectionalStatusTerm) > 0) {
			throw new CorrectionalStatusTermConflictException(
					"Conflicting correctional status terms exist");
		}
		
		// Saves correctional status term
		this.correctionalStatusTermDao.makePersistent(correctionalStatusTerm);
		
		// Determines supervisory organization term. Use existing if the
		// supervisory organization term matches that of the placement term
		// - otherwise end existing  and create a new one
		SupervisoryOrganizationTerm supervisoryOrganizationTerm;
		SupervisoryOrganizationTerm startSupervisoryOrganizationTerm
			= this.supervisoryOrganizationTermDao.findForOffenderOnDate(
					offender, startDate);
		SupervisoryOrganizationTerm endSupervisoryOrganizationTerm
			= this.supervisoryOrganizationTermDao.findForOffenderOnDate(
					offender, endDate);
		if ((startSupervisoryOrganizationTerm != null
				&& startSupervisoryOrganizationTerm.equals(
						endSupervisoryOrganizationTerm))
				&& supervisoryOrganization.equals(
						startSupervisoryOrganizationTerm
							.getSupervisoryOrganization())) {
			
			// Nothing needs to be done
			supervisoryOrganizationTerm = startSupervisoryOrganizationTerm;
		} else if ((startSupervisoryOrganizationTerm != null
				&& startSupervisoryOrganizationTerm.equals(
						endSupervisoryOrganizationTerm))
				&& !supervisoryOrganization.equals(
						startSupervisoryOrganizationTerm
							.getSupervisoryOrganization())) {
			
			// TODO: Implement splitting of supervisory organization terms - SA
			throw new UnsupportedOperationException(
					"Don\'t know how to split supervisory organization terms");
		} else {
			if (startSupervisoryOrganizationTerm != null) {
				if (startSupervisoryOrganizationTerm
							.getSupervisoryOrganization()
						.equals(supervisoryOrganization)) {
					
					// Use starting supervisory organization term if matching
					// Adjust end date of starting supervisory organization to
					// if before end date of placement term
					supervisoryOrganizationTerm
						= startSupervisoryOrganizationTerm;
					if (startSupervisoryOrganizationTerm.getDateRange() != null
							&& startSupervisoryOrganizationTerm.getDateRange()
								.getEndDate() != null) {
						if (endDate == null || (endDate != null
								&& startSupervisoryOrganizationTerm
									.getDateRange().getEndDate().getTime()
										< endDate.getTime())) {
							startSupervisoryOrganizationTerm.setDateRange(
									DateRange.adjustEndDate(
											startSupervisoryOrganizationTerm
												.getDateRange(), endDate));
							startSupervisoryOrganizationTerm.setUpdateSignature(
									this.createUpdateSignature());
							startSupervisoryOrganizationTerm.setUpdateSignature(
									this.createUpdateSignature());
							this.supervisoryOrganizationTermDao.makePersistent(
									startSupervisoryOrganizationTerm);
						}
					}
				} else {
					
					// End starting supervisory organization and start new one
					startSupervisoryOrganizationTerm.setDateRange(
							DateRange.adjustEndDate(
									startSupervisoryOrganizationTerm
										.getDateRange(), startDate));
					startSupervisoryOrganizationTerm.setUpdateSignature(
							this.createUpdateSignature());
					this.supervisoryOrganizationTermDao
							.makePersistent(startSupervisoryOrganizationTerm);
					supervisoryOrganizationTerm
						= this.createSupervisoryOrganizationTerm(
							offender, supervisoryOrganization, dateRange);
				}
			} else {
				
				// Use end supervisory organization term if available and has a
				// matching supervisory organization
				// Adjust start date if necessary
				if (endSupervisoryOrganizationTerm != null
						&& supervisoryOrganization.equals(
								endSupervisoryOrganizationTerm
									.getSupervisoryOrganization())) {
					supervisoryOrganizationTerm
						= endSupervisoryOrganizationTerm;
					if (endSupervisoryOrganizationTerm.getDateRange() != null
							&& endSupervisoryOrganizationTerm.getDateRange()
								.getStartDate() != null) {
						if (startDate == null || (startDate != null
								&& endSupervisoryOrganizationTerm.getDateRange()
									.getStartDate().getTime()
										> startDate.getTime())) {
							endSupervisoryOrganizationTerm.setDateRange(
									DateRange.adjustStartDate(
										endSupervisoryOrganizationTerm
											.getDateRange(), startDate));
							endSupervisoryOrganizationTerm.setUpdateSignature(
									this.createUpdateSignature());
							this.supervisoryOrganizationTermDao.makePersistent(
									endSupervisoryOrganizationTerm);
						}
					}
				} else {
					
					// Start new supervisory organization
					supervisoryOrganizationTerm
						= this.createSupervisoryOrganizationTerm(
								offender, supervisoryOrganization, dateRange);
				}
			}
			
			// Start ending supervisory organization term on end date of
			// placement term
			if (endSupervisoryOrganizationTerm != null) {
				if (!supervisoryOrganization.equals(
						endSupervisoryOrganizationTerm
							.getSupervisoryOrganization())) {
					endSupervisoryOrganizationTerm.setDateRange(
							DateRange.adjustStartDate(
									endSupervisoryOrganizationTerm
										.getDateRange(), endDate));
					endSupervisoryOrganizationTerm.setUpdateSignature(
							this.createUpdateSignature());
					this.supervisoryOrganizationTermDao.makePersistent(
							endSupervisoryOrganizationTerm);
				}
			}
		}
		
		// Ensures no other supervisory organization terms exist during range
		if (this.supervisoryOrganizationTermDelegate
				.countForOffenderBetweenDatesExcluding(
						offender, startDate, endDate,
						startSupervisoryOrganizationTerm,
						endSupervisoryOrganizationTerm) > 0) {
			throw new SupervisoryOrganizationTermConflictException(
					"Conflicting supervisory organization terms exist");
		}
		
		// Saves supervisory organization term
		this.supervisoryOrganizationTermDao
			.makePersistent(supervisoryOrganizationTerm);
				
		// Creates, persists and returns new placement term
		PlacementTerm placementTerm
			= this.placementTermInstanceFactory.createInstance();
		placementTerm.setCreationSignature(this.createCreationSignature());
		placementTerm.setOffender(offender);
		placementTerm.setUpdateSignature(this.createUpdateSignature());
		placementTerm.setStatus(PlacementStatus.PLACED);
		placementTerm.setStatusDateRange(null);
		placementTerm.setDateRange(dateRange);
		placementTerm
			.setSupervisoryOrganizationTerm(supervisoryOrganizationTerm);
		placementTerm.setCorrectionalStatusTerm(correctionalStatusTerm);
		placementTerm.setStartChangeReason(startChangeReason);
		placementTerm.setEndChangeReason(endChangeReason);
		placementTerm.setLocked(false);
		return this.placementTermDao.makePersistent(placementTerm);
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm changeSupervisoryOrganization(
			final Offender offender,
			final SupervisoryOrganization supervisoryOrganization,
			final DateRange dateRange,
			final PlacementTermChangeReason startChangeReason,
			final PlacementTermChangeReason endChangeReason)
					throws SupervisoryOrganizationTermConflictException,
						PlacementTermConflictException, 
						PlacementTermExistsAfterException,
						PlacementTermExistsBeforeException, 
						OffenderNotUnderSupervisionException {
		Date startDate = DateRange.getStartDate(dateRange);
		Date endDate = DateRange.getEndDate(dateRange);
		
		// Prevents dates from being equal unless both are null
		if (startDate != null && startDate.equals(
				DateRange.getEndDate(dateRange))) {
			throw new IllegalArgumentException(
					"Start and end date cannot be equal");
		}
		
		// Find the active placement term
		PlacementTerm placementTerm 
				= this.placementTermDao.findForOffenderOnDate(offender, 
						startDate);
		
		// Throw exception when no placement is found
		if (placementTerm == null) {
			throw new OffenderNotUnderSupervisionException("Offender needs to "
					+ "be placed in order to change supervision.");
		}

		// Throw exception when placements exist before the end date when the
		// start date is null
		if (startDate == null) {
			Long existingTerms = this.placementTermDao
					.countForOffenderBeforeDate(offender, endDate, 
							placementTerm);
			if (existingTerms > 0) {
				throw new PlacementTermExistsBeforeException(existingTerms + 
					" existing placement term(s) exist prior to this term.");
			}
		}
		
		// Throw exception when placements exist after the start date when the
		// end date is null
		if (endDate == null) {
			Long existingTerms = this.placementTermDao
					.countForOffenderAfterDate(offender, startDate, 
							placementTerm); 
			if (existingTerms > 0) {
				throw new PlacementTermExistsAfterException(existingTerms + 
					" existing placement term(s) exist after to this term.");
			}
		}
		// Update supervisory organiztion term to end on the effective date of 
		// the dateRange parameter
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
				= placementTerm.getSupervisoryOrganizationTerm();
		supervisoryOrganizationTerm = this.updateSupervisoryOrganizationTerm(
				supervisoryOrganizationTerm, supervisoryOrganizationTerm
				.getSupervisoryOrganization(), new DateRange(
				DateRange.getStartDate(supervisoryOrganizationTerm
						.getDateRange()), startDate));
		this.supervisoryOrganizationTermDao.makePersistent(
				supervisoryOrganizationTerm);
		
		// Update existing active placement term to end on the effective date of 
		// the dateRange parameter
		placementTerm.setDateRange(new DateRange(
				DateRange.getStartDate(placementTerm.getDateRange()),
				startDate));
		placementTerm.setSupervisoryOrganizationTerm(
				supervisoryOrganizationTerm);
		this.placementTermDao.makePersistent(placementTerm);
		
		// Use existing correctional status term on new placement term
		CorrectionalStatusTerm correctionalStatusTerm 
			= placementTerm.getCorrectionalStatusTerm();
		
		// Creates new supervisory organization term for the new placement term
		SupervisoryOrganizationTerm newSupervisoryOrganizationTerm 
			= this.createSupervisoryOrganizationTerm(offender, 
					supervisoryOrganization, dateRange);
		this.supervisoryOrganizationTermDao.makePersistent(
				newSupervisoryOrganizationTerm);
		
		// Creates, persists and returns new placement term
		PlacementTerm newPlacementTerm
			= this.placementTermInstanceFactory.createInstance();
		newPlacementTerm.setCreationSignature(this.createCreationSignature());
		newPlacementTerm.setOffender(offender);
		newPlacementTerm.setUpdateSignature(this.createUpdateSignature());
		newPlacementTerm.setStatus(PlacementStatus.PLACED);
		newPlacementTerm.setStatusDateRange(null);
		newPlacementTerm.setDateRange(dateRange);
		newPlacementTerm
			.setSupervisoryOrganizationTerm(newSupervisoryOrganizationTerm);
		newPlacementTerm.setCorrectionalStatusTerm(correctionalStatusTerm);
		newPlacementTerm.setStartChangeReason(startChangeReason);
		newPlacementTerm.setEndChangeReason(endChangeReason);
		newPlacementTerm.setLocked(false);
		return this.placementTermDao.makePersistent(newPlacementTerm);
	}
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm update(final PlacementTerm placementTerm,
			final Date endDate,
			final PlacementStatus status,
			final DateRange statusDateRange,
			final PlacementTermChangeReason startChangeReason,
			final PlacementTermChangeReason endChangeReason)
					throws CorrectionalStatusTermConflictException,
						SupervisoryOrganizationTermConflictException,
						PlacementTermLockedException {
		
		// Prevents update of locked placement term
		if (placementTerm.getLocked()) {
			throw new PlacementTermLockedException("Placement term locked");
		}
		
		// Checks status and date requirements
		checkStatusAndStatusDateRequirements(status, statusDateRange);
		
		// Prevents dates from being equal unless both are null
		final Date startDate = DateRange.getStartDate(
				placementTerm.getDateRange());
		if (startDate != null && startDate.equals(endDate)) {
			throw new IllegalArgumentException(
					"Start and end date cannot be equal");
		}
		
		// Determines start and end date of existing placement term
		final Date oldStartDate
		= DateRange.getStartDate(placementTerm.getDateRange());
		final Date oldEndDate
			= DateRange.getEndDate(placementTerm.getDateRange());
		
		// Reference offender
		final Offender offender = placementTerm.getOffender();
		
		// Stores correctional status term on new start date and end date
		CorrectionalStatusTerm correctionalStatusTermOnStartDate
			= this.correctionalStatusTermDao.findForOffenderOnDate(
					offender, startDate);
		CorrectionalStatusTerm correctionalStatusTermOnEndDate
			= this.correctionalStatusTermDao.findForOffenderOnDate(
					offender, endDate);
		
		if (!(correctionalStatusTermOnStartDate != null
				&& correctionalStatusTermOnStartDate.equals(
						correctionalStatusTermOnEndDate))) {
			
			// Throws exception if conflicting correctional status terms exist
			// TODO - Remove this block once proven logically impossible - SA
			if (!placementTerm.getCorrectionalStatusTerm()
					.equals(correctionalStatusTermOnStartDate)) {
				
				// It is logically impossible for correctional status term
				// of placement term to not be correctional status term
				// on placement term start date when start date cannot be
				// changed.
				throw new AssertionError("Logically impossible condition");
			}
			
			// Adjusts start date of placement status term only if equal
			// to old start date of placement term
			// TODO - Remove this block once proven redundant - SA
			if (DateRange.startDateEquals(
					placementTerm.getCorrectionalStatusTerm()
						.getDateRange(), oldStartDate)) {
				placementTerm.getCorrectionalStatusTerm().setDateRange(
						DateRange.adjustStartDate(
								placementTerm.getCorrectionalStatusTerm()
									.getDateRange(), startDate));
			}
			
			// Throws exception if conflicting correctional status terms exist
			if (!placementTerm.getCorrectionalStatusTerm()
					.equals(correctionalStatusTermOnEndDate)) {
				
				// Figures out date bounds
				Date searchStartDate;
				if (endDate != null && oldEndDate == null) {
					searchStartDate = endDate;
				} else if (oldEndDate != null && endDate == null) {
					searchStartDate = oldEndDate;
				} else {
					searchStartDate
						= DateManipulator.findEarliest(endDate, oldEndDate);
				}
				Date searchEndDate
					= DateManipulator.findLatest(endDate, oldEndDate);
				
				// Performs search for conflicting correctional status term -
				// throws exception if conflicts are found
				if (searchStartDate != null || searchEndDate != null) {
					
					// TODO Should correctionalStatusTermOnEndDate be added to
					// excludedCorrectionalStatusTerms? [SA]
					List<CorrectionalStatusTerm> excludedCorrectionalStatusTerms
						= new ArrayList<CorrectionalStatusTerm>();
					if (correctionalStatusTermOnEndDate != null
						&& DateRange.getEndDate(correctionalStatusTermOnEndDate
								.getDateRange()) != null
						&& startDate != null && !correctionalStatusTermOnEndDate
							.getDateRange().getEndDate().equals(endDate)) {
						
						// Exclude correctional status term with start date of
						// end  date from search for conflicting correctional
						// status  terms
						CorrectionalStatusTerm
						correctionalStatusTermWithStartDate
							= this.correctionalStatusTermDao
								.findForOffenderWithStartDate(offender,
										correctionalStatusTermOnEndDate
											.getDateRange().getEndDate());
						if (correctionalStatusTermWithStartDate != null) {
							excludedCorrectionalStatusTerms.add(
									correctionalStatusTermWithStartDate);
						}
					}
					long count;
					if (excludedCorrectionalStatusTerms.size() > 0) {
						count = this.correctionalStatusTermDao
							.countForOffenderBetweenDatesExcluding(offender,
								searchStartDate, searchEndDate,
								excludedCorrectionalStatusTerms.toArray(
										new CorrectionalStatusTerm[] { }));
					} else {
						count = this.correctionalStatusTermDao
								.countForOffenderBetweenDates(offender,
									searchStartDate, searchEndDate);
					}
					if (count > 0) {
						throw new CorrectionalStatusTermConflictException(
								"Conflicting correctional status terms exist");
					}
				}
				
				// Removes old correctional status term and uses one on end date
				// TODO - Figure out why equals is not used - SA
				if (correctionalStatusTermOnEndDate != null
						&& correctionalStatusTermOnEndDate
							.getCorrectionalStatus().equals(placementTerm
									.getCorrectionalStatusTerm()
									.getCorrectionalStatus())
						&& !placementTerm.getCorrectionalStatusTerm()
							.getCorrectionalStatus().equals(
									placementTerm.getCorrectionalStatusTerm()
									.getCorrectionalStatus())) {
					CorrectionalStatusTerm oldCorrectionalStatusTerm
						= placementTerm.getCorrectionalStatusTerm();
					placementTerm.setCorrectionalStatusTerm(
							correctionalStatusTermOnEndDate);
					this.correctionalStatusTermDao.makeTransient(
							oldCorrectionalStatusTerm);
				}
			}
			
			// Adjusts end date of correctional status term only if equal
			/// to old end date of placement term
			if (DateRange.endDateEquals(
					placementTerm.getCorrectionalStatusTerm()
						.getDateRange(), oldEndDate)) {
				placementTerm.getCorrectionalStatusTerm().setDateRange(
						DateRange.adjustEndDate(
								placementTerm.getCorrectionalStatusTerm()
									.getDateRange(), endDate));
			}
		} else {
			if (!placementTerm.getCorrectionalStatusTerm()
					.equals(correctionalStatusTermOnStartDate)) {
				throw new AssertionError(
						"Overlapping correctional status terms"); 
			}
			if (!placementTerm.getCorrectionalStatusTerm()
					.equals(correctionalStatusTermOnEndDate)) {
				throw new AssertionError(
						"Overlapping correctional status terms");
			}
			placementTerm.getCorrectionalStatusTerm().setDateRange(
					new DateRange(startDate, endDate));
		}
		placementTerm.setCorrectionalStatusTerm(
				this.correctionalStatusTermDao.makePersistent(
						placementTerm.getCorrectionalStatusTerm()));
		
		// Stores supervisory organization term on new start date and end date
		SupervisoryOrganizationTerm supervisoryOrganizationTermOnStartDate
			= this.supervisoryOrganizationTermDao.findForOffenderOnDate(
					offender, startDate);
		SupervisoryOrganizationTerm supervisoryOrganizationTermOnEndDate
			= this.supervisoryOrganizationTermDao.findForOffenderOnDate(
					offender, endDate);
		
		if (!(supervisoryOrganizationTermOnStartDate != null
				&& supervisoryOrganizationTermOnStartDate.equals(
						supervisoryOrganizationTermOnEndDate))) {
			
			// Throws exception if conflicting supervisory organization terms
			// exist
			// TODO - Remove this block once proven logically impossible - SA
			if (!placementTerm.getSupervisoryOrganizationTerm()
					.equals(supervisoryOrganizationTermOnStartDate)) {
				
				// It is logically impossible for supervisory organization term
				// of placement term to not be supervisory organization term
				// on placement term start date when start date cannot be
				// changed
				throw new AssertionError("Logically impossible condition");
			}
			
			// Adjusts start date of supervisory organization term only if
			// equal to old start date of placement term
			// TODO - Remove this block once proven redundant - SA
			if (DateRange.startDateEquals(
					placementTerm.getSupervisoryOrganizationTerm()
						.getDateRange(), oldStartDate)) {
				placementTerm.getSupervisoryOrganizationTerm()
					.setDateRange(DateRange.adjustStartDate(placementTerm
							.getSupervisoryOrganizationTerm()
								.getDateRange(), startDate));
			}
			
			// Throws exception if conflicting supervisory organization terms
			// exist
			if (!placementTerm.getSupervisoryOrganizationTerm()
					.equals(supervisoryOrganizationTermOnEndDate)) {
				
				// Figures out date bounds
				Date searchStartDate;
				if (endDate != null && oldEndDate == null) {
					searchStartDate = endDate;
				} else if (oldEndDate != null && endDate == null) {
					searchStartDate = oldEndDate;
				} else {
					searchStartDate
						= DateManipulator.findEarliest(endDate, oldEndDate);
				}
				Date searchEndDate
					= DateManipulator.findLatest(endDate, oldEndDate);
				
				// Performs search for conflicting supervisory organization
				// terms - throws exception if conflicts are found
				if (searchStartDate != null || searchEndDate != null) {

					// TODO Should supervisoryOrganizationTermOnEndDate be added
					// to excludedSupervisoryOrganizationTerms? [SA]
					List<SupervisoryOrganizationTerm>
						excludedSupervisoryOrganizationTerms
							= new ArrayList<SupervisoryOrganizationTerm>();
					if (supervisoryOrganizationTermOnEndDate != null
						&& DateRange.getEndDate(
								supervisoryOrganizationTermOnEndDate
									.getDateRange()) != null
						&& startDate != null
							&& supervisoryOrganizationTermOnEndDate
								.getDateRange().getEndDate().equals(endDate)) {
						
						// Exclude supervisory organization term with start date
						// of end date from search for conflicting supervisory
						// organization  terms
						SupervisoryOrganizationTerm
						supervisoryOrganizationTermWithStartDate
							= this.supervisoryOrganizationTermDao
								.findForOffenderWithStartDate(offender,
										supervisoryOrganizationTermOnEndDate
											.getDateRange().getEndDate());
						if (supervisoryOrganizationTermWithStartDate != null) {
							excludedSupervisoryOrganizationTerms.add(
									supervisoryOrganizationTermWithStartDate);
						}
					}
					long count;
					if (excludedSupervisoryOrganizationTerms.size() > 0) {
						count = this.supervisoryOrganizationTermDao
							.countForOffenderBetweenDatesExcluding(offender,
								searchStartDate, searchEndDate,
								excludedSupervisoryOrganizationTerms
									.toArray(
									new SupervisoryOrganizationTerm[] { }));
					} else {
						count = this.supervisoryOrganizationTermDao
								.countForOffenderBetweenDates(offender,
									searchStartDate, searchEndDate);
					}
					if (count > 0) {
						throw new SupervisoryOrganizationTermConflictException(
							"Conflicting supervisory organization terms exist");
					}
				}
				
				// Removes old supervisory organization term and uses one on end
				// date
				// TODO - Figure out why equals is not used - SA
				if (supervisoryOrganizationTermOnEndDate != null
						&& supervisoryOrganizationTermOnEndDate
							.getSupervisoryOrganization().equals(
									placementTerm
										.getSupervisoryOrganizationTerm()
										.getSupervisoryOrganization())
						&& !placementTerm.getSupervisoryOrganizationTerm()
							.getSupervisoryOrganization().equals(
									placementTerm
										.getSupervisoryOrganizationTerm()
										.getSupervisoryOrganization())) {
					SupervisoryOrganizationTerm oldSupervisoryOrganizationTerm
						= placementTerm.getSupervisoryOrganizationTerm();
					placementTerm.setSupervisoryOrganizationTerm(
							supervisoryOrganizationTermOnEndDate);
					this.supervisoryOrganizationTermDao.makeTransient(
							oldSupervisoryOrganizationTerm);
				}
			}
			
			// Adjusts end date of supervisory organization term only if
			// equal to old end date of placement term
			if (DateRange.endDateEquals(placementTerm
					.getSupervisoryOrganizationTerm()
						.getDateRange(), oldEndDate)) {
				placementTerm.getSupervisoryOrganizationTerm()
					.setDateRange(DateRange.adjustEndDate(placementTerm
							.getSupervisoryOrganizationTerm()
								.getDateRange(), endDate));
			}
		} else {
			if (!placementTerm.getSupervisoryOrganizationTerm()
					.equals(supervisoryOrganizationTermOnStartDate)) {
				throw new AssertionError(
						"Overlapping supervisory organization terms"); 
			}
			if (!placementTerm.getSupervisoryOrganizationTerm()
					.equals(supervisoryOrganizationTermOnEndDate)) {
				throw new AssertionError(
						"Overlapping supervisory organization terms");
			}
			placementTerm.getSupervisoryOrganizationTerm().setDateRange(
					new DateRange(startDate, endDate));
		}
		placementTerm.setSupervisoryOrganizationTerm(
				this.supervisoryOrganizationTermDao.makePersistent(
						placementTerm.getSupervisoryOrganizationTerm()));
		
		// Ends location term on status date if status is escaped
		if (PlacementStatus.ESCAPED.equals(status)
					&& DateRange.getStartDate(statusDateRange) != null) {
			LocationTerm locationTerm
				= this.locationTermDao
					.findByOffenderOnDate(offender,
							DateRange.getStartDate(statusDateRange));
			if (locationTerm != null) {
				locationTerm.setDateRange(
						DateRange.adjustEndDate(locationTerm.getDateRange(),
								DateRange.getStartDate(statusDateRange)));
				this.locationTermDao.makePersistent(locationTerm);
			}
		}
		
		// Updates existing placement term
		placementTerm.setUpdateSignature(this.createUpdateSignature());
		placementTerm.setStatus(status);
		placementTerm.setStatusDateRange(statusDateRange);
		placementTerm.setDateRange(new DateRange(startDate, endDate));
		placementTerm.setStartChangeReason(startChangeReason);
		placementTerm.setEndChangeReason(endChangeReason);
		return this.placementTermDao.makePersistent(placementTerm);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final PlacementTerm placementTerm) {
		DateRange oldDateRange
			= DateRange.deepCopy(placementTerm.getDateRange());
		CorrectionalStatusTerm correctionalStatusTerm
			= placementTerm.getCorrectionalStatusTerm();
		SupervisoryOrganizationTerm supervisoryOrganizationTerm
			= placementTerm.getSupervisoryOrganizationTerm();
		this.placementTermDao.makeTransient(placementTerm);
		if (DateRange.areEqual(oldDateRange,
				correctionalStatusTerm.getDateRange())) {
			this.correctionalStatusTermDao
				.makeTransient(correctionalStatusTerm);
		} else {
			if (!DateRange.startDatesAreEqual(
					correctionalStatusTerm.getDateRange(), oldDateRange)) {
				correctionalStatusTerm.setDateRange(DateRange.adjustEndDate(
						correctionalStatusTerm.getDateRange(),
						DateRange.getStartDate(oldDateRange)));
				this.correctionalStatusTermDao
					.makePersistent(correctionalStatusTerm);
			} else if (!DateRange.endDatesAreEqual(
					correctionalStatusTerm.getDateRange(), oldDateRange)) {
				correctionalStatusTerm.setDateRange(DateRange.adjustStartDate(
						correctionalStatusTerm.getDateRange(),
						DateRange.getEndDate(oldDateRange)));
				this.correctionalStatusTermDao
					.makePersistent(correctionalStatusTerm);
			} else {
				
				// Date ranges must be equal
				throw new AssertionError("Date ranges are equal");
			}
		}
		if (DateRange.areEqual(oldDateRange,
				supervisoryOrganizationTerm.getDateRange())) {
			this.supervisoryOrganizationTermDao
				.makeTransient(supervisoryOrganizationTerm);
		} else {
			if (!DateRange.startDatesAreEqual(
					supervisoryOrganizationTerm.getDateRange(), oldDateRange)) {
				supervisoryOrganizationTerm.setDateRange(DateRange
						.adjustEndDate(supervisoryOrganizationTerm
								.getDateRange(),
								DateRange.getStartDate(oldDateRange)));
				this.supervisoryOrganizationTermDao
					.makePersistent(supervisoryOrganizationTerm);
			} else if (!DateRange.endDatesAreEqual(
					supervisoryOrganizationTerm.getDateRange(), oldDateRange)) {
				supervisoryOrganizationTerm.setDateRange(DateRange
						.adjustStartDate(supervisoryOrganizationTerm
								.getDateRange(),
								DateRange.getEndDate(oldDateRange)));
				this.supervisoryOrganizationTermDao
					.makePersistent(supervisoryOrganizationTerm);
			} else {
				
				// Date ranges must be equal
				throw new AssertionError("Date ranges are equal");
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTermNote createNote(
			final PlacementTerm placementTerm,
			final Date date,
			final String value)
					throws PlacementTermNoteExistsException {
		return this.placementTermNoteDelegate
				.create(placementTerm, date, value);
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTermNote updateNote(
			final PlacementTermNote note,
			final Date date,
			final String value)
					throws PlacementTermNoteExistsException {
		return this.placementTermNoteDelegate.update(note, date, value);
	}

	/** {@inheritDoc} */
	@Override
	public void removeNote(final PlacementTermNote note) {
		this.placementTermNoteDelegate.remove(note);
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTermNote> findNotesByPlacementTerm(
			final PlacementTerm placementTerm) {
		return this.placementTermNoteDelegate
				.findByPlacementTerm(placementTerm);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<CorrectionalStatus> findCorrectionalStatuses() {
		return this.correctionalStatusDao.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> findSupervisoryOrganizationsByState(
			final State state) {
		return this.supervisoryOrganizationDao.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDao.findHomeState();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStatesForSupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		List<Location> locations = this.locationDao
				.findByOrganization(supervisoryOrganization);
		List<State> states = new ArrayList<State>();
		for (Location location : locations) {
			states.add(location.getAddress().getZipCode().getCity().getState());
		}
		return states;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findHomeStates() {
		return this.stateDao.findInHomeCountry();
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTermChangeReason> findChangeReasons() {
		return this.placementTermChangeReasonDao.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<CorrectionalStatusTerm> findCorrectionalStatusTermsByOffender(
			final Offender offender) {
		return this.correctionalStatusTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public CorrectionalStatusTerm findCorrectionalStatusTermByOffenderOnDate(
			final Offender offender, final Date date) {
		return this.correctionalStatusTermDao.findForOffenderOnDate(offender,
				date);
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganizationTerm>
	findSupervisoryOrganizationTermsByOffender(final Offender offender) {
		return this.supervisoryOrganizationTermDao.findByOffender(offender);
	}

	/** {@inheritDoc} */
	@Override
	public SupervisoryOrganizationTerm
	findSupervisoryOrganizationTermByOffenderOnDate(
			final Offender offender, final Date date) {
		return this.supervisoryOrganizationTermDao
				.findForOffenderOnDate(offender, date);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganization>
	findSupervisoryOrganizationsForCorrectionalStatus(
			final CorrectionalStatus correctionalStatus) {
		return this.supervisoryOrganizationDao
				.findForCorrectionalStatus(correctionalStatus);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganization>
	findSupervisoryOrganizationsForCorrectionalStatusByState(
			final CorrectionalStatus correctionalStatus, final State state) {
		return this.supervisoryOrganizationDao
				.findByCorrectionalStatusForState(correctionalStatus, state);
	}
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm findPlacementTerm(Offender offender, 
			Date effectiveDate) {
		return this.placementTermDao.findForOffenderOnDate(
				offender, effectiveDate);
	}

	/** {@inheritDoc} */
	@Override
	public List<CorrectionalStatus> findAllowedCorrectionalStatuses(
			CorrectionalStatus fromCorrectionalStatus) {
		return this.correctionalStatusDao
				.findAllowedCorrectionalStatuses(fromCorrectionalStatus);
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTermChangeReason> findAllowedStartChangeReasons(
			CorrectionalStatus fromCorrectionalStatus,
			CorrectionalStatus toCorrectionalStatus) {
		return this.placementTermChangeReasonDao.findAllowedStartChangeReasons(
				fromCorrectionalStatus, toCorrectionalStatus);
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTermChangeReason> findAllowedEndChangeReasons(
			CorrectionalStatus fromCorrectionalStatus) {
		return this.placementTermChangeReasonDao.findAllowedEndChangeReasons(
				fromCorrectionalStatus);
	}
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm findPlacementTermForOffenderWithEndDate(
			final Offender offender, final Date endDate) {
		return this.placementTermDao.findForOffenderWithEndDate(
				offender, endDate);
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm findLocationTermByOffenderOnDate(
			final Offender offender, final Date effectiveDate) {
		return this.locationTermDao.findByOffenderOnDate(
				offender, effectiveDate);
	}
	
	/* Helper methods. */
	
	// Creates a new correctional status term
	private CorrectionalStatusTerm createCorrectionalStatusTerm(
			final Offender offender,
			final CorrectionalStatus correctionalStatus,
			final DateRange dateRange) {
		CorrectionalStatusTerm correctionalStatusTerm
			= this.correctionalStatusTermInstanceFactory.createInstance();
		correctionalStatusTerm.setCreationSignature(
				this.createCreationSignature());
		correctionalStatusTerm.setOffender(offender);
		return this.updateCorrectionalStatusTerm(correctionalStatusTerm,
				correctionalStatus, dateRange);
	}
	
	// Updates a correctional status term
	private CorrectionalStatusTerm updateCorrectionalStatusTerm(
			final CorrectionalStatusTerm correctionalStatusTerm,
			final CorrectionalStatus correctionalStatus,
			final DateRange dateRange) {
		correctionalStatusTerm.setUpdateSignature(
				this.createUpdateSignature());
		correctionalStatusTerm.setCorrectionalStatus(correctionalStatus);
		correctionalStatusTerm.setDateRange(dateRange);
		return correctionalStatusTerm;
	}
	
	// Creates a new supervisory organization term
	private SupervisoryOrganizationTerm createSupervisoryOrganizationTerm(
			final Offender offender,
			final SupervisoryOrganization supervisoryOrganization,
			final DateRange dateRange) {
		SupervisoryOrganizationTerm supervisoryOrganizationTerm
			= this.supervisoryOrganizationTermInstanceFactory.createInstance();
		supervisoryOrganizationTerm.setCreationSignature(
				this.createCreationSignature());
		supervisoryOrganizationTerm.setOffender(offender);
		return this.updateSupervisoryOrganizationTerm(
				supervisoryOrganizationTerm, supervisoryOrganization,
				dateRange);
	}

	// Updates supervisory organization term
	private SupervisoryOrganizationTerm updateSupervisoryOrganizationTerm(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final SupervisoryOrganization supervisoryOrganization,
			final DateRange dateRange) {
		supervisoryOrganizationTerm.setUpdateSignature(
				this.createUpdateSignature());
		supervisoryOrganizationTerm.setSupervisoryOrganization(
				supervisoryOrganization);
		supervisoryOrganizationTerm.setDateRange(dateRange);
		return supervisoryOrganizationTerm;
	}
	
	// Requires status date for escape, abscond or returned statuses
	private void checkStatusAndStatusDateRequirements(
			final PlacementStatus status, final DateRange statusDateRange) {
		if (PlacementStatus.ESCAPED.equals(status)
				|| PlacementStatus.ABSCONDED.equals(status)
				|| PlacementStatus.RETURNED_FROM_ESCAPE.equals(status)
				|| PlacementStatus.RETURNED_FROM_ESCAPE.equals(status)) {
			if (statusDateRange == null) {
				throw new IllegalArgumentException(
						String.format("Status date required for %s status",
								status));
			}
		}
	}
	
	// Creates and returns creation signature
	private CreationSignature createCreationSignature() {
		return new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate());
	}
	
	// Creates and returns update signature
	private UpdateSignature createUpdateSignature() {
		return new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate());
	}
}