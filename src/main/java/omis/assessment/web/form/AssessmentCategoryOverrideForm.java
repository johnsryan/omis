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
package omis.assessment.web.form;

import java.io.Serializable;
import java.util.Date;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.CategoryOverrideReason;
import omis.staff.domain.StaffAssignment;

/**
 * Assessment Category Override Form.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Apr 25, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentCategoryOverrideForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date overrideDate;
	
	private AssessmentRating overrideRating;
	
	private CategoryOverrideReason overrideReason;
	
	private StaffAssignment authorizedBy;
	
	/**
	 * Default constructor for Assessment Category Override Form.
	 */
	public AssessmentCategoryOverrideForm() {
	}

	/**
	 * Returns the overrideDate.
	 * @return overrideDate - Date
	 */
	public Date getOverrideDate() {
		return this.overrideDate;
	}

	/**
	 * Sets the overrideDate.
	 * @param overrideDate - Date
	 */
	public void setOverrideDate(final Date overrideDate) {
		this.overrideDate = overrideDate;
	}

	/**
	 * Returns the overrideRating.
	 * @return overrideRating - AssessmentRating
	 */
	public AssessmentRating getOverrideRating() {
		return this.overrideRating;
	}

	/**
	 * Sets the overrideRating.
	 * @param overrideRating - AssessmentRating
	 */
	public void setOverrideRating(final AssessmentRating overrideRating) {
		this.overrideRating = overrideRating;
	}

	/**
	 * Returns the overrideReason.
	 * @return overrideReason - CategoryOverrideReason
	 */
	public CategoryOverrideReason getOverrideReason() {
		return this.overrideReason;
	}

	/**
	 * Sets the overrideReason.
	 * @param overrideReason - CategoryOverrideReason
	 */
	public void setOverrideReason(final CategoryOverrideReason overrideReason) {
		this.overrideReason = overrideReason;
	}

	/**
	 * Returns the authorizedBy.
	 * @return authorizedBy - StaffAssignment
	 */
	public StaffAssignment getAuthorizedBy() {
		return this.authorizedBy;
	}

	/**
	 * Sets the authorizedBy.
	 * @param authorizedBy - StaffAssignment
	 */
	public void setAuthorizedBy(final StaffAssignment authorizedBy) {
		this.authorizedBy = authorizedBy;
	}
}
