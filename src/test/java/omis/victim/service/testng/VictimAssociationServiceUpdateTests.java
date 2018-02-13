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

import java.beans.PropertyEditor;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.beans.factory.spring.CustomDateEditorFactory;
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
 * Tests update of victim.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"victimAssociation", "update"})
public class VictimAssociationServiceUpdateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Property editor factories. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Service delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("relationshipDelegate")
	private RelationshipDelegate relationshipDelegate;
	
	@Autowired
	@Qualifier("victimAssociationDelegate")
	private VictimAssociationDelegate victimAssociationDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("victimAssociationService")
	private VictimAssociationService victimAssociationService;
	
	/* Test methods. */
	
	/**
	 * Victim association update.
	 *
	 *
	 * @throws VictimExistsException victim exists 
	 * @throws RelationshipExistsException relationship exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 */
	@Test
	public void testUpdate() 
			throws VictimExistsException, RelationshipExistsException, 
			ReflexiveRelationshipException {		
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person person = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		Relationship relationship 
			= this.relationshipDelegate.findOrCreate(offender, person);
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);				
		VictimAssociation victimAssociation 
			= this.victimAssociationDelegate.create(relationship, 
				registeredDate, packetSent, packetSendDate, flags);
		
		// Action
		victimAssociation = this.victimAssociationService.update(
				victimAssociation, registeredDate, packetSent, 
				packetSendDate, flags);					
		
		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("relationship.firstPerson", offender)
		.addExpectedValue("relationship.secondPerson", person)
		.addExpectedValue("registerDate", registeredDate)
		.addExpectedValue("packetSent", packetSent)
		.addExpectedValue("packetSendDate", packetSendDate)
		.addExpectedValue("flags", flags)
			.performAssertions(victimAssociation);
	}
	
	/**
	 * Tests duplicate victim association on update.
	 *
	 *
	 * @throws VictimExistsException victim exists 
	 * @throws RelationshipExistsException relationship exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 */
	@Test(expectedExceptions = {VictimExistsException.class, 
			ReflexiveRelationshipException.class})
	public void testVictimAssociationExistsException() 
			throws VictimExistsException, RelationshipExistsException, 
			ReflexiveRelationshipException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person person = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		Relationship relationship 
			= this.relationshipDelegate.findOrCreate(offender, person);
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true); 
		this.victimAssociationDelegate.create(relationship, 
				registeredDate, packetSent, packetSendDate, flags);
		this.victimAssociationService
				.associate(offender, person, registeredDate, packetSent,
						packetSendDate, flags);
		Date registeredDate2 = this.parseDateText("02/11/2014");
		Boolean packetSent2 = false;
		Date packetSendDate2 = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags2 = new VictimAssociationFlags();
		flags2.setBusiness(false);
		flags2.setDocRegistered(true);
		flags2.setVictim(false);
		flags2.setVineRegistered(false);
		VictimAssociation victimAssociation1
			= this.victimAssociationService.associate(offender, person, 
					registeredDate2, packetSent2, packetSendDate2, 
					flags2);
		
		// Action
		this.victimAssociationService.update(
				victimAssociation1, registeredDate2, packetSent, 
				packetSendDate2, flags2);	
	}
	
	/* Helper methods. */
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		PropertyEditor propertyEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		propertyEditor.setAsText(dateText);
		return (Date) propertyEditor.getValue();
	}
}