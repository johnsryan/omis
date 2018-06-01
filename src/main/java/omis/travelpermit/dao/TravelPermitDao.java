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

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.offender.domain.Offender;
import omis.travelpermit.domain.TravelPermit;

/**
 * Data access object for travel permit.
 * 
 * @author Yidong Li
 * @version 0.1.1 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public interface TravelPermitDao
		extends GenericDao<TravelPermit> {
	/**
	 * Returns the existing travel permit if it matches the criteria.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @return existing travel permit
	 */
	TravelPermit findExistingTravelPermit(Offender offender, Date startDate); 
	
	/**
	 * Returns the existing travel permit if it matches the criteria.
	 * 
	 * @param permit travel permit
	 * @param startDate start date
	 * @param offender offender
	 * @return existing travel permit
	 */
	TravelPermit findExcludedExistingTravelPermit(TravelPermit permit,
		Offender offender, Date startDate);
	
	/**
	 * Returns the existing travel permits by offender.
	 * 
	 * @param offender offender
	 * @return existing travel permits
	 */
	List<TravelPermit> findByOffender(Offender offender);
}