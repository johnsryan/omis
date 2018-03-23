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
import java.util.Date;
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
import omis.assessment.domain.AssessmentNote;
import omis.assessment.service.AssessmentNoteService;
import omis.assessment.web.form.AssessmentItemOperation;
import omis.assessment.web.form.AssessmentNoteForm;
import omis.assessment.web.form.AssessmentNoteItem;
import omis.assessment.web.validator.AssessmentNoteFormValidator;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Assessment Note Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 15, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/assessment/notes")
@PreAuthorize("hasRole('USER')")
public class AssessmentNoteController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/assessment/notes/edit";
	
	private static final String ASSESSMENT_NOTE_ITEMS_ACTION_MENU_VIEW_NAME =
			"/assessment/notes/includes/assessmentNoteItemsActionMenu";
	
	private static final String ASSESSMENT_NOTE_ITEM_ROW_VIEW_NAME =
			"/assessment/notes/includes/assessmentNoteItemTableRow";
	
	/* Model Keys */

	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_NOTE_ITEM_INDEX_MODEL_KEY =
			"assessmentNoteItemIndex";
	
	private static final String ASSESSMENT_NOTE_ITEM_MODEL_KEY =
			"assessmentNoteItem";
	
	private static final String FORM_MODEL_KEY = "assessmentNoteForm";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"assessmentNote.exists";

	private static final String ERROR_BUNDLE_NAME = "omis.assessment.msgs.form";
	
	
	/* Services */
	
	@Autowired
	@Qualifier("assessmentNoteService")
	private AssessmentNoteService assessmentNoteService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
				administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("assessmentNotePropertyEditorFactory")
	private PropertyEditorFactory assessmentNotePropertyEditorFactory;

	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Validator */
	
	@Autowired
	@Qualifier("assessmentNoteFormValidator")
	private AssessmentNoteFormValidator assessmentNoteFormValidator;
	
	/**
	 * Default constructor for Assessment Note Controller.
	 */
	public AssessmentNoteController() {
	}
	
	/**
	 * Returns the Model and View for the Assessment Note edit/list screen.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model and View for the Assessment Note edit/list screen.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_NOTE_CREATE') or "
			+ "hasRole('ASSESSMENT_NOTE_EDIT') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.prepareEditMav(administeredQuestionnaire);
	}
	
	/**
	 * Processes Assessment Note Items and returns to the Assessment Note
	 * edit/list screen.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param form - Assessment Note Form
	 * @param bindingResult - Binding Result
	 * @return Model and View for the Assessment Note edit/list screen.
	 * @throws DuplicateEntityFoundException - When an Assessment Note already
	 * exists with the provided date and description for the specified
	 * Administered Questionnaire.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ASSESSMENT_NOTE_CREATE') or "
			+ "hasRole('ASSESSMENT_NOTE_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(
			value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire,
			final AssessmentNoteForm form,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.assessmentNoteFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			System.out.println("kazoooo");
			return this.prepareEditMav(administeredQuestionnaire, form);
		} else {
			this.processNotes(form.getAssessmentNoteItems(),
					administeredQuestionnaire);
			return this.prepareEditMav(administeredQuestionnaire);
		}
	}
	
	/**
	 * Returns the model and view for a Assessment Note Item Row.
	 * 
	 * @param assessmentNoteItemIndex - Integer Assessment Note Item Index
	 * @return ModelAndView - model and view for a Assessment Note Item Row.
	 */
	@RequestMapping(value = "createAssessmentNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentNoteItemRow(@RequestParam(
			value = "assessmentNoteItemIndex", required = true)
			final Integer assessmentNoteItemIndex) {
		ModelMap map = new ModelMap();
		AssessmentNoteItem noteItem = new AssessmentNoteItem();
		noteItem.setItemOperation(AssessmentItemOperation.CREATE);
		map.addAttribute(ASSESSMENT_NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(ASSESSMENT_NOTE_ITEM_INDEX_MODEL_KEY,
				assessmentNoteItemIndex);
		return new ModelAndView(ASSESSMENT_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns a model and view for assessment note items action menu.
	 * 
	 * @return ModelAndView - model and view for assessment note items
	 * action menu
	 */
	@RequestMapping(value = "assessmentNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				ASSESSMENT_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns a Model and View to show current Assessment Notes for the 
	 * provided Administered Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model and View to show current Assessment Notes for the 
	 * provided Administered Questionnaire.
	 */
	private ModelAndView prepareEditMav(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		AssessmentNoteForm form = new AssessmentNoteForm();
		
		List<AssessmentNoteItem> items = new ArrayList<AssessmentNoteItem>();
		for (AssessmentNote note : this.assessmentNoteService
				.findAssessmentNotesByAdministeredQuestionnaire(
						administeredQuestionnaire)) {
			AssessmentNoteItem item = new AssessmentNoteItem();
			item.setAssessmentNote(note);
			item.setDate(note.getDate());
			item.setDescription(note.getDescription());
			item.setItemOperation(AssessmentItemOperation.UPDATE);
			items.add(item);
		}
		form.setAssessmentNoteItems(items);
		return this.prepareEditMav(administeredQuestionnaire, form);
	}

	/**
	 * Returns a Model and View to show the Assessment Notes for the provided
	 * Administered Questionnaire by Assessment Note Items on the provided
	 * Assessment Note Form.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param form - Assessment Note Form
	 * @return Model and View to show the Assessment Notes for the provided
	 * Administered Questionnaire by Assessment Note Items on the provided
	 * Assessment Note Form.
	 */
	private ModelAndView prepareEditMav(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final AssessmentNoteForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		map.addAttribute(FORM_MODEL_KEY, form);
		map.addAttribute(ASSESSMENT_NOTE_ITEM_INDEX_MODEL_KEY,
				form.getAssessmentNoteItems().size());
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Processes Assment Note Items for creation/updating/removal of Assessment
	 * Notes.
	 * 
	 * @param assessmentNoteItems - Assessment Note Items to process.
	 * @param administeredQuestionnaire - Administered Questionnaire associated
	 * with the Assessment Notes
	 * @throws DuplicateEntityFoundException - When an Assessment Note already
	 * exists with the provided date and description for the specified
	 * Administered Questionnaire.
	 */
	private void processNotes(
			final List<AssessmentNoteItem> assessmentNoteItems,
			final AdministeredQuestionnaire administeredQuestionnaire)
					throws DuplicateEntityFoundException {
		for (AssessmentNoteItem item : assessmentNoteItems) {
			if (AssessmentItemOperation.CREATE.equals(
					item.getItemOperation())) {
				this.assessmentNoteService.createAssessmentNote(
						administeredQuestionnaire, item.getDescription(),
						item.getDate());
			} else if (AssessmentItemOperation.UPDATE.equals(
					item.getItemOperation())) {
				if (this.isNoteChanged(item.getAssessmentNote(), item.getDate(),
						item.getDescription())) {
					this.assessmentNoteService.updateAssessmentNote(
						item.getAssessmentNote(), administeredQuestionnaire,
						item.getDescription(), item.getDate());
				}
			} else if (AssessmentItemOperation.REMOVE.equals(
					item.getItemOperation())) {
				this.assessmentNoteService.removeAssessmentNote(
						item.getAssessmentNote());
			}
		}
	}
	
	/**
	 * Checks if an Assessment Note has been changed and returns true if it
	 * has.
	 * 
	 * @param note - Assessment Note to check for change
	 * @param date - Note Date from Form
	 * @param description - Note Description from Form
	 * @return Boolean - true if either the form's note date or value is
	 * different from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final AssessmentNote note,
			final Date date, final String description) {
		if (!note.getDescription().equals(description)) {
			return true;
		}
		if (!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
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
		binder.registerCustomEditor(AssessmentNote.class,
				this.assessmentNotePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionnaire.class,
				this.administeredQuestionnairePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
	}
}
