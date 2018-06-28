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
package omis.travelpermit.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.offender.domain.Offender;
import omis.travelpermit.report.TravelPermitProfileItemReportService;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/**
 * Travel permit profile item.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jun 6, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitProfileItem extends AbstractProfileItem 
	implements ProfileItem{

	private static final long serialVersionUID = 1L;
	
	private static final String TRAVEL_PERMIT_COUNT_MODEL_KEY
		= "travelPermitCount";

	private final TravelPermitProfileItemReportService
		travelPermitProfileItemReportService; 

	/** Instantiates an implementation of travel permit profile item. */
	public TravelPermitProfileItem(final Set<String> requiredAuthorizations,
			final String includePage,
			final String name, 
			final ProfileItemRegistry profileItemRegistry,
			final Boolean enabled,
			final TravelPermitProfileItemReportService
			travelPermitProfileItemReportService) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled); 
		this.travelPermitProfileItemReportService 
			= travelPermitProfileItemReportService;
	}

	/** {@inheritDoc} */
	@Override
	public void build(Map<String, Object> map, Offender offender, 
			UserAccount userAccount, Date date) {
		map.put(TRAVEL_PERMIT_COUNT_MODEL_KEY, 
				this.travelPermitProfileItemReportService
				.findTravePermitsCountByOffenderAndDate(offender, date));
	}
}