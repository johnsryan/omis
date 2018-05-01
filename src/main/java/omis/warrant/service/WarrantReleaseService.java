/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.warrant.service;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.jail.domain.Jail;
import omis.person.domain.Person;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantRelease;
import omis.warrant.exception.WarrantReleaseExistsException;

/**
 * WarrantReleaseService.java
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (May 9, 2017)
 *@since OMIS 3.0
 *
 */
public interface WarrantReleaseService {
	
	/**
	 * Creates a WarrantRelease with the specified properties
	 * @param warrant - Warrant
	 * @param instructions - String
	 * @param releaseTimestamp - Date
	 * @param addressee - String
	 * @param clearedBy - Person
	 * @param clearedByDate - Date
	 * @return Newly created WarrantRelease
	 * @throws DuplicateEntityFoundException - When a WarrantRelease already
	 * exists with the specified Warrant
	 */
	public WarrantRelease create(Warrant warrant,
			String instructions, Date releaseTimestamp,
			String addressee, Person clearedBy,
			Date clearedByDate)
					throws WarrantReleaseExistsException;
	
	/**
	 * Updates a WarrantRelease with the specified properties
	 * @param warrantRelease - WarrantRelease to update
	 * @param instructions - String
	 * @param releaseTimestamp - Date
	 * @param addressee - String
	 * @param clearedBy - Person
	 * @param clearedByDate - Date
	 * @return Updated WarrantRelease
	 * @throws DuplicateEntityFoundException - When a WarrantRelease already
	 * exists with the specified WarrantRelease's Warrant
	 */
	public WarrantRelease update(WarrantRelease warrantRelease,
			String instructions,Date releaseTimestamp,
			String addressee, Person clearedBy,
			Date clearedByDate)
					throws WarrantReleaseExistsException;
	
	/**
	 * Removes a WarrantRelease
	 * @param warrantRelease - WarrantRelease to remove
	 */
	public void remove(WarrantRelease warrantRelease);
	
	/**
	 * Returns a WarrantRelease by specified Warrant
	 * @param warrant - Warrant
	 * @return WarrantRelease found by specified Warrant
	 */
	public WarrantRelease findByWarrant(Warrant warrant);
	
	/**
	 * Returns a list of all Jails
	 * @return List of all Jails
	 */
	public List<Jail> findAllJails();	
}