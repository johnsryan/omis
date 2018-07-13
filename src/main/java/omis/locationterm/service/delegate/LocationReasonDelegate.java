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
package omis.locationterm.service.delegate;

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.locationterm.dao.LocationReasonDao;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.exception.LocationReasonExistsException;

/**
 * Delegate for location reasons.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Nov 2, 2015)
 * @since OMIS 3.0
 */
public class LocationReasonDelegate {

	/* Instance factories. */
	
	private final InstanceFactory<LocationReason>
	locationReasonInstanceFactory;
	
	/* DAOs. */
	
	private final LocationReasonDao locationReasonDao;
	
	/* Constructors. */
	
	/**
	 * Instantiates data access object for location reasons.
	 * 
	 * @param locationReasonInstanceFactory instance factory for
	 * location reasons
	 * @param locationReasonDao data access object for location reasons
	 */
	public LocationReasonDelegate(
			final InstanceFactory<LocationReason>
				locationReasonInstanceFactory,
			final LocationReasonDao locationReasonDao) {
		this.locationReasonInstanceFactory
			= locationReasonInstanceFactory;
		this.locationReasonDao = locationReasonDao;
	}
	
	/* Methods. */
	
	/**
	 * Returns location reasons.
	 * 
	 * @return location reasons
	 */
	public List<LocationReason> findAll() {
		return this.locationReasonDao.findAll();
	}

	/**
	 * Creates location reason.
	 * 
	 * @param name name
	 * @param sortOrder sort order
	 * @param valid whether valid
	 * @return creates location reason
	 * @throws DuplicateEntityFoundException if reason exists
	 */
	public LocationReason create(
			final String name, final Short sortOrder, final Boolean valid)
				throws LocationReasonExistsException {
		if (this.locationReasonDao.find(name) != null) {
			throw new LocationReasonExistsException("Location reason exists");
		}
		LocationReason reason = this.locationReasonInstanceFactory
				.createInstance();
		reason.setName(name);
		reason.setSortOrder(sortOrder);
		reason.setValid(valid);
		return this.locationReasonDao.makePersistent(reason);
	}
}