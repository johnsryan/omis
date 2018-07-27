package omis.organization.domain;

import java.io.Serializable;

/**
 * Organization.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 19, 2013)
 * @since OMIS 3.0
 */
public interface Organization
		extends Serializable {

	/**
	 * Sets the ID.
	 * 
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns the ID.
	 * 
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the name.
	 * 
	 * @param name name
	 */
	void setName(String name);
	
	/**
	 * Returns the name.
	 * 
	 * @return name
	 */
	String getName();
	
	/**
	 * Sets alias.
	 * 
	 * @param alias alias
	 */
	void setAlias(String alias);
	
	/**
	 * Returns alias.
	 * 
	 * @return alias
	 */
	String getAlias();
	
	/**
	 * Sets the parent organization.
	 * 
	 * @param parent parent organization
	 */
	void setParent(Organization parent);
	
	/**
	 * Returns the parent organization.
	 * 
	 * @return parent organization
	 */
	Organization getParent();
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * 
	 * <p>Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * 
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
	 * 
	 * <p>Any mandatory property of {@code this} may be used in the hash code.
	 * If a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * 
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
	
	/**
	 * Returns textual representation of {@code this} including name, alias
	 * and parent name.
	 * 
	 * @return textual representation of {@code this} including name, alias
	 * and parent name
	 */
	@Override
	String toString() ;
}