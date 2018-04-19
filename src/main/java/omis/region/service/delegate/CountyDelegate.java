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
package omis.region.service.delegate;

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.region.dao.CountyDao;
import omis.region.domain.County;
import omis.region.domain.State;
import omis.region.exception.CountyExistsException;

/**
 * Delegate for counties.
 * 
 *@author Annie Jacques
 *@author Stephen Abson 
 *@version 0.1.0 (May 9, 2017)
 *@since OMIS 3.0
 */
public class CountyDelegate {
	
	public final CountyDao countyDao;
	
	public final InstanceFactory<County> countyInstanceFactory;

	/**
	 * Contructor for CountyDelegate
	 * @param countyDao
	 */
	public CountyDelegate(final CountyDao countyDao,
			final InstanceFactory<County> countyInstanceFactory) {
		this.countyDao = countyDao;
		this.countyInstanceFactory = countyInstanceFactory;
	}
	
	/**
	 * Returns a list of all Countys
	 * @return List of all Countys
	 */
	public List<County> findAll() {
		return this.countyDao.findAll();
	}
	
	/**
	 * Returns all counties for the specified State.
	 * 
	 * @param state State whose counties to return
	 * @return counties for specified State
	 */
	public List<County> findByState(final State state){
		return this.countyDao.findByState(state);
	}
	
	/**
	 * Creates a County.
	 * 
	 * @param name name
	 * @param state State
	 * @param valid whether valid
	 * @return Newly Created County
	 * @throws CountyExistsException if county exists
	 */
	public County create(final String name, final State state,
			final Boolean valid) throws CountyExistsException {
		if (this.countyDao.find(name, state) != null) {
			throw new CountyExistsException("County exists");
		}
		County county = this.countyInstanceFactory.createInstance();
		
		county.setName(name);
		county.setState(state);
		county.setValid(valid);
		
		return this.countyDao.makePersistent(county);
	}
}
