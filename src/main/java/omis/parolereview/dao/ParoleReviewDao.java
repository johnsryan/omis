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
package omis.parolereview.dao;

import omis.dao.GenericDao;
import omis.offender.domain.Offender;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.StaffRoleCategory;
import omis.staff.domain.StaffAssignment;

/**
 * Data access object for parole review.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public interface ParoleReviewDao extends GenericDao<ParoleReview> {

	/**
	 * Returns the specified parole review for the specified staff 
	 * assignment and offender.
	 * 
	 * @param staffAssignment staff assignment
	 * @param offender offender
	 * @return parole review
	 */
	ParoleReview find(StaffAssignment staffAssignment, Offender offender, 
			StaffRoleCategory staffRole);
	
	/**
	 * Returns the specified parole review for the specified staff 
	 * assignment, offender, and staff role excluding the specified parole 
	 * review.
	 * 
	 * @param staffAssignment staff assignment
	 * @param offender offender
	 * @param excludedParoleReview excluded parole review
	 * @return parole review
	 */
	ParoleReview findExcluding(StaffAssignment staffAssignment, 
			Offender offender, StaffRoleCategory staffRole, 
			ParoleReview excludedParoleReview);
}