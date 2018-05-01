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
package omis.family.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.VerificationSignature;
import omis.contact.domain.Contact;
import omis.contact.domain.OnlineAccount;
import omis.contact.domain.OnlineAccountHost;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.domain.component.PoBox;
import omis.contact.exception.ContactExistsException;
import omis.contact.exception.OnlineAccountExistsException;
import omis.contact.exception.TelephoneNumberExistsException;
import omis.contact.service.delegate.ContactDelegate;
import omis.contact.service.delegate.OnlineAccountDelegate;
import omis.contact.service.delegate.OnlineAccountHostDelegate;
import omis.contact.service.delegate.TelephoneNumberDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationNote;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.family.service.FamilyAssociationService;
import omis.family.service.delegate.FamilyAssociationCategoryDelegate;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.family.service.delegate.FamilyAssociationNoteCategoryDesignatorDelegate;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.person.service.delegate.SuffixDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.relationship.service.delegate.RelationshipNoteDelegate;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.service.delegate.ResidenceTermDelegate;

/**
 * Implementation of services for family association.
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (June 2, 2013)
 * @since OMIS 3.0
 */
public class FamilyAssociationServiceImpl implements FamilyAssociationService {
	private final FamilyAssociationCategoryDelegate 
		familyAssociationCategoryDelegate;
	private final FamilyAssociationDelegate familyAssociationDelegate;
	private final AddressDelegate addressDelegate;
	private final ResidenceTermDelegate residenceTermDelegate;
	private final ContactDelegate contactDelegate;
	private final TelephoneNumberDelegate telephoneNumberDelegate;
	private final OnlineAccountDelegate onlineAccountDelegate;
	private final RelationshipDelegate relationshipDelegate;
	private final OnlineAccountHostDelegate onlineAccountHostDelegate;
	private final PersonDelegate personDelegate;
	private final CountryDelegate countryDelegate;
	private final ZipCodeDelegate zipCodeDelegate;
	private final StateDelegate stateDelegate;
	private final CityDelegate cityDelegate;
	private final SuffixDelegate suffixDelegate;
	private final RelationshipNoteDelegate relationshipNoteDelegate;
	private final FamilyAssociationNoteCategoryDesignatorDelegate
			familyAssociationNoteCategoryDesignatorDelegate;
	
	/**
	 * Instantiates an instance of family association service with the
	 * specified data access objects and component retrievers.
	 * 
	 * @param familyAssociationCategoryDelegate 
	 * family association category delegate
	 * @param auditComponentRetriever audit component retriever
	 * @param relationshipInstanceFactory relationship instance factory
	 * @param familyAssociationNoteInstanceFactory family association note 
	 * 	instance factory
	 * @param familyAssociationDelegate family association delegate
	 * @param addressDelegate address delegate
	 * @param residenceTermDelegate residence term delegate
	 * @param contactDelegate contact delegate
	 * @param telephoneNumberDelegate telephone number delegate
	 * @param onlineAccountDelegate online account delegate
	 * @param onlineAccountHostDelegate online account delegate 
	 * @param relationshipDelegate relationship delegate
	 * @param personDelegate person delegate
	 * @param countryDelegate country delegate
	 * @param zipCodeDelegate zip code delegate
	 * @param stateDelegate state delegate
	 * @param cityDelegate city delegate
	 * @param personIdentityDelegate person identity delegate 
	 * @param suffixDelegate suffix delegate
	 * @param relationshipNoteDelegate relationship note delegate
	 * @param familyAssociationNoteCategoryDesignatorDelegate family association
	 * @param familyAssociationDelegate familyAssociationDelegate
	 * note category designator delegate
	 */
	public FamilyAssociationServiceImpl(
		final FamilyAssociationCategoryDelegate 
		familyAssociationCategoryDelegate,
		final AuditComponentRetriever auditComponentRetriever,
		final InstanceFactory<Relationship> relationshipInstanceFactory,
		final InstanceFactory<FamilyAssociationNote> 
		familyAssociationNoteInstanceFactory,
		final FamilyAssociationDelegate familyAssociationDelegate,
		final AddressDelegate addressDelegate,
		final ResidenceTermDelegate residenceTermDelegate,
		final ContactDelegate contactDelegate,
		final TelephoneNumberDelegate telephoneNumberDelegate,
		final OnlineAccountDelegate onlineAccountDelegate,
		final RelationshipDelegate relationshipDelegate,
		final OnlineAccountHostDelegate onlineAccountHostDelegate,
		final PersonDelegate personDelegate,
		final CountryDelegate countryDelegate,
		final ZipCodeDelegate zipCodeDelegate,
		final StateDelegate stateDelegate,
		final CityDelegate cityDelegate,
		final PersonIdentityDelegate personIdentityDelegate,
		final SuffixDelegate suffixDelegate,
		final RelationshipNoteDelegate relationshipNoteDelegate,
		final FamilyAssociationNoteCategoryDesignatorDelegate
		familyAssociationNoteCategoryDesignatorDelegate) {
		this.familyAssociationCategoryDelegate 
			= familyAssociationCategoryDelegate;
		this.familyAssociationDelegate = familyAssociationDelegate;
		this.addressDelegate = addressDelegate;
		this.residenceTermDelegate = residenceTermDelegate;
		this.contactDelegate = contactDelegate;
		this.telephoneNumberDelegate = telephoneNumberDelegate;
		this.onlineAccountDelegate = onlineAccountDelegate;
		this.relationshipDelegate = relationshipDelegate;
		this.onlineAccountHostDelegate = onlineAccountHostDelegate;
		this.personDelegate = personDelegate;
		this.countryDelegate = countryDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
		this.cityDelegate = cityDelegate;
		this.stateDelegate = stateDelegate;
		this.suffixDelegate = suffixDelegate;
		this.relationshipNoteDelegate = relationshipNoteDelegate;
		this.familyAssociationNoteCategoryDesignatorDelegate
			= familyAssociationNoteCategoryDesignatorDelegate;
	}
	
