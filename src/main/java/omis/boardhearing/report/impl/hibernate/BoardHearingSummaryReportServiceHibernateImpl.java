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
package omis.boardhearing.report.impl.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.report.BoardHearingSummary;
import omis.boardhearing.report.BoardHearingSummaryReportService;
import omis.offender.domain.Offender;

/**
 * Board Hearing Summary Report Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingSummaryReportServiceHibernateImpl
		implements BoardHearingSummaryReportService {
	
	private static final String FIND_SUMMARIES_BY_OFFENDER_QUERY_NAME =
			"findBoardHearingSummariesByOffender";
	
	private static final String SUMMARIZE_QUERY_NAME = "summarizeBoardHearing";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String BOARD_HEARING_PARAM_NAME = "boardHearing";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public BoardHearingSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**{@inheritDoc} */
	@Override
	public List<BoardHearingSummary> findBoardHearingSummariesByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<BoardHearingSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_SUMMARIES_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public BoardHearingSummary summarize(final BoardHearing boardHearing) {
		BoardHearingSummary summary = (BoardHearingSummary)
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_QUERY_NAME)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing)
				.setReadOnly(true)
				.uniqueResult();
		
		return summary;
	}
}