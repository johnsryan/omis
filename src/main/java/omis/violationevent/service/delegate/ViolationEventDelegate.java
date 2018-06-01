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
package omis.violationevent.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Unit;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.violationevent.dao.ViolationEventDao;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.component.Event;

/**
 * Violation event delegate.
 * 
 * @author Annie Jacques
 * @author Ryan Johns 
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public class ViolationEventDelegate {
private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Violation Event already exists with given jurisdiction, hearing,"
			+ " date, details, and category for this offender.";
	
	private final ViolationEventDao violationEventDao;
	
	private final InstanceFactory<ViolationEvent> 
		violationEventInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for ViolationEventDelegate
	 * @param violationEventDao
	 * @param violationEventInstanceFactory
	 * @param auditComponentRetriever
	 */
	public ViolationEventDelegate(
			final ViolationEventDao violationEventDao,
			final InstanceFactory<ViolationEvent> 
				violationEventInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.violationEventDao = violationEventDao;
		this.violationEventInstanceFactory = violationEventInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new ViolationEvent with the specified properties
	 * @param offender - Offender
	 * @param jurisdiction - Organization
	 * @param unit unit
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return Newly Created ViolationEvent
	 * @throws DuplicateEntityFoundException - when a ViolationEvent already
	 * exists with all of the given properties for the Offender
	 */
	public ViolationEvent create(final Offender offender,
			final Organization jurisdiction, final Unit unit,
			final Date date, final String details,
			final ViolationEventCategory category)
					throws DuplicateEntityFoundException {
		if(this.violationEventDao.find(offender, jurisdiction, unit, date,
				details, category) != null){
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		ViolationEvent violationEvent = 
				this.violationEventInstanceFactory.createInstance();
		
		violationEvent.setOffender(offender);
		violationEvent.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		populateViolationEvent(violationEvent, jurisdiction, unit, date, 
				details, category);		
		return this.violationEventDao.makePersistent(violationEvent);
	}
	
	/**
	 * Updates a violation event with specified properties
	 * @param violationEvent - ViolationEvent to update
	 * @param jurisdiction - Organization
	 * @param unit unit
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return Updated ViolationEvent
	 * @throws DuplicateEntityFoundException - when a ViolationEvent already
	 * exists with all of the given properties for the Offender
	 */
	public ViolationEvent update(final ViolationEvent violationEvent,
			final Organization jurisdiction, final Unit unit,
			final Date date, final String details,
			final ViolationEventCategory category)
					throws DuplicateEntityFoundException {
		if(this.violationEventDao.findExcluding(violationEvent,
				violationEvent.getOffender(), jurisdiction, unit, date,
				details, category) != null){
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		populateViolationEvent(violationEvent, jurisdiction, unit, date, 
				details, category);
		
		return this.violationEventDao.makePersistent(violationEvent);
	}

	/**
	 * Removes a specified violationEvent
	 * @param violationEvent - ViolationEvent to remove
	 */
	public void remove(final ViolationEvent violationEvent) {
		this.violationEventDao.makeTransient(violationEvent);
	}
	
	/**
	 * Finds and returns a list of ViolationEvents by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents by specified offender
	 */
	public List<ViolationEvent> findByOffender(final Offender offender) {
		return this.violationEventDao.findByOffender(offender);
	}
	
	/**
	 * Finds and returns a list of ViolationEvents with no Infraction/resolution
	 * association by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents with no Infraction/resolution
	 * association by specified offender
	 */
	public List<ViolationEvent> findUnresolvedByOffender(
			final Offender offender) {
		return this.violationEventDao.findUnresolvedByOffender(offender);
	}
	
	// Populates a violation event
	private void populateViolationEvent(final ViolationEvent violationEvent, 
			final Organization jurisdiction, final Unit unit, final Date date, 
			final String details, final ViolationEventCategory category) {
		Event event = new Event();
		event.setDate(date);
		event.setDetails(details);
		
		violationEvent.setCategory(category);
		violationEvent.setEvent(event);
		violationEvent.setJurisdiction(jurisdiction);
		violationEvent.setUnit(unit);
		violationEvent.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
	}
}