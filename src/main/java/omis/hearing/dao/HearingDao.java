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
package omis.hearing.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.violationevent.domain.ViolationEvent;

/**
 * Data access object for hearing.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.3 (May 17, 2018)
 * @since OMIS 3.0
 */
public interface HearingDao extends GenericDao<Hearing> {

	/**
	 * Returns a hearing with specified properties
	 * @param location - Location
	 * @param offender - Offender
	 * @param date - Date
	 * @param officer user account
	 * @param category - HearingCategory
	 * @return hearing with specified properties
	 */
	Hearing find(Location location, Offender offender, Date date,
			UserAccount officer, HearingCategory category);
	
	/**
	 * Returns a hearing with specified properties excluding specified hearing
	 * @param location - Location
	 * @param offender - Offender
	 * @param date - Date
	 * @param officer user account 
	 * @param category - HearingCategory
	 * @param hearing - Hearing to exclude
	 * @return hearing with specified properties excluding specified hearing
	 */
	Hearing findExcluding(Location location, Offender offender, Date date,
			UserAccount officer, HearingCategory category, Hearing hearing);
	
	/**
	 * Returns a list of Hearings with specified offender
	 * @param offender
	 * @return List of Hearings with specified offender
	 */
	List<Hearing> findByOffender(Offender offender);

	/**
	 * Returns a list of hearings for the specified violation event.
	 * 
	 * @param violationEvent violation event
	 * @return list of hearings
	 */
	List<Hearing> findByViolationEvent(ViolationEvent violationEvent);
}