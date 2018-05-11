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
package omis.hearing.web.form;

import java.io.Serializable;

import omis.hearing.domain.UserAttendance;
import omis.user.domain.UserAccount;

/**
 * User attendance item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 3, 2018)
 * @since OMIS 3.0
 */
public class UserAttendanceItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private UserAttendance userAttendance;
	
	private UserAccount userAccount;
	
	private ItemOperation itemOperation;
	
	/**
	 * Instantiates a user attendance item.
	 */
	public UserAttendanceItem() {
	}
	
	/**
	 * Returns the user attendance.
	 * 
	 * @return user attendance
	 */
	public UserAttendance getUserAttended() {
		return userAttendance;
	}

	/**
	 * Sets the user attendance.
	 * 
	 * @param userAttendance user attendance
	 */
	public void setUserAttended(UserAttendance userAttendance) {
		this.userAttendance = userAttendance;
	}

	/**
	 * Returns the user account.
	 * 
	 * @return user account
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * Sets the user account.
	 * 
	 * @param userAccount user account
	 */
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * Returns the item operation.
	 * 
	 * @return item operation
	 */
	public ItemOperation getItemOperation() {
		return itemOperation;
	}

	/**
	 * Sets the item operation.
	 * 
	 * @param itemOperation item operation
	 */
	public void setItemOperation(ItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
}