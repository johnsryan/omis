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
import omis.family.service.FamilyAssociationService;
import omis.family.service.delegate.FamilyAssociationCategoryDelegate;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Test family association service allowed.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Nov 27, 2017)
 * @since OMIS 3.0
 */
@Test(groups = {"family", "service"})
public class FamilyAssociationServiceAssociateTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Delegates. */
	
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
	 * Tests allowed dates when existing end date and new start date supplied
	 * are the same.
	 *
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship exception 
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllowedExistingEndDateNewStartDateSame() 
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingEndDate = this.parseDateText("11/01/2017");
		Date newStartDate = this.parseDateText("11/01/2017");
		//Actions
		this.testAllowedDatesOnAssociateImpl(
				null, existingEndDate, newStartDate, null);		
	}
	
	/**
	 * Tests allowed dates when existing end date and new start date supplied
	 * are the different.
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship exception 
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllowedExistingEndDateNewStartDateDifferent() 
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingEndDate = this.parseDateText("11/01/2017");
		Date newStartDate = this.parseDateText("11/15/2017");
		//Actions
		this.testAllowedDatesOnAssociateImpl(
				null, existingEndDate, newStartDate, null);		
	}
	
	/**
	 * Tests allowed dates when existing start date and new end date supplied
	 * are the same.
	 *
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship exception 
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllowedExistingStartDateNewEndDateSame()
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingStartDate = this.parseDateText("11/01/2017");
		Date newEndDate = this.parseDateText("11/01/2017");
		//Actions
		this.testAllowedDatesOnAssociateImpl(
				existingStartDate, null, null, newEndDate);		
	}

	/**
	 * Tests allowed dates when new end date and existing start date supplied
	 * are the different.
	 *
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship exception 
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllowedExistingStartDateAfterNewEndDateDifferent()
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date newEndDate = this.parseDateText("11/01/2017");
		Date existingStartDate = this.parseDateText("11/15/2017");
		//Actions
		this.testAllowedDatesOnAssociateImpl(
				existingStartDate, null, null, newEndDate);		
	}
	
	/**
	 * Test allowed date ranges when all dates supplied and existing ends 
	 * on new start date.
	 *
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllDateRangesAllowedExistingEndOnNewStart()
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingStartDate = this.parseDateText("11/01/2017");
		Date existingEndDate = this.parseDateText("11/15/2017");
		Date newStartDate = this.parseDateText("11/15/2017");
		Date newEndDate = this.parseDateText("11/30/2017");		
		//Actions		
		this.testAllowedDatesOnAssociateImpl(
				existingStartDate, existingEndDate, newStartDate, newEndDate);	
	}
	
	/**
	 * Test allowed date ranges when all dates supplied and existing ends 
	 *  before new start date.
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllDateRangesAllowedExistingEndBeforeNewStart()
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingStartDate = this.parseDateText("11/01/2017");
		Date existingEndDate = this.parseDateText("11/15/2017");
		Date newStartDate = this.parseDateText("11/20/2017");
		Date newEndDate = this.parseDateText("11/30/2017");		
		//Actions		
		this.testAllowedDatesOnAssociateImpl(
				existingStartDate, existingEndDate, newStartDate, newEndDate);	
	}
	
	/**
	 * Test allowed date ranges when all dates supplied and existing date range
	 * after new date range and existing start begins on new end date.
	 *
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllDateRangesAllowedExistingAfterNewDateRanges()
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingStartDate = this.parseDateText("11/15/2017");
		Date existingEndDate = this.parseDateText("11/30/2017");
		Date newStartDate = this.parseDateText("11/01/2017");
		Date newEndDate = this.parseDateText("11/15/2017");		
		//Actions		
		this.testAllowedDatesOnAssociateImpl(
				existingStartDate, existingEndDate, newStartDate, newEndDate);	
	}
	
	/**
	 * Test allowed date ranges when all dates supplied and existing date ranges
	 *  after new date ranges and existing start is after new end date.
	 *
	 *
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 */
	@Test
	public void 
	familyAssociationServiceAssociateTestsAllDateRangesAllowedExistingAfterNewDifferentDates()
			throws FamilyAssociationConflictException, 
			ReflexiveRelationshipException, FamilyAssociationExistsException, 
			FamilyAssociationCategoryExistsException, 
			RelationshipExistsException {
		//Arrangements
		Date existingStartDate = this.parseDateText("11/20/2017");
		Date existingEndDate = this.parseDateText("11/30/2017");
		Date newStartDate = this.parseDateText("11/01/2017");
		Date newEndDate = this.parseDateText("11/15/2017");		
		//Actions		
		this.testAllowedDatesOnAssociateImpl(
				existingStartDate, existingEndDate, newStartDate, newEndDate);	
	}
	
	/**
	 * Test allowed dates when associating a family association.
	 *
	 *
	 * @param existingStartDate existing start date
	 * @param existingEndDate existing end date
	 * @param newStartDate new start date
	 * @param newEndDate new end date
	 * @return family association
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws RelationshipExistsException relationship exists
	 * @throws FamilyAssociationCategoryExistsException family association 
	 * category exists
	 */
	private FamilyAssociation testAllowedDatesOnAssociateImpl(
			final Date existingStartDate, final Date existingEndDate, 
			final Date newStartDate, final Date newEndDate) 
					throws FamilyAssociationConflictException, 
					ReflexiveRelationshipException, 
					FamilyAssociationExistsException, 
					FamilyAssociationCategoryExistsException, 
					RelationshipExistsException {
		//Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"OLastName", "OFirstName", "OMiddleName", null);
		Person familyMember = this.personDelegate.create(
				"PLastName", "PFirstName", "PMiddleName", null);
		DateRange dateRange = new DateRange(newStartDate, newEndDate);
		DateRange exsitingDateRange = new DateRange(
				existingStartDate, existingEndDate);
		FamilyAssociationCategory category 
			= this.familyAssociationCategoryDelegate
			.create("testCategoryName", true, (short) 23, 
					FamilyAssociationCategoryClassification.EXTENDED_FAMILY);
		FamilyAssociationFlags flags = new FamilyAssociationFlags();
		flags.setCohabitant(false);
		flags.setDependent(true);
		flags.setEmergencyContact(false);
		Date marriageDate = new Date(16072008);
		Date divorceDate = new Date(15052017);	
		Relationship relationship = this.relationshipDelegate
				.create(offender, familyMember);		
		this.familyAssociationDelegate
				.create(exsitingDateRange, category, flags, marriageDate, 
						divorceDate, relationship);
		FamilyAssociation familyAssocication
			= this.familyAssociationService
					.associate(offender, familyMember, dateRange, category, 
							flags, marriageDate, divorceDate);
		
		return familyAssocication;
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