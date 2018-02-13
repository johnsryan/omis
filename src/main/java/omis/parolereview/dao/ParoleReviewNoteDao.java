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
package omis.parolereview.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewNote;

/**
 * Data access object for parole review note.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public interface ParoleReviewNoteDao extends GenericDao<ParoleReviewNote> {

	/**
	 * Returns the specified parole review note for the specified date, 
	 * description and parole review.
	 * 
	 * @param paroleReview parole review
	 * @param date date
	 * @param description description
	 * @return parole review note
	 */
	ParoleReviewNote find(ParoleReview paroleReview, Date date, 
			String description);
	
	/**
	 * Returns the specified parole review note for the specified date, 
	 * description and parole review.excluding the specified parole review note.
	 * 
	 * @param paroleReview parole review
	 * @param date date
	 * @param description description
	 * @param excludedParoleReviewNote excluded parole review note
	 * @return parole review note
	 */
	ParoleReviewNote findExcluding(ParoleReview paroleReview, Date date, 
			String description, ParoleReviewNote excludedParoleReviewNote);
	
	/**
	 * Returns a list of parole review notes for the specified parole review.
	 * 
	 * @param paroleReview parole review
	 * @return list of parole review notes
	 */
	List<ParoleReviewNote> findByParoleReview(ParoleReview paroleReview);
}