	/**{@inheritDoc} */
	@Override
	public FamilyAssociation associate(final Offender offender, 
		final Person familyMember, 
		final DateRange dateRange, final FamilyAssociationCategory category,
		final FamilyAssociationFlags flags, 
		final Date marriageDate, final Date divorceDate)
		throws FamilyAssociationExistsException, ReflexiveRelationshipException,
		FamilyAssociationConflictException {
		if (offender.equals(familyMember)) {
			throw new ReflexiveRelationshipException("Second person "
					+ "cannot equal first person in realtionship");
		}
		Relationship relationship = relationshipDelegate.findOrCreate(
			offender, familyMember);
		return this.familyAssociationDelegate.create(dateRange, category, 
				flags, marriageDate, divorceDate, relationship);
	}
	
	/**{@inheritDoc} */
	@Override
	public FamilyAssociation update(final FamilyAssociation familyAssociation, 
		final DateRange dateRange, final FamilyAssociationCategory category, 
		final FamilyAssociationFlags flags, 
		final Date marriageDate, final Date divorceDate) 
			throws FamilyAssociationExistsException, 
			FamilyAssociationConflictException {
		return this.familyAssociationDelegate.update(familyAssociation, 
				dateRange, category, flags, marriageDate, divorceDate);
	}
	
