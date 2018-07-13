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
package omis.offenderrelationship.service.testng;

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
import omis.offenderrelationship.service.UpdateOffenderRelationService;
import omis.person.domain.Person;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.person.service.delegate.PersonNameDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update offender relation-with social security number 
 * and without passing in on update, even if one exist.
 *
 * @author Sheronda Vaughn
 * @author Stephen Abson
 * @version 0.0.1 (Mar 22, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"offenderrelationship"})
public class UpdateOffenderRelationServiceUpdateRelationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("personNameDelegate")
	private PersonNameDelegate personNameDelegate;
	
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
	@Qualifier("updateOffenderRelationService")
	private UpdateOffenderRelationService updateOffenderRelationService;

	/* Test methods. */

	/**
	 * Test update relation.
	 *
	 *
	 * @throws PersonNameExistsException person name exists
	 * @throws PersonIdentityExistsException person identity exists
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test
	public void testUpdateRelation() 
			throws PersonNameExistsException, 
			PersonIdentityExistsException, DuplicateEntityFoundException {
		// Arrangements		
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		final String suffix = "Jr";
		final Sex sex = Sex.FEMALE;
		final String birthPlaceName = "Bozeman";
		final Date birthDate = this.parseDateText("02/17/1978");
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthCity = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Integer socialSecurityNumber = 123456789;
		final String stateId = "12345";
		
		Person person = this.personDelegate.createWithIdentity(lastName, 
				firstName, middleName, suffix, sex, birthDate, birthCountry, 
				birthState, birthCity, socialSecurityNumber, stateId, null, 
				null);
		// Action
		person = this.updateOffenderRelationService.updateRelation(
				person, lastName, firstName, middleName, suffix, sex, birthDate,
				birthCountry, birthState, birthCity, socialSecurityNumber, 
				stateId, null, null);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("name.lastName", lastName)
			.addExpectedValue("name.firstName", firstName)
			.addExpectedValue("name.middleName", middleName)
			.addExpectedValue("name.suffix", suffix)
			.addExpectedValue("identity.sex", sex)
			.addExpectedValue("identity.birthDate", birthDate)
			.addExpectedValue("identity.birthCountry", birthCountry)
			.addExpectedValue("identity.birthState", birthState)
			.addExpectedValue("identity.birthPlace", birthCity)
			.addExpectedValue("identity.socialSecurityNumber", 
					socialSecurityNumber)
			.addExpectedValue("identity.stateIdNumber", stateId)
			.addExpectedValue("identity.deceased", null)
			.addExpectedValue("identity.deathDate", null)
			.performAssertions(person);
	}

	/**
	 * Tests update of relation with SSN when relation initially does not
	 * have an identity.
	 * 
	 * @throws PersonIdentityExistsException if person identity exists
	 * @throws PersonNameExistsException if person name exists
	 */
	public void testUpdateRelationWithoutIdentity()
			throws PersonNameExistsException,
				PersonIdentityExistsException {
		
		// Arrangements
		final String lastName = "LastName";
		final String firstName = "FirstName";
		Person person = this.personDelegate
				.create(lastName, firstName, null, null);
		
		// Action - updates person with identity information
		final Integer socialSecurityNumber = 123456789;
		final Sex female = Sex.FEMALE;
		final Date birthDate = this.parseDateText("12/12/1992");
		final Boolean deceased = false;
		person = this.updateOffenderRelationService
				.updateRelation(person, lastName, firstName, null, null, female,
						birthDate, null, null, null, socialSecurityNumber, null,
						deceased, null);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("name.lastName", lastName)
			.addExpectedValue("name.firstName", firstName)
			.addExpectedValue("name.middleName", null)
			.addExpectedValue("name.suffix", null)
			.addExpectedValue("identity.sex", female)
			.addExpectedValue("identity.birthDate", birthDate)
			.addExpectedValue("identity.birthCountry", null)
			.addExpectedValue("identity.birthState", null)
			.addExpectedValue("identity.birthPlace", null)
			.addExpectedValue("identity.socialSecurityNumber",
					socialSecurityNumber)
			.addExpectedValue("identity.stateIdNumber", null)
			.addExpectedValue("identity.deceased", deceased)
			.addExpectedValue("identity.deathDate", null)
			.performAssertions(person);
	}
	
	/**
	 * Test update relation with existing social security number, but updated
	 * from person identity social security number.
	 *
	 *
	 * @throws PersonNameExistsException person name exists
	 * @throws PersonIdentityExistsException person identity exists
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test
	public void testUpdateRelationWithSSNFromPersonIdentity()
			throws PersonNameExistsException, 
			PersonIdentityExistsException, DuplicateEntityFoundException {
		// Arrangements		
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		final String suffix = "Jr";
		final Sex sex = Sex.FEMALE;
		final String birthPlaceName = "Bozeman";
		final Date birthDate = this.parseDateText("02/17/1978");
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthCity = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Integer socialSecurityNumber = 123456789;
		final String stateId = "12345";
		
		Person person = this.personDelegate.createWithIdentity(lastName, 
				firstName, middleName, suffix, sex, birthDate, birthCountry, 
				birthState, birthCity, socialSecurityNumber, stateId, null, 
				null);
		// Action
		person = this.updateOffenderRelationService.updateRelation(
				person, lastName, firstName, middleName, suffix, sex, birthDate,
				birthCountry, birthState, birthCity, 
				person.getIdentity().getSocialSecurityNumber(), 
				stateId, null, null);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("name.lastName", lastName)
			.addExpectedValue("name.firstName", firstName)
			.addExpectedValue("name.middleName", middleName)
			.addExpectedValue("name.suffix", suffix)
			.addExpectedValue("identity.sex", sex)
			.addExpectedValue("identity.birthDate", birthDate)
			.addExpectedValue("identity.birthCountry", birthCountry)
			.addExpectedValue("identity.birthState", birthState)
			.addExpectedValue("identity.birthPlace", birthCity)
			.addExpectedValue("identity.socialSecurityNumber", 
					person.getIdentity().getSocialSecurityNumber())
			.addExpectedValue("identity.stateIdNumber", stateId)
			.addExpectedValue("identity.deceased", null)
			.addExpectedValue("identity.deathDate", null)
			.performAssertions(person);
	}

	/**
	 * Test person name exists.
	 *
	 *
	 * @throws PersonNameExistsException person name exists
	 * @throws PersonIdentityExistsException person identity exists
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {PersonNameExistsException.class})
	public void testPersonNameExistsException() 
			throws PersonNameExistsException, PersonIdentityExistsException,
			DuplicateEntityFoundException {
		// Arrangements
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		final String suffix = "Jr";
		
		final Sex sex = Sex.FEMALE;
		final String birthPlaceName = "Bozeman";
		final Date birthDate = this.parseDateText("02/17/1978");
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthCity = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Integer socialSecurityNumber = 123456789;
		final String stateId = "12345";
	
		Person eperson = this.personDelegate.create(
				"ELName", "EFName", "EMName", null);
		this.personNameDelegate.create(
				eperson, "ELName", "EFName", "EMName", null);
		Person person = this.personDelegate.createWithIdentity(lastName, 
				firstName, middleName, suffix, sex, birthDate, birthCountry, 
				birthState, birthCity, socialSecurityNumber, stateId, null, 
				null);
		// Action
		this.updateOffenderRelationService.updateRelation(
				person, "ELName", "EFName", "EMName", null, sex, birthDate,
				birthCountry, birthState, birthCity, socialSecurityNumber, 
				stateId, null, null);
	}
  
	/**
	 * Test person identity exists.
	 *
	 *
	 * @throws PersonNameExistsException person name exists
	 * @throws PersonIdentityExistsException person identity exists
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	@Test(expectedExceptions = {PersonIdentityExistsException.class})
	public void testPersonIdentityExistsException() 
			throws PersonNameExistsException, PersonIdentityExistsException,
				DuplicateEntityFoundException {
		// Arrangements
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		final String suffix = "Jr";
		final Sex sex = Sex.FEMALE;
		final String birthPlaceName = "Bozeman";
		final Date birthDate = this.parseDateText("02/17/1978");
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthCity = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Integer socialSecurityNumber = 123456789;
		final String stateId = "12345";
		
		Person eperson = this.personDelegate.createWithIdentity(
				lastName, firstName, middleName, suffix, sex, birthDate, 
				birthCountry, birthState, birthCity, socialSecurityNumber, 
				stateId, null, null);
		
		this.personIdentityDelegate.create(eperson, sex, birthDate, 
				birthCountry, birthState, birthCity, socialSecurityNumber, 
				stateId, null, null);
		
		// Action
		this.updateOffenderRelationService.updateRelation(
				eperson, lastName, firstName, middleName, suffix, sex, 
				birthDate, birthCountry, birthState, birthCity, 
				socialSecurityNumber, stateId, null, null);
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