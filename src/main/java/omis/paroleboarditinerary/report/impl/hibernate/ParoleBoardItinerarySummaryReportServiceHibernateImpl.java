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
package omis.paroleboarditinerary.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.report.BoardAttendeeSummary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummary;
import omis.paroleboarditinerary.report.ParoleBoardItinerarySummaryReportService;

/**
 * Hibernate implementation of the parole board itinerary summary report 
 * service.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItinerarySummaryReportServiceHibernateImpl 
		implements ParoleBoardItinerarySummaryReportService {

	/* Queries. */
	
	private static final String FIND_BOARD_ITINERARIES_BY_DATE_QUERY_NAME =
			"findParoleBoardItinerariesByEffectiveDate";
	
	private static final String FIND_BOARD_ITINERARIES_BY_DATE_RANGE_QUERY_NAME 
			= "findParoleBoardItinerariesByDateRange";
	
	private static final String SUMMARIZE_BY_BOARD_ITINERARY_QUERY_NAME = 
			"findParoleBoardItinerarySummaryByParoleBoardItinerary";
	
	private static final String 
			FIND_BOARD_ATTENDEES_BY_BOARD_ITINERARY_QUERY_NAME =
					"findParoleBoardAttendeeSummariesByParoleBoardItinerary";
	
	/* Parameters.*/ 
	
	private static final String EFFECTIVE_DATE_PARAMETER_NAME = "effectiveDate";
	
	private static final String START_DATE_PARAMETER_NAME = "startDate";
	
	private static final String END_DATE_PARAMETER_NAME = "endDate";
	
	private static final String BOARD_ITINERARY_PARAMETER_NAME =
			"boardItinerary";
	
	/* Members. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public ParoleBoardItinerarySummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardItinerary> findParoleBoardItineraryByEffectiveDate(
			final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardItinerary> itineraries = 
				(List<ParoleBoardItinerary>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BOARD_ITINERARIES_BY_DATE_QUERY_NAME)
				.setTimestamp(EFFECTIVE_DATE_PARAMETER_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		return itineraries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardItinerary> findParoleBoardItineraryByDateRange(
			final Date startDate, final Date endDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardItinerary> itineraries = 
				(List<ParoleBoardItinerary>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BOARD_ITINERARIES_BY_DATE_RANGE_QUERY_NAME)
				.setTimestamp(START_DATE_PARAMETER_NAME, startDate)
				.setTimestamp(END_DATE_PARAMETER_NAME, endDate)
				.setReadOnly(true)
				.list();
		return itineraries;
	}
	
	/** {@inheritDoc} */
	@Override
	public ParoleBoardItinerarySummary summarize(
			final ParoleBoardItinerary paroleBoardItinerary) {
		ParoleBoardItinerarySummary summary = (ParoleBoardItinerarySummary) this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_BOARD_ITINERARY_QUERY_NAME)
				.setParameter(BOARD_ITINERARY_PARAMETER_NAME, 
						paroleBoardItinerary)
				.setReadOnly(true)
				.uniqueResult();
		return summary;
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardAttendeeSummary> 
			findParoleBoardAttendeeSummariesByParoleBoardItinerary(
					final ParoleBoardItinerary paroleBoardItinerary) {
		@SuppressWarnings("unchecked")
		List<BoardAttendeeSummary> summaries = (List<BoardAttendeeSummary>) this
				.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_BOARD_ATTENDEES_BY_BOARD_ITINERARY_QUERY_NAME)
				.setParameter(BOARD_ITINERARY_PARAMETER_NAME,
						paroleBoardItinerary)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}