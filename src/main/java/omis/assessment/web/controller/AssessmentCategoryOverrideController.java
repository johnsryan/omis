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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.service.AssessmentService;
import omis.assessment.web.form.AssessmentCategoryOverrideForm;
import omis.assessment.web.validator.AssessmentCategoryOverrideFormValidator;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * TODO
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Apr 18, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/assessment/rating")
@PreAuthorize("hasRole('USER')")
public class AssessmentCategoryOverrideController {
	
	/* View names. */

	private static final String EDIT_VIEW_NAME = "assessment/rating/edit";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME =
			"assessment/rating/includes/assessmentCategoryOverrideActionMenu";
	
	/* Model Keys */
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_CATEGORY_SCORE_MODEL_KEY =
			"assessmentCategoryScore";
	
	private static final String ASSESSMENT_CATEGORY_OVERRIDE_MODEL_KEY =
			"assessmentCategoryOverride";
	
	private static final String FORM_MODEL_KEY =
			"assessmentCategoryOverrideForm";
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"assessmentCategoryOverride.exists";

	private static final String ERROR_BUNDLE_NAME = "omis.assessment.msgs.form";
	
	/* Property Editor Factories */

	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
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

	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Validator */
	
	@Autowired
	@Qualifier("assessmentCategoryOverrideFormValidator")
	private AssessmentCategoryOverrideFormValidator
		assessmentCategoryOverrideFormValidator;
	
	/* Service */
	
	@Autowired
	@Qualifier("assessmentService")
	private AssessmentService assessmentService;
	
	/**
	 * 
	 */
	public AssessmentCategoryOverrideController() {
	}
	
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_CATEGORY_OVERRIDE_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "assessmentCategoryScore", required = true)
			final AssessmentCategoryScore assessmentCategoryScore) {
		return this.prepareEditMav(assessmentCategoryScore);
	}
	
	
	
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_CATEGORY_OVERRIDE_VIEW') or "
			+ "hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "assessmentCategoryOverride", required = true)
			final AssessmentCategoryOverride assessmentCategoryOverride) {
		return this.prepareEditMav(assessmentCategoryOverride);
	}
	
	/* Action Menus */
	
	@RequestMapping(value = "/assessmentCategoryOverrideActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentCategoryOverrideActionMenu(
			@RequestParam(value = "assessmentCategoryScore", required = true)
			final AssessmentCategoryScore assessmentCategoryScore) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				assessmentCategoryScore.getAdministeredQuestionnaire());
		return new ModelAndView(ACTION_MENU_VIEW_NAME, map);
	}
	
	/*Helper Methods */
	
	/**
	 * @param assessmentCategoryScore
	 * @return
	 */
	private ModelAndView prepareEditMav(
			final AssessmentCategoryScore assessmentCategoryScore) {
		return this.prepareEditMav(assessmentCategoryScore,
				new AssessmentCategoryOverrideForm());
	}

	/**
	 * @param assessmentCategoryScore
	 * @param assessmentCategoryOverrideForm
	 * @return
	 */
	private ModelAndView prepareEditMav(
			final AssessmentCategoryScore assessmentCategoryScore,
			final AssessmentCategoryOverrideForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(ASSESSMENT_CATEGORY_SCORE_MODEL_KEY,
				assessmentCategoryScore);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	private ModelAndView prepareEditMav(
			final AssessmentCategoryOverride assessmentCategoryOverride) {
		AssessmentCategoryOverrideForm form =
				new AssessmentCategoryOverrideForm();
		
		//TODO: There's no date on an override
		form.setOverrideDate(new Date());
		form.setAuthorizedBy(assessmentCategoryOverride
				.getApprovedStaffAssignment());
		form.setOverrideRating(assessmentCategoryOverride
				.getAssessmentRating());
		//TODO:
		form.setOverrideReason(null);
		
		return this.prepareEditMav(assessmentCategoryOverride, form);
	}
	
	/**
	 * @param assessmentCategoryOverride
	 * @param form
	 * @return
	 */
	private ModelAndView prepareEditMav(
			final AssessmentCategoryOverride assessmentCategoryOverride,
			final AssessmentCategoryOverrideForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(ASSESSMENT_CATEGORY_SCORE_MODEL_KEY,
				assessmentCategoryOverride.getAssessmentCategoryScore());
		map.addAttribute(ASSESSMENT_CATEGORY_OVERRIDE_MODEL_KEY,
				assessmentCategoryOverride);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}

	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
					exception);
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
