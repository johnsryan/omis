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
package omis.presentenceinvestigation.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.presentenceinvestigation.dao.PresentenceInvestigationDelayDao;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;

/**
 * Hibernate implementation of the presentence investigation delay data access 
 * object.
 *
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationDelayDaoHibernateImpl 
	extends GenericHibernateDaoImpl<PresentenceInvestigationDelay> 
	implements PresentenceInvestigationDelayDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findPresentenceInvestigationDelay";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findPresentenceInvestigationDelayExcluding";
	
	private static final String 
			FIND_BY_PRESENTENCE_INVESTIGATION_REQUEST_QUERY_NAME = 
					"findPresentenceInvestigationDelaysByPresentenceInvestigationRequest";
	
	/* Parameters. */
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String PRESENTENCE_INVESTIGATION_REQUEST_PARAM_NAME = 
			"presentenceInvestigationRequest";
	
	private static final String REASON_PARAM_NAME = "reason";
	
	private static final String EXCLUDED_DELAY_PARAM_NAME = "excludedDelay";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  presentence investigation delay.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public PresentenceInvestigationDelayDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationDelay find(final Date date,
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest,
			final PresentenceInvestigationDelayCategory reason) {
		PresentenceInvestigationDelay presentenceInvestigationDelay = 
				(PresentenceInvestigationDelay) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setDate(DATE_PARAM_NAME, date)
				.setParameter(PRESENTENCE_INVESTIGATION_REQUEST_PARAM_NAME, 
						presentenceInvestigationRequest)
				.setParameter(REASON_PARAM_NAME, reason)
				.uniqueResult();
		return presentenceInvestigationDelay;
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationDelay findExcluding(final Date date,
			final PresentenceInvestigationRequest 
					presentenceInvestigationRequest,
			final PresentenceInvestigationDelayCategory reason, 
			final PresentenceInvestigationDelay excludedDelay) {
		PresentenceInvestigationDelay presentenceInvestigationDelay = 
				(PresentenceInvestigationDelay) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setDate(DATE_PARAM_NAME, date)
				.setParameter(PRESENTENCE_INVESTIGATION_REQUEST_PARAM_NAME, 
						presentenceInvestigationRequest)
				.setParameter(REASON_PARAM_NAME, reason)
				.setParameter(EXCLUDED_DELAY_PARAM_NAME, excludedDelay)
				.uniqueResult();
		return presentenceInvestigationDelay;
	}

	/** {@inheritDoc} */
	@Override
	public List<PresentenceInvestigationDelay> 
			findByPresentenceInvestigationRequest(
					PresentenceInvestigationRequest 
							presentenceInvestigationRequest) {
		@SuppressWarnings("unchecked")
		List<PresentenceInvestigationDelay> delays = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_BY_PRESENTENCE_INVESTIGATION_REQUEST_QUERY_NAME)
				.setParameter(PRESENTENCE_INVESTIGATION_REQUEST_PARAM_NAME, 
						presentenceInvestigationRequest)
				.list();
		return delays;
	}
}