package omis.courtcase.web.controller;

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
import omis.courtcase.domain.CourtCase;
import omis.courtcase.report.CourtCaseSummary;
import omis.courtcase.report.CourtCaseSummaryReportService;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Report controller for court case.
 * 
 * @author Joel Norris
 * @author Ryan Johns
 * @author Sierra Rosales
 * @version 0.1.1 (Apr 18, 2016)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/courtCase")
@PreAuthorize("hasRole('USER')")
public class ReportCourtCaseController {

	/* View names. */
	
	private static final String LIST_VIEW_NAME = "courtCase/list";
	
	/* Model names. */
	
	private static final String COURT_CASE_SUMMARIES_MODEL_NAME = 
			"courtCaseSummaries";
	
	private static final String DEFENDANT_MODEL_KEY = "defendant";
	
	/* Report services. */
	
	@Autowired
	@Qualifier("courtCaseSummaryReportService")
	private CourtCaseSummaryReportService courtCaseSummaryReportService;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtCasePropertyEditorFactory")
	private PropertyEditorFactory courtCasePropertyEditorFactory;

	/* Helpers. */
	@Autowired 
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
/* Report names. */
	
	private static final String COURT_CASE_LISTING_REPORT_NAME 
		= "/Legal/CourtCase/Court_Case_Listing";
	
	private static final String COURT_CASE_DETAIL_LISTING_REPORT_NAME 
	= "/Legal/CourtCase/Detailed_Court_Case_Listing";

	private static final String COURT_CASE_DETAILS_REPORT_NAME 
		= "/Legal/CourtCase/Court_Case_Details";
	
	private static final String REPORT_OF_VIOLATION_REPORT_NAME
	    = "/Legal/CourtCase/Report_of_Violation";

	/* Report parameter names. */
	
	private static final String COURT_CASE_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String COURT_CASE_DETAILS_ID_REPORT_PARAM_NAME 
		= "COURT_CASE_ID";
	
	private static final String REPORT_OF_VIOLATION_ID_REPORT_PARAM_NAME 
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
	
	/**
	 * Instantiates a default instance of report court case controller.
	 */
	public ReportCourtCaseController() {
		//Default constructor.
	}
	
	/* URL invokable methods. */
		
	/**
	 * Shows a listing screen of court case summary objects that show an
	 * overview of pertinent information pertaining to a court case including
	 * the specified defendant.
	 * 
	 * @param defendant defendant
	 * @return model and view to present a list of court case summaries
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('COURT_CASE_LIST')")
	public ModelAndView list(@RequestParam(value = "defendant", required = true)
			final Person defendant) {
		ModelMap map = new ModelMap();
		List<CourtCaseSummary> courtCaseSummaries = 
				this.courtCaseSummaryReportService.summarizeByPerson(defendant);
		map.addAttribute(COURT_CASE_SUMMARIES_MODEL_NAME, courtCaseSummaries);
		this.offenderSummaryModelDelegate.add(map, (Offender) defendant);
		map.addAttribute(DEFENDANT_MODEL_KEY, defendant);
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
/* Reports. */
	
	/**
	 * Returns the report for the specified offenders court cases.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('COURT_CASE_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the detailed report for the specified offenders court cases.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/courtCaseDetailListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('COURT_CASE_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportCourtCaseDetailListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(COURT_CASE_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				COURT_CASE_DETAIL_LISTING_REPORT_NAME,
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
	@PreAuthorize("hasRole('COURT_CASE_VIEW') or hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('COURT_CASE_VIEW') or hasRole('ADMIN')")
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
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Person.class,
				this.personPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(CourtCase.class,
				this.courtCasePropertyEditorFactory.createPropertyEditor());
	}
}