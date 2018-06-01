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
package omis.residence.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.audit.domain.VerificationSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.residence.dao.NonResidenceTermDao;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceStatus;
import omis.residence.exception.NonResidenceTermExistsException;

/**
 * Non-Residence term service implementation delegate.
 * 
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 4, 2016)
 * @since OMIS 3.0
 *
 */

public class NonResidenceTermDelegate {

	/* Data access objects. */
	
	private final NonResidenceTermDao nonResidenceTermDao;
		
	/* Component retrievers. */
	
	private AuditComponentRetriever auditComponentRetriever;

	/* Instance factories. */
	
	private final InstanceFactory<NonResidenceTerm> 
		nonResidenceTermInstanceFactory;
	
	/* Constructor. */

	/**
	 * Instantiates a non residence term service implementation delegate with 
	 * the specified data access object and instance factory.
	 * 
	 * @param nonResidenceTermDao non residence term dao	 
	 * @param auditComponentRetriever audit component retriever
	 * @param nonResidenceTermInstanceFactory 
	 * non residence term instance factory
	 */
	public NonResidenceTermDelegate(
			final NonResidenceTermDao nonResidenceTermDao,
			final AuditComponentRetriever auditComponentRetriever,
			final InstanceFactory<NonResidenceTerm>
				nonResidenceTermInstanceFactory) {
		this.nonResidenceTermDao = nonResidenceTermDao;
		this.auditComponentRetriever = auditComponentRetriever;
		this.nonResidenceTermInstanceFactory = nonResidenceTermInstanceFactory;
	}
	
	/* Management Methods. */
	
