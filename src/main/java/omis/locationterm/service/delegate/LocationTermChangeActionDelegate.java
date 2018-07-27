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
import omis.locationterm.dao.LocationTermChangeActionDao;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.LocationTermChangeActionExistsException;

/**
 * Delegate for location term change actions.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class LocationTermChangeActionDelegate {

	/* Instance factories. */
	
	private final InstanceFactory<LocationTermChangeAction>
	locationTermChangeActionInstanceFactory;
	
	/* Data access objects. */
	
	private final LocationTermChangeActionDao locationTermChangeActionDao;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for location term change actions.
	 * 
	 * @param locationTermChangeActionInstanceFactory instance factory for
	 * location term change actions
	 * @param locationTermChangeActionDao data access object for location term
	 * change actions
	 */
	public LocationTermChangeActionDelegate(
			final InstanceFactory<LocationTermChangeAction>
				locationTermChangeActionInstanceFactory,
			final LocationTermChangeActionDao locationTermChangeActionDao) {
		this.locationTermChangeActionInstanceFactory
			= locationTermChangeActionInstanceFactory;
		this.locationTermChangeActionDao = locationTermChangeActionDao;
	}
	
	/* Methods. */
	
	/**
	 * Creates location term change action.
	 * 
	 * @param name name
	 * @param valid whether action is valid
	 * @return newly created location term change action
	 * @throws LocationTermChangeActionExistsException if location term change
	 * action exists
	 */
	public LocationTermChangeAction create(
				final String name, final Boolean valid)
			throws LocationTermChangeActionExistsException {
		if (this.locationTermChangeActionDao.find(name) != null) {
			throw new LocationTermChangeActionExistsException(
					"Location term change action exists");
		}
		LocationTermChangeAction changeAction
			= this.locationTermChangeActionInstanceFactory.createInstance();
		changeAction.setName(name);
		changeAction.setValid(valid);
		return this.locationTermChangeActionDao.makePersistent(changeAction);
	}
	
	/**
	 * Returns location term change actions.
	 * 
	 * @return location term change actions
	 */
	public List<LocationTermChangeAction> findAll() {
		return this.locationTermChangeActionDao.findAll();
	}
}