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
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.AddressUnitDesignatorDelegate;
import omis.address.service.delegate.StreetSuffixDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonTermConflictException;
import omis.locationterm.exception.LocationReasonTermExistsAfterException;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.service.delegate.LocationReasonDelegate;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Location term service reason term creation tests.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceUpdateReasonTermTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
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
	@Qualifier("streetSuffixDelegate")
	private StreetSuffixDelegate streetSuffixDelegate;
	
	@Autowired
	@Qualifier("locationTermDelegate")
	private LocationTermDelegate locationTermDelegate;
	
	@Autowired
	@Qualifier("addressUnitDesignatorDelegate")
	private AddressUnitDesignatorDelegate addressUnitDesignatorDelegate;
	
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
	@Qualifier("locationReasonDelegate")
	private LocationReasonDelegate locationReasonDelegate;
	
	@Autowired
	@Qualifier("locationReasonTermDelegate")
	private LocationReasonTermDelegate locationReasonTermDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Tests. */
	
	/**
	 * Tests update of location reason term date range.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	public void testUpdateReasonTermDateRange()
			throws DuplicateEntityFoundException,
			LocationReasonTermConflictException, 
			DateRangeOutOfBoundsException, 
			LocationReasonTermExistsAfterException {
		
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates,
				true, true);
		City helena = this.cityDelegate
				.create("Helena", true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000", null, null, BuildingCategory.APARTMENT,
						mt59602);
		Organization prison = this.organizationDelegate
				.create("Prison", "PRS", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				null);
		LocationReasonTerm reasonTerm = this.locationReasonTermDelegate.create(
				locationTerm, dateRange, reason);
		dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				this.parseDateText("02/21/2017"));
		
		// Action
		this.locationTermService.updateReasonTerm(reasonTerm, dateRange, 
				reason);
		
		// Assert
		assertValues(reasonTerm, locationTerm, dateRange, reason);
	}
	
	/**
	 * Tests update of location reason term reason.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	public void testUpdateReasonTermReason()
			throws DuplicateEntityFoundException,
			LocationReasonTermConflictException, 
			DateRangeOutOfBoundsException, 
			LocationReasonTermExistsAfterException {
		
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates,
				true, true);
		City helena = this.cityDelegate
				.create("Helena", true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000", null, null, BuildingCategory.APARTMENT,
						mt59602);
		Organization prison = this.organizationDelegate
				.create("Prison", "PRS", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				null);
		LocationReasonTerm reasonTerm = this.locationReasonTermDelegate.create(
				locationTerm, dateRange, reason);
		
		LocationReason newReason = this.locationReasonDelegate
				.create("New Reason", (short) 2, true);
		
		// Action
		this.locationTermService.updateReasonTerm(reasonTerm, dateRange, 
				newReason);
		
		// Assert
		assertValues(reasonTerm, locationTerm, dateRange, newReason);
	}
	
	/**
	 * Tests duplicate entity exception is properly thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException()
			throws DuplicateEntityFoundException,
			LocationReasonTermConflictException, 
			DateRangeOutOfBoundsException, 
			LocationReasonTermExistsAfterException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates,
				true, true);
		City helena = this.cityDelegate
				.create("Helena", true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000", null, null, BuildingCategory.APARTMENT,
						 mt59602);
		Organization prison = this.organizationDelegate
				.create("Prison", "PRS", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				this.parseDateText("11/10/2016"));
		this.locationReasonTermDelegate.create(locationTerm, dateRange, reason);
		DateRange secondDateRange = new DateRange(
				this.parseDateText("11/10/2016"), null);
		LocationReasonTerm locationReasonTerm = this.locationReasonTermDelegate
				.create(locationTerm, secondDateRange, reason);
		
		// Action
		this.locationTermService.updateReasonTerm(locationReasonTerm, dateRange, 
				reason);
	}
	
	/**
	 * Tests date range out of bounds exception is properly thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	@Test(expectedExceptions = {DateRangeOutOfBoundsException.class})
	public void testDateRangeOutOfBoundsException()
			throws DuplicateEntityFoundException,
			LocationReasonTermConflictException, 
			DateRangeOutOfBoundsException, 
			LocationReasonTermExistsAfterException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates,
				true, true);
		City helena = this.cityDelegate
				.create("Helena", true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000", null, null, BuildingCategory.APARTMENT,
						mt59602);
		Organization prison = this.organizationDelegate
				.create("Prison", "PRS", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				null);
		LocationReasonTerm reasonTerm = this.locationReasonTermDelegate
				.create(locationTerm, dateRange, reason);
		dateRange = new DateRange(this.parseDateText("09/23/2015"), 
				null);
		
		// Action
		this.locationTermService.updateReasonTerm(reasonTerm, dateRange,
				reason);
	}
	
	/**
	 * Tests location reason term conflict exception is properly thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	@Test(expectedExceptions = {LocationReasonTermConflictException.class})
	public void testLocationReasonTermConflictException()
			throws DuplicateEntityFoundException,
			LocationReasonTermConflictException, 
			DateRangeOutOfBoundsException, 
			LocationReasonTermExistsAfterException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates,
				true, true);
		City helena = this.cityDelegate
				.create("Helena", true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000", null, null, BuildingCategory.APARTMENT,
						 mt59602);
		Organization prison = this.organizationDelegate
				.create("Prison", "PRS", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("01/23/2017"), 
				this.parseDateText("01/24/2018"));
		this.locationReasonTermDelegate.create(locationTerm, dateRange, reason);
		dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				this.parseDateText("01/23/2017"));
		LocationReasonTerm reasonTerm = this.locationReasonTermDelegate
				.create(locationTerm, dateRange, reason);
		dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				this.parseDateText("01/01/2018"));
		
		// Action
		this.locationTermService.updateReasonTerm(reasonTerm, dateRange,
				reason);
	}
	
	/**
	 * Tests location reason term exists after exception is properly thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	@Test(expectedExceptions = {LocationReasonTermExistsAfterException.class})
	public void testLocationReasonTermExistsAfterException()
			throws DuplicateEntityFoundException,
			LocationReasonTermConflictException, 
			DateRangeOutOfBoundsException, 
			LocationReasonTermExistsAfterException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates,
				true, true);
		City helena = this.cityDelegate
				.create("Helena", true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000", null, null, BuildingCategory.APARTMENT,
						mt59602);
		Organization prison = this.organizationDelegate
				.create("Prison", "PRS", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("01/23/2017"), 
				null);
		this.locationReasonTermDelegate.create(locationTerm, dateRange, reason);
		dateRange = new DateRange(this.parseDateText("09/23/2016"), 
				this.parseDateText("01/23/2017"));
		LocationReasonTerm reasonTerm = this.locationReasonTermDelegate
				.create(locationTerm, dateRange, reason);
		dateRange.setEndDate(null);
		
		// Action
		this.locationTermService.updateReasonTerm(reasonTerm, dateRange,
				reason);
	}
	
	/**
	 * Tests that, given a location term with two reason terms, the earliest
	 * reason term can be updated to its original values.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds
	 * @throws LocationReasonTermConflictException if conflicting reason terms
	 * exist
	 * @throws LocationReasonTermExistsAfterException if reason terms exist
	 * after 
	 */
	public void testUpdateEarliestAllowedMultipleReasonSameValues()
			throws DuplicateEntityFoundException,
				LocationReasonTermExistsAfterException,
				LocationReasonTermConflictException,
				DateRangeOutOfBoundsException {
		
		// Arrangements places Blofeld in jail pending new charges and then
		// on a revokation
		Offender ernstBlofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Organization jail = this.organizationDelegate
				.create("Jail", "JAIL", null);
		Country usa = this.countryDelegate
				.create("United States of America", "USA", true);
		State mt = this.stateDelegate
				.create("Montana", "MT", usa, true, true);
		City helena = this.cityDelegate.create("Helena", true, mt, usa);
		ZipCode mt59601 = this.zipCodeDelegate
				.create(helena, "59601", null, true);
		Address jailAddress = this.addressDelegate
				.findOrCreate("1111 1ST S", null, null, null, mt59601);
		Date locationStartDate = this.parseDateText("01/01/2001");
		Date locationEndDate = this.parseDateText("03/03/2003");
		Location jailLocation = this.locationDelegate
				.create(jail,
						new DateRange(locationStartDate, locationEndDate),
						jailAddress);
		LocationTerm jailTerm = this.locationTermDelegate
				.create(ernstBlofeld, jailLocation, locationStartDate,
						locationEndDate, false);
		LocationReason pendingNewCharges
			= this.locationReasonDelegate.create(
					"Pending New Charges", (short) 1, true);
		Date revocationDate = this.parseDateText("02/02/2002");
		LocationReasonTerm newChargesReasonTerm
			= this.locationReasonTermDelegate
				.create(jailTerm, new DateRange(
						locationStartDate, revocationDate), pendingNewCharges);
		LocationReason pendingRevocation
			= this.locationReasonDelegate.create(
					"Pending Revocation", (short) 1, true);
		this.locationReasonTermDelegate.create(jailTerm,
				new DateRange(revocationDate, locationEndDate),
				pendingRevocation);
		
		// Action - attempt to save reason of pending new charges (earliest
		// reason term) with same values
		newChargesReasonTerm = this.locationTermService
					.updateReasonTerm(newChargesReasonTerm,
							new DateRange(locationStartDate, revocationDate),
							pendingNewCharges);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", locationStartDate)
			.addExpectedValue("dateRange.endDate", revocationDate)
			.addExpectedValue("reason", pendingNewCharges)
			.addExpectedValue("locationTerm", jailTerm)
			.performAssertions(newChargesReasonTerm);
	}
	
	/**
	 * Tests that, given a location term with two reason terms, the earliest
	 * reason term can be updated to corrected values.
	 * 
	 * <p>In this case, the original reason terms have a gap - the earliest is
	 * corrected so that the gap is closed.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws LocationReasonTermExistsAfterException if reason terms exist
	 * after 
	 * @throws LocationReasonTermConflictException if conflicting reason
	 * terms exist
	 * @throws DateRangeOutOfBoundsException if date range is out of
	 * bounds
	 */
	public void testUpdateEarliestAllowedMultipleReasonDifferentValues()
			throws DuplicateEntityFoundException,
			LocationReasonTermExistsAfterException,
			LocationReasonTermConflictException,
			DateRangeOutOfBoundsException {
		
		// Arrangements - place Blofeld in jail pending new charges with an
		// erroneous break in reasons before revocation
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Organization jail = this.organizationDelegate.create(
				"Jail", "JAIL", null);
		Country usa = this.countryDelegate.create("United States", "USA", true);
		State mt = this.stateDelegate.create("Montana", "MT", usa, true, true);
		City butte = this.cityDelegate.create("Butte", true, mt, usa);
		ZipCode mt59701 = this.zipCodeDelegate.create(
				butte, "59701", null, true);
		Address jailAddress = this.addressDelegate.findOrCreate(
				"2020 2ND ST 1", null, null, null, mt59701);
		Date locationStartDate = this.parseDateText("01/01/2001");
		Date locationEndDate = this.parseDateText("03/03/2003");
		Location jailLocation = this.locationDelegate
				.create(jail,
						new DateRange(locationStartDate, locationEndDate),
						jailAddress);
		LocationTerm jailTerm = this.locationTermDelegate.create(
				blofeld, jailLocation, locationStartDate, locationEndDate,
				false);
		LocationReason pendingNewCharges = this.locationReasonDelegate
				.create("Pending New Charges", (short) 1, true);
		Date incorrectRevocationDate = this.parseDateText(
				"01/02/2002");
		LocationReasonTerm newChargesTerm
			= this.locationReasonTermDelegate.create(
					jailTerm, new DateRange(
							locationStartDate, incorrectRevocationDate),
					pendingNewCharges);
		Date revocationDate = this.parseDateText("02/02/2002");
		LocationReason pendingRevocation
			= this.locationReasonDelegate.create("Pending Revocation",
					(short) 1, true);
		this.locationReasonTermDelegate.create(jailTerm,
				new DateRange(revocationDate, locationEndDate),
				pendingRevocation);
		
		// Action - fix the error by correcting the pending new charge reason
		this.locationTermService.updateReasonTerm(newChargesTerm,
				new DateRange(locationStartDate, revocationDate),
				pendingNewCharges);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", locationStartDate)
			.addExpectedValue("dateRange.endDate", revocationDate)
			.addExpectedValue("locationTerm", jailTerm)
			.addExpectedValue("reason", pendingNewCharges)
			.performAssertions(newChargesTerm);
	}
	
	/**
	 * Tests that, given a location term with two reason terms, the latest
	 * reason term can be updated to its original values.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws LocationReasonTermExistsAfterException if reason terms exist
	 * after
	 * @throws LocationReasonTermConflictException if conflicting reason terms
	 * exist
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds
	 */
	public void testUpdateLatestAllowedMultipleReasonSameValues()
			throws DuplicateEntityFoundException,
				LocationReasonTermExistsAfterException,
				LocationReasonTermConflictException,
				DateRangeOutOfBoundsException {
		
		// Arrangements - place Blofeld in jail pending new charges and
		// then a revocation immediately afterwards
		Offender ernstBlofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Organization jail = this.organizationDelegate
				.create("Jail", "JAIL", null);
		Country usa = this.countryDelegate.create(
				"United States of America", "USA", true);
		State mt = this.stateDelegate.create("Montana", "MT", usa, true, true);
		City havre = this.cityDelegate.create("Havre", true, mt, usa);
		ZipCode mt59501 = this.zipCodeDelegate
				.create(havre, "59501", null, true);
		Address jailAddress = this.addressDelegate
				.findOrCreate("5000 5TH ST SUITE 5", null, null,
						null, mt59501);
		Date locationStartDate = this.parseDateText("01/01/2001");
		Date locationEndDate = this.parseDateText("03/03/2003");
		Location jailLocation = this.locationDelegate
				.create(jail, new DateRange(locationStartDate, locationEndDate),
						jailAddress);
		LocationTerm jailTerm = this.locationTermDelegate.create(
				ernstBlofeld, jailLocation, locationStartDate,
				locationEndDate, false);
		Date revocationDate = this.parseDateText("02/02/2002");
		LocationReason pendingNewCharges
			= this.locationReasonDelegate.create("Pending New Charges",
					(short) 1, true);
		this.locationReasonTermDelegate.create(jailTerm,
				new DateRange(locationStartDate, revocationDate),
				pendingNewCharges);
		LocationReason revocation = this.locationReasonDelegate
				.create("Pending Revocation", (short) 1, true);
		LocationReasonTerm revocationReasonTerm
			= this.locationReasonTermDelegate.create(jailTerm,
					new DateRange(revocationDate, locationEndDate),
					revocation);
		
		// Action - updates revocation reason term to same values
		revocationReasonTerm = this.locationTermService
				.updateReasonTerm(revocationReasonTerm,
					new DateRange(revocationDate, locationEndDate),
					revocation);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", revocationDate)
			.addExpectedValue("dateRange.endDate", locationEndDate)
			.addExpectedValue("locationTerm", jailTerm)
			.addExpectedValue("reason", revocation)
			.performAssertions(revocationReasonTerm);
	}
	
	/**
	 * Tests that, given a location term with two reasons, the latest reason
	 * term can be updated to corrected values.
	 * 
	 * <p>In this case, the original reason terms have a gap - the latest
	 * is corrected so that the gap is closed.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws LocationReasonTermExistsAfterException if reason terms exist
	 * after
	 * @throws LocationReasonTermConflictException if conflicting reason terms
	 * exist
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds
	 */
	public void testUpdateLatestAllowedMultipleReasonDifferentValues()
			throws DuplicateEntityFoundException,
				LocationReasonTermExistsAfterException,
				LocationReasonTermConflictException,
				DateRangeOutOfBoundsException {
		
		// Arrangements - place Blofeld in jail pending new charges with an
		// erroneous break in reasons before revocation
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Organization jail = this.organizationDelegate.create(
				"County Jail", "CJL", null);
		Country usa = this.countryDelegate
				.create("United States of America", "USA", true);
		State montana = this.stateDelegate
				.create("Montana", "MT", usa, true, true);
		City helena = this.cityDelegate
				.create("Helena", true, montana, usa);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address jailAddress = this.addressDelegate
				.findOrCreate("6000 6TH ST 6", null, null, null, mt59602);
		Date locationStartDate = this.parseDateText("01/01/2001");
		Date locationEndDate = this.parseDateText("03/03/2003");
		Location jailLocation = this.locationDelegate
				.create(jail, new DateRange(locationStartDate, locationEndDate),
						jailAddress);
		LocationTerm jailTerm = this.locationTermDelegate.create(
			blofeld, jailLocation, locationStartDate, locationEndDate, false);
		LocationReason pendingNewCharges
			= this.locationReasonDelegate.create(
					"Pending New Charges", (short) 1, true);
		Date revocationDate = this.parseDateText("02/02/2002");
		this.locationReasonTermDelegate.create(jailTerm,
				new DateRange(locationStartDate, revocationDate),
				pendingNewCharges);
		LocationReason revocation = this.locationReasonDelegate
				.create("Pending Revocation", (short) 1, true);
		Date erroneousRevocationDate
			= this.parseDateText("02/03/2002");
		LocationReasonTerm revocationReasonTerm
			= this.locationReasonTermDelegate.create(jailTerm,
					new DateRange(erroneousRevocationDate, locationEndDate),
					revocation);
		
		// Action - correct erroneous revocation date
		revocationReasonTerm = this.locationTermService
				.updateReasonTerm(revocationReasonTerm,
						new DateRange(revocationDate, locationEndDate),
						revocation);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", revocationDate)
			.addExpectedValue("dateRange.endDate", locationEndDate)
			.addExpectedValue("locationTerm", jailTerm)
			.addExpectedValue("reason", revocation)
			.performAssertions(revocationReasonTerm);
	}
	
	/* Helper methods. */
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
	
	// Asserts values are correct
	private void assertValues(final LocationReasonTerm locationReasonTerm, 
			final LocationTerm locationTerm, final DateRange dateRange, 
			final LocationReason reason) {
		assert locationTerm.equals(locationReasonTerm.getLocationTerm()) 
			: String.format("Wrong location term: %s found; %s expected", 
					locationReasonTerm.getLocationTerm().getId(),
					locationTerm.getId());
		assert DateRange.areEqual(dateRange, locationReasonTerm.getDateRange())
			: String.format("Wrong date range: %s found; %s expected", 
					locationReasonTerm.getDateRange().toString(),
					dateRange.toString());
		assert reason.equals(locationReasonTerm.getReason()) : String.format(
				"Wrong reason: %s found; %s expected", 
				locationReasonTerm.getReason().getName(), reason.getName());
	}
}