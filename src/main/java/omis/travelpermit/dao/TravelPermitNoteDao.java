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
package omis.travelpermit.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;

/**
 * Data access object for travel permit note.
 * 
 * @author Yidong Li
 * @version 0.1.1 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public interface TravelPermitNoteDao
		extends GenericDao<TravelPermitNote> {
	/**
	 * Returns the existing travel permit note if it matches the criteria.
	 * 
	 * @param premit travel permit
	 * @param date date
	 * @param value value
	 * @return existing travel permit
	 */
	TravelPermitNote findExistingTravelPermitNote(TravelPermit permit, Date date,
		String value); 
	
	/**
	 * Returns the excluded existing travel permit note if it matches the criteria.
	 * 
	 * @param note travel permit note
	 * @param date date
	 * @param value value
	 * @return existing travel permit note
	 */
	TravelPermitNote findExcludedExistingTravelPermitNote(TravelPermitNote note,
		Date date, String value);
	
	/**
	 * Returns existing travel permit notes by permits
	 * 
	 * @param permit travel permit
	 * @return existing travel permit notes
	 */
	List<TravelPermitNote> findNotes(TravelPermit permit);
}