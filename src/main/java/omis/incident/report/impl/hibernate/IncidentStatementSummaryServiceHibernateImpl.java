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

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.incident.domain.Jurisdiction;
import omis.incident.report.IncidentStatementSummary;
import omis.incident.report.IncidentStatementSummaryService;
import omis.incident.report.InvolvedPartySummary;
import omis.location.domain.Location;
import omis.person.domain.Person;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of incident report service.
 * 
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class IncidentStatementSummaryServiceHibernateImpl
		implements IncidentStatementSummaryService {

	/* Queries */
	private static final String FIND_INVOLVED_PARTIES_BY_CRITERIA_QUERY_NAME
		= "findInvolvedPartiesByCriteria";
	private static final String FIND_BY_LOCATIN_QUERY_NAME
		= "findIncidentStatementsByLocation";
	private static final String FIND_BY_INVOLVED_PEOPLE_QUERY_NAME
		= "findIncidentStatementsByInvolvedPeople";
	private static final String FIND_BY_DOCUMENTER
		= "findIncidentStatementsByDocumenter";
	
	/* Parameter names */
	private final static String START_DATE_PARAM_NAME = "startDate";
	private final static String END_DATE_PARAM_NAME = "endDate";
	private final static String LOCATION_PARAM_NAME = "location";
	private final static String REPORT_ID_PARAM_NAME = "reportId";
	
	private final static String PERSONS_PARAM_NAME = "persons";
	private final static String JURISDICTION_PARAM_NAME = "jurisdiction";
	
	private final static String DOCUMENTER_PARAM_NAME = "documenter";
	
	/* Property names. */
	private final static String LOCATION_PROPERTY_NAME = "location";
	private final static String JURISDICTION_PROPERTY_NAME = "jurisdiction";
	
	/* Entity names. */
	private final static String FACILITY_ENTITY_NAME
		= "omis.facility.domain.Facility";
	private static final String INCIDENT_STATEMENT_ENTITY_NAME
		= "omis.incident.domain.IncidentStatement";
	
	private SessionFactory sessionFactory;
		
	/* Component retriever. */
	
	private AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of incident 
	 * report summary service
	 * @param sessionFactory session factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public IncidentStatementSummaryServiceHibernateImpl(final SessionFactory
		sessionFactory, final AuditComponentRetriever auditComponentRetriever) {
			this.sessionFactory = sessionFactory;
			this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Method implementations. */

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<InvolvedPartySummary> findInvolvedParties(final Long id) {
		List<InvolvedPartySummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_INVOLVED_PARTIES_BY_CRITERIA_QUERY_NAME)
			.setParameter(REPORT_ID_PARAM_NAME, id)
			.setReadOnly(true)
			.list();
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<IncidentStatementSummary> findByInvolvedPeople(
			final Date startDate, final Date endDate,
			final Jurisdiction jurisdiction,
			final Location location, final List<Person> involvedPeople) {
		@SuppressWarnings("unchecked")
		List<IncidentStatementSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_BY_INVOLVED_PEOPLE_QUERY_NAME)
			.setDate(START_DATE_PARAM_NAME,startDate)
			.setDate(END_DATE_PARAM_NAME,endDate)
			.setParameterList(PERSONS_PARAM_NAME, involvedPeople)
			.setParameter(LOCATION_PARAM_NAME, location, 
				this.sessionFactory.getClassMetadata(FACILITY_ENTITY_NAME)
				.getPropertyType(LOCATION_PROPERTY_NAME))
			.setParameter(JURISDICTION_PARAM_NAME, jurisdiction, 
				this.sessionFactory.getClassMetadata(
						INCIDENT_STATEMENT_ENTITY_NAME)
				.getPropertyType(JURISDICTION_PROPERTY_NAME))
			.setReadOnly(true)
			.list();
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<IncidentStatementSummary> findByLocation(final Date startDate,
			final Date endDate, final Jurisdiction jurisdiction,
			final Location location) {
		@SuppressWarnings("unchecked")
		List<IncidentStatementSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_LOCATIN_QUERY_NAME)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setParameter(LOCATION_PARAM_NAME, location, 
						this.sessionFactory
						.getClassMetadata(FACILITY_ENTITY_NAME)
						.getPropertyType(LOCATION_PROPERTY_NAME))
				.setParameter(JURISDICTION_PARAM_NAME, jurisdiction, 
						this.sessionFactory.getClassMetadata(
								INCIDENT_STATEMENT_ENTITY_NAME)
						.getPropertyType(JURISDICTION_PROPERTY_NAME))
				.setReadOnly(true)
				.list();
				return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<IncidentStatementSummary> findByCurrentUser() {
		@SuppressWarnings("unchecked")
		List<IncidentStatementSummary> summaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_BY_DOCUMENTER)
				.setParameter(DOCUMENTER_PARAM_NAME,
						this.auditComponentRetriever.retrieveUserAccount())
				.setReadOnly(true)
				.list();
		return summaries;
	}
}