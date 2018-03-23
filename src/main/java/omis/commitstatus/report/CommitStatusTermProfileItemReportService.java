package omis.commitstatus.report;

import java.util.Date;

import omis.offender.domain.Offender;

/**
 * Report service for commit status profile items.
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.1.1 (Mar 22, 2018)
 * @since OMIS 3.0
 */
public interface CommitStatusTermProfileItemReportService {

	/**
	 * Returns count of commit status term effective on date.
	 * 
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return commit status term count on effective date
	 */
	Integer findCommitStatusTermCountByOffenderAndDate(Offender offender, 
			Date effectiveDate);
}