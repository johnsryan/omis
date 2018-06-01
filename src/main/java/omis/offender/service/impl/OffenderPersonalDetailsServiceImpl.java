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
import omis.demographics.domain.Sex;
import omis.offender.domain.Offender;
import omis.offender.service.OffenderPersonalDetailsService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.PersonIdentity;
import omis.person.domain.PersonName;
import omis.person.domain.Suffix;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.person.service.delegate.PersonNameDelegate;
import omis.person.service.delegate.SuffixDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;

/**
 * Implementation of offender personal details service.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @version 0.1.0 (Sep 26, 2014)
 * @since OMIS 3.0
 */
public class OffenderPersonalDetailsServiceImpl 
	implements OffenderPersonalDetailsService {

	/* Delegates. */
	
	private final PersonNameDelegate personNameDelegate;
	
	private final PersonIdentityDelegate personIdentityDelegate;
	
	private final SuffixDelegate suffixDelegate;
	
	private final CountryDelegate countryDelegate;
	
	private final StateDelegate stateDelegate;
	
	private final CityDelegate cityDelegate;
	
	private final OffenderDelegate offenderDelegate;
	
	/**
	 * Instantiates implementation of service for offender personal details.
	 * 
	 * @param personNameDelegate Delegate for person names
	 * @param personIdentityDelegate Delegate for person identities
	 * @param suffixDelegate Delegate for suffixes
	 * @param countryDelegate Delegate for countries
	 * @param stateDelegate delegate for States
	 * @param cityDelegate delegate for cities
	 * @param offenderDelegate Delegate for offenders
	 */
	public OffenderPersonalDetailsServiceImpl(
			final PersonNameDelegate personNameDelegate,
			final PersonIdentityDelegate personIdentityDelegate,
			final SuffixDelegate suffixDelegate,
			final CountryDelegate countryDelegate,
			final StateDelegate stateDelegate, final CityDelegate cityDelegate,
			final OffenderDelegate offenderDelegate) {
		this.personNameDelegate = personNameDelegate;
		this.personIdentityDelegate = personIdentityDelegate;
		this.suffixDelegate = suffixDelegate;
		this.countryDelegate = countryDelegate;
		this.stateDelegate = stateDelegate;
		this.cityDelegate = cityDelegate;
		this.offenderDelegate = offenderDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public PersonName updateName(final Offender offender, final String lastName,
			final String firstName, final String middleName, 
			final String suffix) throws PersonNameExistsException {
		return this.personNameDelegate
				.update(offender.getName(), lastName, firstName,
						middleName, suffix);
	}

	/** {@inheritDoc}  */
	@Override
	public PersonIdentity changeIdentity(final Offender offender, 
			final Date birthDate, final Country birthCountry,
			final State birthState, final City birthPlace,
			final Integer socialSecurityNumber, final String stateIdNumber,
			final Sex sex, final Boolean deceased, final Date deathDate) 
					throws PersonIdentityExistsException {
		final PersonIdentity identity;
		if (offender.getIdentity() != null) {
			identity = this.personIdentityDelegate
					.update(offender.getIdentity(), sex, birthDate, birthCountry,
							birthState, birthPlace,socialSecurityNumber,
							stateIdNumber, deceased, deathDate);
		}
		else {
			identity = this.personIdentityDelegate
					.create(offender, sex, birthDate, birthCountry, birthState,
							birthPlace, socialSecurityNumber, stateIdNumber,
							deceased, deathDate);
		}
		this.offenderDelegate.updateOffender(offender, offender.getName(),
				identity);
		
		return identity;
	}

	/** {@inheritDoc} */
	@Override
	public PersonIdentity changeIdentityWithoutSsn(final Offender offender,
			final Date birthDate, final Country birthCountry,
			final State birthState, final City birthPlace,
			final String stateIdNumber, final Sex sex, final Boolean deceased,
			final Date deathDate) throws PersonIdentityExistsException {
		final PersonIdentity identity;
		if (offender.getIdentity() != null) {
			identity = this.personIdentityDelegate
					.update(offender.getIdentity(), sex, birthDate, birthCountry,
							birthState, birthPlace, null,
							stateIdNumber, deceased, deathDate);
		} else {
			identity = this.personIdentityDelegate
					.create(offender, sex, birthDate, birthCountry, birthState,
							birthPlace, null, stateIdNumber,
							deceased, deathDate);
		}
		this.offenderDelegate.updateOffender(offender, offender.getName(),
				identity);
		return identity;
	}

	/** {@inheritDoc} */
	@Override
	public List<Suffix> findSuffixes() {
		return this.suffixDelegate.findAll();
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
	public List<City> findCitiesByCountryWithoutState(
			final Country country) {
		return this.cityDelegate.findByCountryWithoutState(country);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasStates(final Country country) {
		return this.stateDelegate.countByCountry(country) > 0;
	}
}