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
package omis.chronologicalnote.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.chronologicalnote.dao.ChronologicalNoteDao;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note.
 * 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 31, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNote>
		implements ChronologicalNoteDao {
	/* Query names. */
	private static final String FIND_BY_OFFENDER_QUERY_NAME
		= "findByOffender";
	private static final String FIND_QUERY_NAME	= "findChronologicalNote";
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findExcludingChronologicalNote";

	/* Parameters. */
	private static final String OFFENDER_PARAM_NAME = "offender";
	private static final String DATE_PARAM_NAME = "date";
	private static final String TITLE_PARAM_NAME = "title";
	private static final String CHRONOLOGICAL_NOTE_PARAM_NAME
		= "chronologicalNote";
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * chronological note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ChronologicalNoteDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNote> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNote> notes = this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.list();
		return notes;
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNote find(final Date date, final Offender offender,
		final String title) {
		ChronologicalNote note = (ChronologicalNote) this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setParameter(DATE_PARAM_NAME, date)
			.setParameter(TITLE_PARAM_NAME, title)
			.uniqueResult();
		return note;
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNote findExcluding(final ChronologicalNote note,
		final Date date, final Offender offender, final String title) {
		ChronologicalNote chronologicalNote
			= (ChronologicalNote) this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setParameter(DATE_PARAM_NAME, date)
			.setParameter(TITLE_PARAM_NAME, title)
			.setParameter(CHRONOLOGICAL_NOTE_PARAM_NAME, note)
			.uniqueResult();
		return chronologicalNote;
	}
	
}