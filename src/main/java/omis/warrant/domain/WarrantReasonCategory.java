package omis.warrant.domain;

/**
 * Warrant reason category.
 * 
 * @author Annie Jacques 
 * @author: Joel Norris
 * @version 0.1.1 (April 4, 2018)
 * @since OMIS 3.0
 */
public enum WarrantReasonCategory {
	
	/** Warrant to Arrest Parolee. */
	WARRANT_PAROLEE,
	/** Warrant to Arrest Conditional Release. */
	WARRANT_CONDITIONAL_RELEASE,
	
	/** Authorization to Pick Up and Hold Probationer. */
	AUTHORIZATION_TO_PICKUP_AND_HOLD;
	
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