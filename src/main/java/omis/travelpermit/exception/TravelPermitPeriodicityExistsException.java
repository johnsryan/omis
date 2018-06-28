package omis.travelpermit.exception;

import omis.exception.DuplicateEntityFoundException;

public class TravelPermitPeriodicityExistsException
extends DuplicateEntityFoundException {

private static final long serialVersionUID = 1L;
	
	/** Instantiates default residence term exists exception. */
	public TravelPermitPeriodicityExistsException() {
		//Default constructor
	}

	/**
	 * Constructor.
	 * 
	 * @param message message
	 * @param cause cause
	 */
	public TravelPermitPeriodicityExistsException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message message
	 */
	public TravelPermitPeriodicityExistsException(final String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause cause
	 */
	public TravelPermitPeriodicityExistsException(final Throwable cause) {
		super(cause);
	}
}
