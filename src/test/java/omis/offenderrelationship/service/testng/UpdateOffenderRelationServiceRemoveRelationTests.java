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
package omis.offenderrelationship.service.testng;

import java.beans.PropertyEditor;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.exception.DateConflictException;
import omis.exception.DuplicateEntityFoundException;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.domain.FamilyAssociationCategoryClassification;
import omis.family.exception.FamilyAssociationCategoryExistsException;
import omis.family.exception.FamilyAssociationConflictException;
import omis.family.exception.FamilyAssociationExistsException;
import omis.family.service.delegate.FamilyAssociationCategoryDelegate;
import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.offenderrelationship.service.UpdateOffenderRelationService;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipExistsException;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;
import omis.victim.exception.VictimExistsException;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.victim.service.delegate.VictimNoteCategoryDelegate;
import omis.victim.service.delegate.VictimNoteDelegate;
import omis.visitation.domain.VisitationApproval;
import omis.visitation.domain.VisitationAssociationCategory;
import omis.visitation.domain.VisitationAssociationFlags;
import omis.visitation.service.delegate.VisitationAssociationCategoryDelegate;
import omis.visitation.service.delegate.VisitationAssociationDelegate;

/**
 * Tests removal of relation method of service to update offender relations.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 3, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"offenderRelationship", "service"})
public class UpdateOffenderRelationServiceRemoveRelationTests
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
	
	@Autowired
	@Qualifier("victimAssociationDelegate")
	private VictimAssociationDelegate victimAssociationDelegate;
	
	@Autowired
	@Qualifier("victimNoteCategoryDelegate")
	private VictimNoteCategoryDelegate victimNoteCategoryDelegate;
	
	@Autowired
	@Qualifier("victimNoteDelegate")
	private VictimNoteDelegate victimNoteDelegate;
	
	@Autowired
	@Qualifier("visitationAssociationCategoryDelegate")
	private VisitationAssociationCategoryDelegate
	visitationAssociationCategoryDelegate;
	
	@Autowired
	@Qualifier("visitationAssociationDelegate")
	private VisitationAssociationDelegate visitationAssociationDelegate;
	
	/* Service. */
	
	@Autowired
	@Qualifier("updateOffenderRelationService")
	private UpdateOffenderRelationService updateOffenderRelationService;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory datePropertyEditorFactory;
	
	/* Test methods. */
	
	/**
	 * Tests removal of relationship and all associations.
	 * 
	 * @throws ReflexiveRelationshipException if both persons of relationship
	 * are the same person 
	 * @throws RelationshipExistsException if relationship exists
	 * @throws FamilyAssociationCategoryExistsException if family association
	 * category exists
	 * @throws FamilyAssociationConflictException if conflicting family
	 * associations exist
	 * @throws FamilyAssociationExistsException if family association exists 
	 * @throws VictimExistsException if victim exists
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws DateConflictException if conflicting visitation associations
	 * exist
	 */
	public void testRemoval()
			throws RelationshipExistsException,
				ReflexiveRelationshipException,
				FamilyAssociationCategoryExistsException,
				FamilyAssociationExistsException,
				FamilyAssociationConflictException,
				VictimExistsException,
				DuplicateEntityFoundException,
				DateConflictException {
		
		// Arrangements - relates offender as family member, victim and visitor
		// Adds relationship notes
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		Person relation = this.personDelegate.create(
				"Oberhauser", "Hanes", null, null);
		Relationship relationship = this.relationshipDelegate
				.create(offender, relation);
		FamilyAssociationCategory father
			= this.familyAssociationCategoryDelegate.create(
					"Father", true, (short) 1,
					FamilyAssociationCategoryClassification.PARENT);
		this.familyAssociationDelegate.create(
				new DateRange(this.parseDateText("12/12/2011"), null), 
						father, null, null, null, relationship);
		VictimAssociation victimAssociation
			= this.victimAssociationDelegate.create(
				relationship, null, null, null, null);
		VictimNoteCategory general = this.victimNoteCategoryDelegate
				.create("General", (short) 1, true, true);
		VictimNote unassociatedNote
			= this.victimNoteDelegate.create(relation, general, null,
				this.parseDateText("12/12/2012"),
				"A general note");
		this.victimNoteDelegate.create(relation, general, victimAssociation,
				this.parseDateText("12/12/2012"),
				"Another general note");
		VisitationAssociationCategory fatherVisitor
			= this.visitationAssociationCategoryDelegate.create(
					"Father/Visitor", (short) 1, true);
		VisitationApproval approval = new VisitationApproval(
				true, this.parseDateText("12/12/2012"));
		VisitationAssociationFlags flags = new VisitationAssociationFlags(
				true, true, true, true);
		this.visitationAssociationDelegate.create(relationship, fatherVisitor,
				approval, null, null, flags, null, null);
		
		// Action - removes relationship and associations
		this.updateOffenderRelationService
			.removeRelationship(offender, relation);
		
		// Asserts that relationship and associations are gone
		assert this.relationshipDelegate.find(offender, relation) == null
			: "Relationship exists";
		assert this.familyAssociationDelegate
				.countByRelationship(relationship) == 0
			: "Family associations exist";
		assert this.victimAssociationDelegate
				.countByRelationship(relationship) == 0
			: "Victim associations exist";
		assert this.visitationAssociationDelegate
				.countByRelationship(relationship) == 0
			: "Visitation associations exist";
		assert this.victimNoteDelegate
					.countByAssociation(victimAssociation) == 0
			: "Associated notes exist";
		long notesCount = this.victimNoteDelegate.countByVictim(relation);
		assert notesCount == 1
			: String.format("Wrong number of notes - 1 expected; %d found",
					notesCount);
		assert this.victimNoteDelegate.findByVictim(relation).get(0)
					.equals(unassociatedNote)
			: "Unassociated note does not exist";
	}
	
	/* Helper methods. */
	
	// Converts date text into date
	private Date parseDateText(final String dateText) {
		PropertyEditor propertyEditor = this.datePropertyEditorFactory
				.createCustomDateOnlyEditor(true);
		propertyEditor.setAsText(dateText);
		return (Date) propertyEditor.getValue();
	}
}