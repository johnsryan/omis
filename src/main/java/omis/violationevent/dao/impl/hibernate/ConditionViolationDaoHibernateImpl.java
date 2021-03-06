package omis.violationevent.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.condition.domain.Condition;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.violationevent.dao.ConditionViolationDao;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.ViolationEvent;

/**
 * ConditionViolationDaoHibernateImpl.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Feb 15, 2017)
 *@since OMIS 3.0
 *
 */
public class ConditionViolationDaoHibernateImpl
		extends GenericHibernateDaoImpl<ConditionViolation>
		implements ConditionViolationDao {
	
	private static final String FIND_CONDITION_VIOLATION_QUERY_NAME =
			"findConditionViolation";
	
	private static final String FIND_CONDITION_VIOLATION_EXCLUDING_QUERY_NAME =
			"findConditionViolationExcluding";
	
	private static final String
		FIND_CONDITION_VIOLATIONS_BY_VIOLATION_EVENT_QUERY_NAME =
			"findConditionViolationsByViolationEvent";
	
	private static final String
		FIND_UNRESOLVED_CONDITION_VIOLATIONS_BY_VIOLATION_EVENT_QUERY_NAME =
			"findUnresolvedConditionViolationsByViolationEvent";
	
	
	private static final String CONDITION_PARAM_NAME = "condition";
	
	private static final String VIOLATION_EVENT_PARAM_NAME = "violationEvent";
	
	private static final String CONDITION_VIOLATION_PARAM_NAME =
			"conditionViolation";
	
	
	
	/**
	 * @param sessionFactory
	 * @param entityName
	 */
	protected ConditionViolationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public ConditionViolation find(final Condition condition,
			final ViolationEvent violationEvent) {
		ConditionViolation conditionViolation = (ConditionViolation)
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_CONDITION_VIOLATION_QUERY_NAME)
				.setParameter(CONDITION_PARAM_NAME, condition)
				.setParameter(VIOLATION_EVENT_PARAM_NAME, violationEvent)
				.uniqueResult();
		
		return conditionViolation;
	}

	/**{@inheritDoc} */
	@Override
	public ConditionViolation findExcluding(final Condition condition,
			final ViolationEvent violationEvent,
			final ConditionViolation excludedConditionViolation) {
		ConditionViolation conditionViolation = (ConditionViolation)
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_CONDITION_VIOLATION_EXCLUDING_QUERY_NAME)
				.setParameter(CONDITION_PARAM_NAME, condition)
				.setParameter(VIOLATION_EVENT_PARAM_NAME, violationEvent)
				.setParameter(CONDITION_VIOLATION_PARAM_NAME,
						excludedConditionViolation)
				.uniqueResult();
		
		return conditionViolation;
	}

	/**{@inheritDoc} */
	@Override
	public List<ConditionViolation> findByViolationEvent(
			final ViolationEvent violationEvent) {
		@SuppressWarnings("unchecked")
		List<ConditionViolation> conditionViolations =
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
					FIND_CONDITION_VIOLATIONS_BY_VIOLATION_EVENT_QUERY_NAME)
				.setParameter(VIOLATION_EVENT_PARAM_NAME, violationEvent)
				.list();
			
		return conditionViolations;
	}

	/**{@inheritDoc} */
	@Override
	public List<ConditionViolation> findUnresolvedByViolationEvent(
			final ViolationEvent violationEvent) {
		@SuppressWarnings("unchecked")
		List<ConditionViolation> conditionViolations =
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
				FIND_UNRESOLVED_CONDITION_VIOLATIONS_BY_VIOLATION_EVENT_QUERY_NAME)
				.setParameter(VIOLATION_EVENT_PARAM_NAME, violationEvent)
				.list();
			
		return conditionViolations;
	}

}
