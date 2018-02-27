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
package omis.paroleplan.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.domain.component.Evaluation;

/**
 * Parole plan.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public interface ParolePlan extends Creatable, Updatable {

	/**
	 * Sets the ID of the parole plan.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the parole plan.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the vocational plan.
	 * 
	 * @param vocationalPlan vocational plan
	 */
	void setVocationalPlan(String vocationalPlan);
	
	/**
	 * Returns the vocational plan.
	 * 
	 * @return vocational plan
	 */
	String getVocationalPlan();
	
	/**
	 * Sets the residence plan.
	 * 
	 * @param residencePlan residence plan
	 */
	void setResidencePlan(String residencePlan);
	
	/**
	 * Returns the residence plan.
	 * 
	 * @return residence plan
	 */
	String getResidencePlan();
	
	/**
	 * Sets the treatment plan.
	 * 
	 * @param treatmentPlan treatment plan
	 */
	void setTreatmentPlan(String treatmentPlan);
	
	/**
	 * Returns the treatment plan.
	 * 
	 * @return treatment plan
	 */
	String getTreatmentPlan();
	
	/**
	 * Sets the parole eligibility.
	 * 
	 * @param paroleEligibility parole eligibility
	 */
	void setParoleEligibility(ParoleEligibility paroleEligibility);
	
	/**
	 * Returns the parole eligibility.
	 * 
	 * @return parole eligibility
	 */
	ParoleEligibility getParoleEligibility();
	
	/**
	 * Sets the evaluation.
	 * 
	 * @param evaluation evaluation
	 */
	void setEvaluation(Evaluation evaluation);
	
	/**
	 * Returns the evaluation.
	 * 
	 * @return evaluation
	 */
	Evaluation getEvaluation();
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}