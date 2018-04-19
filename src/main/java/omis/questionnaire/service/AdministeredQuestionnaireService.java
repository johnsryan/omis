package omis.questionnaire.service;

import java.util.Date;
import java.util.List;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.questionnaire.domain.AdministeredQuestionValue;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.AdministeredQuestionnaireSectionNote;
import omis.questionnaire.domain.AdministeredQuestionnaireSectionStatus;
import omis.questionnaire.domain.AllowedQuestion;
import omis.questionnaire.domain.AnswerValue;
import omis.questionnaire.domain.Question;
import omis.questionnaire.domain.QuestionnaireSection;
import omis.questionnaire.domain.QuestionnaireType;
import omis.user.domain.UserAccount;

/**
 * Administered Questionnaire Service.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Apr 5, 2018)
 *@since OMIS 3.0
 *
 */
public interface AdministeredQuestionnaireService {
	
	/**
	 * Finds and returns a list of all questionnaireTypes by specified
	 * effective date.
	 * @param effectiveDate - Date
	 * @return List of QuestionnaireTypes
	 */
	List<QuestionnaireType> getAllQuestionnaireTypes(Date effectiveDate);
	
	/**
	 * Creates a new AdministeredQuestionnaire.
	 * @param answerer - Person
	 * @param draft - Boolean
	 * @param comments - String
	 * @param assessor - Person
	 * @param date - Date
	 * @param questionnaireType - QuestionnaireType
	 * @return newly created AdministeredQuestionnaire
	 * @throws DuplicateEntityFoundException - when an administered
	 * questionnaire already exists for given answerer
	 */
	AdministeredQuestionnaire createAdministeredQuestionnaire(
			Person answerer, Boolean draft, String comments, Person assessor,
			Date date, QuestionnaireType questionnaireType) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an administered questionnaire.
	 * @param administeredQuestionnaire - administered questionnaire to update
	 * @param answerer - Person answerer
	 * @param draft - Boolean draft
	 * @param comments - String comments
	 * @param assessor - Person assessor
	 * @param date - Date
	 * @param questionnaireType - QuestionnaireType
	 * @return updated AdministeredQuestionnaire
	 * @throws DuplicateEntityFoundException - when a separate administered 
	 * questionnaire already exists for that answerer
	 */
	AdministeredQuestionnaire editAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire,
			Person answerer, Boolean draft, String comments, Person assessor,
			Date date, QuestionnaireType questionnaireType)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes an administered questionnaire.
	 * @param administeredQuestionnaire - administered questionnaire to remove
	 */
	void removeAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Creates a new AdministeredQuestionValue.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param question - Question
	 * @param questionnaireSection - Questionnaire Section
	 * @param answerValue - Answer Value
	 * @param answerValueText - String answer value text
	 * @param comments - String comments
	 * @return Newly created Administered
	 * @throws DuplicateEntityFoundException - when administeredQuestionValue
	 * already exists with all given properties
	 */
	AdministeredQuestionValue createAdministeredQuestionValue(
			AdministeredQuestionnaire administeredQuestionnaire,
			Question question, QuestionnaireSection questionnaireSection,
			AnswerValue answerValue, String answerValueText, String comments)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing administeredQuestionValue.
	 * @param administeredQuestionValue - administeredQuestionValue to update
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param question - Question
	 * @param questionnaireSection - Questionnaire Section
	 * @param answerValue - Answer Value
	 * @param answerValueText - String answer value text
	 * @param comments - String comments
	 * @return Updated AdministeredQuestionValue
	 * @throws DuplicateEntityFoundException - when administeredQuestionValue
	 * already exists with all given properties 
	 */
	AdministeredQuestionValue editAdministeredQuestionValue(
			AdministeredQuestionValue administeredQuestionValue,
			AdministeredQuestionnaire administeredQuestionnaire,
			Question question, QuestionnaireSection questionnaireSection,
			AnswerValue answerValue, String answerValueText, String comments)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes an AdministeredQuestionValue.
	 * @param administeredQuestionValue - AdministeredQuestionValue to remove
	 */
	void removeAdministeredQuestionValue(
			AdministeredQuestionValue administeredQuestionValue);
	
	/**
	 * Creates a new AdministeredQuestionnaireSectionNote.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param questionnaireSection - Questionnaire Section
	 * @param comments - String
	 * @return AdministeredQuestionnaireSectionNote - Administered Questionnaire
	 * Section Note
	 * @throws DuplicateEntityFoundException - when 
	 * AdministeredQuestionnaireSectionNote already exists with given properties
	 */
	AdministeredQuestionnaireSectionNote 
		createAdministeredQuestionnaireSectionNote(
				AdministeredQuestionnaire administeredQuestionnaire,
				QuestionnaireSection questionnaireSection,
				String comments)
						throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing AdministeredQuestionnaireSectionNote.
	 * @param administeredQuestionnaireSectionNote -
	 * AdministeredQuestionnaireSectionNote to update
	 * @param questionnaireSection - Questionnaire Section
	 * @param comments - String
	 * @return AdministeredQuestionnaireSectionNote - Updated Administered
	 * Questionnaire Section Note
	 * @throws DuplicateEntityFoundException - when
	 * AdministeredQuestionnaireSectionNote already exists with given properties
	 * for its administeredQuestionnaire
	 * 
	 */
	AdministeredQuestionnaireSectionNote 
		updateAdministeredQuestionnaireSectionNote(
			AdministeredQuestionnaireSectionNote 
				administeredQuestionnaireSectionNote,
			QuestionnaireSection questionnaireSection, String comments)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes an AdministeredQuestionnaireSectionNote.
	 * @param administeredQuestionnaireSectionNote - Administered Questionnaire
	 * Section Note
	 */
	void removeAdministeredQuestionnaireSectionNote(
			AdministeredQuestionnaireSectionNote 
				administeredQuestionnaireSectionNote);
	
	/**
	 * Returns an AdministeredQuestionValue by specified question
	 * and administered questionnaire.
	 * @param question - Question
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return AdministeredQuestionValue by specified question
	 * and administered questionnaire.
	 */
	AdministeredQuestionValue 
		findAdministeredQuestionValueByQuestionAndQuestionnaire(
			Question question, 
			AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns a list of AdministeredQuestionValues by specified question
	 * and administered questionnaire.
	 * @param question - Question
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List of AdministeredQuestionValues by specified question
	 * and administered questionnaire.
	 */
	List<AdministeredQuestionValue> 
		findAdministeredQuestionValuesByQuestionAndQuestionnaire(
				Question question, AdministeredQuestionnaire
					administeredQuestionnaire);
	
	/**
	 * Returns an AdministeredQuestionnaireSectionNote found by 
	 * AdministeredQuestionnaire and QuestionnaireSection.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param questionnaireSection - Questionnaire Section
	 * @return AdministeredQuestionnaireSectionNote
	 */
	AdministeredQuestionnaireSectionNote 
		findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
				AdministeredQuestionnaire administeredQuestionnaire,
				QuestionnaireSection questionnaireSection);
	
	/**
	 * Creates an AdministeredQuestionnaireSectionStatus.
	 * @param questionnaireSection - Questionnaire Section
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param draft - Boolean
	 * @param date - Date
	 * @return created AdministeredQuestionnaireSectionStatus
	 * @throws DuplicateEntityFoundException When a Administered
	 * Questionnaire Section Status exists with the specified properties.
	 */
	AdministeredQuestionnaireSectionStatus 
		createAdministeredQuestionnaireSectionStatus(
				QuestionnaireSection questionnaireSection, 
				AdministeredQuestionnaire administeredQuestionnaire, 
				Boolean draft, Date date)throws DuplicateEntityFoundException;
	
	/**
	 * Updates an AdministeredQuestionnaireSectionStatus.
	 * @param administeredQuestionnaireSectionStatus -
	 * 	AdministeredQuestionnaireSectionStatus to update
	 * @param draft - Boolean
	 * @param date - Date
	 * @return updated AdministeredQuestionnaireSectionStatus
	 * @throws DuplicateEntityFoundException When another Administered
	 * Questionnaire Section Status exists with the specified properties.
	 */
	AdministeredQuestionnaireSectionStatus 
		updateAdministeredQuestionnaireSectionStatus(
			AdministeredQuestionnaireSectionStatus 
				administeredQuestionnaireSectionStatus, 
			Boolean draft, Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Removes a AdministeredQuestionnaireSectionStatus.
	 * @param administeredQuestionnaireSectionStatus - 
	 * 	AdministeredQuestionnaireSectionStatus to remove
	 */
	void removeAdministeredQuestionnaireSectionStatus(
			AdministeredQuestionnaireSectionStatus 
			administeredQuestionnaireSectionStatus);
	
	/**
	 * Returns a list of all allowed questions by specified question section.
	 * @param questionnaireSection - Questionnaire Section
	 * @return List<AllowedQuestion> by QuestionSection
	 */
	List<AllowedQuestion> findAllowedQuestionsByQuestionSection(
			QuestionnaireSection questionnaireSection);
	
	/**
	 * Returns Administered Question Value with specified properties
	 * and that does not contain an Answer Value.
	 * @param question - Question
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param questionnaireSection - Questionnaire Section
	 * @return AdministeredQuestionValue - Administered Question Value with
	 * specified properties and that does not contain an Answer Value. 
	 */
	AdministeredQuestionValue findAdministeredQuestionValueByNoAnswerValue(
			Question question,
			AdministeredQuestionnaire administeredQuestionnaire,
			QuestionnaireSection questionnaireSection);
	
	/**
	 * Returns a list of AdministeredQuestionnaireSectionStatuses found by
	 * specified AdministeredQuestionnaire.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return list of AdministeredQuestionnaireSectionStatuses found by
	 * specified AdministeredQuestionnaire
	 */
	List<AdministeredQuestionnaireSectionStatus> 
		findAdministeredQuestionnaireSectionStatusByAdministeredQuestionnaire(
				AdministeredQuestionnaire administeredQuestionnaire);
	
	/**
	 * Returns a list of user accounts by given query.
	 * 
	 * @param query - query
	 * @return list of user accounts by given query
	 */
	List<UserAccount> findUserAccounts(String query);
	
	
	/**
	 * Returns a list of QuestionnaireSections by specified QuestionnaireType.
	 * @param questionnaireType - Questionnaire Type
	 * @return List of QuestionnaireSections by specified QuestionnaireType
	 */
	List<QuestionnaireSection> 
		findQuestionnaireSectionsByQuestionnaireType(
				QuestionnaireType questionnaireType);
	
}
