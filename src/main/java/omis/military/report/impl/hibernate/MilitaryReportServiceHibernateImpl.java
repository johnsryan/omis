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
package omis.military.report.impl.hibernate;

import java.util.List;

import omis.military.domain.MilitaryServiceTerm;
import omis.military.report.MilitaryReportService;
import omis.military.report.MilitaryServiceTermNoteSummary;
import omis.military.report.MilitaryServiceTermSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Military report service hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class MilitaryReportServiceHibernateImpl 
implements MilitaryReportService {

	/* Session factory. */
	
	private final SessionFactory sessionFactory;
	
	/* Query names. */
	
	private static final String 
	FIND_SERVICE_TERM_SUMMARIES_BY_OFFENDER_QUERY_NAME
		= "findServiceTermSummariesByOffender";
	
	private static final String FIND_NOTE_SUMMARIES_BY_SERVICE_TERM_QUERY_NAME
		= "findMilitaryServiceTermNoteSummariesByMilitaryServiceTerm";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String SERVICE_TERM_PARAM_NAME = "serviceTerm";
	
	/* Constructor. */
	
	/**
	 * Instantiates a military report service with the specified session
	 * factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public MilitaryReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<MilitaryServiceTermSummary> 
	findMilitaryServiceTermSummariesByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<MilitaryServiceTermSummary> serviceTermSummaries =
				this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_SERVICE_TERM_SUMMARIES_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return serviceTermSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<MilitaryServiceTermNoteSummary> 
	findMilitaryServiceTermNotesByMilitaryServiceTerm(
			final MilitaryServiceTerm serviceTerm) {
		@SuppressWarnings("unchecked")
		List<MilitaryServiceTermNoteSummary> noteSummaries 
			= this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_NOTE_SUMMARIES_BY_SERVICE_TERM_QUERY_NAME)
				.setParameter(SERVICE_TERM_PARAM_NAME, serviceTerm)
				.setReadOnly(true)
				.list();
		return noteSummaries;
	}
}