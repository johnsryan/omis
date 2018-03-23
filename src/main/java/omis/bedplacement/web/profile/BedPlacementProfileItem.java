package omis.bedplacement.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.bedplacement.report.BedPlacementProfileItemReportService;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/** Profile item for bed placement.
 * @author Ryan Johns
 * @author Trevor Isles
 * @version 0.1.0 (Mar 14, 2016)
 * @since OMIS 3.0 */
public class BedPlacementProfileItem 
	extends AbstractProfileItem 
	implements ProfileItem {
	private final static long serialVersionUID = 1L;
	
	/* Model keys. */
	private final static String BED_PLACEMENT_MODEL_KEY = "bedPlacement";
	
	private final BedPlacementProfileItemReportService 
		bedPlacementProfileItemReportService;
	
	/** Constructor.
	 * @param requiredAuthorizations - require authorizations.
	 * @param includePage - include page.
	 * @param profileItemRegistry - profile item registry.
	 * @param name - name
	 * @param bedPlacementProfileItemReportService - bed placement profile
	 * item report service.
	 * @param enabled whether enabled*/
	public BedPlacementProfileItem(final Set<String> requiredAuthorizations,
			final String includePage,
			final String name,
			final ProfileItemRegistry profileItemRegistry,
			final BedPlacementProfileItemReportService 
				bedPlacementProfileItemReportService,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
		this.bedPlacementProfileItemReportService 
			= bedPlacementProfileItemReportService;
	}
	
	/** {@inheritDoc} */
	@Override
	public void build(final Map<String, Object> map, final Offender offender,
			final UserAccount userAccount, final Date date) {
		map.put(BED_PLACEMENT_MODEL_KEY, this
				.bedPlacementProfileItemReportService
				.findBedPlacementExistenceByOffenderAndDate(offender,date));
	}
}
