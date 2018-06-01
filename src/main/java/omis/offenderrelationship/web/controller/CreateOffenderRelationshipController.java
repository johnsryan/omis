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
package omis.offenderrelationship.web.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.BuildingCategory;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.address.web.form.AddressFields;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.ContactExistsException;
import omis.contact.exception.OnlineAccountExistsException;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.contact.report.ContactReportService;
import omis.contact.report.ContactSummary;
import omis.contact.web.controller.delegate.OnlineAccountFieldsControllerDelegate;
import omis.contact.web.controller.delegate.PoBoxFieldsControllerDelegate;
import omis.contact.web.controller.delegate.TelephoneNumberFieldsControllerDelegate;
import omis.contact.web.form.OnlineAccountContactItemOperation;
import omis.contact.web.form.PoBoxFields;
import omis.contact.web.form.TelephoneNumberItemOperation;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offenderrelationship.service.CreateRelationshipsService;
import omis.offenderrelationship.web.form.CreateRelationshipsForm;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.offenderrelationship.web.form.OnlineAccountContactItem;
import omis.offenderrelationship.web.form.TelephoneNumberItem;
import omis.offenderrelationship.web.validator.CreateRelationshipsFormValidator;
import omis.offenderrelationship.web.validator.CreateRelationshipsFromSearchFormValidator;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.exception.PersonExistsException;
import omis.person.web.delegate.PersonFieldsControllerDelegate;
import omis.person.web.form.PersonFields;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.util.StringUtility;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.exception.VisitationExistsException;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Offender relationship controller.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @author Yidong Li
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.1.2 (Feb 12, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/offenderRelationship/")
@PreAuthorize("hasRole('USER')")
public class CreateOffenderRelationshipController {
	/* Redirect URLs */
	private static final String LIST_REDIRECT 
		= "redirect:/offenderRelationship/list.html?offender=%d";
	
	/* URLs. */
	
	// Base URL of all operations - including links in controller 
	// Operations that do not use this URL should be updated to do so - SA
	private static final String BASE_URL = "/offenderRelationship/create";

	/* View names. */
	private static final String CREATE_VIEW_NAME 
		= "offenderRelationship/create";
	private static final String OFFENDER_RELATION_EDIT_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/create/"
				+ "offenderRelationCreateActionMenu";
	private static final String OFFENDER_RELATION_LIST_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/offenderRelationListActionMenu";
	private static final String TELEPHONE_NUMBERS_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/create/"
				+ "offenderRelationshipCreateTelephoneNumbersActionMenu";
	private static final String CREATE_TELEPHONE_NUMBER_TABLE_ROW_VIEW_NAME 
		= "offenderRelationship/includes/create/createTelephoneNumberTableRow";
	private static final String CREATE_ONLINE_ACCOUNT_TABLE_ROW_VIEW_NAME 
		= "offenderRelationship/includes/create/createOnlineAccountTableRow";
	private static final String CREATE_RELATIONSHIP_NOTE_TABLE_ROW_VIEW_NAME
		= "offenderRelationship/includes/offenderRelationshipNoteItemTableRow";
	private static final String EMAILS_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/create/"
				+ "offenderRelationshipCreateEmailActionMenu";
	private static final String
	OFFENDER_RELATION_NOTE_ITEMS_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/create/"
				+ "offenderRelationshipNoteItemsActionMenu";
	private static final String ADDRESSES_VIEW_NAME
		= "address/json/addresses";

	/* Model Keys. */
	private static final String 
		OFFENDER_RELATIONSHIP_TELEPHONE_NUMBER_INDEX_MODEL_KEY 
		= "offenderRelationshipTelephoneNumberIndex";
	private static final String 
		OFFENDER_RELATIONSHIP_ONLINE_ACCOUNT_INDEX_MODEL_KEY 
		= "offenderRelationshipOnlineAccountIndex";
	private static final String OFFENDER_RELATIONSHIP_NOTE_INDEX_MODEL_KEY
		= "offenderRelationshipNoteItemIndex";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String CREATE_RELATIONSHIPS_FORM_MODEL_KEY
		= "createRelationshipsForm";
	private static final String NAME_SUFFIXES_MODEL_KEY = "nameSuffixes";
	private static final String HOME_TYPES_MODEL_KEY = "homeTypes";
	private static final String STREET_SUFFIXES_MODEL_KEY = "streetSuffixes";
	private static final String UNIT_DESIGNATORS_MODEL_KEY = "unitDesignators";
	private static final String COUNTRIES_MODEL_KEY = "countries";
	private static final String RELATION_MODEL_KEY = "relation";
	private static final String TELEPHONE_NUMBER_ITEM_MODEL_KEY
		= "telephoneNumberItem";
	private static final String ONLINE_ACCOUNT_HOSTS_MODEL_KEY 
		= "onlineAccountHosts";
	private static final String ONLINE_ACCOUNT_CONTACT_ITEM_MODEL_KEY
		= "onlineAccountContactItem";
	private static final String TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY 
		= "telephoneNumberIndex";
	private static final String ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY 
		= "onlineAccountIndex";
	private static final String TELEPHONE_NUMBER_CATEGORY_MODEL_KEY
		= "telephoneNumberCategories";
	private static final String ADDRESSES_MODEL_KEY
		= "addresses";
	private static final String FAMILY_ASSOCIATION_CATEGORIES_MODEL_KEY
		= "familyAssociationCategories";
	private static final String VISITATIION_ASSOCIATION_CATEGORIES_MODEL_KEY
		= "visitationAssociationCategories";
	private static final String CREATE_FAMILY_MEMBER_STATUS 
		= "createFamilyMemberStatus";
	private static final String CREATE_VICTIM_STATUS 
		= "createVictimStatus";
	private static final String CREATE_VISITOR_STATUS 
		= "createVisitorStatus";
	private static final String BIRTH_COUNTRIES_MODEL_KEY = "birthCountries";
	private static final String PO_BOX_COUNTRIES_MODEL_KEY = "poBoxCountries";
	private static final String NEW_RELATION_MODEL_KEY = "newRelation";
	private static final String NOTE_CATEGORIES_MODEL_KEY
		= "offenderRelationshipNoteCategories";
	private static final String OFFENDER_RELATIONSHIP_NOTE_ITEM_MODEL_KEY
		= "offenderRelationshipNoteItem";
	private static final String SPAUSE_STATUS 
	= "spauseStatus";
	
	// Used to add base URL to views
	private static final String BASE_URL_MODEL_KEY = "baseUrl";
	
	// Field name of note items
	private static final String
	OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME_MODEL_KEY
		= "offenderRelationshipNoteItemsFieldName";
	
	private static final String EXISTING_CONTACT_SUMMARY_MODEL_KEY
	= "existingContactSummary";
	
	/* Message keys. */
	private static final String ADDRESS_EXISTS_EXCEPTION_MESSAGE_KEY
		= "address.Conflicts";
	private static final String VICTIM_EXISTS_EXCEPTION_MESSAGE_KEY
		= "victim.Conflicts";
	private static final String VISITATION_EXISTS_EXCEPTION_MESSAGE_KEY
		= "visitation.Conflicts";
	private static final String ZIPCODE_EXISTS_EXCEPTION_MESSAGE_KEY
		= "zipCode.exists";
	private static final String CONTACT_EXISTS_EXCEPTION_MESSAGE_KEY
		= "contact.Conflicts";
	private static final String PERSON_EXISTS_EXCEPTION_MESSAGE_KEY
		= "person.Conflicts";
	private static final String TELEPHONE_NUMBER_EXISTS_EXCEPTION_MESSAGE_KEY
		= "telephoneNumber.Conflicts";
	private static final String ONLINE_ACCOUNT_EXISTS_EXCEPTION_MESSAGE_KEY
		= "onlineAccount.Conflicts";
	private static final String
		FAMILY_ASSOCIATION_CONFLICT_EXCEPTION_MESSAGE_KEY
		= "familyAssociation.Conflicts";
	private static final String CITY_EXISTS_EXCEPTION_MESSAGE_KEY
		= "city.Conflicts";
	private static final String NOTE_EXISTS_EXCEPTION_MESSAGE_KEY
		= "note.Conflicts";
	
	/* Property names. */
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = "addressFields";
	private static final String PO_BOX_FIELDS_PROPERTY_NAME = "poBoxFields";
	private static final String PERSON_FIELDS_PROPERTY_NAME = "personFields";
	
	/* Fields names. */
	private static final String PERSON_FIELDS_NAME = "personFields";
	private static final String ADDRESS_FIELDS_NAME
		= "addressFields";
	private static final String PO_BOX_FIELDS_NAME = "poBoxFields";

	private static final String OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME
		= "noteItems";
	
	/* Message bundles. */
	private static final String ERROR_BUNDLE_NAME
		= "omis.offenderrelationship.msgs.form";
	
	/* Property Editors. */
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;

	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;

	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;

	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;

	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;

	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;

	@Autowired
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;

	@Autowired
	@Qualifier("addressUnitDesignatorPropertyEditorFactory")
	private PropertyEditorFactory addressUnitDesignatorPropertyEditorFactory;

	@Autowired
	@Qualifier("streetSuffixPropertyEditorFactory")
	private PropertyEditorFactory streetSuffixPropertyEditorFactory;

	@Autowired
	@Qualifier("familyAssociationCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
		familyAssociationCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("visitationAssociationCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
		visitationAssociationCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("onlineAccountHostPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountHostPropertyEditorFactory;
	
	@Autowired
	@Qualifier("telephoneNumberPropertyEditorFactory")
	private PropertyEditorFactory telephoneNumberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("onlineAccountPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("relationshipNoteCategoryPropertyEditorFactory")
	private PropertyEditorFactory relationshipNoteCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("relationshipNotePropertyEditorFactory")
	private PropertyEditorFactory relationshipNotePropertyEditorFactory;
	
	/* Services. */
	@Autowired
	@Qualifier("createRelationshipsService")
	private CreateRelationshipsService createRelationshipsService;

	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	@Autowired
	@Qualifier("contactReportService")
	private ContactReportService contactReportService;

	/* Validators. */
	@Autowired
	@Qualifier("createRelationshipsFormValidator")
	private CreateRelationshipsFormValidator createRelationshipsFormValidator;
	
	@Autowired
	@Qualifier("createRelationshipsFromSearchFormValidator")
	private CreateRelationshipsFromSearchFormValidator 
		createRelationshipsFromSearchFormValidator;

	/* Controller Delegates. */
	@Autowired
	@Qualifier("personFieldsControllerDelegate")
	private PersonFieldsControllerDelegate personFieldsControllerDelegate;

	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;

	@Autowired
	@Qualifier("poBoxFieldsControllerDelegate")
	private PoBoxFieldsControllerDelegate poBoxFieldsControllerDelegate;

	@Autowired
	@Qualifier("telephoneNumberFieldsControllerDelegate")
	private TelephoneNumberFieldsControllerDelegate 
		telephoneNumberFieldsControllerDelegate;

	@Autowired
	@Qualifier("onlineAccountFieldsControllerDelegate")
	private OnlineAccountFieldsControllerDelegate 
		onlineAccountFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	/*@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;*/

	/* Helpers. */
	@Autowired
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;

	/* Constructor. */
	/**
	 * Instantiates a default instance of offender relationship controller.
	 */
	public CreateOffenderRelationshipController() {
		// Default constructor.
	}

	/* URL mapped methods. */
	/**
	 * Displays the create relationships form.
	 * 
	 * @param offender offender
	 * @param relation relation
	 * @return model and view to display create relationships form
	 */
	@RequestMapping(value = "create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFENDER_RELATIONSHIP_CREATE')")
	public ModelAndView create(
		@RequestParam(value = "offender", required = true) 
		final Offender offender,
		@RequestParam(value = "relation", required = false) 
		final Person relation) {
		CreateRelationshipsForm createRelationForm 
			= new CreateRelationshipsForm();
		createRelationForm.setAddressOperation(
			OffenderRelationshipAddressOperation.EXISTING);
		createRelationForm.setCreateFamilyMember(false);
		createRelationForm.setCreateVictim(false);
		createRelationForm.setCreateVisitor(false);
		
		ModelAndView test = this.prepareCreateMav(createRelationForm, offender, 
			relation);
		return test;
	}
	
	/**
	 * Save the offender relationships form.
	 * 
	 * @param offender offender
	 * @param associatedPerson associated person
	 * @param createRelationshipsForm createRelationshipsForm
	 * @throws DuplicateEntityFoundException DuplicateEntityFoundException
	 * @throws ReflexiveRelationshipException ReflexiveRelationshipException
	 * @throws DateConflictException DateConflictException
	 * @throws FamilyAssociationConflictException Family Association Conflict 
	 * @throws FamilyAssociationExistsException family association exists
	 * exception
	 * @throws PersonExistsException person exists exception
	 * @throws AddressExistsException address exists exception
	 * @throws ZipCodeExistsException zip code exists exception
	 * @throws CityExistsException city exists exception
	 * @throws ContactExistsException contact exists exception
	 * @throws TelephoneNumberExistsException telephone number exists exception
	 * @throws VictimExistsException victim exists exception
	 * @throws VisitationExistsException visitation exists exception
	 * @throws OnlineAccountExistsException online account exists exception
	 * 
	 * Exception
	 * @param result result
	 * @return model and view to display the "list"
	 */
	@RequestMapping(value = "create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFENDER_RELATIONSHIP_CREATE')")
	public ModelAndView save(
		@RequestParam(value = "offender", required = true) 
		final Offender offender,
		@RequestParam(value = "relation", required = false) 
		final Person associatedPerson,
		final CreateRelationshipsForm createRelationshipsForm,
		final BindingResult result) throws DuplicateEntityFoundException,
			ReflexiveRelationshipException, DateConflictException,
			FamilyAssociationConflictException, PersonExistsException,
			AddressExistsException, ZipCodeExistsException, CityExistsException,
			ContactExistsException, TelephoneNumberExistsException,
			FamilyAssociationExistsException, VictimExistsException,
			VisitationExistsException, OnlineAccountExistsException {
		Person relation;
		if (associatedPerson == null) {
			// Create new
			if (createRelationshipsForm.getPoBoxFields().getCountry()
				!= null) {
				if (this.createRelationshipsService.hasStates(
					// Has state
					createRelationshipsForm.getPoBoxFields().getCountry())) {
					createRelationshipsForm.getPoBoxFields().setHasState(true);
				} else {
					createRelationshipsForm.getPoBoxFields().setHasState(false);
				}
			} else {
				createRelationshipsForm.getPoBoxFields().setHasState(true);
			}
			
			List<TelephoneNumberItem> validationTelephoneNumberItems 
				= new ArrayList<TelephoneNumberItem>();
			for (TelephoneNumberItem telephoneNumberItem
				: createRelationshipsForm.getTelephoneNumberItems()) {
				if (TelephoneNumberItemOperation.CREATE.equals(
					telephoneNumberItem.getOperation())) {
					validationTelephoneNumberItems.add(telephoneNumberItem);
				}
			}
			createRelationshipsForm.setTelephoneNumberItems(
				validationTelephoneNumberItems);
		
			List<OnlineAccountContactItem> validationOnlineAccountContactItems 
				= new ArrayList<OnlineAccountContactItem>();
			for (OnlineAccountContactItem onlineAccountContactItem 
				: createRelationshipsForm.getOnlineAccountContactItems()) {
				if (OnlineAccountContactItemOperation.CREATE.equals(
					onlineAccountContactItem.getOperation())) {
					validationOnlineAccountContactItems.add(
						onlineAccountContactItem);
				}
			}
			createRelationshipsForm.setOnlineAccountContactItems(
				validationOnlineAccountContactItems);
			
			this.createRelationshipsFormValidator.validate(
				createRelationshipsForm, result); 
			
			if (result.hasErrors()) {
				return this.prepareRedisplayEditMav(createRelationshipsForm, 
					offender, associatedPerson, result);
			}
			
			Boolean personFieldsNewCity = false;
			Boolean addressFieldsNewCity = false;
			Boolean poBoxFieldsNewCity = false;
			if (createRelationshipsForm.getPersonFields().getNewCity()) {
				personFieldsNewCity = true;
			}
			if (createRelationshipsForm.getEnterAddress() != null 
				&& createRelationshipsForm.getEnterAddress()) {
				// If checkbox checked
				if (OffenderRelationshipAddressOperation.NEW.equals(
					createRelationshipsForm.getAddressOperation())
					&& createRelationshipsForm.getAddressFields()
					.getNewCity()) {
					addressFieldsNewCity = true;
				}
			}
			if (createRelationshipsForm.getEnterPoBox() != null
				&& createRelationshipsForm.getEnterPoBox()) {
				// If checkbox checked
				if (createRelationshipsForm.getPoBoxFields().getNewCity()) {
					poBoxFieldsNewCity = true;
				}
			}
			String personFieldsNewCityName;
			String addressFieldsNewCityName;
			String poBoxFieldsNewCityName;
			String personFieldsCountryName;
			String addressFieldsCountryName;
			String poBoxFieldsCountryName;
			String personFieldsStateName;
			String addressFieldsStateName;
			String poBoxFieldsStateName;
			// person fields create new city
			if (personFieldsNewCity) {
				personFieldsNewCityName = createRelationshipsForm
					.getPersonFields().getCityName();
				personFieldsCountryName = createRelationshipsForm
					.getPersonFields().getBirthCountry().getName();
				if (createRelationshipsForm.getPersonFields().getBirthState()
					!= null) {
					personFieldsStateName
						= createRelationshipsForm.getPersonFields()
					.getBirthState().getName();
				} else {
					personFieldsStateName = null;
				}
			} else {
				// person fields not create new city
				personFieldsNewCityName = null;
				if (createRelationshipsForm.getPersonFields().getBirthCountry()
					!= null) {
					personFieldsCountryName = createRelationshipsForm
						.getPersonFields().getBirthCountry().getName();
					if (createRelationshipsForm.getPersonFields()
						.getBirthState() != null) {
						personFieldsStateName = createRelationshipsForm
							.getPersonFields().getBirthState().getName();
					} else {
						personFieldsStateName = null;
					}
				} else {
					personFieldsCountryName = null;
					personFieldsStateName = null;
				}
			}
			if (addressFieldsNewCity) {
				// Address fields creates new city
				addressFieldsNewCityName = createRelationshipsForm
					.getAddressFields().getCityName();
				addressFieldsCountryName = createRelationshipsForm
					.getAddressFields().getCountry().getName();
				if (createRelationshipsForm.getAddressFields()
					.getState() != null) {
					addressFieldsStateName = createRelationshipsForm
					.getAddressFields().getState().getName();
				} else {
					addressFieldsStateName = null;
				}
			} else {
				// Address fields not creates new city
				addressFieldsNewCityName = null;
				// checked, existing
				if (createRelationshipsForm.getEnterAddress() != null
					&& createRelationshipsForm.getEnterAddress()
					&& (!OffenderRelationshipAddressOperation.NEW.equals(
						createRelationshipsForm.getAddressOperation()))) {
					if (createRelationshipsForm.getAddressFields().getCountry()
						!= null) {
						addressFieldsCountryName = createRelationshipsForm
							.getAddressFields().getCountry().getName();
						if (createRelationshipsForm.getAddressFields()
							.getState() != null) {
							addressFieldsStateName = createRelationshipsForm
								.getAddressFields().getState().getName();
						} else {
							addressFieldsStateName = null;
						}
					} else {
						addressFieldsCountryName = null;
						addressFieldsStateName = null;
					}
				} else {
					// not checked
					addressFieldsCountryName = null;
					addressFieldsStateName = null;
				}
			}
			if (poBoxFieldsNewCity) {
				// Po box creates new city
				poBoxFieldsNewCityName = createRelationshipsForm
					.getPoBoxFields().getCityName();
				poBoxFieldsCountryName = createRelationshipsForm
					.getPoBoxFields().getCountry().getName();
				if (createRelationshipsForm.getPoBoxFields().getState()
					!= null) {
					poBoxFieldsStateName 
						= createRelationshipsForm.getPoBoxFields()
					.getState().getName();
				} else {
					poBoxFieldsStateName = null;
				}
			} else {
				// check box unchecked or check box checked + "existing"
				poBoxFieldsNewCityName = null;
				if (createRelationshipsForm.getEnterPoBox() != null
					&& createRelationshipsForm.getEnterPoBox()) {
					// checked + "existing"
					if (createRelationshipsForm.getPoBoxFields().getCountry()
						!= null) {
						poBoxFieldsCountryName = createRelationshipsForm
							.getPoBoxFields().getCountry().getName();
						if (createRelationshipsForm.getPoBoxFields().getState()
							!= null) {
							poBoxFieldsStateName = createRelationshipsForm
								.getPoBoxFields().getState().getName();
						} else {
							poBoxFieldsStateName = null;
						}
					} else {
						poBoxFieldsCountryName = null;
						poBoxFieldsStateName = null;
					}
				} else {
					// unchecked
					poBoxFieldsCountryName = null;
					poBoxFieldsStateName = null;
				}
			}
				
			Boolean addressFieldsNewZipCode = false;
			Boolean poBoxFieldsNewZipCode = false;
			String addressFieldsZipCodeValue;
			String addressFieldsZipCodeExtension;
			String poBoxFieldsZipCodeValue;
			String poBoxFieldsZipCodeExtension;
			if (createRelationshipsForm.getEnterAddress() != null
				&& createRelationshipsForm.getEnterAddress()) {
				// Checked
				if (OffenderRelationshipAddressOperation.NEW.equals(
					createRelationshipsForm.getAddressOperation())
					&& createRelationshipsForm
					.getAddressFields().getNewZipCode()) {
					addressFieldsNewZipCode = true;
					addressFieldsZipCodeValue = createRelationshipsForm
						.getAddressFields().getZipCodeValue();
					addressFieldsZipCodeExtension = createRelationshipsForm
						.getAddressFields().getZipCodeExtension();
				} else {
					addressFieldsZipCodeValue = null;
					addressFieldsZipCodeExtension = null;
				}
			} else {
				// unchecked
				addressFieldsZipCodeValue = null;
				addressFieldsZipCodeExtension = null;
			}
			if (createRelationshipsForm.getEnterPoBox() != null
				&& createRelationshipsForm.getEnterPoBox()) {
				// checked
				if (createRelationshipsForm.getPoBoxFields().getNewZipCode()) {
					poBoxFieldsNewZipCode = true;
					poBoxFieldsZipCodeValue 
						= createRelationshipsForm.getPoBoxFields()
						.getZipCodeValue();
					poBoxFieldsZipCodeExtension = createRelationshipsForm
						.getPoBoxFields().getZipCodeExtension();
				} else {
					poBoxFieldsZipCodeValue = null;
					poBoxFieldsZipCodeExtension = null;
				}
			} else {
				// unchecked
				poBoxFieldsZipCodeValue = null;
				poBoxFieldsZipCodeExtension = null;
			}
		
			PoBox poBox = new PoBox();
			
			City newCreatedAddressFieldsCity;
			City newCreatedPoBoxFieldsCity;
			City newCreatedPersonFieldsCity;
			
			ZipCode newCreatedAddressFieldsZipCode;
			ZipCode newCreatedPoBoxFieldsZipCode;
			
			Boolean addressPoBoxFieldsNewCity = false;
				
			if (personFieldsNewCity && addressFieldsNewCity
				&& poBoxFieldsNewCity) {
				// All three fields are going to create new cities)
				if ((addressFieldsNewCityName.equals(poBoxFieldsNewCityName))
					&& (addressFieldsNewCityName.equals(
						personFieldsNewCityName))
					&& (addressFieldsCountryName.equals(poBoxFieldsCountryName))
					&& (addressFieldsCountryName.equals(
						personFieldsCountryName))
					&& ((addressFieldsStateName != null && personFieldsStateName
					!= null && addressFieldsStateName.equals(
						personFieldsStateName))
						|| (addressFieldsStateName == null
						&& personFieldsStateName == null))
					&& ((addressFieldsStateName != null && poBoxFieldsStateName
					!= null && addressFieldsStateName.equals(
					poBoxFieldsStateName))
					|| (addressFieldsStateName == null && poBoxFieldsStateName
					== null))) {
		// If all new cities in address, po box and person fields are the same
					// Create new city for all fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getAddressFields().getState(),
						addressFieldsNewCityName, 
						createRelationshipsForm.getAddressFields()
						.getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = true;
				} else if ((addressFieldsNewCityName.equals(
					poBoxFieldsNewCityName))
					&& (addressFieldsCountryName.equals(poBoxFieldsCountryName))
					&& ((addressFieldsStateName != null && poBoxFieldsStateName
					!= null && addressFieldsStateName.equals(
					poBoxFieldsStateName))
					|| (addressFieldsStateName == null
					&& poBoxFieldsStateName == null))
					&& ((!addressFieldsNewCityName.equals(
						personFieldsNewCityName))
					|| (!addressFieldsCountryName.equals(
						personFieldsCountryName))
					|| ((addressFieldsStateName != null && personFieldsStateName
					!= null && !addressFieldsStateName.equals(
						personFieldsStateName))
					|| (addressFieldsStateName != null
					&& personFieldsStateName == null)
					|| (addressFieldsStateName == null && personFieldsStateName
					!= null))))	{
// If poBox and address fields new cities are the same, but different from 
// person fields
					// Create new city for address and poBox fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
							createRelationshipsForm.getAddressFields()
							.getState(), addressFieldsNewCityName, 
							createRelationshipsForm.getAddressFields()
							.getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity 
						= this.createRelationshipsService.createCity(
							createRelationshipsForm.getPersonFields()
							.getBirthState(),
							personFieldsNewCityName,
							createRelationshipsForm.getPersonFields()
							.getBirthCountry()); 
					addressPoBoxFieldsNewCity = true;
				} else if ((addressFieldsNewCityName.equals(
					personFieldsNewCityName))
					&& (addressFieldsCountryName.equals(
						personFieldsCountryName))
					&& (addressFieldsStateName != null && personFieldsStateName
					!= null && addressFieldsStateName.equals(
						personFieldsStateName)
						|| (addressFieldsStateName == null
						&& personFieldsStateName == null))
					&& ((!addressFieldsNewCityName.equals(
							poBoxFieldsNewCityName))
						|| (!addressFieldsCountryName.equals(
							poBoxFieldsCountryName))
						|| (addressFieldsStateName != null
						&& poBoxFieldsStateName != null
						&& (!addressFieldsStateName.equals(
								poBoxFieldsStateName))
						|| (addressFieldsStateName != null
						&& poBoxFieldsStateName == null)
						|| (addressFieldsStateName == null
						&& poBoxFieldsStateName != null)))) {
					// If person and address fields new cities are the same, 
					// but different from po box fields
					// Create new city for address and person fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
							createRelationshipsForm.getAddressFields()
							.getState(), addressFieldsNewCityName,
							createRelationshipsForm.getAddressFields()
							.getCountry());
					// Create new city for po box fields
					newCreatedPoBoxFieldsCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getPoBoxFields().getState(),
						poBoxFieldsNewCityName,
						createRelationshipsForm.getPoBoxFields().getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = false;
				} else if ((personFieldsNewCityName.equals(
					poBoxFieldsNewCityName))
					&& (personFieldsCountryName.equals(poBoxFieldsCountryName))
					&& (poBoxFieldsStateName != null && personFieldsStateName
					!= null
					&& personFieldsStateName.equals(poBoxFieldsStateName)
					|| (poBoxFieldsStateName == null
					&& personFieldsStateName == null))
					&& ((!addressFieldsNewCityName.equals(
						personFieldsNewCityName))
					|| (!addressFieldsCountryName.equals(
						personFieldsCountryName))
					|| (addressFieldsStateName != null && personFieldsStateName
					!= null && (!addressFieldsStateName.equals(
						personFieldsStateName))
					|| (addressFieldsStateName != null
					&& personFieldsStateName == null)
					|| (addressFieldsStateName == null
					&& personFieldsStateName != null)))) {
					// If person and po box fields new cities are the same, 
					//but different from address fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
							createRelationshipsForm.getPersonFields()
							.getBirthState(),
							poBoxFieldsNewCityName,
							createRelationshipsForm.getPersonFields()
							.getBirthCountry());
					// Create new city for person and poBox fields
					// Create new city for address fields
					newCreatedAddressFieldsCity 
						= this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getAddressFields().getState(),
						addressFieldsNewCityName,
						createRelationshipsForm.getAddressFields()
						.getCountry());
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = false;
				} else {
					// All new cities in three fields are different
					// Create new city for po box fields
					newCreatedPoBoxFieldsCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getPoBoxFields().getState(),
						poBoxFieldsNewCityName,
						createRelationshipsForm.getPoBoxFields().getCountry());
					// Create new city for address fields
					newCreatedAddressFieldsCity 
						= this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getAddressFields().getState(),
						addressFieldsNewCityName,
						createRelationshipsForm.getAddressFields()
						.getCountry());
					newCreatedPersonFieldsCity 
						= this.createRelationshipsService.createCity(
							createRelationshipsForm.getPersonFields()
							.getBirthState(),
							personFieldsNewCityName,
							createRelationshipsForm.getPersonFields()
							.getBirthCountry());
					addressPoBoxFieldsNewCity = false;
				}
			} else if (poBoxFieldsNewCity && addressFieldsNewCity
				&& (!personFieldsNewCity)) {
				// po box and address fields create new cities, 
				// person fields not
				if (poBoxFieldsNewCityName.equals(addressFieldsNewCityName)
					&& poBoxFieldsCountryName.equals(addressFieldsCountryName)
					&& ((poBoxFieldsStateName != null && addressFieldsStateName
					!= null
					&& poBoxFieldsStateName.equals(addressFieldsStateName)
						|| (poBoxFieldsStateName == null
						&& addressFieldsStateName == null)))) {
					// po box and address fields' new city names are same
					// Create same new city for address and person fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
							createRelationshipsForm.getAddressFields()
							.getState(),
							addressFieldsNewCityName,
							createRelationshipsForm.getAddressFields()
							.getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = null;
					addressPoBoxFieldsNewCity = true;
				} else {
					// Different new cities for po box and address fields
					newCreatedAddressFieldsCity 
						= this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getAddressFields().getState(),
						addressFieldsNewCityName,
						createRelationshipsForm.getAddressFields()
						.getCountry());
					newCreatedPoBoxFieldsCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getPoBoxFields().getState(),
						poBoxFieldsNewCityName,
						createRelationshipsForm.getPoBoxFields().getCountry());
					newCreatedPersonFieldsCity = null;
					addressPoBoxFieldsNewCity = false;
				}
			} else if ((!poBoxFieldsNewCity) && addressFieldsNewCity
					&& personFieldsNewCity) {
			// person and address fields create new cities, po box fields not
				if (personFieldsNewCityName.equals(addressFieldsNewCityName)
					&& personFieldsCountryName.equals(addressFieldsCountryName)
					&& (personFieldsStateName != null
					&& addressFieldsStateName != null
					&& personFieldsStateName.equals(addressFieldsStateName)
					|| (personFieldsStateName == null
					&& addressFieldsStateName == null))) {
					// person and address fields' new city names are same
					// Create same new city for address and person fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getAddressFields().getState(),
						addressFieldsNewCityName,
						createRelationshipsForm.getAddressFields()
						.getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = null;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = false;
				} else {
					// Different new cities for person and address fields
					newCreatedAddressFieldsCity
						= this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getAddressFields().getState(),
						addressFieldsNewCityName,
						createRelationshipsForm.getAddressFields()
						.getCountry());
					newCreatedPoBoxFieldsCity = null;
					newCreatedPersonFieldsCity 
						= this.createRelationshipsService.createCity(
							createRelationshipsForm.getPersonFields()
							.getBirthState(),
							personFieldsNewCityName,
							createRelationshipsForm.getPersonFields()
							.getBirthCountry());
					addressPoBoxFieldsNewCity = false;
				}
			} else if (poBoxFieldsNewCity && (!addressFieldsNewCity)
				&& personFieldsNewCity) {
			// person and po box fields create new cities, address fields not
				if (personFieldsNewCityName.equals(poBoxFieldsNewCityName)
					&& personFieldsCountryName.equals(poBoxFieldsCountryName)
					&& ((personFieldsStateName != null
					&& poBoxFieldsStateName != null
					&& personFieldsStateName.equals(poBoxFieldsStateName))
					|| (personFieldsStateName == null
					&& poBoxFieldsStateName == null))
					) {
					// person and po box fields' new city names are same
					// Create same new city for address and person fields
					City newCreatedCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getPersonFields()
						.getBirthState(),
						personFieldsNewCityName,
						createRelationshipsForm.getPersonFields()
						.getBirthCountry());
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					newCreatedAddressFieldsCity = null;
					addressPoBoxFieldsNewCity = false;
				} else {
					// Different new cities for person and po box fields
					newCreatedPoBoxFieldsCity = this.createRelationshipsService
						.createCity(
						createRelationshipsForm.getPoBoxFields().getState(),
						poBoxFieldsNewCityName,
						createRelationshipsForm.getPoBoxFields().getCountry());
					newCreatedPersonFieldsCity 
						= this.createRelationshipsService.createCity(
							createRelationshipsForm.getPersonFields()
							.getBirthState(),
							personFieldsNewCityName,
							createRelationshipsForm.getPersonFields()
							.getBirthCountry());
					newCreatedAddressFieldsCity = null;
					addressPoBoxFieldsNewCity = false;
				}
			} else if (poBoxFieldsNewCity && (!addressFieldsNewCity)
				&& (!personFieldsNewCity)) {
				// po box fields create new cities, person and address
				// fields not
				// Create same new city for address and person fields
				newCreatedPoBoxFieldsCity = this.createRelationshipsService
					.createCity(
					createRelationshipsForm.getPoBoxFields().getState(),
					poBoxFieldsNewCityName,
					createRelationshipsForm.getPoBoxFields().getCountry());
				newCreatedAddressFieldsCity = null;
				newCreatedPersonFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			} else if ((!poBoxFieldsNewCity) && addressFieldsNewCity
					&& (!personFieldsNewCity)) {
				// address fields create new cities, person and po box 
				// fields not
				// Create same new city for address and person fields
				newCreatedAddressFieldsCity = this.createRelationshipsService
					.createCity(
					createRelationshipsForm.getAddressFields().getState(),
					addressFieldsNewCityName,
					createRelationshipsForm.getAddressFields().getCountry());
				newCreatedPoBoxFieldsCity = null;
				newCreatedPersonFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			} else if (personFieldsNewCity && (!addressFieldsNewCity)
					&& (!poBoxFieldsNewCity)) {
				// person fields create new cities, po box and 
				// address fields not
				newCreatedPoBoxFieldsCity = null;
				newCreatedAddressFieldsCity = null;
				newCreatedPersonFieldsCity = this.createRelationshipsService
					.createCity(
					createRelationshipsForm.getPersonFields().getBirthState(),
					personFieldsNewCityName,
					createRelationshipsForm.getPersonFields()
					.getBirthCountry());
				addressPoBoxFieldsNewCity = false;
			} else {
				// None creates new city
				newCreatedPoBoxFieldsCity = null;
				newCreatedAddressFieldsCity = null;
				newCreatedPersonFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			}
			
			// Both address and po box fields will create new zip codes
			if (addressFieldsNewZipCode && poBoxFieldsNewZipCode) {
				// New zip codes in address and po box fields are same
				if (addressFieldsZipCodeValue.equals(poBoxFieldsZipCodeValue) 
					&& addressFieldsZipCodeExtension.equals(
						poBoxFieldsZipCodeExtension)) {
		// Both address and po box fields create new cities and they are same
					if (addressPoBoxFieldsNewCity) {
		// Create new zip code for address and po box fields
						ZipCode newCreatedZipCode 
								= this.createRelationshipsService.createZipCode(
									newCreatedPoBoxFieldsCity,
									createRelationshipsForm.getAddressFields()
									.getZipCodeValue(), 
									createRelationshipsForm.getAddressFields()
									.getZipCodeExtension());
						newCreatedAddressFieldsZipCode = newCreatedZipCode;
						newCreatedPoBoxFieldsZipCode = newCreatedZipCode;
						poBox.setValue(createRelationshipsForm.getPoBoxFields()
								.getPoBoxValue());
						poBox.setZipCode(newCreatedZipCode);
					} else {
			// new cities created for po box and address fields are different
						if (newCreatedPoBoxFieldsCity != null
			// Both po box and address fields create new cities,but different
								&& newCreatedAddressFieldsCity != null) {
							newCreatedAddressFieldsZipCode = 
									this.createRelationshipsService
									.createZipCode(newCreatedAddressFieldsCity,
										createRelationshipsForm
										.getAddressFields()
										.getZipCodeValue(), 
										createRelationshipsForm
										.getAddressFields()
										.getZipCodeExtension());
							newCreatedPoBoxFieldsZipCode = 
									this.createRelationshipsService
									.createZipCode(
										newCreatedPoBoxFieldsCity,
										createRelationshipsForm.getPoBoxFields()
										.getZipCodeValue(), 
										createRelationshipsForm.getPoBoxFields()
										.getZipCodeExtension());
						} else if (newCreatedPoBoxFieldsCity == null
								&& newCreatedAddressFieldsCity != null) {
		// Po box field does not creates new city and address fields does 
							newCreatedAddressFieldsZipCode = 
									this.createRelationshipsService
									.createZipCode(
										newCreatedAddressFieldsCity, 
										createRelationshipsForm
										.getAddressFields()
											.getZipCodeValue(), 
										createRelationshipsForm
										.getAddressFields()
											.getZipCodeExtension());
							newCreatedPoBoxFieldsZipCode =
									this.createRelationshipsService
									.createZipCode(
										createRelationshipsForm.getPoBoxFields()
										.getCity(),
										createRelationshipsForm.getPoBoxFields()
										.getZipCodeValue(),
										createRelationshipsForm.getPoBoxFields()
										.getZipCodeExtension());
						} else if (newCreatedPoBoxFieldsCity != null
								&& newCreatedAddressFieldsCity == null) {
				// Po box field creates new city and address fields does not
							newCreatedAddressFieldsZipCode = 
								this.createRelationshipsService.createZipCode(
										createRelationshipsForm
										.getAddressFields().getCity(), 
										createRelationshipsForm
										.getAddressFields()
											.getZipCodeValue(), 
										createRelationshipsForm
										.getAddressFields()
											.getZipCodeExtension());
							newCreatedPoBoxFieldsZipCode = 
										this.createRelationshipsService
										.createZipCode(
											newCreatedPoBoxFieldsCity,
											createRelationshipsForm
											.getPoBoxFields().getZipCodeValue(),
											createRelationshipsForm
											.getPoBoxFields()
											.getZipCodeExtension());
						} else {
								// none create new zip code
							if (!createRelationshipsForm.getAddressFields()
									.getCity().equals(createRelationshipsForm
									.getPoBoxFields().getCity())) {
								newCreatedAddressFieldsZipCode = 
										this.createRelationshipsService
										.createZipCode(
											createRelationshipsForm
											.getAddressFields()
											.getCity(), 
											createRelationshipsForm
											.getAddressFields()
											.getZipCodeValue(), 
											createRelationshipsForm
											.getAddressFields()
											.getZipCodeExtension());
								newCreatedPoBoxFieldsZipCode = 
										this.createRelationshipsService
										.createZipCode(
											createRelationshipsForm
											.getPoBoxFields().getCity(),
											createRelationshipsForm
											.getPoBoxFields().getZipCodeValue(),
											createRelationshipsForm
											.getPoBoxFields()
											.getZipCodeExtension());
							} else {
								newCreatedAddressFieldsZipCode = 
										this.createRelationshipsService
										.createZipCode(
											createRelationshipsForm
											.getAddressFields()
											.getCity(),
											createRelationshipsForm
											.getAddressFields()
											.getZipCodeValue(), 
											createRelationshipsForm
											.getAddressFields()
											.getZipCodeExtension());
								newCreatedPoBoxFieldsZipCode 
									= newCreatedAddressFieldsZipCode; 
							}
						}
					}
					poBox.setValue(createRelationshipsForm.getPoBoxFields()
							.getPoBoxValue());
					poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
				} else {
					// new zip codes in address and po box fields are different
					if (newCreatedAddressFieldsCity != null) {
						newCreatedAddressFieldsZipCode 
							= this.createRelationshipsService.createZipCode(
								newCreatedAddressFieldsCity,
								createRelationshipsForm.getAddressFields()
									.getZipCodeValue(), 
								createRelationshipsForm.getAddressFields()
									.getZipCodeExtension());
					} else {
						newCreatedAddressFieldsZipCode 
							= this.createRelationshipsService.createZipCode(
							createRelationshipsForm.getAddressFields()
								.getCity(),
							createRelationshipsForm.getAddressFields()
								.getZipCodeValue(), 
							createRelationshipsForm.getAddressFields()
								.getZipCodeExtension());
					}
					if (newCreatedPoBoxFieldsCity != null) {
						newCreatedPoBoxFieldsZipCode 
							= this.createRelationshipsService.createZipCode(
								newCreatedPoBoxFieldsCity,
								createRelationshipsForm.getPoBoxFields()
									.getZipCodeValue(), 
								createRelationshipsForm.getPoBoxFields()
									.getZipCodeExtension());
					} else {
						newCreatedPoBoxFieldsZipCode 
							= this.createRelationshipsService.createZipCode(
							createRelationshipsForm.getPoBoxFields().getCity(),
							createRelationshipsForm.getPoBoxFields()
								.getZipCodeValue(), 
							createRelationshipsForm.getPoBoxFields()
								.getZipCodeExtension());
					}
					poBox.setValue(createRelationshipsForm.getPoBoxFields()
						.getPoBoxValue());
					poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
				}
			} else if (addressFieldsNewZipCode && (!poBoxFieldsNewZipCode)) {
				// Address fields create new zip code, po box fields not
				if (newCreatedAddressFieldsCity == null) {
					newCreatedAddressFieldsZipCode 
						= this.createRelationshipsService.createZipCode(
						createRelationshipsForm.getAddressFields().getCity(),
						createRelationshipsForm.getAddressFields()
							.getZipCodeValue(), 
						createRelationshipsForm.getAddressFields()
							.getZipCodeExtension());
				} else {
					newCreatedAddressFieldsZipCode 
						= this.createRelationshipsService.createZipCode(
						newCreatedAddressFieldsCity,
						createRelationshipsForm.getAddressFields()
							.getZipCodeValue(), 
						createRelationshipsForm.getAddressFields()
							.getZipCodeExtension());
				}
				newCreatedPoBoxFieldsZipCode = null;
				poBox.setValue(createRelationshipsForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(createRelationshipsForm.getPoBoxFields()
					.getZipCode());
			} else if ((!addressFieldsNewZipCode) && poBoxFieldsNewZipCode) {
				// Po Box fields create new zip code, address fields not
				if (newCreatedPoBoxFieldsCity == null) {
					newCreatedPoBoxFieldsZipCode 
						= this.createRelationshipsService.createZipCode(
						createRelationshipsForm.getPoBoxFields().getCity(),
						createRelationshipsForm.getPoBoxFields()
							.getZipCodeValue(), 
						createRelationshipsForm.getPoBoxFields()
							.getZipCodeExtension());
				} else {
					newCreatedPoBoxFieldsZipCode 
						= this.createRelationshipsService.createZipCode(
						newCreatedPoBoxFieldsCity,
						createRelationshipsForm.getPoBoxFields()
							.getZipCodeValue(), 
						createRelationshipsForm.getPoBoxFields()
							.getZipCodeExtension());
				}
				newCreatedAddressFieldsZipCode = null;
				poBox.setValue(createRelationshipsForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
			} else {
				// None create new zip codes
				newCreatedPoBoxFieldsZipCode = null;
				newCreatedAddressFieldsZipCode = null;
				poBox.setValue(createRelationshipsForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(createRelationshipsForm.getPoBoxFields()
					.getZipCode());
			}
			
			Address address;
			if (createRelationshipsForm.getEnterAddress() != null
				&& createRelationshipsForm.getEnterAddress()) {
				if (OffenderRelationshipAddressOperation.NEW.equals(
					createRelationshipsForm.getAddressOperation())) {
					// New address
					if (createRelationshipsForm.getAddressFields()
						.getNewCity()) {
						// New city in address fields
					    address = this.createRelationshipsService.createAddress(
					    createRelationshipsForm.getAddressFields().getValue(), 
					    newCreatedAddressFieldsZipCode);   
					} else {
						// Existing city, existing zip code in address fields
						if (newCreatedAddressFieldsZipCode == null) {
							// Existing zip code
						    address = this.createRelationshipsService
						    	.createAddress(
						    createRelationshipsForm.getAddressFields()
						    .getValue(), 
						    createRelationshipsForm.getAddressFields()
						    .getZipCode());   
						} else {
							// New created zip code
						    address = this.createRelationshipsService
						    	.createAddress(
						    createRelationshipsForm.getAddressFields()
						    .getValue(), 
						    newCreatedAddressFieldsZipCode);   
						}
					}
				} else {
					// Existing address in address fields
					address = createRelationshipsForm.getAddress();
				}
			} else {
				address = null;
			}
				
			if (!createRelationshipsForm.getPersonFields().getNewCity()) {
				// Exiting city
				Integer socialSecurityNumber;
				if (createRelationshipsForm.getPersonFields()
						.getSocialSecurityNumber() != null
						&& !createRelationshipsForm.getPersonFields()
							.getSocialSecurityNumber().isEmpty()) {
					socialSecurityNumber
						= Integer.valueOf(createRelationshipsForm
								.getPersonFields()
									.getSocialSecurityNumber()
										.replaceAll("-", ""));
				} else {
					socialSecurityNumber = null;
				}
				Person newCreatedAssociated = this.createRelationshipsService
					.createRelation(
					createRelationshipsForm.getPersonFields().getLastName(), 
					createRelationshipsForm.getPersonFields().getFirstName(), 
					createRelationshipsForm.getPersonFields().getMiddleName(), 
					createRelationshipsForm.getPersonFields().getSuffix(), 
					createRelationshipsForm.getPersonFields().getSex(), 
					createRelationshipsForm.getPersonFields().getBirthDate(), 
					createRelationshipsForm.getPersonFields().getBirthCountry(),
					createRelationshipsForm.getPersonFields().getBirthState(), 
					createRelationshipsForm.getPersonFields().getBirthCity(), 
					socialSecurityNumber, 
					createRelationshipsForm.getPersonFields()
					.getStateIdNumber(),
					createRelationshipsForm.getPersonFields().getDeceased(), 
					createRelationshipsForm.getPersonFields().getDeathDate());
				relation = newCreatedAssociated;
				/* Create victim association */
				if (createRelationshipsForm.getCreateVictim() != null
					&& createRelationshipsForm.getCreateVictim()) {
					VictimAssociationFlags flags = new VictimAssociationFlags(
						createRelationshipsForm.getVictimFields().getVictim(),
						createRelationshipsForm.getVictimFields()
							.getDocRegistered(),
						createRelationshipsForm.getVictimFields()
							.getBusiness(),
						createRelationshipsForm.getVictimFields()
							.getVineRegistered());
					this.createRelationshipsService.associateVictim(
							offender, newCreatedAssociated, 
							createRelationshipsForm.getVictimFields()
								.getRegisterDate(), 
							createRelationshipsForm.getVictimFields()
								.getPacketSent(), 
							createRelationshipsForm.getVictimFields()
								.getPacketSendDate(), 
							flags);
				}
				
				/* Create family member association */
				if (createRelationshipsForm.getCreateFamilyMember() != null
					&& createRelationshipsForm.getCreateFamilyMember()) {
					DateRange newCreatedFamilyDateRange = new DateRange(
						createRelationshipsForm.getFamilyAssociationFields()
						.getStartDate(), 
						createRelationshipsForm.getFamilyAssociationFields()
						.getEndDate());
					FamilyAssociationFlags newCreatedFamilyAssociationFlags 
						= new FamilyAssociationFlags(createRelationshipsForm
							.getFamilyAssociationFields().getCohabitant(), 
							createRelationshipsForm.getFamilyAssociationFields()
							.getDependent(),
							createRelationshipsForm.getFamilyAssociationFields()
							.getEmergencyContact());
					this.createRelationshipsService.associateFamilyMember(
						offender, 
						newCreatedAssociated,
						newCreatedFamilyDateRange,
						createRelationshipsForm.getFamilyAssociationFields()
							.getCategory(),
						newCreatedFamilyAssociationFlags,
						createRelationshipsForm.getFamilyAssociationFields()
							.getMarriageDate(),
						createRelationshipsForm.getFamilyAssociationFields()
							.getDivorceDate());
				}
				
				/* Create vistation association */
				if (createRelationshipsForm.getCreateVisitor() != null
					&& createRelationshipsForm.getCreateVisitor()) {
					VisitationAssociationFlags visitationAssociationFlags 
						= new VisitationAssociationFlags(
							createRelationshipsForm
								.getVisitationAssociationFields().getMoney(),
							createRelationshipsForm
								.getVisitationAssociationFields()
								.getNonContact(),
							createRelationshipsForm
								.getVisitationAssociationFields()
								.getCourtOrder(),
							createRelationshipsForm
								.getVisitationAssociationFields()
								.getSpecialVisit());
					VisitationApproval visitationApproval 
						= new VisitationApproval(createRelationshipsForm
							.getVisitationAssociationFields().getApproved(),
							createRelationshipsForm
							.getVisitationAssociationFields()
							.getDecisionDate());
					this.createRelationshipsService.associateVisitor(offender, 
						newCreatedAssociated, 
						createRelationshipsForm.getVisitationAssociationFields()
							.getCategory(), 
						visitationApproval,
						createRelationshipsForm.getVisitationAssociationFields()
							.getStartDate(),
						createRelationshipsForm.getVisitationAssociationFields()
							.getEndDate(),
						visitationAssociationFlags, 
						createRelationshipsForm.getVisitationAssociationFields()
							.getNotes(),
						createRelationshipsForm.getVisitationAssociationFields()
							.getGuardianship());
				}
				
				/* Create contact if it does not exist 
				 * for "newCreatedAssociated" */
				this.createRelationshipsService.changeContact(
					newCreatedAssociated, poBox, address);
				
				/* Create telephone number */
				List<TelephoneNumberItem> telephoneNumberItems 
					= createRelationshipsForm.getTelephoneNumberItems();
				for (TelephoneNumberItem item : telephoneNumberItems) {
					if (TelephoneNumberItemOperation.CREATE.equals(
						item.getOperation())) {
						this.createRelationshipsService.createTelephoneNumber(
							newCreatedAssociated, 
							item.getTelephoneNumberFields().getValue(), 
							item.getTelephoneNumberFields().getExtension(), 
							resolveCheckBoxValue(item.getTelephoneNumberFields()
								.getPrimary()),
							resolveCheckBoxValue(item.getTelephoneNumberFields()
								.getActive()),
							item.getTelephoneNumberFields().getCategory());
					}
				}
				/* Create online account */
				List<OnlineAccountContactItem> onlineAccountItems 
					= createRelationshipsForm.getOnlineAccountContactItems();
				for (OnlineAccountContactItem onlineAccountContactItem
						: onlineAccountItems) {
					if (OnlineAccountContactItemOperation.CREATE.equals(
						onlineAccountContactItem.getOperation())) {
						this.createRelationshipsService.createOnlineAccount(
							newCreatedAssociated, 
							onlineAccountContactItem.getOnlineAccountFields()
							.getName(),
							resolveCheckBoxValue(onlineAccountContactItem
								.getOnlineAccountFields().getPrimary()),
							onlineAccountContactItem.getOnlineAccountFields()
							.getHost(),
							resolveCheckBoxValue(onlineAccountContactItem
								.getOnlineAccountFields().getActive()));	
					}
				}
			} else {
				// New city
				Integer socialSecurityNumber;
				if (createRelationshipsForm.getPersonFields()
						.getSocialSecurityNumber() != null
						&& !createRelationshipsForm.getPersonFields()
							.getSocialSecurityNumber().isEmpty()) {
					socialSecurityNumber
						= Integer.valueOf(createRelationshipsForm
								.getPersonFields()
									.getSocialSecurityNumber()
										.replaceAll("-", ""));
				} else {
					socialSecurityNumber = null;
				}
				Person newCreatedAssociated = this.createRelationshipsService
					.createRelation(
					createRelationshipsForm.getPersonFields().getLastName(), 
					createRelationshipsForm.getPersonFields().getFirstName(), 
					createRelationshipsForm.getPersonFields().getMiddleName(), 
					createRelationshipsForm.getPersonFields().getSuffix(), 
					createRelationshipsForm.getPersonFields().getSex(), 
					createRelationshipsForm.getPersonFields().getBirthDate(), 
					createRelationshipsForm.getPersonFields().getBirthCountry(),
					createRelationshipsForm.getPersonFields().getBirthState(), 
					newCreatedPersonFieldsCity,
					socialSecurityNumber, 
					createRelationshipsForm.getPersonFields()
						.getStateIdNumber(), 
					createRelationshipsForm.getPersonFields().getDeceased(), 
					createRelationshipsForm.getPersonFields().getDeathDate());
				relation = newCreatedAssociated;
				/* Create victim association */
				if (createRelationshipsForm.getCreateVictim() != null
					&& createRelationshipsForm.getCreateVictim()) {
					VictimAssociationFlags flags = new VictimAssociationFlags(
								createRelationshipsForm.getVictimFields()
								.getVictim(),
								createRelationshipsForm.getVictimFields()
									.getDocRegistered(),
								createRelationshipsForm.getVictimFields()
									.getBusiness(),
								createRelationshipsForm.getVictimFields()
									.getVineRegistered());
					this.createRelationshipsService.associateVictim(offender,
								newCreatedAssociated, 
								createRelationshipsForm.getVictimFields()
									.getRegisterDate(), 
								createRelationshipsForm.getVictimFields()
									.getPacketSent(), 
								createRelationshipsForm.getVictimFields()
									.getPacketSendDate(), 
								flags);
				}
				
				/* Create family member association */
				if (createRelationshipsForm.getCreateFamilyMember() != null
					&& createRelationshipsForm.getCreateFamilyMember()) {
					DateRange newCreatedFamilyDateRange = new DateRange(
							createRelationshipsForm.getFamilyAssociationFields()
							.getStartDate(), 
							createRelationshipsForm.getFamilyAssociationFields()
							.getEndDate());
					FamilyAssociationFlags newCreatedFamilyAssociationFlags 
							= new FamilyAssociationFlags(createRelationshipsForm
								.getFamilyAssociationFields().getCohabitant(), 
								createRelationshipsForm
								.getFamilyAssociationFields()
								.getDependent(),
								createRelationshipsForm
								.getFamilyAssociationFields()
								.getEmergencyContact());
					this.createRelationshipsService.associateFamilyMember(
							offender, 
							newCreatedAssociated,
							newCreatedFamilyDateRange,
							createRelationshipsForm.getFamilyAssociationFields()
								.getCategory(),
							newCreatedFamilyAssociationFlags,
							createRelationshipsForm.getFamilyAssociationFields()
								.getMarriageDate(),
							createRelationshipsForm.getFamilyAssociationFields()
								.getDivorceDate());
				}
				
				/* Create vistation association */
				if (createRelationshipsForm.getCreateVisitor() != null
					&& createRelationshipsForm.getCreateVisitor()) {
					VisitationAssociationFlags visitationAssociationFlags 
							= new VisitationAssociationFlags(
								createRelationshipsForm
									.getVisitationAssociationFields()
									.getMoney(),
								createRelationshipsForm
									.getVisitationAssociationFields()
									.getNonContact(),
								createRelationshipsForm
									.getVisitationAssociationFields()
									.getCourtOrder(),
								createRelationshipsForm
									.getVisitationAssociationFields()
									.getSpecialVisit());
					VisitationApproval visitationApproval 
							= new VisitationApproval(createRelationshipsForm
								.getVisitationAssociationFields().getApproved(),
								createRelationshipsForm
								.getVisitationAssociationFields()
								.getDecisionDate());
					VisitationAssociation visitationAssociation = 
							this.createRelationshipsService.associateVisitor(
							offender,
							newCreatedAssociated, 
							createRelationshipsForm
							.getVisitationAssociationFields()
								.getCategory(), 
							visitationApproval,
							createRelationshipsForm
							.getVisitationAssociationFields()
								.getStartDate(),
							createRelationshipsForm
							.getVisitationAssociationFields()
								.getEndDate(),
							visitationAssociationFlags, 
							createRelationshipsForm
							.getVisitationAssociationFields()
								.getNotes(),
							createRelationshipsForm
							.getVisitationAssociationFields()
								.getGuardianship());
					visitationAssociation
								.setNotes(createRelationshipsForm
								.getVisitationAssociationFields().getNotes());
				}
				
				/* Create contact if it does not exist for 
				 * "newCreatedAssociated" */
				this.createRelationshipsService.changeContact(
					newCreatedAssociated, poBox, address);
					
				/* Create telephone number. DuplicateEntityFoundException */
				List<TelephoneNumberItem> telephoneNumberItems 
					= createRelationshipsForm.getTelephoneNumberItems();
				for (TelephoneNumberItem item : telephoneNumberItems) {
					if (TelephoneNumberItemOperation.CREATE.equals(
						item.getOperation())) {
						this.createRelationshipsService.changeContact(
							newCreatedAssociated, poBox, address);
						this.createRelationshipsService.createTelephoneNumber(
							newCreatedAssociated, 
							item.getTelephoneNumberFields().getValue(), 
							item.getTelephoneNumberFields().getExtension(), 
							resolveCheckBoxValue(item.getTelephoneNumberFields()
								.getPrimary()), 
							resolveCheckBoxValue(item.getTelephoneNumberFields()
								.getActive()),
							item.getTelephoneNumberFields().getCategory());
					}
				}
				/* Create online account. DuplicateEntityFoundException */
				List<OnlineAccountContactItem> onlineAccountItems 
					= createRelationshipsForm.getOnlineAccountContactItems();
				for (OnlineAccountContactItem onlineAccountContactItem
						: onlineAccountItems) {
					if (OnlineAccountContactItemOperation.CREATE.equals(
						onlineAccountContactItem.getOperation())) {
						this.createRelationshipsService.createOnlineAccount(
								newCreatedAssociated, 
								onlineAccountContactItem
								.getOnlineAccountFields().getName(), 
								resolveCheckBoxValue(onlineAccountContactItem
									.getOnlineAccountFields().getPrimary()),
								onlineAccountContactItem
								.getOnlineAccountFields().getHost(),
								resolveCheckBoxValue(onlineAccountContactItem
									.getOnlineAccountFields().getActive()));
					}
				}
			}
		} else {
			// Existing... click the "Create Family Association" link on the 
			// left side of each row on the search result screen
			this.createRelationshipsFromSearchFormValidator.validate(
				createRelationshipsForm, result); 
			if (result.hasErrors()) {
				return this.prepareRedisplayEditMav(createRelationshipsForm, 
					offender, associatedPerson, result);
			}
			
			relation = associatedPerson;
			/* Create victim association */
			if (createRelationshipsForm.getCreateVictim() != null
				&& createRelationshipsForm.getCreateVictim()) {
				VictimAssociationFlags flags = new VictimAssociationFlags(
						createRelationshipsForm.getVictimFields().getVictim(),
						createRelationshipsForm.getVictimFields()
							.getDocRegistered(),
						createRelationshipsForm.getVictimFields()
							.getBusiness(),
						createRelationshipsForm.getVictimFields()
							.getVineRegistered());
				this.createRelationshipsService.associateVictim(
						offender, associatedPerson, 
						createRelationshipsForm.getVictimFields()
							.getRegisterDate(), 
						createRelationshipsForm.getVictimFields()
							.getPacketSent(), 
						createRelationshipsForm.getVictimFields()
							.getPacketSendDate(), 
						flags);
			}
			
			/* Create family member association */
			if (createRelationshipsForm.getCreateFamilyMember() != null
				&& createRelationshipsForm.getCreateFamilyMember()) {
				DateRange newCreatedFamilyDateRange = new DateRange(
						createRelationshipsForm.getFamilyAssociationFields()
						.getStartDate(), 
						createRelationshipsForm.getFamilyAssociationFields()
						.getEndDate());
				FamilyAssociationFlags newCreatedFamilyAssociationFlags 
						= new FamilyAssociationFlags(createRelationshipsForm
							.getFamilyAssociationFields().getCohabitant(), 
							createRelationshipsForm.getFamilyAssociationFields()
							.getDependent(),
							createRelationshipsForm.getFamilyAssociationFields()
							.getEmergencyContact());
				this.createRelationshipsService.associateFamilyMember(
						offender, associatedPerson,
						newCreatedFamilyDateRange,
						createRelationshipsForm.getFamilyAssociationFields()
							.getCategory(),
						newCreatedFamilyAssociationFlags,
						createRelationshipsForm.getFamilyAssociationFields()
							.getMarriageDate(),
						createRelationshipsForm.getFamilyAssociationFields()
							.getDivorceDate());
			}
			
			/* Create vistation association */
			if (createRelationshipsForm.getCreateVisitor() != null
				&& createRelationshipsForm.getCreateVisitor()) {
				VisitationAssociationFlags visitationAssociationFlags 
						= new VisitationAssociationFlags(
							createRelationshipsForm
								.getVisitationAssociationFields().getMoney(),
							createRelationshipsForm
								.getVisitationAssociationFields()
								.getNonContact(),
							createRelationshipsForm
								.getVisitationAssociationFields()
								.getCourtOrder(),
							createRelationshipsForm
								.getVisitationAssociationFields()
								.getSpecialVisit());
				VisitationApproval visitationApproval 
						= new VisitationApproval(createRelationshipsForm
							.getVisitationAssociationFields().getApproved(),
							createRelationshipsForm
							.getVisitationAssociationFields()
							.getDecisionDate());
				VisitationAssociation visitationAssociation = 
						this.createRelationshipsService.associateVisitor(
						offender, associatedPerson, 
						createRelationshipsForm.getVisitationAssociationFields()
							.getCategory(), 
						visitationApproval,
						createRelationshipsForm.getVisitationAssociationFields()
							.getStartDate(),
						createRelationshipsForm.getVisitationAssociationFields()
							.getEndDate(),
						visitationAssociationFlags, 
						createRelationshipsForm.getVisitationAssociationFields()
							.getNotes(),
						createRelationshipsForm.getVisitationAssociationFields()
							.getGuardianship());
				visitationAssociation.setNotes(createRelationshipsForm
							.getVisitationAssociationFields().getNotes());
			}
		}
		if (createRelationshipsForm.getNoteItems() != null) {
			for (OffenderRelationshipNoteItem item
					: createRelationshipsForm.getNoteItems()) {
				if (OffenderRelationshipNoteItemOperation.CREATE
						.equals(item.getOperation())) {
					this.createRelationshipsService
						.createNote(offender, relation,
								item.getFields().getCategory(),
								item.getFields().getValue(),
								item.getFields().getDate());
				} else if (item.getOperation() != null) {
					throw new UnsupportedOperationException(
							String.format("Note operation not supported: %s",
									item.getOperation()));
				}
			}
		}
		return new ModelAndView(String.format(LIST_REDIRECT, 
				offender.getId()));
	}
	
	/* Helper methods. */

	/*
	 * Prepares a model and view for creating.
	 * 
	 * @param form create relationships form
	 * 
	 * @param offender offender
	 * 
	 * @param map model map
	 * 
	 * @return prepared model and view
	 */
	private ModelAndView prepareCreateMav(final CreateRelationshipsForm form, 
			final Offender offender, final Person relation) {
		ModelAndView mav = new ModelAndView(CREATE_VIEW_NAME);
		if(form.getFamilyAssociationFields()!=null){
			if(form.getFamilyAssociationFields().getCategory()!=null){
				if(FamilyAssociationCategoryClassification.SPOUSE.equals(form
					.getFamilyAssociationFields().getCategory().getClassification())){
					mav.addObject(SPAUSE_STATUS, true);
				} else {
					mav.addObject(SPAUSE_STATUS, false);
				}
			} else {
				mav.addObject(SPAUSE_STATUS, false);
			}
		}else {
			mav.addObject(SPAUSE_STATUS, false);
		}
		if (form.getCreateFamilyMember() == null) {
			mav.addObject(CREATE_FAMILY_MEMBER_STATUS, false);
		} else {
			mav.addObject(CREATE_FAMILY_MEMBER_STATUS,
				form.getCreateFamilyMember());
		}
		if (form.getCreateVictim() == null) {
			mav.addObject(CREATE_VICTIM_STATUS, false);
		} else {
			mav.addObject(CREATE_VICTIM_STATUS, form.getCreateVictim());
		}
		if (form.getCreateVisitor() == null) {
			mav.addObject(CREATE_VISITOR_STATUS, false);
		} else {
			mav.addObject(CREATE_VISITOR_STATUS, form.getCreateVisitor());
		}
		
		int offenderRelationshipTelephoneNumberIndex
			= form.getTelephoneNumberItems().size();
		int onlineAccountItemIndexValue
			= form.getOnlineAccountContactItems().size();
		int offenderRelationshipNoteIndex
			= form.getNoteItems().size();
		
		mav.addObject(OFFENDER_RELATIONSHIP_TELEPHONE_NUMBER_INDEX_MODEL_KEY, 
			offenderRelationshipTelephoneNumberIndex);
		mav.addObject(OFFENDER_RELATIONSHIP_ONLINE_ACCOUNT_INDEX_MODEL_KEY, 
			onlineAccountItemIndexValue);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_INDEX_MODEL_KEY,
				offenderRelationshipNoteIndex);
		mav.addObject(CREATE_RELATIONSHIPS_FORM_MODEL_KEY, form);
		List<Suffix> nameSuffixes 
			= this.createRelationshipsService.findNameSuffixes();
		List<String> nameSuffixStrings 
			= this.createRelationshipsService.findSuffixNames();
		mav.addObject(NAME_SUFFIXES_MODEL_KEY, nameSuffixes);
		mav.addObject(HOME_TYPES_MODEL_KEY, BuildingCategory.values());
		List<StreetSuffix> streetSuffixes 
			= this.createRelationshipsService.findStreetSuffixes();
		mav.addObject(STREET_SUFFIXES_MODEL_KEY, streetSuffixes);
		List<AddressUnitDesignator> addressUnitDesignators 
			= this.createRelationshipsService.findAddressUnitDesignators();
		mav.addObject(UNIT_DESIGNATORS_MODEL_KEY, addressUnitDesignators);
		List<Country> countries = this.createRelationshipsService
			.findCountries();
		
		mav.addObject(COUNTRIES_MODEL_KEY, countries);
		mav.addObject(BIRTH_COUNTRIES_MODEL_KEY, countries);
		mav.addObject(PO_BOX_COUNTRIES_MODEL_KEY, countries);
		
		List<FamilyAssociationCategory> familyAssociationCategories 
			= this.createRelationshipsService.findFamilyAssociationCategories();
		mav.addObject(FAMILY_ASSOCIATION_CATEGORIES_MODEL_KEY, 
			familyAssociationCategories);
		
		List<VisitationAssociationCategory> visitationAssociationCategories 
			= this.createRelationshipsService
			.findVisitationAssociationCategories();
		mav.addObject(VISITATIION_ASSOCIATION_CATEGORIES_MODEL_KEY, 
			visitationAssociationCategories);
		
		List<OnlineAccountHost> onlineAccountHosts 
			= this.createRelationshipsService.findOnlineAccountHosts();
		/*OffenderSummary offenderSummary = this.offenderReportService
			.summarizeOffender(offender);
		mav.addObject(OFFENDER_SUMMARY_MODEL_KEY, offenderSummary);*/
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(RELATION_MODEL_KEY, relation);
		boolean newRelation = false;
		if (relation == null) {
			newRelation = true;
		} else {
			newRelation = false;
		}
		mav.addObject(NEW_RELATION_MODEL_KEY, newRelation);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		
		ModelMap map = mav.getModelMap();
		if (relation != null) {
			ContactSummary existingContactSummary = this.contactReportService
					.summarizeByPerson(relation);
				map.addAttribute(EXISTING_CONTACT_SUMMARY_MODEL_KEY,
						existingContactSummary);
		}
		map.addAttribute(TELEPHONE_NUMBER_CATEGORY_MODEL_KEY,
				TelephoneNumberCategory.values());
		AddressFields addressFields = form.getAddressFields();
		if (addressFields != null) {
			if (addressFields.getCity() != null) {
				if (this.createRelationshipsService.hasStates(
					addressFields.getCountry())) {
					if (addressFields.getState() != null) {
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(
							map, countries, 
							this.createRelationshipsService.findStates(
							addressFields.getCountry()), 
							this.createRelationshipsService.findCitiesByState(
							addressFields.getState()), 
							this.createRelationshipsService.findZipCodes(
							addressFields.getCity()), 
							ADDRESS_FIELDS_PROPERTY_NAME); 
					} else {
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(
							map, countries, this.createRelationshipsService
							.findStates(addressFields.getCountry()), 
							this.createRelationshipsService
							.findCitiesByCountryWithoutState(
							addressFields.getCountry()), 
							this.createRelationshipsService.findZipCodes(
							addressFields.getCity()), 
							ADDRESS_FIELDS_PROPERTY_NAME);
					}
				} else {
					// No state
					this.addressFieldsControllerDelegate
						.prepareEditAddressFields(
						map, countries, null, 
						this.createRelationshipsService.findCitiesByCountry(
							addressFields.getCity().getCountry()), 
						this.createRelationshipsService.findZipCodes(
							addressFields.getCity()), 
						ADDRESS_FIELDS_PROPERTY_NAME); 
				}
			} else {
				// no city
				if (form.getAddressFields().getCountry() != null) {
					if (this.createRelationshipsService.hasStates(
						addressFields.getCountry())) {
						if (addressFields.getState() != null) {
							this.addressFieldsControllerDelegate
								.prepareEditAddressFields(map, countries, 
								this.createRelationshipsService.findStates(
								addressFields.getCountry()), 
								this.createRelationshipsService
								.findCitiesByState(
								addressFields.getState()), 
								null, ADDRESS_FIELDS_PROPERTY_NAME);
					    } else {
					    	// No state
					    	this.addressFieldsControllerDelegate
						    	.prepareEditAddressFields(map, countries, 
								this.createRelationshipsService.findStates(
								addressFields.getCountry()), 
								this.createRelationshipsService
								.findCitiesByCountryWithoutState(
								addressFields.getCountry()),
								null, ADDRESS_FIELDS_PROPERTY_NAME);
					    }
				    } else {
				    	// No country
				    	this.addressFieldsControllerDelegate
				    		.prepareEditAddressFields(
				    		map, countries, null, 
				    		this.createRelationshipsService.findCitiesByCountry(
							addressFields.getCountry()), null, 
							ADDRESS_FIELDS_PROPERTY_NAME);
				    }
			    } else {
			    	// No country
			    	List<State> states 
						= this.createRelationshipsService.findStates(null);
					List<City> cities 
						= this.createRelationshipsService
						.findCitiesByState(null);
					List<ZipCode> zipCodes 
						= this.createRelationshipsService.findZipCodes(null);
					this.addressFieldsControllerDelegate
						.prepareEditAddressFields(map, 
						countries, states, cities, zipCodes, 
						ADDRESS_FIELDS_PROPERTY_NAME);
			    }
		    }
		} else {
			List<State> states 
				= this.createRelationshipsService.findStates(null);
			List<City> cities 
				= this.createRelationshipsService.findCitiesByState(null);
			List<ZipCode> zipCodes 
				= this.createRelationshipsService.findZipCodes(null);
			this.addressFieldsControllerDelegate.prepareEditAddressFields(map, 
				countries, states, cities, zipCodes, 
				ADDRESS_FIELDS_PROPERTY_NAME);
		}
		
		if (form.getPoBoxFields() != null) {
			PoBoxFields poBoxFields = form.getPoBoxFields();
//			poBoxFields.setNewZipCode(false);
			if (poBoxFields != null) {
				if (poBoxFields.getCity() != null) {
					if (this.createRelationshipsService.hasStates(
						poBoxFields.getCountry())) {
						if (poBoxFields.getState() != null) {
							this.poBoxFieldsControllerDelegate
								.prepareEditPoBoxFields(map, countries, 
								this.createRelationshipsService.findStates(
								poBoxFields.getCountry()), 
								this.createRelationshipsService
								.findCitiesByState(
								poBoxFields.getCity().getState()), 
								this.createRelationshipsService.findZipCodes(
								poBoxFields.getCity()), 
								PO_BOX_FIELDS_PROPERTY_NAME); 
						} else {
							this.poBoxFieldsControllerDelegate
								.prepareEditPoBoxFields(map, countries, 
								this.createRelationshipsService.findStates(
								poBoxFields.getCountry()), 
								this.createRelationshipsService
								.findCitiesByCountryWithoutState(
								poBoxFields.getCountry()), 
								this.createRelationshipsService.findZipCodes(
								poBoxFields.getCity()), 
								PO_BOX_FIELDS_PROPERTY_NAME);
						}
					} else {
						// No state
						this.poBoxFieldsControllerDelegate
							.prepareEditPoBoxFields(
							map, countries, null, 
							this.createRelationshipsService
								.findCitiesByCountry(
								poBoxFields.getCity().getCountry()), 
							this.createRelationshipsService.findZipCodes(
								poBoxFields.getCity()), 
							PO_BOX_FIELDS_PROPERTY_NAME); 
					}
				} else {
					// no city -> no zip code
					if (poBoxFields.getCountry() != null) {
						if (poBoxFields.getHasStates()) {
							if (poBoxFields.getState() != null) {
								this.poBoxFieldsControllerDelegate
									.prepareEditPoBoxFields(map, countries, 
									this.createRelationshipsService.findStates(
									poBoxFields.getCountry()), 
									this.createRelationshipsService
									.findCitiesByState(
									poBoxFields.getState()), null, 
									PO_BOX_FIELDS_PROPERTY_NAME);
							} else {
								this.poBoxFieldsControllerDelegate
									.prepareEditPoBoxFields(map, countries, 
									this.createRelationshipsService.findStates(
									poBoxFields.getCountry()),
									this.createRelationshipsService
									.findCitiesByCountryWithoutState(
									poBoxFields.getCountry()), null, 
									PO_BOX_FIELDS_PROPERTY_NAME);
							}
						} else {
							// No state
							this.poBoxFieldsControllerDelegate
								.prepareEditPoBoxFields(map, countries, null, 
								this.createRelationshipsService
								.findCitiesByCountry(
								poBoxFields.getCountry()),	null, 
								PO_BOX_FIELDS_PROPERTY_NAME);
						}
					} else {
						// No country
						this.poBoxFieldsControllerDelegate
							.prepareEditPoBoxFields(map, countries, null, null, 
							null, PO_BOX_FIELDS_PROPERTY_NAME);
					}
				}
			}
		} else {
			List<State> states 
				= this.createRelationshipsService.findStates(null);
			List<City> cities 
				= this.createRelationshipsService.findCitiesByState(null);
			List<ZipCode> zipCodes 
				= this.createRelationshipsService.findZipCodes(null);
			this.poBoxFieldsControllerDelegate.prepareEditPoBoxFields(map, 
				countries, states, cities, zipCodes, 
				PO_BOX_FIELDS_PROPERTY_NAME);
		}
		
		if (form.getPersonFields() != null) {
			PersonFields personFields = form.getPersonFields();
			if (personFields != null) {
				if (personFields.getBirthCity() != null) {
					if (this.createRelationshipsService.hasStates(
						personFields.getBirthCountry())) {
						if (personFields.getBirthState() != null) {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, nameSuffixStrings,
										countries, 
								this.createRelationshipsService.findStates(
									personFields.getBirthCountry()),
								this.createRelationshipsService
								.findCitiesByState(
								personFields.getBirthState()),
								PERSON_FIELDS_PROPERTY_NAME);
						} else {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, nameSuffixStrings,
								countries, 
								this.createRelationshipsService.findStates(
									personFields.getBirthCountry()),
								this.createRelationshipsService
								.findCitiesByCountryWithoutState(
								personFields.getBirthCountry()),
								PERSON_FIELDS_PROPERTY_NAME);
						}
					} else {
						// No state
						this.personFieldsControllerDelegate
							.prepareEditPersonFields(map, nameSuffixStrings, 
							countries, 
							null, 
							this.createRelationshipsService
							.findCitiesByCountry(personFields
							.getBirthCountry()), 
							PERSON_FIELDS_PROPERTY_NAME);   
					}
				} else {
					// no city
					if (personFields.getBirthCountry() != null) {
						if	(this.createRelationshipsService.hasStates(
							personFields.getBirthCountry())) {
							if (personFields.getBirthState() != null) {
								this.personFieldsControllerDelegate.
									prepareEditPersonFields(map, 
									nameSuffixStrings, 
									countries, 
									this.createRelationshipsService.findStates(
										personFields.getBirthCountry()), 
									this.createRelationshipsService
									.findCitiesByState(
									personFields.getBirthState()),
										PERSON_FIELDS_PROPERTY_NAME);
							} else {
								this.personFieldsControllerDelegate.
									prepareEditPersonFields(map, 
									nameSuffixStrings, 
									countries, 
									this.createRelationshipsService
									.findStates(personFields
										.getBirthCountry()), 
									this.createRelationshipsService
									.findCitiesByCountryWithoutState(
									personFields.getBirthCountry()),
									PERSON_FIELDS_PROPERTY_NAME);
							}
						} else {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, nameSuffixStrings,
									countries, 
									null, this.createRelationshipsService
									.findCitiesByCountry(personFields
									.getBirthCountry()), 
									PERSON_FIELDS_PROPERTY_NAME);
						}
					} else {
						this.personFieldsControllerDelegate
							.prepareEditPersonFields(map, nameSuffixStrings, 
							countries, 
							null, null, PERSON_FIELDS_PROPERTY_NAME);
					}
				}
			}
		} else {
			List<State> states 
				= this.createRelationshipsService.findStates(null);
			List<City> cities 
				= this.createRelationshipsService.findCitiesByState(null);
			this.personFieldsControllerDelegate.prepareEditPersonFields(map, 
				nameSuffixStrings, countries, states, cities, 
				PERSON_FIELDS_PROPERTY_NAME);
		}
		
		this.onlineAccountFieldsControllerDelegate
			.prepareEditOnlineAccountFields(map, onlineAccountHosts);
		this.telephoneNumberFieldsControllerDelegate
			.prepareEditTelephoneNumberFields(map);
		return new ModelAndView(CREATE_VIEW_NAME, map);
	}
	
	// Prepares redisplay edit model and view
	private ModelAndView prepareRedisplayEditMav(
		final CreateRelationshipsForm createRelationshipsForm,
		final Offender offender,
		final Person relation,
		final BindingResult result) {
		ModelAndView mav = this.prepareCreateMav(createRelationshipsForm, 
			offender, relation); 
		List<RelationshipNoteCategory> noteCategories
			= this.createRelationshipsService.findDesignatedNoteCategories();
		mav.addObject(NOTE_CATEGORIES_MODEL_KEY, noteCategories);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ CREATE_RELATIONSHIPS_FORM_MODEL_KEY, result);
		return mav;
	}
	
	/**
	 * Returns the state options view with a collections of state for the
	 * specified country for person, address and po box fields snippet.
	 * 
	 * @param country country
	 * @param fieldsName fields name
	 * @return model and view to show state options
	 */
	@RequestMapping(value = "/{fieldsName}/findStateOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showStateOptions(
			@PathVariable(value = "fieldsName")
				final String fieldsName,
			@RequestParam(value = "country", required = true)
				final Country country) {
		List<State> states 
			= this.createRelationshipsService.findStates(country);
		if (PERSON_FIELDS_NAME.equals(fieldsName)) {
			return this.personFieldsControllerDelegate
			.showStateOptions(states, PERSON_FIELDS_NAME);
		} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate
			.showStateOptions(states, ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poBoxFieldsControllerDelegate
			.showStateOptions(states, PO_BOX_FIELDS_NAME);
		} else {
			throw new UnsupportedOperationException(
				String.format("Fields name not supported %s", fieldsName));
		}
	}

	/**
	 * Returns the city options view with a collection of cities for the
	 * specified state for person, address and po box fields snippet.
	 * 
	 * @param state state
	 * @param country country
	 * @param fieldsName fields name
	 * @return model and view to show city options
	 */
	@RequestMapping(value = "/{fieldsName}/findCityOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showCityOptions(
		@PathVariable(value = "fieldsName")
			final String fieldsName,
		@RequestParam(value = "state", required = false)
			final State state,
		@RequestParam(value = "country", required = true)
		final Country country) {
		if (state != null) {
			if (PERSON_FIELDS_NAME.equals(fieldsName)) {
				return this.personFieldsControllerDelegate.showCityOptions(
				this.createRelationshipsService.findCitiesByState(state), 
				PERSON_FIELDS_NAME);
			} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
				return this.addressFieldsControllerDelegate.showCityOptions(
				this.createRelationshipsService.findCitiesByState(state),
				ADDRESS_FIELDS_NAME);
			} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
				return this.poBoxFieldsControllerDelegate.showCityOptions(
				this.createRelationshipsService.findCitiesByState(state),
				PO_BOX_FIELDS_NAME);
			} else {
				throw new UnsupportedOperationException(
					String.format("Fields name not supported %s", fieldsName));
			}
		} else {
			if (this.createRelationshipsService.hasStates(country)) {
				if (PERSON_FIELDS_NAME.equals(fieldsName)) {
					return this.personFieldsControllerDelegate.showCityOptions(
					this.createRelationshipsService
					.findCitiesByCountryWithoutState(country), 
					PERSON_FIELDS_NAME);
				} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
					return this.addressFieldsControllerDelegate.showCityOptions(
					this.createRelationshipsService
					.findCitiesByCountryWithoutState(country),
					ADDRESS_FIELDS_NAME);
				} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
					return this.poBoxFieldsControllerDelegate.showCityOptions(
					this.createRelationshipsService
					.findCitiesByCountryWithoutState(country),
					PO_BOX_FIELDS_NAME);
				} else {
					throw new UnsupportedOperationException(
						String.format("Fields name not supported %s",
						fieldsName));
				}
			} else {
				if (PERSON_FIELDS_NAME.equals(fieldsName)) {
					return this.personFieldsControllerDelegate.showCityOptions(
					this.createRelationshipsService.findCitiesByCountry(
					country), PERSON_FIELDS_NAME);
				} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
					return this.addressFieldsControllerDelegate.showCityOptions(
					this.createRelationshipsService.findCitiesByCountry(
					country), ADDRESS_FIELDS_NAME);
				} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
					return this.poBoxFieldsControllerDelegate.showCityOptions(
					this.createRelationshipsService.findCitiesByCountry(
					country), PO_BOX_FIELDS_NAME);
				} else {
					throw new UnsupportedOperationException(
						String.format("Fields name not supported %s",
						fieldsName));
				}
			}
		}
	}

	/**
	 * Returns the zip code options view with a collection of zip codes for the
	 * specified city for address, poBox field snippet.
	 * 
	 * @param city city
	 * @param fieldsName fields name
	 * @return model and view to show zip code options
	 */
	@RequestMapping(value = "/{fieldsName}/findZipCodeOptions.html", 
		method = RequestMethod.GET)
	public ModelAndView showZipCodeOptions(
		@PathVariable(value = "fieldsName")
			final String fieldsName,
		@RequestParam(value = "city", required = true)
			final City city) {
		if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate.showZipCodeOptions(
			this.createRelationshipsService.findZipCodes(city),
			ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poBoxFieldsControllerDelegate.showZipCodeOptions(
			this.createRelationshipsService.findZipCodes(city),
			PO_BOX_FIELDS_NAME);
		} else {
			throw new UnsupportedOperationException(
				String.format("Fields name not supported %s",
				fieldsName));
		}
	}
	
	/**
	 * Displays the action menu for the specified offender's relationships 
	 * create screen.
	 * 
	 * @param offender offender
	 * @return model and view for offender relationships action menu
	 */
	@RequestMapping(value = "offenderRelationCreateActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayOffenderRelationshipsCreateActionMenu(
		@RequestParam(value = "offender", required = true)
		final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(OFFENDER_RELATION_EDIT_ACTION_MENU_VIEW_NAME,
			map);
	}
	
	/**
	 * Displays the action menu for the specified offender's relationships 
	 * list screen.
	 * 
	 * @param offender offender
	 * @return model and view for offender relationships action menu
	 */
	@RequestMapping(value = "offenderRelationListActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayOffenderRelationshipsListActionMenu(
		@RequestParam(value = "offender", required = true)
		final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(OFFENDER_RELATION_LIST_ACTION_MENU_VIEW_NAME,
			map);
	}
	
	/**
	 * Displays action menu for relationship note items.
	 * 
	 * @return action menu for relationship note items
	 */
	@RequestMapping(value = "/create/noteItemsActionMenu.html")
	public ModelAndView displayOffenderRelationshipNoteItemsActionMenu() {
		return new ModelAndView(
				OFFENDER_RELATION_NOTE_ITEMS_ACTION_MENU_VIEW_NAME);
	}
	
	/**
	 * Returns a view for telephone number action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for telephone number action menu
	 */
	@RequestMapping(value = "/telephoneNumberCreateActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView telephoneNumberActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(TELEPHONE_NUMBERS_ACTION_MENU_VIEW_NAME, map);
	}	
	
	/**
	 * Adds a telephone number item.
	 * 
	 * @param telephoneNumberItemIndex telephone number item index
	 * @return model and view for a telephone number item view
	 */
	@RequestMapping(
		value = "/addCreateOffenderRelationshipTelephoneNumberItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addTelephoneNumberItem(@RequestParam(
		value = "telephoneNumberItemIndex", required = true)
		final int telephoneNumberItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY, 
			telephoneNumberItemIndex);
		map.addAttribute(TELEPHONE_NUMBER_CATEGORY_MODEL_KEY, 
			TelephoneNumberCategory.values());
		TelephoneNumberItem telephoneNumberItem = new TelephoneNumberItem();
		telephoneNumberItem.setOperation(TelephoneNumberItemOperation.CREATE);
		map.addAttribute(TELEPHONE_NUMBER_ITEM_MODEL_KEY, telephoneNumberItem);
		return new ModelAndView(CREATE_TELEPHONE_NUMBER_TABLE_ROW_VIEW_NAME, 
			map);
	}
	
	/**
	 * Adds an online account item.
	 * 
	 * @param onlineAccountItemIndex online account item index
	 * @return model and view for an email item view
	 */
	@RequestMapping(
			value = "/addCreateOffenderRelationshipOnlineAccountItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addEmailItem(@RequestParam(
		value = "onlineAccountItemIndex", required = true)
		final int onlineAccountItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY, 
			onlineAccountItemIndex); 
		List<OnlineAccountHost> onlineAccountHosts 
			= this.createRelationshipsService.findOnlineAccountHosts();
		map.addAttribute(ONLINE_ACCOUNT_HOSTS_MODEL_KEY, onlineAccountHosts);
		OnlineAccountContactItem onlineAccountContactItem 
			= new OnlineAccountContactItem(); 
		onlineAccountContactItem.setOperation(
			OnlineAccountContactItemOperation.CREATE);
		map.addAttribute(ONLINE_ACCOUNT_CONTACT_ITEM_MODEL_KEY, 
			onlineAccountContactItem);
		return new ModelAndView(CREATE_ONLINE_ACCOUNT_TABLE_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Adds offender relationship note item.
	 * 
	 * @param offenderRelationshipNoteItemIndex item index of offender
	 * relationship note
	 * @return offender relationship note item
	 */
	@RequestMapping(
			value = "/create/createRelationshipNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView createRelationshipNoteItem(
			@RequestParam(
					value = "offenderRelationshipNoteItemIndex",
					required = true)
				final Integer offenderRelationshipNoteItemIndex) {
		List<RelationshipNoteCategory> noteCategories
			= this.createRelationshipsService.findDesignatedNoteCategories();
		OffenderRelationshipNoteItem offenderRelationshipNoteItem
			= new OffenderRelationshipNoteItem();
		offenderRelationshipNoteItem.setOperation(
				OffenderRelationshipNoteItemOperation.CREATE);
		ModelAndView mav = new ModelAndView(
				CREATE_RELATIONSHIP_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_INDEX_MODEL_KEY,
				offenderRelationshipNoteItemIndex);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_ITEM_MODEL_KEY,
				offenderRelationshipNoteItem);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME_MODEL_KEY,
				OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME);
		mav.addObject(BASE_URL_MODEL_KEY, BASE_URL);
		mav.addObject(NOTE_CATEGORIES_MODEL_KEY, noteCategories);
		return mav;
	}
	
	/**
	 * Returns a view for online account action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for online account action menu
	 */
	@RequestMapping(value = "/onlineAccountContactEditActionMenu.html",
		method = RequestMethod.GET)
	public ModelAndView onlineAccountContactActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(EMAILS_ACTION_MENU_VIEW_NAME, map);
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
		 	final String searchCriteria) {
		List<Address> addresses;
		if (StringUtility.hasContent(searchCriteria)) {
			addresses = this.createRelationshipsService
				.findAddresses(searchCriteria);  
		} else {
			addresses = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(ADDRESSES_VIEW_NAME);
		mav.addObject(ADDRESSES_MODEL_KEY, addresses);
		return mav;
	}
	
	/**
	 * Return if the marriage and divorce dates should be displayed.
	 * 
	 * @param category category
	 * @return spouse
	 */
	@RequestMapping(value = "/displayMarriageDivorceDate.json", 
		method = RequestMethod.GET)
	@ResponseBody public Boolean displayMarriageDivorceDate(
		@RequestParam(value = "category", required = true)
			final FamilyAssociationCategory category) {
		Boolean spouse;
		if (category.getClassification()
			.equals(FamilyAssociationCategoryClassification.SPOUSE)) {
			spouse = true;
		} else {
			spouse = false;
		}
		return spouse;
	}
	
	/**
	 * Handles {@code AddressExistsException}.
	 * 
	 * @param addressExistsException address exists exception thrown
	 * @return screen to handle {@code AddressExistsException}
	 */
	@ExceptionHandler(AddressExistsException.class)
	public ModelAndView handleAddressExistsException(
		final AddressExistsException addressExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			ADDRESS_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, addressExistsException);
	}
	
	/**
	 * Handles {@code VictimExistsException}.
	 * 
	 * @param victimExistsException victim exists exception thrown
	 * @return screen to handle {@code VictimExistsException}
	 */
	@ExceptionHandler(VictimExistsException.class)
	public ModelAndView handleVictimExistsException(
		final VictimExistsException victimExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			VICTIM_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, victimExistsException);
	}
	
	/**
	 * Handles {@code VisitationExistsException}.
	 * 
	 * @param visitationExistsException visitation exists exception thrown
	 * @return screen to handle {@code VisitationExistsException}
	 */
	@ExceptionHandler(VisitationExistsException.class)
	public ModelAndView handleVisitationExistsException(
		final VisitationExistsException visitationExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			VISITATION_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, visitationExistsException);
	}
	
	/**
	 * Handles {@code TelephoneNumberExistsException}.
	 * 
	 * @param telephoneNumberExistsException telephone number exists exception
	 * thrown
	 * @return screen to handle {@code TelephoneNumberExistsException}
	 */
	@ExceptionHandler(TelephoneNumberExistsException.class)
	public ModelAndView handleTelephoneNumberExistsException(
		final TelephoneNumberExistsException telephoneNumberExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			TELEPHONE_NUMBER_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, telephoneNumberExistsException);
	}
	
	/**
	 * Handles {@code OnlineAccountExistsException}.
	 * 
	 * @param onlineAccountExistsException online account exists exception
	 * thrown
	 * @return screen to handle {@code OnlineAccountExistsException}
	 */
	@ExceptionHandler(OnlineAccountExistsException.class)
	public ModelAndView handleOnlineAccountExistsException(
		final OnlineAccountExistsException onlineAccountExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			ONLINE_ACCOUNT_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, onlineAccountExistsException);
	}
	
	/**
	 * Handles {@code FamilyAssociationConflictException}.
	 * 
	 * @param familyAssociationConflictException family association Conflict
	 * exception thrown
	 * @return screen to handle {@code FamilyAssociationConflictException}
	 */
	@ExceptionHandler(FamilyAssociationConflictException.class)
	public ModelAndView handleFamilyAssociationConflictException(
		final FamilyAssociationConflictException
		familyAssociationConflictException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			FAMILY_ASSOCIATION_CONFLICT_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, familyAssociationConflictException);
	}
	
	/**
	 * Handles {@code ZipCodeExistsException}.
	 * 
	 * @param zipCodeExistsException Zip code exists exception thrown
	 * @return screen to handle {@code ZipCodeExistsException}
	 */
	@ExceptionHandler(ZipCodeExistsException.class)
	public ModelAndView handleZipCodeExistsException(
		final ZipCodeExistsException zipCodeExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			ZIPCODE_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, zipCodeExistsException);
	}
	
	/**
	 * Handles {@code ContactExistsException}.
	 * 
	 * @param contactExistsException contact exists exception thrown
	 * @return screen to handle {@code ContactExistsException}
	 */
	@ExceptionHandler(ContactExistsException.class)
	public ModelAndView handleContactExistsException(
		final ContactExistsException contactExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			CONTACT_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, 	contactExistsException);
	}
	
	/**
	 * Handles {@code PersonExistsException}.
	 * 
	 * @param personExistsException person exists exception thrown
	 * @return screen to handle {@code PersonExistsException}
	 */
	@ExceptionHandler(PersonExistsException.class)
	public ModelAndView handlePersonExistsException(
		final PersonExistsException personExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			PERSON_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, 	personExistsException);
	}
	
	/**
	 * Handles {@code CityExistsException}.
	 * 
	 * @param cityExistsException city exists exception
	 * @return screen to handle {@code CityExistsException}
	 */
	@ExceptionHandler(CityExistsException.class)
	public ModelAndView handleCityExistsException(
		final CityExistsException cityExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			CITY_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, cityExistsException);
	}
	
	/**
	 * Handles {@code RelationshipNoteExistsException}.
	 * 
	 * @param relationshipNoteExistsException relationship note exists exception
	 * @return screen to handle {@code RelationshipNoteExistsException}
	 */
	@ExceptionHandler(RelationshipNoteExistsException.class)
	public ModelAndView handleRelationshipNoteExistsException(
		final RelationshipNoteExistsException relationshipNoteExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			NOTE_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, relationshipNoteExistsException);
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
			this.offenderPropertyEditorFactory
			.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class, 
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Person.class, 
			this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Address.class, 
			this.addressPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class, 
			this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class, 
			this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class, 
			this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class, 
			this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(AddressUnitDesignator.class,
			this.addressUnitDesignatorPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(StreetSuffix.class, 
			this.streetSuffixPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(FamilyAssociationCategory.class, 
			this.familyAssociationCategoryPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(VisitationAssociationCategory.class, 
			this.visitationAssociationCategoryPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccountHost.class,
			this.onlineAccountHostPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TelephoneNumber.class,
			this.telephoneNumberPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccount.class,
			this.onlineAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(RelationshipNote.class,
				this.relationshipNotePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(RelationshipNoteCategory.class,
			this.relationshipNoteCategoryPropertyEditorFactory
				.createPropertyEditor());
	}
	
	// Returns true if value is true; false otherwise
	private Boolean resolveCheckBoxValue(final Boolean value) {
		if (value != null) {
			return value;
		} else {
			return false;
		}
	}
}