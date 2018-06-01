package omis.travelpermit.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.family.domain.FamilyAssociation;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermitNote;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.service.TravelPermitService;
import omis.travelpermit.web.form.TravelPermitForm;
import omis.travelpermit.web.form.TravelPermitNoteItem;
import omis.travelpermit.web.form.TravelPermitNoteItemOperation;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.StringUtility;
import omis.vehicle.domain.VehicleMake;
import omis.vehicle.domain.VehicleModel;
import omis.workassignment.domain.FenceRestriction;
import omis.workassignment.domain.WorkAssignment;
import omis.workassignment.domain.WorkAssignmentCategory;
import omis.workassignment.domain.WorkAssignmentChangeReason;
import omis.workassignment.domain.WorkAssignmentGroup;
import omis.workassignment.domain.WorkAssignmentNote;
import omis.workassignment.report.WorkAssignmentReportService;
import omis.workassignment.report.WorkAssignmentSummary;
import omis.workassignment.service.WorkAssignmentService;
import omis.workassignment.web.form.WorkAssignmentForm;
import omis.workassignment.web.form.WorkAssignmentNoteItem;
import omis.workassignment.web.form.WorkAssignmentNoteItemOperation;
import omis.workassignment.web.validator.WorkAssignmentFormValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/** Controller for travel permit.
 * @author Yidong Li
 * @version 0.1.1 (May 22, 2018)
 * @since OMIS 3.0 */
@Controller
@RequestMapping("/travelPermit")
@PreAuthorize("hasRole('USER')")
public class TravelPermitController {
	/* views */
	private static final String EDIT_VIEW_NAME = "travelPermit/edit";
	private static final String TRAVEL_PERMIT_PARTIAL_ADDRESS_CITY_VIEW_NAME
		= "travelPermit/includes/partialAddressCityOptions";
	private static final String TRAVEL_PERMIT_PARTIAL_ADDRESS_ZIP_CODE_VIEW_NAME
		= "travelPermit/includes/partialAddressZipCodeOptions";
	private static final String
	TRAVEL_PERMIT_NOTE_ITEMS_ACTION_MENU_VIEW_NAME
	= "travelPermit/includes/"
			+ "travelPermitNotesActionMenu";
	private static final String CREATE_TRAVEL_PERMIT_NOTE_TABLE_ROW_VIEW_NAME
	= "travelPermit/includes/noteTableRow";
	private static final String ADDRESSES_VIEW_NAME
	= "address/json/addresses";
	private static final String USER_ACCOUNTS_VIEW_NAME 
	= "user/json/userAccounts";
	private static final String TRAVEL_PERMIT_ACTION_MENU_VIEW_NAME
	= "travelPermit/includes/travelPermitActionMenu";
	private static final String TRAVEL_METHOD_VIEW_NAME
	= "travelPermit/includes/travelMethod";
	
	/* Redirects. */
	private static final String LIST_REDIRECT
		= "redirect:/travelPermit/list.html?offender=%d";
	
	/* Property editor. */
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("travelMethodPropertyEditorFactory")
	private PropertyEditorFactory travelMethodPropertyEditorFactory;
	
	@Autowired 
	private CustomDateEditorFactory customDateEditorFactory;
		
	/* model keys */
	private static final String PERIODICITIES_MODEL_KEY = "periodicities";
	private static final String TRAVEL_PERMIT_FORM_MODEL_KEY 
		= "travelPermitForm";
	private static final String USER_ACCOUNTS_MODEL_KEY = "userAccounts";
	private static final String DESTINATION_OPTIONS_MODEL_KEY
	= "destinationOptions";
	private static final String TRANSPORT_METHODS_MODEL_KEY
	= "transportMethods";
	private static final String ADDRESS_OPTIONS_MODEL_KEY
	="addressOptions";
	private static final String PARTIAL_ADDRESS_COUNTRIES_MODEL_KEY 
	= "partialAddressCountries";
	private static final String PARTIAL_ADDRESS_STATES_MODEL_KEY
	="partialAddressStates";
	private static final String PARTIAL_ADDRESS_CITIES_MODEL_KEY
	="partialAddressCities";
	private static final String PARTIAL_ADDRESS_ZIPCODEES_MODEL_KEY
	="partialAddressZipCodes";
	private static final String STATES_MODEL_KEY 
	= "states";
	private static final String TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY
	= "travelPermitNoteItemIndex";
	private static final String ORIGINAL_TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY
	="originalTravelPermitNoteIndex";
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = "addressFields";
	private static final String CREATE_TRAVEL_PERMIT_MODEL_KEY 
	= "createTravelPermit";
	private static final String TRAVEL_PERMIT_NOTE_ITEM_MODEL_KEY
	= "traelPermitNoteIndex";
	private static final String ADDRESSES_MODEL_KEY = "addresses";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String TRAVEL_METHOD_MODEL_KEY = "travelMethod";
	
