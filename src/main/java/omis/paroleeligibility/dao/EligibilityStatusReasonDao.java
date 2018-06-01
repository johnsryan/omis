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
package omis.paroleeligibility.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;

/**
 * Eligibility status reason data access object.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public interface EligibilityStatusReasonDao 
	extends GenericDao<EligibilityStatusReason> {
	
	/**
	 * Finds eligibility status reasons.
	 * 
	 * @return Finds eligibility status reasons.
	 */
	List<EligibilityStatusReason> findEligibilityStatusReasons();

	/**
	 * Finds an eligibility status reason
	 * 
	 * @param name name of the eligibility status reason
	 * @param statusCategory Eligibility Status Category
	 * @return eligibility status reason
	 */
	EligibilityStatusReason findEligibilityStatusReason(
			String name, EligibilityStatusCategory statusCategory);

	/**
	 * Finds an eligibility status reason, not including the specified
	 * eligibility status reason.
	 * @param eligibilityStatusReason eligibility status reason
	 * @param name name of the eligibility status reason
	 * @param statusCategory Eligibility Status Category
	 * @return eligibility status reason, not including the specified
	 * eligibility status reason
	 */
	EligibilityStatusReason findEligibilityStatusReasonExcluding(
			EligibilityStatusReason eligibilityStatusReason, String name,
			EligibilityStatusCategory statusCategory);
	
	/**
	 * Returns a list of Eligibility Status Reasons by the specified
	 * Eligibility Status Category.
	 * 
	 * @param statusCategory Eligibility Status Category
	 * @return List of Eligibility Status Reasons by the specified
	 * Eligibility Status Category.
	 */
	List<EligibilityStatusReason> findByStatusCategory(
				EligibilityStatusCategory statusCategory);
	
}
