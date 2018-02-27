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
package omis.paroleboardlocation.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.location.domain.Location;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Parole Board Location Data Access Object.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public interface ParoleBoardLocationDao
		extends GenericDao<ParoleBoardLocation> {

	/**
	 * Finds the parole board location matching the specified parameters.
	 * 
	 * @param location location
	 * @return parole board location
	 */
	ParoleBoardLocation find(Location location);

	/**
	 * Finds the parole board location matching the specified parameters.
	 * 
	 * @param location location
	 * @param excludedParoleBoardLocation excluded parole board location
	 * @return parole board location
	 */
	ParoleBoardLocation findExcluding(Location location, 
			ParoleBoardLocation excludedParoleBoardLocation);

	/**
	 * Returns a list of supervisory organization locations that are not 
	 * currently associated with a parole board location, excluding the 
	 * specified parole board location.
	 * 
	 * @param paroleBoardLocation parole board location to exclude
	 * @return list of supervisory organization locations
	 */
	List<Location> findUnassociatedSupervisoryOrganizationLocations(final ParoleBoardLocation paroleBoardLocation);
}