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
package omis.assessment.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.assessment.dao.SumRatedCategoryDao;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.SumRatedCategory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of the sum rated category data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class SumRatedCategoryDaoHibernateImpl 
		extends GenericHibernateDaoImpl<SumRatedCategory>
		implements SumRatedCategoryDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findSumRatedCategory";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findSumRatedCategoryExcluding";
	
	private static final String FIND_BY_RATING_CATGORY_QUERY_NAME = 
			"findSumRatedCategoryByRatingCategory";
	
	/* Parameters. */
	
	private static final String RATING_CATEGORY_PARAM_NAME = "ratingCategory";
	
	private static final String VALID_PARAM_NAME = "valid";
	
	private static final String EXCLUDED_PARAM_NAME = "excludedSumRatedCategory";
	
	/** Instantiates a hibernate implementation of the data access object for 
	*  sum rated category.
	* 
	* @param sessionFactory session factory
	* @param entityName entity name
	*/
	public SumRatedCategoryDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public SumRatedCategory find(final RatingCategory ratingCategory, 
			final Boolean valid) {
		SumRatedCategory sumRatedCategory = (SumRatedCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.setParameter(VALID_PARAM_NAME, valid)
				.uniqueResult();
		return sumRatedCategory;
	}

	/** {@inheritDoc} */
	@Override
	public SumRatedCategory findExcluding(final RatingCategory ratingCategory, 
			final Boolean valid, 
			final SumRatedCategory excludedSumRatedCategory) {
		SumRatedCategory sumRatedCategory = (SumRatedCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.setParameter(VALID_PARAM_NAME, valid)
				.setParameter(EXCLUDED_PARAM_NAME, excludedSumRatedCategory)
				.uniqueResult();
		return sumRatedCategory;
	}

	/** {@inheritDoc} */
	@Override
	public SumRatedCategory findByRatingCategory(
			final RatingCategory ratingCategory) {
		SumRatedCategory sumRatedCategory = (SumRatedCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_RATING_CATGORY_QUERY_NAME)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.uniqueResult();
		return sumRatedCategory;
	}
}