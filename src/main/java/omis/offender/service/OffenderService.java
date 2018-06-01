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
package omis.offender.service;

import omis.offender.domain.Offender;

/**
 * Service for offenders.
 * 
 * <p>This service will be removed.
 * 
 * @author Stephen Abson
 * @version 0.2.0 (Jan 7, 2016)
 * @since OMIS 3.0
 */
public interface OffenderService {
	
	/**
	 * Throws {@code UnsupportedOperationException}.
	 * 
	 * @param offenderNumber offender number of offender to return
	 * @return does not return
	 */
	Offender findByOffenderNumber(Integer offenderNumber);
}