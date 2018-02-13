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
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;
import omis.victim.exception.VictimNoteCategoryExistsException;
import omis.victim.exception.VictimNoteExistsException;
import omis.victim.service.VictimAssociationService;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.victim.service.delegate.VictimNoteCategoryDelegate;
import omis.victim.service.delegate.VictimNoteDelegate;

/**
 * Test victim association note creation.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 31, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"victim"})
public class VictimAssociationServiceVictimAssociationNoteCreateTests
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
	
	@Autowired
	@Qualifier("victimNoteDelegate")
	private VictimNoteDelegate victimNoteDelegate;
	
	@Autowired
	@Qualifier("victimNoteCategoryDelegate")
	private VictimNoteCategoryDelegate victimNoteCategoryDelegate;
		
	/* Services. */
	
	@Autowired
	@Qualifier("victimAssociationService")
	private VictimAssociationService victimAssociationService;
	
	/**
	 * Test add victim note.
	 *
	 *
	 * @throws VictimNoteExistsException victim note exists 
	 * @throws VictimExistsException victim exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws VictimNoteCategoryExistsException victim note category
	 */
	@Test
	public void testAddNote() throws VictimNoteExistsException, 
		VictimExistsException, ReflexiveRelationshipException, 
		VictimNoteCategoryExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person person = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);			
		VictimAssociation victimAssociation = this.victimAssociationService
				.associate(offender, person, registeredDate, packetSent,
						packetSendDate, flags);
		Date victimAssociationNoteDate = this.parseDateText("01/01/2017");
		String value = "Testing note creation";
		VictimNoteCategory victimNoteCategory = this.victimNoteCategoryDelegate
				.create("categoryName", (short) 1, true, true);
		//Action
		VictimNote note = this.victimAssociationService.addNote(
				victimAssociation, victimNoteCategory, 
				victimAssociationNoteDate, value);
		
		//Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("value", value)
			.addExpectedValue("date", victimAssociationNoteDate)
			.addExpectedValue("category", victimNoteCategory)
			.addExpectedValue("association", victimAssociation)
			.performAssertions(note);
	}
	
	/**
	 * Test disassociate victim note.
	 *
	 *
	 * @throws VictimNoteExistsException victim note exists 
	 * @throws VictimExistsException victim exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws VictimNoteCategoryExistsException victim note category
	 */
	@Test
	public void testDisassociateNote() throws VictimExistsException, 
		ReflexiveRelationshipException, VictimNoteExistsException, 
		VictimNoteCategoryExistsException {		
		//Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person victim = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);			
		VictimAssociation victimAssociation = this.victimAssociationService
				.associate(offender, victim, registeredDate, packetSent,
						packetSendDate, flags);
		Date victimAssociationNoteDate = this.parseDateText("01/01/2017");
		String value = "Testing note creation";
		VictimNoteCategory victimNoteCategory = this.victimNoteCategoryDelegate
				.create("categoryName", (short) 1, true, true);
		VictimNote victimNote = this.victimNoteDelegate.create(
				victim, victimNoteCategory, victimAssociation, 
				victimAssociationNoteDate, value);
		
		//Action
		VictimNote note = this.victimAssociationService
				.disassociateNote(victimNote);
		
		//Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("victim", victimNote.getVictim())
		.addExpectedValue("association", victimNote.getAssociation())
		.addExpectedValue("date", victimNote.getDate())
		.addExpectedValue("category", victimNote.getCategory())
		.addExpectedValue("value", victimNote.getValue())
		.performAssertions(note);
	}
	
	/**
	 * Tests {@code VictimNoteExistsException} is thrown.
	 *
	 *
	 * @throws VictimNoteExistsException victim note exists 
	 * @throws VictimExistsException victim exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws VictimNoteCategoryExistsException victim note category
	 */
	@Test(expectedExceptions = {VictimNoteExistsException.class})
	public void testVictimNoteExistsException() throws VictimExistsException, 
		ReflexiveRelationshipException, VictimNoteCategoryExistsException, 
		VictimNoteExistsException {
		//Arrangements
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Greene", "Dominic", "", "");
		Person person = this.personDelegate
				.create("Mathis", "Rene", "", "");
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);			
		VictimAssociation victimAssociation = this.victimAssociationService
				.associate(offender, person, registeredDate, packetSent,
						packetSendDate, flags);
		Date victimAssociationNoteDate = this.parseDateText("01/01/2017");
		String value = "Testing note creation";
		VictimNoteCategory victimNoteCategory = this.victimNoteCategoryDelegate
				.create("categoryName", (short) 1, true, true);
		this.victimNoteDelegate.create(
				person, victimNoteCategory, victimAssociation, 
				victimAssociationNoteDate, value);
		
		//Action
		this.victimAssociationService.addNote(victimAssociation, 
				victimNoteCategory, victimAssociationNoteDate, value);
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