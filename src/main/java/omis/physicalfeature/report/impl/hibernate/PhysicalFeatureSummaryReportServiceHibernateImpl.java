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
package omis.physicalfeature.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.physicalfeature.report.PhysicalFeatureSummaryReportService;
import omis.physicalfeature.report.PhysicalFeatureSummary;

/**
 * Hibernate implementation of the physical feature report summary service.
 *
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class PhysicalFeatureSummaryReportServiceHibernateImpl 
	implements PhysicalFeatureSummaryReportService {
	
	/* Queries. */
	
	private static final String FIND_PHYSICAL_FEATURE_BY_OFFENDER_QUERY_NAME
		= "findPhysicalFeatureByOffender";
	
	/* Parameters.*/ 
	
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	/* Members. */
	private final SessionFactory sessionFactory;

	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public PhysicalFeatureSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<PhysicalFeatureSummary> findByOffender(Offender offender) {
		@SuppressWarnings("unchecked")
		List<PhysicalFeatureSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_PHYSICAL_FEATURE_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}