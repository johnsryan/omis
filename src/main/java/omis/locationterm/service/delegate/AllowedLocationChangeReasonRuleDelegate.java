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
import omis.locationterm.dao.AllowedLocationChangeReasonRuleDao;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationTermChangeAction;

/**
 * Delegate for allowed location change reason rules.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class AllowedLocationChangeReasonRuleDelegate {

	private final AllowedLocationChangeReasonRuleDao
	allowedLocationChangeReasonRuleDao;
	
	/**
	 * Instantiates allowed location change reason rule delegate.
	 * 
	 * @param allowedLocationChangeReasonRuleDao data access for allowed
	 * location change reason rule
	 */
	public AllowedLocationChangeReasonRuleDelegate(
			final AllowedLocationChangeReasonRuleDao
				allowedLocationChangeReasonRuleDao) {
		this.allowedLocationChangeReasonRuleDao
			= allowedLocationChangeReasonRuleDao;
	}
	
	/**
	 * Returns location reasons by change action.
	 * 
	 * @param changeAction location reasons by change action
	 * @return location reasons by changge action
	 */
	public List<LocationReason> findLocationReasonsForChangeAction(
			final LocationTermChangeAction changeAction) {
		return this.allowedLocationChangeReasonRuleDao
				.findLocationReasonsForChangeAction(changeAction);
	}
	
	/**
	 * Returns location reasons allowed for location.
	 * 
	 * @param location location
	 * @return location reasons allowed for location
	 */
	public List<LocationReason> findLocationReasonsAllowedForLocation(
			final Location location) {
		return this.allowedLocationChangeReasonRuleDao
				.findLocationReasonsAllowedForLocation(location);
	}
}