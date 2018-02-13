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
package omis.trackeddocument.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.docket.domain.Docket;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.trackeddocument.domain.TrackedDocumentCategory;
import omis.trackeddocument.domain.TrackedDocumentReceival;
import omis.trackeddocument.exception.TrackedDocumentReceivalExistsException;
import omis.trackeddocument.service.DocumentTrackingService;
import omis.trackeddocument.web.form.TrackedDocumentForm;
import omis.trackeddocument.web.form.TrackedDocumentReceivalItem;
import omis.trackeddocument.web.form.TrackedDocumentReceivalItemOperation;
import omis.trackeddocument.web.validator.TrackedDocumentFormValidator;
import omis.user.domain.UserAccount;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for tracked document.
 * 
 * @author Yidong Li
 * @author Sierra Rosales
 * @version 0.1.5 (Jan 8, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/trackedDocument")
@PreAuthorize("hasRole('USER')")
public class ManageTrackedDocumentController {
	/* views */
	private static final String EDIT_VIEW_NAME = "trackedDocument/edit";
	private static final String
		TRACKED_DOCUMENT_EDIT_TABLE_ACTION_MENU_VIEW_NAME
		= "trackedDocument/includes/trackedDocumentEditTableActionMenu";
	private static final String	
		TRACKED_DOCUMENT_EDIT_SCREEN_ACTION_MENU_VIEW_NAME
		= "trackedDocument/includes/trackedDocumentEditScreenActionMenu";
	private static final String 
		CREATE_TRACKED_DOCUMENTS_TABLE_ROW_VIEW_NAME 
		= "trackedDocument/includes/createTrackedDocumentsTableRow";
	
	/* model keys */
	private static final String TRACKED_DOCUMENT_EDIT_FORM_MODEL_KEY 
		= "trackedDocumentForm"; 
	private static final String TRACKED_DOCUMENT_CATEGORIES_MODEL_KEY
		= "categories";
	private static final String 
		TRACKED_DOCUMENT_RECEIVAL_EXISTS_EXCEPTION_MESSAGE_KEY
		= "trackedDocument.Conflicts";
	private static final String ITEM_INDEX_MODEL_KEY
		= "itemIndex";
	private static final String DOCKETS_MODEL_KEY = "dockets";
	private static final String TRACKED_DOCUMENT_ITEM_INDEX_MODEL_KEY 
		= "trackedDocumentReceivalIndex";
	private static final String TRACKED_DOCUMENT_RECEIVAL_ITEM_MODEL_KEY
		= "trackedDocumentReceivalItem";
	private static final String OFFENDER_MODEL_KEY
		= "offender";
	private static final String CREATE_FLAG_MODEL_KEY
		= "createFlag";
	private static final String TRACKED_DOCUMENT_MODEL_KEY 
		= "trackedDocument";
	
	/* Report names. */
	
	private static final String TRACKED_DOCUMENT_LISTING_REPORT_NAME
		= "/Legal/Tracked_Documents/Tracked_Document_Listing";
	
	private static final String TRACKED_DOCUMENT_DETAILS_REPORT_NAME
		= "/Legal/Tracked_Documents/Tracked_Document_Details";
	
	/* Report parameter names. */
	
	private static final String TRACKED_DOCUMENT_LISTING_ID_REPORT_PARAM_NAME
		= "OFFENDER_ID";
	
	private static final String TRACKED_DOCUMENT_DETAILS_ID_REPORT_PARAM_NAME
		= "DOCKET_ID";	
	
	/* Message bundles. */
	private static final String ERROR_BUNDLE_NAME
		= "omis.trackeddocument.msgs.form";
				
	/* Redirects. */
	private static final String LIST_REDIRECT
		= "redirect:/trackedDocumentReport/list.html?offender=%d";
	
	/* Property editor. */
	@Autowired
	@Qualifier("trackedDocumentReceivalPropertyEditorFactory")
	private PropertyEditorFactory trackedDocumentReceivalPropertyEditorFactory;
	
	@Autowired
	@Qualifier("trackedDocumentCategoryPropertyEditorFactory")
	private PropertyEditorFactory trackedDocumentCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
		
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
		
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("docketPropertyEditorFactory")
	private PropertyEditorFactory docketPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
		
	/* Services. */
	@Autowired
	@Qualifier("documentTrackingService")
	private DocumentTrackingService documentTrackingService;
	
	/* Delegate */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Validator */
	@Autowired
	@Qualifier("trackedDocumentFormValidator")
	private TrackedDocumentFormValidator trackedDocumentFormValidator;	
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;	
	
	/* Constructor. */
	/** Instantiates a default tracked document management controller. */
	public ManageTrackedDocumentController() {
		// Default instantiation
	}
	
	/**
	 * Displays screen to create a new tracked document.
	 * 
	 * @param offender offender
	 * @return model and view to create a new tracked document
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(value = "offender", 
		required = true) final Offender offender) {
		List<TrackedDocumentCategory> categories
			= this.documentTrackingService.findCategories();
		TrackedDocumentForm trackedDocumentForm = new TrackedDocumentForm();
		Boolean createFlag = true;
		return prepareEditMav(trackedDocumentForm, categories, offender,
			createFlag, 0);
	}	
	
	/**
	 * Save a new tracked document.
	 * 
	 * @param offender offender
	 * @param trackedDocumentForm tracked document form
	 * @param result binding result
	 * @return model and view of list screen
	 * @throws TrackedDocumentReceivalExistsException 
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "offender", 
		required = true) final Offender offender,
		final TrackedDocumentForm trackedDocumentForm,
		final BindingResult result)
		throws TrackedDocumentReceivalExistsException {
		this.trackedDocumentFormValidator.validate(trackedDocumentForm,
			result);
		if (result.hasErrors()) {
			List<TrackedDocumentCategory> categories
				= this.documentTrackingService.findCategories();
			int itemIndex = trackedDocumentForm
				.getTrackedDocumentReceivalItems().size();
			return prepareRedisplayEditMav(trackedDocumentForm, categories,
				offender, true, itemIndex, result);
		}
		
		for (TrackedDocumentReceivalItem trackedDocumentReceivalItem
			: trackedDocumentForm.getTrackedDocumentReceivalItems()) {
			if (TrackedDocumentReceivalItemOperation.CREATE.equals(
				trackedDocumentReceivalItem.getOperation())) {
				this.documentTrackingService.trackReceival(
					trackedDocumentForm.getDocket(), 
					trackedDocumentReceivalItem.getCategory(), 
					trackedDocumentReceivalItem.getReceivedDate(), 
					trackedDocumentReceivalItem.getReceivedByUserAccount());
			} 
		}
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Edit tracked documents.
	 * 
	 * @param docket docket
	 * @return model and view to edit tracked documents screen
	 * 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value = "docket", 
		required = true) final Docket docket) {
		TrackedDocumentForm trackedDocumentForm = new TrackedDocumentForm();
		List<TrackedDocumentReceival> trackedDocumentReceivals 
			= this.documentTrackingService.findReceivalsByDocket(docket);
		for (TrackedDocumentReceival receival : trackedDocumentReceivals) {
			TrackedDocumentReceivalItem item
				= new TrackedDocumentReceivalItem();
			item.setCategory(receival.getCategory());
			item.setOperation(TrackedDocumentReceivalItemOperation.EDIT);
			item.setReceivedByUserAccount(receival.getReceivedByUserAccount());
			item.setReceivedDate(receival.getReceivedDate());
			item.setTrackedDocumentReceival(receival);
			trackedDocumentForm.getTrackedDocumentReceivalItems().add(item);
		}
		List<TrackedDocumentCategory> categories
			= this.documentTrackingService.findCategories();
		int itemIndex = trackedDocumentForm.getTrackedDocumentReceivalItems()
			.size();
		trackedDocumentForm.setDocket(docket);
		return prepareEditMav(trackedDocumentForm, categories,
			(Offender) docket.getPerson(), false, itemIndex);
	}
	
	/**
	 * Save tracked documents edit screen.
	 * 
	 * @param docket docket
	 * @param trackedDocumentForm submitted form
	 * @param result binding result
	 * @return model and view for list screen
	 * @throws TrackedDocumentReceivalExistsException 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value = "docket", 
		required = true) final Docket docket,
		final TrackedDocumentForm trackedDocumentForm,
		final BindingResult result)
		throws TrackedDocumentReceivalExistsException {
		this.trackedDocumentFormValidator.validate(trackedDocumentForm, result);
		if (result.hasErrors()) {
			List<TrackedDocumentCategory> categories
				= this.documentTrackingService.findCategories();
			int itemIndex = trackedDocumentForm
				.getTrackedDocumentReceivalItems().size();
			return prepareRedisplayEditMav(trackedDocumentForm, categories,
				(Offender) docket.getPerson(), false, itemIndex, result);
		}
		
		for (TrackedDocumentReceivalItem trackedDocumentReceivalItem
			: trackedDocumentForm.getTrackedDocumentReceivalItems()) {
			if (TrackedDocumentReceivalItemOperation.CREATE.equals(
				trackedDocumentReceivalItem.getOperation())) {
				this.documentTrackingService.trackReceival(docket, 
					trackedDocumentReceivalItem.getCategory(), 
					trackedDocumentReceivalItem.getReceivedDate(), 
					trackedDocumentReceivalItem.getReceivedByUserAccount());
		    }
			if (TrackedDocumentReceivalItemOperation.REMOVE.equals(
				trackedDocumentReceivalItem.getOperation())) {
				this.documentTrackingService.remove(trackedDocumentReceivalItem
					.getTrackedDocumentReceival());
			} 
			
			if (!TrackedDocumentReceivalItemOperation.REMOVE.equals(
				trackedDocumentReceivalItem.getOperation())
				&& !TrackedDocumentReceivalItemOperation.CREATE.equals(
				trackedDocumentReceivalItem.getOperation())
				&& !TrackedDocumentReceivalItemOperation.EDIT.equals(
				trackedDocumentReceivalItem.getOperation())
				&& trackedDocumentReceivalItem.getOperation() != null) {
				throw new UnsupportedOperationException(
					String.format("Unsupported operation: %s",
					trackedDocumentReceivalItem.getOperation()));
			}
		}
		return new ModelAndView(String.format(LIST_REDIRECT, 
			((Offender) (docket.getPerson())).getId()));
	}
	
	/**
	 * Removes existing tracked document receival related to certain docket.
	 * 
	 * @param docket docket
	 * @return redirect to list screen
	 */
	@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "docket", required = true)
			final Docket docket) {
		Offender offender = (Offender) docket.getPerson();
		this.documentTrackingService.removeByDocket(docket);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}	
	
	/**
	 * Returns an action menu of the edit screen action menu. 
	 * 
	 * @param offender offender
	 * @return model and view for action menu of the edit screen action menu 
	 *
	 */
	@RequestMapping(value = "/trackedDocumentEditScreenActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView trackedDocumentEditScreenActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
			TRACKED_DOCUMENT_EDIT_SCREEN_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns an action menu for the table of edit screen.
	 * 
	 * @param offender offender
	 * @return An action menu for the table of edit screen
	 */
	@RequestMapping(value = "/trackedDocumentEditTableActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView trackedDocumentEditTableActionMenu(
		@RequestParam(value = "offender",	required = true) 
		final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(
			TRACKED_DOCUMENT_EDIT_TABLE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Adds a row for a tracked document item.
	 * 
	 * @param trackedDocumentItemIndex tracked document item index
	 * @param offender offender
	 * @return model and view for a row of tracked document
	 */
	@RequestMapping(value = "/addTrackedDocumentItem.html",
		method = RequestMethod.GET)
	public ModelAndView addTrackedDocumentItem(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "trackedDocumentItemIndex", required = true)
		final int trackedDocumentItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(TRACKED_DOCUMENT_ITEM_INDEX_MODEL_KEY,
			trackedDocumentItemIndex);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		
		List<TrackedDocumentCategory> categories
			= this.documentTrackingService.findCategories(); 
		map.addAttribute(TRACKED_DOCUMENT_CATEGORIES_MODEL_KEY, categories);
		
		TrackedDocumentReceivalItem trackedDocumentReceivalItem 
			= new TrackedDocumentReceivalItem();
		trackedDocumentReceivalItem.setOperation(
			TrackedDocumentReceivalItemOperation.CREATE);
		String username = SecurityContextHolder.getContext()
			.getAuthentication().getName();
		UserAccount userAccount
			= this.documentTrackingService.findUserAccount(username);
		
		trackedDocumentReceivalItem.setReceivedByUserAccount(userAccount);
		
		map.addAttribute(TRACKED_DOCUMENT_RECEIVAL_ITEM_MODEL_KEY, 
			trackedDocumentReceivalItem);
//		map.addAttribute(CREATE_FLAG_MODEL_KEY, true);
		return new ModelAndView(
			CREATE_TRACKED_DOCUMENTS_TABLE_ROW_VIEW_NAME, map);
	}
		
	private ModelAndView prepareEditMav(
		final TrackedDocumentForm trackedDocumentForm,
		final List<TrackedDocumentCategory> categories,
		final Offender offender, final Boolean createFlag,
		final int itemIndex) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(TRACKED_DOCUMENT_EDIT_FORM_MODEL_KEY,
			trackedDocumentForm);
		mav.addObject(TRACKED_DOCUMENT_CATEGORIES_MODEL_KEY, categories);
		mav.addObject(CREATE_FLAG_MODEL_KEY, createFlag);
		mav.addObject(ITEM_INDEX_MODEL_KEY, itemIndex);
		List<Docket> dockets = this.documentTrackingService
			.findDockets(offender);
		mav.addObject(DOCKETS_MODEL_KEY, dockets);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		return mav;
	}	
	
	private ModelAndView prepareRedisplayEditMav(
		final TrackedDocumentForm trackedDocumentForm,
		final List<TrackedDocumentCategory> categories,
		final Offender offender, final Boolean createFlag,
		final int itemIndex, final BindingResult result) {
		ModelAndView mav = this.prepareEditMav(trackedDocumentForm,
			categories, offender, createFlag, itemIndex);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ TRACKED_DOCUMENT_MODEL_KEY, result);
		return mav;
	}	
		
	/**
	 * Handles {@code TrackedDocumentReceivalExistsException}.
	 * 
	 * @param trackedDocumentReceivalExistsException exception thrown
	 * @return screen to handle {@code TrackedDocumentReceivalExistsException}
	 */
	@ExceptionHandler(TrackedDocumentReceivalExistsException.class)
	public ModelAndView handleTrackedDocumentReceivalExistsException(
		final TrackedDocumentReceivalExistsException
		trackedDocumentReceivalExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			TRACKED_DOCUMENT_RECEIVAL_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, trackedDocumentReceivalExistsException);
	}

	/**
	 * Returns the tracked document listing report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/trackedDocumentListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportTrackedDocumentListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(TRACKED_DOCUMENT_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getId()));
		byte[] doc = this.reportRunner.runReport(
				TRACKED_DOCUMENT_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the tracked document detail report for the specified docket.
	 * 
	 * @param docket docket
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/trackedDocumentDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRACKED_DOCUMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportTrackedDocumentDetails(@RequestParam(
			value = "docket", required = true)
			final Docket docket,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(TRACKED_DOCUMENT_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(docket.getId()));
		byte[] doc = this.reportRunner.runReport(
				TRACKED_DOCUMENT_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(UserAccount.class,
			this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Person.class,
			this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TrackedDocumentReceival.class,
			this.trackedDocumentReceivalPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(Docket.class,
			this.docketPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TrackedDocumentCategory.class,
			this.trackedDocumentCategoryPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(Date.class, this.customDateEditorFactory
			.createCustomDateOnlyEditor(true));
	}	
}