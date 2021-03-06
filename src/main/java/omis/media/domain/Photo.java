package omis.media.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * Photo.
 * 
 * @author Stephen Abson
 * @version 0.1.2 (Feb 8, 2013)
 * @since OMIS 3.0
 */
public interface Photo extends Creatable, Updatable {

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
	 * Sets the filename.
	 * 
	 * @param filename filename
	 */
	void setFilename(String filename);
	
	/**
	 * Returns the filename.
	 * 
	 * @return filename
	 */
	String getFilename();
	
	/**
	 * Sets the photo date.
	 * 
	 * @param date date
	 */
	void setDate(Date date);
	
	/**
	 * Returns the photo date.
	 * 
	 * @return date
	 */
	Date getDate();
	
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
	 * Returns a string representation containing the filename.
	 * 
	 * @return string containing filename
	 */
	@Override
	String toString();
}