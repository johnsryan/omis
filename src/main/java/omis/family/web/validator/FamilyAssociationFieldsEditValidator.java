package omis.family.web.validator;

import java.io.Serializable;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.web.form.FamilyAssociationForm;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItemOperation;
import omis.offenderrelationship.web.validator.OffenderRelationshipNoteFieldsValidatorDelegate;
import omis.util.EqualityChecker;

/**
 * Validator for family association edit screen.
 * 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.0 (Dec 10, 2015)
 * @since OMIS 3.0
 */
public class FamilyAssociationFieldsEditValidator implements Validator {
	/** Instantiates a validator for family form. */
	private OffenderRelationshipNoteFieldsValidatorDelegate 
		offenderRelationshipNoteFieldsValidatorDelegate;
	
	/**
	 * Instantiates a validator for family association fields edit screen.
	 * @param offenderRelationshipNoteFieldsValidatorDelegate offender
	 * relationship note fields validator delegate
	 */
	public FamilyAssociationFieldsEditValidator(
		final OffenderRelationshipNoteFieldsValidatorDelegate 
		offenderRelationshipNoteFieldsValidatorDelegate) {
		this.offenderRelationshipNoteFieldsValidatorDelegate
			= offenderRelationshipNoteFieldsValidatorDelegate;
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return FamilyAssociationForm.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		FamilyAssociationForm familyAssociationFields 
			= (FamilyAssociationForm) target;
		
		int index = 0;
		if (familyAssociationFields.getFamilyAssociationNoteItems() != null) {
			for (OffenderRelationshipNoteItem item : familyAssociationFields
				.getFamilyAssociationNoteItems()) {
				if (item.getOperation() != null
						&& !OffenderRelationshipNoteItemOperation.REMOVE
						.equals(item.getOperation())) {
					this.offenderRelationshipNoteFieldsValidatorDelegate
						.validate(
						String.format(
								"familyAssociationNoteItems[%d].fields", index),
						item.getFields(), errors);
			
					for (int innerNoteIndex = 0; innerNoteIndex < index;
							innerNoteIndex++) {
						OffenderRelationshipNoteItem innerItem
							= familyAssociationFields
							.getFamilyAssociationNoteItems()
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
										"familyAssociationNoteItems[%d].fields",
										index),
										"offenderRelationshipNote.duplicate");
								break;
							}
						}
					}
				}
				index++;
			}
		}
		
		if (familyAssociationFields.getCategory() == null) {
			errors.rejectValue("category", 
				"familyAssociationFields.category.empty");
		}
		if(familyAssociationFields.getCategory()!=null){
			if(FamilyAssociationCategoryClassification.SPOUSE.equals(
				familyAssociationFields.getCategory().getClassification())){
				if(familyAssociationFields.getMarriageDate()!=null
					&&familyAssociationFields.getDivorceDate()!=null
					&&familyAssociationFields.getMarriageDate().getTime()
					>familyAssociationFields.getDivorceDate()
					.getTime()){
					errors.rejectValue("marriageDate",
						"mDateGreaterThanDDate");
					
				}
			} else {
				if(familyAssociationFields.getStartDate()!=null
					&&familyAssociationFields.getEndDate()!=null
					&&familyAssociationFields.getStartDate().getTime()
					>familyAssociationFields.getEndDate().getTime()){
					errors.rejectValue("startDate",
						"dateRange.startDateGreaterThanEndDate");
					
				}
			}
		}
	}
}  