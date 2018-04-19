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
package omis.assessment.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.assessment.report.AssessmentSummaryReportService;
import omis.beans.factory.PropertyEditorFactory;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Home Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 22, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/assessment")
@PreAuthorize("hasRole('USER')")
public class AssessmentHomeController {
	
	private static final String HOME_VIEW_NAME = "/assessment/home/home";
	
	/* Model Keys */
	
	private static final String ASSESSMENT_SUMMARY_MODEL_KEY =
			"assessmentSummary";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
		administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("assessmentSummaryReportService")
	private AssessmentSummaryReportService assessmentSummaryReportService;
	
	/**
	 * Displays the Assessment home model and View.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return ModelAndView - assessment home model and view.
	 */
	@RequestMapping(value = "/home.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_VIEW') or "
					+ "hasRole('ADMIN')")
	public ModelAndView showContent(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		map.addAttribute(ASSESSMENT_SUMMARY_MODEL_KEY,
				this.assessmentSummaryReportService.summarize(
						administeredQuestionnaire));
		this.offenderSummaryModelDelegate.add(map,
				(Offender) administeredQuestionnaire.getAnswerer());
		return new ModelAndView(HOME_VIEW_NAME, map);
	}
	
	/**
	 * Init binder.
	 * @param binder - web data binder.
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionnaire.class, 
				this.administeredQuestionnairePropertyEditorFactory
					.createPropertyEditor());
	}
}
