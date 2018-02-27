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
package omis.paroleboardcondition.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.paroleboardcondition.report.ParoleBoardAgreementProfileItemService;

/**
 * Hibernate implementation of parole board agreement profile item.
 *
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardAgreementProfileItemServiceHibernateImpl 
	implements ParoleBoardAgreementProfileItemService {
	
	private static final String 
		FIND_PAROLE_BOARD_AGREEMENT_COUNT_BY_OFFENDER_QUERY_NAME
			= "findParoleBoardAgreementCountByOffender";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private final SessionFactory sessionFactory;
	
	
	public ParoleBoardAgreementProfileItemServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Long findParoleBoardAgreementCountByOffender(
			final Offender offender) {
		return ((Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_PAROLE_BOARD_AGREEMENT_COUNT_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.uniqueResult());
	}

}
