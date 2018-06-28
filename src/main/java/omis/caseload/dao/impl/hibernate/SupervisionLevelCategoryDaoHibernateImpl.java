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
package omis.caseload.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.caseload.dao.SupervisionLevelCategoryDao;
import omis.caseload.domain.SupervisionLevelCategory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of data access object for supervision level category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jun 13, 2018)
 * @since OMIS 3.0
 */
public class SupervisionLevelCategoryDaoHibernateImpl
		extends GenericHibernateDaoImpl<SupervisionLevelCategory> 
		implements SupervisionLevelCategoryDao {

/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findSupervisionLevelCategory";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findSupervisionLevelCategoryExcluding";
	
	private static final String FIND_VALID_QUERY_NAME = 
			"findSupervisionLevelCategories";
	
	/* Parameters. */
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String EXCLUDED_SUPERVISION_LEVEL_CATEGORY_PARAM_NAME =
			"excludedSupervisionLevelCategory";	
	
	/** 
	 * Instantiates a hibernate implementation of supervision level category 
	 * data access object.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public SupervisionLevelCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public SupervisionLevelCategory find(final String description) {
		SupervisionLevelCategory supervisionLevelCategory = 
				(SupervisionLevelCategory) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.uniqueResult();
		return supervisionLevelCategory;
	}

	/** {@inheritDoc} */
	@Override
	public SupervisionLevelCategory findExcluding(final String description,
			final SupervisionLevelCategory excludedSupervisionLevelCategory) {
		SupervisionLevelCategory supervisionLevelCategory = 
				(SupervisionLevelCategory) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(EXCLUDED_SUPERVISION_LEVEL_CATEGORY_PARAM_NAME, 
						excludedSupervisionLevelCategory)
				.uniqueResult();
		return supervisionLevelCategory;
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisionLevelCategory> findValid() {
		@SuppressWarnings("unchecked")
		List<SupervisionLevelCategory> categories = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VALID_QUERY_NAME)
				.list();
		return categories;
	}
}