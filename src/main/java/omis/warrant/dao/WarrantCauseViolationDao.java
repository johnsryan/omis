/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.warrant.dao;

import java.util.List;

import omis.condition.domain.ConditionClause;
import omis.dao.GenericDao;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantCauseViolation;

/**
 * Warrant cause violation data access object.
 * 
 *@author Annie Jacques
 *@author Joel Norris
 *@version 0.1.1 (January 24, 2018)
 *@since OMIS 3.0
 *
 */
public interface WarrantCauseViolationDao
	extends GenericDao<WarrantCauseViolation> {
	
	/**
	 * Returns a warrant cause violation for the specified warrant, and condition clause.
	 * 
	 * @param warrant warrant
	 * @param conditionClause condition clause
	 * @return warrant cause violation
	 */
	public WarrantCauseViolation find(Warrant warrant, ConditionClause conditionClause);
	
	/**
	 * Returns a warrant cause violation for the specified warrant, and condition clause,
	 * excluding the specified warrant cause violation.
	 * 
	 * @param warrant warrant
	 * @param conditionClause condition clause
	 * @param warrantCauseViolationExcluded warrant cause violation excluded
	 * from search
	 * @return warrant cause violation
	 */
	public WarrantCauseViolation findExcluding(
			Warrant warrant, ConditionClause condition,
			WarrantCauseViolation warrantCauseViolationExcluded);
	
	/**
	 * Returns warrant cause violations for the specified warrant.
	 * 
	 * @param warrant warrant
	 * @return List of warrant cause violations with specified warrant
	 */
	public List<WarrantCauseViolation> findByWarrant(Warrant warrant);
	
}
