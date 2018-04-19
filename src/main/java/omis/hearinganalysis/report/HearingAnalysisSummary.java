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
package omis.hearinganalysis.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Hearing analysis summary.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Date hearingEligibleDate;
	
	private final Date reviewDate;
	
	private final String analystLastName;
	
	private final String analystMiddleName;
	
	private final String analystFirstName;
	
	private final String analystTitleName;
	
	public HearingAnalysisSummary(final Date hearingEligibleDate,
			final Date reviewDate, final String analystLastName,
			final String analystMiddleName, final String analystFirstName,
			final String analystTitleName) {
		this.hearingEligibleDate = hearingEligibleDate;
		this.reviewDate = reviewDate;
		this.analystLastName = analystLastName;
		this.analystMiddleName = analystMiddleName;
		this.analystFirstName = analystFirstName;
		this.analystTitleName = analystTitleName;
	}

	/**
	 * Returns the hearing eligible date.
	 * 
	 * @return hearing eligible date
	 */
	public Date getHearingEligibleDate() {
		return hearingEligibleDate;
	}

	/**
	 * Returns the review date.
	 * 
	 * @return review date
	 */
	public Date getReviewDate() {
		return reviewDate;
	}

	/**
	 * Returns the analyst last name.
	 * 
	 * @return analyst last name
	 */
	public String getAnalystLastName() {
		return analystLastName;
	}

	/**
	 * Returns the analyst middle name.
	 * 
	 * @return analyst middle name
	 */
	public String getAnalystMiddleName() {
		return analystMiddleName;
	}

	/**
	 * Returns the analyst first name.
	 * 
	 * @return analyst first name
	 */
	public String getAnalystFirstName() {
		return analystFirstName;
	}

	/**
	 * Returns the analyst title name.
	 * 
	 * @return analyst title name
	 */
	public String getAnalystTitleName() {
		return analystTitleName;
	}
	
}
