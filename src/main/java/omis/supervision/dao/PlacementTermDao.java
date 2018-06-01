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
package omis.supervision.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.offender.domain.Offender;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganizationTerm;

/**
 * Data access object for placement terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Oct 9, 2013)
 * @since OMIS 3.0
 */
public interface PlacementTermDao
		extends GenericDao<PlacementTerm> {

	/**
	 * Returns placement terms for the specified offender.
	 * 
	 * @param offender offender
	 * @return placement terms for offender
	 */
	List<PlacementTerm> findByOffender(Offender offender);
	
	/**
	 * Returns placement term for the offender on the date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return placement term for offender on date
	 */
	PlacementTerm findForOffenderOnDate(Offender offender, Date date);
	
	/**
	 * Returns placement term with specified property values.
	 * 
	 * <p>Returns {@code null} if no such placement term exists.
	 * 
	 * @param offender offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param correctionalStatusTerm correctional status term
	 * @param supervisoryOrganizationTerm supervisory organization term
	 * @return placement term with specified property values or {@code null}
	 * if no such placement term exists
	 */
	PlacementTerm find(Offender offender, Date startDate, Date endDate,
			CorrectionalStatusTerm correctionalStatusTerm,
			SupervisoryOrganizationTerm supervisoryOrganizationTerm);
	
	/**
	 * Returns placement term with specified property values.
	 * 
	 * <p>Returns {@code null} if no such placement term exists that is not
	 * {@code excludedPlacementTerm}.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param correctionalStatusTerm correctional status term
	 * @param supervisoryOrganizationTerm supervisory organization term
	 * @param excludedPlacementTerm placement term to exclude
	 * @return placement term with specified property values or {@code null}
	 * if no such placement term exists that is not
	 * {@code excludedPlacementTerm}
	 */
	PlacementTerm findExcluding(Offender offender, Date startDate,
			Date endDate, CorrectionalStatusTerm correctionalStatusTerm,
			SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			PlacementTerm excludedPlacementTerm);
	
	/**
	 * Returns number of placement terms for offender between dates.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of placement terms for offender between dates
	 */
	long countForOffenderBetweenDates(
			Offender offender, Date startDate, Date endDate);
	
	/**
	 * Returns number of placement terms for offender between dates excluding
	 * selected placement terms.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedPlacementTerms placement terms to exclude
	 * @return number of placement terms for offender between dates
	 */
	long countForOffenderBetweenDatesExcluding(
			Offender offender, Date startDate, Date endDate,
			PlacementTerm... excludedPlacementTerms);
	
	/**
	 * Returns placement terms with correctional status term.
	 * 
	 * @param correctionalStatusTerm correctional status term
	 * @return placement terms with correctional status term
	 */
	List<PlacementTerm> findByCorrectionalStatusTerm(
			CorrectionalStatusTerm correctionalStatusTerm);
	
	/**
	 * Returns number of placement terms with correctional status term.
	 * 
	 * @param correctionalStatusTerm correctional status term
	 * @return number of placement terms with correctional status term
	 */
	long countByCorrectionalStatusTerm(
			CorrectionalStatusTerm correctionalStatusTerm);
	
	/**
	 * Returns placement terms with supervisory organization term.
	 * 
	 * @param supervisoryOrganizationTerm supervisory organization term
	 * @return placement terms with supervisory organization term
	 */
	List<PlacementTerm> findBySupervisoryOrganizationTerm(
			SupervisoryOrganizationTerm supervisoryOrganizationTerm);
	
	/**
	 * Returns number of placement terms with supervisory organization term.
	 * 
	 * @param supervisoryOrganizationTerm supervisory organization term
	 * @return number of placement terms with supervisory organization term
	 */
	long countBySupervisoryOrganizationTerm(
			SupervisoryOrganizationTerm supervisoryOrganizationTerm);
	
	/**
	 * Returns placement term with end date.
	 * 
	 * <p>Note that the placement term is not active on end date (end date is
	 * not inclusive of active term).
	 * 
	 * @param offender offender
	 * @param endDate end date
	 * @return placement term with end date
	 */
	PlacementTerm findForOffenderWithEndDate(Offender offender, Date endDate);
	
	/**
	 * Returns number of placement terms for an offender that exists after the 
	 * date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @param excludedPlacementTerm excluded placement term
	 * @return number of placement terms for an offender that exists after the 
	 * date
	 */
	long countForOffenderAfterDate(Offender offender, Date date, 
			PlacementTerm excludedPlacementTerm);
	
	/**
	 * Returns number of placement terms for an offender that exists before the 
	 * date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @param excludedPlacementTerm excluded placement term
	 * @return number of placement terms for an offender that exists before the 
	 * date
	 */
	long countForOffenderBeforeDate(Offender offender, Date date, 
			PlacementTerm excludedPlacementTerm);
}