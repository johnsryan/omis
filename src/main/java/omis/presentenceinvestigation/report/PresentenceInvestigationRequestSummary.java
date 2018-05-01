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
package omis.presentenceinvestigation.report;

import java.util.Date;

/** 
 * Report object for presentence investigation summary.
 * 
 * @author Ryan Johns
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.3 (Apr 24, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationRequestSummary {
	
	private final Long presentenceInvestigationRequestId;
	private final Long offenderId;
	private final String docketValue;
	private final Date requestDate;
	private final String assignedUserFirstName;
	private final String assignedUserLastName;
	private final String assignedUserUserName;
	private final Date expectedCompletionDate;
	private final Date completionDate;
	private final String offenderFirstName;
	private final String offenderLastName;
	private final String offenderMiddleName;
	private final Integer offenderNumber;
	private final Date sentenceDate;
	private final String category;
	private final Long completedTaskCount;
	private final Long totalTaskCount;
	private final Date submissionDate;
	private final PresentenceInvestigationRequestStatus status;
	
	/** 
	 * Constructor.
	 * 
	 * @param presentenceInvestigationRequstId - presentence investigation
	 * request id.
	 * @param offenderId - offender id
	 * @param docketValue - docketValue.
	 * @param requestDate - request date.
	 * @param assignedUserFirstName - assigned user first name.
	 * @param assignedUserLastName - assigned user last name.
	 * @param assignedUserUserName - assigned user user name
	 * @param expectedCompletionDate - expected completion date.
	 * @param completionDate - completion date.	 
	 * @param offenderFirstName - offender first name
	 * @param offenderLastName - offender last name
	 * @param offenderMiddleName - offender middle name
	 * @param offenderNumber - offender number
	 * @param sentenceDate - sentence Date
	 * @param category - Presentence Investigation Category
	 * @param completedTaskCount - Completed task count.
	 * @param totalTaskCount - Total task count.
	 * @param status presentence investigation request status
	 */
	public PresentenceInvestigationRequestSummary(
			final Long presentenceInvestigationRequestId, final Long offenderId,
			final String docketValue, final Date requestDate,
			final String assignedUserFirstName, final String assignedUserLastName,
			final String assignedUserUserName, final Date expectedCompletionDate,
			final Date completionDate, final String offenderFirstName,
			final String offenderLastName, final String offenderMiddleName,
			final Integer offenderNumber, final Date sentenceDate,
			final String category, final Long completedTaskCount, 
			final Long totalTaskCount, final Date submissionDate,
			final Long delayCount) {
		this.presentenceInvestigationRequestId = presentenceInvestigationRequestId;
		this.offenderId = offenderId;
		this.docketValue = docketValue;
		this.requestDate = requestDate;
		this.assignedUserFirstName = assignedUserFirstName;
		this.assignedUserLastName = assignedUserLastName;
		this.assignedUserUserName = assignedUserUserName;
		this.expectedCompletionDate = expectedCompletionDate;
		this.completionDate = completionDate;
		this.offenderFirstName = offenderFirstName;
		this.offenderLastName = offenderLastName;
		this.offenderMiddleName = offenderMiddleName;
		this.offenderNumber = offenderNumber;
		this.sentenceDate = sentenceDate;
		this.category = category;
		this.completedTaskCount = completedTaskCount;
		this.totalTaskCount = totalTaskCount;
		this.submissionDate = submissionDate;
		if (submissionDate != null) {
			this.status = PresentenceInvestigationRequestStatus.SUBMITTED;
		} else if (delayCount > 0) {
			this.status = PresentenceInvestigationRequestStatus.DELAYED;
		} else {
			this.status = PresentenceInvestigationRequestStatus.IN_PROGRESS;
		}
	}

	/**
	 * @return the presentenceInvestigationRequestId
	 */
	public Long getPresentenceInvestigationRequestId() {
		return this.presentenceInvestigationRequestId;
	}
	

	/**
	 * @return the offenderId
	 */
	public Long getOffenderId() {
		return this.offenderId;
	}

	/**
	 * @return the docketValue
	 */
	public String getDocketValue() {
		return this.docketValue;
	}

	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return this.requestDate;
	}

	/**
	 * @return the assignedUserFirstName
	 */
	public String getAssignedUserFirstName() {
		return this.assignedUserFirstName;
	}

	/**
	 * @return the assignedUserLastName
	 */
	public String getAssignedUserLastName() {
		return this.assignedUserLastName;
	}
	
	/**
	 * @return the assignedUserUserName
	 */
	public String getAssignedUserUserName() {
		return this.assignedUserUserName;
	}

	/**
	 * @return the expectedCompletionDate
	 */
	public Date getExpectedCompletionDate() {
		return this.expectedCompletionDate;
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return this.completionDate;
	}

	/**
	 * @return the offenderFirstName
	 */
	public String getOffenderFirstName() {
		return this.offenderFirstName;
	}

	/**
	 * @return the offenderLastName
	 */
	public String getOffenderLastName() {
		return this.offenderLastName;
	}

	/**
	 * @return the offenderNumber
	 */
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}

	/**
	 * Returns the offenderMiddleName
	 * @return offenderMiddleName - String
	 */
	public String getOffenderMiddleName() {
		return offenderMiddleName;
	}

	/**
	 * Returns the sentenceDate
	 * @return sentenceDate - Date
	 */
	public Date getSentenceDate() {
		return sentenceDate;
	}

	/**
	 * Returns the category
	 * @return category - String
	 */
	public String getCategory() {
		return category;
	}
	
	
	/** Returns completed tasks.
	 * @return completed tasks count. */
	public Long getCompletedTaskCount() {
		return this.completedTaskCount;
	}
	
	/** Returns total tasks.
	 * @return total task count. */
	public Long getTotalTaskCount() {
		return this.totalTaskCount;
	}
	
	/** Gets Submission date.
	 * @return submission date. */
	public Date getSubmissionDate() {
		return this.submissionDate;
	}

	/**
	 * Returns the status.
	 *
	 * @return status
	 */
	public PresentenceInvestigationRequestStatus getStatus() {
		return status;
	}
}