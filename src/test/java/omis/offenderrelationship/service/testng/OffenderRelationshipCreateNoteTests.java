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
package omis.offenderrelationship.service.testng;
import java.util.Date;

import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.offenderrelationship.service.CreateRelationshipsService;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.relationship.service.delegate.RelationshipNoteCategoryDelegate;
import omis.relationship.service.delegate.RelationshipNoteDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

/**
 * Test offender relationship "create note" service method.
 *
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"offenderRelationship"})
public class OffenderRelationshipCreateNoteTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Delegates. */
	@Autowired
	@Qualifier("relationshipDelegate")
	private RelationshipDelegate relationshipDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("relationshipNoteCategoryDelegate")
	private RelationshipNoteCategoryDelegate relationshipNoteCategoryDelegate;
	
	@Autowired
	@Qualifier("relationshipNoteDelegate")
	private RelationshipNoteDelegate relationshipNoteDelegate;
	
	/* Services */
	@Autowired
	@Qualifier("createRelationshipsService")
	private CreateRelationshipsService createRelationshipsService;
	
	/**
	 * Tests the creation of offender relationship notes.
	 * @throws DuplicateEntityFoundException 
	 * @throws ReflexiveRelationshipException 
	 */
	@Test
	public void testOffenderRelationshipCreateNote() 
		throws DuplicateEntityFoundException, ReflexiveRelationshipException,
		RelationshipNoteExistsException {
		// Arrangement
		Offender firstPerson = this.offenderDelegate.createWithoutIdentity(
				"testLastName1", "testFirstName1", "testMiddleName1", "Mr.");
		Person secondPerson = this.personDelegate.create("testLastName2", 
			"testFirstName2", "testMiddleName2", "Ms.");
		final int shortInt = 12;
		RelationshipNoteCategory relationshipNoteCategory 
			= this.relationshipNoteCategoryDelegate.create(
			"relationshipNoteCategory1", new Short((short) shortInt));
		final int dateInt = 111111111;
		Date date = new Date(dateInt);
		
		// Action
		RelationshipNote relationshipNote = this.createRelationshipsService
			.createNote(firstPerson, secondPerson, relationshipNoteCategory,
					"Note tests", date);
	
		// Assertions
		assert firstPerson.equals(relationshipNote.getRelationship()
			.getFirstPerson())
		: String.format("Wrong First Person: #%s #%s expected; #%s #%s found",
			firstPerson.getName().getFirstName(), 
			firstPerson.getName().getLastName(), 
			relationshipNote.getRelationship().getFirstPerson().getName()
				.getFirstName(),
			relationshipNote.getRelationship().getFirstPerson().getName()
				.getLastName());
		
		assert secondPerson.equals(relationshipNote.getRelationship()
			.getSecondPerson())
		: String.format("Wrong second Person: #%s #%s expected; #%s #%s found",
			secondPerson.getName().getFirstName(), 
			secondPerson.getName().getLastName(), 
			relationshipNote.getRelationship().getSecondPerson().getName()
				.getFirstName(),
			relationshipNote.getRelationship().getSecondPerson().getName()
				.getLastName());
		
		assert relationshipNoteCategory.equals(relationshipNote.getCategory())
		: String.format("Wrong Relationship Note Category: "
				+ "#%s expected; #%s found",
			relationshipNoteCategory.getName(), 
			relationshipNote.getCategory().getName());
		
		assert date.equals(relationshipNote.getDate())
		: String.format("Wrong Date: #%s expected; #%s found", date, 
			relationshipNote.getDate());
	}
	
	/**
	 * Tests duplicate offender relationship notes on creation.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate notes exist
	 * @throws ReflexiveRelationshipException ReflexiveRelationshipException
	 */
	@Test(expectedExceptions = {RelationshipNoteExistsException.class})
	public void testDuplicateOffenderRelationshipsCreate() 
			throws DuplicateEntityFoundException, ReflexiveRelationshipException,
			RelationshipNoteExistsException {
		
		// Assignment
		Offender firstPerson = this.offenderDelegate.createWithoutIdentity(
				"testLastName1", "testFirstName1", "testMiddleName1", "Mr.");
		Person secondPerson = this.personDelegate.create("testLastName2", 
			"testFirstName2", "testMiddleName2", "Ms.");
		Relationship relationship = this.relationshipDelegate.create(
			firstPerson, secondPerson);
		final int shortInt = 12;
		final int dateInt = 111111111;
		RelationshipNoteCategory relationshipNoteCategory 
			= this.relationshipNoteCategoryDelegate.create(
			"relationshipNoteCategory1", new Short((short) shortInt));
		Date date = new Date(dateInt);
		this.relationshipNoteDelegate.create(relationship, 
			relationshipNoteCategory, "Note tests",	date);
		
		// Action
		this.createRelationshipsService.createNote(firstPerson, secondPerson, 
			relationshipNoteCategory, "Note tests",	date);
	}
}
