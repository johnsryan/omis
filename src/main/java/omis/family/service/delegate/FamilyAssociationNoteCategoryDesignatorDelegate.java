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
package omis.family.service.delegate;

import java.util.List;

import omis.family.dao.FamilyAssociationNoteCategoryDesignatorDao;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationNoteCategoryDesignator;
import omis.family.exception.FamilyAssociationNoteCategoryDesignatorExistsException;
import omis.instance.factory.InstanceFactory;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;

/**
 * Family association note category designator delegate implementation.
 * 
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.1 (March 8, 2018)
 * @since OMIS 3.0
 */
public class FamilyAssociationNoteCategoryDesignatorDelegate {

	/* Data access objects. */
	
	private FamilyAssociationNoteCategoryDesignatorDao
		familyAssociationNoteCategoryDesignatorDao;
	
	/* Instance factories. */
	
	private InstanceFactory<FamilyAssociationNoteCategoryDesignator>
		familyAssociationNoteCategoryDesignatorInstanceFactory;
	
	/* Constructor. */
	
	/**
	 * Instantiates a family association note category designator service
	 * implementation delegate with the specified data access object and
	 * instance factory.
	 * 
	 * @param familyAssociationNoteCategoryDesignatorDao
	 * family association note category designator dao
	 * @param familyAssociationNoteCategoryDesignatorInstanceFactory family
	 * association note category designator instance factory
	 *
	 */
	public FamilyAssociationNoteCategoryDesignatorDelegate(
			final FamilyAssociationNoteCategoryDesignatorDao
			familyAssociationNoteCategoryDesignatorDao,
			final InstanceFactory<FamilyAssociationNoteCategoryDesignator> 
			familyAssociationNoteCategoryDesignatorInstanceFactory) {
		this.familyAssociationNoteCategoryDesignatorDao
			= familyAssociationNoteCategoryDesignatorDao;
		this.familyAssociationNoteCategoryDesignatorInstanceFactory
			= familyAssociationNoteCategoryDesignatorInstanceFactory;
	}
	
	/* Management methods. */
	
	/**
	 * Creates a family association note category designator.
	 * 
	 * @param category family association note category
	 * @return created family association note category designator
	 * @throws FamilyAssociationNoteCategoryDesignatorExistsException family
	 * association note category designator exists exception
	 */
	public FamilyAssociationNoteCategoryDesignator create(
		final RelationshipNoteCategory category)
		throws FamilyAssociationNoteCategoryDesignatorExistsException {
		if (this.familyAssociationNoteCategoryDesignatorDao.findByCatgory(
			category) != null) {
			throw new FamilyAssociationNoteCategoryDesignatorExistsException(
				"Family association note category designator already exists");
		}
		
		FamilyAssociationNoteCategoryDesignator designator
			= this.familyAssociationNoteCategoryDesignatorInstanceFactory
			.createInstance();
		designator.setRelationshipNoteCategory(category);
		return this.familyAssociationNoteCategoryDesignatorDao.makePersistent(
			designator);
	}
	
	/**
	 * Removes a family association note category designator.
	 * 
	 * @param designator family association note category designator
	 */
	public void remove(final FamilyAssociationNoteCategoryDesignator
		designator) {
		this.familyAssociationNoteCategoryDesignatorDao.makeTransient(
			designator);
	}
	
	/**
	 * Find related relationship note categories.
	 * 
	 * @return created family association note category designator
	 */
	public List<RelationshipNoteCategory>
		findDesignatedRelationshipNoteCategories() {
		return this.familyAssociationNoteCategoryDesignatorDao
		.findAllCategories();
	}
	
	/**
	 * Find related relationship notes associated with a specified family
	 * association.
	 * @deprecated use findDesignatedNotesByRelationship()
	 * @param familyAssociation family association
	 * @return all related relationship notes associated with a specified family
	 * association
	 */
	@Deprecated
	public List<RelationshipNote> findRelationshipNotes(
		final FamilyAssociation familyAssociation) {
		return this.familyAssociationNoteCategoryDesignatorDao
			.findRelationshipNotes(familyAssociation);
	}

	/**
	 * Returns designated family notes by relationship.
	 * 
	 * @param relationship relationship
	 * @return designated family notes
	 */
	public List<RelationshipNote> findDesignatedNotesByRelationship(
			final Relationship relationship) {
		return this.familyAssociationNoteCategoryDesignatorDao
				.findDesignatedNotesByRelationship(relationship);
	}
}