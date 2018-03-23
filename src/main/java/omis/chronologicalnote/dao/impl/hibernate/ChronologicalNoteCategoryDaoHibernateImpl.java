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

import java.util.List;

import omis.chronologicalnote.dao.ChronologicalNoteCategoryDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note
 * category.
 * 
 * @author Yidong Li
 * @author Joel Norris
 * @author Stephen Abson
 * @version 0.1.1 (Jan 31, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNoteCategory>
		implements ChronologicalNoteCategoryDao {
	
	/* Query names. */
	
	private static final String FIND_CATEGORIES_QUERY_NAME
		= "findChronologicalNoteCategories";
	private static final String FIND_ALL_CATEGORIES_QUERY_NAME
		= "findValidChronologicalNoteCategories";
	private static final String FIND_CATEGORY_QUERY_NAME
		= "findChronologicalNoteCategory";
	private static final String FIND_CATEGORIES_BY_GROUP_QUERY_NAME
		= "findChronologicalNoteCategoriesByGroup";
	
	/* Parameter names. */
	
	private static final String NAME_PARAM_NAME = "name";
	private static final String GROUP_PARAM_NAME = "group";
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * chronological note category.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ChronologicalNoteCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findCategories() {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategory> categories = this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_CATEGORIES_QUERY_NAME)
			.list();
		return categories;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findAll() {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategory> categories = this.getSessionFactory()
			.getCurrentSession()
				.getNamedQuery(FIND_ALL_CATEGORIES_QUERY_NAME)
				.list();
		return categories;
	}

	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategory find(
			final String name,
			final ChronologicalNoteCategoryGroup group) {
		ChronologicalNoteCategory category = (ChronologicalNoteCategory)
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_CATEGORY_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(GROUP_PARAM_NAME, group)
				.uniqueResult();
		return category;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findCategoriesByGroup(
	final ChronologicalNoteCategoryGroup group) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategory> categories = 
			(List<ChronologicalNoteCategory>) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_CATEGORIES_BY_GROUP_QUERY_NAME)
			.setParameter(GROUP_PARAM_NAME, group)
			.list();
		return categories;
	}
}