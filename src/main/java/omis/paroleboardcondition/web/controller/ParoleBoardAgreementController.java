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
package omis.paroleboardcondition.web.controller;

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
import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementAssociableDocument;
import omis.condition.domain.AgreementCategory;
import omis.condition.domain.Condition;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.io.DocumentPersister;
import omis.document.io.DocumentRetriever;
import omis.document.io.impl.DocumentFilenameGenerator;
import omis.document.web.form.DocumentTagItem;
import omis.document.web.form.DocumentTagOperation;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.io.FileRemover;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;
import omis.paroleboardcondition.service.ParoleBoardConditionService;
import omis.paroleboardcondition.web.form.ParoleBoardAgreementAssociableDocumentItem;
import omis.paroleboardcondition.web.form.ParoleBoardAgreementForm;
import omis.paroleboardcondition.web.form.ParoleBoardConditionItemOperation;
import omis.paroleboardcondition.web.validator.ParoleBoardAgreementFormValidator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Parole Board Agreement Controller.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 5, 2018)
 * @since OMIS 3.0
 *
 */
@Controller
@RequestMapping("/paroleBoardCondition")
@PreAuthorize("hasRole('USER')")
public class ParoleBoardAgreementController {
	
	/* View Names */
	
	private static final String EDIT_VIEW_NAME = "/paroleBoardCondition/edit";
	
	private static final String PAROLE_BOARD_AGREEMENT_ACTION_MENU_VIEW_NAME =
			"/paroleBoardCondition/includes/paroleBoardAgreementActionMenu";
	
	private static final String
		AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_ROW_VIEW_NAME =
			"/paroleBoardCondition/includes/"
			+ "paroleBoardAgreementAssociableDocumentItemContent";

	private static final String DOCUMENT_TAG_ITEM_ROW_VIEW_NAME =
			"/paroleBoardCondition/includes/documentTagItemContent";
	
	private static final String LIST_REDIRECT_VIEW_NAME =
			"redirect:/paroleBoardCondition/list.html?offender=%d";
	
	/* Model Keys */
	
	private static final String PAROLE_BOARD_AGREEMENT_FORM_MODEL_KEY =
			"paroleBoardAgreementForm";
	
	private static final String PAROLE_BOARD_AGREEMENT_MODEL_KEY =
			"paroleBoardAgreement";
	
	private static final String PAROLE_BOARD_AGREEMENT_CATEGORY_MODEL_KEY =
			"paroleBoardAgreementCategory";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String
		AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_INDEX_MODEL_KEY =
			"agreementAssociableDocumentItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY =
			"documentTagItem";
	
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY =
			"documentTagItemIndex";
	
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY =
			"documentTagItemIndexes";
	
	private static final String AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_MODEL_KEY =
			"agreementAssociableDocumentItem";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	private static final String HEARING_ANALYSIS_MODEL_KEY = "hearingAnalysis";
	
	/* Message Keys */
	
	private static final String PAROLE_BOARD_AGREEMENT_EXISTS_MESSAGE_KEY =
			"paroleBoardAgreement.exists";
	
	private static final String ERROR_WRITING_FILE_MSG = 
			"Error writing file: %s";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME =
			"omis.paroleboardcondition.msgs.form";
	
	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("paroleBoardConditionDocumentPersister")
	private DocumentPersister paroleBoardConditionDocumentPersister;
	
	@Autowired
	@Qualifier("paroleBoardConditionDocumentRetriever")
	private DocumentRetriever paroleBoardConditionDocumentRetriever;
	
	@Autowired
	@Qualifier("paroleBoardConditionDocumentRemover")
	private FileRemover paroleBoardConditionDocumentRemover;	
	
	/* Services. */
	
