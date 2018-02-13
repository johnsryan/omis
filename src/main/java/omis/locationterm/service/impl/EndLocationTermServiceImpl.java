package omis.locationterm.service.impl;

import java.util.Date;

import omis.locationterm.domain.LocationTerm;
import omis.locationterm.service.EndLocationTermService;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;

/**
 * Implementation of service to end location terms.
 * 
 * @deprecated update location term instead
 * @author Stephen Abson
 * @version 0.0.1 (Feb 5, 2018)
 * @since OMIS 3.0
 */
@Deprecated
public class EndLocationTermServiceImpl
		implements EndLocationTermService {
	
	private final LocationTermDelegate locationTermDelegate;

	/**
	 * Instantiates implementation of service to end location term.s
	 * 
	 * @param locationTermDelegate delegate for location terms
	 */
	public EndLocationTermServiceImpl(
			final LocationTermDelegate locationTermDelegate) {
		this.locationTermDelegate = locationTermDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm endLocationTerm(
			final Offender offender, final Date effectiveDate) {
		return this.locationTermDelegate.endLocationTerm(
				offender, effectiveDate);
	}
}
