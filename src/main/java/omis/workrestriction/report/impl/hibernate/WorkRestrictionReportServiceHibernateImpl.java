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
package omis.workrestriction.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.workrestriction.report.WorkRestrictionReportService;
import omis.workrestriction.report.WorkRestrictionSummary;

/**
 * WorkRestrictionReportServiceHibernateImpl.java
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 *
 */
public class WorkRestrictionReportServiceHibernateImpl 
	implements WorkRestrictionReportService {
	
	private static final String 
		FIND_WORK_RESTRICTION_SUMMARIES_BY_OFFENDER_QUERY_NAME
			= "findWorkRestrictionSummariesByOffender";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private final SessionFactory sessionFactory;
	
	
	/**
	 * Constructor for work restriction report service hibernate implementation
	 * @param sessionFactory
	 */
	public WorkRestrictionReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public List<WorkRestrictionSummary> summariesByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<WorkRestrictionSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
					FIND_WORK_RESTRICTION_SUMMARIES_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		return summaries;
	}
}