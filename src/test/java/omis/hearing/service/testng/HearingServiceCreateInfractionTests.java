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
package omis.hearing.service.testng;

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
import omis.condition.domain.Agreement;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionCategory;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.ConditionTitle;
import omis.condition.service.delegate.AgreementDelegate;
import omis.condition.service.delegate.ConditionClauseDelegate;
import omis.condition.service.delegate.ConditionDelegate;
import omis.condition.service.delegate.ConditionTitleDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.disciplinaryCode.service.delegate.DisciplinaryCodeDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.hearing.domain.component.Resolution;
import omis.hearing.service.HearingService;
import omis.hearing.service.delegate.HearingDelegate;
import omis.hearing.service.delegate.InfractionDelegate;
import omis.hearing.service.delegate.InfractionPleaDelegate;
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
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.service.delegate.ConditionViolationDelegate;
import omis.violationevent.service.delegate.DisciplinaryCodeViolationDelegate;
import omis.violationevent.service.delegate.ViolationEventDelegate;

/**
 * Tests method to create infractions.
 *
 * @author Josh Divine
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
@Test
public class HearingServiceCreateInfractionTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private HearingDelegate hearingDelegate;

	@Autowired
	private ConditionViolationDelegate conditionViolationDelegate;

	@Autowired
	private DisciplinaryCodeViolationDelegate disciplinaryCodeViolationDelegate;

	@Autowired
	private InfractionPleaDelegate infractionPleaDelegate;

	@Autowired
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private AddressDelegate addressDelegate;
	
	@Autowired
	private PersonDelegate personDelegate;

	@Autowired
	private LocationDelegate locationDelegate;

	@Autowired
	private OffenderDelegate offenderDelegate;

	@Autowired
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	private ViolationEventDelegate violationEventDelegate;
	
	@Autowired
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	private ConditionDelegate conditionDelegate;
	
	@Autowired
	private DisciplinaryCodeDelegate disciplinaryCodeDelegate;
	
	@Autowired
	private AgreementDelegate agreementDelegate;
	
	@Autowired
	private ConditionClauseDelegate conditionClauseDelegate;
	
	@Autowired
	private ConditionTitleDelegate conditionTitleDelegate;
	
	@Autowired
	private InfractionDelegate infractionDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("hearingService")
	private HearingService hearingService;

	/* Test methods. */

	/**
	 * Tests method to create infractions.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateInfraction() throws DuplicateEntityFoundException {
		// Arrangements
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create("Country", "USA", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321", null,
				null, zipCode);
		Person person = this.personDelegate.create("Pennyworth", "Alfred", "J", 
				null);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);;
		UserAccount officer = this.userAccountDelegate.create(person, "USER", 
				"", null, 0, true);
		Hearing hearing = this.hearingDelegate.create(location, offender, false, 
				this.parseDateText("05/04/2017"), HearingCategory.DISCIPLINARY, 
				officer);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("Batcave", "TBC", null);
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, null, this.parseDateText("05/05/2017"), 
				"Event Details", ViolationEventCategory.DISCIPLINARY);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/05/2017"), null, null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate.create(
				"Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Description", conditionTitle, true);
		Condition condition = this.conditionDelegate.create(agreement, "Clause", 
				conditionClause, ConditionCategory.STANDARD, false);
		ConditionViolation conditionViolation = this.conditionViolationDelegate
				.create(condition, violationEvent);
		DisciplinaryCode disciplinaryCode = this.disciplinaryCodeDelegate
				.create("Value", "Description", "Extended Description");
		DisciplinaryCodeViolation disciplinaryCodeViolation = this
				.disciplinaryCodeViolationDelegate.create(disciplinaryCode, 
						violationEvent);
		Resolution resolution = new Resolution(null, "Decision", "Reason", 
				person, null, DispositionCategory.NOT_GUILTY, 
				ResolutionClassificationCategory.FORMAL);
		InfractionPlea plea = this.infractionPleaDelegate.create("Guilty", true);

		// Action
		Infraction infraction = this.hearingService.createInfraction(hearing, 
				conditionViolation, disciplinaryCodeViolation, resolution, plea);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("hearing", hearing)
			.addExpectedValue("conditionViolation", conditionViolation)
			.addExpectedValue("disciplinaryCodeViolation", 
					disciplinaryCodeViolation)
			.addExpectedValue("resolution", resolution)
			.addExpectedValue("plea", plea)
			.performAssertions(infraction);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Country country = this.countryDelegate.create("Country", "USA", true);
		State state = this.stateDelegate.create("State", "ST", country, true, 
				true);
		City city = this.cityDelegate.create("City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321", null,
				null, zipCode);
		Person person = this.personDelegate.create("Pennyworth", "Alfred", "J", 
				null);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);;
		UserAccount officer = this.userAccountDelegate.create(person, "USER", 
				"", null, 0, true);
		Hearing hearing = this.hearingDelegate.create(location, offender, false, 
				this.parseDateText("05/04/2017"), HearingCategory.DISCIPLINARY, 
				officer);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("Batcave", "TBC", null);
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, null, this.parseDateText("05/05/2017"), 
				"Event Details", ViolationEventCategory.DISCIPLINARY);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/05/2017"), null, null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate.create(
				"Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Description", conditionTitle, true);
		Condition condition = this.conditionDelegate.create(agreement, "Clause", 
				conditionClause, ConditionCategory.STANDARD, false);
		ConditionViolation conditionViolation = this.conditionViolationDelegate
				.create(condition, violationEvent);
		DisciplinaryCode disciplinaryCode = this.disciplinaryCodeDelegate
				.create("Value", "Description", "Extended Description");
		DisciplinaryCodeViolation disciplinaryCodeViolation = this
				.disciplinaryCodeViolationDelegate.create(disciplinaryCode, 
						violationEvent);
		Resolution resolution = new Resolution(null, "Decision", "Reason", 
				person, null, DispositionCategory.NOT_GUILTY, 
				ResolutionClassificationCategory.FORMAL);
		InfractionPlea plea = this.infractionPleaDelegate.create("Guilty", true);
		this.infractionDelegate.create(hearing, conditionViolation, 
				disciplinaryCodeViolation, resolution, plea);

		// Action
		this.hearingService.createInfraction(hearing, conditionViolation, 
				disciplinaryCodeViolation, resolution, plea);
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