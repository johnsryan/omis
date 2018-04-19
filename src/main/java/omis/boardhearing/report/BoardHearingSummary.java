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
package omis.boardhearing.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Board Hearing Summary.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class BoardHearingSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Long boardHearingId;
	
	private final Date hearingDate;
	
	private final String hearingStatusName;
	
	private final String appearanceCategoryName;
	
	private final String hearingAnalystLastName;
	
	private final String hearingAnalystMiddleName;
	
	private final String hearingAnalystFirstName;
	
	private final String decisionName;

	/**
	 * @param boardHearingId - Long
	 * @param hearingDate - Date
	 * @param hearingStatusName - String
	 * @param appearanceCategoryName - String
	 * @param hearingAnalystLastName - String
	 * @param hearingAnalystMiddleName - String
	 * @param hearingAnalystFirstName - String
	 * @param decisionName - String
	 */
	public BoardHearingSummary(final Long boardHearingId,
			final Date hearingDate, final String hearingStatusName,
			final String appearanceCategoryName,
			final String hearingAnalystLastName,
			final String hearingAnalystMiddleName,
			final String hearingAnalystFirstName,
			final String decisionName) {
		this.boardHearingId = boardHearingId;
		this.hearingDate = hearingDate;
		this.hearingStatusName = hearingStatusName;
		this.appearanceCategoryName = appearanceCategoryName;
		this.hearingAnalystLastName = hearingAnalystLastName;
		this.hearingAnalystMiddleName = hearingAnalystMiddleName;
		this.hearingAnalystFirstName = hearingAnalystFirstName;
		this.decisionName = decisionName;
	}

	/**
	 * Returns the boardHearingId.
	 * @return boardHearingId - Long
	 */
	public Long getBoardHearingId() {
		return this.boardHearingId;
	}

	/**
	 * Returns the hearingDate.
	 * @return hearingDate - Date
	 */
	public Date getHearingDate() {
		return this.hearingDate;
	}

	/**
	 * Returns the hearingStatusName.
	 * @return hearingStatusName - String
	 */
	public String getHearingStatusName() {
		return this.hearingStatusName;
	}

	/**
	 * Returns the appearanceCategoryName.
	 * @return appearanceCategoryName - String
	 */
	public String getAppearanceCategoryName() {
		return this.appearanceCategoryName;
	}

	/**
	 * Returns the hearingAnalystLastName.
	 * @return hearingAnalystLastName - String
	 */
	public String getHearingAnalystLastName() {
		return this.hearingAnalystLastName;
	}

	/**
	 * Returns the hearingAnalystMiddleName.
	 * @return hearingAnalystMiddleName - String
	 */
	public String getHearingAnalystMiddleName() {
		return this.hearingAnalystMiddleName;
	}

	/**
	 * Returns the hearingAnalystFirstName.
	 * @return hearingAnalystFirstName - String
	 */
	public String getHearingAnalystFirstName() {
		return this.hearingAnalystFirstName;
	}

	/**
	 * Returns the decisionName.
	 * @return decisionName - String
	 */
	public String getDecisionName() {
		return this.decisionName;
	}
}
