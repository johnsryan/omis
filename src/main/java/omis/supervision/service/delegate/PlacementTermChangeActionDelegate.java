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
package omis.supervision.service.delegate;

import omis.instance.factory.InstanceFactory;
import omis.supervision.dao.PlacementTermChangeActionDao;
import omis.supervision.domain.PlacementTermChangeAction;
import omis.supervision.exception.PlacementTermChangeActionExistsException;

/**
 * Delegate for placement term change actions.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jun 21, 2016)
 * @since OMIS 3.0
 */
public class PlacementTermChangeActionDelegate {

	private final InstanceFactory<PlacementTermChangeAction>
	placementTermChangeActionInstanceFactory;
	
	private final PlacementTermChangeActionDao placementTermChangeActionDao;
	
	/**
	 * Instantiates delegate for placement term change action.
	 * 
	 * @param placementTermChangeActionInstanceFactory instance factory for
	 * placement term change actions
	 * @param placementTermChangeActionDao data access object for placement
	 * term change actions
	 */
	public PlacementTermChangeActionDelegate(
			final InstanceFactory<PlacementTermChangeAction>
			placementTermChangeActionInstanceFactory,
			final PlacementTermChangeActionDao placementTermChangeActionDao) {
		this.placementTermChangeActionInstanceFactory
			= placementTermChangeActionInstanceFactory;
		this.placementTermChangeActionDao = placementTermChangeActionDao;
	}
	
	/**
	 * Creates placement term change action.
	 * 
	 * @param name name
	 * @return placement term change action
	 * @throws PlacementTermChangeActionExistsException if change action exists
	 */
	public PlacementTermChangeAction create(final String name)
			throws PlacementTermChangeActionExistsException {
		if (this.placementTermChangeActionDao.find(name) != null) {
			throw new PlacementTermChangeActionExistsException(
					"Placement term change action");
		}
		PlacementTermChangeAction changeAction
			= this.placementTermChangeActionInstanceFactory
				.createInstance();
		changeAction.setName(name);
		return this.placementTermChangeActionDao.makePersistent(changeAction);
	}
}