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

import omis.demographics.dao.TribeDao;
import omis.demographics.domain.Tribe;
import omis.instance.factory.InstanceFactory;

/**
 * TribeDelegate.java.
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Nov 10, 2016)
 *@since OMIS 3.0
 *
 */
public class TribeDelegate {
	
	private final TribeDao tribeDao;
	private final InstanceFactory<Tribe> tribeInstanceFactory;

	/**
	 * Contructor for TribeDelegate.
	 * @param tribeDao tribe data access object
	 * @param tribeInstanceFactory tribe instance factory
	 */
	public TribeDelegate(final TribeDao tribeDao, 
			final InstanceFactory<Tribe> tribeInstanceFactory) {
		this.tribeDao = tribeDao;
		this.tribeInstanceFactory = tribeInstanceFactory;
	}
	
	/**
	 * Returns a list of all Tribes.
	 * @return List of all Tribes
	 */
	public List<Tribe> findAll() {
		return this.tribeDao.findAll();
	}
	
	/**
	 * Returns tribe.
	 *
	 *
	 * @param name name
	 * @param valid valid
	 * @return tribe
	 */
	public Tribe create(final String name, final Boolean valid) {
		Tribe tribe = this.tribeInstanceFactory.createInstance();
		tribe.setName(name);
		tribe.setValid(valid);
		return this.tribeDao.makePersistent(tribe);		
	}
}