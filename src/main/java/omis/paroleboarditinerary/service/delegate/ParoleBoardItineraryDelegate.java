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
package omis.paroleboarditinerary.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.paroleboarditinerary.dao.ParoleBoardItineraryDao;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Parole board itinerary delegate.
 *
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.4 (Apr 18, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryDelegate {

	/* Data access objects. */
	
	private final ParoleBoardItineraryDao paroleBoardItineraryDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParoleBoardItinerary> 
			paroleBoardItineraryInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/** Instantiates an implementation of parole board itinerary delegate with 
	 * the specified date access object and instance factory.
	 * 
	 * @param paroleBoardItineraryDao parole board itinerary data access object
	 * @param paroleBoardItineraryInstanceFactory parole board itinerary 
	 * instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ParoleBoardItineraryDelegate(
			final ParoleBoardItineraryDao paroleBoardItineraryDao,
			final InstanceFactory<ParoleBoardItinerary> 
				paroleBoardItineraryInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleBoardItineraryDao = paroleBoardItineraryDao;
		this.paroleBoardItineraryInstanceFactory = 
				paroleBoardItineraryInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new parole board itinerary.
	 * 
	 * @param paroleBoardLocation parole board location
	 * @param startDate start date
	 * @param endDate end date
	 * @return parole board itinerary
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleBoardItinerary create(
			final ParoleBoardLocation paroleBoardLocation, final Boolean onsite,
			final Date startDate, final Date endDate) 
					throws DuplicateEntityFoundException {
		if (this.paroleBoardItineraryDao.find(paroleBoardLocation,
				startDate, endDate)  != null) {
			throw new DuplicateEntityFoundException(
					"Parole board itinerary already exists.");
		}
		ParoleBoardItinerary boardItinerary = this
				.paroleBoardItineraryInstanceFactory.createInstance();
		boardItinerary.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		populateBoardItinerary(boardItinerary, paroleBoardLocation, onsite,
				startDate, endDate);
		return this.paroleBoardItineraryDao.makePersistent(boardItinerary);
	}

	/**
	 * Updates an existing parole board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @param paroleBoardLocation parole board location
	 * @param startDate start date
	 * @param endDate end date
	 * @return parole board itinerary
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleBoardItinerary update(
			final ParoleBoardItinerary boardItinerary,
			final ParoleBoardLocation paroleBoardLocation, final Boolean onsite,
			final Date startDate, final Date endDate) 
					throws DuplicateEntityFoundException {
		if (this.paroleBoardItineraryDao.findExcluding(paroleBoardLocation,
				startDate, endDate, boardItinerary) != null) {
			throw new DuplicateEntityFoundException(
					"Parole board itinerary already exists.");
		}
		populateBoardItinerary(boardItinerary, paroleBoardLocation, onsite,
				startDate, endDate);
		return this.paroleBoardItineraryDao.makePersistent(boardItinerary);
	}
	
	/**
	 * Removes the specified parole board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 */
	public void remove(final ParoleBoardItinerary boardItinerary) {
		this.paroleBoardItineraryDao.makeTransient(boardItinerary);
	}
	
	/**
	 * Returns a list of parole board itineraries after the specified date.
	 * 
	 * @param effectiveDate effective date
	 * @return list of parole board itineraries
	 */
	public List<ParoleBoardItinerary> findByEffectiveDate(
			final Date effectiveDate) {
		return this.paroleBoardItineraryDao.findByEffectiveDate(effectiveDate);
	}
	
	// Populates a parole board itinerary
	private void populateBoardItinerary(
			final ParoleBoardItinerary boardItinerary,
			final ParoleBoardLocation paroleBoardLocation, final Boolean onsite,
			final Date startDate, final Date endDate) {
		boardItinerary.setParoleBoardLocation(paroleBoardLocation);
		boardItinerary.setOnsite(onsite);
		boardItinerary.setDateRange(new DateRange(startDate, endDate));
		boardItinerary.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}