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
package omis.questionnaire.web.controller;

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
import omis.questionnaire.report.AnswerValueSummary;
import omis.questionnaire.report.QuestionSummary;
import omis.questionnaire.service.AdministeredQuestionnaireService;
import omis.questionnaire.web.form.AdministeredQuestionValueItem;
import omis.questionnaire.web.form.AdministeredQuestionnaireSectionForm;
import omis.questionnaire.web.form.QuestionAnswerItem;
import omis.questionnaire.web.validator.AdministeredQuestionnaireSectionFormValidator;
import omis.util.StringUtility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Questionnaire Section Controller.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.2 (Apr 5, 2018)
 * @since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/questionnaire/")
@PreAuthorize("hasRole('USER')")
public class QuestionnaireSectionController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/questionnaire/editSection";
	
	private static final String QUESTIONNAIRE_SECTION_ACTION_MENU_VIEW_NAME =
			"questionnaire/includes/administeredQuestionnaireSectionActionMenu";
	
	/* Model Keys */
	
	private static final String QUESTION_ANSWER_SUMMARIES_MODEL_KEY = 
			"questionAnswerSummaries";
	
	private static final String QUESTIONNAIRE_SECTION_STATUS_MODEL_KEY =
			"questionnaireSectionStatus";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String FORM_MODEL_KEY = 
			"administeredQuestionnaireSectionForm";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	
	private static final String SECTION_LIST_REDIRECT = 
			"redirect:/questionnaire/sectionList.html?"
			+ "offender=%d&administeredQuestionnaire=%d";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"entity.administeredQuestionnaire.exists";
	
	/* Bundle */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.questionnaire.msgs.form";
	
	/* Services */
	
	@Autowired
	@Qualifier("administeredQuestionnaireService")
	private AdministeredQuestionnaireService administeredQuestionnaireService;
	
	@Autowired
	@Qualifier("administeredQuestionnaireReportService")
	private AdministeredQuestionnaireReportService
		administeredQuestionnaireReportService;
	
	
	/* Property Editors */
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
			administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("administeredQuestionnaireSectionStatusPropertyEditorFactory")
	private PropertyEditorFactory 
		administeredQuestionnaireSectionStatusPropertyEditorFactory;
	
	@Autowired
	@Qualifier("answerValuePropertyEditorFactory")
	private PropertyEditorFactory answerValuePropertyEditorFactory;
	
	@Autowired
	@Qualifier("questionPropertyEditorFactory")
	private PropertyEditorFactory questionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("allowedQuestionPropertyEditorFactory")
	private PropertyEditorFactory allowedQuestionPropertyEditorFactory;
	
	@Autowired
	@Qualifier("administeredQuestionValuePropertyEditorFactory")
	private PropertyEditorFactory
		administeredQuestionValuePropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Helpers */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	private AdministeredQuestionnaireSectionFormValidator formValidator;
	
	
	/**
	 * Default Constructor for QuestionnaireSectionController.
	 */
	public QuestionnaireSectionController() {
		
	}
	
	
	/**
	 * Returns a view for editing (answering) an administered 
	 * questionnaire section.
	 * @param offender - offender
	 * @param sectionStatus - administered questionnaire section status
	 * @return ModelAndView - view for editing (answering) an administered 
	 * questionnaire section
	 */
	@RequestMapping(value = "/editSection.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('QUESTIONNAIRE_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "administeredQuestionnaireSectionStatus", 
			required = true) final AdministeredQuestionnaireSectionStatus
			sectionStatus) {
		if (!sectionStatus.getAdministeredQuestionnaire().getDraft()) {
			//Questionnaire has been finalized and cannot be edited.
			return new ModelAndView(String.format(SECTION_LIST_REDIRECT, 
					offender.getId(),
					sectionStatus.getAdministeredQuestionnaire().getId()));
		}
		
		Boolean returning = false;
		
		List<AllowedQuestion> allowedQuestions =
				this.administeredQuestionnaireService
				.findAllowedQuestionsByQuestionSection(
						sectionStatus.getQuestionnaireSection());
		
		checkReturned : for (AllowedQuestion allowedQuestion
				: allowedQuestions) {
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
			
			return prepareEditMav(offender, sectionStatus, form);
		} else {
			//not returning, create new.
			return prepareEditMav(offender, sectionStatus, 
					new AdministeredQuestionnaireSectionForm());
		}
	}
	
	/**
	 * Attempts to save the questionnaire section (administered question values,
	 * questionnaire section notes, and administered questionnaire section 
	 * status), validating final versions, and returns to the section list view.
	 * @param offender - offender
	 * @param sectionStatus - administered questionnaire section status
	 * @param finalFlag - String
	 * @param form - QuestionnaireSectionForm
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - returns to the edit view on validation errors, or
	 * to the section list view on successful validation/saving
	 * @throws DuplicateEntityFoundException - when any of administered question
	 * values, questionnaire section notes, or administered questionnaire
	 * section status already exists
	 */
	@RequestMapping(value = "/editSection.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('QUESTIONNAIRE_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(
			value = "offender", required = true) final Offender offender,
			@RequestParam(value = "administeredQuestionnaireSectionStatus", 
			required = true) final AdministeredQuestionnaireSectionStatus
			sectionStatus, @RequestParam(required = false, value = "final")
			final String finalFlag,
			final AdministeredQuestionnaireSectionForm form,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		
		//Checking to see which submit button was pressed. 
		//If button named "final" was pressed, then finalFlag will not be null
		//If it is null, then the "draft" button was pressed
		Boolean draft = true;
		if (finalFlag != null) {
			draft = false;
		}
		
		
		//Only if the user submitted a "final" form will it get validated
		if (!draft) {
			this.formValidator.validate(form, bindingResult);
		}
		
		if (bindingResult.hasErrors()) {
			return prepareEditMav(offender, sectionStatus, form);
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
			processQuestionAnswerItems(
					form.getQuestionAnswerItems(), sectionStatus);
			
			//create/update/remove the AdministeredQuestionnaireSectionNote
			if (sectionNote != null) {
				if (form.getSectionComments() != null
						&& !form.getSectionComments().isEmpty()) {
					//note exists in the database and on the form, so update it
					this.administeredQuestionnaireService
						.updateAdministeredQuestionnaireSectionNote(sectionNote,
							questionnaireSection, form.getSectionComments());
				} else if (form.getSectionComments() == null
						|| form.getSectionComments().isEmpty()) {
					//note exists in the database but not on the form, 
					//so remove it
					this.administeredQuestionnaireService
					.removeAdministeredQuestionnaireSectionNote(sectionNote);
				}
			} else {
				if (form.getSectionComments() != null
						&& !form.getSectionComments().isEmpty()) {
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
		return new ModelAndView(String.format(SECTION_LIST_REDIRECT, 
				offender.getId(),
				sectionStatus.getAdministeredQuestionnaire().getId()));
	}
	
	/**
	 * Displays the action menu for questionnaire section screen.
	 * @param offender - offender
	 * @param questionnaire - administered questionnaire
	 * @return ModelAndView - view of action menu for questionnaire 
	 * section screen
	 */
	@RequestMapping(value = "/administeredQuestionnaireSectionActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayQuestionnaireSectionActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender, 
			@RequestParam(value = "administeredQuestionnaire", 
			required = true) final AdministeredQuestionnaire questionnaire) {
		ModelMap map = new ModelMap();
		
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY, questionnaire);
		
		return new ModelAndView(QUESTIONNAIRE_SECTION_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	/**
	 * Returns a prepared Model and View for editing.
	 * @param offender - Offender
	 * @param sectionStatus - administered questionnaire section status
	 * @param form - QuestionnaireSectionForm
	 * @return ModelAndView - for editing a QuestionnaireSection
	 */
	public ModelAndView prepareEditMav(final Offender offender, final
			AdministeredQuestionnaireSectionStatus sectionStatus, final
			AdministeredQuestionnaireSectionForm form) {
		ModelMap map = new ModelMap();
		List<AllowedQuestion> allowedQuestions =
				this.administeredQuestionnaireService
				.findAllowedQuestionsByQuestionSection(
						sectionStatus.getQuestionnaireSection());
		LinkedHashMap<QuestionSummary, List<AnswerValueSummary>> 
				questionAnswerSummaries 
					= new LinkedHashMap<QuestionSummary, 
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
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
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
	 * Sets up and registers property editors.
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
		binder.registerCustomEditor(
				AdministeredQuestionnaireSectionStatus.class, 
				this.administeredQuestionnaireSectionStatusPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AnswerValue.class, 
				this.answerValuePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Question.class, 
				this.questionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AllowedQuestion.class, 
				this.allowedQuestionPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionValue.class, 
				this.administeredQuestionValuePropertyEditorFactory
				.createPropertyEditor());
	}
	
}
