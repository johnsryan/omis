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

import java.util.List;

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.CategoryOverrideReason;
import omis.dao.GenericDao;

/**
 * Data access object for assessment category override reason.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentCategoryOverrideReasonDao 
		extends GenericDao<AssessmentCategoryOverrideReason>{

	/**
	 * Returns the assessment category override reason for the specified 
	 * parameters.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param categoryOverrideReason category override reason
	 * @return assessment category override reason
	 */
	AssessmentCategoryOverrideReason find(
			AssessmentCategoryOverride assessmentCategoryOverride, 
			CategoryOverrideReason categoryOverrideReason);
	
	/**
	 * Returns the assessment category override reason for the specified 
	 * parameters excluding the specified assessment category override reason.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param categoryOverrideReason category override reason
	 * @param excludedAssessmentCategoryOverrideReason excluded assessment 
	 * category override reason
	 * @return assessment category override reason
	 */
	AssessmentCategoryOverrideReason findExcluding(
			AssessmentCategoryOverride assessmentCategoryOverride, 
			CategoryOverrideReason categoryOverrideReason,
			AssessmentCategoryOverrideReason 
					excludedAssessmentCategoryOverrideReason);

	/**
	 * Returns a list of assessment category override reasons for the specified 
	 * assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @return list of assessment category override reasons
	 */
	List<AssessmentCategoryOverrideReason> findByAssessmentCategoryOverride(
			AssessmentCategoryOverride assessmentCategoryOverride);
}