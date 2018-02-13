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
package omis.parolereview.service.delegate;

import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.parolereview.dao.ParoleEndorsementCategoryDao;
import omis.parolereview.domain.ParoleEndorsementCategory;

/**
 * Parole endorsement category delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class ParoleEndorsementCategoryDelegate {

	/* Data access objects. */
	
	private final ParoleEndorsementCategoryDao paroleEndorsementCategoryDao;

	/* Instance factories. */
	
	private final InstanceFactory<ParoleEndorsementCategory> 
			paroleEndorsementCategoryInstanceFactory;
	
	/** Instantiates an implementation of parole endorsement category delegate 
	 * with the specified data access object and instance factory.
	 * 
	 * @param paroleEndorsementCategoryDao parole endorsement category data 
	 * access object
	 * @param paroleEndorsementCategoryInstanceFactory parole endorsement 
	 * category instance factory
	 */
	public ParoleEndorsementCategoryDelegate(
			final ParoleEndorsementCategoryDao paroleEndorsementCategoryDao,
			final InstanceFactory<ParoleEndorsementCategory> 
					paroleEndorsementCategoryInstanceFactory) {
		this.paroleEndorsementCategoryDao = paroleEndorsementCategoryDao;
		this.paroleEndorsementCategoryInstanceFactory = 
				paroleEndorsementCategoryInstanceFactory;
	}
	
	/**
	 * Creates a new parole endorsement category.
	 * 
	 * @param name name
	 * @param valid valid
	 * @return parole endorsement category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleEndorsementCategory create(final String name, 
			final Boolean valid) throws DuplicateEntityFoundException {
		if (this.paroleEndorsementCategoryDao.find(name) != null) {
			throw new DuplicateEntityFoundException(
					"Parole endorsement category already exists.");
		}
		ParoleEndorsementCategory paroleEndorsementCategory = this
				.paroleEndorsementCategoryInstanceFactory.createInstance();
		paroleEndorsementCategory.setName(name);
		paroleEndorsementCategory.setValid(valid);
		return this.paroleEndorsementCategoryDao.makePersistent(
				paroleEndorsementCategory);
	}
	
	/**
	 * Creates an existing parole endorsement category.
	 * 
	 * @param paroleEndorsementCategory parole endorsement category
	 * @param name name
	 * @param valid valid
	 * @return parole endorsement category
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public ParoleEndorsementCategory udpate(
			final ParoleEndorsementCategory paroleEndorsementCategory, 
			final String name, final Boolean valid) 
					throws DuplicateEntityFoundException {
		if (this.paroleEndorsementCategoryDao.findExcluding(name, 
				paroleEndorsementCategory) != null) {
			throw new DuplicateEntityFoundException(
					"Parole endorsement category already exists.");
		}
		paroleEndorsementCategory.setName(name);
		paroleEndorsementCategory.setValid(valid);
		return this.paroleEndorsementCategoryDao.makePersistent(
				paroleEndorsementCategory);
	}
	
	/**
	 * Removes the specified parole endorsement category.
	 * 
	 * @param paroleEndorsementCategory parole endorsement category
	 */
	public void remove(
			final ParoleEndorsementCategory paroleEndorsementCategory) {
		this.paroleEndorsementCategoryDao.makeTransient(
				paroleEndorsementCategory);
	}
	
	/**
	 * Returns a list of parole endorsement categories.
	 * 
	 * @return list of parole endorsement categories
	 */
	public List<ParoleEndorsementCategory> findAll() {
		return this.paroleEndorsementCategoryDao.findAll();
	}
}
