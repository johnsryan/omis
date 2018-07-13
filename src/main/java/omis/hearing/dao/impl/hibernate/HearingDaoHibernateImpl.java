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
package omis.hearing.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.hearing.dao.HearingDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingCategory;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.user.domain.UserAccount;
import omis.violationevent.domain.ViolationEvent;

/**
 * Implementation of data access object for hearing.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @author Ryan Johns
 * @version 0.1.4 (July 6, 2018)
 * @since OMIS 3.0
 */
public class HearingDaoHibernateImpl
	extends GenericHibernateDaoImpl<Hearing> implements HearingDao {
	
	/* Query Names */
	
	private static final String FIND_HEARING_QUERY_NAME = "findHearing";
	
	private static final String FIND_HEARING_EXCLUDING_QUERY_NAME =
			"findHearingExcluding";
	
	private static final String FIND_HEARINGS_BY_OFFENDER_QUERY_NAME =
			"findHearingsByOffender";
	
	private static final String FIND_HEARINGS_BY_VIOLATION_EVENT_QUERY_NAME =
			"findHearingsByViolationEvent";
	
	/* Param Names */
	
	private static final String LOCATION_PARAM_NAME = "location";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String OFFICER_PARAM_NAME = "officer";
	
	private static final String HEARING_PARAM_NAME = "hearing";
	
	private static final String CATEGORY_PARAM_NAME = "category";
	
	private static final String VIOLATION_EVENT_PARAM_NAME = "violationEvent";
	
	/**
	 * @param sessionFactory
	 * @param entityName
	 */
	protected HearingDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public Hearing find(final Location location, final Offender offender,
			final Date date, final UserAccount officer,
			final HearingCategory category) {
		Hearing hearing = (Hearing) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_HEARING_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, location,
						this.getEntityPropertyType(LOCATION_PARAM_NAME))
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(OFFICER_PARAM_NAME, officer,
						this.getEntityPropertyType(OFFICER_PARAM_NAME))
				.setParameter(CATEGORY_PARAM_NAME, category,
						this.getEntityPropertyType(CATEGORY_PARAM_NAME))
				.uniqueResult();
		
		return hearing;
	}

	/**{@inheritDoc} */
	@Override
	public Hearing findExcluding(final Location location,
			final Offender offender, final Date date,
			final UserAccount officer, final HearingCategory category,
			final Hearing hearing) {
		Hearing hearingExcluding = (Hearing) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_HEARING_EXCLUDING_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, location, 
						this.getEntityPropertyType(LOCATION_PARAM_NAME))
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(OFFICER_PARAM_NAME, officer,
						this.getEntityPropertyType(OFFICER_PARAM_NAME))
				.setParameter(CATEGORY_PARAM_NAME, category,
						this.getEntityPropertyType(CATEGORY_PARAM_NAME))
				.setParameter(HEARING_PARAM_NAME, hearing)
				.uniqueResult();
		
		return hearingExcluding;
	}

	/**{@inheritDoc} */
	@Override
	public List<Hearing> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<Hearing> hearings = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_HEARINGS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.list();
		
		return hearings;
	}

	/** {@inheritDoc} */
	@Override
	public List<Hearing> findByViolationEvent(
			final ViolationEvent violationEvent) {
		@SuppressWarnings("unchecked")
		List<Hearing> hearings = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_HEARINGS_BY_VIOLATION_EVENT_QUERY_NAME)
				.setParameter(VIOLATION_EVENT_PARAM_NAME, violationEvent)
				.list();
		return hearings;
	}
}