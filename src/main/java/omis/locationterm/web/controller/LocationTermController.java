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
package omis.locationterm.web.controller;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.location.domain.Location;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsAfterException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermChangeActionAssociationExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.exception.LocationTermLockedException;
import omis.locationterm.report.LocationTermReportService;
import omis.locationterm.report.LocationTermSummary;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.web.form.LocationReasonTermItem;
import omis.locationterm.web.form.LocationReasonTermItemOperation;
import omis.locationterm.web.form.LocationTermForm;
import omis.locationterm.web.validator.LocationTermFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.organization.domain.Organization;
import omis.region.domain.State;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;
import omis.web.util.RedirectUrlRetriever;

/**
 * Controller for location terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jan 15, 2014)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/locationTerm")
@PreAuthorize("hasRole('USER')")
public class LocationTermController {

	/* View names. */
	
	private static final String LIST_VIEW_NAME = "locationTerm/list";

	private static final String EDIT_VIEW_NAME = "locationTerm/edit";
	
	private static final String LOCATIONS_VIEW_NAME
		= "location/includes/locations";
	
	private static final String BOOLEAN_VALUE_VIEW_NAME
		= "common/json/booleanValue";
	
	private static final String REASONS_VIEW_NAME
		= "locationTerm/includes/locationReasons";
	
	/* Redirects. */
	
	private static final String LIST_REDIRECT
		= "redirect:/locationTerm/list.html?offender=%d";
	
	/* Action menus. */
	
	private static final String LOCATION_TERM_ACTION_MENU_VIEW_NAME
		= "locationTerm/includes/locationTermActionMenu";

	private static final String LOCATION_TERMS_ACTION_MENU_VIEW_NAME
		= "locationTerm/includes/locationTermsActionMenu";
	
	private static final String REASON_TERMS_ACTION_MENU_VIEW_NAME
		= "locationTerm/includes/reasonTermsActionMenu";
	
	private static final String REASON_TERM_EDIT_TABLE_ROW_VIEW_NAME
		= "locationTerm/includes/reasonTermEditTableRow";
	
	/* Model keys. */

	private static final String LOCATION_TERM_MODEL_KEY = "locationTerm";

	private static final String LOCATION_TERM_FORM_MODEL_KEY
		= "locationTermForm";
	
	private static final String LOCATIONS_MODEL_KEY = "locations";
	
	private static final String REASONS_MODEL_KEY = "reasons";
	
	private static final String DEFAULT_REASON_MODEL_KEY = "defaultReason";

	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String REASON_TERM_ITEM_MODEL_KEY
		= "locationReasonTermItem";

	private static final String LOCATION_MODEL_KEY = "location";

	private static final String SUPERVISORY_ORGANIZATION_LOCATIONS_MODEL_KEY
		= "supervisoryOrganizationLocations";

	private static final String SUPERVISORY_ORGANIZATION_MODEL_KEY
		= "supervisoryOrganization";
	
	private static final String LOCATION_TERM_SUMMARIES_MODEL_KEY
		= "locationTermSummaries";
	
	private static final String REASON_TERM_ITEM_INDEX_MODEL_KEY
		= "locationReasonTermItemIndex";
	
	private static final String CORRECTIONAL_STATUS_MODEL_KEY
		= "correctionalStatus";
	
	private static final String PLACEMENT_TERM_MODEL_KEY = "placementTerm";
	
	private static final String DEFAULT_LOCATION_MODEL_KEY = "defaultLocation";
	
	private static final String STATES_MODEL_KEY = "states";

	private static final String TO_LOCATION_MODEL_KEY = "toLocation";
	
	private static final String CHANGE_ACTIONS_MODEL_KEY = "changeActions";
	
	private static final String CHANGE_ACTION_MODEL_KEY = "changeAction";
	
	private static final String BOOLEAN_VALUE_MODEL_KEY = "booleanValue";
	
	/* Message bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.locationterm.msgs.form";

	/* Message keys. */
	
	private static final String LOCATION_REASON_EXISTS_MESSAGE_KEY
		= "locationReasonTerm.existsForLocationTerm";

	private static final String LOCATION_TERM_CONFLICT_MESSAGE_KEY
		= "locationTerm.conflict";

	private static final String LOCATION_REASON_TERMS_OUT_OF_BOUNDS_MESSAGE_KEY
		= "locationReasonTerm.dateRangeOutOfLocationTermDateRangeBounds";

	private static final String LOCATION_TERM_EXISTS_AFTER_EXCEPTION
		= "locationTerm.existsAfter";
	
	private static final String LOCATION_REASON_TERM_CONFLICT_MESSAGE_KEY
		= "locationReasonTerm.conflict";

	private static final String LOCATION_REASON_TERM_EXISTS_AFTER_MESSAGE_KEY
		= "locationReasonTerm.existsAfter";
	
	private static final String LOCATION_TERM_LOCKED_MESSAGE_KEY
		= "locationTerm.locked";

	private static final String OFFENDER_NOT_UNDER_SUPERVISION_MESSAGE_KEY
		= "offender.notUnderSupervision";

	private static final String LOCATION_TERM_EXISTS_MESSAGE_KEY
		= "locationTerm.exists";
	
	private static final String
	LOCATION_TERM_CHANGE_ACTION_ASSOCIATION_EXISTS_MESSAGE_KEY
		= "locationTermChangeActionAssociation.exists";
	
	/* Report names. */
	
	private static final String LOCATION_TERM_LISTING_REPORT_NAME 
		= "/Placement/LocationTerms/Location_Term_Listing";
	
	private static final String LOCATION_TERM_DETAILS_REPORT_NAME 
		= "/Placement/LocationTerms/Location_Term_Details";
	
	/* Report parameter names. */
	
	private static final String LOCATION_TERM_LISTING_ID_REPORT_PARAM_NAME
		= "DOC_ID";
	
	private static final String LOCATION_TERM_DETAILS_ID_REPORT_PARAM_NAME
		= "LOC_TERM_ID";

	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("locationTermReportService")
	private LocationTermReportService locationTermReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationTermPropertyEditorFactory")
	private PropertyEditorFactory locationTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationReasonPropertyEditorFactory")
	private PropertyEditorFactory locationReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationReasonTermPropertyEditorFactory")
	private PropertyEditorFactory locationReasonTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationTermChangeActionPropertyEditorFactory")
	private PropertyEditorFactory locationTermChangeActionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("organizationPropertyEditorFactory")
	private PropertyEditorFactory organizationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("correctionalStatusPropertyEditorFactory")
	private PropertyEditorFactory correctionalStatusPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("locationTermFormValidator")
	private LocationTermFormValidator locationTermFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructor. */
	
	/** Instantiates a controller for location terms. */
	public LocationTermController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows location terms for offender.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @param effectiveTime effective time
	 * @return location terms for offender
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_TERM_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "effectiveDate", required = false)
				final Date effectiveDate,
			@RequestParam(value = "effectiveTime", required = false)
				final String effectiveTime) {
		Date effectiveDateTime;
		if (effectiveDate != null) {
			effectiveDateTime = DateManipulator
					.getDateAtTimeOfDay(
							effectiveDate, this.parseTimeText(effectiveTime));
		} else {
			effectiveDateTime = new Date();
		}
		PlacementTerm placementTerm = this.locationTermService
				.findPlacementTermForOffenderOnDate(
						offender, effectiveDateTime);
		LocationTerm locationTerm = this.locationTermService
				.findForOffenderOnDate(offender, effectiveDateTime);
		List<LocationTermSummary> locationTermSummaries
			= locationTermReportService.summarizeByOffenderOnDate(
				offender, effectiveDateTime);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(LOCATION_TERM_SUMMARIES_MODEL_KEY, locationTermSummaries);
		if (placementTerm != null) {
			if (placementTerm.getSupervisoryOrganizationTerm() != null) {
				mav.addObject(SUPERVISORY_ORGANIZATION_MODEL_KEY,
						placementTerm.getSupervisoryOrganizationTerm()
							.getSupervisoryOrganization());
			}
			mav.addObject(CORRECTIONAL_STATUS_MODEL_KEY,
					placementTerm.getCorrectionalStatusTerm()
						.getCorrectionalStatus());
		}
		if (locationTerm != null) {
			mav.addObject(LOCATION_MODEL_KEY, locationTerm.getLocation());
		}
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	/**
	 * Shows form to create new location term for offender.
	 * 
	 * <p>Change action is required until supervisory organization is supported.
	 * 
	 * @param offender offender
	 * @param organization organization
	 * @param defaultStartDate default start date
	 * @param defaultStartTime default start time
	 * @param toLocation location to which to send
	 * @param changeAction change action - required until supervisory
	 * organization is supported
	 * @return form to create new location term for offender
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "defaultStartDate", required = false)
				final Date defaultStartDate,
			@RequestParam(value = "defaultStartTime", required = false)
				final String defaultStartTime,
			@RequestParam(value = "toLocation", required = false)
				final Location toLocation,
			@RequestParam(value = "changeAction", required = true)
				final LocationTermChangeAction changeAction) {
		Date effectiveDate;
		if (defaultStartDate != null) {
			effectiveDate = DateManipulator.getDateAtTimeOfDay(
					defaultStartDate,
					this.parseTimeText(defaultStartTime));
		} else {
			effectiveDate = new Date();
		}
		PlacementTerm placementTerm = this.locationTermService
				.findPlacementTermForOffenderOnDate(offender, effectiveDate);
		State homeState = this.locationTermService.findHomeState();
		List<Location> locations;
		if (toLocation == null && placementTerm != null) {
			if (changeAction != null) {
				if (homeState != null) {
					locations = this.locationTermService
						.findLocationsForActionForCorrectionalStatusInState(
								changeAction,
								placementTerm.getCorrectionalStatusTerm()
									.getCorrectionalStatus(),
								homeState);
				} else {
					locations = this.locationTermService
						.findLocationsForActionForCorrectionalStatus(
								changeAction,
								placementTerm.getCorrectionalStatusTerm()
									.getCorrectionalStatus());
				}
			} else {
				if (homeState != null) {
					locations = this.locationTermService
						.findLocationsForCorrectionalStatusInState(
								placementTerm.getCorrectionalStatusTerm()
									.getCorrectionalStatus(), homeState);
				} else {
					locations = this.locationTermService
							.findLocationsForCorrectionalStatus(
									placementTerm.getCorrectionalStatusTerm()
										.getCorrectionalStatus());
				}
			}
		} else {
			locations = Collections.emptyList();
		}
		LocationTermForm locationTermForm = new LocationTermForm();
		locationTermForm.setAllowMultipleReasonTerms(false);
		locationTermForm.setAllowSingleReasonTerm(true);
		locationTermForm.setStartDate(effectiveDate);
		locationTermForm.setStartTime(effectiveDate);
		if (toLocation != null) {
			locationTermForm.setLocation(toLocation);
			locationTermForm.setAllowLocation(false);
			locationTermForm.setAllowState(false);
		} else {
			locationTermForm.setAllowLocation(true);
			locationTermForm.setAllowState(true);
			locationTermForm.setState(homeState);
			if (placementTerm != null
					&& placementTerm.getSupervisoryOrganizationTerm() != null) {
				locationTermForm.setEndDate(
						DateRange.getEndDate(placementTerm.getDateRange()));
				locationTermForm.setEndTime(
						DateRange.getEndDate(placementTerm.getDateRange()));
				List<Location> locationByOrganizations
					= this.locationTermService
						.findLocationsByOrganization(
								placementTerm.getSupervisoryOrganizationTerm()
									.getSupervisoryOrganization());
				if (locationByOrganizations.size() == 1) {
					locationTermForm.setLocation(
							locationByOrganizations.get(0));
				}
			}
		}
		List<LocationReason> reasons;
		if (changeAction != null) {
			reasons = this.locationTermService
					.findReasonsForChangeAction(changeAction);
		} else if (toLocation != null) {
			reasons = this.locationTermService
					.findReasonsAllowedForLocation(toLocation);
		} else if (locationTermForm.getLocation() != null) {
			reasons = this.locationTermService
					.findReasonsAllowedForLocation(
							locationTermForm.getLocation());
		} else {
			reasons = Collections.emptyList();
		}
		ModelAndView mav = this.prepareEditMav(
				offender, locationTermForm, locations, reasons);
		if (placementTerm != null) {
			mav.addObject(PLACEMENT_TERM_MODEL_KEY, placementTerm);
		}
		if (toLocation != null) {
			mav.addObject(TO_LOCATION_MODEL_KEY, toLocation);
		}
		if (changeAction != null) {
			mav.addObject(CHANGE_ACTION_MODEL_KEY, changeAction);
		}
		return mav;
	}

	/**
	 * Shows form to update an existing location term for offender.
	 * 
	 * @param locationTerm location term
	 * @param defaultEndDate default end date - used if end date of location
	 * term is not set
	 * @param defaultEndTime default end time - used if default end date is set
	 * and end date of location term is not
	 * @return form to update existing location term for offender
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_TERM_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "locationTerm", required = true)
				final LocationTerm locationTerm,
			@RequestParam(value = "defaultEndDate", required = false)
				final Date defaultEndDate,
			@RequestParam(value = "defaultEndTime", required = false)
				final String defaultEndTime) {
		List<Location> locations;
		State state = locationTerm.getLocation().getAddress().getZipCode()
				.getCity().getState();
		locations = this.locationTermService
				.findLocationsAllowedForPlacementInState(state);
		LocationTermForm locationTermForm = new LocationTermForm();
		locationTermForm.setState(state);
		locationTermForm.setAllowLocation(false);
		locationTermForm.setAllowState(false);
		locationTermForm.setLocation(locationTerm.getLocation());
		if (locationTerm.getDateRange() != null) {
			locationTermForm.setStartDate(
					locationTerm.getDateRange().getStartDate());
			locationTermForm.setStartTime(
					locationTerm.getDateRange().getStartDate());
			if (locationTerm.getDateRange().getEndDate() != null) {
				locationTermForm.setEndDate(
						locationTerm.getDateRange().getEndDate());
				locationTermForm.setEndTime(
						locationTerm.getDateRange().getEndDate());
			} else {
				if (defaultEndDate != null) {
					locationTermForm.setEndDate(defaultEndDate);
					locationTermForm.setEndTime(this.parseTimeText(defaultEndTime));
				}
			}
		} else {
			if (defaultEndDate != null) {
				locationTermForm.setEndDate(defaultEndDate);
				locationTermForm.setEndTime(this.parseTimeText(defaultEndTime));
			}
		}
		locationTermForm.setAllowMultipleReasonTerms(false);
		List<LocationReasonTerm> reasonTerms = this.locationTermService
				.findReasonTerms(locationTerm);
		Collections.reverse(reasonTerms);
		if (reasonTerms.size() > 1 || (reasonTerms.size() == 1
				&& !DateRange.areEqual(locationTerm.getDateRange(),
						reasonTerms.get(0).getDateRange()))) {
			locationTermForm.setAssociateMultipleReasonTerms(true);
			locationTermForm.setAllowSingleReasonTerm(false);
			List<LocationReasonTermItem> reasonTermItems
				= new ArrayList<LocationReasonTermItem>();
			for (LocationReasonTerm reasonTerm : reasonTerms) {
				LocationReasonTermItem reasonTermItem
					= new LocationReasonTermItem();
				reasonTermItem.setOperation(
						LocationReasonTermItemOperation.UPDATE);
				reasonTermItem.setReasonTerm(reasonTerm);
				reasonTermItem.setReason(reasonTerm.getReason());
				reasonTermItem.setStartDate(
						DateRange.getStartDate(reasonTerm.getDateRange()));
				reasonTermItem.setStartTime(
						DateRange.getStartDate(reasonTerm.getDateRange()));
				reasonTermItem.setEndDate(
						DateRange.getEndDate(reasonTerm.getDateRange()));
				reasonTermItem.setEndTime(
						DateRange.getEndDate(reasonTerm.getDateRange()));
				reasonTermItems.add(reasonTermItem);
				locationTermForm.setReasonTermItems(reasonTermItems);
			}
		} else if (reasonTerms.size() == 1) {
			locationTermForm.setAssociateMultipleReasonTerms(false);
			locationTermForm.setAllowSingleReasonTerm(true);
			LocationReasonTerm reasonTerm = reasonTerms.get(0);
			locationTermForm.setReason(reasonTerm.getReason());
			List<LocationReasonTermItem> reasonTermItems
				= new ArrayList<LocationReasonTermItem>();
			LocationReasonTermItem reasonTermItem
				= new LocationReasonTermItem();
			reasonTermItem.setOperation(
					LocationReasonTermItemOperation.UPDATE);
			reasonTermItem.setReasonTerm(reasonTerm);
			reasonTermItem.setReason(reasonTerm.getReason());
			reasonTermItem.setStartDate(
					DateRange.getStartDate(reasonTerm.getDateRange()));
			reasonTermItem.setStartTime(
					DateRange.getStartDate(reasonTerm.getDateRange()));
			reasonTermItem.setEndDate(
					DateRange.getEndDate(reasonTerm.getDateRange()));
			reasonTermItem.setEndTime(
					DateRange.getEndDate(reasonTerm.getDateRange()));
			reasonTermItems.add(reasonTermItem);
			locationTermForm.setReasonTermItems(reasonTermItems);
		} else {
			locationTermForm.setAssociateMultipleReasonTerms(false);
			locationTermForm.setAllowSingleReasonTerm(true);
		}
		List<LocationReason> reasons = this.locationTermService
				.findReasonsAllowedForLocation(locationTerm.getLocation());
		ModelAndView mav = this.prepareEditMav(
				locationTerm.getOffender(), locationTermForm,
				locations, reasons);
		mav.addObject(LOCATION_TERM_MODEL_KEY, locationTerm);
		return mav;
	}
	
	/**
	 * Saves a new location term.
	 * 
	 * @param offender offender for whom to save new location term
	 * @param toLocation location to which to send
	 * @param changeAction location term change action
	 * @param locationTermForm form for location term
	 * @param result binding result
	 * @param session HTTP session
	 * @return redirect to list location terms
	 * @throws LocationTermConflictException if conflicting location terms exist
	 * @throws LocationTermExistsAfterException if location terms exist after 
	 * the start date when an end date is not specified
	 * @throws DateRangeOutOfBoundsException 
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 * @throws OffenderNotUnderSupervisionException offender is not under 
	 * supervision on the specified start date
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationReasonTermExistsException if reason term exists
	 * @throws LocationTermChangeActionAssociationExistsException if association
	 * between location term and change action exists
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('LOCATION_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "toLocation", required = false)
				final Location toLocation,
			@RequestParam(value = "changeAction", required = false)
				final LocationTermChangeAction changeAction,
			final LocationTermForm locationTermForm,
			final BindingResult result,
			final HttpSession session)
					throws LocationTermConflictException, 
						LocationTermExistsAfterException, 
						LocationReasonTermConflictException, 
						DateRangeOutOfBoundsException, 
						LocationReasonTermExistsAfterException, 
						OffenderNotUnderSupervisionException,
						LocationTermExistsException,
						LocationReasonTermExistsException,
						LocationTermChangeActionAssociationExistsException {
		this.locationTermFormValidator.validate(locationTermForm, result);
		if (result.hasErrors()) {
			Date effectiveDate;
			List<Location> locations;
			PlacementTerm placementTerm;
			if (locationTermForm.getStartDate() != null) {
				effectiveDate = DateManipulator.getDateAtTimeOfDay(
						locationTermForm.getStartDate(),
						locationTermForm.getEndDate());
				placementTerm = this.locationTermService
						.findPlacementTermForOffenderOnDate(
								offender, effectiveDate);
				if (toLocation == null && placementTerm != null) {
					if (changeAction != null) {
						if (locationTermForm.getState() != null) {
							locations = this.locationTermService
							.findLocationsForActionForCorrectionalStatusInState(
									changeAction,
									placementTerm.getCorrectionalStatusTerm()
										.getCorrectionalStatus(),
											locationTermForm.getState());
						} else {
							locations = this.locationTermService
								.findLocationsForActionForCorrectionalStatus(
									changeAction,
									placementTerm.getCorrectionalStatusTerm()
											.getCorrectionalStatus());
						}
					} else {
						if (locationTermForm.getState() != null) {
							locations = this.locationTermService
									.findLocationsForCorrectionalStatusInState(
									placementTerm
										.getCorrectionalStatusTerm()
											.getCorrectionalStatus(),
									locationTermForm.getState());
						} else {
							locations = this.locationTermService
									.findLocationsForCorrectionalStatus(
									placementTerm
										.getCorrectionalStatusTerm()
											.getCorrectionalStatus());
						}
					}
				} else {
					locations = Collections.emptyList();
				}
			} else {
				locations = Collections.emptyList();
				placementTerm = null;
			}
			List<LocationReason> reasons;
			if (locationTermForm.getLocation() != null) {
				reasons = this.locationTermService
					.findReasonsAllowedForLocation(
							locationTermForm.getLocation());
			} else if (changeAction != null) {
				reasons = this.locationTermService
						.findReasonsForChangeAction(changeAction);
			} else if (toLocation != null) {
				reasons = this.locationTermService
						.findReasonsAllowedForLocation(toLocation);
			} else {
				reasons = Collections.emptyList();
			}
			ModelAndView mav = this.prepareRedisplayEditMav(
					offender, locations, reasons, locationTermForm, result);
			if (placementTerm != null) {
				mav.addObject(PLACEMENT_TERM_MODEL_KEY, placementTerm);
			}
			if (toLocation != null) {
				mav.addObject(TO_LOCATION_MODEL_KEY, toLocation);
			}
			if (changeAction != null) {
				mav.addObject(CHANGE_ACTION_MODEL_KEY, changeAction);
			}
			return mav;
		}
		DateRange dateRange = this.createDateRange(
				locationTermForm.getStartDate(),
				locationTermForm.getStartTime(),
				locationTermForm.getEndDate(),
				locationTermForm.getEndTime());
		Location location;
		if (toLocation != null) {
			location = toLocation;
		} else {
			location = locationTermForm.getLocation();
		}
		LocationTerm locationTerm = this.locationTermService.create(
				offender, location, dateRange);
		if (locationTermForm.getAllowMultipleReasonTerms() != null
				&& locationTermForm.getAllowMultipleReasonTerms()
				&& locationTermForm.getAssociateMultipleReasonTerms() != null
				&& locationTermForm.getAssociateMultipleReasonTerms()) {
			for (LocationReasonTermItem reasonTermItem
					: locationTermForm.getReasonTermItems()) {
				this.locationTermService.createReasonTerm(locationTerm,
						this.createDateRange(
								reasonTermItem.getStartDate(),
								reasonTermItem.getStartTime(),
								reasonTermItem.getEndDate(),
								reasonTermItem.getEndTime()),
						reasonTermItem.getReason());
			}
		} else if (locationTermForm.getAllowSingleReasonTerm() != null
				&& locationTermForm.getAllowSingleReasonTerm()) {
			if (locationTermForm.getReason() != null) {
				this.locationTermService.createReasonTerm(locationTerm, dateRange,
						locationTermForm.getReason());
			}
		} else {
			
			// Thrown when neither single or multiple reason terms are allowed
			throw new UnsupportedOperationException(
					"Must do something with reasons");
		}
		if (changeAction != null) {
			this.locationTermService.associateChangeAction(
					locationTerm, changeAction);
		}
		
		// Checks for redirect URLs, if found, returns it
		// Otherwise return to location term listing
		RedirectUrlRetriever redirectUrlRetriever
				= RedirectUrlRetriever.createInstance(session);
		if (redirectUrlRetriever.isSet()) {
			return new ModelAndView(
					"redirect:" + redirectUrlRetriever.retrieveAndRemove());
		} else {
			return new ModelAndView(
					String.format(LIST_REDIRECT, offender.getId()));
		}
	}

	/**
	 * Updates an existing location term.
	 * 
	 * @param locationTerm location term to update
	 * @param locationTermForm form for location term
	 * @param result binding result
	 * @return redirect to list location terms
	 * @throws LocationTermConflictException if conflicting location terms exist
	 * @throws DateRangeOutOfBoundsException if existing location reason terms
	 * are out of the date range bounds of the location term
	 * @throws LocationTermExistsAfterException if existing location terms exist 
	 * after the start date when the end date is null
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException offender is not under 
	 * supervision on the specified start date
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationReasonTermExistsException if reason term exists 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('LOCATION_TERM_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "locationTerm", required = true)
				final LocationTerm locationTerm,
			final LocationTermForm locationTermForm,
			final BindingResult result)
					throws LocationTermConflictException,
						DateRangeOutOfBoundsException, 
						LocationTermExistsAfterException, 
						LocationReasonTermConflictException, 
						LocationReasonTermExistsAfterException,
						LocationTermLockedException, 
						OffenderNotUnderSupervisionException,
						LocationTermExistsException,
						LocationReasonTermExistsException {
		this.locationTermFormValidator.validate(locationTermForm, result);
		if (result.hasErrors()) {
			List<Location> locations;
			if (locationTermForm.getState() != null) {
				locations = this.locationTermService
						.findLocationsAllowedForPlacementInState(
								locationTermForm.getState());
			} else {
				locations = this.locationTermService
						.findLocationsAllowedForPlacement();
			}
			List<LocationReason> reasons = this.locationTermService
					.findReasonsAllowedForLocation(locationTerm.getLocation());
			ModelAndView mav =  this.prepareRedisplayEditMav(
					locationTerm.getOffender(), locations, reasons,
					locationTermForm, result);
			mav.addObject(LOCATION_TERM_MODEL_KEY, locationTerm);
			return mav;
		}
		DateRange dateRange = this.createDateRange(
			locationTermForm.getStartDate(), locationTermForm.getStartTime(),
			locationTermForm.getEndDate(), locationTermForm.getEndTime());
		LocationTerm updatedLocationTerm
				= this.locationTermService.update(locationTerm,
						locationTerm.getLocation(), dateRange);
		if (locationTermForm.getAllowMultipleReasonTerms() != null
				&& locationTermForm.getAllowMultipleReasonTerms()
				&& locationTermForm.getAssociateMultipleReasonTerms() != null
				&& locationTermForm.getAssociateMultipleReasonTerms()) {
			for (LocationReasonTermItem reasonTermItem
					: locationTermForm.getReasonTermItems()) {
				if (LocationReasonTermItemOperation.CREATE
						.equals(reasonTermItem.getOperation())) {
					this.locationTermService.createReasonTerm(
							updatedLocationTerm,
							this.createDateRange(
									reasonTermItem.getStartDate(),
									reasonTermItem.getStartTime(),
									reasonTermItem.getEndDate(),
									reasonTermItem.getEndTime()),
							reasonTermItem.getReason());
				} else if (LocationReasonTermItemOperation.UPDATE
						.equals(reasonTermItem.getOperation())) {
					this.locationTermService.updateReasonTerm(
							reasonTermItem.getReasonTerm(),
							this.createDateRange(
									reasonTermItem.getStartDate(),
									reasonTermItem.getStartTime(),
									reasonTermItem.getEndDate(),
									reasonTermItem.getEndTime()),
							reasonTermItem.getReason());
				} else if (LocationReasonTermItemOperation.REMOVE
						.equals(reasonTermItem.getOperation())) {
					this.locationTermService.removeReasonTerm(
							reasonTermItem.getReasonTerm());
				} else if (reasonTermItem.getOperation() != null) {
					throw new UnsupportedOperationException(String.format(
							"Unsupported reason term item operation: %s",
							reasonTermItem.getOperation()));
				}
			}
		} else {
			List<LocationReasonTerm> reasonTerms = this.locationTermService
					.findReasonTerms(updatedLocationTerm);
			if (reasonTerms.size() == 0) {
				if (locationTermForm.getReason() != null) {
					this.locationTermService.createReasonTerm(
							updatedLocationTerm, dateRange,
							locationTermForm.getReason());
				}
			} else if (reasonTerms.size() == 1) {
				LocationReasonTerm reasonTerm = reasonTerms.get(0);
				if (locationTermForm.getReason() != null) {
					this.locationTermService.updateReasonTerm(
						reasonTerm, dateRange, locationTermForm.getReason());
				} else {
					this.locationTermService.removeReasonTerm(reasonTerm);
				}
			} else {
				
				// Can't handle more than one reason existing for the location
				throw new IllegalStateException("Multiple reasons exist");
			}
		}
		return new ModelAndView(String.format(LIST_REDIRECT,
				updatedLocationTerm.getOffender().getId()));
	}

	/**
	 * Removes a location term.
	 * 
	 * @param locationTerm location term to remove
	 * @return redirect to list location terms
	 * @throws LocationReasonTermExistsException if location reason terms exist
	 * for location term
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_TERM_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "locationTerm", required = true)
				final LocationTerm locationTerm)
						throws LocationReasonTermExistsException {
		Offender offender = locationTerm.getOffender();
		this.locationTermService.remove(locationTerm);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/* Screen parts. */
	
	/**
	 * Creates reason term row.
	 * 
	 * @param itemIndex item index
	 * @return row to create reason term
	 */
	@RequestMapping(
			value = "/createReasonTermRow.html", method = RequestMethod.GET)
	public ModelAndView createReasonTermRow(
			@RequestParam(value = "itemIndex", required = true)
				final Integer itemIndex,
			@RequestParam(value = "location", required = false)
				final Location location) {
		LocationReasonTermItem locationReasonTermItem
			= new LocationReasonTermItem();
		locationReasonTermItem.setOperation(
				LocationReasonTermItemOperation.CREATE);
		List<LocationReason> reasons;
		if (location != null) {
			reasons = this.locationTermService
					.findReasonsAllowedForLocation(location);
		} else {
			reasons = this.locationTermService.findReasons();
		}
		ModelAndView mav = new ModelAndView(
				REASON_TERM_EDIT_TABLE_ROW_VIEW_NAME);
		mav.addObject(REASON_TERM_ITEM_MODEL_KEY, locationReasonTermItem);
		mav.addObject(REASON_TERM_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(REASONS_MODEL_KEY, reasons);
		return mav;
	}
	
	/* AJAX invocable methods. */
	
	/**
	 * Returns location for offender on date.
	 * 
	 * @param offender offender
	 * @param locationTerm location term
	 * @param effectiveDate effective date
	 * @param effectiveTime effective time
	 * @param state State
	 * @param defaultLocation default location
	 * @param changeAction change action
	 * @return locations for offender on date
	 */
	@RequestMapping(value = "/findLocations.html", method = RequestMethod.GET)
	public ModelAndView findLocations(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "locationTerm", required = false)
				final LocationTerm locationTerm,
			@RequestParam(value = "effectiveDate", required = false)
				final Date effectiveDate,
			@RequestParam(value = "effectiveTime", required = false)
				final String effectiveTime,
			@RequestParam(value = "state", required = false)
				final State state,
			@RequestParam(value = "defaultLocation", required = false)
				final Location defaultLocation,
			@RequestParam(value = "changeAction", required = false)
				final LocationTermChangeAction changeAction) {
		List<Location> locations;
		if (locationTerm != null) {
			if (state != null) {
				locations = this.locationTermService
						.findLocationsAllowedForPlacementInState(state);
			} else {
				locations = this.locationTermService
						.findLocationsAllowedForPlacement();
			}
		} else if (effectiveDate != null) {
			Date effectiveDateTime = DateManipulator.getDateAtTimeOfDay(
					effectiveDate, this.parseTimeText(effectiveTime));
			PlacementTerm placementTerm = this.locationTermService
					.findPlacementTermForOffenderOnDate(
							offender, effectiveDateTime);
			if (placementTerm != null) {
				if (state != null) {
					if (changeAction != null) {
						locations = this.locationTermService
							.findLocationsForActionForCorrectionalStatusInState(
									changeAction,
									placementTerm.getCorrectionalStatusTerm()
										.getCorrectionalStatus(), state);
					} else {
						locations = this.locationTermService
								.findLocationsForCorrectionalStatusInState(
										placementTerm.getCorrectionalStatusTerm()
											.getCorrectionalStatus(), state);						
					}
				} else {
					if (changeAction != null) {
						locations = this.locationTermService
								.findLocationsForActionForCorrectionalStatus(
										changeAction,
										placementTerm.getCorrectionalStatusTerm()
											.getCorrectionalStatus());
					} else {
						locations = this.locationTermService
								.findLocationsForCorrectionalStatus(
										placementTerm.getCorrectionalStatusTerm()
											.getCorrectionalStatus());
					}
				}
			} else {
				locations = Collections.emptyList();
			}
		} else {
			locations = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(LOCATIONS_VIEW_NAME);
		mav.addObject(LOCATIONS_MODEL_KEY, locations);
		if (defaultLocation != null) {
			mav.addObject(DEFAULT_LOCATION_MODEL_KEY, defaultLocation);
		}
		return mav;
	}
	
	/**
	 * Returns reasons allowed for location.
	 * 
	 * @param location location
	 * @return reasons allowed for location
	 */
	@RequestMapping(
			value = "/findAllowedReasons.html", method = RequestMethod.GET)
	public ModelAndView findAllowedReasons(
			@RequestParam(value = "location", required = true)
				final Location location,
			@RequestParam(value = "defaultReason", required = false)
				final LocationReason defaultReason) {
		List<LocationReason> reasons = this.locationTermService
				.findReasonsAllowedForLocation(location);
		ModelAndView mav = new ModelAndView(REASONS_VIEW_NAME);
		mav.addObject(REASONS_MODEL_KEY, reasons);
		mav.addObject(DEFAULT_REASON_MODEL_KEY, defaultReason);
		return mav;
	}
	
	/**
	 * Returns whether offender is placed on effective date (and time).
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @param effectiveTime effective time
	 * @return whether offender is placed on effective date (and time)
	 */
	@RequestMapping(value = "/isPlaced.html", method = RequestMethod.GET)
	public ModelAndView isPlaced(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			@RequestParam(value = "effectiveDate", required = false)
				final Date effectiveDate,
			@RequestParam(value = "effectiveTime", required = false)
				final String effectiveTime) {
		boolean value;
		if (effectiveDate != null) {
			Date effectiveDateTime = DateManipulator.getDateAtTimeOfDay(
					effectiveDate, this.parseTimeText(effectiveTime));
			PlacementTerm placementTerm = this.locationTermService
					.findPlacementTermForOffenderOnDate(
							offender, effectiveDateTime);
			value = placementTerm != null;
		} else {
			value = false;
		}
		ModelAndView mav = new ModelAndView(BOOLEAN_VALUE_VIEW_NAME);
		mav.addObject(BOOLEAN_VALUE_MODEL_KEY, value);
		return mav;
	}
	
	/* Business handlers. */
	
	/**
	 * Handles {@code LocationReasonTermExistsException}.
	 * 
	 * @param locationReasonTermExistsException exception thrown
	 * @return screen to handle {@code LocationReasonTermExistsException}
	 */
	@ExceptionHandler(LocationReasonTermExistsException.class)
	public ModelAndView handleLocationReasonTermExistsException(
			final LocationReasonTermExistsException
				locationReasonTermExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_REASON_EXISTS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, locationReasonTermExistsException);
	}
	
	/**
	 * Handles {@code LocationTermExistsException}.
	 * 
	 * @param locationTermExistsException exception thrown
	 * @return screen to handle {@code LocationTermExistsException}
	 */
	@ExceptionHandler(LocationTermExistsException.class)
	public ModelAndView handleLocationTermExistsException(
			final LocationTermExistsException locationTermExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_TERM_EXISTS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, locationTermExistsException);
	}
	
	/**
	 * Handles {@code LocationTermConflictException}.
	 * 
	 * @param locationTermConflictException exception thrown
	 * @return screen to handle {@code LocationTermConflictException}
	 */
	@ExceptionHandler(LocationTermConflictException.class)
	public ModelAndView handleLocationTermConflictException(
			final LocationTermConflictException locationTermConflictException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_TERM_CONFLICT_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, locationTermConflictException);
	}
	
	/**
	 * Handles {@code DateRangeOutOfBoundsException}.
	 * 
	 * @param dateRangeOutOfBoundsException exception thrown
	 * @return screen to handle {@code DateRangeOutOfBoundsException}
	 */
	@ExceptionHandler(DateRangeOutOfBoundsException.class)
	public ModelAndView handleDateRangeOutOfBoundsException(
			final DateRangeOutOfBoundsException dateRangeOutOfBoundsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_REASON_TERMS_OUT_OF_BOUNDS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, dateRangeOutOfBoundsException);
	}
	
	/**
	 * Handles {@code LocationReasonTermConflictException}.
	 * 
	 * @param locationReasonTermConflictException exception thrown
	 * @return screen to handle {@code LocationReasonTermConflictException}
	 */
	@ExceptionHandler(LocationReasonTermConflictException.class)
	public ModelAndView handleLocationReasonTermConflictException(
			final LocationReasonTermConflictException 
				locationReasonTermConflictException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_REASON_TERM_CONFLICT_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, locationReasonTermConflictException);
	}
	
	/**
	 * Handles {@code LocationReasonTermExistsAfterException}.
	 * @param locationReasonTermExistsAfterException exception thrown
	 * @return screen to handle {@code LocationReasonTermExistsAfterException}
	 */
	@ExceptionHandler(LocationReasonTermExistsAfterException.class)
	public ModelAndView handleLocationReasonTermExistsAfterException(
			final LocationReasonTermExistsAfterException 
				locationReasonTermExistsAfterException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_REASON_TERM_EXISTS_AFTER_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				locationReasonTermExistsAfterException);
	}
	
	/**
	 * Handles {@code LocationTermExistsAfterException}.
	 * @param locationTermExistsAfterException exception thrown
	 * @return screen to handle {@code LocationTermExistsAfterException}
	 */
	@ExceptionHandler(LocationTermExistsAfterException.class)
	public ModelAndView handleLocationTermExistsAfterException(
			final LocationTermExistsAfterException 
				locationTermExistsAfterException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_TERM_EXISTS_AFTER_EXCEPTION, ERROR_BUNDLE_NAME, 
				locationTermExistsAfterException);
	}
	
	/**
	 * Handles {@code LocationTermLockedException}.
	 * @param locationTermLockedException exception thrown
	 * @return screen to handle {@code LocationTermLockedException}
	 */
	@ExceptionHandler(LocationTermLockedException.class)
	public ModelAndView handleLocationTermLockedException(
			final LocationTermLockedException
				locationTermLockedException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_TERM_LOCKED_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				locationTermLockedException);
	}
	
	/**
	 * Handles {@code OffenderNotUnderSupervisionException}.
	 * 
	 * @param offenderNotUnderSupervisionException exception thrown
	 * @return model and view to handle {@code 
	 * OffenderNotUnderSupervisionException}
	 */
	@ExceptionHandler(OffenderNotUnderSupervisionException.class)
	public ModelAndView handleOffenderNotUnderSupervisionException(
			final OffenderNotUnderSupervisionException
				offenderNotUnderSupervisionException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				OFFENDER_NOT_UNDER_SUPERVISION_MESSAGE_KEY,
				ERROR_BUNDLE_NAME,
				offenderNotUnderSupervisionException);
	}
	
	/**
	 * Handles {@code LocationTermChangeActionAssociationExistsException}.
	 * 
	 * @param locationTermChangeActionAssociationExistsException exception
	 * thrown
	 * @return model and view to handle
	 * {@code LocationTermChangeActionAssociationExistsException}
	 */
	@ExceptionHandler(LocationTermChangeActionAssociationExistsException.class)
	public ModelAndView
	handleLocationTermChangeActionAssociationExistsException(
			final LocationTermChangeActionAssociationExistsException
				locationTermChangeActionAssociationExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_TERM_CHANGE_ACTION_ASSOCIATION_EXISTS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME,
				locationTermChangeActionAssociationExistsException);
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for location term.
	 * 
	 * @param offender offender
	 * @return action menu for location term
	 */
	@RequestMapping(
			value = "/locationTermActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showLocationTermActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				LOCATION_TERM_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Shows action menu for location terms.
	 * 
	 * @param locationTerm location term
	 * @param offender offender
	 * @param location location
	 * @param supervisoryOrganization supervisory organization
	 * @return action menu for location terms
	 */
	@RequestMapping(
			value = "/locationTermsActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showLocationTermsActionMenu(
			@RequestParam(value = "locationTerm", required = false)
				final LocationTerm locationTerm,
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "location", required = false)
				final Location location,
			@RequestParam(
					value = "supervisoryOrganization", required = false)
				final SupervisoryOrganization supervisoryOrganization,
			@RequestParam(
					value = "correctionalStatus", required = false)
				final CorrectionalStatus correctionalStatus) {
		ModelAndView mav = new ModelAndView(
				LOCATION_TERMS_ACTION_MENU_VIEW_NAME);
		if (locationTerm != null) {
			mav.addObject(LOCATION_TERM_MODEL_KEY, locationTerm);
		} else {
			List<LocationTermChangeAction> changeActions
				= this.locationTermService.findChangeActions();
			mav.addObject(CHANGE_ACTIONS_MODEL_KEY, changeActions);
		}
		if (correctionalStatus != null
				&& correctionalStatus.getLocationRequired()) {
			if (location != null) {
				mav.addObject(LOCATION_MODEL_KEY, location);
			}
			if (supervisoryOrganization != null) {
				List<Location> supervisoryOrganizationLocations
					= this.locationTermService
						.findLocationsByOrganization(supervisoryOrganization);
				mav.addObject(SUPERVISORY_ORGANIZATION_LOCATIONS_MODEL_KEY,
					supervisoryOrganizationLocations);
			}
		}
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Shows action menu for reason terms.
	 * 
	 * @return action menu for reason terms
	 */
	@RequestMapping(
			value = "/reasonTermsActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showReasonTermsActionMenu() {
		return new ModelAndView(REASON_TERMS_ACTION_MENU_VIEW_NAME);
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/locationTermListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportLocationTermListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(LOCATION_TERM_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				LOCATION_TERM_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified location term.
	 * 
	 * @param locationTerm military service term
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/locationTermDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportLocationTermDetails(@RequestParam(
			value = "locationTerm", required = true)
			final LocationTerm locationTerm,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(LOCATION_TERM_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(locationTerm.getId()));
		byte[] doc = this.reportRunner.runReport(
				LOCATION_TERM_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Helper methods. */
	
	// Returns model and view to edit location term with locations limited
	// by correctional status
	private ModelAndView prepareEditMav(
			final Offender offender,
			final LocationTermForm locationTermForm,
			final List<Location> locations,
			final List<LocationReason> reasons) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(LOCATION_TERM_FORM_MODEL_KEY, locationTermForm);
		if (locationTermForm.getAllowLocation() != null
				&& locationTermForm.getAllowLocation()) {
			mav.addObject(LOCATIONS_MODEL_KEY, locations);
		}
		mav.addObject(REASONS_MODEL_KEY, reasons);
		if (locationTermForm.getReasonTermItems() != null) {
			mav.addObject(REASON_TERM_ITEM_INDEX_MODEL_KEY,
				locationTermForm.getReasonTermItems().size());
		}
		if (locationTermForm.getAllowState() != null
				&& locationTermForm.getAllowState()) {
			List<State> states = this.locationTermService.findHomeStates();
			mav.addObject(STATES_MODEL_KEY, states);
		}
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	// Returns model and view to redisplay location term
	private ModelAndView prepareRedisplayEditMav(final Offender offender,
			final List<Location> locations,
			final List<LocationReason> reasons,
			final LocationTermForm locationTermForm,
			final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(
					offender, locationTermForm, locations, reasons);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ LOCATION_TERM_FORM_MODEL_KEY, result);
		return mav;
	}
	
	// Adds offender summary to model and view
	private void addOffenderSummary(final ModelAndView mav,
			final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}

	// Creates date range from start date, start time, end date, end time
	// Throws IllegalArgumentException if a time is provided with no date
	private DateRange createDateRange(
			final Date startDate, final Date startTime,
			final Date endDate, final Date endTime) {
		final Date startDateTime;
		if (startDate != null) {
			startDateTime = DateManipulator.getDateAtTimeOfDay(
					startDate, startTime);
		} else if (startTime != null) {
			throw new IllegalArgumentException(
					"Start time not allowed without start date");
		} else {
			startDateTime = null;
		}
		final Date endDateTime;
		if (endDate != null) {
			endDateTime = DateManipulator.getDateAtTimeOfDay(endDate, endTime);
		} else if (endTime != null) {
			throw new IllegalArgumentException(
					"End time not allowed without end date");
		} else {
			endDateTime = null;
		}
		return new DateRange(startDateTime, endDateTime);
	}
	
	// Parses time - needed as time property editor does not work for request
	// params [SA, Dec 13, 2016]
	private Date parseTimeText(final String timeText) {
		if (timeText != null && !timeText.isEmpty()) {
			PropertyEditor propertyEditor = this.datePropertyEditorFactory
				.createCustomTimeOnlyEditor(true);
			propertyEditor.setAsText(timeText);
			return (Date) propertyEditor.getValue();
		} else {
			return null;
		}
	}
	
	/* Init binder. */
	
	/**
	 * Sets up init binder.
	 * 
	 * @param binder binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(LocationTerm.class,
				this.locationTermPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
				this.locationPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(LocationReason.class,
				this.locationReasonPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(LocationTermChangeAction.class,
				this.locationTermChangeActionPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(CorrectionalStatus.class,
				this.correctionalStatusPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(LocationReasonTerm.class,
				this.locationReasonTermPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Organization.class,
				this.organizationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "startDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "effectiveDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "startTime",
				this.datePropertyEditorFactory
					.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endTime",
				this.datePropertyEditorFactory
					.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "defaultStartDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "defaultEndDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "reasonTermItems.startDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "reasonTermItems.startTime",
				this.datePropertyEditorFactory
					.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "reasonTermItems.endDate",
				this.datePropertyEditorFactory
					.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "reasonTermItems.endTime",
				this.datePropertyEditorFactory
					.createCustomTimeOnlyEditor(true));
	}
}