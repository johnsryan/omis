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
package omis.hearinganalysis.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Hearing analysis.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public interface HearingAnalysis extends Creatable, Updatable {

	/**
	 * Sets the ID of the hearing analysis.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the hearing analysis.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the parole eligibility.
	 * 
	 * @param eligibility parole eligibility
	 */
	void setEligibility(ParoleEligibility eligibility);
	
	/**
	 * Returns the parole eligibility.
	 * 
	 * @return parole eligibility
	 */
	ParoleEligibility getEligibility();
	
	/**
	 * Sets the hearing analysis category.
	 * 
	 * @param category hearing analysis category
	 */
	void setCategory(HearingAnalysisCategory category);
	
	/**
	 * Returns the hearing analysis category.
	 * 
	 * @return hearing analysis category
	 */
	HearingAnalysisCategory getCategory();
	
	/**
	 * Sets the analyst.
	 * 
	 * @param analyst board attendee
	 */
	void setAnalyst(BoardAttendee analyst);
	
	/**
	 * Returns the analyst.
	 * 
	 * @return analyst
	 */
	BoardAttendee getAnalyst();
	
	/**
	 * Sets the parole board itinerary.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 */
	void setParoleBoardItinerary(ParoleBoardItinerary paroleBoardItinerary);
	
	/**
	 * Returns the parole board itinerary.
	 * 
	 * @return parole board itinerary
	 */
	ParoleBoardItinerary getParoleBoardItinerary();
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}
