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
package omis.offender.service.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.service.OffenderPersonalDetailsService;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create new city for offender personal details.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 21, 2018)
 * @since OMIS 3.0
 */
@Test
public class OffenderPersonalDetailsServiceCreateCityTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("personIdentityDelegate")
	private PersonIdentityDelegate personIdentityDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;

	/* Services. */

	@Autowired
	@Qualifier("offenderPersonalDetailsService")
	private OffenderPersonalDetailsService offenderPersonalDetailsService;

	/* Test methods. */

	@Test
	public void testCreateCity() throws CityExistsException, 
		DuplicateEntityFoundException {
		// Arrangements
		final String cityName = "CityName";
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);

		// Action
		City city = this.offenderPersonalDetailsService.createCity(
				cityName, birthState, birthCountry);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("name", cityName)
			.addExpectedValue("state", birthState)
			.addExpectedValue("country", birthCountry)
			.performAssertions(city);
	}

	@Test(expectedExceptions = {CityExistsException.class})
	public void testCityExistsException() throws DuplicateEntityFoundException {
		// Arrangements
		final String cityName = "CityName";
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		this.cityDelegate.create(cityName, true, birthState, birthCountry);
		
		// Action
		this.offenderPersonalDetailsService.createCity(
				cityName, birthState, birthCountry);
	}
}