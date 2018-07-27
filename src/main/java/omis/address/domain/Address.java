package omis.address.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * Address.
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 25, 2013)
 * @since OMIS 3.0
 */
public interface Address
		extends Creatable, Updatable {

	/**
	 * Sets the ID.
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns the ID.
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Returns the value.
	 * 
	 * @return value
	 */
	String getValue();
	
	/**
	 * Sets the value.
	 * 
	 * @param value value
	 */
	void setValue(String value);
	
	/**
	 * Sets the ZIP code.
	 * @param zipCode ZIP code
	 */
	void setZipCode(ZipCode zipCode);
	
	/**
	 * Returns the ZIP code.
	 * @return ZIP code
	 */
	ZipCode getZipCode();
	
	/**
	 * Sets building category.
	 * 
	 * @param buildingCategory building category
	 */
	void setBuildingCategory(BuildingCategory buildingCategory);
	
	/**
	 * Returns building category.
	 * 
	 * @return building category
	 */
	BuildingCategory getBuildingCategory();
	
	/**
	 * Sets designator.
	 * 
	 * @param designator designator
	 */
	void setDesignator(String designator);
	
	/**
	 * Returns the address designator.
	 * 
	 * @return designator
	 */
	String getDesignator();
	
	/**
	 * Sets the coordinates.
	 * 
	 * @param coordinates coordinates
	 */
	void setCoordinates(String coordinates);
	
	/**
	 * Returns the coordinates.
	 * 
	 * @return coordinates
	 */
	String getCoordinates();
	
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
	
	/**
	 * Returns a string representation of the address containing the value and
	 * ZIP code.
	 * @return string including value and ZIP code
	 */
	@Override
	String toString();	
}