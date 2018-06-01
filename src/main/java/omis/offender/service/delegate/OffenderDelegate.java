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
package omis.offender.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.country.domain.Country;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.dao.OffenderDao;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderExistsException;
import omis.person.domain.Person;
import omis.person.domain.PersonIdentity;
import omis.person.domain.PersonName;
import omis.region.domain.City;
import omis.region.domain.State;

/**
 * Delegate for offenders.
 *
 * @author Stephen Abson et al
 * @version 0.0.1 (Dec 31, 2015)
 * @since OMIS 3.0
 */
public class OffenderDelegate {
	
	/* Instance factories. */
	
	private final InstanceFactory<PersonName> personNameInstanceFactory;
	
	private final InstanceFactory<PersonIdentity> personIdentityInstanceFactory;
	
	private final InstanceFactory<Offender> offenderInstanceFactory;

	/* Data access objects. */
	
	private final OffenderDao offenderDao;
	
	/* Audit component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Delegate for offenders.
	 * 
	 * @param personNameInstanceFactory instance factory for person names
	 * @param personIdentityInstanceFactory instance factory for person
	 * identities
	 * @param offenderInstanceFactory instance factory for offenders
	 * @param offenderDao data access for offenders
	 * @param auditComponentRetriever audit component retriever
	 */
	public OffenderDelegate(
			final InstanceFactory<PersonName> personNameInstanceFactory,
			final InstanceFactory<PersonIdentity> personIdentityInstanceFactory,
			final InstanceFactory<Offender> offenderInstanceFactory,
			final OffenderDao offenderDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.personNameInstanceFactory = personNameInstanceFactory;
		this.personIdentityInstanceFactory = personIdentityInstanceFactory;
		this.offenderInstanceFactory = offenderInstanceFactory;
		this.offenderDao = offenderDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Methods. */
	
	/**
	 * Returns whether person is an offender.
	 * 
	 * @param person person
	 * @return whether person is an offender
	 */
	public boolean isOffender(final Person person) {
		return this.offenderDao.findAsOffender(person) != null;
	}

	/**
	 * Creates offender.
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber State ID number
	 * @param birthDate date of birth
	 * @param birthCountry country of birth
	 * @param birthState State of birth
	 * @param birthPlace place of birth
	 * @param sex sex
	 * @return new offender
	 */
	public Offender create(final String lastName, final String firstName,
			final String middleName, final String suffix,
			final Integer socialSecurityNumber,
			final String stateIdNumber,
			final Date birthDate, final Country birthCountry,
			final State birthState, final City birthPlace, final Sex sex) {
		PersonName personName = this.personNameInstanceFactory
				.createInstance();
		personName.setLastName(lastName);
		personName.setFirstName(firstName);
		personName.setMiddleName(middleName);
		personName.setSuffix(suffix);
		personName.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		personName.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		PersonIdentity personIdentity
			= this.personIdentityInstanceFactory.createInstance();
		personIdentity.setSocialSecurityNumber(socialSecurityNumber);
		personIdentity.setStateIdNumber(stateIdNumber);
		personIdentity.setBirthDate(birthDate);
		personIdentity.setBirthCountry(birthCountry);
		personIdentity.setBirthState(birthState);
		personIdentity.setBirthPlace(birthPlace);
		personIdentity.setSex(sex);
		personIdentity.setDeceased(false);
		personIdentity.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		personIdentity.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		Offender offender = this.offenderInstanceFactory
				.createInstance();
		offender.setOffenderNumber(this.offenderDao.findNextOffenderNumber());
		offender.setName(personName);
		offender.setIdentity(personIdentity);
		offender.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		offender.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		personName.setPerson(offender);
		personIdentity.setPerson(offender);
		return this.offenderDao.makePersistent(offender);
	}
	
	/**
	 * Creates offender.
	 * 
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @return new offender
	 */
	public Offender createWithoutIdentity(
			final String lastName, final String firstName,
			final String middleName, final String suffix) {
		PersonName personName = this.personNameInstanceFactory
				.createInstance();
		personName.setLastName(lastName);
		personName.setFirstName(firstName);
		personName.setMiddleName(middleName);
		personName.setSuffix(suffix);
		personName.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		personName.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		Offender offender = this.offenderInstanceFactory
				.createInstance();
		offender.setOffenderNumber(this.offenderDao.findNextOffenderNumber());
		offender.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		offender.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		offender.setName(personName);
		personName.setPerson(offender);
		return this.offenderDao.makePersistent(offender);
	}
	
	/**
	 * Updates offender.
	 * 
	 * @param offender offender
	 * @param name name
	 * @param identity identity
	 * @return updated offender
	 */
	public Offender updateOffender(final Offender offender,
			final PersonName name, final PersonIdentity identity){
		offender.setName(name);
		offender.setIdentity(identity);
		offender.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.offenderDao.makePersistent(offender);
	}
	
	/**
	 * Updates offender name.
	 * 
	 * @param offender offender
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @return updated offender
	 */
	public Offender updateName(final Offender offender,
			final String lastName, final String firstName,
			final String middleName, final String suffix) {
		offender.getName().setFirstName(firstName);
		offender.getName().setLastName(lastName);
		offender.getName().setMiddleName(middleName);
		offender.getName().setSuffix(suffix);
		offender.getName().setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.offenderDao.makePersistent(offender);
	}
	
	/**
	 * Updates offender identity.
	 * 
	 * @param offender offender
	 * @param socialSecurityNumber social security number
	 * @param stateIdNumber State ID number
	 * @param birthDate birth date
	 * @param birthCountry birth country
	 * @param birthPlace birth place
	 * @param sex sex
	 * @return updated offender
	 */
	public Offender updateIdentity(
			final Offender offender,
			final Integer socialSecurityNumber,
			final String stateIdNumber, final  Date birthDate,
			final Country birthCountry, final State birthState,
			final City birthPlace, final Sex sex,
			final Boolean deceased, final Date deathDate) {
		PersonIdentity identity;
		if (offender.getIdentity() != null) {
			identity = offender.getIdentity();
		} else {
			identity = this.personIdentityInstanceFactory
					.createInstance();
			identity.setCreationSignature(new CreationSignature(
					this.auditComponentRetriever.retrieveUserAccount(),
					this.auditComponentRetriever.retrieveDate()));
			offender.setIdentity(identity);
			identity.setPerson(offender);
		}
		identity.setSex(sex);
		identity.setBirthDate(birthDate);
		identity.setBirthState(birthState);
		identity.setBirthPlace(birthPlace);
		identity.setSocialSecurityNumber(socialSecurityNumber);
		identity.setStateIdNumber(stateIdNumber);
		identity.setDeceased(deceased);
		identity.setDeathDate(deathDate);
		identity.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		identity.setBirthCountry(birthCountry);
		return this.offenderDao.makePersistent(offender);
	}
	
	/**
	 * Checks whether an offender exists with the given offender number.
	 * 
	 * @param offenderNumber offender number of which to determine the validity
	 * @return whether an offender exists with given number
	 */
	public boolean isOffenderNumberValid(final Integer offenderNumber) {
		return this.offenderDao.isOffenderNumberValid(offenderNumber);
	}
	
	/**
	 * Returns offenders with the specified last name first name.
	 * 
	 * @param firstName first name
	 * @param lastName last name
	 * @return offenders with specified last and first name
	 */
	public List<Offender> findOffenderByName(final String firstName,
			final String lastName) {
		return this.offenderDao.findOffenderByName(firstName, lastName);
	}
	
	/**
	 * Returns the offender with the specified offender number.
	 * 
	 * @param offenderNumber offender number
	 * @return offender with specified offender number
	 */
	public Offender findByOffenderNumber(final Integer offenderNumber) {
		return this.offenderDao.findByOffenderNumber(offenderNumber);
	}
	
	/**
	 * Returns the next offender number.
	 * 
	 * @return next offender number
	 */
	public Integer findNextOffenderNumber() {
		return this.offenderDao.findNextOffenderNumber();
	}
	
	/**
	 * Returns person as offender; {@code null} if person is not an offender.
	 * 
	 * @param person person
	 * @return person as offender; {@code null} if person is not an offender
	 */
	public Offender findAsOffender(final Person person) {
		return this.offenderDao.findAsOffender(person);
	}
	
	/**
	 * Expunges offender.
	 * 
	 * @param offender offender to expunge
	 * @throws UnsupportedOperationException if expunging of offenders is not
	 * supported
	 */
	public void expunge(final Offender offender) {
		this.offenderDao.expunge(offender);
	}
	
	/**
	 * Returns any and all offender who have the specified identity and name.
	 * 
	 * @param identity person identity
	 * @param name person name
	 * @return list of matching offenders
	 */
	public List<Offender> findOffenderByIdentityAndName(
			final PersonIdentity identity, final PersonName name) {
		return this.offenderDao.findOffenderByIdentityAndName(identity, name);
	}
	
	/**
	 * Converts person to offender.
	 * 
	 * <p>Once converted, original person reference should no longer be used
	 * 
	 * @param person person - should not be used after operation
	 * @return newly created offender
	 * @throws DuplicateEntityFoundException if person is an offender
	 */
	public Offender convertPerson(final Person person)
				throws OffenderExistsException {
		if (this.isOffender(person)) {
			throw new OffenderExistsException("Person is offender");
		}
		Integer nextOffenderNumber = this.offenderDao
				.findNextOffenderNumber();
		return this.offenderDao.convertPerson(person, nextOffenderNumber);
	}
}