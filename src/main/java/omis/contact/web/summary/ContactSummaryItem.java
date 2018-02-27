package omis.contact.web.summary;

import java.util.Date;
import java.util.Map;

import omis.contact.report.ContactSummaryItemReportService;
import omis.offender.domain.Offender;
import omis.offender.web.summary.AbstractSummaryItem;
import omis.offender.web.summary.SummaryItem;
import omis.offender.web.summary.SummaryItemRegistry;

/**
 * Contact Summary Item.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Oct 26, 2017)
 *@since OMIS 3.0
 *
 */
public class ContactSummaryItem extends AbstractSummaryItem
		implements SummaryItem {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CONTACT_SUMMARY_MODEL_KEY =
			"offenderContactSummary";
	
	private ContactSummaryItemReportService contactSummaryItemReportService;
	
	/**
	 * @param contactSummaryItemReportService - Contact Summary Report Service
	 * @param includedPageName - String
	 * @param summaryItemRegistry - Summary Item Registry
	 * @param order - int
	 * @param enabled - Boolean
	 */
	public ContactSummaryItem(
			final ContactSummaryItemReportService
				contactSummaryItemReportService,
			final String includedPageName,
			final SummaryItemRegistry summaryItemRegistry, final int order,
			final boolean enabled) {
		super(includedPageName, summaryItemRegistry, order, enabled);
		this.contactSummaryItemReportService = contactSummaryItemReportService;
	}

	/**{@inheritDoc} */
	@Override
	public void build(final Map<String, Object> map,
			final Offender offender, final Date date) {
		map.put(CONTACT_SUMMARY_MODEL_KEY, this.contactSummaryItemReportService
				.findContactSummaryByOffender(offender));
	}

}
