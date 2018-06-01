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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import omis.contact.web.controller.delegate.OnlineAccountFieldsControllerDelegate;
import omis.contact.web.controller.delegate.PoBoxFieldsControllerDelegate;
import omis.contact.web.controller.delegate.TelephoneNumberFieldsControllerDelegate;
import omis.contact.web.form.OnlineAccountContactItemOperation;
import omis.contact.web.form.OnlineAccountFields;
import omis.contact.web.form.PoBoxFields;
import omis.contact.web.form.TelephoneNumberFields;
import omis.contact.web.form.TelephoneNumberItemOperation;
import omis.country.domain.Country;
import omis.family.domain.FamilyAssociation;
import omis.family.exception.FamilyAssociationConflictException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.offenderrelationship.service.UpdateOffenderRelationService;
import omis.offenderrelationship.web.controller.delegate.OffenderRelationshipNoteFieldsControllerDelegate;
import omis.offenderrelationship.web.form.EditRelationshipsForm;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteFields;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.offenderrelationship.web.form.OnlineAccountContactItem;
import omis.offenderrelationship.web.form.TelephoneNumberItem;
import omis.offenderrelationship.web.validator.EditRelationshipsFormValidator;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.exception.PersonExistsException;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.person.web.delegate.PersonFieldsControllerDelegate;
import omis.person.web.form.PersonFields;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.util.StringUtility;
import omis.victim.exception.VictimExistsException;
import omis.visitation.exception.VisitationExistsException;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Edit offender relationship controller.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.1.1 (Nov 21, 2017)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/offenderRelationship/update")
@PreAuthorize("hasRole('USER')")
public class UpdateOffenderRelationController {
	/* View names. */
	private static final String EDIT_VIEW_NAME = "offenderRelationship/edit";
	private static final String OFFENDER_RELATION_LIST_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/offenderRelationListActionMenu";
	private static final String OFFENDER_RELATION_EDIT_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/update/offenderRelationEditActionMenu";
	private static final String ADDRESSES_VIEW_NAME
		= "address/json/addresses";
	private static final String TELEPHONE_NUMBERS_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/update/"
				+ "offenderRelationshipTelephoneNumbersActionMenu";
	private static final String EMAILS_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/update/"
				+ "offenderRelationshipEmailActionMenu";
	private static final String NOTES_ACTION_MENU_VIEW_NAME
		= "offenderRelationship/includes/update/"
				+ "offenderRelationshipNoteItemsActionMenu";
	private static final String EDIT_TELEPHONE_NUMBER_TABLE_ROW_VIEW_NAME 
		= "offenderRelationship/includes/update/editTelephoneNumberTableRow";
	private static final String EDIT_ONLINE_ACCOUNT_TABLE_ROW_VIEW_NAME 
		= "offenderRelationship/includes/update/editOnlineAccountTableRow";
	private static final String CREATE_RELATIONSHIP_NOTE_TABLE_ROW_VIEW_NAME
		= "offenderRelationship/includes/offenderRelationshipNoteItemTableRow";
	
	/* Model keys. */
	private static final String EDIT_RELATIONSHIPS_FORM_MODEL_KEY
		= "editRelationshipsForm";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String OFFENDER_SUMMARY_MODEL_KEY = "offenderSummary";
	private static final String NAME_SUFFIXES_MODEL_KEY = "nameSuffixes";
	private static final String COUNTRIES_MODEL_KEY = "countries";
	private static final String HOME_TYPES_MODEL_KEY = "homeTypes";
	private static final String STREET_SUFFIXES_MODEL_KEY = "streetSuffixes";
	private static final String UNIT_DESIGNATORS_MODEL_KEY = "unitDesignators";
	private static final String RELATION_MODEL_KEY = "relation";
	private static final String 
		OFFENDER_RELATIONSHIP_TELEPHONE_NUMBER_INDEX_MODEL_KEY 
		= "offenderRelationshipTelephoneNumberIndex";
	private static final String 
		OFFENDER_RELATIONSHIP_ONLINE_ACCOUNT_INDEX_MODEL_KEY 
		= "offenderRelationshipOnlineAccountIndex";
	private static final String
		OFFENDER_RELATIONSHIP_NOTE_ITEM_INDEX_MODEL_KEY
		= "offenderRelationshipNoteItemIndex";
	private static final String TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY 
		= "telephoneNumberIndex";
	private static final String TELEPHONE_NUMBER_CATEGORY_MODEL_KEY
		= "telephoneNumberCategories";
	private static final String TELEPHONE_NUMBER_ITEM_MODEL_KEY
		= "telephoneNumberItem";
	private static final String ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY 
		= "onlineAccountIndex";
	private static final String ONLINE_ACCOUNT_HOSTS_MODEL_KEY 
		= "onlineAccountHosts";
	private static final String ONLINE_ACCOUNT_CONTACT_ITEM_MODEL_KEY
		= "onlineAccountContactItem";
	private static final String ADDRESSES_MODEL_KEY
		= "addresses";
	private static final String BIRTH_COUNTRIES_MODEL_KEY = "birthCountries";
	private static final String PO_BOX_COUNTRIES_MODEL_KEY = "poBoxCountries";
	private static final String RELATIONSHIP_MODEL_KEY = "relationship";
	private static final String EXISTING_ADDRESS_MODEL_KEY
		= "existingAddress";
	private static final String BASE_URL_MODEL_KEY = "baseUrl";
	private static final String OFFENDER_RELATIONSHIP_NOTE_ITEM_MODEL_KEY
		= "offenderRelationshipNoteItem";
	private static final String
	OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME_MODEL_KEY
		= "offenderRelationshipNoteItemsFieldName";
	
	/* Message keys. */
	private static final String PERSON_NAME_EXISTS_EXCEPTION_MESSAGE_KEY
		= "personName.Conflicts";
	private static final String PERSON_IDENTITY_EXISTS_EXCEPTION_MESSAGE_KEY
		= "personIdentity.Conflicts";
	private static final String ADDRESS_EXISTS_EXCEPTION_MESSAGE_KEY
		= "address.Conflicts";
	private static final String CITY_EXISTS_EXCEPTION_MESSAGE_KEY
		= "city.Conflicts";
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
	private static final String NOTE_EXISTS_EXCEPTION_MESSAGE_KEY
		= "note.Conflicts";
	
	/* Property names. */
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = "addressFields";
	private static final String PO_BOX_FIELDS_PROPERTY_NAME = "poBoxFields";
	private static final String PERSON_FIELDS_PROPERTY_NAME = "personFields";
	
	/* Report names. */
	private static final String FAMILY_ASSOCIATION_DETAILS_REPORT_NAME 
		= "/Relationships/Family/Family_Details_Redacted";
	
	/* Fields names. */
	private static final String PERSON_FIELDS_NAME = "personFields";
	private static final String ADDRESS_FIELDS_NAME
		= "addressFields";
	private static final String PO_BOX_FIELDS_NAME = "poBoxFields";
	private static final String OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME
		= "noteItems";
	
	/* Redirects. */
	private static final String LIST_REDIRECT
		= "redirect:/offenderRelationship/list.html?offender=%d";
	
	/* URLs. */
	
	private static final String BASE_URL = "/offenderRelationship/update";
	
	/* Report parameter names. */
	private static final String FAMILY_MEMBER_DETAILS_ID_REPORT_PARAM_NAME 
		= "FAMILY_ASSOC_ID";
	
	/* Message bundles. */
	private static final String ERROR_BUNDLE_NAME
		= "omis.offenderrelationship.msgs.form";
	
