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
package omis.hearing.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.UserAttendance;
import omis.user.domain.UserAccount;

/**
 * Data access object for user attendance.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 3, 2018)
 * @since OMIS 3.0
 */
public interface UserAttendanceDao extends GenericDao<UserAttendance> {
	
	/**
	 * Returns a user attendance with the specified properties.
	 * 
	 * @param hearing hearing
	 * @param userAccount user account
	 * @return user attendance
	 */
	UserAttendance find(Hearing hearing, UserAccount userAccount);
	
	/**
	 * Returns a user attendance with the specified properties excluding the 
	 * specified user attendance.
	 * 
	 * @param hearing hearing
	 * @param userAccount user account
	 * @param excludedUserAttendance excluded user attendance
	 * @return user attendance
	 */
	UserAttendance findExcluding(Hearing hearing, UserAccount userAccount,
			UserAttendance excludedUserAttendance);
	
	/**
	 * Returns a list of user attendances by specified hearing.
	 * 
	 * @param hearing hearing
	 * @return list of user attendances
	 */
	List<UserAttendance> findByHearing(Hearing hearing);
}