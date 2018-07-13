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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.service.LocationTermService;
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
 * Tests look up of locations allowed for correctional status by State.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 11, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceFindLocationsForCorrectionalStatusInStateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Service delegates. */
	
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
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("allowedSupervisoryOrganizationRuleDelegate")
	private AllowedSupervisoryOrganizationRuleDelegate
	allowedSupervisoryOrganizationRuleDelegate;
	
	/* Service. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Unit tests. */
	
	/**
	 * Tests look up of allowed location on correctional status in State.
	 * 
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws LocationExistsException if location exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public void testFind()
			throws CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				SupervisoryOrganizationExistsException,
				LocationExistsException,
				CorrectionalStatusExistsException,
				AllowedSupervisoryOrganizationRuleExistsException {
		
		// Arrangements - creates prison allowed on secure status in Montana
		Country country = this.countryDelegate.create(
				"United States", "US", true);
		State montana = this.createMontana(country);
		ZipCode zipCode = this.createMontanaZipCode(montana);
		Address prisonAddress = this.createAddress(
				"1000 1000TH ST #1000", zipCode);
		SupervisoryOrganization prisonOrganization
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Date startDate = this.parseDateText("12/12/2012");
		Location prison = this.locationDelegate
				.create(prisonOrganization, new DateRange(startDate, null),
						prisonAddress);
		CorrectionalStatus secure = this.createSecureStatus();
		this.allowedSupervisoryOrganizationRuleDelegate.create(
				secure, prisonOrganization);
		
		// Action - look up allowed locations on secure status in Montana
		List<Location> allowedLocations = this.locationTermService
				.findLocationsForCorrectionalStatusInState(secure, montana);
		
		// Assertions - verify that prison was returned
		assert allowedLocations.size() == 1
				: "Unexpected number of allowed locations returned";
		assert allowedLocations.get(0).equals(prison)
				: "Unexpected location allowed";
	}
	
	/**
	 * Tests that lookups for one State do not return allowed locations from
	 * another when the correctional status is the same.
	 * 
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws LocationExistsException if location exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public void testNoResultsWhenAllowedInOtherState()
			throws CountryExistsException,
				StateExistsException,
				SupervisoryOrganizationExistsException,
				LocationExistsException,
				AllowedSupervisoryOrganizationRuleExistsException,
				CityExistsException,
				ZipCodeExistsException,
				CorrectionalStatusExistsException {
	
		// Arrangements - creates prison allowed on secure status in Montana
		Country country = this.countryDelegate.create(
				"United States", "US", true);
		State montana = this.createMontana(country);
		ZipCode zipCode = this.createMontanaZipCode(montana);
		Address prisonAddress = this.createAddress(
				"1000 1000TH ST #1000", zipCode);
		SupervisoryOrganization prisonOrganization
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Date startDate = this.parseDateText("12/12/2012");
		this.locationDelegate.create(prisonOrganization,
				new DateRange(startDate, null), prisonAddress);
		CorrectionalStatus secure = this.createSecureStatus();
		this.allowedSupervisoryOrganizationRuleDelegate.create(
				secure, prisonOrganization);
		
		// Action - look up allowed locations on secure status in Idaho
		State idaho = this.createIdaho(country);
		List<Location> allowedLocations = this.locationTermService
				.findLocationsForCorrectionalStatusInState(secure, idaho);
		
		// Assertions - verify that no results are returned
		assert allowedLocations.isEmpty()
			: "Allowed locations from different State returned";
	}
	
	/**
	 * Tests that look ups for allowed locations in same State but on different
	 * statues return no results.
	 * 
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws LocationExistsException if location exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public void testNoResultsWhenNotAllowedForStatusSameState()
			throws CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				SupervisoryOrganizationExistsException,
				LocationExistsException,
				CorrectionalStatusExistsException,
				AllowedSupervisoryOrganizationRuleExistsException {
		
		// Arrangements - creates prison allowed on secure status in Montana
		Country country = this.countryDelegate.create(
				"United States", "US", true);
		State montana = this.createMontana(country);
		ZipCode zipCode = this.createMontanaZipCode(montana);
		Address prisonAddress = this.createAddress(
				"1000 1000TH ST #1000", zipCode);
		SupervisoryOrganization prisonOrganization
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Date startDate = this.parseDateText("12/12/2012");
		this.locationDelegate.create(prisonOrganization,
				new DateRange(startDate, null), prisonAddress);
		CorrectionalStatus secure = this.createSecureStatus();
		this.allowedSupervisoryOrganizationRuleDelegate.create(
				secure, prisonOrganization);
		
		// Action - look up allowed locations on Alt-Secure status in Montana
		CorrectionalStatus altSecure = this.createAltSecureStatus();
		List<Location> allowedLocations = this.locationTermService
				.findLocationsForCorrectionalStatusInState(altSecure, montana);
		
		// Assertions - verify that no results are returned
		assert allowedLocations.isEmpty()
			: "Allowed locations from different State returned";
	}
	
	/* Helper methods. */
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
	
	// Creates address
	private Address createAddress(final String value, final ZipCode zipCode) {
		return this.addressDelegate.findOrCreate(
				value, null, null, null, zipCode);
	}
	
	// Creates Montana ZIP code
	private ZipCode createMontanaZipCode(final State montana)
			throws CityExistsException,
				ZipCodeExistsException {
		City city = this.cityDelegate.create(
				"Montana", true, montana, montana.getCountry());
		return this.zipCodeDelegate.create(city, "59602", null, true);
	}
	
	// Creates secure status
	private CorrectionalStatus createSecureStatus()
			throws CorrectionalStatusExistsException {
		return this.correctionalStatusDelegate.create(
				"Secure", "SEC", true, (short) 1, true);
	}
	
	// Creates alt-secure status
	private CorrectionalStatus createAltSecureStatus()
			throws CorrectionalStatusExistsException {
		return this.correctionalStatusDelegate.create(
				"Alt-Secure", "ALT", true, (short) 1, true);
	}

	
	// Returns Montana
	private State createMontana(final Country country)
			throws StateExistsException {
		return this.stateDelegate.create("Montana", "MT", country, true, true);
	}
	
	// Returns Idaho
	private State createIdaho(final Country country)
			throws StateExistsException {
		return this.stateDelegate.create("Idaho", "ID", country, true, true);
	}
}