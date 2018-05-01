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
package omis.visitation.report.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.facility.domain.Facility;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.report.AlternateNameSummary;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.report.VisitationAssociationSummary;
import omis.visitation.report.VisitationAssociationSummaryReportService;

/**
 * Report service implementation for visitor list.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @author Sheronda Vaughn
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class VisitationAssociationSummaryReportServiceHibernateImpl 
	implements VisitationAssociationSummaryReportService {
	/* Query Names */
	
	private static final String SUMMARIZE_VISITOR_LIST_BY_OFFENDER_QUERY_NAME =
			"summarizeVisitorListByOffender";
	
	private static final String
		SUMMARIZE_VISITATION_ASSOCIATIONS_BY_OFFENDER_IN_RANGE_QUERY_NAME
		= "summarizeVisitationAssociationsByOffenderInRange";
	
	private static final String SUMMARIZE_VISITOR_LIST_BY_FACILITY_QUERY_NAME =
			"summarizeVisitorListByFacility";
	
	private static final String FIND_ALTERNATIVE_NAMES_QUERY_NAME
		= "findAlternativeNames";
	
	private static final String 
		COUNT_VISITATION_ASSOCIATION_BY_OFFENDER_VISITOR_QUERY_NAME
		= "countVistationAssociationByOffenderVisitor";

	private static final String 
		FIND_VISITATION_ASSOCIATION_BY_OFFENDER_VISITOR_QUERY_NAME
		= "findVisitationAssociationByOffenderVisitor";
	
	/* Parameters */
	
	private static final String OFFENDER_PARAM = "offender";
	
	private static final String DATE_PARAM = "date";
	
	private static final String FACILITY_PARAM = "facility";
	
	private static final String START_DATE_PARAM = "startDate";
	
	private static final String END_DATE_PARAM = "endDate";
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String VISITOR_PARAM_NAME = "visitor";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private SessionFactory sessionFactory;
	
	private final OffenderDelegate offenderDelegate;
	
	/**
	 * Instantiates a default instance of visitor list report service
	 * hibernate implementation.
	 * 
	 * @param offenderDelegate offenderDelegate
	 */
	public VisitationAssociationSummaryReportServiceHibernateImpl(
			final OffenderDelegate offenderDelegate) {
		this.offenderDelegate = offenderDelegate;
	}

	/**
	 * Return the session factory.
	 * @return the sessionFactory
	 */
	public final SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * Set the session factory.
	 * @param sessionFactory the sessionFactory to set
	 */
	public final void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociationSummary> summarizeVisitationAssociations(
			final Offender offender, final Date date) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociationSummary> visitorSummaries = 
				(List<VisitationAssociationSummary>)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(SUMMARIZE_VISITOR_LIST_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM, offender)
				.setTimestamp(DATE_PARAM, date)
				.setReadOnly(true)
				.list();
		return visitorSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociationSummary> 
		summarizeVisitationAssociationsByFacility(
			final Facility facility, final Date dateTime) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociationSummary> visitorSummaries = 
				(List<VisitationAssociationSummary>)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(SUMMARIZE_VISITOR_LIST_BY_FACILITY_QUERY_NAME)
				.setParameter(FACILITY_PARAM, facility)
				.setTimestamp(DATE_PARAM, dateTime)
				.setReadOnly(true)
				.list();
		return visitorSummaries;
	}

	@Override
	public List<VisitationAssociationSummary>
		summarizeVisitationAssociationsInRange(final Offender offender,
				final Date startDate, final Date endDate) {
		@SuppressWarnings("unchecked")
	List<VisitationAssociationSummary> visitorSummaries 
			= (List<VisitationAssociationSummary>)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
			SUMMARIZE_VISITATION_ASSOCIATIONS_BY_OFFENDER_IN_RANGE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM, offender)
				.setTimestamp(START_DATE_PARAM, startDate)
				.setTimestamp(END_DATE_PARAM, endDate)
				.setReadOnly(true)
				.list();
		return visitorSummaries;
	}
	
	@Override
	public List<AlternateNameSummary>  summarizeAlternativeNames(
		final Person person, final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<AlternateNameSummary> alternativeNameSummaries 
			 = this.sessionFactory.getCurrentSession()
			 .getNamedQuery(FIND_ALTERNATIVE_NAMES_QUERY_NAME)
			 .setParameter(PERSON_PARAM_NAME, person)
			 .setParameter(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			 .setReadOnly(true)
			 .list();
		return alternativeNameSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean visitationAssociationExists(
			final Offender offender, final Person visitor) {
		final Boolean query = (Boolean) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						COUNT_VISITATION_ASSOCIATION_BY_OFFENDER_VISITOR_QUERY_NAME)
				.setParameter(OFFENDER_PARAM, offender)
				.setParameter(VISITOR_PARAM_NAME, visitor)
				.uniqueResult();
		return query;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation findVisitationAssociation(
			final Offender offender, final Person visitor) {
		VisitationAssociation association 
			= (VisitationAssociation) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_VISITATION_ASSOCIATION_BY_OFFENDER_VISITOR_QUERY_NAME)
				.setParameter(OFFENDER_PARAM, offender)
				.setParameter(VISITOR_PARAM_NAME, visitor)
				.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean isOffender(final Person person) {
		return this.offenderDelegate.isOffender(person);
	}
}