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
package omis.presentenceinvestigation.domain;

import java.io.Serializable;

/**
 * Presentence investigation delay category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public interface PresentenceInvestigationDelayCategory extends Serializable {

	/**
	 * Returns the id of the presentence investigation delay category.
	 * 
	 * @return id
	 */
	public Long getId();
	
	/**
	 * Sets the id of the presentence investigation delay category.
	 * 
	 * @param id id
	 */
	public void setId(Long id);
	
	/**
	 * Returns the name for the presentence investigation delay category.
	 * 
	 * @return name
	 */
	public String getName();
	
	/**
	 * Sets the name for the presentence investigation delay category.
	 * 
	 * @param name name
	 */
	public void setName(String name);
	
	/**
	 * Returns whether the presentence investigation delay category is valid.
	 * 
	 * @return valid
	 */
	public Boolean getValid();
	
	/**
	 * Sets whether the presentence investigation delay category is valid.
	 * 
	 * @param valid valid
	 */
	public void setValid(Boolean valid);
}