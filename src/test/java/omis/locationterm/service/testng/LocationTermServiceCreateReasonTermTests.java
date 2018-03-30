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
 * Location term servce reason term creation tests.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceCreateReasonTermTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locatioDelegate;
	
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
	 * Tests creation of location reason term.
	 * 
	 * @throws DuplicateEntityFoundException if location reason term exists
	 * @throws DateRangeOutOfBoundsException if date range exists out side of 
	 * location term date range
	 * @throws LocationReasonTermConflictException if overlapping location 
	 * reason terms
	 * @throws LocationReasonTermExistsAfterException date range is not ended 
	 * and exists location reason terms exist after the start date
	 */
	public void testCreateReasonTerm()
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
		Location prisonLocation = this.locatioDelegate
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
		
		// Action
		LocationReasonTerm locationReasonTerm = this.locationTermService
				.createReasonTerm(locationTerm, dateRange, reason);
		
		// Assert
		assertValues(locationReasonTerm, locationTerm, dateRange, reason);
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
		Location prisonLocation = this.locatioDelegate
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
		this.locationReasonTermDelegate.create(locationTerm, dateRange, reason);
		
		// Action
		this.locationTermService.createReasonTerm(locationTerm, dateRange,
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
		Location prisonLocation = this.locatioDelegate
				.create(prison, new DateRange(
						this.parseDateText("08/23/2016"), null),
						prisonAddress);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, prisonLocation,
						this.parseDateText("09/23/2016"), null, false);
		LocationReason reason = this.locationReasonDelegate
				.create("Initial Commit", (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("09/23/2015"), 
				null);
		
		// Action
		this.locationTermService.createReasonTerm(locationTerm, dateRange,
				reason);
	}
	
	/**
	 * Tests that multiple reasons are allowed providing date ranges
	 * do not overlap.
	 * 
	 * @throws DateRangeOutOfBoundsException if date range is out of bounds
	 * @throws LocationReasonTermExistsAfterException if location reason term
	 * exists after
	 * @throws LocationReasonTermConflictException if location reason term
	 * exists before
	 * @throws DuplicateEntityFoundException if any entities exist 
	 */
	public void testMultipleReasonsAllowed()
			throws LocationReasonTermConflictException,
				LocationReasonTermExistsAfterException,
				DateRangeOutOfBoundsException,
				DuplicateEntityFoundException {
		
		// Arrangements - locate Blofeld in jail - first pending new charges
		Offender blofeld = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		Organization jail = this.organizationDelegate
				.create("Jail", "JL", null);
		Country usa = this.countryDelegate.create(
				"United States of America", "USA", true);
		State mt = this.stateDelegate.create(
				"Montana", "MT", usa, true, true);
		City helena = this.cityDelegate.create("Helena", true, mt, usa);
		ZipCode mt59601 = this.zipCodeDelegate.create(
				helena, "59601", null, true);
		Address address = this.addressDelegate.findOrCreate(
				"1111 1ST ST W", null, null, BuildingCategory.CONDO, mt59601);
		Location location = this.locatioDelegate
				.create(jail, new DateRange(
						this.parseDateText("12/12/2012"), null), address);
		Date locationStartDate = this.parseDateText("12/12/2012");
		Date locationEndDate = this.parseDateText("14/14/2014");
		LocationTerm locationTerm = this.locationTermDelegate.create(
				blofeld, location, locationStartDate, locationEndDate, false);
		LocationReason pendingNewCharges = this.locationReasonDelegate
				.create("Pending New Charges", (short) 1, true);
		Date revokationDate = this.parseDateText("13/01/2013");
		this.locationReasonTermDelegate.create(locationTerm,
					new DateRange(locationStartDate, revokationDate),
					pendingNewCharges);
		
		// Action - hold Blofeld in jail while pending revocation
		LocationReason pendingRevocation = this.locationReasonDelegate
				.create("Pending Revocation", (short) 1, true);
		LocationReasonTerm pendingRevocationReasonTerm
			= this.locationTermService.createReasonTerm(locationTerm,
					new DateRange(revokationDate, locationEndDate),
					pendingRevocation);
		
		// Asserts pending revokation values
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", revokationDate)
			.addExpectedValue("dateRange.endDate", locationEndDate)
			.addExpectedValue("reason", pendingRevocation)
			.performAssertions(pendingRevocationReasonTerm);
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
		Location prisonLocation = this.locatioDelegate
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
				this.parseDateText("01/01/2018"));
		
		// Action
		this.locationTermService.createReasonTerm(locationTerm, dateRange,
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
		Location prisonLocation = this.locatioDelegate
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
		
		dateRange = new DateRange(this.parseDateText("09/23/2016"), null);
		
		// Action
		this.locationTermService.createReasonTerm(locationTerm, dateRange,
				reason);
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