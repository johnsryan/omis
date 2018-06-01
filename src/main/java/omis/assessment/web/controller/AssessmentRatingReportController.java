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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingRank;
import omis.assessment.report.AssessmentSummaryReportService;
import omis.assessment.service.AssessmentService;
import omis.beans.factory.PropertyEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Rating Report Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Apr 18, 2018)
 *@since OMIS 3.0
 */
@Controller
@RequestMapping("/assessment/rating")
@PreAuthorize("hasRole('USER')")
public class AssessmentRatingReportController {
	
	/* View names. */

	private static final String LIST_VIEW_NAME = "assessment/rating/list";
	
	private static final String ASSESSMENT_RATINGS_ACTION_MENU_VIEW_NAME =
			"assessment/rating/includes/assessmentRatingsActionMenu";
	
	private static final String ASSESSMENT_RATINGS_ROW_ACTION_MENU_VIEW_NAME =
			"assessment/rating/includes/assessmentRatingsRowActionMenu";
	
	private static final String
		ASSESSMENT_CATEGORY_RATINGS_ACTION_MENU_VIEW_NAME =
			"assessment/rating/includes/assessmentCategoryRatingsActionMenu";
	
	private static final String
		ASSESSMENT_CATEGORY_RATINGS_ROW_ACTION_MENU_VIEW_NAME =
			"assessment/rating/includes/assessmentCategoryRatingsRowActionMenu";
	
	private static final String GRAPH_DATA_MODEL_KEY = "graphData";
	
	/* Model Keys */
	
	private static final String ASSESSMENT_RATING_SUMMARIES_MODEL_KEY =
			"assessmentRatingSummaries";
	
	private static final String ASSESSMENT_CATEGORY_SCORE_SUMMARIES_MODEL_KEY =
			"assessmentCategoryScoreSummaries";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_CATEGORY_SCORE_MODEL_KEY =
			"assessmentCategoryScore";
	
	private static final String ASSESSMENT_CATEGORY_OVERRIDE_MODEL_KEY =
			"assessmentCategoryOverride";
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
		administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("assessmentCategoryScorePropertyEditorFactory")
	private PropertyEditorFactory assessmentCategoryScorePropertyEditorFactory;

	@Autowired
	@Qualifier("assessmentCategoryOverridePropertyEditorFactory")
	private PropertyEditorFactory
		assessmentCategoryOverridePropertyEditorFactory;
	
	/* Service */
	
	@Autowired
	@Qualifier("assessmentSummaryReportService")
	private AssessmentSummaryReportService assessmentSummaryReportService;
	
	@Autowired
	@Qualifier("assessmentService")
	private AssessmentService assessmentService;
	
	/**
	 * 
	 */
	public AssessmentRatingReportController() {
	}
	
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_RATING_LIST') or hasRole('ADMIN')")
	public ModelAndView list(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire)
					throws DuplicateEntityFoundException {
		ModelMap map = new ModelMap();
		
		if(!administeredQuestionnaire.getDraft()) {
			this.assessmentService.assess(administeredQuestionnaire);
		}
		map.addAttribute(ASSESSMENT_RATING_SUMMARIES_MODEL_KEY,
				this.assessmentSummaryReportService
				.findAssessmentRatingSummariesByAdministeredQuestionnaire(
						administeredQuestionnaire));
		map.addAttribute(ASSESSMENT_CATEGORY_SCORE_SUMMARIES_MODEL_KEY,
				this.assessmentSummaryReportService.
				findAssessmentCategoryScoreSummariesByAdministeredQuestionnaire(
						administeredQuestionnaire));
		List<RatingCategory> ratingCategories = this.assessmentService
				.findRatingCategoriesByQuestionnaireType(
						administeredQuestionnaire.getQuestionnaireType());
		HashMap<AssessmentCategoryScore, List<AssessmentRating>> data =
				new HashMap<AssessmentCategoryScore, List<AssessmentRating>>();
		for (RatingCategory ratingCategory : ratingCategories) {
			data.put(this.assessmentService
					.findAssessmentCategoryScoreByRatingCategoryAndAdministeredQuestionnaire(
							ratingCategory, administeredQuestionnaire),
					this.assessmentService.findAssessmentRatingsByCategory(
							ratingCategory));
		}
		map.addAttribute(GRAPH_DATA_MODEL_KEY, data);
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/* Action Menus */
	
	@RequestMapping(value = "assessmentRatingsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentRatingsActionMenu(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		return new ModelAndView(
				ASSESSMENT_RATINGS_ACTION_MENU_VIEW_NAME, map);
	}
	
	@RequestMapping(value = "assessmentRatingsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentRatingRowsActionMenu(
			@RequestParam(value = "assessmentCategoryScore", required = true)
			final AssessmentCategoryScore assessmentCategoryScore) {
		ModelMap map = new ModelMap();
		map.addAttribute(ASSESSMENT_CATEGORY_SCORE_MODEL_KEY,
				assessmentCategoryScore);
		map.addAttribute(ASSESSMENT_CATEGORY_OVERRIDE_MODEL_KEY,
				this.assessmentService
				.findAssessmentCategoryOverrideByAssessmentCategoryScore(
						assessmentCategoryScore));
		return new ModelAndView(
				ASSESSMENT_RATINGS_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	@RequestMapping(value = "assessmentCategoryRatingsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayCategoryAssessmentRatingsActionMenu(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		return new ModelAndView(
				ASSESSMENT_CATEGORY_RATINGS_ACTION_MENU_VIEW_NAME, map);
	}
	
	@RequestMapping(value = "assessmentCategoryRatingsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentCategoryRatingRowsActionMenu(
			@RequestParam(value = "assessmentCategoryScore", required = true)
			final AssessmentCategoryScore assessmentCategoryScore) {
		ModelMap map = new ModelMap();
		map.addAttribute(ASSESSMENT_CATEGORY_SCORE_MODEL_KEY,
				assessmentCategoryScore);
		map.addAttribute(ASSESSMENT_CATEGORY_OVERRIDE_MODEL_KEY,
				this.assessmentService
				.findAssessmentCategoryOverrideByAssessmentCategoryScore(
						assessmentCategoryScore));
		return new ModelAndView(
				ASSESSMENT_CATEGORY_RATINGS_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(AdministeredQuestionnaire.class,
				this.administeredQuestionnairePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AssessmentCategoryScore.class,
				this.assessmentCategoryScorePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AssessmentCategoryOverride.class,
				this.assessmentCategoryOverridePropertyEditorFactory
					.createPropertyEditor());
	}
}
