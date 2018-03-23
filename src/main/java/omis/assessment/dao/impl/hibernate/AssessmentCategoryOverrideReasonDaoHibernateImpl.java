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

import java.util.List;

import org.hibernate.SessionFactory;

import omis.assessment.dao.AssessmentCategoryOverrideReasonDao;
import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.CategoryOverrideReason;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of the assessment category override reason data 
 * access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideReasonDaoHibernateImpl 
		extends GenericHibernateDaoImpl<AssessmentCategoryOverrideReason> 
		implements AssessmentCategoryOverrideReasonDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = 
			"findAssessmentCategoryOverrideReason";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findAssessmentCategoryOverrideReasonExcluding";
	
	private final static String FIND_BY_ASSESSMENT_CATEGORY_OVERRIDE_QUERY_NAME =
			"findAssessmentCategoryOverrideReasonsByAssessmentCategoryOverride";
	
	/* Parameters. */
	
	private final static String ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME = 
			"assessmentCategoryOverride";
	
	private final static String CATEGORY_OVERRIDE_REASON_PARAM_NAME = 
			"categoryOverrideReason";
	
	private final static String 
			EXCLUDED_ASSESSMENT_CATEGORY_OVERRIDE_REASON_PARAM_NAME = 
					"excludedAssessmentCategoryOverrideReason";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * assessment category override reason.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AssessmentCategoryOverrideReasonDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverrideReason find(
			final AssessmentCategoryOverride assessmentCategoryOverride,
			final CategoryOverrideReason categoryOverrideReason) {
		AssessmentCategoryOverrideReason assessmentCategoryOverrideReason =
				(AssessmentCategoryOverrideReason) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						assessmentCategoryOverride)
				.setParameter(CATEGORY_OVERRIDE_REASON_PARAM_NAME, 
						categoryOverrideReason)
				.uniqueResult();
		return assessmentCategoryOverrideReason;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverrideReason findExcluding(
			final AssessmentCategoryOverride assessmentCategoryOverride,
			final CategoryOverrideReason categoryOverrideReason,
			final AssessmentCategoryOverrideReason 
					excludedAssessmentCategoryOverrideReason) {
		AssessmentCategoryOverrideReason assessmentCategoryOverrideReason =
				(AssessmentCategoryOverrideReason) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						assessmentCategoryOverride)
				.setParameter(CATEGORY_OVERRIDE_REASON_PARAM_NAME, 
						categoryOverrideReason)
				.setParameter(
						EXCLUDED_ASSESSMENT_CATEGORY_OVERRIDE_REASON_PARAM_NAME,
						excludedAssessmentCategoryOverrideReason)
				.uniqueResult();
		return assessmentCategoryOverrideReason;
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentCategoryOverrideReason> 
			findByAssessmentCategoryOverride(
					final AssessmentCategoryOverride assessmentCategoryOverride) {
		@SuppressWarnings("unchecked")
		List<AssessmentCategoryOverrideReason> reasons = this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_ASSESSMENT_CATEGORY_OVERRIDE_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						assessmentCategoryOverride)
				.list();
		return reasons;
	}
}