	/* Report runners. */
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* PropertyEditors. */
	@Autowired
	@Qualifier("relationshipPropertyEditorFactory")
	private PropertyEditorFactory relationshipPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
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
	@Qualifier("onlineAccountHostPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountHostPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;
	
	@Autowired
	@Qualifier("telephoneNumberPropertyEditorFactory")
	private PropertyEditorFactory telephoneNumberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("onlineAccountPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("relationshipNotePropertyEditorFactory")
	private PropertyEditorFactory relationshipNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("relationshipNoteCategoryPropertyEditorFactory")
	private PropertyEditorFactory relationshipNoteCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("familyAssociationPropertyEditorFactory")
	private PropertyEditorFactory familyAssociationPropertyEditorFactory;
		
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
	
	/* Services. */
	@Autowired
	@Qualifier("updateOffenderRelationService")
	private UpdateOffenderRelationService updateOffenderRelationService;
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Validator */
	@Autowired
	@Qualifier("editRelationshipsFormValidator")
	private EditRelationshipsFormValidator editRelationshipsFormValidator;
	
	/* Helper */
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderRelationshipNoteFieldsControllerDelegate")
	private OffenderRelationshipNoteFieldsControllerDelegate
	offenderRelationshipNoteFieldsControllerDelegate;
	
	/**
	 * Instantiates a default instance of offender relationship controller.
	 */
	public UpdateOffenderRelationController() {
		//Default constructor
	}
	
	/**
	 * Save the edit screen for offender relationship.
	 * 
	 * @param result result
	 * @param relationship relationship
	 * @param editRelationshipsForm edit relationship form
	 * @return model and view to redirect to list screen
	 * @throws AddressExistsException address exists exception
	 * @throws ZipCodeExistsException zipCode exists exception
	 * @throws PersonNameExistsException person name exists exception
	 * @throws PersonIdentityExistsException person identity exists exception
	 * @throws CityExistsException city exists exception
	 * @throws RelationshipNoteExistsException relationship note exists
	 * exception
	 * @throws ContactExistsException contact exists exception
	 * @throws TelephoneNumberExistsException telephone number exists exception
	 * @throws OnlineAccountExistsException online account exists exception
	 */
	@RequestMapping(value = "edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFENDER_RELATIONSHIP_EDIT')")
	public ModelAndView edit(
		@RequestParam(value = "relationship", required = true)
		final Relationship relationship,
		final EditRelationshipsForm editRelationshipsForm,
		final BindingResult result) 
				throws AddressExistsException,
				ZipCodeExistsException, PersonNameExistsException,
				PersonIdentityExistsException, CityExistsException,
				RelationshipNoteExistsException, ContactExistsException,
				TelephoneNumberExistsException, OnlineAccountExistsException,
				RelationshipNoteExistsException {
		if (editRelationshipsForm.getPoBoxFields().getCountry() != null) {
			// Has state
			if (this.updateOffenderRelationService.hasStates(
				editRelationshipsForm.getPoBoxFields().getCountry())) {
				editRelationshipsForm.getPoBoxFields().setHasState(true);
			} else {
				editRelationshipsForm.getPoBoxFields().setHasState(false);
			}
		} else {
			editRelationshipsForm.getPoBoxFields().setHasState(true);
		}
		
		this.editRelationshipsFormValidator.validate(editRelationshipsForm, 
			result); 
		if (result.hasErrors()) {
			return this.prepareRedisplayEditMav(editRelationshipsForm, result, 
				relationship);
		}
		
		Boolean personFieldsNewCity = false;
		Boolean addressFieldsNewCity = false;
		Boolean poBoxFieldsNewCity = false;
		if (editRelationshipsForm.getPersonFields().getNewCity()) {
			personFieldsNewCity = true;
		}
		// If checkbox checked
		if (editRelationshipsForm.getEnterAddress() != null
			&& editRelationshipsForm.getEnterAddress()) {
			if (OffenderRelationshipAddressOperation.NEW.equals(
				editRelationshipsForm.getAddressOperation())
				&& editRelationshipsForm.getAddressFields().getNewCity()) {
				addressFieldsNewCity = true;
			}
		}
		// If checkbox checked
		if (editRelationshipsForm.getEnterPoBox() != null
			&& editRelationshipsForm.getEnterPoBox()) {
			if (editRelationshipsForm.getPoBoxFields().getNewCity()) {
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
			personFieldsNewCityName = editRelationshipsForm
				.getPersonFields().getCityName();
			personFieldsCountryName = editRelationshipsForm
				.getPersonFields().getBirthCountry().getName();
			if (editRelationshipsForm.getPersonFields().getBirthState()
				!= null) {
				personFieldsStateName = editRelationshipsForm.getPersonFields()
				.getBirthState().getName();
			} else {
				personFieldsStateName = null;
			}
		} else {
			// person fields not create new city
			personFieldsNewCityName = null;
			if (editRelationshipsForm.getPersonFields().getBirthCountry()
				!= null) {
				personFieldsCountryName = editRelationshipsForm
					.getPersonFields().getBirthCountry().getName();
				if (editRelationshipsForm.getPersonFields().getBirthState()
					!= null) {
					personFieldsStateName = editRelationshipsForm
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
			addressFieldsNewCityName = editRelationshipsForm
				.getAddressFields().getCityName();
			addressFieldsCountryName = editRelationshipsForm
				.getAddressFields().getCountry().getName();
			if (editRelationshipsForm.getAddressFields().getState() != null) {
				addressFieldsStateName
					= editRelationshipsForm.getAddressFields().getState()
					.getName();
			} else {
				addressFieldsStateName = null;
			}
		} else {
			// Address fields not creates new city
			addressFieldsNewCityName = null;
			// checked, existing
			if (editRelationshipsForm.getEnterAddress() != null
				&& editRelationshipsForm.getEnterAddress()
				&& (!OffenderRelationshipAddressOperation.NEW.equals(
					editRelationshipsForm.getAddressOperation()))) {
				if (editRelationshipsForm.getAddressFields().getCountry()
					!= null) {
					addressFieldsCountryName = editRelationshipsForm
						.getAddressFields().getCountry().getName();
					if (editRelationshipsForm.getAddressFields().getState()
						!= null) {
						addressFieldsStateName = editRelationshipsForm
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
			poBoxFieldsNewCityName = editRelationshipsForm.getPoBoxFields()
				.getCityName();
			poBoxFieldsCountryName = editRelationshipsForm.getPoBoxFields()
				.getCountry().getName();
			if (editRelationshipsForm.getPoBoxFields().getState() != null) {
				poBoxFieldsStateName = editRelationshipsForm.getPoBoxFields()
				.getState().getName();
			} else {
				poBoxFieldsStateName = null;
			}
		} else {
			// Po box not creates new city. check box unchecked or check box 
			// checked + "existing"
			poBoxFieldsNewCityName = null;
			if (editRelationshipsForm.getEnterPoBox() != null
				&& editRelationshipsForm.getEnterPoBox()) {
				// checked + "existing"
				if (editRelationshipsForm.getPoBoxFields().getCountry()
					!= null) {
					poBoxFieldsCountryName = editRelationshipsForm
						.getPoBoxFields().getCountry().getName();
					if (editRelationshipsForm.getPoBoxFields().getState()
						!= null) {
						poBoxFieldsStateName = editRelationshipsForm
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
		if (editRelationshipsForm.getEnterAddress() != null
			&& editRelationshipsForm.getEnterAddress()) {
			// Checked
			if (OffenderRelationshipAddressOperation.NEW.equals(
				editRelationshipsForm.getAddressOperation())
				&& editRelationshipsForm
				.getAddressFields().getNewZipCode()) {
				addressFieldsNewZipCode = true;
				addressFieldsZipCodeValue = editRelationshipsForm
					.getAddressFields().getZipCodeValue();
				addressFieldsZipCodeExtension = editRelationshipsForm
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
		if (editRelationshipsForm.getEnterPoBox() != null
			&& editRelationshipsForm.getEnterPoBox()) {
			// checked
			if (editRelationshipsForm.getPoBoxFields().getNewZipCode()) {
				poBoxFieldsNewZipCode = true;
				poBoxFieldsZipCodeValue = editRelationshipsForm.getPoBoxFields()
					.getZipCodeValue();
				poBoxFieldsZipCodeExtension = editRelationshipsForm
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
			
		// All three fields are going to create new cities
		if (personFieldsNewCity && addressFieldsNewCity
			&& poBoxFieldsNewCity) { 
			if ((addressFieldsNewCityName.equals(poBoxFieldsNewCityName))
				&& (addressFieldsNewCityName.equals(personFieldsNewCityName))
				&& (addressFieldsCountryName.equals(poBoxFieldsCountryName))
				&& (addressFieldsCountryName.equals(personFieldsCountryName))
				&& ((addressFieldsStateName != null
				&& personFieldsStateName != null
				&& addressFieldsStateName.equals(personFieldsStateName))
				|| (addressFieldsStateName == null
				&& personFieldsStateName == null))
				&& ((addressFieldsStateName != null
				&& poBoxFieldsStateName != null
				&& addressFieldsStateName.equals(poBoxFieldsStateName))
				|| (addressFieldsStateName == null
				&& poBoxFieldsStateName == null))) {
		// If all new cities in address, po box and person fields are the same
		// Create new city for all fields
				City newCreatedCity = this.updateOffenderRelationService
					.createCity(
					addressFieldsNewCityName, 
					editRelationshipsForm.getAddressFields().getState(),
					editRelationshipsForm.getAddressFields().getCountry());
				newCreatedAddressFieldsCity = newCreatedCity;
				newCreatedPoBoxFieldsCity = newCreatedCity;
				newCreatedPersonFieldsCity = newCreatedCity;
				addressPoBoxFieldsNewCity = true;
			} else if ((addressFieldsNewCityName.equals(poBoxFieldsNewCityName))
				&& (addressFieldsCountryName.equals(poBoxFieldsCountryName))
				&& ((addressFieldsStateName != null
				&& poBoxFieldsStateName != null
				&& addressFieldsStateName.equals(poBoxFieldsStateName))
				|| (addressFieldsStateName == null
				&& poBoxFieldsStateName == null))
				&& ((!addressFieldsNewCityName.equals(personFieldsNewCityName))
				|| (!addressFieldsCountryName.equals(personFieldsCountryName))
				|| ((addressFieldsStateName != null
				&& personFieldsStateName != null
				&& !addressFieldsStateName.equals(personFieldsStateName))
				|| (addressFieldsStateName != null
				&& personFieldsStateName == null)
				|| (addressFieldsStateName == null
				&& personFieldsStateName != null)))) {
				// If poBox and address fields new cities are the same, 
				// but different from person fields
				City newCreatedCity = this.updateOffenderRelationService
				// Create new city for address and poBox fields
					.createCity(
						addressFieldsNewCityName, 
						editRelationshipsForm.getAddressFields().getState(),
						editRelationshipsForm.getAddressFields().getCountry());
				newCreatedAddressFieldsCity = newCreatedCity;
				newCreatedPoBoxFieldsCity = newCreatedCity;
				newCreatedPersonFieldsCity 
					= this.updateOffenderRelationService.createCity(
						personFieldsNewCityName, 
						editRelationshipsForm.getPersonFields().getBirthState(),
						editRelationshipsForm.getPersonFields()
						.getBirthCountry()); 
				addressPoBoxFieldsNewCity = true;
			} else if ((addressFieldsNewCityName.equals(
					personFieldsNewCityName))
				&& (addressFieldsCountryName.equals(personFieldsCountryName))
				&& (addressFieldsStateName != null
				&& personFieldsStateName != null
				&& addressFieldsStateName.equals(personFieldsStateName)
				|| (addressFieldsStateName == null
				&& personFieldsStateName == null))
				&& ((!addressFieldsNewCityName.equals(poBoxFieldsNewCityName))
				|| (!addressFieldsCountryName.equals(poBoxFieldsCountryName))
				|| (addressFieldsStateName != null
				&& poBoxFieldsStateName != null
				&& (!addressFieldsStateName.equals(poBoxFieldsStateName))
				|| (addressFieldsStateName != null
				&& poBoxFieldsStateName == null)
				|| (addressFieldsStateName == null
				&& poBoxFieldsStateName != null)))) {
			// If person and address fields new cities are the same, 
			// but different from po box fields
			// Create new city for address and person fields
				City newCreatedCity = this.updateOffenderRelationService
					.createCity(
						addressFieldsNewCityName, 
						editRelationshipsForm.getAddressFields().getState(),
						editRelationshipsForm.getAddressFields().getCountry());
				newCreatedPoBoxFieldsCity = this.updateOffenderRelationService
				// Create new city for po box fields
					.createCity(
					poBoxFieldsNewCityName, 
					editRelationshipsForm.getPoBoxFields().getState(),
					editRelationshipsForm.getPoBoxFields().getCountry());
				newCreatedAddressFieldsCity = newCreatedCity;
				newCreatedPersonFieldsCity = newCreatedCity;
				addressPoBoxFieldsNewCity = false;
			} else if ((personFieldsNewCityName.equals(poBoxFieldsNewCityName))
				&& (personFieldsCountryName.equals(poBoxFieldsCountryName))
				&& (poBoxFieldsStateName != null
				&& personFieldsStateName != null
				&& personFieldsStateName.equals(poBoxFieldsStateName)
				|| (poBoxFieldsStateName == null
				&& personFieldsStateName == null))
				&& ((!addressFieldsNewCityName.equals(personFieldsNewCityName))
				|| (!addressFieldsCountryName.equals(personFieldsCountryName))
				|| (addressFieldsStateName != null
				&& personFieldsStateName != null
				&& (!addressFieldsStateName.equals(personFieldsStateName))
				|| (addressFieldsStateName != null
				&& personFieldsStateName == null)
				|| (addressFieldsStateName == null
				&& personFieldsStateName != null)))) {
// If person and po box fields new cities are the same, 
// but different from address fields
// Create new city for person and poBox fields
				City newCreatedCity = this.updateOffenderRelationService
					.createCity(
					poBoxFieldsNewCityName, 
					editRelationshipsForm.getPersonFields().getBirthState(),
					editRelationshipsForm.getPersonFields().getBirthCountry());
				// Create new city for address fields
				newCreatedAddressFieldsCity = this.updateOffenderRelationService
					.createCity(
					addressFieldsNewCityName, 
					editRelationshipsForm.getAddressFields().getState(),
					editRelationshipsForm.getAddressFields().getCountry());
				newCreatedPoBoxFieldsCity = newCreatedCity;
				newCreatedPersonFieldsCity = newCreatedCity;
				addressPoBoxFieldsNewCity = false;
			} else {
				// All new cities in three fields are different
				// Create new city for po box fields
				newCreatedPoBoxFieldsCity = this.updateOffenderRelationService
					.createCity(
					poBoxFieldsNewCityName, 
					editRelationshipsForm.getPoBoxFields().getState(),
					editRelationshipsForm.getPoBoxFields().getCountry());
				// Create new city for address fields
				newCreatedAddressFieldsCity = this.updateOffenderRelationService
					.createCity(
					addressFieldsNewCityName, 
					editRelationshipsForm.getAddressFields().getState(),
					editRelationshipsForm.getAddressFields().getCountry());
				newCreatedPersonFieldsCity 
					= this.updateOffenderRelationService.createCity(
					personFieldsNewCityName, 
					editRelationshipsForm.getPersonFields().getBirthState(),
					editRelationshipsForm.getPersonFields().getBirthCountry());
				addressPoBoxFieldsNewCity = false;
			}
		} else if (poBoxFieldsNewCity && addressFieldsNewCity
			&& (!personFieldsNewCity)) {
			// po box and address fields create new cities, person fields not
			if (poBoxFieldsNewCityName.equals(addressFieldsNewCityName)
				&& poBoxFieldsCountryName.equals(addressFieldsCountryName)
				&& ((poBoxFieldsStateName != null
				&& addressFieldsStateName != null
				&& poBoxFieldsStateName.equals(addressFieldsStateName)
				|| (poBoxFieldsStateName == null
				&& addressFieldsStateName == null)))) {
				// po box and address fields' new city names are same
				// Create same new city for address and person fields
				City newCreatedCity = this.updateOffenderRelationService
					.createCity(
						addressFieldsNewCityName, 
						editRelationshipsForm.getAddressFields().getState(),
						editRelationshipsForm.getAddressFields().getCountry());
				newCreatedAddressFieldsCity = newCreatedCity;
				newCreatedPoBoxFieldsCity = newCreatedCity;
				newCreatedPersonFieldsCity = null;
				addressPoBoxFieldsNewCity = true;
			} else {
				// Different new cities for po box and address fields
				newCreatedAddressFieldsCity = this.updateOffenderRelationService
					.createCity(
					addressFieldsNewCityName, 
					editRelationshipsForm.getAddressFields().getState(),
					editRelationshipsForm.getAddressFields().getCountry());
				newCreatedPoBoxFieldsCity = this.updateOffenderRelationService
					.createCity(
					poBoxFieldsNewCityName, 
					editRelationshipsForm.getPoBoxFields().getState(),
					editRelationshipsForm.getPoBoxFields().getCountry());
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
				City newCreatedCity = this.updateOffenderRelationService
					.createCity(
					addressFieldsNewCityName, 
					editRelationshipsForm.getAddressFields().getState(),
					editRelationshipsForm.getAddressFields().getCountry());
				newCreatedAddressFieldsCity = newCreatedCity;
				newCreatedPoBoxFieldsCity = null;
				newCreatedPersonFieldsCity = newCreatedCity;
				addressPoBoxFieldsNewCity = false;
			} else {
				// Different new cities for person and address fields
				newCreatedAddressFieldsCity = this.updateOffenderRelationService
					.createCity(
					addressFieldsNewCityName, 
					editRelationshipsForm.getAddressFields().getState(),
					editRelationshipsForm.getAddressFields().getCountry());
				newCreatedPoBoxFieldsCity = null;
				newCreatedPersonFieldsCity 
					= this.updateOffenderRelationService.createCity(
					personFieldsNewCityName, 
					editRelationshipsForm.getPersonFields().getBirthState(),
					editRelationshipsForm.getPersonFields().getBirthCountry());
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
				&& poBoxFieldsStateName == null))) {
				// person and po box fields' new city names are same
				// Create same new city for address and person fields
				City newCreatedCity = this.updateOffenderRelationService
					.createCity(
					personFieldsNewCityName, 
					editRelationshipsForm.getPersonFields().getBirthState(),
					editRelationshipsForm.getPersonFields().getBirthCountry());
				newCreatedPoBoxFieldsCity = newCreatedCity;
				newCreatedPersonFieldsCity = newCreatedCity;
				newCreatedAddressFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			} else {
				// Different new cities for person and po box fields
				newCreatedPoBoxFieldsCity = this.updateOffenderRelationService
					.createCity(
					poBoxFieldsNewCityName, 
					editRelationshipsForm.getPoBoxFields().getState(),
					editRelationshipsForm.getPoBoxFields().getCountry());
				newCreatedPersonFieldsCity 
					= this.updateOffenderRelationService.createCity(
					personFieldsNewCityName, 
					editRelationshipsForm.getPersonFields().getBirthState(),
					editRelationshipsForm.getPersonFields().getBirthCountry());
				newCreatedAddressFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			}
		} else if (poBoxFieldsNewCity && (!addressFieldsNewCity)
				&& (!personFieldsNewCity)) {
			// po box fields create new cities, person and address fields not
			// Create same new city for address and person fields
			newCreatedPoBoxFieldsCity = this.updateOffenderRelationService
				.createCity(
				poBoxFieldsNewCityName, 
				editRelationshipsForm.getPoBoxFields().getState(),
				editRelationshipsForm.getPoBoxFields().getCountry());
			newCreatedAddressFieldsCity = null;
			newCreatedPersonFieldsCity = null;
			addressPoBoxFieldsNewCity = false;
		} else if ((!poBoxFieldsNewCity) && addressFieldsNewCity
				&& (!personFieldsNewCity)) {
			// address fields create new cities, person and po box fields not
			// Create same new city for address and person fields
			newCreatedAddressFieldsCity = this.updateOffenderRelationService
				.createCity(
				addressFieldsNewCityName, 
				editRelationshipsForm.getAddressFields().getState(),
				editRelationshipsForm.getAddressFields().getCountry());
			newCreatedPoBoxFieldsCity = null;
			newCreatedPersonFieldsCity = null;
			addressPoBoxFieldsNewCity = false;
		} else if (personFieldsNewCity && (!addressFieldsNewCity)
			&& (!poBoxFieldsNewCity)) {
			// person fields create new cities, po box and address fields not
			newCreatedPoBoxFieldsCity = null;
			newCreatedAddressFieldsCity = null;
			if (editRelationshipsForm.getPersonFields().getBirthState()
				!= null) {
				newCreatedPersonFieldsCity = this.updateOffenderRelationService
				.createCity(personFieldsNewCityName, 
				editRelationshipsForm.getPersonFields().getBirthState(),
				editRelationshipsForm.getPersonFields().getBirthCountry());
			} else {
				newCreatedPersonFieldsCity = this.updateOffenderRelationService
				.createCity(personFieldsNewCityName, null,
					editRelationshipsForm.getPersonFields().getBirthCountry());
			}
			addressPoBoxFieldsNewCity = false;
		} else {
			// None creates new city
			newCreatedPoBoxFieldsCity = null;
			newCreatedAddressFieldsCity = null;
			newCreatedPersonFieldsCity = null;
			addressPoBoxFieldsNewCity = false;
		}
		
		if (addressFieldsNewZipCode && poBoxFieldsNewZipCode) {
			// Both address and po box fields will create new zip codes
			if (addressFieldsZipCodeValue.equals(poBoxFieldsZipCodeValue)
				&& addressFieldsZipCodeExtension.equals(
					poBoxFieldsZipCodeExtension)) {
				// New zip codes in address and po box fields are same
				if (addressPoBoxFieldsNewCity) {
		// Both address and po box fields create new cities and they are same
		// Create new zip code for address and po box fields 
					ZipCode newCreatedZipCode 
							= this.updateOffenderRelationService
							.createZipCode(
								editRelationshipsForm.getAddressFields()
								.getZipCodeValue(), 
								editRelationshipsForm.getAddressFields()
								.getZipCodeExtension(), 
							newCreatedPoBoxFieldsCity);
					newCreatedAddressFieldsZipCode = newCreatedZipCode;
					newCreatedPoBoxFieldsZipCode = newCreatedZipCode;
					poBox.setValue(editRelationshipsForm.getPoBoxFields()
							.getPoBoxValue());
					poBox.setZipCode(newCreatedZipCode);
				} else {
			// new cities created for po box and address fields are different
					if (newCreatedPoBoxFieldsCity != null
							&& newCreatedAddressFieldsCity != null) {
			// Both po box and address fields create new cities,but different
						newCreatedAddressFieldsZipCode = 
								this.updateOffenderRelationService
								.createZipCode(
									editRelationshipsForm.getAddressFields()
									.getZipCodeValue(), 
									editRelationshipsForm.getAddressFields()
									.getZipCodeExtension(), 
									newCreatedAddressFieldsCity);
						newCreatedPoBoxFieldsZipCode = 
								this.updateOffenderRelationService
								.createZipCode(
									editRelationshipsForm.getPoBoxFields()
									.getZipCodeValue(), 
									editRelationshipsForm.getPoBoxFields()
									.getZipCodeExtension(), 
									newCreatedPoBoxFieldsCity);
					} else if (newCreatedPoBoxFieldsCity == null
						&& newCreatedAddressFieldsCity != null) {
		// Po box field does not creates new city and address fields does
						newCreatedAddressFieldsZipCode = 
							this.updateOffenderRelationService.createZipCode(
								editRelationshipsForm.getAddressFields()
									.getZipCodeValue(), 
								editRelationshipsForm.getAddressFields()
									.getZipCodeExtension(),
								newCreatedAddressFieldsCity);
						newCreatedPoBoxFieldsZipCode =
									this.updateOffenderRelationService
									.createZipCode(
										editRelationshipsForm.getPoBoxFields()
										.getZipCodeValue(),
										editRelationshipsForm.getPoBoxFields()
										.getZipCodeExtension(),
										editRelationshipsForm.getPoBoxFields()
										.getCity());
					} else if (newCreatedPoBoxFieldsCity != null
						&& newCreatedAddressFieldsCity == null) {
					// Po box field creates new city and address fields does not
						newCreatedAddressFieldsZipCode = 
							this.updateOffenderRelationService.createZipCode(
								editRelationshipsForm.getAddressFields()
								.getZipCodeValue(), 
								editRelationshipsForm.getAddressFields()
								.getZipCodeExtension(),
								editRelationshipsForm.getAddressFields()
								.getCity());
						newCreatedPoBoxFieldsZipCode = 
							this.updateOffenderRelationService.createZipCode(
								editRelationshipsForm.getPoBoxFields()
								.getZipCodeValue(),
								editRelationshipsForm.getPoBoxFields()
								.getZipCodeExtension(),
								newCreatedPoBoxFieldsCity);
					} else {
						// none create new zip code
						if (!editRelationshipsForm.getAddressFields().getCity()
							.equals(editRelationshipsForm.getPoBoxFields()
							.getCity())) {
							newCreatedAddressFieldsZipCode = 
								this.updateOffenderRelationService
								.createZipCode(
									editRelationshipsForm.getAddressFields()
									.getZipCodeValue(), 
									editRelationshipsForm.getAddressFields()
									.getZipCodeExtension(), 
									editRelationshipsForm.getAddressFields()
									.getCity());
							newCreatedPoBoxFieldsZipCode = 
								this.updateOffenderRelationService
								.createZipCode(
									editRelationshipsForm.getPoBoxFields()
									.getZipCodeValue(), 
									editRelationshipsForm.getPoBoxFields()
									.getZipCodeExtension(), 
									editRelationshipsForm.getPoBoxFields()
									.getCity());
						} else {
							newCreatedAddressFieldsZipCode = 
								this.updateOffenderRelationService
								.createZipCode(
									editRelationshipsForm.getAddressFields()
									.getZipCodeValue(), 
									editRelationshipsForm.getAddressFields()
									.getZipCodeExtension(), 
									editRelationshipsForm.getAddressFields()
									.getCity());
							newCreatedPoBoxFieldsZipCode 
								= newCreatedAddressFieldsZipCode; 
						}
					}
				}
				poBox.setValue(editRelationshipsForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
			} else {
				// new zip codes in address and po box fields are different
				if (newCreatedAddressFieldsCity != null) {
					newCreatedAddressFieldsZipCode 
						= this.updateOffenderRelationService.createZipCode(
							editRelationshipsForm.getAddressFields()
								.getZipCodeValue(), 
							editRelationshipsForm.getAddressFields()
								.getZipCodeExtension(), 
							newCreatedAddressFieldsCity);
				} else {
					newCreatedAddressFieldsZipCode 
						= this.updateOffenderRelationService.createZipCode(
						editRelationshipsForm.getAddressFields()
							.getZipCodeValue(), 
						editRelationshipsForm.getAddressFields()
							.getZipCodeExtension(), 
						editRelationshipsForm.getAddressFields().getCity());
				}
				if (newCreatedPoBoxFieldsCity != null) {
					newCreatedPoBoxFieldsZipCode 
						= this.updateOffenderRelationService.createZipCode(
							editRelationshipsForm.getPoBoxFields()
								.getZipCodeValue(), 
							editRelationshipsForm.getPoBoxFields()
								.getZipCodeExtension(), 
							newCreatedPoBoxFieldsCity);
				} else {
					newCreatedPoBoxFieldsZipCode 
						= this.updateOffenderRelationService.createZipCode(
						editRelationshipsForm.getPoBoxFields()
							.getZipCodeValue(), 
						editRelationshipsForm.getPoBoxFields()
							.getZipCodeExtension(), 
						editRelationshipsForm.getPoBoxFields().getCity());
				}
				poBox.setValue(editRelationshipsForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
			}
		} else if (addressFieldsNewZipCode && (!poBoxFieldsNewZipCode)) {
			// Address fields create new zip code, po box fields not
			if (newCreatedAddressFieldsCity == null) {
				newCreatedAddressFieldsZipCode 
					= this.updateOffenderRelationService.createZipCode(
					editRelationshipsForm.getAddressFields()
						.getZipCodeValue(), 
					editRelationshipsForm.getAddressFields()
						.getZipCodeExtension(), 
					editRelationshipsForm.getAddressFields().getCity());
			} else {
				newCreatedAddressFieldsZipCode 
					= this.updateOffenderRelationService.createZipCode(
					editRelationshipsForm.getAddressFields()
						.getZipCodeValue(), 
					editRelationshipsForm.getAddressFields()
						.getZipCodeExtension(), 
					newCreatedAddressFieldsCity);
			}
			newCreatedPoBoxFieldsZipCode = null;
			poBox.setValue(editRelationshipsForm.getPoBoxFields()
				.getPoBoxValue());
			poBox.setZipCode(editRelationshipsForm.getPoBoxFields()
				.getZipCode());
		} else if ((!addressFieldsNewZipCode) && poBoxFieldsNewZipCode) {
			// Po Box fields create new zip code, address fields not
			if (newCreatedPoBoxFieldsCity == null) {
				newCreatedPoBoxFieldsZipCode 
					= this.updateOffenderRelationService.createZipCode(
					editRelationshipsForm.getPoBoxFields().getZipCodeValue(), 
					editRelationshipsForm.getPoBoxFields()
					.getZipCodeExtension(), 
					editRelationshipsForm.getPoBoxFields().getCity());
			} else {
				newCreatedPoBoxFieldsZipCode 
					= this.updateOffenderRelationService.createZipCode(
					editRelationshipsForm.getPoBoxFields().getZipCodeValue(), 
					editRelationshipsForm.getPoBoxFields()
					.getZipCodeExtension(), 
					newCreatedPoBoxFieldsCity);
			}
			newCreatedAddressFieldsZipCode = null;
			poBox.setValue(editRelationshipsForm.getPoBoxFields()
				.getPoBoxValue());
			poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
		} else {
			// None create new zip codes
			newCreatedPoBoxFieldsZipCode = null;
			newCreatedAddressFieldsZipCode = null;
			poBox.setValue(editRelationshipsForm.getPoBoxFields()
				.getPoBoxValue());
			poBox.setZipCode(editRelationshipsForm.getPoBoxFields()
				.getZipCode());
		}	
		City city;
		if (editRelationshipsForm.getPersonFields().getNewCity() != null 
				&& editRelationshipsForm.getPersonFields().getNewCity()) {
			city = newCreatedPersonFieldsCity;
		} else {
			city = editRelationshipsForm.getPersonFields().getBirthCity();
		}
		//if (!editRelationshipsForm.getPersonFields().getNewCity()) {
			// Exiting city
		
		if (hasRole("ADMIN") || hasRole("OFFENDER_RELATIONSHIP_SSN_EDIT") 
				|| hasRole("OFFENDER_RELATIONSHIP_SSN_VIEW")) {		
			Integer socialSecurityNumber;
			if (editRelationshipsForm.getPersonFields()
					.getSocialSecurityNumber() != null 
					&& !"".equals(editRelationshipsForm
							.getPersonFields()
							.getSocialSecurityNumber())) {
				socialSecurityNumber = Integer.valueOf(editRelationshipsForm
						.getPersonFields().getSocialSecurityNumber()
						.replaceAll("-", ""));
			} else {
				socialSecurityNumber = null;
			}		
			this.updateOffenderRelationService.updateRelation(
				relationship.getSecondPerson(), 
				editRelationshipsForm.getPersonFields().getLastName(), 
				editRelationshipsForm.getPersonFields().getFirstName(), 
				editRelationshipsForm.getPersonFields().getMiddleName(), 
				editRelationshipsForm.getPersonFields().getSuffix(), 
				editRelationshipsForm.getPersonFields().getSex(), 
				editRelationshipsForm.getPersonFields().getBirthDate(), 
				editRelationshipsForm.getPersonFields().getBirthCountry(), 
				editRelationshipsForm.getPersonFields().getBirthState(), 
				city, socialSecurityNumber, 
				editRelationshipsForm.getPersonFields().getStateIdNumber(), 
				editRelationshipsForm.getPersonFields().getDeceased(), 
				editRelationshipsForm.getPersonFields().getDeathDate());
		} else {
			this.updateOffenderRelationService.updateRelationWithoutSsn(
				relationship.getSecondPerson(), 
				editRelationshipsForm.getPersonFields().getLastName(), 
				editRelationshipsForm.getPersonFields().getFirstName(), 
				editRelationshipsForm.getPersonFields().getMiddleName(), 
				editRelationshipsForm.getPersonFields().getSuffix(), 
				editRelationshipsForm.getPersonFields().getSex(), 
				editRelationshipsForm.getPersonFields().getBirthDate(), 
				editRelationshipsForm.getPersonFields().getBirthCountry(), 
				editRelationshipsForm.getPersonFields().getBirthState(), 
				city,
				editRelationshipsForm.getPersonFields().getStateIdNumber(), 
				editRelationshipsForm.getPersonFields().getDeceased(), 
				editRelationshipsForm.getPersonFields().getDeathDate());
		}
		
		/* Create address. No exception */
		Address address;
		if (editRelationshipsForm.getEnterAddress() != null
			&& editRelationshipsForm.getEnterAddress()) {
			// EnterAddress check box checked
			if (OffenderRelationshipAddressOperation.NEW.equals(
				editRelationshipsForm.getAddressOperation())) {
				// New address
				if (editRelationshipsForm.getAddressFields().getNewCity()) {
					// New city in address fields
				    address = this.updateOffenderRelationService.createAddress(
				    editRelationshipsForm.getAddressFields().getValue(), 
				    newCreatedAddressFieldsZipCode);   
				} else {
					// Existing city, existing zip code in address fields
					if (newCreatedAddressFieldsZipCode == null) {
						// Existing zip code
					    address = this.updateOffenderRelationService
					    	.createAddress(
					    editRelationshipsForm.getAddressFields().getValue(), 
					    editRelationshipsForm.getAddressFields()
					    .getZipCode());   
					} else {
						// New created zip code
					    address = this.updateOffenderRelationService
					    	.createAddress(
					    editRelationshipsForm.getAddressFields().getValue(), 
					    newCreatedAddressFieldsZipCode);   
					}
				}
			} else if (editRelationshipsForm.getAddressOperation()
				.equals(OffenderRelationshipAddressOperation.CURRENT)) {
				// New address
				address = this.updateOffenderRelationService
					.findMailingAddress(relationship.getSecondPerson());
			} else {
				// Existing address in address fields
				address = editRelationshipsForm.getAddress();
			}
		} else {
			address = null;
		}
		
		/* Create contact. DuplicateEntityFoundException */
		if ((editRelationshipsForm.getEnterAddress() != null
			&& editRelationshipsForm.getEnterAddress())
			&& (editRelationshipsForm.getEnterPoBox() != null
			&& editRelationshipsForm.getEnterPoBox())) {
			// Address, po box  both checked
			this.updateOffenderRelationService.changeContact(relationship
				.getSecondPerson(), poBox, address);
		}
		if ((editRelationshipsForm.getEnterAddress() != null
			&& editRelationshipsForm.getEnterAddress())
			&& ((editRelationshipsForm.getEnterPoBox() != null
			&& !editRelationshipsForm.getEnterPoBox())
			|| (editRelationshipsForm.getEnterPoBox() == null))) {
			// Address checked, po box unchecked
			this.updateOffenderRelationService.changeContact(
				relationship.getSecondPerson(), null, address);
		}
		if (((editRelationshipsForm.getEnterAddress() != null
			&& !editRelationshipsForm.getEnterAddress())
			|| (editRelationshipsForm.getEnterAddress() == null))
			&& (editRelationshipsForm.getEnterPoBox() != null
			&& editRelationshipsForm.getEnterPoBox())) {
			// Address unchecked, po box checked
			this.updateOffenderRelationService.changeContact(
				relationship.getSecondPerson(), poBox, null);
		}
		if (((editRelationshipsForm.getEnterAddress() != null
			&& !editRelationshipsForm.getEnterAddress())
			|| (editRelationshipsForm.getEnterAddress() == null))
			&& ((editRelationshipsForm.getEnterPoBox() != null
			&& !editRelationshipsForm.getEnterPoBox())
			|| (editRelationshipsForm.getEnterPoBox() == null))) {
			// Address, po box both unchecked
			this.updateOffenderRelationService.changeContact(
				relationship.getSecondPerson(), null, null);
		}
		
		/* Create telephone number. DuplicateEntityFoundException */
		List<TelephoneNumberItem> telephoneNumberItems 
			= editRelationshipsForm.getTelephoneNumberItems();
		int originalTotalTelepphoneNumber = this.updateOffenderRelationService
			.findTelephoneNumbers(relationship.getSecondPerson()).size();
		int telephoneNumberCounter = 0;
		for (TelephoneNumberItem item : telephoneNumberItems) {
			if ((item.getOperation() == null)
				|| (TelephoneNumberItemOperation.REMOVE.equals(
				item.getOperation()))) {
				if (telephoneNumberCounter < originalTotalTelepphoneNumber) {
					this.updateOffenderRelationService.removeTelephoneNumber(
						item.getTelephoneNumber());
				}
			}
			if (TelephoneNumberItemOperation.UPDATE.equals(
				item.getOperation())) {
				this.updateOffenderRelationService.updateTelephoneNumber(
					item.getTelephoneNumber(), 
					item.getTelephoneNumberFields().getValue(), 
					item.getTelephoneNumberFields().getExtension(), 
					resolveCheckBoxValue(item.getTelephoneNumberFields()
							.getPrimary()), 
					resolveCheckBoxValue(item.getTelephoneNumberFields()
							.getActive()),
					item.getTelephoneNumberFields().getCategory());
			}
			if (TelephoneNumberItemOperation.CREATE.equals(
				item.getOperation())) {
				this.updateOffenderRelationService.createTelephoneNumber(
					relationship.getSecondPerson(), 
					item.getTelephoneNumberFields().getValue(), 
					item.getTelephoneNumberFields().getExtension(), 
					resolveCheckBoxValue(item.getTelephoneNumberFields()
							.getPrimary()), 
					resolveCheckBoxValue(item.getTelephoneNumberFields()
							.getActive()),
					item.getTelephoneNumberFields().getCategory());
			}
			telephoneNumberCounter = telephoneNumberCounter + 1;
		}
			
		/* Add online account. DuplicateEntityFoundException */
		List<OnlineAccountContactItem> onlineAccountItems 
			= editRelationshipsForm.getOnlineAccountContactItems();
		int originalTotalOnlineAccount = this.updateOffenderRelationService
			.findOnlineAccounts(relationship.getSecondPerson()).size();
		int onlineAccountCounter = 0;
		for (OnlineAccountContactItem onlineAccountContactItem 
			: onlineAccountItems) {
			if (OnlineAccountContactItemOperation.CREATE.equals(
				onlineAccountContactItem.getOperation())) {
				this.updateOffenderRelationService.createOnlineAccount(
						relationship.getSecondPerson(), 
						onlineAccountContactItem.getOnlineAccountFields()
							.getName(), 
						onlineAccountContactItem.getOnlineAccountFields()
							.getHost(), 
						resolveCheckBoxValue(onlineAccountContactItem
							.getOnlineAccountFields().getPrimary()),
						resolveCheckBoxValue(onlineAccountContactItem
							.getOnlineAccountFields().getActive()));
			}
			if (OnlineAccountContactItemOperation.UPDATE.equals(
				onlineAccountContactItem.getOperation())) {
				this.updateOffenderRelationService.updateOnlineAccount(
						onlineAccountContactItem.getOnlineAccount(), 
						onlineAccountContactItem.getOnlineAccountFields()
						.getName(),
						onlineAccountContactItem.getOnlineAccountFields()
						.getHost(), 
						resolveCheckBoxValue(onlineAccountContactItem
						.getOnlineAccountFields().getPrimary()),
						resolveCheckBoxValue(onlineAccountContactItem
						.getOnlineAccountFields().getActive()));
			}
			if (OnlineAccountContactItemOperation.REMOVE.equals(
				onlineAccountContactItem.getOperation())) {
				if (onlineAccountCounter < originalTotalOnlineAccount) {
					this.updateOffenderRelationService.removeOnlineAccount(
						onlineAccountContactItem.getOnlineAccount());
				}
			}
			onlineAccountCounter = onlineAccountCounter + 1;
		}
		
		// Creates/updates/removes notes
		for (OffenderRelationshipNoteItem noteItem
				: editRelationshipsForm.getNoteItems()) {
			if (noteItem.getOperation() != null) {
				if (OffenderRelationshipNoteItemOperation
						.CREATE.equals(noteItem.getOperation())) {
					this.updateOffenderRelationService
						.createNote(relationship,
								noteItem.getFields().getCategory(),
								noteItem.getFields().getValue(),
								noteItem.getFields().getDate());
				} else if (OffenderRelationshipNoteItemOperation
						.UPDATE.equals(noteItem.getOperation())) {
					this.updateOffenderRelationService
						.updateNote(noteItem.getNote(),
								noteItem.getFields().getCategory(),
								noteItem.getFields().getValue(),
								noteItem.getFields().getDate());
				} else if (OffenderRelationshipNoteItemOperation
						.REMOVE.equals(noteItem.getOperation())) {
					this.updateOffenderRelationService
						.removeNote(noteItem.getNote());
				} else {
					throw new UnsupportedOperationException(
							String.format("Note operation not supported: %s",
									noteItem.getOperation()));
				}
			}
		}
		
		// Redirects to listing screen
		Person person = relationship.getFirstPerson();
		return new ModelAndView(String.format(LIST_REDIRECT, person.getId()));
	}
	
	/**
	 * Displays the specified person with information from the edit 
	 * relationships form.
	 * 
	 * @param relationship relationship
	 * @return model and view to redirect to appropriate update screen
	 */
	@RequestMapping(value = "edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFENDER_RELATIONSHIP_VIEW')")
	public ModelAndView update(
		@RequestParam(value = "relationship", required = true)
		final Relationship relationship) {
		EditRelationshipsForm editRelationshipsForm 
			= new EditRelationshipsForm();
		// Set person fields
		PersonFields personFields = new PersonFields();
		final List<State> personFieldsBirthStates;
		final List<City> personFieldsBirthCities;
		
		if (relationship.getSecondPerson().getName() != null) {
			personFields.setFirstName(relationship.getSecondPerson().getName()
					.getFirstName());
			personFields.setLastName(relationship.getSecondPerson().getName()
					.getLastName());
			personFields.setMiddleName(relationship.getSecondPerson().getName()
					.getMiddleName());
			personFields.setSuffix(relationship.getSecondPerson().getName()
					.getSuffix());
		}
		if (relationship.getSecondPerson().getIdentity() != null) {
			personFields.setBirthCity(relationship.getSecondPerson()
					.getIdentity().getBirthPlace());
			personFields.setBirthCountry(relationship.getSecondPerson()
					.getIdentity().getBirthCountry());  
			personFields.setBirthState(relationship.getSecondPerson()
					.getIdentity().getBirthState());  
			personFields.setBirthDate(relationship.getSecondPerson()
					.getIdentity().getBirthDate());
			personFields.setDeathDate(relationship.getSecondPerson()
					.getIdentity().getDeathDate());
			personFields.setDeceased(relationship.getSecondPerson()
					.getIdentity().getDeceased());
			personFields.setSex(relationship.getSecondPerson().getIdentity()
					.getSex());
			if (relationship.getSecondPerson().getIdentity()
					.getSocialSecurityNumber() != null) {
				if (hasRole("ADMIN") || hasRole("OFFENDER_RELATIONSHIP_SSN_EDIT") 
						|| hasRole("OFFENDER_RELATIONSHIP_SSN_VIEW")) {
					String stringSSN = String.format("%09d",
							relationship.getSecondPerson().getIdentity()
							.getSocialSecurityNumber());
					if (stringSSN.length() != 9) {
						throw new IllegalStateException(
								"Invalid Social Security Number");
					} else {
						String formattedSSN = String
								.format("%s-%s-%s",
										stringSSN.substring(0,3),
										stringSSN.substring(3,5),
										stringSSN.substring(5,9));
						personFields.setSocialSecurityNumber(formattedSSN);
					}
					editRelationshipsForm.setValidateSocialSecurityNumber(true);
				} else {
					String socialSecurityNumber
						= relationship.getSecondPerson().getIdentity()
						.getSocialSecurityNumber().toString(); 
					if (socialSecurityNumber.length() >= 4) {
						socialSecurityNumber = "XXX-XX-"
								+ socialSecurityNumber.substring(
										socialSecurityNumber.length() - 4,
										socialSecurityNumber.length());
					} else {
						socialSecurityNumber = "XXX-XX-"
								+ socialSecurityNumber;
					}				
				personFields.setSocialSecurityNumber(socialSecurityNumber);
				editRelationshipsForm.setValidateSocialSecurityNumber(false);
				}
			}
			personFields.setStateIdNumber(relationship.getSecondPerson()
					.getIdentity().getStateIdNumber());
			personFieldsBirthStates = this.updateOffenderRelationService
				.findStates(relationship.getSecondPerson().getIdentity()
						.getBirthCountry());
			if (relationship.getSecondPerson().getIdentity().getBirthState()
					!= null) {
				personFieldsBirthCities = this.updateOffenderRelationService
				.findCitiesByState(relationship.getSecondPerson().getIdentity()
						.getBirthState());
			} else {
				personFieldsBirthCities 
					= this.updateOffenderRelationService
				.findCitiesByCountryWithoutState(relationship.getSecondPerson()
				.getIdentity().getBirthCountry());
			}
		} else {
			personFieldsBirthStates = Collections.emptyList();
			personFieldsBirthCities = Collections.emptyList();
		}
		editRelationshipsForm.setPersonFields(personFields);

		/* Set po box field */
		PoBox poBox = this.updateOffenderRelationService.findPoBox(
			relationship.getSecondPerson());
		PoBoxFields poBoxFields = new PoBoxFields();
		if (poBox != null) {
			if (poBox.getZipCode() != null) {
				poBoxFields.setCity(poBox.getZipCode().getCity());
				poBoxFields.setCityName(poBox.getZipCode().getCity().getName());
				poBoxFields.setCountry(poBox.getZipCode().getCity()
						.getCountry());
				poBoxFields.setState(poBox.getZipCode().getCity().getState());
				poBoxFields.setZipCodeExtension(poBox.getZipCode()
						.getExtension());
				poBoxFields.setZipCodeValue(poBox.getZipCode().getValue());
			}
			poBoxFields.setPoBoxValue(poBox.getValue());
			poBoxFields.setZipCode(poBox.getZipCode());
			poBoxFields.setNewZipCode(false);
			editRelationshipsForm.setEnterPoBox(true);
			editRelationshipsForm.setPoBoxFields(poBoxFields);
		} else {
			editRelationshipsForm.setPoBoxFields(null);
		}
		/* Set telephone number fields */
		List<TelephoneNumber> telephoneNumbers 
			= this.updateOffenderRelationService.findTelephoneNumbers(
					relationship.getSecondPerson());
		List<TelephoneNumberItem> telephoneNumberItems 
			= new ArrayList<TelephoneNumberItem>();
		if (telephoneNumbers != null) {
			for (TelephoneNumber item : telephoneNumbers) {
				TelephoneNumberItem telephoneNumberItem 
					= new TelephoneNumberItem();
				telephoneNumberItem.setId(item.getId()); 
				telephoneNumberItem.setOperation(
					TelephoneNumberItemOperation.UPDATE);
				TelephoneNumberFields telephoneNumberField 
					= new TelephoneNumberFields();
				telephoneNumberField.setCategory(item.getCategory());
				telephoneNumberField.setExtension(item.getExtension());
				telephoneNumberField.setPrimary(resolveCheckBoxValue(
						item.getPrimary()));
				telephoneNumberField.setValue(item.getValue());
				telephoneNumberField.setCategory(item.getCategory());
				telephoneNumberField.setActive(resolveCheckBoxValue(
						item.getActive()));
				telephoneNumberItem.setTelephoneNumberFields(
						telephoneNumberField);
				telephoneNumberItem.setTelephoneNumber(item);
				telephoneNumberItem.setOperation(
					TelephoneNumberItemOperation.UPDATE);
				telephoneNumberItems.add(telephoneNumberItem);
			}
		}
		editRelationshipsForm.setTelephoneNumberItems(telephoneNumberItems);
				
		/* Set online account fields */
		List<OnlineAccount> onlineAccounts 
			= this.updateOffenderRelationService.findOnlineAccounts(
					relationship.getSecondPerson());
		List<OnlineAccountContactItem> onlineAccountContactItems 
			= new ArrayList<OnlineAccountContactItem>();
		if (onlineAccounts != null) {
			for (OnlineAccount item : onlineAccounts) {
				OnlineAccountFields onlineAccountFields
					= new OnlineAccountFields();
				onlineAccountFields.setActive(item.getActive());
				onlineAccountFields.setHost(item.getHost());
				onlineAccountFields.setName(item.getName());
				onlineAccountFields.setPrimary(item.getPrimary());
				OnlineAccountContactItem onlineAccountContactItem 
					= new OnlineAccountContactItem();
				onlineAccountContactItem.setId(item.getId());
				onlineAccountContactItem.setOnlineAccountFields(
					onlineAccountFields);
				onlineAccountContactItem.setOperation(
					OnlineAccountContactItemOperation.UPDATE);
				onlineAccountContactItem.setOnlineAccount(item);
				onlineAccountContactItems.add(onlineAccountContactItem);
			}
		}
		editRelationshipsForm.setOnlineAccountContactItems(
			onlineAccountContactItems);
		
		/* Set address fields */
		AddressFields addressFields = new AddressFields();
		Address address = this.updateOffenderRelationService
			.findMailingAddress(relationship.getSecondPerson());
		
		if (address != null) {
			editRelationshipsForm.setAddressFields(addressFields);
			editRelationshipsForm.setEnterAddress(true);
			/* Set current address as default at edit screen */
			editRelationshipsForm.setAddressOperation(
				OffenderRelationshipAddressOperation.CURRENT);
//			editRelationshipsForm.setCurrentAddress(address);
		} else {
			/* Set existing address as default at edit screen 
			 * when current address does not exist*/
			editRelationshipsForm.setAddressOperation(
				OffenderRelationshipAddressOperation.EXISTING);
		}
		
		/* Set current address as default at edit screen */
		/*editRelationshipsForm.setAddressOperation(
			OffenderRelationshipAddressOperation.CURRENT);*/
		
		// Adds notes
		List<RelationshipNote> notes = this.updateOffenderRelationService
				.findNotes(relationship);
		List<OffenderRelationshipNoteItem> noteItems
			= new ArrayList<OffenderRelationshipNoteItem>();
		for (RelationshipNote note : notes) {
			OffenderRelationshipNoteItem noteItem
				= new OffenderRelationshipNoteItem();
			noteItem.setOperation(OffenderRelationshipNoteItemOperation.UPDATE);
			noteItem.setNote(note);
			OffenderRelationshipNoteFields fields
				= new OffenderRelationshipNoteFields();
			fields.setCategory(note.getCategory());
			fields.setDate(note.getDate());
			fields.setValue(note.getValue());
			noteItem.setFields(fields);
			noteItems.add(noteItem);
		}
		editRelationshipsForm.setNoteItems(noteItems);
		
		// Returns edit model and view
		return this.prepareEditMav(editRelationshipsForm, 
			personFieldsBirthStates, personFieldsBirthCities, relationship);
	}
	
	/** returns addresses given name search criteria.
	 * @param searchCriteria searchCriteria
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
			addresses = this.updateOffenderRelationService
				.findAddresses(searchCriteria);  
		} else {
			addresses = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(ADDRESSES_VIEW_NAME);
		mav.addObject(ADDRESSES_MODEL_KEY, addresses);
		return mav;
	}
	
	/* Helper Methods. */
	
	/*
	 * Prepares a model and view for editing.
	 * 
	 * @param form edit relationships form
	 * @param personFieldsBirthStates a list of states for person fields
	 * @param personFieldsBirthCities a list of cities for person fields
	 * @param address address
	 * @return model and view for editing offender relationship
	 */
	private ModelAndView prepareEditMav(
		final EditRelationshipsForm form,
		final List<State> personFieldsBirthStates, 
		final List<City> personFieldsBirthCities,
		final Relationship relationship) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		Address address = this.updateOffenderRelationService
			.findMailingAddress(relationship.getSecondPerson());
		mav.addObject(EXISTING_ADDRESS_MODEL_KEY, address);
		mav.addObject(EDIT_RELATIONSHIPS_FORM_MODEL_KEY, form);
		mav.addObject(RELATIONSHIP_MODEL_KEY, relationship);

		List<Suffix> nameSuffixes 
			= this.updateOffenderRelationService.findNameSuffixes();
		List<String> nameSuffixStrings 
			= this.updateOffenderRelationService.findSuffixNames();
		mav.addObject(NAME_SUFFIXES_MODEL_KEY, nameSuffixes);
		mav.addObject(HOME_TYPES_MODEL_KEY, BuildingCategory.values());
		List<StreetSuffix> streetSuffixes 
			= this.updateOffenderRelationService.findStreetSuffixes();
		mav.addObject(STREET_SUFFIXES_MODEL_KEY, streetSuffixes);
		List<AddressUnitDesignator> addressUnitDesignators 
			= this.updateOffenderRelationService.findAddressUnitDesignators();
		mav.addObject(UNIT_DESIGNATORS_MODEL_KEY, addressUnitDesignators);
				
		List<Country> countries = this.updateOffenderRelationService
			.findCountries();
		mav.addObject(COUNTRIES_MODEL_KEY, countries);
		mav.addObject(BIRTH_COUNTRIES_MODEL_KEY, countries);
		mav.addObject(PO_BOX_COUNTRIES_MODEL_KEY, countries);
		
		List<OnlineAccountHost> onlineAccountHosts 
			= this.updateOffenderRelationService.findOnlineAccountHosts();
		OffenderSummary offenderSummary = this.offenderReportService
			.summarizeOffender((Offender) (relationship.getFirstPerson()));
		mav.addObject(OFFENDER_SUMMARY_MODEL_KEY, offenderSummary);
		mav.addObject(OFFENDER_MODEL_KEY,
				(Offender) (relationship.getFirstPerson()));
		mav.addObject(RELATION_MODEL_KEY, relationship.getSecondPerson());
		
		mav.addObject(OFFENDER_RELATIONSHIP_TELEPHONE_NUMBER_INDEX_MODEL_KEY,
			form.getTelephoneNumberItems().size());
		
		mav.addObject(OFFENDER_RELATIONSHIP_ONLINE_ACCOUNT_INDEX_MODEL_KEY, 
			form.getOnlineAccountContactItems().size());
		
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_ITEM_INDEX_MODEL_KEY,
			form.getNoteItems().size());
		
		ModelMap map = mav.getModelMap();
		AddressFields addressFields = form.getAddressFields();
		if (addressFields != null) {
			if (addressFields.getCity() != null) {
				if (this.updateOffenderRelationService.hasStates(
					addressFields.getCountry())) {
					if (addressFields.getState() != null) {
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(
							map, countries, 
							this.updateOffenderRelationService.findStates(
							addressFields.getCountry()), 
							this.updateOffenderRelationService
							.findCitiesByState(
							addressFields.getState()), 
							this.updateOffenderRelationService.findZipCodes(
							addressFields.getCity()), 
							ADDRESS_FIELDS_PROPERTY_NAME);
					} else {
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(
							map, countries, 
							this.updateOffenderRelationService.findStates(
							addressFields.getCountry()), 
							this.updateOffenderRelationService
							.findCitiesByCountryWithoutState(
							addressFields.getCountry()), 
							this.updateOffenderRelationService.findZipCodes(
							addressFields.getCity()), 
							ADDRESS_FIELDS_PROPERTY_NAME);	
					}
				} else {
					// No state
					this.addressFieldsControllerDelegate
						.prepareEditAddressFields(
						map, countries, null, 
						this.updateOffenderRelationService.findCitiesByCountry(
							addressFields.getCity().getCountry()), 
						this.updateOffenderRelationService.findZipCodes(
							addressFields.getCity()), 
						ADDRESS_FIELDS_PROPERTY_NAME); 
				}
			} else {
				 // no city
				if (addressFields.getCountry() != null) {
					if (this.updateOffenderRelationService.hasStates(
						addressFields.getCountry())) {
						if (addressFields.getState() != null) {
							this.addressFieldsControllerDelegate
								.prepareEditAddressFields(
								map, countries, 
								this.updateOffenderRelationService
								.findStates(addressFields.getCountry()), 
								this.updateOffenderRelationService
								.findCitiesByState(
								addressFields.getState()), null, 
								ADDRESS_FIELDS_PROPERTY_NAME);
						} else {
							this.addressFieldsControllerDelegate
								.prepareEditAddressFields(
								map, countries, 
								this.updateOffenderRelationService
								.findStates(addressFields.getCountry()), 
								this.updateOffenderRelationService
								.findCitiesByCountryWithoutState(
								addressFields.getCountry()), null, 
								ADDRESS_FIELDS_PROPERTY_NAME);	
						}
					} else {
						// No state
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(
							map, countries, null,
							this.updateOffenderRelationService
							.findCitiesByCountry(form.getAddressFields()
									.getCountry()),
							null, ADDRESS_FIELDS_PROPERTY_NAME);
					}
				} else {
					// No country. Should never hit here
					this.addressFieldsControllerDelegate
						.prepareEditAddressFields(
						map, countries, null, null, null, 
						ADDRESS_FIELDS_PROPERTY_NAME);
				}
			}
		} else {
			this.addressFieldsControllerDelegate.prepareEditAddressFields(
				map, countries, null, null, null, ADDRESS_FIELDS_PROPERTY_NAME);
		}
		
		PoBoxFields poBoxFields = form.getPoBoxFields();
		if (poBoxFields != null) {
			if (poBoxFields.getCity() != null) {
				if (this.updateOffenderRelationService.hasStates(
					form.getPoBoxFields().getCountry())) {
					if (poBoxFields.getState() != null) {
						this.poBoxFieldsControllerDelegate
							.prepareEditPoBoxFields(
							map, countries, 
							this.updateOffenderRelationService.findStates(
								poBoxFields.getCountry()), 
							this.updateOffenderRelationService
							.findCitiesByState(
								poBoxFields.getCity().getState()), 
							this.updateOffenderRelationService.findZipCodes(
								poBoxFields.getCity()), 
							PO_BOX_FIELDS_PROPERTY_NAME);
					} else {
				    	this.poBoxFieldsControllerDelegate
					    	.prepareEditPoBoxFields(
							map, countries, 
							this.updateOffenderRelationService.findStates(
								poBoxFields.getCountry()), 
							this.updateOffenderRelationService
							.findCitiesByCountryWithoutState(
								poBoxFields.getCountry()), 
							this.updateOffenderRelationService.findZipCodes(
								form.getPoBoxFields().getCity()), 
							PO_BOX_FIELDS_PROPERTY_NAME);
				    }
				} else {
					// No state
					this.poBoxFieldsControllerDelegate
						.prepareEditPoBoxFields(map, 
						countries, null, 
						this.updateOffenderRelationService.findCitiesByCountry(
							poBoxFields.getCity().getCountry()), 
						this.updateOffenderRelationService.findZipCodes(
							poBoxFields.getCity()), 
						PO_BOX_FIELDS_PROPERTY_NAME); 
				}
			} else {
				// no city
				if (poBoxFields.getCountry() != null) {
					if (poBoxFields.getHasStates()) {
						if (poBoxFields.getState() != null) {
							this.poBoxFieldsControllerDelegate
								.prepareEditPoBoxFields(
									map, countries, 
								this.updateOffenderRelationService.findStates(
								poBoxFields.getCountry()), 
								this.updateOffenderRelationService
								.findCitiesByState(
								poBoxFields.getState()), null, 
								PO_BOX_FIELDS_PROPERTY_NAME);
						} else {
							this.poBoxFieldsControllerDelegate
									.prepareEditPoBoxFields(
										map, countries, 
									this.updateOffenderRelationService
									.findStates(
									poBoxFields.getCountry()), 
									this.updateOffenderRelationService
								.findCitiesByCountryWithoutState(
								poBoxFields.getCountry()), null, 
								PO_BOX_FIELDS_PROPERTY_NAME);	
						}
					} else {
						// No state
						this.poBoxFieldsControllerDelegate
							.prepareEditPoBoxFields(
							map, countries, null,
							this.updateOffenderRelationService
							.findCitiesByCountry(poBoxFields.getCountry()),
							null, PO_BOX_FIELDS_PROPERTY_NAME);
					}
				} else {
					// No country
					this.poBoxFieldsControllerDelegate
						.prepareEditPoBoxFields(map, 
						countries, null, null, null,
						PO_BOX_FIELDS_PROPERTY_NAME);
				}
			}
		} else {
			List<State> states 
				= this.updateOffenderRelationService.findStates(null);
			List<City> cities 
				= this.updateOffenderRelationService.findCitiesByState(null);
			List<ZipCode> zipCodes 
				= this.updateOffenderRelationService.findZipCodes(null);
			this.poBoxFieldsControllerDelegate.prepareEditPoBoxFields(map, 
				countries, states, cities, zipCodes, 
				PO_BOX_FIELDS_PROPERTY_NAME);
		}
		
		if (form.getPersonFields() != null) {
			PersonFields personFields = form.getPersonFields();
			if (personFields != null) {
				if (personFields.getBirthCity() != null) {
					if (this.updateOffenderRelationService.hasStates(
						personFields.getBirthCountry())) {
						if (personFields.getBirthState() != null) {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, nameSuffixStrings,
									countries, 
								this.updateOffenderRelationService.findStates(
									personFields.getBirthCountry()),
								this.updateOffenderRelationService
								.findCitiesByState(
								personFields.getBirthState()),
								PERSON_FIELDS_PROPERTY_NAME);
						} else {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, nameSuffixStrings,
								countries, 
								this.updateOffenderRelationService.findStates(
									personFields.getBirthCountry()),
								this.updateOffenderRelationService
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
							this.updateOffenderRelationService
							.findCitiesByCountry(personFields
							.getBirthCountry()), 
							PERSON_FIELDS_PROPERTY_NAME);   
					}
				} else {
					// no city
					if (personFields.getBirthCountry() != null) {
						if (this.updateOffenderRelationService.hasStates(
							personFields.getBirthCountry())) {
							if (personFields.getBirthState() != null) {
								this.personFieldsControllerDelegate.
									prepareEditPersonFields(map,
									nameSuffixStrings, 
									countries, 
									this.updateOffenderRelationService
									.findStates(personFields.getBirthCountry()),
									this.updateOffenderRelationService
									.findCitiesByState(
										personFields.getBirthState()),
										PERSON_FIELDS_PROPERTY_NAME);
							} else {
								this.personFieldsControllerDelegate.
									prepareEditPersonFields(map, 
										nameSuffixStrings,
									countries, 
									this.updateOffenderRelationService
									.findStates(personFields
										.getBirthCountry()), 
									this.updateOffenderRelationService
									.findCitiesByCountryWithoutState(
									personFields.getBirthCountry()),
									PERSON_FIELDS_PROPERTY_NAME);
							}
						} else {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, nameSuffixStrings,
									countries, 
									null, this.updateOffenderRelationService
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
				= this.updateOffenderRelationService.findStates(null);
			List<City> cities 
				= this.updateOffenderRelationService.findCitiesByState(null);
			this.personFieldsControllerDelegate.prepareEditPersonFields(map, 
				nameSuffixStrings, countries, states, cities, 
				PERSON_FIELDS_PROPERTY_NAME);
		}

		List<RelationshipNoteCategory> noteCategories
			= this.updateOffenderRelationService
				.findDesignatedNoteCategories();
		
		this.onlineAccountFieldsControllerDelegate
			.prepareEditOnlineAccountFields(map, onlineAccountHosts);
		this.telephoneNumberFieldsControllerDelegate
			.prepareEditTelephoneNumberFields(map);
		this.offenderRelationshipNoteFieldsControllerDelegate
			.prepareEditOffenderRelationshipNotesFields(map, noteCategories);
		
		this.offenderSummaryModelDelegate.add(map,
			(Offender) relationship.getFirstPerson());
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	// Prepares redisplay edit model and view
	private ModelAndView prepareRedisplayEditMav(
		final EditRelationshipsForm editRelationshipsForm,
		final BindingResult result,
		final Relationship relationship) {
		final List<State> personFieldsBirthStates;
		final List<City> personFieldsBirthCities;
		if (relationship.getSecondPerson().getIdentity() != null) {
			personFieldsBirthStates = this.updateOffenderRelationService
				.findStates(relationship.getSecondPerson().getIdentity()
				.getBirthCountry());
			if (relationship.getSecondPerson().getIdentity().getBirthState()
				!= null) {
				personFieldsBirthCities = this.updateOffenderRelationService
				.findCitiesByState(relationship.getSecondPerson().getIdentity()
				.getBirthState());
			} else {
				personFieldsBirthCities = this.updateOffenderRelationService
				.findCitiesByCountry(relationship.getSecondPerson()
				.getIdentity().getBirthCountry());
			}
			
		} else {
			personFieldsBirthStates = Collections.emptyList();
			personFieldsBirthCities = Collections.emptyList();
		}
		ModelAndView mav = this.prepareEditMav(editRelationshipsForm, 
			personFieldsBirthStates, personFieldsBirthCities, relationship); 
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ EDIT_RELATIONSHIPS_FORM_MODEL_KEY, result);
		return mav;
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
	 * Displays the action menu for the specified offender's relationships 
	 * edit screen.
	 * 
	 * @param offender offender
	 * @return model and view for offender relationships action menu
	 */
	@RequestMapping(value = "offenderRelationEditActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView displayOffenderRelationshipsEditActionMenu(
		@RequestParam(value = "offender", required = true)
		final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(OFFENDER_RELATION_EDIT_ACTION_MENU_VIEW_NAME,
			map);
	}
	
	/**
	 * Returns the state options view with a collections of state for the
	 * specified country for person, poBox, address fields snippet.
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
			= this.updateOffenderRelationService.findStates(country);
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
				this.updateOffenderRelationService.findCitiesByState(state), 
				PERSON_FIELDS_NAME);
			} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
				return this.addressFieldsControllerDelegate.showCityOptions(
				this.updateOffenderRelationService.findCitiesByState(state),
				ADDRESS_FIELDS_NAME);
			} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
				return this.poBoxFieldsControllerDelegate.showCityOptions(
				this.updateOffenderRelationService.findCitiesByState(state),
				PO_BOX_FIELDS_NAME);
			} else {
				throw new UnsupportedOperationException(
					String.format("Fields name not supported %s",
					fieldsName));
			}
		} else {
			if (this.updateOffenderRelationService.hasStates(country)) {
				if (PERSON_FIELDS_NAME.equals(fieldsName)) {
					return this.personFieldsControllerDelegate
					.showCityOptions(this.updateOffenderRelationService
					.findCitiesByCountryWithoutState(country),
					PERSON_FIELDS_NAME);
				} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
					return this.addressFieldsControllerDelegate
					.showCityOptions(this.updateOffenderRelationService
					.findCitiesByCountryWithoutState(country),
					ADDRESS_FIELDS_NAME);
				} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
					return this.poBoxFieldsControllerDelegate.showCityOptions(
					this.updateOffenderRelationService
					.findCitiesByCountryWithoutState(country),
					PO_BOX_FIELDS_NAME);
				} else {
					throw new UnsupportedOperationException(
						String.format("Fields name not supported %s",
						fieldsName));
				}
			} else {
				if (PERSON_FIELDS_NAME.equals(fieldsName)) {
					return this.personFieldsControllerDelegate
					.showCityOptions(this.updateOffenderRelationService
					.findCitiesByCountry(country), PERSON_FIELDS_NAME);
				} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
					return this.addressFieldsControllerDelegate
					.showCityOptions(this.updateOffenderRelationService
					.findCitiesByCountry(country), ADDRESS_FIELDS_NAME);
				} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
					return this.poBoxFieldsControllerDelegate
					.showCityOptions(this.updateOffenderRelationService
					.findCitiesByCountry(country), PO_BOX_FIELDS_NAME);
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
			this.updateOffenderRelationService.findZipCodes(city),
			ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poBoxFieldsControllerDelegate.showZipCodeOptions(
			this.updateOffenderRelationService.findZipCodes(city),
			PO_BOX_FIELDS_NAME);
		} else {
			throw new UnsupportedOperationException(
				String.format("Fields name not supported %s",
				fieldsName));
		}
	}
	
	/**
	 * Returns a view for telephone number action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for telephone number action menu
	 */
	@RequestMapping(value = "/telephoneNumberEditActionMenu.html",
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
		value = "/addEditOffenderRelationshipTelephoneNumberItem.html", 
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
		return new ModelAndView(EDIT_TELEPHONE_NUMBER_TABLE_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Adds an online account item.
	 * 
	 * @param onlineAccountItemIndex online account item index
	 * @return model and view for an email item view
	 */
	@RequestMapping(value = "addEditOffenderRelationshipOnlineAccountItem.html",
		method = RequestMethod.GET)
	public ModelAndView addEmailItem(@RequestParam(
		value = "onlineAccountItemIndex", required = true)
		final int onlineAccountItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY, 
			onlineAccountItemIndex); 
		List<OnlineAccountHost> onlineAccountHosts 
			= this.updateOffenderRelationService.findOnlineAccountHosts();
		map.addAttribute(ONLINE_ACCOUNT_HOSTS_MODEL_KEY, onlineAccountHosts);
		OnlineAccountContactItem onlineAccountContactItem 
			= new OnlineAccountContactItem(); 
		onlineAccountContactItem.setOperation(
			OnlineAccountContactItemOperation.CREATE);
		map.addAttribute(ONLINE_ACCOUNT_CONTACT_ITEM_MODEL_KEY, 
			onlineAccountContactItem);
		return new ModelAndView(EDIT_ONLINE_ACCOUNT_TABLE_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns new offender relationship note item.
	 * 
	 * @param offenderRelationshipNoteItemIndex offender relationship note
	 * item index
	 * @return new offender relationship note item
	 */
	@RequestMapping(value = "createRelationshipNoteItem.html",
			method = RequestMethod.GET)
	public ModelAndView createRelationshipNoteItem(
			@RequestParam(
					value = "offenderRelationshipNoteItemIndex",
					required = true)
				final Integer offenderRelationshipNoteItemIndex) {
		List<RelationshipNoteCategory> noteCategories
			= this.updateOffenderRelationService.findDesignatedNoteCategories();
		OffenderRelationshipNoteItem offenderRelationshipNoteItem
			= new OffenderRelationshipNoteItem();
		offenderRelationshipNoteItem.setOperation(
				OffenderRelationshipNoteItemOperation.CREATE);
		ModelAndView mav = new ModelAndView(
				CREATE_RELATIONSHIP_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_ITEM_INDEX_MODEL_KEY,
				offenderRelationshipNoteItemIndex);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_ITEM_MODEL_KEY,
				offenderRelationshipNoteItem);
		mav.addObject(OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME_MODEL_KEY,
				OFFENDER_RELATIONSHIP_NOTE_ITEMS_FIELD_NAME);
		mav.addObject(BASE_URL_MODEL_KEY, BASE_URL);
		this.offenderRelationshipNoteFieldsControllerDelegate
			.prepareEditOffenderRelationshipNotesFields(
					mav.getModelMap(), noteCategories);
		return mav;
	}
	
	/**
	 * Returns a view for online account action menu pertaining to the specified
	 * offender.
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
	
	/**
	 * Shows action menu for notes.
	 * 
	 * @return action menu for notes
	 */
	@RequestMapping(
			value = "/noteItemsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView noteItemsActionMenu() {
		ModelAndView mav = new ModelAndView(NOTES_ACTION_MENU_VIEW_NAME);
		return mav;
	}
	
	/**
	 * Remove this relationships.
	 * 
	 * @param relationship relationship
	 * @return model and view to redirect to appropriate update screen
	 */
	@RequestMapping(value = "remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('OFFENDER_RELATIONSHIP_REMOVE')")
	public ModelAndView remove(
		@RequestParam(value = "relationship", required = true)
		final Relationship relationship) {
		this.updateOffenderRelationService.removeRelationship(
			(Offender) (relationship.getFirstPerson()), 
			relationship.getSecondPerson());
		return new ModelAndView(String.format(LIST_REDIRECT, 
				((Offender) (relationship.getFirstPerson())).getId()));
	}
	
	/**
	 * Returns the report for family member details.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @param familyAssociation family association
	 * @return response entity with report
	 */
	@RequestMapping(value = "/familyAssociationDetailsReport.html",
			method = RequestMethod.GET)
	public ResponseEntity<byte []> reportFamilyMemberDetails(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat,
			@RequestParam(value = "familyAssociation", required = true)
			final FamilyAssociation familyAssociation) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FAMILY_MEMBER_DETAILS_ID_REPORT_PARAM_NAME,
			Long.toString(familyAssociation.getId()));
		byte[] doc = this.reportRunner.runReport(
			FAMILY_ASSOCIATION_DETAILS_REPORT_NAME,
			reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
			doc, reportFormat);
	}
	
	// Returns whether the current user has the specified role
		private boolean hasRole(final String role) {
			for (GrantedAuthority authority :
					SecurityContextHolder.getContext().getAuthentication()
						.getAuthorities()) {
				if (role.equals(authority.getAuthority())) {
					return true;
				}
			}
			return false;
		}	
	
	/**
	 * Handles {@code PersonNameExistsException}.
	 * 
	 * @param personNameExistsException person name exists exception
	 * @return screen to handle {@code PersonNameExistsException}
	 */
	@ExceptionHandler(PersonNameExistsException.class)
	public ModelAndView handlePersonNameExistsException(
		final PersonNameExistsException personNameExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			PERSON_NAME_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, personNameExistsException);
	}
	
	/**
	 * Handles {@code PersonIdentityExistsException}.
	 * 
	 * @param personIdentityExistsException person identity exists exception
	 * @return screen to handle {@code PersonIdentityExistsException}
	 */
	@ExceptionHandler(PersonIdentityExistsException.class)
	public ModelAndView handlePersonIdentityExistsException(
		final PersonIdentityExistsException personIdentityExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			PERSON_IDENTITY_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, personIdentityExistsException);
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
	 * Handles {@code CityExistsException}.
	 * 
	 * @param cityExistsException city exists exception thrown
	 * @return screen to handle {@code CityExistsException}
	 */
	@ExceptionHandler(CityExistsException.class)
	public ModelAndView handleCityExistsException(
		final CityExistsException cityExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			CITY_EXISTS_EXCEPTION_MESSAGE_KEY, ERROR_BUNDLE_NAME,
			cityExistsException);
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
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(Person.class,
			this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(StreetSuffix.class,
			this.streetSuffixPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(AddressUnitDesignator.class,
			this.addressUnitDesignatorPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(Country.class,
			this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
			this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
			this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class,
			this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccountHost.class,
			this.onlineAccountHostPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Address.class,
			this.addressPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TelephoneNumber.class,
			this.telephoneNumberPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccount.class,
			this.onlineAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(RelationshipNote.class,
			this.relationshipNotePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(RelationshipNoteCategory.class,
			this.relationshipNoteCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Relationship.class,
			this.relationshipPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(FamilyAssociation.class,
			this.familyAssociationPropertyEditorFactory.createPropertyEditor());
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