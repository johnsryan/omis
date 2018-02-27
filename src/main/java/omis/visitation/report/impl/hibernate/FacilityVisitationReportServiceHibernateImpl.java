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

import omis.bedplacement.report.BedPlacementLookupDelegate;
import omis.facility.domain.Facility;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.visitation.report.FacilityVisitationOffenderSummary;
import omis.visitation.report.FacilityVisitationReportService;
import omis.visitation.report.VisitSummary;

/**
 * Facility visitation report service hibernate implementation.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class FacilityVisitationReportServiceHibernateImpl
implements FacilityVisitationReportService {

	/* Helpers. */
	
	private final SessionFactory sessionFactory;
	private final BedPlacementLookupDelegate bedPlacementLookupDelegate;
	
	/* Query names. */
	
	private static final String SUMMARIZE_FACILITY_VISITATION_ON_DATE_QUERY_NAME
	= "summarizeFacilityVisitationOnDate";
	private static final String FIND_BY_OFFENDER_ON_DATE_QUERY_NAME
	= "findVisitSummariesByOffenderOnDate";
	private static final String 
	SUMMARIZE_FACILITY_VISITATION_IN_DATE_RANGE_QUERY_NAME
	= "summarizeFacilityVisitationInDateRange";
	private static final String
	FIND_VISITING_OFFFENDERS_BY_LOCATION_ON_DATE_QUERY_NAME
	= "findVisitingOffendersByLocationOnDate";
//	private static final String FIND_HOUSING_UNIT_NAME_BY_OFFENDER_ON_DATE
//	= "findHousingUnitNameByOffenderOnDate";
	private static final String FIND_MUG_SHOT_ID_BY_OFFENDER_QUERY_NAME
	= "findMugShotIdByOffender";
	
	/* Parameter names. */
	
	private static final String DATE_PARAM_NAME = "date";
	private static final String START_DATE_PARAM_NAME = "startDate";
	private static final String END_DATE_PARAM_NAME = "endDate";
	private static final String LOCATION_PARAM_NAME = "location";
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	/* Constructors. */
	
	/**
	 * Instantiates an instance of facility visitation report service with
	 * the specified session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public FacilityVisitationReportServiceHibernateImpl(
			final SessionFactory sessionFactory,
			final BedPlacementLookupDelegate bedPlacementLookupDelegate) {
		this.sessionFactory = sessionFactory;
		this.bedPlacementLookupDelegate = bedPlacementLookupDelegate;
	}
	
	/* Getters. */

	/**
	 * Returns session factory.
	 * 
	 * @return session factory
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	/**
	 * Returns the bed placement lookup delegate.
	 * 
	 * @return bed placement lookup delegate
	 */
	public BedPlacementLookupDelegate getBedPlacementLookupDelegate() {
		return this.bedPlacementLookupDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public List<FacilityVisitationOffenderSummary> 
	summarizeFacilityVisitsOnDate(final Facility facility,
			final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<FacilityVisitationOffenderSummary> offenderSummaries = 
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_FACILITY_VISITATION_ON_DATE_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, facility)
				.setDate(DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.list();
		return offenderSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<FacilityVisitationOffenderSummary> 
	summarizeFacilityVisitsInDateRange(final Facility facility,
			final Date startDate, final Date endDate) {
		@SuppressWarnings("unchecked")
		List<FacilityVisitationOffenderSummary> offenderSummaries = 
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						SUMMARIZE_FACILITY_VISITATION_IN_DATE_RANGE_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, facility)
				.setDate(START_DATE_PARAM_NAME, startDate)
				.setDate(END_DATE_PARAM_NAME, endDate)
				.setReadOnly(true)
				.list();
		return offenderSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitSummary> findVisitSummariesByOffenderOnDate(
			final Offender offender, final Date date) {
		@SuppressWarnings("unchecked")
		List<VisitSummary> visitSummaries = 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list();
		return visitSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitSummary> findVisitSummariesByOffenderInDateRange(
			Offender offender, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public List<Offender> findVisitingOffendersByLocationOnDate(
			final Location location, final Date date) {
		@SuppressWarnings("unchecked")
		List<Offender> offenders = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_VISITING_OFFFENDERS_BY_LOCATION_ON_DATE_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, location)
				.setDate(DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.list();
		return offenders;
	}

	/** {@inheritDoc} */
	@Override
	public String findOffenderHousingUnit(Offender offender, Date date) {
//		String unitName = (String) this.getSessionFactory()
//				.getCurrentSession()
//				.getNamedQuery(FIND_HOUSING_UNIT_NAME_BY_OFFENDER_ON_DATE)
//				.setParameter(OFFENDER_PARAM_NAME, offender)
//				.setDate(DATE_PARAM_NAME, date)
//				.uniqueResult();
		return this.bedPlacementLookupDelegate.findUnitNameByOffender(offender,
				date);
	}

	/** {@inheritDoc} */
	@Override
	public Long findMugShotIdByOffender(Offender offender) {
		Long photoId = (Long) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_MUG_SHOT_ID_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		return photoId;
	}
}