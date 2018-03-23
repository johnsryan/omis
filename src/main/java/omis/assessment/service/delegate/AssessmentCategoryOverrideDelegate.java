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
package omis.assessment.service.delegate;

import omis.assessment.dao.AssessmentCategoryOverrideDao;
import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.AssessmentRating;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.staff.domain.StaffAssignment;

/**
 * Assessment category override delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideDelegate {

	/* Data access objects. */
	
	private final AssessmentCategoryOverrideDao assessmentCategoryOverrideDao;

	/* Instance factories. */
	
	private final InstanceFactory<AssessmentCategoryOverride> 
			assessmentCategoryOverrideInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** 
	 * Instantiates an implementation of assessment category override delegate 
	 * with the specified date access object and instance factory.
	 * 
	 * @param assessmentCategoryOverrideDao assessment category override data 
	 * access object
	 * @param assessmentCategoryOverrideInstanceFactory assessment category 
	 * override instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public AssessmentCategoryOverrideDelegate(
			final AssessmentCategoryOverrideDao assessmentCategoryOverrideDao,
			final InstanceFactory<AssessmentCategoryOverride> 
					assessmentCategoryOverrideInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.assessmentCategoryOverrideDao = assessmentCategoryOverrideDao;
		this.assessmentCategoryOverrideInstanceFactory = 
				assessmentCategoryOverrideInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new assessment category override.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @param assessmentRating assessment rating
	 * @param notes notes
	 * @param approvedStaffAssignment approved staff assignment
	 * @return assessment category override
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryOverride create(
			final AssessmentCategoryScore assessmentCategoryScore, 
			final AssessmentRating assessmentRating, final String notes, 
			final StaffAssignment approvedStaffAssignment)  
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryOverrideDao.find(assessmentCategoryScore, 
				assessmentRating) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category override already exists.");
		}
		AssessmentCategoryOverride assessmentCategoryOverride = this
				.assessmentCategoryOverrideInstanceFactory.createInstance();
		populateAssessmentCategoryOverride(assessmentCategoryOverride, 
				assessmentCategoryScore, assessmentRating, notes,
				approvedStaffAssignment);
		assessmentCategoryOverride.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.assessmentCategoryOverrideDao.makePersistent(
				assessmentCategoryOverride);
	}
	
	/**
	 * Updates an existing assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param assessmentCategoryScore assessment category score
	 * @param assessmentRating assessment rating
	 * @param notes notes
	 * @param approvedStaffAssignment approved staff assignment
	 * @return assessment category override
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryOverride update(
			final AssessmentCategoryOverride assessmentCategoryOverride,
			final AssessmentCategoryScore assessmentCategoryScore, 
			final AssessmentRating assessmentRating, final String notes, 
			final StaffAssignment approvedStaffAssignment)  
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryOverrideDao.findExcluding(
				assessmentCategoryScore, assessmentRating, 
				assessmentCategoryOverride) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category override already exists.");
		}
		populateAssessmentCategoryOverride(assessmentCategoryOverride, 
				assessmentCategoryScore, assessmentRating, notes,
				approvedStaffAssignment);
		return this.assessmentCategoryOverrideDao.makePersistent(
				assessmentCategoryOverride);
	}

	/**
	 * Removes the specified assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 */
	public void remove(
			final AssessmentCategoryOverride assessmentCategoryOverride) {
		this.assessmentCategoryOverrideDao.makeTransient(
				assessmentCategoryOverride);
	}

	/**
	 * Returns the assessment category override for the specified assessment 
	 * category score.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @return assessment category override
	 */
	public AssessmentCategoryOverride findByAssessmentCategoryScore(
			final AssessmentCategoryScore assessmentCategoryScore) {
		return this.assessmentCategoryOverrideDao.findByAssessmentCategoryScore(
				assessmentCategoryScore);
	}
	
	// Populates an assessment category override
	private void populateAssessmentCategoryOverride(
			final AssessmentCategoryOverride assessmentCategoryOverride,
			final AssessmentCategoryScore assessmentCategoryScore, 
			final AssessmentRating assessmentRating,
			final String notes, final StaffAssignment approvedStaffAssignment) {
		assessmentCategoryOverride.setApprovedStaffAssignment(
				approvedStaffAssignment);
		assessmentCategoryOverride.setAssessmentCategoryScore(
				assessmentCategoryScore);
		assessmentCategoryOverride.setAssessmentRating(assessmentRating);
		assessmentCategoryOverride.setNotes(notes);
		assessmentCategoryOverride.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}