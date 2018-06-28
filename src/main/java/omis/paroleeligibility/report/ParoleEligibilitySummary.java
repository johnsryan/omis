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

import java.io.Serializable;
import java.util.Date;

import omis.paroleeligibility.domain.EligibilityStatusCategory;

/**
 * Parole eligibility summary.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.2 (May 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleEligibilitySummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long paroleEligibilityId;
	
	private final Long boardHearingId;
	
	private final Date hearingEligibilityDate;
	
	private final String appearanceCategoryName;
	
	private final EligibilityStatusCategory statusCategory;
	
	private final Date statusDate;
	 
	private final Date reviewDate;
	
	private final String reasonName;
	
	private final Long hearingAnalysisId;
	
	private final String lastName;
	
	private final String firstName;
	
	private final String middleName;
	
	private final Integer offenderNumber;
	
	private final Date hearingAnalysisExpectedCompletionDate;
	
	private final String boardHearingItineraryLocationName;
	
	private final Date hearingDate;
	
	/**
	 * Summary of parole eligibility.
	 * 
	 * @param paroleEligibilityId parole eligibility id
	 * @param hearingEligibilityDate hearing eligibility date
	 * @param appearanceCategoryName appearance category name
	 * @param statusCategory eligibility status category
	 * @param statusDate status date
	 * @param reviewDate review date
	 * @param reasonName reason name
	 */
	public ParoleEligibilitySummary(
			final Long paroleEligibilityId,
			final Date hearingEligibilityDate,
			final String appearanceCategoryName,
			final EligibilityStatusCategory statusCategory,
			final Date statusDate,
			final Date reviewDate,
			final String reasonName) {
		this(paroleEligibilityId, null, null, hearingEligibilityDate, 
				appearanceCategoryName, statusCategory, statusDate, reviewDate, 
				reasonName, null, null, null, null, null, null, null);
	}
	
	/**
	 * Summary of parole eligibility.
	 * 
	 * @param paroleEligibilityId parole eligibility id
	 * @param hearingAnalysisId hearing analysis id
	 * @param hearingEligibilityDate hearing eligibility date
	 * @param appearanceCategoryName appearance category name
	 * @param statusCategory eligibility status category
	 * @param statusDate status date
	 * @param reviewDate review date
	 * @param reasonName reason name
	 * @param lastName last name of the offender
	 * @param firstName first name of the offender
	 * @param middleName middle name of the offender
	 * @param offenderNumber offender number
	 * @param hearingAnalysisExpectedCompletionDate hearing analysis expected
	 * completion date
	 * @param boardHearingItineraryLocationName board hearing itinerary
	 * location name
	 * @param hearingDate hearing date
	 */
	public ParoleEligibilitySummary(
			final Long paroleEligibilityId, final Long hearingAnalysisId,
			final Long boardHearingId,
			final Date hearingEligibilityDate, 
			final String appearanceCategoryName,
			final EligibilityStatusCategory statusCategory,
			final Date statusDate, final Date reviewDate, 
			final String reasonName, final String lastName, 
			final String firstName, final String middleName,
			final Integer offenderNumber,
			final Date hearingAnalysisExpectedCompletionDate,
			final String boardHearingItineraryLocationName,
			final Date hearingDate) {
		this.paroleEligibilityId = paroleEligibilityId;
		this.hearingEligibilityDate = hearingEligibilityDate;
		this.appearanceCategoryName = appearanceCategoryName;
		this.statusCategory = statusCategory;
		this.statusDate = statusDate;
		this.reviewDate = reviewDate;
		this.reasonName = reasonName;
		this.hearingAnalysisId = hearingAnalysisId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.offenderNumber = offenderNumber;
		this.hearingAnalysisExpectedCompletionDate =
				hearingAnalysisExpectedCompletionDate;
		this.boardHearingItineraryLocationName =
				boardHearingItineraryLocationName;
		this.hearingDate = hearingDate;
		this.boardHearingId = boardHearingId; 
	}
	
	/**
	 * Returns the parole eligibility id.
	 * 
	 * @return parole eligibility id
	 */
	public Long getParoleEligibilityId() {
		return this.paroleEligibilityId;
	}

	/**
	 * Returns the hearing analysis id.
	 *
	 * @return hearing analysis id
	 */
	public Long getHearingAnalysisId() {
		return hearingAnalysisId;
	}

	/**
	 * Returns the hearing eligibility date.
	 * 
	 * @return hearing eligibility date
	 */
	public Date getHearingEligibilityDate() {
		return this.hearingEligibilityDate;
	}

	/**
	 * Returns the appearance category name.
	 * 
	 * @return appearance category name
	 */
	public String getAppearanceCategoryName() {
		return this.appearanceCategoryName;
	}

	/**
	 * Returns the eligibility status category.
	 * 
	 * @return eligibility status category
	 */
	public EligibilityStatusCategory getStatusCategory() {
		return this.statusCategory;
	}
	
	/**
	 * Returns the hearing eligibility date of a parole eligibility.
	 * 
	 * @return hearing eligibility date
	 */
	public Date getStatusDate() {
		return this.statusDate;
	}
	
	/**
	 * Returns the review date of a parole eligibility.
	 * 
	 * @return review date
	 */
	public Date getReviewDate() {
		return this.reviewDate;
	}
	
	/**
	 * Returns the date of a parole eligibility status.
	 * 
	 * @return eligibility status date
	 */
	public String getReasonName() {
		return this.reasonName;
	}

	/**
	 * Returns the last name of the offender.
	 *
	 * @return last name of the offender
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the first name of the offender.
	 *
	 * @return first name of the offender
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the middle name of the offender.
	 *
	 * @return middle name of the offender
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Returns the offender number.
	 *
	 * @return offender number
	 */
	public Integer getOffenderNumber() {
		return offenderNumber;
	}
	
	/**
	 * Returns the hearingAnalysisExpectedCompletionDate.
	 * @return hearingAnalysisExpectedCompletionDate - Date
	 */
	public Date getHearingAnalysisExpectedCompletionDate() {
		return this.hearingAnalysisExpectedCompletionDate;
	}

	/**
	 * Returns the boardHearingItineraryLocationName.
	 * @return boardHearingItineraryLocationName - String
	 */
	public String getBoardHearingItineraryLocationName() {
		return this.boardHearingItineraryLocationName;
	}

	/**
	 * Returns the hearing date.
	 *
	 * @return hearing date
	 */
	public Date getHearingDate() {
		return hearingDate;
	}

	/**
	 * Returns the board Hearing ID.
	 * 
	 * @return boardHearingId - board hearing ID
	 */
	public Long getBoardHearingId() {
		return this.boardHearingId;
	}
	
	
}