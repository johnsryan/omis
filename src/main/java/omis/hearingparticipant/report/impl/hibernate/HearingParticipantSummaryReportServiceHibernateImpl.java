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
package omis.hearingparticipant.report.impl.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import omis.boardhearing.domain.BoardHearing;
import omis.hearingparticipant.report.HearingParticipantSummary;
import omis.hearingparticipant.report.HearingParticipantSummaryReportService;

/**
 * Hearing Participant Summary Report Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 *
 */
public class HearingParticipantSummaryReportServiceHibernateImpl
		implements HearingParticipantSummaryReportService {
	
	private static final String FIND_BY_HEARING_QUERY_NAME =
			"findHearingParticipantSummariesByHearing";
	
	private static final String BOARD_HEARING_PARAM_NAME = "boardHearing";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public HearingParticipantSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**{@inheritDoc} */
	@Override
	public List<HearingParticipantSummary>
		findHearingParticipantSummariesByHearing(
				final BoardHearing boardHearing) {
		@SuppressWarnings("unchecked")
		List<HearingParticipantSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(FIND_BY_HEARING_QUERY_NAME)
				.setParameter(BOARD_HEARING_PARAM_NAME, boardHearing)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}
}