	/* Services. */
	@Autowired
	@Qualifier("travelPermitService")
	private TravelPermitService travelPermitService; 
	
	/* Delegate */
	@Autowired
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;
	
	/* Validator */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Report names. */
	
	private static final String OFFENDER_WORK_ASSIGNMENT_HISTORY_REPORT_NAME 
		= "/Placement/WorkAssignments/Offender_Work_Assignment_History";
	
	private static final String WORK_ASSIGNMENTS_LISTING_REPORT_NAME 
		= "/Placement/WorkAssignments/Work_Assignments_Listing";	

	private static final String WORK_ASSIGNMENTS_DETAILS_REPORT_NAME 
		= "/Placement/WorkAssignments/Work_Assignments_Details";

	/* Report parameter names. */
	
	private static final String WORK_ASSIGNMENTS_LISTING_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	private static final String WORK_ASSIGNMENTS_DETAILS_ID_REPORT_PARAM_NAME 
		= "WORK_ASSIGNMENT_ID";
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Controller delegates. */
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Constructor. */
	
	/** Instantiates a default work assignment controller. */
	public TravelPermitController() {
		// Default instantiation
	}
	
	/**
	 * Displays screen to create new a travel permit.
	 * @param offender offender for whom to create vehicle association
	 * @return model and view to create a new travel permit 
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_ASSIGNMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
		@RequestParam(value = "offender", required = true)
			final Offender offender) {
				TravelPermitForm travelPermitForm 
					= new TravelPermitForm();
				boolean createTravelPermit = true; 
				List<TravelPermitNoteItem> travelPermitNoteItems 
					= new ArrayList<TravelPermitNoteItem>();
				int travelPermitNoteItemsIndex = travelPermitNoteItems.size();
				return this.prepareEditMav(travelPermitForm, offender, 
					createTravelPermit, travelPermitNoteItems, 
					travelPermitNoteItemsIndex, 0);
	}
	
	/**
	 * Saves a new created travel permit.
	 * 
	 * @param offender offender
	 * @param workAssignmentForm work assignment form
	 * @param result binding result
	 * @return redirect to list vehicle association by offender
	 * @throws DuplicateEntityFoundException 
	 */
	/*@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
		@RequestParam(value = "offender", required = true)
			final Offender offender,
			final TravelPermitForm travelPermitForm,
			final BindingResult result) throws DuplicateEntityFoundException {
			this.workAssignmentFormValidator.validate(travelPermitForm, result);
			if (result.hasErrors()) {
				return this.prepareRedisplayEditMav(
					offender, workAssignmentForm, result, true, 
					workAssignmentForm.getWorkAssignmentNoteItems(),0);
			} 
			
			WorkAssignment workAssignment = this.workAssignmentService.create(
				offender, workAssignmentForm.getFenceRestriction(), 
				workAssignmentForm.getWorkAssignmentCategory(), 
				workAssignmentForm.getWorkAssignmentChangeReason(), 
				workAssignmentForm.getAssignmentDate(), 
				workAssignmentForm.getTerminationDate(), 
				workAssignmentForm.getComments(),
				workAssignmentForm.getEndExistingWorkAssignment());
			
			for (WorkAssignmentNoteItem noteItem : 
				workAssignmentForm.getWorkAssignmentNoteItems()){
				if(noteItem.getOperation().equals(
					WorkAssignmentNoteItemOperation.CREATE)){
					this.workAssignmentService.addNote(workAssignment, 
						noteItem.getDate(), noteItem.getNote());
				}
			}
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}	*/
	
