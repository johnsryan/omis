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

import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.RatingCategory;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Implementation of assessment category score.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryScoreImpl implements AssessmentCategoryScore {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private AdministeredQuestionnaire administeredQuestionnaire;
	
	private RatingCategory ratingCategory;
	
	private BigDecimal score;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Instantiates an implementation of assessment category score.
	 */
	public AssessmentCategoryScoreImpl() {
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
	public void setAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		this.administeredQuestionnaire = administeredQuestionnaire;
	}

	/** {@inheritDoc} */
	@Override
	public AdministeredQuestionnaire getAdministeredQuestionnaire() {
		return administeredQuestionnaire;
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
	public void setScore(final BigDecimal score) {
		this.score = score;
	}

	/** {@inheritDoc} */
	@Override
	public BigDecimal getScore() {
		return score;
	}

	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AssessmentCategoryScore)) {
			return false;
		}
		AssessmentCategoryScore that = (AssessmentCategoryScore) obj;
		if (this.getAdministeredQuestionnaire() == null) {
			throw new IllegalStateException(
					"Administered questionnaire required.");
		}
		if (!this.getAdministeredQuestionnaire().equals(
				that.getAdministeredQuestionnaire())) {
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
		if (this.getAdministeredQuestionnaire() == null) {
			throw new IllegalStateException(
					"Administered questionnaire required.");
		}
		if (this.getRatingCategory() == null) {
			throw new IllegalStateException("Rating category required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAdministeredQuestionnaire().hashCode();
		hashCode = 29 * hashCode + this.getRatingCategory().hashCode();
		return hashCode;
	}
}