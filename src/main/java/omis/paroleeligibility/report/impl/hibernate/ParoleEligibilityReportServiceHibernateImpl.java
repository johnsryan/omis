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
package omis.paroleeligibility.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.service.delegate.HearingAnalysisDelegate;
import omis.offender.domain.Offender;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.report.ParoleEligibilityReportService;
import omis.paroleeligibility.report.ParoleEligibilitySummary;

/**
 * Hibernate implementation of the parole eligibility report service.
 *
 * @author Trevor Isles
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.5 (Jul 10, 2018)
 * @since OMIS 3.0
 */
public class ParoleEligibilityReportServiceHibernateImpl 
		implements ParoleEligibilityReportService {
	
	/* Queries. */
	
	private static final String FIND_PAROLE_ELIGIBILITIES_BY_OFFENDER_QUERY_NAME = 
			"findParoleEligibilitiesByOffender";
	
	private static final String FIND_UNRESOLVED_PAROLE_ELIGIBILITIES_QUERY_NAME = 
			"findUnresolvedParoleEligibilities";
	
	private static final String FIND_UNSCHEDULED_PAROLE_ELIGIBILITIES_QUERY_NAME
			= "findUnscheduledParoleEligibilitySummaries";
	
	private static final String SUMMARIZE_PAROLE_ELIGIBILITY_QUERY_NAME =
			"summarizeParoleEligibility";
	
	private static final String FIND_PAROLE_ELIGIBILITIES_BY_ITINERARY_QUERY_NAME = 
			"findParoleEligibilitiesByItinerary";
	
	private static final String 
			FIND_UNRESOLVED_PAROLE_ELIGIBILITIES_BY_DATE_RANGE_QUERY_NAME = 
			"findUnresolvedParoleEligibilitiesByDateRange";
	
	/* Parameters.*/ 
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String PAROLE_ELIGIBILITY_PARAM_NAME =
			"paroleEligibility";
	
	private static final String ITINERARY_PARAM_NAME = "itinerary";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
		
	/* Members. */
	
	private final SessionFactory sessionFactory;

	/* Delegates. */
	
	private final HearingAnalysisDelegate hearingAnalysisDelegate;
	
	private final BoardHearingDelegate boardHearingDelegate;
	
	/**
	 * Constructor.
	 * 
	 * @param sessionFactory session factory
	 */
	public ParoleEligibilityReportServiceHibernateImpl(
			final SessionFactory sessionFactory,
			final HearingAnalysisDelegate hearingAnalysisDelegate,
			final BoardHearingDelegate boardHearingDelegate) {
		this.sessionFactory = sessionFactory;
		this.hearingAnalysisDelegate = hearingAnalysisDelegate;
		this.boardHearingDelegate = boardHearingDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ParoleEligibilitySummary> findByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ParoleEligibilitySummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_PAROLE_ELIGIBILITIES_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis findHearingAnalysisByParoleEligibility(
			final ParoleEligibility eligibility) {
		return this.hearingAnalysisDelegate.findByParoleEligibility(
				eligibility);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleEligibilitySummary> findUnresolvedEligibilitySummaries() {
		@SuppressWarnings("unchecked")
		List<ParoleEligibilitySummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_UNRESOLVED_PAROLE_ELIGIBILITIES_QUERY_NAME)
				.setReadOnly(true)
				.list();
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ParoleEligibilitySummary> findUnscheduledEligibilitySummaries() {
		@SuppressWarnings("unchecked")
		List<ParoleEligibilitySummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_UNSCHEDULED_PAROLE_ELIGIBILITIES_QUERY_NAME)
				.setReadOnly(true)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public BoardHearing findBoardHearingByParoleEligibility(
			final ParoleEligibility eligibility) {
		return this.boardHearingDelegate.findByParoleEligibility(eligibility);
	}

	/**{@inheritDoc} */
	@Override
	public ParoleEligibilitySummary summarizeParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		ParoleEligibilitySummary summary = (ParoleEligibilitySummary)
				this.sessionFactory.getCurrentSession().getNamedQuery(
						SUMMARIZE_PAROLE_ELIGIBILITY_QUERY_NAME)
				.setParameter(PAROLE_ELIGIBILITY_PARAM_NAME, paroleEligibility)
				.setReadOnly(true)
				.uniqueResult();
		return summary;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleEligibilitySummary> findByItinerary(
			final ParoleBoardItinerary itinerary) {
		@SuppressWarnings("unchecked")
		List<ParoleEligibilitySummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_PAROLE_ELIGIBILITIES_BY_ITINERARY_QUERY_NAME)
				.setParameter(ITINERARY_PARAM_NAME, itinerary)
				.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleEligibilitySummary> 
			findUnresolvedEligibilitySummariesByDateRange(final Date startDate, 
					final Date endDate) {
		@SuppressWarnings("unchecked")
		List<ParoleEligibilitySummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_UNRESOLVED_PAROLE_ELIGIBILITIES_BY_DATE_RANGE_QUERY_NAME)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.list();
		return summaries;
	}
}