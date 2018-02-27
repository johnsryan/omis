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

import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingCategory;
import omis.questionnaire.domain.AnswerValue;

/**
 * Implementation of answer rating.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AnswerRatingImpl implements AnswerRating {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private AnswerValue answerValue;
	
	private BigDecimal value;
	
	private RatingCategory ratingCategory;
	
	/**
	 * Instantiates an implementation of answer rating. 
	 */
	public AnswerRatingImpl() {
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
	public void setAnswerValue(final AnswerValue answerValue) {
		this.answerValue = answerValue;
	}

	/** {@inheritDoc} */
	@Override
	public AnswerValue getAnswerValue() {
		return answerValue;
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final BigDecimal value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getValue() {
		return value;
	}

	/** {@inheritDoc} */
	@Override
	public void setRatingCategory(final RatingCategory ratingCategory) {
		this.ratingCategory = ratingCategory;
	}

	/** {@inheritDoc} */
	@Override
	public RatingCategory getRatingCategory() {
		return ratingCategory;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}	
		if (!(o instanceof AnswerRating)) {
			return false;
		}
		AnswerRating that = (AnswerRating) o;
		if (this.getAnswerValue() == null) {
			throw new IllegalStateException("Answer value required.");
		}
		if (!this.getAnswerValue().equals(that.getAnswerValue())) {
			return false;
		}
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		if (!this.getRatingCategory().equals(that.getRatingCategory())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAnswerValue() == null) {
			throw new IllegalStateException("Answer value required.");
		}
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAnswerValue().hashCode();
		hashCode = 29 * hashCode + this.getRatingCategory().hashCode();
		return hashCode;
	}
}