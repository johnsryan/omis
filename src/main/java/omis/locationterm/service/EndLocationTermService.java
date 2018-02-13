package omis.locationterm.service;

import java.util.Date;

import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;

/**
 * Service to end location term.
 * 
 * @deprecated update location term instead
 * @author Stephen Abson
 * @version 0.0.1 (Feb 5, 2018)
 * @since OMIS 3.0
 */
@Deprecated
public interface EndLocationTermService {

	/**
	 * Ends location term.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return ended location term
	 */
	LocationTerm endLocationTerm(Offender offender, Date effectiveDate);
}