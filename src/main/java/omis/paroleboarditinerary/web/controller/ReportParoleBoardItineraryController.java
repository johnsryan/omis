package omis.paroleboarditinerary.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.report.BoardAttendeeSummary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummaryReportService;
import omis.paroleboarditinerary.web.form.ParoleBoardItinerarySearchForm;
import omis.util.DateManipulator;

/**
 * Controller for displaying parole board itineraries.
 *
 * @author Josh Divine
 * @version 0.1.0 (Nov 29, 2017)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleBoardItinerary")
public class ReportParoleBoardItineraryController {

	/* View names. */
	
	private static final String VIEW_NAME = "paroleBoardItinerary/list";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/paroleBoardItinerariesActionMenu";

	/* Model keys. */
	
	private static final String PAROLE_BOARD_ITINERARY_SUMMARIES_MODEL_KEY = 
			"paroleBoardItinerarySummaries";
	
	private static final String PAROLE_BOARD_ITINERARY_MODEL_KEY = 
			"paroleBoardItinerary";
	
	private static final String PAROLE_BOARD_ITINERARY_SEARCH_FORM_MODEL_KEY = 
			"paroleBoardItinerarySearchForm";
	
	private static final String BOARD_ATTENDEE_SUMMARIES_MODEL_KEY = 
			"boardAttendeeSummaries";
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleBoardItineraryPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Report services. */
	
	@Autowired
	@Qualifier("paroleBoardItinerarySummaryReportService")
	private ParoleBoardItinerarySummaryReportService 
			paroleBoardItinerarySummaryReportService;
	
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
	}
}
