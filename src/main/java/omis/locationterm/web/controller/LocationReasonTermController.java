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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.service.LocationReasonTermService;
import omis.locationterm.web.form.LocationReasonTermForm;
import omis.locationterm.web.validator.LocationReasonTermFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderMismatchException;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for location reason terms.
 * 
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 16, 2014)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/locationTerm/locationReasonTerm")
@PreAuthorize("hasRole('USER')")
public class LocationReasonTermController {

	/* View names. */
	
	private static final String LIST_VIEW_NAME
		= "locationTerm/locationReasonTerm/list";

	private static final String EDIT_VIEW_NAME
		= "locationTerm/locationReasonTerm/edit";
	
	/* Action menu view names. */
	
	private static final String LOCATION_REASON_TERM_ACTION_MENU_VIEW_NAME
		= "locationTerm/locationReasonTerm/includes"
				+ "/locationReasonTermActionMenu";

	private static final String LOCATION_REASON_TERMS_ACTION_MENU_VIEW_NAME
		= "locationTerm/locationReasonTerm/includes"
				+ "/locationReasonTermsActionMenu";

	/* Redirects. */
	
	private static final String LIST_REDIRECT
		= "redirect:/locationTerm/locationReasonTerm/list.html?offender=%d";
	
	/* Model keys. */
	
	private static final String LOCATION_REASON_TERMS_MODEL_KEY
		= "locationReasonTerms";

	private static final String LOCATION_REASON_TERM_FORM_MODEL_KEY
		= "locationReasonTermForm";
	
	private static final String LOCATION_REASON_TERM_MODEL_KEY
		= "locationReasonTerm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String LOCATION_REASONS_MODEL_KEY = "reasons";

	private static final String LOCATION_TERMS_MODEL_KEY = "locationTerms";

	/* Message bundles. */
	
	private static final String ERROR_BUNDLE_NAME
		= "omis.locationterm.msgs.form";

	/* Message keys. */
	
	private static final String LOCATION_REASON_TERM_EXISTS_MESSAGE_KEY
		= "locationReasonTerm.exists";

	private static final String OFFENDER_MISMATCH_MESSAGE_KEY
		= "locationReasonTerm.offenderMismatch";

	private static final String LOCATION_REASON_TERM_CONFLICT_MESSAGE_KEY 
		= "locationReasonTerm.conflict";

	private static final String LOCATION_REASON_TERM_OUT_OF_BOUNDS_MESSAGE_KEY
		= "locationReasonTerm.dateRangeOutOfLocationTermDateRangeBounds";
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationReasonTermService")
	private LocationReasonTermService locationReasonTermService;
	
	/* Report services. */
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationTermPropertyEditorFactory")
	private PropertyEditorFactory locationTermPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationReasonTermPropertyEditorFactory")
	private PropertyEditorFactory locationReasonTermPropertyEditorFactory;

	@Autowired
	@Qualifier("locationReasonPropertyEditorFactory")
	private PropertyEditorFactory locationReasonPropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("locationReasonTermFormValidator")
	private LocationReasonTermFormValidator locationReasonTermFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructor. */
	
