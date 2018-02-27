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
package omis.assessment.domain;

import java.io.Serializable;

/**
 * Assessment category override reason.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 23, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentCategoryOverrideReason extends Serializable {

	/**
	 * Sets the ID of the assessment category override reason.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the assessment category override reason.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 */
	void setAssessmentCategoryOverride(
			AssessmentCategoryOverride assessmentCategoryOverride);
	
	/**
	 * Returns the assessment category override.
	 * 
	 * @return assessment category override
	 */
	AssessmentCategoryOverride getAssessmentCategoryOverride();
	
	/**
	 * Sets the category override reason.
	 * 
	 * @param categoryOverrideReason category override reason
	 */
	void setCategoryOverrideReason(
			CategoryOverrideReason categoryOverrideReason);
	
	/**
	 * Returns the category override reason.
	 * 
	 * @return category override reason
	 */
	CategoryOverrideReason getCategoryOverrideReason();
}