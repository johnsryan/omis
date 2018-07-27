package omis.paroleboarditinerary.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.report.BoardAttendeeSummary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummaryReportService;
import omis.paroleboarditinerary.web.form.ParoleBoardItinerarySearchForm;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.report.ParoleEligibilityReportService;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/**
 * Controller for displaying parole board itineraries.
 *
 * @author Josh Divine
 * @author Sierra Rosales
 * @version 0.1.2 (Jul 23, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleBoardItinerary")
public class ReportParoleBoardItineraryController {

	/* View names. */
	
	private static final String VIEW_NAME = "paroleBoardItinerary/list";
	
	private static final String HEARINGS_VIEW_NAME = 
			"paroleBoardItinerary/listHearings";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/paroleBoardItinerariesActionMenu";

	private static final String HEARINGS_ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/itineraryHearingsActionMenu";
	
	private static final String HEARINGS_ROW_ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/itineraryHearingsRowActionMenu";
	
	/* Model keys. */
	
	private static final String PAROLE_BOARD_ITINERARY_SUMMARIES_MODEL_KEY = 
			"paroleBoardItinerarySummaries";
	
	private static final String PAROLE_BOARD_ITINERARY_MODEL_KEY = 
			"paroleBoardItinerary";
	
	private static final String PAROLE_BOARD_ITINERARY_SEARCH_FORM_MODEL_KEY = 
			"paroleBoardItinerarySearchForm";
	
	private static final String BOARD_ATTENDEE_SUMMARIES_MODEL_KEY = 
			"boardAttendeeSummaries";
	
	private static final String SCHEDULED_HEARINGS_MODEL_KEY = 
			"scheduledHearings";
	
	private static final String UNRESOLVED_ELIGIBILITY_SUMMARIES_MODEL_KEY = 
			"unresolvedEligibilitySummaries";
	
	private static final String PAROLE_BOARD_ITINERARY_SUMMARY_MODEL_KEY = 
			"itinerarySummary";
	
	private static final String PAROLE_ELIGIBILITY_MODEL_KEY = "eligibility";
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = "hearingAnalysis";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	/* Report Names. */
	
	private static final String BOARD_ITINERARY_DETAILS_REPORT_NAME
		= "/BOPP/Parole_Board_Itinerary_Details";
	
	/* Report Parameters. */
	
	private static final String BOARD_ITINERARY_DETAILS_ID_REPORT_PARAM_NAME
		= "ITINERARY_ID";	
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleBoardItineraryPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityPropertyEditorFactory;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("paroleBoardItinerarySummaryReportService")
	private ParoleBoardItinerarySummaryReportService 
			paroleBoardItinerarySummaryReportService;
	
	@Autowired
	@Qualifier("paroleEligibilityReportService")
	private ParoleEligibilityReportService paroleEligibilityReportService;
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for parole board itineraries. */
	public ReportParoleBoardItineraryController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen that lists parole board itineraries
	 * 
	 * @return screen that lists parole board itineraries
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "date", required = false)
				final Date date,
			@RequestParam(value = "startDate", required = false)
				final Date startDate,
			@RequestParam(value = "endDate", required = false)
				final Date endDate) {
		List<ParoleBoardItinerary> paroleBoardItineraries;
		ParoleBoardItinerarySearchForm form = 
				new ParoleBoardItinerarySearchForm();
		if (date != null) {
			form.setDate(date);
			form.setSingleDate(false);
			paroleBoardItineraries = this
					.paroleBoardItinerarySummaryReportService
					.findParoleBoardItineraryByEffectiveDate(date);
		} else if (startDate != null || endDate != null) {
			form.setStartDate(startDate);
			form.setEndDate(endDate);
			form.setSingleDate(false);
			paroleBoardItineraries = this
					.paroleBoardItinerarySummaryReportService
					.findParoleBoardItineraryByDateRange(null, null);
		} else {
			form.setSingleDate(false);
			paroleBoardItineraries = this
					.paroleBoardItinerarySummaryReportService
					.findParoleBoardItineraryByDateRange(null, null);
		}
		return prepareModelAndView(form, paroleBoardItineraries);
	}

	/**
	 * Updates the screen that lists parole board itineraries.
	 * 
	 * @param form parole board itinerary search form
	 * @param result binding result
	 * @return model and view for parole board itineraries list screen
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_LIST') or hasRole('ADMIN')")
	public ModelAndView updateList(final ParoleBoardItinerarySearchForm form,
				final BindingResult result) {
		List<ParoleBoardItinerary> paroleBoardItineraries;
		if (form.getSingleDate()) {
			paroleBoardItineraries = this
					.paroleBoardItinerarySummaryReportService
					.findParoleBoardItineraryByEffectiveDate(form.getDate());
			form.setStartDate(null);
			form.setEndDate(null);
		} else {
			paroleBoardItineraries = this
					.paroleBoardItinerarySummaryReportService
					.findParoleBoardItineraryByDateRange(form.getStartDate(), 
							form.getEndDate());
			form.setDate(null);
		}
		return prepareModelAndView(form, paroleBoardItineraries);
	}
	
	/**
	 * Shows screen that lists scheduled hearings and unresolved eligibilities.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @return screen that lists scheduled hearings and unresolved eligibilities
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/listHearings.html", method = RequestMethod.GET)
	public ModelAndView listHearings(
			@RequestParam(value = "paroleBoardItinerary", required = true)
				final ParoleBoardItinerary paroleBoardItinerary) {
		ModelAndView mav = new ModelAndView(HEARINGS_VIEW_NAME);
		mav.addObject(SCHEDULED_HEARINGS_MODEL_KEY, 
				this.paroleEligibilityReportService.findByItinerary(
						paroleBoardItinerary));
		mav.addObject(UNRESOLVED_ELIGIBILITY_SUMMARIES_MODEL_KEY, 
				this.paroleEligibilityReportService
				.findUnresolvedEligibilitySummariesByDateRange(
						DateRange.getStartDate(
								paroleBoardItinerary.getDateRange()), 
						DateRange.getEndDate(
								paroleBoardItinerary.getDateRange())));
		mav.addObject(PAROLE_BOARD_ITINERARY_SUMMARY_MODEL_KEY, 
				this.paroleBoardItinerarySummaryReportService.summarize(
						paroleBoardItinerary));
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for parole board itineraries.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @return action menu for parole board itineraries
	 */
	@RequestMapping(value = "/paroleBoardItinerariesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "paroleBoardItinerary", required = false)
				final ParoleBoardItinerary paroleBoardItinerary) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_BOARD_ITINERARY_MODEL_KEY, 
				paroleBoardItinerary);
		return mav;
	}
	
	/**
	 * Shows action menu for scheduled hearings.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @return action menu for parole board itineraries
	 */
	@RequestMapping(value = "/itineraryHearingsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showItineraryHearingsActionMenu(
			@RequestParam(value = "paroleBoardItinerary", required = true)
				final ParoleBoardItinerary paroleBoardItinerary) {
		ModelAndView mav = new ModelAndView(HEARINGS_ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_BOARD_ITINERARY_MODEL_KEY, 
				paroleBoardItinerary);
		return mav;
	}
	
	/**
	 * Shows action menu for scheduled hearings.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @param eligibility parole eligibility
	 * @return action menu for parole board itineraries
	 */
	@RequestMapping(value = "/itineraryHearingsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showItineraryHearingsRowActionMenu(
			@RequestParam(value = "paroleBoardItinerary", required = true)
				final ParoleBoardItinerary paroleBoardItinerary,
			@RequestParam(value = "eligibility", required = true)
				final ParoleEligibility eligibility) {
		ModelAndView mav = new ModelAndView(HEARINGS_ROW_ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_BOARD_ITINERARY_MODEL_KEY, paroleBoardItinerary);
		mav.addObject(PAROLE_ELIGIBILITY_MODEL_KEY, eligibility);
		mav.addObject(HEARING_ANALYSIS_MODEL_KEY, this
				.paroleEligibilityReportService
				.findHearingAnalysisByParoleEligibility(eligibility));
		mav.addObject(BOARD_HEARING_MODEL_KEY, this
				.paroleEligibilityReportService
				.findBoardHearingByParoleEligibility(eligibility));
		return mav;
	}
	
	/**
	 * Returns the report for the specified board itinerary.
	 * 
	 * @param ParoleBoardItinerary paroleBoardItinerary
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/boardItineraryDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_LIST') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportBoardItineraryDetails(@RequestParam(
			value = "paroleBoardItinerary", required = true)
			final ParoleBoardItinerary paroleBoardItinerary,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(BOARD_ITINERARY_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(paroleBoardItinerary.getId()));
		byte[] doc = this.reportRunner.runReport(
				BOARD_ITINERARY_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/* Helper methods. */
	
	// Adds itinerary and board attendee summaries
	private ModelAndView prepareModelAndView(
			final ParoleBoardItinerarySearchForm form,
			final List<ParoleBoardItinerary> paroleBoardItinerarys) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_BOARD_ITINERARY_SEARCH_FORM_MODEL_KEY, form);
		List<ParoleBoardItinerarySummary> itinerarySummaries = 
				new ArrayList<ParoleBoardItinerarySummary>();
		Map<Long, List<BoardAttendeeSummary>> attendeeSummaries = 
				new HashMap<Long, List<BoardAttendeeSummary>>();
		for (ParoleBoardItinerary boardItinerary : paroleBoardItinerarys) {
			ParoleBoardItinerarySummary itinerarySummary = 
					this.paroleBoardItinerarySummaryReportService.summarize(
							boardItinerary);
			itinerarySummaries.add(itinerarySummary);
			attendeeSummaries.put(itinerarySummary.getId(), 
					this.paroleBoardItinerarySummaryReportService
					.findParoleBoardAttendeeSummariesByParoleBoardItinerary(
							boardItinerary));
		}
		mav.addObject(PAROLE_BOARD_ITINERARY_SUMMARIES_MODEL_KEY, 
				itinerarySummaries);
		mav.addObject(BOARD_ATTENDEE_SUMMARIES_MODEL_KEY, attendeeSummaries);
		return mav;
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(ParoleBoardItinerary.class, 
				this.paroleBoardItineraryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(ParoleEligibility.class, 
				this.paroleEligibilityPropertyEditorFactory
				.createPropertyEditor());
	}
}