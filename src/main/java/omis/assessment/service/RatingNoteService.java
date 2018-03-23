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

import java.util.Date;
import java.util.List;

import omis.assessment.domain.RatingNote;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Rating note service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public interface RatingNoteService {

	/**
	 * Creates a new rating note.
	 * 
	 * @param date date
	 * @param description description
	 * @param administeredQuestionnaire administered questionnaire
	 * @return rating note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	RatingNote createRatingNote(Date date, String description, 
			AdministeredQuestionnaire administeredQuestionnaire) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing rating note.
	 * 
	 * @param ratingNote rating note
	 * @param date date
	 * @param description description
	 * @return rating note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	RatingNote updateRatingNote(RatingNote ratingNote, Date date, 
			String description) throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified rating note.
	 * 
	 * @param ratingNote rating note
	 */
	void removeRatingNote(RatingNote ratingNote);
	
	/**
	 * Return a list of rating notes for the specified administered 
	 * questionnaire.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of rating notes
	 */
	List<RatingNote> findRatingNotesByAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
}