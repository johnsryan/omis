package omis.questionnaire.service.impl;

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
import omis.questionnaire.service.AdministeredQuestionnaireService;
import omis.questionnaire.service.delegate.AdministeredQuestionValueDelegate;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireDelegate;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireSectionNoteDelegate;
import omis.questionnaire.service.delegate.AdministeredQuestionnaireSectionStatusDelegate;
import omis.questionnaire.service.delegate.AllowedQuestionDelegate;
import omis.questionnaire.service.delegate.QuestionnaireSectionDelegate;
import omis.questionnaire.service.delegate.QuestionnaireTypeDelegate;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;

/**
 * Administered Questionnaire Service Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Apr 5, 2018)
 *@since OMIS 3.0
 *
 */
public class AdministeredQuestionnaireServiceImpl
	implements AdministeredQuestionnaireService {
	/* Delegates */
	
	private final QuestionnaireTypeDelegate questionnaireTypeDelegate;
	
	private final AdministeredQuestionnaireDelegate 
		administeredQuestionnaireDelegate;
	
	private final AdministeredQuestionValueDelegate 
		administeredQuestionValueDelegate;
	
	private final AdministeredQuestionnaireSectionNoteDelegate
		administeredQuestionnaireSectionNoteDelegate;
	
	private final AdministeredQuestionnaireSectionStatusDelegate
		administeredQuestionnaireSectionStatusDelegate;
	
	private final AllowedQuestionDelegate allowedQuestionDelegate;
	
	private final QuestionnaireSectionDelegate questionnaireSectionDelegate;
	
	private final UserAccountDelegate userAccountDelegate;
	
	
	
	/* Constructor */
	
	
	/**
	 * Constructor for Administered Questionnaire Service Impl.
	 * @param questionnaireTypeDelegate - questionnaire type delegate
	 * @param administeredQuestionnaireDelegate - administered questionnaire 
	 * delegate
	 * @param administeredQuestionValueDelegate - administered question value
	 * delegate
	 * @param administeredQuestionnaireSectionNoteDelegate - administered
	 * questionnaire section note delegate
	 * @param administeredQuestionnaireSectionStatusDelegate - administered
	 * questionnaire section status delegate
	 * @param allowedQuestionDelegate - allowed question delegate
	 * @param questionnaireSectionDelegate - questionnaire section delegate
	 * @param userAccountDelegate - user account delegate
	 */
	public AdministeredQuestionnaireServiceImpl(
			final QuestionnaireTypeDelegate questionnaireTypeDelegate,
			final AdministeredQuestionnaireDelegate 
				administeredQuestionnaireDelegate,
			final AdministeredQuestionValueDelegate 
				administeredQuestionValueDelegate,
			final AdministeredQuestionnaireSectionNoteDelegate 
				administeredQuestionnaireSectionNoteDelegate,
			final AdministeredQuestionnaireSectionStatusDelegate
				administeredQuestionnaireSectionStatusDelegate,
			final AllowedQuestionDelegate allowedQuestionDelegate,
			final QuestionnaireSectionDelegate questionnaireSectionDelegate,
			final UserAccountDelegate userAccountDelegate) {
		this.questionnaireTypeDelegate = questionnaireTypeDelegate;
		this.administeredQuestionnaireDelegate =
				administeredQuestionnaireDelegate;
		this.administeredQuestionValueDelegate =
				administeredQuestionValueDelegate;
		this.administeredQuestionnaireSectionNoteDelegate = 
				administeredQuestionnaireSectionNoteDelegate;
		this.administeredQuestionnaireSectionStatusDelegate =
				administeredQuestionnaireSectionStatusDelegate;
		this.allowedQuestionDelegate = allowedQuestionDelegate;
		this.questionnaireSectionDelegate = questionnaireSectionDelegate;
		this.userAccountDelegate = userAccountDelegate;
	}
	
	/* Service Methods */

	/**{@inheritDoc} */
	@Override
	public List<QuestionnaireType> getAllQuestionnaireTypes(
			final Date effectiveDate) {
		return this.questionnaireTypeDelegate.findAllByDate(effectiveDate);
	}

	/**{@inheritDoc} 
	 * @throws DuplicateEntityFoundException */
	@Override
	public AdministeredQuestionnaire createAdministeredQuestionnaire(
			final Person answerer, final Boolean draft, final String comments,
			final Person assessor, final Date date, 
			final QuestionnaireType questionnaireType) 
					throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireDelegate.create(answerer, draft, 
				comments, assessor, date, questionnaireType);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaire editAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final Person answerer, final Boolean draft, final String comments, 
			final Person assessor, final Date date,
			final QuestionnaireType questionnaireType)
					throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireDelegate.update(
				administeredQuestionnaire, draft, comments, assessor, date, 
				questionnaireType);
	}

	/**{@inheritDoc} */
	@Override
	public void removeAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		this.administeredQuestionnaireDelegate.remove(
				administeredQuestionnaire);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionValue createAdministeredQuestionValue(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final Question question, 
			final QuestionnaireSection questionnaireSection, 
			final AnswerValue answerValue, final  String answerValueText,
			final String comments)
					throws DuplicateEntityFoundException {
		return this.administeredQuestionValueDelegate.create(
				administeredQuestionnaire, question, questionnaireSection, 
				answerValue, answerValueText, comments);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionValue editAdministeredQuestionValue(
			final AdministeredQuestionValue administeredQuestionValue,
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final Question question, 
			final QuestionnaireSection questionnaireSection, 
			final AnswerValue answerValue, final String answerValueText,
			final String comments) 
					throws DuplicateEntityFoundException {
		return this.administeredQuestionValueDelegate.update(
				administeredQuestionValue, administeredQuestionnaire, question, 
				questionnaireSection, answerValue, answerValueText, comments);
	}

	/**{@inheritDoc} */
	@Override
	public void removeAdministeredQuestionValue(
			final AdministeredQuestionValue administeredQuestionValue) {
		this.administeredQuestionValueDelegate.remove(
				administeredQuestionValue);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaireSectionNote 
		createAdministeredQuestionnaireSectionNote(
				final AdministeredQuestionnaire administeredQuestionnaire, 
				final QuestionnaireSection questionnaireSection,
				final String comments)
						throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireSectionNoteDelegate.create(
				administeredQuestionnaire, questionnaireSection, comments);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaireSectionNote 
		updateAdministeredQuestionnaireSectionNote(
				final AdministeredQuestionnaireSectionNote 
					administeredQuestionnaireSectionNote,
				final QuestionnaireSection questionnaireSection, 
				final String comments) 
						throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireSectionNoteDelegate.update(
				administeredQuestionnaireSectionNote, questionnaireSection, 
				comments);
	}

	/**{@inheritDoc} */
	@Override
	public void removeAdministeredQuestionnaireSectionNote(
			final AdministeredQuestionnaireSectionNote 
				administeredQuestionnaireSectionNote) {
		this.administeredQuestionnaireSectionNoteDelegate.remove(
				administeredQuestionnaireSectionNote);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionValue 
		findAdministeredQuestionValueByQuestionAndQuestionnaire(
			final Question question, 
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.administeredQuestionValueDelegate
				.findByQuestionAndAdministeredQuestionnaire(question,
						administeredQuestionnaire);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<AdministeredQuestionValue> 
		findAdministeredQuestionValuesByQuestionAndQuestionnaire(
			final Question question, 
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.administeredQuestionValueDelegate
				.findAllByQuestionAndAdministeredQuestionnaire(question,
						administeredQuestionnaire);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaireSectionNote 
		findAdministeredQuestionnaireSectionNoteByQuestionnaireAndSection(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final QuestionnaireSection questionnaireSection) {
		return this.administeredQuestionnaireSectionNoteDelegate
				.findByQuestionnaireAndSection(administeredQuestionnaire,
						questionnaireSection);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaireSectionStatus 
		createAdministeredQuestionnaireSectionStatus(
			final QuestionnaireSection questionnaireSection,
			final AdministeredQuestionnaire administeredQuestionnaire,
			final Boolean draft, final Date date) 
					throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireSectionStatusDelegate
				.create(questionnaireSection, administeredQuestionnaire, draft, 
						date);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionnaireSectionStatus 
		updateAdministeredQuestionnaireSectionStatus(
			final AdministeredQuestionnaireSectionStatus 
				administeredQuestionnaireSectionStatus, final Boolean draft, 
			final Date date) throws DuplicateEntityFoundException {
		return this.administeredQuestionnaireSectionStatusDelegate
				.update(administeredQuestionnaireSectionStatus, draft, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeAdministeredQuestionnaireSectionStatus(
			final AdministeredQuestionnaireSectionStatus 
				administeredQuestionnaireSectionStatus) {
		this.administeredQuestionnaireSectionStatusDelegate
			.remove(administeredQuestionnaireSectionStatus);
	}

	/**{@inheritDoc} */
	@Override
	public List<AllowedQuestion> findAllowedQuestionsByQuestionSection(
			final QuestionnaireSection questionnaireSection) {
		return this.allowedQuestionDelegate
				.findAllByQuestionnaireSection(questionnaireSection);
	}

	/**{@inheritDoc} */
	@Override
	public List<AdministeredQuestionnaireSectionStatus> 
		findAdministeredQuestionnaireSectionStatusByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.administeredQuestionnaireSectionStatusDelegate
				.findByAdministeredQuestionnaire(administeredQuestionnaire);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<UserAccount> findUserAccounts(final String query) {
		return this.userAccountDelegate.search(query);
	}

	/**{@inheritDoc} */
	@Override
	public List<QuestionnaireSection>
		findQuestionnaireSectionsByQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		return this.questionnaireSectionDelegate
				.findByQuestionnaireType(questionnaireType);
	}

	/**{@inheritDoc} */
	@Override
	public AdministeredQuestionValue
		findAdministeredQuestionValueByNoAnswerValue(
			final Question question,
			final AdministeredQuestionnaire administeredQuestionnaire,
			final QuestionnaireSection questionnaireSection) {
		return this.administeredQuestionValueDelegate.findByNoAnswerValue(
				question, administeredQuestionnaire, questionnaireSection);
	}

}
