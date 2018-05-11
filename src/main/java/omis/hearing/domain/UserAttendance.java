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
package omis.hearing.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.user.domain.UserAccount;

/**
 * Hearing user attendance.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 3, 2018)
 * @since OMIS 3.0
 */
public interface UserAttendance extends Creatable, Updatable {

	/** 
	 * Returns the id.
	 * 
	 * @return id. 
	 */
	Long getId();
	
	/** 
	 * Sets the id.
	 * 
	 * @param id id
	 */
	void setId(Long id);
	
	/**
	 * Returns the hearing.
	 * 
	 * @return hearing
	 */
	Hearing getHearing();
	
	/**
	 * Sets the hearing.
	 * 
	 * @param hearing
	 */
	void setHearing(Hearing hearing);
	
	/**
	 * Returns the user account.
	 * 
	 * @return user account
	 */
	UserAccount getUserAccount();
	
	/**
	 * Sets the user account.
	 * 
	 * @param userAccount user account
	 */
	void setUserAccount(UserAccount userAccount);
}