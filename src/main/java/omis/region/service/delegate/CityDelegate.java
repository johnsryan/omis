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

import omis.country.domain.Country;
import omis.instance.factory.InstanceFactory;
import omis.region.dao.CityDao;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;

/**
 * Delegate for cities.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jul 11, 2015)
 * @since OMIS 3.0
 */
public class CityDelegate {
	
	/* Data access objects. */
	
	private final CityDao cityDao;
	
	/* Instance factories. */
	
	private final InstanceFactory<City> cityInstanceFactory;

	/**
	 * Instantiates delegate for cities.
	 * 
	 * @param cityDao data access object for cities
	 * @param cityInstanceFactory instance factory for cities
	 */
	public CityDelegate(final CityDao cityDao,
			final InstanceFactory<City> cityInstanceFactory) {
		this.cityDao = cityDao;
		this.cityInstanceFactory = cityInstanceFactory;
	}
	
	/**
	 * Creates city.
	 * 
	 * @param name name
	 * @param state State
	 * @param country country
	 * @return new city
	 * @throws CityExistsException if city exists
	 */
	public City create(
			final String name, final Boolean valid, final State state,
			final Country country)
				throws CityExistsException {
		
		// If State is provided, checks that country of State equals country 
		if (state != null && !state.getCountry().equals(country)) {
			throw new IllegalArgumentException("State not in country");
		}
		
		// Checks if city exists
		if (this.cityDao.find(name, state, country) != null) {
			throw new CityExistsException("City exists");
		}
		
		// Creates new city
		City city = this.cityInstanceFactory.createInstance();
		city.setName(name);
		city.setValid(valid);
		city.setState(state);
		city.setCountry(country);
		return this.cityDao.makePersistent(city);
	}
	
	/**
	 * Returns cities by State.
	 * 
	 * @param state State
	 * @return cities by State
	 */
	public List<City> findByState(final State state) {
		return this.cityDao.findByState(state);
	}
	
	/**
	 * Returns cities by country.
	 * 
	 * @param country country
	 * @return cities by country
	 */
	public List<City> findByCountry(final Country country) {
		return this.cityDao.findByCountry(country);
	}
	
	/**
	 * Returns cities by country that do not have a State.
	 * 
	 * @param country country
	 * @return cities by country that do not have a State
	 */
	public List<City> findByCountryWithoutState(final Country country) {
		return this.cityDao.findByCountryWithoutState(country);
	}

	/**
	 * Returns city.
	 * 
	 * <p>If city does not exist, creates and returns city.
	 * 
	 * @param name name of city
	 * @param valid whether valid
	 * @param state State
	 * @param country country
	 * @return city
	 */
	public City findOrCreate(final String name, final Boolean valid,
			final State state, final Country country) {
		City city = this.cityDao.find(name, state, country);
		if (city != null) {
			return city;
		} else {
			return this.createImpl(name, valid, state, country);
		}
	}
	
	// Creates city
	private City createImpl(final String name, final Boolean valid,
			final State state, final Country country) {
		City city = this.cityInstanceFactory.createInstance();
		city.setName(name);
		city.setValid(valid);
		city.setState(state);
		city.setCountry(country);
		return this.cityDao.makePersistent(city);
	}
}