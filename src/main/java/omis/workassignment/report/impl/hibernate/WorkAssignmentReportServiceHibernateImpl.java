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
package omis.workassignment.report.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.workassignment.report.WorkAssignmentReportService;
import omis.workassignment.report.WorkAssignmentSummary;

/**
 * Hibernate implementation of work assignment report service.
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class WorkAssignmentReportServiceHibernateImpl
		implements WorkAssignmentReportService {

	/* Query names. */
	private static final String FIND_WORK_ASSIGNMENT_BY_OFFENDER_QUERY_NAME 
		= "findWorkAssignmentByOffender";

	/* Parameter names. */
	private final static String OFFENDER_PARAM_NAME = "offender";
	
	/* Session factories. */
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of work assignment service.
	 * 
	 * @param sessionFactory session factory
	 */
	public WorkAssignmentReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<WorkAssignmentSummary> findByOffender(Offender offender) {
		List<WorkAssignmentSummary> summaries
		= new ArrayList<WorkAssignmentSummary>();
		@SuppressWarnings("unchecked")
		List<WorkAssignmentSummary> workAssignments = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_WORK_ASSIGNMENT_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		summaries.addAll(workAssignments);
		return summaries;
	}
}