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
package omis.paroleplan.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.component.Evaluation;

/**
 * Implementation of parole plan.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanImpl implements ParolePlan {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String vocationalPlan;
	
	private String residencePlan;
	
	private String treatmentPlan;
	
	private ParoleEligibility paroleEligibility;
	
	private Evaluation evaluation;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Instantiates an implementation of parole plan.
	 */
	public ParolePlanImpl() {
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
	public void setVocationalPlan(final String vocationalPlan) {
		this.vocationalPlan = vocationalPlan;
	}

	/** {@inheritDoc} */
	@Override
	public String getVocationalPlan() {
		return vocationalPlan;
	}

	/** {@inheritDoc} */
	@Override
	public void setResidencePlan(final String residencePlan) {
		this.residencePlan = residencePlan;
	}

	/** {@inheritDoc} */
	@Override
	public String getResidencePlan() {
		return residencePlan;
	}

	/** {@inheritDoc} */
	@Override
	public void setTreatmentPlan(final String treatmentPlan) {
		this.treatmentPlan = treatmentPlan;
	}

	/** {@inheritDoc} */
	@Override
	public String getTreatmentPlan() {
		return treatmentPlan;
	}

	/** {@inheritDoc} */
	@Override
	public void setParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		this.paroleEligibility = paroleEligibility;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEligibility getParoleEligibility() {
		return paroleEligibility;
	}

	/** {@inheritDoc} */
	@Override
	public void setEvaluation(final Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	/** {@inheritDoc} */
	@Override
	public Evaluation getEvaluation() {
		return evaluation;
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
		if (!(obj instanceof ParolePlan)) {
			return false;
		}
		ParolePlan that = (ParolePlan) obj;
		if (this.getParoleEligibility() == null) {
			throw new IllegalStateException("Parole eligibility required");
		}
		if (!this.getParoleEligibility().equals(that.getParoleEligibility())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getParoleEligibility() == null) {
			throw new IllegalStateException("Parole eligibility required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getParoleEligibility().hashCode();
		
		return hashCode;
	}
}