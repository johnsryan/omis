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
package omis.assessment.service;

import java.util.List;

/**
 * Assessment rating service delegate registry.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentRatingServiceDelegateRegistry {

	/**
	 * Registers an assessment rating service delegate.
	 * 
	 * @param item assessment rating service delegate
	 */
	void register(AssessmentRatingServiceDelegate item);
	
	/**
	 * Returns the list of assessment rating service delegates.
	 * 
	 * @return list of assessment rating service delegates
	 */
	List<AssessmentRatingServiceDelegate> getItems();
}