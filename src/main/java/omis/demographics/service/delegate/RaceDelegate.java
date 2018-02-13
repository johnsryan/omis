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
package omis.demographics.service.delegate;

import java.util.List;

import omis.demographics.dao.RaceDao;
import omis.demographics.domain.Race;
import omis.instance.factory.InstanceFactory;

/**
 * RaceDelegate.java.
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Nov 10, 2016)
 *@since OMIS 3.0
 *
 */
public class RaceDelegate {
	/* Data access objects. */
	private final RaceDao raceDao;
	
	/* Instance factories. */
	private final InstanceFactory<Race> raceInstanceFactory;

	/**
	 * Contructor for RaceDelegate.
	 * @param raceDao race data access object
	 * @param raceInstanceFactory race instance factory
	 */
	public RaceDelegate(final RaceDao raceDao, 
			final InstanceFactory<Race> raceInstanceFactory) {
		this.raceDao = raceDao;
		this.raceInstanceFactory = raceInstanceFactory;
	}
	
	/**
	 * Returns a list of all Races.
	 * @return List of all Races
	 */
	public List<Race> findAll() {
		return this.raceDao.findAll();
	}
	
	/**
	 * Returns a newly created race.
	 *
	 *
	 * @param name name
	 * @param valid valid
	 * @return race
	 */
	public Race create(final String name, final Boolean valid) {
		
		Race race = this.raceInstanceFactory.createInstance();
		race.setName(name);
		race.setValid(valid);
		return this.raceDao.makePersistent(race);
	}	
}