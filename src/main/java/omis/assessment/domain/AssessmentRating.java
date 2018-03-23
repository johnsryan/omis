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

import java.io.Serializable;
import java.math.BigDecimal;

import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Assessment rating.
 * 
 * @author Josh Divine
 * @version 0.1.2 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public interface AssessmentRating extends Serializable {

	/**
	 * Sets the ID of the assessment rating.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the assessment rating.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the min.
	 * 
	 * @param min min
	 */
	void setMin(BigDecimal min);
	
	/**
	 * Returns the min.
	 * 
	 * @return min
	 */
	BigDecimal getMin();
	
	/**
	 * Sets the max.
	 * 
	 * @param max max
	 */
	void setMax(BigDecimal max);
	
	/**
	 * Returns the max.
	 * 
	 * @return max
	 */
	BigDecimal getMax();
	
	/**
	 * Sets the sex.
	 * 
	 * @param sex sex
	 */
	void setSex(Sex sex);
	
	/**
	 * Returns the sex.
	 * 
	 * @return sex
	 */
	Sex getSex();
	
	/**
	 * Sets the questionnaire type.
	 * 
	 * @param questionnaireType questionnaire type
	 */
	void setQuestionnaireType(QuestionnaireType questionnaireType);
	
	/**
	 * Returns the questionnaire type.
	 * 
	 * @return questionnaire type
	 */
	QuestionnaireType getQuestionnaireType();
	
	/**
	 * Sets whether the assessment rating is valid.
	 * 
	 * @param valid whether the assessment rating is valid
	 */
	void setValid(Boolean valid);
	
	/**
	 * Returns whether the assessment rating is valid.
	 * 
	 * @return whether the assessment rating is valid
	 */
	Boolean getValid();
	
	/**
	 * Sets the date range.
	 * 
	 * @param dateRange date range
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns the date range.
	 * 
	 * @return date range
	 */
	DateRange getDateRange();
	
	/**
	 * Sets the rating category.
	 * 
	 * @param category rating category
	 */
	void setCategory(RatingCategory category);
	
	/**
	 * Returns the rating category.
	 * 
	 * @return rating category
	 */
	RatingCategory getCategory();
	
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
	 * Sets the rating rank.
	 * 
	 * @param rank rating rank
	 */
	void setRank(RatingRank rank);
	
	/**
	 * Returns the rating rank.
	 * 
	 * @return rating rank
	 */
	RatingRank getRank();
}