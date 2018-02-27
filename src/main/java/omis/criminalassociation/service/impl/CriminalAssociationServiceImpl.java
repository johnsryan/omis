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
package omis.criminalassociation.service.impl;


import java.util.List;

import omis.address.dao.AddressDao;
import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.contact.dao.ContactDao;
import omis.contact.dao.TelephoneNumberDao;
import omis.contact.domain.Contact;
import omis.contact.domain.TelephoneNumber;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.criminalassociation.dao.CriminalAssociationDao;
import omis.criminalassociation.domain.CriminalAssociation;
import omis.criminalassociation.domain.CriminalAssociationCategory;
import omis.criminalassociation.domain.component.CriminalAssociationFlags;
import omis.criminalassociation.exception.CriminalAssociationExistsException;
import omis.criminalassociation.service.CriminalAssociationService;
import omis.criminalassociation.service.delegate.CriminalAssociationCategoryDelegate;
import omis.criminalassociation.service.delegate.CriminalAssociationServiceDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.person.dao.PersonDao;
import omis.person.domain.Person;
import omis.person.domain.PersonName;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.service.delegate.ResidenceTermDelegate;

/**
 * Implementation of services for criminal association.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (Apr 14, 2015)
 * @since OMIS 3.0
 */
public class CriminalAssociationServiceImpl 
	implements CriminalAssociationService {

	/* Data Access Objects */
	
	private final CriminalAssociationDao criminalAssociationDao;	
	
	private final PersonDao personDao;
	
	private final AddressDao addressDao;
	
	private final ContactDao contactDao;
	
	private final TelephoneNumberDao telephoneNumberDao;
	
	private final CriminalAssociationServiceDelegate 
		criminalAssociationServiceDelegate;
	private final CriminalAssociationCategoryDelegate 
		criminalAssociationCategoryDelegate;
	
	private final ResidenceTermDelegate residenceTermDelegate;
	
	/* Instance Factories */
	
	private final InstanceFactory<Person> personInstanceFactory;
	
	private final InstanceFactory<PersonName> personNameInstanceFactory;
	
	private final InstanceFactory<Address> addressInstanceFactory;
	
	private final InstanceFactory<Contact> contactInstanceFactory;
	
	private final InstanceFactory<TelephoneNumber> 
		telephoneNumberInstanceFactory;
	
	/* Audit Component Retriever */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Instantiates a criminal association service implementation with the 
	 * specified data access objects, instance factories, and audit component 
	 * retriever.
	 * 
	 * @param criminalAssociationDao criminal association DAO
	 * @param criminalAssociationCategoryDelegate 
	 * criminal association category Delegate
	 * @param personDao person DAO
	 * @param addressDao address DAO
	 * @param criminalAssociationServiceDelegate criminal association service 
	 * @param contactDao contact DAO
	 * @param telephoneNumberDao telephone number DAO
	 * @param residenceTermDelegate residence term delegate
	 * @param personInstanceFactory person instance factory
	 * @param personNameInstanceFactory person name instance factory
	 * @param addressInstanceFactory address instance factory
	 * @param contactInstanceFactory contact instance factory
	 * @param telephoneNumberInstanceFactory telephone number instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public CriminalAssociationServiceImpl(final CriminalAssociationDao 
			criminalAssociationDao,	 
		final CriminalAssociationCategoryDelegate 
			criminalAssociationCategoryDelegate,
		final AddressDao addressDao, final PersonDao personDao,
		final ContactDao contactDao, 
		final TelephoneNumberDao telephoneNumberDao,
		final CriminalAssociationServiceDelegate 
			criminalAssociationServiceDelegate,
		final ResidenceTermDelegate residenceTermDelegate,
		final InstanceFactory<Person> personInstanceFactory,
		final InstanceFactory<PersonName> personNameInstanceFactory,
		final InstanceFactory<Address> addressInstanceFactory,
		final InstanceFactory<Contact> contactInstanceFactory,
		final InstanceFactory<TelephoneNumber> telephoneNumberInstanceFactory,
		final AuditComponentRetriever auditComponentRetriever) {
		this.criminalAssociationDao = criminalAssociationDao;
		this.criminalAssociationServiceDelegate 
			= criminalAssociationServiceDelegate;
		this.addressDao = addressDao;
		this.personDao = personDao;
		this.contactDao = contactDao;
		this.telephoneNumberDao = telephoneNumberDao;
		this.criminalAssociationCategoryDelegate 
			= criminalAssociationCategoryDelegate;
		this.residenceTermDelegate = residenceTermDelegate;
		this.personInstanceFactory = personInstanceFactory;
		this.personNameInstanceFactory = personNameInstanceFactory;
		this.addressInstanceFactory = addressInstanceFactory;
		this.contactInstanceFactory = contactInstanceFactory;
		this.telephoneNumberInstanceFactory = telephoneNumberInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**{@inheritDoc} */
	@Override
	public CriminalAssociation associate(final Offender offender, 
			final Person associate, final DateRange dateRange, 
			final CriminalAssociationCategory category, 
		final CriminalAssociationFlags criminalAssociationFlags) 
			throws ReflexiveRelationshipException, 
			CriminalAssociationExistsException {
		CriminalAssociation criminalAssociation 
			= this.criminalAssociationServiceDelegate
			.associateCriminally(offender, associate, dateRange, category,
			criminalAssociationFlags);
		return criminalAssociation;
	}

	/** {@inheritDoc} */
	@Override
	public CriminalAssociation update(
			final CriminalAssociation criminalAssociation, 
			final DateRange dateRange, 
			final CriminalAssociationCategory criminalAssociationCategory, 
			final CriminalAssociationFlags criminalAssociationFlags) {
		return this.criminalAssociationServiceDelegate.update(
			criminalAssociation, dateRange, criminalAssociationCategory, 
			criminalAssociationFlags);
	}
	
	/**{@inheritDoc}*/
	@Override
	public void remove(final CriminalAssociation criminalAssociation) {
		this.criminalAssociationServiceDelegate.remove(criminalAssociation);
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<CriminalAssociationCategory> findCategories() {
		return this.criminalAssociationCategoryDelegate
			.findCriminalAssociationCategories();
	}
	
	/**{@inheritDoc}*/
	@Override
	public Person createCriminalAssociate(final String lastName, 
			final String firstName, final String middleName, 
			final String suffix) {
		Person person = this.personInstanceFactory.createInstance();
		PersonName personName = this.personNameInstanceFactory.createInstance();
		CreationSignature creationSignature = new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate());
		UpdateSignature updateSignature = new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate());
		person.setName(personName);
		personName.setPerson(person);
		personName.setFirstName(firstName);
		personName.setLastName(lastName);
		personName.setMiddleName(middleName);
		personName.setSuffix(suffix);
		personName.setUpdateSignature(updateSignature);
		personName.setCreationSignature(creationSignature);
		person.setCreationSignature(creationSignature);
		person.setUpdateSignature(updateSignature);
		return this.personDao.makePersistent(person);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Person updateCriminalAssociate(final Person criminalAssociate, 
			final String lastName, final String firstName, 
			final String middleName, final String suffix) {
		criminalAssociate.getName().setFirstName(firstName);
		criminalAssociate.getName().setLastName(lastName);
		criminalAssociate.getName().setMiddleName(middleName);
		return criminalAssociate;
	}
	
	/**{@inheritDoc}*/
	@Override
	public Address createAddress(final String value, final ZipCode zipCode) {
		Address address = this.addressInstanceFactory.createInstance();
		address.setZipCode(zipCode);
		address.setValue(value);
		this.addressDao.makePersistent(address);
		return address;
	}
	
	/**{@inheritDoc} */
	@Override
	public ResidenceTerm createResidenceTerm(final Person person, 
			final Address address) 
					throws PrimaryResidenceExistsException, 
					ResidenceStatusConflictException, 
					DuplicateEntityFoundException,
					ResidenceTermExistsException {
		ResidenceTerm residenceTerm = this.residenceTermDelegate
				.createResidenceTerm(person, null, ResidenceCategory.PRIMARY, 
				address, ResidenceStatus.FOSTER_CARE, null, null, null);
		return residenceTerm;
		 
	}
	
	/**{@inheritDoc} */
	@Override
	public Contact addTelephoneNumber(final Person person, 
			final Long telephoneNumber) 
					throws TelephoneNumberExistsException {
		Contact contact;
		if (this.contactDao.find(person) == null) {
			contact = this.contactInstanceFactory.createInstance();
			contact.setPerson(person);
			this.contactDao.makePersistent(contact);
		} else {
			contact = this.contactDao.find(person);
			if (this.telephoneNumberDao
					.find(contact, telephoneNumber) != null) {
				throw new TelephoneNumberExistsException(
						"Telephone number exists");
			}
		}
				
		TelephoneNumber telephoneNumberVar 
			= this.telephoneNumberInstanceFactory.createInstance();
		telephoneNumberVar.setContact(contact);
		telephoneNumberVar.setValue(telephoneNumber);
		telephoneNumberVar.setPrimary(true);
		telephoneNumberVar.setActive(true);
		this.telephoneNumberDao.makePersistent(telephoneNumberVar);
		return contact;
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<CriminalAssociation> findByOffender(final Offender offender) {
		return this.criminalAssociationDao.findByOffender(offender);
	}
	
	/**{@inheritDoc}*/	
	@Override
	public List<CriminalAssociation> findByOtherOffender(
			final Offender offender) {
		return this.criminalAssociationDao.findByOtherOffender(offender);
	}
}