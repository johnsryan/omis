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
package omis.boardconsideration.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.hearinganalysis.domain.HearingAnalysis;

/**
 * Board consideration.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public interface BoardConsideration extends Creatable, Updatable {
	
	/**
	 * Sets the ID of the board consideration.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the board consideration.
	 *
	 * @return ID
	 */
	Long getId();

	/**
	 * Sets the hearing analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 */
	void setHearingAnalysis(HearingAnalysis hearingAnalysis);
	
	/**
	 * Returns the hearing analysis.
	 * 
	 * @return hearing analysis
	 */
	HearingAnalysis getHearingAnalysis();
	
	/**
	 * Sets the title.
	 * 
	 * @param title title
	 */
	void setTitle(String title);
	
	/**
	 * Returns the title.
	 * 
	 * @return title
	 */
	String getTitle();
		
	/**
	 * Sets the description.
	 * 
	 * @param description description
	 */
	void setDescription(String description);
	
	/**
	 * Returns the description.
	 * 
	 * @return description
	 */
	String getDescription();
	
	/**
	 * Sets whether the board consideration was accepted.
	 * 
	 * @param accepted accepted
	 */
	void setAccepted(Boolean accepted);
	
	/**
	 * Returns whether the board consideration was accepted.
	 * 
	 * @return accepted
	 */
	Boolean getAccepted();
	
	/**
	 * Sets the board consideration category.
	 * 
	 * @param category board consideration category
	 */
	void setCategory(BoardConsiderationCategory category);
	
	/**
	 * Returns the board consideration category.
	 * 
	 * @return board consideration category
	 */
	BoardConsiderationCategory getCategory();
}