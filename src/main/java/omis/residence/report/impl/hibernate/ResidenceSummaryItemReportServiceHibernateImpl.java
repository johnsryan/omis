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
package omis.residence.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.residence.report.ResidenceSummary;
import omis.residence.report.ResidenceSummaryItemReportService;

/**
 * Residence Summary Item Report Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ResidenceSummaryItemReportServiceHibernateImpl
		implements ResidenceSummaryItemReportService {
	
	private static final String FIND_RESIDENCE_SUMMARY_QUERY_NAME =
			"findCurrentPrimaryResidentTermByOffender";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private final SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public ResidenceSummaryItemReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public ResidenceSummary findResidenceSummaryByOffender(
			final Offender offender) {
		ResidenceSummary residenceSummary = (ResidenceSummary)
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_RESIDENCE_SUMMARY_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(EFFECTIVE_DATE_PARAM_NAME, new Date())
				.setReadOnly(true)
				.uniqueResult();
		
		return residenceSummary;
	}
}