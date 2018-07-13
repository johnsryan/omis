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
package omis.paroleeligibility.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.offender.domain.Offender;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Parole Eligibility Data Access Object.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.2 (Jul 3, 2018)
 * @since OMIS 3.0
 */
public interface ParoleEligibilityDao extends GenericDao<ParoleEligibility> {

	/**
	 * Finds a parole eligibility.
	 * 
	 * @param offender offender
	 * @param hearingEligibilityDate hearing eligibility date
	 * @return parole eligibility
	 */
	ParoleEligibility find(Offender offender, Date hearingEligibilityDate);

	/**
	 * Finds a parole eligibility, not including the specified parole 
	 * eligibility.
	 * @param paroleEligibility parole eligibility
	 * @param hearingEligibilityDate hearing eligibility date
	 * @param offender offender
	 * @return parole eligibility, not including the specified parole
	 * eligibility
	 */
	ParoleEligibility findExcluding(ParoleEligibility paroleEligibility, 
			Offender offender, Date hearingEligibilityDate);
	
	/**
	 * Returns a list of Parole Eligibilities for an offender after the
	 * given date.
	 * 
	 * @param offender Offender
	 * @param date Date
	 * @return List of Parole Eligibilities for an offender after the
	 * given date.
	 */
	List<ParoleEligibility> findByOffenderAfterDate(
			Offender offender, Date date);
	
	/**
	 * Returns a list of Parole Eligibilities by the specified Parole Board
	 * Itinerary.
	 * 
	 * @param itinerary - Parole Board Itinerary
	 * @return List of Parole Eligibilities by the specified Parole Board
	 * Itinerary.
	 */
	List<ParoleEligibility> findByItinerary(ParoleBoardItinerary itinerary);
	
	/**
	 * Returns a list of Parole Eligibilities that have no scheduled hearing.
	 * 
	 * @return List of Parole Eligibilities that have no scheduled hearing.
	 */
	List<ParoleEligibility> findUnscheduled();
	
	/**
	 * Returns a list of unscheduled Parole Eligibilities as well as
	 * Parole Eligibilities by the the specified Parole Board Itinerary.
	 * 
	 * @param including - Parole Board Itinerary
	 * @return List of unscheduled Parole Eligibilities as well as
	 * Parole Eligibilities by the the specified Parole Board Itinerary.
	 */
	List<ParoleEligibility> findUnscheduledIncluding(
			ParoleBoardItinerary including);
	
}
