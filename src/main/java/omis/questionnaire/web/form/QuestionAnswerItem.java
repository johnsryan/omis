package omis.questionnaire.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import omis.questionnaire.domain.AdministeredQuestionValue;
import omis.questionnaire.domain.AllowedQuestion;
import omis.questionnaire.domain.AnswerCardinality;
import omis.questionnaire.domain.AnswerValue;

/**
 * Question Answer Items.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Mar 28, 2018)
 *@since OMIS 3.0
 *
 */
public class QuestionAnswerItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private AllowedQuestion allowedQuestion;
	
	private AdministeredQuestionValue administeredQuestionValue;
	
	private AnswerValue answerValue;
	
	private List<AdministeredQuestionValueItem> administeredQuestionValueItems =
			new ArrayList<AdministeredQuestionValueItem>();
	
	private String answerValueText;
	
	private String comments;
	
	private AnswerCardinality answerCardinality;
	
	private Boolean addComments;
	
	/**
	 * Default constructor for QuestionAnswerItem.
	 */
	public QuestionAnswerItem() {
		
	}
	
	/**
	 * Returns the allowedQuestion.
	 * @return allowedQuestion - AllowedQuestion
	 */
	public AllowedQuestion getAllowedQuestion() {
		return this.allowedQuestion;
	}
	
	/**
	 * Sets the allowedQuestion.
	 * @param allowedQuestion - AllowedQuestion
	 */
	public void setAllowedQuestion(final AllowedQuestion allowedQuestion) {
		this.allowedQuestion = allowedQuestion;
	}



	/**
	 * Returns the administeredQuestionValue.
	 * @return administeredQuestionValue - AdministeredQuestionValue
	 */
	public AdministeredQuestionValue getAdministeredQuestionValue() {
		return this.administeredQuestionValue;
	}

	/**
	 * Sets the administeredQuestionValue.
	 * @param administeredQuestionValue - AdministeredQuestionValue
	 */
	public void setAdministeredQuestionValue(
			final AdministeredQuestionValue administeredQuestionValue) {
		this.administeredQuestionValue = administeredQuestionValue;
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
	
	/**
	 * Returns the administeredQuestionValueItems.
	 * @return administeredQuestionValueItems - List of Administered
	 * Question Value Items
	 */
	public List<AdministeredQuestionValueItem>
				getAdministeredQuestionValueItems() {
		return this.administeredQuestionValueItems;
	}

	/**
	 * Sets the administeredQuestionValueItems.
	 * @param administeredQuestionValueItems - List of Administered
	 * Question Value Items
	 */
	public void setAdministeredQuestionValueItems(
			final List<AdministeredQuestionValueItem>
					administeredQuestionValueItems) {
		this.administeredQuestionValueItems = administeredQuestionValueItems;
	}

	/**
	 * @return the answerValueText
	 */
	public String getAnswerValueText() {
		return this.answerValueText;
	}

	/**
	 * @param answerValueText the answerValueText to set
	 */
	public void setAnswerValueText(final String answerValueText) {
		this.answerValueText = answerValueText;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return this.comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(final String comments) {
		this.comments = comments;
	}

	/**
	 * @return the answerCardinality
	 */
	public AnswerCardinality getAnswerCardinality() {
		return this.answerCardinality;
	}

	/**
	 * @param answerCardinality the answerCardinality to set
	 */
	public void setAnswerCardinality(
			final AnswerCardinality answerCardinality) {
		this.answerCardinality = answerCardinality;
	}

	/**
	 * @return the addComments
	 */
	public Boolean getAddComments() {
		return this.addComments;
	}

	/**
	 * @param addComments the addComments to set
	 */
	public void setAddComments(final Boolean addComments) {
		this.addComments = addComments;
	}
	
	
}
