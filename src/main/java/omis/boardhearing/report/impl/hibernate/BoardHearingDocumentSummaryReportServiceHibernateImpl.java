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
import omis.boardhearing.report.BoardHearingDocumentSummary;
import omis.boardhearing.report.BoardHearingDocumentSummaryReportService;

/**
 * Board hearing document summary report service hibernate implementation.
 *
 * @author Trevor Isles
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingDocumentSummaryReportServiceHibernateImpl 
	implements BoardHearingDocumentSummaryReportService {
	
	private static final String FIND_DOCUMENT_SUMMARIES_BY_HEARING_QUERY_NAME
		= "findBoardHearingDocumentSummariesByHearing";
	
	private static final String BOARD_HEARING_PARAM_NAME = "boardHearing";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory
	 */
	public BoardHearingDocumentSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<BoardHearingDocumentSummary> 
		findBoardHearingDocumentSummariesByHearing(
			final BoardHearing boardHearing) {
		@SuppressWarnings("unchecked")
		List<BoardHearingDocumentSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_DOCUMENT_SUMMARIES_BY_HEARING_QUERY_NAME)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing)
				.list();
		
		return summaries;
	}

}