	/** Instantiates a controller for location reason terms. */
	public LocationReasonTermController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows list of location reason terms by offender.
	 * 
	 * @param offender offender
	 * @return screen listing location reason terms by offender
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_REASON_TERM_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		List<LocationReasonTerm> locationReasonTerms
			= this.locationReasonTermService.findByOffender(offender);
		mav.addObject(LOCATION_REASON_TERMS_MODEL_KEY, locationReasonTerms);
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	/**
	 * Shows screen to create new location reason term for offender.
	 * 
	 * @param offender offender
	 * @return screen to create new location reason term for offender
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_REASON_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		return this.prepareEditMav(offender, new LocationReasonTermForm());
	}
	
	/**
	 * Shows screen to update existing location reason term.
	 * 
	 * @param locationReasonTerm location reason term to update
	 * @return screen to update existing location reason term
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_REASON_TERM_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "locationReasonTerm", required = true)
				final LocationReasonTerm locationReasonTerm) {
		LocationReasonTermForm locationReasonTermForm
			= new LocationReasonTermForm();
		locationReasonTermForm.setLocationTerm(
				locationReasonTerm.getLocationTerm());
		locationReasonTermForm.setReason(locationReasonTerm.getReason());
		if (locationReasonTerm.getDateRange() != null) {
			locationReasonTermForm.setStartDate(
					locationReasonTerm.getDateRange().getStartDate());
			locationReasonTermForm.setStartTime(
					locationReasonTerm.getDateRange().getStartDate());
			locationReasonTermForm.setEndDate(
					locationReasonTerm.getDateRange().getEndDate());
			locationReasonTermForm.setEndTime(
					locationReasonTerm.getDateRange().getEndDate());
		}
		ModelAndView mav = this.prepareEditMav(locationReasonTerm.getOffender(),
				locationReasonTermForm);
		mav.addObject(LOCATION_REASON_TERM_MODEL_KEY, locationReasonTerm);
		return mav;
	} 
	
	/**
	 * Saves a new location reason term for offender.
	 * 
	 * @param offender offender
	 * @param locationReasonTermForm form for location reason term
	 * @param result binding result
	 * @return redirect to screen listing location reason terms by offender
	 * @throws LocationReasonTermExistsException if the location reason term exists
	 * @throws OffenderMismatchException if the offender and the offender of
	 * the location term are not the same
	 * @throws LocationReasonTermConflictException if conflicting location
	 * reason terms exist
	 * @throws DateRangeOutOfBoundsException if the reason term is outside of
	 * the bounds of the location term
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('LOCATION_REASON_TERM_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			final LocationReasonTermForm locationReasonTermForm,
			final BindingResult result)
					throws LocationReasonTermExistsException,
						OffenderMismatchException,
						LocationReasonTermConflictException,
						DateRangeOutOfBoundsException {
		this.locationReasonTermFormValidator
			.validate(locationReasonTermForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayEditMav(offender,
					locationReasonTermForm, result);
		}
		this.locationReasonTermService.create(
				offender, locationReasonTermForm.getLocationTerm(),
				locationReasonTermForm.getReason(),
				this.createDateRange(
						locationReasonTermForm.getStartDate(),
						locationReasonTermForm.getStartTime(),
						locationReasonTermForm.getEndDate(),
						locationReasonTermForm.getEndTime()));
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Updates an existing location reason term.
	 * 
	 * @param locationReasonTerm location reason term
	 * @param locationReasonTermForm form for location reason term
	 * @param result binding result
	 * @return redirect to screen listing location reason terms by offender
	 * @throws LocationReasonTermExistsException if location reason term exists
	 * @throws OffenderMismatchException if the offender of the location reason
	 * term and location term are not the same
	 * @throws LocationReasonTermConflictException if conflicting location
	 * reason terms exist
	 * @throws DateRangeOutOfBoundsException if the reason term is outside of
	 * the bounds of the location term 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('LOCATION_REASON_TERM_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "locationReasonTerm", required = true)
				final LocationReasonTerm locationReasonTerm,
			final LocationReasonTermForm locationReasonTermForm,
			final BindingResult result)
					throws LocationReasonTermExistsException,
						OffenderMismatchException,
						LocationReasonTermConflictException,
						DateRangeOutOfBoundsException {
		this.locationReasonTermFormValidator
			.validate(locationReasonTermForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = prepareRedisplayEditMav(
					locationReasonTerm.getOffender(),
					locationReasonTermForm, result);
			mav.addObject(LOCATION_REASON_TERM_MODEL_KEY, locationReasonTerm);
			return mav;
		}
		this.locationReasonTermService.update(
				locationReasonTerm,
				locationReasonTerm.getLocationTerm(),
				locationReasonTermForm.getReason(),
				this.createDateRange(
						locationReasonTermForm.getStartDate(),
						locationReasonTermForm.getStartTime(),
						locationReasonTermForm.getEndDate(),
						locationReasonTermForm.getEndTime()));
		return new ModelAndView(String.format(LIST_REDIRECT,
				locationReasonTerm.getOffender().getId()));
	}
	
	/**
	 * Removes location reason term.
	 * 
	 * @param locationReasonTerm location reason term to remove
	 * @return redirect to screen listing location reason terms by offender
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('LOCATION_REASON_TERM_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "locationReasonTerm", required = true)
				final LocationReasonTerm locationReasonTerm) {
		Offender offender = locationReasonTerm.getOffender();
		this.locationReasonTermService.remove(locationReasonTerm);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/* Exception handlers. */
	
