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
package omis.offenderrelationship.web.validator;

import java.io.Serializable;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.address.web.validator.delegate.AddressFieldsValidatorDelegate;
import omis.contact.web.form.OnlineAccountContactItemOperation;
import omis.contact.web.form.OnlineAccountFields;
import omis.contact.web.form.TelephoneNumberFields;
import omis.contact.web.form.TelephoneNumberItemOperation;
import omis.contact.web.validator.delegate.OnlineAccountFieldsValidatorDelegate;
import omis.contact.web.validator.delegate.PoBoxFieldsValidatorDelegate;
import omis.contact.web.validator.delegate.TelephoneNumberFieldsValidatorDelegate;
import omis.offenderrelationship.web.controller.OffenderRelationshipAddressOperation;
import omis.offenderrelationship.web.form.EditRelationshipsForm;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.offenderrelationship.web.form.OnlineAccountContactItem;
import omis.offenderrelationship.web.form.TelephoneNumberItem;
import omis.person.web.validator.delegate.PersonFieldsValidatorDelegate;
import omis.util.EqualityChecker;

/**
 * Edit offender relationships form validator.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class EditRelationshipsFormValidator implements Validator {
	/* Validator Delegates. */
	private final PersonFieldsValidatorDelegate personFieldsValidatorDelegate;
	private final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate;
	private final PoBoxFieldsValidatorDelegate poBoxFieldsValidatorDelegate;
	private final TelephoneNumberFieldsValidatorDelegate 
		telephoneNumberFieldsValidatorDelegate;
	private final OnlineAccountFieldsValidatorDelegate 
		onlineAccountFieldsValidatorDelegate;
	private final OffenderRelationshipNoteFieldsValidatorDelegate
		offenderRelationshipNoteFieldsValidatorDelegate;
	
	/* Constructors. */
	/**
	 * Instantiates an instance of edit offender relationships form validator 
	 * with the specified delegates.
	 * 
	 * @param personFieldsValidatorDelegate person fields validator delegate
	 * @param addressFieldsValidatorDelegate address fields validator delegate
	 * @param poBoxFieldsValidatorDelegate poBoxFields validator delegate
	 * @param telephoneNumberFieldsValidatorDelegate telephoneNumberFields 
	 * validator delegate
	 * @param onlineAccountFieldsValidatorDelegate onlineAccountFields 
	 * validator delegate
	 * @param offenderRelationshipNoteFieldsValidatorDelegate delegate for
	 * offender relationship note fields
	 */
	public EditRelationshipsFormValidator(
			final PersonFieldsValidatorDelegate personFieldsValidatorDelegate,
			final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate,
			final PoBoxFieldsValidatorDelegate poBoxFieldsValidatorDelegate,
			final TelephoneNumberFieldsValidatorDelegate 
				telephoneNumberFieldsValidatorDelegate,
			final OnlineAccountFieldsValidatorDelegate 
				onlineAccountFieldsValidatorDelegate,
			final OffenderRelationshipNoteFieldsValidatorDelegate
				offenderRelationshipNoteFieldsValidatorDelegate) {
		this.personFieldsValidatorDelegate = personFieldsValidatorDelegate;
		this.addressFieldsValidatorDelegate = addressFieldsValidatorDelegate;
		this.poBoxFieldsValidatorDelegate = poBoxFieldsValidatorDelegate;
		this.telephoneNumberFieldsValidatorDelegate = 
			telephoneNumberFieldsValidatorDelegate;
		this.onlineAccountFieldsValidatorDelegate 
			= onlineAccountFieldsValidatorDelegate;
		this.offenderRelationshipNoteFieldsValidatorDelegate
			= offenderRelationshipNoteFieldsValidatorDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return EditRelationshipsForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		EditRelationshipsForm form = (EditRelationshipsForm) target;
		this.personFieldsValidatorDelegate.validatePersonFields(
			form.getPersonFields(), "personFields", errors);
		if(form.getEnterAddress() != null && form.getEnterAddress() == true){ 
			if(form.getAddressOperation().equals(
				OffenderRelationshipAddressOperation.NEW)){
				this.addressFieldsValidatorDelegate.validateAddressFields(
					form.getAddressFields(), "addressFields", errors);
			}
			if(form.getAddressOperation().equals(
				OffenderRelationshipAddressOperation.EXISTING)){
				if (form.getAddress() == null) {
					errors.rejectValue("address", "address.empty");
				}
			}
		}
		if(form.getEnterPoBox() != null && form.getEnterPoBox() == true){
			this.poBoxFieldsValidatorDelegate.validatePoBoxFields(
				form.getPoBoxFields(), "poBoxFields", errors);
		}
		
		
		if(form.getTelephoneNumberItems().size()!=0){
			boolean primaryTelephoneExists = false;
			int nonRemoveCount = 0;
			for(int index=0; index < form.getTelephoneNumberItems().size(); 
					index++) {
				TelephoneNumberItem telephoneItem = form
						.getTelephoneNumberItems().get(index);
				if((telephoneItem.getOperation() != null) && (!telephoneItem
						.getOperation().equals(
								TelephoneNumberItemOperation.REMOVE))){
					TelephoneNumberFields fields = telephoneItem
							.getTelephoneNumberFields();
					if(fields != null){
						this.telephoneNumberFieldsValidatorDelegate
							.validateTelephoneNumberFields(fields,
									"telephoneNumberItems[" + index + 
									"].telephoneNumberFields", errors);
						if(fields.getPrimary() != null && 
								fields.getPrimary()) {
							if (fields.getActive() != null &&
								fields.getActive()) {
								if (primaryTelephoneExists) {
									errors.rejectValue("telephoneNumberItems[" + 
											index + 
											"].telephoneNumberFields.primary", 
											"telephoneNumberPrimary.extra");
								} else {
									primaryTelephoneExists = true;
								}
							} else {
								errors.rejectValue("telephoneNumberItems[" + 
										index + 
										"].telephoneNumberFields.active", 
										"telephoneNumberActive.true");
							}
						}
						for (int innerIndex = 0; innerIndex < index; 
								innerIndex++) {
							if (fields.getValue() != null && form
									.getTelephoneNumberItems().get(innerIndex)
									.getTelephoneNumberFields() != null &&
									fields.getValue().equals(form
											.getTelephoneNumberItems().get(
											innerIndex)
											.getTelephoneNumberFields()
											.getValue()) &&
									!TelephoneNumberItemOperation
									.REMOVE.equals(form
											.getTelephoneNumberItems().get(
													innerIndex)
											.getOperation())) {
								errors.rejectValue("telephoneNumberItems[" + 
													index + 
													"].telephoneNumberFields.value",
													"telephoneNumber.duplicate");
								break;
							}
						}
					}
					nonRemoveCount++;
				}
			}
			if(!primaryTelephoneExists && nonRemoveCount > 0){
				errors.rejectValue(
						"telephoneNumberItems", "telephoneNumberPrimary.empty");
			}
		}
		if(form.getOnlineAccountContactItems().size()!=0){
			boolean primaryOnlineAccountExists = false;
			int nonRemoveCount = 0;
			for(int index=0; index < form.getOnlineAccountContactItems().size(); 
					index++) {
				OnlineAccountContactItem accountItem = form
						.getOnlineAccountContactItems().get(index);
				if(accountItem.getOperation() != null &&
						!accountItem.getOperation().equals(
						OnlineAccountContactItemOperation.REMOVE)){
					OnlineAccountFields fields = accountItem
							.getOnlineAccountFields();
					if(fields != null){
						this.onlineAccountFieldsValidatorDelegate
							.validateOnlineAccountFields(fields, 
									"onlineAccountContactItems[" + index + 
									"].onlineAccountFields",
									errors);
						if(fields.getPrimary() != null && 
								fields.getPrimary().equals(true)){
							if (fields.getActive() != null && 
									fields.getActive()) {
								if (primaryOnlineAccountExists) {
									errors.rejectValue(
											"onlineAccountContactItems[" + index + 
											"].onlineAccountFields.primary", 
											"emailPrimary.extra");
								} else {
									primaryOnlineAccountExists = true;
								}
							} else {
								errors.rejectValue("onlineAccountContactItems[" + 
										index + "].onlineAccountFields.active", 
										"onlineAccountActive.true");
							}
						}
						for (int innerIndex = 0; innerIndex < index; 
								innerIndex++) {
							if (fields.getName() != null && form
									.getOnlineAccountContactItems()
									.get(innerIndex).getOnlineAccountFields() != 
									null && fields.getName().equals(form
											.getOnlineAccountContactItems()
											.get(innerIndex)
											.getOnlineAccountFields().getName()) 
									&& !OnlineAccountContactItemOperation
									.REMOVE.equals(form
											.getOnlineAccountContactItems().get(
											innerIndex).getOperation())) {
								errors.rejectValue("onlineAccountContactItems[" +
											index + 
											"].onlineAccountFields.name",
											"onlineAccount.duplicate");
								break;
							}
						}
					}
					nonRemoveCount++;
				}
			}
			if(!primaryOnlineAccountExists && nonRemoveCount > 0){
				errors.rejectValue(
					"onlineAccountContactItems", "onlineAccountPrimary.empty");
			}
		}
		
		// Validates notes
		if (form.getNoteItems() != null) {
			int index = 0;
			for (OffenderRelationshipNoteItem noteItem : form.getNoteItems()) {
				if (OffenderRelationshipNoteItemOperation.CREATE
							.equals(noteItem.getOperation())
						|| OffenderRelationshipNoteItemOperation.UPDATE
							.equals(noteItem.getOperation())) {
					this.offenderRelationshipNoteFieldsValidatorDelegate
						.validate(
								String.format("noteItems[%s].fields", index),
								noteItem.getFields(), errors);
				}
				
				// Checks for duplicates
				for (int innerIndex = 0; innerIndex < index; innerIndex++) {
					OffenderRelationshipNoteItem innerItem
						= form.getNoteItems().get(innerIndex);
					if (OffenderRelationshipNoteItemOperation.CREATE
								.equals(noteItem.getOperation())
							|| OffenderRelationshipNoteItemOperation.UPDATE
								.equals(noteItem.getOperation())) {
						if (noteItem.getFields() != null
								&& innerItem.getFields() != null
								&& EqualityChecker.create(Serializable.class)
								.add(noteItem.getFields().getDate(),
										innerItem.getFields().getDate())
								.add(noteItem.getFields().getCategory(),
										innerItem.getFields().getCategory())
								.add(noteItem.getFields().getValue(),
										innerItem.getFields().getValue())
								.check()) {
							errors.rejectValue(String.format(
									"noteItems[%d].fields", index),
								"offenderRelationshipNote.duplicate");
							break;
						}
					}
				}
				index++;
			}
		}
	}
}