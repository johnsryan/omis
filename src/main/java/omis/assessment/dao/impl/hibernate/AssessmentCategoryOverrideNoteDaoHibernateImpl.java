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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.assessment.dao.AssessmentCategoryOverrideNoteDao;
import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideNote;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

/**
 * Hibernate implementation of the assessment category override note data access 
 * object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideNoteDaoHibernateImpl 
		extends GenericHibernateDaoImpl<AssessmentCategoryOverrideNote>
		implements AssessmentCategoryOverrideNoteDao {
	
	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findAssessmentCategoryOverrideNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findAssessmentCategoryOverrideNoteExcluding";
	
	private static final String FIND_BY_ASSESSMENT_CATEGORY_OVERRIDE_QUERY_NAME = 
			"findAssessmentCategoryOverrideNotesByAssessmentCategoryOverride";
	
	/* Parameters. */
	
	private static final String ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME = 
			"assessmentCategoryOverride";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedAssessmentCategoryOverrideNote";
	
	/** Instantiates a hibernate implementation of the data access object for 
	*  assessment category override.
	* 
	* @param sessionFactory session factory
	* @param entityName entity name
	*/
	public AssessmentCategoryOverrideNoteDaoHibernateImpl(
		final SessionFactory sessionFactory, final String entityName) {
	super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverrideNote find(
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final String description, final Date date) {
		AssessmentCategoryOverrideNote assessmentCategoryOverrideNote = 
				(AssessmentCategoryOverrideNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						assessmentCategoryOverride)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.uniqueResult();
		return assessmentCategoryOverrideNote;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverrideNote findExcluding(
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final String description, final Date date,
			final AssessmentCategoryOverrideNote 
					excludedAssessmentCategoryOverrideNote) {
		AssessmentCategoryOverrideNote assessmentCategoryOverrideNote = 
				(AssessmentCategoryOverrideNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						assessmentCategoryOverride)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(EXCLUDED_PARAM_NAME, 
						excludedAssessmentCategoryOverrideNote)
				.uniqueResult();
		return assessmentCategoryOverrideNote;
	}

	/** {@inheritDoc} */
	@Override
	public List<AssessmentCategoryOverrideNote> 
			findByAssessmentCategoryOverride(
					final AssessmentCategoryOverride assessmentCategoryOverride) {
		@SuppressWarnings("unchecked")
		List<AssessmentCategoryOverrideNote> notes = this.getSessionFactory()
		.getCurrentSession()
				.getNamedQuery(FIND_BY_ASSESSMENT_CATEGORY_OVERRIDE_QUERY_NAME)
				.setParameter(ASSESSMENT_CATEGORY_OVERRIDE_PARAM_NAME, 
						assessmentCategoryOverride)
				.list();
		return notes;
	}
}