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
package omis.boardhearing.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Parole Board Hearing.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public interface BoardHearing extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the Board Hearing.
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the ID of the Board Hearing.
	 * @param id - Long
	 */
	void setId(Long id);
	
	/**
	 * Returns the Itinerary for the Board Hearing.
	 * @return itinerary - ParoleBoardItinerary
	 */
	ParoleBoardItinerary getItinerary();
	
	/**
	 * Sets the Itinerary for the Board Hearing.
	 * @param itinerary - ParoleBoardItinerary
	 */
	void setItinerary(ParoleBoardItinerary itinerary);
	
	/**
	 * Returns the HearingDate for the Board Hearing.
	 * @return hearingDate - Date
	 */
	Date getHearingDate();
	
	/**
	 * Sets the HearingDate for the Board Hearing.
	 * @param hearingDate - Date
	 */
	void setHearingDate(Date hearingDate);
	
	/**
	 * Returns the ParoleEligibility for the Board Hearing.
	 * @return paroleEligibility - ParoleEligibility
	 */
	ParoleEligibility getParoleEligibility();
	
	/**
	 * Sets the ParoleEligibility for the Board Hearing.
	 * @param paroleEligibility - ParoleEligibility
	 */
	void setParoleEligibility(ParoleEligibility paroleEligibility);
	
	/**
	 * Returns the BoardHearingCategory for the Board Hearing.
	 * @return boardHearingCategory - BoardHearingCategory
	 */
	BoardHearingCategory getCategory();
	
	/**
	 * Sets the BoardHearingCategory for the Board Hearing.
	 * @param category - BoardHearingCategory
	 */
	void setCategory(BoardHearingCategory category);
	
	/**
	 * Returns the Cancellation for the Board Hearing.
	 * @return cancellation - CancellationCategory
	 */
	CancellationCategory getCancellation();
	
	/**
	 * Sets the Cancellation for the Board Hearing.
	 * @param cancellation - CancellationCategory
	 */
	void setCancellation(CancellationCategory cancellation);
	
	/**
	 * Returns the VideoConference for the Board Hearing.
	 * @return videoConference - Boolean
	 */
	Boolean getVideoConference();
	
	/**
	 * Sets the VideoConference for the Board Hearing.
	 * @param videoConference - Boolean
	 */
	void setVideoConference(Boolean videoConference);
	
	/** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	int hashCode();
}