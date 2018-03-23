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
package omis.family.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import omis.address.exception.ZipCodeExistsException;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.address.web.form.AddressFields;
import omis.audit.domain.VerificationMethod;
import omis.audit.domain.VerificationSignature;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.contact.domain.Contact;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountCategory;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.ContactExistsException;
import omis.contact.report.ContactReportService;
import omis.contact.report.ContactSummary;
import omis.contact.web.controller.delegate.ContactSummaryModelDelegate;
import omis.contact.web.controller.delegate.OnlineAccountFieldsControllerDelegate;
import omis.contact.web.controller.delegate.PoBoxFieldsControllerDelegate;
import omis.contact.web.controller.delegate.TelephoneNumberFieldsControllerDelegate;
import omis.contact.web.form.PoBoxFields;
import omis.country.domain.Country;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.domain.FamilyAssociationNote;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.family.exception.FamilyAssociationNoteExistsException;
import omis.family.report.FamilyAssociationReportService;
import omis.family.service.FamilyAssociationService;
import omis.family.web.form.FamilyAssociationForm;
import omis.family.web.form.FamilyAssociationNoteItem;
import omis.family.web.form.FamilyAssociationNoteItemOperation;
import omis.family.web.form.FamilyAssociationOnlineAccountItem;
import omis.family.web.form.FamilyAssociationOnlineAccountItemOperation;
import omis.family.web.form.FamilyAssociationTelephoneNumberItem;
import omis.family.web.form.FamilyAssociationTelephoneNumberItemOperation;
import omis.family.web.validator.FamilyAssociationFieldsEditValidator;
import omis.family.web.validator.FamilyAssociationFormValidator;
import omis.location.domain.Location;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.web.delegate.PersonFieldsControllerDelegate;
import omis.person.web.form.PersonFields;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.user.domain.UserAccount;
import omis.util.StringUtility;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for Family.
 * 
 * @author Ryan Johns
 * @author Jason Nelson
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.5 (Dec 11, 2017)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/family")
@PreAuthorize("hasRole('USER')")
public class ManageFamilyAssociationController {
	/* views */
	private static final String EDIT_VIEW_NAME = "family/edit";
	private static final String FAMILY_ASSOCIATION_ACTION_MENU_VIEW_NAME
		= "family/includes/familyAssociationActionMenu";
	private static final String FAMILY_ASSOCIATION_NOTES_ACTION_MENU_VIEW_NAME
		= "family/includes/familyAssociationNotesActionMenu";
	private static final String NOTE_ITEM_VIEW_NAME 
		= "family/includes/noteItem";
	private static final String TELEPHONE_NUMBERS_ACTION_MENU_VIEW_NAME
		= "family/includes/telephoneNumbersActionMenu";
	private static final String EMAILS_ACTION_MENU_VIEW_NAME
		= "family/includes/emailsActionMenu";
	private static final String ADDRESSES_VIEW_NAME
		= "address/json/addresses";
	private static final String 
		CREATE_FAMILY_ASSOCIATION_TELEPHONE_NUMBER_TABLE_ROW_VIEW_NAME 
		= "family/includes/createFamilyAssociationTelephoneNumberTableRow";
	private static final String 
		CREATE_FAMILY_ASSOCIATION_ONLINE_ACCOUNT_TABLE_ROW_VIEW_NAME 
		= "family/includes/createFamilyAssociationOnlineAccountTableRow";
	
	/* model keys */
	private static final String NAME_SUFFIXES_MODEL_KEY = "nameSuffixes";
	private static final String HOME_TYPES_MODEL_KEY = "homeTypes";
	private static final String COUNTRIES_MODEL_KEY = "countries";
	private static final String BIRTH_COUNTRIES_MODEL_KEY = "birthCountries";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String FAMILY_ASSOCIATION_EDIT_FORM_MODEL_KEY 
		= "familyAssociationForm"; 
	private static final String FAMILY_ASSOCIATION_MODEL_KEY
		= "familyAssociation";
	private static final 
		String FAMILY_ASSOCIATION_TELEPHONE_NUMBER_ITEMS_MODEL_KEY
		= "familyAssociationTelephoneNumberItems";
	private static final String FAMILY_ASSOCIATION_CATEGORIES_MODEL_KEY
		= "categories";
	private static final String FAMILY_ASSOCIATION_NOTE_INDEX_MODEL_KEY
		= "familyAssociationNoteIndex";
	private static final String FAMILY_ASSOCIATION_FIELDS_MODEL_KEY 
		= "familyAssociationFields";
	private static final String ONLINE_ACCOUNT_HOSTS_MODEL_KEY 
		= "onlineAccountHosts";
	private static final String TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY 
		= "familyAssociationTelephoneNumberIndex";
	private static final String TELEPHONE_NUMBER_CATEGORY_MODEL_KEY
		= "telephoneNumberCategories";
	private static final String EMAIL_ITEM_INDEX_MODEL_KEY 
		= "familyAssociationOnlineAccountIndex";
	private static final String FAMILY_ASSOCIATION_EMAIL_ITEMS_MODEL_KEY
		= "familyAssociationOnlineAccountItems";
	private static final 
		String FAMILY_ASSOCIATION_TELEPHONE_NUMBER_ITEM_MODEL_KEY
		= "familyAssociationTelephoneNumberItem";
	private static final String FAMILY_ASSOCIATION_EMAIL_ITEM_MODEL_KEY
		= "familyAssociationOnlineAccountItem";
	private static final String FAMILY_ASSOCIATION_NOTE_ITEM_MODEL_KEY
		= "familyAssociationNoteItem";
	private static final String FAMILY_ASSOCIATION_NOTE_ITEMS_MODEL_KEY
		= "familyAssociationNoteItems";
	private static final String CREATE_FLAG_MODEL_KEY = "createFlag";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MSG1 
		= "Sex selection is required when Relationship is Son or Daughter";
	private static final String SEXES_MODEL_KEY = "sexes";
	private static final String ADDRESSES_MODEL_KEY
		= "addresses";
	private static final String PO_BOX_COUNTRIES_MODEL_KEY = "poBoxContries";
	private static final String FAMILY_MEMBER_MODEL_KEY = "familyMember";
	private static final String EXISTING_FAMILY_MEMBER_MODEL_KEY 
		= "existingFamilyMember";
	private static final String FAMILY_ASSOCIATION_EXISTS_MESSAGE_KEY
		= "familyAssociation.exists";
	private static final String FAMILY_ASSOCIATION_CONFLICT_MESSAGE_KEY
		= "familyAssociation.conflicts";
	private static final String EXISTING_CONTACT_SUMMARY_MODEL_KEY
		= "existingContactSummary";
	private static final String RESIDENCE_TREM_EXISTS_EXCEPTION_MESSAGE_KEY
		= "residenceTerm.Exists";
	private static final String CONTACT_EXISTS_EXCEPTION_MESSAGE_KEY
		= "contact.Exists";
	private static final String ZIPCODE_EXISTS_EXCEPTION_MESSAGE_KEY
		= "zipCode.Exists";
	
	/* Message bundles. */
	private static final String ERROR_BUNDLE_NAME = "omis.family.msgs.form";
	
				
	/* Redirects. */
	private static final String LIST_REDIRECT
		= "redirect:/family/list.html?offender=%d";
	
	/* Property Name */
	private static final String ADDRESS_FIELDS_PROPERTY_NAME = "addressFields";
	private static final String PO_BOX_FIELDS_PROPERTY_NAME = "poBoxFields";
	private static final String PERSON_FIELDS_PROPERTY_NAME = "personFields";
	
	/* Fields names. */
	private static final String PERSON_FIELDS_NAME = "personFields";
	private static final String ADDRESS_FIELDS_NAME	= "addressFields";
	private static final String PO_BOX_FIELDS_NAME = "poBoxFields";
	
	/* Property editor. */
	@Autowired
	@Qualifier("countryPropertyEditorFactory")
	private PropertyEditorFactory countryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory1;
	
	@Autowired
	@Qualifier("statePropertyEditorFactory")
	private PropertyEditorFactory statePropertyEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("verificationMethodPropertyEditorFactory")
	private PropertyEditorFactory verificationMethodPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("userAccountPropertyEditorFactory")
	private PropertyEditorFactory userAccountPropertyEditorFactory;
	
