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
package omis.person.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.instance.factory.InstanceFactory;
import omis.person.dao.AlternativeIdentityAssociationDao;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.Person;
import omis.person.domain.PersonIdentity;
import omis.person.exception.AlternativeIdentityAssociationExistsException;

/**
 * Delegate for alternative identity associations.
 * 
 * @author Annie Jacques
 * @author Stephen Abson 
 * @version 0.1.0 (Nov 9, 2016)
 * @since OMIS 3.0
 */
public class AlternativeIdentityAssociationDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Alternative Identity Association Already Exists For Given Person "
			+ "Identity, Category, and Date Range";
	
	private final AlternativeIdentityAssociationDao 
		alternativeIdentityAssociationDao;
	
	private final InstanceFactory<AlternativeIdentityAssociation> 
		alternativeIdentityAssociationInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Instantiates alternative identity association delegate.
	 * 
	 * @param alternativeIdentityAssociationDao data access object for
	 * alternative identity association
	 * @param alternativeIdentityAssociationDelegateInstanceFactory instance
	 * factory for alternative identity association
	 * @param auditComponentRetriever audit component retriever
	 */
	public AlternativeIdentityAssociationDelegate(
			final AlternativeIdentityAssociationDao 
				alternativeIdentityAssociationDao,
			final InstanceFactory<AlternativeIdentityAssociation> 
				alternativeIdentityAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.alternativeIdentityAssociationDao = 
				alternativeIdentityAssociationDao;
		this.alternativeIdentityAssociationInstanceFactory = 
				alternativeIdentityAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates an Alternative Identity Association with Given Parameters.
	 * 
	 * @param personIdentity
	 * @param dateRange
	 * @param alternativeIdentityCategory
	 * @param alternativeNameAssociation
	 * @return AlternativeIdentityAssociation Alternative Identity Association 
	 * with Given Parameters
	 * @throws AlternativeIdentityAssociationExistsException if alternative
	 * identity association already exists
	 */
	public AlternativeIdentityAssociation create(
			final PersonIdentity personIdentity, final DateRange dateRange, 
			final AlternativeIdentityCategory alternativeIdentityCategory, 
			final AlternativeNameAssociation alternativeNameAssociation)
					throws AlternativeIdentityAssociationExistsException {
		if(this.alternativeIdentityAssociationDao
				.find(personIdentity, alternativeIdentityCategory, dateRange) 
					!= null){
			throw new AlternativeIdentityAssociationExistsException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		AlternativeIdentityAssociation alternativeIdentityAssociation = 
				this.alternativeIdentityAssociationInstanceFactory
					.createInstance();
		
		alternativeIdentityAssociation.setAlternativeNameAssociation(
				alternativeNameAssociation);
		alternativeIdentityAssociation.setCategory(alternativeIdentityCategory);
		alternativeIdentityAssociation.setDateRange(dateRange);
		alternativeIdentityAssociation.setIdentity(personIdentity);
		alternativeIdentityAssociation.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		alternativeIdentityAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.alternativeIdentityAssociationDao
				.makePersistent(alternativeIdentityAssociation);
	}
	
	/**
	 * Updates an alternative identity association with the given parameters.
	 * 
	 * @param alternativeIdentityAssociation association to update
	 * @param personIdentity person identity
	 * @param dateRange date range
	 * @param alternativeIdentityCategory category
	 * @param alternativeNameAssociation associated name
	 * @return AlternativeIdentityAssociation updated alternative identity 
	 * association with the given parameters
	 * @throws AlternativeIdentityAssociationExistsException if alternative
	 * identity association already exists
	 */
	public AlternativeIdentityAssociation update(
			final AlternativeIdentityAssociation alternativeIdentityAssociation,
			final PersonIdentity personIdentity, final DateRange dateRange,
			final AlternativeIdentityCategory alternativeIdentityCategory,
			final AlternativeNameAssociation alternativeNameAssociation)
			throws AlternativeIdentityAssociationExistsException {
		if(this.alternativeIdentityAssociationDao
				.findExcluding(alternativeIdentityAssociation, 
						personIdentity, alternativeIdentityCategory, dateRange) 
							!= null) {
			throw new AlternativeIdentityAssociationExistsException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		alternativeIdentityAssociation.setAlternativeNameAssociation(
				alternativeNameAssociation);
		alternativeIdentityAssociation.setCategory(alternativeIdentityCategory);
		alternativeIdentityAssociation.setDateRange(dateRange);
		alternativeIdentityAssociation.setIdentity(personIdentity);
		alternativeIdentityAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.alternativeIdentityAssociationDao
				.makePersistent(alternativeIdentityAssociation);
	}
	
	/**
	 * Removes an alternative identity association
	 * @param alternativeIdentityAssociationDelegate
	 */
	public void remove(final AlternativeIdentityAssociation
			alternativeIdentityAssociationDelegate){
		this.alternativeIdentityAssociationDao
			.makeTransient(alternativeIdentityAssociationDelegate);
	}
	
	/**
	 * Returns alternative identity associations for person.
	 * 
	 * @param person person
	 * @return alternative identity associations for person
	 */
	public List<AlternativeIdentityAssociation> findByPerson(
			final Person person){
		return this.alternativeIdentityAssociationDao.findByPerson(person);
	}
	
	/**
	 * Returns alternative identity associations for person active on date.
	 * 
	 * @param person person
	 * @param date date
	 * @return alternative identity associations for person active on date
	 */
	public List<AlternativeIdentityAssociation> findByPersonOnDate(
			final Person person, final Date date){
		return this.alternativeIdentityAssociationDao
				.findByPersonOnDate(person, date);
	}
}