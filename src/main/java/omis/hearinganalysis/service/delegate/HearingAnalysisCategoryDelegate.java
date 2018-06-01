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
package omis.hearinganalysis.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.dao.HearingAnalysisCategoryDao;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.instance.factory.InstanceFactory;

/**
 * Hearing analysis category delegate.
 *
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (May 29, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisCategoryDelegate {

	/* Data access objects. */
	
	private final HearingAnalysisCategoryDao hearingAnalysisCategoryDao;

	/* Instance factories. */
	
	private final InstanceFactory<HearingAnalysisCategory> 
			hearingAnalysisCategoryInstanceFactory;
	
	/**
	 * Instantiates an implementation of hearing analysis category delegate with 
	 * the specified data access object and instance factory.
	 * 
	 * @param hearingAnalysisCategoryDao hearing analysis category data access 
	 * object
	 * @param hearingAnalysisCategoryInstanceFactory hearing analysis category 
	 * instance factory
	 */
	public HearingAnalysisCategoryDelegate(
			final HearingAnalysisCategoryDao hearingAnalysisCategoryDao, 
			final InstanceFactory<HearingAnalysisCategory> 
					hearingAnalysisCategoryInstanceFactory) {
		this.hearingAnalysisCategoryDao = hearingAnalysisCategoryDao;
		this.hearingAnalysisCategoryInstanceFactory = 
				hearingAnalysisCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new hearing analysis category.
	 * 
	 * @param name name
	 * @param valid valid
	 * @return hearing analysis category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public HearingAnalysisCategory create(final String name, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.hearingAnalysisCategoryDao.find(name) != null) {
			throw new DuplicateEntityFoundException(
					"Hearing analysis category already exists");
		}
		HearingAnalysisCategory hearingAnalysisCategory = 
				this.hearingAnalysisCategoryInstanceFactory.createInstance();
		populateHearingAnalysisCategory(hearingAnalysisCategory, name, valid);
		return this.hearingAnalysisCategoryDao.makePersistent(
				hearingAnalysisCategory);
	}
	
	/**
	 * Updates an existing hearing analysis category.
	 * 
	 * @param hearingAnalysisCategory hearing analysis category
	 * @param name name
	 * @param valid valid
	 * @return hearing analysis category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public HearingAnalysisCategory update(
			final HearingAnalysisCategory hearingAnalysisCategory, 
			final String name, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.hearingAnalysisCategoryDao.findExcluding(name, 
				hearingAnalysisCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Hearing analysis category already exists");
		}
		populateHearingAnalysisCategory(hearingAnalysisCategory, name, valid);
		return this.hearingAnalysisCategoryDao.makePersistent(
				hearingAnalysisCategory);
	}

	/**
	 * Removes an existing hearing analysis category.
	 * 
	 * @param hearingAnalysisCategory hearing analysis category
	 */
	public void remove(HearingAnalysisCategory hearingAnalysisCategory) {
		this.hearingAnalysisCategoryDao.makeTransient(hearingAnalysisCategory);
	}
	
	/**
	 * Returns a list of all hearing analysis categories.
	 * 
	 * @return list of all hearing analysis categories
	 */
	public List<HearingAnalysisCategory> findAll() {
		return this.hearingAnalysisCategoryDao
				.findAllHearingAnalysisCategories();
	}

	// Populates a hearing analysis category
	private void populateHearingAnalysisCategory(
			final HearingAnalysisCategory hearingAnalysisCategory,
			final String name, final Boolean valid) {
		hearingAnalysisCategory.setName(name);
		hearingAnalysisCategory.setValid(valid);
	}
}
