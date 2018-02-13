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
package omis.criminalassociation.dao.impl.hibernate;

import java.util.List;

import omis.criminalassociation.dao.CriminalAssociationCategoryDao;
import omis.criminalassociation.domain.CriminalAssociationCategory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;

import org.hibernate.SessionFactory;

/**
 * Hibernate entity configurable implementation of data access object for
 * criminal association category.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.1 (Apr, 14 2015)
 * @since OMIS 3.0
 *
 */
public class CriminalAssociationCategoryDaoHibernateImpl 
	extends GenericHibernateDaoImpl<CriminalAssociationCategory>
	implements CriminalAssociationCategoryDao {
	
	/* Query names.*/
	
	private static final String FIND_CRIMINAL_ASSOCIATION_CATEGORIES 
		= "findCriminalAssociationCategories";
	
	private static final String FIND_CRIMINAL_ASSOCIATION_CATEGORY 
		= "findCriminalAssociationCategory";
	
	/* Parameter names. */
	private final String NAME_PARAM_NAME = "name";
	
	/* Constructors. */
	
	/**
	 * Instantiates a data access object for criminal association category with 
	 * the specified session factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public CriminalAssociationCategoryDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<CriminalAssociationCategory> 
		findCriminalAssociationCategories() {
		@SuppressWarnings("unchecked")
		List<CriminalAssociationCategory> criminalAssociationCategories 
			= getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_CRIMINAL_ASSOCIATION_CATEGORIES)
			.list();
		return criminalAssociationCategories;
	}

	/** {@inheritDoc} */
	@Override
	public CriminalAssociationCategory find(final String name) {
		CriminalAssociationCategory category = (CriminalAssociationCategory)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_CRIMINAL_ASSOCIATION_CATEGORY)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return category;
	}
}