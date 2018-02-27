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
package omis.supervisionfee.report.impl.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.supervisionfee.domain.MonthlySupervisionFee;
import omis.supervisionfee.report.SupervisionFeeRequirementSummary;
import omis.supervisionfee.report.SupervisionFeeSummary;
import omis.supervisionfee.report.SupervisionFeeSummaryReportService;

/**
 * Hibernate implementation of the supervision fee report service.
 * 
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class SupervisionFeeSummaryReportServiceHibernateImpl 
				implements SupervisionFeeSummaryReportService {

	/* Queries. */
	
	private static final String 
					FIND_SUPERVISION_FEE_REQUIREMENTS_BY_SUPERVISION_FEE
					= "findSupervisionFeeRequirementsBySupervisionFee";

	private static final String FIND_SUPERVISION_FEES_BY
					= "findSupervisionFeesBy";
	
	/* Parameters. */

	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String MONTHLY_SUPERVISION_FEE 
					= "monthlySupervisionFee";
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	/* Memebers. */

	private final SessionFactory sessionFactory;

	/**
	 * Constructor.
	 * @param sessionFactory session factory
	 */
	public SupervisionFeeSummaryReportServiceHibernateImpl(
					final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<SupervisionFeeSummary> findByOffender(final Offender offender,
			final Date effectiveDate) {
		List<MonthlySupervisionFee> supervisionFees;		
		List<SupervisionFeeSummary> supervisionFeeSummaries 
						= new ArrayList<SupervisionFeeSummary>();		
		List<SupervisionFeeRequirementSummary> feeRequirements 
							= new ArrayList<SupervisionFeeRequirementSummary>();
		supervisionFees = this.findSupervisionFeesBy(offender);		
		for (MonthlySupervisionFee fee : supervisionFees) {	
			feeRequirements = this
							.findSupervisionFeeRequirementsBySupervisionFee(
							fee, effectiveDate);
			supervisionFeeSummaries.add(
							new SupervisionFeeSummary(fee, feeRequirements, 
									effectiveDate));
		}	
		return supervisionFeeSummaries;
	}	

	/** {@inheritDoc} */
	@Override
	public List<SupervisionFeeRequirementSummary> 
					findSupervisionFeeRequirementsBySupervisionFee(
					final MonthlySupervisionFee monthlySupervisionFee,
					final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<SupervisionFeeRequirementSummary> list = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_SUPERVISION_FEE_REQUIREMENTS_BY_SUPERVISION_FEE)
				.setParameter(MONTHLY_SUPERVISION_FEE, monthlySupervisionFee)
				.setParameter(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		return list;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<MonthlySupervisionFee> findSupervisionFeesBy(
					final Offender offender) {
		@SuppressWarnings("unchecked")
		List<MonthlySupervisionFee> list = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_SUPERVISION_FEES_BY)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return list;
	}
}