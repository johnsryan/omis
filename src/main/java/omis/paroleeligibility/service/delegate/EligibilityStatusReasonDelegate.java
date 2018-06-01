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
package omis.paroleeligibility.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleeligibility.dao.EligibilityStatusReasonDao;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;

/**
 * Delegate for the eligibility status reason.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public class EligibilityStatusReasonDelegate {
	
	/* Instance factories. */
	
	private final InstanceFactory<EligibilityStatusReason> 
		eligibilityStatusReasonInstanceFactory;
	
	/* DAOs. */
	
	private final EligibilityStatusReasonDao eligibilityStatusReasonDao;
	
	/* Constructor. */
	
	public EligibilityStatusReasonDelegate(
			final InstanceFactory<EligibilityStatusReason> 
				eligibilityStatusReasonInstanceFactory,
			final EligibilityStatusReasonDao eligibilityStatusReasonDao) {
		this.eligibilityStatusReasonInstanceFactory 
			= eligibilityStatusReasonInstanceFactory;
		this.eligibilityStatusReasonDao = eligibilityStatusReasonDao;
	}
	
	/**
	 * Creates a new eligibility status reason
	 * 
	 * @param name eligibility status reason name
	 * @param valid whether an eligibility status reason is valid
	 * @return eligibility status reason
	 * @throws DuplicateEntityFoundException
	 */
	public EligibilityStatusReason create(
			final String name, final EligibilityStatusCategory statusCategory,
			final Boolean valid)
		throws DuplicateEntityFoundException {
		if (this.eligibilityStatusReasonDao.findEligibilityStatusReason(
				name, statusCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate eligibility status reason found.");
		}
		
		EligibilityStatusReason statusReason = 
				this.eligibilityStatusReasonInstanceFactory.createInstance();
				this.populateEligibilityStatusReason(statusReason, name,
						statusCategory, valid);
		return this.eligibilityStatusReasonDao.makePersistent(statusReason);
	}
	
	/**
	 * Updates an eligibility status reason
	 * 
	 * @param eligibilityStatusReason eligibility status reason
	 * @param name name of the eligibility status reason
	 * @param valid whether an eligibility status reason is valid
	 * @return updated eligibilty status reason
	 * @throws DuplicateEntityFoundException
	 */
	public EligibilityStatusReason update(
			final EligibilityStatusReason eligibilityStatusReason,
			final String name, final EligibilityStatusCategory statusCategory,
			final Boolean valid)
		throws DuplicateEntityFoundException {
		if (this.eligibilityStatusReasonDao
				.findEligibilityStatusReasonExcluding(eligibilityStatusReason,
						name, statusCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate eligibility status reason found.");
		}
		this.populateEligibilityStatusReason(
				eligibilityStatusReason, name, statusCategory, valid);
		return this.eligibilityStatusReasonDao.makePersistent(
				eligibilityStatusReason);
	}
	
	/**
	 * Removes an eligibility status reason
	 * 
	 * @param eligibilityStatusReason
	 */
	public void remove(final EligibilityStatusReason eligibilityStatusReason) {
		this.eligibilityStatusReasonDao.makeTransient(eligibilityStatusReason);
	}
	
	/**
	 * Returns eligibility status reasons
	 * 
	 * @param eligibilityStatusReason
	 * @return eligibility status reasons
	 */
	public List<EligibilityStatusReason> findEligibilityStatusReasons() {
		return this.eligibilityStatusReasonDao
				.findEligibilityStatusReasons();
	}
	
	/**
	 * Populates the specified eligibility status reason.
	 * 
	 * @param eligibilityStatusReason
	 * @param name
	 * @param valid
	 */
	private void populateEligibilityStatusReason(
			final EligibilityStatusReason eligibilityStatusReason,
			final String name,
			final EligibilityStatusCategory statusCategory,
			final Boolean valid) {
		eligibilityStatusReason.setName(name);
		eligibilityStatusReason.setStatusCategory(statusCategory);
		eligibilityStatusReason.setValid(valid);
	}
	
	/**
	 * Returns a list of Eligibility Status Reasons by the specified
	 * Eligibility Status Category.
	 * 
	 * @param statusCategory Eligibility Status Category
	 * @return List of Eligibility Status Reasons by the specified
	 * Eligibility Status Category.
	 */
	public List<EligibilityStatusReason> findByStatusCategory(
				final EligibilityStatusCategory statusCategory) {
		return this.eligibilityStatusReasonDao.findByStatusCategory(
				statusCategory);
	}
}
