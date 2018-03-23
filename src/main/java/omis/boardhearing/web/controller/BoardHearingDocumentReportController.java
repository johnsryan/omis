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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.boardhearing.report.BoardHearingDocumentSummaryReportService;
import omis.boardhearing.web.controller.delegate.BoardHearingSummaryModelDelegate;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;

/**
 *
 *
 * @author Trevor Isles
 * @version 0.1.0 (Mar 20, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/boardHearing/document")
@PreAuthorize("hasRole('USER')")
public class BoardHearingDocumentReportController {
	
	private static final String LIST_VIEW_NAME = "/boardHearing/document/list";
	
	private static final String BOARD_HEARING_DOCUMENTS_ACTION_MENU_VIEW_NAME
		= "/boardHearing/document/includes/boardHearingDocumentsActionMenu";
	
	private static final String 
		BOARD_HEARING_DOCUMENTS_ROW_ACTION_MENU_VIEW_NAME
		= "/boardHearing/document/includes/boardHearingDocumentsRowActionMenu";
	
	/* Model Keys */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String BOARD_HEARING_MODEL_KEY = "boardHearing";
	
	private static final String BOARD_HEARING_DOCUMENT_SUMMARIES_MODEL_KEY
		= "boardHearingDocumentSummaries";
	
	private static final String BOARD_HEARING_ASSOCIABLE_DOCUMENT_MODEL_KEY
		= "boardHearingAssociableDocument";
	
	/* Helpers */
	
	@Autowired
	@Qualifier("boardHearingSummaryModelDelegate")
	private BoardHearingSummaryModelDelegate boardHearingSummaryModelDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Property Editor Factories */
	
	@Autowired
	@Qualifier("boardHearingPropertyEditorFactory")
	private PropertyEditorFactory boardHearingPropertyEditorFactory;
	
	@Autowired
	@Qualifier("boardHearingAssociableDocumentPropertyEditorFactory")
	private PropertyEditorFactory 
				boardHearingAssociableDocumentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	/* Service */
	
	@Autowired
	@Qualifier("boardHearingDocumentSummaryReportService")
	private BoardHearingDocumentSummaryReportService 
				boardHearingDocumentSummaryReportService;
	
	/**
	 * Default constructor for board hearing document report controller.
	 */
	public BoardHearingDocumentReportController() {
		// Default instantiation
	}
	
	/**
	 * Displays the Model And View of the board hearing documents list screen.
	 * 
	 * @param boardHearing - board hearing
	 * @return Model And View of the board hearing documents list screen.
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('BOARD_HEARING_DOCUMENT_LIST') or hasRole('ADMIN')")
	public ModelAndView showContent(@RequestParam(value = "boardHearing", 
			required = true)
			final BoardHearing boardHearing) {
		ModelMap map = new ModelMap();
		map.addAttribute(BOARD_HEARING_DOCUMENT_SUMMARIES_MODEL_KEY, 
				this.boardHearingDocumentSummaryReportService
				.findBoardHearingDocumentSummariesByHearing(
						boardHearing));
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		this.offenderSummaryModelDelegate.add(map, boardHearing
				.getParoleEligibility().getOffender());
		this.boardHearingSummaryModelDelegate.add(map, boardHearing);
		return new ModelAndView(LIST_VIEW_NAME, map);
	}
	
	/**
	 * Displays the Model And View for the board hearing documents action menu.
	 * 
	 * @param boardHearing - board hearing
	 * @return Model and View for the board hearing documents action menu
	 */
	@RequestMapping(value = "/boardHearingDocumentsActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayBoardHearingDocumentsActionMenu(
			@RequestParam(value = "boardHearing", required = true)
			final BoardHearing boardHearing) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, boardHearing
				.getParoleEligibility().getOffender());
		map.addAttribute(BOARD_HEARING_MODEL_KEY, boardHearing);
		return new ModelAndView(BOARD_HEARING_DOCUMENTS_ACTION_MENU_VIEW_NAME, 
				map);
	}
	
	/**
	 * Displays the Model and View for the board hearing documents row action
	 * menu.
	 * 
	 * @param boardHearingAssociableDocument - board hearing associable document
	 * @return Model and View for the board hearing documents row action menu
	 */
	@RequestMapping(value = "/boardHearingDocumentsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayBoardHearingDocumentRowActionMenu(
			@RequestParam(value = "boardHearingAssociableDocument",
			required = true)
			final BoardHearingAssociableDocument boardHearingAssociableDocument) {
		ModelMap map = new ModelMap();
		map.addAttribute(BOARD_HEARING_ASSOCIABLE_DOCUMENT_MODEL_KEY, 
				boardHearingAssociableDocument);
		return new ModelAndView(
				BOARD_HEARING_DOCUMENTS_ROW_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Init binder.
	 * @param binder - web data binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(BoardHearingAssociableDocument.class, 
				this.boardHearingAssociableDocumentPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(BoardHearing.class, 
				this.boardHearingPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
	}

}
