package omis.sentence.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.conviction.domain.Conviction;
import omis.courtcase.domain.CourtCase;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.sentence.dao.SentenceDao;
import omis.sentence.domain.Sentence;

/**
 * Hibernate implementation of data access object for sentences.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.3 (Apr 27, 2017)
 * @since OMIS 3.0
 */
public class SentenceDaoHibernateImpl 
	extends GenericHibernateDaoImpl<Sentence>
	implements SentenceDao {

	/* Named queries. */
	
	private static final String FIND_BY_CONVICTION_QUERY_NAME =
			"findSentenceByConviction";

	private static final String FIND_ACTIVE_BY_CONVICTION_QUERY_NAME =
			"findActiveSentenceByConviction";
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME
			= "findSentencesByOffender";
	
	private static final String FIND_QUERY_NAME
		= "findSentence";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findSentenceExcluding";
	
	private static final String FIND_ACTIVE_EXCLUDING_COURT_CASE_QUERY_NAME
		= "findActiveSentencesExcludingCourtCase";
	
	private static final String COUNT_SENTENCES_BY_CONVICTION_QUERY_NAME
		= "countSentencesByConviction";
	
	private static final String FIND_ACTIVE_QUERY_NAME = "findActiveSentences";
	
	private static final String REMOVE_BY_CONVICTION_QUERY_NAME 
		= "removeSentencesByConviction";
	
	private static final String UPDATE_WITH_CONSECUTIVE_QUERY_NAME
		= "updateSentenceConnectionsWithConsecutiveSentence";
	
	private static final String COUNT_CONNECTED_QUERY_NAME
		= "countSentenceConnected";
	
	/* Parameters. */
	
	private static final String CONVICTION_PARAM_NAME = "conviction";

	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String COURT_CASE_PARAM_NAME = "courtCase";

	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private static final String SENTENCE_PARAM_NAME = "sentence";

	private static final String OLD_SENTENCE_PARAM_NAME = "oldSentence";

	private static final String NEW_SENTENCE_PARAM_NAME = "newSentence";
	
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * sentences with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public SentenceDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<Sentence> findByConviction(final Conviction conviction) {
		@SuppressWarnings("unchecked")
		List<Sentence> sentences = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_CONVICTION_QUERY_NAME)
				.setParameter(CONVICTION_PARAM_NAME, conviction)
				.list();
		return sentences;
	}
	
	/** {@inheritDoc} */
	@Override
	public Sentence findActiveByConviction(final Conviction conviction) {
		Sentence sentence = (Sentence)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ACTIVE_BY_CONVICTION_QUERY_NAME)
				.setParameter(CONVICTION_PARAM_NAME, conviction)
				.uniqueResult();
		return sentence;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Sentence> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<Sentence> sentences = this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.list();
		return sentences;
	}

	/** {@inheritDoc} */
	@Override
	public List<Sentence> findActiveSentencesForOtherCourtCases(
			final Person person, final CourtCase courtCase) {
		@SuppressWarnings("unchecked")
		List<Sentence> sentences = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ACTIVE_EXCLUDING_COURT_CASE_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setParameter(COURT_CASE_PARAM_NAME, courtCase)
				.list();
		return sentences;
	}

	/** {@inheritDoc} */
	@Override
	public Sentence find(final Conviction conviction, 
			final Date effectiveDate) {
		Sentence sentence = (Sentence) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(CONVICTION_PARAM_NAME, conviction)
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.uniqueResult();
		return sentence;
	}

	/** {@inheritDoc} */
	@Override
	public Sentence findExcluding(final Sentence excludedSentence, 
			final Conviction conviction, final Date effectiveDate) {
		Sentence sentence = (Sentence) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(CONVICTION_PARAM_NAME, conviction)
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setParameter(SENTENCE_PARAM_NAME, excludedSentence)
				.uniqueResult();
		return sentence;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Sentence> findActiveSentences(final Person person) {
		@SuppressWarnings("unchecked")
		List<Sentence> sentences = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ACTIVE_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.list();
		return sentences;
	}

	/** {@inheritDoc} */
	@Override
	public void removeByConviction(final Conviction conviction) {
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(REMOVE_BY_CONVICTION_QUERY_NAME)
			.setParameter(CONVICTION_PARAM_NAME, conviction)
			.executeUpdate();
	}

	@Override
	public Integer countSentencesByConviction(Conviction conviction) {
		Integer count = ((Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_SENTENCES_BY_CONVICTION_QUERY_NAME)
				.setParameter(CONVICTION_PARAM_NAME, conviction)
				.uniqueResult()).intValue();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public int updateWithConsecutiveSentence(
			final Sentence oldSentence, final Sentence newSentence) {
		return this.getSessionFactory().getCurrentSession()
				.getNamedQuery(UPDATE_WITH_CONSECUTIVE_QUERY_NAME)
				.setParameter(OLD_SENTENCE_PARAM_NAME, oldSentence)
				.setParameter(NEW_SENTENCE_PARAM_NAME, newSentence)
				.executeUpdate();
	}

	/** {@inheritDoc} */
	@Override
	public long countConnected(final Sentence sentence) {
		return (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_CONNECTED_QUERY_NAME)
				.setParameter(SENTENCE_PARAM_NAME, sentence)
				.uniqueResult();
	}
}