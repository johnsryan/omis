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
package omis.violationevent.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.facility.service.delegate.FacilityDelegate;
import omis.facility.service.delegate.UnitDelegate;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.service.ViolationEventService;
import omis.violationevent.service.delegate.ViolationEventDelegate;

/**
 * Tests method to update violation events.
 *
 * @author Josh Divine
 * @version 0.1.0 (May 23, 2018)
 * @since OMIS 3.0
 */
@Test
public class ViolationEventServiceUpdateViolationEventTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private ViolationEventDelegate violationEventDelegate;

	@Autowired
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;

	@Autowired
	private UnitDelegate unitDelegate;
	
	@Autowired
	private FacilityDelegate facilityDelegate;

	@Autowired
	private LocationDelegate locationDelegate;

	@Autowired
	private AddressDelegate addressDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;

	@Autowired
	private StateDelegate stateDelegate;

	@Autowired
	private CountryDelegate countryDelegate;

	@Autowired
	private OffenderDelegate offenderDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("violationEventService")
	private ViolationEventService violationEventService;

	/* Test methods. */

	/**
	 * Tests the update of the jurisdiction for a violation event.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateViolationEventJurisdiction() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "S", country, false, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("1", null, null, 
				null, zipCode);
		Location location = this.locationDelegate.create(jurisdiction, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		Facility facility = this.facilityDelegate.create(location, "Facility", 
				"F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, unit, date, details, category);
		SupervisoryOrganization newJurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg2", "SO2", 
						null);

		// Action
		violationEvent = this.violationEventService.updateViolationEvent(
				violationEvent, newJurisdiction, unit, date, details, category);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("jurisdiction", newJurisdiction)
			.addExpectedValue("unit", unit)
			.addExpectedValue("event.date", date)
			.addExpectedValue("event.details", details)
			.addExpectedValue("category", category)
			.performAssertions(violationEvent);
	}

	/**
	 * Tests the update of the unit for a violation event.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateViolationEventUnit() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "S", country, false, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("1", null, null, 
				null, zipCode);
		Location location = this.locationDelegate.create(jurisdiction, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		Facility facility = this.facilityDelegate.create(location, "Facility", 
				"F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, unit, date, details, category);
		Unit newUnit = this.unitDelegate.create("Unit 2", "U2", true, facility, 
				null);

		// Action
		violationEvent = this.violationEventService.updateViolationEvent(
				violationEvent, jurisdiction, newUnit, date, details, category);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("jurisdiction", jurisdiction)
			.addExpectedValue("unit", newUnit)
			.addExpectedValue("event.date", date)
			.addExpectedValue("event.details", details)
			.addExpectedValue("category", category)
			.performAssertions(violationEvent);
	}
	
	/**
	 * Tests the update of the date for a violation event.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateViolationEventDate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "S", country, false, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("1", null, null, 
				null, zipCode);
		Location location = this.locationDelegate.create(jurisdiction, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		Facility facility = this.facilityDelegate.create(location, "Facility", 
				"F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, unit, date, details, category);
		Date newDate = this.parseDateText("05/21/2017");

		// Action
		violationEvent = this.violationEventService.updateViolationEvent(
				violationEvent, jurisdiction, unit, newDate, details, category);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("jurisdiction", jurisdiction)
			.addExpectedValue("unit", unit)
			.addExpectedValue("event.date", newDate)
			.addExpectedValue("event.details", details)
			.addExpectedValue("category", category)
			.performAssertions(violationEvent);
	}
	
	/**
	 * Tests the update of the details for a violation event.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateViolationEventDetails() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "S", country, false, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("1", null, null, 
				null, zipCode);
		Location location = this.locationDelegate.create(jurisdiction, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		Facility facility = this.facilityDelegate.create(location, "Facility", 
				"F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, unit, date, details, category);
		String newDetails = "New details";

		// Action
		violationEvent = this.violationEventService.updateViolationEvent(
				violationEvent, jurisdiction, unit, date, newDetails, category);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("jurisdiction", jurisdiction)
			.addExpectedValue("unit", unit)
			.addExpectedValue("event.date", date)
			.addExpectedValue("event.details", newDetails)
			.addExpectedValue("category", category)
			.performAssertions(violationEvent);
	}
	
	/**
	 * Tests the update of the category for a violation event.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateViolationEventCategory() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "S", country, false, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("1", null, null, 
				null, zipCode);
		Location location = this.locationDelegate.create(jurisdiction, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		Facility facility = this.facilityDelegate.create(location, "Facility", 
				"F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, unit, date, details, category);
		ViolationEventCategory newCategory = ViolationEventCategory.SUPERVISION;

		// Action
		violationEvent = this.violationEventService.updateViolationEvent(
				violationEvent, jurisdiction, unit, date, details, newCategory);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("jurisdiction", jurisdiction)
			.addExpectedValue("unit", unit)
			.addExpectedValue("event.date", date)
			.addExpectedValue("event.details", details)
			.addExpectedValue("category", newCategory)
			.performAssertions(violationEvent);
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
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Country country = this.countryDelegate.create("Country", "C", true);
		State state = this.stateDelegate.create("State", "S", country, false, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("1", null, null, 
				null, zipCode);
		Location location = this.locationDelegate.create(jurisdiction, 
				new DateRange(this.parseDateText("01/01/2000"), null), address);
		Facility facility = this.facilityDelegate.create(location, "Facility", 
				"F", true, null);
		Unit unit = this.unitDelegate.create("Unit", "U", true, facility, null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		this.violationEventDelegate.create(offender, jurisdiction, unit, date, 
				details, category);
		Date secondDate = this.parseDateText("05/21/2017");
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, unit, secondDate, details, category);
		
		// Action
		violationEvent = this.violationEventService.updateViolationEvent(
				violationEvent, jurisdiction, unit, date, details, category);
	}

	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}