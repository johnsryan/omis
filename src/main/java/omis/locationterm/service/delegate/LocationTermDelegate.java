package omis.locationterm.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.locationterm.dao.LocationTermDao;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;

/**
 * Delegate for location terms.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Feb 18, 2015)
 * @since OMIS 3.0
 */
public class LocationTermDelegate {

	private final LocationTermDao locationTermDao;
	
	private final InstanceFactory<LocationTerm> locationTermInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates a delegate for location terms.
	 * 
	 * @param locationTermDao data access object for location terms
	 * @param locationTermInstanceFactory instance factory for location terms
	 * @param auditComponentRetriever audit component retriever
	 */
	public LocationTermDelegate(
			final LocationTermDao locationTermDao,
			final InstanceFactory<LocationTerm> locationTermInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.locationTermDao = locationTermDao;
		this.locationTermInstanceFactory = locationTermInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Returns location term for offender on date.
	 * 
	 * @param offender offender
	 * @param date date
	 * @return location term for offender on date
	 */
	public LocationTerm findByOffenderOnDate(final Offender offender,
			final Date date) {
		return this.locationTermDao.findByOffenderOnDate(offender, date);
	}

	/**
	 * Creates new location term.
	 * 
	 * @param offender offender
	 * @param location location
	 * @param startDate start date
	 * @param endDate end date
	 * @param locked whether locked
	 * @return new location term
	 * @throws DuplicateEntityFoundException if location term exists
	 */
	public LocationTerm create(
			final Offender offender, final Location location,
			final Date startDate, final Date endDate, final Boolean locked)
				throws DuplicateEntityFoundException {
		
		// Throws exception if location term exists
		if (this.locationTermDao.find(offender, startDate, endDate) != null) {
			throw new DuplicateEntityFoundException("Location term exists");
		}
		
		// Creates and persists location term
		LocationTerm locationTerm
			= this.locationTermInstanceFactory.createInstance();
		locationTerm.setOffender(offender);
		locationTerm.setLocation(location);
		locationTerm.setDateRange(new DateRange(startDate, endDate));
		locationTerm.setLocked(locked);
		locationTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		locationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.locationTermDao.makePersistent(locationTerm);
	}
	
	/**
	 * Updates existing location term.
	 * 
	 * @param locationTerm location term
	 * @param location location
	 * @param startDate start date
	 * @param endDate end date
	 * @param locked whether locked
	 * @return update location term
	 * @throws DuplicateEntityFoundException
	 */
	public LocationTerm update(
				final LocationTerm locationTerm, final Location location,
				final Date startDate, final Date endDate, final Boolean locked)
			throws DuplicateEntityFoundException {
		
		// Throws exception if location term exists
		if (this.locationTermDao.findExcluding(
				locationTerm.getOffender(), startDate, endDate, locationTerm)
					!= null) {
			throw new DuplicateEntityFoundException("Location term exists");
		}
		
		// Updates and persists location term
		locationTerm.setLocation(location);
		locationTerm.setDateRange(new DateRange(startDate, endDate));
		locationTerm.setLocked(locked);
		locationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.locationTermDao.makePersistent(locationTerm);
	}
	
	/**
	 * Returns number of location terms for offender between dates that are not
	 * excluded.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedLocationTerms excluded location terms
	 * @return number of location terms for offender between dates
	 */
	public long countExcluding(
			final Offender offender, final Date startDate, final Date endDate,
			LocationTerm... excludedLocationTerms) {
		return this.locationTermDao.countExcluding(offender, startDate, endDate,
				excludedLocationTerms);
	}
	
	/**
	 * Returns number of location terms for offender between dates.
	 * 
	 * @param offender offender
	 * @param startDate start date
	 * @param endDate end date
	 * @return number of location terms for offender between dates
	 */
	public long count(
			final Offender offender, final Date startDate, final Date endDate) {
		return this.locationTermDao.count(offender, startDate, endDate);
	}

	/**
	 * Changes date range of location term.
	 * 
	 * @param locationTerm location term
	 * @param startDate start date
	 * @param endDate end date
	 * @return updated location term
	 * @throws DuplicateEntityFoundException if term with changed dates exists
	 */
	public LocationTerm changeDateRange(
			final LocationTerm locationTerm,
			final Date startDate, final Date endDate)
				throws DuplicateEntityFoundException {
		if (this.locationTermDao.find(locationTerm.getOffender(),
				startDate, endDate) != null) {
			throw new DuplicateEntityFoundException();
		}
		locationTerm.setDateRange(new DateRange(startDate, endDate));
		locationTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.locationTermDao.makePersistent(locationTerm);
	}
	
	/**
	 * End location term.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return ended location term
	 * @deprecated update location term instead
	 */
	@Deprecated
	public LocationTerm endLocationTerm(
			final Offender offender, final Date effectiveDate) {
		return this.locationTermDao.endLocationTerm(offender, effectiveDate);
	}
}