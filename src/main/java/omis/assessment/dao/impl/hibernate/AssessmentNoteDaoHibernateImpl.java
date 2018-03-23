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
import omis.assessment.dao.AssessmentNoteDao;
import omis.assessment.domain.AssessmentNote;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Note DAO Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 15, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentNoteDaoHibernateImpl extends
		GenericHibernateDaoImpl<AssessmentNote> implements AssessmentNoteDao {
	
	private static final String FIND_QUERY_NAME = "findAssessmentNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findAssessmentNoteExcluding";
	
	private static final String FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME =
			"findAssessmentNotesByAdministeredQuestionnaire";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_NOTE_PARAM_NAME = "assessmentNote";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String entity name
	 */
	public AssessmentNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentNote find(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final String description, final Date date) {
		AssessmentNote assessmentNote =
				(AssessmentNote) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
						administeredQuestionnaire)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setTimestamp(DATE_PARAM_NAME, date)
				.uniqueResult();
		
		return assessmentNote;
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentNote findExcluding(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final String description, final Date date,
			final AssessmentNote assessmentNoteExcluding) {
		AssessmentNote assessmentNote =
				(AssessmentNote) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
						administeredQuestionnaire)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(ASSESSMENT_NOTE_PARAM_NAME,
						assessmentNoteExcluding)
				.uniqueResult();
		
		return assessmentNote;
	}

	/**{@inheritDoc} */
	@Override
	public List<AssessmentNote> findByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<AssessmentNote> assessmentNotes = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
						administeredQuestionnaire)
				.list();
		
		return assessmentNotes;
	}
}
