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
import omis.address.service.delegate.AddressUnitDesignatorDelegate;
import omis.address.service.delegate.StreetSuffixDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DateRangeOutOfBoundsException;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationReason;
import omis.locationterm.domain.LocationReasonTerm;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationReasonExistsException;
import omis.locationterm.exception.LocationReasonTermExistsException;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.service.delegate.LocationReasonDelegate;
import omis.locationterm.service.delegate.LocationReasonTermDelegate;
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
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusExistsException;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationExistsException;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Location term service create tests.
 *
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceCreateTests
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
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate 
			supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("locationReasonTermDelegate")
	private LocationReasonTermDelegate locationReasonTermDelegate;
	
	@Autowired
	@Qualifier("locationReasonDelegate")
	private LocationReasonDelegate locationReasonDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Tests. */
	
	/**
	 * Test creation of location term. 
	 * 
	 * @throws DuplicateEntityFoundException if location term exists
	 * @throws DateRangeOutOfBoundsException if location term is out of bounds
	 * @throws LocationTermConflictException if location term conflicts
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	public void testCreation()
			throws DuplicateEntityFoundException,
			LocationTermConflictException, DateRangeOutOfBoundsException, 
			LocationTermExistsAfterException, 
			OffenderNotUnderSupervisionException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helena = this.cityDelegate.create("Helena",
				true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address megaJailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization megaJailOrg = this.organizationDelegate
				.create("Mega Jail", "MEGA", null);
		Location megaJailLoc = this.locationDelegate.create(
				megaJailOrg, null, megaJailAddress);
		DateRange dateRange = new DateRange(this.parseDateText("09/12/2001"), 
				this.parseDateText("12/03/2016"));
		SupervisoryOrganization supervisoryOrganization 
				= this.supervisoryOrganizationDelegate.create("SO", "SO", null);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
				= this.supervisoryOrganizationTermDelegate.create(offender,
						dateRange, supervisoryOrganization);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		CorrectionalStatusTerm correctionalStatusTerm 
				= this.correctionalStatusTermDelegate.create(offender, 
						dateRange, correctionalStatus );
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, null, null, 
				false);

		// Action
		LocationTerm locationTerm = this.locationTermService.create(offender, 
				megaJailLoc, dateRange);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("location", megaJailLoc)
			.addExpectedValue("dateRange", dateRange)
			.performAssertions(locationTerm);
	}
	
	/**
	 * Tests that attempts to create duplicate location terms result in a
	 * {@code LocationTermExistsException} being thrown.
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CountryExistsException if country exists 
	 * @throws StateExistsException if State exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws CityExistsException if city exists
	 * @throws LocationExistsException if location exists - asserted
	 * @throws LocationTermExistsException if location term exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermExistsAfterException if location term exists
	 * after
	 * @throws LocationTermConflictException if conflicting location terms
	 * exists
	 */
	@Test(expectedExceptions = {LocationTermExistsException.class})
	public void testLocationTermExistsException()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				SupervisoryOrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				ZipCodeExistsException,
				CityExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
	
		// Arrangements - places offender, locates in prison
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, dateRange, secure);
		this.placementTermDelegate.create(
				offender, dateRange, null, secureTerm, null, null, false);
		SupervisoryOrganization prisonOrganization
				= this.supervisoryOrganizationDelegate
					.create("Prison", "PRSN", null);
		Country country = this.countryDelegate
				.create("United States", "USA", true);
		State state = this.stateDelegate
				.create("Montana", "MT", country, true, true);
		City city = this.cityDelegate.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate
				.create(city, "59602", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1000 1000TH ST #1000", null, null, null, zipCode);
		Location prisonLocation = this.locationDelegate
				.create(prisonOrganization, dateRange, address);
		try {
			this.locationTermDelegate.create(offender, prisonLocation,
				DateRange.getStartDate(dateRange), null, false);
		} catch (LocationTermExistsException e) {
			throw new AssertionError(e);
		}
		
		// Action - attempts to create duplicate location term
		this.locationTermService
			.create(offender, prisonLocation, dateRange);
	}
	
	/**
	 * Tests that location term on start date is ended with start date of
	 * new location term.
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CountryExistsException if country exists
	 * @throws LocationExistsException if location exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization term exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws OrganizationExistsException if organization exists
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist
	 * @throws LocationTermExistsAfterException if location term exists
	 * @throws OffenderNotUnderSupervisionException if offender is
	 * not under supervision
	 */
	public void testAdjustStartLocationTerm()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				CountryExistsException,
				LocationExistsException,
				SupervisoryOrganizationExistsException,
				StateExistsException, 
				CityExistsException,
				ZipCodeExistsException,
				LocationTermExistsException,
				OrganizationExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arrangements - places offender, locates in prison indefinitely
		// (with null end date)
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"),
				null);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(offender, dateRange, secure);
		this.placementTermDelegate
			.create(offender, dateRange, null, secureTerm, null, null, false);
		Country country = this.countryDelegate
				.create("United States", "USA", true);
		State state = this.stateDelegate
				.create("Washington", "WA", country, true, true);
		City city = this.cityDelegate
				.create("Spokane", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate
				.create(city, "99201", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1000 1000ST UNIT 1000", null, null, null, zipCode);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate
				.create("State Prison", "SP", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, address);
		LocationTerm prisonTerm = this.locationTermDelegate.create(
				offender, prisonLocation, this.parseDateText("12/12/2012"),
				null, false);
		
		// Action - creates location term at hospital starting after prison term
		Address hospitalAddress = this.addressDelegate
				.findOrCreate("2000 2000TH ST 2000", null, null, null, zipCode);
		Organization hospital = this.organizationDelegate
				.create("City Hospital", "CHOSP", null);
		Location hospitalLocation = this.locationDelegate
				.create(hospital, dateRange, hospitalAddress);
		DateRange hospitalDateRange = new DateRange(
				this.parseDateText("12/12/2013"), null);
		this.locationTermService.create(offender, hospitalLocation,
				hospitalDateRange);
		
		// Asserts that original, prison term was ended on start date of new,
		// hospital location
		Date prisonEndDate = prisonTerm.getDateRange().getEndDate();
		assert prisonEndDate.equals(
				hospitalDateRange.getStartDate())
			: String.format("Wrong end date - %s expected; %s found",
					prisonEndDate, hospitalDateRange.getStartDate());
	}
	
	/**
	 * Tests that location term on end date is started with end date of new
	 * location term.
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status term
	 * exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException  if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws OrganizationExistsException if organization exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermExistsAfterException if location term exists after 
	 * @throws LocationTermConflictException if conflicting location term exists
	 */
	public void testAdjustEndLocationTerm()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				OrganizationExistsException,
				LocationExistsException,
				LocationTermExistsException,
				SupervisoryOrganizationExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arranges offender in hospital
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(offender, placementRange, secure);
		this.placementTermDelegate.create(
				offender, placementRange, null, secureTerm, null, null, false);
		Country country = this.countryDelegate
				.create("United States", "US", true);
		State state = this.stateDelegate
				.create("Montana", "MT", country, true, true);
		City city = this.cityDelegate
				.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate
				.create(city, "59601", null, true);
		Address address = this.addressDelegate
				.findOrCreate("1000 100TH ST 1000", null, null, null, zipCode);
		Organization hospital = this.organizationDelegate
				.create("Hospital", "HOSP", null);
		DateRange hospitalRange = new DateRange(
				this.parseDateText("12/12/2013"), null);
		Location hospitalLocation = this.locationDelegate
				.create(hospital, null, address);
		this.locationTermDelegate.create(
				offender, hospitalLocation,
				DateRange.getStartDate(hospitalRange),
				DateRange.getEndDate(hospitalRange), false);
		
		// Action - creates location term in prison starting before hospital
		// term but finishing after hospital term starts
		Address prisonAddress = this.addressDelegate
				.findOrCreate("2000 1000TH ST 3000", null, null, null, zipCode);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("State Prison", "SP", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, prisonAddress);
		DateRange prisonRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2014"));
		this.locationTermService.create(offender, prisonLocation, prisonRange);
		
		// Asserts that start date of hospital term is end date of prison term
		LocationTerm locationTermOnPrisonRangeEndDate
			= this.locationTermDelegate
				.findByOffenderOnDate(offender,
						DateRange.getEndDate(prisonRange));
		assert locationTermOnPrisonRangeEndDate.getLocation()
				.equals(hospitalLocation)
			: "Wrong location term found";
		assert locationTermOnPrisonRangeEndDate.getDateRange().getStartDate()
				.equals(prisonRange.getEndDate())
			: String.format("Wrong start date - %s expected; %s found",
					prisonRange.getEndDate(),
					locationTermOnPrisonRangeEndDate.getDateRange()
						.getStartDate());
	}

	/**
	 * Tests that location term on end date is started with end date of new
	 * location term and location term on start date is ended with start date
	 * of new location term.
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status term
	 * exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException  if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws OrganizationExistsException if organization exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermExistsAfterException if location term exists after 
	 * @throws LocationTermConflictException if conflicting location term exists
	 */
	public void testAdjustStartAndEndLocationTerm()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				OrganizationExistsException,
				LocationExistsException,
				LocationTermExistsException,
				SupervisoryOrganizationExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arranges offender in hospital
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2010"), null);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(offender, placementRange, secure);
		this.placementTermDelegate.create(
				offender, placementRange, null, secureTerm, null, null, false);
		Country country = this.countryDelegate
				.create("United States", "US", true);
		State state = this.stateDelegate
				.create("Montana", "MT", country, true, true);
		City city = this.cityDelegate
				.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate
				.create(city, "59601", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("2000 1000TH ST 3000", null, null, null, zipCode);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("State Prison", "SP", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, prisonAddress);
		DateRange firstPrisonRange = new DateRange(
				this.parseDateText("12/12/2010"),
				this.parseDateText("12/12/2012"));
		this.locationTermDelegate.create(
				offender, prisonLocation,
				DateRange.getStartDate(firstPrisonRange),
				DateRange.getEndDate(firstPrisonRange), false);
		Address address = this.addressDelegate
				.findOrCreate("1000 100TH ST 1000", null, null, null, zipCode);
		Organization hospital = this.organizationDelegate
				.create("Hospital", "HOSP", null);
		DateRange hospitalRange = new DateRange(
				this.parseDateText("12/12/2013"), null);
		Location hospitalLocation = this.locationDelegate
				.create(hospital, null, address);
		this.locationTermDelegate.create(
				offender, hospitalLocation,
				DateRange.getStartDate(hospitalRange),
				DateRange.getEndDate(hospitalRange), false);
		
		// Action - creates location term in prison starting before hospital
		// term but finishing after hospital term starts
		DateRange secondPrisonRange = new DateRange(
				this.parseDateText("12/12/2011"),
				this.parseDateText("12/12/2014"));
		this.locationTermService.create(
				offender, prisonLocation, secondPrisonRange);
		
		// Asserts that start date of hospital term is end date of prison term
		LocationTerm locationTermOnFirstPrisonRangeStartDate
			= this.locationTermDelegate
				.findByOffenderOnDate(offender,
						DateRange.getStartDate(firstPrisonRange));
		assert locationTermOnFirstPrisonRangeStartDate.getLocation()
				.equals(prisonLocation)
			: "Wrong location term found";
		assert locationTermOnFirstPrisonRangeStartDate.getDateRange()
				.getEndDate().equals(secondPrisonRange.getStartDate())
			: String.format("Wrong end date - %s expected; %s found", 
					secondPrisonRange.getStartDate(),
				locationTermOnFirstPrisonRangeStartDate.getDateRange()
					.getEndDate());
		LocationTerm locationTermOnSecondPrisonRangeEndDate
			= this.locationTermDelegate
				.findByOffenderOnDate(offender,
						DateRange.getEndDate(secondPrisonRange));
		assert locationTermOnSecondPrisonRangeEndDate.getLocation()
				.equals(hospitalLocation)
			: "Wrong location term found";
		assert locationTermOnSecondPrisonRangeEndDate.getDateRange()
				.getStartDate().equals(secondPrisonRange.getEndDate())
			: String.format("Wrong start date - %s expected; %s found",
					secondPrisonRange.getEndDate(),
					locationTermOnSecondPrisonRangeEndDate.getDateRange()
						.getStartDate());
	}
	
	/**
	 * Tests if business exception {@code LocationTermConflictException} is 
	 * thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location term already exists
	 * @throws LocationTermConflictException if location terms conflict
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {LocationTermConflictException.class})
	public void testLocationTermConflictException() 
			throws DuplicateEntityFoundException, LocationTermConflictException, 
			LocationTermExistsAfterException, 
			OffenderNotUnderSupervisionException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helena = this.cityDelegate.create("Helena",
				true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address megaJailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization megaJailOrg = this.organizationDelegate
				.create("Mega Jail", "MEGA", null);
		Location megaJailLoc = this.locationDelegate.create(
				megaJailOrg, null, megaJailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, 
				this.parseDateText("1/1/2015"));
		SupervisoryOrganization supervisoryOrganization 
				= this.supervisoryOrganizationDelegate.create("SO", "SO", null);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
				= this.supervisoryOrganizationTermDelegate.create(offender,
						dateRange, supervisoryOrganization);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		CorrectionalStatusTerm correctionalStatusTerm 
				= this.correctionalStatusTermDelegate.create(offender, 
						dateRange, correctionalStatus );
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, null, null, 
				false);
		this.locationTermDelegate.create(offender, megaJailLoc, startDate, 
				endDate, false);
		
		startDate = endDate;
		endDate = this.parseDateText("12/14/2013");
		this.locationTermDelegate.create(offender, megaJailLoc, startDate, 
				endDate, false);
		
		startDate = endDate;
		endDate = null;
		this.locationTermDelegate.create(offender, megaJailLoc, startDate, 
				endDate, false);

		startDate = this.parseDateText("10/10/2010");
		endDate = this.parseDateText("1/1/2015");
		
		// Action
		this.locationTermService.create(offender, megaJailLoc, 
				new DateRange(startDate, endDate));
	}
	
	/**
	 * Tests whether the business exception 
	 * {@code LocationTermExistsAfterException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location term already exists
	 * @throws LocationTermConflictException if location terms conflict
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {LocationTermExistsAfterException.class})
	public void testLocationTermExistsAfterException() 
			throws DuplicateEntityFoundException, LocationTermConflictException, 
			LocationTermExistsAfterException, 
			OffenderNotUnderSupervisionException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helena = this.cityDelegate.create("Helena",
				true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address megaJailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				 mt59602);
		Organization megaJailOrg = this.organizationDelegate
				.create("Mega Jail", "MEGA", null);
		Location megaJailLoc = this.locationDelegate.create(
				megaJailOrg, null, megaJailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, 
				this.parseDateText("12/14/2013"));
		SupervisoryOrganization supervisoryOrganization 
				= this.supervisoryOrganizationDelegate.create("SO", "SO", null);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
				= this.supervisoryOrganizationTermDelegate.create(offender,
						dateRange, supervisoryOrganization);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		CorrectionalStatusTerm correctionalStatusTerm 
				= this.correctionalStatusTermDelegate.create(offender, 
						dateRange, correctionalStatus );
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, null, null, 
				false);
		this.locationTermDelegate.create(offender, megaJailLoc, startDate, 
				endDate, false);
		
		startDate = endDate;
		endDate = this.parseDateText("12/14/2013");
		this.locationTermDelegate.create(offender, megaJailLoc, startDate, 
				endDate, false);

		startDate = this.parseDateText("10/10/2010");
		endDate = null;
		
		// Action
		this.locationTermService.create(offender, megaJailLoc, 
				new DateRange(startDate, endDate));
	}
	
	/**
	 * Tests if business exception {@code OffenderNotUnderSupervisionException} 
	 * is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if location term already exists
	 * @throws LocationTermConflictException if location terms conflict
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testOffenderNotUnderSupervisionException() 
			throws DuplicateEntityFoundException, LocationTermConflictException, 
			LocationTermExistsAfterException, 
			OffenderNotUnderSupervisionException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", null, null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "US", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helena = this.cityDelegate.create("Helena",
				true, mt, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate
				.create(helena, "59602", null, true);
		Address megaJailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization megaJailOrg = this.organizationDelegate
				.create("Mega Jail", "MEGA", null);
		Location megaJailLoc = this.locationDelegate.create(
				megaJailOrg, null, megaJailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		
		// Action
		this.locationTermService.create(offender, megaJailLoc, 
				new DateRange(startDate, endDate));
	}
	
	/**
	 * Tests that {@code IllegalArgumentException) is thrown when start date
	 * and end date are equal on create.
	 * 
	 * @throws IllegalArgumentException if start date and end date are equal
	 * - asserted
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists 
	 * @throws OrganizationExistsException if organization exists
	 * @throws CountryExistsException if country exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermExistsAfterException if location term exists after
	 * @throws LocationTermConflictException if conflicting location terms
	 * exists
	 * @throws LocationTermExistsException if location term exists 
	 */
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testIllegalArgumentExceptionWhenStartDateEqualsEndDate()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				OrganizationExistsException,
				CountryExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arranges offender on secure placement
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, placementRange, secure);
		this.placementTermDelegate.create(
				offender, placementRange, null, secureTerm, null, null, false);
		
		// Action - attempts to create location term where start date equals
		// end date
		Organization hospital = this.organizationDelegate
				.create("Hospital", "HOSP", null);
		Country country = this.countryDelegate.create(
				"United States", "USA", true);
		City city = this.cityDelegate.create(
				"Washington DC", true, null, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "20001", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1000 1000TH ST 1000", null, null, null, zipCode);
		Location hospitalLocation = this.locationDelegate
				.create(hospital, null, address);
		this.locationTermService.create(offender, hospitalLocation,
				new DateRange(
						this.parseDateText("12/12/2017"),
						this.parseDateText("12/12/2017")));	
	}
	
	/**
	 * Tests that reason term for existing location term is automatically
	 * end date with that of start date of new location term.
	 * 
	 * @throws CorrectionalStatusExistsException if correctional status term
	 * exists 
	 * @throws CorrectionalStatusTermExistsException if correctional status term
	 * exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists 
	 * @throws LocationExistsException if location exists 
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationReasonExistsException if location reason exists
	 * @throws LocationReasonTermExistsException if location reason term
	 * exists
	 * @throws OrganizationExistsException if organization exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermExistsAfterException if location term exists
	 * after new location term 
	 * @throws LocationTermConflictException if conflicting location terms
	 * exists with new location term 
	 */
	public void testReasonTermAutoEndDate()
			throws CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				SupervisoryOrganizationExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationReasonExistsException,
				LocationReasonTermExistsException,
				OrganizationExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arrangements - places offender in prison with a reason of prison
		// placement
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		DateRange prisonPlacementRange = new DateRange(
				this.parseDateText("12/12/2011"), null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(offender, prisonPlacementRange, secure);
		this.placementTermDelegate.create(
				offender, prisonPlacementRange, null, secureTerm, null, null,
				false);
		Country country = this.countryDelegate.create(
				"United States", "US", true);
		State state = this.stateDelegate.create(
				"Montana", "MT", country, true, true);
		City city = this.cityDelegate.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "59601", null, true);
		Address prisonAddress = this.addressDelegate.findOrCreate(
				"1000 1000TH ST 1000", null, null, null, zipCode);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, prisonAddress);
		LocationTerm prisonLocationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						DateRange.getStartDate(prisonPlacementRange),
						DateRange.getEndDate(prisonPlacementRange),
						false);
		LocationReason prisonPlacementReason = this.locationReasonDelegate
				.create("Prison Placement", (short) 1, true);
		this.locationReasonTermDelegate.create(prisonLocationTerm,
					prisonPlacementRange, prisonPlacementReason);
		
		// Action - transfers offender to hospital
		Address hospitalAddress = this.addressDelegate.findOrCreate(
				"2000 2000TH DR 2000", null, null, null, zipCode);
		Organization hospital = this.organizationDelegate.create(
				"Hospital","HOSP", null);
		Location hospitalLocation = this.locationDelegate.create(
				hospital, null, hospitalAddress);
		DateRange hospitalRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		this.locationTermService.create(
				offender, hospitalLocation, hospitalRange);
		
		// Asserts that reason term is ended on day of hospital transfer
		LocationReasonTerm prisonPlacementReasonTerm
			= this.locationReasonTermDelegate
				.findForLocationTermOnDate(prisonLocationTerm,
						DateRange.getStartDate(prisonPlacementRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(prisonPlacementRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getStartDate(hospitalRange))
			.addExpectedValue("reason", prisonPlacementReason)
			.performAssertions(prisonPlacementReasonTerm);
	}
	
	/**
	 * Tests that when a new location term is created starting before but ending
	 * during an existing location term with a reason term, the reason term
	 * start date is adjusted to that of the end date of the new location term.
	 * 
	 * @throws OrganizationExistsException if organization exists
	 * @throws CountryExistsException if country exist
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationReasonExistsException if location reason exists
	 * @throws LocationReasonTermExistsException if location reason term exists
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermExistsAfterException if location terms exist after 
	 * @throws LocationTermConflictException if conflicting location terms exist
	 */
	public void testReasonTermAutoStartDate()
			throws OrganizationExistsException,
				CountryExistsException,
				StateExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationReasonExistsException,
				LocationReasonTermExistsException,
				CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException,
				SupervisoryOrganizationExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arranges offender in hospital
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Grant", "Donald", null, null);
		Organization hospital = this.organizationDelegate.create(
				"Hospital", "HOSP", null);
		Country country = this.countryDelegate.create(
				"United States", "US", true);
		State state = this.stateDelegate.create(
				"North Dakota", "ND", country, true, true);
		City city = this.cityDelegate.create("Minot", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "58701", null, true);
		Address hospitalAddress = this.addressDelegate.findOrCreate(
				"3000 3000TH RD", null, null, null, zipCode);
		Location hospitalLocation = this.locationDelegate.create(
				hospital, null, hospitalAddress);
		DateRange hospitalRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2014"));
		LocationTerm hospitalLocationTerm = this.locationTermDelegate.create(
				offender, hospitalLocation,
				DateRange.getStartDate(hospitalRange),
				DateRange.getEndDate(hospitalRange), false);
		LocationReason medicalAttentionReason = this.locationReasonDelegate
				.create("Medical Attention", (short) 1, true);
		this.locationReasonTermDelegate.create(
				hospitalLocationTerm, hospitalRange, medicalAttentionReason);
		
		// Action - starts a prison term before the hospital stay but ending
		// during
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2010"),
				DateRange.getEndDate(hospitalRange));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, placementRange, secure);
		this.placementTermDelegate.create(
				offender, placementRange, null, secureTerm, null, null, false);
		DateRange prisonRange = new DateRange(
				DateRange.getStartDate(placementRange),
				this.parseDateText("12/12/2013"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Address prisonAddress = this.addressDelegate.findOrCreate(
				"4000 4000 AVE", null, null, null, zipCode);
		Location prisonLocation = this.locationDelegate.create(
				prison, null, prisonAddress);
		this.locationTermService.create(offender, prisonLocation, prisonRange);
		
		// Asserts that start date of medical attention reason term is adjusted
		// to end date of prison term
		LocationReasonTerm medicalAttentionReasonTerm
			= this.locationReasonTermDelegate
				.findForLocationTermOnDate(hospitalLocationTerm,
						DateRange.getEndDate(prisonRange));
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getEndDate(prisonRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(hospitalRange))
			.addExpectedValue("reason", medicalAttentionReason)
			.performAssertions(medicalAttentionReasonTerm);
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