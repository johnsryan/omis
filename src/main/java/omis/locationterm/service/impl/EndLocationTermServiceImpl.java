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
package omis.locationterm.service.impl;

import java.util.Date;

import omis.locationterm.domain.LocationTerm;
import omis.locationterm.service.EndLocationTermService;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;

/**
 * Implementation of service to end location terms.
 * 
 * @deprecated update location term instead
 * @author Stephen Abson
 * @version 0.0.1 (Feb 5, 2018)
 * @since OMIS 3.0
 */
@Deprecated
public class EndLocationTermServiceImpl
		implements EndLocationTermService {
	
	private final LocationTermDelegate locationTermDelegate;

	/**
	 * Instantiates implementation of service to end location term.s
	 * 
	 * @param locationTermDelegate delegate for location terms
	 */
	public EndLocationTermServiceImpl(
			final LocationTermDelegate locationTermDelegate) {
		this.locationTermDelegate = locationTermDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm endLocationTerm(
			final Offender offender, final Date effectiveDate) {
		return this.locationTermDelegate.endLocationTerm(
				offender, effectiveDate);
	}
}
