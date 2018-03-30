package omis.health.domain;

import java.io.Serializable;

import omis.location.domain.Location;

/**
 * Medical facility.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jun 23, 2014)
 * @since OMIS 3.0
 */
public interface MedicalFacility
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
	 * Sets the abbreviation.
	 * 
	 * @param abbreviation abbreviation
	 */
	void setAbbreviation(String abbreviation);
	
	/**
	 * Returns the abbreviation.
	 * 
	 * @return abbreviation
	 */
	String getAbbreviation();
	
	/**
	 * Sets the location.
	 * 
	 * @param location location
	 */
	void setLocation(Location location);
	
	/**
	 * Returns the location.
	 * 
	 * @return location
	 */
	Location getLocation();
	
	/**
	 * Sets whether {@code this} is active.
	 * 
	 * @param active whether {@code this} is active.
	 */
	void setActive(Boolean active);
	
	/**
	 * Returns whether {@code this} is active.
	 * 
	 * @return whether {@code 
	 */
	Boolean getActive();
	
	/**
	 * Sets whether hospital.
	 * 
	 * @param hospital whether hospital
	 */
	void setHospital(Boolean hospital);
	
	/**
	 * Returns whether hospital.
	 * 
	 * @return whether hospital
	 */
	Boolean getHospital();
	
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