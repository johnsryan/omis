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
package omis.chronologicalnote.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omis.beans.factory.PropertyEditorFactory;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.report.ChronologicalNoteReportService;
import omis.chronologicalnote.report.ChronologicalNoteSummary;
import omis.chronologicalnote.web.form.ChronologicalNoteFilterOptionsForm;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for chronological note report.
 * 
 * @author Yidong Li
 * @version 0.1.5 (Feb 1, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/chronologicalNote")
@PreAuthorize("hasRole('USER')")
public class ReportChronologicalNoteController {
	/* views */
	private static final String LIST_VIEW_NAME = "chronologicalNote/list";
	private static final String 
		CHRONOLOGICAL_NOTE_LIST_SCREEN_ACTION_MENU_VIEW_NAME
		= "chronologicalNote/includes/chronologicalNoteListScreenActionMenu";
	private static final String 
		CHRONOLOGICAL_NOTE_LIST_ROW_ACTION_MENU_VIEW_NAME
		= "chronologicalNote/includes/chronologicalNoteListRowActionMenu";
	
	/* model keys */
	private static final String CHRONOLOGICAL_NOTE_SUMMARIES_MODEL_KEY
		= "chronologicalNoteSummaries";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String NOTE_MODEL_KEY = "note";
	private static final String CATEGORIES_MODEL_KEY = "categories";
	private static final String CHRONOLOGICAL_NOTE_FILTER_OPTIONS_FORM_MODEL_KEY
		= "chronologicalNoteFilterOptionsForm";
		
	/* Message bundles. */
	
	/* Property editor. */
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("chronologicalNotePropertyEditorFactory")
	private PropertyEditorFactory chronologicalNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryPropertyEditorFactory")
	private PropertyEditorFactory
		chronologicalNoteCategoryPropertyEditorFactory;
		
	/* Services. */
	@Autowired
	@Qualifier("chronologicalNoteReportService")
	private ChronologicalNoteReportService chronologicalNoteReportService;
	
	/* Delegate */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Report names. */
	
	private static final String CHRONOLOGICAL_NOTE_LISTING_REPORT_NAME 
		= "/CaseManagement/Chronological_Notes/Chronological_Notes_Listing";
	private static final String CHRONOLOGICAL_NOTE_DETAILS_REPORT_NAME
		= "/CaseManagement/Chronological_Notes/Chronological_Note_Details";

	/* Report parameter names. */
	
	private static final String CHRONOLOGICAL_NOTE_LISTING_ID_REPORT_PARAM_NAME
		= "DOC_ID";
	private static final String CHRONOLOGICAL_NOTE_DETAILS_ID_REPORT_PARAM_NAME
		= "CHRONO_NOTE_ID";	
	
	/* Report runners. */
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	
	/* Constructor. */
	/** Instantiates a default chronological note report controller. */
	public ReportChronologicalNoteController() {
		// Default instantiation
	}
	
	/**
	 * Render a view of chronological note list screen.
	 * 
	 * @param offender offender
	 * @return view of chronological note list screen
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('CHRONOLOGICAL_NOTE_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender", required = true) 
		final Offender offender) {
		List<ChronologicalNoteSummary> chronologicalNoteSummaries
			= new ArrayList<ChronologicalNoteSummary>();	
		chronologicalNoteSummaries = this.chronologicalNoteReportService
			.findByOffender(offender);
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(CHRONOLOGICAL_NOTE_SUMMARIES_MODEL_KEY,
			chronologicalNoteSummaries);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		List<ChronologicalNoteCategory> categories
			= this.chronologicalNoteReportService.findCategories();
		mav.addObject(CATEGORIES_MODEL_KEY, categories);
		ChronologicalNoteFilterOptionsForm form
			= new ChronologicalNoteFilterOptionsForm();
		mav.addObject(CHRONOLOGICAL_NOTE_FILTER_OPTIONS_FORM_MODEL_KEY, form);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Submit chronological note list screen.
	 * 
	 * @param offender offender
	 * @param form chronological note filter options form
	 * @param result binding result
	 * @return view to the list screen
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('CHRONOLOGICAL_NOTE_LIST') or hasRole('ADMIN')")
	public ModelAndView submitList(@RequestParam(value = "offender",
		required = true) final Offender offender,
		final ChronologicalNoteFilterOptionsForm form,
		final BindingResult result) {
		List<ChronologicalNoteSummary> chronologicalNoteSummaries
			= new ArrayList<ChronologicalNoteSummary>();
		if (form.getCategories() == null) {
			chronologicalNoteSummaries.addAll(
				this.chronologicalNoteReportService.findByOffender(offender));
		} else {
			chronologicalNoteSummaries.addAll(
				this.chronologicalNoteReportService.findByOffenderAndCategories(
				offender, form.getCategories()));
		}
		ModelAndView mav = new ModelAndView(LIST_VIEW_NAME);
		mav.addObject(CHRONOLOGICAL_NOTE_SUMMARIES_MODEL_KEY,
			chronologicalNoteSummaries);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		List<ChronologicalNoteCategory> categoryOptions
			= this.chronologicalNoteReportService.findCategories();
		mav.addObject(CATEGORIES_MODEL_KEY, categoryOptions);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}
	
	/**
	 * Returns a view for chronological note list screen action menu pertaining
	 * to the specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for chronological note list screen action menu
	 */
	@RequestMapping(value = "/chronologicalNoteListScreenActionMenu.html",
		method = RequestMethod.GET)
	public ModelAndView chronologicalNoteListScreenActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
			CHRONOLOGICAL_NOTE_LIST_SCREEN_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view for chronological note list screen row action menu. 
	 *
	 * @param note note
	 * @return view for chronological note row action menu
	 */
	@RequestMapping(value = "/chronologicalNoteListRowActionMenu.html",
		method = RequestMethod.GET)
	public ModelAndView chronologicalNoteListRowActionMenu(@RequestParam(
		value = "note",	required = true) final ChronologicalNote note) {
		ModelMap map = new ModelMap();
		map.addAttribute(NOTE_MODEL_KEY, note);
		return new ModelAndView(
			CHRONOLOGICAL_NOTE_LIST_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns the report for the chronological notes.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/chronologicalNoteListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('CHRONOLOGICAL_NOTE_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportChronologicalNoteListing(@RequestParam(
		value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "reportFormat", required = true)
		final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(CHRONOLOGICAL_NOTE_LISTING_ID_REPORT_PARAM_NAME,
			Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
			CHRONOLOGICAL_NOTE_LISTING_REPORT_NAME,	reportParamMap,
			reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns a specific chronological note detail report.
	 * 
	 * @param note note
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/chronologicalNoteDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('CHRONOLOGICAL_NOTE_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportTrackedDocumentDetails(@RequestParam(
			value = "chronologicalNote", required = true)
			final ChronologicalNote note,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(CHRONOLOGICAL_NOTE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(note.getId()));
		byte[] doc = this.reportRunner.runReport(
				CHRONOLOGICAL_NOTE_DETAILS_REPORT_NAME,
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
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(ChronologicalNoteCategory.class,
			this.chronologicalNoteCategoryPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(ChronologicalNote.class,
			this.chronologicalNotePropertyEditorFactory.createPropertyEditor());
	}	
}