	/**
	 * Creates a non residence term. 
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param status status
	 * @param location location
	 * @param confirmed confirmed
	 * @param notes notes
	 * @param verificationSignature verification signature
	 * @return non residence term
	 * @throws NonResidenceTermExistsException thrown when a duplicate 
	 * non residence term exist
	 */
	public NonResidenceTerm createNonResidenceTerm(final Person person,
			final DateRange dateRange, final ResidenceStatus status, 
			final Location location, final Boolean confirmed,
			final String notes, 
			final VerificationSignature verificationSignature)
			throws NonResidenceTermExistsException {
		if (this.nonResidenceTermDao.find(
				person, location, status) != null) {
			throw new NonResidenceTermExistsException(
					"Duplicate non residence term found");
		}		
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermInstanceFactory
			.createInstance();
		nonResidenceTerm.setPerson(person);
		nonResidenceTerm.setDateRange(dateRange);
		nonResidenceTerm.setStatus(status);
		nonResidenceTerm.setLocation(location);
		nonResidenceTerm.setNotes(notes);
		nonResidenceTerm.setConfirmed(confirmed);
		nonResidenceTerm.setVerificationSignature(verificationSignature);

		nonResidenceTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		nonResidenceTerm.setUpdateSignature(new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		return this.nonResidenceTermDao.makePersistent(nonResidenceTerm);
	}
	
	/**
	 * Updates the specified non residence term.
	 * 
	 * @param nonResidenceTerm non residence term
	 * @param dateRange date range
	 * @param status status
	 * @param location location
	 * @param confirmed confirmed
	 * @param notes notes
	 * @param verificationSignature verification signature
	 * @return non residence term
	 * @throws NonResidenceTermExistsException thrown when a duplicate 
	 * non residence term exist
	 */
	public NonResidenceTerm updateNonResidenceTerm(
			final NonResidenceTerm nonResidenceTerm, final DateRange dateRange,
			final ResidenceStatus status, final Location location, 
			final Boolean confirmed, final String notes, 
			final VerificationSignature verificationSignature)
			throws NonResidenceTermExistsException {		
		if (this.nonResidenceTermDao.findExcluding(nonResidenceTerm.getPerson(),
				location, status, nonResidenceTerm) != null) {
			throw new NonResidenceTermExistsException(
					"Duplicate non residence term found");
		}		
		nonResidenceTerm.setDateRange(dateRange);
		nonResidenceTerm.setStatus(status);
		nonResidenceTerm.setLocation(location);
		nonResidenceTerm.setConfirmed(confirmed);
		nonResidenceTerm.setNotes(notes);
		nonResidenceTerm.setVerificationSignature(verificationSignature);
		nonResidenceTerm.setUpdateSignature(new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		return nonResidenceTerm;
	}

	/**
	 * Creates a new homeless term.
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param city city
	 * @param state state
	 * @param notes notes
	 * @param confirmed 
	 * @return homeless term
	 * @throws NonResidenceTermExistsException thrown when a duplicate 
	 * non residence term exist
	 */
	public NonResidenceTerm createHomelessTerm(final Person person,
			final DateRange dateRange, final City city, final State state, 
			final String notes, final Boolean confirmed) 
					throws NonResidenceTermExistsException {
		if (this.nonResidenceTermDao.find(person, 
				null, ResidenceStatus.HOMELESS) != null) {
			throw new NonResidenceTermExistsException(
					"Duplicate non residence term found");
		}		
		NonResidenceTerm nonResidenceTerm = this.nonResidenceTermInstanceFactory
				.createInstance();	
		nonResidenceTerm.setStatus(ResidenceStatus.HOMELESS);
		nonResidenceTerm.setPerson(person);
		nonResidenceTerm.setDateRange(dateRange);
		nonResidenceTerm.setCity(city);
		nonResidenceTerm.setState(state);
		nonResidenceTerm.setNotes(notes);
		nonResidenceTerm.setConfirmed(confirmed);
		nonResidenceTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		nonResidenceTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.nonResidenceTermDao.makePersistent(nonResidenceTerm);		
	}
	
	/**
	 * Updates a homeless term.
	 * 
	 * @param nonResidenceTerm non residence term
	 * @param dateRange date range
	 * @param city city
	 * @param state state
	 * @param notes notes
	 * @param confirmed confirmed
	 * @return homeless term
	 * @throws NonResidenceTermExistsException thrown when a duplicate 
	 * non residence term exist
	 */
	public NonResidenceTerm updateHomelessTerm(
			final NonResidenceTerm nonResidenceTerm, final DateRange dateRange,
			final City city, final State state, final String notes, 
			final Boolean confirmed) 
					throws NonResidenceTermExistsException {
		if (this.nonResidenceTermDao.findExcluding(nonResidenceTerm.getPerson(),
				null, nonResidenceTerm.getStatus(), nonResidenceTerm) != null) {
			throw new NonResidenceTermExistsException(
					"Duplicate non residence term found");
		}
		nonResidenceTerm.setConfirmed(confirmed);
		nonResidenceTerm.setDateRange(dateRange);
		nonResidenceTerm.setCity(city);
		nonResidenceTerm.setState(state);
		nonResidenceTerm.setNotes(notes);
		nonResidenceTerm.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.nonResidenceTermDao.makePersistent(nonResidenceTerm);
	}

	/**
	 * Removes a non residence term.
	 * 
	 * @param nonResidenceTerm non residence term
	 */
	public void removeNonResidenceTerm(
			final NonResidenceTerm nonResidenceTerm) {
		this.nonResidenceTermDao.makeTransient(nonResidenceTerm);	
	}
	
	/**
	 * Returns a list of non residence terms by person and date range.
	 *
	 *
	 * @param person person
	 * @param dateRange date range
	 * @return list of non residence terms
	 */
	public List<NonResidenceTerm> findNonResidenceTermByPersonAndDateRange(
			final Person person, final DateRange dateRange) {
		return this.nonResidenceTermDao
				.findNonResidenceTermByPersonAndDateRange(person, dateRange);
	}
	
	/**
	 * Returns a list of non residence terms by person and date range excluding
	 * the one in view.
	 *
	 *
	 * @param person person
	 * @param dateRange date range
	 * @param nonResidenceTerm non residence term
	 * @return list of non residence terms
	 */
	public List<NonResidenceTerm> 
		findNonResidenceTermByPersonAndDateRangeExcluding(final Person person, 
				final DateRange dateRange, 
				final NonResidenceTerm nonResidenceTerm) {
		return this.nonResidenceTermDao
				.findNonResidenceTermByPersonAndDateRangeExcluding(
						person, dateRange, nonResidenceTerm);
	}
	
	/**
	 * Returns a list of associated non residence terms.
	 *
	 *
	 * @param person person
	 * @param dateRange date range
	 * @return list of associated non residence terms
	 */
	public List<NonResidenceTerm> findAssociatedNonResidenceTerms(
			final Person person, final DateRange dateRange) {
		return this.nonResidenceTermDao.findAssociatedNonResidenceTerms(
				person, dateRange);
	}

	/**
	 * Returns non residence terms for the specified person on the 
	 * specified date.
	 *  
	 * @param person person
	 * @param date effective date
	 * @return list of non residence terms
	 */
	public List<NonResidenceTerm> findNonResidenceTermsByPersonAndDate(
			final Person person, final Date date) {
		return this.nonResidenceTermDao.findByPersonAndDate(person, date);
	}
}	