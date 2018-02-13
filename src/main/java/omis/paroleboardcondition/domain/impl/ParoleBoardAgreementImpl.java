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
import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;
import omis.util.EqualityChecker;

/**
 * Parole Board Agreement Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine 
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public class ParoleBoardAgreementImpl implements ParoleBoardAgreement {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Agreement agreement;
	
	private ParoleBoardAgreementCategory category;
	
	private HearingAnalysis hearingAnalysis;
	
	private BoardHearing boardHearing;
	
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
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}
	
	/**{@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
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
	public Agreement getAgreement() {
		return this.agreement;
	}

	/**{@inheritDoc} */
	@Override
	public void setAgreement(final Agreement agreement) {
		this.agreement = agreement;
	}

	/**{@inheritDoc} */
	@Override
	public ParoleBoardAgreementCategory getCategory() {
		return this.category;
	}

	/**{@inheritDoc} */
	@Override
	public void setCategory(final ParoleBoardAgreementCategory category) {
		this.category = category;
	}
	
	/** {@inheritDoc} */
	@Override
	public HearingAnalysis getHearingAnalysis() {
		return hearingAnalysis;
	}

	/** {@inheritDoc} */
	@Override
	public void setHearingAnalysis(final HearingAnalysis hearingAnalysis) {
		this.hearingAnalysis = hearingAnalysis;
	}

	/** {@inheritDoc} */
	@Override
	public BoardHearing getBoardHearing() {
		return boardHearing;
	}

	/** {@inheritDoc} */
	@Override
	public void setBoardHearing(final BoardHearing boardHearing) {
		this.boardHearing = boardHearing;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParoleBoardAgreement)) {
			return false;
		}
		
		ParoleBoardAgreement that = (ParoleBoardAgreement) obj;
		
		if (this.getAgreement() == null) {
			throw new IllegalStateException("Agreement required.");
		}
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required.");
		}
		
		return EqualityChecker.create(Serializable.class)
				.add(this.getAgreement(), that.getAgreement())
				.add(this.getCategory(), that.getCategory())
				.add(this.getBoardHearing(), that.getBoardHearing())
				.add(this.getHearingAnalysis(), that.getHearingAnalysis())
				.check();
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAgreement() == null) {
			throw new IllegalStateException("Agreement required.");
		}
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAgreement().hashCode();
		hashCode = 29 * hashCode + this.getCategory().hashCode();
		if (this.getBoardHearing() != null) {
			hashCode = 29 * hashCode + this.getBoardHearing().hashCode();
		}
		if (this.getHearingAnalysis() != null) {
			hashCode = 29 * hashCode + this.getHearingAnalysis().hashCode();
		}
		
		return hashCode;
	}
}