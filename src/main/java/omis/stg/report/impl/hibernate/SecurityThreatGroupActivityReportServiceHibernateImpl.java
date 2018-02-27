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
package omis.stg.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.stg.domain.SecurityThreatGroupActivity;
import omis.stg.report.SecurityThreatGroupActivityInvolvementSummary;
import omis.stg.report.SecurityThreatGroupActivityReportService;
import omis.stg.report.SecurityThreatGroupActivitySummary;

/**
 * Hibernate implementation of report service for security threat group
 * activities.
 *
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class SecurityThreatGroupActivityReportServiceHibernateImpl
		implements SecurityThreatGroupActivityReportService {
	
	/* Queries. */
	
	private static final String SUMMARIZE_BY_OFFENDER_QUERY_NAME 
		= "summarizeSecurityThreatGroupActivitiesByOffender";
	
	private static final String SUMMARIZE_INVOLVEMENT_QUERY_NAME 
		= "summarizeSecurityThreatGroupActivityInvolvementByActivity";
	
	/* Parameters. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String ACTIVITY_PARAM_NAME = "activity";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for security
	 * threat group activities.
	 * 
	 * @param sessionFactory session factory
	 */
	public SecurityThreatGroupActivityReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<SecurityThreatGroupActivitySummary> summarizeByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<SecurityThreatGroupActivitySummary> oSummary 
			= this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return oSummary;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<SecurityThreatGroupActivityInvolvementSummary> summarizeInvolvement(
			final SecurityThreatGroupActivity activity) {
		@SuppressWarnings("unchecked")
		List<SecurityThreatGroupActivityInvolvementSummary> iSummary 
			= this.sessionFactory.getCurrentSession()
				.getNamedQuery(SUMMARIZE_INVOLVEMENT_QUERY_NAME)
				.setParameter(ACTIVITY_PARAM_NAME, activity)
				.setReadOnly(true)
				.list();
		return iSummary;
	}	
}