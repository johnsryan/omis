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
package omis.mentalhealthreview.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.mentalhealthreview.dao.MentalHealthNoteDao;
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;

/**
 * Hibernate implementation of the mental health note data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthNoteDaoHibernateImpl 
		extends GenericHibernateDaoImpl<MentalHealthNote>
		implements MentalHealthNoteDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findMentalHealthNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findMentalHealthNoteExcluding";
	
	private static final String FIND_BY_MENTAL_HEALTH_REVIEW_QUERY_NAME = 
			"findMentalHealthNotesByMentalHealthReview";
	
	/* Parameters. */
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String MENTAL_HEALTH_REVIEW_PARAM_NAME = 
			"mentalHealthReview";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedMentalHealthNote";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  mental health note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public MentalHealthNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public MentalHealthNote find(final MentalHealthReview mentalHealthReview, 
			final Date date, final String description) {
		MentalHealthNote mentalHealthNote = (MentalHealthNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(MENTAL_HEALTH_REVIEW_PARAM_NAME, 
						mentalHealthReview)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.uniqueResult();
		return mentalHealthNote;
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthNote findExcluding(
			final MentalHealthReview mentalHealthReview, final Date date, 
			final String description,
			final MentalHealthNote excludedMentalHealthNote) {
		MentalHealthNote mentalHealthNote = (MentalHealthNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(MENTAL_HEALTH_REVIEW_PARAM_NAME, 
						mentalHealthReview)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(EXCLUDED_PARAM_NAME, excludedMentalHealthNote)
				.uniqueResult();
		return mentalHealthNote;
	}

	/** {@inheritDoc} */
	@Override
	public List<MentalHealthNote> findByMentalHealthReview(
			final MentalHealthReview mentalHealthReview) {
		@SuppressWarnings("unchecked")
		List<MentalHealthNote> notes = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_MENTAL_HEALTH_REVIEW_QUERY_NAME)
				.setParameter(MENTAL_HEALTH_REVIEW_PARAM_NAME, 
						mentalHealthReview)
				.list();
		return notes;
	}
}