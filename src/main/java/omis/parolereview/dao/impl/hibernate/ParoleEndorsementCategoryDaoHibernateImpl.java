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
package omis.parolereview.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.parolereview.dao.ParoleEndorsementCategoryDao;
import omis.parolereview.domain.ParoleEndorsementCategory;

/**
 * Hibernate implementation of the parole endorsement category data access 
 * object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class ParoleEndorsementCategoryDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParoleEndorsementCategory>
		implements ParoleEndorsementCategoryDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findParoleEndorsementCategory";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParoleEndorsementCategoryExcluding";
	
	/* Parameters. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedParoleEndorsementCategory";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  parole endorsement category.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParoleEndorsementCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEndorsementCategory find(final String name) {
		ParoleEndorsementCategory category = (ParoleEndorsementCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return category;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEndorsementCategory findExcluding(final String name,
			final ParoleEndorsementCategory excludedParoleEndorsementCategory) {
		ParoleEndorsementCategory category = (ParoleEndorsementCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(EXCLUDED_PARAM_NAME, 
						excludedParoleEndorsementCategory)
				.uniqueResult();
		return category;
	}
}