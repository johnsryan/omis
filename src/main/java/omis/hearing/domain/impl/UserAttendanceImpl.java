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
package omis.hearing.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.UserAttendance;
import omis.user.domain.UserAccount;

/**
 * Implementation of user attendance.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 3, 2018)
 * @since OMIS 3.0
 */
public class UserAttendanceImpl implements UserAttendance {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Hearing hearing;
	
	private UserAccount userAccount;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;

	/**
	 * Instantiates an implementation of user attendance. 
	 */
	public UserAttendanceImpl() {
		// Default constructor.
	}
	
	/**{@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/**{@inheritDoc} */
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	/**{@inheritDoc} */
	@Override
	public Hearing getHearing() {
		return this.hearing;
	}

	/**{@inheritDoc} */
	@Override
	public void setHearing(final Hearing hearing) {
		this.hearing = hearing;
	}

	/**{@inheritDoc} */
	@Override
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	/**{@inheritDoc} */
	@Override
	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
	/**{@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj){
		if(this == obj){
			return true;
		}
		if(!(obj instanceof UserAttendance)){
			return false;
		}
		
		UserAttendance that = (UserAttendance) obj;
		
		if(this.getHearing() == null){
			throw new IllegalStateException("Hearing required.");
		}
		if(!this.getHearing().equals(that.getHearing())){
			return false;
		}
		if(this.getUserAccount() == null){
			throw new IllegalStateException("User account required.");
		}
		if(!this.getUserAccount().equals(that.getUserAccount())){
			return false;
		}
		
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if(this.getHearing() == null){
			throw new IllegalStateException("Hearing required.");
		}
		if(this.getUserAccount() == null){
			throw new IllegalStateException("User account required.");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getHearing().hashCode();
		hashCode = 29 * hashCode + this.getUserAccount().hashCode();
		return hashCode;
	}
}