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
package omis.family.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.datatype.DateRange;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.domain.component.FamilyAssociationFlags;
import omis.family.exception.FamilyAssociationCategoryExistsException;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.family.exception.FamilyAssociationNoteExistsException;
import omis.family.service.FamilyAssociationService;
import omis.family.service.delegate.FamilyAssociationCategoryDelegate;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.family.service.delegate.FamilyAssociationNoteDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteCategoryExistsException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.relationship.service.delegate.RelationshipNoteCategoryDelegate;
import omis.relationship.service.delegate.RelationshipNoteDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests family association note creation.
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2
 * @since OMIS 3.0
 */
@Test(groups = {"family"})
public class FamilyAssociationServiceFamilyAssociationNoteCreateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	/* Delegates. */
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("familyAssociationCategoryDelegate")
	private FamilyAssociationCategoryDelegate familyAssociationCategoryDelegate;
	
	@Autowired
	@Qualifier("familyAssociationDelegate")
	private FamilyAssociationDelegate familyAssociationDelegate;
	
	@Autowired
	@Qualifier("relationshipDelegate")
	private RelationshipDelegate relationshipDelegate;
	
	@Autowired
	@Qualifier("familyAssociationNoteDelegate")
	private FamilyAssociationNoteDelegate familyAssociationNoteDelegate;
	
	@Autowired
	@Qualifier("relationshipNoteCategoryDelegate")
	private RelationshipNoteCategoryDelegate relationshipNoteCategoryDelegate;
	
	@Autowired
	@Qualifier("relationshipNoteDelegate")
	private RelationshipNoteDelegate relationshipNoteDelegate;
	
	/* Service to test. */
	@Autowired
	@Qualifier("familyAssociationService")
	private FamilyAssociationService familyAssociationService;
	
	/**
	 * Tests the creation of family association note.
	 * 
	 * @throws ReflexiveRelationshipException if relationship is between same 
	 * person
	 * @throws FamilyAssociationConflictException if association overlaps 
	 * @throws FamilyAssociationExistsException 
	 * @throws FamilyAssociationNoteExistsException 
	 * @throws FamilyAssociationCategoryExistsException 
	 * @throws RelationshipNoteExistsException 
	 */
	@Test()
	public void testAddNote() throws FamilyAssociationNoteExistsException, 
		ReflexiveRelationshipException, FamilyAssociationConflictException, 
		FamilyAssociationExistsException, 
		FamilyAssociationNoteExistsException, 
		FamilyAssociationCategoryExistsException,
		RelationshipNoteCategoryExistsException,
		RelationshipNoteExistsException {
		// Arrangement
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
			"Kevin", "Johns", "Mr.");
		Person familyMember = personDelegate.create("Li", "Yidong", "CIC311", 
			"Mr.");
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("12/01/2017"));
		FamilyAssociationCategory category = this
				.familyAssociationCategoryDelegate.create("testName", true, 
						(short) 1, 
						FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags = new FamilyAssociationFlags(true, true, 
				true);
		Date marriageDate = this.parseDateText("01/01/2017");
		Date divorceDate = this.parseDateText("12/01/2017");
		Relationship relationship = this.relationshipDelegate.findOrCreate(
				offender, familyMember);
		FamilyAssociation familyAssociation = this.familyAssociationDelegate
				.create(dateRange, category, flags, marriageDate, divorceDate, 
						relationship);
		Date familyAssociationNoteDate = this.parseDateText("01/01/2017");
		String value = "Testing note creation";
		RelationshipNoteCategory relationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("category",
			(short)34);
		
		// Action
		RelationshipNote familyAssociationNote =  this
				.familyAssociationService.addRelationshipNote(familyAssociation, 
					relationshipNoteCategory, familyAssociationNoteDate, value);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("category", relationshipNoteCategory)
			.addExpectedValue("date", familyAssociationNoteDate)
			.addExpectedValue("value", value)
			.addExpectedValue("relationship", relationship)
			.performAssertions(familyAssociationNote);
	}
	
	/**
	 * Tests {@code FamilyAssociationNoteExistsException} is thrown.
	 * 
	 * @throws ReflexiveRelationshipException if relationship is between same 
	 * person
	 * @throws FamilyAssociationConflictException if association overlaps
	 * @throws FamilyAssociationExistsException 
	 * @throws FamilyAssociationNoteExistsException 
	 * @throws FamilyAssociationCategoryExistsException 
	 * @throws RelationshipNoteCategoryExistsException
	 * @throws RelationshipNoteExistsException
	 */
	@Test(expectedExceptions = {RelationshipNoteExistsException.class})
	public void testFamilyAssociationNoteExistsException() 
		throws ReflexiveRelationshipException,
		FamilyAssociationConflictException, FamilyAssociationExistsException, 
		FamilyAssociationNoteExistsException, 
		FamilyAssociationCategoryExistsException,
		RelationshipNoteExistsException,
		RelationshipNoteCategoryExistsException {
		// Arrangement
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
				"Kevin", "Johns", "Mr.");
		Person familyMember = personDelegate.create("Li", "Yidong", "CIC311", 
			"Mr.");
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("12/01/2017"));
		FamilyAssociationCategory category = this
				.familyAssociationCategoryDelegate.create("testName", true, 
						(short) 1, 
						FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags = new FamilyAssociationFlags(true, true, 
				true);
		Date marriageDate = this.parseDateText("01/01/2017");
		Date divorceDate = this.parseDateText("12/01/2017");
		Relationship relationship = this.relationshipDelegate.findOrCreate(
				offender, familyMember);
		FamilyAssociation familyAssociation = this.familyAssociationDelegate
				.create(dateRange, category, flags, marriageDate, divorceDate, 
						relationship);
		Date familyAssociationNoteDate = this.parseDateText("01/01/2017");
		String value = "Testing note creation";
		RelationshipNoteCategory relationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("category",
			(short) 32);
		/*this.familyAssociationNoteDelegate.create(familyAssociation, 
				familyAssociationNoteDate, value);*/
		this.relationshipNoteDelegate.create(relationship,
			relationshipNoteCategory, value, familyAssociationNoteDate);
		
		// Action
		this.familyAssociationService.addRelationshipNote(familyAssociation,
			relationshipNoteCategory, familyAssociationNoteDate, value);

	}
	
	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}