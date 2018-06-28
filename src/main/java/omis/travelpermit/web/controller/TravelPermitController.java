/*
 * OMIS - Offender Management Information System.
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
package omis.travelpermit.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.PersonName;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;
import omis.travelpermit.exception.TravelPermitExistsException;
import omis.travelpermit.exception.TravelPermitNoteExistsException;
import omis.travelpermit.service.TravelPermitService;
import omis.travelpermit.web.form.TravelPermitForm;
import omis.travelpermit.web.form.TravelPermitNoteItem;
import omis.travelpermit.web.form.TravelPermitNoteItemOperation;
import omis.travelpermit.web.validator.TravelPermitFormValidator;
import omis.user.domain.UserAccount;
import omis.util.StringUtility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

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
	private static final String TRAVEL_METHOD_VIEW_NAME
	= "travelPermit/includes/travelMethod";
	private static final String TRAVEL_PERMIT_ACTION_MENU_VIEW_NAME
	= "travelPermit/includes/travelPermitActionMenu";
	
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
	@Qualifier("travelPermitPeriodicityPropertyEditorFactory")
	private PropertyEditorFactory travelPermitPeriodicityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;
	
	@Autowired
	@Qualifier("travelPermitPropertyEditorFactory")
	private PropertyEditorFactory travelPermitPropertyEditorFactory;
	
	@Autowired
	@Qualifier("travelPermitNotePropertyEditorFactory")
	private PropertyEditorFactory travelPermitNotePropertyEditorFactory;
	
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
	="addressOption";
	private static final String PARTIAL_ADDRESS_COUNTRIES_MODEL_KEY 
	= "partialAddressCountries";
	private static final String PARTIAL_ADDRESS_STATES_MODEL_KEY
	="partialAddressStates";
	private static final String PARTIAL_ADDRESS_CITIES_MODEL_KEY
	="partialAddressCities";
	private static final String PARTIAL_ADDRESS_ZIPCODES_MODEL_KEY
	="partialAddressZipCodes";
	private static final String TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY
	= "travelPermitNoteItemIndex";
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = "addressFields";
	private static final String CREATE_TRAVEL_PERMIT_MODEL_KEY 
	= "createTravelPermit";
	private static final String TRAVEL_PERMIT_NOTE_ITEM_MODEL_KEY
	= "travelPermitNoteItem";
	private static final String ADDRESSES_MODEL_KEY = "addresses";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String TRAVEL_METHOD_MODEL_KEY = "travelMethod";
	private static final String TRAVEL_PERMIT_FIELDS_MODEL_KEY 
	= "travelPermit";
	private static final String TRAVEL_PERMIT_NOTE_ITEMS_MODEL_KEY
	="travelPermitNoteItems";
	private static final String TRAVEL_PERMIT_EXISTS_EXCEPTION_MESSAGE_KEY
	= "travelpermit.Exists";
	private static final String TRAVEL_PERMIT_NOTE_EXISTS_EXCEPTION_MESSAGE_KEY
	= "travelpermitnote.Exists";
	
	/* Services. */
	@Autowired
	@Qualifier("travelPermitService")
	private TravelPermitService travelPermitService; 
	
	/* Delegate */
	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/* Validator */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("travelPermitFormValidator")
	private TravelPermitFormValidator travelPermitFormValidator;
	
	/* Message bundles. */
	private static final String ERROR_BUNDLE_NAME
	= "omis.travelpermit.msgs.form";
	
	/* Report names. */

	/* Report parameter names. */
	
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
	 * @param offender offender for whom to create travel permit
	 * @return model and view to create a new travel permit 
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(
		@RequestParam(value = "offender", required = true)
			final Offender offender, @RequestParam(value = "travelPermit", required = false)
			final TravelPermit travelPermit) {
			TravelPermitForm travelPermitForm 
				= new TravelPermitForm();
			if(travelPermit != null) {
				this.populateTravelPermitForm(travelPermitForm, travelPermit, true);
			}
			boolean createTravelPermit = true; 
			List<TravelPermitNoteItem> travelPermitNoteItems 
				= new ArrayList<TravelPermitNoteItem>();
			int travelPermitNoteItemsIndex = travelPermitNoteItems.size();
			return this.prepareEditMav(travelPermitForm, offender, 
				createTravelPermit, travelPermitNoteItems, 
				travelPermitNoteItemsIndex);
	}
	
	/**
	 * Saves a new created travel permit.
	 * 
	 * @param offender offender
	 * @param travelPermitForm travel permit form
	 * @param result binding result
	 * @return redirect to list travel permit by offender
	 * @throws DuplicateEntityFoundException 
	 * @throws TravelPermitExistsException
	 * @throws TravelPermitNoteExistsException
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		final TravelPermitForm travelPermitForm,
		final BindingResult result) throws TravelPermitExistsException,
		TravelPermitNoteExistsException, DuplicateEntityFoundException {
		this.travelPermitFormValidator.validate(travelPermitForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayEditMav(travelPermitForm, 
			offender, true,
			travelPermitForm.getTravelPermitNoteItems(),
			travelPermitForm.getTravelPermitNoteItems().size(),
			result);		
		}

		Address address = null;
		State state = null;
		City city = null;
		ZipCode zipCode = null;
		TravelDestination destination = new TravelDestination();
		if(DestinationOption.USE_FULL_ADDRESS.equals(
			travelPermitForm.getDestinationOption())){
			if(AddressOption.USE_EXISTING.equals(
				travelPermitForm.getAddressOption())){
				address = travelPermitForm.getAddress();
				zipCode = address.getZipCode();
			}
			if(AddressOption.CREATE_NEW.equals(
				travelPermitForm.getAddressOption())){
				if(travelPermitForm.getAddressFields().getNewCity()){    // New city, new zip code
					city = this.travelPermitService.createCity(
						travelPermitForm.getAddressFields().getState(),
						travelPermitForm.getAddressFields().getValue(),
						travelPermitForm.getAddressFields().getCountry());
					zipCode = this.travelPermitService.createZipCode(city,
						travelPermitForm.getAddressFields().getZipCodeValue(),
						travelPermitForm.getAddressFields()
						.getZipCodeExtension());
					address = this.travelPermitService.createAddress(
						travelPermitForm.getAddressFields().getValue(),
						zipCode);
				}
				if(!travelPermitForm.getAddressFields().getNewCity()      // Existing city, new zip code
					&&travelPermitForm.getAddressFields().getNewZipCode()){
					zipCode = this.travelPermitService.createZipCode(
						travelPermitForm.getAddressFields().getCity(),
						travelPermitForm.getAddressFields().getZipCodeValue(),
						travelPermitForm.getAddressFields()
						.getZipCodeExtension());
					address = this.travelPermitService.createAddress(
						travelPermitForm.getAddressFields().getValue(),
						zipCode);
				}
				if(!travelPermitForm.getAddressFields().getNewCity()
					&&!travelPermitForm.getAddressFields().getNewZipCode()){      // Existing city and zip code
					address = this.travelPermitService.createAddress(
						travelPermitForm.getAddressFields().getValue(),
						travelPermitForm.getAddressFields().getZipCode());
					zipCode = travelPermitForm.getAddressFields().getZipCode();
				}
				state = travelPermitForm.getAddressFields().getState();
			}
			destination.setAddress(address);
		}
		if(DestinationOption.USE_PARTIAL_ADDRESS.equals(
			travelPermitForm.getDestinationOption())){
			if(travelPermitForm.getNewCity()){
				Country country = this.travelPermitService.findHomeCountry();
				city = this.travelPermitService.createCity(
					travelPermitForm.getPartialAddressState(),
					travelPermitForm.getNewCityName(), country);
				zipCode = this.travelPermitService.createZipCode(city,
					travelPermitForm.getNewZipCodeName(),
					travelPermitForm.getNewZipCodeExtension());
			}
			if(!travelPermitForm.getNewCity()){
				city = travelPermitForm.getPartialAddressCity();
				if(travelPermitForm.getNewZipCode()){
					zipCode = this.travelPermitService.createZipCode(city,
					travelPermitForm.getNewZipCodeName(),
					travelPermitForm.getNewZipCodeExtension());
				}
				 else {
					zipCode = travelPermitForm.getPartialAddressZipCode();
				}
			}
			state = travelPermitForm.getPartialAddressState();
			destination.setCity(city);
			destination.setState(state);
			destination.setZipCode(zipCode);
		}
		destination.setName(travelPermitForm.getName());
		if(travelPermitForm.getPhoneNumber().length()!=0){
			String updatedTelephoneNumber
				= travelPermitForm.getPhoneNumber().replace("(", "")
				.replace(")", "").replace("-", "");
			destination.setTelephoneNumber(Long.valueOf(updatedTelephoneNumber));
		}
		DateRange dateRange = new DateRange();
		dateRange.setStartDate(travelPermitForm.getStartDate());
		dateRange.setEndDate(travelPermitForm.getEndDate());
		TravelPermitIssuance issuance = new TravelPermitIssuance(
			travelPermitForm.getIssueDate(), travelPermitForm.getIssuer());
		
		TravelTransportation transportation = new TravelTransportation();
		if(travelPermitForm.getTravelMethod().getDescriptionRequired()){
			transportation.setDescription(travelPermitForm.getVehicleInfo());
		}
		transportation.setMethod(travelPermitForm.getTravelMethod());
		transportation.setNumber(travelPermitForm.getPlateNumber());
		
		OtherTravelers otherTravelers = new OtherTravelers();
		otherTravelers.setPersons(travelPermitForm.getPersons());
		otherTravelers.setRelationships(travelPermitForm.getRelationships());
		
		TravelPermit travelPermit = this.travelPermitService.create(offender,
			travelPermitForm.getTripPurpose(), dateRange,
			travelPermitForm.getPeriodicity(), issuance, transportation,
			destination, otherTravelers);
		
		if(travelPermitForm.getTravelPermitNoteItems()!=null){
			for(TravelPermitNoteItem item : travelPermitForm
				.getTravelPermitNoteItems()){
				if(item.getOperation()!=null){
					if(TravelPermitNoteItemOperation.CREATE.equals(
						item.getOperation())){
						this.travelPermitService.createNote(travelPermit,
							item.getDate(),	item.getNote());
					} else {
						throw new UnsupportedOperationException(
							String.format("Unsupported operation: %s",
							item.getOperation()));
					}
				}
			}
		}
		
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}	
	
	/** Edit an existing travel permit. 
	 * @param travelPermit travel permit.
	 * @return edited travel permit view. */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
		@RequestParam(value = "travelPermit", required = true)
		final TravelPermit travelPermit) {
			TravelPermitForm travelPermitForm = new TravelPermitForm();
			this.populateTravelPermitForm(travelPermitForm, travelPermit, false);
			return prepareEditMav(travelPermitForm, travelPermit.getOffender(),
				false,	travelPermitForm.getTravelPermitNoteItems(),
				travelPermitForm.getTravelPermitNoteItems().size()); 
	}
	
	/**
	 * Updates/saves an existing travel permit.
	 * 
	 * @param travelPermit travel permit
	 * @param travelPermitForm travel permit form
	 * @param result binding result
	 * @return redirect to list travel permits
	 * @throws DuplicateEntityFoundException
	 * @throws TravelPermitExistsException 
	 * @throws TravelPermitNoteExistsException 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('TRAVEL_PERMIT_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value = "travelPermit", required = true)
			final TravelPermit travelPermit,
			final TravelPermitForm travelPermitForm,
			final BindingResult result) throws DuplicateEntityFoundException,
			TravelPermitExistsException, TravelPermitNoteExistsException {	
		this.travelPermitFormValidator.validate(travelPermitForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayEditMav(
				travelPermitForm, travelPermit.getOffender(), false, 
				travelPermitForm.getTravelPermitNoteItems(),
				travelPermitForm.getTravelPermitNoteItems().size(),
				result);
		} 
		
		Address address = null;
		State state = null;
		City city = null;
		ZipCode zipCode = null;
		TravelDestination destination = new TravelDestination();
		if(DestinationOption.USE_FULL_ADDRESS.equals(
			travelPermitForm.getDestinationOption())){
			if(AddressOption.USE_EXISTING.equals(
				travelPermitForm.getAddressOption())){
				address = travelPermitForm.getAddress();
				zipCode = address.getZipCode();
			}
			if(AddressOption.CREATE_NEW.equals(
				travelPermitForm.getAddressOption())){
				if(travelPermitForm.getAddressFields().getNewCity()){    // New city, new zip code
					city = this.travelPermitService.createCity(
						travelPermitForm.getAddressFields().getState(),
						travelPermitForm.getAddressFields().getValue(),
						travelPermitForm.getAddressFields().getCountry());
					zipCode = this.travelPermitService.createZipCode(city,
						travelPermitForm.getAddressFields().getZipCodeValue(),
						travelPermitForm.getAddressFields()
						.getZipCodeExtension());
					address = this.travelPermitService.createAddress(
						travelPermitForm.getAddressFields().getValue(),
						zipCode);
				}
				if(!travelPermitForm.getAddressFields().getNewCity()      // Existing city, new zip code
					&&travelPermitForm.getAddressFields().getNewZipCode()){
					zipCode = this.travelPermitService.createZipCode(
						travelPermitForm.getAddressFields().getCity(),
						travelPermitForm.getAddressFields().getZipCodeValue(),
						travelPermitForm.getAddressFields()
						.getZipCodeExtension());
					address = this.travelPermitService.createAddress(
						travelPermitForm.getAddressFields().getValue(),
						zipCode);
				}
				if(!travelPermitForm.getAddressFields().getNewCity()
					&&!travelPermitForm.getAddressFields().getNewZipCode()){      // Existing city and zip code
					address = this.travelPermitService.createAddress(
						travelPermitForm.getAddressFields().getValue(),
						travelPermitForm.getAddressFields().getZipCode());
					zipCode = travelPermitForm.getAddressFields().getZipCode();
				}
				state = travelPermitForm.getAddressFields().getState();
			}
			destination.setAddress(address);
		}
		if(DestinationOption.USE_PARTIAL_ADDRESS.equals(
			travelPermitForm.getDestinationOption())){
			if(travelPermitForm.getNewCity()){
				Country country = this.travelPermitService.findHomeCountry();
				city = this.travelPermitService.createCity(
					travelPermitForm.getPartialAddressState(),
					travelPermitForm.getNewCityName(), country);
				zipCode = this.travelPermitService.createZipCode(city,
					travelPermitForm.getNewZipCodeName(),
					travelPermitForm.getNewZipCodeExtension());
			}
			if(!travelPermitForm.getNewCity()){
				city = travelPermitForm.getPartialAddressCity();
				if(travelPermitForm.getNewZipCode()){
					zipCode = this.travelPermitService.createZipCode(city,
					travelPermitForm.getNewZipCodeName(),
					travelPermitForm.getNewZipCodeExtension());
				}
				 else {
					zipCode = travelPermitForm.getPartialAddressZipCode();
				}
			}
			state = travelPermitForm.getPartialAddressState();
			
			destination.setCity(city);
			destination.setState(state);
			destination.setZipCode(zipCode);
		}
		destination.setName(travelPermitForm.getName());
		if(travelPermitForm.getPhoneNumber().length()!=0){
			String updatedTelephoneNumber
			= travelPermitForm.getPhoneNumber().replace("(", "")
			.replace(")", "").replace("-", "");
			destination.setTelephoneNumber(Long.valueOf(updatedTelephoneNumber));
		}
				
		DateRange dateRange = new DateRange();
		dateRange.setStartDate(travelPermitForm.getStartDate());
		dateRange.setEndDate(travelPermitForm.getEndDate());
		TravelPermitIssuance issuance = new TravelPermitIssuance(
			travelPermitForm.getIssueDate(), travelPermitForm.getIssuer());
		
		TravelTransportation transportation = new TravelTransportation();
		if(travelPermitForm.getTravelMethod().getDescriptionRequired()){
			transportation.setDescription(travelPermitForm.getVehicleInfo());
		}
		transportation.setMethod(travelPermitForm.getTravelMethod());
		transportation.setNumber(travelPermitForm.getPlateNumber());
		
		OtherTravelers otherTravelers = new OtherTravelers();
		otherTravelers.setPersons(travelPermitForm.getPersons());
		otherTravelers.setRelationships(travelPermitForm.getRelationships());
		
		TravelPermit updatedTravelPermit = this.travelPermitService.update(
			travelPermit, travelPermitForm.getTripPurpose(), dateRange,
			travelPermit.getOffender(), travelPermitForm.getPeriodicity(),
			issuance, transportation, destination, otherTravelers);
		
		if(travelPermitForm.getTravelPermitNoteItems()!=null){
			for(TravelPermitNoteItem item : travelPermitForm.getTravelPermitNoteItems()){
				if(item.getOperation()!=null){
					if(TravelPermitNoteItemOperation.CREATE.equals(
						item.getOperation())){
						this.travelPermitService.createNote(updatedTravelPermit,
							item.getDate(),	item.getNote());
					} else if(TravelPermitNoteItemOperation.EDIT.equals(
							item.getOperation())){
						this.travelPermitService.updateNote(
							item.getTravelPermitNote(), item.getDate(),
							item.getNote());
					} else if(TravelPermitNoteItemOperation.REMOVE.equals(
							item.getOperation())){
						this.travelPermitService.removeNote(
							item.getTravelPermitNote());
					} else {
						throw new UnsupportedOperationException(
							String.format("Unsupported operation: %s",
							item.getOperation()));
					}
				}
			}
		}
		return new ModelAndView(String.format(LIST_REDIRECT,
			travelPermit.getOffender().getId()));
	}
	
	// Returns a model and view for editing the specified travel permit
	private ModelAndView prepareEditMav(
		final TravelPermitForm travelPermitForm, 
		final Offender offender, final Boolean createTravelPermit,
		final List<TravelPermitNoteItem> travelPermitNoteItems,
		final int travelPermitNoteIndex) {
			ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
			ModelMap map = mav.getModelMap();
			mav.addObject(TRAVEL_PERMIT_FORM_MODEL_KEY, 
				travelPermitForm);
			List<TravelPermitPeriodicity> periodicities 
			= this.travelPermitService.findPeriodicity();
			mav.addObject(PERIODICITIES_MODEL_KEY, periodicities);
			DestinationOption[] destinationOptions
				= DestinationOption.values(); 
			mav.addObject(DESTINATION_OPTIONS_MODEL_KEY, destinationOptions);
			AddressOption[] addressOptions
			= AddressOption.values(); 
			mav.addObject(ADDRESS_OPTIONS_MODEL_KEY, addressOptions);

			List<TravelMethod> transportMethods
			= this.travelPermitService.findTravelMethods();
			mav.addObject(TRANSPORT_METHODS_MODEL_KEY, transportMethods);
			
			mav.addObject(CREATE_TRAVEL_PERMIT_MODEL_KEY, createTravelPermit);
			List<Country> countries = this.travelPermitService.findCountries();
			mav.addObject(PARTIAL_ADDRESS_COUNTRIES_MODEL_KEY, countries);
			mav.addObject(TRAVEL_PERMIT_NOTE_INDEX_MODEL_KEY,
				travelPermitNoteIndex);
			List<State> states 
				= this.travelPermitService.findStates();
			List<City> cities 
				= this.travelPermitService.findCitiesByState(null);
			List<ZipCode> zipCodes 
				= this.travelPermitService.findZipCodes(null);
				this.addressFieldsControllerDelegate.prepareEditAddressFields(
					map, countries, states, cities, zipCodes, 
					ADDRESS_FIELDS_PROPERTY_NAME);
				
			Country country = this.travelPermitService.findHomeCountry();
			List<State> partialAddressStates 
				= this.travelPermitService.findStatesByCountry(country);
			mav.addObject(PARTIAL_ADDRESS_STATES_MODEL_KEY, partialAddressStates);
			mav.addObject(OFFENDER_MODEL_KEY, offender);
			mav.addObject(TRAVEL_PERMIT_NOTE_ITEMS_MODEL_KEY,
				travelPermitNoteItems);
			this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
			if(travelPermitForm.getStartDate()!=null){
				List<City> partialAddressCities 
				= this.travelPermitService.findCitiesByState(
					travelPermitForm.getPartialAddressState());
				mav.addObject(PARTIAL_ADDRESS_CITIES_MODEL_KEY,
					partialAddressCities);
				List<ZipCode> partialAddressZipCodes 
				= this.travelPermitService.findZipCodes(
					travelPermitForm.getPartialAddressCity());
				mav.addObject(PARTIAL_ADDRESS_ZIPCODES_MODEL_KEY,
					partialAddressZipCodes);
			}
			return mav;
	}
	
	// Prepares redisplay edit/create screen
	private ModelAndView prepareRedisplayEditMav(
		final TravelPermitForm travelPermitForm, 
		final Offender offender, final Boolean createTravelPermit,
		final List<TravelPermitNoteItem> travelPermitNoteItems,
		final int travelPermitNoteIndex,
		final  BindingResult result) {
		ModelAndView mav = this.prepareEditMav(travelPermitForm, offender, 
			createTravelPermit,	travelPermitNoteItems, travelPermitNoteIndex);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ TRAVEL_PERMIT_FIELDS_MODEL_KEY, result);
		return mav;
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
			List<City> cities = this.travelPermitService.findCitiesByState(
				state);
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
			List<ZipCode> zipCodes = this.travelPermitService.findZipCodes(
				city);
			map.addAttribute(PARTIAL_ADDRESS_ZIPCODES_MODEL_KEY, zipCodes); 
		} 
		return new ModelAndView(TRAVEL_PERMIT_PARTIAL_ADDRESS_ZIP_CODE_VIEW_NAME,
				map); 
	}
	
	/**
	 * Returns the state options view with a collections of state for the
	 * specified country for address fields snippet.
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
			= this.travelPermitService.findStatesByCountry(country);
		return this.addressFieldsControllerDelegate.showStateOptions(states, 
				addressFieldsPropertyName);
	}

	/**
	 * Returns the city options view with a collection of cities for the
	 * specified address fields snippet.
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
			if (this.travelPermitService.hasStates(country)) {
				return this.addressFieldsControllerDelegate.showCityOptions(
				this.travelPermitService.findCitiesByCountryWithoutState(country),
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
	 * Adds a travel permit note.
	 * 
	 * @param noteItemIndex travel permit note index
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
	@RequestMapping(value = "/findAddress.json", 
		method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView searchAddressByCriteria(
		  @RequestParam(value = "term", required = false) 
		 	final String searchCriteria) {
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
			= this.travelPermitService.searchUserAccounts(query.toUpperCase( ));
		ModelAndView mav = new ModelAndView(USER_ACCOUNTS_VIEW_NAME);
		mav.addObject(USER_ACCOUNTS_MODEL_KEY, userAccounts);
		return mav;
	}
	
	/**
	 * Returns a view for travel permit transport method.
	 * 
	 * @param travelMethod travel method 
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
	}
	
	/**
	 * Returns a view for travel permit create screen action menu pertaining to
	 * the specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for travel permit action menu
	 */
	@RequestMapping(value = "/travelPermitEditActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView travelPermitActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(TRAVEL_PERMIT_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Removes an existing travel permit.
	 * 
	 * @param travelPermit travel permit
	 * @return redirect to list
	 */
	@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('TRAVEL_PERMIT_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "travelPermit", required = true)
			final TravelPermit travelPermit) {
		List<TravelPermitNote> travelPermitNotes 
			= this.travelPermitService.findNotes(travelPermit);
		Offender offender = travelPermit.getOffender();
		for(TravelPermitNote travelPermitNote : travelPermitNotes){
			this.travelPermitService.removeNote(travelPermitNote);
		}
		this.travelPermitService.remove(travelPermit);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Handles {@code TravelPermitExistsException}.
	 * 
	 * @param TravelPermitExistsException exception thrown when a same travel
	 * permit already exists
	 * @return screen to handle {@code TravelPermitExistsException}
	 */
	@ExceptionHandler(TravelPermitExistsException.class)
	public ModelAndView handleTravelPermitExistsException(
		final TravelPermitExistsException travelPermitExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			TRAVEL_PERMIT_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, travelPermitExistsException);
	}
	
	
	/**
	 * Handles {@code TravelPermitNoteExistsException}.
	 * 
	 * @param TravelPermitNoteExistsException exception thrown a same travel
	 * permit note already exists
	 * @return screen to handle {@code TravelPermitNoteExistsException}
	 */
	@ExceptionHandler(TravelPermitNoteExistsException.class)
	public ModelAndView handleTravelPermitNoteExistsException(
		final TravelPermitNoteExistsException travelPermitNoteExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			TRAVEL_PERMIT_NOTE_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, travelPermitNoteExistsException);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates a travel permit form with values from the specified travel permit.
	 * 
	 * @param form travel permit form
	 * @param permit travel permit
	 * @param copy whether this form is for copying the specified travel permit
	 * @return populated travel permit form
	 */
	TravelPermitForm populateTravelPermitForm(final TravelPermitForm form, TravelPermit permit, final Boolean copy) {
		if(permit.getDestination().getAddress()!=null){   // Full address
			Address address = permit.getDestination().getAddress();
			form.setAddressOption(AddressOption.USE_EXISTING);
			form.setDestinationOption(
				DestinationOption.USE_FULL_ADDRESS);
			if(address.getValue()==null){
				form.setAddressQuery(String.format("%s %s %s",
				address.getZipCode().getCity().getName(),
				address.getZipCode().getCity().getState().getAbbreviation(),
				address.getZipCode().getValue()));
			} else {
				form.setAddressQuery(String.format("%s %s %s %s",
				address.getValue(),
				address.getZipCode().getCity().getName(),
				address.getZipCode().getCity().getState().getAbbreviation(),
				address.getZipCode().getValue()));
			}
			form.setAddress(address);
		} else {  // Partial address
			form.setDestinationOption(
				DestinationOption.USE_PARTIAL_ADDRESS);
			form.setPartialAddressState(
				permit.getDestination().getState());
			form.setPartialAddressCity(
				permit.getDestination().getCity());
			form.setPartialAddressZipCode(
				permit.getDestination().getZipCode());
		}
		if(!copy) {
			//If this form is not being populated for the purpose of a "copy", then
			//populate the dates, and notes associated with the specified permit. - JN
			form.setEndDate(
				permit.getDateRange().getEndDate());
			form.setStartDate(
				permit.getDateRange().getStartDate());
			form.setIssueDate(permit.getIssuance().getDate());
			form.setTravelMethod(permit.getTransportation()
				.getMethod());
			form.setPlateNumber(permit.getTransportation()
				.getNumber());
			form.setVehicleInfo(permit.getTransportation()
				.getDescription());
			List<TravelPermitNoteItem> noteItems
			= new ArrayList<TravelPermitNoteItem>();
			List<TravelPermitNote> notes = this.travelPermitService.findNotes(
				permit);
			for(TravelPermitNote note : notes){
				TravelPermitNoteItem item = new TravelPermitNoteItem();
				item.setDate(note.getDate());
				item.setNote(note.getValue());
				item.setOperation(TravelPermitNoteItemOperation.EDIT);
				item.setTravelPermitNote(note);
				item.setUpdateSignature(note.getUpdateSignature());
				noteItems.add(item);
			}
			form.setTravelPermitNoteItems(noteItems);
		}
		UserAccount issuer = permit.getIssuance().getIssuer();
		form.setIssuer(permit.getIssuance().getIssuer());
		PersonName name = issuer.getUser().getName();
		form.setIssuerInput(String.format("(%s) %s %s",
			issuer.getUsername(), name.getFirstName(),
			name.getLastName()));
		
		form.setName(permit.getDestination().getName());
		form.setPartialAddressCity(permit
			.getDestination().getCity());
		form.setPartialAddressState(permit
			.getDestination().getState());
		form.setPartialAddressZipCode(permit
			.getDestination().getZipCode());
		form.setPeriodicity(permit.getPeriodicity());
		if(permit.getOtherTravellers()!=null){
			form.setPersons(
			permit.getOtherTravellers().getPersons());
			form.setRelationships(
			permit.getOtherTravellers().getRelationships());
		}
		if(permit.getDestination().getTelephoneNumber()!=null)
			form.setPhoneNumber(permit.getDestination()
				.getTelephoneNumber().toString());
		form.setTripPurpose(permit.getPurpose());
		return form;
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
		binder.registerCustomEditor(TravelPermitPeriodicity.class,
			this.travelPermitPeriodicityPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(UserAccount.class,
			this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Address.class,
			this.addressPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TravelPermit.class,
			this.travelPermitPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TravelPermitNote.class,
			this.travelPermitNotePropertyEditorFactory.createPropertyEditor());
	}
}
