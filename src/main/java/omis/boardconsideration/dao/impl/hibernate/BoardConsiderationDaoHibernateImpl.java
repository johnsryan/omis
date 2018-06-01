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

import java.util.List;

import org.hibernate.SessionFactory;

import omis.boardconsideration.dao.BoardConsiderationDao;
import omis.boardconsideration.domain.BoardConsideration;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Hibernate implementation of the board consideration data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationDaoHibernateImpl 
		extends GenericHibernateDaoImpl<BoardConsideration> 
		implements BoardConsiderationDao {

/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findBoardConsideration";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findBoardConsiderationExcluding";
	
	private static final String FIND_BY_HEARING_ANALYSIS_QUERY_NAME = 
			"findBoardConsiderationsByHearingAnalysis";
	
	/* Parameters. */
	
	private static final String HEARING_ANALYSIS_PARAM_NAME = "hearingAnalysis";
	
	private static final String TITLE_PARAM_NAME = "title";
	
	private static final String EXCLUDED_BOARD_CONSIDERATION_PARAM_NAME = 
			"excludedBoardConsideration";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  board consideration.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public BoardConsiderationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardConsideration> findByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		@SuppressWarnings("unchecked")
		List<BoardConsideration> boardConsiderations = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_HEARING_ANALYSIS_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis)
				.list();
		return boardConsiderations;
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsideration find(final HearingAnalysis hearingAnalysis, 
			final String title) {
		BoardConsideration boardConsideration = (BoardConsideration) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis)
				.setParameter(TITLE_PARAM_NAME, title)
				.uniqueResult();
		return boardConsideration;
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsideration findExcluding(
			final HearingAnalysis hearingAnalysis, final String title,
			final BoardConsideration excludedBoardConsideration) {
		BoardConsideration boardConsideration = (BoardConsideration) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(HEARING_ANALYSIS_PARAM_NAME, hearingAnalysis)
				.setParameter(TITLE_PARAM_NAME, title)
				.setParameter(EXCLUDED_BOARD_CONSIDERATION_PARAM_NAME, 
						excludedBoardConsideration)
				.uniqueResult();
		return boardConsideration;
	}
}