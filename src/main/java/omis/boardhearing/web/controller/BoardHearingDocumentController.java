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
package omis.boardhearing.web.controller;

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

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.boardhearing.service.BoardHearingDocumentService;
import omis.boardhearing.web.controller.delegate.BoardHearingSummaryModelDelegate;
import omis.boardhearing.web.form.BoardHearingDocumentForm;
import omis.boardhearing.web.validator.BoardHearingDocumentFormValidator;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.io.DocumentPersister;
import omis.document.io.DocumentRetriever;
import omis.document.io.impl.DocumentFilenameGenerator;
import omis.document.web.form.DocumentTagItem;
import omis.document.web.form.DocumentTagOperation;
import omis.exception.DuplicateEntityFoundException;
import omis.io.FileRemover;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Board hearing document controller.
 * 
 * @author Trevor Isles
 * @version 0.1.0 (Mar 13, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/boardHearing")
@PreAuthorize("hasRole('USER')")
public class BoardHearingDocumentController {
	
	/* VIEW NAMES */

	private static final String EDIT_VIEW_NAME 
		= "boardHearing/document/edit";
	
	private static final String BOARD_HEARING_DOCUMENT_ACTION_MENU_VIEW_NAME
		= "/boardHearing/document/includes/boardHearingDocumentActionMenu";
	
	/* MODEL KEYS */
	
	private static final String FORM_MODEL_KEY = "boardHearingDocumentForm";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	private static final String BOARD_HEARING_ASSOCIABLE_DOCUMENT_MODEL_KEY
		= "boardHearingAssociableDocument";
	
	/* REDIRECT VIEW NAMES */
	
	private static final String LIST_REDIRECT 
		= "redirect:/boardHearing/document/list.html?boardHearing=%d";
	
	/* Message Keys */
	
	private static final String ENTITY_EXISTS_MESSAGE_KEY
		= "documentEntity.exists";
	
	private static final String ERROR_WRITING_FILE_MSG
		= "Error writing file: %s";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME 
		= "omis.boardHearing.msgs.form";
	
	/* Services */
	
	@Autowired
	@Qualifier("boardHearingDocumentService")
	private BoardHearingDocumentService boardHearingDocumentService;
	
	/* Property Editors */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingAssociableDocumentPropertyEditorFactory")
	private PropertyEditorFactory 
		boardHearingAssociableDocumentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("boardHearingSummaryModelDelegate")
	private BoardHearingSummaryModelDelegate boardHearingSummaryModelDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("documentFilenameGenerator")
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("boardHearingDocumentPersister")
	private DocumentPersister boardHearingDocumentPersister;
	
	@Autowired
	@Qualifier("boardHearingDocumentRetriever")
	private DocumentRetriever boardHearingDocumentRetriever;
	
	@Autowired
	@Qualifier("boardHearingDocumentRemover")
	private FileRemover boardHearingDocumentRemover;
	
	/* Validator */
	
	@Autowired
	@Qualifier("boardHearingDocumentFormValidator")
	private BoardHearingDocumentFormValidator boardHearingDocumentFormValidator;
	
	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
			final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView
		(ENTITY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/**
	 * Returns a model and view to create an board hearing associable document
	 * @param boardHearing - board hearing
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/document/create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_DOCUMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(
			value = "boardHearing", required = true)
		final BoardHearing boardHearing){
		
		return this.prepareEditMav(boardHearing, new BoardHearingDocumentForm(),
				new ModelMap());
	}
	
	/**
	 * Attempts to save an board hearing associable document and returns to the 
	 * list screen on successful save
	 * @param boardHearing - board hearing
	 * @param form - BoardHearingDocumentForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - returns to the list screen after successful save, 
	 * or back to edit screen on form error
	 * @throws DuplicateEntityFoundException - When 
	 * BoardHearingAssociableDocument already exists with provided document and 
	 * board hearing (should never happen under normal circumstances)
	 */
	@RequestMapping(value = "/document/create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('BOARD_HEARING_DOCUMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "boardHearing", 
			required = true)
		final BoardHearing boardHearing, final BoardHearingDocumentForm form,
		final BindingResult bindingResult)
				throws DuplicateEntityFoundException{
		
		this.boardHearingDocumentFormValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			return prepareEditMav(boardHearing, form, new ModelMap());
		}
		else{
			final String fileExtension = form.getFileExtension();
			this.documentFilenameGenerator
					.setExtension(fileExtension);
			final String filename =
					this.documentFilenameGenerator.generate();
			Document document = this.boardHearingDocumentService.createDocument(
					form.getDate(), form.getTitle(), filename, fileExtension);
			this.boardHearingDocumentPersister.persist(document, form.getData());
			this.processDocumentTags(form.getDocumentTagItems(), document);
			
			this.boardHearingDocumentService
				.createBoardHearingAssociableDocument(boardHearing, document);
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					boardHearing.getId()));
		}
	}
	
	/**
	 * Returns ModelAndView to edit a specified board hearing associable document
	 * @param boardHearingAssociableDocument - board hearing associable document 
	 * being edited
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/document/edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_DOCUMENT_VIEW') or "
			+ "hasRole('ADMIN')")
	public ModelAndView edit(
			@RequestParam(value = "boardHearingAssociableDocument",
			required = true)
			final BoardHearingAssociableDocument boardHearingAssociableDocument){
		ModelMap map = new ModelMap();
		map.addAttribute(BOARD_HEARING_ASSOCIABLE_DOCUMENT_MODEL_KEY,
				boardHearingAssociableDocument);
		
		return this.prepareEditMav(boardHearingAssociableDocument
					.getBoardHearing(),
				this.prepareEditForm(boardHearingAssociableDocument), map);
	}
	
	/**
	 * Attempts to update a board hearing associable document and return to the 
	 * list screen
	 * @param boardHearingAssociableDocument - BoardhearingAssociableDocument 
	 * to be edited
	 * @param form - BoardHearingDocumentForm
	 * @param bindingResult - BindingResult
	 * @return ModelAndView - returns to the list screen after successful update, 
	 * or back to edit screen on form error
	 * @throws DuplicateEntityFoundException - When 
	 * BoardHearingAssociableDocument already exists with provided document and 
	 * board hearing (should never happen under normal circumstances)
	 */
	@RequestMapping(value = "/document/edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('BOARD_HEARING_DOCUMENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
			@RequestParam(value = "boardHearingAssociableDocument", 
			required = true)
			final BoardHearingAssociableDocument boardHearingAssociableDocument,
			final BoardHearingDocumentForm form, 
			final BindingResult bindingResult)
					throws DuplicateEntityFoundException{
		
		this.boardHearingDocumentFormValidator.validate(form, bindingResult);
		
		if(bindingResult.hasErrors()){
			ModelMap map = new ModelMap();
			map.addAttribute(BOARD_HEARING_ASSOCIABLE_DOCUMENT_MODEL_KEY,
					boardHearingAssociableDocument);
			return prepareEditMav(boardHearingAssociableDocument
					.getBoardHearing(), form, map);
		}
		else{
			Document document = this.boardHearingDocumentService.updateDocument(
					boardHearingAssociableDocument.getDocument(), 
					form.getDate(), form.getTitle(), form.getFilename(), 
					form.getFileExtension());
			this.processDocumentTags(form.getDocumentTagItems(), document);
			this.boardHearingDocumentService
				.updateBoardHearingAssociableDocument(
						boardHearingAssociableDocument, 
						boardHearingAssociableDocument.getBoardHearing(), 
						document);
			
			
			return new ModelAndView(String.format(LIST_REDIRECT,
					boardHearingAssociableDocument.getBoardHearing().getId()));
		}
	}
	
	/**
	 * Removes a specified board hearing associable document
	 * @param boardHearingAssociableDocument - BoardHearingAssociableDocument 
	 * to be removed
	 * @return ModelAndView - Returns to the list screen after successful 
	 * removal
	 */
	@RequestMapping(value = "/document/remove.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_DOCUMENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
			@RequestParam(value = "boardHearingAssociableDocument", 
			required = true)
			final BoardHearingAssociableDocument boardHearingAssociableDocument){
		List<DocumentTag> documentTags = this.boardHearingDocumentService
				.findDocumentTagsByDocument(boardHearingAssociableDocument
						.getDocument());
		for(DocumentTag documentTag : documentTags){
			this.boardHearingDocumentService.removeDocumentTag(documentTag);
		}
		this.boardHearingDocumentService.removeBoardHearingAssociableDocument(
				boardHearingAssociableDocument);
		this.boardHearingDocumentRemover.remove(boardHearingAssociableDocument
				.getDocument().getFilename());
		this.boardHearingDocumentService.removeDocument
		(boardHearingAssociableDocument.getDocument());
		
		return new ModelAndView(String.format(LIST_REDIRECT,
				boardHearingAssociableDocument.getBoardHearing().getId()));
	}
	
