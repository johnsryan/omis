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
package omis.victim.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.person.domain.Person;
import omis.victim.report.VictimNoteReportService;
import omis.victim.report.VictimNoteSummary;

/**
 * Hibernate implementation of report service for victim notes.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class VictimNoteReportServiceHibernateImpl
		implements VictimNoteReportService {
	
	/* Query names. */
	
	private final String SUMMARIZE_BY_VICTIM_QUERY_NAME
		= "summarizeVictimNotesByVictim";
	
	/* Parameter names. */
	
	private final String VICTIM_PARAM_NAME = "victim";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;

	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of report service for victim notes.
	 * 
	 * @param sessionFactory session factory
	 */
	public VictimNoteReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<VictimNoteSummary> summarizeByVictim(final Person victim) {
		@SuppressWarnings("unchecked")
		List<VictimNoteSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						SUMMARIZE_BY_VICTIM_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}