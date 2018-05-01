package omis.hearing.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.Infraction;
import omis.hearing.report.HearingSummary;
import omis.hearing.report.ViolationSummary;
import omis.hearing.report.ViolationSummaryReportService;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;

/**
 * ViolationReportController.java
 * 
 *@author Annie Jacques 
 *@version 0.1.1 (Aug 3, 2017)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/hearing/violations/")
@PreAuthorize("hasRole('USER')")
public class ViolationReportController {
	
	/* View Names */
	
	private static final String LIST_VIEW_NAME = "/hearing/violations/list";

	private static final String VIOLATION_STATUS_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/violationStatusActionMenu";
	
	private static final String UNRESOLVED_VIOLATIONS_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/unresolvedViolationsActionMenu";
	
	private static final String SCHEDULED_VIOLATIONS_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/scheduledViolationsActionMenu";
	
	private static final String SCHEDULED_VIOLATIONS_ROW_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/scheduledViolationsRowActionMenu";

	private static final String RESOLVED_VIOLATIONS_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/resolvedViolationsActionMenu";

	private static final String RESOLVED_VIOLATIONS_ROW_ACTION_MENU_VIEW_NAME =
			"/hearing/violations/includes/resolvedViolationsRowActionMenu";
	
	/* Model Keys */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String HEARING_MODEL_KEY = "hearing";
	
	private static final String UNRESOLVED_VIOLATION_SUMMARIES_MODEL_KEY =
			"unresolvedViolationSummaries";
	
	private static final String RESOLVED_VIOLATION_SUMMARIES_MODEL_KEY =
			"resolvedViolationSummaries";
	
	private static final String SCHEDULED_HEARING_VIOLATION_SUMMARIES_MODEL_KEY =
			"scheduledHearingViolationSummaries";

	private static final String UNRESOLVED_DISCIPLINARY_VIOLATIONS_EXIST_MODEL_KEY =
			"unresolvedDisciplinaryViolationsExist";

	private static final String UNRESOLVED_CONDITION_VIOLATIONS_EXIST_MODEL_KEY =
			"unresolvedConditionViolationsExist";

	private static final String INFRACTION_MODEL_KEY = "infraction";
	
	/* Report Names */
	
	private static final String RESOLVED_VIOLATION_LISTING_REPORT_NAME =
			"/Compliance/Violations/Resolved_Violations_Listing";
	
	private static final String UNRESOLVED_VIOLATION_LISTING_REPORT_NAME =
			"/Compliance/Violations/Unresolved_Violations_Listing";
	
	private static final String VIOLATION_STATUS_REPORT_NAME =
			"/Compliance/Violations/Violation_Status_Report";
	
	private static final String VIOLATION_SUMMARY_OFFENDER_REPORT_NAME =
			"/Compliance/Violations/Violation_Summary_for_Offender_Distribution";	
	
	private static final String VIOLATIONS_SCHEDULED_FOR_HEARING_REPORT_NAME =
			"/Compliance/Violations/Violations_Scheduled_for_Hearing_Listing";
	
	private static final String HEARING_DETAILS_REPORT_NAME =
			"/Compliance/Hearings/Hearing_Details";
	
	private static final String RESOLVED_VIOLATION_DETAILS_REPORT_NAME =
			"/Compliance/Violations/Resolved_Violation_Details";
	
	/* Report Parameter Names */
	
	private static final String DOC_ID_REPORT_PARAM_NAME =
			"DOC_ID";
	
	private static final String HEARING_DETAILS_REPORT_PARAM_NAME =
			"HEARING_ID";
	
	private static final String RESOLVED_VIOLATION_DETAILS_REPORT_PARAM_NAME =
			"INFRACTION_ID";
	
	/* Service */
	
	@Autowired
	@Qualifier("violationSummaryReportService")
	private ViolationSummaryReportService violationSummaryReportService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("disciplinaryCodeViolationPropertyEditorFactory")
	private PropertyEditorFactory disciplinaryCodeViolationPropertyEditorFactory;

	@Autowired
	@Qualifier("conditionViolationPropertyEditorFactory")
	private PropertyEditorFactory conditionViolationPropertyEditorFactory;

	@Autowired
	@Qualifier("violationEventPropertyEditorFactory")
	private PropertyEditorFactory violationEventPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingPropertyEditorFactory")
	private PropertyEditorFactory hearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("infractionPropertyEditorFactory")
	private PropertyEditorFactory infractionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/**
	 * Default constructor for ViolationReportController
	 */
	public ViolationReportController() {
	}
	
	
	/**
	 * Returns a ModelAndView for the Violations list screen for specified Offender
	 * @param offender - Offender
	 * @return ModelAndView for the Violations list screen for specified Offender
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole ('VIOLATION_LIST')")
	public ModelAndView list(@RequestParam(value = "offender", required = true)
		final Offender offender){
		
		ModelMap map = new ModelMap();
		Boolean disciplinaryViolationsExist = false,
				conditionViolationsExist = false;
		List<ViolationSummary> unresolvedDisciplinarySummaries =
				this.violationSummaryReportService
				.findUnresolvedDisciplinaryViolationSummariesByOffender(offender);
		if(!unresolvedDisciplinarySummaries.isEmpty()) {
			disciplinaryViolationsExist = true;
		}
		List<ViolationSummary> unresolvedConditionSummaries =
				this.violationSummaryReportService
				.findUnresolvedConditionViolationSummariesByOffender(offender);
		if(!unresolvedConditionSummaries.isEmpty()) {
			conditionViolationsExist = true;
		}
		
		List<ViolationSummary> unresolvedSummaries =
				unresolvedDisciplinarySummaries;
		unresolvedSummaries.addAll(unresolvedConditionSummaries);
		
		List<ViolationSummary> resolvedSummaries =
				this.violationSummaryReportService
				.findResolvedViolationSummariesByOffender(offender);
		
		HashMap<HearingSummary, List<ViolationSummary>> scheduledSummaries =
				this.violationSummaryReportService
				.findScheduledViolationSummariesByOffender(offender);
		
		map.addAttribute(UNRESOLVED_VIOLATION_SUMMARIES_MODEL_KEY,
				unresolvedSummaries);
		map.addAttribute(RESOLVED_VIOLATION_SUMMARIES_MODEL_KEY,
				resolvedSummaries);
		map.addAttribute(SCHEDULED_HEARING_VIOLATION_SUMMARIES_MODEL_KEY,
				scheduledSummaries);
		map.addAttribute(UNRESOLVED_DISCIPLINARY_VIOLATIONS_EXIST_MODEL_KEY,
				disciplinaryViolationsExist);
		map.addAttribute(UNRESOLVED_CONDITION_VIOLATIONS_EXIST_MODEL_KEY,
				conditionViolationsExist);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	
	/* Action Menus Views */
	
	/**
	 * Returns the ModelAndView for the Action Menu for Violation Status
	 * @param offender - Offender
	 * @return ModelAndView for the Action Menu for Violation Status
	 */
	@RequestMapping(value = "/violationStatusActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayViolationStatusActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(VIOLATION_STATUS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns the ModelAndView for the Action Menu for Unresolved Violations
	 * @param offender - Offender
	 * @return ModelAndView for the Action Menu for Unresolved Violations
	 */
	@RequestMapping(value = "/unresolvedViolationsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayUnresolvedViolationsActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender,
			@RequestParam(value = "unresolvedDisciplinaryViolationsExist",
				required = true) final Boolean disciplinaryViolationsExist,
			@RequestParam(value = "unresolvedConditionViolationsExist",
				required = true) final Boolean conditionViolationsExist){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(UNRESOLVED_DISCIPLINARY_VIOLATIONS_EXIST_MODEL_KEY,
				disciplinaryViolationsExist);
		map.addAttribute(UNRESOLVED_CONDITION_VIOLATIONS_EXIST_MODEL_KEY,
				conditionViolationsExist);
		
		return new ModelAndView(UNRESOLVED_VIOLATIONS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns the ModelAndView for the Action Menu for Scheduled Violations
	 * @param offender - Offender
	 * @return ModelAndView for the Action Menu for Scheduled Violations
	 */
	@RequestMapping(value = "/scheduledViolationsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayScheduledViolationsActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SCHEDULED_VIOLATIONS_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Returns the ModelAndView for the Action Menu for scheduled Violations
	 * Rows
	 * @param hearing - Hearing
	 * @return ModelAndView for the Action Menu for scheduled Violations rows
	 */
	@RequestMapping(value = "/scheduledViolationsRowActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayScheduledViolationsRowActionMenu(@RequestParam
			(value = "hearing", required = true) final Hearing hearing){
		ModelMap map = new ModelMap();
		map.addAttribute(HEARING_MODEL_KEY, hearing);
		
		return new ModelAndView(SCHEDULED_VIOLATIONS_ROW_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Returns the ModelAndView for the Action Menu for Resolved Violations
	 * @param offender - Offender
	 * @return ModelAndView for the Action Menu for Resolved Violations
	 */
	@RequestMapping(value = "/resolvedViolationsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayResolvedViolationsActionMenu(@RequestParam
			(value = "offender", required = true) final Offender offender){
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(RESOLVED_VIOLATIONS_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Returns the ModelAndView for the Resolved Violations Row Action Menu
	 * @param infraction - Infraction associated with the current row
	 * @return ModelAndView - ModelAndView for the Resolved Violations Row
	 * Action Menu
	 */
	@RequestMapping(value = "/resolvedViolationsRowActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayResolvedViolationsRowActionMenu(
			@RequestParam(value = "infraction", required = true)
			final Infraction infraction) {
		ModelMap map = new ModelMap();
		map.addAttribute(INFRACTION_MODEL_KEY, infraction);
		
		return new ModelAndView(
				RESOLVED_VIOLATIONS_ROW_ACTION_MENU_VIEW_NAME, map);
	}

	/**
	 * Returns the report for the specified offenders resolved violations.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/resolvedViolationsListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportResolvedViolationListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				RESOLVED_VIOLATION_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the report for the specified offenders unresolved violations.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/unresolvedViolationsListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportUnresolvedViolationListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				UNRESOLVED_VIOLATION_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the report for the specified offenders violation statuses.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/violationStatusReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportViolationStatus(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VIOLATION_STATUS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the report for the specified offenders violation summary.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/violationSummaryOffenderReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportViolationSummary(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VIOLATION_SUMMARY_OFFENDER_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}

	/**
	 * Returns the report for the specified offenders violations scheduled for hearing.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/violationsScheduledHearingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportViolationsScheduledHearing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(DOC_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				VIOLATIONS_SCHEDULED_FOR_HEARING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified hearing.
	 * 
	 * @param hearing hearing
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/hearingDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportHearingDetails(@RequestParam(
			value = "hearing", required = true)
			final Hearing hearing,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(HEARING_DETAILS_REPORT_PARAM_NAME,
				Long.toString(hearing.getId()));
		byte[] doc = this.reportRunner.runReport(
				HEARING_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified resolved violation.
	 * 
	 * @param infraction infraction
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/resolvedViolationDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('VIOLATION_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportResolvedViolationDetails(@RequestParam(
			value = "infraction", required = true)
			final Infraction infraction,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(RESOLVED_VIOLATION_DETAILS_REPORT_PARAM_NAME,
				Long.toString(infraction.getId()));
		byte[] doc = this.reportRunner.runReport(
				RESOLVED_VIOLATION_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
		
	/* Helper Methods */
	
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder){
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				DisciplinaryCodeViolation.class,
				this.disciplinaryCodeViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ConditionViolation.class,
				this.conditionViolationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ViolationEvent.class,
				this.violationEventPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Hearing.class, this.hearingPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Infraction.class, this.infractionPropertyEditorFactory
				.createPropertyEditor());
	}
	
	
	
}
