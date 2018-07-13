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
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.sentence.domain.Sentence;
import omis.sentence.report.SentenceReportService;
import omis.sentence.report.SentenceSummary;

/**
 * Controller to report current offense terms.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/offenseTerm")
public class ReportCurrentOffenseTermController {

	/* View names. */
	
	private static final String VIEW_NAME = "offenseTerm/listCurrentOffenses";
	
	/* Action menu views. */
	
	private static final String ACTION_MENU_VIEW_NAME
		= "offenseTerm/includes/currentOffenseTermsActionMenu";
	
	/* Model keys. */
	
	private static final String PERSON_MODEL_KEY = "person";
	
	private static final String SENTENCE_MODEL_KEY = "sentence";
	
	private static final String SENTENCE_SUMMARIES_MODEL_KEY 
		= "sentenceSummaries";

	/* Report services. */
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	@Autowired
	@Qualifier("sentenceReportService")
	private SentenceReportService sentenceReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("sentencePropertyEditorFactory")
	private PropertyEditorFactory sentencePropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;

	/* Report names. */
	
	private static final String COURT_CASE_LISTING_REPORT_NAME 
		= "/Legal/CurrentOffenses/Current_Offenses_Listing";

	/* Report parameter names. */
	
	private static final String COURT_CASE_LISTING_ID_REPORT_PARAM_NAME 
		= "OFFENDER_ID";

	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller to report current offense terms. */
	public ReportCurrentOffenseTermController() {
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
	@RequestMapping(value = "/listCurrentOffenses.html", 
					method = RequestMethod.GET)
	public ModelAndView listCurrentOffenses(
			@RequestParam(value = "person", required = true)
				final Person person) {
		List<SentenceSummary> sentenceSummaries = this.sentenceReportService
				.summarizeActiveSentences(person);
		OffenderSummary offenderSummary = this.offenderReportService
				.summarizeIfOffender(person);
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(SENTENCE_SUMMARIES_MODEL_KEY, sentenceSummaries);
		mav.addObject(PERSON_MODEL_KEY, person);
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
	
	/**
	 * Shows action menu for current offense terms.
	 * 
	 * @param person person
	 * @param sentence sentence
	 * @return action menu for current offense terms
	 */
	@RequestMapping(value = "/currentOffenseTermsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "person", required = false)
				final Person person,
			@RequestParam(value = "sentence", required = false)
				final Sentence sentence) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PERSON_MODEL_KEY, person);
		mav.addObject(SENTENCE_MODEL_KEY, sentence);
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
	@RequestMapping(value = "/currentCourtCaseListingReport.html",
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
		binder.registerCustomEditor(Sentence.class,
				this.sentencePropertyEditorFactory.createPropertyEditor());
	}
}
