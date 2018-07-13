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

import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipExistsException;
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
import omis.victim.service.VictimNoteService;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.victim.service.delegate.VictimNoteCategoryDelegate;

/**
 * Tests method to remove victim note.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 10, 2018)
 * @since OMIS 3.0
 */
@Test
public class VictimNoteServiceRemoveTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private PersonDelegate personDelegate;

	@Autowired
	private VictimNoteCategoryDelegate victimNoteCategoryDelegate;

	@Autowired
	private VictimAssociationDelegate victimAssociationDelegate;
		
	@Autowired
	private RelationshipDelegate relationshipDelegate;

	/* Services. */

	@Autowired
	@Qualifier("victimNoteService")
	private VictimNoteService victimNoteService;

	/* Test methods. */

	@Test
	public void testRemove() throws VictimNoteExistsException, 
		VictimNoteCategoryExistsException, VictimExistsException, 
		RelationshipExistsException, ReflexiveRelationshipException {
		// Arrangements
		Person victim = this.personDelegate.create("LastName", "FirstName", 
				"MiddleName", "Suffix");
		Person firstPerson = this.personDelegate.create("LastNameC", 
				"FirstNameC", "MiddleNameC", "SuffixC");
		VictimNoteCategory category = this.victimNoteCategoryDelegate.create(
				"CategoryName", (short) 1, false, true);
		Relationship relationship = this.relationshipDelegate.create(
				firstPerson, victim);
		Date registeredDate = this.parseDateText("02/11/2014");
		Boolean packetSent = true;
		Date packetSendDate = this.parseDateText("03/01/2014");
		VictimAssociationFlags flags = new VictimAssociationFlags();
		flags.setBusiness(true);
		flags.setDocRegistered(false);
		flags.setVictim(true);
		flags.setVineRegistered(true);
		VictimAssociation association = this.victimAssociationDelegate.create(
				relationship, registeredDate, packetSent, packetSendDate, 
				flags);
		Date date = this.parseDateText("01/01/2017");
		String value = "Testing note creation";

		// Action
		VictimNote victimNote = this.victimNoteService.create(
				victim, category, association, date, value);
		
		// Assertions
		PropertyValueAsserter.create()
			.performAssertions(victimNote);
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