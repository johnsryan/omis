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
import omis.parolereview.domain.StaffRoleCategory;

/**
 * Data access object for staff role category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public interface StaffRoleCategoryDao extends GenericDao<StaffRoleCategory> {

	/**
	 * Returns the specified staff role category for the specified name.
	 * 
	 * @param name name
	 * @return staff role category
	 */
	StaffRoleCategory find(String name);
	
	/**
	 * Returns the specified staff role category for the specified name 
	 * excluding the specified staff role category.
	 * 
	 * @param name name
	 * @param excludedStaffRoleCategory excluded staff role category
	 * @return staff role category
	 */
	StaffRoleCategory findExcluding(String name, StaffRoleCategory excludedStaffRoleCategory);
}