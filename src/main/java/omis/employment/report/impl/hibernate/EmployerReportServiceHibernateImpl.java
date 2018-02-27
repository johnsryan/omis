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
package omis.employment.report.impl.hibernate;

import java.util.List;
import java.util.ArrayList;

import omis.employment.report.EmployerReportService;
import omis.employment.report.EmployerSummary;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of employer report service.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class EmployerReportServiceHibernateImpl
		implements EmployerReportService {
	/* Queries */
	private static final String FIND_EMPLOYER_SUMMARY_BY_NAME_QUERY_NAME 
		= "findEmployerSummaryByName";
		
	/* Parameters */
	private final static String EMPLOYER_NAME_PARAM_NAME = "employerName";
		
	private SessionFactory sessionFactory;
		
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of employer report service
	 * @param sessionFactory session factory
	 */

	public EmployerReportServiceHibernateImpl(final SessionFactory
		sessionFactory) {
			this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override 
	public List<EmployerSummary> summarizeByName(final String employerName) {
		List<EmployerSummary> summaries
			= new ArrayList<EmployerSummary>();
		List<EmployerSummary> employerSummaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_EMPLOYER_SUMMARY_BY_NAME_QUERY_NAME)
			.setParameter(EMPLOYER_NAME_PARAM_NAME, employerName)
			.setReadOnly(true)
			.list();
		summaries.addAll(employerSummaries); 
		return summaries;
	}
}