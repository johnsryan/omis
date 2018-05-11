package omis.hearing.domain;

/**
 * DispositionCategory.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Apr 17, 2017)
 *@since OMIS 3.0
 *
 */
public enum DispositionCategory {
	
	DISMISSED,
	
	GUILTY,
	
	NOT_GUILTY,
	
	NO_FINDING,
	
	REDUCED;
	
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
