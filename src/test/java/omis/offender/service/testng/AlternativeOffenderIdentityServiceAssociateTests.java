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
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.offender.domain.Offender;
import omis.offender.service.AlternativeOffenderIdentityService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.PersonName;
import omis.person.exception.AlternativeIdentityAssociationExistsException;
import omis.person.exception.AlternativeNameAssociationExistsException;
import omis.person.exception.PersonIdentityExistsException;
import omis.person.exception.PersonNameExistsException;
import omis.person.service.delegate.AlternativeIdentityCategoryDelegate;
import omis.person.service.delegate.AlternativeNameAssociationDelegate;
import omis.person.service.delegate.AlternativeNameCategoryDelegate;
import omis.person.service.delegate.PersonNameDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.exception.StateExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to associate alternative offender identity.
 *
 * @author Sheronda Vaughn
 * @author Stephen Abson
 * @version 0.0.1 (Feb 22, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"alternativeIdentity", "service"})
public class AlternativeOffenderIdentityServiceAssociateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personNameDelegate")
	private PersonNameDelegate personNameDelegate;
	
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
	@Qualifier("alternativeNameCategoryDelegate")
	private AlternativeNameCategoryDelegate alternativeNameCategoryDelegate;
	
	@Autowired
	@Qualifier("alternativeIdentityCategoryDelegate")
	private AlternativeIdentityCategoryDelegate 
		alternativeIdentityCategoryDelegate;
	
	@Autowired
	@Qualifier("alternativeNameAssociationDelegate")
	AlternativeNameAssociationDelegate alternativeNameAssociationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("alternativeOffenderIdentityService")
	private AlternativeOffenderIdentityService 
		alternativeOffenderIdentityService;

	/* Test methods. */

	/**
	 * Tests association of alternative identity.
	 * 
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 * @throws CityExistsException if city exists
	 * @throws PersonNameExistsException if person name exists
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists
	 * @throws AlternativeIdentityAssociationExistsException if alternative
	 * identity association exists 
	 * @throws PersonIdentityExistsException if person identity exists
	 */
	public void testAssociate()
			throws CountryExistsException,
				StateExistsException,
				CityExistsException,
				PersonNameExistsException,
				AlternativeNameAssociationExistsException,
				AlternativeIdentityAssociationExistsException,
				PersonIdentityExistsException {
		
		// Arrangements
		final String lastName =  "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		final String suffix = "Suffix";
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
		DateRange dateRange = new DateRange(
				this.parseDateText("01/01/1968"), null);				
		AlternativeNameCategory category = this.alternativeNameCategoryDelegate
				.create("categoryDescription", "categoryName", (short) 1, true);
		AlternativeIdentityCategory altIDCategory 
			= this.alternativeIdentityCategoryDelegate.create(
					"altIdCatDesc", "altIDCatName", (short) 1, true);
		Offender offender = this.offenderDelegate.create(
				lastName, firstName, middleName, suffix, socialSecurityNumber, 
				stateIdNumber, birthDate, birthCountry, birthState, birthPlace, 
				sex);	
		final String lastName2 = "NewLastName";
		final String firstName2 = "NewFirstName";
		final String middleName2 = "NewMiddleName";
		final String suffix2 = "NewSuffix";
		PersonName personName = this.personNameDelegate.create(
				offender, lastName2, firstName2, middleName2, suffix2);
		AlternativeNameAssociation alternativeNameAssociation 
			= this.alternativeNameAssociationDelegate.create(
					personName, dateRange, category);
		offender = this.offenderDelegate.createWithoutIdentity(
				lastName2, firstName2, middleName2, suffix2);
		
		// Action
		AlternativeIdentityAssociation alternativeIdentityAssociation 
			= this.alternativeOffenderIdentityService.associate(offender, 
					alternativeNameAssociation, birthDate, birthCountry, 
					birthPlace, socialSecurityNumber, stateIdNumber, sex, 
					dateRange, altIDCategory);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("identity.person", offender)
			.addExpectedValue("alternativeNameAssociation", 
					alternativeNameAssociation)
			.addExpectedValue("identity.birthDate", birthDate)
			.addExpectedValue("identity.birthCountry", birthCountry)
			.addExpectedValue("identity.birthPlace", birthPlace)
			.addExpectedValue("identity.socialSecurityNumber", 
					socialSecurityNumber)
			.addExpectedValue("identity.stateIdNumber", stateIdNumber)
			.addExpectedValue("identity.sex", sex)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("category", altIDCategory)
			.performAssertions(alternativeIdentityAssociation);
	}

	/**
	 * Tests that duplicate person identities are prevented on association.
	 * 
	 * @throws PersonIdentityExistsException if person identity exists
	 * - asserted
	 * @throws AlternativeIdentityAssociationExistsException if alternative
	 * identity association exists
	 * @throws AlternativeNameAssociationExistsException if alternative name
	 * association exists
	 * @throws PersonNameExistsException if person name exists
	 * @throws CityExistsException if city exists
	 * @throws StateExistsException if State exists
	 * @throws CountryExistsException if country exists
	 */
	@Test(expectedExceptions = {PersonIdentityExistsException.class})
	public void testDuplicateEntityFoundException() 
			throws PersonIdentityExistsException,
				AlternativeIdentityAssociationExistsException,
				AlternativeNameAssociationExistsException,
				PersonNameExistsException,
				CityExistsException,
				StateExistsException,
				CountryExistsException {
		
		// Arrangements
		final String lastName =  "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		final String suffix = "Suffix";
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
		DateRange dateRange = new DateRange(
				this.parseDateText("01/01/1968"), null);
				final String lastName2 = "NewLastName";
		final String firstName2 = "NewFirstName";
		final String middleName2 = "NewMiddleName";
		final String suffix2 = "NewSuffix";		
		AlternativeNameCategory category = this.alternativeNameCategoryDelegate
				.create("categoryDescription", "categoryName", (short) 1, true);
		AlternativeIdentityCategory altIDCategory 
			= this.alternativeIdentityCategoryDelegate.create(
					"altIdCatDesc", "altIDCatName", (short) 1, true);
		Offender offender = this.offenderDelegate.create(
				lastName, firstName, middleName, suffix, socialSecurityNumber, 
				stateIdNumber, birthDate, birthCountry, birthState, birthPlace, 
				sex);
		PersonName personName = this.personNameDelegate.create(
				offender, lastName2, firstName2, middleName2, suffix2);
		AlternativeNameAssociation alternativeNameAssociation 
			= this.alternativeNameAssociationDelegate.create(
					personName, dateRange, category);
		
		// Action
		this.alternativeOffenderIdentityService.associate(offender, 
					alternativeNameAssociation, birthDate, birthCountry, 
					birthPlace, socialSecurityNumber, stateIdNumber, sex, 
					dateRange, altIDCategory);
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