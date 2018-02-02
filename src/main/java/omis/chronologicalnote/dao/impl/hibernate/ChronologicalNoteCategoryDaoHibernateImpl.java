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
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note
 * category.
 * 
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (Jan 31, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNoteCategory>
		implements ChronologicalNoteCategoryDao {
	/* Query names. */
	private static final String FIND_CATEGORIES_QUERY_NAME
		= "findChronologicalNoteCategories";
	private static final String FIND_ALL_CATEGORIES_QUERY_NAME = "findValidChronologicalNoteCategories";
	
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
		List<ChronologicalNoteCategory> categories = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_ALL_CATEGORIES_QUERY_NAME)
				.list();
		return categories;
	}
}