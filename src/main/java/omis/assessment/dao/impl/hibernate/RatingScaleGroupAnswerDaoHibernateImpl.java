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

import omis.assessment.dao.RatingScaleGroupAnswerDao;
import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.assessment.domain.RatingScaleGroupAnswer;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of the rating scale group answer data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 5, 2018)
 * @since OMIS 3.0
 */
public class RatingScaleGroupAnswerDaoHibernateImpl 
		extends GenericHibernateDaoImpl<RatingScaleGroupAnswer>
		implements RatingScaleGroupAnswerDao {
	
	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findRatingScaleGroupAnswer";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findRatingScaleGroupAnswerExcluding";
	
	/* Parameters. */
	
	private final static String ANSWER_RATING_PARAM_NAME = "answerRating";
	
	private final static String SCALE_GROUP_PARAM_NAME = "scaleGroup";
	
	private final static String EXCLUDED_RATING_SCALE_GROUP_ANSWER_PARAM_NAME = 
			"excludedRatingScaleGroupAnswer";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * rating scale group answer.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public RatingScaleGroupAnswerDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public RatingScaleGroupAnswer find(final AnswerRating answerRating, 
			final RatingScaleGroup scaleGroup) {
		RatingScaleGroupAnswer ratingScaleGroupAnswers = 
				(RatingScaleGroupAnswer) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ANSWER_RATING_PARAM_NAME, answerRating)
				.setParameter(SCALE_GROUP_PARAM_NAME, scaleGroup)
				.uniqueResult();
		return ratingScaleGroupAnswers;
	}

	/** {@inheritDoc} */
	@Override
	public RatingScaleGroupAnswer findExcluding(
			final AnswerRating answerRating, final RatingScaleGroup scaleGroup,
			final RatingScaleGroupAnswer excludedRatingScaleGroupAnswer) {
		RatingScaleGroupAnswer ratingScaleGroupAnswers = 
				(RatingScaleGroupAnswer) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ANSWER_RATING_PARAM_NAME, answerRating)
				.setParameter(SCALE_GROUP_PARAM_NAME, scaleGroup)
				.setParameter(EXCLUDED_RATING_SCALE_GROUP_ANSWER_PARAM_NAME, 
						excludedRatingScaleGroupAnswer)
				.uniqueResult();
		return ratingScaleGroupAnswers;
	}
}