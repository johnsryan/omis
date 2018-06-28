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
package omis.caseload.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.caseload.service.OfficerCaseAssignmentService;
import omis.caseload.web.form.OfficerCaseAssignmentForm;
import omis.caseload.web.validator.OfficerCaseAssignmentFormValidator;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.user.domain.UserAccount;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing officer case assignments.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 14, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/caseload/officerCaseAssignment")
@PreAuthorize("hasRole('USER')")
public class ManageOfficerCaseAssignmentController {

	/* View names. */
	
	private static final String EDIT_VIEW_NAME = 
			"caseload/officerCaseAssignment/edit";
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"caseload/officerCaseAssignment/includes/officerCaseAssignmentActionMenu";
	
	/* Model keys. */
	
	private static final String OFFICER_CASE_ASSIGNMENT_FORM_MODEL_KEY =
			"officerCaseAssignmentForm";
	
	private static final String LOCATIONS_MODEL_KEY = "locations";
	
	private static final String SUPERVISION_LEVELS_MODEL_KEY = 
			"supervisionLevels";
	
	private static final String USER_ACCOUNT_MODEL_KEY = "userAccount";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String OFFICER_CASE_ASSIGNMENT_MODEL_KEY =
			"officerCaseAssignment";
	
	/* Redirects. */
	
	private static final String OFFENDER_REDIRECT_URL = 
			"redirect:/caseload/officerCaseAssignment/list.html?offender=%d";
	
	private static final String USER_ACCOUNT_REDIRECT_URL = 
			"redirect:/caseload/officerCaseAssignment/list.html?userAccount=%d";
	
	private static final String REDIRECT_URL = 
			"redirect:/caseload/officerCaseAssignment/list.html";
	
	/* Services. */
	
	@Autowired
	@Qualifier("officerCaseAssignmentService")
	private OfficerCaseAssignmentService officerCaseAssignmentService;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("officerCaseAssignmentFormValidator")
	private OfficerCaseAssignmentFormValidator 
			officerCaseAssignmentFormValidator;
	
	/* Helpers.	 */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	/* Message Keys */
	
	private static final String OFFICER_CASE_ASSIGNMENT_EXISTS_MESSAGE_KEY =
			"officerCaseAssignment.exists";
	
	private static final String 
			OFFICER_CASE_ASSIGNMENT_EXISTS_WITHIN_DATE_RANGE_MESSAGE_KEY =
			"officerCaseAssignment.existsWithinDateRange";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = "omis.caseload.msgs.form";
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("officerCaseAssignmentPropertyEditorFactory")
	private PropertyEditorFactory officerCaseAssignmentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("supervisionLevelCategoryPropertyEditorFactory")
	private PropertyEditorFactory supervisionLevelCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired	
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Constructors. */
	
	/** 
	 * Instantiates an implementation of manage officer case assignment 
	 * controller. 
	 */
	public ManageOfficerCaseAssignmentController() {
		// Default constructor.
	}
	
	/* Screens. */
	
