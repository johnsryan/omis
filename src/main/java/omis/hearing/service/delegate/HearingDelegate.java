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

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.dao.HearingDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.hearing.domain.component.Subject;
import omis.hearing.exception.HearingExistsException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.violationevent.domain.ViolationEvent;

/**
 * HearingDelegate.java
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.3 (May 17, 2018)
 * @since OMIS 3.0
 */
public class HearingDelegate {
	private static final String HEARING_EXISTS_FOUND_MSG =
			"Hearing exists with specified offender, location, date, and officer.";
	
	private final HearingDao hearingDao;
	
	private final InstanceFactory<Hearing> 
		hearingInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for HearingDelegate
	 * @param hearingDao
	 * @param hearingInstanceFactory
	 * @param auditComponentRetriever
	 */
	public HearingDelegate(
			final HearingDao hearingDao,
			final InstanceFactory<Hearing> 
				hearingInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.hearingDao = hearingDao;
		this.hearingInstanceFactory = hearingInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a hearing with the specified parameters
	 * @param location
	 * @param offender
	 * @param inAttendance - Boolean
	 * @param date
	 * @param category - HearingCategory
	 * @param status - HearingStatusCategory
	 * @param officer user account
	 * @return Newly Created Hearing
	 * @throws DuplicateEntityFoundException - when Hearing exists with
	 * specified offender, location, date, and officer
	 */
	public Hearing create(final Location location, final Offender offender,
			final Boolean inAttendance, final Date date,
			final HearingCategory category,
			final UserAccount officer)
			throws HearingExistsException{
		if(this.hearingDao.find(location, offender, date, officer, category) 
				!= null){
			throw new HearingExistsException(HEARING_EXISTS_FOUND_MSG);
		}
		
		Hearing hearing = 
				this.hearingInstanceFactory.createInstance();
		populateHearing(hearing, location, offender, inAttendance, date, 
				category, officer);
		hearing.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		return this.hearingDao.makePersistent(hearing);
	}

	/**
	 * Updates a hearing with the specified parameters
	 * @param hearing - Hearing to update
	 * @param location
	 * @param offender
	 * @param inAttendance - Boolean
	 * @param date
	 * @param category - HearingCategory
	 * @param officer user account
	 * @return Updated Hearing
	 * @throws DuplicateEntityFoundException - when Hearing exists with
	 * specified offender, location, date, and officer
	 */
	public Hearing update(final Hearing hearing, final Location location,
			final Boolean inAttendance, final Date date,
			final HearingCategory category,
			final UserAccount officer)
			throws HearingExistsException{
		if(this.hearingDao.findExcluding(location, 
				hearing.getSubject().getOffender(), date, officer, category, 
				hearing) != null){
			throw new HearingExistsException(HEARING_EXISTS_FOUND_MSG);
		}
		populateHearing(hearing, location, hearing.getSubject().getOffender(), 
				inAttendance, date, category, officer);
		return this.hearingDao.makePersistent(hearing);
	}
	
	/**
	 * Removes a hearing
	 * @param hearing - Hearing to remove
	 */
	public void remove(final Hearing hearing){
		this.hearingDao.makeTransient(hearing);
	}
	
	/**
	 * Returns a list of Hearings with specified offender
	 * @param offender
	 * @return List of Hearings with specified offender
	 */
	public List<Hearing> findByOffender(final Offender offender){
		return this.hearingDao.findByOffender(offender);
	}
	
	// Populates a hearing
	private void populateHearing(final Hearing hearing, final Location location,
			final Offender offender, final Boolean inAttendance, 
			final Date date, final HearingCategory category, 
			final UserAccount officer) {
		Subject subject = new Subject();
		subject.setInAttendance(inAttendance);
		subject.setOffender(offender);
		hearing.setLocation(location);
		hearing.setSubject(subject);
		hearing.setCategory(category);
		hearing.setDate(date);
		hearing.setOfficer(officer);
		hearing.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
	}

	/**
	 * Returns a list of hearings for the specified violation event.
	 * 
	 * @param violationEvent violation event
	 * @return list of hearings
	 */
	public List<Hearing> findByViolationEvent(
			final ViolationEvent violationEvent) {
		return this.hearingDao.findByViolationEvent(violationEvent);
	}
}