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
 * Assessment rating summary.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class AssessmentRatingSummary implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long assessmentRatingId;
	
	private final Long assessmentCategoryScoreId;
	
	private final BigDecimal assessmentCategoryScoreValue;
	
	private final Boolean assessmentCategoryScorePertinent;
	
	private final Boolean override;
	
	private final String assessmentRatingDescription;
	
	private final String rankName;
	
	private final BigDecimal assessmentRatingMin;
	
	private final BigDecimal assessmentRatingMax;
	
	/**
	 * Instantiates an assessment rating summary with the specified parameters.
	 * 
	 * @param assessmentRatingId assessment rating id
	 * @param assessmentCategoryScoreId assessment category score id
	 * @param assessmentCategoryScorePertinent assessment category score 
	 * pertinent
	 * @param override override
	 * @param assessmentRatingDescription assessment rating description
	 * @param rankName rank name
	 * @param assessmentRatingMin assessment rating minimum
	 * @param assessmentRatingMax assessment rating maximum
	 */
	public AssessmentRatingSummary(final Long assessmentRatingId,
			final Long assessmentCategoryScoreId,
			final BigDecimal assessmentCategoryScoreValue,
			final Boolean assessmentCategoryScorePertinent,
			final Boolean override,
			final String assessmentRatingDescription,
			final String rankName,
			final BigDecimal assessmentRatingMin,
			final BigDecimal assessmentRatingMax) {
		this.assessmentRatingId = assessmentRatingId;
		this.assessmentCategoryScoreId = assessmentCategoryScoreId;
		this.assessmentCategoryScoreValue = assessmentCategoryScoreValue;
		this.assessmentCategoryScorePertinent = 
				assessmentCategoryScorePertinent;
		this.override = override;
		this.assessmentRatingDescription = assessmentRatingDescription;
		this.rankName = rankName;
		this.assessmentRatingMin = assessmentRatingMin;
		this.assessmentRatingMax = assessmentRatingMax;
	}

	/**
	 * Returns the assessmentCategoryScoreValue.
	 * @return assessmentCategoryScoreValue - BigDecimal
	 */
	public BigDecimal getAssessmentCategoryScoreValue() {
		return this.assessmentCategoryScoreValue;
	}

	/**
	 * Returns the assessment rating id.
	 *
	 * @return assessment rating id
	 */
	public Long getAssessmentRatingId() {
		return assessmentRatingId;
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
	 * Returns the assessment category score pertinent.
	 *
	 * @return assessment category score pertinent
	 */
	public Boolean getAssessmentCategoryScorePertinent() {
		return assessmentCategoryScorePertinent;
	}

	/**
	 * Returns the override.
	 *
	 * @return override
	 */
	public Boolean getOverride() {
		return override;
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
	 * Returns the rank name.
	 *
	 * @return rank name
	 */
	public String getRankName() {
		return rankName;
	}

	/**
	 * Returns the assessment rating minimum.
	 *
	 * @return assessment rating minimum
	 */
	public BigDecimal getAssessmentRatingMin() {
		return assessmentRatingMin;
	}

	/**
	 * Returns the assessment rating maximum.
	 *
	 * @return assessment rating maximum
	 */
	public BigDecimal getAssessmentRatingMax() {
		return assessmentRatingMax;
	}
}