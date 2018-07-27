package omis.address.domain;

import java.io.Serializable;

import omis.region.domain.City;

/**
 * ZIP code.
 * @author Stephen Abson
 * @version 0.1.0 (Jan 25, 2013)
 * @since OMIS 3.0
 */
public interface ZipCode
		extends Serializable {

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
	 * Sets the value.
	 * @param value value
	 */
	void setValue(String value);
	
	/**
	 * Returns the value.
	 * @return value
	 */
	String getValue();
	
	/**
	 * Sets the extension.
	 * @param extension extension
	 */
	void setExtension(String extension);
	
	/**
	 * Returns the extension.
	 * @return extension
	 */
	String getExtension();
	
	/**
	 * Sets the city.
	 * @param city city
	 */
	void setCity(City city);
	
	/**
	 * Returns the city.
	 * @return city
	 */
	City getCity();
	
	/**
	 * Sets whether the ZIP code is valid.
	 * @param valid whethe ZIP code is valid
	 */
	void setValid(Boolean valid);
	
	/**
	 * Returns whether the ZIP code is valid.
	 * 
	 * @return whether ZIP code is valid
	 */
	Boolean getValid();
	
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
	 * Returns string representation of {@code this} including value, extension,
	 * city and country.
	 * 
	 * @return string representation of {@code this} including value, extension,
	 * city and country
	 */
	@Override
	String toString();
}
