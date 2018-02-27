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

import omis.chronologicalnote.dao.ChronologicalNoteCategoryAssociationDao;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryAssociation;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note
 * category association.
 * 
 * @author Yidong Li
 * @version 0.1.0 (Jan 31, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNoteCategoryAssociation>
		implements ChronologicalNoteCategoryAssociationDao {
	/* Query names. */
	private static final String FIND_BY_NOTE_CATEGORY_QUERY_NAME
		= "findByNoteAndCategory";
	private static final String FIND_BY_NOTE_QUERY_NAME
		= "findChronologicalNoteCategoryAssociationsByNote";
	private static final String FIND_QUERY_NAME
		= "findChronologicalNoteCategoryAssociation";
	private static final String FIND_ASSOCIATED_CATEGORIES_QUERY_NAME
		= "findAssociatedChronologicalNoteCategoriesByNote";

	/* Parameters. */
	private static final String NOTE_PARAM_NAME
		= "note";
	private static final String CATEGORY_PARAM_NAME
		= "category";
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * chronological note category association.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ChronologicalNoteCategoryAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryAssociation findByNoteAndCategory(
		final ChronologicalNote note,
		final ChronologicalNoteCategory category) {
		ChronologicalNoteCategoryAssociation
			chronologicalNoteCategoryAssociation 
			= (ChronologicalNoteCategoryAssociation) this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_BY_NOTE_CATEGORY_QUERY_NAME)
			.setParameter(NOTE_PARAM_NAME, note)
			.setParameter(CATEGORY_PARAM_NAME, category)
			.uniqueResult();
		return chronologicalNoteCategoryAssociation;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategoryAssociation> findChronologicalNoteCategoryAssociationsByNote(final
		ChronologicalNote note) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategoryAssociation> 
			chronologicalNoteCategoryAssociations
			= (List<ChronologicalNoteCategoryAssociation>)
			this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_BY_NOTE_QUERY_NAME)
			.setParameter(NOTE_PARAM_NAME, note)
			.list();
		return chronologicalNoteCategoryAssociations;
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryAssociation find(final ChronologicalNote
		note, final ChronologicalNoteCategory category) {
		ChronologicalNoteCategoryAssociation
			chronologicalNoteCategoryAssociation
			= (ChronologicalNoteCategoryAssociation)
			this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
			.setParameter(NOTE_PARAM_NAME, note)
			.setParameter(CATEGORY_PARAM_NAME, category)
			.uniqueResult();
		return chronologicalNoteCategoryAssociation;
	}

	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findAssociatedCategories(
		final ChronologicalNote note) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategory> categories = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_ASSOCIATED_CATEGORIES_QUERY_NAME)
				.setParameter(NOTE_PARAM_NAME, note)
				.list();
		return categories;
	}
}