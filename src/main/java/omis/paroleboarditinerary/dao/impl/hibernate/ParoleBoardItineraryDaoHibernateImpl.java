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
package omis.paroleboarditinerary.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.paroleboarditinerary.dao.ParoleBoardItineraryDao;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Hibernate implementation of the parole board itinerary data access object.
 *
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.3 (Apr 12, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParoleBoardItinerary>
		implements ParoleBoardItineraryDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findParoleBoardItinerary";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParoleBoardItineraryExcluding";
	
	private static final String FIND_BY_EFFECTIVE_DATE_QUERY_NAME = 
			"findParoleBoardItineraryByEffectiveDate";
	
	/* Parameters. */
	
	private static final String PAROLE_BOARD_LOCATION_PARAM_NAME =
			"paroleBoardLocation";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	private static final String EXCLUDED_ITINERARY_PARAM_NAME = 
			"excludedItinerary";

	private static final String DATE_PARAM_NAME = "date";
	
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  parole board itinerary.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParoleBoardItineraryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardItinerary find(
			final ParoleBoardLocation paroleBoardLocation, 
			final Date startDate, final Date endDate) {
		ParoleBoardItinerary itinerary = (ParoleBoardItinerary) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(PAROLE_BOARD_LOCATION_PARAM_NAME,
						paroleBoardLocation)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return itinerary;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardItinerary findExcluding(
			final ParoleBoardLocation paroleBoardLocation, 
			final Date startDate, final Date endDate,
			final ParoleBoardItinerary excludedItinerary) {
		ParoleBoardItinerary itinerary = (ParoleBoardItinerary) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(PAROLE_BOARD_LOCATION_PARAM_NAME,
						paroleBoardLocation)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setParameter(EXCLUDED_ITINERARY_PARAM_NAME, excludedItinerary)
				.uniqueResult();
		return itinerary;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardItinerary> findByEffectiveDate(
			final Date effectiveDate) {
		@SuppressWarnings("unchecked")
		List<ParoleBoardItinerary> itineraries = (List<ParoleBoardItinerary>) 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_EFFECTIVE_DATE_QUERY_NAME)
				.setParameter(DATE_PARAM_NAME, effectiveDate)
				.list();
		return itineraries;
	}
}