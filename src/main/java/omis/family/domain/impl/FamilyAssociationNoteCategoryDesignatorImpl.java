package omis.family.domain.impl;

import omis.family.domain.FamilyAssociationNoteCategoryDesignator;
import omis.relationship.domain.RelationshipNoteCategory;

/**
 * Family association note category designator implementation.
 * 
 * @author Yidong Li
 * @version 0.1.1 (March 8, 2018)
 * @since OMIS 3.0
 */
public class FamilyAssociationNoteCategoryDesignatorImpl
	implements FamilyAssociationNoteCategoryDesignator {
	private static final long serialVersionUID = 1L;
	private Long id;
	private RelationshipNoteCategory relationshipNoteCategory;
	
	/** Instantiates an implementation of family association note category
	 * designator. */
	public FamilyAssociationNoteCategoryDesignatorImpl() {
		// do nothing
	}
	
	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	/** {@inheritDoc} */
	@Override
	public RelationshipNoteCategory getRelationshipNoteCategory() {
		return this.relationshipNoteCategory;
	}

	/** {@inheritDoc} */
	@Override
	public void setRelationshipNoteCategory(final RelationshipNoteCategory
		relationshipNoteCategory) {
		this.relationshipNoteCategory = relationshipNoteCategory;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FamilyAssociationNoteCategoryDesignator)) {
			return false;
		}
		
		FamilyAssociationNoteCategoryDesignator that
			= (FamilyAssociationNoteCategoryDesignator) obj;
		
		if (this.getRelationshipNoteCategory() == null) {
			throw new IllegalStateException(
			"Relationship note category required.");
		}
		
		if (!this.getRelationshipNoteCategory().equals(
			that.getRelationshipNoteCategory())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getRelationshipNoteCategory() == null) {
			throw new IllegalStateException("Relationship note category"
					+ "required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode
			+ this.getRelationshipNoteCategory().hashCode();
		return hashCode;
	}
}