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

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * Presentence investigation delay.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public interface PresentenceInvestigationDelay extends Creatable, Updatable {

	/**
	 * Returns the id of the presentence investigation delay.
	 * 
	 * @return id
	 */
	public Long getId();
	
	/**
	 * Sets the id of the presentence investigation delay.
	 * 
	 * @param id id
	 */
	public void setId(Long id);
	
	/**
	 * Returns the date of the presentence investigation delay.
	 * 
	 * @return date
	 */
	public Date getDate();
	
	/**
	 * Sets the date of the presentence investigation delay.
	 * 
	 * @param date date
	 */
	public void setDate(Date date);
	
	/**
	 * Returns the presentence investigation request of the presentence 
	 * investigation delay.
	 * 
	 * @return presentence investigation request
	 */
	public PresentenceInvestigationRequest getPresentenceInvestigationRequest();
	
	/**
	 * Sets the presentence investigation request of the presentence 
	 * investigation delay.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 */
	public void setPresentenceInvestigationRequest(
			PresentenceInvestigationRequest presentenceInvestigationRequest);
	
	/**
	 * Returns the presentence investigation delay category of the presentence 
	 * investigation delay.
	 * 
	 * @return presentence investigation delay category
	 */
	public PresentenceInvestigationDelayCategory getReason();
	
	/**
	 * Sets the presentence investigation delay category of the presentence 
	 * investigation delay.
	 * 
	 * @param reason presentence investigation delay category
	 */
	public void setReason(PresentenceInvestigationDelayCategory reason);
}