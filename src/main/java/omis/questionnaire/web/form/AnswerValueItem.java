package omis.questionnaire.web.form;

import omis.questionnaire.domain.AnswerValue;

/**
 * Answer Value Item.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Sep 26, 2016)
 *@since OMIS 3.0
 *@deprecated No Longer needed/used anywhere.
 */
@Deprecated
public class AnswerValueItem {
	
	private AnswerValue answerValue;
	
	/**
	 * Default constructor for AnswerValueItem.
	 */
	public AnswerValueItem() {
	}

	/**
	 * @return the answerValue
	 */
	public AnswerValue getAnswerValue() {
		return this.answerValue;
	}

	/**
	 * @param answerValue the answerValue to set
	 */
	public void setAnswerValue(final AnswerValue answerValue) {
		this.answerValue = answerValue;
	}
	
	
	
}
