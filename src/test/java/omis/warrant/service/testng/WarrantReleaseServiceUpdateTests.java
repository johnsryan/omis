/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.country.domain.Country;
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.jail.domain.Jail;
import omis.jail.domain.JailCategory;
import omis.jail.service.delegate.JailDelegate;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.exception.StateExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.domain.WarrantRelease;
import omis.warrant.exception.WarrantExistsException;
import omis.warrant.exception.WarrantReleaseExistsException;
import omis.warrant.service.WarrantReleaseService;
import omis.warrant.service.WarrantService;
import omis.warrant.service.delegate.WarrantReleaseDelegate;

/**
 * WarrantReleaseServiceUpdateTests.java
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@author Yidong Li
 *@version 0.1.0 (April 24, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantReleaseServiceUpdateTests
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
	private WarrantReleaseDelegate warrantReleaseDelegate;
	
	
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
	@Qualifier("jailDelegate")
	private JailDelegate jailDelegate;
	
	@Test
	public void testWarrantReleaseUpdate() throws WarrantReleaseExistsException,
		CountryExistsException, StateExistsException, CityExistsException, 
		ZipCodeExistsException, DuplicateEntityFoundException {
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"Gotham", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Person clearedBy0 = this.personDelegate.create("Grayson", "Richard",
				null, null);
		final Warrant warrant = this.createWarrant(country, state, city, zipCode);
		WarrantRelease warrantRelease = this.warrantReleaseDelegate
				.create(warrant, "Old Instructions",
						this.parseDateText("10/01/2015"), "Somewhere", clearedBy0,
						this.parseDateText("02/02/2018"));
		final String instructions = "Instructions!";
		final Date releaseTimestamp = this.parseDateText("01/01/2017");
		final String addressee = "Addressee";
		final Person clearedBy = this.personDelegate.create("Dent", "Harvey",
				null, null);
		final Date clearedByDate = this.parseDateText("01/01/2017");
		
				
		warrantRelease = this.warrantReleaseService
				.update(warrantRelease, instructions,
						releaseTimestamp, addressee, clearedBy, clearedByDate);
		
		assert warrant.equals(warrantRelease.getWarrant())
		: String.format("Wrong warrant for warrantRelease: %d found; %d expected",
				warrantRelease.getWarrant().getId(), warrant.getId());
		assert instructions.equals(warrantRelease.getInstructions())
		: String.format("Wrong instructions for warrantRelease: %s found; "
				+ "%s expected",
				warrantRelease.getInstructions(), instructions);
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
					.getName().getFirstName(), clearedBy.getName()
				.getFirstName());
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
	
	private Warrant createWarrant(Country country, State state, City city,
			ZipCode zipCode) throws WarrantExistsException, DuplicateEntityFoundException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Wayne", "Bruce", "Alen", null);
		final Date date = this.parseDateText("05/12/2017");
		final Person issuedBy = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final String addressee = "Addressed To Someone, Somewhere";
		final WarrantReasonCategory warrantReason = WarrantReasonCategory
				.AUTHORIZATION_TO_PICKUP_AND_HOLD_PROBATIONER;
		final Boolean bondable = true;
		final BigDecimal bondRecommendation = new BigDecimal("500");
		
		final Organization organization = this.organizationDelegate.create(
				"Brkham Bsylum", "BB", null);
		final Address address = this.addressDelegate.findOrCreate(
				"321", "123", null,
				null, zipCode);
		final Location location = this.locationDelegate.create(organization,
			new DateRange(this.parseDateText("01/01/2010"),
					this.parseDateText("01/01/2022")), address);
		final Jail jail = this.jailDelegate.create("Brkham Bsylum",
				location, JailCategory.COUNTY, true, 1234567890L);
		
		return this.warrantService.create(offender, date, addressee,
				issuedBy, bondable, bondRecommendation, warrantReason, jail);
	}	
}