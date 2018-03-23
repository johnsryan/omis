package omis.visitation.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.visitation.dao.VisitDao;
import omis.visitation.domain.Visit;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.component.VisitFlags;

/**
 * Visit delegate.
 * 
 * @author Joel Norris
 * @version 0.1.0 (Dec 22, 2016)
 * @since OMIS 3.0
 */
public class VisitDelegate {
	
	/* Data access objects. */
	
	private final VisitDao visitDao;
	
	/* Component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Instance factories. */
	
	private final InstanceFactory<Visit> visitInstanceFactory;
	
	/**
	 * Instantiates a visit delegate with the specified data access object,
	 * component retriever and instance factory.
	 * 
	 * @param visitDao visit data access object
	 * @param auditComponentRetriever audit component retriever
	 * @param visitInstanceFactory visit instance factory
	 */
	public VisitDelegate(final VisitDao visitDao,
			final AuditComponentRetriever auditComponentRetriever,
			final InstanceFactory<Visit> visitInstanceFactory) {
		this.visitDao = visitDao;
		this.auditComponentRetriever = auditComponentRetriever;
		this.visitInstanceFactory = visitInstanceFactory;
	}

	/**
	 * Creates a visit with the specified values.
	 * 
	 * @param association visitation association
	 * @param date date of the visit
	 * @param startTime time the visit started
	 * @param endTime time the visit ended
	 * @param badgeNumber badge number
	 * @param flags visit flags
	 * @param notes notes
	 * @param location location
	 * @return created visit
	 * @throws DuplicateEntityFoundException thrown when a duplicate visit is 
	 * found
	 * @throws DateConflictException thrown when a visit is found within another
	 * visits start date and end date
	 */
	public Visit create(final VisitationAssociation association, 
			final Date startDate, final Date endDate, 
			final String badgeNumber, final VisitFlags flags, 
			final String notes, final Location location)
		throws DuplicateEntityFoundException, DateConflictException {
		if(this.visitDao.findVisit(association, startDate) 
				!= null) {
			throw new DuplicateEntityFoundException("Duplicate visit found");
		}
		if(!this.visitDao.findVisitsInRange(association,
				startDate, endDate).isEmpty()) {
			throw new DateConflictException(
					"Visit found within start and end time");
		}
		Visit visit = this.visitInstanceFactory.createInstance();
		visit.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		this.populateVisit(association, startDate, endDate, badgeNumber,
				flags, notes, visit, location);
		return this.visitDao.makePersistent(visit);
	}
	
	/**
	 * Updates the specified visit.
	 * 
	 * @param association visitation association
	 * @param startDate start date
	 * @param endDate end date 
	 * @param badgeNumber badge number
	 * @param flags visit flags
	 * @param notes notes
	 * @param visit visit
	 * @param location location
	 * @return updated visit
	 * @throws DuplicateEntityFoundException thrown when a duplicate visit is
	 * found.
	 * @throws DateConflictException thrown when a visit is found within the
	 * specified date range.
	 */
	public Visit update(final VisitationAssociation association, 
			final Date startDate, final Date endDate,
			final String badgeNumber, final VisitFlags flags, 
			final String notes, final Visit visit, final Location location)
		throws DuplicateEntityFoundException, DateConflictException {
		if(this.visitDao.findVisitExcluding(visit, association, startDate,
				endDate)!= null) {
			throw new DuplicateEntityFoundException("Duplicate visit found");
		}
		if(!this.visitDao.findVisitsInRangeExcluding(association, startDate,
				endDate, visit).isEmpty()) {
			throw new DateConflictException(
					"Visit found within start and end time");
		}
		this.populateVisit(association, startDate, endDate, badgeNumber,
				flags, notes, visit, location);
		return this.visitDao.makePersistent(visit);
	}
	
	/**
	 * Removes the specified visit.
	 * 
	 * @param visit visit
	 */
	public void remove(final Visit visit) {
		this.visitDao.makeTransient(visit);
	}
	
	/**
	 * Return a list of all visits for the specified offender.
	 * 
	 * @param offender offender
	 * @return list of visit
	 */
	public List<Visit> findVisitByOffender(final Offender offender) {
		return this.visitDao.findVisitByOffender(offender);
	}

	/**
	 * Returns visits for the specified visitation association.
	 * 
	 * @param association visitation association
	 * @return list of visits
	 */
	public List<Visit> findVisitsByVisitationAssociation(VisitationAssociation association) {
		return this.visitDao.findVisitsByAssociation(association);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates the specified visit with the specified values.
	 *  
	 * @param association visitation association
	 * @param startDate start date
	 * @param endDate end date
	 * @param badgeNumber badge number
	 * @param flags visit flags
	 * @param notes notes
	 * @param visit visit
	 * @param location location
	 * @return populated visit
	 */
	private Visit populateVisit(final VisitationAssociation association, 
			final Date startDate, final Date endDate, 
			final String badgeNumber, final VisitFlags flags, 
			final String notes, final Visit visit, final Location location) {
		visit.setVisitationAssociation(association);
		visit.setBadgeNumber(badgeNumber);
		visit.setFlags(flags);
		visit.setDateRange(new DateRange(startDate, endDate));
		visit.setNotes(notes);
		visit.setLocation(location);
		visit.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return visit;
	}
}