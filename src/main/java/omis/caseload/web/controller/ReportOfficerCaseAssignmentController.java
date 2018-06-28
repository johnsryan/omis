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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.caseload.domain.OfficerCaseAssignment;
import omis.caseload.report.OfficerCaseAssignmentSummaryReportService;
import omis.caseload.service.OfficerCaseAssignmentService;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.user.domain.UserAccount;

/**
 * Controller for reporting officer case assignments.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 19, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/caseload/officerCaseAssignment")
@PreAuthorize("hasRole('USER')")
public class ReportOfficerCaseAssignmentController {

	/* View names. */
	
	private static final String VIEW_NAME = 
			"caseload/officerCaseAssignment/list";
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"caseload/officerCaseAssignment/includes/"
			+ "officerCaseAssignmentsActionMenu";
	
	/* Model keys. */
	
	private static final String OFFICER_CASE_ASSIGNMENT_SUMMARIES_MODEL_KEY = 
			"officerCaseAssignmentSummaries";
	
	private static final String USER_ACCOUNT_MODEL_KEY = "userAccount";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String OFFICER_CASE_ASSIGNMENT_MODEL_KEY =
			"officerCaseAssignment";

	/* Services. */
	
	@Autowired
	@Qualifier("officerCaseAssignmentSummaryReportService")
	private OfficerCaseAssignmentSummaryReportService 
			officerCaseAssignmentSummaryReportService;
	
	@Autowired
	@Qualifier("officerCaseAssignmentService")
	private OfficerCaseAssignmentService officerCaseAssignmentService;
	
	/* Helpers.	 */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
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
	
	/* Constructors. */
	
	/** 
	 * Instantiates an implementation of report officer case assignment 
	 * controller. 
	 */
	public ReportOfficerCaseAssignmentController() {
		// Default constructor.
	}
	
	/* Screens. */
	
	/**
	 * Shows a screen to list officer case assignments.
	 * 
	 * @param userAccount user account
	 * @param offender offender
	 * @return model and view
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFICER_CASE_ASSIGNMENT_LIST')")
	public ModelAndView list(
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		if (offender != null) {
			mav.addObject(OFFICER_CASE_ASSIGNMENT_SUMMARIES_MODEL_KEY, 
					this.officerCaseAssignmentSummaryReportService
					.findByOffender(offender));
			mav.addObject(OFFENDER_MODEL_KEY, offender);
			this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		} else if (userAccount != null) {
			mav.addObject(OFFICER_CASE_ASSIGNMENT_SUMMARIES_MODEL_KEY, 
					this.officerCaseAssignmentSummaryReportService
					.findByUser(userAccount));
			mav.addObject(USER_ACCOUNT_MODEL_KEY, userAccount);
			
		} else {
			UserAccount user = this.retrieveUserAccount();
			mav.addObject(OFFICER_CASE_ASSIGNMENT_SUMMARIES_MODEL_KEY, 
					this.officerCaseAssignmentSummaryReportService
					.findByUser(user));
			mav.addObject(USER_ACCOUNT_MODEL_KEY, user);
		}
		return mav;
	}

	/**
	 * Action menu for officer case assignments.
	 * 
	 * @param officerCaseAssignment officer case assignment
	 * @param userAccount user account
	 * @param offender offender
	 * @return action menu
	 */
	@RequestMapping(value = "/officerCaseAssignmentsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView officerCaseAssignmentsActionMenu(
			@RequestParam(value = "officerCaseAssignment", required = false)
			final OfficerCaseAssignment officerCaseAssignment,
			@RequestParam(value = "userAccount", required = false)
					final UserAccount userAccount,
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(USER_ACCOUNT_MODEL_KEY, userAccount);
		mav.addObject(OFFICER_CASE_ASSIGNMENT_MODEL_KEY, officerCaseAssignment);
		return mav;
	}
	
	/* Helper Methods */
	
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
		binder.registerCustomEditor(UserAccount.class,
				this.userAccountPropertyEditorFactory.createPropertyEditor());
	}
}