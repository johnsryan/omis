package omis.need.web.report;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.need.report.NeedProfileItemReportService;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/** Needs profile item.
 * @author Ryan Johns
 * @author Trevor Isles
 * @version 0.1.0 (Mar 16 2016)
 * @since OMIS 3.0 */
public class NeedProfileItem 
	extends AbstractProfileItem implements ProfileItem {
	private static final long serialVersionUID = 1L;
	private static final String NEED_COUNT_MODEL_KEY = "needsCount";

	private final NeedProfileItemReportService needProfileItemReportService;

	/** Constructor.
	 * @param requiredAuthorizations - required authorizations.
	 * @param includePage - include page.
	 * @param name - name.
	 * @param profileItemRegistry - profile item registry.
	 * @param needProfileItemReportService - need profile item report service.
	 * @param enabled - whether enabled */
	public NeedProfileItem(final Set<String> requiredAuthorizations, 
			final String includePage, final String name,
			final ProfileItemRegistry profileItemRegistry,
			final NeedProfileItemReportService needProfileItemReportService,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
		this.needProfileItemReportService = needProfileItemReportService;
	}

	/** {@inheritDoc} */
	@Override
	public void build(final Map<String, Object> map, final Offender offender,
			final UserAccount userAccount,
			final Date date) {
		map.put(NEED_COUNT_MODEL_KEY, this.needProfileItemReportService.findCasePlanObjectiveCountByOffender(offender));
	}

}
