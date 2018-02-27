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
package omis.paroleeligibility.report;

import java.util.List;

import omis.hearinganalysis.domain.HearingAnalysis;
import omis.offender.domain.Offender;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Report service for parole eligibilities.
 *
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public interface ParoleEligibilityReportService {
	
	/**
	 * Returns a list of parole eligibility summaries for the specified 
	 * offender.
	 * 
	 * @param offender offender
	 * @return list of parole eligibility summaries
	 */
	List<ParoleEligibilitySummary> findByOffender(Offender offender);
	
	/**
	 * Finds the hearing analysis for the specified parole eligibility.
	 * 
	 * @param eligibility parole eligibility
	 * @return hearing analysis
	 */
	HearingAnalysis findHearingAnalysisByParoleEligibility(
			ParoleEligibility eligibility);

}
