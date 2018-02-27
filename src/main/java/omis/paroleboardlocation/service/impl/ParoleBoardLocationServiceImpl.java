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
package omis.paroleboardlocation.service.impl;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.ParoleBoardLocationService;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;

/**
 * Implementation of service for parole board location.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardLocationServiceImpl 
		implements ParoleBoardLocationService {

	/* Delegates. */
	
	private final ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/**
	 * Instantiates a parole board location service implementation with the 
	 * specified delegates.
	 * 
	 * @param paroleBoardLocationDelegate parole board location delegate
	 */
	public ParoleBoardLocationServiceImpl(
			final ParoleBoardLocationDelegate paroleBoardLocationDelegate) {
		this.paroleBoardLocationDelegate = paroleBoardLocationDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public ParoleBoardLocation createParoleBoardLocation(
			final Location location, final Boolean videoConferenceCapable)
					throws DuplicateEntityFoundException {
		return this.paroleBoardLocationDelegate.create(location, 
				videoConferenceCapable);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardLocation updateParoleBoardLocation(
			final ParoleBoardLocation paroleBoardLocation, 
			final Location location, final Boolean videoConferenceCapable) 
					throws DuplicateEntityFoundException {
		return this.paroleBoardLocationDelegate.update(paroleBoardLocation, 
				location, videoConferenceCapable);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParoleBoardLocation(
			final ParoleBoardLocation paroleBoardLocation) {
		this.paroleBoardLocationDelegate.remove(paroleBoardLocation);
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findUnassociatedSupervisoryOrganizationLocations(
			final ParoleBoardLocation paroleBoardLocation) {
		return this.paroleBoardLocationDelegate
				.findUnassociatedSupervisoryOrganizationLocations(
						paroleBoardLocation);
	}
}