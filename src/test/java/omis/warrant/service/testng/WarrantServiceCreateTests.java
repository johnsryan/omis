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
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.ConditionTitle;
import omis.condition.service.delegate.ConditionClauseDelegate;
import omis.condition.service.delegate.ConditionTitleDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.courtcase.exception.CourtCaseExistsException;
import omis.datatype.DateRange;
import omis.docket.exception.DocketExistsException;
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
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantArrest;
import omis.warrant.domain.WarrantCauseViolation;
import omis.warrant.domain.WarrantNote;
import omis.warrant.domain.WarrantReasonCategory;
import omis.warrant.exception.WarrantArrestExistsException;
import omis.warrant.exception.WarrantCauseViolationExistsException;
import omis.warrant.exception.WarrantExistsException;
import omis.warrant.exception.WarrantNoteExistsException;
import omis.warrant.service.WarrantService;

/**
 * Warrant Create Tests.
 * 
 *@author Annie Jacques 
 *@author Yidong Li
 *@version 0.1.0 (April 20, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantServiceCreateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("warrantService")
	private WarrantService warrantService;
	
	@Autowired
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;
	
	@Autowired
	private ConditionClauseDelegate conditionClauseDelegate;
	
	@Autowired
	private ConditionTitleDelegate conditionTitleDelegate;
	
	//All this stuff just so we can create a jail for WarrantArrest
	@Autowired
	private JailDelegate jailDelegate;
	
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
	
	/**
	 * @throws DuplicateEntityFoundException - When a duplicate entity is found
	 * @throws WarrantExistsException thrown when a warrant already exists
	 */
	@Test
	public void testWarrantCreate() throws DuplicateEntityFoundException,
		WarrantExistsException {
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
		
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"Gotham", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
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
		Warrant warrant = this.warrantService.create(offender, date, addressee,
				issuedBy, bondable, bondRecommendation, warrantReason, jail);
		
		assert offender.equals(warrant.getOffender())
		: String.format("Wrong offender for warrant: "
				+ "%s found; %s expected",
				warrant.getOffender().getName().getFirstName(),
				offender.getName().getFirstName());
		assert date.equals(warrant.getDate())
		: String.format("Wrong date for warrant: %s found; %s expected",
				warrant.getDate(), date);
		assert issuedBy.equals(warrant.getIssuedBy())
		: String.format("Wrong issuedBy for warrant: %s found; %s expected",
				warrant.getIssuedBy().getName().getFirstName(),
				issuedBy.getName().getFirstName());
		assert addressee.equals(warrant.getAddressee())
		: String.format("Wrong addressee for warrant: %s found; %s expected",
				warrant.getAddressee(), addressee);
		assert bondable.equals(warrant.getBondable())
		: String.format("Wrong bondable for warrant: %s found; %s expected",
				warrant.getBondable(), bondable);
		assert bondRecommendation.equals(warrant.getBondRecommendation())
		: String.format("Wrong bondRecommendation for warrant: %s found; "
				+ "%s expected",
				warrant.getBondRecommendation(), bondRecommendation);
		assert jail.equals(warrant.getHoldingJail())
		: String.format("Wrong jail for warrant: %s found; %s expected",
				warrant.getHoldingJail().getName(),
				jail.getName());
	}
	
	/**
	 * @throws DuplicateEntityFoundException - When a duplicate entity is found
	 * @throws WarrantArrestExistsException - when warrant arrest already exists
	 */
	@Test
	public void testWarrantArrestCreate() throws DuplicateEntityFoundException,
	WarrantArrestExistsException {
		final Date date = this.parseDateText("05/30/2017");
		final Date contactBy = this.parseDateText("06/01/2017");
		final Organization organization = this.organizationDelegate.create(
				"Arkham Asylum", "AA", null);
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"City", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Address address = this.addressDelegate.findOrCreate(
				"123", "321", null,
				null, zipCode);
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final Jail jail = this.jailDelegate.create("Arkham Asylum",
				location, JailCategory.COUNTY, true, 1234567890L);
		final Warrant warrant = this.createWarrant(country, state, city,
			zipCode);
		
		final WarrantArrest warrantArrest = this.warrantService.createArrest(
				warrant, date, jail, contactBy);
		
		assert warrant.equals(warrantArrest.getWarrant())
		: String.format("Wrong warrant for warrantArrest: %d found; "
				+ "%d expected",
				warrantArrest.getWarrant().getId(), warrant.getId());
		assert date.equals(warrantArrest.getDate())
		: String.format("Wrong date for warrantArrest: %s found; %s expected",
				warrantArrest.getDate(), date);
		assert jail.equals(warrantArrest.getJail())
		: String.format("Wrong jail for warrantArrest: %s found; %s expected",
				warrantArrest.getJail().getName(), jail.getName());
		assert contactBy.equals(warrantArrest.getDeterminationDeadline())
		: String.format("Wrong contactBy for warrantArrest: %s found; "
				+ "%s expected", warrantArrest.getDate(), contactBy);
	}
	
	/**
	 * @throws DuplicateEntityFoundException - When a duplicate entity is found
	 * @throws WarrantArrestExistsException - thrown when warrant arrest already
	 * exists
	 */
	@Test
	public void testWarrantNoteCreate() throws DuplicateEntityFoundException,
	WarrantNoteExistsException {
		final String note = "This is a Note!";
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"Gotham", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		
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

		Warrant warrant = this.warrantService.create(offender, date, addressee,
				issuedBy, bondable, bondRecommendation, warrantReason, jail);
		
		WarrantNote warrantNote = this.warrantService
				.createWarrantNote(warrant, note, date);
		
		assert warrant.equals(warrantNote.getWarrant())
		: String.format("Wrong warrant for warrantNote: %d found; %d expected",
				warrantNote.getWarrant().getId(), warrant.getId());
		assert note.equals(warrantNote.getValue())
		: String.format("Wrong note for warrantNote: %s found; %s expected",
				warrantNote.getValue(), note);
		assert date.equals(warrantNote.getDate())
		: String.format("Wrong date for warrantNote: %s found; %s expected",
				warrantNote.getDate(), date);
	}
	
	/**
	 * @throws DuplicateEntityFoundException - When a duplicate entity is found
	 * @throws DocketExistsException - When a duplicate Docket is found
	 * @throws CourtCaseExistsException - When a duplicate Court Case is found
	 * @throws WarrantCauseViolationException - thrown when warrant cause
	 * violation already exists
	 */
	@Test
	public void testWarrantCauseViolationCreate()
			throws DuplicateEntityFoundException, DocketExistsException, 
			CourtCaseExistsException, WarrantCauseViolationExistsException {
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"City", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Warrant warrant = this.createWarrant(country, state, city,
			zipCode);
		
		final ConditionTitle conditionTitle =
				this.conditionTitleDelegate.create(
				"Condition Title");
		final ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);

		final String description = "WCV Description";
		
		final WarrantCauseViolation warrantCauseViolation = this.warrantService
				.createWarrantCauseViolation(warrant, conditionClause,
						description);
		
		assert warrant.equals(warrantCauseViolation.getWarrant())
		: String.format("Wrong warrant for warrantCauseViolation: %d found; "
				+ "%d expected",
				warrantCauseViolation.getWarrant().getId(), warrant.getId());
		assert conditionClause.equals(
			warrantCauseViolation.getConditionClause())
		: String.format("Wrong condition clause for warrantCauseViolation:"
				+ "%s found;"
				+ " %s expected",
				warrantCauseViolation.getConditionClause(),
				conditionClause);
		assert description.equals(warrantCauseViolation.getDescription())
		: String.format("Wrong description for warrantCauseViolation: "
				+ "%s found; "
				+ "%s expected",
				warrantCauseViolation.getDescription(), description);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
	private Warrant createWarrant(Country country, State state, City city,
			ZipCode zipCode) throws DuplicateEntityFoundException,
			WarrantExistsException {
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
