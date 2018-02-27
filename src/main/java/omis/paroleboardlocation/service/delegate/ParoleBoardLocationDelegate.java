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
package omis.paroleboardlocation.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.paroleboardlocation.dao.ParoleBoardLocationDao;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Parole Board Location Delegate.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationDelegate {
	
	private final ParoleBoardLocationDao paroleBoardLocationDao;
	
	private final InstanceFactory<ParoleBoardLocation>
			paroleBoardLocationInstanceFactory;
	
	/**
	 * Contructor for ParoleBoardLocationDelegate.
	 * @param paroleBoardLocationDao - Parole Board Location DAO
	 * @param paroleBoardLocationInstanceFactory - Parole Board Location
	 * Instance Factory
	 */
	public ParoleBoardLocationDelegate(
			final ParoleBoardLocationDao paroleBoardLocationDao,
			final InstanceFactory<ParoleBoardLocation>
				paroleBoardLocationInstanceFactory) {
		this.paroleBoardLocationDao = paroleBoardLocationDao;
		this.paroleBoardLocationInstanceFactory =
				paroleBoardLocationInstanceFactory;
	}
	
	/**
	 * Returns a list of all ParoleBoardLocations.
	 * @return List of all ParoleBoardLocations
	 */
	public List<ParoleBoardLocation> findAll() {
		return this.paroleBoardLocationDao.findAll();
	}
	
	/**
	 * Creates a Parole Board Location for the purposes of unit testing.
	 * 
	 * @param location - Location
	 * @param videoConferenceCapable - Video Conference Capability
	 * @return Newly created Parole Board Location
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleBoardLocation create(final Location location,
			final Boolean videoConferenceCapable) 
					throws DuplicateEntityFoundException {
		if (this.paroleBoardLocationDao.find(location) != null) {
			throw new DuplicateEntityFoundException(
					"Parole board location already exists.");
		}
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationInstanceFactory.createInstance();
		populateParoleBoardLocation(paroleBoardLocation, location, 
				videoConferenceCapable);
		return this.paroleBoardLocationDao.makePersistent(paroleBoardLocation);
	}

	/**
	 * Updates an existing parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @param location location
	 * @param videoConferenceCapable video conference capable
	 * @return parole board location
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleBoardLocation update(
			final ParoleBoardLocation paroleBoardLocation, 
			final Location location, final Boolean videoConferenceCapable) 
					throws DuplicateEntityFoundException {
		if (this.paroleBoardLocationDao.findExcluding(location, 
				paroleBoardLocation) != null) {
			throw new DuplicateEntityFoundException(
					"Parole board location already exists.");
		}
		populateParoleBoardLocation(paroleBoardLocation, location, 
				videoConferenceCapable);
		return this.paroleBoardLocationDao.makePersistent(paroleBoardLocation);
	}

	/**
	 * Removes the specified parole board location.
	 * 
	 * @param paroleBoardLocation parole board location
	 */
	public void remove(final ParoleBoardLocation paroleBoardLocation) {
		this.paroleBoardLocationDao.makeTransient(paroleBoardLocation);
	}

	/**
	 * Returns a list of supervisory organization locations that are not 
	 * currently associated with a parole board location, excluding the 
	 * specified parole board location.
	 * 
	 * @param paroleBoardLocation parole board location to exclude
	 * @return list of supervisory organization locations
	 */
	public List<Location> findUnassociatedSupervisoryOrganizationLocations(
			final ParoleBoardLocation paroleBoardLocation) {
		return this.paroleBoardLocationDao
				.findUnassociatedSupervisoryOrganizationLocations(
						paroleBoardLocation);
	}
	
	private void populateParoleBoardLocation(
			final ParoleBoardLocation paroleBoardLocation, 
			final Location location, final Boolean videoConferenceCapable) {
		paroleBoardLocation.setLocation(location);
		paroleBoardLocation.setVideoConferenceCapable(videoConferenceCapable);
	}
}