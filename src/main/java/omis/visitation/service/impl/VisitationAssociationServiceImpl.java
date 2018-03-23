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
package omis.visitation.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.contact.domain.Contact;
import omis.contact.domain.component.PoBox;
import omis.contact.service.delegate.ContactDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.SuffixDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;
import omis.residence.exception.PrimaryResidenceExistsException;
import omis.residence.exception.ResidenceStatusConflictException;
import omis.residence.exception.ResidenceTermExistsException;
import omis.residence.service.delegate.ResidenceTermDelegate;
import omis.visitation.domain.Visit;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociation;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.service.VisitationAssociationService;
import omis.visitation.service.delegate.VisitDelegate;
import omis.visitation.service.delegate.VisitationAssociationCategoryDelegate;
import omis.visitation.service.delegate.VisitationAssociationDelegate;

/**
 * Visitation association service implementation.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Aug 29, 2017)
 * @since OMIS 3.0
 */
public class VisitationAssociationServiceImpl
implements VisitationAssociationService {

	/* Service Delegates. */
	
	private RelationshipDelegate relationshipDelegate;
	
	private VisitationAssociationCategoryDelegate 
		visitationAssociationCategoryDelegate;
	
	private StateDelegate stateDelegate;
	
	private CountryDelegate countryDelegate;
	
	private CityDelegate cityDelegate; 
	
	private SuffixDelegate suffixDelegate;
	
	private ResidenceTermDelegate residenceTermDelegate;
	
	private VisitationAssociationDelegate visitationAssociationDelegate;
	
	private AddressDelegate addressDelegate;
	
	private ContactDelegate contactDelegate;
	
	private PersonDelegate personDelegate;
	
	private OffenderDelegate offenderDelegate;
	
	private VisitDelegate visitDelegate;
	
	/**
	 * Instantiates a visitation association service with the specified
	 * data access objects, service implementation delegates, instance
	 * factories, and component retrievers.
	 * 
	 * @param relationshipDelegate relationship delegate
	 * @param visitationAssociationCategoryDelegate visitation association 
	 * category delegate
	 * @param stateDelegate state delegate
	 * @param countryDelegate country delegate
	 * @param cityDelegate city delegate
	 * @param suffixDelegate suffix delegate
	 * @param residenceTermDelegate residence term delegate
	 * @param visitationAssociationDelegate visitation association delegate
	 * @param addressDelegate address delegate
	 * @param contactDelegate contact delegate
	 * @param personDelegate person delegate
	 * @param offenderDelegate offender delegate
	 */
	public VisitationAssociationServiceImpl(
			final RelationshipDelegate relationshipDelegate,
			final VisitationAssociationCategoryDelegate
					visitationAssociationCategoryDelegate,
			final StateDelegate stateDelegate,
			final CountryDelegate countryDelegate,
			final CityDelegate cityDelegate,
			final SuffixDelegate suffixDelegate,
			final ResidenceTermDelegate residenceTermDelegate,
			final VisitationAssociationDelegate visitationAssociationDelegate,
			final AddressDelegate addressDelegate,
			final ContactDelegate contactDelegate,
			final PersonDelegate personDelegate,
			final OffenderDelegate offenderDelegate,
			final VisitDelegate visitDelegate) {
		this.relationshipDelegate = relationshipDelegate;
		this.visitationAssociationCategoryDelegate = 
				visitationAssociationCategoryDelegate;
		this.stateDelegate = stateDelegate;
		this.countryDelegate = countryDelegate;
		this.cityDelegate = cityDelegate;
		this.suffixDelegate = suffixDelegate;
		this.residenceTermDelegate = residenceTermDelegate;
		this.visitationAssociationDelegate = visitationAssociationDelegate;
		this.addressDelegate = addressDelegate;
		this.contactDelegate = contactDelegate;
		this.personDelegate = personDelegate;
		this.offenderDelegate = offenderDelegate;
		this.visitDelegate = visitDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation associate(final Offender offender, 
			final Person visitor, final VisitationAssociationCategory category,
			final VisitationApproval approval, Date startDate, Date endDate,
			final VisitationAssociationFlags flags, final String notes, final String guardianship) 
					throws DuplicateEntityFoundException, DateConflictException,
					ReflexiveRelationshipException {
		Relationship relationship = this.relationshipDelegate.findOrCreate(
				offender, visitor);
		return this.visitationAssociationDelegate.create(relationship, category,
				approval, startDate, endDate, flags, notes, guardianship);
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation update(final VisitationAssociation association,
			final VisitationAssociationCategory category,
			final VisitationApproval approval,
			final Date startDate, final Date endDate, 
			final VisitationAssociationFlags flags, final String notes, final String guardianship)
		throws DuplicateEntityFoundException, DateConflictException {
		return this.visitationAssociationDelegate.update(association, category,
				approval, startDate, endDate, flags, notes, guardianship);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final VisitationAssociation association) {
		List<Visit> visits = this.visitDelegate.findVisitsByVisitationAssociation(association);
		for (Visit visit : visits) {
			this.visitDelegate.remove(visit);
		}
		this.visitationAssociationDelegate.remove(association);
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociationCategory> findCategories() {
		return this.visitationAssociationCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public Person createVisitor(final String lastName, final String firstName,
			final String middleName, final String suffix, 
			final Integer socialSecurityNumber,
			final Date birthDate, final City birthCity, final State birthState,
			final Country birthCountry, final Sex sex,
			final String stateIdNumber, final Date deathDate,
			final Boolean deceased) {
		return this.personDelegate.createWithIdentity(lastName, firstName,
				middleName, suffix, sex, birthDate, birthCountry,
				birthState, birthCity, socialSecurityNumber, stateIdNumber,
				deceased, deathDate);
	}

	/** {@inheritDoc} */
	@Override
	public Person updateVisitor(final Person visitor, final String lastName,
			final String firstName, final String middleName, 
			final String suffix, final Integer socialSecurityNumber, 
			final Date birthDate, final City birthCity, final State birthState,
			final Country birthCountry, final Sex sex,
			final String stateIdNumber, final Date deathDate,
			final Boolean deceased) 
		throws DuplicateEntityFoundException {
		return this.personDelegate.updateWithIdentity(visitor, lastName,
				firstName, middleName, suffix, sex, birthDate, birthCountry,
				birthState, birthCity, socialSecurityNumber, stateIdNumber,
				deceased, deathDate);
	}

	/** {@inheritDoc} */
	@Override
	public Address createAddress(final String houseNumber,
			final ZipCode zipCode) {
		return this.addressDelegate.findOrCreate(houseNumber, null, null, null,
				zipCode);
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceTerm createResidenceTerm(final Person person, 
			final Address address)
		throws DuplicateEntityFoundException, ResidenceTermExistsException,
		PrimaryResidenceExistsException, ResidenceStatusConflictException {
		return this.residenceTermDelegate.createResidenceTerm(person,
				new DateRange(new Date(), null), ResidenceCategory.PRIMARY,
				address, ResidenceStatus.RESIDENT, false, null, null);
	}

	/** {@inheritDoc} */
	@Override
	public Contact addContact(final Person person, 
			final Integer telephoneNumber, final PoBox mail)
		throws DuplicateEntityFoundException {
		// TODO: Adjust the interface method, and this implementation
		// to match current contact model.
		return this.contactDelegate.create(person, null, mail);
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceTerm findResidenceTerm(final Person person,
			final Date date) {
		return this.residenceTermDelegate.findByPersonAndDate(person, date,
				ResidenceCategory.PRIMARY);
	}

	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStates(Country country) {
		return this.stateDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCities(State state) {
		return this.cityDelegate.findByState(state);
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(Country country) {
		return this.cityDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> findSuffixNames() {
		List<String> suffixNames = new ArrayList<String>();
		for(Suffix suffix : this.suffixDelegate.findAll()) {
			suffixNames.add(suffix.getName());
		}
		return suffixNames;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation dissociate(
			final VisitationAssociation visitationAssociation,
			final Date endDate) {
		return this.visitationAssociationDelegate.dissociate(
				visitationAssociation, endDate);
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean isOffender(final Person person) {
		return this.offenderDelegate.isOffender(person);
	}

	/** {@inheritDoc} */
	@Override
	public Offender findAsOffender(final Person person) {
		return this.offenderDelegate.findAsOffender(person);
	}
}