	@Autowired
	@Qualifier("familyAssociationCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
	 	familyAssociationCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("cityPropertyEditorFactory")
	private PropertyEditorFactory cityPropertyEditorFactory;
	
	@Autowired
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("streetSuffixPropertyEditorFactory")
	private PropertyEditorFactory streetSuffixPropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressUnitDesignatorPropertyEditorFactory")
	private PropertyEditorFactory addressUnitDesignatorPropertyEditorFactory;
	
	@Autowired
	@Qualifier("familyAssociationNotePropertyEditorFactory")
	private PropertyEditorFactory familyAssociationNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("onlineAccountHostPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountHostPropertyEditorFactory;
	
	@Autowired
	@Qualifier("familyAssociationPropertyEditorFactory")
	private PropertyEditorFactory familyAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("sexPropertyEditorFactory")
	private PropertyEditorFactory sexPropertyEditorFactory;
	
	@Autowired
	@Qualifier("telephoneNumberPropertyEditorFactory")
	private PropertyEditorFactory telephoneNumberPropertyEditorFactory;
	
	/* Services. */
	@Autowired
	@Qualifier("familyAssociationService")
	private FamilyAssociationService familyAssociationService;
	
	@Autowired
	@Qualifier("familyAssociationReportService")
	private FamilyAssociationReportService familyAssociationReportService;
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	@Autowired
	@Qualifier("contactReportService")
	private ContactReportService contactReportService;
	
	/* Delegate */
	@Autowired
	@Qualifier("contactSummaryModelDelegate")
	private ContactSummaryModelDelegate contactSummaryModelDelegate;
	
	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("poBoxFieldsControllerDelegate")
	private PoBoxFieldsControllerDelegate poboxFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("personFieldsControllerDelegate")
	private PersonFieldsControllerDelegate personFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
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
	
	/* Validator */
	@Autowired
	@Qualifier("familyAssociationFormValidator")
	private FamilyAssociationFormValidator familyAssociationFormValidator;
	
	@Autowired
	@Qualifier("familyAssociationFieldsEditValidator")
	private FamilyAssociationFieldsEditValidator 
		familyAssociationFieldsEditValidator;
	
	@Autowired
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;	
	
	
	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Constructor. */
	/** Instantiates a default employment controller. */
	public ManageFamilyAssociationController() {
		// Default instantiation
	}
	
	/**
	 * Displays screen to create a new family association.
	 * 
	 * @param offender offender for whom to create a new family association
	 * @param familyMember family member
	 * @return model and view to create a new family association
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(value = "offender", 
		required = true) final Offender offender,
		@RequestParam(value = "familyMember", required = false) 
		final Person familyMember) {
		FamilyAssociationForm familyAssociationFields 
			= new FamilyAssociationForm();		
		if (familyMember == null) {  
			// Create new family association
			familyAssociationFields.setAddressOperation(
					FamilyAddressOperation.EXISTING);
			List<FamilyAssociationNoteItem> familyAssociationNoteItems = null;
			List<FamilyAssociationTelephoneNumberItem> 
				familyAssociationTelephoneNumberItems 
				= new ArrayList<FamilyAssociationTelephoneNumberItem>();
			List<FamilyAssociationOnlineAccountItem> 
				familyAssociationEmailItems 
				= new ArrayList<FamilyAssociationOnlineAccountItem>();
			int familyAssociationNoteIndex = 0;
			int familyAssociationTelephoneNumberIndex = 0;
			int familyAssociationEmailIndex = 0;
			Boolean createFlag = true;			
			State homeState = this.familyAssociationService.findHomeState();
			if (homeState != null) {
				AddressFields addressFields = new AddressFields();
				addressFields.setCountry(homeState.getCountry());
				familyAssociationFields.setAddressFields(addressFields);
				PersonFields personFields = new PersonFields();
				personFields.setBirthCountry(homeState.getCountry());
				familyAssociationFields.setPersonFields(personFields);
				PoBoxFields poBoxFields = new PoBoxFields();
				poBoxFields.setCountry(homeState.getCountry());
				familyAssociationFields.setPoBoxFields(poBoxFields);
			}			
			return this.prepareEditMav(familyAssociationFields,  offender, 
				null, familyAssociationNoteItems, 
				familyAssociationTelephoneNumberItems, 
				familyAssociationEmailItems,
				familyAssociationNoteIndex,	
				familyAssociationTelephoneNumberIndex,
				familyAssociationEmailIndex, createFlag,
				familyMember);
		} else {  
			// Create family association with existing .....
			familyAssociationFields.setAddressOperation(
				FamilyAddressOperation.EXISTING);
			FamilyAssociation familyAssociation = null;
			Boolean createFlag = true;			
			FamilyAssociationForm familyAssociationForm
				= new FamilyAssociationForm();		
			/* Set Address section */
			Contact contact = this.familyAssociationService.findContactByPerson(
				familyMember);
			AddressFields addressFields = new AddressFields();
			if (contact != null) {
				if (contact.getMailingAddress() != null) {
					familyAssociationForm.setHomeType(contact
							.getMailingAddress().getBuildingCategory());
					familyAssociationForm.setSecondAddressDesignator(contact
						.getMailingAddress().getDesignator());
					addressFields.setZipCode(contact.getMailingAddress()
						.getZipCode());
					addressFields.setValue(contact.getMailingAddress()
						.getValue());
					if (contact.getMailingAddress().getZipCode() != null) {
						addressFields.setZipCodeExtension(contact
							.getMailingAddress().getZipCode().getExtension());
						addressFields.setCity(contact.getMailingAddress()
							.getZipCode().getCity());
						if (contact.getMailingAddress().getZipCode()
								.getCity() != null) {
							addressFields.setCountry(contact.getMailingAddress()
								.getZipCode().getCity().getCountry());
							addressFields.setState(contact.getMailingAddress()
								.getZipCode().getCity().getState());
						}
					}
				}
			}
			familyAssociationForm.setAddressFields(addressFields);			
			/* Set section post office box */
			PoBoxFields poBoxFields = new PoBoxFields();
			poBoxFields.setNewZipCode(true);
			if (contact != null) {
				if (contact.getPoBox() != null) {
					poBoxFields.setPoBoxValue(contact.getPoBox().getValue());
					if (contact.getPoBox().getZipCode() != null) {
						poBoxFields.setCity(contact.getPoBox().getZipCode()
							.getCity());
						if (contact.getPoBox().getZipCode().getCity() != null) {
							poBoxFields.setCountry(contact.getPoBox()
								.getZipCode().getCity().getCountry());
							poBoxFields.setState(contact.getPoBox().getZipCode()
								.getCity().getState());
						}
					}
					if (contact.getMailingAddress() != null) {
						poBoxFields.setZipCode(contact.getMailingAddress()
							.getZipCode());
					}
				}
			}
			familyAssociationForm.setPoBoxFields(poBoxFields);			
			/* Set section Phone */
			List<FamilyAssociationTelephoneNumberItem> 
				familyAssociationTelephoneNumberItems = 
				new ArrayList<FamilyAssociationTelephoneNumberItem>();
			for (TelephoneNumber telephoneNumber : this.familyAssociationService
				.findTelephoneNumbersByContact(contact)) {
				FamilyAssociationTelephoneNumberItem item 
					= new FamilyAssociationTelephoneNumberItem();
				item.setExtension(telephoneNumber.getExtension());
				item.setOperation(
					FamilyAssociationTelephoneNumberItemOperation.EDIT);
				item.setPhoneNumber(telephoneNumber.getValue());
				item.setPhoneType(telephoneNumber.getCategory());
				item.setPrimary(resolveCheckBoxValue(
						telephoneNumber.getPrimary()));
				item.setPrimary(resolveCheckBoxValue(
						telephoneNumber.getActive()));
				familyAssociationTelephoneNumberItems.add(item);
			}
			familyAssociationForm.setFamilyAssociationTelephoneNumberItems(
				familyAssociationTelephoneNumberItems);
			int familyAssociationTelephoneNumberIndex
				= familyAssociationTelephoneNumberItems.size();			
			/* Set section Email */
			List<FamilyAssociationOnlineAccountItem> 
				familyAssociationEmailItems 
				= new ArrayList<FamilyAssociationOnlineAccountItem>();
			for (OnlineAccount onlineAccount 
				: this.familyAssociationService.findOnlineAccountsByContact(
				contact)) {
				FamilyAssociationOnlineAccountItem item 
					= new FamilyAssociationOnlineAccountItem();
				item.setEmail(onlineAccount.getName());
				item.setOnlineAccountHost(onlineAccount.getHost());
				item.setOperation(FamilyAssociationOnlineAccountItemOperation
						.UPDATE);
				item.setPrimary(resolveCheckBoxValue(
						onlineAccount.getPrimary()));
				item.setPrimary(resolveCheckBoxValue(
						onlineAccount.getActive()));
				familyAssociationEmailItems.add(item);
			}	
			familyAssociationForm.setFamilyAssociationOnlineAccountItems(
				familyAssociationEmailItems); 
			int familyAssociationEmailIndex 
				= familyAssociationEmailItems.size();
			/* Set note section */
			List<FamilyAssociationNoteItem> familyAssociationNoteItems 
				= new ArrayList<FamilyAssociationNoteItem>();
			for (FamilyAssociationNote familyAssociationNote 
				: this.familyAssociationService.findNotesByAssociation(
				familyAssociation)) {
				FamilyAssociationNoteItem item 
					= new FamilyAssociationNoteItem();
				item.setDate(familyAssociationNote.getDate());
				item.setNote(familyAssociationNote.getValue());
				item.setOperation(FamilyAssociationNoteItemOperation.CREATE);
				familyAssociationNoteItems.add(item);
			}			
			familyAssociationForm
				.setFamilyAssociationNoteItems(familyAssociationNoteItems);
			/*int familyAssociationNoteIndex = familyAssociationNoteItems
			 * .size()+1;*/
			int familyAssociationNoteIndex = familyAssociationNoteItems.size();
			return this.prepareEditMav(familyAssociationForm,  offender, 
				familyAssociation, familyAssociationNoteItems, 
				familyAssociationTelephoneNumberItems, 
				familyAssociationEmailItems, familyAssociationNoteIndex, 
				familyAssociationTelephoneNumberIndex, 
				familyAssociationEmailIndex, createFlag, familyMember);
		}
	}
	
