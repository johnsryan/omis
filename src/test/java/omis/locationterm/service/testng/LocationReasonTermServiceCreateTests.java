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
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonExistsException;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.service.LocationReasonTermService;
import omis.locationterm.service.delegate.LocationReasonDelegate;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderMismatchException;
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
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create location reason term.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 25, 2018)
 * @since OMIS 3.0
 */
@Test
public class LocationReasonTermServiceCreateTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
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
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationReasonTermService")
	private LocationReasonTermService locationReasonTermService;
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Tests. */
	
	/**
	 * Tests creation of location reason term.
	 * 
	 * @throws LocationReasonTermExistsException location reason term exists exception
	 * @throws OffenderMismatchException offender mismatch exception
	 * @throws LocationReasonTermConflictException location reason term conflict exception
	 * @throws DateRangeOutOfBoundsException date range our of bound exception
	 * @throws LocationTermExistsException  location term exists exception
	 * @throws LocationReasonExistsException 
	 */
	public void testCreate() throws LocationReasonTermExistsException, OffenderMismatchException, 
		LocationReasonTermConflictException, DateRangeOutOfBoundsException, 
		LocationTermExistsException, LocationReasonExistsException {
		//Arrangements	
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"LastName", "FirstName", null, null);
		DateRange dateRange = new DateRange(this.parseDateText("09/12/2001"), 
				this.parseDateText("12/03/2016"));
		LocationReason locationReason = this.locationReasonDelegate.create("ReasonName", (short) 1, true);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, null, 
				dateRange.getStartDate(), dateRange.getEndDate(), false);
		
		//Action
		LocationReasonTerm locationReasonTerm = this.locationReasonTermService.create(
				offender, locationTerm, locationReason, dateRange);
		
		//Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("locationTerm", locationTerm)
			.addExpectedValue("dateRange", dateRange)
			.performAssertions(locationReasonTerm);
	}
	
	/**
	 * Location reason term exists exception.
	 * 
	 * @throws LocationReasonTermExistsException location reason term exists
	 * @throws OffenderMismatchException offender mismatch
	 * @throws LocationReasonTermConflictException location reason term conflict
	 * @throws DateRangeOutOfBoundsException date range out of bounds
	 * @throws LocationReasonExistsException location reason exists
	 * @throws LocationTermExistsException  location reason exists
	 * @throws OrganizationExistsException organization exists
	 * @throws CountryExistsException country exists
	 * @throws StateExistsException state exists
	 * @throws CityExistsException city exists
	 * @throws ZipCodeExistsException zipcode exists
	 * @throws LocationExistsException location exists
	 */
	@Test(expectedExceptions = {LocationReasonTermExistsException.class})
	public void testLocationReasonTermExistsException() throws LocationReasonTermExistsException, 
		OffenderMismatchException, LocationReasonTermConflictException, 
		DateRangeOutOfBoundsException, LocationReasonExistsException, 
		LocationTermExistsException, OrganizationExistsException, CountryExistsException, 
		StateExistsException, CityExistsException, ZipCodeExistsException, LocationExistsException {
	
		//Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"LastName", "FirstName", null, null);
		DateRange dateRange = new DateRange(this.parseDateText("09/12/2001"), 
				this.parseDateText("12/03/2016"));
		LocationReason locationReason = this.locationReasonDelegate.create("ReasonName", (short) 1, true);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helena = this.cityDelegate.create("Helena",
				true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization organization = this.organizationDelegate
				.create("Organization", "MEGA", null);
		Location location = this.locationDelegate.create(
				organization, null, address);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, location, 
				dateRange.getStartDate(), dateRange.getEndDate(), false);	
		 this.locationReasonTermDelegate.create(locationTerm, dateRange, locationReason);
		
		//Action
		this.locationReasonTermService.create(offender, locationTerm, locationReason, dateRange);
	}
	
	/**
	 * Test location reason term conflict.
	 * 
	 * @throws LocationReasonTermExistsException location reason term exists
	 * @throws OffenderMismatchException offender mismatch
	 * @throws LocationReasonTermConflictException location reason term conflict
	 * @throws DateRangeOutOfBoundsException date range out of bounds
	 * @throws LocationReasonExistsException location reason exists
	 * @throws LocationTermExistsException  location reason exists
	 * @throws OrganizationExistsException organization exists
	 * @throws CountryExistsException country exists
	 * @throws StateExistsException state exists
	 * @throws CityExistsException city exists
	 * @throws ZipCodeExistsException zipcode exists
	 * @throws LocationExistsException location exists
	 */
	@Test(expectedExceptions = {LocationReasonTermConflictException.class})
	public void testLocationReasonTermConflictException() throws LocationReasonTermExistsException,
		OffenderMismatchException, LocationReasonTermConflictException, 
		DateRangeOutOfBoundsException, LocationReasonExistsException, CountryExistsException, 
		StateExistsException, CityExistsException, ZipCodeExistsException, 
		OrganizationExistsException, LocationExistsException, LocationTermExistsException {
	
		//Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"LastName", "FirstName", null, null);
		DateRange dateRange = new DateRange(this.parseDateText("09/12/2001"), 
				this.parseDateText("12/03/2016"));
		LocationReason locationReason = this.locationReasonDelegate.create("ReasonName", (short) 1, true);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helena = this.cityDelegate.create("Helena",
				true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization organization = this.organizationDelegate
				.create("Organization", "MEGA", null);
		Location location = this.locationDelegate.create(
				organization, null, address);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, location, 
				dateRange.getStartDate(), dateRange.getEndDate(), false);	
		 this.locationReasonTermDelegate.create(locationTerm, dateRange, locationReason);
		 dateRange = new DateRange(this.parseDateText("12/03/2016"), 
					this.parseDateText("02/03/2017"));
		 this.locationReasonTermDelegate.create(locationTerm, dateRange, locationReason);
		 dateRange = new DateRange(this.parseDateText("12/03/2016"), null);
		 this.locationReasonTermDelegate.create(locationTerm, dateRange, locationReason);
		 dateRange = new DateRange(this.parseDateText("09/12/2000"), 
					this.parseDateText("05/03/2017"));
		 
		//Action
		this.locationReasonTermService.create(offender, locationTerm, locationReason, dateRange);
	}
	
	/**
	 *  Test offender mismatch.
	 * 
	 * @throws LocationReasonExistsException location reason exists
	 * @throws LocationTermExistsException location term exists
	 * @throws LocationReasonTermExistsException location reason term exists
	 * @throws OffenderMismatchException offender mismatch
	 * @throws LocationReasonTermConflictException location reason term conflict
	 * @throws DateRangeOutOfBoundsException date range out of bounds
	 */
	@Test(expectedExceptions = {OffenderMismatchException.class})
	public void testOffenderMismatchException() throws LocationReasonExistsException, 
		LocationTermExistsException, LocationReasonTermExistsException, 
		OffenderMismatchException, LocationReasonTermConflictException, 
		DateRangeOutOfBoundsException {
		
		//Arrangements	
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"LastName", "FirstName", null, null);
		Offender offender2 = this.offenderDelegate.createWithoutIdentity(
				"LName", "FName", "MName", null);
		DateRange dateRange = new DateRange(this.parseDateText("09/12/2001"), 
				this.parseDateText("12/03/2016"));
		LocationReason locationReason = this.locationReasonDelegate.create("ReasonName", (short) 1, true);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, null, 
				dateRange.getStartDate(), dateRange.getEndDate(), false);
		
		//Action
		this.locationReasonTermService.create(
				offender2, locationTerm, locationReason, dateRange);
	}
	 
	/**
	 * Test date range out of bounds.
	 * 
	 * @throws LocationTermExistsException location term exists
	 * @throws LocationReasonExistsException location reason exists
	 * @throws LocationReasonTermExistsException location reason exception exists
	 * @throws OffenderMismatchException offender mismatch
	 * @throws LocationReasonTermConflictException location reason conflict
	 * @throws DateRangeOutOfBoundsException date range out of bounds
	 */
	@Test(expectedExceptions = {DateRangeOutOfBoundsException.class})
	public void testDateRangeOutOfBoundsException() throws LocationTermExistsException, 
		LocationReasonExistsException, LocationReasonTermExistsException, 
		OffenderMismatchException, LocationReasonTermConflictException, 
		DateRangeOutOfBoundsException {
		
		//Arrangements	
				Offender offender = this.offenderDelegate.createWithoutIdentity(
						"LastName", "FirstName", null, null);
				DateRange dateRange = new DateRange(this.parseDateText("09/12/2001"), 
						this.parseDateText("12/03/2016"));
				LocationReason locationReason = this.locationReasonDelegate.create(
						"ReasonName", (short) 1, true);
				LocationTerm locationTerm = this.locationTermDelegate.create(offender, null, 
						dateRange.getStartDate(), dateRange.getEndDate(), false);
				dateRange = new DateRange(this.parseDateText("12/03/2016"), 
						this.parseDateText("12/03/2017"));
				
				//Action
				this.locationReasonTermService.create(
						offender, locationTerm, locationReason, dateRange);
	}
	
	/* Helper methods. */
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
}