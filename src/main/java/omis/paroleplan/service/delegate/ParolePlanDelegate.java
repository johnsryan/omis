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
package omis.paroleplan.service.delegate;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.dao.ParolePlanDao;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.component.Evaluation;
import omis.staff.domain.StaffAssignment;

/**
 * Parole plan delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanDelegate {

	/* Data access objects. */
	
	private final ParolePlanDao parolePlanDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParolePlan> parolePlanInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of parole plan delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param parolePlanDao parole plan data access object
	 * @param parolePlanInstanceFactory parole plan instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParolePlanDelegate(final ParolePlanDao parolePlanDao,
			final InstanceFactory<ParolePlan> parolePlanInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.parolePlanDao = parolePlanDao;
		this.parolePlanInstanceFactory = parolePlanInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new parole plan.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @param evaluator evaluator
	 * @param evaluationDescription evaluation description
	 * @param vocationalPlan vocational plan
	 * @param residencePlan residence plan
	 * @param treatmentPlan treatment plan
	 * @return parole plan
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParolePlan create(final ParoleEligibility paroleEligibility, 
			final StaffAssignment evaluator,
			final String evaluationDescription, final String vocationalPlan, 
			final String residencePlan, final String treatmentPlan) 
					throws DuplicateEntityFoundException{
		if (this.parolePlanDao.find(paroleEligibility) != null) {
			throw new DuplicateEntityFoundException(
					"Parole plan already exists.");
		}
		ParolePlan parolePlan = this.parolePlanInstanceFactory.createInstance();
		populateParolePlan(parolePlan, paroleEligibility, evaluator, 
				evaluationDescription, vocationalPlan, residencePlan, 
				treatmentPlan);
		parolePlan.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.parolePlanDao.makePersistent(parolePlan);
	}

	/**
	 * Updates the specified parole plan.
	 * 
	 * @param parolePlan parole plan
	 * @param paroleEligibility parole eligibility
	 * @param evaluator evaluator
	 * @param evaluationDescription evaluation description
	 * @param vocationalPlan vocational plan
	 * @param residencePlan residence plan
	 * @param treatmentPlan treatment plan
	 * @return parole plan
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParolePlan update(final ParolePlan parolePlan, 
			final ParoleEligibility paroleEligibility,
			final StaffAssignment evaluator, final String evaluationDescription,
			final String vocationalPlan, final String residencePlan, 
			final String treatmentPlan) throws DuplicateEntityFoundException {
		if (this.parolePlanDao.findExcluding(paroleEligibility, parolePlan) != 
				null) {
			throw new DuplicateEntityFoundException(
					"Parole plan already exists.");
		}
		populateParolePlan(parolePlan, paroleEligibility, evaluator, 
				evaluationDescription, vocationalPlan, residencePlan, 
				treatmentPlan);
		return this.parolePlanDao.makePersistent(parolePlan);
	}

	/**
	 * Removes the specified parole plan.
	 * 
	 * @param parolePlan parole plan
	 */
	public void remove(final ParolePlan parolePlan) {
		this.parolePlanDao.makeTransient(parolePlan);
	}

	/**
	 * Returns the parole plan for the specified parole eligibility.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @return parole plan
	 */
	public ParolePlan findByParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		return this.parolePlanDao.findByParoleEligibility(paroleEligibility);
	}
	
	// Populates a parole plan
	private void populateParolePlan(final ParolePlan parolePlan, 
			final ParoleEligibility paroleEligibility,
			final StaffAssignment evaluator, final String evaluationDescription, 
			final String vocationalPlan, final String residencePlan, 
			final String treatmentPlan) {
		parolePlan.setParoleEligibility(paroleEligibility);
		parolePlan.setVocationalPlan(vocationalPlan);
		parolePlan.setResidencePlan(residencePlan);
		parolePlan.setTreatmentPlan(treatmentPlan);
		parolePlan.setEvaluation(new Evaluation(evaluator, 
				evaluationDescription));
		parolePlan.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}