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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;
import omis.paroleeligibility.dao.ParoleEligibilityDao;
import omis.paroleeligibility.domain.ParoleEligibility;

/**
 * Parole eligibility hibernate implementation.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.1 (May 24, 2018)
 * @since OMIS 3.0
 */
public class ParoleEligibilityDaoHibernateImpl 
	extends GenericHibernateDaoImpl<ParoleEligibility> 
	implements ParoleEligibilityDao {

	/* Query names. */
	
	private static final String FIND_PAROLE_ELIGIBILITY_QUERY_NAME 
		= "findParoleEligibility";
	
	private static final String FIND_PAROLE_ELIGIBILITY_EXCLUDING_QUERY_NAME
		= "findParoleEligibilityExcluding";
	
	private static final String FINE_BY_OFFENDER_AFTER_DATE_QUERY_NAME =
			"findParoleEligibilitiesByOffenderAfterDate";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String HEARING_ELIGIBILITY_DATE_PARAM_NAME
		= "hearingEligibilityDate";
	
	private static final String EXCLUDED_PAROLE_ELIGIBILITY_PARAM_NAME 
		= "excludedEligibility";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Constructor */
	
	/**
	 * Instantiates a hibernate implementation of data access object for
	 * parole eligibility.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParoleEligibilityDaoHibernateImpl(
			final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEligibility find(final Offender offender, 
			final Date hearingEligibilityDate) {
		ParoleEligibility eligibility = (ParoleEligibility)
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_PAROLE_ELIGIBILITY_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setParameter(HEARING_ELIGIBILITY_DATE_PARAM_NAME, 
					hearingEligibilityDate)
			.uniqueResult();
		return eligibility;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEligibility findExcluding(
			final ParoleEligibility excludedEligibility, 
			final Offender offender, 
			final Date hearingEligibilityDate) {
		ParoleEligibility eligibility = (ParoleEligibility)
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_PAROLE_ELIGIBILITY_EXCLUDING_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setParameter(EXCLUDED_PAROLE_ELIGIBILITY_PARAM_NAME, 
					excludedEligibility)
			.setParameter(HEARING_ELIGIBILITY_DATE_PARAM_NAME, 
					hearingEligibilityDate)
			.uniqueResult();
		return eligibility;
	}

	/**{@inheritDoc} */
	@Override
	public List<ParoleEligibility> findByOffenderAfterDate(
			final Offender offender, final Date date) {
		@SuppressWarnings("unchecked")
		List<ParoleEligibility> paroleEligibilities =
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FINE_BY_OFFENDER_AFTER_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.list();
		return paroleEligibilities;
	}

}
