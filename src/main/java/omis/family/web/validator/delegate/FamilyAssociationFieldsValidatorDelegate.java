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
package omis.family.web.validator.delegate;

import java.io.Serializable;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.address.web.validator.delegate.AddressFieldsValidatorDelegate;
import omis.contact.web.form.OnlineAccountFields;
import omis.contact.web.form.TelephoneNumberFields;
import omis.contact.web.validator.delegate.OnlineAccountFieldsValidatorDelegate;
import omis.contact.web.validator.delegate.PoBoxFieldsValidatorDelegate;
import omis.contact.web.validator.delegate.TelephoneNumberFieldsValidatorDelegate;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.web.controller.FamilyAddressOperation;
import omis.family.web.form.FamilyAssociationForm;
import omis.family.web.form.FamilyAssociationOnlineAccountItem;
import omis.family.web.form.FamilyAssociationOnlineAccountItemOperation;
import omis.family.web.form.FamilyAssociationTelephoneNumberItem;
import omis.family.web.form.FamilyAssociationTelephoneNumberItemOperation;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.offenderrelationship.web.validator.OffenderRelationshipNoteFieldsValidatorDelegate;
import omis.person.web.validator.delegate.PersonFieldsValidatorDelegate;
import omis.util.EqualityChecker;

/**
 * Validator for family association fields.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.1.2 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class FamilyAssociationFieldsValidatorDelegate implements Validator {
	private AddressFieldsValidatorDelegate addressFieldsValidatorDelegate;
	private PoBoxFieldsValidatorDelegate poBoxFieldsValidatorDelegate;
	private PersonFieldsValidatorDelegate personFieldsValidatorDelegate;
	private TelephoneNumberFieldsValidatorDelegate 
		telephoneNumberFieldsValidatorDelegate;
	private OnlineAccountFieldsValidatorDelegate 
		onlineAccountFieldsValidatorDelegate;
	private OffenderRelationshipNoteFieldsValidatorDelegate
		offenderRelationshipNoteFieldsValidatorDelegate;
	
	/** Instantiates a validator for family form. 
	 * 
	 * @param addressFieldsValidatorDelegate address fields validator delegate
	 * @param poBoxFieldsValidatorDelegate p o box fields validator delegate
	 * @param personFieldsValidatorDelegate person fields validator delegate
	 * @param telephoneNumberFieldsValidatorDelegate telephone number fields
	 * validator delegate
	 * @param onlineAccountFieldsValidatorDelegate online account fields
	 * validator delegate
	 * @param offenderRelationshipNoteFieldsValidatorDelegate offender
	 * relationship note fields validator delegate
	 * 
	 */
	public FamilyAssociationFieldsValidatorDelegate(
			final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate,
			final PoBoxFieldsValidatorDelegate poBoxFieldsValidatorDelegate,
			final PersonFieldsValidatorDelegate personFieldsValidatorDelegate,
			final TelephoneNumberFieldsValidatorDelegate 
					telephoneNumberFieldsValidatorDelegate,
			final OnlineAccountFieldsValidatorDelegate 
					onlineAccountFieldsValidatorDelegate,
			final OffenderRelationshipNoteFieldsValidatorDelegate
			offenderRelationshipNoteFieldsValidatorDelegate) {
		// Default instantiation
		this.addressFieldsValidatorDelegate = addressFieldsValidatorDelegate;
		this.poBoxFieldsValidatorDelegate = poBoxFieldsValidatorDelegate;
		this.personFieldsValidatorDelegate = personFieldsValidatorDelegate;
		this.telephoneNumberFieldsValidatorDelegate = 
				telephoneNumberFieldsValidatorDelegate;
		this.onlineAccountFieldsValidatorDelegate = 
				onlineAccountFieldsValidatorDelegate;
		this.offenderRelationshipNoteFieldsValidatorDelegate
			= offenderRelationshipNoteFieldsValidatorDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return FamilyAssociationForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		FamilyAssociationForm form = (FamilyAssociationForm) target;
		if (form.getPersonFields() != null) {
			this.personFieldsValidatorDelegate.validatePersonFields(
				form.getPersonFields(), "personFields", errors);
		}
		
		if (form.getCategory() == null) {
			errors.rejectValue("category", 
				"familyAssociationFields.category.empty");
		} else {
			if (FamilyAssociationCategoryClassification.CHILD.equals(
					form.getCategory().getClassification()) 
					&& (form.getPersonFields() != null
						&& form.getPersonFields().getSex() == null)) {
				errors.rejectValue("personFields.sex", 
						"familyAssociationFields.sex.empty");
			}
			else if(FamilyAssociationCategoryClassification.SPOUSE.equals(
				form.getCategory().getClassification())){
				if(form.getMarriageDate()!=null
					&&form.getDivorceDate()!=null
					&&form.getMarriageDate().getTime()
					>form.getDivorceDate()
					.getTime()){
					errors.rejectValue("marriageDate",
						"mDateGreaterThanDDate");
					
				}
			} else {
				if(form.getStartDate()!=null
					&&form.getEndDate()!=null
					&&form.getStartDate().getTime()
					>form.getEndDate().getTime()){
					errors.rejectValue("startDate",
						"dateRange.startDateGreaterThanEndDate");
				}
			}
		}
		
		if (form.getFamilyAssociationNoteItems() != null) {
			for (int index = 0; index < form.getFamilyAssociationNoteItems()
					.size(); index++) {
				OffenderRelationshipNoteItem item = form
						.getFamilyAssociationNoteItems().get(index);
				
				if (item.getOperation() != null
						&& !OffenderRelationshipNoteItemOperation.REMOVE
							.equals(item.getOperation())) {
					this.offenderRelationshipNoteFieldsValidatorDelegate
							.validate(
								String.format(
									"familyAssociationNoteItems[%d].fields",
									index),
								item.getFields(), errors);
					
					for (int innerNoteIndex = 0; innerNoteIndex < index;
							innerNoteIndex++) {
						OffenderRelationshipNoteItem innerItem
							= form.getFamilyAssociationNoteItems()
							.get(innerNoteIndex);
						
						if (innerItem.getOperation() != null
								&& !OffenderRelationshipNoteItemOperation.REMOVE
									.equals(innerItem.getOperation())) {
							if (EqualityChecker.create(Serializable.class)
									.add(item.getFields().getDate(),
											innerItem.getFields().getDate())
									.add(item.getFields().getCategory(),
											innerItem.getFields().getCategory())
									.add(item.getFields().getValue(),
											innerItem.getFields().getValue())
									.check()) {
								errors.rejectValue(
										String.format(
											"familyAssociationNoteItems[%d]"
											+ ".fields", index),
										"offenderRelationshipNote.duplicate");
								break;
							}
						}
					}
				}
			}
		}
		
		if (form.getEnterAddress() != null && form.getEnterAddress()) {
			if (form.getAddressOperation() != null) {
				if (form.getAddressOperation().equals(
						FamilyAddressOperation.NEW)) {		
					if (form.getHomeType() != null 
							|| (form.getSecondAddressDesignator() != null 
					    && !form.getSecondAddressDesignator().isEmpty()) 
							|| form.getAddressFields() != null) {
						this.addressFieldsValidatorDelegate
						 	.validateAddressFields(form.getAddressFields(), 
								"addressFields", errors);
					} 
				} else if (form.getAddressOperation().equals(
						FamilyAddressOperation.EXISTING)) {
					if (form.getAddress() == null) {
						errors.rejectValue("addressQuery", 
							"familyAssociationFields.addressQuery.empty");
					}
				}
			}
		}
		
		if (form.getEnterPoBox() != null && form.getEnterPoBox()) {
			if (form.getPoBoxFields() != null) {
				if (form.getPoBoxFields().getCountry() != null 
						|| form.getPoBoxFields().getState() != null 
						|| (form.getPoBoxFields().getNewZipCode() 
								&& (form.getPoBoxFields()
										.getZipCodeValue() != null 
								&& !form.getPoBoxFields().getZipCodeValue()
								.isEmpty())) 
						|| (!form.getPoBoxFields().getNewZipCode() 
								&& form.getPoBoxFields().getZipCode() != null) 
						|| (form.getPoBoxFields().getNewCity() 
								&& (form.getPoBoxFields().getCityName() != null 
								&& !form.getPoBoxFields().getCityName()
										.isEmpty())) 
						|| (!form.getPoBoxFields().getNewCity() 
								&& form.getPoBoxFields().getCity() != null) 
						|| (form.getPoBoxFields().getPoBoxValue() != null 
						&& !form.getPoBoxFields().getPoBoxValue().isEmpty())) {
					this.poBoxFieldsValidatorDelegate.validatePoBoxFields(
						form.getPoBoxFields(), "poBoxFields", 
						errors);
				}
			}
		}
		
		boolean primaryTelephoneExists = false;
		boolean primaryOnlineAccountExists = false;
		if (form.getFamilyAssociationTelephoneNumberItems().size() != 0) {
			for (int index = 0; index < form
					.getFamilyAssociationTelephoneNumberItems().size(); 
					index++) {
				FamilyAssociationTelephoneNumberItem telephoneItem = form
						.getFamilyAssociationTelephoneNumberItems().get(index);
				if ((telephoneItem.getOperation() != null) && (!telephoneItem
						.getOperation().equals(
								FamilyAssociationTelephoneNumberItemOperation
								.REMOVE))) {
					TelephoneNumberFields fields = telephoneItem
							.getTelephoneNumberFields();
					if (fields != null) {
						this.telephoneNumberFieldsValidatorDelegate
							.validateTelephoneNumberFields(fields,
									"familyAssociationTelephoneNumberItems[" 
											+ index + "].telephoneNumberFields",
											errors);
						if (fields.getPrimary() != null 
								&& fields.getPrimary()) {
							if (fields.getActive() != null 
									&& fields.getActive()) {
								if (primaryTelephoneExists) {
									errors.rejectValue(
										"familyAssociationTelephoneNumberItems["
											+ index 
											+ "].telephoneNumberFields.primary",
											"familyAssociationFields"
											+ ".telephoneNumberPrimary.extra");
								} else {
									primaryTelephoneExists = true;
								}
							} else {
								errors.rejectValue(
										"familyAssociationTelephoneNumberItems["
											+ index 
											+ "].telephoneNumberFields.active", 
										"familyAssociationFields"
										+ ".telephoneNumberActive.true");
							}
						}
						for (int innerIndex = 0; innerIndex < index; 
								innerIndex++) {
							if (fields.getValue() != null 
								&& fields.getValue().equals(form
								.getFamilyAssociationTelephoneNumberItems()
								.get(innerIndex)
								.getTelephoneNumberFields()
								.getValue()) 
							    && !FamilyAssociationTelephoneNumberItemOperation
							    .REMOVE.equals(form
									.getFamilyAssociationTelephoneNumberItems()
									.get(innerIndex).getOperation())) {
								errors.rejectValue(
										"familyAssociationTelephoneNumberItems["
												+ index 
												+ "].telephoneNumberFields"
												+ ".value", 
												"familyAssociationFields"
												+ ".telephoneNumber.duplicate");
								break;
							}
						}
					}
				}
			}
			if (!primaryTelephoneExists) {
				errors.rejectValue("familyAssociationTelephoneNumberItems", 
						"familyAssociationFields.telephoneNumberPrimary.empty");
			}
		}
		if (form.getFamilyAssociationOnlineAccountItems().size() != 0) {
			for (int index = 0; index < form
					.getFamilyAssociationOnlineAccountItems().size(); index++) {
				FamilyAssociationOnlineAccountItem accountItem = form
						.getFamilyAssociationOnlineAccountItems().get(index);
				if (!accountItem.getOperation().equals(
						FamilyAssociationOnlineAccountItemOperation.REMOVE)) {
					OnlineAccountFields fields = accountItem
							.getOnlineAccountFields();
					if (fields != null) {
						this.onlineAccountFieldsValidatorDelegate
							 .validateOnlineAccountFields(fields, 
									"familyAssociationOnlineAccountItems["
							 + index + "].onlineAccountFields",
											errors);
						if (fields.getPrimary() != null 
								&& fields.getPrimary().equals(true)) {
							if (fields.getActive() != null 
									&&  fields.getActive()) {
								if (primaryOnlineAccountExists) {
									errors.rejectValue(
										"familyAssociationOnlineAccountItems[" 
											+ index + "].onlineAccountFields"
											+ ".primary", 
											"familyAssociationFields"
											+ ".emailPrimary.extra");
								} else {
									primaryOnlineAccountExists = true;
								}
							} else {
								errors.rejectValue(
										"familyAssociationOnlineAccountItems[" 
												+ index 
												+ "].onlineAccountFields"
												+ ".active", 
												"familyAssociationFields"
												+ ".onlineAccountActive.true");
							}
						}
						for (int innerIndex = 0; innerIndex < index; 
								innerIndex++) {
							if (fields.getName() != null && fields.getName()
									.equals(form
									.getFamilyAssociationOnlineAccountItems()
									.get(innerIndex)
									.getOnlineAccountFields().getName())
								&& !FamilyAssociationOnlineAccountItemOperation
									.REMOVE.equals(form
									.getFamilyAssociationOnlineAccountItems()
									.get(innerIndex).getOperation())) {
								errors.rejectValue(
										"familyAssociationOnlineAccountItems[" 
									+ index 
									+ "].onlineAccountFields.name", 
									"familyAssociationFields.onlineAccount"
									+ ".duplicate");
								break;
							}
						}
					}
				}
			}
			if (!primaryOnlineAccountExists) {
				errors.rejectValue("familyAssociationOnlineAccountItems", 
						"familyAssociationFields.onlineAccountPrimary.empty");
			}
		}
	}
}  