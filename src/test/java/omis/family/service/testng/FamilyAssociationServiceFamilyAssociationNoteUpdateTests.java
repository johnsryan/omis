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

import omis.beans.factory.spring.CustomDateEditorFactory;
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
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipNoteCategoryExistsException;
import omis.relationship.exception.RelationshipNoteExistsException;
import omis.relationship.service.delegate.RelationshipNoteCategoryDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests "update" of family association note.
 *
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"family"})
public class FamilyAssociationServiceFamilyAssociationNoteUpdateTests
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
	@Qualifier("relationshipNoteCategoryDelegate")
	private RelationshipNoteCategoryDelegate relationshipNoteCategoryDelegate;
	
	/* Service to test. */
	@Autowired
	@Qualifier("familyAssociationService")
	private FamilyAssociationService familyAssociationService;
	
	/* Property editors. */
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	/**
	 * Family association note update.
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws FamilyAssociationNoteExistsException family association note 
	 * exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws RelationshipNoteCategoryExistsException relationship note
	 * category exists exception
	 * @throws RelationshipNoteExistsException relationship note exists
	 * exception
	 */
	@Test
	public void testFamilyAssociationNoteUpdate() 
			throws ReflexiveRelationshipException, 
			FamilyAssociationConflictException, 
			FamilyAssociationCategoryExistsException, 
			FamilyAssociationExistsException, 
			FamilyAssociationNoteExistsException,
			RelationshipNoteCategoryExistsException,
			RelationshipNoteExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
			"Kevin", "Johns", "Mr.");
		Person familyMember = personDelegate.create("Li", "Yidong", "CIC311", 
			"Mr.");
		DateRange familyAssociationDateRange = new DateRange();
		Date familyAssociationStartDate = this.parseDateText("01/18/2015");
		Date familyAssociationEndDate = this.parseDateText("01/19/2016");
		familyAssociationDateRange.setEndDate(familyAssociationEndDate);
		familyAssociationDateRange.setStartDate(familyAssociationStartDate);
		FamilyAssociationCategory category 
			= this.familyAssociationCategoryDelegate.create("testName", 
			(Boolean) true, new Short((short) 1),
			FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags = new FamilyAssociationFlags();
		flags.setCohabitant(true);
		flags.setDependent(true);
		flags.setEmergencyContact(true);
		Date marriageDate = this.parseDateText("01/15/2017");
		Date divorceDate = this.parseDateText("01/31/2017");
		
		FamilyAssociation familyAssociation = this.familyAssociationService
			.associate(offender, familyMember, familyAssociationDateRange, 
			category, flags, marriageDate, divorceDate);
		
		Date familyAssociationNoteDate = this.parseDateText("01/01/2015");
		String value = "Testing note creation";
		
		RelationshipNoteCategory relationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("category",
			(short) 34);
		
		RelationshipNote familyAssociationNote = 
			this.familyAssociationService.addRelationshipNote(familyAssociation,
				relationshipNoteCategory, familyAssociationNoteDate, value);
		
		DateRange newFamilyAssociationDateRange = new DateRange();
		Date newFamilyAssociationStartDate = this.parseDateText("02/11/2014");
		Date newFamilyAssociationEndDate = this.parseDateText("03/01/2014");
		newFamilyAssociationDateRange.setEndDate(newFamilyAssociationEndDate);
		newFamilyAssociationDateRange.setStartDate(
				newFamilyAssociationStartDate);
		FamilyAssociationFlags newFlags = new FamilyAssociationFlags();
		newFlags.setCohabitant(false);
		newFlags.setDependent(false);
		newFlags.setEmergencyContact(false);
		Date newFamilyAssociationNoteDate = this.parseDateText("01/01/2017");
		String newValue = "New testing note creation";
		RelationshipNoteCategory updatedRelationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("updatedCategory",
			(short) 34);
		
		this.familyAssociationService.updateRelationshipNote(
			familyAssociationNote, updatedRelationshipNoteCategory,
			newFamilyAssociationNoteDate, newValue);
		
		// Assertions
		assert newFamilyAssociationNoteDate.equals(
				familyAssociationNote.getDate())
		: String.format("Wrong note date: #%s expected; #%s found",
			newFamilyAssociationNoteDate, familyAssociationNote.getDate());
		assert newValue.equals(familyAssociationNote.getValue())
		: String.format("Wrong value: #%s expected; #%s found",
			newValue, familyAssociationNote.getValue());
		assert updatedRelationshipNoteCategory.equals(
			familyAssociationNote.getCategory())
		: String.format("Wrong value: #%s expected; #%s found",
			updatedRelationshipNoteCategory,
			familyAssociationNote.getCategory());
	}
	
	/**
	 * Tests duplicate family association note on update.
	 * 
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws FamilyAssociationNoteExistsException family association note 
	 * exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws RelationshipNoteCategoryExistsException relationship note
	 * category exists exception
	 * @throws RelationshipNoteExistsException relationship note exists
	 * exception
	 */
	@Test(expectedExceptions = {RelationshipNoteExistsException.class, 
			ReflexiveRelationshipException.class})
	public void testDuplicateFamilyAssociationNoteUpdate() 
		throws FamilyAssociationNoteExistsException, 
			ReflexiveRelationshipException, FamilyAssociationConflictException, 
			FamilyAssociationCategoryExistsException, 
			FamilyAssociationExistsException,
			RelationshipNoteCategoryExistsException,
			RelationshipNoteExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
			"Kevin", "Johns", "Mr.");
		Person familyMember = personDelegate.create("Li", "Yidong", "CIC311", 
			"Mr.");
		DateRange familyAssociationDateRange = new DateRange();
		Date familyAssociationStartDate = this.parseDateText("01/18/2015");
		Date familyAssociationEndDate = this.parseDateText("01/19/2016");
		familyAssociationDateRange.setEndDate(familyAssociationEndDate);
		familyAssociationDateRange.setStartDate(familyAssociationStartDate);
		FamilyAssociationCategory category 
			= this.familyAssociationCategoryDelegate.create("testName", 
			(Boolean) true, new Short((short) 1),
			FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags = new FamilyAssociationFlags();
		flags.setCohabitant(true);
		flags.setDependent(true);
		flags.setEmergencyContact(true);
		Date marriageDate = this.parseDateText("01/15/2017");
		Date divorceDate = this.parseDateText("01/31/2017");
		
		FamilyAssociation familyAssociation = this.familyAssociationService
			.associate(offender, familyMember, familyAssociationDateRange, 
			category, flags, marriageDate, divorceDate);
		
		Date familyAssociationNoteDate = this.parseDateText("01/01/2015");
		String value = "Testing note creation";
		RelationshipNoteCategory relationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("updatedCategory",
			(short) 34);
		
		RelationshipNote familyAssociationNote 
			= this.familyAssociationService.addRelationshipNote(
				familyAssociation, relationshipNoteCategory,
				familyAssociationNoteDate, value);
		
		DateRange newFamilyAssociationDateRange = new DateRange();
		Date newFamilyAssociationStartDate = this.parseDateText("02/11/2014");
		Date newFamilyAssociationEndDate = this.parseDateText("03/01/2014");
		newFamilyAssociationDateRange.setEndDate(newFamilyAssociationEndDate);
		newFamilyAssociationDateRange.setStartDate(
				newFamilyAssociationStartDate);
		
		FamilyAssociationFlags newFlags = new FamilyAssociationFlags();
		newFlags.setCohabitant(false);
		newFlags.setDependent(false);
		newFlags.setEmergencyContact(false);
				
		Date newFamilyAssociationNoteDate = this.parseDateText("01/01/2017");
		String newValue = "New testing note creation";
	
		RelationshipNoteCategory newRelationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("existingCategory",
			(short) 34);
		
		this.familyAssociationService.addRelationshipNote(familyAssociation,
			newRelationshipNoteCategory, newFamilyAssociationNoteDate,
			newValue);
		
		// Action
		this.familyAssociationService.updateRelationshipNote(
			familyAssociationNote, newRelationshipNoteCategory,
			newFamilyAssociationNoteDate, newValue);
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