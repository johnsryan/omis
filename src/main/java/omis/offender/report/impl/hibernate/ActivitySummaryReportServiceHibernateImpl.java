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
package omis.offender.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.offender.report.ActivitySummary;
import omis.offender.report.ActivitySummaryReportService;
import omis.offender.report.delegate.hibernate.BasicInformationActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.CaseManagementActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.ComplianceActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.HealthActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.LegalActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.PlacementActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.RelationshipsActivitySummaryReportDelegate;
import omis.offender.report.delegate.hibernate.SafetyActivitySummaryReportDelegate;

/**
 * Activity Summary Report Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ActivitySummaryReportServiceHibernateImpl
		implements ActivitySummaryReportService {
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME =
			"findActivitySummariesByOffender";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private final BasicInformationActivitySummaryReportDelegate
		basicInformationActivitySummaryReportDelegate;
	
	private final PlacementActivitySummaryReportDelegate
		placementActivitySummaryReportDelegate;
	
	private final LegalActivitySummaryReportDelegate
		legalActivitySummaryReportDelegate;
	
	private final CaseManagementActivitySummaryReportDelegate
		caseManagementActivitySummaryReportDelegate;
	
	private final SafetyActivitySummaryReportDelegate
		safetyActivitySummaryReportDelegate;
	
	private final ComplianceActivitySummaryReportDelegate
		complianceActivitySummaryReportDelegate;
	
	private final HealthActivitySummaryReportDelegate
		healthActivitySummaryReportDelegate;
	
	private final RelationshipsActivitySummaryReportDelegate
		relationshipsActivitySummaryReportDelegate;
	
	private final SessionFactory sessionFactory;

	/**
	 * @param basicInformationActivitySummaryReportDelegate - Basic Information
	 * Activity Summary Report Delegate
	 * @param placementActivitySummaryReportDelegate - Placement Activity
	 * Summary Report Delegate
	 * @param legalActivitySummaryReportDelegate - Legal Activity Summary
	 * Report Delegate
	 * @param caseManagementActivitySummaryReportDelegate - Case Management
	 * Activity Summary Report Delegate
	 * @param safetyActivitySummaryReportDelegate - Safety Activity Summary
	 * Report Delegate
	 * @param complianceActivitySummaryReportDelegate - Compliance Activity
	 * Summary Report Delegate
	 * @param healthActivitySummaryReportDelegate - Health Activity Summary
	 * Report Delegate
	 * @param relationshipsActivitySummaryReportDelegate - Relationships
	 * Activity Summary Report Delegate
	 * @param sessionFactory - Session Factory
	 */
	public ActivitySummaryReportServiceHibernateImpl(
			final BasicInformationActivitySummaryReportDelegate
				basicInformationActivitySummaryReportDelegate,
			final PlacementActivitySummaryReportDelegate
				placementActivitySummaryReportDelegate,
			final LegalActivitySummaryReportDelegate
				legalActivitySummaryReportDelegate,
			final CaseManagementActivitySummaryReportDelegate
				caseManagementActivitySummaryReportDelegate,
			final SafetyActivitySummaryReportDelegate
				safetyActivitySummaryReportDelegate,
			final ComplianceActivitySummaryReportDelegate
				complianceActivitySummaryReportDelegate,
			final HealthActivitySummaryReportDelegate
				healthActivitySummaryReportDelegate,
			final RelationshipsActivitySummaryReportDelegate
				relationshipsActivitySummaryReportDelegate,
			final SessionFactory sessionFactory) {
		this.basicInformationActivitySummaryReportDelegate =
				basicInformationActivitySummaryReportDelegate;
		this.placementActivitySummaryReportDelegate =
				placementActivitySummaryReportDelegate;
		this.legalActivitySummaryReportDelegate =
				legalActivitySummaryReportDelegate;
		this.caseManagementActivitySummaryReportDelegate =
				caseManagementActivitySummaryReportDelegate;
		this.safetyActivitySummaryReportDelegate =
				safetyActivitySummaryReportDelegate;
		this.complianceActivitySummaryReportDelegate =
				complianceActivitySummaryReportDelegate;
		this.healthActivitySummaryReportDelegate =
				healthActivitySummaryReportDelegate;
		this.relationshipsActivitySummaryReportDelegate =
				relationshipsActivitySummaryReportDelegate;
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ActivitySummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		
		return summaries;
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary>
		findBasicInformationActivitySummariesByOffender(
			final Offender offender) {
		return this.basicInformationActivitySummaryReportDelegate
				.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findPlacementActivitySummariesByOffender(
			final Offender offender) {
		return this.placementActivitySummaryReportDelegate
				.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findLegalActivitySummariesByOffender(
			final Offender offender) {
		return this.legalActivitySummaryReportDelegate.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findCaseManagementActivitySummariesByOffender(
			final Offender offender) {
		return this.caseManagementActivitySummaryReportDelegate
				.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findSafetyActivitySummariesByOffender(
			final Offender offender) {
		return this.safetyActivitySummaryReportDelegate
				.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findComplianceActivitySummariesByOffender(
			final Offender offender) {
		return this.complianceActivitySummaryReportDelegate
				.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findHealthActivitySummariesByOffender(
			final Offender offender) {
		return this.healthActivitySummaryReportDelegate
				.findByOffender(offender);
	}

	/**{@inheritDoc} */
	@Override
	public List<ActivitySummary> findRelationshipsActivitySummariesByOffender(
			final Offender offender) {
		return this.relationshipsActivitySummaryReportDelegate
				.findByOffender(offender);
	}
}