	/** Edit work assignment. 
	 * @param vehicleAssociation vehicle association.
	 * @return edited vehicle association view. */
	/*@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
		@RequestParam(value = "workAssignment", required = true)
		final WorkAssignment workAssignment) {
			WorkAssignmentForm workAssignmentForm 
				= new WorkAssignmentForm();
			workAssignmentForm.setAssignmentDate(
				workAssignment.getAssignedDate());
			workAssignmentForm.setFenceRestriction(
				workAssignment.getFenceRestriction());
			workAssignmentForm.setComments(workAssignment.getComments());
			workAssignmentForm.setTerminationDate(
				workAssignment.getTerminationDate());
			workAssignmentForm.setWorkAssignmentCategory(
				workAssignment.getCategory());
			workAssignmentForm.setWorkAssignmentChangeReason(
				workAssignment.getChangeReason());
			List<WorkAssignmentNote> workAssignmentNotes 
				= this.workAssignmentService.findNotes(workAssignment);
			List<WorkAssignmentNoteItem> workAssignmentNoteItems 
			= new ArrayList<WorkAssignmentNoteItem>();
			for(WorkAssignmentNote note : workAssignmentNotes)
			{
				WorkAssignmentNoteItem item = new WorkAssignmentNoteItem();
				item.setDate(note.getDate());
				item.setNote(note.getValue());
				item.setOperation(WorkAssignmentNoteItemOperation.UPDATE);
				workAssignmentNoteItems.add(item);
			}
			workAssignmentForm.setWorkAssignmentNoteItems(workAssignmentNoteItems);
			int workAssignmentNoteIndex = workAssignmentNoteItems.size();
			int originalWorkAssignmentNoteIndex = this.workAssignmentService
					.findNotes(workAssignment).size();
					
			ModelAndView mav = prepareEditMav(workAssignmentForm, 
				workAssignment.getOffender(), false, workAssignmentNoteItems,
				workAssignmentNoteIndex, originalWorkAssignmentNoteIndex); 
			mav.addObject(WORK_ASSIGNMENT_MODEL_KEY, workAssignment);
			return mav; 
	}*/
	
	/**
	 * Updates an existing work assignment.
	 * 
	 * @param workAssignment work assignment
	 * @param workAssignmentForm work assignment form
	 * @param result binding result
	 * @return redirect to list vehicle association
	 * @throws DuplicateEntityFoundException
	 */
	/*@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value = "workAssignment", required = true)
			final WorkAssignment workAssignment,
			final WorkAssignmentForm workAssignmentForm,
			final BindingResult result) throws DuplicateEntityFoundException {	
		this.workAssignmentFormValidator.validate(
			workAssignmentForm, result);
		if (result.hasErrors()) {
			ModelAndView mav = this.prepareRedisplayEditMav(
				workAssignment.getOffender(), workAssignmentForm, result, false,
				workAssignmentForm.getWorkAssignmentNoteItems(),
				this.workAssignmentService.findNotes(workAssignment).size());
				return mav;
		}

		this.workAssignmentService.update(workAssignment, 
			workAssignmentForm.getFenceRestriction(), 
			workAssignmentForm.getWorkAssignmentCategory(), 
			workAssignmentForm.getWorkAssignmentChangeReason(), 
			workAssignmentForm.getAssignmentDate(), 
			workAssignmentForm.getTerminationDate(), 
			workAssignmentForm.getComments(),
			workAssignmentForm.getEndExistingWorkAssignment());
		
		 Update/add work assignment notes 
		List<WorkAssignmentNote> originalWorkAssignmentNotes 
			= this.workAssignmentService.findNotes(workAssignment);
		int originalWorkAssignmentNoteIndex 
			= originalWorkAssignmentNotes.size();	
		List<WorkAssignmentNoteItem> workAssignmentNoteItems 
			= workAssignmentForm.getWorkAssignmentNoteItems();
		int newWorkAssignmentNoteIndex = workAssignmentNoteItems.size()-1;
		
		for (int index = 0; index < originalWorkAssignmentNoteIndex; index++) {
			if(WorkAssignmentNoteItemOperation.UPDATE.equals(
					workAssignmentNoteItems.get(index).getOperation())){
					this.workAssignmentService.updateNote(
						originalWorkAssignmentNotes.get(index), 
						workAssignmentNoteItems.get(index).getDate(), 
						workAssignmentNoteItems.get(index).getNote());
			}
			if(WorkAssignmentNoteItemOperation.REMOVE.equals(
				workAssignmentNoteItems.get(index).getOperation())){
				this.workAssignmentService.removeNote(
					originalWorkAssignmentNotes.get(index));
			}
		}
		
		for (int index = originalWorkAssignmentNoteIndex; 
			index <= newWorkAssignmentNoteIndex; index++) {
			if(WorkAssignmentNoteItemOperation.CREATE.equals(
				workAssignmentNoteItems.get(index).getOperation())){
				this.workAssignmentService.addNote(workAssignment, 
				workAssignmentNoteItems.get(index).getDate(), 
				workAssignmentNoteItems.get(index).getNote());
			}
		}
		
		return new ModelAndView(String.format(LIST_REDIRECT,
			workAssignment.getOffender().getId()));
	}*/
	
