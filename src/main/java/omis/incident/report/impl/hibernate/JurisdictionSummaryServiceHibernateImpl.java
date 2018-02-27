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
package omis.incident.report.impl.hibernate;

import java.util.List;

import omis.incident.report.JurisdictionSummary;
import omis.incident.report.JurisdictionSummaryService;

import org.hibernate.SessionFactory;

/**
 * Jurisdiction summary service hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class JurisdictionSummaryServiceHibernateImpl
implements JurisdictionSummaryService {

	/* Queries */
	
	private final String SUMMARIZE_JURISDICTIONS_QUERY_NAME
		= "summarizeJurisdictions";
	
	/* Session factory. */
	
	private SessionFactory sessionFactory;
	
	/**
	 * Instantiates a default instance of jurisdiction summary service.
	 */
	public JurisdictionSummaryServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<JurisdictionSummary> summarizeJurisdictions() {
		@SuppressWarnings("unchecked")
		List<JurisdictionSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_JURISDICTIONS_QUERY_NAME)
				.setReadOnly(true)
				.list();
		return summaries;
	}
}