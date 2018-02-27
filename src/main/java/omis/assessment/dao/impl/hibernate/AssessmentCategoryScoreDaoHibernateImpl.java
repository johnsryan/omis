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

import omis.assessment.dao.AssessmentCategoryScoreDao;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.RatingCategory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Hibernate implementation of the assessment category score data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryScoreDaoHibernateImpl 
		extends GenericHibernateDaoImpl<AssessmentCategoryScore>
		implements AssessmentCategoryScoreDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = 
			"findAssessmentCategoryScore";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findAssessmentCategoryScoreExcluding";
	
	/* Parameters. */
	
	private final static String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME = 
			"administeredQuestionnaire";
	
	private final static String RATING_CATEGORY_PARAM_NAME = "ratingCategory";
	
	private final static String EXCLUDED_ASSESSMENT_CATEGORY_SCORE_PARAM_NAME
			= "excludedAssessmentCategoryScore";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * assessment category score.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AssessmentCategoryScoreDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryScore find(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final RatingCategory ratingCategory) {
		AssessmentCategoryScore assessmentCategoryScore = 
				(AssessmentCategoryScore) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.uniqueResult();
		return assessmentCategoryScore;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryScore findExcluding(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final RatingCategory ratingCategory, 
			final AssessmentCategoryScore excludedAssessmentCategoryScore) {
		AssessmentCategoryScore assessmentCategoryScore = 
				(AssessmentCategoryScore) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.setParameter(EXCLUDED_ASSESSMENT_CATEGORY_SCORE_PARAM_NAME, 
						excludedAssessmentCategoryScore)
				.uniqueResult();
		return assessmentCategoryScore;
	}
}