	/**{@inheritDoc}*/
	@Override
	public void remove(final FamilyAssociation familyAssociation) {
		this.familyAssociationDelegate.remove(familyAssociation);
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<FamilyAssociationCategory> findCategories() {
		return this.familyAssociationCategoryDelegate.findAll();
	}
	
	/**{@inheritDoc}*/
	@Override  
	public Person createFamilyMember(final String lastName, 
		final String firstName, final String middleName, final String suffix, 
		final Sex sex, final Date birthDate, final Country birthCountry, 
		final State birthState, final City birthCity, 
		final Integer socialSecurityNumber,
		final String stateId, final Boolean deceased, final Date deathDate) {
		Person person = this.personDelegate.createWithIdentity(lastName, 
			firstName, middleName, suffix, sex, birthDate, birthCountry, 
			birthState, birthCity, socialSecurityNumber, stateId, deceased, 
			deathDate);
		return person;
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<Suffix> findNameSuffixes() {
		return this.suffixDelegate.findAll();
	}
	
	/**{@inheritDoc}*/
	@Override 
	public Address createAddress(final String value, final String designator,
		final BuildingCategory buildingCategory, final ZipCode zipCode)
		throws AddressExistsException {
		return this.addressDelegate.findOrCreate(value, designator, null, 
			buildingCategory, zipCode);
	}
	
	/**{@inheritDoc}*/
	@Override
	public ResidenceTerm createResidenceTerm(final Person person, 
		final Address address, 
		final VerificationSignature verificationSignature) 
			throws ResidenceTermExistsException, 
			PrimaryResidenceExistsException, ResidenceStatusConflictException {
		return this.residenceTermDelegate
			.createResidenceTerm(person, null, ResidenceCategory.PRIMARY, 
			address, ResidenceStatus.RESIDENT, true, null, null);
	}
	
	/**{@inheritDoc}*/
	@Override	
	public Contact addContact(final Person person, final Address mailingAddress,
		final PoBox poBox) throws ContactExistsException {
		return this.contactDelegate.create(person, mailingAddress, poBox);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Contact findContactByPerson(final Person person) {
		return this.contactDelegate.find(person);
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<TelephoneNumber> findTelephoneNumbersByContact(
			final Contact contact) {
		return this.telephoneNumberDelegate.findByContact(contact);
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<OnlineAccount> findOnlineAccountsByContact(
			final Contact contact) {
		return this.onlineAccountDelegate.findByContact(contact);
	};
	
	/**{@inheritDoc}*/
	@Override
	public Contact updateContact(final Contact contact, 
			final Address mailingAddress, final PoBox poBox) 
		throws ContactExistsException {
		return this.contactDelegate.update(contact, mailingAddress, poBox);
	}
	
	/**{@inheritDoc}*/
	@Override
	public TelephoneNumber addTelephoneNumber(final Contact contact, 
		final Long value, final Integer extension, final Boolean primary, 
		final Boolean active, final TelephoneNumberCategory category)
			throws TelephoneNumberExistsException {
		return this.telephoneNumberDelegate.create(contact, value, extension, 
			primary, active, category);
	}
	
	/**{@inheritDoc}*/
	@Override
	public TelephoneNumber updateTelephoneNumber(
		final TelephoneNumber telephoneNumber, final Long value, 
		final Integer extension, final Boolean primary, final Boolean active,
		final TelephoneNumberCategory category) 
		throws TelephoneNumberExistsException {
		return this.telephoneNumberDelegate.update(telephoneNumber, value, 
			extension, primary, active, category);
	}
	
	/**{@inheritDoc}*/
	@Override
	public void removeTelephoneNumber(final TelephoneNumber telephoneNumber) {
		this.telephoneNumberDelegate.remove(telephoneNumber);
	}
	
	/**{@inheritDoc}*/
	@Override
	public OnlineAccount addOnlineAccount(final Contact contact, 
		final String name, final Boolean primary, final OnlineAccountHost host,
		final Boolean active) 
		throws OnlineAccountExistsException {
		if (active == null) {
			return this.onlineAccountDelegate.create(contact, name, false, 
				primary, host);
		} else {
			return this.onlineAccountDelegate.create(contact, name, active, 
					primary, host);
		}
	}
	
	/**{@inheritDoc}*/
	@Override
	public OnlineAccount updateOnlineAccount(final OnlineAccount onlineAccount, 
		final String name, final Boolean primary, final OnlineAccountHost host,
		final Boolean active)	
		throws OnlineAccountExistsException {
		return this.onlineAccountDelegate.update(onlineAccount, name, active, 
			primary, host);
	}
	
	/**{@inheritDoc}*/
	@Override
	public void removeOnlineAccount(final OnlineAccount onlineAccount) {
		this.onlineAccountDelegate.remove(onlineAccount);		
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<OnlineAccountHost> findOnlineAccountHosts() {
		return this.onlineAccountHostDelegate.findAll();
	}
	
	/**{@inheritDoc}*/
	@Override
	public List<RelationshipNote> findNotesByRelationship(
		final Relationship relationship) {
		return this.familyAssociationNoteCategoryDesignatorDelegate
				.findDesignatedNotesByRelationship(relationship);
	}
	
	/**{@inheritDoc}*/
	@Override
	public Contact findContact(final Person person) {
		return this.contactDelegate.find(person);
	}
	
	/**{@inheritDoc}*/
	@Override
	public ResidenceTerm findResidenceTerm(final Person person, 
		final Date effectiveDate) {
		return this.residenceTermDelegate.findResidenceTerm(person, 
			effectiveDate, ResidenceCategory.PRIMARY, ResidenceStatus.RESIDENT);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodesByCity(final City city) {
		return this.zipCodeDelegate.findByCity(city);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state) {
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final String value, final String extension, 
		final City city) throws ZipCodeExistsException {
		return this.zipCodeDelegate.create(city, value, extension, true);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country) {
		return this.stateDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country) {
		return this.cityDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean hasStates(final Country country) {
		return this.stateDelegate.countByCountry(country) > 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(final String name, final State state, 
		final Country country) 
		throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("deprecation")
	@Override
	public List<Address> findAddresses(final String addressQuery) {
		return this.addressDelegate.findAddressesByValue(addressQuery);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<String> findSuffixNames() {
		return this.suffixDelegate.findSuffixNames();
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountryWithoutState(
			final Country country) {
		return this.cityDelegate.findByCountryWithoutState(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public RelationshipNote addRelationshipNote(
		final FamilyAssociation familyAssociation,
		final RelationshipNoteCategory category, final Date date,
		final String value)	throws RelationshipNoteExistsException {
		return this.relationshipNoteDelegate.create(
			familyAssociation.getRelationship(), category, value, date);
	};
		
	/** {@inheritDoc} */
	@Override
	public RelationshipNote updateRelationshipNote(
		final RelationshipNote relationshipNote,
		final RelationshipNoteCategory category, final Date date,
		final String value)	throws RelationshipNoteExistsException {
		return this.relationshipNoteDelegate.update(relationshipNote, category,
			value, date);
	};
		
	/** {@inheritDoc} */
	@Override
	public void removeRelationshipNote(
		final RelationshipNote relationshipNote) {
		this.relationshipNoteDelegate.remove(relationshipNote);
	};
		
	/** {@inheritDoc} */
	@Override
	public List<RelationshipNoteCategory>
		findDesignatedRelationshipNoteCategories() {
		return this.familyAssociationNoteCategoryDesignatorDelegate
			.findDesignatedRelationshipNoteCategories();
	};
		
	/** {@inheritDoc} */
	@Override
	public List<RelationshipNote> findRelationshipNotes(final FamilyAssociation
		familyAssociation) {
		return this.familyAssociationNoteCategoryDesignatorDelegate
			.findDesignatedNotesByRelationship(
					familyAssociation.getRelationship());
	}
}