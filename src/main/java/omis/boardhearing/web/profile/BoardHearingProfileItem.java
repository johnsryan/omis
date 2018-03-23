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
package omis.boardhearing.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import omis.boardhearing.report.BoardHearingProfileItemReportService;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/**
 * Board Hearing Profile Item.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 19, 2018)
 *@since OMIS 3.0
 *
 */
public class BoardHearingProfileItem extends AbstractProfileItem
		implements ProfileItem {
	
	private static final long serialVersionUID = 1L;
	
	private static final String BOARD_HEARING_COUNT_MODEL_KEY =
			"boardHearingCount";
	
	private final BoardHearingProfileItemReportService
			boardHearingProfileItemReportService;
	
	/**
	 * Constructor for Board Hearing Profile Item.
	 * 
	 * @param requiredAuthorizations - Set of Strings Required Authorizations
	 * @param includePage - String include Page
	 * @param name - String name
	 * @param profileItemRegistry - Profile Item Registry
	 * @param enabled - Boolean enabled
	 * @param boardHearingProfileItemReportService - Board Hearing Profile
	 * Item Report Service
	 */
	public BoardHearingProfileItem(final Set<String> requiredAuthorizations,
			final String includePage, final String name,
			final ProfileItemRegistry profileItemRegistry,
			final Boolean enabled,
			final BoardHearingProfileItemReportService
					boardHearingProfileItemReportService) {
		super(requiredAuthorizations, includePage, name,
				profileItemRegistry, enabled);
		this.boardHearingProfileItemReportService =
				boardHearingProfileItemReportService;
	}

	/**{@inheritDoc} */
	@Override
	public void build(final Map<String, Object> map,
			final Offender offender, final UserAccount userAccount,
			final Date date) {
		map.put(BOARD_HEARING_COUNT_MODEL_KEY,
				this.boardHearingProfileItemReportService
				.findBoardHearingCountByOffender(offender));
	}
}
