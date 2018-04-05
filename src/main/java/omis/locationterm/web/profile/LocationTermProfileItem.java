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
package omis.locationterm.web.profile;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import omis.locationterm.report.LocationTermProfileItemReportService;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.web.profile.AbstractProfileItem;
import omis.web.profile.ProfileItem;
import omis.web.profile.ProfileItemRegistry;

/**
 * Location term profile item.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class LocationTermProfileItem
		extends AbstractProfileItem
		implements ProfileItem {
	
	private static final long serialVersionUID = 1L;

	/* Model keys. */
	
	private static final String LOCATION_TERM_EXISTS_MODEL_KEY
		= "locationTermExists";
	
	/* Report services. */
	
	private final LocationTermProfileItemReportService
	locationTermProfileItemReportService;
	
	/* Constructors. */
	
	/**
	 * Instantiates location term profile item.
	 * 
	 * @param requiredAuthorizations required auhorizations
	 * @param includePage include page
	 * @param name name
	 * @param profileItemRegistry profile item registry
	 * @param locationTermProfileItemReportService service
	 * @param enabled whether enabled
	 */
	public LocationTermProfileItem(
			final Set<String> requiredAuthorizations,
			final String includePage,
			final String name,
			final ProfileItemRegistry profileItemRegistry,
			final LocationTermProfileItemReportService
				locationTermProfileItemReportService,
			final Boolean enabled) {
		super(requiredAuthorizations, includePage, name, profileItemRegistry,
				enabled);
		this.locationTermProfileItemReportService
			= locationTermProfileItemReportService;
	}
	
	/* Method implementations. */

	/** {@inheritDoc} */
	@Override
	public void build(
			final Map<String, Object> modelMap,
			final Offender offender,
			final UserAccount userAccount,
			final Date date) {
		boolean locationTermExists
			 = this.locationTermProfileItemReportService
			 	.findLocationExistenceByOffenderAndDate(offender, date);
		modelMap.put(LOCATION_TERM_EXISTS_MODEL_KEY, locationTermExists);
	}
}