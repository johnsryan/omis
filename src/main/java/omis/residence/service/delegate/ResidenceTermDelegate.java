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

import omis.address.domain.Address;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.audit.domain.VerificationSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.residence.dao.ResidenceTermDao;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.ResidenceTermExistsException;

/**
 * Residence term service implementation delegate.
 * 
 * @author Sheronda Vaughn
 * @author Yidong Li
 * @version 0.1.0 (May 5, 2015)
 * @since OMIS 3.0
 *
 */
public class ResidenceTermDelegate {

	/* Data access objects. */
	
	private final ResidenceTermDao residenceTermDao;
	
	/* Instance factories. */	

	private InstanceFactory<ResidenceTerm> 
		residenceTermInstanceFactory;
	
	/* Component retrievers. */
	
	private AuditComponentRetriever auditComponentRetriever;

	/* Constructor. */
	
	/**
	 * Instantiates a residence term service implementation delegate with 
	 * the specified date access object and instance factory.
	 * 
	 * @param residenceTermDao residence term dao
	 * @param residenceTermInstanceFactory residence term instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public ResidenceTermDelegate(
			final ResidenceTermDao residenceTermDao,
			final InstanceFactory<ResidenceTerm> residenceTermInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.residenceTermDao = residenceTermDao;
		this.residenceTermInstanceFactory = residenceTermInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Management methods. */
	
