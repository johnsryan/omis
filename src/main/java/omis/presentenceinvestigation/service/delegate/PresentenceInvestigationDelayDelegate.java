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
package omis.presentenceinvestigation.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.presentenceinvestigation.dao.PresentenceInvestigationDelayDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;

/**
 * Presentence investigation delay delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationDelayDelegate {

	/* Data access objects. */
	
	private final PresentenceInvestigationDelayDao 
			presentenceInvestigationDelayDao;

	/* Instance factories. */
	
	private final InstanceFactory<PresentenceInvestigationDelay> 
			presentenceInvestigationDelayInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponenetRetriever;
	
	/** 
	 * Instantiates an implementation of presentence investigation delay 
	 * delegate with the specified data access object and instance factory.
	 *  
	 * @param presentenceInvestigationDelayDao presentence investigation
	 * delay data access object
	 * @param presentenceInvestigationDelayInstanceFactory presentence
	 * investigation delay instance factory
	 * @param auditComponentRetriever audit component retriever 
	 */
	public PresentenceInvestigationDelayDelegate(
			final PresentenceInvestigationDelayDao 
					presentenceInvestigationDelayDao,
			final InstanceFactory<PresentenceInvestigationDelay>
					presentenceInvestigationDelayInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.presentenceInvestigationDelayDao = 
				presentenceInvestigationDelayDao;
	this.presentenceInvestigationDelayInstanceFactory = 
			presentenceInvestigationDelayInstanceFactory;
		this.auditComponenetRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new presentence investigation delay.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @param date date
	 * @param reason presentence investigation delay category
	 * @return presentence investigation delay
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public PresentenceInvestigationDelay create(
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest, final Date date, 
			final PresentenceInvestigationDelayCategory reason) 
					throws DuplicateEntityFoundException {
		if (this.presentenceInvestigationDelayDao.find(date, 
				presentenceInvestigationRequest, reason) != null) {
			throw new DuplicateEntityFoundException(
					"Presentence investigation delay already exists.");
		}
		PresentenceInvestigationDelay presentenceInvestigationDelay = this
				.presentenceInvestigationDelayInstanceFactory.createInstance();
		populatePresentenceInvestigationDelay(presentenceInvestigationDelay, 
				presentenceInvestigationRequest, date, reason);
		presentenceInvestigationDelay.setCreationSignature(
				new CreationSignature(
						this.auditComponenetRetriever.retrieveUserAccount(), 
						this.auditComponenetRetriever.retrieveDate()));
		return this.presentenceInvestigationDelayDao.makePersistent(
				presentenceInvestigationDelay);
	}
	
	/**
	 * Updates an existing presentence investigation delay.
	 * 
	 * @param presentenceInvestigationDelay presentence investigation delay
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @param date date
	 * @param reason presentence investigation delay category
	 * @return presentence investigation delay
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public PresentenceInvestigationDelay update(
			final PresentenceInvestigationDelay presentenceInvestigationDelay,
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest, final Date date, 
			final PresentenceInvestigationDelayCategory reason) 
					throws DuplicateEntityFoundException {
		if (this.presentenceInvestigationDelayDao.findExcluding(date, 
				presentenceInvestigationRequest, reason, 
				presentenceInvestigationDelay) != null) {
			throw new DuplicateEntityFoundException(
					"Presentence investigation delay already exists.");
		}
		populatePresentenceInvestigationDelay(presentenceInvestigationDelay, 
				presentenceInvestigationRequest, date, reason);
		return this.presentenceInvestigationDelayDao.makePersistent(
				presentenceInvestigationDelay);
	}

	/**
	 * Removes the specified presentence investigation delay.
	 * 
	 * @param presentenceInvestigationDelay presentence investigation delay
	 */
	public void remove(
			final PresentenceInvestigationDelay presentenceInvestigationDelay) {
		this.presentenceInvestigationDelayDao.makeTransient(
				presentenceInvestigationDelay);
	}
	
	/**
	 * Returns a list of presentence investigation delays for the specified 
	 * presentence investigation request.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @return list of presentence investigation delays
	 */
	public List<PresentenceInvestigationDelay> 
			findByPresentenceInvestigationRequest(
					PresentenceInvestigationRequest 
							presentenceInvestigationRequest) {
		return this.presentenceInvestigationDelayDao
				.findByPresentenceInvestigationRequest(
						presentenceInvestigationRequest);
	}
	
	// Populates a presentence investigation delay
	private void populatePresentenceInvestigationDelay(
			final PresentenceInvestigationDelay presentenceInvestigationDelay,
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest, final Date date,
			final PresentenceInvestigationDelayCategory reason) {
		presentenceInvestigationDelay.setDate(date);
		presentenceInvestigationDelay.setPresentenceInvestigationRequest(
				presentenceInvestigationRequest);
		presentenceInvestigationDelay.setReason(reason);
		presentenceInvestigationDelay.setUpdateSignature(new UpdateSignature(
				this.auditComponenetRetriever.retrieveUserAccount(), 
				this.auditComponenetRetriever.retrieveDate()));
	}
}