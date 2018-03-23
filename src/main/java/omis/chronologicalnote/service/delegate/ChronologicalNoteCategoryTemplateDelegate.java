/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnote.service.delegate;

import omis.chronologicalnote.dao.ChronologicalNoteCategoryTemplateDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryTemplate;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryTemplateExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Chronological note category template delegate.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryTemplateDelegate {

	/* Data access objects. */
	private ChronologicalNoteCategoryTemplateDao
	chronologicalNoteCategoryTemplateDao;
	
	/*Instance factories. */
	private InstanceFactory<ChronologicalNoteCategoryTemplate>
	chronologicalNoteCategoryTemplateInstanceFactory;
	
	/**
	 * Instantiates a chronological note category template delegate with the
	 * specified data access object and instance factory.
	 * 
	 * @param chronologicalNoteCategoryTemplateDao chronological note category
	 * template data access object
	 * @param chronologicalNoteCategoryTemplateInstanceFactory chronological
	 * note category template instance factory
	 */
	public ChronologicalNoteCategoryTemplateDelegate(
			final ChronologicalNoteCategoryTemplateDao
			chronologicalNoteCategoryTemplateDao,
			final InstanceFactory<ChronologicalNoteCategoryTemplate>
			chronologicalNoteCategoryTemplateInstanceFactory) {
		this.chronologicalNoteCategoryTemplateDao
		= chronologicalNoteCategoryTemplateDao;
		this.chronologicalNoteCategoryTemplateInstanceFactory
			= chronologicalNoteCategoryTemplateInstanceFactory;
	}
	
	/**
	 * Creates a new chronological note category template with the specified
	 * chronological note category and text.
	 * 
	 * @param category chronological note category
	 * @return newly created chronological note category
	 * @throws ChronologicalNoteCategoryTemplateExistsException Thrown when a
	 * duplicate chronological note category is found
	 */
	public ChronologicalNoteCategoryTemplate create(
		final ChronologicalNoteCategory category,
		final String text)
			throws ChronologicalNoteCategoryTemplateExistsException {
		if (this.chronologicalNoteCategoryTemplateDao.find(category)
			!= null) {
			throw new ChronologicalNoteCategoryTemplateExistsException(""
					+ "Chronological note category template already existse");
		}
		ChronologicalNoteCategoryTemplate template
		= this.chronologicalNoteCategoryTemplateInstanceFactory
		.createInstance();
		template.setCategory(category);
		template.setText(text);
		return this.chronologicalNoteCategoryTemplateDao
		.makePersistent(template);
	}
	
	/**
	 * Removes chronological note category template.
	 * 
	 * @param template chronological note category template
	 *
	 */
	public void remove(final ChronologicalNoteCategoryTemplate template){
		this.chronologicalNoteCategoryTemplateDao.makeTransient(template);
	}
	
	/**
	 * Find a chronological note category template with the specified
	 * chronological note category.
	 * 
	 * @param category chronological note category
	 * @return chronological note category template
	 */
	public ChronologicalNoteCategoryTemplate findCategoryTemplateByCategory(
		final ChronologicalNoteCategory category){
		return this.chronologicalNoteCategoryTemplateDao.find(category);
	}
}
