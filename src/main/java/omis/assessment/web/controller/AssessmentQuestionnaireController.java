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
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.questionnaire.domain.AdministeredQuestionValue;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.AdministeredQuestionnaireSectionNote;
import omis.questionnaire.domain.AdministeredQuestionnaireSectionStatus;
import omis.questionnaire.domain.AllowedQuestion;
import omis.questionnaire.domain.AnswerCardinality;
import omis.questionnaire.domain.AnswerValue;
import omis.questionnaire.domain.Question;
import omis.questionnaire.domain.QuestionnaireSection;
import omis.questionnaire.report.AdministeredQuestionnaireReportService;
import omis.questionnaire.report.AdministeredQuestionnaireSectionSummary;
import omis.questionnaire.report.AdministeredQuestionnaireSummary;
import omis.questionnaire.report.AnswerValueSummary;
import omis.questionnaire.report.QuestionSummary;
import omis.questionnaire.service.AdministeredQuestionnaireService;
import omis.questionnaire.web.form.AdministeredQuestionValueItem;
import omis.questionnaire.web.form.AdministeredQuestionnaireSectionForm;
import omis.questionnaire.web.form.QuestionAnswerItem;
import omis.questionnaire.web.form.QuestionnaireReviewForm;
import omis.questionnaire.web.form.SectionReviewItem;
import omis.questionnaire.web.validator.AdministeredQuestionnaireSectionFormValidator;
import omis.questionnaire.web.validator.QuestionnaireReviewFormValidator;
import omis.util.StringUtility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Assessment Questionnaire Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 21, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/assessment/questionnaire")
@PreAuthorize("hasRole('USER')")
public class AssessmentQuestionnaireController {
	
	/* View Names */
	
	private static final String SECTION_VIEW_NAME =
			"/assessment/questionnaire/edit";
	
	private static final String ASSESSMENT_QUESTIONNAIRE_ACTION_MENU_VIEW_NAME
		= "/assessment/questionnaire/includes/"
				+ "assessmentQuestionnaireActionMenu";
	
	private static final String REVIEW_SECTION_ACTION_MENU_VIEW_NAME =
			"/assessment/questionnaire/includes/reviewSectionActionMenu";
	
	private static final String REVIEW_ASSESSMENT_ACTION_MENU_VIEW_NAME =
			"/assessment/questionnaire/includes/reviewAssessmentActionMenu";
	
	private static final String REVIEW_VIEW_NAME =
			"/assessment/questionnaire/review";
	
	private static final String SECTION_VIEW_REDIRECT =
			"redirect:/assessment/questionnaire/section.html?"
			+ "sectionStatus=%d";
	
	private static final String LIST_REDIRECT = 
			"redirect:/assessment/questionnaire/list.html?offender=%d";
	
	/* Model Keys */
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String SECTION_SUMMARIES_MODEL_KEY =
			"sectionSummaries";
	
	private static final String 
		QUESTIONNAIRE_SECTION_QUESTION_SUMMARIES_MODEL_KEY =
			"sectionQuestionSummaries";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_SUMMARY_MODEL_KEY =
			"administeredQuestionnaireSummary";
	
	private static final String QUESTION_ANSWER_SUMMARIES_MODEL_KEY =
			"questionAnswerSummaries";
	
	private static final String QUESTIONNAIRE_SECTION_STATUS_MODEL_KEY =
			"questionnaireSectionStatus";
	
	private static final String FORM_MODEL_KEY =
			"administeredQuestionnaireSectionForm";
	
	private static final String REVIEW_FORM_MODEL_KEY =
			"questionnaireReviewForm";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"entity.administeredQuestionnaire.exists";
	
	/* Bundle */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.questionnaire.msgs.form";
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
		administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("administeredQuestionValuePropertyEditorFactory")
	private PropertyEditorFactory
		administeredQuestionValuePropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionnaireTypePropertyEditorFactory")
	private PropertyEditorFactory questionnaireTypePropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("answerValuePropertyEditorFactory")
	private PropertyEditorFactory answerValuePropertyEditorFactory;
	
