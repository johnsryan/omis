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

import omis.assessment.dao.RatingNoteDao;
import omis.assessment.domain.RatingNote;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Hibernate implementation of the rating note data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class RatingNoteDaoHibernateImpl 
		extends GenericHibernateDaoImpl<RatingNote> implements RatingNoteDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findRatingNote";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findRatingNoteExcluding";
	
	private final static String FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME = 
			"findRatingNotesByAdministeredQuestionnaire";
	
	/* Parameters. */
	
	private final static String DATE_PARAM_NAME = "date";
	
	private final static String DESCRIPTION_PARAM_NAME = "description";
	
	private final static String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME = 
			"administeredQuestionnaire";
	
	private final static String EXCLUDED_RATING_CATEGORY_PARAM_NAME = 
			"excludedRatingNote";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * rating note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public RatingNoteDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public RatingNote find(final Date date, final String description, 
			final AdministeredQuestionnaire administeredQuestionnaire) {
		RatingNote ratingNote = (RatingNote) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.uniqueResult();
		return ratingNote;
	}

	/** {@inheritDoc} */
	@Override
	public RatingNote findExcluding(final Date date, final String description, 
			final AdministeredQuestionnaire administeredQuestionnaire,
			final RatingNote excludedRatingNote) {
		RatingNote ratingNote = (RatingNote) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.setParameter(EXCLUDED_RATING_CATEGORY_PARAM_NAME, 
						excludedRatingNote)
				.uniqueResult();
		return ratingNote;
	}

	/** {@inheritDoc} */
	@Override
	public List<RatingNote> findByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<RatingNote> ratingNotes = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.list();
		return ratingNotes;
	}
}