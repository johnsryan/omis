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

import java.util.List;

import omis.assessment.dao.RatingScaleGroupDao;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingScaleGroup;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Rating scale group delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 4, 2018)
 * @since OMIS 3.0
 */
public class RatingScaleGroupDelegate {

	/* Data access objects. */
	
	private final RatingScaleGroupDao ratingScaleGroupDao;

	/* Instance factories. */
	
	private final InstanceFactory<RatingScaleGroup> 
			ratingScaleGroupInstanceFactory;
	
	/**
	 * Instantiates an implementation of rating scale group delegate with the 
	 * specified date access object and instance factory.
	 * 
	 * @param ratingScaleGroupDao rating scale group data access object
	 * @param ratingScaleGroupInstanceFactory rating scale group instance 
	 * factory
	 */
	public RatingScaleGroupDelegate(
			final RatingScaleGroupDao ratingScaleGroupDao,
			final InstanceFactory<RatingScaleGroup> 
					ratingScaleGroupInstanceFactory) {
		this.ratingScaleGroupDao = ratingScaleGroupDao;
		this.ratingScaleGroupInstanceFactory = ratingScaleGroupInstanceFactory;
	}
	
	/**
	 * Creates a new rating scale group.
	 * 
	 * @param scaleName scale name
	 * @return rating scale group
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingScaleGroup create(final String scaleName) 
			throws DuplicateEntityFoundException {
		if (this.ratingScaleGroupDao.find(scaleName) != null) {
			throw new DuplicateEntityFoundException(
					"Rating scale group already exists.");
		}
		RatingScaleGroup ratingScaleGroup = this.ratingScaleGroupInstanceFactory
				.createInstance();
		ratingScaleGroup.setScaleName(scaleName);
		return this.ratingScaleGroupDao.makePersistent(ratingScaleGroup);
	}
	
	/**
	 * Creates a new rating scale group.
	 * 
	 * @param scaleName scale name
	 * @return rating scale group
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public RatingScaleGroup update(final RatingScaleGroup ratingScaleGroup,
			final String scaleName) throws DuplicateEntityFoundException {
		if (this.ratingScaleGroupDao.findExcluding(scaleName, ratingScaleGroup) 
				!= null) {
			throw new DuplicateEntityFoundException(
					"Rating scale group already exists.");
		}
		ratingScaleGroup.setScaleName(scaleName);
		return this.ratingScaleGroupDao.makePersistent(ratingScaleGroup);
	}
	
	/**
	 * Removes the specified rating scale group.
	 * 
	 * @param ratingScaleGroup rating scale group
	 */
	public void remove(final RatingScaleGroup ratingScaleGroup) {
		this.ratingScaleGroupDao.makeTransient(ratingScaleGroup);
	}

	/**
	 * Returns a list of rating scale groups for the specified administered 
	 * questionnaire and rating category.
	 * 
	 * @param administeredQuestionnaire administered questionnaire 
	 * @param ratingCategory rating category
	 * @return list of rating scale groups
	 */
	public List<RatingScaleGroup> findByAdministeredQuestionnaireAndRatingCategory(
			final AdministeredQuestionnaire administeredQuestionnaire, 
			final RatingCategory ratingCategory) {
		return this.ratingScaleGroupDao
				.findByAdministeredQuestionnaireAndRatingCategory(
						administeredQuestionnaire, ratingCategory);
	}
}