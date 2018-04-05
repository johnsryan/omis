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
import omis.family.service.delegate.FamilyAssociationNoteDelegate;
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
 * Tests family association note removal.
 * 
 *@author Yidong Li 
 *@author Sheronda Vaughn
 *@version 0.1.0 (June 29, 2017)
 *@since OMIS 3.0
 *
 */
@Test(groups = {"family"})
public class FamilyAssociationServiceFamilyAssociationNoteRemoveTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	/* Delegate */
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("familyAssociationCategoryDelegate")
	private FamilyAssociationCategoryDelegate familyAssociationCategoryDelegate;
	
	@Autowired
	@Qualifier("familyAssociationNoteDelegate")
	private FamilyAssociationNoteDelegate familyAssociationNoteDelegate;
	
	@Autowired
	@Qualifier("relationshipNoteCategoryDelegate")
	private RelationshipNoteCategoryDelegate relationshipNoteCategoryDelegate;
	
	/* Service */
	@Autowired
	@Qualifier("familyAssociationService")
	private FamilyAssociationService familyAssociationService;
	
	/**
	 * Tests the removal of family association note.
	 * 
	 * @throws FamilyAssociationNoteExistsException family association note 
	 * exists exception
	 * @throws ReflexiveRelationshipException reflexive relationship exception 
	 * @throws FamilyAssociationConflictException family association conflict
	 * exception 
	 * @throws FamilyAssociationCategoryExistsException family association
	 * category exists exception 
	 * @throws FamilyAssociationExistsException family association exists
	 * exception 
	 * @throws RelationshipNoteCategoryExistsException relationship note
	 * category exists exception 
	 * @throws RelationshipNoteExistsException relationship note exists
	 * exception
	 */
	@Test
	public void testFamilyAssociationNoteRemove() 
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
		Date familyAssociationStartDate = new Date(11111111);
		Date familyAssociationEndDate = new Date(22345678);
		familyAssociationDateRange.setEndDate(familyAssociationEndDate);
		familyAssociationDateRange.setStartDate(familyAssociationStartDate);
		FamilyAssociationCategory category 
			= this.familyAssociationCategoryDelegate.create("testName", 
			(Boolean) true, new Short((short) 23), 
			FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags = new FamilyAssociationFlags();
		flags.setCohabitant(true);
		flags.setDependent(true);
		flags.setEmergencyContact(true);
		Date marriageDate = new Date(1113333);
		Date divorceDate = new Date(2113333);
		
		FamilyAssociation familyAssociation = this.familyAssociationService
			.associate(offender, familyMember, familyAssociationDateRange, 
			category, flags, marriageDate, divorceDate);
		
		Date familyAssociationNoteDate = new Date(21111111);
		String value = "Testing note creation";
		
		RelationshipNoteCategory relationshipNoteCategory
			= this.relationshipNoteCategoryDelegate.create("category",
			(short) 34);
		
		// Action
		RelationshipNote familyAssociationNote = 
			this.familyAssociationService.addRelationshipNote(familyAssociation,
				relationshipNoteCategory, familyAssociationNoteDate, value);
		
		// Action
		this.familyAssociationService.removeRelationshipNote(
			familyAssociationNote);
		
		// Assertion
		assert !(this.familyAssociationNoteDelegate.findExists()
			.contains(familyAssociationNote)) : "Family association note was "
					+ "not removed!";
	}
}
