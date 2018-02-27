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
package omis.offenseterm.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.offenseterm.report.OffenseTermProfileItemService;
import omis.person.domain.Person;

/** 
 * Service implementation for offense term profile related operations.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0 
 */
public class OffenseTermProfileItemServiceImpl 
	implements OffenseTermProfileItemService {

	private static final String FIND_OFFENSE_TERM_COUNT_BY_PERSON_QUERY_NAME = 
			"findCourtCaseCountByDefendant";
	
	private static final String 
			FIND_CURRENT_OFFENSE_TERM_COUNT_BY_PERSON_QUERY_NAME = 
					"findActiveSentenceCountByPerson";
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String DEFENDANT_PARAM_NAME = "defendant";
	
	private final SessionFactory sessionFactory;

	/**
	 * Constructor.
	 * 
	 * @param sessionFactory session factory
	 */
	public OffenseTermProfileItemServiceImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer findOffenseTermCountByPerson(Person person) {
		Long count = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(FIND_OFFENSE_TERM_COUNT_BY_PERSON_QUERY_NAME)
				.setParameter(DEFENDANT_PARAM_NAME, person)
				.setReadOnly(true)
				.uniqueResult();
		return count.intValue();
	}

	/** {@inheritDoc} */
	@Override
	public Integer findCurrentOffenseTermCountByPerson(Person person) {
		Long count = (Long) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						FIND_CURRENT_OFFENSE_TERM_COUNT_BY_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setReadOnly(true)
				.uniqueResult();
		return count.intValue();
	}
}