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
package omis.locationterm.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.locationterm.dao.LocationReasonTermDao;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of data access object for location reason terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 8, 2013)
 * @since OMIS 3.0
 */
public class LocationReasonTermDaoHibernateImpl
		extends GenericHibernateDaoImpl<LocationReasonTerm>
		implements LocationReasonTermDao {

	/* Queries. */
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME
		= "findLocationReasonTermsByOffender";
	
	private static final String FIND_QUERY_NAME
		= "findLocationReasonTerm";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findLocationReasonTermExcluding";
	
	private static final String COUNT_BY_LOCATION_TERM_QUERY_NAME
		= "countLocationReasonTermsByLocationTerm";
	
	private static final String FIND_BY_LOCATION_TERM_QUERY_NAME
		= "findLocationReasonTermsByLocationTerm";
	
	private static final String COUNT_QUERY_NAME
		= "countLocationReasonTermsByLocationTermBetweenDates";
	
	private static final String COUNT_EXCLUDING_QUERY_NAME
		= "countLocationReasonTermsByLocationTermBetweenDatesExcluding";
	
	private static final String
	COUNT_FOR_LOCATION_TERM_OUT_OF_DATE_BOUNDS_QUERY_NAME
		= "countLocationReasonTermsByLocationTermOutsideOfDates";
	
	private static final String FIND_FOR_LOCATION_TERM_ON_DATE_QUERY_NAME
		= "findLocationReasonTermForLocationTermOnDate";
	
	private static final String COUNT_AFTER_DATE_EXCLUDING_QUERY_NAME
		= "countLocationReasonTermsForOffenderAfterDateExcluding";
	
	private static final String FIND_WITH_START_DATE_QUERY_NAME
		= "findLocationReasonTermWithStartDate";
	
	/* Parameters. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	private static final String LOCATION_TERM_PARAM_NAME = "locationTerm";
	
	private static final String EXCLUDED_LOCATION_REASON_TERMS_PARAM_NAME
		= "excludedLocationReasonTerms";

	private static final String DATE_PARAM_NAME = "date";

	private static final String FIND_FOR_OFFENDER_ON_DATE_QUERY_NAME
		= "findLocationReasonTermForOffenderOnDate";

	private static final String EXCLUDED_LOCATION_REASON_TERM_PARAM_NAME
		= "excludedLocationReasonTerm";

	private static final String DELETE_BY_LOCATION_TERM_QUERY_NAME
		= "deleteLocationReasonTermsByLocationTerm";
	
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * location reason terms with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public LocationReasonTermDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<LocationReasonTerm> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<LocationReasonTerm> locationReasonTerms
			= this.getSessionFactory().getCurrentSession().getNamedQuery(
					FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender).list();
		return locationReasonTerms;
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm find(final Offender offender,
			final LocationTerm locationTerm, final Date startDate,
			final Date endDate) {
		LocationReasonTerm locationReasonTerm = (LocationReasonTerm)
			this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return locationReasonTerm;
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm findExcluding(final Offender offender,
			final LocationTerm locationTerm, final Date startDate,
			final Date endDate,
			final LocationReasonTerm... excludedLocationReasonTerms) {
		LocationReasonTerm locationReasonTerm = (LocationReasonTerm)
			this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setParameterList(EXCLUDED_LOCATION_REASON_TERMS_PARAM_NAME,
						excludedLocationReasonTerms)
				.uniqueResult();
		return locationReasonTerm;
	}

	/** {@inheritDoc} */
	@Override
	public long countByLocationTerm(final LocationTerm locationTerm) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_BY_LOCATION_TERM_QUERY_NAME)
				.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationReasonTerm> findByLocationTerm(
			final LocationTerm locationTerm) {
		@SuppressWarnings("unchecked")
		List<LocationReasonTerm> reasonTerms
			= this.getSessionFactory().getCurrentSession().getNamedQuery(
						FIND_BY_LOCATION_TERM_QUERY_NAME)
					.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
					.list();
		return reasonTerms;
	}

	/** {@inheritDoc} */
	@Override
	public long count(final LocationTerm locationTerm,
			final Date startDate, final Date endDate) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_QUERY_NAME)
				.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public long countExcluding(final LocationTerm locationTerm,
			final Date startDate, final Date endDate,
			final LocationReasonTerm... excludedLocationReasonTerms) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_EXCLUDING_QUERY_NAME)
				.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setParameterList(EXCLUDED_LOCATION_REASON_TERMS_PARAM_NAME,
						excludedLocationReasonTerms)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public long countForLocationTermOutOfDateBounds(
			final LocationTerm locationTerm, final Date startDate,
			final Date endDate) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
			.getNamedQuery(
					COUNT_FOR_LOCATION_TERM_OUT_OF_DATE_BOUNDS_QUERY_NAME)
			.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
			.setTimestamp(START_DATE_PARAM_NAME, startDate)
			.setTimestamp(END_DATE_PARAM_NAME, endDate).uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm findForLocationTermOnDate(
			final LocationTerm locationTerm, final Date date) {
		LocationReasonTerm locationReasonTerm = (LocationReasonTerm)
				this.getSessionFactory().getCurrentSession().getNamedQuery(
						FIND_FOR_LOCATION_TERM_ON_DATE_QUERY_NAME)
					.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
					.setTimestamp(DATE_PARAM_NAME, date).uniqueResult();
		return locationReasonTerm;
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm findForOffenderOnDate(final Offender offender,
			final Date date) {
		LocationReasonTerm locationReasonTerm = (LocationReasonTerm)
				this.getSessionFactory().getCurrentSession().getNamedQuery(
						FIND_FOR_OFFENDER_ON_DATE_QUERY_NAME)
					.setParameter(OFFENDER_PARAM_NAME, offender)
					.setTimestamp(DATE_PARAM_NAME, date).uniqueResult();
		return locationReasonTerm;
	}

	/** {@inheritDoc} */
	@Override
	public long countAfterDateExcluding(Offender offender, Date startDate,
			LocationReasonTerm excludedLocationReasonTerm) {
		long count = (long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_AFTER_DATE_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, startDate)
				.setParameter(EXCLUDED_LOCATION_REASON_TERM_PARAM_NAME, 
						excludedLocationReasonTerm)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public LocationReasonTerm findWithStartDate(
			final LocationTerm locationTerm, final Date startDate) {
		LocationReasonTerm locationReasonTerm = (LocationReasonTerm)
				this.getSessionFactory().getCurrentSession()
					.getNamedQuery(FIND_WITH_START_DATE_QUERY_NAME)
					.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
					.setTimestamp(START_DATE_PARAM_NAME, startDate)
					.uniqueResult();
		return locationReasonTerm;
	}
	
	/** {@inheritDoc} */
	@Override
	public int removeByLocationTerm(final LocationTerm locationTerm) {
		return this.getSessionFactory().getCurrentSession().getNamedQuery(
				DELETE_BY_LOCATION_TERM_QUERY_NAME)
			.setParameter(LOCATION_TERM_PARAM_NAME, locationTerm)
			.executeUpdate();
	}
}