	@Autowired
	@Qualifier("allowedQuestionPropertyEditorFactory")
	private PropertyEditorFactory allowedQuestionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("administeredQuestionnaireSectionStatusPropertyEditorFactory")
	private PropertyEditorFactory 
		administeredQuestionnaireSectionStatusPropertyEditorFactory;
	
	/* Helpers */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Services */
	
	@Autowired
	@Qualifier("administeredQuestionnaireService")
	private AdministeredQuestionnaireService administeredQuestionnaireService;
	
	@Autowired
	@Qualifier("administeredQuestionnaireReportService")
	private AdministeredQuestionnaireReportService
		administeredQuestionnaireReportService;
	
	/* Validator */

	@Autowired
	private AdministeredQuestionnaireSectionFormValidator formValidator;
	
	@Autowired
	private QuestionnaireReviewFormValidator reviewFormValidator;
	
	
	/**
	 * Default constructor for Assessment Questionnaire Controller.
	 */
	public AssessmentQuestionnaireController() {
	}
	
	/**
	 * Returns the Model and View for administration of an Assessment centric
	 * Administered Questionnaire.
	 * @param questionnaire - Administered Questionnaire
	 * @return Model And View for administration of an Assessment centric
	 * Administered Questionnaire.
	 */
	@RequestMapping(value = "administer.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('QUESTIONNAIRE_LIST')")
	public ModelAndView administer(
		@RequestParam(value = "administeredQuestionnaire", required = true)
		final AdministeredQuestionnaire questionnaire) {
		return new ModelAndView(String.format(SECTION_VIEW_REDIRECT, 
			this.administeredQuestionnaireReportService
			.findAdministeredQuestionnaireSectionSummariesByAdministeredQuestionnaire(
					questionnaire).get(0).getQuestionnaireSectionStatusId()));
	}
	
