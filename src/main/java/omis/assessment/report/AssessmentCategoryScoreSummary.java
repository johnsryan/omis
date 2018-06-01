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
package omis.assessment.report;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Assessment category score summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryScoreSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long assessmentCategoryScoreId;
	
	private final String ratingCategoryDescription;
	
	private final String ratingRankName;
	
	private final String assessmentRatingDescription;
	
	private final BigDecimal assessmentCategoryScoreValue;
	
	private final BigDecimal assessmentRatingMin;
	
	private final BigDecimal assessmentRatingMax;
	
	/**
	 * Instantiates an assessment category score summary with the specified 
	 * parameters.
	 * 
	 * @param assessmentCategoryScoreId assessment category score id
	 * @param ratingCategoryDescription rating category description
	 * @param ratingRankName rating rank name
	 * @param assessmentRatingDescription assessment rating description
	 * @param assessmentCategoryScoreValue assessment category score value
	 */
	public AssessmentCategoryScoreSummary(final Long assessmentCategoryScoreId,
			final String ratingCategoryDescription,
			final String ratingRankName,
			final String assessmentRatingDescription,
			final BigDecimal assessmentCategoryScoreValue,
			final BigDecimal assessmentRatingMin,
			final BigDecimal assessmentRatingMax) {
		this.assessmentCategoryScoreId = assessmentCategoryScoreId;
		this.ratingCategoryDescription = ratingCategoryDescription;
		this.ratingRankName = ratingRankName;
		this.assessmentRatingDescription = assessmentRatingDescription;
		this.assessmentCategoryScoreValue = assessmentCategoryScoreValue;
		this.assessmentRatingMin = assessmentRatingMin;
		this.assessmentRatingMax = assessmentRatingMax;
	}

	/**
	 * Returns the assessment category score id.
	 *
	 * @return assessment category score id
	 */
	public Long getAssessmentCategoryScoreId() {
		return assessmentCategoryScoreId;
	}

	/**
	 * Returns the rating category description.
	 *
	 * @return rating category description
	 */
	public String getRatingCategoryDescription() {
		return ratingCategoryDescription;
	}

	/**
	 * Returns the rating rank name.
	 *
	 * @return rating rank name
	 */
	public String getRatingRankName() {
		return ratingRankName;
	}

	/**
	 * Returns the assessment rating description.
	 *
	 * @return assessment rating description
	 */
	public String getAssessmentRatingDescription() {
		return assessmentRatingDescription;
	}

	/**
	 * Returns the assessment category score value.
	 *
	 * @return assessment category score value
	 */
	public BigDecimal getAssessmentCategoryScoreValue() {
		return assessmentCategoryScoreValue;
	}

	/**
	 * Returns the assessmentRatingMin.
	 * @return assessmentRatingMin - BigDecimal
	 */
	public BigDecimal getAssessmentRatingMin() {
		return this.assessmentRatingMin;
	}

	/**
	 * Returns the assessmentRatingMax.
	 * @return assessmentRatingMax - BigDecimal
	 */
	public BigDecimal getAssessmentRatingMax() {
		return this.assessmentRatingMax;
	}
	
	
}