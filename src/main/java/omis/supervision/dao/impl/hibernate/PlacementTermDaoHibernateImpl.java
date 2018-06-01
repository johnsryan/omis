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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;
import omis.supervision.dao.PlacementTermDao;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.SupervisoryOrganizationTerm;

/**
 * Hibernate implementation of data access object for placement terms.
 * 
 * @author Stephen Abson
 * @author Jason Nelson
 * @version 0.1.0 (Oct 9, 2013)
 * @since OMIS 3.0
 */
public class PlacementTermDaoHibernateImpl
		extends GenericHibernateDaoImpl<PlacementTerm>
		implements PlacementTermDao {	

	/* Query names. */
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME
			= "findPlacementTermsByOffender";
	
	private static final String FIND_FOR_OFFENDER_ON_DATE_QUERY_NAME
			= "findPlacementTermForOffenderOnDate";
	
	private static final String FIND_QUERY_NAME = "findPlacementTerm";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
			= "findPlacementTermExcluding";
	
	private static final String COUNT_BETWEEN_DATES_FOR_OFFENDER_QUERY_NAME
			= "countPlacementTermsBetweenDatesForOffender";
	
	private static final String
	COUNT_BETWEEN_DATES_FOR_OFFENDER_EXCLUDING_QUERY_NAME
			= "countPlacementTermsBetweenDatesForOffenderExcluding";
	
	private static final String FIND_BY_CORRECTIONAL_STATUS_TERM_QUERY_NAME
			= "findPlacementTermsByCorrectionalStatusTerm";
	
	private static final String FIND_BY_SUPERVISORY_ORGANIZATION_TERM_QUERY_NAME
			= "findPlacementTermsBySupervisoryOrganizationTerm";


	private static final String COUNT_WITH_CORRECTIONAL_STATUS_TERM_QUERY_NAME
			= "countPlacementTermsByCorrectionalStatusTerm";

	private static final String COUNT_WITH_SUPERVISORY_ORGANIZATION_TERM_QUERY_NAME
			= "countPlacementTermsBySupervisoryOrganizationTerm";
	
	private static final String FIND_WITH_END_DATE_QUERY_NAME
			= "findPlacementTermForOffenderWithEndDate";
	
	private static final String COUNT_FOR_OFFENDER_AFTER_DATE
			= "countPlacementTermsByOffenderAfterDate";
	
	private static final String COUNT_FOR_OFFENDER_BEFORE_DATE
			= "countPlacementTermsByOffenderBeforeDate";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";

	private static final String DATE_PARAM_NAME = "date";

	private static final String START_DATE_PARAM_NAME = "startDate";

	private static final String END_DATE_PARAM_NAME = "endDate";

	private static final String EXCLUDED_PARAM_NAME = "excludedPlacementTerm";

	private static final String CORRECTIONAL_STATUS_TERM_PARAM_NAME
		= "correctionalStatusTerm";
	
	private static final String SUPERVISORY_ORGANIZATION_TERM_PARAM_NAME
		= "supervisoryOrganizationTerm";

	private static final String EXCLUDE_PLACEMENT_TERMS_PARAM_NAME
		= "excludePlacementTerms";

	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * placement terms with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public PlacementTermDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<PlacementTerm> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<PlacementTerm> terms = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender).list();
		return terms;
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findForOffenderOnDate(
			final Offender offender, final Date date) {
		PlacementTerm term = (PlacementTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_FOR_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, date).uniqueResult();
		return term;
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm find(final Offender offender,
			final Date startDate, final Date endDate,
			final CorrectionalStatusTerm correctionalStatusTerm,
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm) {
		PlacementTerm placementTerm
			= (PlacementTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(CORRECTIONAL_STATUS_TERM_PARAM_NAME,
						correctionalStatusTerm)
				.setParameter(SUPERVISORY_ORGANIZATION_TERM_PARAM_NAME,
						supervisoryOrganizationTerm)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return placementTerm;
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findExcluding(final Offender offender,
			final Date startDate, final Date endDate,
			final CorrectionalStatusTerm correctionalStatusTerm,
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm,
			final PlacementTerm excludedPlacementTerm) {
		PlacementTerm placementTerm
			= (PlacementTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(CORRECTIONAL_STATUS_TERM_PARAM_NAME,
						correctionalStatusTerm)
				.setParameter(SUPERVISORY_ORGANIZATION_TERM_PARAM_NAME,
						supervisoryOrganizationTerm)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setParameter(EXCLUDED_PARAM_NAME, excludedPlacementTerm)
				.uniqueResult();
		return placementTerm;
	}

	/** {@inheritDoc} */
	@Override
	public long countForOffenderBetweenDates(final Offender offender,
			final Date startDate, final Date endDate) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_BETWEEN_DATES_FOR_OFFENDER_QUERY_NAME)
				.setParameter(START_DATE_PARAM_NAME, startDate)
				.setParameter(END_DATE_PARAM_NAME, endDate)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public long countForOffenderBetweenDatesExcluding(final Offender offender,
			final Date startDate, final Date endDate,
			final PlacementTerm... excludedPlacementTerms) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
						COUNT_BETWEEN_DATES_FOR_OFFENDER_EXCLUDING_QUERY_NAME)
				.setParameter(START_DATE_PARAM_NAME, startDate)
				.setParameter(END_DATE_PARAM_NAME, endDate)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameterList(EXCLUDE_PLACEMENT_TERMS_PARAM_NAME,
						excludedPlacementTerms)
				.uniqueResult();
		return count;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<PlacementTerm> findByCorrectionalStatusTerm(
			final CorrectionalStatusTerm correctionalStatusTerm) {
		@SuppressWarnings("unchecked")
		List<PlacementTerm> placementTerms =
				this.getSessionFactory().getCurrentSession().getNamedQuery(
						FIND_BY_CORRECTIONAL_STATUS_TERM_QUERY_NAME)
					.setParameter(CORRECTIONAL_STATUS_TERM_PARAM_NAME,
						correctionalStatusTerm).list();
		return placementTerms;
	}

	/** {@inheritDoc} */
	@Override
	public List<PlacementTerm> findBySupervisoryOrganizationTerm(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm) {
		@SuppressWarnings("unchecked")
		List<PlacementTerm> placementTerms =
				this.getSessionFactory().getCurrentSession().getNamedQuery(
						FIND_BY_SUPERVISORY_ORGANIZATION_TERM_QUERY_NAME)
					.setParameter(SUPERVISORY_ORGANIZATION_TERM_PARAM_NAME,
						supervisoryOrganizationTerm).list();
		return placementTerms;
	}

	/** {@inheritDoc} */
	@Override
	public long countByCorrectionalStatusTerm(
			final CorrectionalStatusTerm correctionalStatusTerm) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_WITH_CORRECTIONAL_STATUS_TERM_QUERY_NAME)
				.setParameter(CORRECTIONAL_STATUS_TERM_PARAM_NAME,
						correctionalStatusTerm).uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public long countBySupervisoryOrganizationTerm(
			final SupervisoryOrganizationTerm supervisoryOrganizationTerm) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
						COUNT_WITH_SUPERVISORY_ORGANIZATION_TERM_QUERY_NAME)
				.setParameter(SUPERVISORY_ORGANIZATION_TERM_PARAM_NAME,
						supervisoryOrganizationTerm).uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public PlacementTerm findForOffenderWithEndDate(
			final Offender offender, final Date endDate) {
		PlacementTerm placementTerm = (PlacementTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_WITH_END_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return placementTerm;
	}

	/** {@inheritDoc} */
	@Override
	public long countForOffenderAfterDate(final Offender offender, 
			final Date date, final PlacementTerm excludedPlacementTerm) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_FOR_OFFENDER_AFTER_DATE)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(EXCLUDED_PARAM_NAME, excludedPlacementTerm)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public long countForOffenderBeforeDate(final Offender offender, 
			final Date date, final PlacementTerm excludedPlacementTerm) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_FOR_OFFENDER_BEFORE_DATE)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(EXCLUDED_PARAM_NAME, excludedPlacementTerm)
				.uniqueResult();
		return count;
	}
}