	/**
	 * Returns the Model and View for displaying a questionnaire section for
	 * administration.
	 * @param sectionStatus - Administered Question Section Status
	 * @return Model and View for displaying a questionnaire section for
	 * administration.
	 */
	@RequestMapping(value = "section.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('QUESTIONNAIRE_CREATE')"
			+ " or hasRole('QUESTIONNAIRE_EDIT')")
	public ModelAndView section(
			@RequestParam(value = "sectionStatus", required = true)
			final AdministeredQuestionnaireSectionStatus sectionStatus) {
		Boolean returning = false;
		List<AllowedQuestion> allowedQuestions =
				this.administeredQuestionnaireService
				.findAllowedQuestionsByQuestionSection(
						sectionStatus.getQuestionnaireSection());
		checkReturned
			: for (AllowedQuestion allowedQuestion : allowedQuestions) {
			if (this.administeredQuestionnaireService
					.findAdministeredQuestionValuesByQuestionAndQuestionnaire(
					allowedQuestion.getQuestion(),
					sectionStatus.getAdministeredQuestionnaire()) != null) {
				returning = true;
				break checkReturned;
			}
		}
		
		if (returning) {
			//returning, populate form
			AdministeredQuestionnaireSectionForm form =
					new AdministeredQuestionnaireSectionForm();
			List<QuestionAnswerItem> questionAnswerItems =
					new ArrayList<QuestionAnswerItem>();
			
			for (AllowedQuestion allowedQuestion : allowedQuestions) {
				QuestionAnswerItem item = new QuestionAnswerItem();
				
				if (allowedQuestion.getAnswerCardinality()
						.equals(AnswerCardinality.SINGLE)) {
					AdministeredQuestionValue administeredQuestionValue =
							this.administeredQuestionnaireService
							.findAdministeredQuestionValueByQuestionAndQuestionnaire(
								allowedQuestion.getQuestion(), 
								sectionStatus.getAdministeredQuestionnaire());
					if (administeredQuestionValue != null) {
						item.setAnswerValue(administeredQuestionValue
								.getAnswerValue());
						item.setAnswerValueText(administeredQuestionValue
								.getAnswerValueText());
						item.setComments(administeredQuestionValue
								.getComments());
						item.setAdministeredQuestionValue(
								administeredQuestionValue);
					}
				} else if (allowedQuestion.getAnswerCardinality()
						.equals(AnswerCardinality.MULTIPLE)) {
					List<AdministeredQuestionValue> administeredQuestionValues
						= this.administeredQuestionnaireService
							.findAdministeredQuestionValuesByQuestionAndQuestionnaire(
								allowedQuestion.getQuestion(), 
								sectionStatus.getAdministeredQuestionnaire());
					List<AdministeredQuestionValueItem> admnstrdQstnValueItems =
							new ArrayList<AdministeredQuestionValueItem>();
					
					String comments = null;
					String answerValueText = null;
					
					for (AdministeredQuestionValue value 
							: administeredQuestionValues) {
						AdministeredQuestionValueItem valueItem =
								new AdministeredQuestionValueItem();
						valueItem.setAnswerValue(value.getAnswerValue());
						valueItem.setAdministeredQuestionValue(value);
						admnstrdQstnValueItems.add(valueItem);
						
						if (comments == null && value.getComments() != null) {
							comments = value.getComments();
						}
						if (answerValueText == null
								&& value.getAnswerValueText() != null) {
							answerValueText = value.getAnswerValueText();
						}
						item.setAdministeredQuestionValue(value);
					}
					item.setAdministeredQuestionValueItems(
							admnstrdQstnValueItems);
					item.setComments(comments);
					item.setAnswerValueText(answerValueText);
				}
				item.setAllowedQuestion(allowedQuestion);
				item.setAnswerCardinality(allowedQuestion
						.getAnswerCardinality());
				questionAnswerItems.add(item);
			}
			
			form.setQuestionAnswerItems(questionAnswerItems);
			
			AdministeredQuestionnaireSectionNote sectionNote = 
					this.administeredQuestionnaireService
			.findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
							sectionStatus.getAdministeredQuestionnaire(),
							sectionStatus.getQuestionnaireSection());
			
			if (sectionNote != null) {
				form.setSectionComments(sectionNote.getComments());
			}
			
			return prepareEditMav(sectionStatus, form);
		} else {
			//not returning, create new.
			return prepareEditMav(sectionStatus, 
					new AdministeredQuestionnaireSectionForm());
		}
	}
	
	/**
	 * Saves Administered Question Values and Answer Values for a Questionnaire
	 * Section.
	 * @param sectionStatus - Administered Questionnaire Section Status.
	 * @param finalFlag - Boolean flag to discern whether the section is a
	 * draft or to be finalized.
	 * @param form - Administered Questionnaire Section Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - 
	 * @throws DuplicateEntityFoundException - When a AdministeredQuestionValue
	 * or AnswerValue already exists.
	 */
	@RequestMapping(value = "section.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('QUESTIONNAIRE_CREATE')"
			+ " or hasRole('QUESTIONNAIRE_EDIT')")
	public ModelAndView save(
		@RequestParam(value = "sectionStatus", required = true)
		final AdministeredQuestionnaireSectionStatus sectionStatus,
		@RequestParam(required = false, value = "final")
		final String finalFlag, final AdministeredQuestionnaireSectionForm form,
		final BindingResult bindingResult)
				throws DuplicateEntityFoundException {
		Boolean draft = true;
		if (finalFlag != null) {
			draft = false;
		}
		
		//Only if the user submitted a "final" form will it get validated
		if (!draft) {
			this.formValidator.validate(form, bindingResult);
		}
		
		if (bindingResult.hasErrors()) {
			return prepareEditMav(sectionStatus, form);
		} else {
			QuestionnaireSection questionnaireSection = 
					sectionStatus.getQuestionnaireSection();
			AdministeredQuestionnaire administeredQuestionnaire = 
					sectionStatus.getAdministeredQuestionnaire();
			AdministeredQuestionnaireSectionNote sectionNote =
					this.administeredQuestionnaireService
			.findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
					administeredQuestionnaire, questionnaireSection);
			
			//send in all QuestionAnswerItems for processing
			this.processQuestionAnswerItems(
					form.getQuestionAnswerItems(), sectionStatus);
			
			//create/update/remove the AdministeredQuestionnaireSectionNote
			if (sectionNote != null) {
				if (StringUtility.hasContent(form.getSectionComments())) {
					//note exists in the database and on the form, so update it
					this.administeredQuestionnaireService
						.updateAdministeredQuestionnaireSectionNote(sectionNote,
							questionnaireSection, form.getSectionComments());
				} else if (!StringUtility.hasContent(
						form.getSectionComments())) {
					//note exists in the database but not on the form,
					//so remove it
					this.administeredQuestionnaireService
					.removeAdministeredQuestionnaireSectionNote(sectionNote);
				}
			} else {
				if (StringUtility.hasContent(form.getSectionComments())) {
					//a note does not exist in the database but does on the form
					//so create it
					this.administeredQuestionnaireService
						.createAdministeredQuestionnaireSectionNote(
							administeredQuestionnaire, questionnaireSection,
							form.getSectionComments());
				}
			}
			//update AdministeredQuestionnaireSectionStatus with any possible
			//draft changes 
			this.administeredQuestionnaireService
				.updateAdministeredQuestionnaireSectionStatus(
						sectionStatus, draft, sectionStatus.getDate());
		}
		return new ModelAndView(String.format(LIST_REDIRECT, sectionStatus
				.getAdministeredQuestionnaire().getAnswerer().getId()));
	}
	
	/**
	 * Returns a view of the questionnaire review screen.
	 * @param administeredQuestionnaire - administered questionnaire
	 * being reviewed
	 * @return ModelAndView - view of the questionnaire review screen
	 */
	@RequestMapping(value = "/review.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView review(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.prepareReviewMav(administeredQuestionnaire,
				new QuestionnaireReviewForm());
	}
	
	/**
	 * Attempts to finalize the administered questionnaire and return to the
	 * questionnaire list screen.
	 * @param administeredQuestionnaire - administered questionnaire to be 
	 * finalized
	 * @param form - questionnaire review form
	 * @param bindingResult - binding result
	 * @return ModelAndView - redirects to the questionnaire list screen on
	 * successful finalization, or back to the review screen on error
	 * @throws DuplicateEntityFoundException - when somehow a duplicate 
	 * administered questionnaire already exists (should logically never happen
	 * from this location)
	 */
	@RequestMapping(value = "/review.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView finalize(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final QuestionnaireReviewForm form, 
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		
		this.reviewFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareReviewMav(administeredQuestionnaire, form);
		} else {
			this.administeredQuestionnaireService.editAdministeredQuestionnaire(
					administeredQuestionnaire, 
					administeredQuestionnaire.getAnswerer(), 
					false, administeredQuestionnaire.getComments(), 
					administeredQuestionnaire.getAssessor(), 
					administeredQuestionnaire.getDate(), 
					administeredQuestionnaire.getQuestionnaireType());

			return new ModelAndView(String.format(LIST_REDIRECT,
					administeredQuestionnaire.getAnswerer().getId()));
		}
	}
	
	/**
	 * Returns the Model And View for the Assessment Questionnaire Action Menu.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model And View for the Assessment Questionnaire Action Menu 
	 */
	@RequestMapping(value = "/assessmentQuestionnaireActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentQuestionnaireActionMenu(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		return new ModelAndView(ASSESSMENT_QUESTIONNAIRE_ACTION_MENU_VIEW_NAME,
				map);
	}
	
	/**
	 * Returns the Model and View for the Review Section Action Menu.
	 * @param sectionStatus - Administered Questionnaire Section Status
	 * @return Model and View for the Review Section Action Menu.
	 */
	@RequestMapping(value = "/reviewSectionActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayReviewSectionActionMenu(
			@RequestParam(value = "sectionStatus", required = true)
			final AdministeredQuestionnaireSectionStatus sectionStatus) {
		ModelMap map = new ModelMap();
		map.addAttribute(QUESTIONNAIRE_SECTION_STATUS_MODEL_KEY,
				sectionStatus);
		return new ModelAndView(REVIEW_SECTION_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns the Model and View for Assessment Questionnaires Row Action Menu.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model and View for Assessment Questionnaires Row Action Menu.
	 */
	@RequestMapping(value = "/reviewAssessmentActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayReviewAssessmentActionMenuActionMenu(
			@RequestParam(value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		return new ModelAndView(REVIEW_ASSESSMENT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * @param sectionStatus
	 * @param form
	 * @return
	 */
	private ModelAndView prepareEditMav(
			final AdministeredQuestionnaireSectionStatus sectionStatus,
			final AdministeredQuestionnaireSectionForm form) {
		ModelMap map = new ModelMap();
		
		List<AllowedQuestion> allowedQuestions =
				this.administeredQuestionnaireService
				.findAllowedQuestionsByQuestionSection(
						sectionStatus.getQuestionnaireSection());
		LinkedHashMap<QuestionSummary, List<AnswerValueSummary>> 
				questionAnswerSummaries = new LinkedHashMap<QuestionSummary,
					List<AnswerValueSummary>>();
		
		for (AllowedQuestion allowedQuestion : allowedQuestions) {
			questionAnswerSummaries.put(
					this.administeredQuestionnaireReportService
						.summarize(allowedQuestion),
					this.administeredQuestionnaireReportService
						.findAnswerValueSummariesByAllowedQuestion(
								allowedQuestion));
		}
		map.addAttribute(QUESTION_ANSWER_SUMMARIES_MODEL_KEY,
				questionAnswerSummaries);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				sectionStatus.getAdministeredQuestionnaire());
		map.addAttribute(QUESTIONNAIRE_SECTION_STATUS_MODEL_KEY, sectionStatus);
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_SUMMARY_MODEL_KEY,
				this.administeredQuestionnaireReportService
				.summarize(sectionStatus.getAdministeredQuestionnaire()));
		map.addAttribute(SECTION_SUMMARIES_MODEL_KEY,
				this.administeredQuestionnaireReportService
				.findAdministeredQuestionnaireSectionSummariesByAdministeredQuestionnaire(
						sectionStatus.getAdministeredQuestionnaire()));
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				sectionStatus.getAdministeredQuestionnaire());
		return new ModelAndView(SECTION_VIEW_NAME, map);
	}
	
	/**
	 * Returns a prepared model and view for the questionnaire review screen.
	 * @param administeredQuestionnaire - administered questionnaire to be
	 * reviewed
	 * @param form - questionnaire review form
	 * @return ModelAndView - prepared model and view for the questionnaire
	 * review screen
	 */
	public ModelAndView prepareReviewMav(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final QuestionnaireReviewForm form) {
		ModelMap map = new ModelMap();
		
		List<AdministeredQuestionnaireSectionStatus> sectionStatuses =
				this.administeredQuestionnaireService
		.findAdministeredQuestionnaireSectionStatusByAdministeredQuestionnaire(
				administeredQuestionnaire);
		
		LinkedHashMap<AdministeredQuestionnaireSectionSummary,
			List<QuestionSummary>> sectionQuestionSummaries = new LinkedHashMap
				<AdministeredQuestionnaireSectionSummary,
				List<QuestionSummary>>();
		List<QuestionSummary> questionSummaries;
		List<SectionReviewItem> sectionReviewItems = new 
				ArrayList<SectionReviewItem>();
		
		for (AdministeredQuestionnaireSectionStatus sectionStatus 
				: sectionStatuses) {
			questionSummaries = 
					this.administeredQuestionnaireReportService
					.findQuestionSummariesByQuestionnaireSection(
							sectionStatus.getQuestionnaireSection());
			
			findSectionSummary : for (
					AdministeredQuestionnaireSectionSummary sectionSummary
					: this.administeredQuestionnaireReportService
				.findAdministeredQuestionnaireSectionSummariesByAdministeredQuestionnaire(
						sectionStatus.getAdministeredQuestionnaire())) {
				if (sectionSummary.getQuestionnaireSectionId().equals(
						sectionStatus.getQuestionnaireSection().getId())) {
					sectionQuestionSummaries.put(sectionSummary,
							questionSummaries);
					break findSectionSummary;
				}
			}
		}
		
		for (AdministeredQuestionnaireSectionStatus sectionStatus
				: sectionStatuses) {
			
			List<AllowedQuestion> allowedQuestions =
					this.administeredQuestionnaireService
					.findAllowedQuestionsByQuestionSection(
							sectionStatus.getQuestionnaireSection());
			
			List<QuestionAnswerItem> questionAnswerItems = new
					ArrayList<QuestionAnswerItem>();
			AdministeredQuestionValue administeredQuestionValue;
			List<AdministeredQuestionValue> administeredQuestionValues;
			QuestionAnswerItem item;
			
			
			for (AllowedQuestion allowedQuestion : allowedQuestions) {
				item = new QuestionAnswerItem();
				
				item.setAnswerCardinality(allowedQuestion
						.getAnswerCardinality());
				
				if (allowedQuestion.getAnswerCardinality()
						.equals(AnswerCardinality.SINGLE)) {
					administeredQuestionValue = 
							this.administeredQuestionnaireService
					.findAdministeredQuestionValueByQuestionAndQuestionnaire(
					allowedQuestion.getQuestion(), administeredQuestionnaire);
					if (administeredQuestionValue != null) {
						item.setAnswerValue(administeredQuestionValue
								.getAnswerValue());
						item.setAnswerValueText(administeredQuestionValue
								.getAnswerValueText());
						item.setComments(administeredQuestionValue
								.getComments());
					}
				} else if (allowedQuestion.getAnswerCardinality()
						.equals(AnswerCardinality.MULTIPLE)) {
					administeredQuestionValues
						= this.administeredQuestionnaireService
					.findAdministeredQuestionValuesByQuestionAndQuestionnaire(
							allowedQuestion.getQuestion(), 
							administeredQuestionnaire);
					List<AdministeredQuestionValueItem> admnstrdQstnValueItems =
							new ArrayList<AdministeredQuestionValueItem>();
					
					String comments = null;
					String answerValueText = null;
					
					for (AdministeredQuestionValue value 
							: administeredQuestionValues) {
						AdministeredQuestionValueItem valueItem =
								new AdministeredQuestionValueItem();
						valueItem.setAnswerValue(value.getAnswerValue());
						valueItem.setAdministeredQuestionValue(value);
						admnstrdQstnValueItems.add(valueItem);
						
						if (comments == null && value.getComments() != null) {
							comments = value.getComments();
						}
						if (answerValueText == null
								&& value.getAnswerValueText() != null) {
							answerValueText = value.getAnswerValueText();
						}
						item.setAdministeredQuestionValue(value);
					}
					item.setAdministeredQuestionValueItems(
							admnstrdQstnValueItems);
					item.setComments(comments);
					item.setAnswerValueText(answerValueText);
				}
				item.setAllowedQuestion(allowedQuestion);
				item.setAnswerCardinality(allowedQuestion
						.getAnswerCardinality());
				questionAnswerItems.add(item);
			}
			
			AdministeredQuestionnaireSectionNote sectionNote = 
					this.administeredQuestionnaireService
			.findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
							sectionStatus.getAdministeredQuestionnaire(), 
							sectionStatus.getQuestionnaireSection());
			
			SectionReviewItem sectionReviewItem = new SectionReviewItem();
			
			sectionReviewItem.setQuestionAnswerItems(questionAnswerItems);
			if (sectionNote != null) {
				sectionReviewItem.setSectionNotes(sectionNote.getComments());
			}
			sectionReviewItem.setSectionStatus(sectionStatus);
			
			sectionReviewItems.add(sectionReviewItem);
		}
		AdministeredQuestionnaireSummary administeredQuestionnaireSummary =
				this.administeredQuestionnaireReportService.summarize(
						administeredQuestionnaire);
		
		form.setSectionReviewItems(sectionReviewItems);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_SUMMARY_MODEL_KEY, 
				administeredQuestionnaireSummary);
		map.addAttribute(REVIEW_FORM_MODEL_KEY, form);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY, 
				administeredQuestionnaire);
		map.addAttribute(
				QUESTIONNAIRE_SECTION_QUESTION_SUMMARIES_MODEL_KEY, 
				sectionQuestionSummaries);
		
		return new ModelAndView(REVIEW_VIEW_NAME, map);
	}
	
	/**
	 * Processes QuestionAnswerItems to create, update, or remove administered
	 * question values.
	 * @param items - List of QuestionAnswerItems
	 * @param sectionStatus - Administered Questionnaire Section Status
	 * @throws DuplicateEntityFoundException - When an administeredQuestionValue
	 * already exists
	 */
	private void processQuestionAnswerItems(
			final List<QuestionAnswerItem> items,
			final AdministeredQuestionnaireSectionStatus sectionStatus)
					throws DuplicateEntityFoundException {
		QuestionnaireSection qstnnreSctn =
				sectionStatus.getQuestionnaireSection();
		AdministeredQuestionnaire admnstrdQstnnre =
				sectionStatus.getAdministeredQuestionnaire();
		
		for (QuestionAnswerItem item : items) {
			Question question = item.getAllowedQuestion().getQuestion();
			if (item.getAnswerCardinality().equals(AnswerCardinality.SINGLE)) {
				AdministeredQuestionValue admnstrdQstnVlue =
						item.getAdministeredQuestionValue();
				if (admnstrdQstnVlue != null) {
					//update or remove
					if (item.getAnswerValue() != null) {
						//Database item exists and form item exists, so update
						this.administeredQuestionnaireService
							.editAdministeredQuestionValue(admnstrdQstnVlue,
								admnstrdQstnnre, question, qstnnreSctn,
								item.getAnswerValue(),
								item.getAnswerValueText(), item.getComments());
					} else if (item.getAnswerValue() == null
							&& (!StringUtility.areNullOrEmpty(
									item.getAnswerValueText(),
									item.getComments()))) {
						//Database value exists, form has no answerValue
						//but does have text/comments, so update with 
						//only the text/comments
						this.administeredQuestionnaireService
							.editAdministeredQuestionValue(admnstrdQstnVlue,
								admnstrdQstnnre, question, qstnnreSctn, null,
								item.getAnswerValueText(), item.getComments());
					} else if (item.getAnswerValue() == null
							&& (StringUtility.areNullOrEmpty(
									item.getAnswerValueText(),
									item.getComments()))) {
						//database value exists but form item is empty, remove
						this.administeredQuestionnaireService
							.removeAdministeredQuestionValue(admnstrdQstnVlue);
					}
				} else {
					//create
					if (item.getAnswerValue() != null) {
					//database value does not exist, form item does exist
					//with an answer value, so create it
						this.administeredQuestionnaireService
							.createAdministeredQuestionValue(admnstrdQstnnre,
								question, qstnnreSctn, item.getAnswerValue(),
								item.getAnswerValueText(), item.getComments());
					} else if (item.getAnswerValue() == null
								&& (!StringUtility.areNullOrEmpty(
										item.getAnswerValueText(),
										item.getComments()))) {
						//database value does not exist, form item does exist
						//without an answer value but with text/comments, so 
						//create it with only text/comments
						this.administeredQuestionnaireService
							.createAdministeredQuestionValue(admnstrdQstnnre,
								question, qstnnreSctn, null,
								item.getAnswerValueText(), item.getComments());
					}
				}
			} else if (item.getAnswerCardinality()
					.equals(AnswerCardinality.MULTIPLE)) {
				Boolean hasAnswerValue = false;
				Boolean hasTextOrComments = !StringUtility.areNullOrEmpty(
						item.getComments(), item.getAnswerValueText());
				for (AdministeredQuestionValueItem aqvItem
						: item.getAdministeredQuestionValueItems()) {
					AdministeredQuestionValue admstrdQstnValue =
							aqvItem.getAdministeredQuestionValue();
					AnswerValue aqvAnswerValue = null;
					if (admstrdQstnValue != null) {
						aqvAnswerValue = admstrdQstnValue.getAnswerValue();
					}
					AnswerValue answerValue = aqvItem.getAnswerValue();
					if (aqvAnswerValue != null) {
						//AdministeredQuestionValue already exists
						if (answerValue != null) {
							hasAnswerValue = true;
							//Update AdministeredQuestionValue with the
							//AnswerValue on the form.
							this.administeredQuestionnaireService
								.editAdministeredQuestionValue(admstrdQstnValue,
									admnstrdQstnnre, question, qstnnreSctn,
									answerValue, item.getAnswerValueText(),
									item.getComments());
						} else {
							//No AnswerValue on form, so remove the
							//AdministeredQuestionValue
							this.administeredQuestionnaireService
								.removeAdministeredQuestionValue(
									admstrdQstnValue);
						}
					} else {
						//AdministeredQuestionValue does not exist
						if (answerValue != null) {
							hasAnswerValue = true;
							//There is a new AnswerValue on the form, create
							//new AdministeredQuestionValue with the AnswerValue
							this.administeredQuestionnaireService
								.createAdministeredQuestionValue(
									admnstrdQstnnre, question, qstnnreSctn,
									answerValue, item.getAnswerValueText(),
									item.getComments());
						}
					}
				}
				//Preserves Question comments or Answer Value texts even if
				//no AnswerValues were selected.
				AdministeredQuestionValue aqv =
						this.administeredQuestionnaireService
						.findAdministeredQuestionValueByNoAnswerValue(
								question, admnstrdQstnnre, qstnnreSctn);
				if (!(hasAnswerValue) && hasTextOrComments && aqv == null) {
					this.administeredQuestionnaireService
						.createAdministeredQuestionValue(
							admnstrdQstnnre, question, qstnnreSctn,
							null, item.getAnswerValueText(),
							item.getComments());
				} else if (!(hasAnswerValue) && hasTextOrComments
						&& aqv != null) {
					if (!aqv.getAnswerValueText().equals(
							item.getAnswerValueText())
							|| !aqv.getComments().equals(item.getComments())) {
						this.administeredQuestionnaireService
							.editAdministeredQuestionValue(aqv, admnstrdQstnnre,
									question, qstnnreSctn, null,
									item.getAnswerValueText(),
									item.getComments());
					}
				} else if ((!hasTextOrComments || hasAnswerValue)
						&& aqv != null) {
					this.administeredQuestionnaireService
						.removeAdministeredQuestionValue(aqv);
				}
			}
		}
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
				ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, exception);
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editor factories.
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionnaire.class, 
				this.administeredQuestionnairePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionValue.class, 
				this.administeredQuestionValuePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				AdministeredQuestionnaireSectionStatus.class, 
				this.administeredQuestionnaireSectionStatusPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AnswerValue.class, 
				this.answerValuePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AllowedQuestion.class, 
				this.allowedQuestionPropertyEditorFactory
				.createPropertyEditor());
	}
}
