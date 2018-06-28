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
package omis.victim.service.delegate.impl.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

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
import omis.victim.exception.VictimExistsException;
import omis.victim.service.delegate.VictimAssociationDelegate;

/**
 * Tests for victim association removal by relationship operation
 * implementation.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jun 28, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"victim", "serviceOperation"})
public class VictimAssociationRemovalByRelationshipOperationImplTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

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
	@Qualifier("relationshipAssociatedRemovalOperationRegistry")
	private AssociatedEntityOperationRegistry<Relationship>
	relationshipAssociatedRemovalOperationRegistry;
	
	@Autowired
	@Qualifier("victimAssociationDelegate")
	private VictimAssociationDelegate victimAssociationDelegate;
	
	/**
	 * Tests removal of victim associations by relationship using operation.
	 * 
	 * @throws ReflexiveRelationshipException if two persons of relationship
	 * are the same
	 * @throws RelationshipExistsException if relationship exists 
	 * @throws VictimExistsException victim exists exception
	 */
	public void testRemoval()
			throws RelationshipExistsException, ReflexiveRelationshipException,
				VictimExistsException {
		
		// Arrangements - relates victim and offender
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		Person victim = this.personDelegate.create(
				"di Vincenzo", "Teresa", null, null);
		Relationship relationship = this.relationshipDelegate
				.create(offender, victim);
		this.victimAssociationDelegate
				.create(relationship, null, null, null, null);
		
		// Action - removes all associated entities to relationship
		this.relationshipAssociatedRemovalOperationRegistry.applyAll(
				relationship);
		
		// Asserts that victim association is removed
		assert this.victimAssociationDelegate
					.countByRelationship(relationship) == 0
				: "Victim associations still exist";
	}
}