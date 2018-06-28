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
import omis.supervision.dao.AllowedCorrectionalStatusChangeDao;
import omis.supervision.domain.AllowedCorrectionalStatusChange;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTermChangeAction;
import omis.supervision.exception.AllowedCorrectionalStatusChangeExistsException;

/**
 * Delegate for allowed correctional status changes.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 23, 2016)
 * @since OMIS 3.0
 */
public class AllowedCorrectionalStatusChangeDelegate {
	
	private final InstanceFactory<AllowedCorrectionalStatusChange>
	allowedCorrectionalStatusChangeInstanceFactory;
	
	private final AllowedCorrectionalStatusChangeDao
	allowedCorrectionalStatusChangeDao;
	
	/**
	 * Instantiates delegate for allowed correctional status changes.
	 * 
	 * @param allowedCorrectionalStatusChangeInstanceFactory instance factory
	 * for allowed correctional status changes
	 * @param allowedCorrectionalStatusChangeDao data access object for allowed
	 * correctional status changes
	 */
	public AllowedCorrectionalStatusChangeDelegate(
			final InstanceFactory<AllowedCorrectionalStatusChange>
				allowedCorrectionalStatusChangeInstanceFactory,
			final AllowedCorrectionalStatusChangeDao
				allowedCorrectionalStatusChangeDao) {
		this.allowedCorrectionalStatusChangeInstanceFactory
			= allowedCorrectionalStatusChangeInstanceFactory;
		this.allowedCorrectionalStatusChangeDao
			= allowedCorrectionalStatusChangeDao;
	}

	/**
	 * Returns allowed correctional status change.
	 * 
	 * @param fromCorrectionalStatus
	 * @param toCorrectionalStatus
	 * @return allowed correctional status change
	 */
	public AllowedCorrectionalStatusChange find(
			final CorrectionalStatus fromCorrectionalStatus,
			final CorrectionalStatus toCorrectionalStatus) {
		return this.allowedCorrectionalStatusChangeDao.find(
				fromCorrectionalStatus, toCorrectionalStatus);
	}
	
	/**
	 * Creates allowed correctional status change.
	 * 
	 * @param action action
	 * @param fromCorrectionalStatus from correctional status
	 * @param toCorrectionalStatus to correctional status
	 * @return allowed correctional status change
	 * @throws AllowedCorrectionalStatusChangeExistsException if allowed
	 * correctional status change exists
	 */
	public AllowedCorrectionalStatusChange create(
			final PlacementTermChangeAction action,
			final CorrectionalStatus fromCorrectionalStatus,
			final CorrectionalStatus toCorrectionalStatus)
				throws AllowedCorrectionalStatusChangeExistsException {
		if (this.allowedCorrectionalStatusChangeDao
				.find(fromCorrectionalStatus, toCorrectionalStatus) != null) {
			throw new AllowedCorrectionalStatusChangeExistsException(
					"Allowed correctional status change exists");
		}
		AllowedCorrectionalStatusChange allowedChange
			= this.allowedCorrectionalStatusChangeInstanceFactory
				.createInstance();
		allowedChange.setAction(action);
		allowedChange.setFromCorrectionalStatus(fromCorrectionalStatus);
		allowedChange.setToCorrectionalStatus(toCorrectionalStatus);
		return this.allowedCorrectionalStatusChangeDao
				.makePersistent(allowedChange);
	}
}