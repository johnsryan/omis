package omis.warrant.service.testng;

import java.math.BigDecimal;
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
import omis.facility.service.delegate.FacilityDelegate;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.County;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.CountyDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.domain.WarrantRelease;
import omis.warrant.service.WarrantReleaseService;
import omis.warrant.service.WarrantService;

/**
 * WarrantReleaseServiceCreateTests.java
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (May 15, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantReleaseServiceCreateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	@Autowired
	private WarrantService warrantService;
	
	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("warrantReleaseService")
	private WarrantReleaseService warrantReleaseService;
	
	@Autowired
	private FacilityDelegate facilityDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	private AddressDelegate addressDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private CountyDelegate countyDelegate;
	
	@Test
	public void testWarrantReleaseCreate() throws DuplicateEntityFoundException {
		final Organization organization = this.organizationDelegate.create(
				"Arkham Asylum", "AA", null);
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"Gotham", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Address address = this.addressDelegate.findOrCreate("123", "321", null,
				null, zipCode);
		final Long telephoneNumber = new Long("1234567");
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final Facility facility = this.facilityDelegate.create(location,
				"Arkham Asylum", "AA", true, telephoneNumber);
		final County county = this.countyDelegate.create("Gotham County",
				state, true);
		final String instructions = "Instructions!";
		final Date releaseTimestamp = this.parseDateText("01/01/2017");
		final String addressee = "Addressee";
		final Warrant warrant = this.createWarrant();
		final Person clearedBy = this.personDelegate.create("Dent", "Harvey",
				null, null);
		final Date clearedByDate = this.parseDateText("01/01/2017");
		
		final WarrantRelease warrantRelease = this.warrantReleaseService
				.create(warrant, instructions, county, facility,
						releaseTimestamp, addressee, clearedBy, clearedByDate);
		
		assert warrant.equals(warrantRelease.getWarrant())
		: String.format("Wrong warrant for warrantRelease: %d found; %d expected",
				warrantRelease.getWarrant().getId(), warrant.getId());
		assert instructions.equals(warrantRelease.getInstructions())
		: String.format("Wrong instructions for warrantRelease: %s found; "
				+ "%s expected",
				warrantRelease.getInstructions(), instructions);
		assert county.equals(warrantRelease.getCounty())
		: String.format("Wrong county for warrantRelease: %s found; %s expected",
				warrantRelease.getCounty().getName(), county.getName());
		assert facility.equals(warrantRelease.getFacility())
		: String.format("Wrong facility for warrantRelease: %s found; %s expected",
				warrantRelease.getFacility().getName(), facility.getName());
		assert releaseTimestamp.equals(warrantRelease.getReleaseTimestamp())
		: String.format("Wrong releaseTimestamp for warrantRelease: %s found; "
				+ "%s expected",
				warrantRelease.getReleaseTimestamp(), releaseTimestamp);
		assert addressee.equals(warrantRelease.getAddressee())
		: String.format("Wrong addressee for warrantRelease: %s found; %s expected",
				warrantRelease.getAddressee(), addressee);
		assert clearedBy.equals(warrantRelease.getClearSignature().getPerson())
		: String.format("Wrong clearedBy for warrantRelease: %s found; %s expected",
				warrantRelease.getClearSignature().getPerson()
					.getName().getFirstName(), clearedBy.getName().getFirstName());
		assert clearedByDate.equals(warrantRelease.getClearSignature().getDate())
		: String.format("Wrong clearedByDate for warrantRelease: %s found; "
				+ "%s expected",
				warrantRelease.getClearSignature().getDate(), clearedByDate);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
	private Warrant createWarrant() throws DuplicateEntityFoundException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final Date date = this.parseDateText("05/12/2017");
		final Person issuedBy = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final String addressee = "Addressed To Someone, Somewhere";
		final WarrantReasonCategory warrantReason = WarrantReasonCategory
				.AUTHORIZATION_TO_PICKUP_AND_HOLD;
		final Boolean bondable = true;
		final BigDecimal bondRecommendation = new BigDecimal("500");
		
		return this.warrantService.create(offender, date, addressee,
				issuedBy, bondable, bondRecommendation, warrantReason);
	}
}
