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
package omis.presentenceinvestigation.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.court.domain.Court;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequestNote;
import omis.presentenceinvestigation.service.PresentenceInvestigationRequestService;
import omis.presentenceinvestigation.web.form.PresentenceInvestigationDelayItem;
import omis.presentenceinvestigation.web.form.PresentenceInvestigationItemOperation;
import omis.presentenceinvestigation.web.form.PresentenceInvestigationRequestForm;
import omis.presentenceinvestigation.web.form.PresentenceInvestigationRequestNoteItem;
import omis.presentenceinvestigation.web.validator.PresentenceInvestigationRequestFormValidator;
import omis.user.domain.UserAccount;
import omis.util.DateManipulator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/** 
 * Controller for presentence investigation request related operations.
 * 
 * @author Ryan Johns
 * @author Joel Norris
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.6 (May 9, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/presentenceInvestigation/request")
@PreAuthorize("hasRole('USER')")
public class PresentenceInvestigationRequestController {
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = 
			"/presentenceInvestigation/request/edit";
	
	private static final String INVESTIGATION_REQUEST_ACTION_MENU_VIEW_NAME = 
					"/presentenceInvestigation/request/includes/"
					+ "presentenceInvestigationRequestActionMenu";

	private static final String 
			PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_ROW_VIEW_NAME =
					"/presentenceInvestigation/request/includes/"
					+ "presentenceInvestigationRequestNoteTableRow";
	
	private static final String
			PRESENTENCE_INVESTIGATION_DELAY_ITEM_ROW_VIEW_NAME =
			"/presentenceInvestigation/request/includes/"
			+ "presentenceInvestigationDelayTableRow";
	
	private static final String PERSON_SEARCH_FIELDS_VIEW_NAME =
			"/presentenceInvestigation/request/includes/searchFields";

	private static final String
		PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEMS_ACTION_MENU_VIEW_NAME =
				"/presentenceInvestigation/request/includes/"
				+ "presentenceInvestigationRequestNoteItemsActionMenu";
	
	private static final String
		PRESENTENCE_INVESTIGATION_DELAY_ITEMS_ACTION_MENU_VIEW_NAME =
				"/presentenceInvestigation/request/includes/"
				+ "presentenceInvestigationDelayItemsActionMenu";
	
	/* Redirect view names. */
	
	private static final String LIST_REDIRECT_BY_USER = 
			"redirect:/presentenceInvestigation/request/"
			+ "list.html?assignedUser=%d";
	
	private static final String LIST_REDIRECT_BY_OFFENDER = 
			"redirect:/presentenceInvestigation/request/"
			+ "list.html?offender=%d";
	
	private static final String HOME_REDIRECT = 
			"redirect:/presentenceInvestigation/"
			+ "home.html?presentenceInvestigationRequest=%d";

	/* Model keys. */
	
	private static final String FORM_MODEL_KEY 
					= "presentenceInvestigationRequestForm";
	
	private static final String ASSIGNED_USER_MODEL_KEY = "assignedUser";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String PRESENTENCE_INVESTIGATION_REQUEST_MODEL_KEY = 
			"presentenceInvestigationRequest";
	
	private static final String SUFFIXES_MODEL_KEY = "suffixes";
	
	private static final String COURTS_MODEL_KEY = "courts";
	
	private static final String CATEGORIES_MODEL_KEY = "categories";
	
	private static final String
			PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_INDEX_MODEL_KEY =
					"presentenceInvestigationRequestNoteItemIndex";

	private static final String
			PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_MODEL_KEY =
					"presentenceInvestigationRequestNoteItem";
	
	private static final String USER_ACCOUNT_MODEL_KEY =
			"AuditComponentRetrieverSpringMvcImpl#auditUserAccount";
	
	private static final String
			PRESENTENCE_INVESTIGATION_DELAY_ITEM_INDEX_MODEL_KEY =
					"presentenceInvestigationDelayItemIndex";

	private static final String
			PRESENTENCE_INVESTIGATION_DELAY_ITEM_MODEL_KEY =
					"presentenceInvestigationDelayItem";
	
	private static final String DELAY_CATEGORIES_MODEL_KEY = "delayCategories";
	
	/* Message Keys */
	
	private static final String 
			PRESENTENCE_INVESTIGATION_REQUEST_EXISTS_MESSAGE_KEY = 
					"presentenceInvestigationRequest.exists";
	
	private static final String DOCKET_EXISTS_MESSAGE_KEY =
			"request.docket.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.presentenceinvestigation.msgs.form";

	/* Services. */
	
	@Autowired
	private PresentenceInvestigationRequestService 
			presentenceInvestigationRequestService;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("presentenceInvestigationRequestPropertyEditorFactory")
	private PropertyEditorFactory 
			presentenceInvestigationRequestPropertyEditorFctry;
	
	@Autowired
	@Qualifier("presentenceInvestigationRequestNotePropertyEditorFactory")
	private PropertyEditorFactory 
			presentenceInvestigationRequestNotePropertyEditorFactory;

	@Autowired
	@Qualifier("presentenceInvestigationCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
			presentenceInvestigationCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("docketPropertyEditorFactory")
	private PropertyEditorFactory docketPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("suffixPropertyEditorFactory")
	private PropertyEditorFactory suffixPropertyEditorFactory;
	
	@Autowired
	@Qualifier("courtPropertyEditorFactory")
	private PropertyEditorFactory courtPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory 
			offenderPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("presentenceInvestigationDelayPropertyEditorFactory")
	private PropertyEditorFactory 
			presentenceInvestigationDelayPropertyEditorFactory;
	
	@Autowired
	@Qualifier("presentenceInvestigationDelayCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
			presentenceInvestigationDelayCategoryPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Validators. */
	
	@Autowired
	private PresentenceInvestigationRequestFormValidator
			presentenceInvestigationRequestFormValidator;
	
	/** Screen for creation of presentence investigation request.
	 * @param assignedUser - assigned user user account.
	 * @param offender - Person.
	 * @param onReturn - on return.
	 * @return model and view to create presentence investigation. */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PRESENTENCE_INVESTIGATION_REQUEST_CREATE') or "
					+ "hasRole('ADMIN')")
	public ModelAndView create(
					@RequestParam(value = "assignedUser", required = false)
					final UserAccount assignedUser, 
					@RequestParam(value = "offender", required = false) 
					final Person offender){
		
		PresentenceInvestigationRequestForm form 
						= new PresentenceInvestigationRequestForm();
		
		if (offender != null) {
			form.setPerson(offender);
		} else if (assignedUser != null) {
			form.setAssignedUserAccount(assignedUser);
			form.setCreatePerson(false);
		}
		
		return this.prepareEditMav(new ModelMap(), form, assignedUser, offender);
	}
	
	/** Saves a new presentence investigation request. 
	 * @param assignedUser - userAccount
	 * @param offender - Person
	 * @param onReturn - String, used to evaluate which list screen to return to
	 * @param form - presentence investigation
	 * request form.
	 * @param bindingResult - binding result
	 * @return model and view to redirect to list url. 
	 * @throws DuplicateEntityFoundException - when duplicate request exists. 
	 * @throws DocketExistsException */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PRESENTENCE_INVESTIGATION_REQUEST_CREATE') or "
					+ "hasRole('ADMIN')")
	public ModelAndView save(
					@RequestParam(value = "assignedUser", required = false) 
						final UserAccount assignedUser, 
					@RequestParam(value = "offender", required = false) 
						final Person offender, 
					@RequestParam(value = "onReturn", required = true) 
						final String onReturn, 
					final PresentenceInvestigationRequestForm
						form,
					final BindingResult bindingResult)
							throws DuplicateEntityFoundException,
							DocketExistsException {
		this.presentenceInvestigationRequestFormValidator.validate(
						form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(new ModelMap(), form,
					assignedUser, offender);
		} else {
			Person person = form.getPerson();
			if(form.getCreatePerson() != null && form.getCreatePerson() == true){
				person = this.presentenceInvestigationRequestService.createPerson(
						form.getLastName(), form.getFirstName(), 
						form.getMiddleName(), form.getSuffix());
			}
			
			Docket docket = this.presentenceInvestigationRequestService
					.createDocket(person, form.getCourt(),
							form.getDocketValue());
			DateManipulator dateManipulator = new DateManipulator(
					form.getRequestDate());
			dateManipulator.changeDate(30);
			PresentenceInvestigationRequest presentenceInvestigationRequest 
							= this.presentenceInvestigationRequestService
								.create(form.getAssignedUserAccount(),
								form.getRequestDate(),
								dateManipulator.getDate(), docket, 
								form.getSentenceDate(), 
								form.getCategory(), form.getSubmissionDate());
			this.processItems(form.getPresentenceInvestigationRequestNoteItems(),
					presentenceInvestigationRequest);
			this.processDelayItems(form.getPresentenceInvestigationDelayItems(), 
					presentenceInvestigationRequest);
			return new ModelAndView(String.format(
						HOME_REDIRECT, 
							presentenceInvestigationRequest.getId()));
		}
	}
	
	/** Screen to view/edit a presentence investigation request.
	* @param presentenceInvestigationRequest - 
	* presentence Investigation Request.
	* @param assignedUser - userAccount
	* @param offender - Person
	* @param onReturn - String, used to evaluate which list screen to return to
	* @return model and view to create presentence investigation. */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PRESENTENCE_INVESTIGATION_REQUEST_VIEW') or "
					+ "hasRole('ADMIN')")
	public ModelAndView edit(
					@RequestParam(value = "presentenceInvestigationRequest", 
						required = true)
					final PresentenceInvestigationRequest 
						presentenceInvestigationRequest, 
					@RequestParam(value = "assignedUser", required = false) 
					final UserAccount assignedUser, 
					@RequestParam(value = "offender", required = false)
					Person offender,
					@RequestParam(value = "onReturn", required = true) 
					final String onReturn) {
		
		PresentenceInvestigationRequestForm form
						= new PresentenceInvestigationRequestForm();
		form.setDocketValue(
				presentenceInvestigationRequest.getDocket().getValue());
		form.setCourt(
				presentenceInvestigationRequest.getDocket().getCourt());
		form.setRequestDate(
				presentenceInvestigationRequest.getRequestDate());
		form.setAssignedUserAccount(
				presentenceInvestigationRequest.getAssignedUser());
		form.setPerson(
				presentenceInvestigationRequest.getDocket().getPerson());
		form.setSentenceDate(
				presentenceInvestigationRequest.getSentenceDate());
		form.setCategory(presentenceInvestigationRequest.getCategory());
		form.setSubmissionDate(presentenceInvestigationRequest
				.getSubmissionDate());
		List<PresentenceInvestigationRequestNoteItem> noteItems =
				new ArrayList<PresentenceInvestigationRequestNoteItem>();
		for(PresentenceInvestigationRequestNote note :
			this.presentenceInvestigationRequestService
			.findPresentenceInvestigationRequestNotesByPresentenceInvestigationRequest(
					presentenceInvestigationRequest)){
			PresentenceInvestigationRequestNoteItem item =
					new PresentenceInvestigationRequestNoteItem();
			item.setDate(note.getDate());
			item.setDescription(note.getDescription());
			item.setPresentenceInvestigationRequestNote(note);
			item.setItemOperation(PresentenceInvestigationItemOperation.UPDATE);
			noteItems.add(item);
		}
		form.setPresentenceInvestigationRequestNoteItems(noteItems);
		List<PresentenceInvestigationDelayItem> delayItems = new ArrayList<>();
		for (PresentenceInvestigationDelay delay : this
				.presentenceInvestigationRequestService
				.findPresentenceInvestigationDelays(
						presentenceInvestigationRequest)) {
			PresentenceInvestigationDelayItem delayItem = 
					new PresentenceInvestigationDelayItem();
			delayItem.setDate(delay.getDate());
			delayItem.setReason(delay.getReason());
			delayItem.setPresentenceInvestigationDelay(delay);
			delayItem.setItemOperation(
					PresentenceInvestigationItemOperation.UPDATE);
			delayItems.add(delayItem);
		}
		form.setPresentenceInvestigationDelayItems(delayItems);
		return this.prepareEditMav(new ModelMap(),
				presentenceInvestigationRequest, form, assignedUser,
				presentenceInvestigationRequest.getDocket().getPerson());
	}
	 
	
	/** Updates the specified presentence investigation request. 
	* 
	* @param presentenceInvestigationRequest - presentence investigation request
	* @param assignedUser - userAccount
	* @param offender - Person
	* @param onReturn - String, used to evaluate which list screen to return to
	* @param form - presentence investigation
	* request form.
	* @param bindingResult - binding result
	* @return model and view to redirect to list url. 
	* @throws DuplicateEntityFoundException - request already exists. 
	 * @throws DocketExistsException */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PRESENTENCE_INVESTIGATION_REQUEST_EDIT') or "
					+ "hasRole('ADMIN')")
	public ModelAndView update(
					@RequestParam(value = "presentenceInvestigationRequest", 
						required = true) 
					final PresentenceInvestigationRequest 
					presentenceInvestigationRequest,
					@RequestParam(value = "assignedUser", required = false) 
					final UserAccount assignedUser,
					@RequestParam(value = "offender", required = false) 
					final Person offender,
					@RequestParam(value = "onReturn", required = true) 
					final String onReturn,
					final PresentenceInvestigationRequestForm form,
					final BindingResult bindingResult)
						throws DuplicateEntityFoundException, 
							DocketExistsException {
		this.presentenceInvestigationRequestFormValidator.validate(
						form, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return this.prepareEditMav(new ModelMap(),
					presentenceInvestigationRequest, form, assignedUser,
					offender);
		} else {
			Docket docket = this.presentenceInvestigationRequestService
					.updateDocket(presentenceInvestigationRequest.getDocket(),
							form.getCourt(),
							form.getDocketValue());
			DateManipulator dateManipulator = new DateManipulator(
					form.getRequestDate());
			dateManipulator.changeDate(30);
			this.presentenceInvestigationRequestService.update(
							presentenceInvestigationRequest,
							form.getAssignedUserAccount(),
							form.getRequestDate(),
							dateManipulator.getDate(), docket,
							form.getSentenceDate(), 
							presentenceInvestigationRequest.getCategory(),
							form.getSubmissionDate());
			this.processItems(form.getPresentenceInvestigationRequestNoteItems(),
					presentenceInvestigationRequest);
			this.processDelayItems(form.getPresentenceInvestigationDelayItems(), 
					presentenceInvestigationRequest);
			return new ModelAndView(String.format(
						HOME_REDIRECT, 
							presentenceInvestigationRequest.getId()));
		}
	}
	
	/**
	 * Removes specified presentence investigation request and redirects to the 
	 * presentence investigation request list screen.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @param assignedUser - userAccount
	 * @param offender - Person
	 * @param onReturn - String, used to evaluate which list screen to return to
	 * @return redirect to presentence investigation request list screen
	 */
	@RequestMapping(value = "remove.html",
					method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') "
					+ "or hasRole('PRESENTENCE_INVESTIGATION_REQUEST_REMOVE')")
	public ModelAndView remove(
					@RequestParam(value = "presentenceInvestigationRequest",
					required = true) final PresentenceInvestigationRequest 
					presentenceInvestigationRequest,
						@RequestParam(value = "assignedUser", required = false) 
						final UserAccount assignedUser,
						@RequestParam(value = "offender", required = false) 
						final Person offender,
						@RequestParam(value = "onReturn", required = true) 
						final String onReturn) {
		for(PresentenceInvestigationRequestNote note :
			this.presentenceInvestigationRequestService
			.findPresentenceInvestigationRequestNotesByPresentenceInvestigationRequest(
					presentenceInvestigationRequest)){
			this.presentenceInvestigationRequestService
				.removePresentenceInvestigationRequestNote(note);
		}
		//TODO remove all the things.
		this.presentenceInvestigationRequestService.remove(
						presentenceInvestigationRequest);
		
		if ("byUser".equals(onReturn)) {
			return new ModelAndView(String.format(
					LIST_REDIRECT_BY_USER, presentenceInvestigationRequest
						.getAssignedUser().getId()));
		} else if ("byOffender".equals(onReturn)) {
			return new ModelAndView(String.format(
						LIST_REDIRECT_BY_OFFENDER, 
						presentenceInvestigationRequest.getDocket().getPerson()
							.getId()));
		} else {
			return new ModelAndView(String.format(
						LIST_REDIRECT_BY_USER, presentenceInvestigationRequest
							.getAssignedUser().getId()));
		}
	}
	
	/**
	 * Returns the ModelAndView for a PresentenceInvestigationNoteItem
	 * @param presentenceInvestigationRequestNoteItemIndex - Integer
	 * @return ModelAndView for a PresentenceInvestigationNoteItem
	 */
	@RequestMapping(value = "createPresentenceInvestigationRequestNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayPresentenceInvestigationRequestNoteItem(@RequestParam(
			value = "presentenceInvestigationRequestNoteItemIndex", required = true)
				final Integer presentenceInvestigationRequestNoteItemIndex){
		ModelMap map = new ModelMap();
		
		PresentenceInvestigationRequestNoteItem item =
				new PresentenceInvestigationRequestNoteItem();
		
		item.setItemOperation(PresentenceInvestigationItemOperation.CREATE);
		map.addAttribute(PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_MODEL_KEY,
				item);
		map.addAttribute(
				PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_INDEX_MODEL_KEY,
				presentenceInvestigationRequestNoteItemIndex);
		
		return new ModelAndView(
				PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns the model and view for a presentence investigation delay item.
	 * 
	 * @param presentenceInvestigationDelayItemIndex presentence investigation 
	 * delay item index
	 * @return model and view for a presentence investigation delay item
	 */
	@RequestMapping(value = "createPresentenceInvestigationDelayItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayPresentenceInvestigationDelayItem(@RequestParam(
			value = "presentenceInvestigationDelayItemIndex", required = true)
				final Integer presentenceInvestigationDelayItemIndex){
		ModelMap map = new ModelMap();
		
		PresentenceInvestigationDelayItem item =
				new PresentenceInvestigationDelayItem();
		
		item.setItemOperation(PresentenceInvestigationItemOperation.CREATE);
		map.addAttribute(PRESENTENCE_INVESTIGATION_DELAY_ITEM_MODEL_KEY, item);
		map.addAttribute(
				PRESENTENCE_INVESTIGATION_DELAY_ITEM_INDEX_MODEL_KEY,
				presentenceInvestigationDelayItemIndex);
		map.addAttribute(DELAY_CATEGORIES_MODEL_KEY, 
				this.presentenceInvestigationRequestService
				.findPresentenceInvestigationDelayCategories());
		return new ModelAndView(
				PRESENTENCE_INVESTIGATION_DELAY_ITEM_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns the ModelAndView for Person Search Fields
	 * @return ModelAndView for Person Search Fields
	 */
	@RequestMapping(value = "displayPersonSearch.html",
			method = RequestMethod.GET)
	public ModelAndView displayPersonSearch(){
		return new ModelAndView(PERSON_SEARCH_FIELDS_VIEW_NAME);
	}
	
	/**
	 * Displays the presentence investigation request action menu.
	 * 
	 * @param assignedUser - userAccount
	 * @param offender - Person
	 * @param presentenceInvestigationRequest - presentence investigation
	 * request
	 * @return model and view for presentence investigation request action menu
	 */
	@RequestMapping(value = "/presentenceInvestigationRequestActionMenu.html",
					method = RequestMethod.GET)
	public ModelAndView displayInvestigationRequestActionMenu(
					@RequestParam(value = "offender", required = false) 
					final Person offender,
					@RequestParam(value = "presentenceInvestigationRequest", 
						required = false) 
					final PresentenceInvestigationRequest 
						presentenceInvestigationRequest) {
		ModelMap map = new ModelMap();
		map.addAttribute(ASSIGNED_USER_MODEL_KEY, this.retrieveUserAccount());
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(PRESENTENCE_INVESTIGATION_REQUEST_MODEL_KEY, 
						presentenceInvestigationRequest);
		return new ModelAndView(
				INVESTIGATION_REQUEST_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Returns the ModelAndView for the PresentenceInvestigationRequestNoteItems
	 * Action Menu
	 * @return ModelAndView for the PresentenceInvestigationRequestNoteItems
	 * Action Menu
	 */
	@RequestMapping(value="/presentenceInvestigationRequestNoteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayPresentenceInvestigationRequestNoteItemsActionMenu(){
		return new ModelAndView(
				PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEMS_ACTION_MENU_VIEW_NAME);
	}
	
	/**
	 * Returns the model and view for the presentence investigation delay items 
	 * action menu.
	 * 
	 * @return model and view for the presentence investigation delay items 
	 * action menu
	 */
	@RequestMapping(value="/presentenceInvestigationDelayItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayPresentenceInvestigationDelayItemsActionMenu(){
		return new ModelAndView(
				PRESENTENCE_INVESTIGATION_DELAY_ITEMS_ACTION_MENU_VIEW_NAME);
	}
	
	/* Helper methods. */
	
	/**
	 * Prepares a ModelAndView without a PresentenceInvestigationRequest
	 * @param modelMap - ModelMap
	 * @param form - PresentenceInvestigationRequestForm
	 * @param assignedUser - UserAccount
	 * @param offender - Person
	 * @return Prepared ModelAndView
	 */
	private ModelAndView prepareEditMav(final ModelMap modelMap,
			final PresentenceInvestigationRequestForm form,
			final UserAccount assignedUser, final Person offender){
		if (offender != null &&
				this.presentenceInvestigationRequestService.isOffender(offender)) {
			this.offenderSummaryModelDelegate.add(modelMap, (Offender) offender);
		}
		modelMap.addAttribute(CATEGORIES_MODEL_KEY,
				this.presentenceInvestigationRequestService
				.findAllPresentenceInvestigationCategories());
		modelMap.addAttribute(FORM_MODEL_KEY, form);
		modelMap.addAttribute(ASSIGNED_USER_MODEL_KEY, assignedUser);
		modelMap.addAttribute(OFFENDER_MODEL_KEY, offender);
		modelMap.addAttribute(COURTS_MODEL_KEY,
				this.presentenceInvestigationRequestService.findCourts());
		modelMap.addAttribute(SUFFIXES_MODEL_KEY,
				this.presentenceInvestigationRequestService.findSuffixes());
		modelMap.addAttribute(DELAY_CATEGORIES_MODEL_KEY, 
				this.presentenceInvestigationRequestService
				.findPresentenceInvestigationDelayCategories());
		modelMap.addAttribute(
				PRESENTENCE_INVESTIGATION_REQUEST_NOTE_ITEM_INDEX_MODEL_KEY,
				form.getPresentenceInvestigationRequestNoteItems().size());
		modelMap.addAttribute(
				PRESENTENCE_INVESTIGATION_DELAY_ITEM_INDEX_MODEL_KEY,
				form.getPresentenceInvestigationDelayItems().size());
		return new ModelAndView(EDIT_VIEW_NAME, modelMap);
	}
	
	/**
	 * Prepares a ModelAndView with a PresentenceInvestigationRequest
	 * @param modelMap - ModelMap
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @param form - PresentenceInvestigationRequestForm
	 * @param assignedUser - UserAccount
	 * @param offender - Person
	 * @return Prepared ModelAndView
	 */
	private ModelAndView prepareEditMav(final ModelMap modelMap,
			final PresentenceInvestigationRequest
			presentenceInvestigationRequest,
			final PresentenceInvestigationRequestForm form,
			final UserAccount assignedUser, final Person offender){
		modelMap.addAttribute(PRESENTENCE_INVESTIGATION_REQUEST_MODEL_KEY, 
							presentenceInvestigationRequest);
		return this.prepareEditMav(modelMap, form, assignedUser, offender);
	}
	
	/**
	 * Processes PresentenceInvestigationRequestNoteItems for creation, updating,
	 * and removal of PresentenceInvestigationRequestNotes
	 * @param items - PresentenceInvestigationRequestNoteItems
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @throws DuplicateEntityFoundException - When a
	 * PresentenceInvestigationRequestNote already exists with given date and
	 * description for specified PresentenceInvestigationRequest
	 */
	private void processItems(
			final List<PresentenceInvestigationRequestNoteItem> items,
			final PresentenceInvestigationRequest presentenceInvestigationRequest)
					throws DuplicateEntityFoundException{
		for(PresentenceInvestigationRequestNoteItem item : items){
			if(PresentenceInvestigationItemOperation.CREATE.equals(
					item.getItemOperation())){
				this.presentenceInvestigationRequestService
				.createPresentenceInvestigationRequestNote(
						presentenceInvestigationRequest,
						item.getDescription(), item.getDate());
			}
			else if(PresentenceInvestigationItemOperation.UPDATE.equals(
					item.getItemOperation())){
				if(this.isNoteChanged(
						item.getPresentenceInvestigationRequestNote(),
						item.getDate(), item.getDescription())) {
					this.presentenceInvestigationRequestService
					.updatePresentenceInvestigationRequestNote(
							item.getPresentenceInvestigationRequestNote(),
							item.getDescription(), item.getDate());
				}
			}
			else if(PresentenceInvestigationItemOperation.REMOVE.equals(
					item.getItemOperation())){
				this.presentenceInvestigationRequestService
				.removePresentenceInvestigationRequestNote(
						item.getPresentenceInvestigationRequestNote());
			}
		}
	}
	
	private void processDelayItems(
			final List<PresentenceInvestigationDelayItem> items,
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest) 
							throws DuplicateEntityFoundException{
		for(PresentenceInvestigationDelayItem item : items){
			if(PresentenceInvestigationItemOperation.CREATE.equals(
					item.getItemOperation())){
				this.presentenceInvestigationRequestService
				.createPresentenceInvestigationDelay(
						presentenceInvestigationRequest, item.getDate(), 
						item.getReason());
			}
			else if(PresentenceInvestigationItemOperation.UPDATE.equals(
					item.getItemOperation())){
				this.presentenceInvestigationRequestService
				.updatePresentenceInvestigationDelay(
						item.getPresentenceInvestigationDelay(), item.getDate(), 
						item.getReason());
			}
			else if(PresentenceInvestigationItemOperation.REMOVE.equals(
					item.getItemOperation())){
				this.presentenceInvestigationRequestService
				.removePresentenceInvestigationDelay(
						item.getPresentenceInvestigationDelay());
			}
		}
	}
	
	/**
	 * Checks if a Presentence Investigation Request Note has been changed and
	 * returns true if it has
	 * @param note - Presentence Investigation Request Note to check for change
	 * @param date - Note Date from Form
	 * @param value - Note Value from Form
	 * @return Boolean - true if either the form's note date or value is different
	 * from the original Note's, false otherwise.
	 */
	private boolean isNoteChanged(final PresentenceInvestigationRequestNote note,
			final Date date, final String value) {
		if(!note.getDescription().equals(value)) {
			return true;
		}
		if(!(note.getDate().getTime() == date.getTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves the current users account.
	 *
	 *
	 * @return current user
	 */
	private UserAccount retrieveUserAccount() {
		UserAccount userAccount = (UserAccount) RequestContextHolder
				.getRequestAttributes()
					.getAttribute(USER_ACCOUNT_MODEL_KEY,
						RequestAttributes.SCOPE_REQUEST);
		if (userAccount == null) {
			String username = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			userAccount = 
					this.presentenceInvestigationRequestService
					.findUserAccountByUsername(username);
			RequestContextHolder.getRequestAttributes()
				.setAttribute(USER_ACCOUNT_MODEL_KEY, userAccount,
						RequestAttributes.SCOPE_REQUEST);
		}
		return userAccount;
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles duplicate entity found exceptions.
	 * 
	 * @param exception duplicate entity found exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleDuplicateEntityFoundException(
					final DuplicateEntityFoundException exception) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(
					PRESENTENCE_INVESTIGATION_REQUEST_EXISTS_MESSAGE_KEY, 
					ERROR_BUNDLE_NAME, exception);
	}
	
	/**
	 * Handles docket exists exceptions.
	 * 
	 * @param exception docket exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DocketExistsException.class)
	public ModelAndView handleDocketExistsException(
			final DocketExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				DOCKET_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/** Init binder.
	 * @param binder - web data binder. */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				UserAccount.class, this.userAccountPropertyEditorFactory
							.createPropertyEditor());
		binder.registerCustomEditor(
				Docket.class, this.docketPropertyEditorFactory
							.createPropertyEditor());
		binder.registerCustomEditor(
				Suffix.class, this.suffixPropertyEditorFactory
							.createPropertyEditor());
		binder.registerCustomEditor(
				Court.class, this.courtPropertyEditorFactory
							.createPropertyEditor());
		binder.registerCustomEditor(
				Person.class, this.personPropertyEditorFactory
							.createPropertyEditor());
		binder.registerCustomEditor(Offender.class, 
							this.offenderPropertyEditorFactory
								.createOffenderPropertyEditor());
		binder.registerCustomEditor(
						Date.class, this.customDateEditorFactory 
							.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(PresentenceInvestigationRequest.class, 
						this.presentenceInvestigationRequestPropertyEditorFctry
							.createPropertyEditor());
		binder.registerCustomEditor(PresentenceInvestigationRequestNote.class, 
				this.presentenceInvestigationRequestNotePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(PresentenceInvestigationCategory.class, 
				this.presentenceInvestigationCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(PresentenceInvestigationDelay.class, 
				this.presentenceInvestigationDelayPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(PresentenceInvestigationDelayCategory.class, 
				this.presentenceInvestigationDelayCategoryPropertyEditorFactory
					.createPropertyEditor());
	}
} 