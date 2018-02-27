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
package omis.paroleplan.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import omis.staff.domain.StaffAssignment;

/**
 * Parole plan form.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private StaffAssignment evaluator;
	
	private String evaluationDescription;
	
	private String vocationalPlan;
	
	private String residencePlan;
	
	private String treatmentPlan;
	
	private List<ParolePlanDocumentAssociationItem> 
			parolePlanDocumentAssociationItems = new ArrayList<>();
	
	private List<ParolePlanNoteItem> parolePlanNoteItems = new ArrayList<>();
	
	/**
	 * Instantiates a default parole plan form. 
	 */
	public ParolePlanForm() {
	}

	/**
	 * Returns the evaluator.
	 *
	 * @return evaluator
	 */
	public StaffAssignment getEvaluator() {
		return evaluator;
	}

	/**
	 * Sets the evaluator.
	 *
	 * @param evaluator staff assignment
	 */
	public void setEvaluator(final StaffAssignment evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * Returns the evaluation description.
	 *
	 * @return evaluation description
	 */
	public String getEvaluationDescription() {
		return evaluationDescription;
	}

	/**
	 * Sets the evaluation description.
	 *
	 * @param evaluationDescription evaluation description
	 */
	public void setEvaluationDescription(final String evaluationDescription) {
		this.evaluationDescription = evaluationDescription;
	}

	/**
	 * Returns the vocational plan.
	 *
	 * @return vocational plan
	 */
	public String getVocationalPlan() {
		return vocationalPlan;
	}

	/**
	 * Sets the vocational plan.
	 *
	 * @param vocationalPlan vocational plan
	 */
	public void setVocationalPlan(final String vocationalPlan) {
		this.vocationalPlan = vocationalPlan;
	}

	/**
	 * Returns the residence plan.
	 *
	 * @return residence plan
	 */
	public String getResidencePlan() {
		return residencePlan;
	}

	/**
	 * Sets the residence plan.
	 *
	 * @param residencePlan residence plan
	 */
	public void setResidencePlan(final String residencePlan) {
		this.residencePlan = residencePlan;
	}

	/**
	 * Returns the treatment plan.
	 *
	 * @return treatment plan
	 */
	public String getTreatmentPlan() {
		return treatmentPlan;
	}

	/**
	 * Sets the treatment plan.
	 *
	 * @param treatmentPlan treatment plan
	 */
	public void setTreatmentPlan(final String treatmentPlan) {
		this.treatmentPlan = treatmentPlan;
	}

	/**
	 * Returns the list of parole plan document association items.
	 *
	 * @return list of parole plan document association items
	 */
	public List<ParolePlanDocumentAssociationItem> 
			getParolePlanDocumentAssociationItems() {
		return parolePlanDocumentAssociationItems;
	}

	/**
	 * Sets the list of parole plan document association items.
	 *
	 * @param parolePlanDocumentAssociationItems list of parole plan 
	 * document association items
	 */
	public void setParolePlanDocumentAssociationItems(
			final List<ParolePlanDocumentAssociationItem>
					parolePlanDocumentAssociationItems) {
		this.parolePlanDocumentAssociationItems = 
				parolePlanDocumentAssociationItems;
	}

	/**
	 * Returns the list of parole plan note items.
	 *
	 * @return list of parole plan note items
	 */
	public List<ParolePlanNoteItem> getParolePlanNoteItems() {
		return parolePlanNoteItems;
	}

	/**
	 * Sets the list of parole plan note items.
	 *
	 * @param parolePlanNoteItems parole plan note items
	 */
	public void setParolePlanNoteItems(
			final List<ParolePlanNoteItem> parolePlanNoteItems) {
		this.parolePlanNoteItems = parolePlanNoteItems;
	}
}