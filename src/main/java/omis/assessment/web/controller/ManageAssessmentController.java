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
import java.util.List;

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

import omis.assessment.service.AssessmentService;
import omis.assessment.web.form.AssessmentForm;
import omis.assessment.web.validator.AssessmentFormValidator;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.QuestionnaireCategory;
import omis.questionnaire.domain.QuestionnaireSection;
import omis.questionnaire.domain.QuestionnaireType;
import omis.questionnaire.service.AdministeredQuestionnaireService;

/**
 * Controller for managing assessments.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.0 (Apr 12, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/assessment")
@PreAuthorize("hasRole('USER')")
public class ManageAssessmentController {

	/* View names. */

	private static final String VIEW_NAME = "assessment/edit";
	
	/* Action menus. */
	
	private static final String ACTION_MENU_VIEW_NAME =
			"assessment/includes/assessmentActionMenu";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL =
			"redirect:/assessment/home.html?administeredQuestionnaire=%d";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String QUESTIONNAIRE_TYPES_MODEL_KEY =
			"questionnaireTypes";
	
	private static final String ASSESSMENT_FORM_MODEL_KEY = "assessmentForm";
	
	private static final String QUESTIONNAIRE_CATEGORY_MODEL_KEY =
			"questionnaireCategory";
	
	/* Services. */
	
	@Autowired
	@Qualifier("assessmentService")
	private AssessmentService assessmentService;
	
	//TODO: add required methods to assessment service, and remove this.
	@Autowired
	@Qualifier("administeredQuestionnaireService")
	private AdministeredQuestionnaireService administeredQuestionnaireService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionnaireCategoryPropertyEditorFactory")
	private PropertyEditorFactory questionnaireCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionnaireTypePropertyEditorFactory")
	private PropertyEditorFactory questionnaireTypePropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("assessmentFormValidator")
	private AssessmentFormValidator assessmentFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructors. */
	
	/** Instantiates controller for assessments. */
	public ManageAssessmentController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to create an assessment.
	 * 
	 * @param offender offender
	 * @param questionnaireCategory questionnaire category
	 * @return screen to create an assessment
	 */
	@PreAuthorize("hasRole('ASSESSMENT_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
					final Offender offender,
			@RequestParam(value = "questionnaireCategory", required = true)
			final QuestionnaireCategory questionnaireCategory) {
		AssessmentForm assessmentForm = new AssessmentForm();
		assessmentForm.setAssessDate(new Date());
		ModelAndView mav = this.prepareEditMav(assessmentForm, offender,
				questionnaireCategory);
		return mav;
	}
	
	/**
	 * Saves a new assessment.
	 * 
	 * @param offender offender
	 * @param questionnaireCategory questionnaire category
	 * @param assessmentForm assessment form
	 * @param bindingResult binding result
	 * @return redirect to work assessment screen
	 * @throws DuplicateEntityFoundException if administered questionnaire
	 * already exists
	 */
	@PreAuthorize("hasRole('ASSESSMENT_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true) 
					final Offender offender,
			@RequestParam(value = "questionnaireCategory", required = true) 
			final QuestionnaireCategory questionnaireCategory,
			final AssessmentForm assessmentForm,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.assessmentFormValidator.validate(assessmentForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareRedisplay(assessmentForm, offender,
					questionnaireCategory, bindingResult);
		}
		AdministeredQuestionnaire administeredQuestionnaire = this
				.assessmentService.createAdministeredQuestionnaire(offender,
						true, assessmentForm.getComments(),
						assessmentForm.getAssessor(),
						assessmentForm.getAssessDate(),
						assessmentForm.getQuestionnaireType());
		for (QuestionnaireSection questionnaireSection
				: this.administeredQuestionnaireService
				.findQuestionnaireSectionsByQuestionnaireType(
						assessmentForm.getQuestionnaireType())) {
			this.administeredQuestionnaireService
				.createAdministeredQuestionnaireSectionStatus(
						questionnaireSection, administeredQuestionnaire,
						true, assessmentForm.getAssessDate());
		}
		return new ModelAndView(String.format(REDIRECT_URL,
				administeredQuestionnaire.getId()));
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for assessments.
	 * 
	 * @param offender offender
	 * @return action menu for parole board itineraries
	 */
	@RequestMapping(value = "/assessmentActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/* Helper methods. */
	
	// Prepares the model and view to display the screen to edit assessments.
	private ModelAndView prepareEditMav(final AssessmentForm asseessmentForm,
			final Offender offender,
			final QuestionnaireCategory questionnaireCategory) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(ASSESSMENT_FORM_MODEL_KEY, asseessmentForm);
		mav.addObject(QUESTIONNAIRE_CATEGORY_MODEL_KEY, questionnaireCategory);
		this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		List<QuestionnaireType> questionaireTypes = this.assessmentService
				.findQuestionaireTypesByQuestionnaireCategory(
						questionnaireCategory);
		mav.addObject(QUESTIONNAIRE_TYPES_MODEL_KEY, questionaireTypes);
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final AssessmentForm assessmentForm,
			final Offender offender,
			final QuestionnaireCategory questionnaireCategory,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareEditMav(assessmentForm, offender,
				questionnaireCategory);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ ASSESSMENT_FORM_MODEL_KEY, bindingResult);
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
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(QuestionnaireCategory.class, 
				this.questionnaireCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(QuestionnaireType.class, 
				this.questionnaireTypePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Person.class, 
				this.personPropertyEditorFactory.createPropertyEditor());
	}
}