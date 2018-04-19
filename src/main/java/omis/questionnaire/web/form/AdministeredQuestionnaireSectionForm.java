package omis.questionnaire.web.form;

import java.util.ArrayList;
import java.util.List;

/**
 * Questionnaire Section Form.
 * 
 *@author Annie Wahl
 *@version 0.1.0 (Sep 19, 2016)
 *@since OMIS 3.0
 *
 */
public class AdministeredQuestionnaireSectionForm {
	
	private List<QuestionAnswerItem> questionAnswerItems =
			new ArrayList<QuestionAnswerItem>();
	
	private String sectionComments;
	
	/**
	 * Default Constructor for QuestionnaireSectionForm.
	 */
	public AdministeredQuestionnaireSectionForm() {
		
	}

	/**
	 * @return the questionAnswerItems
	 */
	public List<QuestionAnswerItem> getQuestionAnswerItems() {
		return this.questionAnswerItems;
	}

	/**
	 * @param questionAnswerItems the questionAnswerItems to set
	 */
	public void setQuestionAnswerItems(
			final List<QuestionAnswerItem> questionAnswerItems) {
		this.questionAnswerItems = questionAnswerItems;
	}

	/**
	 * @return the sectionComments
	 */
	public String getSectionComments() {
		return this.sectionComments;
	}

	/**
	 * @param sectionComments the sectionComments to set
	 */
	public void setSectionComments(final String sectionComments) {
		this.sectionComments = sectionComments;
	}
	
	
}
