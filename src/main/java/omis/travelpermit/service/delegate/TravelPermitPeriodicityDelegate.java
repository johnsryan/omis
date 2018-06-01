/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.service.delegate;

import java.util.List;

import omis.travelpermit.dao.TravelPermitPeriodicityDao;
import omis.travelpermit.domain.TravelPermitPeriodicity;


/**
 * Delegate for travel method.
 *
 * @author Yidong Li
 * @version 0.0.2 (Aug 18, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitPeriodicityDelegate {
	/* Resources. */
	private final TravelPermitPeriodicityDao travelPermitPeriodicityDao;
	
	/* Constructors. */
	/**
	 * Instantiates delegate for managing travel method.
	 * 
	 * @param travelMethodDao data access object for travel method
	 */
	public TravelPermitPeriodicityDelegate(
		final TravelPermitPeriodicityDao travelPermitPeriodicityDao) {
		this.travelPermitPeriodicityDao = travelPermitPeriodicityDao;
	}

	/**
	 * Find travel permit periodicity
	 * 
	 * @return a list of all travel permit periodicities
	 *
	 */
	public List<TravelPermitPeriodicity> findTravelPermitPeriodicities(){
		return this.travelPermitPeriodicityDao.findAllPeriodicities();
	}
}