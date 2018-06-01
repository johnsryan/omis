/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.offender.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.offender.domain.Offender;
import omis.offender.report.AlternativeOffenderIdentityProfileItemReportService;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/** Profile item for offender alternative identity.
 * @author Ryan Johns
 * @author Trevor Isles
 * @version 0.1.0 (Apr 12, 2016)
 * @since OMIS 3.0 */
public class OffenderAlternativeIdentityProfileItem 
	extends AbstractProfileItem 
	implements ProfileItem {
	private final static long serialVersionUID = 1L;
	
	private final static String 
	ALTERNATIVE_OFFENDER_IDENTITY_COUNT_MODEL_KEY 
		= "alternativeOffenderIdentityCount";
	
	private final AlternativeOffenderIdentityProfileItemReportService
		alternativeOffenderIdentityProfileItemReportService;
	
	/** Constructor.
	 * @param requiredAuthorizations - required authorizations.
	 * @param includePage - include page.
	 * @param profileItemRegistry - profile item registry.
	 * @param name - name.
	 * @param alternativeOffenderIdentityProfileItemReportService - alternative
	 * offender identity profile item report service. 
	 * @param enabled - whether enabled*/
	public OffenderAlternativeIdentityProfileItem(
			final Set<String> requiredAuthorizations,
			final String includePage, 
			final ProfileItemRegistry profileItemRegistry,
			final String name,
			final AlternativeOffenderIdentityProfileItemReportService 
			alternativeOffenderIdentityProfileItemReportService,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
		this.alternativeOffenderIdentityProfileItemReportService 
		= alternativeOffenderIdentityProfileItemReportService;
	}
	
	/** {@inheritDoc} */
	@Override
	public void build(final Map<String, Object> map, final Offender offender,
			final UserAccount userAccount, final Date date) {
		map.put(ALTERNATIVE_OFFENDER_IDENTITY_COUNT_MODEL_KEY, 
				this.alternativeOffenderIdentityProfileItemReportService
				.findAlternativeOffenderIdentityCountByOffenderAndDate(
						offender, date));
	}
}

