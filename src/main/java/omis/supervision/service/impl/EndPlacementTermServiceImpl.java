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
package omis.supervision.service.impl;

import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
import omis.supervision.service.EndPlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;

/**
 * Implementation of service for ending placement terms.
 * 
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.1.1 (Dec 14, 2017)
 * @since OMIS 3.0
 */
public class EndPlacementTermServiceImpl implements EndPlacementTermService {

	private final PlacementTermDelegate placementTermDelegate;
	
	private final PlacementTermChangeReasonDelegate 
		placementTermChangeReasonDelegate;
	
	private final CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	private final SupervisoryOrganizationTermDelegate 
		supervisoryOrganizationTermDelegate;
	
	private final LocationTermDelegate locationTermDelegate;
	
	private final LocationReasonTermDelegate locationReasonTermDelegate;
	
	
	/**
	 * Instantiates an implementation of service for ending placement terms.
	 * 
	 * @param placementTermDelegate placement term delegate
	 * @param placementTermChangeReasonDelegate placement term change reason 
	 * delegate
	 */
	public EndPlacementTermServiceImpl(
			final PlacementTermDelegate placementTermDelegate, 
			final PlacementTermChangeReasonDelegate 
				placementTermChangeReasonDelegate,
			final CorrectionalStatusTermDelegate correctionalStatusTermDelegate,
			final SupervisoryOrganizationTermDelegate 
				supervisoryOrganizationTermDelegate,
			final LocationTermDelegate locationTermDelegate,
			final LocationReasonTermDelegate locationReasonTermDelegate) {
		this.placementTermDelegate = placementTermDelegate;
		this.placementTermChangeReasonDelegate 
			= placementTermChangeReasonDelegate;
		this.correctionalStatusTermDelegate = correctionalStatusTermDelegate;
		this.supervisoryOrganizationTermDelegate 
			= supervisoryOrganizationTermDelegate;
		this.locationTermDelegate = locationTermDelegate;
		this.locationReasonTermDelegate = locationReasonTermDelegate;
	}
	
	
	/** {@inheritDoc} */
	@Override
	public PlacementTerm endPlacementTerm(
			final PlacementTerm placementTerm, 
			final Date endDate,
			final PlacementTermChangeReason endChangeReason)
				throws CorrectionalStatusTermExistsException,
					SupervisoryOrganizationTermExistsException,
					PlacementTermExistsException,
					LocationTermExistsException {
		//End correctional status term if available
		CorrectionalStatusTerm correctionalStatusTerm 
			= placementTerm.getCorrectionalStatusTerm();
		if (correctionalStatusTerm != null) {
			correctionalStatusTerm = this.correctionalStatusTermDelegate.update(
					correctionalStatusTerm, 
					new DateRange(DateRange.getStartDate(correctionalStatusTerm
							.getDateRange()), endDate), 
					correctionalStatusTerm.getCorrectionalStatus());
		} else {
			throw new RuntimeException("Correctional status term required.");
		}
		//End supervisory organization term if available
		SupervisoryOrganizationTerm supervisoryOrganizationTerm
			= placementTerm.getSupervisoryOrganizationTerm();
		if (supervisoryOrganizationTerm != null) {
			supervisoryOrganizationTerm	
				= this.supervisoryOrganizationTermDelegate.update(
						supervisoryOrganizationTerm, 
						new DateRange(DateRange.getStartDate(
								supervisoryOrganizationTerm.getDateRange()), 
								endDate), 
						supervisoryOrganizationTerm
								.getSupervisoryOrganization());
		}
		LocationReasonTerm locationReasonTerm 
			= this.locationReasonTermDelegate.findForOffenderOnDate(
					placementTerm.getOffender(), endDate);
		if (locationReasonTerm != null) {
			try {
				this.locationReasonTermDelegate.changeDateRange(locationReasonTerm, 
						DateRange.getStartDate(locationReasonTerm.getDateRange()),
						endDate);
			} catch (LocationReasonTermExistsException e) {
				
				// An existing reason term with the same start date would be
				// bad data - don't recover - SA
				throw new AssertionError("Reason term exists", e);
			}
		}
		LocationTerm locationTerm 
			= this.locationTermDelegate.findByOffenderOnDate(
					placementTerm.getOffender(), endDate);
		if (locationTerm != null) {
			this.locationTermDelegate.changeDateRange(locationTerm, 
					DateRange.getStartDate(locationTerm.getDateRange()),
					endDate);
		}
		return this.placementTermDelegate.update(placementTerm, 
				placementTerm.getStatus(), placementTerm.getStatusDateRange(), 
				new DateRange(DateRange.getStartDate(
						placementTerm.getDateRange()), endDate), 
				supervisoryOrganizationTerm, 
				correctionalStatusTerm, 
				placementTerm.getStartChangeReason(), endChangeReason,
				placementTerm.getLocked());
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTermChangeReason> findAllowedEndChangeReasons(
			final CorrectionalStatus fromCorrectionalStatus) {
		return this.placementTermChangeReasonDelegate
				.findAllowed(fromCorrectionalStatus, null);
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findPlacementTerm(final Offender offender, 
			final Date effectiveDate) {
		return this.placementTermDelegate.findForOffenderOnDate(offender, 
				effectiveDate);
	}

}
