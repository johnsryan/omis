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
import omis.family.service.FamilyAssociationService;
import omis.family.service.delegate.FamilyAssociationCategoryDelegate;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests "update" of family association
 *
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"family"})
public class FamilyAssociationServiceFamilyAssociationUpdateTests
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
	
	/* Service to test. */
	@Autowired
	@Qualifier("familyAssociationService")
	private FamilyAssociationService familyAssociationService;
	
	/**
	 * Family association update.
	 *
	 *
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 */
	@Test
	public void testFamilyAssociationUpdate() 
			throws FamilyAssociationExistsException, 
		ReflexiveRelationshipException, FamilyAssociationConflictException, 
		FamilyAssociationCategoryExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
			"Obama", "Kevin", "Johns", "Mr.");
		DateRange originalDateRange = new DateRange();
		Date orignalStartDate = new Date(105120000);
		Date originalEndDate = new Date(205120000);
		originalDateRange.setStartDate(orignalStartDate);
		originalDateRange.setEndDate(originalEndDate);
		Date originalMarriageDate = new Date(115120000);
		Date originalDivorceDate = new Date(215120000);
		Person originalFamilyMember = personDelegate.create("Li", "Yidong", 
			"CIC311", "Mr.");
		FamilyAssociationCategory originalCategory 
			= this.familyAssociationCategoryDelegate.create("originalCategory", 
				(Boolean) true, new Short((short) 23), 
				FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags originalFlags = new FamilyAssociationFlags();
		originalFlags.setCohabitant(true);
		originalFlags.setDependent(true);
		originalFlags.setEmergencyContact(true);
		
		DateRange newDateRange = new DateRange();
		Date newStartDate = new Date(5120000);
		Date newEndDate = new Date(105120000);
		newDateRange.setStartDate(newStartDate);
		newDateRange.setEndDate(newEndDate);
		Date newMarriageDate = new Date(105120000);
		Date newDivorceDate = new Date(205120000);
		Relationship relationship = this.relationshipDelegate.findOrCreate(
				offender, originalFamilyMember);
		FamilyAssociationCategory newCategory 
			= this.familyAssociationCategoryDelegate.create("newCategory", 
				(Boolean) true, new Short((short) 57), 
				FamilyAssociationCategoryClassification.SIBLING);
		FamilyAssociationFlags newFlags = new FamilyAssociationFlags();
		newFlags.setCohabitant(false);
		newFlags.setDependent(false);
		newFlags.setEmergencyContact(false);
		FamilyAssociation familyAssociation = this.familyAssociationDelegate
				.create(originalDateRange, originalCategory, originalFlags, 
						originalMarriageDate, originalDivorceDate, 
						relationship);
		
		// Action
		
		familyAssociation = this.familyAssociationService.update(
				familyAssociation, newDateRange, newCategory, newFlags, 
				newMarriageDate, newDivorceDate);
		
		// Assertions
		PropertyValueAsserter.create()
		.addExpectedValue("dateRange", newDateRange)
		.addExpectedValue("category", newCategory)
		.addExpectedValue("flags", newFlags)
		.addExpectedValue("marriageDate", newMarriageDate)
		.addExpectedValue("divorceDate", newDivorceDate)
			.performAssertions(familyAssociation);
	}
	
	/**
	 * Tests duplicate family association on update.
	 * 
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists 
	 */
	@Test(expectedExceptions = {FamilyAssociationExistsException.class, 
		ReflexiveRelationshipException.class})
	public void testFamilyAssociationExistsException() 
		throws FamilyAssociationExistsException, ReflexiveRelationshipException,
			FamilyAssociationConflictException, 
			FamilyAssociationCategoryExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
			"Obama", "Kevin", "Johns", "Mr.");
		DateRange dateRange1 = new DateRange();
		Date startDate1 = new Date(105120000);
		Date endDate1 = new Date(205120000);
		dateRange1.setStartDate(startDate1);
		dateRange1.setEndDate(endDate1);
		Date marriageDate1 = new Date(115120000); 
		Date divorceDate1 = new Date(215120000);
		Person familyMember1 = personDelegate.create("Li", "Yidong", 
			"CIC311", "Mr.");
		FamilyAssociationCategory category1 
			= this.familyAssociationCategoryDelegate.create("category", 
				(Boolean) true, new Short((short) 23), 
				FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags1 = new FamilyAssociationFlags();
		flags1.setCohabitant(true);
		flags1.setDependent(true);
		flags1.setEmergencyContact(true);
		Relationship relationship = this.relationshipDelegate.findOrCreate(
				offender, familyMember1);
		this.familyAssociationDelegate.create(dateRange1, category1, flags1, 
				marriageDate1, divorceDate1, relationship);
		this.familyAssociationService
			.associate(offender, familyMember1, dateRange1, category1, flags1, 
					marriageDate1, divorceDate1);
		
		DateRange dateRange2 = new DateRange();
		Date startDate2 = new Date(155120000);
		Date endDate2 = new Date(255120000);
		dateRange2.setStartDate(startDate2);
		dateRange2.setEndDate(endDate2);
		Date marriageDate2 = new Date(175120000); 
		Date divorceDate2 = new Date(275120000);
		FamilyAssociationCategory category2 
			= this.familyAssociationCategoryDelegate.create("newCategory", 
					(Boolean) true, new Short((short) 23), 
					FamilyAssociationCategoryClassification.PARENT);
		FamilyAssociationFlags flags2 = new FamilyAssociationFlags();
		flags2.setCohabitant(false);
		flags2.setDependent(false);
		flags2.setEmergencyContact(false);
		FamilyAssociation familyAssociation1 = this.familyAssociationService
				.associate(offender, familyMember1, dateRange2, category2, 
						flags2, marriageDate2, divorceDate2);
		
		// Action
		this.familyAssociationService.update(familyAssociation1, dateRange2, 
				category1, flags2, marriageDate2, divorceDate2);
	}	
	
	/**
	 * Tests overlapped family association on update.
	 * 
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists 
	 */
	@Test(expectedExceptions = {FamilyAssociationConflictException.class})
	public void testOverlappedFamilyAssociationUpdate() 
		throws FamilyAssociationExistsException, ReflexiveRelationshipException,
			FamilyAssociationConflictException, 
			FamilyAssociationCategoryExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
			"Obama", "Kevin", "Johns", "Mr.");
		DateRange dateRange1 = new DateRange();
		Date startDate1 = new Date(105120000);
		Date endDate1 = new Date(205120000);
		dateRange1.setStartDate(startDate1);
		dateRange1.setEndDate(endDate1);
		Date marriageDate1 = new Date(115120000); 
		Date divorceDate1 = new Date(215120000);
		Person familyMember1 = personDelegate.create("Li", "Yidong", 
			"CIC311", "Mr.");
		FamilyAssociationCategory category1 
			= this.familyAssociationCategoryDelegate.create("category1", 
				(Boolean) true, new Short((short) 23), 
				FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags1 = new FamilyAssociationFlags();
		flags1.setCohabitant(true);
		flags1.setDependent(true);
		flags1.setEmergencyContact(true);
		FamilyAssociation familyAssociation1 = this.familyAssociationService
			.associate(offender, familyMember1, dateRange1, category1, flags1, 
					marriageDate1, divorceDate1);
		
		DateRange dateRange2 = new DateRange();
		Date startDate2 = new Date(255120000);
		Date endDate2 = new Date(305120000);
		dateRange2.setStartDate(startDate2);
		dateRange2.setEndDate(endDate2);
		Date marriageDate2 = new Date(255120000); 
		Date divorceDate2 = new Date(305120000);
		FamilyAssociationCategory category2 
			= this.familyAssociationCategoryDelegate.create("category2", 
				(Boolean) true, new Short((short) 23), 
				FamilyAssociationCategoryClassification.PARENT);
		FamilyAssociationFlags flags2 = new FamilyAssociationFlags();
		flags2.setCohabitant(false);
		flags2.setDependent(false);
		flags2.setEmergencyContact(false);
		this.familyAssociationService.associate(offender, familyMember1, 
			dateRange2, category2, flags2, marriageDate2, divorceDate2);
		
		DateRange dateRange3 = new DateRange();
		Date startDate3 = new Date(275120000);
		Date endDate3 = new Date(295120000);
		dateRange3.setStartDate(startDate3);
		dateRange3.setEndDate(endDate3);
		// Action
		this.familyAssociationService.update(familyAssociation1, dateRange3, 
			category2, flags2, marriageDate2, divorceDate2);
	}	
}