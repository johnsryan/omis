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
package omis.paroleplan.dao;

import omis.dao.GenericDao;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.domain.ParolePlan;

/**
 * Data access object for parole plan.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public interface ParolePlanDao extends GenericDao<ParolePlan> {

	/**
	 * Returns the parole plan with the specified parole eligibility.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @return parole plan
	 */
	ParolePlan find(ParoleEligibility paroleEligibility);

	/**
	 * Returns the parole plan with the specified parole eligibility excluding 
	 * the specified parole plan.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @param excludedParolePlan parole plan
	 * @return parole plan
	 */
	ParolePlan findExcluding(ParoleEligibility paroleEligibility, 
			ParolePlan excludedParolePlan);

	/**
	 * Returns the parole plan for the specified parole eligibility.
	 * 
	 * @param paroleEligibility parole eligibility
	 * @return parole plan
	 */
	ParolePlan findByParoleEligibility(ParoleEligibility paroleEligibility);
}