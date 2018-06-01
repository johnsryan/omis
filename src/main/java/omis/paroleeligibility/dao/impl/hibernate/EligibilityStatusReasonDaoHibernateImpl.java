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
package omis.paroleeligibility.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.paroleeligibility.dao.EligibilityStatusReasonDao;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;

/**
 * Hibernate implementation of data access object for eligibility status reason.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public class EligibilityStatusReasonDaoHibernateImpl 
	extends GenericHibernateDaoImpl<EligibilityStatusReason>
	implements EligibilityStatusReasonDao {

	/* Query names */
	
	private static final String FIND_ELIGIBILITY_STATUS_REASONS_QUERY_NAME
		= "findEligibilityStatusReasons";
	
	private static final String FIND_ELIGIBILITY_STATUS_REASON_QUERY_NAME
		= "findEligibilityStatusReason";
	
	private static final String 
		FIND_ELIGIBILITY_STATUS_REASON_EXLCUDING_QUERY_NAME
			= "findEligibilityStatusReasonExcluding";
	
	private static final String FIND_BY_STATUS_CATEGORY_QUERY_NAME =
			"findEligibilityStatusReasonsByStatusCategory";
	
	/* Parameter names */
	
	private static final String NAME_PARAM_NAME = "name";
	
	private static final String STATUS_CATEGORY_PARAM_NAME = "statusCategory";
	
	private static final String EXCLUDED_REASON_PARAM_NAME
		= "excludedReason";
	
	/* Constructor */
	
	/**
	 * Instantiates a hibernate implementation of data access object for
	 * parole eligibility note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public EligibilityStatusReasonDaoHibernateImpl(
			final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations */
	
	/** {@inheritDoc} */
	@Override
	public List<EligibilityStatusReason> findEligibilityStatusReasons() {
		@SuppressWarnings("unchecked")
		List<EligibilityStatusReason> reasons = this.getSessionFactory()
		.getCurrentSession()
		.getNamedQuery(FIND_ELIGIBILITY_STATUS_REASONS_QUERY_NAME)
		.list();
		return reasons;
	}

	/** {@inheritDoc} */
	@Override
	public EligibilityStatusReason findEligibilityStatusReason(
			final String name, final EligibilityStatusCategory statusCategory) {
		EligibilityStatusReason reason = (EligibilityStatusReason)
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_ELIGIBILITY_STATUS_REASON_QUERY_NAME)
			.setParameter(NAME_PARAM_NAME, name)
			.setParameter(STATUS_CATEGORY_PARAM_NAME, statusCategory)
			.uniqueResult();
		return reason;
	}

	/** {@inheritDoc} */
	@Override
	public EligibilityStatusReason findEligibilityStatusReasonExcluding(
			final EligibilityStatusReason excludedReason,
			final String name, final EligibilityStatusCategory statusCategory) {
		EligibilityStatusReason reason = (EligibilityStatusReason)
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_ELIGIBILITY_STATUS_REASON_EXLCUDING_QUERY_NAME)
			.setParameter(NAME_PARAM_NAME, name)
			.setParameter(STATUS_CATEGORY_PARAM_NAME, statusCategory)
			.setParameter(EXCLUDED_REASON_PARAM_NAME, 
					excludedReason)
			.uniqueResult();
		return reason;
	}

	/**{@inheritDoc} */
	@Override
	public List<EligibilityStatusReason> findByStatusCategory(
			final EligibilityStatusCategory statusCategory) {
		@SuppressWarnings("unchecked")
		List<EligibilityStatusReason> reasons = this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_BY_STATUS_CATEGORY_QUERY_NAME)
			.setParameter(STATUS_CATEGORY_PARAM_NAME, statusCategory)
			.list();
		return reasons;
	}
	
}
