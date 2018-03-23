package omis.family.domain;

import java.io.Serializable;

import omis.relationship.domain.RelationshipNoteCategory;

/** 
 * Family association note category designator.
 * 
 * @author Yidong Li.
 * @version 0.1.2 (March 8, 2018) 
 * @since OMIS 3.0 
 */
public interface FamilyAssociationNoteCategoryDesignator extends Serializable {
	
	/**
	 * Returns the id.
	 * 
	 * @return id
	 */
	Long getId();
	
	/**
	 * Sets the id.
	 * 
	 * @param id id
	 */
	void setId(Long id);
	
	/**
	 * Returns the relationship note category.
	 * 
	 * @return relationship note category
	 */
	RelationshipNoteCategory getRelationshipNoteCategory();
	
	/**
	 * Sets the relationship note category.
	 * 
	 * @param category relationship note category
	 */
	void setRelationshipNoteCategory(RelationshipNoteCategory category);
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);	
	
	/**
	 * Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}