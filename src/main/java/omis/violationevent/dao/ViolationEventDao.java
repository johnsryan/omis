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
package omis.violationevent.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.facility.domain.Unit;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;

/**
 * Violation event data access object.
 * 
 * @author Annie Wahl 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public interface ViolationEventDao extends GenericDao<ViolationEvent> {
	
	
	/**
	 * Finds and returns a ViolationEvent with specified properties
	 * @param offender - offender
	 * @param jurisdiction - Organization
	 * @param unit unit
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return ViolationEvent with specified properties
	 */
	ViolationEvent find(Offender offender, Organization jurisdiction, Unit unit,
			Date date, String details, ViolationEventCategory category);
	
	/**
	 * Finds and returns a ViolationEvent with specified properties excluding
	 * specified ViolationEvent
	 * @param excludedViolationEvent - ViolationEvent to exclude
	 * @param offender - Offender
	 * @param jurisdiction - Organization
	 * @param unit unit
	 * @param date - Date
	 * @param details - String
	 * @param category - ViolationEventCategory
	 * @return ViolationEvent with specified properties excluding
	 * specified ViolationEvent
	 */
	ViolationEvent findExcluding(ViolationEvent excludedViolationEvent,
			Offender offender, Organization jurisdiction, Unit unit,
			Date date, String details, ViolationEventCategory category);
	
	/**
	 * Finds and returns a list of ViolationEvents by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents by specified offender
	 */
	List<ViolationEvent> findByOffender(Offender offender);
	
	/**
	 * Finds and returns a list of ViolationEvents with no Infraction/resolution
	 * association by specified offender
	 * @param offender - Offender 
	 * @return List of ViolationEvents with no Infraction/resolution
	 * association by specified offender
	 */
	List<ViolationEvent> findUnresolvedByOffender(Offender offender);
}