	@Autowired
	@Qualifier("paroleBoardConditionService")
	private ParoleBoardConditionService paroleBoardConditionService;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardAgreementPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardAgreementPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardAgreementCategoryPropertyEditorFactory")
	private PropertyEditorFactory
		paroleBoardAgreementCategoryPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("agreementAssociableDocumentPropertyEditorFactory")
	private PropertyEditorFactory
		agreementAssociableDocumentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("hearingAnalysisPropertyEditorFactory")
	private PropertyEditorFactory hearingAnalysisPropertyEditorFactory;
	
	/* Validator. */
	
	@Autowired
	@Qualifier("paroleBoardAgreementFormValidator")
	private ParoleBoardAgreementFormValidator paroleBoardAgreementFormValidator;
	
	/**
	 * Default constructor for ParoleBoardAgreementController. 
	 */
	public ParoleBoardAgreementController() {
	}
	
	/**
	 * Returns the Model and View for creating a Parole Board Agreement.
	 * 
	 * @param offender - Offender
	 * @param paroleBoardAgreementCategory - Parole Board Agreement Category
	 * @return ModelAndView - Model and View for creating a Parole Board
	 * Agreement.
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_BOARD_CONDITION_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView create(
			@RequestParam(value = "offender", required = true)
				final Offender offender, 
			@RequestParam(value = "paroleBoardAgreementCategory", 
				required = true) final ParoleBoardAgreementCategory 
					paroleBoardAgreementCategory,
			@RequestParam(value = "boardHearing", required = false)
				final BoardHearing boardHearing,
			@RequestParam(value = "hearingAnalysis", required = false)
				final HearingAnalysis hearingAnalysis) {
		if (boardHearing == null && hearingAnalysis == null) {
			throw new IllegalArgumentException(
					"Board hearing or hearing analysis required.");
		}
		return this.prepareEditMav(offender, paroleBoardAgreementCategory,
				boardHearing, hearingAnalysis,
				new ParoleBoardAgreementForm(), new ModelMap());
	}
	
	
	/**
	 * Creates a new Parole Board Agreement and its associated items and returns
	 * to the Board Agreements List screen.
	 * 
	 * @param offender - Offender
	 * @param paroleBoardAgreementCategory - Parole Board Agreement Category
	 * @param form - Parole Board Agreement Category
	 * @param bindingResult - Binding Category
	 * @return ModelAndView - Returns to the Board Agreements List screen, or
	 * back to the creation screen on form error.
	 * @throws DuplicateEntityFoundException - When another Agreement already
	 * exists with the same date range, description, and category for the
	 * specified offender.
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PAROLE_BOARD_CONDITION_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView save(
			@RequestParam(value = "offender", required = true)
				final Offender offender, 
			@RequestParam(value = "paroleBoardAgreementCategory", 
				required = true) final ParoleBoardAgreementCategory 
					paroleBoardAgreementCategory,
			@RequestParam(value = "boardHearing", required = false)
				final BoardHearing boardHearing,
			@RequestParam(value = "hearingAnalysis", required = false)
				final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementForm form,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		if (boardHearing == null && hearingAnalysis == null) {
			throw new IllegalArgumentException(
					"Board hearing or hearing analysis required.");
		}
		this.paroleBoardAgreementFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(offender, paroleBoardAgreementCategory,
					boardHearing, hearingAnalysis, form, new ModelMap());
		} else {
			Agreement agreement = this.paroleBoardConditionService
					.createAgreement(offender, form.getStartDate(),
							form.getEndDate(), form.getDescription(),
							AgreementCategory.BOARD_PARDONS_PAROLE);
			if (boardHearing != null) {
				this.paroleBoardConditionService.createParoleBoardAgreement(
						agreement, boardHearing, paroleBoardAgreementCategory);	
			} else if (hearingAnalysis != null) {
				this.paroleBoardConditionService.createParoleBoardAgreement(
						agreement, hearingAnalysis, 
						paroleBoardAgreementCategory);
			}
			
			this.processItems(agreement,
					form.getAgreementAssociableDocumentItems());
			return new ModelAndView(String.format(
					LIST_REDIRECT_VIEW_NAME, offender.getId()));
					
		}
	}
	
	/**
	 * Returns the Model and View for editing a Parole Board Agreement and its
	 * associated items.
	 * 
	 * @param paroleBoardAgreement - Parole Board Agreement to edit
	 * @return ModelAndView - Model and View for editing a Parole Board
	 * Agreement and its associated items.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_BOARD_CONDITION_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(
			value = "paroleBoardAgreement", required = true)
				final ParoleBoardAgreement paroleBoardAgreement) {
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute(PAROLE_BOARD_AGREEMENT_MODEL_KEY, 
				paroleBoardAgreement);
		return this.prepareEditMav(
				paroleBoardAgreement.getAgreement().getOffender(),
				paroleBoardAgreement.getCategory(),
				paroleBoardAgreement.getBoardHearing(),
				paroleBoardAgreement.getHearingAnalysis(),
				this.populateForm(paroleBoardAgreement), modelMap);
	}
	
	/**
	 * Updates the specified Parole Board Agreements and its associated items
	 * and returns to the Board Agreements List Screen.
	 * 
	 * @param paroleBoardAgreement - Parole Board Agreement to update
	 * @param form - Parole Board Agreement Form
	 * @param bindingResult - Binding Result
	 * @return ModelAndView - Returns to the Board Agreements List screen, or
	 * back to the edit screen on form error.
	 * @throws DuplicateEntityFoundException - When another Agreement already
	 * exists with the same date range, description, and category for the
	 * specified offender.
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PAROLE_BOARD_CONDITION_CREATE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(
			value = "paroleBoardAgreement", required = true)
				final ParoleBoardAgreement paroleBoardAgreement,
				final ParoleBoardAgreementForm form,
				final BindingResult bindingResult)
						throws DuplicateEntityFoundException {
		this.paroleBoardAgreementFormValidator.validate(form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			ModelMap modelMap = new ModelMap();
			modelMap.addAttribute(PAROLE_BOARD_AGREEMENT_MODEL_KEY, 
					paroleBoardAgreement);
			return this.prepareEditMav(
					paroleBoardAgreement.getAgreement().getOffender(), 
					paroleBoardAgreement.getCategory(),
					paroleBoardAgreement.getBoardHearing(),
					paroleBoardAgreement.getHearingAnalysis(),
					form, modelMap);
		} else {
			Agreement agreement = this.paroleBoardConditionService
					.updateAgreement(paroleBoardAgreement.getAgreement(),
							form.getStartDate(), form.getEndDate(),
							form.getDescription(),
							AgreementCategory.BOARD_PARDONS_PAROLE);
			this.paroleBoardConditionService.updateParoleBoardAgreement(
					paroleBoardAgreement, paroleBoardAgreement.getCategory());
			this.processItems(agreement,
					form.getAgreementAssociableDocumentItems());
			return new ModelAndView(String.format(
					LIST_REDIRECT_VIEW_NAME,
					paroleBoardAgreement.getAgreement().getOffender().getId()));
					
		}
	}
	
	/**
	 * Removes the specified Parole Board Agreement and its associated items
	 * and returns to the Board Agreements List screen.
	 * 
	 * @param paroleBoardAgreement - Parole Board Agreement to remove
	 * @return ModelAndView - Parole Board Agreements list screen
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PAROLE_BOARD_CONDITION_REMOVE') "
			+ "or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "paroleBoardAgreement", required = true)
			final ParoleBoardAgreement paroleBoardAgreement) {
		for (Condition condition
				: this.paroleBoardConditionService.findConditionsByAgreement(
						paroleBoardAgreement.getAgreement())) {
			this.paroleBoardConditionService.removeCondition(condition);
		}
		for (AgreementAssociableDocument agreementAssociableDocument
				: this.paroleBoardConditionService
				.findAgreementAssociableDocumentsByAgreement(
						paroleBoardAgreement.getAgreement())) {
			for (DocumentTag documentTag : this.paroleBoardConditionService
					.findDocumentTagsByDocument(
							agreementAssociableDocument.getDocument())) {
				this.paroleBoardConditionService.removeDocumentTag(documentTag);
			}
			this.paroleBoardConditionService
				.removeAgreementAssociableDocument(agreementAssociableDocument);
			this.paroleBoardConditionService.removeDocument(
					agreementAssociableDocument.getDocument());
		}
		this.paroleBoardConditionService.removeParoleBoardAgreement(
				paroleBoardAgreement);
		this.paroleBoardConditionService.removeAgreement(
				paroleBoardAgreement.getAgreement());
		return new ModelAndView(String.format(
				LIST_REDIRECT_VIEW_NAME,
				paroleBoardAgreement.getAgreement().getOffender().getId()));
	}
	
	/**
	 * Returns the Model and View for the Parole Board Agreement Action Menu.
	 * 
	 * @param offender - Offender
	 * @return ModelAndView - Model and View for the Parole Board Agreement
	 * Action Menu.
	 */
	@RequestMapping(value = "/paroleBoardAgreementActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayParoleBoardAgreementActionMenu(
			@RequestParam(value = "offender", required = true)
				final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
				PAROLE_BOARD_AGREEMENT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/* AJAX views */
	
	/**
	 * Returns the Model and View for an Agreement Associable Document item row.
	 * 
	 * @param agreementAssociableDocumentItemIndex - Integer
	 * @return ModelAndView - Model and View for an Agreement Associable
	 * Document item row.
	 */
	@RequestMapping(value = "createAgreementAssociableDocumentItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayAgreementAssociableDocumentItemRow(@RequestParam(
			value = "agreementAssociableDocumentItemIndex", required = true)
				final Integer agreementAssociableDocumentItemIndex) {
		ModelMap map = new ModelMap();
		ParoleBoardAgreementAssociableDocumentItem item =
				new ParoleBoardAgreementAssociableDocumentItem();
		item.setItemOperation(ParoleBoardConditionItemOperation.CREATE);
		map.addAttribute(AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_MODEL_KEY, item);
		map.addAttribute(AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_INDEX_MODEL_KEY,
				agreementAssociableDocumentItemIndex);
		return new ModelAndView(
				AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns the Model and View for a Document Tag Item Row.
	 * 
	 * @param agreementAssociableDocumentItemIndex - Integer
	 * @param documentTagItemIndex - Integer
	 * @return ModelAndView - Model and View for a Document Tag Item Row.
	 */
	@RequestMapping(value = "createDocumentTagItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDocumentTagItemRow(@RequestParam(
			value = "agreementAssociableDocumentItemIndex", required = true)
				final Integer agreementAssociableDocumentItemIndex,
			@RequestParam(value = "documentTagItemIndex", required = true)
					final Integer documentTagItemIndex) {
		ModelMap map = new ModelMap();
		DocumentTagItem tagItem = new DocumentTagItem();
		tagItem.setDocumentTagOperation(DocumentTagOperation.CREATE);
		map.addAttribute(AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_INDEX_MODEL_KEY,
				agreementAssociableDocumentItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_ROW_VIEW_NAME,
				map);
	}
	
	/* Helper Methods */
	
	/**
	 * Populates a Parol Board Agreement form with the given Parole Board
	 * Agreement's properties.
	 * 
	 * @param paroleBoardAgreement - Parole Board Agreement
	 * @return ParoleBoardAgreementForm - Populated Parole Board Agreement Form
	 */
	private ParoleBoardAgreementForm populateForm(
			final ParoleBoardAgreement paroleBoardAgreement) {
		ParoleBoardAgreementForm form = new ParoleBoardAgreementForm();
		form.setDescription(
				paroleBoardAgreement.getAgreement().getDescription());
		form.setStartDate(paroleBoardAgreement.getAgreement()
				.getDateRange().getStartDate());
		form.setEndDate(paroleBoardAgreement.getAgreement()
				.getDateRange().getEndDate());
		List<ParoleBoardAgreementAssociableDocumentItem> documentItems =
				new ArrayList<ParoleBoardAgreementAssociableDocumentItem>();
		for (AgreementAssociableDocument agreementAssociableDocument
				: this.paroleBoardConditionService
				.findAgreementAssociableDocumentsByAgreement(
						paroleBoardAgreement.getAgreement())) {
			ParoleBoardAgreementAssociableDocumentItem item =
					new ParoleBoardAgreementAssociableDocumentItem();
			item.setAgreementAssociableDocument(agreementAssociableDocument);
			item.setItemOperation(ParoleBoardConditionItemOperation.UPDATE);
			item.setDate(agreementAssociableDocument.getDocument().getDate());
			item.setDocument(agreementAssociableDocument.getDocument());
			item.setFileExtension(agreementAssociableDocument.getDocument()
					.getFileExtension());
			item.setTitle(agreementAssociableDocument.getDocument().getTitle());
			item.setData(this.paroleBoardConditionDocumentRetriever.retrieve(
					agreementAssociableDocument.getDocument()));
			List<DocumentTagItem> documentTagItems =
					new ArrayList<DocumentTagItem>();
			for (DocumentTag tag : this.paroleBoardConditionService
					.findDocumentTagsByDocument(
							agreementAssociableDocument.getDocument())) {
				DocumentTagItem tagItem = new DocumentTagItem();
				tagItem.setDocumentTag(tag);
				tagItem.setName(tag.getName());
				tagItem.setDocumentTagOperation(DocumentTagOperation.UPDATE);
				documentTagItems.add(tagItem);
			}
			item.setTagItems(documentTagItems);
			documentItems.add(item);
		}
		form.setAgreementAssociableDocumentItems(documentItems);
		
		return form;
	}

	/**
	 * Returns a prepared Model And View for creation/editing a Parole Board
	 * Agreement.
	 * 
	 * @param offender - Offender
	 * @param paroleBoardAgreementCategory - Parole Board Agreement Category
	 * @param paroleBoardAgreementForm - Parole Board Agreement Form
	 * @param modelMap - Model Map
	 * @return ModelAndView - Prepared Model And View for creation/editing a
	 * Parole Board Agreement.
	 */
	private ModelAndView prepareEditMav(final Offender offender,
			final ParoleBoardAgreementCategory paroleBoardAgreementCategory,
			final BoardHearing boardHearing, 
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementForm paroleBoardAgreementForm,
			final ModelMap modelMap) {
		modelMap.addAttribute(OFFENDER_MODEL_KEY, offender);
		modelMap.addAttribute(PAROLE_BOARD_AGREEMENT_CATEGORY_MODEL_KEY,
				paroleBoardAgreementCategory);
		modelMap.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		modelMap.addAttribute(HEARING_ANALYSIS_MODEL_KEY, hearingAnalysis);
		modelMap.addAttribute(PAROLE_BOARD_AGREEMENT_FORM_MODEL_KEY,
				paroleBoardAgreementForm);
		List<Integer> dtIndexes = new ArrayList<Integer>();
		int counter = 0;
		for (ParoleBoardAgreementAssociableDocumentItem item
				: paroleBoardAgreementForm
				.getAgreementAssociableDocumentItems()) {
			dtIndexes.add(counter, item.getTagItems().size());
			counter++;
		}
		modelMap.addAttribute(
				AGREEMENT_ASSOCIABLE_DOCUMENT_ITEM_INDEX_MODEL_KEY,
				paroleBoardAgreementForm
				.getAgreementAssociableDocumentItems().size());
		modelMap.addAttribute(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, dtIndexes);
		this.offenderSummaryModelDelegate.add(modelMap, offender);
		
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/**
	 * Processes a list of Parole Board Agreement Associable Document Items for
	 * creation, updating, or removal.
	 * 
	 * @param agreement - Agreement
	 * @param documentItems - List of Parole Board Agreement Associable Document
	 * Items
	 * @throws DuplicateEntityFoundException - When an Agreement Associable
	 * Document already exists.
	 */
	private void processItems(final Agreement agreement,
			final List<ParoleBoardAgreementAssociableDocumentItem>
				documentItems)
					throws DuplicateEntityFoundException {
		for (ParoleBoardAgreementAssociableDocumentItem item : documentItems) {
			if (ParoleBoardConditionItemOperation.CREATE.equals(
					item.getItemOperation())) {
				final String fileExtension = item.getFileExtension();
				this.documentFilenameGenerator
						.setExtension(fileExtension);
				final String filename =
						this.documentFilenameGenerator.generate();
				Document document = this.paroleBoardConditionService
						.createDocument(item.getDate(),
								filename, item.getFileExtension(),
								item.getTitle());
				this.paroleBoardConditionDocumentPersister
						.persist(document, item.getData());
				this.paroleBoardConditionService
					.createAgreementAssociableDocument(agreement, document);
				if (item.getTagItems() != null) {
					for (DocumentTagItem tagItem : item.getTagItems()) {
						if (DocumentTagOperation.CREATE.equals(
								tagItem.getDocumentTagOperation())) {
							this.paroleBoardConditionService.createDocumentTag(
									document, tagItem.getName());
						}
					}
				}
			} else if (ParoleBoardConditionItemOperation.UPDATE.equals(
					item.getItemOperation())) {
				Document document = this.paroleBoardConditionService
						.updateDocument(item.getDocument(),
								item.getDate(), item.getTitle());
				if (item.getTagItems() != null) {
					for (DocumentTagItem tagItem : item.getTagItems()) {
						if (DocumentTagOperation.CREATE.equals(
								tagItem.getDocumentTagOperation())) {
							this.paroleBoardConditionService.createDocumentTag(
									document, tagItem.getName());
						}
						if (DocumentTagOperation.UPDATE.equals(
								tagItem.getDocumentTagOperation())) {
							this.paroleBoardConditionService.updateDocumentTag(
									tagItem.getDocumentTag(),
									tagItem.getName());
						}
						if (DocumentTagOperation.REMOVE.equals(
								tagItem.getDocumentTagOperation())) {
							this.paroleBoardConditionService.removeDocumentTag(
									tagItem.getDocumentTag());
						}
					}
				}
			} else if (ParoleBoardConditionItemOperation.REMOVE.equals(
					item.getItemOperation())) {
				for (DocumentTag tag : this.paroleBoardConditionService
						.findDocumentTagsByDocument(item.getDocument())) {
					this.paroleBoardConditionService.removeDocumentTag(tag);
				}
				this.paroleBoardConditionService
					.removeAgreementAssociableDocument(
						item.getAgreementAssociableDocument());
				this.paroleBoardConditionDocumentRemover.remove(
						item.getDocument().getFilename());
				this.paroleBoardConditionService.removeDocument(
						item.getDocument());
			}
		}
	}
	
	/** Retrieves document file.
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response. */
	@RequestMapping(value = "retrieveFile.html", method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.paroleBoardConditionDocumentRetriever
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
				PAROLE_BOARD_AGREEMENT_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
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
		binder.registerCustomEditor(ParoleBoardAgreement.class,
				this.paroleBoardAgreementPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardAgreementCategory.class,
				this.paroleBoardAgreementCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Document.class,
				this.documentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class,
				this.documentTagPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(AgreementAssociableDocument.class,
				this.agreementAssociableDocumentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(BoardHearing.class,
				this.boardHearingPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(HearingAnalysis.class,
				this.hearingAnalysisPropertyEditorFactory
				.createPropertyEditor());
	}
}
