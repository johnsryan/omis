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

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.CategoryOverrideReason;

/**
 * Implementation of assessment category override reason.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideReasonImpl 
		implements AssessmentCategoryOverrideReason {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private AssessmentCategoryOverride assessmentCategoryOverride;
	
	private CategoryOverrideReason categoryOverrideReason;

	/**
	 * Instantiates an implementation of assessment category override reason.
	 */
	public AssessmentCategoryOverrideReasonImpl() {
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
	public void setAssessmentCategoryOverride(
			final AssessmentCategoryOverride assessmentCategoryOverride) {
		this.assessmentCategoryOverride = assessmentCategoryOverride;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryOverride getAssessmentCategoryOverride() {
		return assessmentCategoryOverride;
	}

	/** {@inheritDoc} */
	@Override
	public void setCategoryOverrideReason(
			final CategoryOverrideReason categoryOverrideReason) {
		this.categoryOverrideReason = categoryOverrideReason;
	}

	/** {@inheritDoc} */
	@Override
	public CategoryOverrideReason getCategoryOverrideReason() {
		return categoryOverrideReason;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AssessmentCategoryOverrideReason)) {
			return false;
		}
		AssessmentCategoryOverrideReason that = 
				(AssessmentCategoryOverrideReason) obj;
		if (this.getAssessmentCategoryOverride() == null) {
			throw new IllegalStateException(
					"Assessment category override required.");
		}
		if (!this.getAssessmentCategoryOverride().equals(
				that.getAssessmentCategoryOverride())) {
			return false;
		}
		if (this.getCategoryOverrideReason() == null) {
			throw new IllegalStateException(
					"Category override reason required.");
		}
		if (!this.getCategoryOverrideReason().equals(
				that.getCategoryOverrideReason())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAssessmentCategoryOverride() == null) {
			throw new IllegalStateException(
					"Assessment category override required.");
		}
		if (this.getCategoryOverrideReason() == null) {
			throw new IllegalStateException(
					"Category override reason required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAssessmentCategoryOverride()
						.hashCode();
		hashCode = 29 * hashCode + this.getCategoryOverrideReason().hashCode();
		return hashCode;
	}
}