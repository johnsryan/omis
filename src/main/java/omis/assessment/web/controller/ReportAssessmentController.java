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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import omis.assessment.report.AssessmentCategoryScoreSummary;
import omis.assessment.report.AssessmentSummary;
import omis.assessment.report.AssessmentSummaryReportService;
import omis.beans.factory.PropertyEditorFactory;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;

/**
 * Controller for reporting assessments.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 27, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/assessment")
@PreAuthorize("hasRole('USER')")
public class ReportAssessmentController {

	/* View names. */
	
	private static final String VIEW_NAME = "assessment/list";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME = 
			"assessment/includes/assessmentsActionMenu";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String ASSESSMENT_SUMMARIES_MODEL_KEY = 
			"assessmentSummaries";
	
	private static final String QUESTIONNAIRE_CATEGORIES_MODEL_KEY = 
			"questionnaireCategories";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY = 
			"administeredQuestionnaire";
	/* Services. */
	
	@Autowired
	@Qualifier("assessmentSummaryReportService")
	private AssessmentSummaryReportService assessmentSummaryReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionnaireCategoryPropertyEditorFactory")
	private PropertyEditorFactory questionnaireCategoryPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for assessments. */
	public ReportAssessmentController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	@PreAuthorize("hasRole('ASSESSMENT_LIST') or hasRole('ADMIN')")
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "offender", required = true) 
				final Offender offender) {
		ModelMap map = new ModelMap();
		this.offenderSummaryModelDelegate.add(map, offender);
		List<AdministeredQuestionnaire> administeredQuestionnaires = this
				.assessmentSummaryReportService
				.findAssessmentQuestionnairesByOffender(offender);
		Map<AssessmentSummary, List<AssessmentCategoryScoreSummary>> 
			assessmentSummaries = new HashMap<>();
		for (AdministeredQuestionnaire administeredQuestionnaire : 
				administeredQuestionnaires) {
			assessmentSummaries.put(this.assessmentSummaryReportService
					.summarize(administeredQuestionnaire), 
					this.assessmentSummaryReportService
					.findAssessmentCategoryScoreSummariesByAdministeredQuestionnaire(
							administeredQuestionnaire));
		}
		map.addAttribute(ASSESSMENT_SUMMARIES_MODEL_KEY, assessmentSummaries);
		return new ModelAndView(VIEW_NAME, map);
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for assessments.
	 * 
	 * @param offender offender
	 * @param administeredQuestionnaire administered questionnaire
	 * @return action menu for assessments
	 */
	@RequestMapping(value = "/assessmentsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "administeredQuestionnaire", required = false)
				final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		if (offender != null) {
			mav.addObject(OFFENDER_MODEL_KEY, offender);
			List<QuestionnaireCategory> questionnaireCategories = this
					.assessmentSummaryReportService
					.findAssessmentQuestionCategories();
			mav.addObject(QUESTIONNAIRE_CATEGORIES_MODEL_KEY, 
					questionnaireCategories);
		}
		if (administeredQuestionnaire != null) {
			mav.addObject(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY, 
					administeredQuestionnaire);
		}
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
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(QuestionnaireCategory.class, 
				this.questionnaireCategoryPropertyEditorFactory
				.createPropertyEditor());
	}
}