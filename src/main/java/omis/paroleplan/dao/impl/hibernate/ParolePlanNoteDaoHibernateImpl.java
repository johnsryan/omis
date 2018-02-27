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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.paroleplan.dao.ParolePlanNoteDao;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanNote;

/**
 * Hibernate implementation of the parole plan note data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanNoteDaoHibernateImpl 
		extends GenericHibernateDaoImpl<ParolePlanNote>
		implements ParolePlanNoteDao {
	
	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findParolePlanNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findParolePlanNoteExcluding";
	
	private static final String FIND_BY_PAROLE_PLAN_QUERY_NAME = 
			"findParolePlanNotesByParolePlan";
	
	/* Parameters. */
	
	private static final String PAROLE_PLAN_PARAM_NAME = "parolePlan";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String EXCLUDED_PARAM_NAME = "excludedParolePlanNote";
	
	/** Instantiates a hibernate implementation of the data access object for 
	*  parole plan.
	* 
	* @param sessionFactory session factory
	* @param entityName entity name
	*/
	public ParolePlanNoteDaoHibernateImpl(
		final SessionFactory sessionFactory, final String entityName) {
	super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlanNote find(final ParolePlan parolePlan, 
			final String description, final Date date) {
		ParolePlanNote parolePlanNote = (ParolePlanNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(PAROLE_PLAN_PARAM_NAME, parolePlan)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.uniqueResult();
		return parolePlanNote;
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlanNote findExcluding(final ParolePlan parolePlan, 
			final String description, final Date date,
			final ParolePlanNote excludedParolePlanNote) {
		ParolePlanNote parolePlanNote = (ParolePlanNote) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(PAROLE_PLAN_PARAM_NAME, parolePlan)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(EXCLUDED_PARAM_NAME, excludedParolePlanNote)
				.uniqueResult();
		return parolePlanNote;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParolePlanNote> findByParolePlan(final ParolePlan parolePlan) {
		@SuppressWarnings("unchecked")
		List<ParolePlanNote> notes = this.getSessionFactory()
		.getCurrentSession()
				.getNamedQuery(FIND_BY_PAROLE_PLAN_QUERY_NAME)
				.setParameter(PAROLE_PLAN_PARAM_NAME, parolePlan)
				.list();
		return notes;
	}
}