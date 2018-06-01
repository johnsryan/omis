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
package omis.facility.dao;

import java.util.List;

import omis.dao.GenericDao;
import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Unit data access object.
 * 
 * @author Joel Norris 
 * @author Josh Divine
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public interface UnitDao extends GenericDao<Unit> {

	/**
	 * Returns a list of all units for the specified facility.
	 * 
	 * @param facility facility
	 * @return list of units
	 */
	List<Unit> findByFacility(Facility facility);

	/**
	 * Returns a list of all units for the specified complex.
	 * 
	 * @param complex complex
	 * @return list of units
	 */
	List<Unit> findByComplex(Complex complex);

	/**
	 * Returns the unit with the specified facility and complex.
	 * 
	 * @param facility facility
	 * @param complex complex
	 * @return list of units
	 */
	List<Unit> findUnits(Facility facility, Complex complex);

	/**
	 * Returns the unit with the specified facility and name.
	 * 
	 * @param name name
	 * @param facility facility
	 * @return matching unit
	 */
	Unit find(String name, Facility facility);
	
	/**
	 * Returns the unit with the specified name and facility, excluding the
	 * specified unit.
	 * 
	 * @param name name
	 * @param facility facility
	 * @param unit unit to exclude
	 * @return matching unit
	 */
	Unit findExcluding(String name, Facility facility, Unit unit);

	/**
	 * Returns a list of units for the specified supervisory organization.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @return list of units
	 */
	List<Unit> findBySupervisoryOrganization(
			SupervisoryOrganization supervisoryOrganization);
}