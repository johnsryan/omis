package omis.bedplacement.report;

import java.util.Date;

import omis.offender.domain.Offender;

/** Profile item for bed placement.
 * @author Ryan Johns
 * @author Joel Norris
 * @version 0.1.1 (Mar 12, 2018)
 * @since OMIS 3.0 */
public interface BedPlacementProfileItemReportService {
	/** Finds bed placement existence by offender and date.
	 * @param offender - offender.
	 * @param effectiveDate - effective date.
	 * @return true if exists. */
	Boolean findBedPlacementExistenceByOffenderAndDate(Offender offender, 
			Date effectiveDate);
}
