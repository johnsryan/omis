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
package omis.relationship.service.delegate.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.testng.annotations.Test;

import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipExistsException;
import omis.relationship.exception.RelationshipNoteCategoryExistsException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.relationship.service.delegate.RelationshipNoteCategoryDelegate;
import omis.relationship.service.delegate.RelationshipNoteDelegate;
import omis.service.delegate.AssociatedEntityOperationRegistry;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests for relationship note removal by relationship operation implementation.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 29, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"relationship", "serviceOperation"})
public class RelationshipNoteRemovalByRelationshipOperationImplTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Property editor factory. */
	
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
	@Qualifier("relationshipNoteDelegate")
	private RelationshipNoteDelegate relationshipNoteDelegate;
	
	@Autowired
	@Qualifier("relationshipNoteCategoryDelegate")
	private RelationshipNoteCategoryDelegate relatinshipNoteCategoryDelegate;
	
	/* Operations registry. */
	
	@Autowired
	@Qualifier("relationshipAssociatedRemovalOperationRegistry")
	private AssociatedEntityOperationRegistry<Relationship>
	relationshipAssociatedRemovalOperationRegistry;
	
	/* Tests */
	
	/**
	 * Tests removal of relationship notes by relationship using operation.
	 * 
	 * @throws ReflexiveRelationshipException if two person of relationship are
	 * the same type
	 * @throws RelationshipExistsException if relationship exists 
	 * @throws RelationshipNoteCategoryExistsException if relationship note
	 * category exists
	 * @throws RelationshipNoteExistsException if relationship note exists 
	 */
	public void testRemoval()
				throws RelationshipExistsException,
					ReflexiveRelationshipException,
					RelationshipNoteCategoryExistsException,
					RelationshipNoteExistsException {
		
		// Arrangements - relates victim and offender
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Person victim = this.personDelegate
				.create("di Vincenzo", "Teresa", null, null);
		Relationship relationship = this.relationshipDelegate
				.create(offender, victim);
		RelationshipNoteCategory generalCategory
			= this.relatinshipNoteCategoryDelegate
				.create("General", (short) 1);
		RelationshipNoteCategory contactCategory
			= this.relatinshipNoteCategoryDelegate
				.create("Contact", (short) 1);
		Date date = this.parseDateText("12/13/2018");
		this.relationshipNoteDelegate.create(
				relationship, contactCategory, "Contacted victim", date);
		this.relationshipNoteDelegate.create(
				relationship, generalCategory, "General note about victim",
				date);
		
		// Action - removes all associated entities to relationship
		this.relationshipAssociatedRemovalOperationRegistry
			.applyAll(relationship);
		
		// Asserts that relationship notes are removed
		assert this.relationshipNoteDelegate
				.countByRelationship(relationship) == 0
			: "Relationship notes exist";
	}
	
	/* Helper methods. */
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
}