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

import java.util.Date;

import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideNote;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;

/**
 * Implementation of assessment category override note.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideNoteImpl 
		implements AssessmentCategoryOverrideNote {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Date date;
	
	private String description;
	
	private AssessmentCategoryOverride assessmentCategoryOverride;

	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Instantiates an implementation of assessment category override note.
	 */
	public AssessmentCategoryOverrideNoteImpl() {
		// Default constructor
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
	public void setDate(final Date date) {
		this.date = date;
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		return date;
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AssessmentCategoryOverrideNote)) {
			return false;
		}
		AssessmentCategoryOverrideNote that = 
				(AssessmentCategoryOverrideNote) obj;
		if (this.getAssessmentCategoryOverride() == null) {
			throw new IllegalStateException(
					"Assessment category override required");
		}
		if (!this.getAssessmentCategoryOverride().equals(
				that.getAssessmentCategoryOverride())) {
			return false;
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (!this.getDate().equals(that.getDate())) {
			return false;
		}
		if (this.getDescription() == null) {
			throw new IllegalStateException("Description required");
		}
		if (!this.getDescription().equals(that.getDescription())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAssessmentCategoryOverride() == null) {
			throw new IllegalStateException(
					"Assessment category override required");
		}
		if (this.getDate() == null) {
			throw new IllegalStateException("Date required");
		}
		if (this.getDescription() == null) {
			throw new IllegalStateException("Description required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + 
				this.getAssessmentCategoryOverride().hashCode();
		hashCode = 29 * hashCode + this.getDate().hashCode();
		hashCode = 29 * hashCode + this.getDescription().hashCode();
		
		return hashCode;
	}
}