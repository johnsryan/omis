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
package omis.presentenceinvestigation.domain;

import java.util.Date;


import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.docket.domain.Docket;
import omis.user.domain.UserAccount;

/** 
 * Presentence Investigation Request.
 * 
 * @author Ryan Johns
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.3 (May 9, 2018)
 * @since OMIS 3.0
 */
public interface PresentenceInvestigationRequest 
	extends Creatable, Updatable {
	/** Gets id.
	 * @return id. */
	public Long getId();
	
	/** Gets request date. 
 	 * @return request date. 
 	 */
	public Date getRequestDate();
	
	/** Gets expected completion date.
	 * @return expected completion date. */
	public Date getExpectedCompletionDate();
	
	/** Gets assigned user.
	 * @return assigned user. */
	public UserAccount getAssignedUser();
	
	/** Gets docket.
	 * @return docket. */
	public Docket getDocket();
	
	/** Gets Sentence Date
	 * @return sentenceDate - Date
	 */
	public Date getSentenceDate();
	
	/** Gets submission Date.
	 * @return submission date. */
	public Date getSubmissionDate();
	
	/** Sets submission date.
	 * @param submissionDate - submission date. */
	public void setSubmissionDate(Date submissionDate);
	
	/** Sets id.
	 * @param id - id. */
	public void setId(Long id);
	
	/** Sets request date.
	 * @param requestDate - request date. */
	public void setRequestDate(Date requestDate);
	
		/** Sets expected completion date.
	 * @param expectedCompletionDate - expected completion date. */
	public void setExpectedCompletionDate(Date expectedCompletionDate);
	
	/** Sets assigned user
	 * @param assignedUser- assigned user. */
	public void setAssignedUser(UserAccount assignedUser);
	
	/** Sets docket
	 * @param docket - docket. */
	public void setDocket(Docket docket);
	
	/**
	 * Returns the Category for the PresentenceInvestigationRequest
	 * @return category - PresentenceInvestigationCategory
	 */
	public PresentenceInvestigationCategory getCategory();
	
	/**
	 * Sets the Category for the PresentenceInvestigationRequest
	 * @param category - PresentenceInvestigationCategory
	 */
	public void setCategory(PresentenceInvestigationCategory category);
	
	/**
	 * Sets Sentence Date
	 * @param sentenceDate - Date
	 */
	public void setSentenceDate(Date sentenceDate);
}