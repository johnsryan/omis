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

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * Assessment category override note.
 *
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentCategoryOverrideNote extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the assessment category override note.
	 * 
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the ID of the assessment category override note.
	 * 
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns the description of the assessment category override note.
	 * 
	 * @return description
	 */
	String getDescription();
	
	/**
	 * Sets the description of the assessment category override note.
	 * 
	 * @param description description
	 */
	void setDescription(String description);
	
	/**
	 * Returns the date of the assessment category override note.
	 * 
	 * @return date
	 */
	Date getDate();
	
	/**
	 * Sets the date of the assessment category override note.
	 * 
	 * @param date date
	 */
	void setDate(Date date);
	
	/**
	 * Returns the assessment category override of the assessment category 
	 * override note.
	 * 
	 * @return assessment category override
	 */
	AssessmentCategoryOverride getAssessmentCategoryOverride();
	
	/**
	 * Sets the assessment category override of the assessment category override 
	 * note.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 */
	void setAssessmentCategoryOverride(
			AssessmentCategoryOverride assessmentCategoryOverride);
}