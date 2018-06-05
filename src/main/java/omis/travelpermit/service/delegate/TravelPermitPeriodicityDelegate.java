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

import omis.instance.factory.InstanceFactory;
import omis.travelpermit.dao.TravelPermitPeriodicityDao;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.exception.TravelPermitPeriodicityExistsException;


/**
 * Delegate for travel method.
 *
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (June 06, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitPeriodicityDelegate {
	
	/* Resources. */
	private final TravelPermitPeriodicityDao travelPermitPeriodicityDao;
	private final InstanceFactory<TravelPermitPeriodicity> travelPermitPeriodicityInstanceFactory;
	
	/* Constructors. */
	/**
	 * Instantiates delegate for managing travel method.
	 * 
	 * @param travelMethodDao data access object for travel method
	 * @param travelPermitPeriodicityInstanceFactory travel permit periodicity instance factory
	 */
	public TravelPermitPeriodicityDelegate(
		final TravelPermitPeriodicityDao travelPermitPeriodicityDao,
		final InstanceFactory<TravelPermitPeriodicity> travelPermitPeriodicityInstanceFactory) {
		this.travelPermitPeriodicityDao = travelPermitPeriodicityDao;
		this.travelPermitPeriodicityInstanceFactory = travelPermitPeriodicityInstanceFactory;
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
	
	/**
	 * Creates a travel permit periodicity with the specified name and sort order.
	 * 
	 * @param name name
	 * @param sortOrder sort order
	 * @return newly created travel permit periodicity
	 * @throws TravelPermitPeriodicityExistsException Thrown when a travel permit periodicity already
	 * exists 
	 */
	public TravelPermitPeriodicity create(final String name, final Short sortOrder)
			throws TravelPermitPeriodicityExistsException {
		if (this.travelPermitPeriodicityDao.find(name) != null) {
			throw new TravelPermitPeriodicityExistsException("Duplicate travel permit periodicity found");
		}
		TravelPermitPeriodicity periodicity = this.travelPermitPeriodicityInstanceFactory.createInstance();
		periodicity.setName(name);
		periodicity.setSortOrder(sortOrder);
		return this.travelPermitPeriodicityDao.makePersistent(periodicity);
	}
	
	
}