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
package omis.medicalreview.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
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
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;
import omis.medicalreview.domain.MedicalReviewNote;
import omis.medicalreview.service.MedicalReviewService;
import omis.medicalreview.web.form.MedicalReviewDocumentAssociationItem;
import omis.medicalreview.web.form.MedicalReviewForm;
import omis.medicalreview.web.form.MedicalReviewItemOperation;
import omis.medicalreview.web.form.MedicalReviewNoteItem;
import omis.medicalreview.web.validator.MedicalReviewFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Medical Review Controller.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 1, 2018)
 *@since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/medicalReview")
@PreAuthorize("hasRole('USER')")
public class MedicalReviewController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/medicalReview/edit";
	
	private static final String MEDICAL_REVIEW_ACTION_MENU_VIEW_NAME =
			"/medicalReview/includes/medicalReviewActionMenu";
	
	private static final String MEDICAL_REVIEW_NOTE_ITEM_ROW_VIEW_NAME =
			"/medicalReview/includes/medicalReviewNoteItemTableRow";
	
	private static final String
		MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME =
		"/medicalReview/includes/medicalReviewDocumentAssociationItemContent";
	
	private static final String DOCUMENT_TAG_ITEM_ROW_VIEW_NAME =
			"/medicalReview/includes/documentTagItemContent";
	
	private static final String
		MEDICAL_REVIEW_NOTE_ITEMS_ACTION_MENU_VIEW_NAME =
			"/medicalReview/includes/medicalReviewNoteItemsActionMenu";
	
	private static final String LIST_REDIRECT_VIEW_NAME =
			"redirect:/medicalReview/list.html?offender=%d";
	
	
	/* Model Keys */
	
	private static final String MEDICAL_REVIEW_FORM_MODEL_KEY =
			"medicalReviewForm";
	
	private static final String HEALTH_CLASSIFICATIONS_MODEL_KEY =
			"healthClassifications";
	
	private static final String MEDICAL_REVIEW_MODEL_KEY = "medicalReview";
	
	private static final String OFFENDER_MODEL_KEY = "offender";

	private static final String
		MEDICAL_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY =
			"medicalReviewNoteItemIndex";
	
	private static final String
		MEDICAL_REVIEW_NOTE_ITEM_MODEL_KEY =
			"medicalReviewNoteItem";
	
	private static final String
		MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY =
			"medicalReviewDocumentAssociationItemIndex";
	
	private static final String
		MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY =
			"medicalReviewDocumentAssociationItem";
	
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY =
			"documentTagItem";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY =
			"documentTagItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	/* Message Keys */
	
	private static final String MEDICAL_REVIEW_EXISTS_MESSAGE_KEY =
			"entity.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.medicalreview.msgs.form";

	/* Delegates */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("medicalReviewDocumentPersister")
	private DocumentPersister medicalReviewDocumentPersister;
	
	@Autowired
	@Qualifier("medicalReviewDocumentRetriever")
	private DocumentRetriever medicalReviewDocumentRetriever;
	
	@Autowired
	@Qualifier("medicalReviewDocumentRemover")
	private FileRemover medicalReviewDocumentRemover;
	
	/* Services. */
	
	@Autowired
	@Qualifier("medicalReviewService")
	private MedicalReviewService medicalReviewService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("medicalReviewPropertyEditorFactory")
	private PropertyEditorFactory medicalReviewPropertyEditorFactory;
	
	@Autowired
	@Qualifier("medicalReviewNotePropertyEditorFactory")
	private PropertyEditorFactory medicalReviewNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("medicalReviewDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory
		medicalReviewDocumentAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("medicalHealthClassificationPropertyEditorFactory")
	private PropertyEditorFactory
		medicalHealthClassificationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;

	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	/* Validator */
	
	@Autowired
	@Qualifier("medicalReviewFormValidator")
	private MedicalReviewFormValidator medicalReviewFormValidator;
	
	/**
	 * Default constructor for Medical Review Controller. 
	 */
	public MedicalReviewController() {
	}
	
	/**
	 * Returns the Model and View for creating a Medical Review.
	 * 
	 * @param offender - Offender
	 * @return ModelAndView - Model And View for creating a Medical Review.
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		return this.prepareEditMav(offender);
	}
	
	/**
	 * Creates a Medical Review and returns the model and view for the
	 * Medical Review list screen.
	 * 
	 * @param offender - Offender
	 * @param form - Medical Review Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - redirects to the Medical Review list screen on
	 * successful Medical Review creation, or back to the creation screen on
	 * form error.
	 * @throws DuplicateEntityFoundException - When a Medical Review, Medical
	 * Review Note, or Medical Review Document Association already exist
	 * with the given properties.
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender,
			final MedicalReviewForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.medicalReviewFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(offender, form);
		} else {
			MedicalReview medicalReview = this.medicalReviewService
					.createMedicalReview(offender, form.getDate(),
							form.getText(), form.getHealthClassification());
			this.processItems(medicalReview, form);
			return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
					offender.getId()));
		}
	}
	
	/**
	 * Returns the Model and View for editing a Medical Review.
	 * 
	 * @param medicalReview - Medical Review being edited
	 * @return ModelAndView - Model and View for editing a Medical Review.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "medicalReview", required = true)
				final MedicalReview medicalReview) {
		return this.prepareEditMav(medicalReview);
	}
	
	/**
	 * Updates a Medical Review and returns the model and view for the
	 * Medical Review list screen.
	 * 
	 * @param medicalReview - Medical Review being updated
	 * @param form - Medical Review Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - redirects to the Medical Review list screen on
	 * successful Medical Review update, or back to the edit screen on
	 * form error.
	 * @throws DuplicateEntityFoundException - When a Medical Review, Medical
	 * Review Note, or Medical Review Document Association already exist
	 * with the given properties.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "medicalReview", required = true)
			final MedicalReview medicalReview,
			final MedicalReviewForm form, final BindingResult bindingResult)
					throws DuplicateEntityFoundException {
		this.medicalReviewFormValidator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(medicalReview, form);
		} else {
			this.medicalReviewService.updateMedicalReview(medicalReview,
					medicalReview.getOffender(), form.getDate(), form.getText(),
					form.getHealthClassification());
			this.processItems(medicalReview, form);
			return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
					medicalReview.getOffender().getId()));
		}
	}
	
	/**
	 * Removes the specified Medical Review and its related entities and
	 * returns the model and view for the Medical Review list screen.
	 * 
	 * @param medicalReview - Medical Review to remove
	 * @return ModelAndView - model and view for the Medical Review list screen.
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_REMOVE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "medicalReview", required = true)
			final MedicalReview medicalReview) {
		for (MedicalReviewNote note : this.medicalReviewService
				.findMedicalReviewNotesByMedicalReview(medicalReview)) {
			this.medicalReviewService.removeMedicalReviewNote(note);
		}
		for (MedicalReviewDocumentAssociation documentAssociation 
				: this.medicalReviewService
				.findReviewDocumentAssociationsByMedicalReview(medicalReview)) {
			for (DocumentTag tag : this.medicalReviewService
					.findDocumentTagsByDocument(
							documentAssociation.getDocument())) {
				this.medicalReviewService.removeDocumentTag(tag);
			}
			this.medicalReviewService.removeMedicalReviewDocumentAssociation(
					documentAssociation);
			this.medicalReviewDocumentRemover.remove(
					documentAssociation.getDocument().getFilename());
			this.medicalReviewService.removeDocument(
					documentAssociation.getDocument());
		}
		this.medicalReviewService.removeMedicalReview(medicalReview);
		return new ModelAndView(String.format(LIST_REDIRECT_VIEW_NAME,
				medicalReview.getOffender().getId()));
	}
	
	/**
	 * Returns the model and view for a Medical Review Note item row.
	 * 
	 * @param medicalReviewNoteItemIndex - Integer
	 * @return ModelAndView - model and view for a Medical Review Note item row.
	 */
	@RequestMapping(value = "createMedicalReviewNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayMedicalReviewNoteItemRow(@RequestParam(
			value = "medicalReviewNoteItemIndex", required = true)
			final Integer medicalReviewNoteItemIndex) {
		ModelMap map = new ModelMap();
		MedicalReviewNoteItem noteItem = new MedicalReviewNoteItem();
		noteItem.setItemOperation(MedicalReviewItemOperation.CREATE);
		map.addAttribute(MEDICAL_REVIEW_NOTE_ITEM_MODEL_KEY, noteItem);
		map.addAttribute(MEDICAL_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY,
				medicalReviewNoteItemIndex);
		return new ModelAndView(MEDICAL_REVIEW_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	
	/**
	 * Returns the model and view for a Medical Review Document Association
	 * item.
	 * 
	 * @param medicalReviewDocumentAssociationItemIndex - Integer
	 * @return ModelAndView - model and view for a Medical Review Document
	 * Association item.
	 */
	@RequestMapping(value = "createMedicalReviewDocumentAssociationItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayMedicalReviewDocumentAssociationItemRow(
			@RequestParam(value = "medicalReviewDocumentAssociationItemIndex",
			required = true)
			final Integer medicalReviewDocumentAssociationItemIndex) {
		ModelMap map = new ModelMap();
		MedicalReviewDocumentAssociationItem item =
				new MedicalReviewDocumentAssociationItem();
		item.setItemOperation(MedicalReviewItemOperation.CREATE);
		map.addAttribute(
				MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY, item);
		map.addAttribute(
				MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				medicalReviewDocumentAssociationItemIndex);
		return new ModelAndView(
				MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME, map);
	}
	
	/**
	 * Returns the Model and View for a Document Tag Item Row.
	 * 
	 * @param medicalReviewDocumentAssociationItemIndex - Integer
	 * @param documentTagItemIndex - Integer
	 * @return ModelAndView - Model and View for a Document Tag Item Row.
	 */
	@RequestMapping(value = "createDocumentTagItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDocumentTagItemRow(
			@RequestParam(value = "medicalReviewDocumentAssociationItemIndex",
			required = true)
			final Integer medicalReviewDocumentAssociationItemIndex,
			@RequestParam(value = "documentTagItemIndex", required = true)
					final Integer documentTagItemIndex) {
		ModelMap map = new ModelMap();
		DocumentTagItem tagItem = new DocumentTagItem();
		tagItem.setDocumentTagOperation(DocumentTagOperation.CREATE);
		map.addAttribute(
				MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
					medicalReviewDocumentAssociationItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_ROW_VIEW_NAME,
				map);
	}
	
	/* Action Menus */
	
	/**
	 * Displays the model and view for the Medical Review action menu.
	 * 
	 * @param offender - Offender
	 * @return ModelAndView - model and view for the Medical Review action menu
	 */
	@RequestMapping(value = "medicalReviewActionMenu.html",
					method = RequestMethod.GET)
	@PreAuthorize("hasRole('MEDICAL_REVIEW_LIST') or hasRole('ADMIN')")
	public ModelAndView medicalReviewActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		return new ModelAndView(MEDICAL_REVIEW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Displays the model and view for the Medical Review Note Items Action
	 * menu.
	 * 
	 * @return ModelAndView - model and view for the Medical Review Note Items
	 * Action menu.
	 */
	@RequestMapping(value = "medicalReviewNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayMedicalReviewNoteItemsActionMenu() {
		ModelMap map = new ModelMap();	
		return new ModelAndView(
				MEDICAL_REVIEW_NOTE_ITEMS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* Helper Methods */
	
	/**
	 * Returns the model and view for the initial display of the create screen
	 * for a Medical Review.
	 * 
	 * @param offender - Offender
	 * @return ModelAndView - model and view for the initial display of the
	 * create screen for a Medical Review.
	 */
	private ModelAndView prepareEditMav(final Offender offender) {
		ModelMap map = new ModelMap();
		return this.prepareEditMav(offender, new MedicalReviewForm(), map);
	}
	
	/**
	 * Returns the model and view for redisplaying the create screen for a
	 * Medical Review.
	 * 
	 * @param offender - Offender
	 * @param form - Medical Review Form
	 * @return ModelAndView - model and view for redisplaying the create screen
	 * for a Medical Review.
	 */
	private ModelAndView prepareEditMav(final Offender offender,
			final MedicalReviewForm form) {
		ModelMap map = new ModelMap();
		return this.prepareEditMav(offender, form, map);
	}
	
	/**
	 * Returns the model and view for the initial display of the edit screen
	 * for a Medical Review.
	 * 
	 * @param medicalReview - Medical Review being edited
	 * @return ModelAndView - model and view for the initial display of the
	 * edit screen for a Medical Review.
	 */
	private ModelAndView prepareEditMav(final MedicalReview medicalReview) {
		ModelMap map = new ModelMap();
		map.addAttribute(MEDICAL_REVIEW_MODEL_KEY, medicalReview);
		return this.prepareEditMav(medicalReview.getOffender(),
				this.prepareForm(medicalReview), map);
	}
	
	/**
	 * Returns the model and view for redisplaying the edit screen for a
	 * Medical Review.
	 * 
	 * @param medicalReview - Medical Review to be edited
	 * @param form - Medical Review Form
	 * @return ModelAndView - model and view for redisplaying the edit screen
	 * for a Medical Review.
	 */
	private ModelAndView prepareEditMav(final MedicalReview medicalReview,
			final MedicalReviewForm form) {
		ModelMap map = new ModelMap();
		map.addAttribute(MEDICAL_REVIEW_MODEL_KEY, medicalReview);
		return this.prepareEditMav(medicalReview.getOffender(), form, map);
	}

	/**
	 * Returns the model and view for the create/edit screen of a
	 * Medical Review.
	 * 
	 * @param offender - Offender
	 * @param medicalReviewForm - Medical Review Form
	 * @param map - Model Map
	 * @return ModelAndView - model and view for the create/edit screen of a
	 * Medical Review.
	 */
	private ModelAndView prepareEditMav(final Offender offender,
			final MedicalReviewForm form, final ModelMap map) {
		List<Integer> documentTagIndexes = new ArrayList<Integer>();
		for (MedicalReviewDocumentAssociationItem item 
				: form.getMedicalReviewDocumentAssociationItems()) {
			documentTagIndexes.add(
					form.getMedicalReviewDocumentAssociationItems()
						.indexOf(item), item.getTagItems().size());
		}
		map.addAttribute(MEDICAL_REVIEW_FORM_MODEL_KEY, form);
		map.addAttribute(HEALTH_CLASSIFICATIONS_MODEL_KEY,
				this.medicalReviewService.findMedicalHealthClassifications());
		map.addAttribute(MEDICAL_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY,
				form.getMedicalReviewNoteItems().size());
		map.addAttribute(
				MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				form.getMedicalReviewDocumentAssociationItems().size());
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY,
				documentTagIndexes);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}

	/**
	 * Returns a Medical Review Form populated by the specified Medical Review.
	 * 
	 * @param medicalReview - Medical Review
	 * @return MedicalReviewForm - Medical Review Form populated by the
	 * specified Medical Review.
	 */
	private MedicalReviewForm prepareForm(final MedicalReview medicalReview) {
		MedicalReviewForm form = new MedicalReviewForm();
		List<MedicalReviewNoteItem> noteItems =
				new ArrayList<MedicalReviewNoteItem>();
		List<MedicalReviewDocumentAssociationItem> documentAssociations =
				new ArrayList<MedicalReviewDocumentAssociationItem>();
		form.setDate(medicalReview.getDate());
		form.setHealthClassification(medicalReview.getHealthClassification());
		form.setText(medicalReview.getText());
		for (MedicalReviewNote note : this.medicalReviewService
				.findMedicalReviewNotesByMedicalReview(medicalReview)) {
			MedicalReviewNoteItem item = new MedicalReviewNoteItem();
			item.setDate(note.getDate());
			item.setDescription(note.getDescription());
			item.setMedicalReviewNote(note);
			item.setItemOperation(MedicalReviewItemOperation.UPDATE);
			noteItems.add(item);
		}
		for (MedicalReviewDocumentAssociation documentAssociation
				: this.medicalReviewService
				.findReviewDocumentAssociationsByMedicalReview(medicalReview)) {
			MedicalReviewDocumentAssociationItem item =
					new MedicalReviewDocumentAssociationItem();
			List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>(); 
			item.setData(this.medicalReviewDocumentRetriever.retrieve(
					documentAssociation.getDocument()));
			item.setDate(documentAssociation.getDocument().getDate());
			item.setDocument(documentAssociation.getDocument());
			item.setFileExtension(documentAssociation.getDocument()
					.getFileExtension());
			item.setItemOperation(MedicalReviewItemOperation.UPDATE);
			item.setMedicalReviewDocumentAssociation(documentAssociation);
			item.setTitle(documentAssociation.getDocument().getTitle());
			for (DocumentTag tag : this.medicalReviewService
					.findDocumentTagsByDocument(item.getDocument())) {
				DocumentTagItem tagItem = new DocumentTagItem();
				tagItem.setDocumentTag(tag);
				tagItem.setDocumentTagOperation(DocumentTagOperation.UPDATE);
				tagItem.setName(tag.getName());
				tagItems.add(tagItem);
			}
			item.setTagItems(tagItems);
			documentAssociations.add(item);
		}
		form.setMedicalReviewDocumentAssociationItems(documentAssociations);
		form.setMedicalReviewNoteItems(noteItems);
		
		return form;
	}
	
	private void processItems(final MedicalReview medicalReview,
			final MedicalReviewForm form)
					throws DuplicateEntityFoundException {
		for (MedicalReviewNoteItem item : form.getMedicalReviewNoteItems()) {
			if (MedicalReviewItemOperation.CREATE.equals(
					item.getItemOperation())) {
				this.medicalReviewService.createMedicalReviewNote(medicalReview,
						item.getDescription(), item.getDate());
			} else if (MedicalReviewItemOperation.UPDATE.equals(
					item.getItemOperation())) {
				if (this.isNoteChanged(item.getMedicalReviewNote(),
						item.getDate(), item.getDescription())) {
					this.medicalReviewService.updateMedicalReviewNote(
							item.getMedicalReviewNote(), medicalReview,
							item.getDescription(), item.getDate());
				}
			}  else if (MedicalReviewItemOperation.REMOVE.equals(
					item.getItemOperation())) {
				this.medicalReviewService.removeMedicalReviewNote(
						item.getMedicalReviewNote());
			}
		}
		for (MedicalReviewDocumentAssociationItem item
				: form.getMedicalReviewDocumentAssociationItems()) {
			if (MedicalReviewItemOperation.CREATE.equals(
					item.getItemOperation())) {
				final String fileExtension = item.getFileExtension();
				this.documentFilenameGenerator
						.setExtension(fileExtension);
				final String filename =
						this.documentFilenameGenerator.generate();
				Document document = this.medicalReviewService
						.createDocument(item.getDate(),
								filename, item.getFileExtension(),
								item.getTitle());
				this.medicalReviewDocumentPersister
						.persist(document, item.getData());
				this.medicalReviewService
					.createMedicalReviewDocumentAssociation(
							medicalReview, document);
				if (item.getTagItems() != null) {
					for (DocumentTagItem tagItem : item.getTagItems()) {
						if (DocumentTagOperation.CREATE.equals(
								tagItem.getDocumentTagOperation())) {
							this.medicalReviewService.createDocumentTag(
									document, tagItem.getName());
						}
					}
				}
			} else if (MedicalReviewItemOperation.UPDATE.equals(
					item.getItemOperation())) {
				Document document = this.medicalReviewService
						.updateDocument(item.getDocument(),
								item.getDate(), item.getTitle());
				this.medicalReviewService
					.updateMedicalReviewDocumentAssociation(
						item.getMedicalReviewDocumentAssociation(),
						medicalReview, document);
				if (item.getTagItems() != null) {
					for (DocumentTagItem tagItem : item.getTagItems()) {
						if (DocumentTagOperation.CREATE.equals(
								tagItem.getDocumentTagOperation())) {
							this.medicalReviewService.createDocumentTag(
									document, tagItem.getName());
						}
						if (DocumentTagOperation.UPDATE.equals(
								tagItem.getDocumentTagOperation())) {
							this.medicalReviewService.updateDocumentTag(
									tagItem.getDocumentTag(),
									tagItem.getName());
						}
						if (DocumentTagOperation.REMOVE.equals(
								tagItem.getDocumentTagOperation())) {
							this.medicalReviewService.removeDocumentTag(
									tagItem.getDocumentTag());
						}
					}
				}
			} else if (MedicalReviewItemOperation.REMOVE.equals(
					item.getItemOperation())) {
				for (DocumentTag tag : this.medicalReviewService
						.findDocumentTagsByDocument(item.getDocument())) {
					this.medicalReviewService.removeDocumentTag(tag);
				}
				this.medicalReviewService
					.removeMedicalReviewDocumentAssociation(
						item.getMedicalReviewDocumentAssociation());
				this.medicalReviewDocumentRemover.remove(
						item.getDocument().getFilename());
				this.medicalReviewService.removeDocument(
						item.getDocument());
			}
		}
	}
	
	/**
	 * Checks if a Medical Review Note has been changed and returns true if it
	 * has.
	 * 
	 * @param note - Medical Review Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is
	 * different from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final MedicalReviewNote note,
			final Date date, final String value) {
		if (!note.getDescription().equals(value)) {
			return true;
		}
		if (!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves document file.
	 * 
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response.
	 */
	@RequestMapping(value = "retrieveFile.html", method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.medicalReviewDocumentRetriever
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
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				MEDICAL_REVIEW_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/** Init binder.
	 * @param binder - web data binder. */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(MedicalReview.class,
				this.medicalReviewPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(MedicalReviewNote.class,
				this.medicalReviewNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(MedicalReviewDocumentAssociation.class,
				this.medicalReviewDocumentAssociationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(MedicalHealthClassification.class,
				this.medicalHealthClassificationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Document.class,
				this.documentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class,
				this.documentTagPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
}
