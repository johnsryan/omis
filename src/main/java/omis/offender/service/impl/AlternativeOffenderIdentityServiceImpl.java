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
package omis.offender.service.impl;

import java.util.Date;
import java.util.List;

import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.offender.domain.Offender;
import omis.offender.service.AlternativeOffenderIdentityService;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.PersonIdentity;
import omis.person.exception.AlternativeIdentityAssociationExistsException;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.service.delegate.AlternativeIdentityAssociationDelegate;
import omis.person.service.delegate.AlternativeIdentityCategoryDelegate;
import omis.person.service.delegate.AlternativeNameAssociationDelegate;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;

/**
 * Implementation of service for alternative offender identities.
 * 
 * @author Stephen Abson
 * @version 0.1.1 (Sept 25, 2014)
 * @since OMIS 3.0
 */
public class AlternativeOffenderIdentityServiceImpl
		implements AlternativeOffenderIdentityService {

	/* Data access objects. */
	
	private final AlternativeIdentityAssociationDelegate
		alternativeIdentityAssociationDelegate;
	
	private final PersonIdentityDelegate personIdentityDelegate;
	
	private final AlternativeIdentityCategoryDelegate 
		alternativeIdentityCategoryDelegate;
	
	private final CountryDelegate countryDelegate;
	
	private final StateDelegate stateDelegate;
	
	private final CityDelegate cityDelegate;
	
	private final AlternativeNameAssociationDelegate 
		alternativeNameAssociationDelegate;
	
	
	/**
	 * Instantiates an implementation of service for alternative offender
	 * identities.
	 * 
	 * @param alternativeIdentityAssociationDelegate Delegate for
	 * association of alternative person identities.
	 * @param personIdentityDao Delegate for person identities
	 * @param alternativeIdentityCategoryDelegate Delegate for
	 * alternative identity categories
	 * @param countryDelegate Delegate for countries
	 * @param stateDelegate delegate for States
	 * @param cityDelegate delegate for cities
	 */
	public AlternativeOffenderIdentityServiceImpl(
			final AlternativeIdentityAssociationDelegate
				alternativeIdentityAssociationDelegate,
			final PersonIdentityDelegate personIdentityDelegate,
			final AlternativeIdentityCategoryDelegate
				alternativeIdentityCategoryDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate,
			final CityDelegate cityDelegate,
			final AlternativeNameAssociationDelegate 
			alternativeNameAssociationDelegate) {
		this.alternativeIdentityAssociationDelegate
			= alternativeIdentityAssociationDelegate;
		this.personIdentityDelegate = personIdentityDelegate;
		this.alternativeIdentityCategoryDelegate = 
				alternativeIdentityCategoryDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;		
		this.alternativeNameAssociationDelegate = 
				alternativeNameAssociationDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public AlternativeIdentityAssociation associate(final Offender offender,
			final AlternativeNameAssociation alternativeNameAssociation, 
			final Date birthDate, final Country birthCountry, 
			final City birthPlace, final Integer socialSecurityNumber, 
			final String stateIdNumber, final Sex sex,
			final DateRange dateRange, 
			final AlternativeIdentityCategory category)
					throws AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException {
		PersonIdentity identity = this.personIdentityDelegate
				.create(offender, sex, birthDate, birthCountry,
						(birthPlace != null ? birthPlace.getState() : null),
						birthPlace,socialSecurityNumber, stateIdNumber, null,
						null);
		
		return this.alternativeIdentityAssociationDelegate
					.create(identity, dateRange, category, 
							alternativeNameAssociation);
		
	}

	/** {@inheritDoc} */
	@Override
	public AlternativeIdentityAssociation updateAssociation(
			final AlternativeIdentityAssociation association, 
			final AlternativeNameAssociation alternativeNameAssociation,
			final Date birthDate, final Country birthCountry, 
			final City birthPlace, final Integer socialSecurityNumber, 
			final String stateIdNumber, final Sex sex, 
			final DateRange dateRange, 
			final AlternativeIdentityCategory category)
			throws AlternativeIdentityAssociationExistsException,
				PersonIdentityExistsException {
		PersonIdentity identity = this.personIdentityDelegate
				.update(association.getIdentity(), sex, birthDate, birthCountry,
						(birthPlace != null ? birthPlace.getState() : null),
						birthPlace, socialSecurityNumber,
						stateIdNumber, null, null);
	
		return this.alternativeIdentityAssociationDelegate
				.update(association, identity, dateRange, category,
						alternativeNameAssociation);
	}

	/** {@inheritDoc} */
	@Override
	public AlternativeIdentityAssociation updateAssociationWithoutSsn(
			final AlternativeIdentityAssociation association,	
			final AlternativeNameAssociation alternativeNameAssociation,
			final Date birthDate, final Country birthCountry,
			final City birthPlace, final String stateIdNumber, 
			final Sex sex, final DateRange dateRange, 
			final AlternativeIdentityCategory category) 
					throws AlternativeIdentityAssociationExistsException,
						PersonIdentityExistsException{
		PersonIdentity identity = this.personIdentityDelegate
				.update(association.getIdentity(), sex, birthDate, birthCountry,
						(birthPlace != null ? birthPlace.getState() : null),
						birthPlace, null, stateIdNumber, null, null);
	
		return this.alternativeIdentityAssociationDelegate
				.update(association, identity, dateRange, category,
						alternativeNameAssociation);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(
			final AlternativeIdentityAssociation
				alternativeIdentityAssociation) {
		PersonIdentity identity = alternativeIdentityAssociation.getIdentity();
		this.alternativeIdentityAssociationDelegate.remove(
				alternativeIdentityAssociation);
		this.personIdentityDelegate.remove(identity);
	}

	/** {@inheritDoc} */
	@Override
	public List<PersonIdentity> findAlternativeIdentities(
			final Offender offender) {
		return this.personIdentityDelegate.findAlternativeIdentities(offender);
	}

	/** {@inheritDoc} */
	@Override
	public List<AlternativeIdentityAssociation> findAssociations(
			final Offender offender) {
		return this.alternativeIdentityAssociationDelegate.findByPerson(offender);
	}

	/** {@inheritDoc} */
	@Override
	public List<AlternativeIdentityCategory> findCategories() {
		return this.alternativeIdentityCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Country> findCountries() {
		return this.countryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<State> findStatesByCountry(final Country country) {
		return this.stateDelegate.findByCountry(country);
	}

	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByState(final State state) {
		return this.cityDelegate.findByState(state);
	}
	
	/** {@inheritDoc} */
	@Override
	public City createCity(
			final String name, final State state, final Country country)
				throws CityExistsException {
		return this.cityDelegate.create(name, true, state, country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountry(final Country country) {
		return this.cityDelegate.findByCountry(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<City> findCitiesByCountryWithoutState(final Country country) {
		return this.cityDelegate.findByCountryWithoutState(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<AlternativeNameAssociation> findAlternativeNames(
			Offender offender) {
		return this.alternativeNameAssociationDelegate.findByPerson(offender);
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasStates(final Country country) {
		return this.stateDelegate.countByCountry(country) > 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public State findHomeState() {
		return this.stateDelegate.findHomeState();
	}	
}