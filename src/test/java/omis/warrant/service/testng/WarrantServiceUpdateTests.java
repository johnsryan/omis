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
 * Warrant Update Tests.
 * 
 *@author Annie Jacques 
 *@author Yidong Li
 *@version 0.1.0 (April 24, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantServiceUpdateTests
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
	 * @throws WarrantExistsException - thrown when a warrant already exists
	 */
	@Test
	public void testWarrantUpdate() throws DuplicateEntityFoundException,
		WarrantExistsException {
		final Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Joker", "The", null, null);
		final Date date0 = this.parseDateText("05/12/2017");
		final Person issuedBy0 = this.personDelegate.create(
				"Grayson", "Richard", "J", null);
		final String addressee0 = "Addressed To Someone, Somewhere";
		final WarrantReasonCategory warrantReason0 = WarrantReasonCategory
				.AUTHORIZATION_TO_PICKUP_AND_HOLD_PROBATIONER;
		final Boolean bondable0 = true;
		final BigDecimal bondRecommendation0 = new BigDecimal("500");
		
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
		
		Warrant warrant = this.warrantService.create(
				offender, date0, addressee0, issuedBy0, bondable0,
				bondRecommendation0, warrantReason0, jail);
		
		final Date date = this.parseDateText("01/11/2011");
		final Person issuedBy = this.personDelegate.create(
				"Dent", "Harvey", null, null);
		final String addressee = "Addressed For Success";
		final WarrantReasonCategory warrantReason = WarrantReasonCategory
				.ARREST_PAROLEE;
		final Boolean bondable = false;
		final BigDecimal bondRecommendation = new BigDecimal("0");
		
		final City cityUpdate = this.cityDelegate.create(
				"Ddsa", true, state, country);
		final ZipCode zipCodeUpdate = this.zipCodeDelegate.create(
				cityUpdate, "54321", null, true);
		final Organization organizationUpdate = this.organizationDelegate
			.create("Crkham Csylum", "CC", null);
		final Address addressUpdate = this.addressDelegate.findOrCreate(
				"678", "876", null,
				null, zipCodeUpdate);
		final Location locationUpdate = this.locationDelegate.create(
			organizationUpdate,	new DateRange(this.parseDateText("01/01/2010"),
					this.parseDateText("01/01/2022")), addressUpdate);
		final Jail jailUpdate = this.jailDelegate.create("Crkham Csylum",
				locationUpdate, JailCategory.COUNTY, true, 9876543210L);
		
		warrant = this.warrantService.update(warrant, date, addressee,
				issuedBy, bondable, bondRecommendation, warrantReason,
				jailUpdate);
		
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
	}
	
	/**
	 * @throws DuplicateEntityFoundException - When a duplicate entity is found
	 * @throws WarrantArrestExistsException - thrown when warrant arrest already
	 * exists
	 */
	@Test
	public void testWarrantArrestUpdate() throws DuplicateEntityFoundException,
	WarrantArrestExistsException {
		final Warrant warrant = this.createWarrant();
		final Date date = this.parseDateText("05/30/2017");
		final Date contactBy = this.parseDateText("06/01/2017");
		final Date date0 = this.parseDateText("01/01/2011");
		final Date contactBy0 = this.parseDateText("02/02/2012");
		final Organization organization = this.organizationDelegate.create(
				"Arkham Asylum", "AA", null);
		final Country country = this.countryDelegate.findOrCreate("Country",
			"USA", true);
		final State state = this.stateDelegate.create(
				"StateUpdate", "SU", country, true, true);
		final City city = this.cityDelegate.create(
				"CityUpdate", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		final Address address = this.addressDelegate.findOrCreate(
				"123", "321", null,
				null, zipCode);
		final Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		final Jail jail = this.jailDelegate.create("Arkham Asylum",
				location, JailCategory.TRIBAL, true, 1234567890L);
		final Jail jail0 = this.jailDelegate.create("Gotham Holding",
				location, JailCategory.COUNTY, true, 2234567890L);
		
		WarrantArrest warrantArrest = this.warrantService.createArrest(
				warrant, date0, jail0, contactBy0);
		
		warrantArrest = this.warrantService.updateArrest(
				warrantArrest, date, jail, contactBy);
		
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
				+ "%s expected",
				warrantArrest.getDeterminationDeadline(), contactBy);
	}
	
	/**
	 * @throws DuplicateEntityFoundException - When a duplicate entity is found
	 * @throws WarrantNoteExistsException - thrown when warrant note already
	 * exists
	 */
	@Test
	public void testWarrantNoteUpdate() throws DuplicateEntityFoundException,
	WarrantNoteExistsException {
		final Warrant warrant = this.createWarrant();
		final String note = "This is a Note!";
		final Date date = this.parseDateText("05/05/2017");

		final String note0 = "This is definitely not a Note!";
		final Date date0 = this.parseDateText("01/01/2011");
		
		WarrantNote warrantNote = this.warrantService
				.createWarrantNote(warrant, note0, date0);
		
		warrantNote = this.warrantService
				.updateWarrantNote(warrantNote, note, date);
		
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
	 * @throws WarrantCauseViolationExistsException - thrown when warrant cause
	 * violation already exists
	 */
	@Test
	public void testWarrantCauseViolationUpdate()
			throws DuplicateEntityFoundException, DocketExistsException, 
			CourtCaseExistsException, WarrantCauseViolationExistsException {
		final Warrant warrant = this.createWarrant();
		final ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Condition Title");
		final ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);
		final String description0 = "WCV First Description";
		WarrantCauseViolation warrantCauseViolation = this.warrantService
				.createWarrantCauseViolation(warrant, conditionClause,
						description0);
		final String description = "WCV Description";
		
		warrantCauseViolation = this.warrantService
				.updateWarrantCauseViolation(warrantCauseViolation,
				conditionClause, description);
		
		assert warrant.equals(warrantCauseViolation.getWarrant())
		: String.format("Wrong warrant for warrantCauseViolation: %d found; "
				+ "%d expected",
				warrantCauseViolation.getWarrant().getId(), warrant.getId());
		assert description.equals(warrantCauseViolation.getDescription())
		: String.format("Wrong description for warrantCauseViolation: %s found;"
				+ " %s expected",
				warrantCauseViolation.getDescription(), description);
	}
	
	private Date parseDateText(final String dateText) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(dateText);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
	}
	
	private Warrant createWarrant() throws DuplicateEntityFoundException,
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
		final Country country = this.countryDelegate.create(
				"Country", "USA", true);
		final State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		final City city = this.cityDelegate.create(
				"Gotham", true, state, country);
		final ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
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
