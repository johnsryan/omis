/**
 * 
 */
package omis.facility.domain;

import java.io.Serializable;

import omis.location.domain.Location;

/**
 * @author Joel Norris 
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb, 08 2013)
 * @since OMIS 3.0
 */
public interface Facility extends Serializable {

	/**
	 * Returns the ID.
	 * 
	 * @return the id
	 */
	Long getId();

	/**
	 * Sets the ID.
	 * 
	 * @param id the id to set
	 */
	void setId(Long id);
	
	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 */
	void setName(String name);
	
	/**
	 * Returns the location.
	 * 
	 * @return the location
	 */
	Location getLocation();

	/**
	 * Sets the location.
	 * 
	 * @param location the location to set
	 */
	void setLocation(Location location);
	
	/**
	 * Returns the abbreviation.
	 * 
	 * @return abbreviation
	 */
	String getAbbreviation();
	
	/**
	 * Sets the abbreviation.
	 * 
	 * @param abbreviation abbreviation
	 */
	void setAbbreviation(String abbreviation);
	
	/**
	 * Returns the active value.
	 * 
	 * @return active
	 */
	Boolean getActive();
	
	/**
	 * Sets the active value.
	 * 
	 * @param active active
	 */
	void setActive(Boolean active);
	
	/**
	 * Sets the telephone number.
	 * 
	 * @param telephoneNumber telephone number
	 */
	void setTelephoneNumber(Long telephoneNumber);
	
	/**
	 * Returns the telephone number.
	 * 
	 * @return telephone number
	 */
	Long getTelephoneNumber();
	
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
}
