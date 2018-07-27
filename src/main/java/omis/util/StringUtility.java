package omis.util;

/**
 * Utility for strings.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jan 5, 2016)
 * @since OMIS 3.0
 */
public final class StringUtility {

	// Prevents accidental instantiation...
	private StringUtility() {
		throw new AssertionError("StringUtility instances not allowed");
	}
	
	/**
	 * Returns whether string is {@code null} or empty.
	 * 
	 * @param string string
	 * @return whether string is {@code null} or empty
	 */
	public static boolean isNullOrEmpty(final String string) {
		return string == null || string.isEmpty();
	}
	
	/**
	 * Returns whether all strings are {@code null} or empty.
	 * 
	 * <p>At least two strings are required.
	 * 
	 * @param firstString first string
	 * @param secondString second string
	 * @param strings other strings
	 * @return whether all strings are {@code null} or empty
	 */
	public static boolean areNullOrEmpty(final String firstString,
			final String secondString, final String... strings) {
		if (firstString != null && !firstString.isEmpty()) {
			return false;
		}
		if (secondString != null && !secondString.isEmpty()) {
			return false;
		}
		for (String string : strings) {
			if (string != null && !string.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns whether string is not {@code null} and not empty.
	 * 
	 * @param string string
	 * @return whether string is not {@code null} and not empty
	 */
	public static boolean hasContent(final String string) {
		return string != null && !string.isEmpty();
	}
	
	/**
	 * Returns whether all string are not {@code null} and are not empty.
	 * 
	 * <p>At least two strings are required.
	 * 
	 * @param firstString first string
	 * @param secondString second string
	 * @param strings other strings
	 * @return whether or string are not {@code null} and are not empty
	 */
	public static boolean haveContent(final String firstString,
			final String secondString, final String... strings) {
		if (firstString == null || firstString.isEmpty()) {
			return false;
		}
		if (secondString == null || secondString.isEmpty()) {
			return false;
		}
		for (String string : strings) {
			if (string == null || string.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns whether value is an integer.
	 * 
	 * <p>The value must not have a fractional part.
	 * 
	 * @param value value
	 * @return whether value is an integer
	 */
	public static boolean isIntegral(final String value) {
		return value.matches("-?\\d+");  
	}
	
	/**
	 * Returns whether value is numeric.
	 * 
	 * <p>The value may have a fractional part.
	 * 
	 * @param value value
	 * @return whether value is numeric
	 */
	public static boolean isNumeric(final String value) {
		return value.matches("-?\\d+(\\.\\d+)?");
	}
	
	/**
	 * Returns whether value is a decimal..
	 * 
	 * <p>The value must have a fractional part.
	 * 
	 * @param value value
	 * @return whether value is a decimal
	 */
	public static boolean isDecimal(final String value) {
		return value.matches("-?\\d+\\.\\d+");
	}
}