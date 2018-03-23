package omis.chronologicalnote.exception;

import omis.exception.BusinessException;

/**
 * Chronological note category template exists exception.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryTemplateExistsException
	extends BusinessException {

	private static final long serialVersionUID = 1L;

	/** 
	 * Instantiates a default instance of chronological note category template
	 * exists exception.
	 */
	public ChronologicalNoteCategoryTemplateExistsException() {
		// Default instantiation
	}
	
	/**
	 * Instantiates a chronological note category template exists exception with
	 * the specified message and cause.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public ChronologicalNoteCategoryTemplateExistsException(
		final String message,
		final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a chronological note category template exists exception with
	 * the specified message.
	 * 
	 * @param message message
	 */
	public ChronologicalNoteCategoryTemplateExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Instantiates a chronological note category template exists exception with
	 * the specified cause.
	 * 
	 * @param cause cause
	 */
	public ChronologicalNoteCategoryTemplateExistsException(
		final Throwable cause) {
		super(cause);
	}
}
