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
package omis.presentenceinvestigation.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;

/**
 * Presentence investigation delay data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public interface PresentenceInvestigationDelayDao 
	extends GenericDao<PresentenceInvestigationDelay>{

	/**
	 * Returns the presentence investigation delay with the specified 
	 * parameters.
	 * 
	 * @param date date
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @param reason presentence investigation delay category
	 * @return presentence investigation delay
	 */
	PresentenceInvestigationDelay find(Date date, 
			PresentenceInvestigationRequest presentenceInvestigationRequest, 
			PresentenceInvestigationDelayCategory reason);
	
	/**
	 * Returns the presentence investigation delay with the specified 
	 * parameters excluding the specified presentence investigation delay.
	 * 
	 * @param date date
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @param reason presentence investigation delay category
	 * @param excludedDelay excluded presentence investigation delay
	 * @return presentence investigation delay
	 */
	PresentenceInvestigationDelay findExcluding(Date date, 
			PresentenceInvestigationRequest presentenceInvestigationRequest, 
			PresentenceInvestigationDelayCategory reason,
			PresentenceInvestigationDelay excludedDelay);

	/**
	 * Returns a list of presentence investigation delays for the specified 
	 * presentence investigation request.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @return list of presentence investigation delays
	 */
	List<PresentenceInvestigationDelay> findByPresentenceInvestigationRequest(
			PresentenceInvestigationRequest presentenceInvestigationRequest);
}