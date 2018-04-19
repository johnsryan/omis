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
package omis.hearinganalysis.dao;

import omis.dao.GenericDao;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Data access object for hearing analysis.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public interface HearingAnalysisDao extends GenericDao<HearingAnalysis> {

	/**
	 * Returns the hearing analysis matching the specified parole eligibility 
	 * and analyst.
	 * 
	 * @param eligibility parole eligibility
	 * @param paroleBoardItinerary parole board itinerary
	 * @param category hearing analysis category
	 * @param analyst board attendee
	 * @return hearing analysis
	 */
	HearingAnalysis find(ParoleEligibility eligibility, 
			ParoleBoardItinerary paroleBoardItinerary,
			HearingAnalysisCategory category, BoardAttendee analyst);
	
	/**
	 * Returns the hearing analysis matching the specified parole eligibility, 
	 * and analyst excluding the specified hearing analysis.
	 * 
	 * @param eligibility parole eligibility
	 * @param paroleBoardItinerary parole board itinerary
	 * @param category hearing analysis category
	 * @param analyst board attendee
	 * @param excludedHearingAnalysis excluded hearing analysis
	 * @return hearing analysis
	 */
	HearingAnalysis findExcluding(ParoleEligibility eligibility, 
			ParoleBoardItinerary paroleBoardItinerary,
			HearingAnalysisCategory category, BoardAttendee analyst, 
			HearingAnalysis excludedHearingAnalysis);

	/**
	 * Returns the hearing analysis for the specified parole eligibility.
	 * 
	 * @param eligibility parole eligibility
	 * @return hearing analysis
	 */
	HearingAnalysis findByParoleEligibility(ParoleEligibility eligibility);
}