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
package omis.medicalreview.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Medical Review Summary.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Long medicalReviewId;
	
	private final Date medicalReviewDate;
	
	private final String medicalHealthClassificationName;
	
	private final String updateUserLastName;
	
	private final String updateUserFirstName;
	
	private final String updateUserMiddleName;
	
	/**
	 * @param medicalReviewId - Long Medical Review ID
	 * @param medicalReviewDate - Date Medical Review Date
	 * @param medicalHealthClassificationName - String Medical Health
	 * Classification Name
	 * @param updateUserLastName - String Update User Last Name
	 * @param updateUserFirstName - String Update User First Name
	 * @param updateUserMiddleName - String Update User Middle Name
	 */
	public MedicalReviewSummary(final Long medicalReviewId,
			final Date medicalReviewDate,
			final String medicalHealthClassificationName,
			final String updateUserLastName, final String updateUserFirstName,
			final String updateUserMiddleName) {
		this.medicalReviewId = medicalReviewId;
		this.medicalReviewDate = medicalReviewDate;
		this.medicalHealthClassificationName = medicalHealthClassificationName;
		this.updateUserLastName = updateUserLastName;
		this.updateUserFirstName = updateUserFirstName;
		this.updateUserMiddleName = updateUserMiddleName;
	}

	/**
	 * Returns the medicalReviewId.
	 * @return medicalReviewId - Long
	 */
	public Long getMedicalReviewId() {
		return this.medicalReviewId;
	}

	/**
	 * Returns the medicalReviewDate.
	 * @return medicalReviewDate - Date
	 */
	public Date getMedicalReviewDate() {
		return this.medicalReviewDate;
	}

	/**
	 * Returns the medicalHealthClassificationName.
	 * @return medicalHealthClassificationName - String
	 */
	public String getMedicalHealthClassificationName() {
		return this.medicalHealthClassificationName;
	}

	/**
	 * Returns the updateUserLastName.
	 * @return updateUserLastName - String
	 */
	public String getUpdateUserLastName() {
		return this.updateUserLastName;
	}

	/**
	 * Returns the updateUserFirstName.
	 * @return updateUserFirstName - String
	 */
	public String getUpdateUserFirstName() {
		return this.updateUserFirstName;
	}

	/**
	 * Returns the updateUserMiddleName.
	 * @return updateUserMiddleName - String
	 */
	public String getUpdateUserMiddleName() {
		return this.updateUserMiddleName;
	}
}
