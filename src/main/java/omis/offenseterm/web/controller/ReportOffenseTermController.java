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
package omis.offenseterm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.contact.web.controller.delegate.ContactSummaryModelDelegate;
import omis.courtcase.domain.CourtCase;
import omis.courtcase.report.CourtCaseSummary;
import omis.courtcase.report.CourtCaseSummaryReportService;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Controller to report offense terms.
 *
 * @author Stephen Abson
 * @author Sierra Rosales
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offenseTerm")
public class ReportOffenseTermController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "offenseTerm/list";
	
	/* Action menu views. */
	
	private static final String ACTION_MENU_VIEW_NAME
		= "offenseTerm/includes/offenseTermsActionMenu";
	
	/* Model keys. */
	
	private static final String PERSON_MODEL_KEY = "person";
	
	private static final String COURT_CASE_SUMMARIES_MODEL_KEY
		= "courtCaseSummaries";

	private static final String COURT_CASE_MODEL_KEY = "courtCase";
	
	/* Report services. */
	
	@Autowired
	@Qualifier("courtCaseSummaryReportService")
	private CourtCaseSummaryReportService courtCaseSummaryReportService;
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtCasePropertyEditorFactory")
	private PropertyEditorFactory courtCasePropertyEditorFactory;

	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;
	
	/* Report names. */
	
	private static final String COURT_CASE_LISTING_REPORT_NAME 
		= "/Legal/CourtCase/Court_Case_Listing";
	
	private static final String COURT_CASE_LISTING_REDACTED_REPORT_NAME 
	    = "/Legal/CourtCase/Court_Case_Listing_Redacted";	
	
	private static final String COURT_CASE_DETAIL_LISTING_REPORT_NAME 
	    = "/Legal/CourtCase/Detailed_Court_Case_Listing";	
 
	private static final String COURT_CASE_DETAIL_LISTING_REDACTED_REPORT_NAME 
        = "/Legal/CourtCase/Detailed_Court_Case_Listing_Redacted";	
	
	private static final String COURT_CASE_DETAILS_REPORT_NAME 
		= "/Legal/CourtCase/Court_Case_Details";
	
	private static final String REPORT_OF_VIOLATION_REPORT_NAME
	    = "/Legal/CourtCase/Report_of_Violation";

	private static final String REPORT_OF_VIOLATION_ID_REPORT_PARAM_NAME 
        = "COURT_CASE_ID";

	/* Report parameter names. */
	
	private static final String COURT_CASE_LISTING_ID_REPORT_PARAM_NAME 
		= "OFFENDER_ID";

	private static final String COURT_CASE_DETAILS_ID_REPORT_PARAM_NAME 
		= "COURT_CASE_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller to report offense terms. */
	public ReportOffenseTermController() {
		// Default instantiation
	}

	/* URL invokable methods. */
	
	/**
	 * Shows screen that lists
	 * 
	 * @param person person
	 * @return screen that lists offense terms
	 */
	@PreAuthorize("hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "person", required = true)
				final Person person) {
		List<CourtCaseSummary> courtCaseSummaries
			= this.courtCaseSummaryReportService
				.summarizeByPerson(person);
		OffenderSummary offenderSummary = this.offenderReportService
				.summarizeIfOffender(person);
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PERSON_MODEL_KEY, person);
		mav.addObject(COURT_CASE_SUMMARIES_MODEL_KEY, courtCaseSummaries);
		if (offenderSummary != null) {

			// TODO - Add report service method that returns offender from
			// person - SA
			this.offenderSummaryModelDelegate.add(mav.getModelMap(),
						(Offender) person);
		} else {
			this.contactSummaryModelDelegate.add(mav.getModelMap(), person);
		}
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for offense terms.
	 * 
	 * @param person person
	 * @param courtCase courtCase
	 * @return action menu for offense terms
	 */
	@RequestMapping(value = "/offenseTermsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "person", required = false)
				final Person person,
			@RequestParam(value = "courtCase", required = false)
				final CourtCase courtCase) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PERSON_MODEL_KEY, person);
		mav.addObject(COURT_CASE_MODEL_KEY, courtCase);
		return mav;
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offenders court cases.
	 * 
	 * @param person offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseListing(@RequestParam(
			value = "person", required = true)
			final Person person,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(person.getId()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the redacted report for the specified offenders court cases.
	 * 
	 * @param person offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseListingRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseListingRedacted(@RequestParam(
			value = "person", required = true)
			final Person person,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(person.getId()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_LISTING_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the detailed report for the specified offenders court cases.
	 * 
	 * @param person offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseDetailListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseDetailListing(@RequestParam(
			value = "person", required = true)
			final Person person,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(person.getId()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_DETAIL_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the redacted detailed report for the specified offenders court cases.
	 * 
	 * @param person offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseDetailListingRedactedReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseDetailListingRedacted(@RequestParam(
			value = "person", required = true)
			final Person person,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(person.getId()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_DETAIL_LISTING_REDACTED_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}	
	
	/**
	 * Returns the report for the specified court case.
	 * 
	 * @param courtCase court case
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseDetails(@RequestParam(
			value = "courtCase", required = true)
			final CourtCase courtCase,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(courtCase.getId()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report of violation form for the specified court case.
	 * 
	 * @param courtCase court case
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/reportOfViolationReport.rtf",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('OFFENSE_TERM_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportReportOfViolation(@RequestParam(
			value = "courtCase", required = true)
			final CourtCase courtCase,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(REPORT_OF_VIOLATION_ID_REPORT_PARAM_NAME,
				Long.toString(courtCase.getId()));
		byte[] doc = this.reportRunner.runReport(
				REPORT_OF_VIOLATION_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(CourtCase.class,
				this.courtCasePropertyEditorFactory.createPropertyEditor());
	}
}