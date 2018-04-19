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

import java.math.BigDecimal;

import org.hibernate.SessionFactory;

import omis.assessment.dao.GroupSumRatingDao;
import omis.assessment.domain.GroupSumRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of the group sum rating data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 5, 2018)
 * @since OMIS 3.0
 */
public class GroupSumRatingDaoHibernateImpl 
		extends GenericHibernateDaoImpl<GroupSumRating>
		implements GroupSumRatingDao {
	
	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findGroupSumRating";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findGroupSumRatingExcluding";
	
	private final static String FIND_BY_RATING_SCALE_GROUP_AND_VALUE_QUERY_NAME 
			= "findGroupSumRatingByRatingScaleGroupAndValue";
	
	/* Parameters. */
	
	private final static String MIN_PARAM_NAME = "min";
	
	private final static String MAX_PARAM_NAME = "max";
	
	private final static String VALUE_PARAM_NAME = "value";
	
	private final static String GROUP_PARAM_NAME = "group";
	
	private final static String EXCLUDED_RATING_CATEGORY_PARAM_NAME = 
			"excludedGroupSumRating";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for group 
	 * sum rating.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public GroupSumRatingDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public GroupSumRating find(final BigDecimal min, final BigDecimal max, 
			final BigDecimal value, final RatingScaleGroup group) {
		GroupSumRating groupSumRating = (GroupSumRating) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(MIN_PARAM_NAME, min)
				.setParameter(MAX_PARAM_NAME, max)
				.setParameter(VALUE_PARAM_NAME, value)
				.setParameter(GROUP_PARAM_NAME, group)
				.uniqueResult();
		return groupSumRating;
	}

	/** {@inheritDoc} */
	@Override
	public GroupSumRating findExcluding(final BigDecimal min, 
			final BigDecimal max, final BigDecimal value, 
			final RatingScaleGroup group,
			final GroupSumRating excludedGroupSumRating) {
		GroupSumRating groupSumRating = (GroupSumRating) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(MIN_PARAM_NAME, min)
				.setParameter(MAX_PARAM_NAME, max)
				.setParameter(VALUE_PARAM_NAME, value)
				.setParameter(GROUP_PARAM_NAME, group)
				.setParameter(EXCLUDED_RATING_CATEGORY_PARAM_NAME, 
						excludedGroupSumRating)
				.uniqueResult();
		return groupSumRating;
	}

	/** {@inheritDoc} */
	@Override
	public GroupSumRating findByRatingScaleGroupAndValue(
			final RatingScaleGroup ratingScaleGroup, final BigDecimal value) {
		GroupSumRating groupSumRating = (GroupSumRating) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_RATING_SCALE_GROUP_AND_VALUE_QUERY_NAME)
				.setParameter(GROUP_PARAM_NAME, ratingScaleGroup)
				.setParameter(VALUE_PARAM_NAME, value)
				.uniqueResult();
		return groupSumRating;
	}
}