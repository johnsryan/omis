package omis.placement.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationNotAllowedException;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.placement.service.ChangeCorrectionalStatusService;
import omis.program.domain.Program;
import omis.program.domain.ProgramPlacement;
import omis.program.service.delegate.ProgramDelegate;
import omis.program.service.delegate.ProgramPlacementDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.EndedPlacementTermException;
import omis.supervision.exception.IllegalCorrectionalStatusChangeException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.OffenderUnderSupervisionException;
import omis.supervision.exception.PlacementTermChangeReasonNotAllowedException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.service.delegate.AllowedCorrectionalStatusChangeDelegate;
import omis.supervision.service.delegate.AllowedCorrectionalStatusChangeReasonRuleDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;

/**
 * Implementation of service to change correctional statuses of an offender.
 * 
 * @author Stephen Abson
 * @version 0.1.1 (Dec 19, 2014)
 * @since OMIS 3.0
 */
public class ChangeCorrectionalStatusServiceImpl
		implements ChangeCorrectionalStatusService {
	
	/* Resources */
	
	private final PlacementTermDelegate placementTermDelegate;
	
	private final CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	private final SupervisoryOrganizationTermDelegate
	supervisoryOrganizationTermDelegate;
	
	private final SupervisoryOrganizationDelegate
	supervisoryOrganizationDelegate;
	
	private final AllowedCorrectionalStatusChangeDelegate
	allowedCorrectionalStatusChangeDelegate;
	
	private final PlacementTermChangeReasonDelegate
	placementTermChangeReasonDelegate;
	
	private final AllowedCorrectionalStatusChangeReasonRuleDelegate
	allowedCorrectionalStatusChangeReasonRuleDelegate;
	
	private final LocationTermDelegate locationTermDelegate;
	
	private final LocationReasonTermDelegate locationReasonTermDelegate;

	private final ProgramPlacementDelegate programPlacementDelegate;
	
	private final ProgramDelegate programDelegate;
	
	private final LocationDelegate locationDelegate;

	/* Constructors */
	
	/**
	 * Instantiates an implementation of service to change correctional statuses
	 * of an offender with the specified resources.
	 * 
	 * @param placementTermDelegate delegate for placement terms
	 * @param correctionalStatusTermDelegate delegate for correctional status
	 * terms
	 * @param supervisoryOrganizationTermDelegate delegate for supervisory
	 * organization terms
	 * @param supervisoryOrganizationDelegate delegate for supervisory
	 * organizations
	 * @param allowedCorrectionalStatusChangeDelegate delegate for allowed
	 * correctional status changes
	 * @param placementTermChangeReasonDelegate delegate for placement
	 * term change reasons
	 * @param allowedCorrectionalStatusChangeReasonRuleDelegate delegate for
	 * rule to allow correctional status change reason
	 * @param locationTermDelegate delegate for location terms
	 * @param locationReasonTermDelegate delegate for location reason terms
	 * @param programPlacementDelegate delegate for program placements
	 * @param programDelegate delegate for programs
	 * @param locationDelegate delegate for locations
	 */
	public ChangeCorrectionalStatusServiceImpl(
			final PlacementTermDelegate placementTermDelegate,
			final CorrectionalStatusTermDelegate correctionalStatusTermDelegate,
			final SupervisoryOrganizationTermDelegate
				supervisoryOrganizationTermDelegate,
			final SupervisoryOrganizationDelegate
				supervisoryOrganizationDelegate,
			final AllowedCorrectionalStatusChangeDelegate
				allowedCorrectionalStatusChangeDelegate,
			final PlacementTermChangeReasonDelegate
				placementTermChangeReasonDelegate,
			final AllowedCorrectionalStatusChangeReasonRuleDelegate
				allowedCorrectionalStatusChangeReasonRuleDelegate,
			final LocationTermDelegate locationTermDelegate,
			final LocationReasonTermDelegate locationReasonTermDelegate,
			final ProgramPlacementDelegate programPlacementDelegate,
			final ProgramDelegate programDelegate,
			final LocationDelegate locationDelegate) {
		this.placementTermDelegate = placementTermDelegate;
		this.correctionalStatusTermDelegate = correctionalStatusTermDelegate;
		this.supervisoryOrganizationTermDelegate
			= supervisoryOrganizationTermDelegate;
		this.supervisoryOrganizationDelegate = supervisoryOrganizationDelegate;
		this.allowedCorrectionalStatusChangeDelegate
			= allowedCorrectionalStatusChangeDelegate;
		this.placementTermChangeReasonDelegate
			= placementTermChangeReasonDelegate;
		this.allowedCorrectionalStatusChangeReasonRuleDelegate 
				= allowedCorrectionalStatusChangeReasonRuleDelegate;
		this.locationTermDelegate = locationTermDelegate;
		this.locationReasonTermDelegate = locationReasonTermDelegate;
		this.programPlacementDelegate = programPlacementDelegate;
		this.programDelegate = programDelegate;
		this.locationDelegate = locationDelegate;
	}
	
	/* Method implementations */
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm change(final Offender offender,
			final CorrectionalStatus correctionalStatus,
			final SupervisoryOrganization supervisoryOrganization,
			final PlacementTermChangeReason changeReason,
			final Date effectiveDate, final Date endDate)
				throws IllegalCorrectionalStatusChangeException,
					OffenderNotUnderSupervisionException,
					PlacementTermConflictException,
					PlacementTermChangeReasonNotAllowedException,
					EndedPlacementTermException {
		
		// Determines placement term on effective date
		// Throws exception if null
		PlacementTerm existingPlacementTerm = this.placementTermDelegate
					.findForOffenderOnDate(offender, effectiveDate);
		if (existingPlacementTerm == null) {
			throw new OffenderNotUnderSupervisionException(
				"Correctional status term for offender does not exist on date");
		}
		
		// Throws exception if placement term on effective date is ended
		if (DateRange.getEndDate(existingPlacementTerm.getDateRange())
				!= null) {
			throw new EndedPlacementTermException(
					"Placement term on effective date is ended");
		}
		
		// Throw exception if other placement terms exist between effective
		// date and end date for offender
		long otherPlacementTermCount = this.placementTermDelegate
				.countForOffenderBetweenDatesExcluding(
					offender, effectiveDate, endDate, existingPlacementTerm);
		if (otherPlacementTermCount > 0) {
			throw new PlacementTermConflictException(otherPlacementTermCount
					+ " conflicting placement term(s)");
		}
		
		// Throws exception if correctional status change is not allowed
		// (including to the same status)
		CorrectionalStatusTerm existingCorrectionalStatusTerm
			= existingPlacementTerm.getCorrectionalStatusTerm();
		CorrectionalStatus correctionalStatusOnDate
			= existingCorrectionalStatusTerm.getCorrectionalStatus();
		if (correctionalStatus == null
				|| correctionalStatus.equals(correctionalStatusOnDate)
				|| this.allowedCorrectionalStatusChangeDelegate.find(
						correctionalStatusOnDate, correctionalStatus) == null) {
			String correctionalStatusName;
			if (correctionalStatus != null) {
				correctionalStatusName = correctionalStatus.getName();
			} else {
				correctionalStatusName = null;
			}
			throw new IllegalCorrectionalStatusChangeException(
					String.format("Cannot change status from %s to %s",
							correctionalStatusOnDate.getName(),
							correctionalStatusName));
		}
		
		// Throws exception if change reason is not allowed
		if (this.allowedCorrectionalStatusChangeReasonRuleDelegate.find(
				correctionalStatusOnDate, correctionalStatus, changeReason)
					== null) {
			throw new PlacementTermChangeReasonNotAllowedException(
					String.format("%s not allowed for %s change to %s",
						changeReason.getName(),
						correctionalStatusOnDate.getName(),
						correctionalStatus.getName()));
		}
		
		// Ends existing placement term
		try {
			existingPlacementTerm = this.placementTermDelegate
					.update(existingPlacementTerm, PlacementStatus.PLACED,
							null, DateRange.adjustEndDate(
							existingPlacementTerm.getDateRange(),
								effectiveDate),
							existingPlacementTerm
								.getSupervisoryOrganizationTerm(),
							existingPlacementTerm.getCorrectionalStatusTerm(),
							existingPlacementTerm.getStartChangeReason(),
							changeReason,
							existingPlacementTerm.getLocked());
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError("Placement term cannot exist", e);
		}
		
		// Ends existing correctional status term
		try {
			this.correctionalStatusTermDelegate.update(
					existingCorrectionalStatusTerm,
					DateRange.adjustEndDate(
							existingCorrectionalStatusTerm.getDateRange(),
							effectiveDate),
					existingCorrectionalStatusTerm.getCorrectionalStatus());
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Correctional status term cannot exist", e);
		}
		
		// Creates and persists new correctional status term starting on
		// effective date with new correctional status
		CorrectionalStatusTerm changedCorrectionalStatusTerm;
		try {
			changedCorrectionalStatusTerm = this.correctionalStatusTermDelegate
					.create(offender, new DateRange(effectiveDate, endDate),
							correctionalStatus);
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Correctional status term cannot exist", e);
		}
		
		// If supervisory organization on effective date is same as new
		// supervisory organization, uses term on effective date, otherwise
		// creates and persists new supervisory organization term
		SupervisoryOrganizationTerm supervisoryOrganizationTerm;
		SupervisoryOrganizationTerm existingSupervisoryOrganizationTerm
				= existingPlacementTerm.getSupervisoryOrganizationTerm();
		Date newExistingEndDate;
		if (existingSupervisoryOrganizationTerm.getSupervisoryOrganization()
						.equals(supervisoryOrganization)) {
			newExistingEndDate = endDate;
			supervisoryOrganizationTerm = existingSupervisoryOrganizationTerm;
		} else {
			newExistingEndDate = effectiveDate;
			try {
				supervisoryOrganizationTerm
					= this.supervisoryOrganizationTermDelegate.create(
						offender, new DateRange(effectiveDate, endDate),
						supervisoryOrganization);
			} catch (DuplicateEntityFoundException e) {
				throw new AssertionError(
						"Supervisory organization term cannot exist", e);
			}
		}
		try {
			existingSupervisoryOrganizationTerm
				= this.supervisoryOrganizationTermDelegate.update(
							existingSupervisoryOrganizationTerm,
							DateRange.adjustEndDate(
									existingSupervisoryOrganizationTerm
										.getDateRange(),
									newExistingEndDate),
							existingSupervisoryOrganizationTerm.
								getSupervisoryOrganization());
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Supervisory organization term cannot exist", e);
		}

		// Creates, persists and returns new supervisory organization term
		// starting on effective date with new correctional status
		try {
			return this.placementTermDelegate.create(offender,
					new DateRange(effectiveDate, endDate),
					supervisoryOrganizationTerm,
					changedCorrectionalStatusTerm,
					changeReason,
					null, false);
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError("Placement term cannot exist", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm beginSupervision(final Offender offender,
			final CorrectionalStatus correctionalStatus,
			final SupervisoryOrganization supervisoryOrganization,
			final PlacementTermChangeReason changeReason,
			final Date effectiveDate, final Date endDate)
				throws IllegalCorrectionalStatusChangeException,
					OffenderUnderSupervisionException,
					PlacementTermChangeReasonNotAllowedException,
					PlacementTermConflictException {
		
		// Checks of offender is under supervision on date - throws exception
		// if true
		if (this.placementTermDelegate.findForOffenderOnDate(
				offender, effectiveDate) != null) {
			throw new OffenderUnderSupervisionException(
					"Placement term exists for offender on date");
		}
		
		// Throws exception if conflicting placement terms exist
		long conflictingCount = this.placementTermDelegate
				.countForOffenderBetweenDates(offender, effectiveDate, endDate);
		if (conflictingCount > 0) {
			throw new PlacementTermConflictException(
					conflictingCount + " conflicting placement term(s)");
		}
		
		// Throws exception if correctional status change is not allowed
		if (this.allowedCorrectionalStatusChangeDelegate.find(
				null, correctionalStatus) == null) {
			throw new IllegalCorrectionalStatusChangeException(
					"Cannot begin supervision with "
							+ correctionalStatus.getName() + " status");
		}
		
		// Throws exception if change reason is not allowed
		if (this.allowedCorrectionalStatusChangeReasonRuleDelegate.find(
				null, correctionalStatus, changeReason) == null) {
			throw new PlacementTermChangeReasonNotAllowedException(
					String.format("%s not allowed for %s",
						changeReason.getName(), correctionalStatus.getName()));
		}
		
		// Creates returns and persists starting supervision, supervisory
		// organization and correctional status terms
		CorrectionalStatusTerm correctionalStatusTerm;
		try {
			correctionalStatusTerm = this.correctionalStatusTermDelegate
					.create(offender, new DateRange(effectiveDate, endDate),
							correctionalStatus);
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Correctional status term cannot exist", e);
		}
		SupervisoryOrganizationTerm supervisoryOrganizationTerm;
		try {
			supervisoryOrganizationTerm = this
					.supervisoryOrganizationTermDelegate.create(
							offender, new DateRange(effectiveDate, endDate),
							supervisoryOrganization);
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Supervisory organization term cannot exist", e);
		}
		try {
			return this.placementTermDelegate.create(
					offender, new DateRange(effectiveDate, endDate),
					supervisoryOrganizationTerm, correctionalStatusTerm,
					changeReason, null, false);
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError("Placement term cannot exist", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm placeAtLocation(final Offender offender,
			final Location location,
			final Date effectiveDate, final Date endDate,
			final LocationReason reason)
					throws OffenderNotUnderSupervisionException,
						LocationNotAllowedException,
						LocationTermConflictException,
						DuplicateEntityFoundException {
		
		// Determines placement term on effective date
		// Throws exception if null
		PlacementTerm placementTerm = this.placementTermDelegate
					.findForOffenderOnDate(offender, effectiveDate);
		if (placementTerm == null) {
			throw new OffenderNotUnderSupervisionException(
					"Placement term for offender does not exist on date");
		}
		
		// Throws exception if facility is not allowed for correctional status
		if (!placementTerm.getCorrectionalStatusTerm()
				.getCorrectionalStatus().getLocationRequired()) {
			throw new LocationNotAllowedException(
					"Location not allowed for correctional status "
						+ placementTerm.getCorrectionalStatusTerm()
						.getCorrectionalStatus().getName());
		}
		
		// Looks up existing location term on effective date
		LocationTerm existingLocationTerm = this.locationTermDelegate
				.findByOffenderOnDate(offender, effectiveDate);
		LocationTerm locationTerm;
		if (existingLocationTerm != null) {
			
			// Checks for other locations terms between effective date and end
			// date
			// Throws exception if such location terms exist
			long countConflicting = this.locationTermDelegate
					.countExcluding(offender, effectiveDate,
							endDate, existingLocationTerm);
			if (countConflicting > 0) {
				throw new LocationTermConflictException(
						countConflicting + " conflicting location term(s)");
			}
			
			// Extends time of existing location term to fit current location
			// term if locations are the same, otherwise ends location term
			// on effective date and creates new one
			if (location.equals(existingLocationTerm.getLocation())) {
				Date existingEndDate = DateRange.getEndDate(
						existingLocationTerm.getDateRange());
				if (existingEndDate == null
						|| existingEndDate.compareTo(effectiveDate) < 0) {
					try {
						existingLocationTerm = this.locationTermDelegate
							.update(existingLocationTerm,
									existingLocationTerm.getLocation(),
									DateRange.getStartDate(
										existingLocationTerm.getDateRange()),
									endDate,
									existingLocationTerm.getLocked());
					} catch (DuplicateEntityFoundException e) {
						throw new AssertionError(
								"Location term cannot exist", e);
					}
				}
				locationTerm = existingLocationTerm;
			} else {
				try {
					existingLocationTerm = this.locationTermDelegate
						.update(existingLocationTerm,
								existingLocationTerm.getLocation(),
								DateRange.getStartDate(
										existingLocationTerm.getDateRange()),
								effectiveDate,
								existingLocationTerm.getLocked());
				} catch (DuplicateEntityFoundException e) {
					throw new AssertionError("Location term cannot exist", e);
				}
				try {
					locationTerm = this.locationTermDelegate
						.create(offender, location,
								effectiveDate, endDate, false);
				} catch (DuplicateEntityFoundException e) {
					throw new AssertionError("Location term cannot exist", e);
				}
			}
		} else {
			
			// Checks for locations terms between effective date and end date
			// throws exception if such location terms exist
			long countConflicting = this.locationTermDelegate
					.count(offender, effectiveDate, endDate);
			if (countConflicting > 0) {
				throw new LocationTermConflictException(
						countConflicting + " conflicting location term(s)");
			}
			
			// Creates new location term for location of facility
			locationTerm = this.locationTermDelegate
				.create(offender, location, effectiveDate, endDate, false);
		}
		
		if (reason != null) {
			// Catches and re-throws duplicate reason term found exception as a
			// bug as it is highly unlikely that this would ever happen during
			// normal request/thread/transaction flow
			try {
				this.locationReasonTermDelegate.create(
					locationTerm, new DateRange(effectiveDate, endDate),
					reason);
			} catch (LocationReasonTermExistsException e) {
				throw new AssertionError(
					"It is virtually impossible for a reason to exist for newly"
					+ " created location term", e);
			}
		}
		return locationTerm;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Program> findAllowedProgramsForOffenderOnDate(
			final Offender offender, final Date effectiveDate) {
		PlacementTerm placementTerm
			= this.placementTermDelegate.findForOffenderOnDate(
					offender, effectiveDate);
		if (placementTerm != null) {
			if (placementTerm.getCorrectionalStatusTerm()
					.getCorrectionalStatus().getLocationRequired()) {
				LocationTerm locationTerm = this.locationTermDelegate
					.findByOffenderOnDate(offender, effectiveDate);
				if (locationTerm != null) {
					return this.programDelegate.findOfferedByLocation(
							locationTerm.getLocation());
				} else {
					return this.programDelegate
							.findOfferedBySupervisoryOrganization(
									placementTerm
										.getSupervisoryOrganizationTerm()
											.getSupervisoryOrganization());					
				}
			} else {
				return this.programDelegate
						.findOfferedBySupervisoryOrganization(
								placementTerm.getSupervisoryOrganizationTerm()
									.getSupervisoryOrganization());
			}
		} else {
			return Collections.emptyList();
		}
	}

	/** {@inheritDoc} */
	@Override
	public ProgramPlacement placeOnProgram(final Offender offender,
			final Date effectiveDate, final Date endDate,
			final Program program) throws DuplicateEntityFoundException {
		ProgramPlacement existingProgramPlacement
				= this.programPlacementDelegate.findByOffenderOnDate(
						offender, effectiveDate);
		if (existingProgramPlacement != null) {
			try {
				existingProgramPlacement = this.programPlacementDelegate
					.update(existingProgramPlacement,
							DateRange.adjustEndDate(
									existingProgramPlacement.getDateRange(),
									effectiveDate), program,
							existingProgramPlacement.getPlacementTerm(),
							existingProgramPlacement.getLocationTerm());
			} catch (DuplicateEntityFoundException e) {
				
				// The business key of the program placement should prevent
				// this - SA
				throw new AssertionError(
						"Duplicate program placements to existing should not"
						+ " be possible", e);
			}
		}
		PlacementTerm placementTerm = this.placementTermDelegate
				.findForOffenderOnDate(offender, effectiveDate);
		return this.programPlacementDelegate
				.create(offender, new DateRange(effectiveDate, endDate),
						program, placementTerm, null);
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm endLocation(
			final Offender offender, final Date effectiveDate) {
		LocationTerm locationTerm = this.locationTermDelegate
				.findByOffenderOnDate(offender, effectiveDate);
		if (locationTerm != null) {
			Date locationTermEndDate
				= DateRange.getEndDate(locationTerm.getDateRange());
			if (locationTermEndDate == null
					|| locationTermEndDate.compareTo(effectiveDate) > 0) {
				try {
					locationTerm = this.locationTermDelegate
							.update(locationTerm, locationTerm.getLocation(),
									DateRange.getStartDate(locationTerm
											.getDateRange()),
									effectiveDate,
									locationTerm.getLocked());
				} catch (DuplicateEntityFoundException e) {
					throw new AssertionError("Location term cannot exist", e);
				}
			}
			LocationReasonTerm locationReasonTerm
				= this.locationReasonTermDelegate.findForLocationTermOnDate(
						locationTerm, effectiveDate);
			if (locationReasonTerm != null) {
				Date locationReasonTermEndDate
					= DateRange.getEndDate(locationReasonTerm.getDateRange());
				if (locationReasonTermEndDate == null
						|| locationReasonTermEndDate
							.compareTo(effectiveDate) > 0) {
					
					// Do not propagate the report of the ended location
					// reason term existing - this would most likely be a
					// concurrency issue - SA
					try {
						locationReasonTermDelegate
							.update(locationReasonTerm,
									DateRange.adjustEndDate(locationReasonTerm
											.getDateRange(), effectiveDate),
									locationReasonTerm.getReason());
					} catch (LocationReasonTermExistsException e) {
						throw new AssertionError(
								"Nearly impossible condition encountered", e);
					}
				}
			}
		}
		return locationTerm;
	}
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm endSupervision(final Offender offender,
			final PlacementTermChangeReason changeReason,
			final Date effectiveDate)
				throws IllegalCorrectionalStatusChangeException,
					OffenderNotUnderSupervisionException,
					PlacementTermChangeReasonNotAllowedException,
					PlacementTermConflictException,
					EndedPlacementTermException {

		// Throws exception if offender is not under supervision on 
		// effective date
		PlacementTerm placementTerm
			= this.placementTermDelegate.findForOffenderOnDate(
					offender, effectiveDate);
		if (placementTerm == null) {
			throw new OffenderNotUnderSupervisionException(
					"Placement term for offender does not exist on date");
		}
		
		// Throws exception if placement term on effective date is ended
		if (DateRange.getEndDate(placementTerm.getDateRange())
				!= null) {
			throw new EndedPlacementTermException(
					"Placement term on effective date is ended");
		}
		
		// Throws exception if conflicting placement terms exist
		long conflictingCount = this.placementTermDelegate
				.countForOffenderBetweenDatesExcluding(offender,
						DateRange.getStartDate(placementTerm.getDateRange()),
						effectiveDate, placementTerm);
		if (conflictingCount > 0) {
			throw new PlacementTermConflictException(
					conflictingCount + " conflicting placement term(s)");
		}
		
		// Throws exception if offender is does not have a correctional status
		// from which supervision can be ended
		if (this.allowedCorrectionalStatusChangeDelegate
				.find(placementTerm.getCorrectionalStatusTerm()
						.getCorrectionalStatus(), null) == null) {
			throw new IllegalCorrectionalStatusChangeException(
					"Cannot end supervision from "
						+ placementTerm.getCorrectionalStatusTerm()
							.getCorrectionalStatus().getName() + " status");
		}
		
		// Throws exception if change reason is not allowed
		CorrectionalStatus correctionalStatus = placementTerm
				.getCorrectionalStatusTerm().getCorrectionalStatus();
		if (this.allowedCorrectionalStatusChangeReasonRuleDelegate.find(
				correctionalStatus, null, changeReason) == null) {
			throw new PlacementTermChangeReasonNotAllowedException(
					String.format("%s not allowed for %s",
						changeReason.getName(), correctionalStatus.getName()));
		}
		
		// Ends correctional status term on effective date on effective date
		CorrectionalStatusTerm correctionalStatusTerm;
		try {
			correctionalStatusTerm
				= this.correctionalStatusTermDelegate.update(
						placementTerm.getCorrectionalStatusTerm(),
						DateRange.adjustEndDate(
								placementTerm.getCorrectionalStatusTerm()
								.getDateRange(),
						effectiveDate),
						placementTerm.getCorrectionalStatusTerm()
							.getCorrectionalStatus());
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Correctional status term cannot exist", e);
		}
		
		// Ends supervisory organization term on effective date on effective
		// date
		SupervisoryOrganizationTerm supervisoryOrganizationTerm;
		try {
			supervisoryOrganizationTerm
					= this.supervisoryOrganizationTermDelegate.update(
							placementTerm.getSupervisoryOrganizationTerm(),
							DateRange.adjustEndDate(
									placementTerm
										.getSupervisoryOrganizationTerm()
										.getDateRange(), effectiveDate),
							placementTerm.getSupervisoryOrganizationTerm()
									.getSupervisoryOrganization());
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError(
					"Supervisory organization term cannot exist", e);
		}			
		
		// Ends placement term on effective date on effective date
		try {
			return this.placementTermDelegate.update(placementTerm,
					PlacementStatus.PLACED, null,
					DateRange.adjustEndDate(
							placementTerm.getDateRange(), effectiveDate),
					supervisoryOrganizationTerm,
					correctionalStatusTerm,
					placementTerm.getStartChangeReason(),
					changeReason,
					placementTerm.getLocked());
		} catch (DuplicateEntityFoundException e) {
			throw new AssertionError("Placement term cannot exist", e);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public CorrectionalStatus findCorrectionalStatus(
			final Offender offender, final Date effectiveDate) {
		CorrectionalStatusTerm correctionalStatusTerm
			= this.correctionalStatusTermDelegate.findForOffenderOnDate(
					offender, effectiveDate);
		if (correctionalStatusTerm != null) {
			return correctionalStatusTerm.getCorrectionalStatus();
		} else {
			return null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisoryOrganization> findAllowedSupervisoryOrganizations(
			final CorrectionalStatus correctionalStatus) {
		return this.supervisoryOrganizationDelegate
				.findForCorrectionalStatus(correctionalStatus);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<PlacementTermChangeReason> findAllowedChangeReasons(
			final CorrectionalStatus fromCorrectionalStatus,
			final CorrectionalStatus toCorrectionalStatus) {
		return this.placementTermChangeReasonDelegate
				.findAllowed(fromCorrectionalStatus, toCorrectionalStatus);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForOrganization(
			final Organization organization)
	{
		return this.locationDelegate.findByOrganization(organization);
	}

	/** {@inheritDoc} */
	@Override
	public List<Program> findProgramsOfferedAtLocation(
			final Location location) {
		return this.programDelegate.findOfferedByLocation(location);
	}

	/** {@inheritDoc} */
	@Override
	public List<Program> findProgramsOfferedBySupervisoryOrganization(
			final SupervisoryOrganization supervisoryOrganization) {
		return this.programDelegate.findOfferedBySupervisoryOrganization(
				supervisoryOrganization);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findLocationsForCorrectionalStatus(
			CorrectionalStatus correctionalStatus) {
		List<SupervisoryOrganization> organizations
			= this.supervisoryOrganizationDelegate
				.findForCorrectionalStatus(correctionalStatus);
		return this.locationDelegate
			.findByOrganizations(
					organizations.toArray(
							new SupervisoryOrganization[] { }));
	}
}