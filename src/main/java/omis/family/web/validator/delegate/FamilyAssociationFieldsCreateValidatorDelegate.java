package omis.family.web.validator.delegate;

import org.springframework.validation.Errors;

import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.web.form.FamilyAssociationFields;

/**
 * Delegate to handle validation of creating family association fields.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Mar 3, 2016)
 * @since OMIS 3.0
 */
public class FamilyAssociationFieldsCreateValidatorDelegate {

	/* Constructor. */
	
	/**
	 * Instantiates a default instance of create family association 
	 * fields validator.
	 */
	public FamilyAssociationFieldsCreateValidatorDelegate() {
		// Default constructor.
	}
	
	/**
	 * Validate errors of the family association fields.
	 *
	 *
	 * @param familyAssociationFields family association fields
	 * @param familyAssociationFieldsVariableName family association fields 
	 * variable name
	 * @param errors errors
	 * @return errors
	 */
	public Errors validateFamilyAssociationFields(
			final FamilyAssociationFields familyAssociationFields, 
			final String familyAssociationFieldsVariableName, 
			final Errors errors) {	
		
		if (familyAssociationFields.getCategory() == null) {
			errors.rejectValue(familyAssociationFieldsVariableName 
					+ ".category", "familyAssociationFields.category.empty");
		}
		if(familyAssociationFields.getCategory()!=null
			&&FamilyAssociationCategoryClassification.SPOUSE.equals(
			familyAssociationFields.getCategory().getClassification())){
				if(familyAssociationFields.getMarriageDate()!=null
					&&familyAssociationFields.getDivorceDate()!=null
					&&familyAssociationFields.getMarriageDate().getTime()
					>familyAssociationFields.getDivorceDate()
					.getTime()){
					errors.rejectValue(familyAssociationFieldsVariableName
						+".marriageDate",
						"mDateGreaterThanDDate");
			}
		}
		if(familyAssociationFields.getCategory()!=null
			&&!FamilyAssociationCategoryClassification.SPOUSE.equals(
			familyAssociationFields.getCategory().getClassification())){
			if(familyAssociationFields.getStartDate()!=null
				&&familyAssociationFields.getEndDate()!=null
				&&familyAssociationFields.getStartDate().getTime()
				>familyAssociationFields.getEndDate().getTime()){
				errors.rejectValue(familyAssociationFieldsVariableName
						+".startDate",
					"dateRange.startDateGreaterThanEndDate");
			}
		}
		return errors;		
	}
}