	/**
	 * Shows a screen to create a officer case assignment.
	 * 
	 * @param userAccount user account
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFICER_CASE_ASSIGNMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		OfficerCaseAssignmentForm officerCaseAssignmentForm = 
				new OfficerCaseAssignmentForm();
		if (offender != null) {
			officerCaseAssignmentForm.setSelectedOffender(offender);
		}
		UserAccount officer = null;
		if (userAccount != null) {
			officer = userAccount;
		} else if (offender == null) {
			officer = this.retrieveUserAccount();
		}
		officerCaseAssignmentForm.setOfficer(officer);
		return this.prepareMav(officerCaseAssignmentForm, null, offender, 
				officer);
	}
	
	/**
	 * Shows a screen to edit an officer case assignment.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 * @param userAccount user account
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFICER_CASE_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "officerCaseAssignment", required = true)
					final OfficerCaseAssignment officerCaseAssignment,
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		OfficerCaseAssignmentForm officerCaseAssignmentForm = 
				new OfficerCaseAssignmentForm();
		officerCaseAssignmentForm.setSelectedOffender(
				officerCaseAssignment.getOffender());
		officerCaseAssignmentForm.setOfficer(
				officerCaseAssignment.getOfficer());
		officerCaseAssignmentForm.setLocation(
				officerCaseAssignment.getSupervisionOffice());
		officerCaseAssignmentForm.setSupervisionLevelCategory(
				officerCaseAssignment.getSupervisionLevel());
		officerCaseAssignmentForm.setStartDate(
				officerCaseAssignment.getDateRange().getStartDate());
		officerCaseAssignmentForm.setEndDate(
				officerCaseAssignment.getDateRange().getEndDate());
		return this.prepareMav(officerCaseAssignmentForm, 
				officerCaseAssignment, offender, userAccount);
	}
	
	
	/**
	 * Creates an officer case assignment.
	 * 
	 * @param userAccount user account
	 * @param offender offender
	 * @param officerCaseAssignmentForm officer case assignment form
	 * @param bindingResult binding result
	 * @return redirect to officer case assignment listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date conflict exists
	 */
	@PreAuthorize("hasRole('OFFICER_CASE_ASSIGNMENT_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender,
			final OfficerCaseAssignmentForm officerCaseAssignmentForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, DateConflictException {
		this.officerCaseAssignmentFormValidator.validate(
				officerCaseAssignmentForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(officerCaseAssignmentForm, 
					null, offender, userAccount, bindingResult);
			return mav;
		}
		
		this.officerCaseAssignmentService.createOfficerCaseAssignment(
				officerCaseAssignmentForm.getSelectedOffender(), 
				officerCaseAssignmentForm.getOfficer(), 
				officerCaseAssignmentForm.getStartDate(), 
				officerCaseAssignmentForm.getEndDate(), 
				officerCaseAssignmentForm.getLocation(), 
				officerCaseAssignmentForm.getSupervisionLevelCategory());
		
		ModelAndView mav = null;
		if (offender != null) {
			mav = new ModelAndView(String.format(OFFENDER_REDIRECT_URL, 
					offender.getId()));
		} else if (userAccount != null) {
			mav = new ModelAndView(String.format(USER_ACCOUNT_REDIRECT_URL, 
					userAccount.getId()));
		} else {
			mav = new ModelAndView(REDIRECT_URL);
		}
		return mav;
	}
	
	/**
	 * Updates a officer case assignment.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 * @param officerCaseAssignmentForm officer case assignment form
	 * @param bindingResult binding result
	 * @return redirect to officer case assignment listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date conflict exists
	 */
	@PreAuthorize("hasRole('OFFICER_CASE_ASSIGNMENT_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "officerCaseAssignment", required = true)
					final OfficerCaseAssignment officerCaseAssignment,
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender,
			final OfficerCaseAssignmentForm officerCaseAssignmentForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, DateConflictException {
		this.officerCaseAssignmentFormValidator.validate(
				officerCaseAssignmentForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(officerCaseAssignmentForm, 
					officerCaseAssignment, offender, userAccount, bindingResult);
			return mav;
		}
		
		this.officerCaseAssignmentService.updateOfficerCaseAssignment(
				officerCaseAssignment, 
				officerCaseAssignmentForm.getOfficer(), 
				officerCaseAssignmentForm.getStartDate(), 
				officerCaseAssignmentForm.getEndDate(), 
				officerCaseAssignmentForm.getLocation(), 
				officerCaseAssignmentForm.getSupervisionLevelCategory());
		
		ModelAndView mav = null;
		if (offender != null) {
			mav = new ModelAndView(String.format(OFFENDER_REDIRECT_URL, 
					offender.getId()));
		} else if (userAccount != null) {
			mav = new ModelAndView(String.format(USER_ACCOUNT_REDIRECT_URL, 
					userAccount.getId()));
		} else {
			mav = new ModelAndView(REDIRECT_URL);
		}
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for officer case assignments.
	 * 
	 * @param userAccount user account
	 * @param offender offender
	 * @return action menu
	 */
	@RequestMapping(value = "/officerCaseAssignmentActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(OFFENDER_MODEL_KEY, offender);
		modelMap.put(USER_ACCOUNT_MODEL_KEY, userAccount);
		return new ModelAndView(ACTION_MENU_VIEW_NAME, modelMap);
	}
	
	/* Helper Methods */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final OfficerCaseAssignmentForm officerCaseAssignmentForm,
			final OfficerCaseAssignment officerCaseAssignment,
			final Offender offender, final UserAccount userAccount) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(OFFICER_CASE_ASSIGNMENT_FORM_MODEL_KEY, 
				officerCaseAssignmentForm);
		mav.addObject(OFFICER_CASE_ASSIGNMENT_MODEL_KEY, officerCaseAssignment);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(USER_ACCOUNT_MODEL_KEY, userAccount);
		mav.addObject(LOCATIONS_MODEL_KEY, this.officerCaseAssignmentService
				.findSupervisoryOrganizationLocations());
		mav.addObject(SUPERVISION_LEVELS_MODEL_KEY, this
				.officerCaseAssignmentService.findSupervisionLevelCategories());
		if (offender != null) {
			this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		}
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final OfficerCaseAssignmentForm officerCaseAssignmentForm,
			final OfficerCaseAssignment officerCaseAssignment,
			final Offender offender, final UserAccount userAccount,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(officerCaseAssignmentForm, 
				officerCaseAssignment, offender, userAccount);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ OFFICER_CASE_ASSIGNMENT_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Retrieve current user
	private UserAccount retrieveUserAccount() {
		UserAccount userAccount = (UserAccount) RequestContextHolder
						.getRequestAttributes()
						.getAttribute(USER_ACCOUNT_MODEL_KEY,
						RequestAttributes.SCOPE_REQUEST);
		if (userAccount == null) {
			String username = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			userAccount = this.officerCaseAssignmentService
					.findUserAccountByUsername(username);
			RequestContextHolder.getRequestAttributes()
							.setAttribute(USER_ACCOUNT_MODEL_KEY, userAccount,
									RequestAttributes.SCOPE_REQUEST);
		}
		return userAccount;
	}
	
	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				OFFICER_CASE_ASSIGNMENT_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
					exception);
	}
	
	/**
	 * Handles date conflict exceptions.
	 * 
	 * @param exception date conflict exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DateConflictException.class)
	public ModelAndView handleDateConflictException(
			final DateConflictException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				OFFICER_CASE_ASSIGNMENT_EXISTS_WITHIN_DATE_RANGE_MESSAGE_KEY, 
				ERROR_BUNDLE_NAME, exception);
	}
	
	/* Init binder. */
	
	/**
	 * Sets up init binder.
	 * 
	 * @param binder init binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(OfficerCaseAssignment.class,
				this.officerCaseAssignmentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
				this.locationPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(UserAccount.class,
				this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(SupervisionLevelCategory.class,
				this.supervisionLevelCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
	}
}