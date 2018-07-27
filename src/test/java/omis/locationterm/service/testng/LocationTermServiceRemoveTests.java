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
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.LocationReasonExistsException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermChangeActionAssociationExistsException;
import omis.locationterm.exception.LocationTermChangeActionExistsException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.service.delegate.LocationReasonDelegate;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermChangeActionAssociationDelegate;
import omis.locationterm.service.delegate.LocationTermChangeActionDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.exception.StateExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests method to remove location terms in location term service.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 10, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceRemoveTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Service delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
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
	@Qualifier("locationTermDelegate")
	private LocationTermDelegate locationTermDelegate;
	
	@Autowired
	@Qualifier("locationReasonDelegate")
	private LocationReasonDelegate locationReasonDelegate;
	
	@Autowired
	@Qualifier("locationReasonTermDelegate")
	private LocationReasonTermDelegate locationReasonTermDelegate;

	@Autowired
	@Qualifier("locationTermChangeActionDelegate")
	private LocationTermChangeActionDelegate locationTermChangeActionDelegate;
	
	@Autowired
	@Qualifier("locationTermChangeActionAssociationDelegate")
	private LocationTermChangeActionAssociationDelegate
	locationTermChangeActionAssociationDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Test methods. */
	
	/**
	 * Tests removal of location term.
	 * 
	 * @throws OrganizationExistsException if organization exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 */
	public void testRemoval()
			throws OrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				LocationTermExistsException {
		
		// Arrangements - locates offender at hospital
		Offender offender = this.createOffender();
		Location hospital = this.createHospital();
		Date startDate = this.parseDateText("12/12/2012");
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, hospital, startDate, null, true);
		
		// Action - removes location term
		this.locationTermService.remove(locationTerm);
		
		// Assertions - verify that the location term is removed
		assert locationTermDelegate.findByOffenderOnDate(offender, startDate)
				== null
			: "Location term not removed";
	}
	
	/**
	 * Tests removal of location term with reasons.
	 * 
	 * @throws OrganizationExistsException if organization exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationReasonExistsException if location reason exists
	 * @throws LocationReasonTermExistsException if location reason term exists
	 */
	public void testRemovalWithReasonTerms()
			throws OrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationReasonExistsException,
				LocationReasonTermExistsException {
		
		// Arrangements - locates offender at hospital with reasons
		Offender offender = this.createOffender();
		Location hospital = this.createHospital();
		Date startDate = this.parseDateText("12/12/2012");
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, hospital, startDate, null, true);
		LocationReason reason = this.locationReasonDelegate
				.create("Health issues", (short) 1, true);
		this.locationReasonTermDelegate.create(locationTerm,
				new DateRange(startDate, null), reason);
		
		// Action - removes location term
		this.locationTermService.remove(locationTerm);
		
		// Assertions - verify that the location term is removed
		assert locationTermDelegate.findByOffenderOnDate(offender, startDate)
				== null
			: "Location term not removed";
	}
	
	/**
	 * Tests removal of location term with associated change action
	 * 
	 * @throws OrganizationExistsException if organization exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationTermChangeActionExistsException if change action exists
	 * @throws LocationTermChangeActionAssociationExistsException if association
	 * of location term to change action exists
	 */
	public void testRemovalWithAssociatedChangeAction()
			throws OrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationTermChangeActionExistsException,
				LocationTermChangeActionAssociationExistsException {
		
		// Arrangements - locates offender at hospital with change action
		Offender offender = this.createOffender();
		Location hospital = this.createHospital();
		Date startDate = this.parseDateText("12/12/2012");
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, hospital, startDate, null, true);
		LocationTermChangeAction changeAction
			= this.locationTermChangeActionDelegate.create(
					"Hospital visit", true);
		this.locationTermChangeActionAssociationDelegate
				.create(locationTerm, changeAction);
		
		// Action - removes location term
		this.locationTermService.remove(locationTerm);
		
		// Assertions - verify that the location term is removed
		assert locationTermDelegate.findByOffenderOnDate(offender, startDate)
				== null
			: "Location term not removed";
	}
	
	/* Helper methods. */
	
	// Returns offender
	private Offender createOffender() {
		return this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
	}
	
	// Returns hospital
	private Location createHospital()
			throws OrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException {
		Organization hospital = this.organizationDelegate
				.create("Hospital", "HOSP", null);
		Country country = this.countryDelegate
				.create("United States", "USA", true);
		State state = this.stateDelegate
				.create("Montana", "MT", country, true, true);
		City city = this.cityDelegate
				.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate
				.create(city, "59601", null, true);
		Address address = this.addressDelegate
				.findOrCreate(
						"1000 1000ST ST #1000", null, null, null, zipCode);
		return this.locationDelegate.create(hospital,
				new DateRange(this.parseDateText("12/12/2012"), null), address);
	}
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
}