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
package omis.locationterm.service.testng;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.service.LocationTermService;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.exception.StateExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.AllowedSupervisoryOrganizationRuleExistsException;
import omis.supervision.exception.CorrectionalStatusExistsException;
import omis.supervision.exception.SupervisoryOrganizationExistsException;
import omis.supervision.service.delegate.AllowedSupervisoryOrganizationRuleDelegate;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Unit tests for look up of locations allowed for placement in State.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 17, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceFindLocationsAllowedForPlacementInStateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("allowedSupervisoryOrganizationRuleDelegate")
	private AllowedSupervisoryOrganizationRuleDelegate
	allowedSupervisoryOrganizationRuleDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Test methods. */
	
	/**
	 * Tests that allowed locations are returned by State.
	 * 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 */
	public void testFind()
			throws SupervisoryOrganizationExistsException,
			CorrectionalStatusExistsException,
			AllowedSupervisoryOrganizationRuleExistsException,
			CountryExistsException,
			StateExistsException,
			CityExistsException,
			ZipCodeExistsException,
			LocationExistsException {
		
		// Arrangements - creates allowed prison for status in State
		SupervisoryOrganization prison = this.createPrison();
		CorrectionalStatus secure = this.createSecure();
		this.allowedSupervisoryOrganizationRuleDelegate.create(secure, prison);
		Country country = this.createCountry();
		ZipCode zipCode = this.createMontanaZipCode(country);
		Address prisonAddress = this.createAddress(
				"3000 3000TH ST #3000", zipCode);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, prisonAddress);
		
		// Action - look up allowed locations for State
		State state = zipCode.getCity().getState();
		List<Location> allowedLocations = this.locationTermService
				.findLocationsAllowedForPlacementInState(state);
		
		// Asserts that prison is allowed for State
		assert !allowedLocations.isEmpty()
			: "No locations are allowed";
		assert allowedLocations.get(0).equals(prisonLocation)
			: "Wrong location allowed";
	}
	
	/**
	 * Tests that out of State locations are not returned for allowed
	 * organization.
	 * 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 */
	public void findWithOutOfStateExcluded()
			throws SupervisoryOrganizationExistsException,
				CorrectionalStatusExistsException,
				AllowedSupervisoryOrganizationRuleExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException {
		
		// Arrangements - creates allowed prison for status in State
		SupervisoryOrganization prison = this.createPrison();
		CorrectionalStatus secure = this.createSecure();
		this.allowedSupervisoryOrganizationRuleDelegate.create(secure, prison);
		Country country = this.createCountry();
		ZipCode zipCode = this.createMontanaZipCode(country);
		Address prisonAddress = this.createAddress(
				"3000 3000TH ST #3000", zipCode);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, prisonAddress);
		ZipCode idahoZipCode = this.createIdahoZipCode(country);
		Address idahoPrisonAddress = this.createAddress(
				"1000 1000TH ST #1000", idahoZipCode);
		this.locationDelegate.create(prison, null, idahoPrisonAddress);
		
		// Action - look up allowed locations for State
		State state = zipCode.getCity().getState();
		List<Location> allowedLocations = this.locationTermService
				.findLocationsAllowedForPlacementInState(state);
		
		// Asserts that prison is allowed for State
		int allowedLocationCount = allowedLocations.size();
		assert allowedLocationCount == 1 : String.format(
					"Wrong number of locations allowed: 1 expected; %d found",
					allowedLocationCount);
		assert allowedLocations.get(0).equals(prisonLocation)
			: "Wrong location allowed";
	}
	
	/**
	 * Tests that no locations are allowed when there are no rules. 
	 * 
	 * @throws StateExistsException if State exists 
	 * @throws CountryExistsException if country exists
	 */
	public void findEmpty()
			throws StateExistsException,
				CountryExistsException {
		
		// Arrangements - creates State
		Country country = this.createCountry();
		State state = this.stateDelegate.create(
				"Montana", "MT", country, true, true);
		
		// Action and assertion
		assert this.locationTermService
				.findLocationsAllowedForPlacementInState(state).isEmpty()
			: "Locations allowed";
	}
	
	/* Helper methods. */
	
	// Returns prison
	private SupervisoryOrganization createPrison()
			throws SupervisoryOrganizationExistsException {
		return this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
	}
	
	// Returns secure status
	private CorrectionalStatus createSecure()
			throws CorrectionalStatusExistsException {
		return this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
	}
	
	// Returns country
	private Country createCountry()
			throws CountryExistsException {
		return this.countryDelegate
				.create("United States", "USA", true);
	}
	
	// Returns Montana ZIP code - also creates Montana and a Montana city
	private ZipCode createMontanaZipCode(final Country country)
			throws StateExistsException,
				CityExistsException,
				ZipCodeExistsException {
		State state = this.stateDelegate.create(
				"Montana", "MT", country, true, true);
		City city = this.cityDelegate.create("Helena", true, state, country);
		return this.zipCodeDelegate.create(city, "59601", null, true);
	}
	
	// Returns Idaho ZIP code - also creates Idaho and an Idaho city
	private ZipCode createIdahoZipCode(final Country country)
			throws StateExistsException,
				CityExistsException,
				ZipCodeExistsException {
		State state = this.stateDelegate.create(
				"Idaho", "ID", country, false, true);
		City city = this.cityDelegate.create(
				"Post Falls", true, state, country);
		return this.zipCodeDelegate.create(city, "83877", null, true);
	}
	
	// Returns address
	private Address createAddress(final String value, final ZipCode zipCode) {
		return this.addressDelegate.findOrCreate(
				value, null, null, null, zipCode);
	}
}