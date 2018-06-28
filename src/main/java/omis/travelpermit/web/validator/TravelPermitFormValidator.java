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
package omis.travelpermit.web.validator;
import java.io.Serializable;

import omis.address.web.validator.delegate.AddressFieldsValidatorDelegate;
import omis.travelpermit.web.controller.AddressOption;
import omis.travelpermit.web.controller.DestinationOption;
import omis.travelpermit.web.form.TravelPermitForm;
import omis.travelpermit.web.form.TravelPermitNoteItem;
import omis.travelpermit.web.form.TravelPermitNoteItemOperation;
import omis.util.EqualityChecker;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for travel permit form.
 * 
 * @author Yidong Li
 * @version 0.1.1 (May 22, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitFormValidator implements Validator {
	private static final String TELEPHONE_NUMBER_PROPERTY_NAME 
	= "phoneNumber";
	private static final String TELEPHONE_NUMBER_WRONG_ERROR_KEY
	= "TravelPermit.telephonenumber.wrong";
	private static final String PERIODICITY_PROPERTY_NAME 
		= "periodicity";
	private static final String PERIODICITY_EMPTY_ERROR_KEY
		= "TravelPermit.periodicity.empty";
	private static final String START_DATE_PROPERTY_NAME 
		= "startDate";
	private static final String ADDRESS_QUERY_PROPERTY_NAME
		="addressQuery";
	private static final String START_DATE_EMPTY_ERROR_KEY
		= "TravelPermit.startdate.empty";
	private static final String TRIP_PURPOSE_PROPERTY_NAME
		= "tripPurpose";
	private static final String TRIP_PURPOSE_EMPTY_ERROR_KEY
		= "TravelPermit.trippurpose.empty";
	private static final String DESTINATION_NAME_PROPERTY_NAME
		= "name";
	private static final String DESTINATION_NAME_EMPTY_ERROR_KEY
		= "TravelPermit.name.empty";
	private static final String ISSUER_PROPERTY_NAME
		= "issuer";
	private static final String ISSUER_EMPTY_ERROR_KEY
		= "TravelPermit.issuer.empty";
	private static final String NOTE_DATE_EMPTY_ERROR_KEY
		= "TravelPermit.note.empty";
	private static final String NOTE_VALUE_EMPTY_ERROR_KEY
		="TravelPermit.value.empty";
	private static final String PARTIAL_ADDRESS_STATE_PROPERTY_NAME
		="partialAddressState";
	private static final String PARTIAL_ADDRESS_STATE_EMPTY_ERROR_KEY
		="TravelPermit.partialAddressState.empty";
	private static final String PARTIAL_ADDRESS_NEW_CITY_PROPERTY_NAME
		="newCityName";
	private static final String PARTIAL_ADDRESS_NEW_CITY_EMPTY_ERROR_KEY
		="TravelPermit.newcity.empty";
	private static final String PARTIAL_ADDRESS_CITY_PROPERTY_NAME
		="partialAddressCity";
	private static final String PARTIAL_ADDRESS_CITY_EMPTY_ERROR_KEY
		="TravelPermit.city.empty";
	private static final String PARTIAL_ADDRESS_NEW_ZIP_CODE_PROPERTY_NAME
		="newZipCodeName";
	private static final String PARTIAL_ADDRESS_NEW_ZIP_CODE_EMPTY_ERROR_KEY
		="TravelPermit.newzipcode.empty";
	private static final String PARTIAL_ADDRESS_NEW_ZIP_CODE_EXTENSION_PROPERTY_NAME
		="newZipCodeExtension";
	private static final String PARTIAL_ADDRESS_NEW_ZIP_CODE_EXTENSION_EMPTY_ERROR_KEY
		="TravelPermit.newzipcodeextension.empty";
	private static final String PARTIAL_ADDRESS_ZIP_CODE_PROPERTY_NAME
		="partialAddressZipCode";
	private static final String PARTIAL_ADDRESS_ZIP_CODE_EMPTY_ERROR_KEY
		="TravelPermit.zipcode.empty";
	private static final String ADDRESS_OPTION_PROPERTY_NAME
		="addressOption";
	private static final String DESTINATION_OPTION_PROPERTY_NAME
		="destinationOption";
	private static final String DESTINATION_OPTION_EMPTY_ERROR_KEY
		="TravelPermit.destinationoption.empty";
	private static final String ADDRESS_EMPTY_ERROR_KEY
		="TravelPermit.address.empty";
	private static final String TRAVEL_METHOD_PROPERTY_NAME
		="travelMethod";
	private static final String TRAVEL_METHOD_EMPTY_ERROR_KEY
		="TravelPermit.travelmethod.empty";
	private final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate;
	
	/** Instantiates a validator for travel permit form. */
	public TravelPermitFormValidator(
		final AddressFieldsValidatorDelegate addressFieldsValidatorDelegate) {
		this.addressFieldsValidatorDelegate = addressFieldsValidatorDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return TravelPermitForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		TravelPermitForm travelPermitForm
			= (TravelPermitForm) target;
		if (travelPermitForm.getPeriodicity() == null) {
			errors.rejectValue(PERIODICITY_PROPERTY_NAME,
			PERIODICITY_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getStartDate() == null) {
			errors.rejectValue(START_DATE_PROPERTY_NAME,
			START_DATE_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getTripPurpose().isEmpty()
			|| travelPermitForm.getTripPurpose().length()==0
			|| travelPermitForm.getTripPurpose() == null) {
			errors.rejectValue(TRIP_PURPOSE_PROPERTY_NAME,
			TRIP_PURPOSE_EMPTY_ERROR_KEY);
		}
		if (travelPermitForm.getName().isEmpty()
			|| travelPermitForm.getName().length()==0
			|| travelPermitForm.getName() == null) {
			errors.rejectValue(DESTINATION_NAME_PROPERTY_NAME,
			DESTINATION_NAME_EMPTY_ERROR_KEY);
		}
		
		if (travelPermitForm.getIssuer() == null) {
			errors.rejectValue(ISSUER_PROPERTY_NAME,
			ISSUER_EMPTY_ERROR_KEY);
		}
		
		if(travelPermitForm.getTravelMethod()==null){
			errors.rejectValue(TRAVEL_METHOD_PROPERTY_NAME,
				TRAVEL_METHOD_EMPTY_ERROR_KEY);
		}
		
		if(travelPermitForm.getDestinationOption()!=null){
			if (DestinationOption.USE_FULL_ADDRESS.equals(
					travelPermitForm.getDestinationOption())) {
				if(travelPermitForm.getAddressOption()!=null){
					if(AddressOption.USE_EXISTING.equals(
						travelPermitForm.getAddressOption())){
						if(travelPermitForm.getAddressQuery()==null
							||travelPermitForm.getAddressQuery().isEmpty()){
							errors.rejectValue(ADDRESS_QUERY_PROPERTY_NAME,
								ADDRESS_EMPTY_ERROR_KEY);
						}
					} else if(AddressOption.CREATE_NEW.equals(
						travelPermitForm.getAddressOption())){
							this.addressFieldsValidatorDelegate
							.validateAddressFields(
							travelPermitForm.getAddressFields(),
							"addressFields",
							errors);
					}
				}  else {
					errors.rejectValue(ADDRESS_OPTION_PROPERTY_NAME,
						ADDRESS_EMPTY_ERROR_KEY);
				}
			}
			if (DestinationOption.USE_PARTIAL_ADDRESS.equals(
				travelPermitForm.getDestinationOption())) {
				if(travelPermitForm.getPartialAddressState()==null){
					errors.rejectValue(PARTIAL_ADDRESS_STATE_PROPERTY_NAME,
						PARTIAL_ADDRESS_STATE_EMPTY_ERROR_KEY);
				}
				if(travelPermitForm.getNewCity()==true
					&&(travelPermitForm.getNewCityName()==null
					||travelPermitForm.getNewCityName().isEmpty())){
					errors.rejectValue(PARTIAL_ADDRESS_NEW_CITY_PROPERTY_NAME,
						PARTIAL_ADDRESS_NEW_CITY_EMPTY_ERROR_KEY);
				}
				if(travelPermitForm.getNewCity()!=true
					&&travelPermitForm.getPartialAddressCity()==null){
						errors.rejectValue(PARTIAL_ADDRESS_CITY_PROPERTY_NAME,
							PARTIAL_ADDRESS_CITY_EMPTY_ERROR_KEY);
				}
				if(travelPermitForm.getNewZipCode()==true
					&&(travelPermitForm.getNewZipCodeName()==null
					||travelPermitForm.getNewZipCodeName().isEmpty())){
					errors.rejectValue(
						PARTIAL_ADDRESS_NEW_ZIP_CODE_PROPERTY_NAME,
						PARTIAL_ADDRESS_NEW_ZIP_CODE_EMPTY_ERROR_KEY);
					errors.rejectValue(
						PARTIAL_ADDRESS_NEW_ZIP_CODE_EXTENSION_PROPERTY_NAME,
						PARTIAL_ADDRESS_NEW_ZIP_CODE_EXTENSION_EMPTY_ERROR_KEY);
				} 
				if(travelPermitForm.getNewZipCode()!=true
					&&travelPermitForm.getPartialAddressZipCode()==null){
					errors.rejectValue(PARTIAL_ADDRESS_ZIP_CODE_PROPERTY_NAME,
						PARTIAL_ADDRESS_ZIP_CODE_EMPTY_ERROR_KEY);
				}
			} 
		}
		else {
			errors.rejectValue(DESTINATION_OPTION_PROPERTY_NAME,
				DESTINATION_OPTION_EMPTY_ERROR_KEY);
		}
		
		if (travelPermitForm.getTravelPermitNoteItems() != null) {
			for (int index = 0;
				index < travelPermitForm.getTravelPermitNoteItems().size();
				index++) {
				TravelPermitNoteItem item
					= travelPermitForm.getTravelPermitNoteItems().get(index);
				
				if (item.getOperation() != null
				&& !TravelPermitNoteItemOperation.REMOVE
					.equals(item.getOperation())) {
					if(item.getDate()==null){
						errors.rejectValue(String.format(
							"travelPermitNoteItems[%d].date", index),
							NOTE_DATE_EMPTY_ERROR_KEY);
					}
					if(item.getNote()==null||item.getNote().isEmpty()){
						errors.rejectValue(String.format(
							"travelPermitNoteItems[%d].note", index),
							NOTE_VALUE_EMPTY_ERROR_KEY);
					}
				}
				
				for (int innerNoteIndex = 0; innerNoteIndex < index;
						innerNoteIndex++) {
					TravelPermitNoteItem innerItem
						= travelPermitForm.getTravelPermitNoteItems()
						.get(innerNoteIndex);
					
					if (innerItem.getOperation() != null
							&& !TravelPermitNoteItemOperation.REMOVE
								.equals(innerItem.getOperation())) {
						if (EqualityChecker.create(Serializable.class)
								.add(item.getDate(),
								innerItem.getDate())
								.add(item.getUpdateSignature(),
								innerItem.getUpdateSignature())
								.add(item.getNote(),
								innerItem.getNote())
								.check()) {
							errors.rejectValue(
									String.format(
										"travelPermitNoteItems[%d]"
										+ ".note", index),
									"travelPermitNote.duplicate");
							break;
						}
					}
				}
			}
		}
		if(travelPermitForm.getPhoneNumber()!=null
			&&!travelPermitForm.getPhoneNumber().isEmpty()){
			String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}|\\d{3}-\\d{7}";
			if(!travelPermitForm.getPhoneNumber().matches(pattern)){
				errors.rejectValue(TELEPHONE_NUMBER_PROPERTY_NAME,
					TELEPHONE_NUMBER_WRONG_ERROR_KEY);
			}
		}
	}
}
