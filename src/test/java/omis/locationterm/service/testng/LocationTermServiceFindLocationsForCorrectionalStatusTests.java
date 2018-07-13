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
 * Tests lookup of locations allowed for correctional status.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 10, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceFindLocationsForCorrectionalStatusTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Service delegates. */
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
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
	 * Tests look up of allowed location for correctional status.
	 * 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public void testFind()
			throws SupervisoryOrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				CorrectionalStatusExistsException,
				AllowedSupervisoryOrganizationRuleExistsException {
		
		// Arrangements - allow prison location when secure
		CorrectionalStatus secure = this.createSecureStatus();
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		ZipCode zipCode = this.createZipCode();
		Address prisonAddress = this.createAddress(
				"1000 1000TH ST #1000", zipCode);
		Date startDate = this.parseDateText("12/12/2012");
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(startDate,  null), prisonAddress);
		this.allowedSupervisoryOrganizationRuleDelegate.create(secure, prison);
		
		// Action - look up allowed locations by correctional status
		List<Location> allowedLocations = this.locationTermService
				.findLocationsForCorrectionalStatus(secure);
		
		// Asserts that single location is allowed
		assert allowedLocations.size() == 1
				: String.format("Wrong number of allowed locations"
							+ " - 1 expected; %d found",
							allowedLocations.size());
		assert allowedLocations.get(0).equals(prisonLocation)
				: "Wrong allowed location";
	}
	
	/**
	 * Tests that allowed location is not returned for organization without
	 * location (without error).
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public void testFindWithoutLocationIsEmpty()
			throws CorrectionalStatusExistsException,
				SupervisoryOrganizationExistsException,
				AllowedSupervisoryOrganizationRuleExistsException {
		
		// Arrangements - allow prison organization when secure
		CorrectionalStatus secure = this.createSecureStatus();
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		this.allowedSupervisoryOrganizationRuleDelegate.create(secure, prison);
		
		// Action - look up allowed locations by correctional status
		List<Location> allowedLocations = this.locationTermService
				.findLocationsForCorrectionalStatus(secure);
		
		// Asserts that no locations are allowed
		assert allowedLocations.size() == 0
				: "Locations are allowed";
	}
	
	/**
	 * Tests that locations are not allowed for different correctional status
	 * (without error).
	 * 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws AllowedSupervisoryOrganizationRuleExistsException if allowed
	 * supervisory organization rule exists
	 */
	public void testFindWithDifferentCorrectionalStatusIsEmpty()
			throws CorrectionalStatusExistsException,
				SupervisoryOrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				AllowedSupervisoryOrganizationRuleExistsException {
		
		// Arrangements - allow prison location when secure
		CorrectionalStatus secure = this.createSecureStatus();
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		ZipCode zipCode = this.createZipCode();
		Address prisonAddress = this.createAddress(
				"1000 1000TH ST #1000", zipCode);
		Date startDate = this.parseDateText("12/12/2012");
		this.locationDelegate
				.create(prison, new DateRange(startDate,  null), prisonAddress);
		this.allowedSupervisoryOrganizationRuleDelegate.create(secure, prison);
		
		// Action - look up allowed locations by different correctional status
		CorrectionalStatus altSecure = this.createAltSecure();
		List<Location> allowedLocations = this.locationTermService
				.findLocationsForCorrectionalStatus(altSecure);
		
		// Asserts that no locations are allowed
		assert allowedLocations.size() == 0
				: "Locations are allowed";
	}
	
	/* Helper methods. */

	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
	
	// Creates secure status
	private CorrectionalStatus createSecureStatus()
			throws CorrectionalStatusExistsException {
		return this.correctionalStatusDelegate.create(
				"Secure", "SEC", true, (short) 1, true);
	}
	
	// Creates alt-secure status
	private CorrectionalStatus createAltSecure()
			throws CorrectionalStatusExistsException {
		return this.correctionalStatusDelegate.create(
				"Alt-Secure", "ALT", true, (short) 1, true);
	}
	
	// Returns MT 59602 ZIP code
	private ZipCode createZipCode()
			throws CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException {
		Country country = this.countryDelegate
				.create("United States", "USA", true);
		State state = this.stateDelegate
				.create("Montana", "MT", country, true, true);
		City city = this.cityDelegate.create("Helena", true, state, country);
		return this.zipCodeDelegate.create(city, "59602", null, true);
	}
	
	// Creates address
	private Address createAddress(final String value, final ZipCode zipCode) {
		return this.addressDelegate.findOrCreate(
				value, null, null, null, zipCode);
	}
}