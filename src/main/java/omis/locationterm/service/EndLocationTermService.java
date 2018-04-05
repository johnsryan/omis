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
package omis.locationterm.service;

import java.util.Date;

import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;

/**
 * Service to end location term.
 * 
 * @deprecated update location term instead
 * @author Stephen Abson
 * @version 0.0.1 (Feb 5, 2018)
 * @since OMIS 3.0
 */
@Deprecated
public interface EndLocationTermService {

	/**
	 * Ends location term.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return ended location term
	 */
	LocationTerm endLocationTerm(Offender offender, Date effectiveDate);
}