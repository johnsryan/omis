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
package omis.boardconsideration.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.boardconsideration.dao.BoardConsiderationNoteDao;
import omis.boardconsideration.domain.BoardConsiderationNote;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Hibernate implementation of the board consideration note data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationNoteDaoHibernateImpl 
		extends GenericHibernateDaoImpl<BoardConsiderationNote> 
		implements BoardConsiderationNoteDao {

/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findBoardConsiderationNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findBoardConsiderationNoteExcluding";
	
	private static final String FIND_BY_HEARING_ANALYSIS_QUERY_NAME = 
			"findBoardConsiderationNotesByHearingAnalysis";
	
	/* Parameters. */
	
	private static final String HEARING_ANALYSIS_PARAM_NAME = "hearingAnalysis";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String EXCLUDED_BOARD_CONSIDERATION_NOTE_PARAM_NAME = 
			"excludedBoardConsiderationNote";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  board consideration note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public BoardConsiderationNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardConsiderationNote> findByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		@SuppressWarnings("unchecked")
		List<BoardConsiderationNote> boardConsiderationNotes = this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_HEARING_ANALYSIS_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis)
				.list();
		return boardConsiderationNotes;
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsiderationNote find(final HearingAnalysis hearingAnalysis, 
			final String description, final Date date) {
		BoardConsiderationNote boardConsiderationNote = 
				(BoardConsiderationNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(DATE_PARAM_NAME, date)
				.uniqueResult();
		return boardConsiderationNote;
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsiderationNote findExcluding(
			final HearingAnalysis hearingAnalysis, final String description, 
			final Date date,
			final BoardConsiderationNote excludedBoardConsiderationNote) {
		BoardConsiderationNote boardConsiderationNote = 
				(BoardConsiderationNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(EXCLUDED_BOARD_CONSIDERATION_NOTE_PARAM_NAME, 
						excludedBoardConsiderationNote)
				.uniqueResult();
		return boardConsiderationNote;
	}
}