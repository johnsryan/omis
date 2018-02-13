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
package omis.paroleboardcondition.domain.impl;

import java.io.Serializable;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;
import omis.util.EqualityChecker;

/**
 * Parole Board Agreement Category Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public class ParoleBoardAgreementCategoryImpl
	implements ParoleBoardAgreementCategory {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private Boolean boardHearingAgreement;
	
	private Boolean hearingAnalysisAgreement;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**{@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/**{@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/**{@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}

	/**{@inheritDoc} */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getBoardHearingAgreement() {
		return boardHearingAgreement;
	}

	/** {@inheritDoc} */
	@Override
	public void setBoardHearingAgreement(final Boolean boardHearingAgreement) {
		this.boardHearingAgreement = boardHearingAgreement;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getHearingAnalysisAgreement() {
		return hearingAnalysisAgreement;
	}

	/** {@inheritDoc} */
	@Override
	public void setHearingAnalysisAgreement(
			final Boolean hearingAnalysisAgreement) {
		this.hearingAnalysisAgreement = hearingAnalysisAgreement;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParoleBoardAgreementCategory)) {
			return false;
		}
		
		ParoleBoardAgreementCategory that = (ParoleBoardAgreementCategory) obj;
		
		if (this.getName() == null) {
			throw new IllegalStateException("Name required.");
		}
		if (this.getBoardHearingAgreement() == null) {
			throw new IllegalStateException(
					"Board hearing agreement required.");
		}
		if (this.getHearingAnalysisAgreement() == null) {
			throw new IllegalStateException(
					"Hearing analysis agreement required.");
		}
		return EqualityChecker.create(Serializable.class)
				.add(this.getName(), that.getName())
				.add(this.getBoardHearingAgreement(), 
						that.getBoardHearingAgreement())
				.add(this.getHearingAnalysisAgreement(), 
						that.getHearingAnalysisAgreement())
				.check();
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Name required.");
		}
		if (this.getBoardHearingAgreement() == null) {
			throw new IllegalStateException(
					"Board hearing agreement required.");
		}
		if (this.getHearingAnalysisAgreement() == null) {
			throw new IllegalStateException(
					"Hearing analysis agreement required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getName().hashCode();
		hashCode = 29 * hashCode + this.getBoardHearingAgreement().hashCode();
		hashCode = 29 * hashCode + this.getHearingAnalysisAgreement().hashCode();
		
		return hashCode;
	}
}