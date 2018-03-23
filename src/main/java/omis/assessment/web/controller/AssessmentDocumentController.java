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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.assessment.service.AssessmentDocumentAssociationService;
import omis.assessment.web.form.AssessmentDocumentForm;
import omis.assessment.web.validator.AssessmentDocumentFormValidator;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.io.DocumentPersister;
import omis.document.io.DocumentRetriever;
import omis.document.io.impl.DocumentFilenameGenerator;
import omis.document.web.form.DocumentTagItem;
import omis.document.web.form.DocumentTagOperation;
import omis.exception.DuplicateEntityFoundException;
import omis.io.FileRemover;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Assessment Document Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 9, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/assessment/document")
@PreAuthorize("hasRole('USER')")
public class AssessmentDocumentController {
	
	/* View names */
	
	private static final String EDIT_VIEW_NAME = "/assessment/document/edit";
	
	private static final String ASSESSMENT_DOCUMENT_ACTION_MENU_VIEW_NAME =
			"/assessment/document/includes/assessmentDocumentActionMenu";
	
	private static final String LIST_REDIRECT =
			"redirect:/assessment/document/list.html?"
			+ "administeredQuestionnaire=%d";
	
	/* Model Keys */
	
	private static final String ADMINISTERED_QUESTIONNAIRE_MODEL_KEY =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_DOCUMENT_ASSOCIATION_MODEL_KEY =
			"assessmentDocumentAssociation";
	
	private static final String FORM_MODEL_KEY = "assessmentDocumentForm";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY =
			"assessmentDocumentAssociation.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
	private static final String ERROR_BUNDLE_NAME = "omis.assessment.msgs.form";
	
	/* Services */
	
	@Autowired
	@Qualifier("assessmentDocumentAssociationService")
	private AssessmentDocumentAssociationService
					assessmentDocumentAssociationService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("administeredQuestionnairePropertyEditorFactory")
	private PropertyEditorFactory
				administeredQuestionnairePropertyEditorFactory;
	
