package omis.warrant.report;

import omis.offender.domain.Offender;

/**
 * Warrant profile item report service.
 * 
 * @author Joel Norris
 * @version 0.1.0 (April 3, 2018)
 * @since OMIS 3.0
 */
public interface WarrantProfileItemReportService {

	/**
	 * Counts warrants for the specified offender.
	 * 
	 * @param offender offender
	 * @return count of warrants
	 */
	Integer countWarrantsByOffender(Offender offender);
}