	/**
	 * Removes an existing work assignment.
	 * 
	 * @param workAssignment work assignment to remove
	 * @return redirect to list religious preferences
	 */
	@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "itemIndex", required = true)
			final Integer itemIndex,
		@RequestParam(value = "offender", required = true)
			final Offender offender) {
		/*List<WorkAssignmentNote> workAssignmentNotes 
			= this.workAssignmentService.findNotes(workAssignment);
		Offender offender = workAssignment.getOffender();
		for(WorkAssignmentNote workAssignmentNote : workAssignmentNotes){
			this.workAssignmentService.removeNote(workAssignmentNote);
		}
		this.workAssignmentService.remove(workAssignment);*/
		
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Returns a view for an action menu on list screen
	 * 
	 * @param offender offender
	 * @return model and view for an action menu
	 */
	/*@RequestMapping(value = "/workAssignmentListActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView workAssignmentListActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(WORK_ASSIGNMENT_LIST_ACTION_MENU_VIEW_NAME, map);
	}*/
	
	/**
	 * Returns a view for an action menu on edit/create screen
	 * 
	 * @param offender offender
	 * @return model and view for an action menu
	 */
	/*@RequestMapping(value = "/workAssignmentEditActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView workAssignmentEditActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(WORK_ASSIGNMENT_EDIT_ACTION_MENU_VIEW_NAME, map);
	}*/
	
	/**
	 * Returns a view for an action menu
	 * 
	 * @param offender offender
	 * @return model and view for an action menu
	 */
	/*@RequestMapping(value = "/workAssignmentNoteActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView workAssignmentNoteActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(WORK_ASSIGNMENT_NOTE_ACTION_MENU_VIEW_NAME, map);
	}*/
	
	/**
	 * Adds a work assignment note
	 * 
	 * @param noteItemIndex work assignment note index
	 * @return model and view for a new work assignment note
	 */
	/*@RequestMapping(value = "/addWorkAssignmentNoteItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addFamilyAssociationNoteItem(@RequestParam(
			value = "noteItemIndex", required = true)
			final int noteItemIndex) {
		ModelMap map = new ModelMap();
		WorkAssignmentNoteItem workAssignmentNoteItem 
			= new WorkAssignmentNoteItem();
		workAssignmentNoteItem.setOperation(
			WorkAssignmentNoteItemOperation.CREATE); 
		map.addAttribute(WORK_ASSIGNMENT_NOTE_ITEM_MODEL_KEY, 
			workAssignmentNoteItem);
//		map.addAttribute(WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY, noteItemIndex);
		return new ModelAndView(WORK_ASSIGNMENT_NOTE_ITEM_VIEW_NAME, map);
	}*/
	
	private ModelAndView prepareEditMav(
		final TravelPermitForm travelPermitForm, 
		final Offender offender, final Boolean createTravelPermit,
		final List<TravelPermitNoteItem> travelPermitNoteItems,
		final int travelPermitNoteIndex,
		final int originalTravelPermitNoteIndex) {
			ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
			ModelMap map = mav.getModelMap();
			mav.addObject(TRAVEL_PERMIT_FORM_MODEL_KEY, 
				travelPermitForm);
			List<TravelPermitPeriodicity> periodicities 
			= this.travelPermitService.findPeriodicity();
			mav.addObject(PERIODICITIES_MODEL_KEY, periodicities);
			/*List<UserAccount> userAccounts
			= this.userAccountDelegate.findAll();
			mav.addObject(USER_ACCOUNTS_MODEL_KEY, userAccounts);*/
			List<String> destinationOptions = new ArrayList<String>();
			destinationOptions.add("Use Full Address");
			destinationOptions.add("Use Partial Address");
			mav.addObject(DESTINATION_OPTIONS_MODEL_KEY, destinationOptions);
			List<String> addressOptions = new ArrayList<String>();
			addressOptions.add("Use Existing");
			addressOptions.add("Create New");
			mav.addObject(ADDRESS_OPTIONS_MODEL_KEY, addressOptions);
			
			
			
			
			
			/*List<String> transportMethods = new ArrayList<String>();
			transportMethods.add("Private Vehicle");
			transportMethods.add("Airplane");
			transportMethods.add("Bus");
			transportMethods.add("Train");
			mav.addObject(TRANSPORT_METHODS_MODEL_KEY, transportMethods);*/
			List<TravelMethod> transportMethods
			= this.travelPermitService.findTravelMethods();
			mav.addObject(TRANSPORT_METHODS_MODEL_KEY, transportMethods);
			
			
			
			
			mav.addObject(CREATE_TRAVEL_PERMIT_MODEL_KEY, createTravelPermit);
			List<Country> countries = this.countryDelegate.findAll();
			mav.addObject(PARTIAL_ADDRESS_COUNTRIES_MODEL_KEY, countries);
			mav.addObject(TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY,
				travelPermitNoteIndex);
			mav.addObject(ORIGINAL_TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY,
				originalTravelPermitNoteIndex);
			List<State> states 
				= this.stateDelegate.findByCountry(null);
			List<City> cities 
				= this.cityDelegate.findByState(null);
			List<ZipCode> zipCodes 
				= this.zipCodeDelegate.findByCity(null);
				this.addressFieldsControllerDelegate.prepareEditAddressFields(
					map, countries, states, cities, zipCodes, 
					ADDRESS_FIELDS_PROPERTY_NAME);
				
			Country contryUS = this.countryDelegate.findOrCreate(
				"United States", "US", true);
			List<State> partialAddressStates 
				= this.stateDelegate.findByCountry(contryUS);
			mav.addObject(PARTIAL_ADDRESS_STATES_MODEL_KEY, partialAddressStates);

			/*if(createTravelPermit){
				Country defaultCountry = this.countryDelegate.findOrCreate(
				"United States", "US", true);
				List<State> states = new ArrayList<State>();
				states = this.stateDelegate.findByCountry(defaultCountry);
				mav.addObject(STATES_MODEL_KEY,	states);
			}*/
			
			
			
			
			
			
			
			/*List<WorkAssignmentCategory> workAssignmentCategories 
				= this.workAssignmentService.findCategories();
			mav.addObject(WORK_ASSIGNMENT_CATEGORIES_MODEL_KEY, 
				workAssignmentCategories);
			List<WorkAssignmentChangeReason> workAssignmentChangeReasons 
				= this.workAssignmentService.findChangeReasons();
			mav.addObject(WORK_ASSIGNMENT_CHANGE_REASONS_MODEL_KEY, 
				workAssignmentChangeReasons);
			List<FenceRestriction> fenceRestrictions 
				= this.workAssignmentService.findFenceRestrictions();
			mav.addObject(FENCE_RESTRICTIONS_MODEL_KEY, 
				fenceRestrictions);
			mav.addObject(CREATE_WORK_ASSIGNMENT_MODEL_KEY, createWorkAssignment);
			mav.addObject(WORK_ASSIGNMENT_NOTE_ITEMS_MODEL_KEY, 
				workAssignmentNoteItems);
			mav.addObject(WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY, workAssignmentNoteIndex); 
			mav.addObject(ORIGINAL_WORK_ASSIGNMENT_NOTE_INDEX_MODEL_KEY, 
				originalWorkAssignmentNoteIndex); */
			this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
			return mav;
	}
	
	// Prepares redisplay edit/create screen
	/*private ModelAndView prepareRedisplayEditMav(
		final Offender offender,
		final WorkAssignmentForm workAssignmentForm,
		final BindingResult result,
		final boolean createNew,
		final List<WorkAssignmentNoteItem> workAssignmentNoteItems,
		final int originalWorkAssignmentNoteItems) {
		int workAssignmentNoteIndex = workAssignmentNoteItems.size();
		ModelAndView mav = this.prepareEditMav(workAssignmentForm, offender,
			createNew, workAssignmentNoteItems, workAssignmentNoteIndex,
			originalWorkAssignmentNoteItems);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ WORK_ASSIGNMENT_MODEL_KEY, result);
		return mav;
	}	*/
	
	/**
	 * Returns a view for work assignment action menu pertaining
	 * 
	 * @param offender offender
	 * @return view for employment action menu
	 */
	/*@RequestMapping(value = "/workAssignmentRowActionMenu.html",method =RequestMethod.GET)
	public ModelAndView employmentActionMenu(@RequestParam(value = "offender",
		required = true) final Offender offender,
		@RequestParam(value = "workAssignment",
		required = true) final WorkAssignment workAssignment) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(WORK_ASSIGNMENT_MODEL_KEY, workAssignment);
		return new ModelAndView(WORK_ASSIGNMENT_ROW_ACTION_MENU_VIEW_NAME, map);
	}*/
	

	/* Reports. */

	/**
	 * Returns the report for the specified offenders work assignments for offender distribution.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/offenderWorkAssignmentHistoryReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportOffenderWorkAssignmentsHistory(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WORK_ASSIGNMENTS_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				OFFENDER_WORK_ASSIGNMENT_HISTORY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified offenders work assignments.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/workAssignmentsListingReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWorkAssignmentsListing(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WORK_ASSIGNMENTS_LISTING_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				WORK_ASSIGNMENTS_LISTING_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	
	/**
	 * Returns the report for the specified work assignment.
	 * 
	 * @param workAssignment work assignment
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/workAssignmentsDetailsReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportWorkAssignmentsDetails(@RequestParam(
			value = "workAssignment", required = true)
			final WorkAssignment workAssignment,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(WORK_ASSIGNMENTS_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(workAssignment.getId()));
		byte[] doc = this.reportRunner.runReport(
				WORK_ASSIGNMENTS_DETAILS_REPORT_NAME,
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
		binder.registerCustomEditor(Date.class, 
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, 
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class,
				this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
				this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class,
				this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TravelMethod.class,
				this.travelMethodPropertyEditorFactory.createPropertyEditor());
	}
	
	/**
	 * List city options by state 
	 * 
	 * @param city city
	 * @return redirect to list cities corresponding to state
	 */
	@RequestMapping("listCitiesByState.html")
	public ModelAndView listCitiesByState(
		@RequestParam(value = "state", required = false)
		final State state){
		ModelMap map = new ModelMap();
		
		if(state!=null){
			List<City> cities = this.cityDelegate.findByState(state);
			map.addAttribute(PARTIAL_ADDRESS_CITIES_MODEL_KEY, cities); 
		} 
			
		return new ModelAndView(TRAVEL_PERMIT_PARTIAL_ADDRESS_CITY_VIEW_NAME,
				map); 
	}
	
	/**
	 * List zip codes by city
	 * 
	 * @param city city
	 * @return redirect to list zip codes corresponding to city
	 */
	@RequestMapping("listZipCodesByCity.html")
	public ModelAndView listZipCodesByCity(
		@RequestParam(value = "city", required = false)
		final City city){
		ModelMap map = new ModelMap();
		
		if(city!=null){
			List<ZipCode> zipCodes = this.zipCodeDelegate.findByCity(city);
			map.addAttribute(PARTIAL_ADDRESS_ZIPCODEES_MODEL_KEY, zipCodes); 
		} 
			
		return new ModelAndView(TRAVEL_PERMIT_PARTIAL_ADDRESS_ZIP_CODE_VIEW_NAME,
				map); 
	}
	
	/**
	 * List zip codes by city 
	 * 
	 * @param city city
	 * @return redirect to list cities corresponding to state
	 */
	/*@RequestMapping("listZipCodesByCity.html")
	public ModelAndView listZipCodesByCity(
		@RequestParam(value = "city", required = false)
		final City city){
		ModelMap map = new ModelMap();
		
		if(city!=null){
			List<ZipCode> zipCodes = this.zipCodeDelegate.findByCity(city);
			map.addAttribute(PARTIAL_ADDRESS_ZIPCODEES_MODEL_KEY, zipCodes); 
		} 
			
		return new ModelAndView(TRAVEL_PERMIT_MODELS_VIEW_NAME, map); 
	}*/
	
	
	
	
	
	
	
	
	/**
	 * Returns the state options view with a collections of state for the
	 * specified country for person, address and po box fields snippet.
	 * 
	 * @param country country
	 * @param fieldsName fields name
	 * @return model and view to show state options
	 */
	@RequestMapping(value = "/findStateOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showStateOptions(
			@RequestParam(value = "country", required = true)
			final Country country,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName) {
		List<State> states 
			= this.stateDelegate.findByCountry(country);
		return this.addressFieldsControllerDelegate.showStateOptions(states, 
				addressFieldsPropertyName);
	}

	/**
	 * Returns the city options view with a collection of cities for the
	 * specified state for address fields snippet.
	 * 
	 * @param state state
	 * @param addressFieldsPropertyName address fields property name
	 * @param country country
	 * @return model and view to show city options
	 */
	@RequestMapping(value = "findCityOptions.html", method = RequestMethod.GET)
	public ModelAndView showAddressFieldsCityOptions(
		@RequestParam(value = "state", required = false)
			final State state,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName,
				@RequestParam(value = "country", required = false)
		final Country country) {
		if (state != null) {
			return this.addressFieldsControllerDelegate.showCityOptions(
				this.travelPermitService.findCitiesByState(state),
				addressFieldsPropertyName);
		} else {
			if (this.travelPermitService.hasStates(country) != null) {
				return this.addressFieldsControllerDelegate.showCityOptions(
					this.travelPermitService.findCitiesByCountry(country), 
					addressFieldsPropertyName);
			} else {
				return this.addressFieldsControllerDelegate.showCityOptions(
					this.travelPermitService.findCitiesByCountry(country), 
					addressFieldsPropertyName);
			}
		}
	}

	/**
	 * Returns the zip code options view with a collection of zip codes for the
	 * specified city for address field snippet.
	 * 
	 * @param city city
	 * @param addressFieldsPropertyName address fields property name
	 * @return model and view to show zip code options
	 */
	@RequestMapping(value = "findZipCodeOptions.html", method = RequestMethod.GET)
	public ModelAndView showAddressFieldsZipCodeOptions(
		@RequestParam(value = "city", required = true)
			final City city,
		@RequestParam(value = "addressFieldsPropertyName", required = true)
			final String addressFieldsPropertyName) {
		return this.addressFieldsControllerDelegate.showZipCodeOptions(
			this.travelPermitService.findZipCodes(city), 
			addressFieldsPropertyName);
	}
	
	/**
	 * Returns a view for travel permit notes action menu pertaining to the
	 * specified offender.
	 * 
	 * @return model and view for travel permit note action menu
	 */
	@RequestMapping(value = "/noteActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView travelPermitNotesActionMenu() {
		return new ModelAndView(
				TRAVEL_PERMIT_NOTE_ITEMS_ACTION_MENU_VIEW_NAME);
	}	
	
	/**
	 * Adds a family association note.
	 * 
	 * @param noteItemIndex family association note index
	 * @return model and view for a new family association
	 */
	@RequestMapping(value = "/addTravelPermitNoteItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addTravelPermitNoteItem(@RequestParam(
			value = "noteItemIndex", required = true)
			final int noteItemIndex) {
		TravelPermitNoteItem travelPermitNoteItem 
			= new TravelPermitNoteItem();
		travelPermitNoteItem.setOperation(TravelPermitNoteItemOperation.CREATE); 
		ModelAndView mav = new ModelAndView(
			CREATE_TRAVEL_PERMIT_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY, 
			noteItemIndex);
		mav.addObject(TRAVEL_PERMIT_NOTE_ITEM_MODEL_KEY, 
			travelPermitNoteItem);
		return mav;
	}
	
	/** returns addresses given name search criteria.
	 * @param searchCriteria search criteria
	 * @return view of...address, ..
	 * @throws IOException  */
	@RequestMapping(value = "/findOffenderRelationshipAddress.json", 
		method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView searchAddressByCriteria(
		  @RequestParam(value = "term", required = false) 
		 	final String searchCriteria) throws IOException {
		List<Address> addresses;
		if (StringUtility.hasContent(searchCriteria)) {
			addresses = this.travelPermitService.findAddresses(searchCriteria);
		} else {
			addresses = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(ADDRESSES_VIEW_NAME);
		mav.addObject(ADDRESSES_MODEL_KEY, addresses);
		return mav;
	}
	
	/**
	 * Searches user accounts.
	 * 
	 * @param query query
	 * @return user accounts as JSON
	 */
	@RequestMapping(value = "/searchUserAccounts.json",
			method = RequestMethod.GET)
	public ModelAndView searchUserAccounts(
			@RequestParam(value = "term", required = true)
				final String query) {
		List<UserAccount> userAccounts
			= this.userAccountDelegate.search(query.toUpperCase());
		ModelAndView mav = new ModelAndView(USER_ACCOUNTS_VIEW_NAME);
		mav.addObject(USER_ACCOUNTS_MODEL_KEY, userAccounts);
		return mav;
	}
	
	/**
	 * Returns a view for family association action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for family associations action menu
	 */
	/*@RequestMapping(value = "/travelPermitEditActionMenu.html",
		method = RequestMethod.GET)
	public ModelAndView travelPermitEditActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		TravelMethod travelMethod = 
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(TRAVEL_PERMIT_ACTION_MENU_VIEW_NAME, map);
	}*/
	
	/**
	 * Returns a view for travel permit transport method.
	 * 
	 * @param travelMethod travel method
	 * @return 
	 * @return model and view for travel method
	 */
	@RequestMapping(value = "/transportMethod.html",
			method = RequestMethod.GET)
	public ModelAndView transportMethod(@RequestParam(
		value = "travelMethod",	required = true)
		final TravelMethod travelMethod) {
		ModelMap map = new ModelMap();
		map.addAttribute(TRAVEL_METHOD_MODEL_KEY, travelMethod);
		return new ModelAndView(TRAVEL_METHOD_VIEW_NAME, map);
//			return new ModelAndView(TRAVEL_PERMIT_ACTION_MENU_VIEW_NAME, map);
//		return travelMethod;
			
	}
	
	/**
	 * Removes an existing travel permit note.
	 * 
	 * @param familyAssociation family association
	 * @param offender offender
	 * @return redirect to list religious preferences
	 */
	/*@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "familyAssociation", required = true)
			final FamilyAssociation familyAssociation,
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		List<RelationshipNote> familyAssociationNotes 
			= this.familyAssociationService.findNotesByRelationship(
				familyAssociation.getRelationship());
		for (RelationshipNote note : familyAssociationNotes) {
			this.familyAssociationService.removeRelationshipNote(note);
		}
		this.familyAssociationService.remove(familyAssociation);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}*/
}