	/**
	 * Handles {@code LocationReasonTermExistsException}.
	 * 
	 * @param LocationReasonTermExistsException exception thrown
	 * @return screen to handle {@code LocationReasonTermExistsException}
	 */
	@ExceptionHandler(LocationReasonTermExistsException.class)
	public ModelAndView handleLocationReasonTermExistsException(
			final LocationReasonTermExistsException locationReasonTermExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				LOCATION_REASON_TERM_EXISTS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, locationReasonTermExistsException);
	}
	
	/**
	 * Handles {@code OffenderMismatchException}.
	 * 
	 * @param offenderMismatchException exception thrown
	 * @return screen to handle {@code OffenderMismatchException}
	 */
	@ExceptionHandler(OffenderMismatchException.class)
	public ModelAndView handleOffenderMismatchException(
			final OffenderMismatchException offenderMismatchException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				OFFENDER_MISMATCH_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, offenderMismatchException);
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
				LOCATION_REASON_TERM_CONFLICT_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				locationReasonTermConflictException);
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
				LOCATION_REASON_TERM_OUT_OF_BOUNDS_MESSAGE_KEY,
				ERROR_BUNDLE_NAME, dateRangeOutOfBoundsException);
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for location reason term.
	 * 
	 * @param offender offender
	 * @return action menu for location reason term
	 */
	@RequestMapping(
			value = "/locationReasonTermActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showLocationReasonTermActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(
				LOCATION_REASON_TERM_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Shows action menu for location reason terms.
	 * 
	 * @param offender offender
	 * @param locationReasonTerm location reason term
	 * @return action menu for location reason terms
	 */
	@RequestMapping(
			value = "/locationReasonTermsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showLocationReasonTermsActionmenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "locationReasonTerm", required = false)
				final LocationReasonTerm locationReasonTerm) {
		ModelAndView mav = new ModelAndView(
				LOCATION_REASON_TERMS_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(LOCATION_REASON_TERM_MODEL_KEY, locationReasonTerm);
		return mav;
	}
	
	/* Helper methods. */
	
	// Prepares edit model and view
	private ModelAndView prepareEditMav(
			final Offender offender,
			final LocationReasonTermForm locationReasonTermForm) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(LOCATION_REASON_TERM_FORM_MODEL_KEY,
				locationReasonTermForm);
		List<LocationReason> locationReasons
			= this.locationReasonTermService.findLocationReasons();
		mav.addObject(LOCATION_REASONS_MODEL_KEY, locationReasons);
		List<LocationTerm> locationTerms
			= this.locationReasonTermService.findLocationTerms(offender);
		mav.addObject(LOCATION_TERMS_MODEL_KEY, locationTerms);
		addOffenderSummary(mav, offender);
		return mav;
	}
	
	// Adds offender summary to model and view
	private void addOffenderSummary(final ModelAndView mav,
			final Offender offender) {
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
	}
	
	// Prepares redisplay edit model and view
	private ModelAndView prepareRedisplayEditMav(final Offender offender,
			final LocationReasonTermForm locationReasonTermForm,
			final BindingResult result) {
		ModelAndView mav
			= this.prepareEditMav(offender, locationReasonTermForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ LOCATION_REASON_TERM_FORM_MODEL_KEY, result);
		return mav;
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
				this.locationTermPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(LocationReasonTerm.class,
				this.locationReasonTermPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(LocationReason.class,
				this.locationReasonPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "startDate",
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
	}
}