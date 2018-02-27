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

import omis.facility.domain.Facility;
import omis.offender.domain.Offender;
import omis.visitation.report.VisitSummary;
import omis.visitation.report.VisitSummaryReportService;

import org.hibernate.SessionFactory;

/**
 * Visitor Report Service Hibernate Impl.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class VisitSummaryReportServiceHibernateImpl 
	implements VisitSummaryReportService {

	/* Query names. */
	
	private static final String FIND_BY_OFFENDER_ON_DATE_QUERY_NAME
	= "findVisitSummariesByOffenderOnDate";
	
	private static final String FIND_BY_OFFENDER_IN_RANGE_QUERY_NAME
	= "findVisitsByOffenderInDateRange";
	
	private static final String FIND_BY_FACILITY_ON_DATE_QUERY_NAME
	 = "findVisitLogSummariesByFacilityOnDate";
	
	private static final String FIND_BY_FACILITY_IN_RANGE_QUERY_NAME
	= "findVisitLogSummariesByFacilityInRange";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	private static final String FACILITY_PARAM_NAME = "facility";
	
	private SessionFactory sessionFactory;
	
	/** 
	 * Instantiates a default instance of visitor log report service
	 * hibernate implementation.
	 */
	public VisitSummaryReportServiceHibernateImpl() {
		//Default constructor.
	}
	
	/**
	 * Return the session factory.
	 * @return session factory
	 */
	public final SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	/**
	 * Set the session factory.
	 * @param sessionFactory session factory
	 */
	public final void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<VisitSummary> findVisitSummariesByOffenderOnDate(
			final Offender offender, final Date date, final Date dateTime) {
		@SuppressWarnings("unchecked")
		List<VisitSummary> visitorLogSummaries = 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list();
		return visitorLogSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitSummary> findVisitSummariesByOffenderInDateRange(
			final Offender offender, final Date startDate, final Date endDate,
			final Date dateTime) {
		@SuppressWarnings("unchecked")
		List<VisitSummary> visitorLogSummaries = 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_IN_RANGE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setReadOnly(true)
				.list();
		return visitorLogSummaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<VisitSummary> findVisitSummariesByFacilityOnDate(
			final Facility facility, final Date date, final Date dateTime) {
		@SuppressWarnings("unchecked")
		List<VisitSummary> visitorLogSummaries = 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_FACILITY_ON_DATE_QUERY_NAME)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setParameter(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list();
		return visitorLogSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitSummary> findSummariesByFacilityInRange(
			Facility facility, Date startDate, Date endDate) {
		@SuppressWarnings("unchecked")
		List<VisitSummary> visitSummaries =
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_FACILITY_IN_RANGE_QUERY_NAME)
				.setParameter(FACILITY_PARAM_NAME, facility)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setReadOnly(true)
				.list();
		return visitSummaries;
	}
}