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
import omis.region.dao.StateDao;
import omis.region.domain.State;
import omis.region.exception.StateExistsException;

/**
 * Delegate for States.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jul 14, 2015)
 * @since OMIS 3.0
 */
public class StateDelegate {
	
	/* Instance factories. */
	
	private final InstanceFactory<State> stateInstanceFactory;
	
	/* Data access objects. */
	
	private final StateDao stateDao;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for States.
	 * 
	 * @param stateInstanceFactory instance factory for States
	 * @param stateDao data access object for States
	 */
	public StateDelegate(
			final InstanceFactory<State> stateInstanceFactory,
			final StateDao stateDao) {
		this.stateInstanceFactory = stateInstanceFactory;
		this.stateDao = stateDao;
	}
	
	/* Methods. */
	
	/**
	 * Counts States by country.
	 * 
	 * @param country country
	 * @return number of States in country
	 */
	public long countByCountry(final Country country) {
		return this.stateDao.countByCountry(country);
	}

	/**
	 * Returns States by country.
	 * 
	 * @param country country
	 * @return States by country
	 */
	public List<State> findByCountry(final Country country) {
		return this.stateDao.findByCountry(country);
	}
	
	/**
	 * Returns home State.
	 * 
	 * @return State
	 */
	public State findHomeState() {
		return this.stateDao.findHomeState();
	}
	
	/**
	 * Returns State.
	 * 
	 * <p>If State does not exist, creates and returns it.
	 * 
	 * @param name name
	 * @param abbreviation abbreviation
	 * @param country country
	 * @param home whether home State
	 * @param valid whether valid
	 * @return State
	 */
	public State findOrCreate(
			final String name, final String abbreviation, final Country country,
			final Boolean home, final Boolean valid) {
		State state = this.stateDao.find(name, country);
		if (state != null) {
			return state;
		} else {
			return this.createImpl(name, abbreviation, country, home, valid);
		}
	}
	
	/**
	 * Creates state.
	 * 
	 * @param name name
	 * @param abbreviation abbreviation
	 * @param country country
	 * @param home home
	 * @param valid whether valid
	 * @return created state
	 * @throws StateExistsException if state exists
	 */
	public State create(final String name, final String abbreviation,
			final Country country, final Boolean home, final Boolean valid)
				throws StateExistsException {
		if (this.stateDao.find(name, country) != null) {
			throw new StateExistsException("Country exists");
		}
		return this.createImpl(name, abbreviation, country, home, valid);
	}
	
	/**
	 * Returns all states
	 * @return list of states
	 */
	public List<State> findAll() {
		return this.stateDao.findAll();
	}
	
	// Creates State
	private State createImpl(
			final String name, final String abbreviation, final Country country,
			final Boolean home, final Boolean valid) {
		State state = this.stateInstanceFactory.createInstance();
		state.setName(name);
		state.setAbbreviation(abbreviation);
		state.setCountry(country);
		state.setHome(home);
		state.setValid(valid);
		return this.stateDao.makePersistent(state);
	}

	/**
	 * Returns States in home country.
	 * 
	 * @return States in home country
	 */
	public List<State> findInHomeCountry() {
		return this.stateDao.findInHomeCountry();
	}
}