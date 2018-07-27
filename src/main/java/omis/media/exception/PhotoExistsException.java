package omis.media.exception;

import omis.exception.DuplicateEntityFoundException;

/**
 * Photo exists exception.
 * 
 * @author Joel Norris
 * @version 0.1.0 (July 9, 2018)
 * @since OMIS 3.0
 */
public class PhotoExistsException extends DuplicateEntityFoundException {

	private static final long serialVersionUID = 1L;

	/** Instantiates a default instance of photo exists exception. */
	public PhotoExistsException() {
		// Default instantiation
	}
	
	/**
	 * Instantiates photo exists exception with the specified message and cause.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public PhotoExistsException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates photo exists exception with the specified message.
	 * 
	 * @param message message
	 */
	public PhotoExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Instantiates photo exists exception with the specified cause.
	 * 
	 * @param cause cause
	 */
	public PhotoExistsException(final Throwable cause) {
		super(cause);
	}
}