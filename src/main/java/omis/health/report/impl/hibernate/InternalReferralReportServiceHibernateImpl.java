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
package omis.health.report.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.facility.domain.Facility;
import omis.health.report.InternalReferralReportService;
import omis.health.report.ResolvedInternalReferralSummary;
import omis.health.report.ScheduledInternalReferralSummary;
import omis.health.report.delegate.UnitLookUpDelegate;

/** 
 * Hibernate implementation of scheduled referrals.
 * 
 * @author Ryan Johns
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.1.2 (Feb 15, 2018)
 * @since OMIS 3.0 
 */
public class InternalReferralReportServiceHibernateImpl
		implements InternalReferralReportService {
	
	private static final String FACILITY_PARAM_NAME = "facility";

	private static final String
	FIND_SCHEDULED_INTERNAL_REFERRALS_SUMMARY_BY_FACILITY =
		"findScheduledInternalReferralSummaryByFacility";

	private static final String
	FIND_RESOLVED_INTERNAL_REFERRALS_SUMMARY_BY_FACILITY =
		"findResolvedInternalReferralSummaryByFacility";

	private final SessionFactory sessionFactory;
	
	private final UnitLookUpDelegate unitLookUpDelegate;

	/** Constructor.
	 * @param sessionFactory session factory. */
	public InternalReferralReportServiceHibernateImpl(
			final SessionFactory sessionFactory,
			final UnitLookUpDelegate unitLookUpDelegate) {
		this.sessionFactory = sessionFactory;
		this.unitLookUpDelegate = unitLookUpDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public List<ScheduledInternalReferralSummary> findScheduledInternalReferrals(
			final Facility facility) {
		@SuppressWarnings("unchecked")
		final List<ScheduledInternalReferralSummary> result =
				(List<ScheduledInternalReferralSummary>) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_SCHEDULED_INTERNAL_REFERRALS_SUMMARY_BY_FACILITY)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setReadOnly(true)
				.list();

		// Look away now... [SA]
		
		// TODO: Remove this when units can be looked up from OMIS 3.0 data
		final List<ScheduledInternalReferralSummary> summaries =
				new ArrayList<ScheduledInternalReferralSummary>();
		for (ScheduledInternalReferralSummary summary : result) {
			String unitName = this.unitLookUpDelegate
					.findUnitAbbreviationByOffenderNumber(
							summary.getOffenderNumber(), null);
			summaries.add(new ScheduledInternalReferralSummary(
				summary.getAppointmentId(),
				summary.getReferralId(),
				summary.getOffenderLastName(),
				summary.getOffenderMiddleName(),
				summary.getOffenderFirstName(),
				summary.getOffenderNumber(),
				summary.getScheduledDate(),
				summary.getProviderLastName(),
				summary.getProviderMiddleName(),
				summary.getProviderFirstName(),
				summary.getProviderTitleName(),
				unitName,
				summary.getNotes(),
				summary.getLocationDesignator(),
				summary.getReasonName()
			));
		}
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<ResolvedInternalReferralSummary> findResolvedInternalReferrals(
			final Facility facility) {
		@SuppressWarnings("unchecked")
		final List<ResolvedInternalReferralSummary> result =
			(List<ResolvedInternalReferralSummary>) this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_RESOLVED_INTERNAL_REFERRALS_SUMMARY_BY_FACILITY)
			.setParameter(FACILITY_PARAM_NAME, facility)
			.setReadOnly(true)
			.list();

		return result;
	}
}