	@Autowired
	@Qualifier("assessmentDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory
				assessmentDocumentAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("documentFilenameGenerator")
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("assessmentDocumentPersister")
	private DocumentPersister assessmentDocumentPersister;
	
	@Autowired
	@Qualifier("assessmentDocumentRetriever")
	private DocumentRetriever assessmentDocumentRetriever;
	
	@Autowired
	@Qualifier("assessmentDocumentRemover")
	private FileRemover assessmentDocumentRemover;
	
	/* Validator */
	
	@Autowired
	@Qualifier("assessmentDocumentFormValidator")
	private AssessmentDocumentFormValidator assessmentDocumentFormValidator;
	
	/**
	 * Default Constructor for Assessment Document Controller. 
	 */
	public AssessmentDocumentController() {
	}
	
	
	/**
	 * Displays the Model and View for the Assessment Document create screen.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model and View for the Assessment Document create screen.
	 */
	@RequestMapping(value = "/create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_DOCUMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.prepareEditMav(administeredQuestionnaire);
	}
	
	/**
	 * Saves an Assessment Document Association and returns to the Assessment
	 * Documents list screen.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param form - Assessment Document Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - List screen on successful save, or back to the
	 * create screen on form error.
	 * @throws DuplicateEntityFoundException - When a duplicate Assessment
	 * Document Association, Document, or Document Tag already exists.
	 */
	@RequestMapping(value = "/create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ASSESSMENT_DOCUMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(
			value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire,
			final AssessmentDocumentForm form,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.assessmentDocumentFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return prepareEditMav(administeredQuestionnaire, form);
		} else {
			final String fileExtension = form.getFileExtension();
			this.documentFilenameGenerator
					.setExtension(fileExtension);
			final String filename =
					this.documentFilenameGenerator.generate();
			Document document = this.assessmentDocumentAssociationService
					.createDocument(form.getDate(), filename, fileExtension,
							form.getTitle());
			this.assessmentDocumentPersister.persist(document, form.getData());
			this.processDocumentTags(form.getDocumentTagItems(), document);
			
			this.assessmentDocumentAssociationService
				.createAssessmentDocumentAssociation(document, form.getDate(),
					administeredQuestionnaire);
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					administeredQuestionnaire.getId()));
		}
	}
	

	/**
	 * Displays the Model and View for the Assessment Document edit screen.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @return Model and View for the Assessment Document edit screen.
	 */
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_DOCUMENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "assessmentDocumentAssociation", required = true)
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		return this.prepareEditMav(assessmentDocumentAssociation);
	}
	
	/**
	 * Updates an Assessment Document Association and returns to the Assessment
	 * Documents list screen.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @param form - Assessment Document Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - List screen on successful update, or back to the
	 * edit screen on form error.
	 * @throws DuplicateEntityFoundException - When a duplicate Assessment
	 * Document Association, Document, or Document Tag already exists.
	 */
	@RequestMapping(value = "/edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ASSESSMENT_DOCUMENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(
			value = "assessmentDocumentAssociation", required = true)
			final AssessmentDocumentAssociation assessmentDocumentAssociation,
			final AssessmentDocumentForm form,
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.assessmentDocumentFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return prepareEditMav(assessmentDocumentAssociation, form);
		} else {
			Document document = this.assessmentDocumentAssociationService
					.updateDocument(assessmentDocumentAssociation.getDocument(),
							form.getDate(), form.getTitle());
			this.processDocumentTags(form.getDocumentTagItems(), document);
			this.assessmentDocumentAssociationService
				.updateAssessmentDocumentAssociation(
						assessmentDocumentAssociation, document, form.getDate(),
						assessmentDocumentAssociation
							.getAdministeredQuestionnaire());
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					assessmentDocumentAssociation
					.getAdministeredQuestionnaire().getId()));
		}
	}
	
	/**
	 * Removes the specified Assessment Document Association and returns to the
	 * Assessment Document list screen.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @return ModelAndView - Returns to the Assessment Document list screen.
	 */
	@RequestMapping(value = "/remove.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ASSESSMENT_DOCUMENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "assessmentDocumentAssociation", 
			required = true)
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		List<DocumentTag> documentTags =
				this.assessmentDocumentAssociationService
					.findDocumentTagsByDocument(assessmentDocumentAssociation
						.getDocument());
		for (DocumentTag documentTag : documentTags) {
			this.assessmentDocumentAssociationService
				.removeDocumentTag(documentTag);
		}
		this.assessmentDocumentAssociationService
			.removeAssessmentDocumentAssociation(assessmentDocumentAssociation);
		this.assessmentDocumentRemover.remove(assessmentDocumentAssociation
				.getDocument().getFilename());
		this.assessmentDocumentAssociationService.removeDocument(
				assessmentDocumentAssociation.getDocument());
		
		return new ModelAndView(String.format(LIST_REDIRECT,
				assessmentDocumentAssociation
				.getAdministeredQuestionnaire().getId()));
	}
	
	/**
	 * Displays the Model and View for the Assessment Document action menu.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model and View for the Assessment Document action menu.
	 */
	@RequestMapping(value = "/assessmentDocumentActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayAssessmentDocumentActionMenu(@RequestParam(
			value = "administeredQuestionnaire", required = true)
			final AdministeredQuestionnaire administeredQuestionnaire) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		
		return new ModelAndView(ASSESSMENT_DOCUMENT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Helpers Methods */
	
	/**
	 * Returns the model and view for editing an Assessment Document
	 * Association.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @return Model and view for editing an Assessment Document
	 * Association.
	 */
	private ModelAndView prepareEditMav(
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		return this.prepareEditMav(assessmentDocumentAssociation,
				this.populateForm(assessmentDocumentAssociation));
	}

	/**
	 * Returns the model and view for editing an Assessment Document
	 * Association with a prepared Assessment Document Form.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @param form - Assessment Document Form
	 * @return Model and view for editing an Assessment Document
	 * Association with a prepared Assessment Document Form.
	 */
	private ModelAndView prepareEditMav(
			final AssessmentDocumentAssociation assessmentDocumentAssociation,
			final AssessmentDocumentForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(ASSESSMENT_DOCUMENT_ASSOCIATION_MODEL_KEY,
				assessmentDocumentAssociation);
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				assessmentDocumentAssociation.getAdministeredQuestionnaire());
		map.addAttribute(FORM_MODEL_KEY, form);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Returns the model and view for creating an Assessment Document
	 * Association.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return Model and view for creating an Assessment Document
	 * Association.
	 */
	private ModelAndView prepareEditMav(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.prepareEditMav(administeredQuestionnaire,
				new AssessmentDocumentForm());
	}
	
	/**
	 * Returns the model and view for creating an Assessment Document
	 * Association with a prepared Assessment Document Form.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param form - Assessment Document Form
	 * @return Model and view for creating an Assessment Document
	 * Association with a prepared Assessment Document Form.
	 */
	private ModelAndView prepareEditMav(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final AssessmentDocumentForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(ADMINISTERED_QUESTIONNAIRE_MODEL_KEY,
				administeredQuestionnaire);
		map.addAttribute(FORM_MODEL_KEY, form);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	
	/**
	 * Returns a populated Assessment Document Form based on the provided
	 * Assessment Document Association.
	 * 
	 * @param assessmentDocumentAssociation - Assessment Document Association
	 * @return Populated Assessment Document Form based on the provided
	 * Assessment Document Association.
	 */
	private AssessmentDocumentForm populateForm(
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		AssessmentDocumentForm form = new AssessmentDocumentForm();
		List<DocumentTag> documentTags =
				this.assessmentDocumentAssociationService
				.findDocumentTagsByDocument(
						assessmentDocumentAssociation.getDocument());
		List<DocumentTagItem> documentTagItems =
				new ArrayList<DocumentTagItem>();
		for (DocumentTag tag : documentTags) {
			DocumentTagItem item = new DocumentTagItem();
			
			item.setDocumentTag(tag);
			item.setName(tag.getName());
			item.setDocumentTagOperation(DocumentTagOperation.UPDATE);
			
			documentTagItems.add(item);
		}
		
		form.setDate(assessmentDocumentAssociation.getDocument().getDate());
		form.setDocument(assessmentDocumentAssociation.getDocument());
		form.setDocumentTagItems(documentTagItems);
		form.setFileExtension(assessmentDocumentAssociation.getDocument()
				.getFileExtension());
		form.setTitle(assessmentDocumentAssociation.getDocument().getTitle());
		form.setData(this.assessmentDocumentRetriever.retrieve(
				assessmentDocumentAssociation.getDocument()));
		
		return form;
	}

	/**
	 * Processes a list of documentTag items for creation, updating, or removal.
	 * 
	 * @param documentTagItems - List of DocumentTagItems
	 * @param document - Document for which the documentTags are being processed
	 * for
	 * @throws DuplicateEntityFoundException - When a document tag already
	 * exists with given name and document
	 */
	private void processDocumentTags(
			final List<DocumentTagItem> documentTagItems, 
			final Document document) throws DuplicateEntityFoundException {
		if (documentTagItems != null) {
			Iterator<DocumentTagItem> documentTagIterator = 
					documentTagItems.iterator();
			while (documentTagIterator.hasNext()) {
				final DocumentTagItem documentTagItem =
						documentTagIterator.next();
				final DocumentTagOperation documentTagOperation = 
						documentTagItem.getDocumentTagOperation();
				if (DocumentTagOperation.UPDATE.equals(
						documentTagOperation)) {
					this.assessmentDocumentAssociationService.updateDocumentTag(
							documentTagItem.getDocumentTag(), 
							documentTagItem.getName());
				} else if (DocumentTagOperation.CREATE.equals(
						documentTagOperation)) {
					this.assessmentDocumentAssociationService.createDocumentTag(
							document, documentTagItem.getName());
				} else if (DocumentTagOperation.REMOVE.equals(
						documentTagOperation)) {
					this.assessmentDocumentAssociationService.removeDocumentTag(
							documentTagItem.getDocumentTag());
				}
			}
		}
	}
	
	/** 
	 * Retrieves document file.
	 * 
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response. */
	@RequestMapping(value = "/retrieveFile.html",
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.assessmentDocumentRetriever
				.retrieve(document);
		httpServletResponse.setContentType("application/octet-stream");
		httpServletResponse.setHeader("Content-Disposition", 
				"attachment; filename=\"" + document.getFilename() + "\"");
		try {
			OutputStream outputStream = httpServletResponse.getOutputStream();
			outputStream.write(bytes);
			outputStream.flush();
			outputStream.close();
		} catch (IOException ioException) {
			throw new RuntimeException(String.format(ERROR_WRITING_FILE_MSG, 
					document.getFilename()));
		}
	}
	
	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception - duplicate entity found exception
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
		binder.registerCustomEditor(AssessmentDocumentAssociation.class,
				this.assessmentDocumentAssociationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AdministeredQuestionnaire.class,
				this.administeredQuestionnairePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
}
