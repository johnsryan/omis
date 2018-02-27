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
package omis.hearing.report.impl.hibernate;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.hearing.domain.Hearing;
import omis.hearing.report.HearingSummary;
import omis.hearing.report.ViolationSummary;
import omis.hearing.report.ViolationSummaryReportService;
import omis.offender.domain.Offender;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;

/**
 * ViolationSummaryReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ViolationSummaryReportServiceHibernateImpl
	implements ViolationSummaryReportService {
	
	
	private static final String
		FIND_UNRESOLVED_DISCIPLINARY_VIOLATION_SUMMARIES_QUERY_NAME =
				"findUnresolvedDisciplinaryViolationSummaries";
	
	private static final String
		FIND_UNRESOLVED_CONDITION_VIOLATION_SUMMARIES_QUERY_NAME =
				"findUnresolvedConditionViolationSummaries";
	
	
	private static final String
		FIND_RESOLVED_CONDITION_VIOLATION_SUMMARIES_QUERY_NAME =
			"findResolvedConditionViolationSummaries";
	
	private static final String
		FIND_RESOLVED_DISCIPLINARY_CODE_VIOLATION_SUMMARIES_QUERY_NAME =
			"findResolvedDisciplinaryCodeViolationSummaries";
	
	private static final String
		FIND_UNADJUDICATED_HEARINGS_BY_OFFENDER_QUERY_NAME =
			"findUnadjudicatedHearingsByOffender";
	
	private static final String
		FIND_UNRESOLVED_DISCIPLINARY_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME =
			"findUnresolvedDisciplinaryViolationSummariesByHearing";
	
	private static final String
		FIND_UNRESOLVED_CONDITION_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME =
			"findUnresolvedConditionViolationSummariesByHearing";
	
	private static final String
		FIND_DISCIPLINARY_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME =
			"findDisciplinaryViolationSummariesByHearing";

private static final String
		FIND_CONDITION_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME =
			"findConditionViolationSummariesByHearing";
	
	private static final String SUMMARIZE_CONDITION_VIOLATION_QUERY_NAME =
			"summarizeConditionViolation";
	
	private static final String SUMMARIZE_DISCIPLINARY_CODE_VIOLATION_QUERY_NAME =
			"summarizeDisciplinaryCodeViolation";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String CONDITION_VIOLATION_PARAM_NAME =
			"conditionViolation";
	
	private static final String DISCIPLINARY_CODE_VIOLATION_PARAM_NAME =
			"disciplinaryCodeViolation";
	
	private static final String HEARING_ID_PARAM_NAME = "hearingId";
	
	private static final String HEARING_PARAM_NAME = "hearing";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory
	 */
	public ViolationSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<ViolationSummary> findUnresolvedViolationSummariesByOffender(
			final Offender offender) {
		List<ViolationSummary> summaries =
			this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
					FIND_UNRESOLVED_DISCIPLINARY_VIOLATION_SUMMARIES_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		
		summaries.addAll(this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_UNRESOLVED_CONDITION_VIOLATION_SUMMARIES_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list());
		
		return summaries;
	}
	
	/**{@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<HearingSummary, List<ViolationSummary>>
			findScheduledViolationSummariesByOffender(final Offender offender) {
		
		HashMap<HearingSummary, List<ViolationSummary>> summaries =
				new HashMap<HearingSummary, List<ViolationSummary>>();
		
		List<HearingSummary> hearingSummaries =
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_UNADJUDICATED_HEARINGS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		
		for(HearingSummary hearingSummary : hearingSummaries){
			List<ViolationSummary> violationSummaries =
					this.sessionFactory.getCurrentSession()
					.getNamedQuery(
					FIND_UNRESOLVED_DISCIPLINARY_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME)
					.setParameter(HEARING_ID_PARAM_NAME,
							hearingSummary.getHearingId())
					.setReadOnly(true)
					.list();
			violationSummaries.addAll(
					this.sessionFactory.getCurrentSession()
					.getNamedQuery(
				FIND_UNRESOLVED_CONDITION_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME)
					.setParameter(HEARING_ID_PARAM_NAME,
							hearingSummary.getHearingId())
					.setReadOnly(true)
					.list());
			
			summaries.put(hearingSummary, violationSummaries);
		}
		
		return summaries;
	}
	
	/**{@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<ViolationSummary> findAllViolationSummariesByHearing(
			final Hearing hearing) {
		List<ViolationSummary> summaries =
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(
					FIND_DISCIPLINARY_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing)
				.setReadOnly(true)
				.list();
		summaries.addAll(
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_CONDITION_VIOLATION_SUMMARIES_BY_HEARING_QUERY_NAME)
				.setParameter(HEARING_PARAM_NAME, hearing)
				.setReadOnly(true)
				.list());
		
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationSummary>
		findUnresolvedDisciplinaryViolationSummariesByOffender(
				final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ViolationSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
					FIND_UNRESOLVED_DISCIPLINARY_VIOLATION_SUMMARIES_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationSummary>
		findUnresolvedConditionViolationSummariesByOffender(
				final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ViolationSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
					FIND_UNRESOLVED_CONDITION_VIOLATION_SUMMARIES_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		
		return summaries;
	}

	/**{@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<ViolationSummary> findResolvedViolationSummariesByOffender(
			final Offender offender) {
		List<ViolationSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_RESOLVED_DISCIPLINARY_CODE_VIOLATION_SUMMARIES_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		summaries.addAll(this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_RESOLVED_CONDITION_VIOLATION_SUMMARIES_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list());
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public ViolationSummary summarize(final ConditionViolation conditionViolation) {
		ViolationSummary summary = (ViolationSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_CONDITION_VIOLATION_QUERY_NAME)
				.setParameter(CONDITION_VIOLATION_PARAM_NAME, conditionViolation)
				.setReadOnly(true)
				.uniqueResult();
		
		return summary;
	}

	/**{@inheritDoc} */
	@Override
	public ViolationSummary summarize(
			final DisciplinaryCodeViolation disciplinaryCodeViolation) {
		ViolationSummary summary = (ViolationSummary) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_DISCIPLINARY_CODE_VIOLATION_QUERY_NAME)
				.setParameter(DISCIPLINARY_CODE_VIOLATION_PARAM_NAME,
						disciplinaryCodeViolation)
				.setReadOnly(true)
				.uniqueResult();
		
		return summary;
	}
}