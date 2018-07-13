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
import java.util.Date;

import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingCategory;
import omis.boardhearing.domain.BoardHearingParticipant;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Board Hearing Item.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jun 28, 2018)
 *@since OMIS 3.0
 *
 */
public class BoardHearingItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private BoardHearing boardHearing;
	
	private ParoleEligibility paroleEligibility;
	
	private Boolean selected;
	
	private Date hearingDate;
	
	private BoardHearingCategory category;
	
	private Boolean videoConference;
	
	private BoardHearingParticipant boardHearingParticipant1;
	
	private ParoleBoardMember boardMember1;

	private BoardHearingParticipant boardHearingParticipant2;
	
	private ParoleBoardMember boardMember2;
	
	private BoardHearingParticipant boardHearingParticipant3;
	
	private ParoleBoardMember boardMember3;
	
	private BoardHearingItemOperation itemOperation;
	
	
	/**
	 * Instantiates Board Hearing Item.
	 */
	public BoardHearingItem() {
		//default constructor
	}
	
	/**
	 * Returns the boardHearing.
	 * @return boardHearing - BoardHearing
	 */
	public BoardHearing getBoardHearing() {
		return this.boardHearing;
	}

	/**
	 * Sets the boardHearing.
	 * @param boardHearing - BoardHearing
	 */
	public void setBoardHearing(final BoardHearing boardHearing) {
		this.boardHearing = boardHearing;
	}
	
	/**
	 * Returns the paroleEligibility.
	 * @return paroleEligibility - ParoleEligibility
	 */
	public ParoleEligibility getParoleEligibility() {
		return this.paroleEligibility;
	}

	/**
	 * Sets the paroleEligibility.
	 * @param paroleEligibility - ParoleEligibility
	 */
	public void setParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		this.paroleEligibility = paroleEligibility;
	}
	
	/**
	 * Returns the selected.
	 * @return selected - Boolean
	 */
	public Boolean getSelected() {
		return this.selected;
	}

	/**
	 * Sets the selected.
	 * @param selected - Boolean
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
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
	 * Returns the category.
	 * @return category - BoardHearingCategory
	 */
	public BoardHearingCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the category.
	 * @param category - BoardHearingCategory
	 */
	public void setCategory(final BoardHearingCategory category) {
		this.category = category;
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
	 * Returns the itemOperation.
	 * @return itemOperation - BoardHearingItemOperation
	 */
	public BoardHearingItemOperation getItemOperation() {
		return this.itemOperation;
	}

	/**
	 * Sets the itemOperation.
	 * @param itemOperation - BoardHearingItemOperation
	 */
	public void setItemOperation(
			final BoardHearingItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
	
	
	
}
