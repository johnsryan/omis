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
package omis.hearing.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.dao.UserAttendanceDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.UserAttendance;
import omis.hearing.exception.UserAttendanceExistsException;
import omis.instance.factory.InstanceFactory;
import omis.user.domain.UserAccount;

/**
 * Delegate for user attendance.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 3, 2018)
 * @since OMIS 3.0
 */
public class UserAttendanceDelegate {
	
	private static final String USER_ATTENDANCE_EXISTS_FOUND_MSG =
			"User attendance exists for specified hearing with given user "
			+ "account";
	
	private final UserAttendanceDao userAttendanceDao;
	
	private final InstanceFactory<UserAttendance> 
		userAttendanceInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for user attendance delegate.
	 * 
	 * @param userAttendanceDao user attendance data access object
	 * @param userAttendanceInstanceFactory user attendance instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public UserAttendanceDelegate(
			final UserAttendanceDao userAttendanceDao,
			final InstanceFactory<UserAttendance> 
				userAttendanceInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.userAttendanceDao = userAttendanceDao;
		this.userAttendanceInstanceFactory = userAttendanceInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new user attendance.
	 * 
	 * @param hearing hearing
	 * @param userAccount user account
	 * @return user attendance
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public UserAttendance create(final Hearing hearing,
			final UserAccount userAccount)throws UserAttendanceExistsException{
		if(this.userAttendanceDao.find(hearing, userAccount) != null){
			throw new UserAttendanceExistsException(
					USER_ATTENDANCE_EXISTS_FOUND_MSG);
		}
		UserAttendance userAttendance = this.userAttendanceInstanceFactory
				.createInstance();
		userAttendance.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		populateUserAttendance(userAttendance, hearing, userAccount);
		
		return this.userAttendanceDao.makePersistent(userAttendance);
	}
	
	/**
	 * Updates an existing user attendance.
	 * 
	 * @param userAttendance user attendance
	 * @param userAccount user account
	 * @return user attendance
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public UserAttendance update(final UserAttendance userAttendance,
			final Hearing hearing, final UserAccount userAccount) 
					throws UserAttendanceExistsException{
		if(this.userAttendanceDao.findExcluding(hearing, userAccount, 
				userAttendance) != null){
			throw new UserAttendanceExistsException(
					USER_ATTENDANCE_EXISTS_FOUND_MSG);
		}
		populateUserAttendance(userAttendance, hearing, userAccount);
		
		return this.userAttendanceDao.makePersistent(userAttendance);
	}

	/**
	 * Removes a user attendance.
	 * 
	 * @param userAttendance user attendance
	 */
	public void remove(final UserAttendance userAttendance){
		this.userAttendanceDao.makeTransient(userAttendance);
	}
	
	/**
	 * Returns a list of user attendances by the specified hearing.
	 * 
	 * @param hearing hearing
	 * @return list of user attendances
	 */
	public List<UserAttendance> findAllByHearing(final Hearing hearing){
		return this.userAttendanceDao.findByHearing(hearing);
	}
	
	// Populates a user attendance
	private void populateUserAttendance(final UserAttendance userAttendance, 
			final Hearing hearing, final UserAccount userAccount) {
		userAttendance.setHearing(hearing);
		userAttendance.setUserAccount(userAccount);
		userAttendance.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
	}
}