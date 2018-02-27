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
package omis.person.service.delegate;

import java.util.List;

import omis.instance.factory.InstanceFactory;
import omis.person.dao.AlternativeIdentityCategoryDao;
import omis.person.domain.AlternativeIdentityCategory;

/**
 * AlternativeIdentityCategoryDelegate.java.
 * 
 *@author Annie Jacques 
 *@author Sheronda Vaughn
 *@version 0.1.0 (Nov 9, 2016)
 *@since OMIS 3.0
 *
 */
public class AlternativeIdentityCategoryDelegate {
	
	private final AlternativeIdentityCategoryDao alternativeIdentityCategoryDao;
	private final InstanceFactory<AlternativeIdentityCategory> 
		alternativeIdentityCategoryInstanceFactory;
	/**
	 * Constructor for AlternativeIdentityCategoryDelegate.
	 * @param alternativeIdentityCategoryDao alternative identity category data
	 * access object
	 * @param alternativeIdentityCategoryInstanceFactory alternative identity 
	 * category instance factory
	 */
	public AlternativeIdentityCategoryDelegate(
			final AlternativeIdentityCategoryDao 
			alternativeIdentityCategoryDao, 
			final InstanceFactory<AlternativeIdentityCategory> 
				alternativeIdentityCategoryInstanceFactory) {
		this.alternativeIdentityCategoryDao = alternativeIdentityCategoryDao;
		this.alternativeIdentityCategoryInstanceFactory 
			= alternativeIdentityCategoryInstanceFactory;
	}
	
	/**
	 * Returns a list of all AlternativeIdentityCategories.
	 * @return list of all AlternativeIdentityCategories
	 */
	public List<AlternativeIdentityCategory> findAll() {
		return this.alternativeIdentityCategoryDao.findAll();
	}
	
	/**
	 * Returns a new alternative identity category.
	 *
	 *
	 * @param description description
	 * @param name name
	 * @param sortOrder sort order
	 * @param valid valid
	 * @return new category
	 */
	public AlternativeIdentityCategory create(final String description,
			final String name, final Short sortOrder, final Boolean valid) {
		AlternativeIdentityCategory category 
			= this.alternativeIdentityCategoryInstanceFactory.createInstance();
		
		category.setDescription(description);
		category.setName(name);
		category.setValid(valid);
		category.setSortOrder(sortOrder);
		
		return this.alternativeIdentityCategoryDao.makePersistent(category);
	}
}
