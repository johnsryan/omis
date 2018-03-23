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

import java.util.List;

import omis.chronologicalnote.dao.ChronologicalNoteCategoryGroupTemplateDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroupTemplate;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryGroupTemplateExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Chronological note category group template delegate.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryGroupTemplateDelegate {

	/* Data access objects. */
	private ChronologicalNoteCategoryGroupTemplateDao
	chronologicalNoteCategoryGroupTemplateDao;
	
	/*Instance factories. */
	private InstanceFactory<ChronologicalNoteCategoryGroupTemplate>
	chronologicalNoteCategoryGroupTemplateInstanceFactory;
	
	/**
	 * Instantiates a chronological note category group delegate with the
	 * specified data access object and instance factory.
	 * 
	 * @param chronologicalNoteCategoryGroupTemplateDao chronological note
	 * category group template data access object
	 * @param chronologicalNoteCategoryGroupTemplateInstanceFactory
	 * chronological note category group template instance factory
	 */
	public ChronologicalNoteCategoryGroupTemplateDelegate(
			final ChronologicalNoteCategoryGroupTemplateDao
			chronologicalNoteCategoryGroupTemplateDao,
			final InstanceFactory<ChronologicalNoteCategoryGroupTemplate>
			chronologicalNoteCategoryGroupTemplateInstanceFactory) {
		this.chronologicalNoteCategoryGroupTemplateDao = chronologicalNoteCategoryGroupTemplateDao;
		this.chronologicalNoteCategoryGroupTemplateInstanceFactory
			= chronologicalNoteCategoryGroupTemplateInstanceFactory;
	}
	
	/**
	 * Creates a new chronological note category group template with the
	 * specified chronological note category group name and text.
	 * 
	 * @param name chronological note category group name
	 * @param text text for chrono narrative
	 * @return newly created chronological note category group template
	 * @throws ChronologicalNoteCategoryGroupTemplateExistsException Thrown when
	 * a duplicate chronological note category group is found
	 */
	public ChronologicalNoteCategoryGroupTemplate create(
		final String text, final ChronologicalNoteCategoryGroup group)
			throws ChronologicalNoteCategoryGroupTemplateExistsException {
		if (this.chronologicalNoteCategoryGroupTemplateDao
			.findChronologicalNoteCategoryGroupTemplate(group, text) != null) {
			throw new ChronologicalNoteCategoryGroupTemplateExistsException(""
					+ "Chronological note category group template already"
					+ "exists");
		}
		ChronologicalNoteCategoryGroupTemplate template
		= this.chronologicalNoteCategoryGroupTemplateInstanceFactory
		.createInstance();
		template.setGroup(group);
		template.setText(text);
		return this.chronologicalNoteCategoryGroupTemplateDao
			.makePersistent(template);
	}
	
	/**
	 * Removes chronological note category group template.
	 * 
	 * @param template chronological note category group template
	 *
	 */
	public void remove(final ChronologicalNoteCategoryGroupTemplate template){
		this.chronologicalNoteCategoryGroupTemplateDao.makeTransient(template);
	}
	
	/**
	 * Find a chronological note category group template with the
	 * specified chronological note category group.
	 * 
	 * @param group chronological note category group
	 * @return A chronological note category group template
	 */
	public List<ChronologicalNoteCategoryGroupTemplate> findByGroup(
			final ChronologicalNoteCategoryGroup group) {
		return this.chronologicalNoteCategoryGroupTemplateDao
			.findByGroup(group);
	}
}