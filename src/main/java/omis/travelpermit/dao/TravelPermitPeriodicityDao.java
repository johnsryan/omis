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
package omis.travelpermit.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.travelpermit.domain.TravelPermitPeriodicity;

/**
 * Data access object for travel permit periodicity.
 * 
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (June 4, 2018)
 * @since OMIS 3.0
 */
public interface TravelPermitPeriodicityDao
	extends GenericDao<TravelPermitPeriodicity> {
	/**
	 * Returns all travel permit periodicities
	 * 
	 * @return existing travel permit periodicities
	 */
	List<TravelPermitPeriodicity> findAllPeriodicities();
	
	TravelPermitPeriodicity find(String name);
}