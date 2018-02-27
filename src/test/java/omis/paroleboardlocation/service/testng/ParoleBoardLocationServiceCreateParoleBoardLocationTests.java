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
package omis.paroleboardlocation.service.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.ParoleBoardLocationService;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create parole board locations.
 *
 * @author Josh Divine
 * @version 0.0.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
@Test
public class ParoleBoardLocationServiceCreateParoleBoardLocationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("paroleBoardLocationDelegate")
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleBoardLocationService")
	private ParoleBoardLocationService paroleBoardLocationService;

	/* Test methods. */

	/**
	 * Tests the creation of parole board locations.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateParoleBoardLocation() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 Easy St.", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", "O", 
				null);
		Location location = this.locationDelegate.create(organization, null, 
				address);
		Boolean videoConferenceCapable = true;

		// Action
		ParoleBoardLocation paroleBoardLocation = this
				.paroleBoardLocationService.createParoleBoardLocation(location, 
						videoConferenceCapable);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("location", location)
			.addExpectedValue("videoConferenceCapable", videoConferenceCapable)
			.performAssertions(paroleBoardLocation);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1 Easy St.", null, 
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create("Org", "O", 
				null);
		Location location = this.locationDelegate.create(organization, null, 
				address);
		Boolean videoConferenceCapable = true;
		this.paroleBoardLocationDelegate.create(location, 
				videoConferenceCapable);

		// Action
		this.paroleBoardLocationService.createParoleBoardLocation(location,
				videoConferenceCapable);
	}

}