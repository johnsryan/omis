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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.offender.domain.Offender;
import omis.offender.service.OffenderPersonalDetailsService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.PersonName;
import omis.person.exception.PersonNameExistsException;
import omis.person.service.delegate.PersonNameDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update offender name.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 21, 2018)
 * @since OMIS 3.0
 */
@Test
public class OffenderPersonalDetailsServiceUpdateNameTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
		
	@Autowired
	@Qualifier("personNameDelegate")
	private PersonNameDelegate personNameDelegate;	

	/* Services. */

	@Autowired
	@Qualifier("offenderPersonalDetailsService")
	private OffenderPersonalDetailsService offenderPersonalDetailsService;
	 
	/* Test methods. */

	@Test
	public void testUpdateName() throws PersonNameExistsException {
		// Arrangements
		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";
		String suffix = null;
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				lastName, firstName, middleName, null);
		final String newLastName = "NewLastName";
		
		// Action
		PersonName personName = this.offenderPersonalDetailsService.updateName(
				offender, newLastName, firstName, middleName, suffix);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", offender)
			.addExpectedValue("lastName", newLastName)
			.addExpectedValue("firstName", firstName)
			.addExpectedValue("middleName", middleName)
			.addExpectedValue("suffix", suffix)
			.performAssertions(personName);
	}

	@Test(expectedExceptions = {PersonNameExistsException.class})
	public void testPersonNameExistsException() 
			throws PersonNameExistsException {
		// Arrangements		

		final String lastName = "LastName";
		final String firstName = "FirstName";
		final String middleName = "MiddleName";	
		final String newLastName = "NewLastName";
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				lastName, firstName, middleName, null);
		this.personNameDelegate.create(offender, 
				newLastName, firstName, 
				middleName, null);
		// Action
		this.offenderPersonalDetailsService.updateName(offender, newLastName, 
				firstName, middleName, null);
	}	
}