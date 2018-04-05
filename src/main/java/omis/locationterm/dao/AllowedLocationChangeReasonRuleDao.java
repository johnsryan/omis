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
package omis.locationterm.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.location.domain.Location;
import omis.locationterm.domain.AllowedLocationChangeReasonRule;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationTermChangeAction;

/**
 * Data access object for allowed location change reason rule.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public interface AllowedLocationChangeReasonRuleDao
		extends GenericDao<AllowedLocationChangeReasonRule> {

	/**
	 * Returns location reasons for change action.
	 * 
	 * @param changeAction change action
	 * @return location reasons for change action
	 */
	List<LocationReason> findLocationReasonsForChangeAction(
			LocationTermChangeAction changeAction);
	
	/**
	 * Returns location reasons allowed for location.
	 * 
	 * @param location location
	 * @return location reasons allowed for location
	 */
	List<LocationReason> findLocationReasonsAllowedForLocation(
			Location location);
}