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
package omis.assessment.dao;

import java.util.Date;
import java.util.List;

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideNote;
import omis.dao.GenericDao;

/**
 * Data access object for assessment category override note.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentCategoryOverrideNoteDao 
		extends GenericDao<AssessmentCategoryOverrideNote> {

	/**
	 * Returns the assessment category override note for the specified 
	 * assessment category override, description, and date.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param description description
	 * @param date date
	 * @return assessment category override note
	 */
	AssessmentCategoryOverrideNote find(
			AssessmentCategoryOverride assessmentCategoryOverride, 
			String description, Date date);

	/**
	 * Returns the assessment category override note for the specified 
	 * assessment category override, description, and date excluding the 
	 * specified assessment category override note.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param description description
	 * @param date date
	 * @param excludedAssessmentCategoryOverrideNote assessment category 
	 * override note
	 * @return assessment category override note
	 */
	AssessmentCategoryOverrideNote findExcluding(
			AssessmentCategoryOverride assessmentCategoryOverride, 
			String description, Date date, 
			AssessmentCategoryOverrideNote excludedAssessmentCategoryOverrideNote);

	/**
	 * Returns a list of assessment category override notes for the specified 
	 * assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @return list of assessment category override notes
	 */
	List<AssessmentCategoryOverrideNote> findByAssessmentCategoryOverride(
			AssessmentCategoryOverride assessmentCategoryOverride);
}