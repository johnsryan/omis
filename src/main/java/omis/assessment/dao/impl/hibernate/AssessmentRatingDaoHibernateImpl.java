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
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.assessment.dao.AssessmentRatingDao;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingRank;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.demographics.domain.Sex;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Hibernate implementation of the assessment rating data access object.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class AssessmentRatingDaoHibernateImpl 
		extends GenericHibernateDaoImpl<AssessmentRating>
		implements AssessmentRatingDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findAssessmentRating";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findAssessmentRatingExcluding";
	
	private final static String FIND_BY_RATING_CATEGORY_QUERY_NAME = 
			"findAssessmentRatingsByRatingCategory";
	
	/* Parameters. */
	
	private final static String QUESTIONNAIRE_TYPE_PARAM_NAME = 
			"questionnaireType";
	
	private final static String SEX_PARAM_NAME = "sex";
	
	private final static String MIN_PARAM_NAME = "min";
	
	private final static String MAX_PARAM_NAME = "max";
	
	private final static String START_DATE_PARAM_NAME = "startDate";
	
	private final static String END_DATE_PARAM_NAME = "endDate";
	
	private final static String EXCLUDED_ASSESSMENT_RATING_PARAM_NAME = 
			"excludedAssessmentRating";
	
	private final static String RATING_CATEGORY_PARAM_NAME = "ratingCategory";
	
	private final static String RATING_RANK_PARAM_NAME = "ratingRank";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * assessment rating.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AssessmentRatingDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentRating find(final QuestionnaireType questionnaireType, 
			final Sex sex, final BigDecimal min, final BigDecimal max,
			final Date startDate, final Date endDate, 
			final RatingCategory ratingCategory, final RatingRank rank) {
		AssessmentRating assessmentRating = (AssessmentRating) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
				.setParameter(SEX_PARAM_NAME, sex)
				.setParameter(MIN_PARAM_NAME, min)
				.setParameter(MAX_PARAM_NAME, max)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.setParameter(RATING_RANK_PARAM_NAME, rank)
				.uniqueResult();
		return assessmentRating;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentRating findExcluding(
			final QuestionnaireType questionnaireType, final Sex sex, 
			final BigDecimal min, final BigDecimal max, final Date startDate, 
			final Date endDate, final RatingCategory ratingCategory, 
			final RatingRank rank, 
			final AssessmentRating excludedAssessmentRating) {
		AssessmentRating assessmentRating = (AssessmentRating) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
				.setParameter(SEX_PARAM_NAME, sex)
				.setParameter(MIN_PARAM_NAME, min)
				.setParameter(MAX_PARAM_NAME, max)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.setParameter(RATING_RANK_PARAM_NAME, rank)
				.setParameter(EXCLUDED_ASSESSMENT_RATING_PARAM_NAME, 
						excludedAssessmentRating)
				.uniqueResult();
		return assessmentRating;
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentRating> findByRatingCategory(
			final RatingCategory ratingCategory) {
		@SuppressWarnings("unchecked")
		List<AssessmentRating> assessmentRatings = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_RATING_CATEGORY_QUERY_NAME)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.list();
		return assessmentRatings;
	}
}