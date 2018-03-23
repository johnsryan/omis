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
package omis.assessment.dao;

import java.util.Date;
import java.util.List;

import omis.assessment.domain.RatingNote;
import omis.dao.GenericDao;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Data access object for rating note.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public interface RatingNoteDao extends GenericDao<RatingNote> {

	/**
	 * Returns the rating note matching the specified date, description and 
	 * administered questionnaire.
	 * 
	 * @param date date
	 * @param description description
	 * @param administeredQuestionnaire administered questionnaire
	 * @return rating note
	 */
	RatingNote find(Date date, String description, 
			AdministeredQuestionnaire administeredQuestionnaire);

	/**
	 * Returns the rating note matching the specified date, description and 
	 * administered questionnaire excluding the specified rating note.
	 * 
	 * @param date date
	 * @param description description
	 * @param administeredQuestionnaire administered questionnaire
	 * @param excludedRatingNote rating note
	 * @return rating note
	 */
	RatingNote findExcluding(Date date, String description, 
			AdministeredQuestionnaire administeredQuestionnaire,
			RatingNote excludedRatingNote);

	/**
	 * Returns a list of rating notes for the specified administered 
	 * questionnaire.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @return list of rating notes
	 */
	List<RatingNote> findByAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
}