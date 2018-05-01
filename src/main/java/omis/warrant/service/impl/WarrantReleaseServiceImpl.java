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
package omis.warrant.service.impl;

import java.util.Date;
import java.util.List;

import omis.jail.domain.Jail;
import omis.jail.service.delegate.JailDelegate;
import omis.person.domain.Person;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantRelease;
import omis.warrant.exception.WarrantReleaseExistsException;
import omis.warrant.service.WarrantReleaseService;
import omis.warrant.service.delegate.WarrantReleaseDelegate;

/**
 * WarrantReleaseServiceImpl.java
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (May 9, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantReleaseServiceImpl implements WarrantReleaseService {
	
	private final WarrantReleaseDelegate warrantReleaseDelegate;
	
	private final JailDelegate jailDelegate;
	
	/**
	 * 
	 * Instantiates a warrant release service with the specified delegates.
	 * 
	 * @param warrantReleaseDelegate warrant release delegate
	 * @param jailDelegate jail delegate
	 */
	public WarrantReleaseServiceImpl(
			final WarrantReleaseDelegate warrantReleaseDelegate,
			final JailDelegate jailDelegate) {
		this.warrantReleaseDelegate = warrantReleaseDelegate;
		this.jailDelegate = jailDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public WarrantRelease create(
			final Warrant warrant, final String instructions,
			final Date releaseTimestamp, final String addressee,
			final Person clearedBy, final Date clearedByDate)
					throws WarrantReleaseExistsException {
		return this.warrantReleaseDelegate.create(
				warrant, instructions, releaseTimestamp,
				addressee, clearedBy, clearedByDate);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantRelease update(
			final WarrantRelease warrantRelease, final String instructions,
			final Date releaseTimestamp, final String addressee,
			final Person clearedBy, final Date clearedByDate)
					throws WarrantReleaseExistsException {
		return this.warrantReleaseDelegate.update(
				warrantRelease, instructions, releaseTimestamp,
				addressee, clearedBy, clearedByDate);
	}

	/**{@inheritDoc} */
	@Override
	public void remove(final WarrantRelease warrantRelease) {
		this.warrantReleaseDelegate.remove(warrantRelease);
	}

	/**{@inheritDoc} */
	@Override
	public WarrantRelease findByWarrant(final Warrant warrant) {
		return this.warrantReleaseDelegate.findByWarrant(warrant);
	}

	/**{@inheritDoc} */
	@Override
	public List<Jail> findAllJails() {
		return this.jailDelegate.findAll();
	}
}