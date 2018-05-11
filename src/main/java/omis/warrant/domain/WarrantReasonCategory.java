package omis.warrant.domain;

/**
 * Warrant reason category.
 * 
 * @author Annie Jacques 
 * @author: Joel Norris
 * @author: Stephen Abson
 * @version 0.1.1 (April 4, 2018)
 * @since OMIS 3.0
 */
public enum WarrantReasonCategory {
	
	/** Warrant to arrest parolee. */
	ARREST_PAROLEE,
	
	/** Warrant to arrest conditional release offender. */
	ARREST_CONDITIONAL_RELEASE_OFFENDER,
	
	/** Authorization to pick up and hold probationer. */
	AUTHORIZATION_TO_PICKUP_AND_HOLD_PROBATIONER,
	
	/** Warrant to arrest ISC Offender */
	ARREST_INTERSTATE_COMPACT_OFFENDER,
	
	/** Warrant to arrest parole absconder. */
	ARREST_PAROLE_ABSCONDER,
	
	/** Warrant to arrest conditional release absconder. */
	ARREST_CONDITIONAL_RELEASE_ABSCONDER;
	
	/**
	 * Returns the instance name.
	 * 
	 * @return instance name
	 */
	public String getName() {
		return this.name();
	}
	
	/**
	 * Returns the name.
	 * 
	 * @return name
	 */
	@Override
	public String toString() {
		return this.name();
	}	
}