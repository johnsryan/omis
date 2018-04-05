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
package omis.locationterm.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;

/**
 * Data access object for period during which the reason an offender is at a
 * location applies.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 7, 2013)
 * @since OMIS 3.0
 */
public interface LocationReasonTermDao
		extends GenericDao<LocationReasonTerm> {

	/**
	 * Returns location reason terms for offender.
	 * 
	 * @param offender offender
	 * @return location reason terms for offender
	 */
	List<LocationReasonTerm> findByOffender(Offender offender);
	
	/**
	 * Returns location reason term.
	 * 
	 * @param offender offender
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return location reason term
	 */
	LocationReasonTerm find(Offender offender, LocationTerm locationTerm,
			Date startDate, Date endDate);
	
	/**
	 * Returns location reason term that is not one of the excluded location
	 * reason terms.
	 * 
	 * @param offender offender
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationReasonTerms location reason terms to exclude
	 * @return location reason term
	 */
	LocationReasonTerm findExcluding(Offender offender,
			LocationTerm locationTerm, Date startDate, Date endDate,
			LocationReasonTerm... excludedLocationReasonTerms);

	/**
	 * Returns number of location reason terms by location term.
	 * 
	 * @param locationTerm location term
	 * @return number of location reason terms by location term
	 */
	long countByLocationTerm(LocationTerm locationTerm);
	
	/**
	 * Returns location reason terms by location term.
	 * 
	 * @param locationTerm location term
	 * @return location reason terms by location term
	 */
	List<LocationReasonTerm> findByLocationTerm(LocationTerm locationTerm);

	/**
	 * Returns number of conflicting location reason terms.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of conflicting location reason terms
	 */
	long count(LocationTerm locationTerm, Date startDate, Date endDate);

	/**
	 * Returns number of conflicting location reason terms that are not
	 * excluded.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationReasonTerms location reason terms to exclude
	 * @return number of conflicting location reason terms
	 */
	long countExcluding(LocationTerm locationTerm, Date startDate,
			Date endDate, LocationReasonTerm... excludedLocationReasonTerms);

	/**
	 * Returns number of location reason terms that exist outside of start and
	 * end date range.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of location reason terms that exist outside of start and
	 * end date range
	 */
	long countForLocationTermOutOfDateBounds(LocationTerm locationTerm,
			Date startDate, Date endDate);
	
	/**
	 * Returns location reason term for location term on date.
	 * 
	 * @param locationTerm location term
	 * @param date date
	 * @return location reason term for location term on date
	 */
	LocationReasonTerm findForLocationTermOnDate(LocationTerm locationTerm,
			Date date);

	/**
	 * Returns location reason term for offender on date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return location reason term for offender on date
	 */
	LocationReasonTerm findForOffenderOnDate(Offender offender, Date date);
	
	/**
	 * Returns number of location reason terms for an offender after the 
	 * specified date that are not excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param excludedLocationReasonTerm excluded location reason term
	 * @return number of location terms for an offender after the specified date
	 */
	long countAfterDateExcluding(Offender offender, Date startDate, 
			LocationReasonTerm excludedLocationReasonTerm);

	/**
	 * Returns reason term with start date.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @return reason term with start date
	 */
	LocationReasonTerm findWithStartDate(LocationTerm locationTerm,
			Date startDate);
	
	/**
	 * Removes location reason terms by location term.
	 * 
	 * @param locationTerm location term by which to remove
	 * @return location terms removed
	 */
	int removeByLocationTerm(LocationTerm locationTerm);
}