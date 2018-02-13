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
package omis.offenderrelationship.service.impl;

import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.AddressUnitDesignator;
import omis.address.domain.StreetSuffix;
import omis.address.domain.ZipCode;
import omis.address.exception.AddressExistsException;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.AddressUnitDesignatorDelegate;
import omis.address.service.delegate.StreetSuffixDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
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
import omis.exception.DateConflictException;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.family.service.delegate.FamilyAssociationCategoryDelegate;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.offender.domain.Offender;
import omis.offenderrelationship.service.CreateRelationshipsService;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.exception.PersonExistsException;
import omis.person.service.delegate.PersonDelegate;
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
import omis.relationship.service.delegate.RelationshipNoteCategoryDesignatorDelegate;
import omis.relationship.service.delegate.RelationshipNoteDelegate;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.exception.VisitationExistsException;
import omis.visitation.service.delegate.VisitationAssociationCategoryDelegate;
import omis.visitation.service.delegate.VisitationAssociationDelegate;

/**
 * Create relationships service implementation.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @author Stephen Abson
 * @version 0.1.2 (Nov 8, 20117)
 * @since OMIS 3.0
 */
public class CreateRelationshipsServiceImpl 
		implements CreateRelationshipsService {
	
	/* Service Delegates. */
	
	private final FamilyAssociationDelegate familyAssociationDelegate;
	
	private final VictimAssociationDelegate victimAssociationDelegate;
	
	private final VisitationAssociationDelegate visitationAssociationDelegate;
	
	private final PersonDelegate personDelegate;
	
	private final AddressDelegate addressDelegate;
	
	private final CityDelegate cityDelegate;
	
	private final StateDelegate stateDelegate;
	
	private final TelephoneNumberDelegate telephoneNumberDelegate;
	
	private final OnlineAccountDelegate onlineAccountDelegate;

	private final ContactDelegate contactDelegate;
	
	private final OnlineAccountHostDelegate onlineAccountHostDelegate;
	
	private final ZipCodeDelegate zipCodeDelegate;
	
	private final StreetSuffixDelegate streetSuffixDelegate;
	
	private final CountryDelegate countryDelegate;
	
	private final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate;
	
	private final RelationshipDelegate relationshipDelegate;
	
	private final SuffixDelegate suffixDelegate;
	
	private final RelationshipNoteDelegate relationshipNoteDelegate;
	
	private final RelationshipNoteCategoryDesignatorDelegate
	relationshipNoteCategoryDesignatorDelegate;
	
	private final FamilyAssociationCategoryDelegate
	familyAssociationCategoryDelegate;
	
	private final VisitationAssociationCategoryDelegate
	visitationAssociationCategoryDelegate;

	/**
	 * Instantiates an instance of  create relationships service with the
	 * specified data access objects, delegates, and component retrievers.
	 * 
	 * @param familyAssociationCategoryDelegate delegate for family association
	 * categories
	 * @param visitationAssociationCategoryDelegate delegate for visitation
	 * association categories
	 * @param familyAssociationDelegate family association delegate
	 * @param victimAssociationDelegate victim association delegate
	 * @param visitationAssociationDelegete visitation association delegate
	 * @param personDelegate person delegate
	 * @param addressDelegate address delegate
	 * @param cityDelegate city delegate
	 * @param stateDelegate state delegate
	 * @param telephoneNumberDelegate telephone number delegate
	 * @param onlineAccountDelegate online account delegate
	 * @param contactDelegate contact delegate
	 * @param onlineAccountHostDelegate online account host delegate
	 * @param zipCodeDelegate ZIP code delegate
	 * @param suffixDelegate suffix delegate
	 * @param streetSuffixDelegate street suffix delegate
	 * @param countryDelegate country delegate
	 * @param addressUnitDesignatorDelegate address unit designator delegate
	 * @param suffixDelegate suffix delegate
	 * @param relationshipNoteDelegate delegate for relationship notes
	 * @param relationshipNoteCategoryDesignatorDelegate delegate for
	 * relationship note category designator
	 */
	public CreateRelationshipsServiceImpl(
			final FamilyAssociationCategoryDelegate
			familyAssociationCategoryDelegate,
			final VisitationAssociationCategoryDelegate
			visitationAssociationCategoryDelegate,
			final FamilyAssociationDelegate familyAssociationDelegate,
			final VictimAssociationDelegate victimAssociationDelegate,
			final VisitationAssociationDelegate visitationAssociationDelegate,
			final PersonDelegate personDelegate,
			final AddressDelegate addressDelegate,			
			final CityDelegate cityDelegate,
			final StateDelegate stateDelegate,
			final TelephoneNumberDelegate telephoneNumberDelegate,
			final OnlineAccountDelegate onlineAccountDelegate,
			final ContactDelegate contactDelegate,
			final OnlineAccountHostDelegate onlineAccountHostDelegate,
			final ZipCodeDelegate zipCodeDelegate,
			final SuffixDelegate suffixDelegate,
			final StreetSuffixDelegate streetSuffixDelegate,
			final CountryDelegate countryDelegate,
			final AddressUnitDesignatorDelegate addressUnitDesignatorDelegate,
			final RelationshipDelegate relationshipDelegate,
			final RelationshipNoteDelegate relationshipNoteDelegate,
			final RelationshipNoteCategoryDesignatorDelegate
				relationshipNoteCategoryDesignatorDelegate) {
		this.familyAssociationCategoryDelegate
			= familyAssociationCategoryDelegate;
		this.visitationAssociationCategoryDelegate 
			= visitationAssociationCategoryDelegate;
		this.familyAssociationDelegate = familyAssociationDelegate;
		this.victimAssociationDelegate = victimAssociationDelegate;
		this.visitationAssociationDelegate = visitationAssociationDelegate;
		this.personDelegate = personDelegate;
		this.addressDelegate = addressDelegate;		
		this.cityDelegate = cityDelegate;
		this.stateDelegate = stateDelegate;
		this.telephoneNumberDelegate = telephoneNumberDelegate;
		this.onlineAccountDelegate = onlineAccountDelegate;
		this.contactDelegate = contactDelegate;
		this.onlineAccountHostDelegate = onlineAccountHostDelegate;
		this.zipCodeDelegate = zipCodeDelegate;
		this.streetSuffixDelegate = streetSuffixDelegate;
		this.countryDelegate = countryDelegate;
		this.addressUnitDesignatorDelegate = addressUnitDesignatorDelegate;
		this.relationshipDelegate = relationshipDelegate;
		this.suffixDelegate = suffixDelegate;
		this.relationshipNoteDelegate = relationshipNoteDelegate;
		this.relationshipNoteCategoryDesignatorDelegate
			= relationshipNoteCategoryDesignatorDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public Person createRelation(final String lastName, final String firstName, 
			final String middleName, final String suffix, final Sex sex,
			final Date birthDate, final Country birthCountry, 
			final State birthState, final City birthCity, 
			final Integer socialSecurityNumber, final String stateId,
			final Boolean deceased, final Date deathDate) 
					throws PersonExistsException {
		return this.personDelegate.createWithIdentity(lastName, firstName, 
				middleName, suffix, sex, birthDate, birthCountry, 
				birthState, birthCity, socialSecurityNumber, stateId, 
				deceased, deathDate);
	}

	/** {@inheritDoc} */
	@Override
	public List<Suffix> findNameSuffixes() {
		return this.suffixDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String number, final ZipCode zipCode)
			throws AddressExistsException {
		return this.addressDelegate.findOrCreate(
				number, null, null, null, zipCode);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("deprecation")
	@Override
	public List<Address> findAddresses(final String addressQuery) {
		return this.addressDelegate.findAddressesByValue(addressQuery);
	}//TODO ..Get with Ryan on how to do.

	/** {@inheritDoc} */
	@Override
	public List<StreetSuffix> findStreetSuffixes() {
		return this.streetSuffixDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<AddressUnitDesignator> findAddressUnitDesignators() {
		return this.addressUnitDesignatorDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}
		
	/** {@inheritDoc} */
	@Override
	public Boolean hasStates(final Country country) {
		return this.stateDelegate.countByCountry(country) > 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<FamilyAssociationCategory> findFamilyAssociationCategories() {
		return this.familyAssociationCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociationCategory>
	findVisitationAssociationCategories() {
		return this.visitationAssociationCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStates(final Country country) {
		return this.stateDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country) {
		return this.cityDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state) {
		return this.cityDelegate.findByState(state);
	}

	/** {@inheritDoc} */
	@Override
	public List<ZipCode> findZipCodes(final City city) {
		return this.zipCodeDelegate.findByCity(city);
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(final State state, final String name, final Country country) 
			throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}

	/** {@inheritDoc} */
	@Override
	public ZipCode createZipCode(final City city, final String value, 
			final String extension) throws ZipCodeExistsException {
		return this.zipCodeDelegate.create(city, value, extension, true);
	}

	/** {@inheritDoc} */
	@Override
	public Contact changeContact(final Person relation, final PoBox poBox, 
			final Address mailingAddress)
			throws ContactExistsException {
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {			
			this.contactDelegate.update(contact, mailingAddress, 
					poBox);
		} else {
			return this.contactDelegate.create(relation, mailingAddress, 
					poBox);
		}
		return contact;
	}

	/** {@inheritDoc} */
	@Override
	public Address findMailingAddress(final Person relation) {
		Address mailingAddress;
		Contact contact = this.contactDelegate.find(relation);
		if (contact != null) {
			mailingAddress = contact.getMailingAddress();
		} else {
			mailingAddress = null;
		}
		return mailingAddress;
	}

	/** {@inheritDoc} */
	@Override
	public PoBox findPoBox(final Person relation) {	
		Contact contact = this.contactDelegate.find(relation);
		PoBox poBox;
		if (contact != null) {
			poBox = contact.getPoBox();
		} else {
			poBox = null;
		}
		return poBox;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TelephoneNumber> findTelephoneNumbers(final Person relation) {
		return this.telephoneNumberDelegate.findByContact(
				this.contactDelegate.find(relation));
	}

	/** {@inheritDoc} */
	@Override
	public List<OnlineAccount> findOnlineAccounts(final Person relation) {
		return this.onlineAccountDelegate.findByContact(
				this.contactDelegate.find(relation));
	}

	/** {@inheritDoc} */
	@Override
	public TelephoneNumber createTelephoneNumber(final Person relation, 
			final Long value, final Integer extension, final Boolean primary, 
			final Boolean active, final TelephoneNumberCategory category) 
					throws TelephoneNumberExistsException {
			return this.telephoneNumberDelegate.create(
					this.contactDelegate.find(relation), 
					value, extension, primary, active, category);
	}

	/** {@inheritDoc} */
	@Override
	public TelephoneNumber updateTelephoneNumber(
			final TelephoneNumber telephoneNumber, final Long value, 
			final Integer extension, final Boolean primary, final Boolean active,
			final TelephoneNumberCategory category) 
					throws TelephoneNumberExistsException {		
		return this.telephoneNumberDelegate.update(
				telephoneNumber, value, extension, primary, active, category);
	}

	/** {@inheritDoc} */
	@Override
	public void removeTelephoneNumber(
			final TelephoneNumber telephoneNumber) {
		this.telephoneNumberDelegate.remove(telephoneNumber);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount createOnlineAccount(final Person relation, 
			final String name, final Boolean primary, 
			final OnlineAccountHost host, final Boolean active)
			throws OnlineAccountExistsException {
		return this.onlineAccountDelegate.create(
				this.contactDelegate.find(relation), name, active, primary, host);
	}

	/** {@inheritDoc} */
	@Override
	public OnlineAccount updateOnlineAccount(final OnlineAccount onlineAccount, 
			final String name, final Boolean primary, final Boolean active,
			final OnlineAccountHost host)
			throws OnlineAccountExistsException {		
		return this.onlineAccountDelegate.update(onlineAccount, name, 
				active, primary, host);
	}

	/** {@inheritDoc} */
	@Override
	public void removeOnlineAccount(final OnlineAccount onlineAccount) {
		this.onlineAccountDelegate.remove(onlineAccount);
	}

	/** {@inheritDoc} */
	@Override
	public List<OnlineAccountHost> findOnlineAccountHosts() {
		return this.onlineAccountHostDelegate.findAll();
	}
	
	/** {@inheritDoc} 
	 * @throws ReflexiveRelationshipException */
	@Override
	public FamilyAssociation associateFamilyMember(final Offender offender, 
		final Person familyMember, final DateRange dateRange,
		final FamilyAssociationCategory category, 
		final FamilyAssociationFlags flags, final Date marriageDate, 
		final Date divorceDate)	
			throws FamilyAssociationExistsException, 
			ReflexiveRelationshipException,
			DateConflictException, FamilyAssociationConflictException {
		Relationship relationship = this.relate(offender, familyMember);	
		if(this.familyAssociationDelegate.findDateRangeOverlap(
				relationship, dateRange)	> 0){
			throw new DateConflictException(
				"Cannot create family association with date range overlap "
				+ "with one or more exisitng family assocaitions ");
		}
		return this.familyAssociationDelegate
			.create(dateRange, category, flags, marriageDate, divorceDate, 
					relationship);
	}

	/** {@inheritDoc} */
	@Override
	public VictimAssociation associateVictim(final Offender offender, 
			final Person victim, 
			final Date registerDate, final Boolean packetSent, 
			final Date packetSentDate, final VictimAssociationFlags flags) 
			throws VictimExistsException, 
			ReflexiveRelationshipException {
		Relationship relationship = this.relate(offender, victim);
		return this.victimAssociationDelegate.create(relationship, registerDate, 
				packetSent, packetSentDate, flags);
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation associateVisitor(final Offender offender, 
			final Person visitor, final VisitationAssociationCategory category, 
			final VisitationApproval approval, final Date startDate, 
			final Date endDate,	final VisitationAssociationFlags flags, 
			final String notes, final String guardianship)
					throws VisitationExistsException,
						ReflexiveRelationshipException,
						DateConflictException {
		Relationship relationship = this.relate(offender, visitor);
		return this.visitationAssociationDelegate.create(relationship, 
				category, approval, startDate, endDate, flags, notes,
				guardianship);
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}
	
	@Override
	public List<String> findSuffixNames() {
		return this.suffixDelegate.findSuffixNames();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountryWithoutState(
			final Country country) {
		return this.cityDelegate.findByCountryWithoutState(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<RelationshipNoteCategory> findDesignatedNoteCategories() {
		return this.relationshipNoteCategoryDesignatorDelegate
				.findDesignatedCategories();
	}

	/** {@inheritDoc} */
	@Override
	public RelationshipNote createNote(final Offender offender,
			final Person relation, final RelationshipNoteCategory category,
			final String value, final Date date)
					throws RelationshipNoteExistsException,
						ReflexiveRelationshipException {
		Relationship relationship = this.relationshipDelegate
				.findOrCreate(offender, relation);
		return this.relationshipNoteDelegate.create(
				relationship, category, value, date);
	}
	
	/* Helper methods. */
	
	/*
	 * Returns the relationship for 2 people, if one doesn't already exist this
	 * will create a new one.
	 * 
	 * @param firstPerson first person
	 * @param secondPerson second person
	 * @return relationship
	 */
	private Relationship relate(final Person firstPerson,
			final Person secondPerson) throws ReflexiveRelationshipException {
		Relationship relationship = this.relationshipDelegate
				.findOrCreate(firstPerson, secondPerson);
		return relationship;
	}	
}