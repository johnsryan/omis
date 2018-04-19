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

import omis.assessment.domain.AnswerRating;
import omis.assessment.domain.RatingScaleGroup;
import omis.assessment.domain.RatingScaleGroupAnswer;

/**
 * Implementation of rating scale group answer.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 3, 2018)
 * @since OMIS 3.0
 */
public class RatingScaleGroupAnswerImpl implements RatingScaleGroupAnswer {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private AnswerRating answerRating;
	
	private RatingScaleGroup scaleGroup;
	
	/**
	 * Instantiates an implementation of rating scale group answer.
	 */
	public RatingScaleGroupAnswerImpl() {
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
	public void setAnswerRating(final AnswerRating answerRating) {
		this.answerRating = answerRating;
	}

	/** {@inheritDoc} */
	@Override
	public AnswerRating getAnswerRating() {
		return answerRating;
	}

	/** {@inheritDoc} */
	@Override
	public void setScaleGroup(final RatingScaleGroup scaleGroup) {
		this.scaleGroup = scaleGroup;
	}

	/** {@inheritDoc} */
	@Override
	public RatingScaleGroup getScaleGroup() {
		return scaleGroup;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RatingScaleGroupAnswer)) {
			return false;
		}
		RatingScaleGroupAnswer that = (RatingScaleGroupAnswer) obj;
		if (this.getAnswerRating() == null ) {
			throw new IllegalStateException("Answer rating required.");
		}
		if (!this.getAnswerRating().equals(that.getAnswerRating())) {
			return false;
		}
		if (this.getScaleGroup() == null ) {
			throw new IllegalStateException("Rating scale group required.");
		}
		if (!this.getScaleGroup().equals(that.getScaleGroup())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAnswerRating() == null ) {
			throw new IllegalStateException("Answer rating required.");
		}
		if (this.getScaleGroup() == null ) {
			throw new IllegalStateException("Rating scale group required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAnswerRating().hashCode();
		hashCode = 29 * hashCode + this.getScaleGroup().hashCode();
		return hashCode;
	}
}