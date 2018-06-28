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

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.supervision.dao.PlacementTermChangeReasonDao;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.exception.PlacementTermChangeReasonExistsException;

/**
 * Delegate for placement term change reasons.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 23, 2016)
 * @since OMIS 3.0
 */
public class PlacementTermChangeReasonDelegate {
	
	private final PlacementTermChangeReasonDao placementTermChangeReasonDao;
	
	private final InstanceFactory<PlacementTermChangeReason>
	placementTermChangeReasonInstanceFactory;

	/**
	 * Instantiates delegate for placement term change reasons.
	 * 
	 * @param placementTermChangeReasonInstanceFactory instance factory
	 * for placement term change reasons
	 * @param placementTermChangeReasonDao data access object for placement
	 * term change reasons
	 */
	public PlacementTermChangeReasonDelegate(
			final InstanceFactory<PlacementTermChangeReason>
				placementTermChangeReasonInstanceFactory,
			final PlacementTermChangeReasonDao
				placementTermChangeReasonDao) {
		this.placementTermChangeReasonInstanceFactory
			= placementTermChangeReasonInstanceFactory;
		this.placementTermChangeReasonDao = placementTermChangeReasonDao;
	}

	/**
	 * Returns allowed placement term change reasons.
	 * 
	 * @param fromCorrectionalStatus from correctional status
	 * @param toCorrectionalStatus to correctional status
	 * @return allowed placement term change reason
	 */
	public List<PlacementTermChangeReason> findAllowed(
			final CorrectionalStatus fromCorrectionalStatus,
			final CorrectionalStatus toCorrectionalStatus) {
		return this.placementTermChangeReasonDao
				.findAllowed(fromCorrectionalStatus, toCorrectionalStatus);
	}

	/**
	 * Creates placement term change reason.
	 * 
	 * @param name name
	 * @param sortOrder sort order
	 * @param validStartReason whether valid as start reason
	 * @param validEndReason whether valid as end reason
	 * @return placement term change reason
	 * @throws PlacementTermChangeReasonExistsException if placement term change
	 * reason exists
	 */
	public PlacementTermChangeReason create(
			final String name, final Short sortOrder,
			final Boolean validStartReason, final Boolean validEndReason)
				throws PlacementTermChangeReasonExistsException {
		if (this.placementTermChangeReasonDao.find(name) != null) {
			throw new PlacementTermChangeReasonExistsException(
					"Change reason exists");
		}
		PlacementTermChangeReason changeReason
			= this.placementTermChangeReasonInstanceFactory.createInstance();
		changeReason.setName(name);
		changeReason.setSortOrder(sortOrder);
		changeReason.setValidEndReason(validEndReason);
		changeReason.setValidStartReason(validStartReason);
		return this.placementTermChangeReasonDao
				.makePersistent(changeReason);
	}
}