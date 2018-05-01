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
package omis.family.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationNoteCategoryDesignator;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;

/**
 * Data access object for family association note category designator.
 * 
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (March 8, 2018)
 * @since OMIS 3.0
 */
public interface FamilyAssociationNoteCategoryDesignatorDao
	extends GenericDao<FamilyAssociationNoteCategoryDesignator> {

	/**
	 * Returns the all existing relationship note categories.
	 *  
	 * @return a list of relationship note categories
	 */
	List<RelationshipNoteCategory> findAllCategories();
	
	/**
	 * Find an existing family association note category designator by
	 * relationship note category.
	 * @param category family association note category 
	 * @return family association note category designator 
	 */
	FamilyAssociationNoteCategoryDesignator findByCatgory(
		RelationshipNoteCategory category);
	
	/**
	 * Find related relationship notes associated with a specified family
	 * association.
	 * @deprecated use findDesignatedNotesByRelationship() instead
	 * @param familyAssociation family association
	 * @return all related relationship note associated with a specified family
	 * association
	 */
	@Deprecated
	List<RelationshipNote> findRelationshipNotes(
		FamilyAssociation familyAssociation);

	/**
	 * Returns designated notes by relationship.
	 * 
	 * @param relationship relationship
	 * @return designated notes by relationship
	 */
	List<RelationshipNote> findDesignatedNotesByRelationship(
			Relationship relationship);
}