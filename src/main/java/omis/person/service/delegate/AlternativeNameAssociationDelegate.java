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
import omis.person.dao.AlternativeNameAssociationDao;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.Person;
import omis.person.domain.PersonName;
import omis.person.exception.AlternativeNameAssociationExistsException;

/**
 * Delegate for alternative name associations.
 * 
 * @author Annie Jacques
 * @author Stephen Abson
 * @version 0.1.0 (Nov 9, 2016)
 * @since OMIS 3.0
 */
public class AlternativeNameAssociationDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Alternative Name Association Already Exists With Given Person Name, "
			+ "Category, and Date Range";
	
	private final AlternativeNameAssociationDao alternativeNameAssociationDao;
	
	private final InstanceFactory<AlternativeNameAssociation> 
		alternativeNameAssociationInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for AlternativeNameAssociationDelegate
	 * @param alternativeNameAssociationDao
	 * @param alternativeNameAssociationInstanceFactory
	 * @param auditComponentRetriever
	 */
	public AlternativeNameAssociationDelegate(
			final AlternativeNameAssociationDao alternativeNameAssociationDao,
			final InstanceFactory<AlternativeNameAssociation> 
				alternativeNameAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.alternativeNameAssociationDao = alternativeNameAssociationDao;
		this.alternativeNameAssociationInstanceFactory = 
				alternativeNameAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	
	/**
	 * Creates an AlternativeNameAssociation with the given parameters
	 * @param name - person name
	 * @param dateRange - date range
	 * @param category - alternative name category
	 * @return AlternativeNameAssociation with the given parameters
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists
	 */
	public AlternativeNameAssociation create(final PersonName name, 
			final DateRange dateRange, final AlternativeNameCategory category)
			throws AlternativeNameAssociationExistsException {
		if(this.alternativeNameAssociationDao.find(name, category, 
				name.getPerson(), dateRange)){
			throw new AlternativeNameAssociationExistsException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		AlternativeNameAssociation alternativeNameAssociation = 
				this.alternativeNameAssociationInstanceFactory.createInstance();
		
		alternativeNameAssociation.setCategory(category);
		alternativeNameAssociation.setDateRange(dateRange);
		alternativeNameAssociation.setName(name);
		alternativeNameAssociation.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		alternativeNameAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.alternativeNameAssociationDao
				.makePersistent(alternativeNameAssociation);
	}
	
	/**
	 * Updates an AlternativeNameAssociation with the given parameters
	 * @param alternativeNameAssociation - alternative name association to 
	 * update
	 * @param name - person name
	 * @param dateRange - date range
	 * @param category - alternative name category
	 * @return Updated AlternativeNameAssociation with the given parameters
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists
	 */
	public AlternativeNameAssociation update(
			final AlternativeNameAssociation alternativeNameAssociation, 
			final PersonName name, final DateRange dateRange, 
			final AlternativeNameCategory category)
			throws AlternativeNameAssociationExistsException {
		if(this.alternativeNameAssociationDao.findExcluding(
				alternativeNameAssociation, name, category, name.getPerson(),
				dateRange)){
			throw new AlternativeNameAssociationExistsException(
					DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		alternativeNameAssociation.setCategory(category);
		alternativeNameAssociation.setDateRange(dateRange);
		alternativeNameAssociation.setName(name);
		alternativeNameAssociation.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.alternativeNameAssociationDao
				.makePersistent(alternativeNameAssociation);
	}
	
	/**
	 * Removes specified alternativeNameAssocation
	 * @param alternativeNameAssociation
	 */
	public void remove(final AlternativeNameAssociation 
			alternativeNameAssociation){
		this.alternativeNameAssociationDao
			.makeTransient(alternativeNameAssociation);
	}
	
	
	/**
	 * Returns alternative name association for person.
	 * 
	 * @param person person
	 * @return alternative name association for person
	 */
	public List<AlternativeNameAssociation> findByPerson(
			final Person person) {
		return this.alternativeNameAssociationDao.findByPerson(person);
	}
	
	
	/**
	 * Returns alternative name association for person.
	 * 
	 * @param person person
	 * @return alternative name association for person
	 */
	public List<AlternativeNameAssociation> findByPersonOnDate(
			final Person person, final Date date) {
		return this.alternativeNameAssociationDao
				.findByPersonOnDate(person, date);
	}
	
}