/* Action Menus */
	
	/**
	 * Returns model and view for board hearing document action menu
	 * @param boardHearing - board hearing
	 * @return ModelAndView
	 */
	@RequestMapping(value="/document/boardHearingDocumentActionMenu.html", 
			method=RequestMethod.GET)
	public ModelAndView displayBoardHearingDocumentActionMenu(@RequestParam(
			value = "boardHearing", required = true) 
		final BoardHearing boardHearing){
		
		ModelMap map = new ModelMap();
		
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		
		return new ModelAndView(BOARD_HEARING_DOCUMENT_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	/* Helpers */
	
	/**
	 * Prepares a ModelAndView for creation/editing of a board hearing 
	 * associable document
	 * @param boardHearing - Board hearing for which 
	 * BoardHearingAssociableDocument is being made
	 * @param form - BoardHearingDocumentForm
	 * @param map - Modelmap
	 * @return ModelAndView - edit screen for an BoardHearingAssociableDocument
	 */
	private ModelAndView prepareEditMav(final BoardHearing boardHearing,
			final BoardHearingDocumentForm form, final ModelMap map){

		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		map.addAttribute(FORM_MODEL_KEY, form);
		this.offenderSummaryModelDelegate.add(map, boardHearing
				.getParoleEligibility().getOffender());
		this.boardHearingSummaryModelDelegate.add(map, boardHearing);
		
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/**
	 * Populates a board hearing document form with an existing 
	 * BoardHearingAssociableDocument's properties 
	 * @param boardHearingAssociableDocument - BoardHearingAssociableDocument 
	 * to be edited
	 * @return Populated BoardHearingDocumentForm
	 */
	private BoardHearingDocumentForm prepareEditForm(
			final BoardHearingAssociableDocument boardHearingAssociableDocument){
		
		BoardHearingDocumentForm form = new BoardHearingDocumentForm();
		
		List<DocumentTag> documentTags = this.boardHearingDocumentService
				.findDocumentTagsByDocument(
						boardHearingAssociableDocument.getDocument());
		List<DocumentTagItem> documentTagItems 
			= new ArrayList<DocumentTagItem>();
		for(DocumentTag tag : documentTags){
			DocumentTagItem item = new DocumentTagItem();
			
			item.setDocumentTag(tag);
			item.setName(tag.getName());
			item.setDocumentTagOperation(DocumentTagOperation.UPDATE);
			
			documentTagItems.add(item);
		}
		
		form.setDate(boardHearingAssociableDocument.getDocument().getDate());
		form.setDocument(boardHearingAssociableDocument.getDocument());
		form.setDocumentTagItems(documentTagItems);
		form.setFileExtension(boardHearingAssociableDocument.getDocument()
				.getFileExtension());
		form.setTitle(boardHearingAssociableDocument.getDocument().getTitle());
		form.setData(this.boardHearingDocumentRetriever.retrieve(
				boardHearingAssociableDocument.getDocument()));
		
		return form;
	}
	
	/**
	 * Processes a list of documentTag items for creation, updating, or removal
	 * @param documentTagItems - List of DocumentTagItems
	 * @param document - Document for which the documentTags are being processed
	 * for
	 * @throws DuplicateEntityFoundException - When a document tag already exists
	 * with given name and document
	 */
	private void processDocumentTags(
			final List<DocumentTagItem> documentTagItems, 
			final Document document) throws DuplicateEntityFoundException {
		if(documentTagItems != null){
			Iterator<DocumentTagItem> documentTagIterator = 
					documentTagItems.iterator();
			while (documentTagIterator.hasNext()) {
				final DocumentTagItem documentTagItem 
					= documentTagIterator.next();
				final DocumentTagOperation documentTagOperation = 
						documentTagItem.getDocumentTagOperation();
				if (documentTagOperation == DocumentTagOperation.UPDATE) {
					this.boardHearingDocumentService.updateDocumentTag(
							documentTagItem.getDocumentTag(), 
							documentTagItem.getName(), document);
				} else if (documentTagOperation == DocumentTagOperation.CREATE){
					this.boardHearingDocumentService.createDocumentTag(
							documentTagItem.getName(), document);
				} else if (documentTagOperation == DocumentTagOperation.REMOVE){
					this.boardHearingDocumentService.removeDocumentTag(
							documentTagItem.getDocumentTag());
				}
			}
		}
	}
	
	/** Retrieves document file.
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response. */
	@RequestMapping(value = "document/retrieveFile.html", method 
			= RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.boardHearingDocumentRetriever
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
	
/* InitBinder */
	
	/**
	 * Sets up and registers property editors
	 * 
	 * @param binder - web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder){
		binder.registerCustomEditor(BoardHearingAssociableDocument.class, 
				this.boardHearingAssociableDocumentPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(BoardHearing.class, 
				this.boardHearingPropertyEditorFactory.createPropertyEditor());
	}

}
