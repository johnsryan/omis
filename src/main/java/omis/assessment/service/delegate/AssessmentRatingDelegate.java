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
package omis.assessment.service.delegate;

import java.math.BigDecimal;
import java.util.List;

import omis.assessment.dao.AssessmentRatingDao;
import omis.assessment.domain.AssessmentRating;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingRank;
import omis.datatype.DateRange;
import omis.demographics.domain.Sex;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Assessment rating delegate.
 * 
 * @author Josh Divine
 * @version 0.1.2 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class AssessmentRatingDelegate {

	/* Data access objects. */
	
	private final AssessmentRatingDao assessmentRatingDao;

	/* Instance factories. */
	
	private final InstanceFactory<AssessmentRating> 
			assessmentRatingInstanceFactory;
	
	/**
	 * Instantiates an implementation of assessment rating delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param assessmentRatingDao assessment rating data access object
	 * @param assessmentRatingInstanceFactory assessment rating instance factory
	 */
	public AssessmentRatingDelegate(
			final AssessmentRatingDao assessmentRatingDao,
			final InstanceFactory<AssessmentRating> 
					assessmentRatingInstanceFactory) {
		this.assessmentRatingDao = assessmentRatingDao;
		this.assessmentRatingInstanceFactory = assessmentRatingInstanceFactory;
	}
	
	/**
	 * Creates a new assessment rating.
	 * 
	 * @param questionnaireType questionnaire type
	 * @param sex sex
	 * @param dateRange date range
	 * @param min min
	 * @param max max
	 * @param valid valid
	 * @param category rating category
	 * @param description description
	 * @param rank rating rank
	 * @return assessment rating
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentRating create(final QuestionnaireType questionnaireType, 
			final Sex sex, final DateRange dateRange, final BigDecimal min, 
			final BigDecimal max, final Boolean valid, 
			final RatingCategory category, final String description,
			final RatingRank rank) 
					throws DuplicateEntityFoundException {
		if (this.assessmentRatingDao.find(questionnaireType, sex, min, max, 
				DateRange.getStartDate(dateRange), 
				DateRange.getEndDate(dateRange), category, rank) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment rating already exists.");
		}
		AssessmentRating assessmentRating = this.assessmentRatingInstanceFactory
				.createInstance();
		populateAssessmentRating(assessmentRating, questionnaireType, sex, 
				dateRange, min, max, valid, category, description, rank);
		return this.assessmentRatingDao.makePersistent(assessmentRating);
	}
	
	/**
	 * Updates an existing assessment rating.
	 * 
	 * @param assessmentRating assessment rating
	 * @param questionnaireType questionnaire type
	 * @param sex sex
	 * @param dateRange date range
	 * @param min min
	 * @param max max
	 * @param valid valid
	 * @param category rating category
	 * @param description description
	 * @param rank rating rank
	 * @return assessment rating
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentRating update(final AssessmentRating assessmentRating,
			final QuestionnaireType questionnaireType, final Sex sex, 
			final DateRange dateRange, final BigDecimal min, 
			final BigDecimal max, final Boolean valid, 
			final RatingCategory category, final String description,
			final RatingRank rank) 
					throws DuplicateEntityFoundException {
		if (this.assessmentRatingDao.findExcluding(questionnaireType, sex, min, 
				max, DateRange.getStartDate(dateRange), 
				DateRange.getEndDate(dateRange), category, rank, 
				assessmentRating) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment rating already exists.");
		}
		populateAssessmentRating(assessmentRating, questionnaireType, sex, 
				dateRange, min, max, valid, category, description, rank);
		return this.assessmentRatingDao.makePersistent(assessmentRating);
	}
	
	/**
	 * Removes the specified assessment rating.
	 * 
	 * @param assessmentRating assessment rating
	 */
	public void remove(final AssessmentRating assessmentRating) {
		this.assessmentRatingDao.makeTransient(assessmentRating);
	}

	/**
	 * Returns a list of assessment ratings for the specified rating category.
	 * 
	 * @param ratingCategory rating category
	 * @return list of assessment ratings
	 */
	public List<AssessmentRating> findByRatingCategory(
			final RatingCategory ratingCategory) {
		return this.assessmentRatingDao.findByRatingCategory(ratingCategory);
	}
	
	// Populates an assessment rating
	private void populateAssessmentRating(
			final AssessmentRating assessmentRating,
			final QuestionnaireType questionnaireType, final Sex sex, 
			final DateRange dateRange, final BigDecimal min,
			final BigDecimal max, final Boolean valid, 
			final RatingCategory category, final String description,
			final RatingRank rank) {
		assessmentRating.setDateRange(dateRange);
		assessmentRating.setMax(max);
		assessmentRating.setMin(min);
		assessmentRating.setQuestionnaireType(questionnaireType);
		assessmentRating.setSex(sex);
		assessmentRating.setValid(valid);
		assessmentRating.setCategory(category);
		assessmentRating.setDescription(description);
		assessmentRating.setRank(rank);
	}
}