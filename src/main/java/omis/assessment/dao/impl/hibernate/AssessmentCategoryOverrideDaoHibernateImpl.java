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

import omis.assessment.dao.AssessmentCategoryOverrideDao;
import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of the assessment category override data access 
 * object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideDaoHibernateImpl 
		extends GenericHibernateDaoImpl<AssessmentCategoryOverride>
		implements AssessmentCategoryOverrideDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = 
			"findAssessmentCategoryOverride";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findAssessmentCategoryOverrideExcluding";
	
	private final static String FIND_BY_ASSESSMENT_CATEGORY_SCORE_QUERY_NAME =
			"findAssessmentCategoryOverrideByAssessmentCategoryScore";
	
	/* Parameters. */
	
	private final static String ASSESSMENT_CATEGORY_SCORE_PARAM_NAME = 
			"assessmentCategoryScore";
	
	private final static String ASSESSMENT_RATING_PARAM_NAME = "assessmentRating";
	
	private final static String EXCLUDED_ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME
			= "excludedAssessmentCategoryOverride";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * assessment category override.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AssessmentCategoryOverrideDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride find(
			final AssessmentCategoryScore assessmentCategoryScore,
			final AssessmentRating assessmentRating) {
		AssessmentCategoryOverride assessmentCategoryOverride = 
				(AssessmentCategoryOverride) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_SCORE_PARAM_NAME, 
						assessmentCategoryScore)
				.setParameter(ASSESSMENT_RATING_PARAM_NAME, assessmentRating)
				.uniqueResult();
		return assessmentCategoryOverride;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride findExcluding(
			final AssessmentCategoryScore assessmentCategoryScore,
			final AssessmentRating assessmentRating, 
			final AssessmentCategoryOverride excludedAssessmentCategoryOverride) {
		AssessmentCategoryOverride assessmentCategoryOverride = 
				(AssessmentCategoryOverride) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_SCORE_PARAM_NAME, 
						assessmentCategoryScore)
				.setParameter(ASSESSMENT_RATING_PARAM_NAME, assessmentRating)
				.setParameter(EXCLUDED_ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						excludedAssessmentCategoryOverride)
				.uniqueResult();
		return assessmentCategoryOverride;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride findByAssessmentCategoryScore(
			final AssessmentCategoryScore assessmentCategoryScore) {
		AssessmentCategoryOverride assessmentCategoryOverride = 
				(AssessmentCategoryOverride) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_ASSESSMENT_CATEGORY_SCORE_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_SCORE_PARAM_NAME, 
						assessmentCategoryScore)
				.uniqueResult();
		return assessmentCategoryOverride;
	}
}