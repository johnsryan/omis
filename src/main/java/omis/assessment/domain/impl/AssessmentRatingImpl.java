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
package omis.assessment.domain.impl;

import java.math.BigDecimal;

import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingRank;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Implementation of assessment rating.
 * 
 * @author Josh Divine
 * @version 0.1.3 (Mar 15, 2018)
 * @since OMIS 3.0
 */
public class AssessmentRatingImpl implements AssessmentRating {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private BigDecimal min;
	
	private BigDecimal max;
	
	private Sex sex;
	
	private QuestionnaireType questionnaireType;
	
	private Boolean valid;
	
	private DateRange dateRange;
	
	private RatingCategory category;
	
	private String description;
	
	private RatingRank rank;
	
	/**
	 * Instantiates an implementation of assessment rating.
	 */
	public AssessmentRatingImpl() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setMin(final BigDecimal min) {
		this.min = min;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getMin() {
		return min;
	}

	/** {@inheritDoc} */
	@Override
	public void setMax(final BigDecimal max) {
		this.max = max;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getMax() {
		return max;
	}

	/** {@inheritDoc} */
	@Override
	public void setSex(final Sex sex) {
		this.sex = sex;
	}

	/** {@inheritDoc} */
	@Override
	public Sex getSex() {
		return sex;
	}

	/** {@inheritDoc} */
	@Override
	public void setQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	/** {@inheritDoc} */
	@Override
	public QuestionnaireType getQuestionnaireType() {
		return questionnaireType;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return valid;
	}

	/** {@inheritDoc} */
	@Override
	public void setDateRange(final DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public DateRange getDateRange() {
		return dateRange;
	}


	/** {@inheritDoc} */
	@Override
	public void setCategory(final RatingCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public RatingCategory getCategory() {
		return category;
	}

	/** {@inheritDoc} */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public String getDescription() {
		return description;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setRank(final RatingRank rank) {
		this.rank = rank;
	}

	/** {@inheritDoc} */
	@Override
	public RatingRank getRank() {
		return rank;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AssessmentRating)) {
			return false;
		}
		AssessmentRating that = (AssessmentRating) obj;
		if (this.getDateRange() == null) {
			throw new IllegalStateException("Date range required.");
		}
		if (!this.getDateRange().equals(that.getDateRange())) {
			return false;
		}
		if (this.getMax() == null) {
			throw new IllegalStateException("Max required.");
		}
		if (!this.getMax().equals(that.getMax())) {
			return false;
		}
		if (this.getMin() == null) {
			throw new IllegalStateException("Min required.");
		}
		if (!this.getMin().equals(that.getMin())) {
			return false;
		}
		if (this.getQuestionnaireType() == null) {
			throw new IllegalStateException("Questionnaire type required.");
		}
		if (!this.getQuestionnaireType().equals(that.getQuestionnaireType())) {
			return false;
		}
		if (this.getSex() == null) {
			throw new IllegalStateException("Sex required.");
		}
		if (!this.getSex().equals(that.getSex())) {
			return false;
		}
		if (this.getRank() == null) {
			throw new IllegalStateException("Ratink rank required.");
		}
		if (!this.getRank().equals(that.getRank())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getDateRange() == null) {
			throw new IllegalStateException("Date range required.");
		}
		if (this.getMax() == null) {
			throw new IllegalStateException("Max required.");
		}
		if (this.getMin() == null) {
			throw new IllegalStateException("Min required.");
		}
		if (this.getQuestionnaireType() == null) {
			throw new IllegalStateException("Questionnaire type required.");
		}
		if (this.getSex() == null) {
			throw new IllegalStateException("Sex required.");
		}
		if (this.getRank() == null) {
			throw new IllegalStateException("Ratink rank required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDateRange().hashCode();
		hashCode = 29 * hashCode + this.getMax().hashCode();
		hashCode = 29 * hashCode + this.getMin().hashCode();
		hashCode = 29 * hashCode + this.getQuestionnaireType().hashCode();
		hashCode = 29 * hashCode + this.getSex().hashCode();
		hashCode = 29 * hashCode + this.getRank().hashCode();
		return hashCode;
	}
}