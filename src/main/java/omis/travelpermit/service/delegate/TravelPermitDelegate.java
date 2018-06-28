/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.travelpermit.dao.TravelPermitDao;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;
import omis.travelpermit.exception.TravelPermitExistsException;

/**
 * Delegate for travel permit.
 *
 * @author Yidong Li
 * @version 0.0.2 (Aug 18, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitDelegate {
	/* Resources. */
	private final TravelPermitDao travelPermitDao;
	private final InstanceFactory<TravelPermit> travelPermitInstanceFactory;
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	/**
	 * Instantiates delegate for managing travel permits.
	 * 
	 * @param travelPermitInstanceFactory instance factory for travel permit
	 * @param auditComponentRetriever audit component retriever
	 * @param travelPermitDao data access object for travel permit
	 */
	public TravelPermitDelegate(
		final TravelPermitDao travelPermitDao,
		final InstanceFactory<TravelPermit> travelPermitInstanceFactory,
		final AuditComponentRetriever auditComponentRetriever) {
		this.travelPermitDao = travelPermitDao;
		this.travelPermitInstanceFactory = travelPermitInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/* Methods. */
	/**
	 * Create travel permit.
	 * 
	 * @param offedner offender
	 * @param periodicity travel permit periodicity
	 * @param issuance travel permit issuance
	 * @param transportation travel transportation
	 * @param destination travel destination
	 * @param dateRange date range
	 * @param purpose purpose
	 * @param otherTravellers other travellers
	 * @throws TravelPermitExistsException permit already exists
	 * @return travel permit
	 */
	public TravelPermit createTravelPermit(final Offender offender, 
		final TravelPermitPeriodicity periodicity,
		final TravelPermitIssuance issuance,
		final TravelTransportation transportation,
		final TravelDestination destination,
		final DateRange dateRange,
		final String purpose,
		final OtherTravelers otherTravelers) 
		throws TravelPermitExistsException {
		if(this.travelPermitDao.findExistingTravelPermit(offender,
			dateRange.getStartDate())!=null){
			throw new TravelPermitExistsException(
				"Travel permit already exists");
		}
		
		TravelPermit travelPermit
		= this.travelPermitInstanceFactory.createInstance();
		travelPermit.setPurpose(purpose);
		travelPermit.setDestination(destination);
		travelPermit.setIssuance(issuance);
		travelPermit.setOffender(offender);
		travelPermit.setPeriodicity(periodicity);
		travelPermit.setTransportation(transportation);
		travelPermit.setDateRange(dateRange);
		travelPermit.setOtherTravellers(otherTravelers);
		travelPermit.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		travelPermit.setUpdateSignature(new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		return this.travelPermitDao.makePersistent(travelPermit);
	}	
	
	/**
	 * Updates travel permit
	 * 
	 * @param workAssignmentNote work assignment note
	 * @param date date
	 * @param notes notes
	 * @param dateRange date range
	 * @param otherTravellers other travellers
	 * @return a work assignment note
	 * @throws DuplicateEntityFoundException if work assignment note exists
	 */
	public TravelPermit updateTravelPermit(final TravelPermit permit, 
			final TravelPermitPeriodicity periodicity,
			final TravelPermitIssuance issuance,
			final TravelTransportation transportation,
			final TravelDestination destination,
			final DateRange dateRange,
			final String purpose,
			final OtherTravelers otherTravellers)
			throws TravelPermitExistsException{
		if (this.travelPermitDao.findExcludedExistingTravelPermit(
			permit, permit.getOffender(), dateRange.getStartDate())!=null) {
			throw new TravelPermitExistsException(
				"Travel permit already exists");
		}
		permit.setPurpose(purpose);
		permit.setPeriodicity(periodicity);
		permit.setIssuance(issuance);
		permit.setTransportation(transportation);
		permit.setDestination(destination);
		permit.setUpdateSignature(new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		permit.setOtherTravellers(otherTravellers);
		permit.setDateRange(dateRange);
		return this.travelPermitDao.makePersistent(permit);
	}
	
	/**
	 * Removes travel permit.
	 * 
	 * @param premit travel permit
	 */
	public void removePermit(final TravelPermit permit) {
		this.travelPermitDao.makeTransient(permit);
	}
	
	/**
	 * Find travel permits by offender
	 * 
	 * @param offender offender
	 * @return a list of travel permits
	 *
	 */
	public List<TravelPermit> findByOffender(final Offender offender){
		return this.travelPermitDao.findByOffender(offender);
	}
}