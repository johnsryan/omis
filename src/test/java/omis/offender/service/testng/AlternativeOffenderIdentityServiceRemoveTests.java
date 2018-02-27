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
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.AlternativeOffenderIdentityService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.AlternativeNameAssociation;
import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.PersonIdentity;
import omis.person.domain.PersonName;
import omis.person.service.delegate.AlternativeIdentityAssociationDelegate;
import omis.person.service.delegate.AlternativeIdentityCategoryDelegate;
import omis.person.service.delegate.AlternativeNameAssociationDelegate;
import omis.person.service.delegate.AlternativeNameCategoryDelegate;
import omis.person.service.delegate.PersonIdentityDelegate;
import omis.person.service.delegate.PersonNameDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to TODO.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 22, 2018)
 * @since OMIS 3.0
 */
@Test
public class AlternativeOffenderIdentityServiceRemoveTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personIdentityDelegate")
	private PersonIdentityDelegate personIdentityDelegate;
	
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
	
	@Autowired
	@Qualifier("alternativeIdentityAssociationDelegate")
	private AlternativeIdentityAssociationDelegate
		alternativeIdentityAssociationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("alternativeOffenderIdentityService")
	private AlternativeOffenderIdentityService 
		alternativeOffenderIdentityService;

	/* Test methods. */

	@Test
	public void testRemove() throws DuplicateEntityFoundException {		
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
		AlternativeIdentityCategory alternativeIdentityCategory 
			= this.alternativeIdentityCategoryDelegate.create(
				"altIdCatDesc", "altIDCatName", (short) 1, true);		
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				lastName, firstName, middleName, suffix);	
		PersonIdentity personIdentity = this.personIdentityDelegate.create(
				offender, sex, birthDate, birthCountry, birthState, birthPlace, 
				socialSecurityNumber, stateIdNumber, null, null);
		final String lastName2 = "NewLastName";
		final String firstName2 = "NewFirstName";
		final String middleName2 = "NewMiddleName";
		final String suffix2 = "NewSuffix";
		PersonName personName = this.personNameDelegate.create(
				offender, lastName2, firstName2, middleName2, suffix2);
		AlternativeNameCategory category = this.alternativeNameCategoryDelegate
				.create("categoryDescription", "categoryName", (short) 1, true);
		AlternativeNameAssociation alternativeNameAssociation 
			= this.alternativeNameAssociationDelegate.create(
					personName, dateRange, category);
		AlternativeIdentityAssociation alternativeIdentityAssociation 
			= this.alternativeIdentityAssociationDelegate.create(
					personIdentity, dateRange, alternativeIdentityCategory, 
					alternativeNameAssociation);;

		// Action
		this.alternativeOffenderIdentityService.remove(
				alternativeIdentityAssociation);

		// Assertions
		PropertyValueAsserter.create()		
			.performAssertions(alternativeIdentityAssociation);
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