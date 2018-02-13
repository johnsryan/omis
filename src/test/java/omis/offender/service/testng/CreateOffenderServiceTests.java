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

import omis.citizenship.domain.Citizenship;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.demographics.domain.DominantSide;
import omis.demographics.domain.Height;
import omis.demographics.domain.PersonDemographics;
import omis.demographics.domain.Sex;
import omis.demographics.domain.Weight;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.exception.OperationNotAuthorizedException;
import omis.immigration.domain.AlienResidence;
import omis.offender.domain.Offender;
import omis.offender.exception.OffenderExistsException;
import omis.offender.service.CreateOffenderService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.offenderflag.domain.FlagUsage;
import omis.offenderflag.domain.OffenderFlag;
import omis.offenderflag.domain.OffenderFlagCategory;
import omis.offenderflag.service.delegate.OffenderFlagCategoryDelegate;
import omis.offenderphoto.domain.OffenderPhotoAssociation;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.religion.domain.Religion;
import omis.religion.domain.ReligionGroup;
import omis.religion.domain.ReligiousPreference;
import omis.religion.service.delegate.ReligionDelegate;
import omis.religion.service.delegate.ReligionGroupDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests service to create offenders.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 16, 2016)
 * @since OMIS 3.0
 */
@Test(groups = "offender")
public class CreateOffenderServiceTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;

	@Autowired
	@Qualifier("createOffenderService")
	private CreateOffenderService createOffenderService;
	
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
	@Qualifier("religionDelegate")
	private ReligionDelegate religionDelegate;
	
	@Autowired
	@Qualifier("religionGroupDelegate")
	private ReligionGroupDelegate religionGroupDelegate;
	
	@Autowired
	@Qualifier("offenderFlagCategoryDelegate")
	private OffenderFlagCategoryDelegate offenderFlagCategoryDelegate;
	
	/**
	 * Tests creation of offender.
	 * 
	 * @throws DuplicateEntityFoundException if offender exists
	 * @throws ParseException if birth date cannot be parsed 
	 */
	public void testOffenderCreation()
			throws DuplicateEntityFoundException, ParseException {
		//Arrangements	
		final String lastName = "Blofeldt";
		final String firstName = "Ernst";
		final String middleName = "Stavro";
		final String suffix = "Jr";
		final Integer socialSecurityNumber = 123456789;
		final String stateIdNumber = "12345";
		final String birthPlaceName = "Bozeman";
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthPlace = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Date birthDate = new SimpleDateFormat("MM/dd/yyyy")
			.parse("02/17/1978");
		final Sex sex = Sex.MALE;
		
		//Action
		Offender offender = 
				this.createOffenderService.create(lastName, 
				firstName, middleName, suffix, socialSecurityNumber, 
				stateIdNumber, birthDate, birthCountry, birthPlace, sex);
	
		// Assert
		PropertyValueAsserter.create()
			.addExpectedValue("name.lastName", lastName)
			.addExpectedValue("name.firstName", firstName)
			.addExpectedValue("name.middleName", middleName)
			.addExpectedValue("name.suffix", suffix)
			.addExpectedValue("identity.socialSecurityNumber", 
					socialSecurityNumber)
			.addExpectedValue("identity.stateIdNumber", stateIdNumber)
			.addExpectedValue("identity.birthDate", birthDate)
			.addExpectedValue("identity.birthCountry", birthCountry)
			.addExpectedValue("identity.birthPlace", birthPlace)
			.addExpectedValue("identity.sex", sex)
			.performAssertions(offender);
	}
	
	/**
	 * Tests conversion of person to offender.
	 *
	 *
	 * @throws DuplicateEntityFoundException if offender exists
	 * @throws ParseException if birth date cannot be parsed 
	 * @throws OffenderExistsException if person being converted 
	 * exists as offender
	 */
	public void testOffenderPersonConversion() 
			throws DuplicateEntityFoundException, ParseException, 
			OffenderExistsException {
		//Arrangements
		final String lastName = "Blofeldt";
		final String firstName = "Ernst";
		final String middleName = "Stavro";
		final String suffix = "Jr";
		final Integer socialSecurityNumber = 123456789;
		final String stateIdNumber = "12345";
		final String birthPlaceName = "Bozeman";
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthPlace = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Date birthDate = new SimpleDateFormat("MM/dd/yyyy")
			.parse("02/17/1978");
		final Sex sex = Sex.MALE;
		Person person = this.personDelegate.create(
				lastName, firstName, middleName, suffix);
		Offender offender = this.offenderDelegate.create(lastName, firstName, 
				middleName, suffix, socialSecurityNumber, stateIdNumber, 
				birthDate, birthCountry, birthState, birthPlace, sex);
		
		//Action	
		offender = this.createOffenderService.convertPerson(person, 
				 lastName, firstName, middleName, suffix, socialSecurityNumber, 
				 stateIdNumber, birthDate, birthCountry, birthPlace, sex);
		
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("name.lastName", lastName)
			.addExpectedValue("name.firstName", firstName)
			.addExpectedValue("name.middleName", middleName)
			.addExpectedValue("name.suffix", suffix)
			.addExpectedValue("identity.socialSecurityNumber", 
					socialSecurityNumber)
			.addExpectedValue("identity.stateIdNumber", stateIdNumber)
			.addExpectedValue("identity.birthDate", birthDate)
			.addExpectedValue("identity.birthCountry", birthCountry)
			.addExpectedValue("identity.birthPlace", birthPlace)
			.addExpectedValue("identity.sex", sex)
			.performAssertions(offender);
	}	

	/**
	 * Tests adding demographics.
	 *
	 *
	 * @throws DuplicateEntityFoundException if offender exists
	 */
	public void testAddDemographics() throws DuplicateEntityFoundException {
		//Arrangements
		Offender offender = this.offenderDelegate.create(
				"Blofeldt", "Ernst", "Stavro", "Jr", null, null, null, null, 
				null, null, null);
		final Integer feet = 6;
		final Integer inches = 4;		
		Height height = new Height(feet, inches);
		final Integer pounds = 250;
		Weight weight = new Weight(pounds);
		PersonPhysique physique 
			= new PersonPhysique(height, weight, null);
		PersonAppearance appearance = new PersonAppearance();
		DominantSide dominantSide = DominantSide.BOTH;
		Boolean hispanicEthnicity = false;
		
		//Action
		PersonDemographics demographics = this.createOffenderService
				.addDemographics(offender, physique, appearance, dominantSide, 
						null, hispanicEthnicity, null, null);
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("person", offender)
			.addExpectedValue("physique", physique)
			.addExpectedValue("appearance", appearance)
			.addExpectedValue("dominantSide", dominantSide)
			.addExpectedValue("race", null)
			.addExpectedValue("hispanicEthnicity", hispanicEthnicity)
			.addExpectedValue("tribe", null)
			.addExpectedValue("maritalStatus", null)
			.performAssertions(demographics);
	}
	
	/**
	 * Tests adding religious preference.
	 *
	 *
	 * @throws DuplicateEntityFoundException if religious preference exists
	 * @throws DateConflictException if date conflict
	 * @throws OperationNotAuthorizedException if not authorized to add
	 */
	public void testAddReligiousPreference() 
			throws DuplicateEntityFoundException, DateConflictException, 
			OperationNotAuthorizedException {
		//Arrangements
		Offender offender = this.offenderDelegate.create(
				"Blofeldt", "Ernst", "Stavro", "Jr", null, null, null, null, 
				null, null, null);
		ReligionGroup group = this.religionGroupDelegate.create("GroupName", 
				(short) 1, true);
		Religion religion = this.religionDelegate.create(
				"ReligionNamed", group, (short) 1, true);
	
		//Action
		ReligiousPreference preference = this.createOffenderService
				.addReligiousPreference(offender, religion);
		
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("religion", religion)
			.performAssertions(preference);				
	}
	
	/**
	 * Test setting the country of citizenship.
	 *
	 *
	 * @throws DuplicateEntityFoundException duplicate entity found 
	 */
	public void testSetCountryOfCitizenship() 
			throws DuplicateEntityFoundException {
		//Arrangements
		Offender offender = this.offenderDelegate.create(
				"Blofeldt", "Ernst", "Stavro", "Jr", null, null, null, null, 
				null, null, null);
		final Country country = this.countryDelegate
				.create("United Status", "US", true);
		
		//Action
		Citizenship citizenship 
			= this.createOffenderService.setCountryOfCitizenship(
					offender, country);
		
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("country", country)
			.performAssertions(citizenship);
	}
	
	/**
	 * Test setting the legal residence status.
	 *
	 */
	public void testSetLegalResidenceStatus() {
		//Arrangements
		Offender offender = this.offenderDelegate.create(
				"Blofeldt", "Ernst", "Stavro", "Jr", null, null, null, null, 
				null, null, null);
		Boolean legal = true;
		
		//Action
		AlienResidence legalResidence = this.createOffenderService
				.setLegalResidenceStatus(offender, legal);
		
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("legal", legal)
			.performAssertions(legalResidence);
	}
	
	/**
	 * Tests setting offender flag.
	 *
	 *
	 * @throws DuplicateEntityFoundException if existing offender flag set
	 */
	public void testOffenderFlag() throws DuplicateEntityFoundException {
		//Arrangements
		Offender offender = this.offenderDelegate.create(
				"Blofeldt", "Ernst", "Stavro", "Jr", null, null, null, null, 
				null, null, null);
		final String name = "CategoryName";
		final String description = "some category description";
		final Boolean required = false;
		final FlagUsage usage = FlagUsage.SECURITY;
		final Boolean value = true;
		OffenderFlagCategory category = this.offenderFlagCategoryDelegate
				.createOffenderFlagCategory(
						name, description, required, (short) 1, usage);
		//Action
		OffenderFlag flag = this.createOffenderService.setFlag(
				offender, category, value);		
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("category", category)
			.addExpectedValue("value", value)
			.performAssertions(flag);
	}
	
	/**
	 * Tests an associated profile photo.
	 *
	 *
	 * @throws DuplicateEntityFoundException duplicate entity found
	 */
	public void testAssociateProfilePhoto() 
			throws DuplicateEntityFoundException {
		//Arrangements
		Offender offender = this.offenderDelegate.create(
				"Blofeldt", "Ernst", "Stavro", "Jr", null, null, null, null, 
				null, null, null);
		final String filename = "ProfileName";
		Date photoDate = this.parseDateText("02/17/1978");
		//Action
		OffenderPhotoAssociation profilePhoto = this.createOffenderService
				.associateProfilePhoto(offender, filename, photoDate);		
		//Assert
		PropertyValueAsserter.create()
			.performAssertions(profilePhoto);
	}
	
	/**
	 * Create city.
	 *
	 *
	 * @throws DuplicateEntityFoundException duplicate entity
	 */
	public void createCity() throws DuplicateEntityFoundException {
		//Arrangements
		final String cityName = "CityName";
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		
		//Action
		City newCity = this.createOffenderService.createCity(
				cityName, birthState, birthCountry);
		
		//Assert
		PropertyValueAsserter.create()
			.addExpectedValue("name", cityName)
			.addExpectedValue("state", birthState)
			.addExpectedValue("country", birthCountry)
			.performAssertions(newCity);
	}
	
	/**
	 * Tests offender exists.
	 *
	 *
	 * @throws DuplicateEntityFoundException duplciate entity found
	 * @throws ParseException parse
	 * @throws OffenderExistsException offender exists
	 */
	@Test(expectedExceptions = {OffenderExistsException.class})
	public void testOffenderExistsException() 
			throws DuplicateEntityFoundException, ParseException, 
			OffenderExistsException {
		final String lastName = "Blofeldt";
		final String firstName = "Ernst";
		final String middleName = "Stavro";
		final String suffix = "Jr";	
		Person person = this.personDelegate.create(
				lastName, firstName, middleName, suffix);
		Offender offender = this.offenderDelegate.convertPerson(person);
		
		this.createOffenderService.convertPerson(
				offender, lastName, firstName, middleName, suffix, 
				null, null, null, null, null, null);
	}
	
	//This method is not currently enabled... there is not a duplicate check for
	//person or offender because old OMIS2 has duplicated date and still needs
	//to be converted for use into OMIS3
	//No unique key to test against. sv
	@Test(expectedExceptions = {DuplicateEntityFoundException.class}, 
			enabled = false)
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException, ParseException {
		//Arrangements	
		final String lastName = "Blofeldt";
		final String firstName = "Ernst";
		final String middleName = "Stavro";
		final String suffix = "Jr";
		final Integer socialSecurityNumber = 123456789;
		final String stateIdNumber = "12345";
		final String birthPlaceName = "Bozeman";
		final Country birthCountry = this.countryDelegate
				.create("United Status", "US", true);
		final State birthState = this.stateDelegate
				.create("Montana", "MT", birthCountry, true, true);
		final City birthPlace = this.cityDelegate.create(
				birthPlaceName, true, birthState, birthCountry);
		final Date birthDate = this.parseDateText("02/17/1978");
		final Sex sex = Sex.MALE;
		this.offenderDelegate.create(lastName, firstName, middleName, 
				suffix, socialSecurityNumber, stateIdNumber, birthDate, 
				birthCountry, birthState, birthPlace, sex);
		
		//Action
		this.createOffenderService.create(lastName, firstName, 
				middleName, suffix, socialSecurityNumber, 
				stateIdNumber, birthDate, birthCountry, birthPlace, 
				sex);
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