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
package omis.locationterm.dao;

import omis.dao.GenericDao;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeActionAssociation;

/**
 * Data access object for association of location term to change action.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2018)
 * @since OMIS 3.0
 */
public interface LocationTermChangeActionAssociationDao
		extends GenericDao<LocationTermChangeActionAssociation> {

	/**
	 * Returns association of location term to change action.
	 * 
	 * @param locationTerm location term
	 * @return association of location term to change action
	 */
	LocationTermChangeActionAssociation find(
			LocationTerm locationTerm);
	
	/**
	 * Returns association of location term to change action with associations
	 * excluded.
	 * 
	 * @param locationTerm location term
	 * @param excludedAssociations excluded associations
	 * @return association of location term to change action
	 */
	LocationTermChangeActionAssociation findExcluding(
			LocationTerm locationTerm,
			LocationTermChangeActionAssociation... excludedAssociations);

	/**
	 * Removes association with change action by location term.
	 * 
	 * <p>Return value is either {@code 0} or {@code 1} as one association
	 * can exist per location term.
	 * 
	 * @param locationTerm location term by which to remove change action
	 * association
	 * @return count of associations removed
	 */
	int removeByLocationTerm(LocationTerm locationTerm);
}