package omis.chronologicalnote.exception;

import omis.exception.BusinessException;

/**
 * Chronological note category exists exception.
 * 
 * @author Joel Norris
 * @version 0.1.0 (February 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryExistsException extends BusinessException {

	private static final long serialVersionUID = 1L;

	/** 
	 * Instantiates a default instance of chronological note category exists exception.
	 */
	public ChronologicalNoteCategoryExistsException() {
		// Default instantiation
	}
	
	/**
	 * Instantiates a chronological note category exists exception with the specified message and cause.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public ChronologicalNoteCategoryExistsException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a chronological note category exists exception with the specified message.
	 * 
	 * @param message message
	 */
	public ChronologicalNoteCategoryExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Instantiates a chronological note category exists exception with the specified cause.
	 * 
	 * @param cause cause
	 */
	public ChronologicalNoteCategoryExistsException(final Throwable cause) {
		super(cause);
	}
}
