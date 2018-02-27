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

import omis.assessment.dao.AnswerRatingDao;
import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingCategory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.questionnaire.domain.AnswerValue;

/**
 * Hibernate implementation of the answer rating data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AnswerRatingDaoHibernateImpl 
		extends GenericHibernateDaoImpl<AnswerRating> 
		implements AnswerRatingDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findAnswerRating";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findAnswerRatingExcluding";
	
	/* Parameters. */
	
	private final static String ANSWER_VALUE_PARAM_NAME = "answerValue";
	
	private final static String RATING_CATEGORY_PARAM_NAME = "ratingCategory";
	
	private final static String EXCLUDED_ANSWER_RATING_PARAM_NAME = 
			"excludedAnswerRating";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * answer rating.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AnswerRatingDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public AnswerRating find(final AnswerValue answerValue, 
			final RatingCategory ratingCategory) {
		AnswerRating answerRating = (AnswerRating) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ANSWER_VALUE_PARAM_NAME, answerValue)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.uniqueResult();
		return answerRating;
	}

	/** {@inheritDoc} */
	@Override
	public AnswerRating findExcluding(final AnswerValue answerValue, 
			final RatingCategory ratingCategory,
			final AnswerRating excludedAnswerRating) {
		AnswerRating answerRating = (AnswerRating) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ANSWER_VALUE_PARAM_NAME, answerValue)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.setParameter(EXCLUDED_ANSWER_RATING_PARAM_NAME, 
						excludedAnswerRating)
				.uniqueResult();
		return answerRating;
	}
}