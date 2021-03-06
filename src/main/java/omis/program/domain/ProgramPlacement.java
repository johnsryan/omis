package omis.program.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.datatype.DateRange;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.OffenderAssociable;
import omis.supervision.domain.PlacementTerm;

/**
 * Program placement.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Dec 8, 2015)
 * @since OMIS 3.0
 */
public interface ProgramPlacement
		extends OffenderAssociable, Creatable, Updatable {

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
	 * Sets date range.
	 * 
	 * @param dateRange date range
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns date range.
	 * 
	 * @return date range
	 */
	DateRange getDateRange();
	
	/**
	 * Sets program.
	 * 
	 * @param program program
	 */
	void setProgram(Program program);
	
	/**
	 * Returns program.
	 * 
	 * @return program
	 */
	Program getProgram();
	
	/**
	 * Sets placement term.
	 * 
	 * @param placementTerm placement term
	 */
	void setPlacementTerm(PlacementTerm placementTerm);
	
	/**
	 * Returns placement term.
	 * 
	 * @return placement term
	 */
	PlacementTerm getPlacementTerm();
	
	/**
	 * Sets location term.
	 * 
	 * @param locationTerm location term
	 */
	void setLocationTerm(LocationTerm locationTerm);
	
	/**
	 * Returns location term.
	 * 
	 * @return location term
	 */
	LocationTerm getLocationTerm();
	
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