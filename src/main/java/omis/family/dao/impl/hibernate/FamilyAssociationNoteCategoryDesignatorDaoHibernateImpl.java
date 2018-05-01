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
package omis.family.dao.impl.hibernate;

import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.family.dao.FamilyAssociationNoteCategoryDesignatorDao;
import omis.family.domain.FamilyAssociation;
import omis.family.domain.FamilyAssociationNoteCategoryDesignator;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;

import org.hibernate.SessionFactory;

/**
 * Implementation of data access object for family association note category
 * designator.
 * 
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (March 8, 2018)
 * @since OMIS 3.0
 */
public class FamilyAssociationNoteCategoryDesignatorDaoHibernateImpl 
	extends GenericHibernateDaoImpl<FamilyAssociationNoteCategoryDesignator>
	implements FamilyAssociationNoteCategoryDesignatorDao {

	/* Query names. */
	
	private static final String
		FIND_DESIGNATED_RELATIONSHIP_NOTE_CATEGORIES_QUERY_NAME
		= "findRelationshipNoteCategoriesDesignated";
	private static final String
		FIND_FAMILY_ASSO_NOTE_CAT_DES_BY_CATEGORY_QUERY_NAME
		= "findFamilyAssociationNoteCategoryDesignatorByCategory";
	private static final String
		FIND_RELATIONSHIP_NOTES_BY_FAMILY_ASSOCIATION_QUERY_NAME
		= "findRelationshipNotesByFamilyAssociation";
	private static final String FIND_DESIGNATED_NOTES_BY_RELATIONSHIP_QUERY_NAME
		= "findDesignatedRelationshipNotesByRelationship";
	
	/* Parameter names. */
	private static final String CATEGORY_PARAM_NAME = "category";
	private static final String FAMILKY_ASSOCIATION_PARAM_NAME
		= "familyAssociation";
	private static final String RELATIONSHIP_PARAM_NAME = "relationship";
	
	/**
	 * Instantiates a default instance of family association note category
	 * designator data access object entity implementation.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public FamilyAssociationNoteCategoryDesignatorDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<RelationshipNoteCategory> findAllCategories() {
		@SuppressWarnings("unchecked")
		List<RelationshipNoteCategory> designators =  
			(List<RelationshipNoteCategory>) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(
				FIND_DESIGNATED_RELATIONSHIP_NOTE_CATEGORIES_QUERY_NAME)
			.list();
		return designators;
	}
	
	/** {@inheritDoc} */
	@Override
	public FamilyAssociationNoteCategoryDesignator findByCatgory(
		final RelationshipNoteCategory	category) {
		FamilyAssociationNoteCategoryDesignator designator =  
			(FamilyAssociationNoteCategoryDesignator) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_FAMILY_ASSO_NOTE_CAT_DES_BY_CATEGORY_QUERY_NAME)
			.setParameter(CATEGORY_PARAM_NAME, category) 
			.uniqueResult();
		return designator;
	};
	
	/** {@inheritDoc} */
	@Override
	public List<RelationshipNote> findRelationshipNotes(final FamilyAssociation
		familyAssociation) {
		@SuppressWarnings("unchecked")
		List<RelationshipNote> notes =  
			(List<RelationshipNote>) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(
				FIND_RELATIONSHIP_NOTES_BY_FAMILY_ASSOCIATION_QUERY_NAME)
			.setParameter(FAMILKY_ASSOCIATION_PARAM_NAME, familyAssociation) 
			.list();
		return notes;
	}

	/** {@inheritDoc} */
	@Override
	public List<RelationshipNote> findDesignatedNotesByRelationship(
			final Relationship relationship) {
		@SuppressWarnings("unchecked")
		List<RelationshipNote> notes
			= this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_DESIGNATED_NOTES_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.list();
		return notes;
	}
}