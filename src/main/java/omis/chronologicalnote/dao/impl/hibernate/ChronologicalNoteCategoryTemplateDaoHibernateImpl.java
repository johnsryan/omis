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
package omis.chronologicalnote.dao.impl.hibernate;

import omis.chronologicalnote.dao.ChronologicalNoteCategoryTemplateDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryTemplate;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note
 * category template.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryTemplateDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNoteCategoryTemplate>
		implements ChronologicalNoteCategoryTemplateDao {
	/* Query names. */
	private static final String
	FIND_CHRONOLOGICAL_NOTE_CATEGORY_TEMPLATE_QUERY_NAME
		= "findChronologicalNoteCategoryTemplate";

	/* Parameters. */
	private static final String CATEGORY_PARAM_NAME
		= "category";
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * chronological note category template dao.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ChronologicalNoteCategoryTemplateDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryTemplate find(
		final ChronologicalNoteCategory category) {
		ChronologicalNoteCategoryTemplate chronologicalNoteCategoryTemplate
			= (ChronologicalNoteCategoryTemplate) this.getSessionFactory()
			.getCurrentSession().getNamedQuery(
				FIND_CHRONOLOGICAL_NOTE_CATEGORY_TEMPLATE_QUERY_NAME)
			.setParameter(CATEGORY_PARAM_NAME, category)
			.uniqueResult();
		return chronologicalNoteCategoryTemplate;
	}
}