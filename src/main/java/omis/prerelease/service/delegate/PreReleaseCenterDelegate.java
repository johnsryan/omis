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
package omis.prerelease.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.prerelease.dao.PreReleaseCenterDao;
import omis.prerelease.domain.PreReleaseCenter;

/**
 * Delegate for prerelease center.
 * 
 * @author Josh Divine
 * @version 0.1.1 (May 17, 2018)
 * @since OMIS 3.0
 */
public class PreReleaseCenterDelegate {
	
	/* Resources. */
	
	private final PreReleaseCenterDao preReleaseCenterDao;
	
	private final InstanceFactory<PreReleaseCenter> 
		preReleaseCenterInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing prerelease center.
	 * 
	 * @param preReleaseCenterDao data access object for prerelease center
	 * @param preReleaseCenterInstanceFactory instance factory for prerelease 
	 * center
	 */
	public PreReleaseCenterDelegate(
			final PreReleaseCenterDao preReleaseCenterDao,
			final InstanceFactory<PreReleaseCenter> 
				preReleaseCenterInstanceFactory) {
		this.preReleaseCenterDao = preReleaseCenterDao;
		this.preReleaseCenterInstanceFactory = preReleaseCenterInstanceFactory;
	}
	
	/**
	 * Creates a new prerelease center.
	 * 
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return created community supervision office
	 * @throws DuplicateEntityFoundException thrown when a duplicate prerelease 
	 * center is found
	 */
	public PreReleaseCenter create(Location location, String name, 
			Long telephoneNumber) throws DuplicateEntityFoundException {
		if (this.preReleaseCenterDao.find(location, name, telephoneNumber) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate prerelease "
					+ "center found");
		}
		PreReleaseCenter preReleaseCenter 
			= this.preReleaseCenterInstanceFactory.createInstance();
		preReleaseCenter.setLocation(location);
		preReleaseCenter.setName(name);
		preReleaseCenter.setTelephoneNumber(telephoneNumber);
		return this.preReleaseCenterDao.makePersistent(preReleaseCenter);
	}
	
	/**
	 * Updates the specified prerelease center.
	 * 
	 * @param preReleaseCenter prerelease center
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return updated prerelease center
	 * @throws DuplicateEntityFoundException thrown when a duplicate prerelease 
	 * center is found
	 */
	public PreReleaseCenter update(PreReleaseCenter preReleaseCenter, 
			Location location, String name, Long telephoneNumber) 
				throws DuplicateEntityFoundException {
		if (this.preReleaseCenterDao.findExcluding(location, 
				name, telephoneNumber, preReleaseCenter) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate prerelease "
					+ "center found");
		}
		preReleaseCenter.setLocation(location);
		preReleaseCenter.setName(name);
		preReleaseCenter.setTelephoneNumber(telephoneNumber);
		return this.preReleaseCenterDao.makePersistent(preReleaseCenter);
	}
	
	/**
	 * Removes the specified prerelease center.
	 * 
	 * @param preReleaseCenter prerelease center
	 */
	public void remove(PreReleaseCenter preReleaseCenter) {
		this.preReleaseCenterDao.makeTransient(preReleaseCenter);
	}

	/**
	 * Returns a list of all prerelease centers.
	 * 
	 * @return list of all prerelease centers
	 */
	public List<PreReleaseCenter> findAll() {
		return this.preReleaseCenterDao.findAll();
	}
}