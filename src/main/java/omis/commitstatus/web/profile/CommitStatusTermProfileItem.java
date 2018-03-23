package omis.commitstatus.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.commitstatus.report.CommitStatusTermProfileItemReportService;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/**
 * Profile item for commit status term.
 *
 * @author Yidong Li
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class CommitStatusTermProfileItem
		extends AbstractProfileItem
		implements ProfileItem {
	
	private static final long serialVersionUID = 1L;
	
	private final CommitStatusTermProfileItemReportService
			commitStatusTermProfileItemReportService;
	
	private static final String COMMIT_STATUS_TERM_COUNT_MODEL_KEY = 
			"commitStatusTermCount";

	/**
	 * Instantiates profile item for commit status term.
	 * 
	 * @param requiredAuthorizations required authorizations
	 * @param includePage include page
	 * @param name name
	 * @param profileItemRegistry profile item registry
	 * @param commitStatusTermProfileItemReportService report service
	 * for profile items for alternative offender names
	 * @param enabled whether enabled
	 */
	public CommitStatusTermProfileItem(
			final Set<String> requiredAuthorizations,
			final String includePage,
			final String name,
			final ProfileItemRegistry profileItemRegistry,
			final CommitStatusTermProfileItemReportService
			commitStatusTermProfileItemReportService,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
		this.commitStatusTermProfileItemReportService
			= commitStatusTermProfileItemReportService;
	}

	/** {@inheritDoc} */
	@Override
	public void build(
			final Map<String, Object> modelMap,
			final Offender offender,
			final UserAccount userAccount,
			final Date date) {
		Integer count = this.commitStatusTermProfileItemReportService
				.findCommitStatusTermCountByOffenderAndDate(offender, date);
		modelMap.put(COMMIT_STATUS_TERM_COUNT_MODEL_KEY, count);
	}
}