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
package omis.visitation.service.delegate.impl.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
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
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.service.delegate.VisitationAssociationCategoryDelegate;
import omis.visitation.service.delegate.VisitationAssociationDelegate;

/**
 * Tests operation to remove visitation associations by relationship.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 2, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"visitation", "serviceOperation"})
public class VisitationAssociationRemovalByRelationshipOperationImplTests
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
	@Qualifier("visitationAssociationDelegate")
	private VisitationAssociationDelegate visitationAssociationDelegate;
	
	@Autowired
	@Qualifier("visitationAssociationCategoryDelegate")
	private VisitationAssociationCategoryDelegate
	visitationAssociationCategoryDelegate;
	
	/* Operations registry. */
	
	@Autowired
	@Qualifier("relationshipAssociatedRemovalOperationRegistry")
	private AssociatedEntityOperationRegistry<Relationship>
	relationshipAssociatedRemovalOperationRegistry;
	
	/**
	 * Tests removal of visitation associations by relationship.
	 * 
	 * @throws ReflexiveRelationshipException if persons of relationship are
	 * the same 
	 * @throws RelationshipExistsException if relationship exists
	 * @throws DuplicateEntityFoundException if visitation category association
	 * exists
	 * @throws DateConflictException if dates conflict
	 */
	public void testRemoval()
			throws ReflexiveRelationshipException,
				DuplicateEntityFoundException,
				RelationshipExistsException,
				DateConflictException {
		
		// Arrangements - relates offender and visitor
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Person visitor = this.personDelegate
				.create("Largo", "Emilio", null, null);
		Relationship relationship = this.relationshipDelegate
				.create(offender, visitor);
		VisitationAssociationCategory businessPartner
			= this.visitationAssociationCategoryDelegate
				.create("Business Partner", (short) 1, true);
		VisitationAssociationFlags flags = new VisitationAssociationFlags(
				true, true, true, true);
		VisitationApproval approval = new VisitationApproval(false, null);
		this.visitationAssociationDelegate.create(
				relationship, businessPartner, approval, null, null, flags,
				null, null);
		
		// Action - removes associations
		this.relationshipAssociatedRemovalOperationRegistry
			.applyAll(relationship);
		
		// Asserts that associations were removed
		assert this.visitationAssociationDelegate
				.countByRelationship(relationship) == 0
			: "Vistiation associations exist";
	}
}