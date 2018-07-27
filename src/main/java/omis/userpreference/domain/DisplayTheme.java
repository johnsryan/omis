package omis.userpreference.domain;

/**
 * Display theme enumeration.
 * 
 * @author Joel Norris
 * @version 0.1.0 (May 14, 2018)
 * @since OMIS 3.0
 */
public enum DisplayTheme {
	
	/** Default. */
	DEFAULT,
	
	/** High contrast. */
	HIGH_CONTRAST;
	
	/**
	 * 	Returns the name.
	 * 
	 * @return name
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
