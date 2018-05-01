package omis.warrant.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * WarrantNote.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public interface WarrantNote extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the WarrantNote
	 * @return ID
	 */
	public Long getId();
	
	/**
	 * Sets the ID of the WarrantNote
	 * @param id - Long
	 */
	public void setId(Long id);
	
	/**
	 * Returns the Warrant for the WarrantNote
	 * @return warrant - Warrant
	 */
	public Warrant getWarrant();
	
	/**
	 * Sets the Warrant for the WarrantNote
	 * @param warrant - Warrant
	 */
	public void setWarrant(Warrant warrant);
	
	/**
	 * Returns the Date for the WarrantNote
	 * @return date - Date
	 */
	public Date getDate();
	
	/**
	 * Sets the Date for the WarrantNote
	 * @param date - Date
	 */
	public void setDate(Date date);
	
	/**
	 * Returns the value.
	 * 
	 * @return value
	 */
	public String getValue();
	
	/**
	 * Sets the value.
	 * 
	 * @param value value
	 */
	public void setValue(String value);
	
	/** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	int hashCode();
	
}
