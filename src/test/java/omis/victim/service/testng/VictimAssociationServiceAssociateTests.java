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
package omis.victim.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.victim.service.VictimAssociationService;
import omis.victim.service.delegate.VictimAssociationDelegate;

/**
 * Tests creation of victims.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"victimAssociation", "create"})
public class VictimAssociationServiceAssociateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Service delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("victimAssociationDelegate")
	private VictimAssociationDelegate victimAssociationDelegate;
	
	@Autowired
	@Qualifier("relationshipDelegate")
	private RelationshipDelegate relationshipDelegate;
		
	/* Services. */
	
	@Autowired
	@Qualifier("victimAssociationService")
	private VictimAssociationService victimAssociationService;
	
	/* Test methods. */
	
	/**
	 * Tests association of victim to offender.
	 * 
	 * @throws VitimExistsException if victim association exists
	 * @throws ReflexiveRelationshipException if offender is victim 
	 */
	@Test
	public void testAssociate()
			throws VictimExistsException, ReflexiveRelationshipException {
		
		// Arrangements
		Offender greene = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person mathis = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);
		
		// Action
		VictimAssociation victimAssociation = this.victimAssociationService
				.associate(greene, mathis, registeredDate, packetSent,
						packetSendDate, flags);
		
		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("relationship.firstPerson", greene)
		.addExpectedValue("relationship.secondPerson", mathis)
		.addExpectedValue("registerDate", registeredDate)
		.addExpectedValue("packetSent", packetSent)
		.addExpectedValue("packetSendDate", packetSendDate)
		.addExpectedValue("flags", flags)
		.performAssertions(victimAssociation);
	}
	
	/**
	 * Tests {@code VictimExistsException} is thrown.
	 *
	 *
	 * @throws VictimExistsException victim exists 
	 * @throws RelationshipExistsException relationship exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 */
	@Test(expectedExceptions = {VictimExistsException.class})
	public void testVictimExistsException() throws VictimExistsException, 
		RelationshipExistsException, ReflexiveRelationshipException {
		//Arrangements
		Offender firstPerson = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person secondPerson = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Relationship relationship = this.relationshipDelegate.findOrCreate(
				firstPerson, secondPerson);
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);
		this.victimAssociationDelegate.create(relationship, registeredDate, 
				packetSent, packetSendDate, flags);
		
		// Action
		this.victimAssociationService.associate(firstPerson, secondPerson, 
				registeredDate, packetSent, packetSendDate, flags);
				
	}
	
	/* Helpers. */
	
	// Parses date text
		private Date parseDateText(final String text) {
			try {
				return new SimpleDateFormat("MM/dd/yyyy").parse(text);
			} catch (ParseException e) {
				throw new RuntimeException("Parse error", e);
			}
		}
}