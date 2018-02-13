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
package omis.parolereview.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.parolereview.dao.ParoleReviewNoteDao;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewNote;

/**
 * Hibernate implementation of the parole review note data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewNoteDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParoleReviewNote>
		implements ParoleReviewNoteDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findParoleReviewNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParoleReviewNoteExcluding";
	
	private static final String FIND_BY_PAROLE_REVIEW_QUERY_NAME = 
			"findParoleReviewNotesByParoleReview";
	
	/* Parameters. */
	
	private static final String PAROLE_REVIEW_PARAM_NAME = "paroleReview";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedParoleReviewNote";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  parole review note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParoleReviewNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReviewNote find(final ParoleReview paroleReview, 
			final Date date, final String description) {
		ParoleReviewNote note = (ParoleReviewNote) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(PAROLE_REVIEW_PARAM_NAME, paroleReview)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.uniqueResult();
		return note;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReviewNote findExcluding(final ParoleReview paroleReview, 
			final Date date, final String description, 
			final ParoleReviewNote excludedParoleReviewNote) {
		ParoleReviewNote note = (ParoleReviewNote) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(PAROLE_REVIEW_PARAM_NAME, paroleReview)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(EXCLUDED_PARAM_NAME, excludedParoleReviewNote)
				.uniqueResult();
		return note;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleReviewNote> findByParoleReview(
			final ParoleReview paroleReview) {
		@SuppressWarnings("unchecked")
		List<ParoleReviewNote> notes = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_PAROLE_REVIEW_QUERY_NAME)
				.setParameter(PAROLE_REVIEW_PARAM_NAME, paroleReview)
				.list();
		return notes;
	}
}