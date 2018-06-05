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
import omis.travelpermit.dao.TravelMethodDao;
import omis.travelpermit.domain.TravelMethod;


/**
 * Delegate for travel method.
 *
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (June 04, 2018)
 * @since OMIS 3.0
 */
public class TravelMethodDelegate {
	
	/* Resources. */
	
	private final TravelMethodDao travelMethodDao;
	private final InstanceFactory<TravelMethod> travelMethodInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing travel method.
	 * 
	 * @param travelMethodDao data access object for travel method
	 * @param travelMethodInstanceFactory travel method instance factory
	 */
	public TravelMethodDelegate( final TravelMethodDao travelMethodDao,
		final InstanceFactory<TravelMethod> travelMethodInstanceFactory) {
		this.travelMethodDao = travelMethodDao;
		this.travelMethodInstanceFactory = travelMethodInstanceFactory;
	}

	/**
	 * Find travel methods
	 * 
	 * @return a list of all travel method
	 *
	 */
	public List<TravelMethod> findTravelMethods(){
		return this.travelMethodDao.findTravelMethods();
	}
	
	/**
	 * Creates a travel method.
	 * 
	 * @param name name
	 * @param descriptionRequired description required
	 * @param descriptionName description name
	 * @param numberName number name
	 * @param sortOrder sort order
	 * @return newly create travel method
	 */
	public TravelMethod create(final String name, final Boolean descriptionRequired,
			final String descriptionName, final String numberName,
			final Short sortOrder) {
		TravelMethod method = this.travelMethodInstanceFactory.createInstance();
		method.setName(name);
		method.setDescriptionRequired(descriptionRequired);
		method.setDescriptionName(descriptionName);
		method.setNumberName(numberName);
		method.setSortOrder(sortOrder);
		return this.travelMethodDao.makePersistent(method);
	}
}