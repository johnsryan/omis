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
package omis.paroleplan.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.document.domain.Document;
import omis.paroleplan.dao.ParolePlanDocumentAssociationDao;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanDocumentAssociation;

/**
 * Hibernate implementation of the parole plan document association data access 
 * object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanDocumentAssociationDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParolePlanDocumentAssociation>
		implements ParolePlanDocumentAssociationDao {
	
	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findParolePlanDocumentAssociation";
	
	private static final String FIND_BY_PAROLE_PLAN_QUERY_NAME = 
			"findParolePlanDocumentAssociationsByParolePlan";
	
	/* Parameters. */
	
	private static final String PAROLE_PLAN_PARAM_NAME = "parolePlan";
	
	private static final String DOCUMENT_PARAM_NAME = "document";
	
	/** Instantiates a hibernate implementation of the data access object for 
	*  parole plan document association.
	* 
	* @param sessionFactory session factory
	* @param entityName entity name
	*/
	public ParolePlanDocumentAssociationDaoHibernateImpl(
		final SessionFactory sessionFactory, final String entityName) {
	super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlanDocumentAssociation find(final ParolePlan parolePlan, 
			final Document document) {
		ParolePlanDocumentAssociation association = 
				(ParolePlanDocumentAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(PAROLE_PLAN_PARAM_NAME, parolePlan)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParolePlanDocumentAssociation> findByParolePlan(
			final ParolePlan parolePlan) {
		@SuppressWarnings("unchecked")
		List<ParolePlanDocumentAssociation> associations = this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_PAROLE_PLAN_QUERY_NAME)
				.setParameter(PAROLE_PLAN_PARAM_NAME, parolePlan)
				.list();
		return associations;
	}
}