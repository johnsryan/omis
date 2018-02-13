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
package omis.mentalhealthreview.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;

/**
 * Data access object for mental health note.
 *
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public interface MentalHealthNoteDao extends GenericDao<MentalHealthNote> {

	/**
	 * Returns the mental health note for the specified parameters.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param description description
	 * @return mental health note
	 */
	MentalHealthNote find(MentalHealthReview mentalHealthReview, Date date, 
			String description);

	/**
	 * Returns the mental health note for the specified parameters excluding the 
	 * specified mental health note.
	 * 
	 * @param mentalHealthReview mental health review
	 * @param date date
	 * @param description description
	 * @param excludedMentalHealthNote excluded mental health note
	 * @return mental health note
	 */
	MentalHealthNote findExcluding(MentalHealthReview mentalHealthReview, 
			Date date, String description, 
			MentalHealthNote excludedMentalHealthNote);

	/**
	 * Returns a list of mental health notes for the specified mental health 
	 * review.
	 * 
	 * @param mentalHealthReview mental health review
	 * @return list of mental health notes
	 */
	List<MentalHealthNote> findByMentalHealthReview(
			MentalHealthReview mentalHealthReview);
}
