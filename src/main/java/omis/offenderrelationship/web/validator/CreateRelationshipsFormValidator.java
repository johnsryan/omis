package omis.offenderrelationship.web.validator;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.address.web.validator.delegate.AddressFieldsValidatorDelegate;
import omis.contact.web.form.OnlineAccountContactItemOperation;
import omis.contact.web.form.TelephoneNumberItemOperation;
import omis.contact.web.validator.delegate.OnlineAccountFieldsValidatorDelegate;
import omis.contact.web.validator.delegate.PoBoxFieldsValidatorDelegate;
import omis.contact.web.validator.delegate.TelephoneNumberFieldsValidatorDelegate;
import omis.family.web.validator.delegate.FamilyAssociationFieldsCreateValidatorDelegate;
import omis.offenderrelationship.web.controller.OffenderRelationshipAddressOperation;
import omis.offenderrelationship.web.form.CreateRelationshipsForm;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.person.web.validator.delegate.PersonFieldsValidatorDelegate;
import omis.util.EqualityChecker;
import omis.visitation.web.validator.delegate.VisitationAssociationFieldsValidatorDelegate;

/**
 * Create relationships form validator.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (May 16, 2016)
 * @since OMIS 3.0
 */
public class CreateRelationshipsFormValidator implements Validator {

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
	
	private final FamilyAssociationFieldsCreateValidatorDelegate 
		familyAssociationFieldsCreateValidatorDelegate;
	
	private final VisitationAssociationFieldsValidatorDelegate
		visitationAssociationFieldsValidatorDelegate;
	
	/* Constructors. */
	
