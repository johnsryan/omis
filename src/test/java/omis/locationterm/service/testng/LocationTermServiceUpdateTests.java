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
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.exception.LocationTermLockedException;
import omis.locationterm.service.LocationTermService;
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
 * Location term service update tests.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceUpdateTests
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
	 * Test update of location term. 
	 * 
	 * @throws DuplicateEntityFoundException if location term exists
	 * @throws DateRangeOutOfBoundsException if location term is out of bounds
	 * @throws LocationTermConflictException if location term conflicts
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	public void testUpdateLocation()
			throws DuplicateEntityFoundException,
			LocationTermConflictException,
			DateRangeOutOfBoundsException,
			LocationTermExistsAfterException,
			LocationTermLockedException, 
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
		Address jailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization jail = this.organizationDelegate
				.create("Jail", "JL", null);
		Organization pnp = this.organizationDelegate
				.create("PnP", "PNP", null);
		Location jailLocation = this.locationDelegate.create(
				jail, null, jailAddress);
		Location pnpLocation = this.locationDelegate
				.create(pnp, null, jailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2016");
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
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, jailLocation, startDate, endDate, false);
		
		// Action
		locationTerm = this.locationTermService.update(locationTerm, pnpLocation,
				new DateRange(startDate, endDate));

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("location", pnpLocation)
			.addExpectedValue("dateRange", new DateRange(startDate, endDate))
			.performAssertions(locationTerm);
	}
	
	/**
	 * Tests that {@code LocationTermExistsException is thrown when an attempt
	 * is made to update a location term to be a duplicate of an existing
	 * location term.
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists 
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists 
	 * @throws LocationExistsException if location exists 
	 * @throws LocationTermExistsException if location term exists
	 * @throws OrganizationExistsException if organization exists
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermLockedException if location term is locked 
	 * @throws LocationTermExistsAfterException if location terms exist after
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist
	 */
	@Test(expectedExceptions = {LocationTermExistsException.class})
	public void testLocationTermExistsException()
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
				OrganizationExistsException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				LocationTermLockedException,
				OffenderNotUnderSupervisionException {
		
		// Arranges offender in prison and then hospital
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2014"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(offender, placementRange, secure);
		this.placementTermDelegate.create(offender, placementRange,
				null, secureTerm, null, null, false);
		Country country = this.countryDelegate
				.create("United States", "USA", true);
		State state = this.stateDelegate
				.create("Montana", "MT", country, true, true);
		City city = this.cityDelegate
				.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate
				.create(city, "59601", null, true);
		Address prisonAddress = this.addressDelegate
				.findOrCreate("1000 1000TH ST UNIT 1000", null, null, null,
						zipCode);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, null, prisonAddress);
		DateRange prisonRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2013"));
		try {
			this.locationTermDelegate
				.create(offender, prisonLocation,
						DateRange.getStartDate(prisonRange),
						DateRange.getEndDate(prisonRange),
						false);
		} catch (LocationTermExistsException e) {
			throw new AssertionError(e);
		}
		Organization hospital = this.organizationDelegate
				.create("Hospital", "HOSP", null);
		Address hospitalAddress = this.addressDelegate
				.findOrCreate("2000 2000TH ST 2000", null, null, null, zipCode);
		Location hospitalLocation = this.locationDelegate
				.create(hospital, null, hospitalAddress);
		DateRange hospitalRange = new DateRange(
				this.parseDateText("12/12/2013"),
				this.parseDateText("12/12/2014"));
		LocationTerm hospitalLocationTerm;
		try {
			hospitalLocationTerm = this.locationTermDelegate
				.create(offender, hospitalLocation,
						DateRange.getStartDate(hospitalRange),
						DateRange.getEndDate(hospitalRange),
						false);
		} catch (LocationTermExistsException e) {
			throw new AssertionError(e);	
		}
		
		// Action - updates hospital location term to be duplicate of prison
		// location term
		this.locationTermService.update(
				hospitalLocationTerm, prisonLocation, prisonRange);
	}
	
	/** 
	 * Tests whether business exception {@code LocationTermConflictException} is
	 * thrown. 
	 * 
	 * <p>Consider removing this as it may be a duplicate of
	 * {@code testUpdateEarlierLocationTermWithNullEndDateFails}.
	 * 
	 * @throws DuplicateEntityFoundException if location term exists
	 * @throws DateRangeOutOfBoundsException if location term is out of bounds
	 * @throws LocationTermConflictException if location term conflicts
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {LocationTermConflictException.class})
	public void testLocationTermConflictException()
			throws DuplicateEntityFoundException,
			LocationTermConflictException,
			DateRangeOutOfBoundsException,
			LocationTermExistsAfterException,
			LocationTermLockedException,
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
		Address jailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization jail = this.organizationDelegate
				.create("Jail", "JAIL", null);
		Organization pnp = this.organizationDelegate
				.create("PnP", "PNP", null);
		Location megaJailLoc = this.locationDelegate.create(
				jail, null, jailAddress);
		Location megaPnpLoc = this.locationDelegate
				.create(pnp, null, jailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2016");
		DateRange dateRange = new DateRange(startDate, 
				this.parseDateText("1/1/2018"));
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
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, megaJailLoc, startDate, endDate, false);
		
		Date conflictStartDate = endDate;
		Date conflictEndDate = this.parseDateText("1/1/2017");
		this.locationTermDelegate.create(offender, megaJailLoc, 
				conflictStartDate, conflictEndDate, false);
		
		endDate = this.parseDateText("1/1/2018");
		
		// Action
		locationTerm = this.locationTermService.update(locationTerm, megaPnpLoc,
				new DateRange(startDate, endDate));
	}
	
	/** 
	 * Tests whether business exception {@code LocationTermExistsAfterException}
	 * is thrown. 
	 * 
	 * @throws DuplicateEntityFoundException if location term exists
	 * @throws DateRangeOutOfBoundsException if location term is out of bounds
	 * @throws LocationTermConflictException if location term conflicts
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {LocationTermExistsAfterException.class})
	public void testLocationTermExistsAfterException()
			throws DuplicateEntityFoundException,
			LocationTermConflictException,
			DateRangeOutOfBoundsException,
			LocationTermExistsAfterException,
			LocationTermLockedException, 
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
		Address jailAddress = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT,
				mt59602);
		Organization jail = this.organizationDelegate
				.create("Jail", "JAIL", null);
		Organization pnp = this.organizationDelegate
				.create("PnP", "PNP", null);
		Location jailLocation = this.locationDelegate.create(
				jail, null, jailAddress);
		Location pnpLocation = this.locationDelegate
				.create(pnp, null, jailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2016");
		DateRange dateRange = new DateRange(startDate, 
				this.parseDateText("1/1/2018"));
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
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, jailLocation, startDate, endDate, false);
		
		Date conflictStartDate = endDate;
		Date conflictEndDate = this.parseDateText("1/1/2017");
		this.locationTermDelegate.create(offender, jailLocation, 
				conflictStartDate, conflictEndDate, false);
		
		// Action
		this.locationTermService.update(locationTerm, pnpLocation,
				new DateRange(startDate, null));
	}
	
	/**
	 * Tests prevention of update to locked location terms.
	 * 
	 * @throws DuplicateEntityFoundException if location term exists
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds 
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {LocationTermLockedException.class})
	public void testLocationTermLockedException()
			throws LocationTermLockedException,
					DuplicateEntityFoundException,
					LocationTermConflictException,
					DateRangeOutOfBoundsException,
					LocationTermExistsAfterException, 
					OffenderNotUnderSupervisionException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		Organization organization = this.organizationDelegate.create(
				"Lewis and Clark County Jail", "LCCJ", null);
		Country country = this.countryDelegate.create(
				"United States", "US", false);
		State state = this.stateDelegate.create(
				"Montana", "MT", country, true, true);
		City city = this.cityDelegate.create("Helena", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "59601", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1000", null, null, BuildingCategory.APARTMENT, zipCode);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("15/05/2011"), null), address);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, location, this.parseDateText("15/05/2011"),
						null, true);
		
		// Action
		this.locationTermService.update(locationTerm,
				locationTerm.getLocation(),
				new DateRange(locationTerm.getDateRange().getStartDate(),
						this.parseDateText("06/23/2012")));
	}
	
	/** 
	 * Tests whether business exception 
	 * {@code OffenderNotUnderSupervisionException} is thrown. 
	 * 
	 * @throws DuplicateEntityFoundException if location term exists
	 * @throws DateRangeOutOfBoundsException if location term is out of bounds
	 * @throws LocationTermConflictException if location term conflicts
	 * @throws LocationTermExistsAfterException if end date is null and other 
	 * location terms exist after the start date
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not under 
	 * supervision
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testOffenderNotUnderSupervisionException()
			throws DuplicateEntityFoundException,
			LocationTermConflictException,
			DateRangeOutOfBoundsException,
			LocationTermExistsAfterException,
			LocationTermLockedException,
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
		Organization jail = this.organizationDelegate
				.create("Jail", "JAIL", null);
		Organization pnp = this.organizationDelegate
				.create("PnP", "PNP", null);
		Location jailLocation = this.locationDelegate.create(
				jail, null, megaJailAddress);
		Location pnpLocation = this.locationDelegate
				.create(pnp, null, megaJailAddress);
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2016");
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, jailLocation, startDate, endDate, false);
		
		// Action
		locationTerm = this.locationTermService.update(
				locationTerm, pnpLocation, new DateRange(startDate, endDate));
	}
	
	/**
	 * Tests that the earliest of two location terms immediately following
	 * one another can be updated with its original values.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws LocationTermConflictException if original location term
	 * conflicts
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds
	 * @throws LocationTermExistsAfterException if location terms exist
	 * after original that conflict
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 */
	public void testUpdateEarlierLocationTermWithOriginalValues()
			throws DuplicateEntityFoundException,
				LocationTermConflictException,
				DateRangeOutOfBoundsException,
				LocationTermExistsAfterException,
				LocationTermLockedException,
				OffenderNotUnderSupervisionException {
		
		// Arrangements - place Blofeld in prison
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Country unitedStates = this.countryDelegate.create(
				"United States", "USA", true);
		State montana = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helenaMontana = this.cityDelegate.create(
				"Helena", true, montana, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate.create(
				helenaMontana, "59602", null, true);
		Address prisonAddress
			= this.addressDelegate.findOrCreate(
					"1000 1ST ST", null, null, null, mt59602);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		Location prisonLocation = this.locationDelegate
				.create(prison, new DateRange(), prisonAddress);
		Date prisonStartDate = this.parseDateText("02/02/2002");
		Date prisonEndDate = this.parseDateText("03/03/2003");
		DateRange prisonDateRange = new DateRange(
				prisonStartDate, prisonEndDate);
		LocationTerm prisonTerm = this.locationTermDelegate
				.create(offender, prisonLocation, prisonStartDate,
						prisonEndDate, false);
		SupervisoryOrganizationTerm prereleaseOrganizationTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, prisonDateRange, prison);
		CorrectionalStatus altSecure = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 1, true);
		CorrectionalStatusTerm correctionalStatusTerm
			= this.correctionalStatusTermDelegate
				.create(offender, prisonDateRange, altSecure);
		this.placementTermDelegate.create(offender, prisonDateRange,
				prereleaseOrganizationTerm, correctionalStatusTerm, null, null,
				true);
		
		// More arrangements - place Blofeld in a prerelease immediately after
		// Add placement term for location as offender must be supervised in
		// order to invoke update method for location term
		Date prereleaseStartDate = this.parseDateText("03/03/2003");
		Date prereleaseEndDate = this.parseDateText("04/04/2004");
		Organization prerelease
			= this.organizationDelegate.create("Prerelease", "PRE", null);
		Address prereleaseAddress = this.addressDelegate
				.findOrCreate("2000 2ND ST", null, null, null, mt59602);
		Location prereleaseLocation = this.locationDelegate
				.create(prerelease, new DateRange(), prereleaseAddress);
		this.locationTermDelegate.create(offender, prereleaseLocation,
				prereleaseStartDate, prereleaseEndDate, false);
		
		// Action - attempt to update prison term with original values
		this.locationTermService.update(prisonTerm, prisonLocation,
				new DateRange(prisonStartDate, prisonEndDate));
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", prisonStartDate)
			.addExpectedValue("dateRange.endDate", prisonEndDate)
			.addExpectedValue("location", prisonLocation)
			.addExpectedValue("offender", offender)
			.performAssertions(prisonTerm);
	}
	
	/**
	 * Tests that the earliest of two location terms immediately following
	 * one another cannot be updated with values conflicting with the later
	 * location term without throwing a {@code LocationTermConflictException}.
	 * 
	 * @throws LocationTermConflictException if update is prevented due to
	 * conflicting dates - asserted
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermLockedException if location term is locked 
	 * @throws LocationTermExistsAfterException if location terms exist after
	 * location term that is to be updated
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds 
	 */
	@Test(expectedExceptions = {LocationTermConflictException.class})
	public void testUpdateEarlierLocationTermWithConflictingEndDate()
			throws LocationTermConflictException,
				DuplicateEntityFoundException,
				DateRangeOutOfBoundsException,
				LocationTermExistsAfterException,
				LocationTermLockedException,
				OffenderNotUnderSupervisionException {
		
		// Arrangements - place Blofeld securely at a prison
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange placementDateRange = new DateRange(
				this.parseDateText("01/01/2011"),
				this.parseDateText("03/03/2013"));
		CorrectionalStatusTerm secureTerm
				= this.correctionalStatusTermDelegate
					.create(blofeld, placementDateRange, secure);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonSupervisoryTerm
				= this.supervisoryOrganizationTermDelegate
					.create(blofeld, placementDateRange, prison);
		this.placementTermDelegate.create(blofeld, placementDateRange,
				prisonSupervisoryTerm, secureTerm, null, null, false);
		
		// More arrangements - locate Blofeld in prison for the earlier
		// part of his prison placement
		Country unitedStates = this.countryDelegate.create(
				"United States", "USA", true);
		State montana = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helenaMontana = this.cityDelegate.create(
				"Helena", true, montana, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate.create(
				helenaMontana, "59602", null, true);
		Address prisonAddress = this.addressDelegate.findOrCreate(
				"1000 1ST ST", null, null, null, mt59602);
		Location prisonLocation = this.locationDelegate.create(
				prison, placementDateRange, prisonAddress);
		Date prisonLocationEndDate = this.parseDateText("02/02/2012");
		LocationTerm prisonTerm = this.locationTermDelegate
				.create(blofeld, prisonLocation,
						DateRange.getStartDate(placementDateRange),
						prisonLocationEndDate, false);
		
		// Even more arrangements - locate Blofeld in jail immediately after
		// prison term - this might represent going out to court
		Address jailAddress = this.addressDelegate
				.findOrCreate("2000 2ND ST", null, null, null, mt59602);
		Organization jail = this.organizationDelegate.create(
				"Jail", "JAIL", null);
		Location jailLocation = this.locationDelegate
				.create(jail, new DateRange(prisonLocationEndDate,
						DateRange.getEndDate(placementDateRange)),
						jailAddress);
		this.locationTermDelegate.create(
				blofeld, jailLocation, prisonLocationEndDate,
				DateRange.getEndDate(placementDateRange), false);
		
		// Action - update prison location term with an end date that conflicts
		// with prison term
		Date conflictingPrisonEndDate = this.parseDateText("02/03/2012");
		this.locationTermService.update(prisonTerm, prisonLocation,
				new DateRange(
						DateRange.getStartDate(placementDateRange),
						conflictingPrisonEndDate));
	}
	
	/**
	 * Tests that the earliest of two location terms immediately following
	 * one another cannot be updated with a {@code null} end date which
	 * would conflict with a future location term without throwing
	 * a {@code LocationTermConflictException}.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws OffenderNotUnderSupervisionException if offender is not under
	 * supervision
	 * @throws LocationTermLockedException if location term is locked 
	 * @throws LocationTermExistsAfterException if location terms exist after
	 * location term that is to be updated - asserted
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds 
	 */
	@Test(expectedExceptions = {LocationTermExistsAfterException.class})
	public void testUpdateEarlierLocationTermWithNullEndDateFails()
			throws LocationTermConflictException,
				DuplicateEntityFoundException,
				DateRangeOutOfBoundsException,
				LocationTermExistsAfterException,
				LocationTermLockedException,
				OffenderNotUnderSupervisionException {
		
		// Arrangements - place Blofeld securely at a prison
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange placementDateRange = new DateRange(
				this.parseDateText("01/01/2011"),
				this.parseDateText("03/03/2013"));
		CorrectionalStatusTerm secureTerm
				= this.correctionalStatusTermDelegate
					.create(blofeld, placementDateRange, secure);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonSupervisoryTerm
				= this.supervisoryOrganizationTermDelegate
					.create(blofeld, placementDateRange, prison);
		this.placementTermDelegate.create(blofeld, placementDateRange,
				prisonSupervisoryTerm, secureTerm, null, null, false);
		
		// More arrangements - locate Blofeld in prison for the earlier
		// part of his prison placement
		Country unitedStates = this.countryDelegate.create(
				"United States", "USA", true);
		State montana = this.stateDelegate.create(
				"Montana", "MT", unitedStates, true, true);
		City helenaMontana = this.cityDelegate.create(
				"Helena", true, montana, unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate.create(
				helenaMontana, "59602", null, true);
		Address prisonAddress = this.addressDelegate.findOrCreate(
				"1000 1ST ST", null, null, null, mt59602);
		Location prisonLocation = this.locationDelegate.create(
				prison, placementDateRange, prisonAddress);
		Date prisonLocationEndDate = this.parseDateText("02/02/2012");
		LocationTerm prisonTerm = this.locationTermDelegate
				.create(blofeld, prisonLocation,
						DateRange.getStartDate(placementDateRange),
						prisonLocationEndDate, false);
		
		// Even more arrangements - locate Blofeld in jail immediately after
		// prison term - this might represent going out to court
		Address jailAddress = this.addressDelegate
				.findOrCreate("2000 2ND ST", null, null, null, mt59602);
		Organization jail = this.organizationDelegate.create(
				"Jail", "JAIL", null);
		Location jailLocation = this.locationDelegate
				.create(jail, new DateRange(prisonLocationEndDate,
						DateRange.getEndDate(placementDateRange)),
						jailAddress);
		this.locationTermDelegate.create(
				blofeld, jailLocation, prisonLocationEndDate,
				DateRange.getEndDate(placementDateRange), false);
		
		// Action - update prison location term with a null end date that
		// conflicts with the future jail location term
		this.locationTermService.update(prisonTerm, prisonLocation,
				new DateRange(
						DateRange.getStartDate(placementDateRange),
						null));
	}
	
	/**
	 * Tests that {@code IllegalArgumentException) is thrown when start date
	 * and end date are on update.
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
	 * @throws LocationTermLockedException if location term is locked
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
				OffenderNotUnderSupervisionException,
				LocationTermLockedException {
		
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
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, hospitalLocation,
						this.parseDateText("12/12/2017"), null, false);
		
		// Action - attempts to update location term to have an equal start
		// and end date
		this.locationTermService.update(locationTerm, hospitalLocation,
				new DateRange(
						this.parseDateText("12/12/2017"),
						this.parseDateText("12/12/2017")));	
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