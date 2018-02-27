/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package omis.paroleplan.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.dao.ParolePlanDao;
import omis.paroleplan.domain.ParolePlan;

/**
 * Hibernate implementation of the parole plan data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParolePlan>
		implements ParolePlanDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findParolePlan";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParolePlanExcluding";
	
	private static final String FIND_BY_PAROLE_ELIGIBILITY_QUERY_NAME = 
			"findParolePlanByParoleEligibility";
	
	/* Parameters. */
	
	private static final String PAROLE_ELIGIBILITY_PARAM_NAME = 
			"paroleEligibility";
	
	private static final String EXCLUDED_PARAM_NAME = "excludedParolePlan";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  parole plan.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParolePlanDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlan find(final ParoleEligibility paroleEligibility) {
		ParolePlan parolePlan = (ParolePlan) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(PAROLE_ELIGIBILITY_PARAM_NAME, paroleEligibility)
				.uniqueResult();
		return parolePlan;
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlan findExcluding(final ParoleEligibility paroleEligibility, 
			final ParolePlan excludedParolePlan) {
		ParolePlan parolePlan = (ParolePlan) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(PAROLE_ELIGIBILITY_PARAM_NAME, paroleEligibility)
				.setParameter(EXCLUDED_PARAM_NAME, excludedParolePlan)
				.uniqueResult();
		return parolePlan;
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlan findByParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		ParolePlan parolePlan = (ParolePlan) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_PAROLE_ELIGIBILITY_QUERY_NAME)
				.setParameter(PAROLE_ELIGIBILITY_PARAM_NAME, paroleEligibility)
				.uniqueResult();
		return parolePlan;
	}
}