	/**
	 * Creates a residence term. 
	 * 
	 * @param person person
	 * @param dateRange date range
	 * @param residenceCategory residence category
	 * @param address address
	 * @param residenceStatus residence status
	 * @param confirmed confirmed
	 * @param notes notes
	 * @param verificationSignature verification signature
	 * @return residence term
	 * @throws ResidenceTermExistsException thrown when a duplicate residence
	 * term exist
	 */
	public ResidenceTerm createResidenceTerm(final Person person,
			final DateRange dateRange, 
			final ResidenceCategory residenceCategory, 
			final Address address, final ResidenceStatus residenceStatus, 
			final Boolean confirmed, final String notes, 
			final VerificationSignature verificationSignature)
			throws ResidenceTermExistsException {
		if (this.residenceTermDao.find(person, dateRange, address) != null) { 
			throw new ResidenceTermExistsException(
					"Duplicate residence term found");
		}		
		ResidenceTerm residenceTerm = this.residenceTermInstanceFactory
				.createInstance();
		residenceTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));	
		residenceTerm.setPerson(person);
		residenceTerm.setStatus(residenceStatus);
		residenceTerm.setCategory(residenceCategory);
		this.populateResidenceTerm(residenceTerm, dateRange, 
				address, verificationSignature, notes, confirmed);		
		
		return this.residenceTermDao.makePersistent(residenceTerm);
	}
	
	
	/**
	 * Updates the specified residence term.
	 * 
	 * @param residenceTerm residence term
	 * @param dateRange date range
	 * @param residenceCategory residence category
	 * @param address address
	 * @param residenceStatus residence status
	 * @param confirmed confirmed
	 * @param notes notes
	 * @param verificationSignature verification signature
	 * @return update residence term
	 * @throws ResidenceTermExistsException thrown when a duplicate 
	 * residence term is found excluding the residence term in view
	 */
	public ResidenceTerm updateResidenceTerm(final ResidenceTerm residenceTerm,
			final DateRange dateRange, 
			final ResidenceCategory residenceCategory, 
			final Address address, final ResidenceStatus residenceStatus, 
			final Boolean confirmed, final String notes, 
			final VerificationSignature verificationSignature)
			throws ResidenceTermExistsException {	
		if (this.residenceTermDao.findExcluding(
				residenceTerm.getPerson(), dateRange, address, 
				residenceTerm) != null) {
			throw new ResidenceTermExistsException(
					"Duplicate residence term found");
		}		
		this.populateResidenceTerm(residenceTerm, dateRange, address, 
				verificationSignature, notes, confirmed);
		residenceTerm.setCategory(residenceCategory);
		residenceTerm.setStatus(residenceStatus);
		return this.residenceTermDao.makePersistent(residenceTerm);
	}
	
	/**
	 * Removes a residence term.
	 * 
	 * @param residenceTerm residence term
	 */
	public void removeResidenceTerm(final ResidenceTerm residenceTerm) {
		this.residenceTermDao.makeTransient(residenceTerm);
	}
	
	/**
	 * Returns the residence term with the specified properties.
	 * 
	 * @param person person
	 * @param startDate start date
	 * @param endDate end date
	 * @param address address
	 * @param category residence category
	 * @param status residence status
	 * @return residence term
	 */
	public ResidenceTerm find(final Person person, final Date startDate,
			final Date endDate, final Address address,
			final ResidenceCategory category,
			final ResidenceStatus status) {
		return this.residenceTermDao.find(person,
				new DateRange(startDate, endDate), address);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates the specified residence term with the specified properties
	 * and an update signature.
	 * 
	 * @param residenceTerm residence term
	 * @param dateRange date range
	 * @param address address
	 * @param verificationSignature verification signature
	 * @param notes notes 
	 * @param confirmed confirmed
	 * @return populated residence term
	 */
	private ResidenceTerm populateResidenceTerm(
			final ResidenceTerm residenceTerm,
			final DateRange dateRange,
			final Address address,
			final VerificationSignature verificationSignature,
			final String notes,
			final Boolean confirmed) {
		residenceTerm.setDateRange(dateRange);
		residenceTerm.setAddress(address);
		residenceTerm.setVerificationSignature(verificationSignature);
		residenceTerm.setNotes(notes);
		residenceTerm.setConfirmed(confirmed);		
		residenceTerm.setUpdateSignature(new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		return residenceTerm;
	}	
	
	/**
	 * Returns the residence term with the specified properties.
	 * 
	 * @param person person
	 * @param effectiveDate effective date
	 * @param category residence category
	 * @param status residence status
	 * @return residence term
	 */
	public ResidenceTerm findResidenceTerm(final Person person, 
		final Date effectiveDate, final ResidenceCategory category,
		final ResidenceStatus status) {
		return this.residenceTermDao.findOnDate(person, effectiveDate, status, 
			category);
	}
	
	/**
	 * Returns the primary residence term for the specified person on the
	 * specified date.
	 * 
	 * @param person person
	 * @param date date
	 * @param category residence category
	 * @return primary residence term
	 */
	public ResidenceTerm findByPersonAndDate(final Person person, 
			final Date date, final ResidenceCategory category) {
		return this.residenceTermDao.findByPersonAndDate(
				person, date, category);
	}
	
	/**
	 * Returns list of associated residence terms by person and date range.
	 *
	 *
	 * @param person person
	 * @param dateRange date range
	 * @return list of residence terms
	 */
	public List<ResidenceTerm> findAssociatedResidenceTerms(final Person person,
			final DateRange dateRange) {
		return this.residenceTermDao.findAssociatedResidenceTerms(
				person, dateRange);
	}
	
	/**
	 * Returns list of residence terms by person and date range.
	 *
	 *
	 * @param person person
	 * @param dateRange date range
	 * @return list of residence terms
	 */
	public List<ResidenceTerm> findResidenceTermsByPerson(final Person person,
			final DateRange dateRange) {
		return this.residenceTermDao.findAssociatedResidenceTerms(
				person, dateRange);
	}
	
	/**
	 * Returns list of residence terms by person excluding on in view.
	 *
	 *
	 * @param person person
	 * @param residenceTerm residence term
	 * @param address address
	 * @param dateRange date range
	 * @return list of residence terms
	 */
	public List<ResidenceTerm> findResidenceTermsByPersonExcluding(final Person
			person, final ResidenceTerm residenceTerm, final Address address, 
			final DateRange dateRange) {
		return this.residenceTermDao.findResidenceTermsByPersonExcluding(
				person, residenceTerm, address, dateRange);
	}
	
	/**
	 * Returns a list of residence terms by offender.
	 *
	 *
	 * @param offender offender
	 * @param date date
	 * @return list of residence terms
	 */
	public List<ResidenceTerm> findResidenceTermsByOffender(
			final Offender offender, final Date date) {
		return this.residenceTermDao.findResidenceTermsByOffender(
				offender, date);
	}
}