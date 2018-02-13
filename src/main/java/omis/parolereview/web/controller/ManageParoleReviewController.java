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
package omis.parolereview.web.controller;

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
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;
import omis.parolereview.domain.ParoleReviewNote;
import omis.parolereview.domain.StaffRoleCategory;
import omis.parolereview.service.ParoleReviewService;
import omis.parolereview.web.form.DocumentTagItem;
import omis.parolereview.web.form.ParoleReviewDocumentAssociationItem;
import omis.parolereview.web.form.ParoleReviewForm;
import omis.parolereview.web.form.ParoleReviewItemOperation;
import omis.parolereview.web.form.ParoleReviewNoteItem;
import omis.parolereview.web.validator.ParoleReviewFormValidator;
import omis.staff.domain.StaffAssignment;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing parole reviews.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleReview")
public class ManageParoleReviewController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "paroleReview/edit";

	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleReview/includes/paroleReviewActionMenu";
	
	private static final String PAROLE_REVIEW_NOTES_ACTION_MENU_VIEW_NAME = 
			"paroleReview/includes/paroleReviewNotesActionMenu";
	
	/* Partial views. */
	
	private static final String DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME = 
			"paroleReview/includes/documentAssociationItemContent";
	
	private static final String DOCUMENT_TAG_ITEM_VIEW_NAME = 
			"paroleReview/includes/documentAssociationTagItemContent";
	
	private static final String PAROLE_REVIEW_NOTE_ITEM_VIEW_NAME = 
			"paroleReview/includes/paroleReviewNoteTableRow";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/paroleReview/list.html?offender=%d";
	
	/* Model keys. */
	
	private static final String PAROLE_REVIEW_FORM_MODEL_KEY = 
			"paroleReviewForm";
	
	private static final String PAROLE_REVIEW_MODEL_KEY = 
			"paroleReview";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String 
			PAROLE_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY = 
			"paroleReviewDocumentAssociationItem";
	
	private static final String DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY = 
			"documentAssociationItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY = "documentTagItem";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY = 
			"documentTagItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	private static final String ENDORSEMENT_CATEGORIES_MODEL_KEY = 
			"endorsementCategories";
	
	private static final String STAFF_ROLE_CATEGORIES_MODEL_KEY = 
			"staffRoleCategories";
	
	private static final String PAROLE_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY = 
			"paroleReviewNoteIndex";
	
	private static final String OPERATION_MODEL_KEY = "operation";
			
	/* Message keys. */


	private static final String PAROLE_REVIEW_EXISTS_MESSAGE_KEY = 
			"paroleReview.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.parolereview.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("paroleReviewService")
	private ParoleReviewService paroleReviewService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleReviewPropertyEditorFactory")
	private PropertyEditorFactory paroleReviewPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("staffAssignmentPropertyEditorFactory")
	private PropertyEditorFactory staffAssignmentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleReviewDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory 
			paroleReviewDocumentAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleReviewNotePropertyEditorFactory")
	private PropertyEditorFactory paroleReviewNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleEndorsementCategoryPropertyEditorFactory")
	private PropertyEditorFactory paroleEndorsementCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("staffRoleCategoryPropertyEditorFactory")
	private PropertyEditorFactory staffRoleCategoryPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("paroleReviewFormValidator")
	private ParoleReviewFormValidator paroleReviewFormValidator;
	
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
	@Qualifier("paroleReviewDocumentPersister")
	private DocumentPersister paroleReviewDocumentPersister;
	
	@Autowired
	@Qualifier("paroleReviewDocumentRetriever")
	private DocumentRetriever paroleReviewDocumentRetriever;
	
	@Autowired
	@Qualifier("paroleReviewDocumentRemover")
	private FileRemover paroleReviewDocumentRemover;
	
	/* Constructors. */
	
	/** Instantiates controller for parole review. */
	public ManageParoleReviewController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to create a parole review.
	 * 
	 * @param offender offender
	 * @return screen to create parole review
	 */
	@PreAuthorize("hasRole('PAROLE_REVIEW_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = true) 
					final Offender offender) {
		ParoleReviewForm paroleReviewForm = 
				new ParoleReviewForm();
		ModelAndView mav = this.prepareMav(paroleReviewForm, offender);
		return mav;
	}
	
	/**
	 * Shows screen to edit a parole review.
	 * 
	 * @param paroleReview parole review
	 * @return screen to edit a parole review
	 */
	@PreAuthorize("hasRole('PAROLE_REVIEW_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "paroleReview", required = true)
				final ParoleReview paroleReview) {
		ParoleReviewForm paroleReviewForm = 
				new ParoleReviewForm();
		paroleReviewForm.setStaffAssignment(
				paroleReview.getStaffAssignment());
		paroleReviewForm.setDate(paroleReview.getDate());
		paroleReviewForm.setText(paroleReview.getText());
		paroleReviewForm.setEndorsement(paroleReview.getEndorsement());
		paroleReviewForm.setStaffRole(paroleReview.getStaffRole());
		List<ParoleReviewDocumentAssociation> documentAssociations = this
				.paroleReviewService
				.findParoleReviewDocumentAssociationsByParoleReview(
						paroleReview);
		List<ParoleReviewDocumentAssociationItem> documentItems = 
				new ArrayList<>();
		for (ParoleReviewDocumentAssociation documentAssociation : 
			documentAssociations) {
			ParoleReviewDocumentAssociationItem item = 
					new ParoleReviewDocumentAssociationItem();
			List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
			List<DocumentTag> tags = this.paroleReviewService
					.findDocumentTagsByDocument(
							documentAssociation.getDocument());
			for(DocumentTag tag : tags){
				DocumentTagItem tagItem = new DocumentTagItem();
				tagItem.setDocumentTag(tag);
				tagItem.setName(tag.getName());
				tagItem.setItemOperation(
						ParoleReviewItemOperation
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
			item.setParoleReviewDocumentAssociation(documentAssociation);
			item.setItemOperation(
					ParoleReviewItemOperation.UPDATE);
			item.setData(this.paroleReviewDocumentRetriever.retrieve(
					item.getDocument()));
			
			documentItems.add(item);
		}
		paroleReviewForm.setParoleReviewDocumentAssociationItems(
				documentItems);
		List<ParoleReviewNote> paroleReviewNotes = this.paroleReviewService
				.findParoleReviewNotesByParoleReview(paroleReview);
		List<ParoleReviewNoteItem> noteItems = new ArrayList<>();
		for (ParoleReviewNote paroleReviewNote : paroleReviewNotes) {
			ParoleReviewNoteItem noteItem = new ParoleReviewNoteItem();
			noteItem.setDate(paroleReviewNote.getDate());
			noteItem.setValue(paroleReviewNote.getDescription());
			noteItem.setParoleReviewNote(paroleReviewNote);
			noteItem.setOperation(ParoleReviewItemOperation.UPDATE);
			noteItems.add(noteItem);
		}
		paroleReviewForm.setParoleReviewNoteItems(noteItems);
		ModelAndView mav = this.prepareMav(paroleReviewForm, 
				paroleReview.getOffender());
		mav.addObject(PAROLE_REVIEW_MODEL_KEY, paroleReview);
		return mav;
	}
	
	/**
	 * Saves a parole review.
	 * 
	 * @param paroleReviewForm parole review form
	 * @param bindingResult binding result
	 * @return redirect to parole review listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date range is not within staff 
	 * assignment date range
	 */
	@PreAuthorize("hasRole('PAROLE_REVIEW_CREATE') or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = true) 
					final Offender offender,
			final ParoleReviewForm paroleReviewForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, 
					DateConflictException {
		this.paroleReviewFormValidator.validate(paroleReviewForm, 
				bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(paroleReviewForm, 
					offender, bindingResult);
			return mav;
			}
		ParoleReview paroleReview = this.paroleReviewService
				.createParoleReview(
						paroleReviewForm.getStaffAssignment(),
						paroleReviewForm.getDate(),
						paroleReviewForm.getText(),
						offender,
						paroleReviewForm.getEndorsement(),
						paroleReviewForm.getStaffRole());
		processDocumentAssociationItems(paroleReview, paroleReviewForm
				.getParoleReviewDocumentAssociationItems());
		processNoteItems(paroleReview, 
				paroleReviewForm.getParoleReviewNoteItems());
		return new ModelAndView(String.format(REDIRECT_URL, offender.getId()));
	}
	
	/**
	 * Updates a parole review.
	 * 
	 * @param paroleReview parole review
	 * @param paroleReviewForm parole review form
	 * @param bindingResult binding result
	 * @return redirect to parole review listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date range is not within staff 
	 * assignment date range
	 */
	@PreAuthorize("hasRole('PAROLE_REVIEW_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "paroleReview", required = true)
				final ParoleReview paroleReview,
			final ParoleReviewForm paroleReviewForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, 
					DateConflictException {
		this.paroleReviewFormValidator.validate(paroleReviewForm, 
				bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(paroleReviewForm, 
					paroleReview.getOffender(), bindingResult);
			return mav;
			}
		ParoleReview managerReview = this.paroleReviewService
				.updateParoleReview(paroleReview, 
				paroleReviewForm.getStaffAssignment(), 
				paroleReviewForm.getDate(),
				paroleReviewForm.getText(),
				paroleReviewForm.getEndorsement(),
				paroleReviewForm.getStaffRole());
		processDocumentAssociationItems(managerReview, paroleReviewForm
				.getParoleReviewDocumentAssociationItems());
		processNoteItems(paroleReview, 
				paroleReviewForm.getParoleReviewNoteItems());
		return new ModelAndView(String.format(REDIRECT_URL, 
				managerReview.getOffender().getId()));
	}
	
	/**
	 * Removes a parole review.
	 * 
	 * @param paroleReview parole review
	 * @return redirect to parole review listing screen
	 */
	@PreAuthorize("hasRole('PAROLE_REVIEW_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "paroleReview", required = true)
				final ParoleReview paroleReview) {
		Long offenderId = paroleReview.getOffender().getId();
		List<ParoleReviewDocumentAssociation> associations = this
				.paroleReviewService
				.findParoleReviewDocumentAssociationsByParoleReview(
						paroleReview);
		for (ParoleReviewDocumentAssociation association : associations) {
			Document document = association.getDocument();
			List<DocumentTag> documentTags = this.paroleReviewService
					.findDocumentTagsByDocument(document);
			for(DocumentTag tag : documentTags) {
				this.paroleReviewService.removeDocumentTag(tag);
			}
			this.paroleReviewService
				.removeParoleReviewDocumentAssociation(association);
			this.paroleReviewDocumentRemover.remove(document.getFilename());
			this.paroleReviewService.removeDocument(document);
		}
		List<ParoleReviewNote> paroleReviewNotes = this.paroleReviewService
				.findParoleReviewNotesByParoleReview(paroleReview);
		for (ParoleReviewNote paroleReviewNote : paroleReviewNotes) {
			this.paroleReviewService.removeParoleReviewNote(paroleReviewNote);
		}
		this.paroleReviewService.removeParoleReview(
				paroleReview);
		return new ModelAndView(String.format(REDIRECT_URL, offenderId)); 
	}
	
	/**
	 * Creates a parole review document association item.
	 * 
	 * @param documentAssociationItemIndex parole review 
	 * document association item index
	 * @return model and view
	 */
	@RequestMapping(value =
			"createParoleReviewDocumentAssociationItem.html",
			method = RequestMethod.GET)
	public ModelAndView createParoleReviewDocumentAssociationItem(
			@RequestParam(value = "documentAssociationItemIndex", 
				required = true)
			final Integer documentAssociationItemIndex){
		ModelMap map = new ModelMap();
		ParoleReviewDocumentAssociationItem associableDocumentItem =
				new ParoleReviewDocumentAssociationItem();
		associableDocumentItem.setItemOperation(
				ParoleReviewItemOperation.CREATE);
		map.addAttribute(
				PAROLE_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY,
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
				ParoleReviewItemOperation.CREATE);
		map.addAttribute(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
			documentAssociationItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_VIEW_NAME,
				map);
	}
	
	/**
	 * Creates a parole review note item.
	 * 
	 * @param paroleReviewNoteIndex parole review note item index
	 * @return model and view
	 */
	@RequestMapping(value = "createParoleReviewNote.html",
			method = RequestMethod.GET)
	public ModelAndView createParoleReviewNote(
			@RequestParam(value = "paroleReviewNoteIndex", required = true)
				final Integer paroleReviewNoteIndex){
		ModelMap map = new ModelMap();
		ParoleReviewNoteItem noteItem = new ParoleReviewNoteItem();
		noteItem.setOperation(ParoleReviewItemOperation.CREATE);
		map.addAttribute(PAROLE_REVIEW_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY,
				noteItem);
		map.addAttribute(PAROLE_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY,
				paroleReviewNoteIndex);
		map.addAttribute(OPERATION_MODEL_KEY, ParoleReviewItemOperation.CREATE);
		return new ModelAndView(PAROLE_REVIEW_NOTE_ITEM_VIEW_NAME, map);
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a parole review.
	 * 
	 * @return action menu for screen to create/edit a parole review
	 */
	@RequestMapping(value = "/paroleReviewActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = false)
					final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Displays action menu for screen to add a parole review note.
	 * 
	 * @return action menu for screen to add a parole review note
	 */
	@RequestMapping(value = "/paroleReviewNotesActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showParoleReviewNotesActionMenu(
			@RequestParam(value = "paroleReviewNoteIndex", required = false)
					final Integer paroleReviewNoteIndex) {
		ModelAndView mav = new ModelAndView(
				PAROLE_REVIEW_NOTES_ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY, 
				paroleReviewNoteIndex);
		return mav;
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles duplicate entity exists exceptions.
	 * 
	 * @param exception duplicate entity exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleParoleReviewExistsException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PAROLE_REVIEW_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final ParoleReviewForm paroleReviewForm,
			final Offender offender) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_REVIEW_FORM_MODEL_KEY, 
				paroleReviewForm);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				(paroleReviewForm
						.getParoleReviewDocumentAssociationItems() != null ? 
								paroleReviewForm
								.getParoleReviewDocumentAssociationItems()
								.size() : 0));
			if(paroleReviewForm
					.getParoleReviewDocumentAssociationItems() != null){
				List<Integer> tagIndexes = new ArrayList<Integer>();
				for(int i = 0; i < paroleReviewForm
						.getParoleReviewDocumentAssociationItems().size(); 
						i++){
					if(paroleReviewForm
							.getParoleReviewDocumentAssociationItems()
							.get(i) != null){
						tagIndexes.add(i,
							(paroleReviewForm
								.getParoleReviewDocumentAssociationItems()
								.get(i).getDocumentTagItems() != null ?
									paroleReviewForm
									.getParoleReviewDocumentAssociationItems()
									.get(i).getDocumentTagItems().size() : 0));
					}
					else{
						tagIndexes.add(i, 0);
					}
				}
				mav.addObject(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, tagIndexes);
			}
		mav.addObject(PAROLE_REVIEW_NOTE_ITEM_INDEX_MODEL_KEY, 
				(paroleReviewForm.getParoleReviewNoteItems() != null ? 
						paroleReviewForm.getParoleReviewNoteItems().size() : 0));
		this.offenderSummaryModelDelegate.add(mav.getModel(), offender);
		mav.addObject(ENDORSEMENT_CATEGORIES_MODEL_KEY, 
				this.paroleReviewService.findParoleEndorsementCategories());
		mav.addObject(STAFF_ROLE_CATEGORIES_MODEL_KEY, 
				this.paroleReviewService.findStaffRoleCategories());
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final ParoleReviewForm paroleReviewForm,
			final Offender offender,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(paroleReviewForm, offender);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ PAROLE_REVIEW_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Processes document association items
	private void processDocumentAssociationItems(
			final ParoleReview paroleReview, 
			final List<ParoleReviewDocumentAssociationItem>
					paroleReviewDocumentAssociationItems) 
							throws DuplicateEntityFoundException {
		if(paroleReviewDocumentAssociationItems != null){
			for(ParoleReviewDocumentAssociationItem item : 
				paroleReviewDocumentAssociationItems){
				if(ParoleReviewItemOperation.CREATE
						.equals(item.getItemOperation())){
					final String fileExtension = item.getFileExtension();
					this.documentFilenameGenerator.setExtension(fileExtension);
					final String filename = 
							this.documentFilenameGenerator.generate();
					Document document = this.paroleReviewService
							.createDocument(item.getFileDate(),
									item.getTitle(), filename, 
									item.getFileExtension());
					this.paroleReviewDocumentPersister
							.persist(document, item.getData());
					this.paroleReviewService
							.createParoleReviewDocumentAssociation(
									document, paroleReview);
					processDocumentTagItems(document,
							item.getDocumentTagItems());
				}
				if(ParoleReviewItemOperation.UPDATE
						.equals(item.getItemOperation())){
					Document document = this.paroleReviewService
							.updateDocument(item.getDocument(),
									item.getFileDate(), item.getTitle());
					processDocumentTagItems(document,
							item.getDocumentTagItems());
				}
				if(ParoleReviewItemOperation.REMOVE
						.equals(item.getItemOperation())){
					for(DocumentTag tag : this.paroleReviewService
							.findDocumentTagsByDocument(item.getDocument())){
						this.paroleReviewService.removeDocumentTag(tag);
					}
					this.paroleReviewService
						.removeParoleReviewDocumentAssociation(
						item.getParoleReviewDocumentAssociation());
					this.paroleReviewDocumentRemover.remove(
							item.getDocument().getFilename());
					this.paroleReviewService.removeDocument(
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
				if(ParoleReviewItemOperation
					.CREATE.equals(tagItem.getItemOperation())){
					this.paroleReviewService.createDocumentTag(
							tagItem.getName(), document);
				}
				if(ParoleReviewItemOperation
					.UPDATE.equals(tagItem.getItemOperation())){
					this.paroleReviewService.updateDocumentTag(
							tagItem.getDocumentTag(),
							tagItem.getName());
				}
				if(ParoleReviewItemOperation
					.REMOVE.equals(tagItem.getItemOperation())){
					this.paroleReviewService.removeDocumentTag(
							tagItem.getDocumentTag());
				}
			}
		}
	}
	
	private void processNoteItems(final ParoleReview paroleReview, 
			final List<ParoleReviewNoteItem> noteItems) 
					throws DuplicateEntityFoundException {
		if (noteItems != null) {
			for (ParoleReviewNoteItem noteItem : noteItems) {
				if (ParoleReviewItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.paroleReviewService.createParoleReviewNote(
							paroleReview, noteItem.getValue(), 
							noteItem.getDate());
				}
				if (ParoleReviewItemOperation.UPDATE.equals(
						noteItem.getOperation())) {
					this.paroleReviewService.updateParoleReviewNote(
							noteItem.getParoleReviewNote(), paroleReview, 
							noteItem.getValue(), noteItem.getDate());
				}
				if (ParoleReviewItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.paroleReviewService.removeParoleReviewNote(
							noteItem.getParoleReviewNote());
				}
			}
		}
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
		final byte[] bytes = this.paroleReviewDocumentRetriever
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
		binder.registerCustomEditor(ParoleReview.class,
				this.paroleReviewPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(StaffAssignment.class,
				this.staffAssignmentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(ParoleReviewDocumentAssociation.class, 
				this.paroleReviewDocumentAssociationPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(ParoleReviewNote.class, 
				this.paroleReviewNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleEndorsementCategory.class, 
				this.paroleEndorsementCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(StaffRoleCategory.class, 
				this.staffRoleCategoryPropertyEditorFactory
				.createPropertyEditor());
	}
}
