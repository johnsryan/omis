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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.offenderrelationship.service.CreateRelationshipsService;
import omis.person.domain.Person;
import omis.person.exception.PersonExistsException;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Test offender relationship creation. 
 *
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"offenderrelationship"})
public class OffenderRelationshipCreateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	/* Delegates. */
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
		
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("personIdentityDelegate")
	private PersonIdentityDelegate personIdentityDelegate;
	
	/* Services */
	@Autowired
	@Qualifier("createRelationshipsService")
	private CreateRelationshipsService createRelationshipsService;
	
	/**
	 * Tests the creation of offender relationship.
	 * @throws DuplicateEntityFoundException DuplicateEntityFoundException
	 * @throws PersonExistsException PersonExistsException
	 */
	@Test
	public void testOffenderRelationshipCreation() 
		throws DuplicateEntityFoundException, PersonExistsException {
		// Arrangement
		final int birthDateInt = 111111111;
		final int deathDateInt = 1222222222;
		Date birthDate = new Date(birthDateInt);
		Date deathDate = new Date(deathDateInt);
		Country birthCountry = this.countryDelegate.create("United States", 
			"U.S.", true);
		State birthState = this.stateDelegate.create("Montana", "MT", 
			birthCountry, true, true);
		City birthCity = this.cityDelegate.create("Helena", true, birthState, 
			birthCountry);
		final Integer socialSecurityNumber = 123456666;
		String stateId = "234";
	
		// Action
		Person person = this.createRelationshipsService
			.createRelation("Smith", "John", "Don", "Mr.", Sex.MALE, birthDate, 
			birthCountry, birthState, birthCity, socialSecurityNumber, stateId, 
			true, deathDate);
				
		// Assertions
		assert birthDate.equals(person.getIdentity().getBirthDate())
		: String.format("Wrong Birth Date: #%s expected; #%s found",
			birthDate, person.getIdentity().getBirthDate());
		assert deathDate.equals(person.getIdentity().getDeathDate())
		: String.format("Wrong Death Date: #%s expected; #%s found",
			deathDate, person.getIdentity().getDeathDate());
		assert birthCountry.equals(person.getIdentity().getBirthCountry())
		: String.format("Wrong Birth Country: #%s expected; #%s found",
			birthCountry.getName(), 
			person.getIdentity().getBirthCountry().getName());
		assert birthState.equals(person.getIdentity().getBirthState())
		: String.format("Wrong Birth State: #%s expected; #%s found",
			birthState.getName(), person.getIdentity().getBirthState()
			.getName());
		assert birthCity.equals(person.getIdentity().getBirthPlace())
		: String.format("Wrong Birth City: #%s expected; #%s found",
			birthCity.getName(), person.getIdentity().getBirthPlace()
			.getName());
		assert stateId.equals(person.getIdentity().getStateIdNumber())
		: String.format("Wrong State ID: #%s expected; #%s found",
			stateId, person.getIdentity().getStateIdNumber());
		assert socialSecurityNumber.equals(
			person.getIdentity().getSocialSecurityNumber())
		: String.format("Wrong Social Security Number: #%s expected; #%s found",
			socialSecurityNumber.toString(), 
			person.getIdentity().getSocialSecurityNumber().toString());
		assert Sex.MALE.equals(person.getIdentity().getSex())
		: String.format("Wrong Sex: #%s expected; #%s found",
			Sex.MALE.toString(), person.getIdentity().getSex().toString());
		assert person.getIdentity().getDeceased()
		: String.format("Wrong Deceased: #%s expected; #%s found",
			true, person.getIdentity().getDeceased().toString());
		assert "Smith".equals(person.getName().getLastName())
		: String.format("Wrong Sex: #%s expected; #%s found",
			"Smith", person.getName().getLastName());
		assert "John".equals(person.getName().getFirstName())
		: String.format("Wrong Sex: #%s expected; #%s found",
			"John", person.getName().getFirstName());
		assert "Don".equals(person.getName().getMiddleName())
		: String.format("Wrong Sex: #%s expected; #%s found",
			"Don", person.getName().getMiddleName());
	}
	
	/**
	 * Tests duplicate offender relationships on creation.
	 * <p>Test is disabled until uniqueness of business key of person can be enforced. 
	 * Once uniqueness can be enforced, this test will be enabled.
	 * @throws DuplicateEntityFoundException if duplicate relationships exist
	 * @throws PersonExistsException person exists exception
	 * exception
	 */
	@Test(expectedExceptions = {PersonExistsException.class}, enabled = false)
	public void testDuplicateOffenderRelationshipsCreate() 
		throws DuplicateEntityFoundException, PersonExistsException {
		// Assignment
		final int birthDateInt = 111111111;
		final int deathDateInt = 1222222222;
		Date birthDate = new Date(birthDateInt);
		Date deathDate = new Date(deathDateInt);
		Country birthCountry = this.countryDelegate.create("United States", 
			"U.S.", true);
		State birthState = this.stateDelegate.create("Montana", "MT", 
			birthCountry, true, true);
		City birthCity = this.cityDelegate.create("Helena", true, birthState, 
			birthCountry);
		final Integer socialSecurityNumber = 123456789;
		String stateId = "234";
	
		// Action
		this.createRelationshipsService.createRelation("Smith", "John", "Don", 
			"Mr.", Sex.MALE, birthDate, birthCountry, birthState, birthCity, 
			socialSecurityNumber, stateId, true, deathDate);
		this.createRelationshipsService.createRelation("Smith", "John", "Don", 
			"Mr.", Sex.MALE, birthDate, birthCountry, birthState, birthCity, 
			socialSecurityNumber, stateId, true, deathDate);
	}
}