package omis.warrant.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.warrant.report.WarrantProfileItemReportService;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/**
 * Warrant profile item.
 * 
 * @author Joel Norris
 * @version 0.1.0 (April 3, 2018)
 * @since OMIS 3.0
 */
public class WarrantProfileItem
	extends AbstractProfileItem
	implements ProfileItem {
	
	private static final long serialVersionUID = 1L;
	
	/* Model keys. */
	
	private static final String WARRANT_COUNT_MODEL_KEY = "warrantCount";
	
	/* Profile item report service. */
	
	private final WarrantProfileItemReportService warrantProfileItemReportService;
	
	public WarrantProfileItem(final Set<String> requiredAuthorizations,
			final String includePage,
			final String name,
			final ProfileItemRegistry profileItemRegistry,
			final WarrantProfileItemReportService warrantProfileItemReportService,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry, enabled);
		this.warrantProfileItemReportService = warrantProfileItemReportService;
	}

	/** {@inheritDoc} */
	@Override
	public void build(final Map<String, Object> map, final Offender offender,
			final UserAccount userAccount, final Date date) {
		map.put(WARRANT_COUNT_MODEL_KEY,
				this.warrantProfileItemReportService.countWarrantsByOffender(offender));
	}
}