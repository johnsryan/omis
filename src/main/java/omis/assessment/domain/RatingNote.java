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
package omis.assessment.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Rating note.
 *
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public interface RatingNote extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the rating note.
	 * 
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the ID of the rating note.
	 * 
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns the description of the rating note.
	 * 
	 * @return description
	 */
	String getDescription();
	
	/**
	 * Sets the description of the rating note.
	 * 
	 * @param description description
	 */
	void setDescription(String description);
	
	/**
	 * Returns the date of the rating note.
	 * 
	 * @return date
	 */
	Date getDate();
	
	/**
	 * Sets the date of the rating note.
	 * 
	 * @param date date
	 */
	void setDate(Date date);
	
	/**
	 * Returns the administered questionnaire of the rating note.
	 * 
	 * @return administered questionnaire
	 */
	AdministeredQuestionnaire getAdministeredQuestionnaire();
	
	/**
	 * Sets the administered questionnaire of the rating note.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 */
	void setAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
}