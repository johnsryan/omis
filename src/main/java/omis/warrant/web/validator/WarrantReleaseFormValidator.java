package omis.warrant.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import omis.warrant.web.form.WarrantReleaseForm;

/**
 * WarrantReleaseFormValidator.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 9, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantReleaseFormValidator implements Validator {

	private static final String ADDRESSEE_REQUIRED_MSG_KEY =
			"warrantRelease.addressee.empty";

	private static final String RELEASE_DATE_REQUIRED_MSG_KEY =
			"warrantRelease.releaseDate.empty";

	private static final String CLEARED_BY_PERSON_REQUIRED =
			"warrant.clearedBy.empty";

	private static final String DATE_REQUIRED_MSG_KEY =
			"warrant.date.empty";
	

	/**{@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return WarrantReleaseForm.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressee",
				ADDRESSEE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "releaseDate",
				RELEASE_DATE_REQUIRED_MSG_KEY);
		ValidationUtils.rejectIfEmpty(errors, "clearedBy",
				CLEARED_BY_PERSON_REQUIRED);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "clearedByDate",
				DATE_REQUIRED_MSG_KEY);
		
	}

}
