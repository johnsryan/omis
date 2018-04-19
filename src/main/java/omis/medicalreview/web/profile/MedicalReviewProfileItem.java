package omis.medicalreview.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/** Profile item for medical review.
 * @author Ryan Johns
 * @version 0.1.0 (Apr 13, 2018)
 * @since OMIS 3.0 */
public class MedicalReviewProfileItem 
	extends AbstractProfileItem 
	implements ProfileItem {
	private static final long serialVersionUID = 1L;

	/** Constructor. */
	public MedicalReviewProfileItem(
			final Set<String> requiredAuthorizations, 
			final String includePage, 
			final ProfileItemRegistry profileItemRegistry,
			final String name,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
	}
	
	/** {@inheritDoc} */
	@Override
	public void build(Map<String, Object> modelMap, Offender offender, 
			UserAccount userAccount, Date date) {
			/* Do nothing. */
	
	}
}
