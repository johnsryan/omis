package omis.hearing.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearing.dao.InfractionDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.Infraction;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;

/**
 * InfractionDaoHibernateImpl.java
 * 
 *@author Annie Jacques 
 *@author Ryan Johns
 *@version 0.1.1 (July 6, 2018)
 *@since OMIS 3.0
 *
 */
public class InfractionDaoHibernateImpl
	extends GenericHibernateDaoImpl<Infraction>
	implements InfractionDao{

	private static final String FIND_INFRACTION_QUERY_NAME = "findInfraction";
	
	private static final String FIND_INFRACTION_EXCLUDING_QUERY_NAME =
			"findInfractionExcluding";
	
	private static final String FIND_INFRACTIONS_BY_HEARING_QUERY_NAME =
			"findInfractionsByHearing";
	
	private static final String HEARING_PARAM_NAME = "hearing";
	
	private static final String CONDITION_VIOLATION_PARAM_NAME =
			"conditionViolation";
	
	private static final String DISCIPLINARY_CODE_VIOLATION_PARAM_NAME =
			"disciplinaryCodeViolation";
	
	private static final String INFRACTION_PARAM_NAME = "infraction";
	
	/**
	 * @param sessionFactory
	 * @param entityName
	 */
	protected InfractionDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public Infraction find(final Hearing hearing,
			final ConditionViolation conditionViolation,
			final DisciplinaryCodeViolation disciplinaryCodeViolation) {
		Infraction infraction = (Infraction) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_INFRACTION_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing, 
						this.getEntityPropertyType(HEARING_PARAM_NAME))
				.setParameter(CONDITION_VIOLATION_PARAM_NAME, 
						conditionViolation, 
						this.getEntityPropertyType(CONDITION_VIOLATION_PARAM_NAME))
				.setParameter(DISCIPLINARY_CODE_VIOLATION_PARAM_NAME,
						disciplinaryCodeViolation, 
						this.getEntityPropertyType(DISCIPLINARY_CODE_VIOLATION_PARAM_NAME))
				.uniqueResult();
		
		return infraction;
	}

	/**{@inheritDoc} */
	@Override
	public Infraction findExcluding(final Hearing hearing,
			final ConditionViolation conditionViolation,
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final Infraction infractionExcluded) {
		Infraction infraction = (Infraction) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_INFRACTION_EXCLUDING_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing, 
						this.getEntityPropertyType(HEARING_PARAM_NAME))
				.setParameter(CONDITION_VIOLATION_PARAM_NAME, conditionViolation, 
						this.getEntityPropertyType(CONDITION_VIOLATION_PARAM_NAME))
				.setParameter(DISCIPLINARY_CODE_VIOLATION_PARAM_NAME,
						disciplinaryCodeViolation, 
						this.getEntityPropertyType(
								DISCIPLINARY_CODE_VIOLATION_PARAM_NAME))
				.setParameter(INFRACTION_PARAM_NAME, infractionExcluded)
				.uniqueResult();
		
		return infraction;
	}

	/**{@inheritDoc} */
	@Override
	public List<Infraction> findByHearing(final Hearing hearing) {
		@SuppressWarnings("unchecked")
		List<Infraction> infractions = this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_INFRACTIONS_BY_HEARING_QUERY_NAME)
			.setParameter(HEARING_PARAM_NAME, hearing)
			.list();
		
		return infractions;
	}

}
