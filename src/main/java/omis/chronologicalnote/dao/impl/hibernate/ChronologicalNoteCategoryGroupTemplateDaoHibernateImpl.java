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

import omis.chronologicalnote.dao.ChronologicalNoteCategoryGroupTemplateDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroupTemplate;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import java.util.List;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note
 * category group template.
 * 
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryGroupTemplateDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNoteCategoryGroupTemplate>
		implements ChronologicalNoteCategoryGroupTemplateDao {
	/* Query names. */
	private static final String FIND_BY_GROUP_QUERY_NAME
	= "findChronologicalNoteCategoryGroupTemplateByGroup";
	
	private static final String FIND_QUERY_NAME
	= "findChronologicalNoteCategoryGroupTemplate";
	
	/* Parameters. */
	private static final String GROUP_PARAM_NAME
		= "group";
	private static final String TEXT_PARAM_NAME
		= "text";
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * chronological note category group template dao.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ChronologicalNoteCategoryGroupTemplateDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryGroupTemplate
		findChronologicalNoteCategoryGroupTemplate(
		final ChronologicalNoteCategoryGroup group,
		final String text) {
		ChronologicalNoteCategoryGroupTemplate
			chronologicalNoteCategoryGroupTemplate 
			= (ChronologicalNoteCategoryGroupTemplate)
			this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
			.setParameter(GROUP_PARAM_NAME, group)
			.setParameter(TEXT_PARAM_NAME, text)
			.uniqueResult();
		return chronologicalNoteCategoryGroupTemplate;
	}

	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategoryGroupTemplate> findByGroup(
			final ChronologicalNoteCategoryGroup group) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategoryGroupTemplate> templates
			= this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_GROUP_QUERY_NAME)
				.setParameter(GROUP_PARAM_NAME, group)
				.list();
		return templates;
	}
	
	/** {@inheritDoc} */
	/*@Override
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
	}*/
	
	/** {@inheritDoc} */
	/*@Override
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
	}*/

	/** {@inheritDoc} */
	/*@Override
	public List<ChronologicalNoteCategory> findAssociatedCategories(
		final ChronologicalNote note) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategory> categories = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_ASSOCIATED_CATEGORIES_QUERY_NAME)
				.setParameter(NOTE_PARAM_NAME, note)
				.list();
		return categories;
	}*/
}