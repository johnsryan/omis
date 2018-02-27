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
package omis.paroleplan.web.controller;

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
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanDocumentAssociation;
import omis.paroleplan.domain.ParolePlanNote;
import omis.paroleplan.service.ParolePlanService;
import omis.paroleplan.web.form.DocumentTagItem;
import omis.paroleplan.web.form.ParolePlanDocumentAssociationItem;
import omis.paroleplan.web.form.ParolePlanForm;
import omis.paroleplan.web.form.ParolePlanItemOperation;
import omis.paroleplan.web.form.ParolePlanNoteItem;
import omis.paroleplan.web.validator.ParolePlanFormValidator;
import omis.staff.domain.StaffAssignment;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing parole plans.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/parolePlan")
public class ManageParolePlanController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "parolePlan/edit";

	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"parolePlan/includes/parolePlanActionMenu";
	
	private static final String PAROLE_PLAN_NOTES_ACTION_MENU_VIEW_NAME = 
			"parolePlan/includes/parolePlanNotesActionMenu";
	
	/* Partial views. */
	
	private static final String DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME = 
			"parolePlan/includes/documentAssociationItemContent";
	
	private static final String DOCUMENT_TAG_ITEM_VIEW_NAME = 
			"parolePlan/includes/documentAssociationTagItemContent";
	
	private static final String PAROLE_PLAN_NOTE_ITEM_VIEW_NAME = 
			"parolePlan/includes/parolePlanNoteTableRow";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/parolePlan/edit.html?paroleEligibility=%d";
	
	/* Model keys. */
	
	private static final String PAROLE_PLAN_FORM_MODEL_KEY = 
			"parolePlanForm";
	
	private static final String PAROLE_PLAN_MODEL_KEY = 
			"parolePlan";
	
	private static final String PAROLE_ELIGIBILITY_MODEL_KEY = 
			"paroleEligibility";
	
	private static final String PAROLE_PLAN_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY 
			= "parolePlanDocumentAssociationItem";
	
	private static final String DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY = 
			"documentAssociationItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY = "documentTagItem";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY = 
			"documentTagItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	private static final String PAROLE_PLAN_NOTE_ITEM_INDEX_MODEL_KEY = 
			"parolePlanNoteIndex";
	
	private static final String OPERATION_MODEL_KEY = "operation";
			
	/* Message keys. */


	private static final String PAROLE_PLAN_EXISTS_MESSAGE_KEY = 
			"parolePlan.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.paroleplan.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("parolePlanService")
	private ParolePlanService parolePlanService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("parolePlanPropertyEditorFactory")
	private PropertyEditorFactory parolePlanPropertyEditorFactory;
	
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
	@Qualifier("parolePlanDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory 
			parolePlanDocumentAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("parolePlanNotePropertyEditorFactory")
	private PropertyEditorFactory parolePlanNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleEligibilityPropertyEditorFactory")
	private PropertyEditorFactory paroleEligibilityPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("parolePlanFormValidator")
	private ParolePlanFormValidator parolePlanFormValidator;
	
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
	@Qualifier("parolePlanDocumentPersister")
	private DocumentPersister parolePlanDocumentPersister;
	
	@Autowired
	@Qualifier("parolePlanDocumentRetriever")
	private DocumentRetriever parolePlanDocumentRetriever;
	
	@Autowired
	@Qualifier("parolePlanDocumentRemover")
	private FileRemover parolePlanDocumentRemover;
	
	/* Constructors. */
	
	/** Instantiates controller for parole plan. */
	public ManageParolePlanController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to edit a parole plan.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @return screen to edit a parole plan
	 */
	@PreAuthorize("hasRole('PAROLE_PLAN_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "paroleEligibility", required = true)
				final ParoleEligibility paroleEligibility) {
		ParolePlan parolePlan = this.parolePlanService
				.findParolePlanByParoleEligibility(paroleEligibility);
		ParolePlanForm parolePlanForm = new ParolePlanForm();
		if (parolePlan != null) {
			parolePlanForm.setEvaluator(
					parolePlan.getEvaluation().getStaffAssignment());
			parolePlanForm.setEvaluationDescription(
					parolePlan.getEvaluation().getDescription());
			parolePlanForm.setVocationalPlan(parolePlan.getVocationalPlan());
			parolePlanForm.setResidencePlan(parolePlan.getResidencePlan());
			parolePlanForm.setTreatmentPlan(parolePlan.getTreatmentPlan());
			List<ParolePlanDocumentAssociation> documentAssociations = this
					.parolePlanService
					.findParolePlanDocumentAssociationsByParolePlan(parolePlan);
			List<ParolePlanDocumentAssociationItem> documentItems = 
					new ArrayList<>();
			for (ParolePlanDocumentAssociation documentAssociation : 
				documentAssociations) {
				ParolePlanDocumentAssociationItem item = 
						new ParolePlanDocumentAssociationItem();
				List<DocumentTagItem> tagItems = new ArrayList<DocumentTagItem>();
				List<DocumentTag> tags = this.parolePlanService
						.findDocumentTagsByDocument(
								documentAssociation.getDocument());
				for(DocumentTag tag : tags){
					DocumentTagItem tagItem = new DocumentTagItem();
					tagItem.setDocumentTag(tag);
					tagItem.setName(tag.getName());
					tagItem.setItemOperation(
							ParolePlanItemOperation
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
				item.setParolePlanDocumentAssociation(documentAssociation);
				item.setItemOperation(
						ParolePlanItemOperation.UPDATE);
				item.setData(this.parolePlanDocumentRetriever.retrieve(
						item.getDocument()));
				documentItems.add(item);
			}
			parolePlanForm.setParolePlanDocumentAssociationItems(
					documentItems);
			List<ParolePlanNote> parolePlanNotes = this.parolePlanService
					.findParolePlanNotesByParolePlan(parolePlan);
			List<ParolePlanNoteItem> noteItems = new ArrayList<>();
			for (ParolePlanNote parolePlanNote : parolePlanNotes) {
				ParolePlanNoteItem noteItem = new ParolePlanNoteItem();
				noteItem.setDate(parolePlanNote.getDate());
				noteItem.setValue(parolePlanNote.getDescription());
				noteItem.setParolePlanNote(parolePlanNote);
				noteItem.setOperation(ParolePlanItemOperation.UPDATE);
				noteItems.add(noteItem);
			}
			parolePlanForm.setParolePlanNoteItems(noteItems);
		}
		ModelAndView mav = this.prepareMav(parolePlanForm, paroleEligibility);
		mav.addObject(PAROLE_PLAN_MODEL_KEY, parolePlan);
		return mav;
	}
	
	/**
	 * Updates a parole plan.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @param parolePlanForm parole plan form
	 * @param bindingResult binding result
	 * @return redirect to parole plan listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 * @throws DateConflictException if date range is not within staff 
	 * assignment date range
	 */
	@PreAuthorize("hasRole('PAROLE_PLAN_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "paroleEligibility", required = true)
				final ParoleEligibility paroleEligibility,
			final ParolePlanForm parolePlanForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException, 
					DateConflictException {
		this.parolePlanFormValidator.validate(parolePlanForm, 
				bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(parolePlanForm, 
					paroleEligibility, bindingResult);
			return mav;
		}
		ParolePlan parolePlan = this.parolePlanService
				.findParolePlanByParoleEligibility(paroleEligibility);
		if (parolePlan != null) {
			parolePlan = this.parolePlanService.updateParolePlan(parolePlan, 
					parolePlanForm.getEvaluator(),
					parolePlanForm.getEvaluationDescription(),
					parolePlanForm.getVocationalPlan(),
					parolePlanForm.getResidencePlan(),
					parolePlanForm.getTreatmentPlan());
		} else {
			parolePlan = this.parolePlanService.createParolePlan(
					paroleEligibility, 
					parolePlanForm.getEvaluator(), 
					parolePlanForm.getEvaluationDescription(),
					parolePlanForm.getVocationalPlan(),
					parolePlanForm.getResidencePlan(),
					parolePlanForm.getTreatmentPlan());
		}
		processDocumentAssociationItems(parolePlan, parolePlanForm
				.getParolePlanDocumentAssociationItems());
		processNoteItems(parolePlan, 
				parolePlanForm.getParolePlanNoteItems());
		return new ModelAndView(String.format(REDIRECT_URL, 
				paroleEligibility.getId()));
	}
	
	/**
	 * Removes a parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @return redirect to parole plan listing screen
	 */
	@PreAuthorize("hasRole('PAROLE_PLAN_REMOVE') or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "parolePlan", required = true)
				final ParolePlan parolePlan) {
		Long eligibilityId = parolePlan.getParoleEligibility().getId();
		List<ParolePlanDocumentAssociation> associations = this
				.parolePlanService
				.findParolePlanDocumentAssociationsByParolePlan(parolePlan);
		for (ParolePlanDocumentAssociation association : associations) {
			Document document = association.getDocument();
			List<DocumentTag> documentTags = this.parolePlanService
					.findDocumentTagsByDocument(document);
			for(DocumentTag tag : documentTags) {
				this.parolePlanService.removeDocumentTag(tag);
			}
			this.parolePlanService
				.removeParolePlanDocumentAssociation(association);
			this.parolePlanDocumentRemover.remove(document.getFilename());
			this.parolePlanService.removeDocument(document);
		}
		List<ParolePlanNote> parolePlanNotes = this.parolePlanService
				.findParolePlanNotesByParolePlan(parolePlan);
		for (ParolePlanNote parolePlanNote : parolePlanNotes) {
			this.parolePlanService.removeParolePlanNote(parolePlanNote);
		}
		this.parolePlanService.removeParolePlan(
				parolePlan);
		return new ModelAndView(String.format(REDIRECT_URL, eligibilityId)); 
	}
	
	/**
	 * Creates a parole plan document association item.
	 * 
	 * @param documentAssociationItemIndex parole plan 
	 * document association item index
	 * @return model and view
	 */
	@RequestMapping(value = "createParolePlanDocumentAssociationItem.html",
			method = RequestMethod.GET)
	public ModelAndView createParolePlanDocumentAssociationItem(
			@RequestParam(value = "documentAssociationItemIndex", 
				required = true)
			final Integer documentAssociationItemIndex){
		ModelMap map = new ModelMap();
		ParolePlanDocumentAssociationItem associableDocumentItem =
				new ParolePlanDocumentAssociationItem();
		associableDocumentItem.setItemOperation(ParolePlanItemOperation.CREATE);
		map.addAttribute(PAROLE_PLAN_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY,
				associableDocumentItem);
		map.addAttribute(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				documentAssociationItemIndex);
		return new ModelAndView(DOCUMENT_ASSOCIATION_ITEM_VIEW_NAME, map);
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
				ParolePlanItemOperation.CREATE);
		map.addAttribute(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
			documentAssociationItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_VIEW_NAME, map);
	}
	
	/**
	 * Creates a parole plan note item.
	 * 
	 * @param parolePlanNoteIndex parole plan note item index
	 * @return model and view
	 */
	@RequestMapping(value = "createParolePlanNote.html",
			method = RequestMethod.GET)
	public ModelAndView createParolePlanNote(
			@RequestParam(value = "parolePlanNoteIndex", required = true)
				final Integer parolePlanNoteIndex){
		ModelMap map = new ModelMap();
		ParolePlanNoteItem noteItem = new ParolePlanNoteItem();
		noteItem.setOperation(ParolePlanItemOperation.CREATE);
		map.addAttribute(PAROLE_PLAN_DOCUMENT_ASSOCIATION_ITEM_MODEL_KEY,
				noteItem);
		map.addAttribute(PAROLE_PLAN_NOTE_ITEM_INDEX_MODEL_KEY,
				parolePlanNoteIndex);
		map.addAttribute(OPERATION_MODEL_KEY, ParolePlanItemOperation.CREATE);
		return new ModelAndView(PAROLE_PLAN_NOTE_ITEM_VIEW_NAME, map);
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a parole plan.
	 * 
	 * @return action menu for screen to create/edit a parole plan
	 */
	@RequestMapping(value = "/parolePlanActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "paroleEligibility", required = false)
					final ParoleEligibility paroleEligibility) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_ELIGIBILITY_MODEL_KEY, paroleEligibility);
		return mav;
	}
	
	/**
	 * Displays action menu for screen to add a parole plan note.
	 * 
	 * @return action menu for screen to add a parole plan note
	 */
	@RequestMapping(value = "/parolePlanNotesActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showParolePlanNotesActionMenu(
			@RequestParam(value = "parolePlanNoteIndex", required = false)
					final Integer parolePlanNoteIndex) {
		ModelAndView mav = new ModelAndView(
				PAROLE_PLAN_NOTES_ACTION_MENU_VIEW_NAME);
		mav.addObject(PAROLE_PLAN_NOTE_ITEM_INDEX_MODEL_KEY, 
				parolePlanNoteIndex);
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
	public ModelAndView handleParolePlanExistsException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PAROLE_PLAN_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final ParolePlanForm parolePlanForm,
			final ParoleEligibility paroleEligibility) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_PLAN_FORM_MODEL_KEY, 
				parolePlanForm);
		mav.addObject(PAROLE_ELIGIBILITY_MODEL_KEY, paroleEligibility);
		mav.addObject(DOCUMENT_ASSOCIATION_ITEM_INDEX_MODEL_KEY,
				(parolePlanForm.getParolePlanDocumentAssociationItems() != 
						null ? parolePlanForm
								.getParolePlanDocumentAssociationItems().size() 
								: 0));
			if(parolePlanForm.getParolePlanDocumentAssociationItems() != null){
				List<Integer> tagIndexes = new ArrayList<Integer>();
				for (int i = 0; i < parolePlanForm
						.getParolePlanDocumentAssociationItems().size(); i++){
					if (parolePlanForm.getParolePlanDocumentAssociationItems()
							.get(i) != null){
						tagIndexes.add(i,
							(parolePlanForm
								.getParolePlanDocumentAssociationItems().get(i)
								.getDocumentTagItems() != null ?
									parolePlanForm
									.getParolePlanDocumentAssociationItems()
									.get(i).getDocumentTagItems().size() : 0));
					}
					else{
						tagIndexes.add(i, 0);
					}
				}
				mav.addObject(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, tagIndexes);
			}
		mav.addObject(PAROLE_PLAN_NOTE_ITEM_INDEX_MODEL_KEY, 
				(parolePlanForm.getParolePlanNoteItems() != null ? 
						parolePlanForm.getParolePlanNoteItems().size() : 0));
		this.offenderSummaryModelDelegate.add(mav.getModel(), 
				paroleEligibility.getOffender());
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final ParolePlanForm parolePlanForm,
			final ParoleEligibility paroleEligibility,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(parolePlanForm, paroleEligibility);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ PAROLE_PLAN_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Processes document association items
	private void processDocumentAssociationItems(
			final ParolePlan parolePlan, 
			final List<ParolePlanDocumentAssociationItem>
					parolePlanDocumentAssociationItems) 
							throws DuplicateEntityFoundException {
		if(parolePlanDocumentAssociationItems != null){
			for(ParolePlanDocumentAssociationItem item : 
				parolePlanDocumentAssociationItems){
				if(ParolePlanItemOperation.CREATE
						.equals(item.getItemOperation())){
					final String fileExtension = item.getFileExtension();
					this.documentFilenameGenerator.setExtension(fileExtension);
					final String filename = 
							this.documentFilenameGenerator.generate();
					Document document = this.parolePlanService
							.createDocument(item.getFileDate(),
									item.getTitle(), filename, 
									item.getFileExtension());
					this.parolePlanDocumentPersister
							.persist(document, item.getData());
					this.parolePlanService
							.createParolePlanDocumentAssociation(parolePlan,
									document);
					processDocumentTagItems(document,
							item.getDocumentTagItems());
				}
				if(ParolePlanItemOperation.UPDATE
						.equals(item.getItemOperation())){
					Document document = this.parolePlanService
							.updateDocument(item.getDocument(),
									item.getFileDate(), item.getTitle());
					processDocumentTagItems(document,
							item.getDocumentTagItems());
				}
				if(ParolePlanItemOperation.REMOVE
						.equals(item.getItemOperation())){
					for(DocumentTag tag : this.parolePlanService
							.findDocumentTagsByDocument(item.getDocument())){
						this.parolePlanService.removeDocumentTag(tag);
					}
					this.parolePlanService
						.removeParolePlanDocumentAssociation(
						item.getParolePlanDocumentAssociation());
					this.parolePlanDocumentRemover.remove(
							item.getDocument().getFilename());
					this.parolePlanService.removeDocument(
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
				if(ParolePlanItemOperation
					.CREATE.equals(tagItem.getItemOperation())){
					this.parolePlanService.createDocumentTag(
							tagItem.getName(), document);
				}
				if(ParolePlanItemOperation
					.UPDATE.equals(tagItem.getItemOperation())){
					this.parolePlanService.updateDocumentTag(
							tagItem.getDocumentTag(),
							tagItem.getName());
				}
				if(ParolePlanItemOperation
					.REMOVE.equals(tagItem.getItemOperation())){
					this.parolePlanService.removeDocumentTag(
							tagItem.getDocumentTag());
				}
			}
		}
	}
	
	private void processNoteItems(final ParolePlan parolePlan, 
			final List<ParolePlanNoteItem> noteItems) 
					throws DuplicateEntityFoundException {
		if (noteItems != null) {
			for (ParolePlanNoteItem noteItem : noteItems) {
				if (ParolePlanItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.parolePlanService.createParolePlanNote(
							parolePlan, noteItem.getValue(), 
							noteItem.getDate());
				}
				if (ParolePlanItemOperation.UPDATE.equals(
						noteItem.getOperation())) {
					this.parolePlanService.updateParolePlanNote(
							noteItem.getParolePlanNote(), noteItem.getValue(), 
							noteItem.getDate());
				}
				if (ParolePlanItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.parolePlanService.removeParolePlanNote(
							noteItem.getParolePlanNote());
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
		final byte[] bytes = this.parolePlanDocumentRetriever
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
		binder.registerCustomEditor(ParolePlan.class,
				this.parolePlanPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(StaffAssignment.class,
				this.staffAssignmentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(ParolePlanDocumentAssociation.class, 
				this.parolePlanDocumentAssociationPropertyEditorFactory.
					createPropertyEditor());
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(ParolePlanNote.class, 
				this.parolePlanNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleEligibility.class, 
				this.paroleEligibilityPropertyEditorFactory
				.createPropertyEditor());
	}
}
