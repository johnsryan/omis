package omis.caseload.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/** Caseload profile item.
 * @author Ryan Johns
 * @version 0.1.0 (Jul 13, 2018)
 * @since OMIS 3.0 */
public class CaseloadProfileItem extends AbstractProfileItem implements ProfileItem {
	private static final long serialVersionUID = 1L;
	
	/** Constructor. 
	 * @param requiredAuthorizations - required authorizations. 
	 * @param includePage - include page.
	 * @param name - name.
	 * @param profileItemRegistry - profile item registry.
	 * @param enabled - enabled. */
	public CaseloadProfileItem(final Set<String> requiredAuthorizations, 
			final String includePage, final String name, 
			final ProfileItemRegistry profileItemRegistry, 
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
	}
	
	/** {@inheritDoc} */
	@Override
	public void build(final Map<String, Object> modelMap, final Offender offender, 
			final UserAccount userAccount, final Date date) {
		//Do nothing.
	}

}
