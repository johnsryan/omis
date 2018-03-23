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

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.dao.GenericDao;

/**
 * Data access object for assessment category override.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentCategoryOverrideDao 
		extends GenericDao<AssessmentCategoryOverride> {

	/**
	 * Finds the specified assessment category override for the specified 
	 * parameters.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @param assessmentRating assessment rating
	 * @return assessment category override
	 */
	AssessmentCategoryOverride find(
			AssessmentCategoryScore assessmentCategoryScore, 
			AssessmentRating assessmentRating);
	
	/**
	 * Finds the specified assessment category override for the specified 
	 * parameters excluding the specified assessment category override.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @param assessmentRating assessment rating
	 * @param excludedAssessmentCategoryOverride excluded assessment category 
	 * override
	 * @return assessment category override
	 */
	AssessmentCategoryOverride findExcluding(
			AssessmentCategoryScore assessmentCategoryScore, 
			AssessmentRating assessmentRating,
			AssessmentCategoryOverride excludedAssessmentCategoryOverride);

	/**
	 * Returns the assessment category override for the specified assessment 
	 * category score.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @return assessment category override
	 */
	AssessmentCategoryOverride findByAssessmentCategoryScore(
			AssessmentCategoryScore assessmentCategoryScore);
}