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

import omis.citizenship.domain.Citizenship;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.OffenderDemographicsService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to change country of citizenship.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 8, 2018)
 * @since OMIS 3.0
 */
@Test
public class OffenderDemographicsServiceChangeCountryOfCitizenshipTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	CountryDelegate countryDelegate;

	/* Services. */

	@Autowired
	@Qualifier("offenderDemographicsService")
	private OffenderDemographicsService offenderDemographicsService;

	/* Test methods. */

	@Test
	public void testChangeCountryOfCitizenship() {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"LastName", "FirstName", "MiddleName", "Sr.");
		Country countryOfCitizenship = this.countryDelegate.findOrCreate(
				"Country", "CT", true);

		// Action
		Citizenship citizenship = this.offenderDemographicsService
				.changeCountryOfCitizenship(offender, countryOfCitizenship);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("country", countryOfCitizenship)
			.performAssertions(citizenship);
	}
}