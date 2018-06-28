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
package omis.supervision.service;

import java.util.Date;
import java.util.List;

import omis.locationterm.exception.LocationTermExistsException;
import omis.offender.domain.Offender;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;

/**
 * Service for ending placement terms.
 * 
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.1.0 (Feb 14, 2017)
 * @since OMIS 3.0
 */
public interface EndPlacementTermService {

	/**
	 * Ends the specified placement term with the provided end date and change 
	 * reason.
	 * 
	 * @param placementTerm placement term
	 * @param endDate end date
	 * @param endChangeReason end change reason
	 * @return placement term
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws LocationTermExistsException if location term exists
	 */
	PlacementTerm endPlacementTerm(PlacementTerm placementTerm, Date endDate, 
			PlacementTermChangeReason endChangeReason) 
					throws CorrectionalStatusTermExistsException,
						SupervisoryOrganizationTermExistsException,
						PlacementTermExistsException,
						LocationTermExistsException;
	
	/**
	 * Returns the list of allowed end change reasons for the specified 
	 * correctional status.
	 * 
	 * @param fromCorrectionalStatus current correctional status
	 * @return placement term change reasons
	 */
	List<PlacementTermChangeReason> findAllowedEndChangeReasons(
			CorrectionalStatus fromCorrectionalStatus);
	
	/**
	 * Finds the placement term for the specified offender on the specified 
	 * date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return placement term
	 */
	PlacementTerm findPlacementTerm(Offender offender, Date effectiveDate);
}
