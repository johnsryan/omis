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
package omis.hearinganalysis.web.form;

import java.io.Serializable;
import java.util.List;

import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;

/**
 * Hearing analysis form.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ParoleBoardItinerary boardItinerary;
	
	private HearingAnalysisCategory category;
	
	private BoardAttendee analyst;
	
	private List<HearingAnalysisNoteItem> hearingAnalysisNoteItems;
	
	/**
	 * Instantiates a default hearing analysis form. 
	 */
	public HearingAnalysisForm() {
		// Default instantiation
	}

	/**
	 * Returns the parole board itinerary.
	 * 
	 * @return parole board itinerary
	 */
	public ParoleBoardItinerary getBoardItinerary() {
		return boardItinerary;
	}

	/**
	 * Sets the parole board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 */
	public void setBoardItinerary(final ParoleBoardItinerary boardItinerary) {
		this.boardItinerary = boardItinerary;
	}

	/**
	 * Returns the hearing analysis category.
	 * 
	 * @return hearing analysis category
	 */
	public HearingAnalysisCategory getCategory() {
		return category;
	}

	/**
	 * Sets the hearing analysis category.
	 * 
	 * @param category hearing analysis category
	 */
	public void setCategory(HearingAnalysisCategory category) {
		this.category = category;
	}

	/**
	 * Returns the analyst.
	 * 
	 * @return analyst
	 */
	public BoardAttendee getAnalyst() {
		return analyst;
	}

	/**
	 * Sets the analyst.
	 * 
	 * @param analyst board attendee
	 */
	public void setAnalyst(final BoardAttendee analyst) {
		this.analyst = analyst;
	}

	/**
	 * Returns the list of hearing analysis note items.
	 * 
	 * @return list of hearing analysis note items
	 */
	public List<HearingAnalysisNoteItem> getHearingAnalysisNoteItems() {
		return hearingAnalysisNoteItems;
	}

	/**
	 * Sets the list of hearing analysis note items.
	 *  
	 * @param hearingAnalysisNoteItems list of hearing analysis note items
	 */
	public void setHearingAnalysisNoteItems(
			final List<HearingAnalysisNoteItem> hearingAnalysisNoteItems) {
		this.hearingAnalysisNoteItems = hearingAnalysisNoteItems;
	}
}