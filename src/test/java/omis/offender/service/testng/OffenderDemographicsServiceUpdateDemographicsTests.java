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

import omis.demographics.domain.DominantSide;
import omis.demographics.domain.EyeColor;
import omis.demographics.domain.HairColor;
import omis.demographics.domain.Height;
import omis.demographics.domain.MaritalStatus;
import omis.demographics.domain.PersonDemographics;
import omis.demographics.domain.Race;
import omis.demographics.domain.Tribe;
import omis.demographics.domain.Weight;
import omis.demographics.domain.component.PersonAppearance;
import omis.demographics.domain.component.PersonPhysique;
import omis.demographics.service.delegate.EyeColorDelegate;
import omis.demographics.service.delegate.HairColorDelegate;
import omis.demographics.service.delegate.MaritalStatusDelegate;
import omis.demographics.service.delegate.PersonDemographicsDelegate;
import omis.demographics.service.delegate.RaceDelegate;
import omis.demographics.service.delegate.TribeDelegate;
import omis.offender.service.OffenderDemographicsService;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update offender demographics.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Feb 8, 2018)
 * @since OMIS 3.0
 */
@Test
public class OffenderDemographicsServiceUpdateDemographicsTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("raceDelegate")
	private RaceDelegate raceDelegate;
	
	@Autowired
	@Qualifier("maritalStatusDelegate")
	private MaritalStatusDelegate maritalStatusDelegate;
	
	@Autowired
	@Qualifier("personDemographicsDelegate")
	private PersonDemographicsDelegate personDemographicsDelegate;
	
	@Autowired
	@Qualifier("hairColorDelegate")
	private HairColorDelegate hairColorDelegate;
	
	@Autowired
	@Qualifier("eyeColorDelegate")
	private EyeColorDelegate eyeColorDelegate;
	
	@Autowired
	@Qualifier("tribeDelegate")
	private TribeDelegate tribeDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("offenderDemographicsService")
	private OffenderDemographicsService offenderDemographicsService;

	/* Test methods. */

	@Test
	public void testUpdateDemographics() {
		// Arrangements
		Person person = this.personDelegate.create(
				"Demographics", "Person", null, null);
		this.personDemographicsDelegate.find(person);
		HairColor hairColor = this.hairColorDelegate.create("Black", true);
		EyeColor eyeColor = this.eyeColorDelegate.create("Hazel", true);
		Tribe tribe = this.tribeDelegate.create("Tribe", true);
		PersonAppearance appearance = new PersonAppearance();
		appearance.setHairColor(hairColor); 
		appearance.setEyeColor(eyeColor);
		Race race = this.raceDelegate.create("Race", true);
		MaritalStatus maritalStatus = this.maritalStatusDelegate.create(
				"MaritalStatus", true);
		Height height = new Height(); 
		height.setFeet(6);
		height.setInches(4);
		Weight weight = new Weight();
		weight.setPounds(275);
		PersonPhysique physique = new PersonPhysique();
		physique.setHeight(height);
		physique.setWeight(weight);
		this.personDemographicsDelegate.create(person, appearance, race, false, 
				physique, DominantSide.RIGHT, maritalStatus, null);
		PersonPhysique newPhysique = new PersonPhysique();
		newPhysique.setHeight(height);
		newPhysique.setWeight(weight);

		// Action
		PersonDemographics personDemographics 
			= this.offenderDemographicsService.updateDemographics(
					person, newPhysique, appearance, DominantSide.RIGHT, race, 
					false, tribe, maritalStatus);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("person", person)
			.addExpectedValue("physique", newPhysique)
			.addExpectedValue("appearance", appearance)
			.addExpectedValue("dominantSide", DominantSide.RIGHT)
			.addExpectedValue("race", race)
			.addExpectedValue("hispanicEthnicity", false)
			.addExpectedValue("tribe", tribe)
			.addExpectedValue("maritalStatus", maritalStatus)
			.performAssertions(personDemographics);
	}
}