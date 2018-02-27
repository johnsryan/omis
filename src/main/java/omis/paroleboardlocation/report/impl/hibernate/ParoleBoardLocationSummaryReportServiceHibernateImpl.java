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
package omis.paroleboardlocation.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.paroleboardlocation.report.ParoleBoardLocationSummary;
import omis.paroleboardlocation.report.ParoleBoardLocationSummaryReportService;

/**
 * Hibernate implementation of the parole board location summary report service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationSummaryReportServiceHibernateImpl 
		implements ParoleBoardLocationSummaryReportService {

	/* Queries. */
	
	private static final String FIND_BOARD_LOCATIONS_QUERY_NAME =
			"findParoleBoardLocationSummaries";
	
	/* Members. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Constructor.
	 *
	 * @param sessionFactory session factory
	 */
	public ParoleBoardLocationSummaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardLocationSummary> findParoleBoardLocationSummaries() {
		@SuppressWarnings("unchecked")
		List<ParoleBoardLocationSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BOARD_LOCATIONS_QUERY_NAME)
				.list();
		return summaries;
	}
}