	/**
	 * Instantiates an instance of create relationships form validator with the
	 * specified delegates.
	 * 
	 * @param personFieldsValidatorDelegate person fields validator delegate
	 * @param addressFieldsValidatorDelegate address fields validator delegate
	 * @param poBoxFieldsValidatorDelegate PO box fields validator delegate
	 * @param telephoneNumberFieldsValidatorDelegate telephone number fields
	 * validator delegate
	 * @param onlineAccountFieldsValidatorDelegate online account fields
	 * validator delegate
	 * @param offenderRelationshipNoteFieldsValidatorDelegate offender
	 * relationship note fields validator delegate
	 * @param familyAssociationFieldsCreateValidatorDelegate family association
	 * fields validator delegate
	 * @param visitationAssociationFieldsValidatorDelegate visitation
	 * association fields validator delegate
	 */
	public CreateRelationshipsFormValidator(
			final PersonFieldsValidatorDelegate personFieldsValidatorDelegate,
			final AddressFieldsValidatorDelegate 
				addressFieldsValidatorDelegate,
			final PoBoxFieldsValidatorDelegate poBoxFieldsValidatorDelegate,
			final TelephoneNumberFieldsValidatorDelegate 
				telephoneNumberFieldsValidatorDelegate,
			final OnlineAccountFieldsValidatorDelegate 
				onlineAccountFieldsValidatorDelegate,
			final OffenderRelationshipNoteFieldsValidatorDelegate
				offenderRelationshipNoteFieldsValidatorDelegate,
			final FamilyAssociationFieldsCreateValidatorDelegate
				familyAssociationFieldsCreateValidatorDelegate, 
			final VisitationAssociationFieldsValidatorDelegate
				visitationAssociationFieldsValidatorDelegate) {
		this.personFieldsValidatorDelegate = Objects.requireNonNull(
				personFieldsValidatorDelegate,
				"Delegate to validate person fields required");
		this.addressFieldsValidatorDelegate = Objects.requireNonNull(
				addressFieldsValidatorDelegate,
				"Delegate to validate address fields required");
		this.poBoxFieldsValidatorDelegate = Objects.requireNonNull(
				poBoxFieldsValidatorDelegate,
				"Delegate to validate PO box fields required");
		this.telephoneNumberFieldsValidatorDelegate 
				= Objects.requireNonNull(telephoneNumberFieldsValidatorDelegate,
					"Delegate to validate telephone number fields required");
		this.onlineAccountFieldsValidatorDelegate 
				= Objects.requireNonNull(onlineAccountFieldsValidatorDelegate,
					"Delegate to validate online account fields required");
		this.offenderRelationshipNoteFieldsValidatorDelegate
				= Objects.requireNonNull(
						offenderRelationshipNoteFieldsValidatorDelegate,
						"Delegate to validate offender relationship note"
								+ " fields required");
		this.familyAssociationFieldsCreateValidatorDelegate 
				= Objects.requireNonNull(
						familyAssociationFieldsCreateValidatorDelegate,
						"Delegate to validate family association"
								+ " fields required");
		this.visitationAssociationFieldsValidatorDelegate 
				= Objects.requireNonNull(
						visitationAssociationFieldsValidatorDelegate,
						"Delegate to validate visitation association"
						+ " fields required"); 
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return CreateRelationshipsForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		CreateRelationshipsForm form = (CreateRelationshipsForm) target;
		// At least one relationship is required
		if((form.getCreateFamilyMember() == null || !form.getCreateFamilyMember())
			&& (form.getCreateVictim() == null || !form.getCreateVictim())
			&& (form.getCreateVisitor() == null || !form.getCreateVisitor())) {
			errors.rejectValue("createFamilyMember", "familyVictimVisitorSelection.empty");
			errors.rejectValue("createVictim", "familyVictimVisitorSelection.empty");
			errors.rejectValue("createVisitor", "familyVictimVisitorSelection.empty");
		}
		
		// Person fields
		this.personFieldsValidatorDelegate.validatePersonFields(
			form.getPersonFields(), "personFields",	errors);
		// Address fields
		if(form.getEnterAddress()!=null&&form.getEnterAddress()==true){  // check box checked
			if(form.getAddressOperation().equals(OffenderRelationshipAddressOperation.NEW)){
				if (form.getAddressFields() != null) {
					this.addressFieldsValidatorDelegate.validateAddressFields(
						form.getAddressFields(), "addressFields", errors);
				}
			}
			if(form.getAddressOperation().equals(OffenderRelationshipAddressOperation.EXISTING)){
				if (form.getAddress() == null) {
					errors.rejectValue("address", "address.empty");
				}
			}
		}
		
		// Po box fields
		if(form.getEnterPoBox()!=null&&form.getEnterPoBox()==true){  // check box checked
			this.poBoxFieldsValidatorDelegate.validatePoBoxFields(
				form.getPoBoxFields(), "poBoxFields", errors);
		}

		// Visitation association fields
		if(form.getCreateVisitor()!=null&&form.getCreateVisitor()!=false){
			if (form.getVisitationAssociationFields()!=null) {
				this.visitationAssociationFieldsValidatorDelegate
					.validateVisiationAssociationFields(
						form.getVisitationAssociationFields(), 
						"visitationAssociationFields", errors);
			}
		}
		// Family association fields
		if(form.getCreateFamilyMember()!=null&&form.getCreateFamilyMember()!=false){
			if (form.getFamilyAssociationFields()!=null) {
				this.familyAssociationFieldsCreateValidatorDelegate
					.validateFamilyAssociationFields(
						form.getFamilyAssociationFields(), 
						"familyAssociationFields", errors);
			}
		}

		int telephoneNumberPrimaryCount = 0;
		int onlineAccountPrimaryCount = 0;
		int telephoneNumberActiveCount = 0;
		int onlineAccountActiveCount = 0;
		
		if(form.getTelephoneNumberItems().size()!=0){
			for(int itemIndex=0; itemIndex < form.getTelephoneNumberItems().size(); itemIndex++) {
				if(form.getTelephoneNumberItems().get(itemIndex).getOperation()
					.equals(TelephoneNumberItemOperation.CREATE)){
					this.telephoneNumberFieldsValidatorDelegate
					.validateTelephoneNumberFields(form.getTelephoneNumberItems()
						.get(itemIndex).getTelephoneNumberFields(), 
					"telephoneNumberItems[" + itemIndex + "].telephoneNumberFields",errors);
					if(form.getTelephoneNumberItems().get(itemIndex)
						.getTelephoneNumberFields()!=null){
						if(form.getTelephoneNumberItems().get(itemIndex)
							.getTelephoneNumberFields().getPrimary()!=null
							&&form.getTelephoneNumberItems().get(itemIndex)
								.getTelephoneNumberFields().getPrimary()
									.equals(true)){
							telephoneNumberPrimaryCount++;
							if(form.getTelephoneNumberItems().get(itemIndex)
							.getTelephoneNumberFields().getActive()==null){
								errors.rejectValue(
									"telephoneNumberItems[" + itemIndex + "].telephoneNumberFields.active", 
									"telephoneNumberActive.true");
							}
						}
						if(form.getTelephoneNumberItems().get(itemIndex)
							.getTelephoneNumberFields().getActive()!=null
							&&form.getTelephoneNumberItems().get(itemIndex)
								.getTelephoneNumberFields().getActive()
									.equals(true)){
							telephoneNumberActiveCount++;
						}
					}
				}
			}
			
			if(telephoneNumberPrimaryCount>1){
				for(int itemIndex=0; itemIndex < form.getTelephoneNumberItems().size(); itemIndex++) {
					if(form.getTelephoneNumberItems().get(itemIndex).getTelephoneNumberFields().getPrimary()!=null
						&&form.getTelephoneNumberItems().get(itemIndex).getTelephoneNumberFields().getPrimary()){
							errors.rejectValue(
								"telephoneNumberItems[" + itemIndex + "].telephoneNumberFields.primary", 
								"telephoneNumberPrimary.extra");
					}
				}
			}
			if(telephoneNumberPrimaryCount==0){
				for(int itemIndex=0; itemIndex < form.getTelephoneNumberItems().size(); itemIndex++) {
					errors.rejectValue(
						"telephoneNumberItems[" + itemIndex + "].telephoneNumberFields.primary", 
						"telephoneNumberPrimary.empty");
				}
			}
			if(telephoneNumberActiveCount==0){
				for(int itemIndex=0; itemIndex < form.getTelephoneNumberItems().size(); itemIndex++) {
					errors.rejectValue(
						"telephoneNumberItems[" + itemIndex + "].telephoneNumberFields.active", 
						"telephoneNumberActive.empty");
				}
			}
		}
		
		if(form.getOnlineAccountContactItems().size()!=0){
			for(int itemIndex=0; itemIndex < form.getOnlineAccountContactItems().size(); itemIndex++) {
				if(form.getOnlineAccountContactItems().get(itemIndex).getOperation()
					.equals(OnlineAccountContactItemOperation.CREATE)){
					this.onlineAccountFieldsValidatorDelegate.validateOnlineAccountFields(
						form.getOnlineAccountContactItems().get(itemIndex).getOnlineAccountFields(), 
					"onlineAccountContactItems[" + itemIndex + "].onlineAccountFields",errors);
					if(form.getOnlineAccountContactItems()
						.get(itemIndex).getOnlineAccountFields()!=null){
						if(form.getOnlineAccountContactItems()
								.get(itemIndex).getOnlineAccountFields().getPrimary()!=null
							&&form.getOnlineAccountContactItems().get(itemIndex)
								.getOnlineAccountFields().getPrimary().equals(true)){
							onlineAccountPrimaryCount++;
							if(form.getOnlineAccountContactItems().get(itemIndex)
								.getOnlineAccountFields().getActive()==null){
									errors.rejectValue(
										"onlineAccountContactItems[" + itemIndex + "].onlineAccountFields.active", 
										"onlineAccountActive.true");
							}
						}
						if(form.getOnlineAccountContactItems().get(itemIndex)
							.getOnlineAccountFields().getActive()!=null
							&&form.getOnlineAccountContactItems().get(itemIndex)
								.getOnlineAccountFields().getActive()
									.equals(true)){
							onlineAccountActiveCount++;
						}
					}
				}
			}
			
			if(onlineAccountPrimaryCount>1){
				for(int itemIndex=0; itemIndex < form.getOnlineAccountContactItems().size(); itemIndex++) {
					if(form.getOnlineAccountContactItems().get(itemIndex).getOnlineAccountFields().getPrimary()!=null
						&&form.getOnlineAccountContactItems().get(itemIndex).getOnlineAccountFields().getPrimary()){
							errors.rejectValue(
								"onlineAccountContactItems[" + itemIndex + "].onlineAccountFields.primary", 
								"emailPrimary.extra");
					}
				}
			}
			if(onlineAccountPrimaryCount==0){
				for(int itemIndex=0; itemIndex < form.getOnlineAccountContactItems().size(); itemIndex++) {
					errors.rejectValue(
						"onlineAccountContactItems[" + itemIndex + "].onlineAccountFields.primary", 
						"onlineAccountPrimary.empty");
				}
			}
			if(onlineAccountActiveCount==0){
				for(int itemIndex=0; itemIndex < form.getOnlineAccountContactItems().size(); itemIndex++) {
					errors.rejectValue(
						"onlineAccountContactItems[" + itemIndex + "].onlineAccountFields.active", 
						"onlineAccountActive.true");
				}
			}
		}
		
		// Validates note items
		if (form.getNoteItems() != null) {
			for (int noteItemIndex = 0;
					noteItemIndex < form.getNoteItems().size();
					noteItemIndex++) {
				OffenderRelationshipNoteItem item
					= form.getNoteItems().get(noteItemIndex);
				
				// Only validate items that are to be operated on (that is, not
				// removed or ignored/null)
				if (item.getOperation() != null
						&& !OffenderRelationshipNoteItemOperation.REMOVE
							.equals(item.getOperation())) {
					this.offenderRelationshipNoteFieldsValidatorDelegate
							.validate(
								String.format(
										"noteItems[%d].fields", noteItemIndex),
								item.getFields(), errors);
					
					// Checks for duplicates
					for (int innerNoteIndex = 0; innerNoteIndex < noteItemIndex;
							innerNoteIndex++) {
						OffenderRelationshipNoteItem innerItem
							= form.getNoteItems().get(innerNoteIndex);
						
						// Only check for duplicates on items that are to be
						// operated on (not removed or ignored/null)
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
												"noteItems[%d].fields",
												noteItemIndex),
										"offenderRelationshipNote.duplicate");
								break;
							}
						}
					}
				}
			}
		}
	}
}