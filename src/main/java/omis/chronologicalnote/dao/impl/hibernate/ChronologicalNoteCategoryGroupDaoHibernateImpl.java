/* 
0* OMIS - Offender Management Information System 
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

import omis.chronologicalnote.dao.ChronologicalNoteCategoryGroupDao;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for chronological note
 * category group.
 * 
 * @author Yidong Li
 * @version 0.1.0 (March 5, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryGroupDaoHibernateImpl
		extends GenericHibernateDaoImpl<ChronologicalNoteCategoryGroup>
		implements ChronologicalNoteCategoryGroupDao {
	/* Query names. */
	private static final String 
	FIND_CHRONOLOGICAL_NOTE_CATEGORY_GROUP_QUERY_NAME
		= "findChronologicalNoteCategoryGroups";

	private static final String 
	FIND_CHRONOLOGICAL_NOTE_CATEGORY_GROUP_BY_CATEGORY_QUERY_NAME
		= "findChronologicalNoteCategoryGroupByCatergory";
	/* Parameters. */
	private static final String NAME_PARAM_NAME = "name";
	
	/* Constructors. */
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * chronological note category template dao.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ChronologicalNoteCategoryGroupDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategoryGroup> findAll() {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategoryGroup>
			chronologicalNoteCategoryGroups 
			= (List<ChronologicalNoteCategoryGroup>) this.getSessionFactory()
			.getCurrentSession().getNamedQuery(
				FIND_CHRONOLOGICAL_NOTE_CATEGORY_GROUP_QUERY_NAME)
			.list();
		return chronologicalNoteCategoryGroups;
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryGroup findByName(final String name) {
		ChronologicalNoteCategoryGroup chronologicalNoteCategoryGroup 
		= (ChronologicalNoteCategoryGroup) this.getSessionFactory()
		.getCurrentSession().getNamedQuery(
			FIND_CHRONOLOGICAL_NOTE_CATEGORY_GROUP_BY_CATEGORY_QUERY_NAME)
		.setParameter(NAME_PARAM_NAME, name)
		.uniqueResult();
		return chronologicalNoteCategoryGroup;
	};
}