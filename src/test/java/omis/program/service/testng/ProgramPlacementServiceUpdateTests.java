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
package omis.program.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.AddressUnitDesignatorDelegate;
import omis.address.service.delegate.StreetSuffixDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.program.domain.Program;
import omis.program.domain.ProgramPlacement;
import omis.program.exception.ProgramPlacementConflictException;
import omis.program.exception.ProgramPlacementExistsAfterException;
import omis.program.exception.ProgramPlacementExistsBeforeException;
import omis.program.exception.ProgramPlacementExistsException;
import omis.program.service.ProgramPlacementService;
import omis.program.service.delegate.ProgramDelegate;
import omis.program.service.delegate.ProgramPlacementDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to test program placement service update.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 13, 2018)
 * @since OMIS 3.0
 */
@Test
public class ProgramPlacementServiceUpdateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	@Autowired
	@Qualifier("programDelegate")
	private ProgramDelegate programDelegate;
	
	@Autowired
	@Qualifier("programPlacementDelegate")
	private ProgramPlacementDelegate programPlacementDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("placementTermChangeReasonDelegate")
	private PlacementTermChangeReasonDelegate placementTermChangeReasonDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate 
		supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("locationTermDelegate")
	private LocationTermDelegate locationTermDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("streetSuffixDelegate")
	private StreetSuffixDelegate streetSuffixDelegate;
	
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
	

	/* Services. */

	@Autowired
	@Qualifier("programPlacementService")
	private ProgramPlacementService programPlacementService;

	/* Test methods. */

	/**
	 * Test update of program placement.
	 *
	 *
	 * @throws ProgramPlacementConflictException program placement conflict 
	 * @throws ProgramPlacementExistsBeforeException program placement exists
	 * @throws ProgramPlacementExistsAfterException program placement exists 
	 * after
	 * @throws OffenderNotUnderSupervisionException offender not under 
	 * supervision
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test
	public void testUpdate() 
			throws ProgramPlacementConflictException, 
			ProgramPlacementExistsBeforeException, 
			ProgramPlacementExistsAfterException, 
			OffenderNotUnderSupervisionException, 
			DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		SupervisoryOrganization supervisoryOrganization 
			= this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
					null);
	
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("SECURE", "SEC", true, (short) 1, true);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
	
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason 
			= this.placementTermChangeReasonDelegate.create(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				changeReason, changeReason, false);
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
		Location location = this.locationDelegate.create(
				supervisoryOrganization, dateRange, address);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, 
				location, startDate, endDate, false);
		Program program = this.programDelegate.create("Program");
		ProgramPlacement programPlacement =
				this.programPlacementDelegate.create(offender, dateRange, 
						program, placementTerm, locationTerm);
		Program  newProgram = this.programDelegate.create("NewProgram");
		
		// Action
		programPlacement = this.programPlacementService.update(
				programPlacement, dateRange, newProgram);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("program", newProgram)
			.performAssertions(programPlacement);
	}

	/**
	 * Test duplicate program placement exist.
	 *
	 *
	  * @throws ProgramPlacementConflictException program placement conflict 
	 * @throws ProgramPlacementExistsBeforeException program placement exists
	 * @throws ProgramPlacementExistsAfterException program placement exists 
	 * after
	 * @throws OffenderNotUnderSupervisionException offender not under 
	 * supervision
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {ProgramPlacementExistsException.class})
	public void testProgramPlacementExistsException() 
			throws ProgramPlacementConflictException, 
			ProgramPlacementExistsBeforeException, 
			ProgramPlacementExistsAfterException, 
			OffenderNotUnderSupervisionException, 
			DuplicateEntityFoundException {
		//Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		SupervisoryOrganization supervisoryOrganization 
			= this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
					null);
	
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("SECURE", "SEC", true, (short) 1, true);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
	
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason 
			= this.placementTermChangeReasonDelegate.create(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				changeReason, changeReason, false);
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
		Location location = this.locationDelegate.create(
				supervisoryOrganization, dateRange, address);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, 
				location, startDate, endDate, false);
		Program program = this.programDelegate.create("Program");	
		this.programPlacementDelegate.create(offender, dateRange, 
				program, placementTerm, locationTerm);	
		Date newStartDate = this.parseDateText("09/12/2001");
		Date newEndDate = this.parseDateText("12/03/2012");
		DateRange newDateRange = new DateRange(newStartDate, newEndDate);
		ProgramPlacement programPlacement =
				this.programPlacementDelegate.create(offender, newDateRange, 
						program, placementTerm, locationTerm);	
		
		// Action
		programPlacement = this.programPlacementService.update(
				programPlacement, dateRange, program);
	}

	/**
	 * Test conflict with program placement.
	 *
	 *
	 * @throws ProgramPlacementConflictException program placement conflict 
	 * @throws ProgramPlacementExistsBeforeException program placement exists
	 * @throws ProgramPlacementExistsAfterException program placement exists 
	 * after
	 * @throws OffenderNotUnderSupervisionException offender not under 
	 * supervision
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {ProgramPlacementConflictException.class})
	public void testProgramPlacementConflictException() 
			throws ProgramPlacementConflictException, 
			ProgramPlacementExistsBeforeException, 
			ProgramPlacementExistsAfterException, 
			OffenderNotUnderSupervisionException, 
			DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		SupervisoryOrganization supervisoryOrganization 
			= this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
					null);
	
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("SECURE", "SEC", true, (short) 1, true);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
	
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason 
			= this.placementTermChangeReasonDelegate.create(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				changeReason, changeReason, false);
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
		Location location = this.locationDelegate.create(
				supervisoryOrganization, dateRange, address);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, 
				location, startDate, endDate, false);
		Program program = this.programDelegate.create("Program");	
		this.programPlacementDelegate.create(offender, dateRange, 
				program, placementTerm, locationTerm);	
		Date programStartDate = startDate;
		Date programEndDate = this.parseDateText("01/15/2002");
		DateRange programDateRange = new DateRange(programStartDate, 
				programEndDate);
		this.programPlacementDelegate.create(offender, programDateRange, 
				program, placementTerm, locationTerm);
		programStartDate = programEndDate;
		programEndDate = this.parseDateText("07/06/2003");
		programDateRange = new DateRange(programStartDate, programEndDate);
		this.programPlacementDelegate.create(offender, programDateRange, 
				program, placementTerm, locationTerm);
		programStartDate = programEndDate;
		programEndDate = this.parseDateText("08/07/2004");
		programDateRange = new DateRange(programStartDate, programEndDate);
		Program newProgram = this.programDelegate.create("NewProgram");
		ProgramPlacement programPlacement = this.programPlacementDelegate
				.create(offender, dateRange, newProgram, 
						placementTerm, locationTerm);
		
		// Action
		this.programPlacementService.update(
				programPlacement, programDateRange, program);
	}

	/**
	 * Test program placement exist after.
	 *
	 *
	 * @throws ProgramPlacementConflictException program placement conflict 
	 * @throws ProgramPlacementExistsBeforeException program placement exists
	 * @throws ProgramPlacementExistsAfterException program placement exists 
	 * after
	 * @throws OffenderNotUnderSupervisionException offender not under 
	 * supervision
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {ProgramPlacementExistsAfterException.class})
	public void testProgramPlacementExistsAfterException() 
			throws ProgramPlacementConflictException, 
			ProgramPlacementExistsBeforeException, 
			ProgramPlacementExistsAfterException, 
			OffenderNotUnderSupervisionException, 
			DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		SupervisoryOrganization supervisoryOrganization 
			= this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
					null);
	
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("SECURE", "SEC", true, (short) 1, true);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = null;
		DateRange dateRange = new DateRange(startDate, endDate);
	
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason 
			= this.placementTermChangeReasonDelegate.create(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				changeReason, changeReason, false);
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
		Location location = this.locationDelegate.create(
				supervisoryOrganization, dateRange, address);
		LocationTerm locationTerm = this.locationTermDelegate.create(offender, 
				location, startDate, endDate, false);
		Program program = this.programDelegate.create("Program");	
		ProgramPlacement programPlacement = this.programPlacementDelegate
				.create(offender, dateRange, 
				program, placementTerm, locationTerm);	
		Date programStartDate = startDate;
		Date programEndDate = this.parseDateText("01/15/2002");
		DateRange programDateRange = new DateRange(programStartDate, 
				programEndDate);
		this.programPlacementDelegate.create(offender, programDateRange, 
				program, placementTerm, locationTerm);
		programStartDate = programEndDate;
		programEndDate = this.parseDateText("07/06/2003");
		programDateRange = new DateRange(programStartDate, programEndDate);
		this.programPlacementDelegate.create(offender, programDateRange, 
				program, placementTerm, locationTerm);
		programStartDate = programEndDate;
		programEndDate = this.parseDateText("08/07/2004");
		programDateRange = new DateRange(programStartDate, programEndDate);
		this.programPlacementDelegate
				.create(offender, programDateRange, program, 
						placementTerm, locationTerm);
		
		// Action
		programPlacement = this.programPlacementService.update(
				programPlacement, dateRange, program);
	}

	/**
	 * Test offender not under supervision.
	 *
	 *
	 * @throws ProgramPlacementConflictException program placement conflict 
	 * @throws ProgramPlacementExistsBeforeException program placement exists
	 * @throws ProgramPlacementExistsAfterException program placement exists 
	 * after
	 * @throws OffenderNotUnderSupervisionException offender not under 
	 * supervision
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testOffenderNotUnderSupervisionException() 
			throws ProgramPlacementConflictException, 
			ProgramPlacementExistsBeforeException, 
			ProgramPlacementExistsAfterException, 
			OffenderNotUnderSupervisionException, 
			DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
	
		PlacementTerm placementTerm = null;
		LocationTerm locationTerm = null;
		Program program = this.programDelegate.create("Program");
		ProgramPlacement placement = this.programPlacementService.create(
				offender, placementTerm, 
				locationTerm, dateRange, program);
		Program newProgram = this.programDelegate.create("NewProgram");
		
		// Action
		this.programPlacementService.update(placement, dateRange, newProgram);
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