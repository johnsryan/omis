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
package omis.violationevent.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.facility.domain.Unit;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.violationevent.dao.ViolationEventDao;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;

/**
 * Hibernate implementation of violation event data access object.
 * 
 * @author Annie Wahl 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public class ViolationEventDaoHibernateImpl
	extends GenericHibernateDaoImpl<ViolationEvent>
		implements ViolationEventDao {
	
	private static final String FIND_VIOLATION_EVENT_QUERY_NAME =
			"findViolationEvent";
	
	private static final String FIND_VIOLATION_EVENT_EXCLUDING_QUERY_NAME =
			"findViolationEventExcluding";
	
	private static final String FIND_VIOLATION_EVENTS_BY_OFFENDER_QUERY_NAME =
			"findViolationEventsByOffender";
	
	private static final String
		FIND_UNRESOLVED_VIOLATION_EVENTS_BY_OFFENDER_QUERY_NAME =
			"findUnresolvedViolationEventsByOffender";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String JURISDICTION_PARAM_NAME = "jurisdiction";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String DETAILS_PARAM_NAME = "details";
	
	private static final String CATEGORY_PARAM_NAME = "category";
	
	private static final String VIOLATION_EVENT_PARAM_NAME = "violationEvent";
	
	private static final String UNIT_PARAM_NAME = "unit";
	
	/**
	 * @param sessionFactory
	 * @param entityName
	 */
	protected ViolationEventDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/**{@inheritDoc} */
	@Override
	public ViolationEvent find(final Offender offender,
			final Organization jurisdiction, final Unit unit,
			final Date date, final String details,
			final ViolationEventCategory category) {
		ViolationEvent violationEvent = (ViolationEvent) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_VIOLATION_EVENT_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(JURISDICTION_PARAM_NAME, jurisdiction)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(DETAILS_PARAM_NAME, details)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(UNIT_PARAM_NAME, unit)
				.uniqueResult();
		
		return violationEvent;
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEvent findExcluding(
			final ViolationEvent excludedViolationEvent, 
			final Offender offender, final Organization jurisdiction, 
			final Unit unit, final Date date, final String details,
			final ViolationEventCategory category) {
		ViolationEvent violationEvent = (ViolationEvent) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_VIOLATION_EVENT_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(JURISDICTION_PARAM_NAME, jurisdiction)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(DETAILS_PARAM_NAME, details)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(UNIT_PARAM_NAME, unit)
				.setParameter(VIOLATION_EVENT_PARAM_NAME, 
						excludedViolationEvent)
				.uniqueResult();
		
		return violationEvent;
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationEvent> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ViolationEvent> violationEvents = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VIOLATION_EVENTS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.list();
		
		return violationEvents;
	}

	/**{@inheritDoc} */
	@Override
	public List<ViolationEvent> findUnresolvedByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ViolationEvent> violationEvents = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_UNRESOLVED_VIOLATION_EVENTS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.list();
		
		return violationEvents;
	}
}