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
package omis.boardhearing.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.boardhearing.domain.CancellationCategory;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardmember.domain.ParoleBoardMember;

/**
 * Board Hearing Form.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ParoleBoardItinerary paroleBoardItinerary;
	
	private Date hearingDate;
	
	private BoardHearingCategory boardHearingCategory;
	
	private Boolean videoConferenceCapable;
	
	private Boolean videoConference;
	
	private BoardHearingParticipant boardHearingParticipant1;
	
	private ParoleBoardMember boardMember1;

	private BoardHearingParticipant boardHearingParticipant2;
	
	private ParoleBoardMember boardMember2;
	
	private BoardHearingParticipant boardHearingParticipant3;
	
	private ParoleBoardMember boardMember3;
	
	private Boolean cancelled;
	
	private CancellationCategory reason;
	
	private List<BoardHearingNoteItem> boardHearingNoteItems =
			new ArrayList<BoardHearingNoteItem>();
	
	/**
	 * Default constructor for Board Hearing Form. 
	 */
	public BoardHearingForm() {
	}
	
	/**
	 * Returns the paroleBoardItinerary.
	 * @return paroleBoardItinerary - ParoleBoardItinerary
	 */
	public ParoleBoardItinerary getParoleBoardItinerary() {
		return this.paroleBoardItinerary;
	}

	/**
	 * Sets the paroleBoardItinerary.
	 * @param paroleBoardItinerary - ParoleBoardItinerary
	 */
	public void setParoleBoardItinerary(
			final ParoleBoardItinerary paroleBoardItinerary) {
		this.paroleBoardItinerary = paroleBoardItinerary;
	}

	/**
	 * Returns the hearingDate.
	 * @return hearingDate - Date
	 */
	public Date getHearingDate() {
		return this.hearingDate;
	}

	/**
	 * Sets the hearingDate.
	 * @param hearingDate - Date
	 */
	public void setHearingDate(final Date hearingDate) {
		this.hearingDate = hearingDate;
	}
	
	/**
	 * Returns the boardHearingCategory.
	 * @return boardHearingCategory - BoardHearingCategory
	 */
	public BoardHearingCategory getBoardHearingCategory() {
		return this.boardHearingCategory;
	}

	/**
	 * Sets the boardHearingCategory.
	 * @param boardHearingCategory - BoardHearingCategory
	 */
	public void setBoardHearingCategory(
			final BoardHearingCategory boardHearingCategory) {
		this.boardHearingCategory = boardHearingCategory;
	}
	
	/**
	 * Returns the videoConferenceCapable.
	 * @return videoConferenceCapable - Boolean
	 */
	public Boolean getVideoConferenceCapable() {
		return this.videoConferenceCapable;
	}

	/**
	 * Sets the videoConferenceCapable.
	 * @param videoConferenceCapable - Boolean
	 */
	public void setVideoConferenceCapable(
			final Boolean videoConferenceCapable) {
		this.videoConferenceCapable = videoConferenceCapable;
	}

	/**
	 * Returns the videoConference.
	 * @return videoConference - Boolean
	 */
	public Boolean getVideoConference() {
		return this.videoConference;
	}

	/**
	 * Sets the videoConference.
	 * @param videoConference - Boolean
	 */
	public void setVideoConference(final Boolean videoConference) {
		this.videoConference = videoConference;
	}

	/**
	 * Returns the boardMember1.
	 * @return boardMember1 - ParoleBoardMember
	 */
	public ParoleBoardMember getBoardMember1() {
		return this.boardMember1;
	}

	/**
	 * Sets the boardMember1.
	 * @param boardMember1 - ParoleBoardMember
	 */
	public void setBoardMember1(final ParoleBoardMember boardMember1) {
		this.boardMember1 = boardMember1;
	}

	/**
	 * Returns the boardMember2.
	 * @return boardMember2 - ParoleBoardMember
	 */
	public ParoleBoardMember getBoardMember2() {
		return this.boardMember2;
	}

	/**
	 * Sets the boardMember2.
	 * @param boardMember2 - ParoleBoardMember
	 */
	public void setBoardMember2(final ParoleBoardMember boardMember2) {
		this.boardMember2 = boardMember2;
	}

	/**
	 * Returns the boardMember3.
	 * @return boardMember3 - ParoleBoardMember
	 */
	public ParoleBoardMember getBoardMember3() {
		return this.boardMember3;
	}

	/**
	 * Sets the boardMember3.
	 * @param boardMember3 - ParoleBoardMember
	 */
	public void setBoardMember3(final ParoleBoardMember boardMember3) {
		this.boardMember3 = boardMember3;
	}

	/**
	 * Returns the cancelled.
	 * @return cancelled - Boolean
	 */
	public Boolean getCancelled() {
		return this.cancelled;
	}

	/**
	 * Sets the cancelled.
	 * @param cancelled - Boolean
	 */
	public void setCancelled(final Boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Returns the reason.
	 * @return reason - CancellationCategory
	 */
	public CancellationCategory getReason() {
		return this.reason;
	}

	/**
	 * Sets the reason.
	 * @param reason - CancellationCategory
	 */
	public void setReason(final CancellationCategory reason) {
		this.reason = reason;
	}

	/**
	 * Returns the boardHearingNoteItems.
	 * @return boardHearingNoteItems - List of BoardHearingNoteItems
	 */
	public List<BoardHearingNoteItem> getBoardHearingNoteItems() {
		return this.boardHearingNoteItems;
	}

	/**
	 * Sets the boardHearingNoteItems.
	 * @param boardHearingNoteItems - List of BoardHearingNoteItems
	 */
	public void setBoardHearingNoteItems(
			final List<BoardHearingNoteItem> boardHearingNoteItems) {
		this.boardHearingNoteItems = boardHearingNoteItems;
	}

	/**
	 * Returns the boardHearingParticipant1.
	 * @return boardHearingParticipant1 - BoardHearingParticipant
	 */
	public BoardHearingParticipant getBoardHearingParticipant1() {
		return this.boardHearingParticipant1;
	}

	/**
	 * Sets the boardHearingParticipant1.
	 * @param boardHearingParticipant1 - BoardHearingParticipant
	 */
	public void setBoardHearingParticipant1(
			final BoardHearingParticipant boardHearingParticipant1) {
		this.boardHearingParticipant1 = boardHearingParticipant1;
	}

	/**
	 * Returns the boardHearingParticipant2.
	 * @return boardHearingParticipant2 - BoardHearingParticipant
	 */
	public BoardHearingParticipant getBoardHearingParticipant2() {
		return this.boardHearingParticipant2;
	}

	/**
	 * Sets the boardHearingParticipant2.
	 * @param boardHearingParticipant2 - BoardHearingParticipant
	 */
	public void setBoardHearingParticipant2(
			final BoardHearingParticipant boardHearingParticipant2) {
		this.boardHearingParticipant2 = boardHearingParticipant2;
	}

	/**
	 * Returns the boardHearingParticipant3.
	 * @return boardHearingParticipant3 - BoardHearingParticipant
	 */
	public BoardHearingParticipant getBoardHearingParticipant3() {
		return this.boardHearingParticipant3;
	}

	/**
	 * Sets the boardHearingParticipant3.
	 * @param boardHearingParticipant3 - BoardHearingParticipant
	 */
	public void setBoardHearingParticipant3(
			final BoardHearingParticipant boardHearingParticipant3) {
		this.boardHearingParticipant3 = boardHearingParticipant3;
	}
	
	
}
