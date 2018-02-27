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
package omis.paroleboardlocation.service;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Service for parole board location.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public interface ParoleBoardLocationService {

	/**
	 * Creates a new parole board location.
	 * 
	 * @param location location
	 * @param videoConferenceCapable video conference capable
	 * @return parole board location
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleBoardLocation createParoleBoardLocation(Location location, 
			Boolean videoConferenceCapable) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @param location location
	 * @param videoConferenceCapable video conference capable
	 * @return parole board location
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	ParoleBoardLocation updateParoleBoardLocation(
			ParoleBoardLocation paroleBoardLocation, Location location, 
			Boolean videoConferenceCapable) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 */
	void removeParoleBoardLocation(ParoleBoardLocation paroleBoardLocation);
	
	/**
	 * Returns a list of supervisory organization locations that are not 
	 * currently associated with a parole board location, excluding the 
	 * specified parole board location.
	 * 
	 * @param paroleBoardLocation parole board location to exclude
	 * @return list of supervisory organization locations
	 */
	List<Location> findUnassociatedSupervisoryOrganizationLocations(ParoleBoardLocation paroleBoardLocation);
}