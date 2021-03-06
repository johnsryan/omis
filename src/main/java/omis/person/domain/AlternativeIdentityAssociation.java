package omis.person.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.datatype.DateRange;

/**
 * Alternative person identity association.
 * 
 * <p>The {@code identity} of this should <b>never</b> be equal to the
 * {@code identity} of {@code person}
 * ({@code this.getPerson().getIdentity().equals(this.getIdentity())}
 * should <b>always</b> return {@code false}).
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 20, 2013)
 * @since OMIS 3.0
 */
public interface AlternativeIdentityAssociation
		extends Creatable, Updatable {

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
	 * Sets the identity.
	 * 
	 * @param identity identity
	 */
	void setIdentity(PersonIdentity identity);
	
	/**
	 * Returns the identity.
	 * 
	 * @return identity
	 */
	PersonIdentity getIdentity();
	
	/**
	 * Sets the date range.
	 * 
	 * @param dateRange date range
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns the date range.
	 * 
	 * @return date range
	 */
	DateRange getDateRange();
	
	/**
	 * Sets the category.
	 * 
	 * @param category category
	 */
	void setCategory(AlternativeIdentityCategory category);
	
	/**
	 * Returns the category.
	 * 
	 * @return category
	 */
	AlternativeIdentityCategory getCategory();	
	
	/**
	 * Sets the alternative name association. 
	 * 
	 * @param alternativeNameAssociation alternative name association
	 */
	void setAlternativeNameAssociation(AlternativeNameAssociation 
			alternativeNameAssociation);
	
	/**
	 * Returns the alternative name association.
	 * 
	 * @return alternative name association
	 */
	AlternativeNameAssociation getAlternativeNameAssociation();
	
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
}