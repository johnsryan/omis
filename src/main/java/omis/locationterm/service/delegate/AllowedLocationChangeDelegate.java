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

import omis.location.domain.Location;
import omis.locationterm.dao.AllowedLocationChangeDao;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.region.domain.State;
import omis.supervision.domain.CorrectionalStatus;

/**
 * Delegate for allowed location changes.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Nov 5, 2015)
 * @since OMIS 3.0
 */
public class AllowedLocationChangeDelegate {

	/* DAOs. */
	
	private final AllowedLocationChangeDao allowedLocationChangeDao;

	/* Constructors. */
	
	/**
	 * Instantiates delegate for allowed location changes.
	 * 
	 * @param allowedLocationChangeDao data access object for allowed location
	 * changes
	 */
	public AllowedLocationChangeDelegate(
			final AllowedLocationChangeDao allowedLocationChangeDao) {
		this.allowedLocationChangeDao = allowedLocationChangeDao;
	}
	
	/* Methods. */
	
	/**
	 * Returns locations allowed for action while on correctional status.
	 * 
	 * @param action action
	 * @param correctionalStatus correctional status
	 * @return allowed locations
	 */
	public List<Location> findLocationsForActionForCorrectionalStatus(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus) {
		return this.allowedLocationChangeDao
				.findLocationsForActionForCorrectionalStatus(
						action, correctionalStatus);
	}

	/**
	 * Returns locations allowed for action while on correctional status in
	 * State.
	 * 
	 * @param action action
	 * @param correctionalStatus correctional status
	 * @param state State
	 * @return allowed location
	 */
	public List<Location> findLocationsForActionForCorrectionalStatusInState(
			final LocationTermChangeAction action,
			final CorrectionalStatus correctionalStatus,
			final State state) {
		return this.allowedLocationChangeDao
				.findLocationsForActionForCorrectionalStatusInState(
						action, correctionalStatus, state);
	}
	
	/**
	 * Returns locations allowed for any change.
	 *  
	 * <p>Effectively, this methods returns any location at which an offender
	 * can be housed.
	 *  
	 * @return locations allowed for any change
	 */
	public List<Location> findLocationsAllowedForAnyChange() {
		return this.allowedLocationChangeDao.findLocationsAllowedForAnyChange();
	}
	
	/**
	 * Returns locations allowed for any change in State.
	 *  
	 * <p>Effectively, this methods returns any location at which an offender
	 * can be housed in State.
	 * 
	 * @param state State
	 * @return locations allowed for any change in State
	 */
	public List<Location> findLocationsAllowedForAnyChangeInState(
			final State state) {
		return this.allowedLocationChangeDao
				.findLocationsAllowedForAnyChangeInState(state);
	}
}