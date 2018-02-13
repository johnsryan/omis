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
package omis.victim.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.victim.dao.VictimNoteCategoryDao;
import omis.victim.domain.VictimNoteCategory;

/**
 * Hibernate implementation of data access object for victim note categories.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 23, 2015)
 * @since OMIS 3.0
 */
public class VictimNoteCategoryDaoHibernateImpl
		extends GenericHibernateDaoImpl<VictimNoteCategory>
		implements VictimNoteCategoryDao {
	
	/* Query names. */

	private static final String FINAL_ALL_QUERY_NAME
			= "findVictimNoteCategories";
	
	private static final String FIND_CATEGORY_QUERY_NAME
		= "findVictimNoteCategory";
	
	/* Parameter names. */
	private static final String NAME_PARAM_NAME = "name";
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of data access object for victim
	 * note categories.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public VictimNoteCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<VictimNoteCategory> findAll() {
		@SuppressWarnings("unchecked")
		List<VictimNoteCategory> categories = this.getSessionFactory()
			.getCurrentSession().getNamedQuery(FINAL_ALL_QUERY_NAME).list();
		return categories;
	}

	/** {@inheritDoc} */
	@Override
	public VictimNoteCategory find(final String name) {
		VictimNoteCategory category = (VictimNoteCategory)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_CATEGORY_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return category;
	}
}