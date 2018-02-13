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
package omis.parolereview.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;
import omis.parolereview.dao.ParoleReviewDao;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.StaffRoleCategory;
import omis.staff.domain.StaffAssignment;

/**
 * Hibernate implementation of the parole review data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParoleReview>
		implements ParoleReviewDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findParoleReview";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParoleReviewExcluding";
	
	/* Parameters. */
	
	private static final String STAFF_ASSIGNMENT_PARAM_NAME = "staffAssignment";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String STAFF_ROLE_PARAM_NAME = "staffRole";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedParoleReview";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  parole review.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParoleReviewDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReview find(final StaffAssignment staffAssignment, 
			final Offender offender, final StaffRoleCategory staffRole) {
		ParoleReview paroleReview = (ParoleReview) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(STAFF_ASSIGNMENT_PARAM_NAME, staffAssignment)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(STAFF_ROLE_PARAM_NAME, staffRole)
				.uniqueResult();
		return paroleReview;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReview findExcluding(
			final StaffAssignment staffAssignment, final Offender offender, 
			final StaffRoleCategory staffRole,
			final ParoleReview excludedParoleReview) {
		ParoleReview paroleReview = (ParoleReview) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(STAFF_ASSIGNMENT_PARAM_NAME, staffAssignment)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(STAFF_ROLE_PARAM_NAME, staffRole)
				.setParameter(EXCLUDED_PARAM_NAME, excludedParoleReview)
				.uniqueResult();
		return paroleReview;
	}
}