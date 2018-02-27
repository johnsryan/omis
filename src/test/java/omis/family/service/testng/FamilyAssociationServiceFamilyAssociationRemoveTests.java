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
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests family association removal.
 * 
 *@author Yidong Li 
 *@author Sheronda Vaughn
 *@version 0.1.0 (June 29, 2017)
 *@since OMIS 3.0
 *
 */
@Test(groups = {"family"})
public class FamilyAssociationServiceFamilyAssociationRemoveTests
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
	@Qualifier("familyAssociationDelegate")
	private FamilyAssociationDelegate familyAssociationDelegate;
	
	@Autowired
	@Qualifier("relationshipDelegate")
	private RelationshipDelegate relationshipDelegate;
	
	/* Service */
	@Autowired
	@Qualifier("familyAssociationService")
	private FamilyAssociationService familyAssociationService;
	
	/**
	 * Test family association remove.
	 *
	 *
	 * @throws FamilyAssociationExistsException family association exists
	 * @throws ReflexiveRelationshipException reflexive relationship
	 * @throws FamilyAssociationConflictException family association conflict
	 * @throws FamilyAssociationCategoryExistsException family association
	 * categoy exists
	 */
	@Test
	public void testFamilyAssociationRemove() 
		throws FamilyAssociationExistsException, ReflexiveRelationshipException, 
		FamilyAssociationConflictException, 
		FamilyAssociationCategoryExistsException {
		// Arrangement
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
			"Kevin", "Johns", "Mr.");
		Person familyMember = personDelegate.create("Li", "Yidong", "CIC311", 
			"Mr.");
		DateRange dateRange = new DateRange();
		Date startDate = this.parseDateText("01/01/2010");
		Date endDate = this.parseDateText("12/31/2020");
		dateRange.setEndDate(endDate);
		dateRange.setStartDate(startDate);
		FamilyAssociationCategory category 
			= this.familyAssociationCategoryDelegate.create("testName", 
			(Boolean) true, new Short((short) 1), 
			FamilyAssociationCategoryClassification.CHILD);
		FamilyAssociationFlags flags = new FamilyAssociationFlags();
		flags.setCohabitant(true);
		flags.setDependent(true);
		flags.setEmergencyContact(true);
		Date marriageDate = this.parseDateText("01/01/2010");
		Date divorceDate = this.parseDateText("12/31/2020");
		FamilyAssociation familyAssociation = this.familyAssociationService
			.associate(offender, familyMember, dateRange, category, flags, 
			marriageDate, divorceDate);
		
		// Action
		this.familyAssociationService.remove(familyAssociation);
		
		// Assertion
		assert !(this.familyAssociationDelegate.findByOffender(offender)
			.contains(familyAssociation))
				: "Family association was not removed!";
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