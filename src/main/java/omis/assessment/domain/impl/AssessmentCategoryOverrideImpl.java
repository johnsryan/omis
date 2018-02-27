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
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.staff.domain.StaffAssignment;

/**
 * Implementation of assessment category override.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideImpl 
		implements AssessmentCategoryOverride {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private AssessmentCategoryScore assessmentCategoryScore;
	
	private AssessmentRating assessmentRating;
	
	private String notes;
	
	private StaffAssignment approvedStaffAssignment;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Initiates an implementation of assessment category override. 
	 */
	public AssessmentCategoryOverrideImpl() {
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
	public void setAssessmentCategoryScore(
			final AssessmentCategoryScore assessmentCategoryScore) {
		this.assessmentCategoryScore = assessmentCategoryScore;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentCategoryScore getAssessmentCategoryScore() {
		return assessmentCategoryScore;
	}

	/** {@inheritDoc} */
	@Override
	public void setAssessmentRating(final AssessmentRating assessmentRating) {
		this.assessmentRating = assessmentRating;
	}

	/** {@inheritDoc} */
	@Override
	public AssessmentRating getAssessmentRating() {
		return assessmentRating;
	}

	/** {@inheritDoc} */
	@Override
	public void setNotes(final String notes) {
		this.notes = notes;
	}

	/** {@inheritDoc} */
	@Override
	public String getNotes() {
		return notes;
	}

	/** {@inheritDoc} */
	@Override
	public void setApprovedStaffAssignment(
			final StaffAssignment approvedStaffAssignment) {
		this.approvedStaffAssignment = approvedStaffAssignment;
	}

	/** {@inheritDoc} */
	@Override
	public StaffAssignment getApprovedStaffAssignment() {
		return approvedStaffAssignment;
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
		if (!(obj instanceof AssessmentCategoryOverride)) {
			return false;
		}
		AssessmentCategoryOverride that = (AssessmentCategoryOverride) obj;
		if (this.getAssessmentCategoryScore() == null) {
			throw new IllegalStateException(
					"Assessment category score required.");
		}
		if (!this.getAssessmentCategoryScore().equals(
				that.getAssessmentCategoryScore())) {
			return false;
		}
		if (this.getAssessmentRating() == null) {
			throw new IllegalStateException("Assessment rating required.");
		}
		if (!this.getAssessmentRating().equals(that.getAssessmentRating())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAssessmentCategoryScore() == null) {
			throw new IllegalStateException(
					"Assessment category score required.");
		}
		if (this.getAssessmentRating() == null) {
			throw new IllegalStateException("Assessment rating required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAssessmentCategoryScore().hashCode();
		hashCode = 29 * hashCode + this.getAssessmentRating().hashCode();
		return hashCode;
	}
}