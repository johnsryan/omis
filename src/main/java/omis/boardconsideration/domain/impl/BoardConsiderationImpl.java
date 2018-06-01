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
package omis.boardconsideration.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Implementation of board consideration.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationImpl implements BoardConsideration {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private HearingAnalysis hearingAnalysis;
	
	private String title;
	
	private String description;
	
	private Boolean accepted;
	
	private BoardConsiderationCategory category;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/** 
	 * Instantiates an implementation of board consideration. 
	 */
	public BoardConsiderationImpl() {
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
	public void setHearingAnalysis(final HearingAnalysis hearingAnalysis) {
		this.hearingAnalysis = hearingAnalysis;
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysis getHearingAnalysis() {
		return hearingAnalysis;
	}

	/** {@inheritDoc} */
	@Override
	public void setTitle(final String title) {
		this.title = title;
	}

	/** {@inheritDoc} */
	@Override
	public String getTitle() {
		return title;
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
	public void setAccepted(final Boolean accepted) {
		this.accepted = accepted;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getAccepted() {
		return accepted;
	}

	/** {@inheritDoc} */
	@Override
	public void setCategory(final BoardConsiderationCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public BoardConsiderationCategory getCategory() {
		return category;
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
		if (!(obj instanceof BoardConsideration)) {
			return false;
		}
		BoardConsideration that = (BoardConsideration) obj;
		if (this.getHearingAnalysis() == null) {
			throw new IllegalStateException("Hearing analysis required");
		}
		if (!this.getHearingAnalysis().equals(that.getHearingAnalysis())) {
			return false;
		}
		if (this.getTitle() == null) {
			throw new IllegalStateException("Title required");
		}
		if (!this.getTitle().equals(that.getTitle())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getHearingAnalysis() == null) {
			throw new IllegalStateException("Hearing analysis required");
		}
		if (this.getTitle() == null) {
			throw new IllegalStateException("Title required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getHearingAnalysis().hashCode();
		hashCode = 29 * hashCode + this.getTitle().hashCode();
		return hashCode;
	}
}