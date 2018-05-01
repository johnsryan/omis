/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.warrant.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.condition.domain.ConditionClause;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.warrant.dao.WarrantCauseViolationDao;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantCauseViolation;

/**
 * Warrant cause violation data access object hibernate implementation.
 * 
 *@author Annie Jacques 
 *@author Joel Norris
 *@version 0.1.1 (January 25, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantCauseViolationDaoHibernateImpl
	extends GenericHibernateDaoImpl<WarrantCauseViolation>
	implements WarrantCauseViolationDao {
	
	private static final String FIND_WARRANT_CAUSE_VIOLATION_QUERY_NAME =
			"findWarrantCauseViolation";
	
	private static final String FIND_WARRANT_CAUSE_VIOLATION_EXCLUDING_QUERY_NAME =
			"findWarrantCauseViolationExcluding";
	
	private static final String FIND_WARRANT_CAUSE_VIOLATIONS_BY_WARRANT_QUERY_NAME =
			"findWarrantCauseViolationsByWarrant";
	
	private static final String WARRANT_PARAM_NAME = "warrant";
	
	private static final String CONDITION_CLAUSE_PARAM_NAME = "conditionClause";
	
	private static final String WARRANT_CAUSE_VIOLATION_PARAM_NAME =
			"warrantCauseViolation";
	
	/**
	 * Instantiates a warrant cause violation data access object with the specified
	 * session factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public WarrantCauseViolationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantCauseViolation find(
			final Warrant warrant,
			final ConditionClause conditionClause) {
		WarrantCauseViolation warrantCauseViolation = (WarrantCauseViolation)
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_WARRANT_CAUSE_VIOLATION_QUERY_NAME)
				.setParameter(WARRANT_PARAM_NAME, warrant)
				.setParameter(CONDITION_CLAUSE_PARAM_NAME, conditionClause)
				.uniqueResult();
		
		return warrantCauseViolation;
	}
	
	/**{@inheritDoc} */
	@Override
	public WarrantCauseViolation findExcluding(
			final Warrant warrant, 
			final ConditionClause conditionClause,
			final WarrantCauseViolation warrantCauseViolationExcluding) {
		WarrantCauseViolation warrantCauseViolation = (WarrantCauseViolation)
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_WARRANT_CAUSE_VIOLATION_EXCLUDING_QUERY_NAME)
				.setParameter(WARRANT_PARAM_NAME, warrant)
				.setParameter(CONDITION_CLAUSE_PARAM_NAME, conditionClause)
				.setParameter(WARRANT_CAUSE_VIOLATION_PARAM_NAME,
						warrantCauseViolationExcluding)
				.uniqueResult();
		
		return warrantCauseViolation;
	}
	
	/**{@inheritDoc} */
	@Override
	public List<WarrantCauseViolation> findByWarrant(final Warrant warrant) {
		@SuppressWarnings("unchecked")
		List<WarrantCauseViolation> warrantCauseViolations =
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_WARRANT_CAUSE_VIOLATIONS_BY_WARRANT_QUERY_NAME)
				.setParameter(WARRANT_PARAM_NAME, warrant)
				.list();
		
		return warrantCauseViolations;
	}
}
