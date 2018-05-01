package omis.warrant.web.validator;

import java.util.EnumSet;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.util.StringUtility;
import omis.warrant.web.form.WarrantCauseViolationItem;
import omis.warrant.web.form.WarrantForm;
import omis.warrant.web.form.WarrantItemOperation;
import omis.warrant.web.form.WarrantNoteItem;

/**
 * WarrantFormValidator.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 9, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantFormValidator implements Validator {

	private static final String DATE_REQUIRED_MSG_KEY = "warrant.date.empty";
	
	private static final String ADDRESSEE_REQUIRED_MSG_KEY =
			"warrant.addressee.empty";
	
	private static final String ISSUED_BY_REQUIRED_MSG_KEY =
			"warrant.issuedBy.empty";
	
	private static final String BOND_RECOMMENDATION_REQUIRED_MSG_KEY =
			"warrant.bondRecommendation.empty";
	
	private static final String BOND_RECOMMENDATION_NUMERIC_MSG_KEY =
			"warrant.bondRecommendation.numeric";
	
	private static final String JAIL_REQUIRED_MSG_KEY = "warrant.jail.empty";
	
	private static final String CONDITION_REQUIRED_MESSAGE_KEY =
			"warrant.conditionClause.empty";
	
	private static final String COURT_CASE_REQUIRED_MESSAGE_KEY =
			"warrant.courtCase.empty";
	
	private static final String NOTE_REQUIRED_MSG_KEY =
			"warrant.note.empty";

	private static final String BOND_OPTION_REQUIRED =
			"warrant.bondRecommended.empty";

	/**{@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return WarrantForm.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		WarrantForm form = (WarrantForm) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date",
				DATE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressee",
				ADDRESSEE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "issuedBy",
				ISSUED_BY_REQUIRED_MSG_KEY);
		if(form.getBondRecommended() == null){
			errors.rejectValue("bondRecommended", BOND_OPTION_REQUIRED);
		}
		else if(form.getBondRecommended()){
			ValidationUtils.rejectIfEmptyOrWhitespace(
					errors, "bondRecommendation",
					BOND_RECOMMENDATION_REQUIRED_MSG_KEY);
			
			if(!StringUtility.isNumeric(form.getBondRecommendation())){
				errors.rejectValue("bondRecommendation",
						BOND_RECOMMENDATION_NUMERIC_MSG_KEY);
			}
		}
		
		if(form.getArrested()){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "arrestDate",
					DATE_REQUIRED_MSG_KEY);
			ValidationUtils.rejectIfEmpty(errors, "arrestJail",
					JAIL_REQUIRED_MSG_KEY);
		}
		if(form.getWarrantCauseViolationItems() != null){
			int i=0;
			for(WarrantCauseViolationItem item :
					form.getWarrantCauseViolationItems()){
				if(EnumSet.of(WarrantItemOperation.CREATE,
						WarrantItemOperation.UPDATE).contains(
								item.getItemOperation())){
					ValidationUtils.rejectIfEmpty(errors,
							"warrantCauseViolationItems["+i+"].conditionClause",
							CONDITION_REQUIRED_MESSAGE_KEY);
				}
				i++;
			}
		}
		if(form.getWarrantNoteItems() != null){
			int i = 0;
			for(WarrantNoteItem item : form.getWarrantNoteItems()){
				if(EnumSet.of(WarrantItemOperation.CREATE,
						WarrantItemOperation.UPDATE).contains(
								item.getItemOperation())){
					ValidationUtils.rejectIfEmpty(errors,
							"warrantNoteItems["+i+"].date",
							DATE_REQUIRED_MSG_KEY);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors,
						"warrantNoteItems["+i+"].note",
						NOTE_REQUIRED_MSG_KEY);
				}
				i++;
			}
		}
	}
}
