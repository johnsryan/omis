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
package omis.presentenceinvestigation.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.presentenceinvestigation.dao.PresentenceInvestigationDelayCategoryDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;

/**
 * Hibernate implementation of the presentence investigation delay category data 
 * access object.
 *
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationDelayCategoryDaoHibernateImpl 
	extends GenericHibernateDaoImpl<PresentenceInvestigationDelayCategory> 
	implements PresentenceInvestigationDelayCategoryDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findPresentenceInvestigationDelayCategory";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findPresentenceInvestigationDelayCategoryExcluding";
	
	private static final String FIND_VALID_QUERY_NAME = 
			"findPresentenceInvestigationDelayCategories";
	
	/* Parameters. */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String EXCLUDED_CATEGORY_PARAM_NAME = 
			"excludedCategory";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  presentence investigation delay category.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public PresentenceInvestigationDelayCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationDelayCategory find(final String name) {
		PresentenceInvestigationDelayCategory category = 
				(PresentenceInvestigationDelayCategory) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return category;
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationDelayCategory findExcluding(
			final String name,
			final PresentenceInvestigationDelayCategory 
					presentenceInvestigationDelayCategoryExcluded) {
		PresentenceInvestigationDelayCategory category = 
				(PresentenceInvestigationDelayCategory) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(EXCLUDED_CATEGORY_PARAM_NAME, 
						presentenceInvestigationDelayCategoryExcluded)
				.uniqueResult();
		return category;
	}

	/** {@inheritDoc} */
	@Override
	public List<PresentenceInvestigationDelayCategory> 
			findPresentenceInvestigationDelayCategories() {
		@SuppressWarnings("unchecked")
		List<PresentenceInvestigationDelayCategory> categories = this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_VALID_QUERY_NAME)
				.list();
		return categories;
	}
}