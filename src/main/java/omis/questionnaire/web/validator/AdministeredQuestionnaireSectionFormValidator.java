package omis.questionnaire.web.validator;

import java.util.EnumSet;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import omis.questionnaire.domain.AnswerCardinality;
import omis.questionnaire.domain.QuestionCategory;
import omis.questionnaire.domain.QuestionConditionality;
import omis.questionnaire.web.form.AdministeredQuestionValueItem;
import omis.questionnaire.web.form.AdministeredQuestionnaireSectionForm;
import omis.questionnaire.web.form.QuestionAnswerItem;

/**
 * Questionnaire Form Validator.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Apr 5, 2018)
 *@since OMIS 3.0
 *
 */
public class AdministeredQuestionnaireSectionFormValidator
		implements Validator {

	private static final String ANSWER_REQUIRED_MSG_KEY = "answer.empty";
	
	private static final String ESSAY_REQUIRED_MSG_KEY = "essay.empty";

	/**{@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return AdministeredQuestionnaireSectionForm
				.class.isAssignableFrom(clazz);
	}

	/**{@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		
		AdministeredQuestionnaireSectionForm form =
				(AdministeredQuestionnaireSectionForm) target;
		if (form.getQuestionAnswerItems() != null) {
			int index = 0;
			for (QuestionAnswerItem item : form.getQuestionAnswerItems()) {
				QuestionCategory questionCategory = item.getAllowedQuestion()
						.getQuestion().getQuestionCategory();
				if (item.getAllowedQuestion().getQuestionConditionality()
						.equals(QuestionConditionality.REQUIRED)) {
					if (EnumSet.of(QuestionCategory.TRUE_FALSE,
							QuestionCategory.MULTIPLE_CHOICE,
							QuestionCategory.MULTIPLE_CHOICE_ESSAY,
							QuestionCategory.SELECT_FROM_LIST)
							.contains(questionCategory)) {
						
						if (item.getAnswerCardinality()
								.equals(AnswerCardinality.SINGLE)) {
							ValidationUtils.rejectIfEmpty(errors,
									"questionAnswerItems[" + index + "]"
										+ ".answerValue",
										ANSWER_REQUIRED_MSG_KEY);
						} else if (item.getAnswerCardinality()
								.equals(AnswerCardinality.MULTIPLE)) {
							Boolean answered = false;
							checkAnswer : for (AdministeredQuestionValueItem
									aqvItem : item
									.getAdministeredQuestionValueItems()) {
								if (aqvItem.getAnswerValue() != null) {
									answered = true;
									break checkAnswer;
								}
							}
							if (!(answered)) {
								errors.rejectValue(
										"questionAnswerItems[" + index + "]"
												+ ".answerValueItems", 
												ANSWER_REQUIRED_MSG_KEY);
							}
						}
					}
					if (EnumSet.of(QuestionCategory.MULTIPLE_CHOICE_ESSAY,
							QuestionCategory.ESSAY, QuestionCategory.CURRENCY,
							QuestionCategory.DATE,
							QuestionCategory.SHORT_ANSWER,
							QuestionCategory.PHONE_NUMBER,
							QuestionCategory.DECIMAL_NUMBER,
							QuestionCategory.WHOLE_NUMBER)
							.contains(questionCategory)) {
						ValidationUtils.rejectIfEmptyOrWhitespace(
							errors, "questionAnswerItems[" + index + "]"
							+ ".answerValueText", 
							ESSAY_REQUIRED_MSG_KEY);
					}
				}
				index++;
			}
		}
	}
}
