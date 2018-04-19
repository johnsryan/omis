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
package omis.paroleboarditinerary.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Data access object for parole board itinerary.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.2 (Apr 12, 2018)
 * @since OMIS 3.0
 */
public interface ParoleBoardItineraryDao 
		extends GenericDao<ParoleBoardItinerary> {
	
	/**
	 * Returns the parole board itinerary that matches the specified
	 * parole board location, start date and end date.
	 * 
	 * @param paroleBoardlocation parole board location
	 * @param startDate start date
	 * @param endDate end date
	 * @return parole board itinerary
	 */
	ParoleBoardItinerary find(ParoleBoardLocation paroleBoardlocation,
			Date startDate, Date endDate);
	
	/**
	 * Returns the parole board itinerary that matches the specified
	 * parole board location, start date and end date excluding the
	 * specified itinerary.
	 * 
	 * @param paroleBoardlocation parole board location
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedItinerary excluded parole board itinerary
	 * @return parole board itinerary
	 */
	ParoleBoardItinerary findExcluding(ParoleBoardLocation paroleBoardlocation,
			Date startDate, Date endDate,
			ParoleBoardItinerary excludedItinerary);

	/**
	 * Returns a list of parole board itineraries after the specified date.
	 * 
	 * @param effectiveDate effective date
	 * @return list of parole board itineraries
	 */
	List<ParoleBoardItinerary> findByEffectiveDate(Date effectiveDate);
}