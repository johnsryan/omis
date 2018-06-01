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
package omis.communitysupervision.service.delegate;

import java.util.List;

import omis.communitysupervision.dao.CommunitySupervisionOfficeDao;
import omis.communitysupervision.domain.CommunitySupervisionOffice;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;

/**
 * Delegate for community supervision office.
 * 
 * @author Josh Divine
 * @version 0.1.1 (May 17, 2018)
 * @since OMIS 3.0
 */
public class CommunitySupervisionOfficeDelegate {

	/* Resources. */
	
	private final CommunitySupervisionOfficeDao communitySupervisionOfficeDao;
	
	private final InstanceFactory<CommunitySupervisionOffice> 
		communitySupervisionOfficeInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing community supervision office.
	 * 
	 * @param communitySupervisionOfficeDao data access object for 
	 * community supervision office
	 * @param communitySupervisionOfficeInstanceFactory instance factory
	 *  for community supervision offices
	 */
	public CommunitySupervisionOfficeDelegate(
			final CommunitySupervisionOfficeDao 
				communitySupervisionOfficeDao,
			final InstanceFactory<CommunitySupervisionOffice> 
				communitySupervisionOfficeInstanceFactory) {
		this.communitySupervisionOfficeDao = communitySupervisionOfficeDao;
		this.communitySupervisionOfficeInstanceFactory 
			= communitySupervisionOfficeInstanceFactory;
	}

	/**
	 * Creates a new community supervision office.
	 * 
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return created community supervision office
	 * @throws DuplicateEntityFoundException thrown when a duplicate community 
	 * supervision office is found
	 */
	public CommunitySupervisionOffice create(Location location, 
			String name, Long telephoneNumber) 
					throws DuplicateEntityFoundException {
		if (this.communitySupervisionOfficeDao.find(location, name, 
				telephoneNumber) != null) {
			throw new DuplicateEntityFoundException("Duplicate community "
					+ "supervision office found");
		}
		CommunitySupervisionOffice communitySupervisionOffice 
			= this.communitySupervisionOfficeInstanceFactory.createInstance();
		communitySupervisionOffice.setLocation(location);
		communitySupervisionOffice.setName(name);
		communitySupervisionOffice.setTelephoneNumber(telephoneNumber);
		return this.communitySupervisionOfficeDao.makePersistent(
				communitySupervisionOffice);
	}
	
	/**
	 * Updates the specified community supervision office.
	 * 
	 * @param communitySupervisionOffice community supervision office
	 * @param location location
	 * @param name name
	 * @param telephoneNumber telephone number
	 * @return updated community supervision office
	 * @throws DuplicateEntityFoundException thrown when a duplicate community 
	 * supervision office is found
	 */
	public CommunitySupervisionOffice update(
			CommunitySupervisionOffice communitySupervisionOffice,
		Location location, String name, Long telephoneNumber) 
				throws DuplicateEntityFoundException {
		if (this.communitySupervisionOfficeDao.findExcluding(location, 
				name, telephoneNumber, communitySupervisionOffice) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate community "
					+ "supervision office found");
		}
		communitySupervisionOffice.setLocation(location);
		communitySupervisionOffice.setName(name);
		communitySupervisionOffice.setTelephoneNumber(telephoneNumber);
		return this.communitySupervisionOfficeDao.makePersistent(
				communitySupervisionOffice);
	}
	
	/**
	 * Removes the specified community supervision office
	 * 
	 * @param communitySupervisionOffice community supervision office
	 */
	public void remove(CommunitySupervisionOffice 
			communitySupervisionOffice) {
		this.communitySupervisionOfficeDao.makeTransient(
				communitySupervisionOffice);
	}

	/**
	 * Returns a list of all community supervision offices.
	 * 
	 * @return list of all community supervision offices
	 */
	public List<CommunitySupervisionOffice> findAll() {
		return this.communitySupervisionOfficeDao.findAll();
	}
}