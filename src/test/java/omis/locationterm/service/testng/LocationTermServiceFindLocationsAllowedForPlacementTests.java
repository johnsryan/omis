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
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
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
 * Unit tests for look up of locations allowed for placement.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 17, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceFindLocationsAllowedForPlacementTests
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
	
	/* Unit tests. */

	/**
	 * Tests that location for organization allowed for correctional status is
	 * correctly returned.
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
		
		// Arrangements - creates supervisory organization with location
		// allowed for correctional status
		SupervisoryOrganization prison = this.createPrison();
		CorrectionalStatus secure = this.createSecure();
		this.allowedSupervisoryOrganizationRuleDelegate
				.create(secure, prison);
		Country country = this.createCountry();
		ZipCode zipCode = this.createMontanaZipCode(country);
		Address prisonAddress = this.createAddress(
				"1000 1000ST #1000", zipCode);
		Location prisonLocation = this.locationDelegate.create(
				prison, null, prisonAddress);
		
		// Action - lookup allowed locations
		List<Location> allowedLocations = this.locationTermService
				.findLocationsAllowedForPlacement();
		
		// Assertions - check that prison is allowed
		assert allowedLocations.size() == 1
				: "Wrong number of allowed locations";
		assert allowedLocations.get(0).equals(prisonLocation)
				: "Wrong allowed location";
	}
	
	/**
	 * Tests that an empty list is returned when locations are not allowed.
	 * 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 * @throws OrganizationExistsException if organization exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 */
	public void testEmptyWhenNotAllowed()
			throws SupervisoryOrganizationExistsException,
				CorrectionalStatusExistsException,
				AllowedSupervisoryOrganizationRuleExistsException,
				OrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException {
		
		// Arrangements - allows prison while secure, creates unrelated
		// locations
		SupervisoryOrganization prison = this.createPrison();
		CorrectionalStatus secure = this.createSecure();
		this.allowedSupervisoryOrganizationRuleDelegate
				.create(secure, prison);
		Organization organization = this.organizationDelegate
				.create("Prerelease", "PRER", null);
		Country country = this.createCountry();
		ZipCode zipCode = this.createMontanaZipCode(country);
		Address pnpOfficeAddress = this.createAddress(
				"2000 2000TH ST #2000", zipCode);
		this.locationDelegate.create(organization, null, pnpOfficeAddress);
		
		// Action - lookup allowed locations
		List<Location> allowedLocations = this.locationTermService
				.findLocationsAllowedForPlacement();
		
		// Asserts that no locations are allowed
		assert allowedLocations.isEmpty()
			: "Locations allowed";
	}
	
	/** Tests that no locations are allowed when there are no rules. */
	public void testEmpty() {
		
		// Action and assertion
		assert this.locationTermService.findLocationsAllowedForPlacement()
				.isEmpty()
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
	
	// Returns Montana ZIP code
	private ZipCode createMontanaZipCode(final Country country)
			throws StateExistsException,
				CityExistsException,
				ZipCodeExistsException {
		State state = this.stateDelegate.create(
				"Montana", "MT", country, true, true);
		City city = this.cityDelegate.create("Helena", true, state, country);
		return this.zipCodeDelegate.create(city, "59601", null, true);
	}
	
	// Returns address
	private Address createAddress(final String value, final ZipCode zipCode) {
		return this.addressDelegate.findOrCreate(
				value, null, null, null, zipCode);
	}
}