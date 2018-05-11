package omis.violationevent.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.violationevent.dao.ViolationEventDao;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.component.Event;

/**
 * ViolationEventDelegate.java
 * 
 *@author Annie Jacques
 *@author Ryan Johns 
 *@version 0.1.1 (May 10, 2018)
 *@since OMIS 3.0
 *
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
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return Newly Created ViolationEvent
	 * @throws DuplicateEntityFoundException - when a ViolationEvent already
	 * exists with all of the given properties for the Offender
	 */
	public ViolationEvent create(final Offender offender,
			final Organization jurisdiction,
			final Date date, final String details,
			final ViolationEventCategory category)
					throws DuplicateEntityFoundException{
		if(this.violationEventDao.find(offender, jurisdiction, date,
				details, category) != null){
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		ViolationEvent violationEvent = 
				this.violationEventInstanceFactory.createInstance();
		
		Event event = new Event();
		event.setDate(date);
		event.setDetails(details);
		
		violationEvent.setCategory(category);
		violationEvent.setEvent(event);
		violationEvent.setJurisdiction(jurisdiction);
		violationEvent.setOffender(offender);
		violationEvent.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		violationEvent.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.violationEventDao.makePersistent(violationEvent);
	}
	
	/**
	 * Updates a violation event with specified properties
	 * @param violationEvent - ViolationEvent to update
	 * @param jurisdiction - Organization
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return Updated ViolationEvent
	 * @throws DuplicateEntityFoundException - when a ViolationEvent already
	 * exists with all of the given properties for the Offender
	 */
	public ViolationEvent update(final ViolationEvent violationEvent,
			final Organization jurisdiction,
			final Date date, final String details,
			final ViolationEventCategory category)
					throws DuplicateEntityFoundException{
		if(this.violationEventDao.findExcluding(violationEvent,
				violationEvent.getOffender(), jurisdiction, date,
				details, category) != null){
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		Event event = new Event();
		event.setDate(date);
		event.setDetails(details);
		
		violationEvent.setCategory(category);
		violationEvent.setEvent(event);
		violationEvent.setJurisdiction(jurisdiction);
		violationEvent.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.violationEventDao.makePersistent(violationEvent);
	}
	
	/**
	 * Removes a specified violationEvent
	 * @param violationEvent - ViolationEvent to remove
	 */
	public void remove(final ViolationEvent violationEvent){
		this.violationEventDao.makeTransient(violationEvent);
	}
	
	/**
	 * Finds and returns a list of ViolationEvents by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents by specified offender
	 */
	public List<ViolationEvent> findByOffender(final Offender offender){
		return this.violationEventDao.findByOffender(offender);
	}
	
	/**
	 * Finds and returns a list of ViolationEvents with no Infraction/resolution
	 * association by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents with no Infraction/resolution
	 * association by specified offender
	 */
	public List<ViolationEvent> findUnresolvedByOffender(final Offender offender){
		return this.violationEventDao.findUnresolvedByOffender(offender);
	}
	
}
