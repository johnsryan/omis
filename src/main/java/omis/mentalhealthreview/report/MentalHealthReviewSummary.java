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
package omis.mentalhealthreview.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Mental health review summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long id;
	
	private final String updatedUserLastName;
	
	private final String updatedUserFirstName;
	
	private final String updatedUserMiddleName;
	
	private final Date date;
	
	private final Date updateDate;
	
	/**
	 * Instantiates an implementation of mental health review summary.
	 * 
	 * @param id mental health review id
	 * @param date date
	 * @param updatedUserLastName updated user last name
	 * @param updatedUserFirstName updated user first name
	 * @param updatedUserMiddleName updated user middle name
	 * @param updateDate update date
	 */
	public MentalHealthReviewSummary(final Long id, final Date date, 
			final String updatedUserLastName, final String updatedUserFirstName, 
			final String updatedUserMiddleName, final Date updateDate) {
		this.id = id;
		this.updatedUserFirstName = updatedUserFirstName;
		this.updatedUserLastName = updatedUserLastName;
		this.updatedUserMiddleName = updatedUserMiddleName;
		this.date = date;
		this.updateDate = updateDate;
	}

	/**
	 * Returns the id.
	 *
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the updated user last name.
	 *
	 * @return updated user last name
	 */
	public String getUpdatedUserLastName() {
		return updatedUserLastName;
	}

	/**
	 * Returns the updated user first name.
	 *
	 * @return updated user first name
	 */
	public String getUpdatedUserFirstName() {
		return updatedUserFirstName;
	}

	/**
	 * Returns the updated user middle name.
	 *
	 * @return updated user middle name
	 */
	public String getUpdatedUserMiddleName() {
		return updatedUserMiddleName;
	}

	/**
	 * Returns the date.
	 *
	 * @return date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Returns the update date.
	 *
	 * @return update date
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
}