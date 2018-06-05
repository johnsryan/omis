package omis.travelpermit.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;
import omis.travelpermit.exception.TravelPermitExistsException;
import omis.travelpermit.exception.TravelPermitPeriodicityExistsException;
import omis.travelpermit.service.TravelPermitService;
import omis.travelpermit.service.delegate.TravelMethodDelegate;
import omis.travelpermit.service.delegate.TravelPermitPeriodicityDelegate;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

@Test(groups = "chronologicalNote")
public class TravelPermitCreateTests
extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Property names. */
	
	private static final String OFFENDER_PROPERTY_NAME = "offender";
	private static final String DATE_RANGE_PROPERTY_NAME = "dateRange";
	
	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("travelPermitPeriodicityDelegate")
	private TravelPermitPeriodicityDelegate travelPermitPeriodicityDelegate;
	
	@Autowired
	@Qualifier("travelMethodDelegate")
	private TravelMethodDelegate travelMethodDelegate;
		
	@Autowired
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate userAccountDelegate;
		
	/* Service. */
	@Autowired
	@Qualifier("travelPermitService")
	private TravelPermitService travelPermitService;
	
	/* Constructor. */
	
	/** Instantiates a default instance of travel permit create tests. */
	public TravelPermitCreateTests() {
		//Default constructor.
	}
	
	/* Tests */
	
	@Test
	public void testCreate() throws TravelPermitExistsException,
		TravelPermitPeriodicityExistsException {
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2018"), null);
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Schmoe", "Joe", "Not So", null);
		TravelPermitIssuance issuance = new TravelPermitIssuance(
				this.parseDateText("01/01/2018"),
				this.userAccountDelegate.findByUsername("AUDIT"));
		TravelPermitPeriodicity periodicity;
		try {
			periodicity = this.travelPermitPeriodicityDelegate.create(
					"New travel periodicity name", (short) 0);
		} catch (TravelPermitPeriodicityExistsException e) {
			throw new TravelPermitPeriodicityExistsException("Travel Permit Periodicity exists: " + e);
		}
		TravelMethod method = this.travelMethodDelegate.create("Travel Method", true,
				"Descriptiony Name","Numbery Name", (short) 0);
		TravelDestination destination = new TravelDestination("Travel Destination Name",
				null, null, null, null, null);
		TravelTransportation transportation = new TravelTransportation("", "", method);
		TravelPermit permit;
		try {
			permit = this.travelPermitService.create(
					offender, "purpose", dateRange, periodicity,
					issuance, transportation, destination);
		} catch (TravelPermitExistsException e) {
			throw new TravelPermitExistsException("Travel Permit exists: " + e);
		}
		
		/* Assertions. */
		PropertyValueAsserter.create()
		.addExpectedValue(OFFENDER_PROPERTY_NAME, offender)
		.addExpectedValue(DATE_RANGE_PROPERTY_NAME, dateRange)
		.performAssertions(permit);
	}
	
	@Test(expectedExceptions = {TravelPermitExistsException.class})
	public void testTravelPermitExistsException() throws TravelPermitExistsException,
		TravelPermitPeriodicityExistsException {
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Schmoe", "Joe", "Not So", null);
		TravelPermitPeriodicity periodicity;
		try {
			periodicity = this.travelPermitPeriodicityDelegate.create(
					"periodicity name", (short) 0);
		} catch (TravelPermitPeriodicityExistsException e) {
			throw new TravelPermitPeriodicityExistsException("Travel Permit Periodicity exists: " + e);
		}

		TravelMethod method = this.travelMethodDelegate.create("Travel Method", true,
				"Descriptiony Name","Numbery Name", (short) 0);
		this.createTravelPermit(offender, periodicity, method);
		
		//Action
		this.createTravelPermit(offender, periodicity, method);
		
	}
	
	/* Helper methods */
	
	/*
	 * Parses the specified string and returns a {@code Date} object
	 * representing the parsed {@code String}.
	 *  
	 * @param text text to parse
	 * @return date 
	 */
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
	
	
	/*
	 * Creates a travel permit with the specified periodicity.
	 * 
	 * @param offender offender
	 * @param periodicity travel permit periodicity
	 * @param method travel method
	 * @return newly created travel permit
	 *
	 * @throws TravelPermitExistsException thrown when a duplicate travel permit exists
	 */
	private TravelPermit createTravelPermit(final Offender offender,
			final TravelPermitPeriodicity periodicity, final TravelMethod method)
		throws TravelPermitExistsException {
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2018"), null);
	
		TravelPermitIssuance issuance = new TravelPermitIssuance(
				this.parseDateText("01/01/2018"),
				this.userAccountDelegate.findByUsername("AUDIT"));
		
		TravelDestination destination = new TravelDestination("Travel Destination Name",
				null, null, null, null, null);
		TravelTransportation transportation = new TravelTransportation("", "", method);
		TravelPermit permit = this.travelPermitService.create(
					offender, "purpose", dateRange, periodicity,
					issuance, transportation, destination);
		return permit;
	}
}
