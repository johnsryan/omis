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

import java.util.List;

import omis.assessment.dao.AssessmentCategoryOverrideReasonDao;
import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideReason;
import omis.assessment.domain.CategoryOverrideReason;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Assessment category override reason delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideReasonDelegate {

	/* Data access objects. */
	
	private final AssessmentCategoryOverrideReasonDao 
			assessmentCategoryOverrideReasonDao;

	/* Instance factories. */
	
	private final InstanceFactory<AssessmentCategoryOverrideReason> 
			assessmentCategoryOverrideReasonInstanceFactory;
	
	/**
	 * Instantiates an implementation of assessment category override reason 
	 * delegate with the specified date access object and instance factory.
	 * 
	 * @param assessmentCategoryOverrideReasonDao assessment category override 
	 * reason data access object
	 * @param assessmentCategoryOverrideReasonInstanceFactory assessment 
	 * category override reason instance factory
	 */
	public AssessmentCategoryOverrideReasonDelegate(
			final AssessmentCategoryOverrideReasonDao 
					assessmentCategoryOverrideReasonDao,
			final InstanceFactory<AssessmentCategoryOverrideReason> 
					assessmentCategoryOverrideReasonInstanceFactory) {
		this.assessmentCategoryOverrideReasonDao = 
				assessmentCategoryOverrideReasonDao;
		this.assessmentCategoryOverrideReasonInstanceFactory = 
				assessmentCategoryOverrideReasonInstanceFactory;
	}
	
	/**
	 * Creates a new assessment category override reason.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param categoryOverrideReason category override reason
	 * @return assessment category override reason
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryOverrideReason create(
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final CategoryOverrideReason categoryOverrideReason) 
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryOverrideReasonDao.find(
				assessmentCategoryOverride, categoryOverrideReason) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category override reason already exists.");
		}
		AssessmentCategoryOverrideReason assessmentCategoryOverrideReason = this
				.assessmentCategoryOverrideReasonInstanceFactory
				.createInstance();
		populateAssessmentCategoryOverrideReason(
				assessmentCategoryOverrideReason, assessmentCategoryOverride,
				categoryOverrideReason);
		return this.assessmentCategoryOverrideReasonDao.makePersistent(
				assessmentCategoryOverrideReason);
	}
	
	/**
	 * Updates an existing assessment category override reason.
	 * 
	 * @param assessmentCategoryOverrideReason assessment category override 
	 * reason
	 * @param assessmentCategoryOverride assessment category override
	 * @param categoryOverrideReason category override reason
	 * @return assessment category override reason
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryOverrideReason update(
			final AssessmentCategoryOverrideReason 
					assessmentCategoryOverrideReason,
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final CategoryOverrideReason categoryOverrideReason) 
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryOverrideReasonDao.findExcluding(
				assessmentCategoryOverride, categoryOverrideReason, 
				assessmentCategoryOverrideReason) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category override reason already exists.");
		}
		populateAssessmentCategoryOverrideReason(
				assessmentCategoryOverrideReason, assessmentCategoryOverride,
				categoryOverrideReason);
		return this.assessmentCategoryOverrideReasonDao.makePersistent(
				assessmentCategoryOverrideReason);
	}

	/**
	 * Removes the specified assessment category override reason.
	 * 
	 * @param assessmentCategoryOverrideReason assessment category override 
	 * reason
	 */
	public void remove(
			final AssessmentCategoryOverrideReason 
					assessmentCategoryOverrideReason) {
		this.assessmentCategoryOverrideReasonDao.makeTransient(
				assessmentCategoryOverrideReason);
	}

	/**
	 * Returns a list of assessment category override reasons for the specified 
	 * assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @return list of assessment category override reasons
	 */
	public List<AssessmentCategoryOverrideReason> 
			findByAssessmentCategoryOverride(
					final AssessmentCategoryOverride assessmentCategoryOverride) {
		return this.assessmentCategoryOverrideReasonDao
				.findByAssessmentCategoryOverride(assessmentCategoryOverride);
	}
	
	// Populates an assessment category override reason
	private void populateAssessmentCategoryOverrideReason(
			final AssessmentCategoryOverrideReason 
					assessmentCategoryOverrideReason,
			final AssessmentCategoryOverride assessmentCategoryOverride,
			final CategoryOverrideReason categoryOverrideReason) {
		assessmentCategoryOverrideReason.setAssessmentCategoryOverride(
				assessmentCategoryOverride);
		assessmentCategoryOverrideReason.setCategoryOverrideReason(
				categoryOverrideReason);
	}
}