package omis.questionnaire.service.delegate;

import java.util.List;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.dao.AdministeredQuestionValueDao;
import omis.questionnaire.domain.AdministeredQuestionValue;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import omis.questionnaire.domain.AnswerValue;
import omis.questionnaire.domain.Question;
import omis.questionnaire.domain.QuestionnaireSection;

/**
 * Administered Question Value Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Apr 5, 2018)
 *@since OMIS 3.0
 *
 */
public class AdministeredQuestionValueDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Administered Question Already Exists With Given Properties";
	
	private final AdministeredQuestionValueDao administeredQuestionValueDao;
	
	private final InstanceFactory<AdministeredQuestionValue> 
		administeredQuestionValueInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for AdministeredQuestionValueDelegate.
	 * @param administeredQuestionValueDao - Administered Question Value DAO
	 * @param administeredQuestionValueInstanceFactory - Administered
	 * Question Value Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public AdministeredQuestionValueDelegate(
			final AdministeredQuestionValueDao administeredQuestionValueDao,
			final InstanceFactory<AdministeredQuestionValue> 
				administeredQuestionValueInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.administeredQuestionValueDao = administeredQuestionValueDao;
		this.administeredQuestionValueInstanceFactory =
				administeredQuestionValueInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	
	/**
	 * Creates a new AdministeredQuestionValue.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param question - Question
	 * @param questionnaireSection - Questionnaire Section
	 * @param answerValue - Answer Value
	 * @param answerValueText - String - answer value text
	 * @param comments - String comments
	 * @return Newly created Administered
	 * @throws DuplicateEntityFoundException - when administeredQuestionValue
	 * already exists with all given properties
	 */
	public AdministeredQuestionValue create(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final Question question, 
			final QuestionnaireSection questionnaireSection,
			final AnswerValue answerValue, final String answerValueText,
			final String comments) throws DuplicateEntityFoundException {
		if (this.administeredQuestionValueDao.find(question, answerValue,
				answerValueText, administeredQuestionnaire,
				questionnaireSection) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		AdministeredQuestionValue administeredQuestionValue =
				this.administeredQuestionValueInstanceFactory.createInstance();
		
		administeredQuestionValue.setAdministeredQuestionnaire(
				administeredQuestionnaire);
		administeredQuestionValue.setAnswerValue(answerValue);
		administeredQuestionValue.setAnswerValueText(answerValueText);
		administeredQuestionValue.setComments(comments);
		administeredQuestionValue.setQuestion(question);
		administeredQuestionValue.setQuestionnaireSection(questionnaireSection);
		administeredQuestionValue.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		administeredQuestionValue.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.administeredQuestionValueDao
				.makePersistent(administeredQuestionValue);
	}
	
	
	/**
	 * Updates an existing administeredQuestionValue.
	 * @param administeredQuestionValue - administeredQuestionValue to update
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param question - Question
	 * @param questionnaireSection - Questionnaire Section
	 * @param answerValue - Answer Value
	 * @param answerValueText - String - answer value text
	 * @param comments - String comments
	 * @return Updated AdministeredQuestionValue
	 * @throws DuplicateEntityFoundException - when administeredQuestionValue
	 * already exists with all given properties 
	 */
	public AdministeredQuestionValue update(
			final AdministeredQuestionValue administeredQuestionValue,
			final AdministeredQuestionnaire administeredQuestionnaire,
			final Question question, 
			final QuestionnaireSection questionnaireSection,
			final AnswerValue answerValue, final String answerValueText,
			final String comments) throws DuplicateEntityFoundException {
		if (this.administeredQuestionValueDao.findExcluding(
				question, answerValue, answerValueText,
				administeredQuestionnaire, questionnaireSection, 
				administeredQuestionValue) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		administeredQuestionValue.setAdministeredQuestionnaire(
				administeredQuestionnaire);
		administeredQuestionValue.setAnswerValue(answerValue);
		administeredQuestionValue.setAnswerValueText(answerValueText);
		administeredQuestionValue.setComments(comments);
		administeredQuestionValue.setQuestion(question);
		administeredQuestionValue.setQuestionnaireSection(questionnaireSection);
		administeredQuestionValue.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.administeredQuestionValueDao
				.makePersistent(administeredQuestionValue);
	}
	
	/**
	 * Removes an AdministeredQuestionValue.
	 * @param administeredQuestionValue - AdministeredQuestionValue to remove
	 */
	public void remove(
			final AdministeredQuestionValue administeredQuestionValue) {
		this.administeredQuestionValueDao
			.makeTransient(administeredQuestionValue);
	}
	
	/**
	 * Returns an AdministeredQuestionValue by specified question and
	 * administered questionnaire.
	 * @param question - Question
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return AdministeredQuestionValue found by specified question and
	 * administered questionnaire.
	 */
	public AdministeredQuestionValue findByQuestionAndAdministeredQuestionnaire(
			final Question question, 
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.administeredQuestionValueDao
			.findByQuestionAndAdministeredQuestionnaire(question, 
				administeredQuestionnaire);
	}
	
	/**
	 * Returns Administered Question Value with specified properties
	 * and that does not contain an Answer Value.
	 * @param question - Question
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param questionnaireSection - Questionnaire Section
	 * @return AdministeredQuestionValue - Administered Question Value with
	 * specified properties and that does not contain an Answer Value. 
	 */
	public AdministeredQuestionValue findByNoAnswerValue(
			final Question question,
			final AdministeredQuestionnaire administeredQuestionnaire,
			final QuestionnaireSection questionnaireSection) {
		return this.administeredQuestionValueDao.findByNoAnswerValue(question,
				administeredQuestionnaire, questionnaireSection);
	}
	
	/**
	 * Returns a list of AdministeredQuestionValues by specified question and
	 * administered questionnaire.
	 * @param question - Question
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List of AdministeredQuestionValues by specified question and
	 * administered questionnaire.
	 */
	public List<AdministeredQuestionValue> 
		findAllByQuestionAndAdministeredQuestionnaire(final Question question, 
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.administeredQuestionValueDao
			.findAllByQuestionAndAdministeredQuestionnaire(question, 
				administeredQuestionnaire);
	}
}
