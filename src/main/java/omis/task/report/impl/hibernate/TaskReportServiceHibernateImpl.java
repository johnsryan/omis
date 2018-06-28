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
package omis.task.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.task.report.TaskReportService;
import omis.task.report.TaskSummary;
import omis.user.domain.UserAccount;

/**
 * Hibernate implementation of report service for tasks.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.0.3 (June 21, 2018)
 * @since OMIS 3.0
 */
public class TaskReportServiceHibernateImpl
		implements TaskReportService {
	
	/* Queries. */
	
	private static final String SUMMARIZE_BY_SOURCE_ACCOUNT_USERNAME_QUERY_NAME
		= "summarizeTasksBySourceAccountUsername";
	
	private static final String FIND_UNINVOKED_COUNT_BY_ASSIGNEE_QUERY_NAME =
			"findUninvokedTaskCountByAssigneeUserAccount";
	
	/* Parameters. */
	
	private static final String USERNAME_PARAM_NAME = "username";
	
	private static final String ASSIGNEE_ACCOUNT_PARAM_NAME =
			"assigneeAccount";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for tasks.
	 * 
	 * @param sessionFactory session factory
	 */
	public TaskReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<TaskSummary> summarizeBySourceAccountUsername(
			final String username) {
		@SuppressWarnings("unchecked")
		List<TaskSummary> taskSummaries
			= this.sessionFactory.getCurrentSession().getNamedQuery(
					SUMMARIZE_BY_SOURCE_ACCOUNT_USERNAME_QUERY_NAME)
				.setParameter(USERNAME_PARAM_NAME, username)
				.setReadOnly(true)
				.list();
		return taskSummaries;
	}

	/**{@inheritDoc} */
	@Override
	public Long findUninvokedTaskCountByAssigneeUserAccount(
			final UserAccount assigneeAccount) {
		return (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_UNINVOKED_COUNT_BY_ASSIGNEE_QUERY_NAME)
				.setParameter(ASSIGNEE_ACCOUNT_PARAM_NAME, assigneeAccount)
				.setReadOnly(true)
				.uniqueResult();
	}
}