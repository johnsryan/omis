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
package omis.paroleeligibility.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleeligibility.dao.ParoleEligibilityDao;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;

/**
 * Delegate for the parole eligibility.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.2 (Jul 3, 2018)
 * @since OMIS 3.0
 */
public class ParoleEligibilityDelegate {
	
	/* Instance factories. */
	private final InstanceFactory<ParoleEligibility> 
		paroleEligibilityInstanceFactory;
	
	/* DAOs. */
	private final ParoleEligibilityDao paroleEligibilityDao;
	
	/* Audit component retriever. */
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	public ParoleEligibilityDelegate(
			final InstanceFactory<ParoleEligibility> 
				paroleEligibilityInstanceFactory,
			final ParoleEligibilityDao paroleEligibilityDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.paroleEligibilityInstanceFactory = paroleEligibilityInstanceFactory;
		this.paroleEligibilityDao = paroleEligibilityDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a parole eligibility.
	 * 
	 * @param hearingEligibilityDate
	 * @param reviewDate
	 * @param appearanceCategory
	 * @return Creates a parole eligibility.
	 * @throws DuplicateEntityFoundException
	 */
	public ParoleEligibility create(
			final Offender offender,
			final Date hearingEligibilityDate,
			final Date reviewDate,
			final ParoleEligibilityStatus paroleEligibilityStatus,
			final AppearanceCategory appearanceCategory) 
		throws DuplicateEntityFoundException {
		if (this.paroleEligibilityDao.find(offender, hearingEligibilityDate) 
				!= null) {
			throw new DuplicateEntityFoundException(
					"Duplicate parole eligibility found.");
		}
		
		ParoleEligibility eligibility = 
			this.paroleEligibilityInstanceFactory.createInstance();
				eligibility.setStatus(paroleEligibilityStatus);
				eligibility.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
			this.populateParoleEligibility(eligibility, offender, 
					hearingEligibilityDate,	reviewDate, paroleEligibilityStatus, 
					appearanceCategory);
		
		return this.paroleEligibilityDao.makePersistent(eligibility);
	}
	
	/**
	 * Updates a parole eligibility
	 * 
	 * @param paroleEligibility
	 * @param hearingEligibilityDate
	 * @param reviewDate
	 * @param appearanceCategory
	 * @return Updates a parole eligibility.
	 * @throws DuplicateEntityFoundException
	 */
	public ParoleEligibility update(
			final ParoleEligibility paroleEligibility,
			final Offender offender,
			final Date hearingEligibilityDate,
			final Date reviewDate,
			final ParoleEligibilityStatus paroleEligibilityStatus,
			final AppearanceCategory appearanceCategory)
		throws DuplicateEntityFoundException {
		if (this.paroleEligibilityDao.findExcluding(paroleEligibility, offender,
				hearingEligibilityDate) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate parole eligibility found.");
		}
		this.populateParoleEligibility(paroleEligibility, offender,
			hearingEligibilityDate, reviewDate, paroleEligibilityStatus, 
			appearanceCategory);
		return this.paroleEligibilityDao.makePersistent(paroleEligibility);
	}
	
	/**
	 * Removes a parole eligibility
	 * 
	 * @param paroleEligibility
	 * @return Removes a parole eligibility.
	 */
	public void remove(final ParoleEligibility paroleEligibility) {
		this.paroleEligibilityDao.makeTransient(paroleEligibility);
	}
	
	/**
	 * Returns a list of Parole Eligibilities for an offender after the
	 * given date.
	 * 
	 * @param offender Offender
	 * @param date Date
	 * @return List of Parole Eligibilities for an offender after the
	 * given date.
	 */
	public List<ParoleEligibility> findByOffenderAfterDate(
			Offender offender, Date date) {
		return this.paroleEligibilityDao
				.findByOffenderAfterDate(offender, date);
	}
	
	/**
	 * Returns a list of Parole Eligibilities by the specified Parole Board
	 * Itinerary.
	 * 
	 * @param itinerary - Parole Board Itinerary
	 * @return List of Parole Eligibilities by the specified Parole Board
	 * Itinerary.
	 */
	public List<ParoleEligibility> findByItinerary(
			final ParoleBoardItinerary itinerary) {
		return this.paroleEligibilityDao.findByItinerary(itinerary);
	}
	
	/**
	 * Returns a list of Parole Eligibilities that have no scheduled hearing.
	 * 
	 * @return List of Parole Eligibilities that have no scheduled hearing.
	 */
	public List<ParoleEligibility> findUnscheduled() {
		return this.paroleEligibilityDao.findUnscheduled();
	}
	
	/**
	 * Returns a list of unscheduled Parole Eligibilities as well as
	 * Parole Eligibilities by the the specified Parole Board Itinerary.
	 * 
	 * @param including - Parole Board Itinerary
	 * @return List of unscheduled Parole Eligibilities as well as
	 * Parole Eligibilities by the the specified Parole Board Itinerary.
	 */
	public List<ParoleEligibility> findUnscheduledIncluding(
			final ParoleBoardItinerary including) {
		return this.paroleEligibilityDao.findUnscheduledIncluding(including);
	}
	
	/**
	 * Populates the specified parole eligibility.
	 * 
	 * @param paroleEligibility
	 * @param hearingEligibilityDate
	 * @param reviewDate
	 * @param appearanceCategory
	 */
	private void populateParoleEligibility(
			final ParoleEligibility paroleEligibility,
			final Offender offender,
			final Date hearingEligibilityDate,
			final Date reviewDate,
			final ParoleEligibilityStatus paroleEligibilityStatus,
			final AppearanceCategory appearanceCategory) {
		paroleEligibility.setOffender(offender);
		paroleEligibility.setHearingEligibilityDate(hearingEligibilityDate);
		paroleEligibility.setReviewDate(reviewDate);
		paroleEligibility.setAppearanceCategory(appearanceCategory);
		paroleEligibility.setStatus(paroleEligibilityStatus);
		paroleEligibility.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
	}
	
	
}
