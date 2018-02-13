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
package omis.mentalhealthreview.web.controller;

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
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.io.FileRemover;
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;
import omis.mentalhealthreview.service.MentalHealthReviewService;
import omis.mentalhealthreview.web.form.DocumentTagItem;
import omis.mentalhealthreview.web.form.MentalHealthNoteItem;
import omis.mentalhealthreview.web.form.MentalHealthReviewDocumentAssociationItem;
import omis.mentalhealthreview.web.form.MentalHealthReviewForm;
import omis.mentalhealthreview.web.form.MentalHealthReviewItemOperation;
import omis.mentalhealthreview.web.validator.MentalHealthReviewFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing mental health reviews.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/mentalHealthReview")
public class ManageMentalHealthReviewController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "mentalHealthReview/edit";

	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"mentalHealthReview/includes/mentalHealthReviewActionMenu";
	
	private static final String 
			MENTAL_HEALTH_REVIEW_NOTE_ITEMS_ACTION_MENU_VIEW_NAME = 
			"/mentalHealthReview/includes/mentalHealthReviewNoteItemsActionMenu";
	
	/* Partial views. */
	
	private static final String DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME = 
			"mentalHealthReview/includes/documentAssociationItemContent";
	
	private static final String DOCUMENT_TAG_ITEM_VIEW_NAME = 
			"mentalHealthReview/includes/documentAssociationTagItemContent";

	private static final String MENTAL_HEALTH_REVIEW_NOTE_ITEM_ROW_VIEW_NAME = 
			"mentalHealthReview/includes/mentalHealthReviewNoteTableRow";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/mentalHealthReview/list.html?offender=%d";
	
	/* Model keys. */
	
	private static final String MENTAL_HEALTH_REVIEW_FORM_MODEL_KEY = 
			"mentalHealthReviewForm";
	
	private static final String MENTAL_HEALTH_REVIEW_MODEL_KEY = 
			"mentalHealthReview";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String 
			MENTAL_HEALTH_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY = 
			"mentalHealthReviewDocumentAssociationItem";
	
	private static final String DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY = 
			"documentAssociationItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY = "documentTagItem";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY = 
			"documentTagItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	private static final String MENTAL_HEALTH_REVIEW_NOTE_ITEM_MODEL_KEY =
			"mentalHealthNoteItem";
	
	private static final String MENTAL_HEALTH_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY =
			"mentalHealthReviewNoteItemIndex";

	/* Message keys. */


	private static final String MENTAL_HEALTH_REVIEW_EXISTS_MESSAGE_KEY = 
			"mentalHealthReview.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.mentalhealthreview.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("mentalHealthReviewService")
	private MentalHealthReviewService mentalHealthReviewService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("mentalHealthReviewPropertyEditorFactory")
	private PropertyEditorFactory mentalHealthReviewPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("mentalHealthReviewDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory 
			mentalHealthReviewDocumentAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("mentalHealthNotePropertyEditorFactory")
	private PropertyEditorFactory mentalHealthNotePropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("mentalHealthReviewFormValidator")
	private MentalHealthReviewFormValidator mentalHealthReviewFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("mentalHealthReviewDocumentPersister")
	private DocumentPersister mentalHealthReviewDocumentPersister;
	
	@Autowired
	@Qualifier("mentalHealthReviewDocumentRetriever")
	private DocumentRetriever mentalHealthReviewDocumentRetriever;
	
	@Autowired
	@Qualifier("mentalHealthReviewDocumentRemover")
	private FileRemover mentalHealthReviewDocumentRemover;
	
	/* Constructors. */
	
	/** Instantiates controller for mental health review. */
	public ManageMentalHealthReviewController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to create a mental health review.
	 * 
	 * @param offender offender
	 * @return screen to create mental health review
	 */
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true) 
					final Offender offender) {
		MentalHealthReviewForm mentalHealthReviewForm = 
				new MentalHealthReviewForm();
		ModelAndView mav = this.prepareMav(mentalHealthReviewForm, offender);
		return mav;
	}
	
	/**
	 * Shows screen to edit a mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return screen to edit a mental health review
	 */
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "mentalHealthReview", required = true)
				final MentalHealthReview mentalHealthReview) {
		MentalHealthReviewForm mentalHealthReviewForm = 
				new MentalHealthReviewForm();
		mentalHealthReviewForm.setDate(mentalHealthReview.getDate());
		mentalHealthReviewForm.setText(mentalHealthReview.getText());
		List<MentalHealthReviewDocumentAssociation> documentAssociations = this
				.mentalHealthReviewService
				.findMentalHealthReviewDocumentAssociationsByMentalHealthReview(
						mentalHealthReview);
		List<MentalHealthReviewDocumentAssociationItem> documentItems = 
				new ArrayList<>();
		for (MentalHealthReviewDocumentAssociation documentAssociation : 
			documentAssociations) {
			MentalHealthReviewDocumentAssociationItem item = 
					new MentalHealthReviewDocumentAssociationItem();
			List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
			List<DocumentTag> tags = this.mentalHealthReviewService
					.findDocumentTagsByDocument(
							documentAssociation.getDocument());
			for(DocumentTag tag : tags){
				DocumentTagItem tagItem = new DocumentTagItem();
				tagItem.setDocumentTag(tag);
				tagItem.setName(tag.getName());
				tagItem.setItemOperation(
						MentalHealthReviewItemOperation
							.UPDATE);
				tagItems.add(tagItem);
			}
			item.setDocument(documentAssociation.getDocument());
			item.setDocumentTagItems(tagItems);
			item.setFileDate(documentAssociation.getDocument().getDate());
			item.setFileExtension(documentAssociation.getDocument()
					.getFileExtension());
			item.setFilename(documentAssociation.getDocument().getFilename());
			item.setTitle(documentAssociation.getDocument().getTitle());
			item.setMentalHealthReviewDocumentAssociation(documentAssociation);
			item.setItemOperation(
					MentalHealthReviewItemOperation.UPDATE);
			item.setData(this.mentalHealthReviewDocumentRetriever.retrieve(
					item.getDocument()));
			
			documentItems.add(item);
		}
		mentalHealthReviewForm.setMentalHealthReviewDocumentAssociationItems(
				documentItems);
		List<MentalHealthNote> mentalHealthNotes = 
				this.mentalHealthReviewService
				.findMentalHealthReviewNotesByMentalHealthReview(
						mentalHealthReview);
		List<MentalHealthNoteItem> mentalHealthNoteItems = new ArrayList<>();
		for (MentalHealthNote mentalHealthNote : mentalHealthNotes) {
			MentalHealthNoteItem item = new MentalHealthNoteItem();
			item.setDate(mentalHealthNote.getDate());
			item.setDescription(mentalHealthNote.getDescription());
			item.setItemOperation(MentalHealthReviewItemOperation.UPDATE);
			item.setMentalHealthNote(mentalHealthNote);
			mentalHealthNoteItems.add(item);
		}
		mentalHealthReviewForm.setMentalHealthNoteItems(mentalHealthNoteItems);
		ModelAndView mav = this.prepareMav(mentalHealthReviewForm, 
				mentalHealthReview.getOffender());
		mav.addObject(MENTAL_HEALTH_REVIEW_MODEL_KEY, mentalHealthReview);
		return mav;
	}
	
	/**
	 * Saves a mental health review.
	 * 
	 * @param mentalHealthReviewForm mental health review form
	 * @param bindingResult binding result
	 * @return redirect to mental health review listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date range is not within staff 
	 * assignment date range
	 */
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true) 
					final Offender offender,
			final MentalHealthReviewForm mentalHealthReviewForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, 
					DateConflictException {
		this.mentalHealthReviewFormValidator.validate(mentalHealthReviewForm, 
				bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(mentalHealthReviewForm, 
					offender, bindingResult);
			return mav;
			}
		MentalHealthReview mentalHealthReview = this.mentalHealthReviewService
				.createMentalHealthReview(mentalHealthReviewForm.getDate(),
						mentalHealthReviewForm.getText(), offender);
		processDocumentAssociationItems(mentalHealthReview, 
				mentalHealthReviewForm
				.getMentalHealthReviewDocumentAssociationItems());
		processNoteItems(mentalHealthReview, 
				mentalHealthReviewForm.getMentalHealthNoteItems());
		return new ModelAndView(String.format(REDIRECT_URL, offender.getId()));
	}
	
	/**
	 * Updates a mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param mentalHealthReviewForm mental health review form
	 * @param bindingResult binding result
	 * @return redirect to mental health review listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date range is not within staff 
	 * assignment date range
	 */
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "mentalHealthReview", required = true)
				final MentalHealthReview mentalHealthReview,
			final MentalHealthReviewForm mentalHealthReviewForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, 
					DateConflictException {
		this.mentalHealthReviewFormValidator.validate(mentalHealthReviewForm, 
				bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(mentalHealthReviewForm, 
					mentalHealthReview.getOffender(), bindingResult);
			return mav;
			}
		MentalHealthReview managerReview = this.mentalHealthReviewService
				.updateMentalHealthReview(mentalHealthReview, 
				mentalHealthReviewForm.getDate(),
				mentalHealthReviewForm.getText());
		processDocumentAssociationItems(managerReview, mentalHealthReviewForm
				.getMentalHealthReviewDocumentAssociationItems());
		processNoteItems(mentalHealthReview, 
				mentalHealthReviewForm.getMentalHealthNoteItems());
		return new ModelAndView(String.format(REDIRECT_URL, 
				managerReview.getOffender().getId()));
	}
	
	/**
	 * Removes a mental health review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return redirect to mental health review listing screen
	 */
	@PreAuthorize("hasRole('MENTAL_HEALTH_REVIEW_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "mentalHealthReview", required = true)
				final MentalHealthReview mentalHealthReview) {
		Long offenderId = mentalHealthReview.getOffender().getId();
		List<MentalHealthReviewDocumentAssociation> associations = this
				.mentalHealthReviewService
				.findMentalHealthReviewDocumentAssociationsByMentalHealthReview(
						mentalHealthReview);
		for (MentalHealthReviewDocumentAssociation association : associations) {
			Document document = association.getDocument();
			List<DocumentTag> documentTags = this.mentalHealthReviewService
					.findDocumentTagsByDocument(document);
			for(DocumentTag tag : documentTags) {
				this.mentalHealthReviewService.removeDocumentTag(tag);
			}
			this.mentalHealthReviewService
				.removeMentalHealthReviewDocumentAssociation(association);
			this.mentalHealthReviewDocumentRemover.remove(document.getFilename());
			this.mentalHealthReviewService.removeDocument(document);
		}
		this.mentalHealthReviewService.removeMentalHealthReview(
				mentalHealthReview);
		return new ModelAndView(String.format(REDIRECT_URL, offenderId)); 
	}
	
	/**
	 * Creates a mental health review document association item.
	 * 
	 * @param documentAssociationItemIndex mental health review 
	 * document association item index
	 * @return model and view
	 */
	@RequestMapping(value =
			"createMentalHealthReviewDocumentAssociationItem.html",
			method = RequestMethod.GET)
	public ModelAndView createMentalHealthReviewDocumentAssociationItem(
			@RequestParam(value = "documentAssociationItemIndex", 
				required = true)
			final Integer documentAssociationItemIndex){
		ModelMap map = new ModelMap();
		MentalHealthReviewDocumentAssociationItem associableDocumentItem =
				new MentalHealthReviewDocumentAssociationItem();
		associableDocumentItem.setItemOperation(
				MentalHealthReviewItemOperation.CREATE);
		map.addAttribute(
				MENTAL_HEALTH_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY,
				associableDocumentItem);
		map.addAttribute(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				documentAssociationItemIndex);
		return new ModelAndView(
				DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME, map);
	}
	
	/**
	 * Displays a document tag item.
	 * 
	 * @param documentAssociationItemIndex document association item index
	 * @param documentTagItemIndex document tag item index
	 * @return model and view
	 */
	@RequestMapping(value = "createDocumentTagItem.html",
			method = RequestMethod.GET)
	public ModelAndView createDocumentTagItem(
			@RequestParam(value = "documentAssociationItemIndex",
					required = true)
				final Integer documentAssociationItemIndex,
			@RequestParam(value = "documentTagItemIndex", required = true)
					final Integer documentTagItemIndex) {
		ModelMap map = new ModelMap();
		DocumentTagItem tagItem = new DocumentTagItem();
		tagItem.setItemOperation(
				MentalHealthReviewItemOperation.CREATE);
		map.addAttribute(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
			documentAssociationItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_VIEW_NAME,
				map);
	}
	
	/**
	 * Returns a model and view of new mental health review note item.
	 *
	 * @param mentalHealthReviewNoteItemIndex mental health review note item 
	 * index
	 * @return model and view
	 */
	@RequestMapping(value = "createMentalHealthReviewNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView createMentalHealthReviewNoteItem(@RequestParam(
			value = "mentalHealthReviewNoteItemIndex", required = true)
			final Integer mentalHealthReviewNoteItemIndex) {
		ModelMap map = new ModelMap();
		MentalHealthNoteItem item = new MentalHealthNoteItem();
		item.setItemOperation(MentalHealthReviewItemOperation.CREATE);
		map.addAttribute(MENTAL_HEALTH_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY, 
				mentalHealthReviewNoteItemIndex);
		map.addAttribute(MENTAL_HEALTH_REVIEW_NOTE_ITEM_MODEL_KEY, item);
		
		return new ModelAndView(
				MENTAL_HEALTH_REVIEW_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a mental health review.
	 * 
	 * @return action menu for screen to create/edit a mental health review
	 */
	@RequestMapping(value = "/mentalHealthReviewActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Return model and view of mental health review note items action menu.
	 *
	 * @return model and view
	 */
	@RequestMapping(value = "/mentalHealthReviewNoteItemsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showMentalHealthReviewNoteItemsActionMenu() {
		return new ModelAndView(
				MENTAL_HEALTH_REVIEW_NOTE_ITEMS_ACTION_MENU_VIEW_NAME);
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles duplicate entity exists exceptions.
	 * 
	 * @param exception duplicate entity exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleMentalHealthReviewExistsException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				MENTAL_HEALTH_REVIEW_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final MentalHealthReviewForm mentalHealthReviewForm,
			final Offender offender) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(MENTAL_HEALTH_REVIEW_FORM_MODEL_KEY, 
				mentalHealthReviewForm);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(MENTAL_HEALTH_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY, 
				(mentalHealthReviewForm.getMentalHealthNoteItems() != null ? 
						mentalHealthReviewForm.getMentalHealthNoteItems().size()
						: 0));
		mav.addObject(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				(mentalHealthReviewForm
						.getMentalHealthReviewDocumentAssociationItems() != null ? 
								mentalHealthReviewForm
								.getMentalHealthReviewDocumentAssociationItems()
								.size() : 0));
		if(mentalHealthReviewForm
				.getMentalHealthReviewDocumentAssociationItems() != null){
			List<Integer> tagIndexes = new ArrayList<Integer>();
			for(int i = 0; i < mentalHealthReviewForm
					.getMentalHealthReviewDocumentAssociationItems().size(); 
					i++){
				if(mentalHealthReviewForm
						.getMentalHealthReviewDocumentAssociationItems()
						.get(i) != null){
					tagIndexes.add(i,
						(mentalHealthReviewForm
							.getMentalHealthReviewDocumentAssociationItems()
							.get(i).getDocumentTagItems() != null ?
								mentalHealthReviewForm
								.getMentalHealthReviewDocumentAssociationItems()
								.get(i).getDocumentTagItems().size() : 0));
				}
				else{
					tagIndexes.add(i, 0);
				}
			}
			mav.addObject(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, tagIndexes);
		}
		this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final MentalHealthReviewForm mentalHealthReviewForm,
			final Offender offender,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(mentalHealthReviewForm, offender);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ MENTAL_HEALTH_REVIEW_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Processes document association items
	private void processDocumentAssociationItems(
			final MentalHealthReview mentalHealthReview, 
			final List<MentalHealthReviewDocumentAssociationItem>
					mentalHealthReviewDocumentAssociationItems) 
							throws DuplicateEntityFoundException {
		if(mentalHealthReviewDocumentAssociationItems != null){
			for(MentalHealthReviewDocumentAssociationItem item : 
				mentalHealthReviewDocumentAssociationItems){
				if(MentalHealthReviewItemOperation.CREATE
						.equals(item.getItemOperation())){
					final String fileExtension = item.getFileExtension();
					this.documentFilenameGenerator.setExtension(fileExtension);
					final String filename = 
							this.documentFilenameGenerator.generate();
					Document document = this.mentalHealthReviewService
							.createDocument(item.getFileDate(),
									item.getTitle(), filename, 
									item.getFileExtension());
					this.mentalHealthReviewDocumentPersister
							.persist(document, item.getData());
					this.mentalHealthReviewService
							.createMentalHealthReviewDocumentAssociation(
									document, mentalHealthReview);
					processDocumentTagItems(document,
							item.getDocumentTagItems());
				}
				if(MentalHealthReviewItemOperation.UPDATE
						.equals(item.getItemOperation())){
					Document document = this.mentalHealthReviewService
							.updateDocument(item.getDocument(),
									item.getFileDate(), item.getTitle());
					processDocumentTagItems(document,
							item.getDocumentTagItems());
				}
				if(MentalHealthReviewItemOperation.REMOVE
						.equals(item.getItemOperation())){
					for(DocumentTag tag : this.mentalHealthReviewService
							.findDocumentTagsByDocument(item.getDocument())){
						this.mentalHealthReviewService.removeDocumentTag(tag);
					}
					this.mentalHealthReviewService
						.removeMentalHealthReviewDocumentAssociation(
						item.getMentalHealthReviewDocumentAssociation());
					this.mentalHealthReviewDocumentRemover.remove(
							item.getDocument().getFilename());
					this.mentalHealthReviewService.removeDocument(
							item.getDocument());
				}
			}
		}
	}
	
	// Processes document tag items
	private void processDocumentTagItems(final Document document, 
			final List<DocumentTagItem> documentTagItems) 
					throws DuplicateEntityFoundException {
		if(documentTagItems != null){
			for(DocumentTagItem tagItem : documentTagItems){
				if(MentalHealthReviewItemOperation
					.CREATE.equals(tagItem.getItemOperation())){
					this.mentalHealthReviewService.createDocumentTag(
							tagItem.getName(), document);
				}
				if(MentalHealthReviewItemOperation
					.UPDATE.equals(tagItem.getItemOperation())){
					this.mentalHealthReviewService.updateDocumentTag(
							tagItem.getDocumentTag(),
							tagItem.getName());
				}
				if(MentalHealthReviewItemOperation
					.REMOVE.equals(tagItem.getItemOperation())){
					this.mentalHealthReviewService.removeDocumentTag(
							tagItem.getDocumentTag());
				}
			}
		}
	}
	
	// Processes note items
	private void processNoteItems(final MentalHealthReview mentalHealthReview,
			final List<MentalHealthNoteItem> mentalHealthNoteItems) 
					throws DuplicateEntityFoundException {
		if (mentalHealthNoteItems != null) {
			for (MentalHealthNoteItem item : mentalHealthNoteItems) {
				if (MentalHealthReviewItemOperation.CREATE.equals(
						item.getItemOperation())) {
					this.mentalHealthReviewService.createMentalHealthReviewNote(
							mentalHealthReview, item.getDescription(), 
							item.getDate());
				} else if (MentalHealthReviewItemOperation.UPDATE.equals(
						item.getItemOperation())) {
					if (this.isNoteChanged(
							item.getMentalHealthNote(), item.getDate(), 
							item.getDescription())) {					
						this.mentalHealthReviewService
							.updateMentalHealthReviewNote(
									item.getMentalHealthNote(), 
									item.getDescription(),
									item.getDate());
					}
				} else if (MentalHealthReviewItemOperation.REMOVE.equals(
						item.getItemOperation())) {
					this.mentalHealthReviewService.removeMentalHealthReviewNote(
							item.getMentalHealthNote());
				} else {
					throw new UnsupportedOperationException(
							"Operation not supported: " 
									+ item.getItemOperation());
				}
			}
		}		
	}
	
	// Checks if the note values have changed
	private boolean isNoteChanged(final MentalHealthNote note, final Date date, 
			final String value) {
		if(!note.getDescription().equals(value)) {
			return true;
		}
		if(!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	/** 
	 * Retrieves document file.
	 * 
	 * @param document document
	 * @param httpServletResponse HTTP servlet response
	 */
	@RequestMapping(value = "retrieveFile.html", method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.mentalHealthReviewDocumentRetriever
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
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(MentalHealthReview.class,
				this.mentalHealthReviewPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(MentalHealthReviewDocumentAssociation.class, 
				this.mentalHealthReviewDocumentAssociationPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(MentalHealthNote.class, 
				this.mentalHealthNotePropertyEditorFactory.
				createPropertyEditor());
	}
}
