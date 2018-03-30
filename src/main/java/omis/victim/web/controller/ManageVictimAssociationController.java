/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.victim.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.web.controller.delegate.AddressFieldsControllerDelegate;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.contact.domain.Contact;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.OnlineAccountExistsException;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.contact.web.controller.delegate.PoBoxFieldsControllerDelegate;
import omis.country.domain.Country;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.person.web.delegate.PersonFieldsControllerDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.util.StringUtility;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.victim.exception.VictimNoteExistsException;
import omis.victim.service.VictimAssociationService;
import omis.victim.web.controller.delegate.VictimSummaryModelDelegate;
import omis.victim.web.form.VictimAssociationForm;
import omis.victim.web.form.VictimFields;
import omis.victim.web.form.VictimMailingAddressOperation;
import omis.victim.web.form.VictimNoteItem;
import omis.victim.web.form.VictimNoteItemOperation;
import omis.victim.web.form.VictimOnlineAccountItem;
import omis.victim.web.form.VictimOnlineAccountOperation;
import omis.victim.web.form.VictimTelephoneNumberItem;
import omis.victim.web.form.VictimTelephoneNumberOperation;
import omis.victim.web.validator.VictimAssociationFormValidator;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller to create, update and remove victim associations.
 *
 * @author Stephen Abson
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 30, 2015)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/victim/association")
public class ManageVictimAssociationController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "victim/association/edit";
	
	private static final String TELEPHONE_NUMBER_ITEM_VIEW_NAME
		= "victim/association/includes/telephoneNumberEditTableRow";

	private static final String ONLINE_ACCOUNT_ITEM_VIEW_NAME
		= "victim/association/includes/onlineAccountEditTableRow";
	
	private static final String NOTE_TABLE_ROW_VIEW_NAME
		= "victim/note/includes/editTableRow";
	
	private static final String ADDRESS_VIEW_NAME = "address/json/addresses";
	
	/* Action menu view names. */
	
	private static final String ACTION_MENU_VIEW_NAME
		= "victim/association/includes/victimAssociationActionMenu";
	
	private static final String NOTES_ACTION_MENU_VIEW_NAME
		= "victim/association/includes/victimAssociationNotesActionMenu";
	
	private static final String TELEPHONE_NUMBERS_ACTION_MENU_VIEW_NAME
		= "victim/association/includes"
				+ "/victimAssociationTelephoneNumbersActionMenu";

	private static final String ONLINE_ACCOUNTS_ACTION_MENU_VIEW_NAME
		= "victim/association/includes"
				+ "/victimAssociationOnlineAccountsActionMenu";
	
	/* Redirects. */
	
	private static final String OFFENDER_REDIRECT
		= "redirect:/victim/association/listByOffender.html?offender=%d";

	private static final String VICTIM_REDIRECT
		= "redirect:/victim/association/listByVictim.html?victim=%d";
	
	/* Model keys. */
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String VICTIM_MODEL_KEY = "victim";

	private static final String VICTIM_ASSOCIATION_FORM_MODEL_KEY
		= "victimAssociationForm";

	private static final String VICTIM_ASSOCIATION_MODEL_KEY
		= "victimAssociation";
	
	private static final String VICTIM_NOTE_CATEGORIES_MODEL_KEY
		 = "victimNoteCategories";
	
	private static final String VICTIM_NOTE_ITEM_INDEX_MODEL_KEY
		= "victimNoteItemIndex";
	
	private static final String VICTIM_NOTE_ITEM_MODEL_KEY
		= "victimNoteItem";

	private static final String TELEPHONE_NUMBER_CATEGORIES_MODEL_KEY
		= "telephoneNumberCategories";

	private static final String TELEPHONE_NUMBER_ITEM_MODEL_KEY
		= "telephoneNumberItem";

	private static final String TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY
		= "telephoneNumberItemIndex";

	private static final String ONLINE_ACCOUNT_HOSTS_MODEL_KEY
		= "onlineAccountHosts";

	private static final String ONLINE_ACCOUNT_ITEM_MODEL_KEY
		= "onlineAccountItem";

	private static final String ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY
		= "onlineAccountItemIndex";
	
	private static final String ADDRESSES_MODEL_KEY = "addresses";
	
	/* Fields names. */
	
	private static final String PERSON_FIELDS_NAME = "personFields";
	
	private static final String MAILING_ADDRESS_FIELDS_NAME
		= "mailingAddressFields";
	
	private static final String PO_BOX_FIELDS_NAME = "poBoxFields";
	
	/* Message keys. */
	
	private static final String VICTIM_EXISTS_MESSAGE_KEY
		= "victimAssociation.exists";
	
	private static final String VICTIM_NOTE_EXISTS_MESSAGE_KEY
		= "victimNote.exists";

	private static final String REFLEXIVE_RELATIONSHIP_MESSAGE_KEY
		= "victimAssociation.reflexiveRelationship";
	
	private static final String CITY_EXISTS_MESSAGE_KEY
		= "city.exists";
	
	private static final String ZIP_CODE_EXISTS_MESSAGE_KEY
		= "zipCode.exists";
	
	private static final String ADDRESS_EXISTS_MESSAGE_KEY
		= "address.exists";
	
	private static final String TELEPHONE_NUMBER_EXISTS_MESSAGE_KEY
		= "telephoneNumber.duplicate";
	
	private static final String ONLINE_ACCOUNT_EXISTS_MESSAGE_KEY
		= "onlineAccount.duplicate";
	
	/* Message bundles. */
	
	private static final String ERROR_BUNDLE_NAME = "omis.victim.msgs.form";
	private static final String CONTACT_ERROR_BUNDLE_NAME 
		= "omis.contact.msgs.form";
	private static final String ADDRESS_ERROR_BUNDLE_NAME 
		= "omis.address.msgs.form";
	private static final String REGION_ERROR_BUNDLE_NAME
		= "omis.region.msgs.form";
	
	/* Services. */

	@Autowired
	@Qualifier("victimAssociationService")
	private VictimAssociationService victimAssociationService;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("victimSummaryModelDelegate")
	private VictimSummaryModelDelegate victimSummaryModelDelegate;
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("personFieldsControllerDelegate")
	private PersonFieldsControllerDelegate personFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("addressFieldsControllerDelegate")
	private AddressFieldsControllerDelegate addressFieldsControllerDelegate;
	
	@Autowired
	@Qualifier("poBoxFieldsControllerDelegate")
	private PoBoxFieldsControllerDelegate poboxFieldsControllerDelegate;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	@Qualifier("victimAssociationPropertyEditorFactory")
	private PropertyEditorFactory victimAssociationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("victimNoteCategoryPropertyEditorFactory")
	private PropertyEditorFactory victimNoteCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("victimNotePropertyEditorFactory")
	private PropertyEditorFactory victimNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("contactPropertyEditorFactory")
	private PropertyEditorFactory contactPropertyEditorFactory;
	
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
	@Qualifier("addressPropertyEditorFactory")
	private PropertyEditorFactory addressPropertyEditorFactory;
	
	@Autowired
	@Qualifier("streetSuffixPropertyEditorFactory")
	private PropertyEditorFactory streetSuffixPropertyEditorFactory;
	
	@Autowired
	@Qualifier("addressUnitDesignatorPropertyEditorFactory")
	private PropertyEditorFactory addressUnitDesignatorPropertyEditorFactory;
	
	@Autowired
	@Qualifier("zipCodePropertyEditorFactory")
	private PropertyEditorFactory zipCodePropertyEditorFactory;
	
	@Autowired
	@Qualifier("onlineAccountHostPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountHostPropertyEditorFactory;
	
	@Autowired
	@Qualifier("telephoneNumberPropertyEditorFactory")
	private PropertyEditorFactory telephoneNumberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("onlineAccountPropertyEditorFactory")
	private PropertyEditorFactory onlineAccountPropertyEditorFactory;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("victimAssociationFormValidator")
	private VictimAssociationFormValidator victimAssociationFormValidator;
	
	/* Constructors. */

	/**
	 * Instantiates controller to create, update and remove victim
	 * associations.
	 */
	public ManageVictimAssociationController() {
		// Default instantiation
	}
	
	/**
	 * Shows screen to create victim association.
	 * 
	 * @param offender offender with whom to associate victim
	 * @param victim victim
	 * @param redirectTarget redirect target
	 * @return screen to create victim association
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_CREATE')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "victim", required = false)
				final Person victim,
			final VictimRedirectTarget redirectTarget) {
		VictimAssociationForm victimAssociationForm
			= new VictimAssociationForm();
		if (victim == null) {
			victimAssociationForm.setShowPersonFields(true);
			victimAssociationForm.setShowMailingAddressFields(true);
			victimAssociationForm.setShowPoBoxFields(true);
			victimAssociationForm.setShowTelephoneNumberItems(true);
			victimAssociationForm.setShowOnlineAccountItems(true);
			victimAssociationForm.setMailingAddressOperation(
					VictimMailingAddressOperation.USE_EXISTING);
		}
		ModelAndView mav = this.prepareEditMav(
				victimAssociationForm, offender, victim);
		if (victim != null) {
			mav.addObject(VICTIM_MODEL_KEY, victim);
		}
		return mav;
	}
	
	/**
	 * Shows screen to edit victim association.
	 * 
	 * @param victimAssociation victim association to edit
	 * @param redirectTarget redirect target
	 * @return screen to edit victim association
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_VIEW')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "victimAssociation", required = true)
				final VictimAssociation victimAssociation,
			final VictimRedirectTarget redirectTarget) {
		VictimAssociationForm victimAssociationForm
			= new VictimAssociationForm();
		victimAssociationForm.setShowPersonFields(false);
		victimAssociationForm.setShowMailingAddressFields(false);
		victimAssociationForm.setShowPoBoxFields(false);
		victimAssociationForm.setShowTelephoneNumberItems(false);
		victimAssociationForm.setShowOnlineAccountItems(false);
		VictimFields victimFields = new VictimFields();
		victimFields.setRegisterDate(victimAssociation.getRegisterDate());
		victimFields.setPacketSendDate(victimAssociation.getPacketSendDate());
		victimFields.setPacketSent(victimAssociation.getPacketSent());
		if (victimAssociation.getFlags() != null) {
			victimFields.setVictim(victimAssociation.getFlags().getVictim());
			victimFields.setDocRegistered(victimAssociation.getFlags()
					.getDocRegistered());
			victimFields.setBusiness(victimAssociation.getFlags()
					.getBusiness());
			victimFields.setVineRegistered(victimAssociation.getFlags()
					.getVineRegistered());
		} else {
			victimFields.setVictim(false);
			victimFields.setDocRegistered(false);
			victimFields.setBusiness(false);
			victimFields.setVineRegistered(false);
		}
		victimAssociationForm.setVictimFields(victimFields);
		List<VictimNote> victimNotes = this.victimAssociationService
				.findNotesByAssociation(victimAssociation);
		List<VictimNoteItem> victimNoteItems = new ArrayList<VictimNoteItem>();
		for (VictimNote victimNote : victimNotes) {
			VictimNoteItem victimNoteItem = new VictimNoteItem();
			victimNoteItem.setAssociation(victimAssociation);
			victimNoteItem.setCategory(victimNote.getCategory());
			victimNoteItem.setDate(victimNote.getDate());
			victimNoteItem.setNote(victimNote);
			victimNoteItem.setOperation(VictimNoteItemOperation.UPDATE);
			victimNoteItem.setValue(victimNote.getValue());
			victimNoteItems.add(victimNoteItem);
		}
		victimAssociationForm.setNoteItems(victimNoteItems);
		Offender offender = (Offender) victimAssociation.getRelationship()
				.getFirstPerson();
		Person victim = victimAssociation.getRelationship().getSecondPerson();
		ModelAndView mav = this.prepareEditMav(
				victimAssociationForm, offender, victim);
		mav.addObject(VICTIM_ASSOCIATION_MODEL_KEY, victimAssociation);
		mav.addObject(VICTIM_MODEL_KEY, victim);
		return mav;
	}
	
	/**
	 * Creates new victim association.
	 * 
	 * @param offender offender with whom to associate victim
	 * @param victim victim
	 * @param redirectTarget redirect target
	 * @param victimAssociationForm form for victim associations
	 * @param result binding result
	 * @return redirect to victim association listing screen
	 * @throws ReflexiveRelationshipException if offender is victim
	 * @throws VictimExistsException if victim association already
	 * exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if zip code exits
	 * @throws AddressExistsException if address exists
	 * @throws TelephoneNumberExistsException if telephone number exists
	 * @throws OnlineAccountExistsException if online account exists
	 * @throws VictimNoteExistsException if victim note exists
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_CREATE')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "victim", required = false)
				final Person victim,
			final VictimRedirectTarget redirectTarget,
			final VictimAssociationForm victimAssociationForm,
			final BindingResult result)
					throws VictimExistsException,
						ReflexiveRelationshipException, 
						CityExistsException, ZipCodeExistsException, 
						AddressExistsException, TelephoneNumberExistsException, 
						OnlineAccountExistsException, 
						VictimNoteExistsException {
		this.victimAssociationFormValidator
			.validate(victimAssociationForm, result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(
					victimAssociationForm, result, offender, victim);
		}
		Person victimToAssociate;
		City birthCity;
		if (victimAssociationForm.getShowPersonFields() != null
				&& victimAssociationForm.getShowPersonFields()) {
			if (victimAssociationForm.getPersonFields().getNewCity() != null
					&& victimAssociationForm.getPersonFields().getNewCity()) {
				try {
					birthCity = this.victimAssociationService
						.createCity(
								victimAssociationForm.getPersonFields()
									.getCityName(),
								victimAssociationForm.getPersonFields()
									.getBirthState(),
								victimAssociationForm.getPersonFields()
									.getBirthCountry());
				} catch (CityExistsException e) {
					
					// Front end should ensure that city does not exist - SA
					throw new AssertionError("City exists", e);
				}
			} else {
				birthCity = victimAssociationForm.getPersonFields()
						.getBirthCity();
			}
			Integer socialSecurityNumber;
			if (victimAssociationForm.getPersonFields()
						.getSocialSecurityNumber() != null
						&& !victimAssociationForm
								.getPersonFields()
									.getSocialSecurityNumber().isEmpty()) {
				socialSecurityNumber = Integer.valueOf(
						victimAssociationForm.getPersonFields()
							.getSocialSecurityNumber().replaceAll("-", ""));
			} else {
				socialSecurityNumber = null;
			}
			victimToAssociate = this.victimAssociationService.createVictim(
					victimAssociationForm.getPersonFields().getLastName(),
					victimAssociationForm.getPersonFields().getFirstName(),
					victimAssociationForm.getPersonFields().getMiddleName(),
					victimAssociationForm.getPersonFields().getSuffix(),
					victimAssociationForm.getPersonFields().getSex(),
					victimAssociationForm.getPersonFields().getBirthDate(),
					victimAssociationForm.getPersonFields().getBirthCountry(),
					victimAssociationForm.getPersonFields().getBirthState(),
					birthCity, socialSecurityNumber,
					victimAssociationForm.getPersonFields().getStateIdNumber(),
					victimAssociationForm.getPersonFields().getDeceased(),
					victimAssociationForm.getPersonFields().getDeathDate());
		} else {
			victimToAssociate = victim;
			birthCity = null;
		}
		VictimAssociation victimAssociation
			= this.victimAssociationService.associate(
				offender, victimToAssociate,
				victimAssociationForm.getVictimFields().getRegisterDate(),
				victimAssociationForm.getVictimFields().getPacketSent(),
				victimAssociationForm.getVictimFields().getPacketSendDate(),
				new VictimAssociationFlags(
						victimAssociationForm.getVictimFields().getVictim(),
						victimAssociationForm.getVictimFields()
							.getDocRegistered(),
						victimAssociationForm.getVictimFields().getBusiness(),
						victimAssociationForm.getVictimFields()
							.getVineRegistered()));
		PoBox poBox;
		Address mailingAddress;
		City mailingCity;
		ZipCode mailingZipCode;
		if (victimAssociationForm.getShowMailingAddressFields() != null
				&& victimAssociationForm.getShowMailingAddressFields()
				&& victimAssociationForm.getEnterMailingAddressFields() != null
				&& victimAssociationForm.getEnterMailingAddressFields()) {
			if (VictimMailingAddressOperation.CREATE_NEW.equals(
					victimAssociationForm.getMailingAddressOperation())) {
				if (victimAssociationForm.getMailingAddressFields()
							.getNewCity() != null
						&& victimAssociationForm.getMailingAddressFields()
							.getNewCity()) {
					if (birthCity != null
							&& birthCity.getName().equals(
								victimAssociationForm.getMailingAddressFields()
									.getCityName())
							&& (birthCity.getState() != null
								&& birthCity.getState().equals(
								victimAssociationForm.getMailingAddressFields()
									.getState())
									|| (birthCity.getState() == null
											&& victimAssociationForm
												.getMailingAddressFields()
													.getState() == null))
							&& birthCity.getCountry().equals(
								victimAssociationForm.getMailingAddressFields()
									.getCountry())) {
						mailingCity = birthCity;
					} else {
						mailingCity = this.victimAssociationService
								.createCity(victimAssociationForm
												.getMailingAddressFields()
												.getCityName(),
											victimAssociationForm
												.getMailingAddressFields()
												.getState(),
											victimAssociationForm
												.getMailingAddressFields()
												.getCountry());
					}
				} else {
					mailingCity = victimAssociationForm
							.getMailingAddressFields().getCity();
				}
				if (victimAssociationForm.getMailingAddressFields()
						.getNewZipCode() != null
						&& victimAssociationForm.getMailingAddressFields()
							.getNewZipCode()) {
					mailingZipCode = this.victimAssociationService
							.createZipCode(victimAssociationForm
										.getMailingAddressFields()
											.getZipCodeValue(),
									victimAssociationForm
										.getMailingAddressFields()
											.getZipCodeExtension(),
									mailingCity);
				} else {
					mailingZipCode
						= victimAssociationForm.getMailingAddressFields()
							.getZipCode();
				}
				mailingAddress = this.victimAssociationService
						.createAddress(victimAssociationForm
							.getMailingAddressFields().getValue(),
							mailingZipCode);
			} else if (VictimMailingAddressOperation.USE_EXISTING.equals(
					victimAssociationForm.getMailingAddressOperation())) {
				mailingAddress = victimAssociationForm
						.getExistingMailingAddress();
				mailingZipCode = null;
				mailingCity = null;
			} else {
				
				// Prevents other mailing address operations
				throw new UnsupportedOperationException(String.format(
						"Unsupported mailing address operation: %s",
						victimAssociationForm.getMailingAddressOperation()));
			}
		} else {
			mailingAddress = null;
			mailingCity = null;
			mailingZipCode = null;
		}
		if (victimAssociationForm.getShowPoBoxFields() != null
				&& victimAssociationForm.getShowPoBoxFields()
				&& victimAssociationForm.getEnterPoBoxFields() != null
				&& victimAssociationForm.getEnterPoBoxFields()) {
			ZipCode poBoxZipCode;
			City poBoxCity;
			if (victimAssociationForm.getPoBoxFields().getNewCity() != null
					&& victimAssociationForm.getPoBoxFields().getNewCity()) {
				if (birthCity != null
							&& birthCity.getName().equals(
								victimAssociationForm.getMailingAddressFields()
									.getCityName())
							&& (birthCity.getState().equals(
								victimAssociationForm.getMailingAddressFields()
									.getState())
									|| (birthCity.getState() == null
											&& victimAssociationForm
												.getMailingAddressFields()
													.getState() == null))
							&& birthCity.getCountry().equals(
								victimAssociationForm.getMailingAddressFields()
									.getCountry())) {
					poBoxCity = birthCity;
				} else if (mailingCity != null
							&& mailingCity.getName().equals(
								victimAssociationForm.getMailingAddressFields()
									.getCityName())
							&& (mailingCity.getState().equals(
								victimAssociationForm.getMailingAddressFields()
									.getState())
									|| (mailingCity.getState() == null
											&& victimAssociationForm
												.getMailingAddressFields()
													.getState() == null))
							&& mailingCity.getCountry().equals(
								victimAssociationForm.getMailingAddressFields()
									.getCountry())) {
					poBoxCity = mailingCity;
				} else {
					poBoxCity = this.victimAssociationService
							.createCity(
									victimAssociationForm.getPoBoxFields()
										.getCityName(),
									victimAssociationForm.getPoBoxFields()
										.getState(),
									victimAssociationForm.getPoBoxFields()
										.getCountry());
				}
			} else {
				poBoxCity = victimAssociationForm.getPoBoxFields()
						.getCity();
			}
			if (victimAssociationForm.getPoBoxFields()
						.getNewZipCode() != null
					&& victimAssociationForm.getPoBoxFields()
						.getNewZipCode()) {
				if (mailingZipCode != null
						&& mailingZipCode.getValue().equals(
								victimAssociationForm.getPoBoxFields()
									.getZipCodeValue())
						&& mailingZipCode.getExtension().equals(
								victimAssociationForm.getPoBoxFields()
									.getZipCodeExtension())
						&& mailingZipCode.getCity().equals(poBoxCity)) {
					poBoxZipCode = mailingZipCode;
				} else {
					poBoxZipCode = this.victimAssociationService
							.createZipCode(
								victimAssociationForm
									.getPoBoxFields().getZipCodeValue(),
								victimAssociationForm
									.getPoBoxFields().getZipCodeExtension(),
								poBoxCity);
				}
			} else {
				poBoxZipCode = victimAssociationForm.getPoBoxFields()
						.getZipCode();
			}
			poBox = new PoBox(
					victimAssociationForm.getPoBoxFields().getPoBoxValue(),
					poBoxZipCode);
		} else {
			poBox = null;
		}
		if (poBox != null || mailingAddress != null) {
			this.victimAssociationService.changeContact(
					victimToAssociate, poBox, mailingAddress);
		}
				
		if (victimAssociationForm.getShowTelephoneNumberItems() != null
				&& victimAssociationForm.getShowTelephoneNumberItems()
				&& victimAssociationForm.getTelephoneNumberItems() != null) {
			for (VictimTelephoneNumberItem telephoneNumberItem
					: victimAssociationForm.getTelephoneNumberItems()) {
				if (telephoneNumberItem != null
						&& telephoneNumberItem.getOperation() != null) {
					Boolean telephoneNumberPrimary;
					if (telephoneNumberItem.getFields().getPrimary()
							!= null && telephoneNumberItem.getFields()
							.getPrimary()) {
						telephoneNumberPrimary = true;
					} else {
						telephoneNumberPrimary = false;
					}
					Boolean telephoneNumberActive;
					if (telephoneNumberItem.getFields().getActive() != null
							&& telephoneNumberItem.getFields().getActive()) {
						telephoneNumberActive = true;
					} else {
						telephoneNumberActive = false;
					}
					if (VictimTelephoneNumberOperation.CREATE.equals(
							telephoneNumberItem.getOperation())) {
						this.victimAssociationService.createTelephoneNumber(
								victimToAssociate,
								telephoneNumberItem.getFields().getValue(),
								telephoneNumberItem.getFields().getExtension(),
								telephoneNumberPrimary, telephoneNumberActive,
								telephoneNumberItem.getFields().getCategory());
					} else {
						throw new UnsupportedOperationException(String.format(
								"Unsupported telephone number operation %s",
								telephoneNumberItem.getOperation()));
					}
				}
			}
		}
		if (victimAssociationForm.getShowOnlineAccountItems() != null
				&& victimAssociationForm.getShowOnlineAccountItems()
				&& victimAssociationForm.getOnlineAccountItems() != null) {
			for (VictimOnlineAccountItem onlineAccountItem
					: victimAssociationForm.getOnlineAccountItems()) {
				if (onlineAccountItem != null
						&& onlineAccountItem.getOperation() != null) {
					Boolean onlineAccountPrimary;
					if (onlineAccountItem.getFields().getPrimary()
							!= null && onlineAccountItem.getFields()
							.getPrimary()) {
						onlineAccountPrimary = true;
					} else {
						onlineAccountPrimary = false;
					}
					Boolean onlineAccountActive;
					if (onlineAccountItem.getFields().getActive() != null
							&& onlineAccountItem.getFields().getActive()) {
						onlineAccountActive = true;
					} else {
						onlineAccountActive = false;
					}
					if (VictimOnlineAccountOperation.CREATE
							.equals(onlineAccountItem.getOperation())) {
						this.victimAssociationService.createOnlineAccount(
								victimToAssociate,
								onlineAccountItem.getFields().getName(),
								onlineAccountItem.getFields().getHost(),
								onlineAccountPrimary, onlineAccountActive);
						
					} else {
						throw new UnsupportedOperationException(String.format(
								"Unsupported online account operation %s",
								onlineAccountItem.getOperation()));
					}
				}
			}
		}
		if (victimAssociationForm.getNoteItems() != null) {
			for (VictimNoteItem victimNoteItem
					: victimAssociationForm.getNoteItems()) {
				if (victimNoteItem != null
						&& victimNoteItem.getOperation() != null) {
					if (VictimNoteItemOperation.CREATE
							.equals(victimNoteItem.getOperation())) {
						this.victimAssociationService.addNote(
								victimAssociation, victimNoteItem.getCategory(),
								victimNoteItem.getDate(),
								victimNoteItem.getValue());
					} else {
						throw new UnsupportedOperationException(String.format(
								"Unsupported victim note operation %s",
								victimNoteItem.getOperation()));
					}
				}
			}
		}
		return this.prepareListRedirect(
				offender, victimToAssociate, redirectTarget);
	}

	/**
	 * Updates victim association.
	 * 
	 * @param victimAssociation victim association to update
	 * @param redirectTarget redirect target
	 * @param victimAssociationForm victim association form
	 * @param result binding result
	 * @return redirect to victim association listing screen
	 * @throws VictimExistsException if victim association already
	 * exists
	 * @throws VictimNoteExistsException if victim note exists 
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_EDIT')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "victimAssociation", required = true)
				final VictimAssociation victimAssociation,
			final VictimRedirectTarget redirectTarget,
			final VictimAssociationForm victimAssociationForm,
			final BindingResult result) throws VictimExistsException, 			
				VictimNoteExistsException {
		Offender offender = (Offender) victimAssociation.getRelationship()
				.getFirstPerson();
		this.victimAssociationFormValidator
			.validate(victimAssociationForm, result);
		if (result.hasErrors()) {
			Person victim = victimAssociation.getRelationship()
					.getSecondPerson();
			ModelAndView mav = this.prepareRedisplayMav(
					victimAssociationForm, result, offender, victim);
			mav.addObject(VICTIM_ASSOCIATION_MODEL_KEY, victimAssociation);
			mav.addObject(VICTIM_MODEL_KEY, victim); 
			return mav;
		}
		this.victimAssociationService.update(victimAssociation,
				victimAssociationForm.getVictimFields().getRegisterDate(),
				victimAssociationForm.getVictimFields().getPacketSent(),
				victimAssociationForm.getVictimFields().getPacketSendDate(),
				new VictimAssociationFlags(
						victimAssociationForm.getVictimFields().getVictim(),
						victimAssociationForm.getVictimFields()
							.getDocRegistered(),
						victimAssociationForm.getVictimFields().getBusiness(),
						victimAssociationForm.getVictimFields()
							.getVineRegistered()));
		if (victimAssociationForm.getShowPersonFields() != null
				&& victimAssociationForm.getShowPersonFields()) {
			
			// Use screen that validates other relationship data entry
			// requirements
			throw new UnsupportedOperationException(
					"Person update not allowed for existing victim");
		}
		if (victimAssociationForm.getShowMailingAddressFields() != null
				&& victimAssociationForm.getShowMailingAddressFields()
				&& victimAssociationForm.getEnterMailingAddressFields() != null
				&& victimAssociationForm.getEnterMailingAddressFields()) {
			
			// Use screen that validates other relationship data entry
			// requirements
			throw new UnsupportedOperationException(
					"Mailing address update not allowed for existing victim");
		}
		if (victimAssociationForm.getShowPoBoxFields() != null
				&& victimAssociationForm.getShowPoBoxFields()
				&& victimAssociationForm.getEnterPoBoxFields() != null
				&& victimAssociationForm.getEnterPoBoxFields()) {
			
			// Use screen that validates other relationship data entry
			// requirements
			throw new UnsupportedOperationException(
					"PO box update not allowed for existing victim");
		}
		if (victimAssociationForm.getShowTelephoneNumberItems() != null
				&& victimAssociationForm.getShowTelephoneNumberItems()) {
			
			// Use screen that validates other relationship data entry
			// requirements
			throw new UnsupportedOperationException(
					"Telephone number update not allowed for existing victim");
		}
		if (victimAssociationForm.getShowOnlineAccountItems() != null
				&& victimAssociationForm.getShowOnlineAccountItems()) {
			
			// Use screen that validates other relationship data entry
			// requirements
			throw new UnsupportedOperationException(
					"Online account update not allowed for existing victim");
		}
		if (victimAssociationForm.getNoteItems() != null) {
			for (VictimNoteItem victimNoteItem
					: victimAssociationForm.getNoteItems()) {
				if (VictimNoteItemOperation.CREATE
						.equals(victimNoteItem.getOperation())) {
					this.victimAssociationService.addNote(
							victimAssociation, victimNoteItem.getCategory(),
							victimNoteItem.getDate(),
							victimNoteItem.getValue());
				} else if (VictimNoteItemOperation.UPDATE
						.equals(victimNoteItem.getOperation())) {
					this.victimAssociationService.updateNote(
							victimNoteItem.getNote(), 
							victimNoteItem.getCategory(), 
							victimNoteItem.getDate(), 
							victimNoteItem.getValue());
				} else if (VictimNoteItemOperation.REMOVE
						.equals(victimNoteItem.getOperation())) {
					this.victimAssociationService.removeNote(
							victimNoteItem.getNote());
				} else if (VictimNoteItemOperation.REMOVE_ASSOCIATION
						.equals(victimNoteItem.getOperation())) {
					this.victimAssociationService.disassociateNote(
							victimNoteItem.getNote());
				}
			}
		}
		Person victim = victimAssociation.getRelationship().getSecondPerson();
		return this.prepareListRedirect(offender, victim, redirectTarget);
	}
	
	/**
	 * Removes victim association.
	 * 
	 * @param victimAssociation association to remove
	 * @param removeNotes whether to remove associated notes - if {@code true}
	 * remove notes, if {@code false} disassociate notes; if {@code null}
	 * attempt to remove the associate with no regard for what notes may exist
	 * (most likely, this will cause an error if notes do exist)
	 * @param redirectTarget redirect target
	 * @return redirect to victim association listing screen
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('VICTIM_ASSOCIATION_REMOVE')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "victimAssociation", required = true)
				final VictimAssociation victimAssociation,
			@RequestParam(value = "removeNotes", required = false)
				final Boolean removeNotes,
			final VictimRedirectTarget redirectTarget) {
		Offender offender = (Offender) victimAssociation.getRelationship()
				.getFirstPerson();
		Person victim = victimAssociation.getRelationship().getSecondPerson();
		if (removeNotes != null) {
			if (removeNotes) {
				this.victimAssociationService.removeWithNotes(
						victimAssociation);
			} else {
				this.victimAssociationService.removeAndDisassociateNotes(
						victimAssociation);
			}
		} else {
			this.victimAssociationService.remove(victimAssociation);
		}
		return this.prepareListRedirect(offender, victim, redirectTarget);
	}
	
	/* Helper methods. */
	
	// Prepares model and view to edit victim association
	private ModelAndView prepareEditMav(
			final VictimAssociationForm victimAssociationForm,
			final Offender offender, final Person victim) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(VICTIM_ASSOCIATION_FORM_MODEL_KEY, victimAssociationForm);
		mav.addObject(VICTIM_NOTE_CATEGORIES_MODEL_KEY,
				this.victimAssociationService
					.findNoteCategories());
		int victimNoteItemIndex;
		if (victimAssociationForm.getNoteItems() != null) {
			victimNoteItemIndex = victimAssociationForm.getNoteItems()
					.size();
		} else {
			victimNoteItemIndex = 0;
		}
		mav.addObject(VICTIM_NOTE_ITEM_INDEX_MODEL_KEY, victimNoteItemIndex);
		int victimContactTelephoneNumberItemIndex;
		if (victimAssociationForm.getTelephoneNumberItems() != null) {
			victimContactTelephoneNumberItemIndex
				= victimAssociationForm.getTelephoneNumberItems().size();
		} else {
			victimContactTelephoneNumberItemIndex = 0;
		}
		mav.addObject(TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY,
				victimContactTelephoneNumberItemIndex);
		mav.addObject(TELEPHONE_NUMBER_CATEGORIES_MODEL_KEY,
				TelephoneNumberCategory.values());
		int victimContactOnlineAccountItemIndex;
		if (victimAssociationForm.getOnlineAccountItems() != null) {
			victimContactOnlineAccountItemIndex
				= victimAssociationForm.getOnlineAccountItems().size();
		} else {
			victimContactOnlineAccountItemIndex = 0;
		}
		mav.addObject(ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY,
				victimContactOnlineAccountItemIndex);
		List<OnlineAccountHost> onlineAccountHosts
			= this.victimAssociationService.findOnlineAccountHosts();
		mav.addObject(ONLINE_ACCOUNT_HOSTS_MODEL_KEY, onlineAccountHosts);
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		this.victimSummaryModelDelegate.add(mav.getModelMap(), victim);
		List<Country> countries = this.victimAssociationService
				.findCountries();
		List<State> states;
		List<City> cities;
		if (victimAssociationForm.getPersonFields() != null) {
			if (victimAssociationForm.getPersonFields()
					.getBirthCountry() != null) {
				Country birthCountry = victimAssociationForm.getPersonFields()
						.getBirthCountry();
				if (this.victimAssociationService
						.hasStates(birthCountry)) {
					states = this.victimAssociationService
							.findStates(birthCountry);
				} else {
					states = Collections.emptyList();
				}
				if (victimAssociationForm.getPersonFields()
						.getBirthState() != null) {
					cities = this.victimAssociationService
							.findCitiesByState(
									victimAssociationForm.getPersonFields()
										.getBirthState());
				} else if (victimAssociationForm.getPersonFields()
						.getBirthCountry() != null) {
					if (this.victimAssociationService.hasStates(
							victimAssociationForm.getPersonFields()
								.getBirthCountry())) {
						cities = this.victimAssociationService
								.findCitiesByCountryWithoutState(
										victimAssociationForm.getPersonFields()
											.getBirthCountry());
					} else {
						cities = this.victimAssociationService
								.findCitiesByCountry(
										victimAssociationForm.getPersonFields()
											.getBirthCountry());
					}
				} else {
					cities = Collections.emptyList();
				}
			} else {
				states = Collections.emptyList();
				cities = Collections.emptyList();
			}
		} else {
			states = Collections.emptyList();
			cities = Collections.emptyList();
		}
		List<String> suffixes = this.victimAssociationService.findSuffixNames();
		this.personFieldsControllerDelegate.prepareEditPersonFields(
				mav.getModelMap(), suffixes, countries, states, cities,
				PERSON_FIELDS_NAME);
		List<State> mailingStates;
		List<City> mailingCities;
		List<ZipCode> mailingZipCodes;
		if (victimAssociationForm.getMailingAddressFields() != null
				&& victimAssociationForm.getMailingAddressFields()
					.getCountry() != null) {
			mailingStates = this.victimAssociationService
					.findStates(victimAssociationForm.getMailingAddressFields()
							.getCountry());
			if (victimAssociationForm.getMailingAddressFields()
					.getState() != null) {
				mailingCities = this.victimAssociationService
						.findCitiesByState(
								victimAssociationForm.getMailingAddressFields()
									.getState());
			} else {
				if (this.victimAssociationService.hasStates(
						victimAssociationForm.getMailingAddressFields()
									.getCountry())) {
					mailingCities = this.victimAssociationService
							.findCitiesByCountryWithoutState(
									victimAssociationForm
									.getMailingAddressFields()
										.getCountry());
				} else {
					mailingCities = this.victimAssociationService
							.findCitiesByCountry(
									victimAssociationForm
									.getMailingAddressFields()
										.getCountry());
				}
			}
			if (victimAssociationForm.getMailingAddressFields()
					.getCity() != null) {
				mailingZipCodes = this.victimAssociationService
						.findZipCodesByCity(
								victimAssociationForm.getMailingAddressFields()
									.getCity());
			} else {
				mailingZipCodes = Collections.emptyList();
			}
		} else {
			mailingStates = Collections.emptyList();
			mailingCities = Collections.emptyList();
			mailingZipCodes = Collections.emptyList();
		}
		
		this.addressFieldsControllerDelegate.prepareEditAddressFields(
				mav.getModelMap(), 
			countries, mailingStates, mailingCities, mailingZipCodes, 
			MAILING_ADDRESS_FIELDS_NAME);
		List<State> poBoxStates;
		List<City> poBoxCities;
		List<ZipCode> poBoxZipCodes;
		if (victimAssociationForm.getPoBoxFields() != null
				&& victimAssociationForm.getPoBoxFields()
					.getCountry() != null) {
			poBoxStates = this.victimAssociationService
					.findStates(victimAssociationForm.getPoBoxFields()
							.getCountry());
			if (victimAssociationForm.getPoBoxFields()
					.getState() != null) {
				poBoxCities = this.victimAssociationService
						.findCitiesByState(
								victimAssociationForm.getPoBoxFields()
									.getState());
			} else {
				if (this.victimAssociationService.hasStates(
						victimAssociationForm.getPoBoxFields()
							.getCountry())) {
					poBoxCities = this.victimAssociationService
							.findCitiesByCountryWithoutState(
									victimAssociationForm.getPoBoxFields()
										.getCountry());
				} else {
					poBoxCities = this.victimAssociationService
							.findCitiesByCountry(
									victimAssociationForm.getPoBoxFields()
										.getCountry());
				}
			}
			if (victimAssociationForm.getPoBoxFields().getCity() != null) {
				poBoxZipCodes = this.victimAssociationService
						.findZipCodesByCity(
								victimAssociationForm.getPoBoxFields()
									.getCity());
			} else {
				poBoxZipCodes = Collections.emptyList();
			}
		} else {
			poBoxStates = Collections.emptyList();
			poBoxCities = Collections.emptyList();
			poBoxZipCodes = Collections.emptyList();
		}
		this.poboxFieldsControllerDelegate.prepareEditPoBoxFields(
				mav.getModelMap(), countries, poBoxStates, poBoxCities,
				poBoxZipCodes, PO_BOX_FIELDS_NAME);
		return mav;
	}
	
	// Prepares model and view to redisplay screen to edit victim associations
	private ModelAndView prepareRedisplayMav(
			final VictimAssociationForm victimAssociationForm,
			final BindingResult result, final Offender offender,
			final Person victim) {
		ModelAndView mav = this.prepareEditMav(
				victimAssociationForm, offender, victim);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ VICTIM_ASSOCIATION_FORM_MODEL_KEY, result);
		return mav;
	}
	
	// Prepares listing redirect based on parameters passed
	private ModelAndView prepareListRedirect(
			final Offender offender, final Person victim,
			final VictimRedirectTarget redirectTarget) {
		if (VictimRedirectTarget.VICTIM.equals(redirectTarget)
				|| (!VictimRedirectTarget.OFFENDER.equals(redirectTarget)
						&& victim != null)) {
			return new ModelAndView(String.format(
					VICTIM_REDIRECT, victim.getId()));
		} else if (offender != null) {
			return new ModelAndView(String.format(
					OFFENDER_REDIRECT, offender.getId()));
		} else {
			throw new AssertionError("I don\'t know where to go!");
		}
	}
	
	/* Exception handlers. */
	
	/**
	 * Handles {@code VictimExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code VictimExistsException} 
	 */
	@ExceptionHandler(VictimExistsException.class)
	public ModelAndView handleVictimExistsException(
			final VictimExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				VICTIM_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				exception);
	}
	
	/**
	 * Handles {@code VictimNoteExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code VictimNoteExistsException} 
	 */
	@ExceptionHandler(VictimNoteExistsException.class)
	public ModelAndView handleVictimNoteExistsException(
			final VictimNoteExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				VICTIM_NOTE_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				exception);
	}
	
	/**
	 * Handles {@code ReflexiveRelationshipException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code ReflexiveRelationshipException}
	 */
	@ExceptionHandler(ReflexiveRelationshipException.class)
	public ModelAndView handleReflexiveRelationshipException(
			final ReflexiveRelationshipException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				REFLEXIVE_RELATIONSHIP_MESSAGE_KEY, ERROR_BUNDLE_NAME,
				exception);
	}
		
	/**
	 * Handles {@code CityExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code CityExistsException}
	 */
	@ExceptionHandler(CityExistsException.class)
	public ModelAndView handleCityExistsException(
			final CityExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				CITY_EXISTS_MESSAGE_KEY, REGION_ERROR_BUNDLE_NAME,
				exception);
	}
	
	/**
	 * Handles {@code ZipCodeExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code ZipCodeExistsException}
	 */
	@ExceptionHandler(ZipCodeExistsException.class)
	public ModelAndView handleZipCodeExistsException(
			final ZipCodeExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ZIP_CODE_EXISTS_MESSAGE_KEY, ADDRESS_ERROR_BUNDLE_NAME,
				exception);
	}
	
	/**
	 * Handles {@code AddressExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code AddressExistsException}
	 */
	@ExceptionHandler(AddressExistsException.class)
	public ModelAndView handleAddressExistsException(
			final AddressExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ADDRESS_EXISTS_MESSAGE_KEY, ADDRESS_ERROR_BUNDLE_NAME,
				exception);
	}
	
	/**
	 * Handles {@code TelephoneNumberExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code TelephoneNumberExistsException}
	 */
	@ExceptionHandler(TelephoneNumberExistsException.class)
	public ModelAndView handleTelephoneNumberExistsException(
			final TelephoneNumberExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				TELEPHONE_NUMBER_EXISTS_MESSAGE_KEY, CONTACT_ERROR_BUNDLE_NAME,
				exception);
	}

	/**
	 * Handles {@code OnlineAccountExistsException}.
	 * 
	 * @param exception exception thrown
	 * @return model and view to handle {@code OnlineAccountExistsException}
	 */
	@ExceptionHandler(OnlineAccountExistsException.class)
	public ModelAndView handleOnlineAccountExistsException(
			final OnlineAccountExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				ONLINE_ACCOUNT_EXISTS_MESSAGE_KEY, CONTACT_ERROR_BUNDLE_NAME,
				exception);
	}
	
	/* Action menus. */
	
	/**
	 * Returns action menu.
	 * 
	 * @param offender offender
	 * @param victim victim
	 * @return action menu
	 */
	@RequestMapping(
			value = "/associationActionMenu.html", method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "victim", required = false)
				final Person victim) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(VICTIM_MODEL_KEY, victim);
		return mav;
	}
	
	/**
	 * Returns notes action menu.
	 * 
	 * @param offender offender
	 * @param victim victim
	 * @return notes action menu
	 */
	@RequestMapping(value = "/associationNotesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showNotesActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "victim", required = false)
				final Person victim) {
		ModelAndView mav = new ModelAndView(NOTES_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		mav.addObject(VICTIM_MODEL_KEY, victim);
		return mav;
	}
	
	/**
	 * Returns telephone numbers action menu.
	 * 
	 * @return telephone numbers action menu
	 */
	@RequestMapping(value = "/telephoneNumbersActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showTelephoneNumbersActionMenu() {
		return new ModelAndView(TELEPHONE_NUMBERS_ACTION_MENU_VIEW_NAME);
	}
	
	/**
	 * Returns online accounts action menu.
	 * 
	 * @return online accounts action menu
	 */
	@RequestMapping(value = "/onlineAccountsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showOnlineAccountsActionMenu() {
		return new ModelAndView(ONLINE_ACCOUNTS_ACTION_MENU_VIEW_NAME);
	}
	
	/* AJAX invokable methods. */
	
	/**
	 * Returns fields to create victim association note.
	 * 
	 * @param itemIndex association note item index
	 * @return fields to create victim association note
	 */
	@RequestMapping(value = "/createNote.html", method = RequestMethod.GET)
	public ModelAndView createNote(
			@RequestParam(value = "itemIndex", required = true)
				final Integer itemIndex) {
		VictimNoteItem victimNoteItem = new VictimNoteItem();
		victimNoteItem.setOperation(VictimNoteItemOperation.CREATE);
		List<VictimNoteCategory> categories = this.victimAssociationService
				.findNoteCategories();
		ModelAndView mav = new ModelAndView(NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(VICTIM_NOTE_CATEGORIES_MODEL_KEY, categories);
		mav.addObject(VICTIM_NOTE_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(VICTIM_NOTE_ITEM_MODEL_KEY, victimNoteItem);
		return mav;
	}

	/**
	 * Returns fields to create victim association telephone number.
	 * 
	 * @param itemIndex telephone number item index
	 * @return fields to create victim association telephone number
	 */
	@RequestMapping(value = "/createTelephoneNumber.html",
			method = RequestMethod.GET)
	public ModelAndView createTelephoneNumber(
			@RequestParam(value = "itemIndex", required = true)
				final Integer itemIndex) {
		VictimTelephoneNumberItem telephoneNumberItem
			= new VictimTelephoneNumberItem();
		telephoneNumberItem.setOperation(
				VictimTelephoneNumberOperation.CREATE);
		ModelAndView mav = new ModelAndView(TELEPHONE_NUMBER_ITEM_VIEW_NAME);
		mav.addObject(TELEPHONE_NUMBER_CATEGORIES_MODEL_KEY,
				TelephoneNumberCategory.values());
		mav.addObject(TELEPHONE_NUMBER_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(TELEPHONE_NUMBER_ITEM_MODEL_KEY, telephoneNumberItem);
		return mav;
	}
	
	/**
	 * Returns fields to create victim association online account.
	 * 
	 * @param itemIndex online account item index
	 * @return fields to create victim association online account
	 */
	@RequestMapping(value = "/createOnlineAccount.html",
			method = RequestMethod.GET)
	public ModelAndView createOnlineAccount(
			@RequestParam(value = "itemIndex", required = true)
				final Integer itemIndex) {
		VictimOnlineAccountItem onlineAccountItem
			= new VictimOnlineAccountItem();
		onlineAccountItem.setOperation(
				VictimOnlineAccountOperation.CREATE);
		List<OnlineAccountHost> onlineAccountHosts
				= this.victimAssociationService
					.findOnlineAccountHosts();
		ModelAndView mav = new ModelAndView(ONLINE_ACCOUNT_ITEM_VIEW_NAME);
		mav.addObject(ONLINE_ACCOUNT_HOSTS_MODEL_KEY, onlineAccountHosts);
		mav.addObject(ONLINE_ACCOUNT_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(ONLINE_ACCOUNT_ITEM_MODEL_KEY, onlineAccountItem);
		return mav;
	}
	
	/**
	 * Returns States.
	 * 
	 * @param fieldsName name of fields
	 * @param country country
	 * @return States
	 */
	@RequestMapping(
			value = "/{fieldsName}/findStates.html", method = RequestMethod.GET)
	public ModelAndView findStates(
			@PathVariable(value = "fieldsName")
				final String fieldsName,
			@RequestParam(value = "country", required = true)
				final Country country) {
		List<State> states = this.victimAssociationService
				.findStates(country);
		if (PERSON_FIELDS_NAME.equals(fieldsName)) {
			return this.personFieldsControllerDelegate
					.showStateOptions(states, PERSON_FIELDS_NAME);
		} else if (MAILING_ADDRESS_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate
					.showStateOptions(states, MAILING_ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poboxFieldsControllerDelegate
					.showStateOptions(states, PO_BOX_FIELDS_NAME);
		} else {
			throw new UnsupportedOperationException(
					String.format("Fields name not supported %s", fieldsName));
		}
	}
	
	/**
	 * Returns cities.
	 * 
	 * @param fieldsName name of fields
	 * @param state state
	 * @param country country
	 * @return cities
	 */
	@RequestMapping(
			value = "/{fieldsName}/findCities.html", method = RequestMethod.GET)
	public ModelAndView findCities(
			@PathVariable(value = "fieldsName")
				final String fieldsName,
			@RequestParam(value = "state", required = false)
				final State state,
			@RequestParam(value = "country", required = false)
				final Country country) {
		List<City> cities;
		if (state != null
				&& this.victimAssociationService.hasStates(
						state.getCountry())) {
			cities = this.victimAssociationService.findCitiesByState(state);
		} else if (country != null) {
			if (this.victimAssociationService.hasStates(country)) {
				cities = this.victimAssociationService
						.findCitiesByCountryWithoutState(country);
			} else {
				cities = this.victimAssociationService.findCitiesByCountry(
						country);
			}
		} else {
			throw new IllegalArgumentException("State or country required");
		}
		if (PERSON_FIELDS_NAME.equals(fieldsName)) {
			return this.personFieldsControllerDelegate
					.showCityOptions(cities, PERSON_FIELDS_NAME);
		} else if (MAILING_ADDRESS_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate
					.showCityOptions(cities, MAILING_ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poboxFieldsControllerDelegate
					.showCityOptions(cities, PO_BOX_FIELDS_NAME);
		} else {
			throw new UnsupportedOperationException(
					String.format("Fields name not supported %s", fieldsName));
		}
	}
	
	/**
	 * Returns ZIP codes.
	 * 
	 * @param fieldsName name of fields
	 * @param city city
	 * @return ZIP codes
	 */
	@RequestMapping(
			value = "/{fieldsName}/findZipCodes.html",
			method = RequestMethod.GET)
	public ModelAndView findZipCodes(
			@PathVariable(value = "fieldsName")
				final String fieldsName,
			@RequestParam(value = "city", required = true)
				final City city) {
		List<ZipCode> zipCodes
			= this.victimAssociationService.findZipCodesByCity(city);
		if (PERSON_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate
					.showZipCodeOptions(zipCodes, MAILING_ADDRESS_FIELDS_NAME);
		} else if (MAILING_ADDRESS_FIELDS_NAME.equals(fieldsName)) {
			return this.addressFieldsControllerDelegate
					.showZipCodeOptions(zipCodes, MAILING_ADDRESS_FIELDS_NAME);
		} else if (PO_BOX_FIELDS_NAME.equals(fieldsName)) {
			return this.poboxFieldsControllerDelegate
					.showZipCodeOptions(zipCodes, PO_BOX_FIELDS_NAME);
		} else {
			throw new UnsupportedOperationException(
					String.format("Fields name not supported %s", fieldsName));
		}
	}
	
	/**
	 * Returns addresses by query.
	 * 
	 * @param query query
	 * @return addresses by query
	 */
	@RequestMapping(
			value = "/findAddressesByQuery.json", method = RequestMethod.GET)
	public ModelAndView findAddressesByQuery(
			@RequestParam(value = "term", required = false)
				final String query) {
		ModelAndView mav = new ModelAndView(ADDRESS_VIEW_NAME);
		List<Address> addresses;
		if (StringUtility.hasContent(query)) {
			addresses
				= this.victimAssociationService.findAddressesByQuery(query);
		} else {
			addresses = Collections.emptyList();
		}
		mav.addObject(ADDRESSES_MODEL_KEY, addresses);
		return mav;
	}
	
	/* Init binders. */

	/**
	 * Registers property editors.
	 * 
	 * @param binder binder
	 */
	@InitBinder
	protected void registerPropertyEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class,
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(Person.class,
				this.personPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(VictimAssociation.class,
				this.victimAssociationPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(VictimNoteCategory.class,
				this.victimNoteCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(VictimNote.class,
				this.victimNotePropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(Contact.class,
				this.contactPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Address.class,
				this.addressPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(Country.class,
				this.countryPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(State.class,
				this.statePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(City.class,
				this.cityPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(StreetSuffix.class,
				this.streetSuffixPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(AddressUnitDesignator.class,
				this.addressUnitDesignatorPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(ZipCode.class,
				this.zipCodePropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccountHost.class,
				this.onlineAccountHostPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(TelephoneNumber.class,
				this.telephoneNumberPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(OnlineAccount.class,
				this.onlineAccountPropertyEditorFactory
					.createPropertyEditor());
	}
}