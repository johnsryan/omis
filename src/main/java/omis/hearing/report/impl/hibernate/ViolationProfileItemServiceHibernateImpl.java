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
package omis.hearing.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.hearing.report.ViolationProfileItemService;
import omis.hearing.report.ViolationProfileSummary;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of violation profile item.
 *
 * @author Sheronda Vaughn
 * @author Annie Jacques
 * @author Josh Divine
 * @version 0.1.0 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ViolationProfileItemServiceHibernateImpl 
		implements ViolationProfileItemService {
	
	private final SessionFactory sessionFactory;
	
	private static final String FIND_DISCIPLINARY_UNRESOLVED_COUNT_BY_OFFENDER =
			"findUnresolvedDisciplinaryViolationCountByOffender";
	
	private static final String FIND_SUPERVISION_UNRESOLVED_COUNT_BY_OFFENDER =
			"findUnresolvedSupervisionViolationCountByOffender";
	
	private static final String FIND_DISCIPLINARY_RESOLVED_COUNT_BY_OFFENDER =
			"findResolvedDisciplinaryViolationCountByOffender";
	
	private static final String FIND_SUPERVISION_RESOLVED_COUNT_BY_OFFENDER =
			"findResolvedSupervisionViolationCountByOffender";
		
	private static final String OFFENDER_PARAM_NAME =
			"offender";

	/** Instantiates an implementation of 
	 * ViolationProfileItemServiceHibernateImpl */
	public ViolationProfileItemServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/** {@inheritDoc} */
	@Override
	public ViolationProfileSummary findViolationSummaryByOffender(
			final Offender offender) {
		Long unresolvedCount = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_DISCIPLINARY_UNRESOLVED_COUNT_BY_OFFENDER)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		unresolvedCount += (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_SUPERVISION_UNRESOLVED_COUNT_BY_OFFENDER)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		Long resolvedCount = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_DISCIPLINARY_RESOLVED_COUNT_BY_OFFENDER)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		resolvedCount += (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_SUPERVISION_RESOLVED_COUNT_BY_OFFENDER)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult();
		
		ViolationProfileSummary violationProfileSummary =
				new ViolationProfileSummary(unresolvedCount, resolvedCount);
		
		return violationProfileSummary;
	}
}