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
package omis.offender.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.OffenderPersonalDetailsService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.PersonIdentity;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to change offender person details.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 21, 2018)
 * @since OMIS 3.0
 */
@Test
public class OffenderPersonalDetailsServiceChangeIdentityTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personIdentityDelegate")
	private PersonIdentityDelegate personIdentityDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("offenderPersonalDetailsService")
	private OffenderPersonalDetailsService offenderPersonalDetailsService;

	/* Test methods. */

	/**
	 * Test change offender identity.
	 *
	 *
	 * @throws PersonIdentityExistsException person identity exists
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test
	public void testChangeIdentity() throws PersonIdentityExistsException, 
			DuplicateEntityFoundException {
		// Arrangements
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		Date birthDate = this.parseDateText("01/01/1968");
		final Country birthCountry = this.countryDelegate.create(
				"United Status", "US", true);
		final State birthState = this.stateDelegate.create(
				"Montana", "MT", birthCountry, true, true);
		City birthPlace = this.cityDelegate.create("Helena", true, 
				birthState, birthCountry);
		Integer socialSecurityNumber = 123456789;
		String stateIdNumber = "12345";
		Sex sex = Sex.FEMALE;
		Boolean deceased = false;
		Date deathDate = null;		
		Offender offender = this.offenderDelegate.create(
				lastName, firstName, middleName, null, socialSecurityNumber, 
				stateIdNumber, birthDate, birthCountry, birthState, birthPlace, 
				sex);

		// Action
		PersonIdentity personIdentity = this.offenderPersonalDetailsService
				.changeIdentity(offender, birthDate, birthCountry, 
						birthState, birthPlace, socialSecurityNumber, 
						stateIdNumber, sex, deceased, deathDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", offender)
			.addExpectedValue("birthDate", birthDate)
			.addExpectedValue("birthCountry", birthCountry)
			.addExpectedValue("birthState", birthState)
			.addExpectedValue("birthPlace", birthPlace)
			.addExpectedValue("socialSecurityNumber", socialSecurityNumber)
			.addExpectedValue("stateIdNumber", stateIdNumber)
			.addExpectedValue("sex", sex)
			.addExpectedValue("deceased", deceased)
			.addExpectedValue("deathDate", deathDate)
			.performAssertions(personIdentity);
	}

	/**
	 * Test person identity exists exception.
	 *
	 *
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {PersonIdentityExistsException.class})
	public void testPersonIdentityExistsException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		Date birthDate = this.parseDateText("01/01/1968");
		final Country birthCountry = this.countryDelegate.create(
				"United Status", "US", true);
		final State birthState = this.stateDelegate.create(
				"Montana", "MT", birthCountry, true, true);
		City birthPlace = this.cityDelegate.create("Helena", true, 
				birthState, birthCountry);
		Integer socialSecurityNumber = 123456789;
		String stateIdNumber = "12345";
		Sex sex = Sex.FEMALE;
		Boolean deceased = false;
		Date deathDate = null;
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				lastName, firstName, middleName, null);
		this.personIdentityDelegate.create(
				offender, sex, birthDate, birthCountry, birthState, birthPlace, 
				socialSecurityNumber, stateIdNumber, deceased, deathDate);
		// Action
		this.offenderPersonalDetailsService
			.changeIdentity(offender, birthDate, birthCountry, birthState, 
				birthPlace, socialSecurityNumber, stateIdNumber, sex, 
				deceased, deathDate);
	}
	
	/* Helper methods */
	
	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}