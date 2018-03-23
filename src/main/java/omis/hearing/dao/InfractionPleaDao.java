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

import java.util.List;
import omis.dao.GenericDao;
import omis.hearing.domain.InfractionPlea;

/**
 * Infraction Plea Data Access Object.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Feb 27, 2018)
 *@since OMIS 3.0
 *
 */
public interface InfractionPleaDao extends GenericDao<InfractionPlea> {
	
	
	/**
	 * Returns a list of all valid Infraction Pleas.
	 * @return List of all valid Infraction Pleas.
	 */
	List<InfractionPlea> findAll();
	
	/**
	 * Returns an Infraction Plea with the specified name.
	 * @param name - String
	 * @return Infraction Plea with the specified name.
	 */
	InfractionPlea find(String name);
	
	/**
	 * Returns an Infraction Plea with the specified name that is not
	 * the specified Infraction Plea.
	 * @param name - String
	 * @param infractionPleaExcluded - Infraction Plea to exclude
	 * @return Infraction Plea with the specified name that is not
	 * the specified Infraction Plea.
	 */
	InfractionPlea findExcluding(String name,
			InfractionPlea infractionPleaExcluded);
}
