package omis.warrant.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.jail.domain.Jail;

/**
 * WarrantArrest.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public interface WarrantArrest extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the WarrantArrest
	 * @return ID
	 */
	public Long getId();
	
	/**
	 * Sets the ID of the WarrantArrest
	 * @param id - Long
	 */
	public void setId(Long id);
	
	/**
	 * Returns the Warrant for the WarrantArrest
	 * @return warrant - Warrant
	 */
	public Warrant getWarrant();
	
	/**
	 * Sets the Warrant for the WarrantArrest
	 * @param warrant - Warrant
	 */
	public void setWarrant(Warrant warrant);
	
	/**
	 * Returns the Date for the WarrantArrest
	 * @return date - Date
	 */
	public Date getDate();
	
	/**
	 * Sets the Date for the WarrantArrest
	 * @param date - Date
	 */
	public void setDate(Date date);
	
	/**
	 * Returns the Jail for the WarrantArrest
	 * @return jail - Jail
	 */
	public Jail getJail();
	
	/**
	 * Sets the Jail for the WarrantArrest
	 * @param jail - Jail
	 */
	public void setJail(Jail jail);
	
	/**
	 * Returns the determination deadline.
	 * 
	 * @return determination deadline
	 */
	public Date getDeterminationDeadline();
	
	/**
	 * Sets the determination deadline.
	 * 
	 * @param determinationDeadline determination deadline
	 */
	public void setDeterminationDeadline(Date determinationDeadline);
	
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
