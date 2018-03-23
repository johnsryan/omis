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

import omis.assessment.dao.AssessmentCategoryScoreDao;
import omis.assessment.domain.AssessmentCategoryScore;
import omis.assessment.domain.RatingCategory;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment category score delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryScoreDelegate {

	/* Data access objects. */
	
	private final AssessmentCategoryScoreDao assessmentCategoryScoreDao;

	/* Instance factories. */
	
	private final InstanceFactory<AssessmentCategoryScore> 
			assessmentCategoryScoreInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** 
	 * Instantiates an implementation of assessment category score delegate 
	 * with the specified date access object and instance factory.
	 * 
	 * @param assessmentCategoryScoreDao assessment category score data 
	 * access object
	 * @param assessmentCategoryScoreInstanceFactory assessment category 
	 * score instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public AssessmentCategoryScoreDelegate(
			final AssessmentCategoryScoreDao assessmentCategoryScoreDao,
			final InstanceFactory<AssessmentCategoryScore> 
					assessmentCategoryScoreInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.assessmentCategoryScoreDao = assessmentCategoryScoreDao;
		this.assessmentCategoryScoreInstanceFactory = 
				assessmentCategoryScoreInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new assessment category score.
	 * 
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @param score score
	 * @return assessment category score
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryScore create(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory, final BigDecimal score)  
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryScoreDao.find(administeredQuestionnaire, 
				ratingCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category score already exists.");
		}
		AssessmentCategoryScore assessmentCategoryScore = this
				.assessmentCategoryScoreInstanceFactory.createInstance();
		populateAssessmentCategoryScore(assessmentCategoryScore, 
				administeredQuestionnaire, ratingCategory, score);
		assessmentCategoryScore.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		return this.assessmentCategoryScoreDao.makePersistent(
				assessmentCategoryScore);
	}
	
	/**
	 * Updates an existing assessment category score.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 * @param administeredQuestionnaire administered questionnaire
	 * @param ratingCategory rating category
	 * @param score score
	 * @return assessment category score
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryScore update(
			final AssessmentCategoryScore assessmentCategoryScore,
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory, final BigDecimal score)  
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryScoreDao.findExcluding(
				administeredQuestionnaire, ratingCategory, 
				assessmentCategoryScore) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category score already exists.");
		}
		populateAssessmentCategoryScore(assessmentCategoryScore, 
				administeredQuestionnaire, ratingCategory, score);
		return this.assessmentCategoryScoreDao.makePersistent(
				assessmentCategoryScore);
	}

	/**
	 * Removes the specified assessment category score.
	 * 
	 * @param assessmentCategoryScore assessment category score
	 */
	public void remove(
			final AssessmentCategoryScore assessmentCategoryScore) {
		this.assessmentCategoryScoreDao.makeTransient(
				assessmentCategoryScore);
	}

	/**
	 * Returns the assessment category score for the specified rating category 
	 * and administered questionnaire.
	 * 
	 * @param ratingCategory rating category
	 * @param administeredQuestionnaire administered questionnaire
	 * @return assessment category score
	 */
	public AssessmentCategoryScore 
			findByRatingCategoryAndAdministeredQuestionnaire(
					final RatingCategory ratingCategory,
					final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.assessmentCategoryScoreDao.find(administeredQuestionnaire, 
				ratingCategory);
	}
	
	// Populates an assessment category score
	private void populateAssessmentCategoryScore(
			final AssessmentCategoryScore assessmentCategoryScore,
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory, final BigDecimal score) {
		assessmentCategoryScore.setAdministeredQuestionnaire(
				administeredQuestionnaire);
		assessmentCategoryScore.setRatingCategory(ratingCategory);
		assessmentCategoryScore.setScore(score);
		assessmentCategoryScore.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}