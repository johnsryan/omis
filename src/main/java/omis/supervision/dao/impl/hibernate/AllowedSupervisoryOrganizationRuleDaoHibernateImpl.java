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
package omis.supervision.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.supervision.dao.AllowedSupervisoryOrganizationRuleDao;
import omis.supervision.domain.AllowedSupervisoryOrganizationRule;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Hibernate implementation of data access object for rules that allow
 * supervisory organizations for correctional statuses.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Dec 4, 2014)
 * @since OMIS 3.0
 */
public class AllowedSupervisoryOrganizationRuleDaoHibernateImpl
		extends GenericHibernateDaoImpl<AllowedSupervisoryOrganizationRule>
		implements AllowedSupervisoryOrganizationRuleDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME
		= "findAllowedSupervisoryOrganizationRule";
	
	/* Parameter names. */
	
	private static final String CORRECTIONAL_STATUS_PARAM_NAME
		= "correctionalStatus";
	
	private static final String SUPERVISORY_ORGANIZATION_PARAM_NAME
		= "supervisoryOrganization";

	/* Constructors. */
	
	/**
	 * Instantiates a Hibernate implementation of data access object for rules
	 * that allow supervisory organizations for correctional statuses.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AllowedSupervisoryOrganizationRuleDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public AllowedSupervisoryOrganizationRule find(
			final CorrectionalStatus correctionalStatus,
			final SupervisoryOrganization supervisoryOrganization) {
		AllowedSupervisoryOrganizationRule rule
			= (AllowedSupervisoryOrganizationRule) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
					.setParameter(CORRECTIONAL_STATUS_PARAM_NAME,
							correctionalStatus)
					.setParameter(SUPERVISORY_ORGANIZATION_PARAM_NAME,
							supervisoryOrganization)
					.uniqueResult();
		return rule;
	}
}