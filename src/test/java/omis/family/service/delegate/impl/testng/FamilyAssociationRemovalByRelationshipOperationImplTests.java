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
package omis.family.service.delegate.impl.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.exception.FamilyAssociationCategoryExistsException;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
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
import omis.service.delegate.AssociatedEntityOperationRegistry;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests for family association removal by relationship operation
 * implementation.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 29, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"family", "serviceOperation"})
public class FamilyAssociationRemovalByRelationshipOperationImplTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

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
	@Qualifier("familyAssociationCategoryDelegate")
	private FamilyAssociationCategoryDelegate familyAssociationCategoryDelegate;
	
	@Autowired
	@Qualifier("familyAssociationDelegate")
	private FamilyAssociationDelegate familyAssociationDelegate;
	
	/* Registry. */
	
	@Autowired
	@Qualifier("relationshipAssociatedRemovalOperationRegistry")
	private AssociatedEntityOperationRegistry<Relationship>
	relationshipAssociatedRemovalOperationRegistry;
	
	/**
	 * Tests removal of family associations by relationship using operation.
	 * 
	 * @throws ReflexiveRelationshipException if offender and family member
	 * are same person 
	 * @throws RelationshipExistsException if offender and family member
	 * are already related 
	 * @throws FamilyAssociationCategoryExistsException if family association
	 * category exists
	 * @throws FamilyAssociationConflictException if conflicting family
	 * associations exist
	 * @throws FamilyAssociationExistsException if family association exists 
	 */
	public void testRemoval()
				throws RelationshipExistsException,
					ReflexiveRelationshipException,
					FamilyAssociationCategoryExistsException, FamilyAssociationExistsException, FamilyAssociationConflictException {
		
		// Arrangements - relates family member and offender as father and son
		Offender offender = this.offenderDelegate
					.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Person familyMember = this.personDelegate.create(
				"Oberhauser", "Hannes", null, null);
		Relationship relationship = this.relationshipDelegate
				.create(offender, familyMember);
		FamilyAssociationCategory father
			= this.familyAssociationCategoryDelegate
				.create("Father", true, (short) 1,
					FamilyAssociationCategoryClassification.PARENT);
		this.familyAssociationDelegate
			.create(null, father, null, null, null, relationship);
		
		// Action - removes entities associated with relationship
		this.relationshipAssociatedRemovalOperationRegistry
			.applyAll(relationship);
		
		// Asserts that family association was removed
		assert this.familyAssociationDelegate
				.countByRelationship(relationship) == 0
			: "Family association exists";
	}
}