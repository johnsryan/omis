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
package omis.region.dao;

import java.util.List;

import omis.country.domain.Country;
import omis.dao.GenericDao;
import omis.region.domain.State;

/**
 * Data access object for states.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.0 (Feb 6, 2013)
 * @since OMIS 3.0
 */
public interface StateDao
		extends GenericDao<State> {

	/**
	 * Returns all States in the specified country.
	 * @param country country the States of which to return
	 * @return States in specified country
	 */
	List<State> findByCountry(Country country);

	/**
	 * Returns the home state.
	 * 
	 * @return state
	 */
	State findHomeState();
	
	/**
	 * Returns States in home country.
	 * 
	 * @return States in home country
	 */
	List<State> findInHomeCountry();
	
	/**
	 * Counts States by country.
	 * 
	 * @param country country
	 * @return number of States in country
	 */
	long countByCountry(Country country);
	
	/**
	 * Returns State.
	 * 
	 * @param name name
	 * @param country country
	 * @return return State
	 */
	State find(String name, Country country);
}