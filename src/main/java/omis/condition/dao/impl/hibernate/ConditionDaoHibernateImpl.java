package omis.condition.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.condition.dao.ConditionDao;
import omis.condition.domain.Agreement;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.Condition;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;

/**
 * Condition Dao Hibernate Implementation
 * 
 * @author Jonny Santy
 * @author Annie Jacques
 * @version 0.1.2 (May 21, 2017)
 * @since OMIS 3.0
 */
public class ConditionDaoHibernateImpl
	extends GenericHibernateDaoImpl <Condition> implements ConditionDao{

	/* Query names. */
	
	private static final String FIND_QUERY_NAME = "findCondition";

	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findConditionExcluding";
	
	private static final String FIND_BY_AGREEMENT_QUERY_NAME = 
			"findConditionsByAgreement";
	
	private static final String FIND_WAIVED_BY_AGREEMENT_QUERY_NAME = 
			"findWaivedConditionsByAgreement";
	
	private static final String FIND_BY_OFFENDER_AND_EFFECTIVE_DATE_QUERY_NAME =
			"findConditionsByOffenderAndEffectiveDate";
	
	private static final String FIND_BY_CLAUSE_AND_OFFENDER_ON_DATE_QUERY_NAME
		= "findConditionsByClauseAndOffenderOnDate";
	
	/* Parameter names. */
	
	private static final String CONDITION_CLAUSE_PARAM_NAME = "conditionClause";
	
	private static final String CLAUSE_PARAM_NAME = "clause";

	private static final String AGREEMENT_PARAM_NAME = "agreement";
	
	private static final String CONDITION_PARAM_NAME = "condition";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";

	/* Constructor. */
	
	/**
	 * Instantiates a data access object for offender cautions with the
	 * specified session factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ConditionDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Condition find(final ConditionClause conditionClause,
			final String clause, final Agreement agreement) {
		Condition condition = (Condition) this
				.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(CONDITION_CLAUSE_PARAM_NAME, conditionClause)
				.setParameter(CLAUSE_PARAM_NAME, clause)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.uniqueResult();
		return condition;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Condition findExcluding(final Condition condition,
			final ConditionClause conditionClause, final String clause,
			final Agreement agreement) {
		Condition newCondition = (Condition) this
				.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(CONDITION_PARAM_NAME, condition)
				.setParameter(CONDITION_CLAUSE_PARAM_NAME, conditionClause)
				.setParameter(CLAUSE_PARAM_NAME, clause)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.uniqueResult();
		return newCondition;
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<Condition> findByAgreement(final Agreement agreement) {
		@SuppressWarnings("unchecked")
		List<Condition> condition = (List<Condition>) 
				this
				.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_BY_AGREEMENT_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.list();
		
		return condition;
	}

	/**{@inheritDoc} */
	@Override
	public List<Condition> findByOffenderAndEffectiveDate(
			final Offender offender, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<Condition> conditions = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_AND_EFFECTIVE_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.list();
		
		return conditions;
	}

	/**{@inheritDoc} */
	@Override
	public List<Condition> findWaivedByAgreement(Agreement agreement) {
		@SuppressWarnings("unchecked")
		List<Condition> conditions = (List<Condition>) 
				this
				.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_WAIVED_BY_AGREEMENT_QUERY_NAME)
				.setParameter(AGREEMENT_PARAM_NAME, agreement)
				.list();
		
		return conditions;
	}

	/**{@inheritDoc} */
	@Override
	public List<Condition> findByConditionClauseAndOffenderOnDate(final ConditionClause clause, 
			final Offender offender, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<Condition> conditions = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_CLAUSE_AND_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(CLAUSE_PARAM_NAME, clause)
				.setDate(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.list();
		return conditions;
	}
}