	/**
	 * Saves a new created family association record.
	 * 
	 * @param offender offender
	 * @param associatedPerson associated person
	 * @param familyAssociationForm family association form
	 * @param result binding result
	 * @return redirect to list employment history by offender
	 * @throws DuplicateEntityFoundException if the vehicle association exists
	 * @throws ReflexiveRelationshipException 
	 * @throws ResidenceStatusConflictException 
	 * @throws PrimaryResidenceExistsException 
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws FamilyAssociationExistsException 
	 * @throws FamilyAssociationNoteExistsException family association note
	 * exists exception
	 * @throws ResidenceTermExistsException residence term exists exception
	 */
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "familyMember", required = false)
		final Person associatedPerson,
		final FamilyAssociationForm familyAssociationForm,
		final BindingResult result) throws DuplicateEntityFoundException, 
			ReflexiveRelationshipException, PrimaryResidenceExistsException, 
			ResidenceStatusConflictException,
			FamilyAssociationConflictException,
			FamilyAssociationExistsException,
			FamilyAssociationNoteExistsException,
			ResidenceTermExistsException, ContactExistsException {
		if (associatedPerson == null) {
			// Create new
			if (familyAssociationForm.getPoBoxFields() != null
				&& this.familyAssociationService.hasStates(familyAssociationForm
					.getPoBoxFields().getCountry())) {
			    familyAssociationForm.getPoBoxFields().setHasState(true);
			} 
			if (familyAssociationForm.getPoBoxFields() != null
				&& !this.familyAssociationService.hasStates(
						familyAssociationForm
					.getPoBoxFields().getCountry())) {
				familyAssociationForm.getPoBoxFields().setHasState(false);
			}			
			int familyAssociationNoteIndex = 0;
			if (familyAssociationForm.getFamilyAssociationNoteItems() != null) {
				List<FamilyAssociationNoteItem> familyAssociationNoteItems 
					= new ArrayList<FamilyAssociationNoteItem>();
				for (FamilyAssociationNoteItem noteItem 
						: familyAssociationForm
						 	.getFamilyAssociationNoteItems()) {
					if (FamilyAssociationNoteItemOperation.CREATE.equals(
						noteItem.getOperation())) {
						familyAssociationNoteItems.add(noteItem);
					}
				}
				familyAssociationForm.setFamilyAssociationNoteItems(
					familyAssociationNoteItems);
				familyAssociationNoteIndex = familyAssociationForm
						.getFamilyAssociationNoteItems().size();
			} 
			int familyAssociationTelephoneNumberIndex = 0;
			if (familyAssociationForm
					.getFamilyAssociationTelephoneNumberItems() != null) {
				List<FamilyAssociationTelephoneNumberItem> telephoneNumberItems 
					= new ArrayList<FamilyAssociationTelephoneNumberItem>();
				for (FamilyAssociationTelephoneNumberItem telephoneNumberItem 
						: familyAssociationForm
						.getFamilyAssociationTelephoneNumberItems()) {
					if (FamilyAssociationTelephoneNumberItemOperation.CREATE
						.equals(telephoneNumberItem.getOperation())) {
						telephoneNumberItems.add(telephoneNumberItem);
					}
				}
				familyAssociationForm.setFamilyAssociationTelephoneNumberItems(
						telephoneNumberItems);
				familyAssociationTelephoneNumberIndex = familyAssociationForm
				.getFamilyAssociationTelephoneNumberItems().size();
			} 
			int familyAssociationEmailIndex = 0;
			if (familyAssociationForm
					.getFamilyAssociationOnlineAccountItems() != null) {
				List<FamilyAssociationOnlineAccountItem> onlineAccountItems 
					= new ArrayList<FamilyAssociationOnlineAccountItem>();
				for (FamilyAssociationOnlineAccountItem onlineAccountItem 
						: familyAssociationForm
						.getFamilyAssociationOnlineAccountItems()) {
					if (FamilyAssociationOnlineAccountItemOperation.CREATE
						.equals(onlineAccountItem.getOperation())) {
						onlineAccountItems.add(onlineAccountItem);
					}
				}
				familyAssociationForm.setFamilyAssociationOnlineAccountItems(
						onlineAccountItems);
				familyAssociationEmailIndex = familyAssociationForm
				.getFamilyAssociationOnlineAccountItems().size();
			}			
			this.familyAssociationFormValidator.validate(familyAssociationForm,
				result);
			if (result.hasErrors()) {
				Boolean createFlag = true;
				return this.prepareRedisplayEditMav(familyAssociationForm, 
					familyAssociationForm.getFamilyAssociationNoteItems(),
					offender, null, null, 
					familyAssociationForm
						.getFamilyAssociationTelephoneNumberItems(),
					familyAssociationForm
						.getFamilyAssociationOnlineAccountItems(),
					familyAssociationNoteIndex, result, 
					familyAssociationTelephoneNumberIndex,
					familyAssociationEmailIndex, createFlag, 
					associatedPerson);		
			}
//			if((familyAssociationForm.getCategory()!=null
//				&&FamilyAssociationCategoryClassification.CHILD.equals(
//					familyAssociationForm.getCategory().getClassification()))
//				&&((familyAssociationForm.getPersonFields()
//			.getBirthDate()==null)
//				||(familyAssociationForm.getPersonFields().getSex()==null))){
//				throw new IllegalArgumentException(
//			ILLEGAL_ARGUMENT_EXCEPTION_MSG1);
//			}
			if ((familyAssociationForm.getCategory() != null)) {
				if (FamilyAssociationCategoryClassification.CHILD.equals(
						familyAssociationForm.getCategory().getClassification())
							&& familyAssociationForm.getPersonFields()
								.getSex() == null) {
					throw new IllegalArgumentException(
								ILLEGAL_ARGUMENT_EXCEPTION_MSG1);
				}
			}			
			VerificationSignature verificationSignature 
				= new VerificationSignature(
				familyAssociationForm.getVerifiedByUserAccount(),
				familyAssociationForm.getVerificationDate(),
				familyAssociationForm.getVerified(),
				familyAssociationForm.getVerificationMethod());
			
			Boolean personFieldsNewCity = false;
			Boolean addressFieldsNewCity = false;
			Boolean poBoxFieldsNewCity = false;
			
			if	(familyAssociationForm.getPersonFields().getNewCity()) {
				personFieldsNewCity = true;
			}
			
			if (familyAssociationForm.getEnterAddress() != null       
					// Enter address check box checked
				&& familyAssociationForm.getEnterAddress()) {
				if (FamilyAddressOperation.NEW.equals(familyAssociationForm
					.getAddressOperation()) && familyAssociationForm
					.getAddressFields().getNewCity()) {
					addressFieldsNewCity = true;
				}
			}
			
			if (familyAssociationForm.getEnterPoBox() != null     
					// Enter po box check box checked
				&& familyAssociationForm.getEnterPoBox()) {
				if (familyAssociationForm.getPoBoxFields().getNewCity()) {
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
			if (personFieldsNewCity) {   
				// person fields create new city
				personFieldsNewCityName = familyAssociationForm
					.getPersonFields().getCityName();
				personFieldsCountryName = familyAssociationForm
					.getPersonFields().getBirthCountry().getName();
				if (familyAssociationForm.getPersonFields()
						.getBirthState() != null) {
					personFieldsStateName = familyAssociationForm
							.getPersonFields().getBirthState().getName();
				} else {
					personFieldsStateName = null;
				} 			
			} else { 
				// person fields not create new city
				personFieldsNewCityName = null;
				if (familyAssociationForm.getPersonFields()
						.getBirthCountry() != null)	{
					personFieldsCountryName = familyAssociationForm
						.getPersonFields().getBirthCountry().getName();
					if (familyAssociationForm.getPersonFields().getBirthState()
						!= null) {
						personFieldsStateName = familyAssociationForm
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
				addressFieldsNewCityName = familyAssociationForm
					.getAddressFields().getCityName();
				addressFieldsCountryName = familyAssociationForm
					.getAddressFields().getCountry().getName();
				if (familyAssociationForm.getAddressFields()
						.getState() != null) {
					addressFieldsStateName = familyAssociationForm
					.getAddressFields().getState().getName();
				} else {
					addressFieldsStateName = null;
				}
			} else { 
				// Address fields not creates new city
				addressFieldsNewCityName = null;
				if (familyAssociationForm.getAddressFields()
						.getCountry() != null) {
					addressFieldsCountryName = familyAssociationForm
						.getAddressFields().getCountry().getName();
					if (familyAssociationForm.getAddressFields()
							.getState() != null) {
						addressFieldsStateName = familyAssociationForm
							.getAddressFields().getState().getName();
					} else {
						addressFieldsStateName = null;
					}
				} else {
					addressFieldsCountryName = null;
					addressFieldsStateName = null;
				}
			}
			if (poBoxFieldsNewCity) {  
				// Po box creates new city 
				poBoxFieldsNewCityName = familyAssociationForm.getPoBoxFields()
					.getCityName();
				poBoxFieldsCountryName = familyAssociationForm.getPoBoxFields()
					.getCountry().getName();
				if (familyAssociationForm.getPoBoxFields().getState() != null) {
					poBoxFieldsStateName 
						= familyAssociationForm.getPoBoxFields()
					.getState().getName();
				} else {
					poBoxFieldsStateName = null;
				}
			} else {  
				// Po box not creates new city
				poBoxFieldsNewCityName = null;
				if (familyAssociationForm.getPoBoxFields()
						.getCountry() != null) {
					poBoxFieldsCountryName = familyAssociationForm
						.getPoBoxFields().getCountry().getName();
					if (familyAssociationForm.getPoBoxFields()
							.getState() != null) {
						poBoxFieldsStateName = familyAssociationForm
							.getPoBoxFields().getState().getName();
					} else {
						poBoxFieldsStateName = null;
					}
				} else {
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
			if (familyAssociationForm.getEnterAddress() != null   
					// Enter address check box checked
				&& familyAssociationForm.getEnterAddress()) {
				if (FamilyAddressOperation.NEW.equals(familyAssociationForm
					.getAddressOperation()) && familyAssociationForm
					.getAddressFields().getNewZipCode()) {
					addressFieldsNewZipCode = true;
					addressFieldsZipCodeValue = familyAssociationForm
						.getAddressFields().getZipCodeValue();
					addressFieldsZipCodeExtension = familyAssociationForm
						.getAddressFields().getZipCodeExtension();
				} else {
					addressFieldsZipCodeValue = null;
					addressFieldsZipCodeExtension = null;
				}
			} else {  
				// Enter address check box unchecked
				addressFieldsZipCodeValue = null;
				addressFieldsZipCodeExtension = null;
			}
			if (familyAssociationForm.getEnterPoBox() != null  
					// Enter po box check box checked
				&& familyAssociationForm.getEnterPoBox()) { 
				if (familyAssociationForm.getPoBoxFields().getNewZipCode()) {
					poBoxFieldsNewZipCode = true;
					poBoxFieldsZipCodeValue 
					 	= familyAssociationForm.getPoBoxFields()
						.getZipCodeValue();
					poBoxFieldsZipCodeExtension = familyAssociationForm
						.getPoBoxFields().getZipCodeExtension();
				} else {
					poBoxFieldsZipCodeValue = null;
					poBoxFieldsZipCodeExtension = null;
				}
			} else {
				// Enter po box check box unchecked
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
					// If all new cities in address, po box and 
					//person fields are the same
					City newCreatedCity = this.familyAssociationService
							.createCity(
									// Create new city for all fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = true;
				} else if ((addressFieldsNewCityName.equals(
						poBoxFieldsNewCityName)) 
						// If poBox and address fields new cities are the same, 
						//but different from person fields  
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
					|| ((addressFieldsStateName != null 
					&& personFieldsStateName != null 
					&& !addressFieldsStateName.equals(personFieldsStateName))
						|| (addressFieldsStateName != null 
						&& personFieldsStateName == null)
						|| (addressFieldsStateName == null 
						&& personFieldsStateName != null)))) {
					City newCreatedCity = this.familyAssociationService
							.createCity(
							// Create new city for address and poBox fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = this.familyAssociationService
						.createCity(personFieldsNewCityName, 
								familyAssociationForm.getPersonFields()
								.getBirthState(), familyAssociationForm
								.getPersonFields().getBirthCountry());
					addressPoBoxFieldsNewCity = true;
				} else if ((addressFieldsNewCityName.equals(
						personFieldsNewCityName)) 
						// If person and address fields new cities are the same,
						//but different from po box fields  
					&& (addressFieldsCountryName.equals(
							personFieldsCountryName))
					&& (addressFieldsStateName != null 
					&& personFieldsStateName != null 
					&& addressFieldsStateName.equals(personFieldsStateName)
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
					City newCreatedCity = this.familyAssociationService
							.createCity(
							// Create new city for address and person fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedPoBoxFieldsCity = this.familyAssociationService
						.createCity(
								// Create new city for po box fields
						poBoxFieldsNewCityName, 
						familyAssociationForm.getPoBoxFields().getState(), 
						familyAssociationForm.getPoBoxFields().getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = false;
				} else if ((personFieldsNewCityName.equals(
						poBoxFieldsNewCityName))
					&& (personFieldsCountryName.equals(poBoxFieldsCountryName))
					&& (poBoxFieldsStateName != null 
					&& personFieldsStateName != null 
					&& personFieldsStateName.equals(poBoxFieldsStateName)
						|| (poBoxFieldsStateName == null 
						&& personFieldsStateName == null))
					&& ((!addressFieldsNewCityName.equals(
							personFieldsNewCityName))
					|| (!addressFieldsCountryName.equals(
							personFieldsCountryName))
					|| (addressFieldsStateName != null 
					&& personFieldsStateName != null 
					&& (!addressFieldsStateName.equals(personFieldsStateName))
					|| (addressFieldsStateName != null 
					&& personFieldsStateName == null)
					|| (addressFieldsStateName == null 
					&& personFieldsStateName != null)))) {
					// If person and po box fields new cities are the same, 
					//but different from address fields  
					City newCreatedCity = this.familyAssociationService
							.createCity(
							// Create new city for person and poBox fields
						poBoxFieldsNewCityName, 
						familyAssociationForm.getPersonFields().getBirthState(),
						familyAssociationForm.getPersonFields()
						.getBirthCountry());
					newCreatedAddressFieldsCity = this.familyAssociationService
						.createCity(
								// Create new city for address fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = false;
				} else { 
					// All new cities in three fields are different
					newCreatedPoBoxFieldsCity = this.familyAssociationService
						.createCity(
								// Create new city for po box fields
						poBoxFieldsNewCityName, 
						familyAssociationForm.getPoBoxFields().getState(), 
						familyAssociationForm.getPoBoxFields().getCountry());
					newCreatedAddressFieldsCity = this.familyAssociationService
						.createCity(
								// Create new city for address fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedPersonFieldsCity = this.familyAssociationService
						.createCity(
						personFieldsNewCityName, 
						familyAssociationForm.getPersonFields().getBirthState(),
						familyAssociationForm.getPersonFields()
						.getBirthCountry());
					addressPoBoxFieldsNewCity = false;
				}
			} else if (poBoxFieldsNewCity && addressFieldsNewCity 
					&& (!personFieldsNewCity)) { 
				// po box and address fields create new cities, 
				//person fields not
				if (poBoxFieldsNewCityName.equals(addressFieldsNewCityName)
					&& poBoxFieldsCountryName.equals(addressFieldsCountryName)
					&& ((poBoxFieldsStateName != null 
					&& addressFieldsStateName != null 
					&& poBoxFieldsStateName.equals(addressFieldsStateName)
						|| (poBoxFieldsStateName == null 
						&& addressFieldsStateName == null)))) { 
					// po box and address fields' new city names are same
					City newCreatedCity = this.familyAssociationService
							.createCity(
							// Create same new city for address and 
									//person fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = null; 
					addressPoBoxFieldsNewCity = true;
				} else { 
					// Different new cities for po box and address fields
					newCreatedAddressFieldsCity = this.familyAssociationService
						.createCity(
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedPoBoxFieldsCity = this.familyAssociationService
						.createCity(
						poBoxFieldsNewCityName, 
						familyAssociationForm.getPoBoxFields().getState(), 
						familyAssociationForm.getPoBoxFields().getCountry());
					newCreatedPersonFieldsCity = null;
					addressPoBoxFieldsNewCity = false;
				}
			} else if ((!poBoxFieldsNewCity) && addressFieldsNewCity 
					&& personFieldsNewCity) {
				// person and address fields create new cities, 
				//po box fields not
				if (personFieldsNewCityName.equals(addressFieldsNewCityName)
					&& personFieldsCountryName.equals(addressFieldsCountryName)
					&& (personFieldsStateName != null 
					&& addressFieldsStateName != null && personFieldsStateName
					.equals(addressFieldsStateName)
					|| (personFieldsStateName == null 
					&& addressFieldsStateName == null))) { 
					// person and address fields' new city names are same
					City newCreatedCity = this.familyAssociationService
							.createCity(
							// Create same new city for address and 
							//person fields
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedAddressFieldsCity = newCreatedCity;
					newCreatedPoBoxFieldsCity = null;
					newCreatedPersonFieldsCity = newCreatedCity;
					addressPoBoxFieldsNewCity = false;
				} else { 
					// Different new cities for person and address fields
					newCreatedAddressFieldsCity = this.familyAssociationService
						.createCity(
						addressFieldsNewCityName, 
						familyAssociationForm.getAddressFields().getState(), 
						familyAssociationForm.getAddressFields().getCountry());
					newCreatedPoBoxFieldsCity = null;
					newCreatedPersonFieldsCity = this.familyAssociationService
						.createCity(
						personFieldsNewCityName, 
						familyAssociationForm.getPersonFields().getBirthState(),
						familyAssociationForm.getPersonFields()
						.getBirthCountry());
					addressPoBoxFieldsNewCity = false;
				}
			} else if (poBoxFieldsNewCity && (!addressFieldsNewCity) 
					&& personFieldsNewCity) {
				// person and po box fields create new cities, 
				//address fields not
				if (personFieldsNewCityName.equals(poBoxFieldsNewCityName)
					&& personFieldsCountryName.equals(poBoxFieldsCountryName)
					&& ((personFieldsStateName != null 
					&& poBoxFieldsStateName != null 
					&& personFieldsStateName.equals(poBoxFieldsStateName))
						|| (personFieldsStateName == null 
						&& poBoxFieldsStateName == null))) { 
					// person and po box fields' new city names are same
					City newCreatedCity 
						= this.familyAssociationService.createCity(
							// Create same new city for address and 
							//person fields
						personFieldsNewCityName, 
						familyAssociationForm.getPersonFields().getBirthState(),
						familyAssociationForm.getPersonFields()
						.getBirthCountry());
					newCreatedPoBoxFieldsCity = newCreatedCity;
					newCreatedPersonFieldsCity = newCreatedCity;
					newCreatedAddressFieldsCity = null;
					addressPoBoxFieldsNewCity = false;
				} else { 
					// Different new cities for person and po box fields
					newCreatedPoBoxFieldsCity = this.familyAssociationService
						.createCity(
						poBoxFieldsNewCityName, 
						familyAssociationForm.getPoBoxFields().getState(), 
						familyAssociationForm.getPoBoxFields().getCountry());
					newCreatedPersonFieldsCity = this.familyAssociationService
						.createCity(personFieldsNewCityName, 
						familyAssociationForm.getPersonFields().getBirthState(),
						familyAssociationForm.getPersonFields()
						.getBirthCountry());
					newCreatedAddressFieldsCity = null;
					addressPoBoxFieldsNewCity = false;
				}
			} else if (poBoxFieldsNewCity && (!addressFieldsNewCity) 
					&& (!personFieldsNewCity)) { 
				// po box fields create new cities, person and 
				//address fields not
				newCreatedPoBoxFieldsCity = this.familyAssociationService
						.createCity(
						// Create same new city for address and person fields
					poBoxFieldsNewCityName, 
					familyAssociationForm.getPoBoxFields().getState(), 
					familyAssociationForm.getPoBoxFields().getCountry());
				newCreatedAddressFieldsCity = null;
				newCreatedPersonFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			} else if ((!poBoxFieldsNewCity) && addressFieldsNewCity 
					&& (!personFieldsNewCity)) { 
				// address fields create new cities, person and 
				//po box fields not
				newCreatedAddressFieldsCity = this.familyAssociationService
						.createCity(
						// Create same new city for address and person fields
					addressFieldsNewCityName, 
					familyAssociationForm.getAddressFields().getState(), 
					familyAssociationForm.getAddressFields().getCountry());
				newCreatedPoBoxFieldsCity = null;
				newCreatedPersonFieldsCity = null;
				addressPoBoxFieldsNewCity = false;
			} else if (personFieldsNewCity && (!addressFieldsNewCity) 
					&& (!poBoxFieldsNewCity)) {
				// person fields create new cities, po box and 
				//address fields not
				newCreatedPoBoxFieldsCity = null;
				newCreatedAddressFieldsCity = null;
				newCreatedPersonFieldsCity = this.familyAssociationService
						.createCity(personFieldsNewCityName, 
					familyAssociationForm.getPersonFields().getBirthState(), 
					familyAssociationForm.getPersonFields().getBirthCountry());
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
						// New zip codes in address and po box fields are same
					&& addressFieldsZipCodeExtension.equals(
							poBoxFieldsZipCodeExtension)) {
					if (addressPoBoxFieldsNewCity) {  
		// Both address and po box fields create new cities and they are same
						ZipCode newCreatedZipCode 
							= this.familyAssociationService
								.createZipCode(
						// Create new zip code for address and po box fields 
								familyAssociationForm.getAddressFields()
									.getZipCodeValue(), 
								familyAssociationForm.getAddressFields()
									.getZipCodeExtension(), 
								newCreatedPoBoxFieldsCity);
						newCreatedAddressFieldsZipCode = newCreatedZipCode;
						newCreatedPoBoxFieldsZipCode = newCreatedZipCode;
						poBox.setValue(familyAssociationForm.getPoBoxFields()
							.getPoBoxValue());
						poBox.setZipCode(newCreatedZipCode);
					} else { 
			// new cities created for po box and address fields are different
						if (newCreatedPoBoxFieldsCity != null  
			// Both po box and address fields create new cities,but different 
								&& newCreatedAddressFieldsCity != null) {
							newCreatedAddressFieldsZipCode = 
								this.familyAssociationService.createZipCode(
									familyAssociationForm.getAddressFields()
									.getZipCodeValue(), 
									familyAssociationForm.getAddressFields()
									.getZipCodeExtension(), 
									newCreatedAddressFieldsCity);
							newCreatedPoBoxFieldsZipCode = 
								this.familyAssociationService.createZipCode(
									familyAssociationForm.getPoBoxFields()
									.getZipCodeValue(), 
									familyAssociationForm.getPoBoxFields()
									.getZipCodeExtension(), 
									newCreatedPoBoxFieldsCity);
						} else if (newCreatedPoBoxFieldsCity == null  
			// Po box field does not creates new city and address fields does 
								&& newCreatedAddressFieldsCity != null) {
							newCreatedAddressFieldsZipCode = 
								this.familyAssociationService.createZipCode(
									familyAssociationForm.getAddressFields()
									.getZipCodeValue(), 
									familyAssociationForm.getAddressFields()
									.getZipCodeExtension(), 
									newCreatedAddressFieldsCity);
							newCreatedPoBoxFieldsZipCode =
								this.familyAssociationService.createZipCode(
									familyAssociationForm.getPoBoxFields()
									.getZipCodeValue(),
									familyAssociationForm.getPoBoxFields()
									.getZipCodeExtension(),
									familyAssociationForm.getPoBoxFields()
									.getCity());
						} else if (newCreatedPoBoxFieldsCity != null  
				// Po box field creates new city and address fields does not 
								&& newCreatedAddressFieldsCity == null) {
							newCreatedAddressFieldsZipCode = 
								this.familyAssociationService.createZipCode(
									familyAssociationForm.getAddressFields()
									.getZipCodeValue(),
									familyAssociationForm.getAddressFields()
									.getZipCodeExtension(),
									familyAssociationForm.getAddressFields()
									.getCity());
							newCreatedPoBoxFieldsZipCode = 
								this.familyAssociationService.createZipCode(
									familyAssociationForm.getPoBoxFields()
									.getZipCodeValue(), 
									familyAssociationForm.getPoBoxFields()
									.getZipCodeExtension(), 
									newCreatedPoBoxFieldsCity);
						} else {  
							// none create new zip code
							if (!familyAssociationForm.getAddressFields()
									.getCity().equals(familyAssociationForm
											.getPoBoxFields().getCity())) {
								newCreatedAddressFieldsZipCode = 
									this.familyAssociationService.createZipCode(
										familyAssociationForm.getAddressFields()
										.getZipCodeValue(), 
										familyAssociationForm.getAddressFields()
										.getZipCodeExtension(), 
										familyAssociationForm.getAddressFields()
										.getCity());
								newCreatedPoBoxFieldsZipCode = 
									this.familyAssociationService.createZipCode(
										familyAssociationForm.getPoBoxFields()
										.getZipCodeValue(), 
										familyAssociationForm.getPoBoxFields()
										.getZipCodeExtension(), 
										familyAssociationForm.getPoBoxFields()
										.getCity());
							} else {
								newCreatedAddressFieldsZipCode = 
									this.familyAssociationService.createZipCode(
										familyAssociationForm.getAddressFields()
										.getZipCodeValue(), 
										familyAssociationForm.getAddressFields()
										.getZipCodeExtension(), 
										familyAssociationForm.getAddressFields()
										.getCity());
								newCreatedPoBoxFieldsZipCode 
									= newCreatedAddressFieldsZipCode; 
							}
						}
					}
					poBox.setValue(familyAssociationForm.getPoBoxFields()
						.getPoBoxValue());
					poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
				}  else {  
					// new zip codes in address and po box fields are different
					if (newCreatedAddressFieldsCity != null) {
						newCreatedAddressFieldsZipCode 
							= this.familyAssociationService.createZipCode(
								familyAssociationForm.getAddressFields()
									.getZipCodeValue(), 
								familyAssociationForm.getAddressFields()
									.getZipCodeExtension(), 
								newCreatedAddressFieldsCity);
					} else {
						newCreatedAddressFieldsZipCode 
							= this.familyAssociationService.createZipCode(
							familyAssociationForm.getAddressFields()
								.getZipCodeValue(), 
							familyAssociationForm.getAddressFields()
								.getZipCodeExtension(), 
							familyAssociationForm.getAddressFields().getCity());
					}
					if (newCreatedPoBoxFieldsCity != null) {
						newCreatedPoBoxFieldsZipCode 
							= this.familyAssociationService.createZipCode(
								familyAssociationForm.getPoBoxFields()
									.getZipCodeValue(), 
								familyAssociationForm.getPoBoxFields()
									.getZipCodeExtension(), 
								newCreatedPoBoxFieldsCity);
					} else {
						newCreatedPoBoxFieldsZipCode 
						 	= this.familyAssociationService.createZipCode(
							familyAssociationForm.getPoBoxFields()
								.getZipCodeValue(), 
							familyAssociationForm.getPoBoxFields()
								.getZipCodeExtension(), 
							familyAssociationForm.getPoBoxFields().getCity());
					}
					poBox.setValue(familyAssociationForm.getPoBoxFields()
						.getPoBoxValue());
					poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
				}
			} else if (addressFieldsNewZipCode && (!poBoxFieldsNewZipCode)) {
				// Address fields create new zip code, po box fields not
				if (newCreatedAddressFieldsCity == null) {
					newCreatedAddressFieldsZipCode 
						= this.familyAssociationService.createZipCode(
						familyAssociationForm.getAddressFields()
							.getZipCodeValue(), 
						familyAssociationForm.getAddressFields()
							.getZipCodeExtension(), 
						familyAssociationForm.getAddressFields().getCity());
				} else {
					newCreatedAddressFieldsZipCode 
						= this.familyAssociationService.createZipCode(
						familyAssociationForm.getAddressFields()
							.getZipCodeValue(), 
						familyAssociationForm.getAddressFields()
							.getZipCodeExtension(), 
						newCreatedAddressFieldsCity);
				}
				newCreatedPoBoxFieldsZipCode = null;
				poBox.setValue(familyAssociationForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(familyAssociationForm.getPoBoxFields()
					.getZipCode());
			} else if ((!addressFieldsNewZipCode) && poBoxFieldsNewZipCode) {
				// Po Box fields create new zip code, address fields not
				if (newCreatedPoBoxFieldsCity == null) {
					newCreatedPoBoxFieldsZipCode 
						= this.familyAssociationService.createZipCode(
						familyAssociationForm.getPoBoxFields()
						.getZipCodeValue(), 
						familyAssociationForm.getPoBoxFields()
						.getZipCodeExtension(), 
						familyAssociationForm.getPoBoxFields().getCity());
				} else {
					newCreatedPoBoxFieldsZipCode 
						= this.familyAssociationService.createZipCode(
						familyAssociationForm.getPoBoxFields()
						.getZipCodeValue(), 
						familyAssociationForm.getPoBoxFields()
						.getZipCodeExtension(), 
						newCreatedPoBoxFieldsCity);
				}
				newCreatedAddressFieldsZipCode = null;
				poBox.setValue(familyAssociationForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(newCreatedPoBoxFieldsZipCode);
			} else { 
				// None create new zip codes
				newCreatedPoBoxFieldsZipCode = null;
				newCreatedAddressFieldsZipCode = null;
				poBox.setValue(familyAssociationForm.getPoBoxFields()
					.getPoBoxValue());
				poBox.setZipCode(familyAssociationForm.getPoBoxFields()
					.getZipCode());
			}
			
			Person familyMember;
			if (!familyAssociationForm.getPersonFields().getNewCity()) {
				familyMember = this.familyAssociationService
					.createFamilyMember(
					familyAssociationForm.getPersonFields().getLastName(), 
					familyAssociationForm.getPersonFields().getFirstName(),
					familyAssociationForm.getPersonFields().getMiddleName(),
					familyAssociationForm.getPersonFields().getSuffix(), 
					familyAssociationForm.getPersonFields().getSex(),
					familyAssociationForm.getPersonFields().getBirthDate(),
					familyAssociationForm.getPersonFields().getBirthCountry(),
					familyAssociationForm.getPersonFields().getBirthState(),
					familyAssociationForm.getPersonFields().getBirthCity(),
					Integer.valueOf(familyAssociationForm.getPersonFields()
					.getSocialSecurityNumber().replaceAll("-", "")),
					familyAssociationForm.getPersonFields().getStateIdNumber(),
					familyAssociationForm.getPersonFields().getDeceased(),
					familyAssociationForm.getPersonFields().getDeathDate());
			} else {
				familyMember = this.familyAssociationService
					.createFamilyMember(
					familyAssociationForm.getPersonFields().getLastName(), 
					familyAssociationForm.getPersonFields().getFirstName(),
					familyAssociationForm.getPersonFields().getMiddleName(),
					familyAssociationForm.getPersonFields().getSuffix(), 
					familyAssociationForm.getPersonFields().getSex(),
					familyAssociationForm.getPersonFields().getBirthDate(),
					familyAssociationForm.getPersonFields().getBirthCountry(),
					familyAssociationForm.getPersonFields().getBirthState(),
					newCreatedPersonFieldsCity,
					Integer.valueOf(familyAssociationForm.getPersonFields()
					.getSocialSecurityNumber().replaceAll("-", "")),
					familyAssociationForm.getPersonFields().getStateIdNumber(),
					familyAssociationForm.getPersonFields().getDeceased(),
					familyAssociationForm.getPersonFields().getDeathDate());
			}
			
			/* Create a new family association. DuplicateEntityFoundException, 
			ReflexiveRelationshipException */
			Date startDate = familyAssociationForm.getStartDate();
			Date endDate = familyAssociationForm.getEndDate();
			DateRange dateRange = new DateRange(startDate, endDate);
			FamilyAssociationFlags familyAssociationFalgs 
				= new FamilyAssociationFlags(familyAssociationForm
						.getCohabitant(), familyAssociationForm.getDependent(),
					familyAssociationForm.getEmergencyContact());
			
			FamilyAssociation familyAssociation 
				= this.familyAssociationService.associate(offender, 
						familyMember, dateRange, 
						familyAssociationForm.getCategory(), 
						familyAssociationFalgs, 
						familyAssociationForm.getMarriageDate(), 
					familyAssociationForm.getDivorceDate());
		/* Add/create family association notes. DuplicateEntityFoundException */
			for (FamilyAssociationNoteItem item 
					: familyAssociationForm.getFamilyAssociationNoteItems()) {
				if (FamilyAssociationNoteItemOperation.CREATE
					.equals(item.getOperation())) {
					this.familyAssociationService.addNote(familyAssociation, 
							item.getDate(), item.getNote());
				}
			}
		
			/* Create address. No exception */
			Address address;
			if (familyAssociationForm.getEnterAddress() != null  
					// Enter address check box checked
				&& familyAssociationForm.getEnterAddress()) {
				if (FamilyAddressOperation.NEW.equals(familyAssociationForm
					.getAddressOperation())) { 
					// New address
					if (familyAssociationForm.getAddressFields().getNewCity()) {
						// New city in address fields
						try {
						    address = this.familyAssociationService
						    		.createAddress(
							familyAssociationForm.getAddressFields().getValue(),
							familyAssociationForm.getSecondAddressDesignator(),
							familyAssociationForm.getHomeType(),
							newCreatedAddressFieldsZipCode);   
						} catch (DuplicateEntityFoundException e) {
							throw new RuntimeException("Address exist", e);
						}
		    /* Create residence term. DuplicateEntityFoundException,
			PrimaryResidenceExistsException, ResidenceStatusConflictException */
					    this.familyAssociationService.createResidenceTerm(
					    	familyMember, address, verificationSignature);
					} else { 
						// Existing city, existing zip code in address fields
						if (newCreatedAddressFieldsZipCode == null) { 
							// Existing zip code
							try {
							    address = this.familyAssociationService
							    		.createAddress(
								familyAssociationForm.getAddressFields()
								.getValue(), 
								familyAssociationForm
								.getSecondAddressDesignator(),
								familyAssociationForm.getHomeType(),
								familyAssociationForm.getAddressFields()
								.getZipCode());   
							} catch (DuplicateEntityFoundException e) {
								throw new RuntimeException("Address exist", e);
							}
		    /* Create residence term. DuplicateEntityFoundException, 
			PrimaryResidenceExistsException, ResidenceStatusConflictException */
						    this.familyAssociationService.createResidenceTerm(
						    	familyMember, address, verificationSignature);
						} else { 
							// New created zip code
							try {
							    address = this.familyAssociationService
							    		.createAddress(
								familyAssociationForm.getAddressFields()
								.getValue(), familyAssociationForm
								.getSecondAddressDesignator(),
								familyAssociationForm.getHomeType(),  
								newCreatedAddressFieldsZipCode);   
							} catch (DuplicateEntityFoundException e) {
								throw new RuntimeException("Address exist", e);
							}
		    /* Create residence term. DuplicateEntityFoundException, 
			PrimaryResidenceExistsException, ResidenceStatusConflictException */
						    this.familyAssociationService.createResidenceTerm(
						    		familyMember, address, 
						    		verificationSignature);
						}
					}
				} else { 
					// Existing address in address fields
					address = familyAssociationForm.getAddress();
					this.familyAssociationService.createResidenceTerm(
					    familyMember, address, verificationSignature);
				}
			} else {   
				// Enter address check box unchecked
				address = null;
			}
			
			/* Create contact. DuplicateEntityFoundException */
			Contact contact = this.familyAssociationService
				.addContact(familyMember, address, poBox);
			
			/* Create telephone number. DuplicateEntityFoundException */
			for (FamilyAssociationTelephoneNumberItem telephoneNumberItem 
					: familyAssociationForm
					.getFamilyAssociationTelephoneNumberItems()) {
				if (FamilyAssociationTelephoneNumberItemOperation.CREATE
					.equals(telephoneNumberItem.getOperation())) {
					Boolean telephoneNumberPrimary;
					if (telephoneNumberItem
							.getTelephoneNumberFields().getPrimary() != null 
							&& telephoneNumberItem
							.getTelephoneNumberFields().getPrimary()) {
						telephoneNumberPrimary = true;
					} else {
						telephoneNumberPrimary = false;
					}
					Boolean telephoneNumberActive;
					if (telephoneNumberItem
							.getTelephoneNumberFields().getActive() != null
							&& telephoneNumberItem
							.getTelephoneNumberFields().getActive()) {
						telephoneNumberActive = true;
					} else {
						telephoneNumberActive = false;
					}
					this.familyAssociationService.addTelephoneNumber(
						contact, 
						telephoneNumberItem.getTelephoneNumberFields()
							.getValue(), 
						telephoneNumberItem.getTelephoneNumberFields()
							.getExtension(), 
						telephoneNumberPrimary, telephoneNumberActive,
						telephoneNumberItem.getTelephoneNumberFields()
							.getCategory()); 
				}
			}
					
			/* Add online account. DuplicateEntityFoundException */
			for (FamilyAssociationOnlineAccountItem emailItem 
					: familyAssociationForm
					.getFamilyAssociationOnlineAccountItems()) {
				if (FamilyAssociationOnlineAccountItemOperation.CREATE
					.equals(emailItem.getOperation())) {
					Boolean onlineAccountPrimary;
					if (emailItem.getOnlineAccountFields().getPrimary()
							!= null && emailItem.getOnlineAccountFields()
							.getPrimary()) {
						onlineAccountPrimary = true;
					} else {
						onlineAccountPrimary = false;
					}
					Boolean onlineAccountActive;
					if (emailItem
							.getOnlineAccountFields().getActive() != null
							&& emailItem.getOnlineAccountFields()
							.getActive()) {
						onlineAccountActive = true;
					} else {
						onlineAccountActive = false;
					}
					OnlineAccountHost onlineAccountHost 
						= emailItem.getOnlineAccountFields().getHost();
					onlineAccountHost.setCategory(
							OnlineAccountCategory.EMAIL);
					this.familyAssociationService.addOnlineAccount(
						contact,
						emailItem.getOnlineAccountFields().getName(),
						onlineAccountPrimary, onlineAccountHost,		
						onlineAccountActive);
				}
			}
			return new ModelAndView(String.format(
					LIST_REDIRECT, offender.getId()));
		} else {  
		// Existing... click the "Create Family Association" link on the left 
			//side of each row on the search result screen
			this.familyAssociationFormValidator.validate(familyAssociationForm,
				result);
			if (result.hasErrors()) {
				Boolean createFlag = true;
				return this.prepareRedisplayEditMav(familyAssociationForm, 
				familyAssociationForm.getFamilyAssociationNoteItems(),
				offender, null, null, 
				familyAssociationForm
				.getFamilyAssociationTelephoneNumberItems(),
				familyAssociationForm.getFamilyAssociationOnlineAccountItems(),
				0, result, 0, 0, createFlag, associatedPerson);
			}
			
			DateRange dateRange = new DateRange();
			dateRange.setStartDate(familyAssociationForm.getStartDate());
			dateRange.setEndDate(familyAssociationForm.getEndDate());
			FamilyAssociationFlags familyAssociationFalgs 
				= new FamilyAssociationFlags(familyAssociationForm
						.getCohabitant(),
				familyAssociationForm.getDependent(),
				familyAssociationForm.getEmergencyContact());
			this.familyAssociationService
				.associate(offender, associatedPerson, 
				dateRange, familyAssociationForm.getCategory(), 
				familyAssociationFalgs, familyAssociationForm.getMarriageDate(),
				familyAssociationForm.getDivorceDate());
			return new ModelAndView(String.format(LIST_REDIRECT, 
				offender.getId()));
		}		
	}
	
	/** Edit an existing family association. 
	 * @param familyAssociation family association.
	 * @param offender offender
	 * @return edited family association view. */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(
		@RequestParam(value = "familyAssociation", required = true)
		final FamilyAssociation familyAssociation,
		@RequestParam(value = "offender", required = true)
		final Offender offender) {
		FamilyAssociationForm familyAssociationForm
			= new FamilyAssociationForm();		
		/* Set Address section */
		Person person = familyAssociation.getRelationship().getSecondPerson();
		Contact contact = this.familyAssociationService
			.findContactByPerson(person);
		AddressFields addressFields = new AddressFields();
		if (contact != null && contact.getMailingAddress() != null) {
			familyAssociationForm.setHomeType(contact.getMailingAddress()
				.getBuildingCategory());
			familyAssociationForm.setSecondAddressDesignator(contact
				.getMailingAddress().getDesignator());
			addressFields.setZipCode(contact.getMailingAddress().getZipCode());
			addressFields.setValue(contact.getMailingAddress()
				.getValue());
			if (contact.getMailingAddress().getZipCode() != null) {
				addressFields.setZipCodeExtension(contact.getMailingAddress()
					.getZipCode().getExtension());
				addressFields.setCity(contact.getMailingAddress().getZipCode()
					.getCity());
				if (contact.getMailingAddress().getZipCode()
						.getCity() != null) {
					addressFields.setCountry(contact.getMailingAddress()
						.getZipCode().getCity().getCountry());
					addressFields.setState(contact.getMailingAddress()
						.getZipCode().getCity().getState());
				}
			}
		}
		familyAssociationForm.setAddressFields(addressFields);		
		/* Set Family Member Identity section */
		PersonFields personFields = new PersonFields();
		if (person.getIdentity() != null) {
			personFields.setBirthCity(person.getIdentity().getBirthPlace());
			if (person.getIdentity().getBirthPlace() != null) {
				personFields.setBirthCountry(person.getIdentity()
					.getBirthPlace().getCountry());
			}
			personFields.setBirthState(person.getIdentity().getBirthState());
			personFields.setBirthDate(person.getIdentity().getBirthDate());
			personFields.setDeathDate(person.getIdentity().getDeathDate());
			personFields.setDeceased(person.getIdentity().getDeceased());
			if (person.getName() != null) {
				personFields.setFirstName(person.getName().getFirstName());
				personFields.setLastName(person.getName().getLastName());
				personFields.setMiddleName(person.getName().getMiddleName());
				personFields.setSuffix(person.getName().getSuffix());
			}
			personFields.setSex(person.getIdentity().getSex());
			if (person.getIdentity()
					.getSocialSecurityNumber() != null) {
				if (hasRole("ADMIN") 
						|| hasRole("FAMILY_ASSOCIATION_SSN_EDIT") 
						|| hasRole("FAMILY_ASSOCIATION_SSN_VIEW")) {
					String stringSSN = String.format("%09d",
							person.getIdentity()
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
					familyAssociationForm.setValidateSocialSecurityNumber(true);
				} else {
					String socialSecurityNumber
						= person.getIdentity()
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
				familyAssociationForm.setValidateSocialSecurityNumber(false);
				}
			}
			personFields.setStateIdNumber(
					person.getIdentity().getStateIdNumber());
			familyAssociationForm.setPersonFields(personFields);
		}		
		/* Set section post office box */
		PoBoxFields poBoxFields = new PoBoxFields();
		poBoxFields.setNewZipCode(true);
		if (contact != null && contact.getPoBox() != null) {
			poBoxFields.setPoBoxValue(contact.getPoBox().getValue());
			if (contact.getPoBox().getZipCode() != null) {
				poBoxFields.setZipCode(contact.getPoBox().getZipCode());
				poBoxFields.setCity(contact.getPoBox().getZipCode().getCity());
				if (contact.getPoBox().getZipCode().getCity() != null) {
					poBoxFields.setCountry(contact.getPoBox().getZipCode()
						.getCity().getCountry());
					poBoxFields.setState(contact.getPoBox().getZipCode()
						.getCity().getState());
				}
			}
		}
		familyAssociationForm.setPoBoxFields(poBoxFields);		
		/* Set section Phone */
		List<FamilyAssociationTelephoneNumberItem> 
		 	familyAssociationTelephoneNumberItems = 
		 		new ArrayList<FamilyAssociationTelephoneNumberItem>();
		for (TelephoneNumber telephoneNumber : this.familyAssociationService
			.findTelephoneNumbersByContact(contact)) {
			FamilyAssociationTelephoneNumberItem item 
				= new FamilyAssociationTelephoneNumberItem();
			item.setTelephoneNumber(telephoneNumber);
			item.setExtension(telephoneNumber.getExtension());
			item.setOperation(
					FamilyAssociationTelephoneNumberItemOperation.EDIT);
			item.setPhoneNumber(telephoneNumber.getValue());
			item.setPhoneType(telephoneNumber.getCategory());
			item.setPrimary(telephoneNumber.getPrimary());
			item.setActive(telephoneNumber.getActive());
			familyAssociationTelephoneNumberItems.add(item);
		}
		familyAssociationForm.setFamilyAssociationTelephoneNumberItems(
			familyAssociationTelephoneNumberItems);
		int familyAssociationTelephoneNumberIndex 
			= familyAssociationTelephoneNumberItems.size();		
		/* Set section Email */
		List<FamilyAssociationOnlineAccountItem> familyAssociationEmailItems = 
				new ArrayList<FamilyAssociationOnlineAccountItem>();
		for (OnlineAccount onlineAccount 
			: this.familyAssociationService
			.findOnlineAccountsByContact(contact)) {
			FamilyAssociationOnlineAccountItem item 
				= new FamilyAssociationOnlineAccountItem();
			item.setOnlineAccount(onlineAccount);
			item.setEmail(onlineAccount.getName());
			item.setOnlineAccountHost(onlineAccount.getHost());
			item.setOperation(
					FamilyAssociationOnlineAccountItemOperation.UPDATE);
			item.setPrimary(onlineAccount.getPrimary());
			item.setActive(onlineAccount.getActive());
			familyAssociationEmailItems.add(item);
		}
		familyAssociationForm.setFamilyAssociationOnlineAccountItems(
			familyAssociationEmailItems); 
		/* Set Family to Offender Association */
		if (familyAssociation.getDateRange() != null) {
			familyAssociationForm.setStartDate(familyAssociation.getDateRange()
				.getStartDate());
			familyAssociationForm.setEndDate(familyAssociation.getDateRange()
				.getEndDate());
		}
		if (familyAssociation.getFlags() != null) {
			familyAssociationForm.setCohabitant(familyAssociation.getFlags()
				.getCohabitant());
			familyAssociationForm.setDependent(familyAssociation.getFlags()
				.getDependent());
			familyAssociationForm.setEmergencyContact(familyAssociation
				.getFlags().getEmergencyContact());
		}
		if (FamilyAssociationCategoryClassification.SPOUSE.equals(
			familyAssociation.getCategory().getClassification())) {
			familyAssociationForm.setDivorceDate(familyAssociation
				.getDivorceDate());
			familyAssociationForm.setMarriageDate(familyAssociation
				.getMarriageDate());
		}
		familyAssociationForm.setCategory(familyAssociation.getCategory());
		/* Set family association notes */
		List<FamilyAssociationNote> familyAssociationNotes 
			= this.familyAssociationService
				.findNotesByAssociation(familyAssociation);
		List<FamilyAssociationNoteItem> familyAssociationNoteItems 
			= new ArrayList<FamilyAssociationNoteItem>();
		for (FamilyAssociationNote note : familyAssociationNotes) {
			FamilyAssociationNoteItem familyAssociationNoteItem 
				= new FamilyAssociationNoteItem();
			familyAssociationNoteItem.setDate(note.getDate());
			familyAssociationNoteItem.setNote(note.getValue());
			familyAssociationNoteItem.setOperation(
				FamilyAssociationNoteItemOperation.EDIT); 
			familyAssociationNoteItems.add(familyAssociationNoteItem);
		}
		familyAssociationForm
			.setFamilyAssociationNoteItems(familyAssociationNoteItems);
		int familyAssociationNoteIndex = familyAssociationNoteItems.size() + 1;
		int familyAssociationEmailIndex = familyAssociationEmailItems.size();
		Boolean createFlag = false;
		return this.prepareEditMav(familyAssociationForm,  offender, 
			familyAssociation, familyAssociationNoteItems, 
			familyAssociationTelephoneNumberItems, 
			familyAssociationEmailItems, familyAssociationNoteIndex, 
			familyAssociationTelephoneNumberIndex, familyAssociationEmailIndex,
			createFlag, person);
	}
	
	/**
	 * Updates an existing family association.
	 * 
	 * @param familyAssociationForm updated family association form
	 * @param familyAssociation family association
	 * @param offender offender
	 * @param result binding result
	 * @return redirect to list of family associations
	 * @throws FamilyAssociationConflictException 
	 * family association conflict exception
	 * @throws FamilyAssociationExistsException 
	 * @throws FamilyAssociationNoteExistsException 
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(
		@RequestParam(value = "offender", required = true)
		final Offender offender,
		@RequestParam(value = "familyAssociation", required = true)
		final FamilyAssociation familyAssociation,
		final FamilyAssociationForm familyAssociationForm,
		final BindingResult result) throws 
		FamilyAssociationConflictException, FamilyAssociationExistsException,
		FamilyAssociationNoteExistsException {	
		this.familyAssociationFieldsEditValidator
		 	.validate(familyAssociationForm,
			result);
		if (result.hasErrors()) {
			int familyAssociationNoteIndex = 0;
			if (familyAssociationForm.getFamilyAssociationNoteItems() != null) {
				familyAssociationNoteIndex = familyAssociationForm
				.getFamilyAssociationNoteItems().size();
			} 
			Boolean createFlag = true;
			return this.prepareRedisplayEditMav(familyAssociationForm, 
				familyAssociationForm.getFamilyAssociationNoteItems(),
				offender, null, null, familyAssociationForm
				.getFamilyAssociationTelephoneNumberItems(),
				familyAssociationForm.getFamilyAssociationOnlineAccountItems(),
				familyAssociationNoteIndex, result,	0, 0, createFlag,
				familyAssociation.getRelationship().getSecondPerson());
		}		
	/* Update the existing family association. DuplicateEntityFoundException, 
	ReflexiveRelationshipException */
		Date startDate = familyAssociationForm.getStartDate();
		Date endDate = familyAssociationForm.getEndDate();
		DateRange dateRange = new DateRange(startDate, endDate);
		FamilyAssociationFlags familyAssociationFalgs 
			= new FamilyAssociationFlags(familyAssociationForm.getCohabitant(),
				familyAssociationForm.getDependent(),
				familyAssociationForm.getEmergencyContact());		
		this.familyAssociationService.update(familyAssociation, dateRange, 
			familyAssociationForm.getCategory(), familyAssociationFalgs, 
			familyAssociationForm.getMarriageDate(), 
			familyAssociationForm.getDivorceDate());		
		/* Update/add family association notes */
		List<FamilyAssociationNote> originalFamilyAssociationNotes 
			= this.familyAssociationService
				.findNotesByAssociation(familyAssociation);
		int originalFamilyAssociationNoteIndex 
			= originalFamilyAssociationNotes.size();	
		List<FamilyAssociationNoteItem> familyAssociationNoteItems 
			= familyAssociationForm.getFamilyAssociationNoteItems();
		int newFamilyAssociationNoteIndex 
			= familyAssociationNoteItems.size() - 1;
		
		for (int index = 0; index < originalFamilyAssociationNoteIndex; 
				index++) {
			if (FamilyAssociationNoteItemOperation.EDIT.equals(
				familyAssociationNoteItems.get(index).getOperation())) {
				this.familyAssociationService.updateNote(
					originalFamilyAssociationNotes.get(index), 
					familyAssociationNoteItems.get(index).getDate(), 
					familyAssociationNoteItems.get(index).getNote());
			}
			if (FamilyAssociationNoteItemOperation.REMOVE.equals(
				familyAssociationNoteItems.get(index).getOperation())) {
				this.familyAssociationService
					.removeNote(originalFamilyAssociationNotes.get(index));
			}
		}
		
		for (int index = originalFamilyAssociationNoteIndex; 
			index <= newFamilyAssociationNoteIndex; index++) {
			if (FamilyAssociationNoteItemOperation.CREATE.equals(
				familyAssociationNoteItems.get(index).getOperation())) {
				this.familyAssociationService.addNote(familyAssociation, 
					familyAssociationNoteItems.get(index).getDate(), 
					familyAssociationNoteItems.get(index).getNote());
			}
		}
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	/**
	 * Removes an existing family association record.
	 * 
	 * @param familyAssociation family association
	 * @param offender offender
	 * @return redirect to list religious preferences
	 */
	@RequestMapping("/remove.html")
	@PreAuthorize("hasRole('FAMILY_ASSOCIATION_REMOVE') or hasRole('ADMIN')")
	public ModelAndView remove(
		@RequestParam(value = "familyAssociation", required = true)
			final FamilyAssociation familyAssociation,
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		List<FamilyAssociationNote> familyAssociationNotes 
			= this.familyAssociationService.findNotesByAssociation(
				familyAssociation);
		for (FamilyAssociationNote note : familyAssociationNotes) {
			this.familyAssociationService.removeNote(note);
		}
		this.familyAssociationService.remove(familyAssociation);
		return new ModelAndView(String.format(LIST_REDIRECT, offender.getId()));
	}
	
	private ModelAndView prepareEditMav(
		final FamilyAssociationForm familyAssociationForm,
		final Offender offender, final FamilyAssociation familyAssociation,
		final List<FamilyAssociationNoteItem> familyAssociationNoteItems,
		final List<FamilyAssociationTelephoneNumberItem> 
			familyAssociationTelephoneNumberItems,
		final List<FamilyAssociationOnlineAccountItem> 
			familyAssociationEmailItems,
		final int familyAssociationNoteIndex, 
		final int familyAssociationTelephoneNumberIndex,
		final int familyAssociationEmailIndex,
		final Boolean createFlag,
		final Person familyMember) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(CREATE_FLAG_MODEL_KEY, createFlag);
		mav.addObject(FAMILY_MEMBER_MODEL_KEY, familyMember);		
		if (familyMember != null) {
			Boolean value = true;
			mav.addObject(EXISTING_FAMILY_MEMBER_MODEL_KEY, value);
		} else {
			Boolean value = false;
			mav.addObject(EXISTING_FAMILY_MEMBER_MODEL_KEY, value);
		}		
		mav.addObject(FAMILY_ASSOCIATION_EDIT_FORM_MODEL_KEY, 
			familyAssociationForm);
		mav.addObject(FAMILY_ASSOCIATION_MODEL_KEY, familyAssociation);
		mav.addObject(FAMILY_ASSOCIATION_NOTE_ITEMS_MODEL_KEY, 
			familyAssociationNoteItems);
		mav.addObject(FAMILY_ASSOCIATION_TELEPHONE_NUMBER_ITEMS_MODEL_KEY, 
			familyAssociationTelephoneNumberItems);
		mav.addObject(FAMILY_ASSOCIATION_EMAIL_ITEMS_MODEL_KEY, 
			familyAssociationEmailItems);
		List<Suffix> nameSuffixes 
			= this.familyAssociationService.findNameSuffixes();
		List<String> suffixes = this.familyAssociationService.findSuffixNames();
		mav.addObject(NAME_SUFFIXES_MODEL_KEY, nameSuffixes);
		mav.addObject(HOME_TYPES_MODEL_KEY, BuildingCategory.values());
		List<Country> countries = this.familyAssociationService.findCountries();
		mav.addObject(COUNTRIES_MODEL_KEY, countries);
		mav.addObject(BIRTH_COUNTRIES_MODEL_KEY, countries);
		mav.addObject(PO_BOX_COUNTRIES_MODEL_KEY, countries);
		List<FamilyAssociationCategory> categories 
			= this.familyAssociationService.findCategories();
		mav.addObject(FAMILY_ASSOCIATION_CATEGORIES_MODEL_KEY, categories);
		mav.addObject(FAMILY_ASSOCIATION_NOTE_INDEX_MODEL_KEY, 
				familyAssociationNoteIndex);
		mav.addObject(TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY, 
			familyAssociationTelephoneNumberIndex);
		mav.addObject(EMAIL_ITEM_INDEX_MODEL_KEY, 
			familyAssociationEmailIndex);		
		List<OnlineAccountHost> onlineAccountHosts 
			= this.familyAssociationService.findOnlineAccountHosts();
		mav.addObject(ONLINE_ACCOUNT_HOSTS_MODEL_KEY, onlineAccountHosts);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		mav.addObject(SEXES_MODEL_KEY, Sex.values());
		ModelMap map = mav.getModelMap();
		map.addAttribute(TELEPHONE_NUMBER_CATEGORY_MODEL_KEY,
			TelephoneNumberCategory.values());		
		AddressFields addressFields = familyAssociationForm.getAddressFields();
		if (familyAssociationForm.getEnterAddress() != null
			&& familyAssociationForm.getEnterAddress()) {
			if (addressFields != null) {
				if (addressFields.getCity() != null) {
					if (this.familyAssociationService.hasStates(
						addressFields.getCountry())) {
						if (addressFields.getState() != null) {					
							this.addressFieldsControllerDelegate
 						 	 	.prepareEditAddressFields(map, countries, 
 						 	 			this.familyAssociationService
										 	.findStatesByCountry(
										 			addressFields.getCountry()),
										this.familyAssociationService
										 	.findCitiesByState(
												 addressFields.getState()), 
										this.familyAssociationService
										 	.findZipCodesByCity(
												 addressFields.getCity()), 
										 		ADDRESS_FIELDS_PROPERTY_NAME);
						} else {
							this.addressFieldsControllerDelegate
								.prepareEditAddressFields(map, countries, 
									 this.familyAssociationService
									 	.findStatesByCountry(
									 			addressFields.getCountry()), 
									 this.familyAssociationService
									 	.findCitiesByCountryWithoutState(
									 			addressFields.getCountry()), 
									 this.familyAssociationService
									 	.findZipCodesByCity(addressFields
									 			.getCity()), 
							 			ADDRESS_FIELDS_PROPERTY_NAME);
						}
					} else {	  
					// No state
						this.addressFieldsControllerDelegate
							.prepareEditAddressFields(map, countries, null, 
								this.familyAssociationService
									.findCitiesByCountry(
										addressFields.getCity().getCountry()), 
								this.familyAssociationService
									.findZipCodesByCity(
										addressFields.getCity()), 
										ADDRESS_FIELDS_PROPERTY_NAME);
					}
				} else {  
					// no city
					if (addressFields.getCountry() != null) {
						if (this.familyAssociationService.hasStates(
							addressFields.getCountry())) {
							if (addressFields.getState() != null) {
								this.addressFieldsControllerDelegate
									.prepareEditAddressFields(map, countries, 
									 this.familyAssociationService
									 	.findStatesByCountry(addressFields
											 .getCountry()), 
									 this.familyAssociationService
									 	.findCitiesByState(addressFields
											 .getState()), null, 
									 	ADDRESS_FIELDS_PROPERTY_NAME);
							} else {
								this.addressFieldsControllerDelegate
								 	.prepareEditAddressFields(map, countries, 
									 this.familyAssociationService
									 	.findStatesByCountry(
											 addressFields.getCountry()), 
									 this.familyAssociationService
									 	.findCitiesByCountryWithoutState(
									 			addressFields.getCountry()), 
									 		null, ADDRESS_FIELDS_PROPERTY_NAME);
							}
						} else { 
							// No state
							this.addressFieldsControllerDelegate
								 .prepareEditAddressFields(map, countries, null,
									 this.familyAssociationService
									 	.findCitiesByCountry(
									 			familyAssociationForm
									 			.getAddressFields()
									 			.getCountry()), null, 
									 		ADDRESS_FIELDS_PROPERTY_NAME);
						}
					} else { 
						// No country
						this.addressFieldsControllerDelegate
						 	.prepareEditAddressFields(map, countries, 
								 null, null, null, 
								 	ADDRESS_FIELDS_PROPERTY_NAME);
					}
				}
			} else {
				List<State> states 
					= this.familyAssociationService.findStatesByCountry(null);
				List<City> cities 
					= this.familyAssociationService.findCitiesByState(null);
				List<ZipCode> zipCodes 
					= this.familyAssociationService.findZipCodesByCity(null);
				this.addressFieldsControllerDelegate.prepareEditAddressFields(
					map, countries, states, cities, zipCodes, 
					ADDRESS_FIELDS_PROPERTY_NAME);
			}
		} else {
			if (familyAssociationForm.getAddressFields() != null) {
				List<State> states 
					= this.familyAssociationService.findStatesByCountry(
							familyAssociationForm.getAddressFields()
							.getCountry());
				List<City> cities 
					= this.familyAssociationService
					.findCitiesByCountryWithoutState(
							familyAssociationForm.getAddressFields()
							.getCountry());
				this.addressFieldsControllerDelegate.prepareEditAddressFields(
						map, countries, states, cities, null, 
						ADDRESS_FIELDS_PROPERTY_NAME);
			}
		}
		if (familyAssociationForm.getEnterPoBox() != null
			&& familyAssociationForm.getEnterPoBox()) {
			if (familyAssociationForm.getPoBoxFields() != null) {
				PoBoxFields poBoxFields 
				 	= familyAssociationForm.getPoBoxFields();
	//			poBoxFields.setNewZipCode(false);
				poBoxFields.setNewZipCode(poBoxFields.getNewZipCode());
				familyAssociationForm.setPoBoxFields(poBoxFields);
			//	if (poBoxFields != null) {
				if (poBoxFields.getCity() != null) {
					if (this.familyAssociationService.hasStates(
							poBoxFields.getCountry())) {
						if (poBoxFields.getState() != null) {
							this.poboxFieldsControllerDelegate
								 .prepareEditPoBoxFields(
										 map, countries, 
										 this.familyAssociationService
									 		.findStatesByCountry(
									 				poBoxFields.getCountry()), 
										 this.familyAssociationService
										 	.findCitiesByState(
												poBoxFields.getCity()
													.getState()), 
										 this.familyAssociationService
											 .findZipCodesByCity(
												poBoxFields.getCity()), 
											 	PO_BOX_FIELDS_PROPERTY_NAME); 
						} else {
							this.poboxFieldsControllerDelegate
								 .prepareEditPoBoxFields(
										map, countries, 
										 this.familyAssociationService
										 	.findStatesByCountry(
										 			poBoxFields.getCountry()), 
										 this.familyAssociationService
										 	.findCitiesByCountryWithoutState(
										 			poBoxFields.getCountry()), 
										 this.familyAssociationService
										 	.findZipCodesByCity(
										 			poBoxFields.getCity()), 
										 		PO_BOX_FIELDS_PROPERTY_NAME); 	
						}
					} else { 						
						// No state
						this.poboxFieldsControllerDelegate
							.prepareEditPoBoxFields(
									map, countries, null, 
									this.familyAssociationService
										.findCitiesByCountryWithoutState(
											poBoxFields.getCountry()), 
									this.familyAssociationService
										.findZipCodesByCity(
											poBoxFields.getCity()), 
									PO_BOX_FIELDS_PROPERTY_NAME); 
					}
				} else { 
						// no city
					if (poBoxFields.getCountry() != null) {
						if (this.familyAssociationService.hasStates(
								poBoxFields.getCountry())) {
							if (poBoxFields.getState() != null) {
								this.poboxFieldsControllerDelegate
									.prepareEditPoBoxFields(map, countries, 
											this.familyAssociationService
											.findStatesByCountry(
													poBoxFields.getCountry()), 
											this.familyAssociationService
											.findCitiesByState(
													poBoxFields.getState()), 
												null, 
												PO_BOX_FIELDS_PROPERTY_NAME);
							} else {
								this.poboxFieldsControllerDelegate
									.prepareEditPoBoxFields(map, countries, 
											this.familyAssociationService
											.findStatesByCountry(
													poBoxFields.getCountry()), 
											this.familyAssociationService
											.findCitiesByCountryWithoutState(
													poBoxFields.getCountry()), 
											null, PO_BOX_FIELDS_PROPERTY_NAME);	
							}
						} else {
								// No state
							this.poboxFieldsControllerDelegate
									.prepareEditPoBoxFields(map, countries, 
											null, 
											this.familyAssociationService
												.findCitiesByCountry(
														familyAssociationForm
														.getPoBoxFields()
														.getCountry()),	null, 
											PO_BOX_FIELDS_PROPERTY_NAME);
						}
					} else {
							// No country
						this.poboxFieldsControllerDelegate
								.prepareEditPoBoxFields(map, countries, 
									null, null, null, 
									PO_BOX_FIELDS_PROPERTY_NAME);
					}
				}
			//	}
			} else {
				List<State> states 
					= this.familyAssociationService.findStatesByCountry(null);
				List<City> cities 
					= this.familyAssociationService.findCitiesByState(null);
				List<ZipCode> zipCodes 
					= this.familyAssociationService.findZipCodesByCity(null);
				this.poboxFieldsControllerDelegate.prepareEditPoBoxFields(map, 
					countries, states, cities, zipCodes, 
					PO_BOX_FIELDS_PROPERTY_NAME);
			}
		} else {
			if (familyAssociationForm.getAddressFields() != null) {
				List<State> states 
					= this.familyAssociationService.findStatesByCountry(
					familyAssociationForm.getPoBoxFields().getCountry());
				List<City> cities 
					= this.familyAssociationService
						.findCitiesByCountryWithoutState(
								familyAssociationForm.getPoBoxFields()
								.getCountry());
	//			List<ZipCode> zipCodes 
	//				= this.familyAssociationService.findZipCodesByCity(null);
				this.poboxFieldsControllerDelegate.prepareEditPoBoxFields(map, 
					countries, states, cities, null, 
					PO_BOX_FIELDS_PROPERTY_NAME);
			}
		}
		
		if (familyAssociationForm.getPersonFields() != null) {
			PersonFields personFields = familyAssociationForm.getPersonFields();
			if (personFields != null) {
				if (personFields.getBirthCity() != null) {
					if (this.familyAssociationService.hasStates(
						personFields.getBirthCountry())) {
						if (personFields.getBirthState() != null) {
							this.personFieldsControllerDelegate
							 	.prepareEditPersonFields(map, suffixes, 
							 			countries,
								 this.familyAssociationService
								 	.findStatesByCountry(
										 personFields.getBirthCountry()),
								 this.familyAssociationService
								 	.findCitiesByState(
										 personFields.getBirthState()),
								 	PERSON_FIELDS_PROPERTY_NAME);
						} else {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, suffixes, 
										countries, 
										this.familyAssociationService
										.findStatesByCountry(
												personFields.getBirthCountry()),
										 this.familyAssociationService
										 .findCitiesByCountryWithoutState(
												 personFields
												 .getBirthCountry()),
										 	PERSON_FIELDS_PROPERTY_NAME);
						}
					} else {
						// No state
						this.personFieldsControllerDelegate
							.prepareEditPersonFields(map, suffixes, countries, 
								null, this.familyAssociationService
								.findCitiesByCountryWithoutState(
										 personFields.getBirthCountry()), 
								 PERSON_FIELDS_PROPERTY_NAME);   
					}
				} else {  
					// no city
					if (personFields.getBirthCountry() != null) {
						if (this.familyAssociationService.hasStates(personFields
								.getBirthCountry())) {
							if (personFields.getBirthState() != null) {
								this.personFieldsControllerDelegate
									.prepareEditPersonFields(map, suffixes, 
										countries, this.familyAssociationService
										.findStatesByCountry(personFields
											.getBirthCountry()), 
										this.familyAssociationService
										.findCitiesByState(personFields
												.getBirthState()),
										PERSON_FIELDS_PROPERTY_NAME);
							} else {
								this.personFieldsControllerDelegate
									.prepareEditPersonFields(map, suffixes, 
										countries, this.familyAssociationService
										.findStatesByCountry(personFields
												.getBirthCountry()), 
										this.familyAssociationService
										.findCitiesByCountryWithoutState(
												personFields.getBirthCountry()),
										 	PERSON_FIELDS_PROPERTY_NAME);
							}
						} else {
							this.personFieldsControllerDelegate
								.prepareEditPersonFields(map, suffixes, 
										countries, 
									null, this.familyAssociationService
									.findCitiesByCountry(
									personFields.getBirthCountry()), 
									PERSON_FIELDS_PROPERTY_NAME);
						}
					} else {
						this.personFieldsControllerDelegate
						 	.prepareEditPersonFields(map, suffixes, countries, 
								 null, null, PERSON_FIELDS_PROPERTY_NAME);
					}
				}
			}
		} else {
			List<State> states 
			 	= this.familyAssociationService.findStatesByCountry(null);
			List<City> cities 
				= this.familyAssociationService.findCitiesByState(null);
			this.personFieldsControllerDelegate.prepareEditPersonFields(map, 
				suffixes, countries, states, cities, 
					 PERSON_FIELDS_PROPERTY_NAME);
		}
		
		this.telephoneNumberFieldsControllerDelegate
			.prepareEditTelephoneNumberFields(map);
		this.onlineAccountFieldsControllerDelegate
			.prepareEditOnlineAccountFields(map, onlineAccountHosts);
		if (familyAssociation != null) {
			this.contactSummaryModelDelegate.add(map, 
				familyAssociation.getRelationship().getSecondPerson());
		}
		if (familyMember != null) {
			ContactSummary existingContactSummary = this.contactReportService
				.summarizeByPerson(familyMember);
			map.addAttribute(EXISTING_CONTACT_SUMMARY_MODEL_KEY,
					existingContactSummary);
		}
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}	
	
	// Prepares redisplay edit model and view
	private ModelAndView prepareRedisplayEditMav(
		final FamilyAssociationForm familyAssociationFields,
		final List<FamilyAssociationNoteItem> familyAssociationNoteItems,
		final Offender offender,
		final FamilyAssociation familyAssociation,
		final List<FamilyAssociationNote> familyAssociationNotes,
		final List<FamilyAssociationTelephoneNumberItem> 
			familyAssociationTelephoneNumberItems,
		final List<FamilyAssociationOnlineAccountItem> 
			familyAssociationEmailItems,
		final int familyAssociationNoteIndex,
		final BindingResult result,
		final int familyAssociationTelephoneNumberIndex,
		final int familyAssociationEmailIndex,
		final Boolean createFlag,
		final Person person) {
		ModelAndView mav = this.prepareEditMav(familyAssociationFields,
			offender, familyAssociation, familyAssociationNoteItems, 
			familyAssociationTelephoneNumberItems, familyAssociationEmailItems,
			familyAssociationNoteIndex, familyAssociationTelephoneNumberIndex,
			familyAssociationEmailIndex, createFlag, person);
			
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
			+ FAMILY_ASSOCIATION_FIELDS_MODEL_KEY, result);
		return mav;
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
	 * Returns a view for family associations action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for family associations action menu
	 */
	/*
	@RequestMapping(value = "/familyAssociationsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView familyAssociationsActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(FAMILY_ASSOCIATIONS_ACTION_MENU_VIEW_NAME, map);
	}	
	*/
	
	/**
	 * Returns a view for family association action menu pertaining to the 
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for family associations action menu
	 */
	@RequestMapping(value = "/familyAssociationActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView familyAssociationActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(FAMILY_ASSOCIATION_ACTION_MENU_VIEW_NAME, map);
	}	
	
	/**
	 * Returns a view for family association notes action menu pertaining to the
	 * specified offender.
	 * 
	 * @param offender offender
	 * @return model and view for family associations action menu
	 */
	@RequestMapping(value = "/familyAssociationNotesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView familyAssociationNotesActionMenu(@RequestParam(
		value = "offender",	required = true) final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(FAMILY_ASSOCIATION_NOTES_ACTION_MENU_VIEW_NAME, 
			map);
	}	
	
	/**
	 * Return if the marriage and divorce dates should be displayed.
	 * 
	 * @param category family association category
	 * @return display or not
	 */
	@RequestMapping(value = "/displayMarriageDivorceDate.json", 
		method = RequestMethod.GET)
	@ResponseBody public Boolean displayMarriageDivorceDate(
		@RequestParam(value = "category", required = true)
			final FamilyAssociationCategory category) {
		Boolean spouse;
		if (category.getClassification() != null
			&& FamilyAssociationCategoryClassification.SPOUSE.equals(
			category.getClassification())) {
			spouse = true;
		} else {
			spouse = false;
		}
		return spouse;
	}
	
	/**
	 * Return if the marriage and divorce dates should be displayed.
	 * 
	 * @param category family association category
	 * @return display or not
	 */
	@RequestMapping(value = "/displayMarriageDivorceDateInitial.json", 
		method = RequestMethod.GET)
	@ResponseBody public Boolean displayMarriageDivorceDateInitial(
		@RequestParam(value = "category", required = true)
			final FamilyAssociationCategory category) {
		Boolean spouse;
		if (FamilyAssociationCategoryClassification.SPOUSE.equals(
			category.getClassification())) {
			spouse = true;
		} else {
			spouse = false;
		}
		return spouse;
	}
	
	/**
	 * Adds a family association note.
	 * 
	 * @param noteItemIndex family association note index
	 * @return model and view for a new family association
	 */
	@RequestMapping(value = "/addFamilyAssociationNoteItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addFamilyAssociationNoteItem(@RequestParam(
			value = "noteItemIndex", required = true)
			final int noteItemIndex) {
		ModelMap map = new ModelMap();
		FamilyAssociationNoteItem familyAssociationNoteItem 
			= new FamilyAssociationNoteItem();
		familyAssociationNoteItem.setOperation(
			FamilyAssociationNoteItemOperation.CREATE); 
		map.addAttribute(FAMILY_ASSOCIATION_NOTE_ITEM_MODEL_KEY, 
			familyAssociationNoteItem);
		map.addAttribute(FAMILY_ASSOCIATION_NOTE_INDEX_MODEL_KEY, 
				noteItemIndex);
		return new ModelAndView(NOTE_ITEM_VIEW_NAME, map);
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
			= this.familyAssociationService.findStatesByCountry(country);
		if (PERSON_FIELDS_NAME.equals(fieldsName)) {
			return this.personFieldsControllerDelegate
			.showStateOptions(states, PERSON_FIELDS_NAME);
		} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate
			.showStateOptions(states, ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poboxFieldsControllerDelegate.showStateOptions(states,
				PO_BOX_FIELDS_NAME);
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
				this.familyAssociationService.findCitiesByState(state), 
				PERSON_FIELDS_NAME);
			} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
				return this.addressFieldsControllerDelegate.showCityOptions(
				this.familyAssociationService.findCitiesByState(state),
				ADDRESS_FIELDS_NAME);
			} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
				return this.poboxFieldsControllerDelegate.showCityOptions(
				this.familyAssociationService.findCitiesByState(state),
				PO_BOX_FIELDS_NAME);
			} else {
				throw new UnsupportedOperationException(
					String.format("Fields name not supported %s", fieldsName));
			}
		} else {
			if (this.familyAssociationService.hasStates(country)) {
				if (PERSON_FIELDS_NAME.equals(fieldsName)) {
					return this.personFieldsControllerDelegate.showCityOptions(
					this.familyAssociationService
					.findCitiesByCountryWithoutState(country), 
					PERSON_FIELDS_NAME);
				} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
					return this.addressFieldsControllerDelegate.showCityOptions(
					this.familyAssociationService
					.findCitiesByCountryWithoutState(country),
					ADDRESS_FIELDS_NAME);
				} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
					return this.poboxFieldsControllerDelegate.showCityOptions(
					this.familyAssociationService
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
					this.familyAssociationService.findCitiesByCountry(
					country), PERSON_FIELDS_NAME);
				} else if (ADDRESS_FIELDS_NAME.equals(fieldsName)) {
					return this.addressFieldsControllerDelegate.showCityOptions(
					this.familyAssociationService.findCitiesByCountry(
					country), ADDRESS_FIELDS_NAME);
				} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
					return this.poboxFieldsControllerDelegate.showCityOptions(
					this.familyAssociationService.findCitiesByCountry(
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
			this.familyAssociationService.findZipCodesByCity(city),
			ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poboxFieldsControllerDelegate.showZipCodeOptions(
			this.familyAssociationService.findZipCodesByCity(city),
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
	@RequestMapping(value = "/telephoneNumberActionMenu.html",
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
	@RequestMapping(value = "/addFamilyAssociationTelephoneNumberItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addTelephoneNumberItem(@RequestParam(
		value = "telephoneNumberItemIndex", required = true)
		final int telephoneNumberItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY, 
			telephoneNumberItemIndex);
		map.addAttribute(TELEPHONE_NUMBER_CATEGORY_MODEL_KEY, 
			TelephoneNumberCategory.values());
		FamilyAssociationTelephoneNumberItem 
			familyAssociationTelephoneNumberItem 
			= new FamilyAssociationTelephoneNumberItem();
		familyAssociationTelephoneNumberItem.setOperation(
			FamilyAssociationTelephoneNumberItemOperation.CREATE);
		map.addAttribute(FAMILY_ASSOCIATION_TELEPHONE_NUMBER_ITEM_MODEL_KEY, 
			familyAssociationTelephoneNumberItem);
		return new ModelAndView(
				CREATE_FAMILY_ASSOCIATION_TELEPHONE_NUMBER_TABLE_ROW_VIEW_NAME, 
				map);
	}
	
	/**
	 * Adds an email item.
	 * 
	 * @param emailItemIndex email item index
	 * @return model and view for an email item view
	 */
	@RequestMapping(value = "/addFamilyAssociationEmailItem.html", 
		method = RequestMethod.GET)
	public ModelAndView addEmailItem(@RequestParam(
		value = "emailItemIndex", required = true)
		final int emailItemIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(EMAIL_ITEM_INDEX_MODEL_KEY, emailItemIndex);
		List<OnlineAccountHost> onlineAccountHosts 
			= this.familyAssociationService.findOnlineAccountHosts();
		map.addAttribute(ONLINE_ACCOUNT_HOSTS_MODEL_KEY, onlineAccountHosts);
		FamilyAssociationOnlineAccountItem familyAssociationEmailItem 
			= new FamilyAssociationOnlineAccountItem();
		familyAssociationEmailItem.setOperation(
			FamilyAssociationOnlineAccountItemOperation.CREATE);
		map.addAttribute(FAMILY_ASSOCIATION_EMAIL_ITEM_MODEL_KEY, 
			familyAssociationEmailItem);
		return new ModelAndView(
			CREATE_FAMILY_ASSOCIATION_ONLINE_ACCOUNT_TABLE_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Returns a view for email action menu pertaining to the specified 
	 * offender.
	 * 
	 * @param offender offender
	 * @return model and view for online account action menu
	 */
	@RequestMapping(value = "/onlineAccountActionMenu.html",
		method = RequestMethod.GET)
	public ModelAndView emailActionMenu(@RequestParam(
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
		 	final String searchCriteria) throws IOException {
		List<Address> addresses;
		if (StringUtility.hasContent(searchCriteria)) {
			addresses = this.familyAssociationService.findAddresses(
					searchCriteria);
		} else {
			addresses = Collections.emptyList();
		}
		ModelAndView mav = new ModelAndView(ADDRESSES_VIEW_NAME);
		mav.addObject(ADDRESSES_MODEL_KEY, addresses);
		return mav;
	}
	
	/**
	 * Handles {@code ResidenceTermExistsException}.
	 * 
	 * @param ResidenceTermExistsException exception thrown
	 * @return screen to handle {@code ResidenceTermExistsException}
	 */
	@ExceptionHandler(ResidenceTermExistsException.class)
	public ModelAndView handleResidenceTermExistsException(
		final ResidenceTermExistsException ResidenceTermExistsException) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
			RESIDENCE_TREM_EXISTS_EXCEPTION_MESSAGE_KEY,
			ERROR_BUNDLE_NAME, ResidenceTermExistsException);
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
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Country.class,
			this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Offender.class,
			this.offenderPropertyEditorFactory.createOffenderPropertyEditor());
		binder.registerCustomEditor(UserAccount.class,
			this.userAccountPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
			this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
			this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class,
			this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Person.class,
			this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(VerificationMethod.class,
			this.verificationMethodPropertyEditorFactory
			.createPropertyEditor());
		binder.registerCustomEditor(Date.class, "startDate",
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "startTime",
			this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endDate",
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Date.class, "endTime",
			this.customDateEditorFactory.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(Date.class,
			this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(FamilyAssociationCategory.class,
			this.familyAssociationCategoryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(StreetSuffix.class,
			this.streetSuffixPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(AddressUnitDesignator.class,
			this.addressUnitDesignatorPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(FamilyAssociationNote.class,
			this.familyAssociationNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
			this.locationPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccountHost.class,
			this.onlineAccountHostPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(FamilyAssociation.class,
			this.familyAssociationPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Sex.class,
			this.sexPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Address.class,
			this.addressPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(TelephoneNumber.class,
			this.telephoneNumberPropertyEditorFactory.createPropertyEditor());
	}
	
	// Returns true if value is true; false otherwise
	private Boolean resolveCheckBoxValue(final Boolean value) {
		return value != null && value;
	}
	
	/**
	 * Handles FamilyAssociationExistsException}.
	 * 
	 * @param familyAssociationExistsException exception thrown
	 * @return screen to handle FamilyAssociationExistsException
	 */
	@ExceptionHandler(FamilyAssociationExistsException.class)
	public ModelAndView handleFamilyAssociationExistsException(
			final FamilyAssociationExistsException 
				familyAssociationExistsException) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(FAMILY_ASSOCIATION_EXISTS_MESSAGE_KEY,
						ERROR_BUNDLE_NAME, familyAssociationExistsException);
	}
	
	/**
	 * Handles FamilyAssociationConflictException}.
	 * 
	 * @param familyAssociationConflictException exception thrown
	 * @return screen to handle FamilyAssociationConflictException
	 */
	@ExceptionHandler(FamilyAssociationConflictException.class)
	public ModelAndView handleFamilyAssociationConflictException(
			final FamilyAssociationConflictException 
				familyAssociationConflictException) {
		return this.businessExceptionHandlerDelegate
				.prepareModelAndView(FAMILY_ASSOCIATION_CONFLICT_MESSAGE_KEY,
						ERROR_BUNDLE_NAME, familyAssociationConflictException);
	}
}