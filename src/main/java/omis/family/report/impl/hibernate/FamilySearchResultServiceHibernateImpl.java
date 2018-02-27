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
package omis.family.report.impl.hibernate;

import java.util.List;
import org.hibernate.SessionFactory;
import omis.family.report.FamilySearchResult;
import omis.family.report.FamilySearchResultService;
import omis.offender.domain.Offender;
import omis.search.util.PersonRegexUtility;

/**
 * Family Search Result Service Hibernate Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class FamilySearchResultServiceHibernateImpl
		implements FamilySearchResultService {
	private static final String FIND_BY_FIRST_LAST_NAME_QUERY_NAME = 
			"findFamilySearchResultByFirstLast";

	private static final String FIND_BY_FULL_NAME_QUERY_NAME =
			"findFamilySearchResultByFirstMiddleLast";

	private static final String FIND_BY_LAST_NAME_QUERY_NAME =
			"findFamilySearchResultByLast";

	private static final String FIRST_NAME_PARAM_NAME = "first";

	private static final String MIDDLE_NAME_PARAM_NAME = "middle";

	private static final String LAST_NAME_PARAM_NAME = "last";

	private static final String NAME1_PARAM_NAME = "name1";

	private static final String NAME2_PARAM_NAME = "name2";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory - Session Factory
	 */
	public FamilySearchResultServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc} */
	@Override
	public List<FamilySearchResult>
		findFamilySearchResultsByUnspecifiedAndOffender(
			final String searchCriteria, final Offender offender) {
		List<FamilySearchResult> result;
		if (PersonRegexUtility.isFirstLast(searchCriteria)) {
			final String[] firstLast = searchCriteria.split("[\\s,]+");
			result = this.findPersonNamesByNameSearch(firstLast[0],
						firstLast[1], offender);
		} else if (PersonRegexUtility.isFirstMiddleLast(searchCriteria)) {
			final String[] firstMiddleLast = searchCriteria.split("[\\s,]+");
			result = this.findPersonNamesByNameSearch(
					firstMiddleLast[0], firstMiddleLast[1], firstMiddleLast[2],
					offender);
		} else if (PersonRegexUtility.isName(searchCriteria)) {
			result = this.findPersonNamesByLastName(searchCriteria, offender);
		} else {
			result = null;
		}

		return result;
	}
	
	/**
	 * @param searchCriteria
	 * @return
	 */
	private List<FamilySearchResult> findPersonNamesByLastName(
			final String name, final Offender offender) {
		@SuppressWarnings("unchecked")
		List<FamilySearchResult> result = this.sessionFactory
				.getCurrentSession().getNamedQuery(FIND_BY_LAST_NAME_QUERY_NAME)
				.setParameter(LAST_NAME_PARAM_NAME, name)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		
		return result;
	}
	
	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @return
	 */
	private List<FamilySearchResult> findPersonNamesByNameSearch(
			final String first, final String middle, final String last,
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<FamilySearchResult> result = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_BY_FULL_NAME_QUERY_NAME)
				.setParameter(FIRST_NAME_PARAM_NAME, first)
				.setParameter(MIDDLE_NAME_PARAM_NAME, middle)
				.setParameter(LAST_NAME_PARAM_NAME, last)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		
		return result;
	}
	
	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	private List<FamilySearchResult> findPersonNamesByNameSearch(
			final String name1, final String name2, final Offender offender) {
		@SuppressWarnings("unchecked")
		List<FamilySearchResult> result = this.sessionFactory
				.getCurrentSession().getNamedQuery(
						FIND_BY_FIRST_LAST_NAME_QUERY_NAME)
				.setParameter(NAME1_PARAM_NAME, name1)
				.setParameter(NAME2_PARAM_NAME, name2)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		
		return result;
	}
}