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
package omis.offenderrelationship.report.impl.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.offender.domain.Offender;
import omis.offenderrelationship.report.OffenderRelationReportService;
import omis.offenderrelationship.report.OffenderRelationSummary;

import org.hibernate.SessionFactory;

/**
 * Offender relation report service implementation.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class OffenderRelationReportServiceHibernateImpl implements 
	OffenderRelationReportService {
	
	/* Queries */
	private static final String 
		FIND_OFFENDER_RELATION_SUMMARY_BY_OFFENDER_QUERY_NAME 
		= "findOffenderRelationSummaryByOffender";
	
	/* Parameters */
	private static final String OFFENDER_PARAM_NAME = "offender";
	private static final  String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private SessionFactory sessionFactory;
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of offender relation. 
	 * report service
	 * @param sessionFactory session factory
	 */
	public OffenderRelationReportServiceHibernateImpl(final SessionFactory
		sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<OffenderRelationSummary> summarizeByOffender(
		final Offender offender, final Date effectiveDate) {
		List<OffenderRelationSummary> summaries
			= new ArrayList<OffenderRelationSummary>();
		@SuppressWarnings("unchecked")
		List<OffenderRelationSummary> internalSummaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
				FIND_OFFENDER_RELATION_SUMMARY_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setParameter(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			.setReadOnly(true)
			.list();
		summaries.addAll(internalSummaries); 
		return summaries;
	}
}