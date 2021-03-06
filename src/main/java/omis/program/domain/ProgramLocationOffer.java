package omis.program.domain;

import java.io.Serializable;

import omis.location.domain.Location;

/**
 * Offers program at facility.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Apr 11, 2016)
 * @since OMIS 3.0
 */
public interface ProgramLocationOffer
		extends Serializable {
	
	/**
	 * Sets ID.
	 * 
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns ID.
	 * 
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets program offered at location.
	 * 
	 * @param program program offered at location
	 */
	void setProgram(Program program);
	
	/**
	 * Returns program offered at location.
	 * 
	 * @return program offered at location
	 */
	Program getProgram();
	
	/**
	 * Sets location at which program is offered.
	 * 
	 * @param location location at which program is offered
	 */
	void setLocation(Location location);
	
	/**
	 * Returns location at which program is offered.
	 * 
	 * @return location at which program is offered
	 */
	Location getLocation();

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