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
package omis.search.report.service.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.search.report.UserSearchResult;
import omis.search.report.service.UserPersonSearchReportService;
import omis.search.util.PersonRegexUtility;

/** 
 * User search report service.
 * 
 * @author Ryan Johns
 * @author Josh Divine
 * @version 0.1.1 (Feb 14, 2018)
 * @since OMIS 3.0 
 */
public class UserPersonSearchReportServiceHibernateImpl
		extends AbstractPersonSearchReportServiceHibernateImpl<UserSearchResult>
		implements UserPersonSearchReportService {

	private static final String FIND_BY_FIRST_LAST_NAME_SEARCH_QUERY_NAME =
			"findUsersNamesByFirstLastNameSearch";

	private static final String[] FIND_BY_FIRST_LAST_NAME_SEARCH_PARAMS =
			{"name1", "name2"};

	private static final String
			FIND_BY_FIRST_MIDDLE_LAST_NAME_SEARCH_QUERY_NAME =
					"findUsersNameByFirstMiddleLastNameSearch";

	private static final String[] FIND_BY_FIRST_MIDDLE_LAST_NAME_SEARCH_PARAMS =
			{"first", "middle", "last"};

	private static final String FIND_BY_NAME_SEARCH_CRITERIA =
			"findUserNamesByLastNameSearchCriteria";

	private static final String FIND_BY_NAME_SEARCH_CRITERIA_PARAM = "name";

	private static final String FIND_BY_USERNAME_SEARCH_QUERY =
			"findUserNamesByUsernameSearchCriteria";

	private static final String FIND_BY_USERNAME_SEARCH_QUERY_PARAM =
			"username";

	private static final String FIND_PERSON_SEARCH_BY_ID = "findUserSearchById";

	private static final String FIND_PERSON_SEARCH_BY_ID_PARAM = "id";

	/** constructor.
	 * @param sessionFactory session factory. */
	public UserPersonSearchReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/** {@inheritDoc} */
	@Override
	public UserSearchResult findById(final Long id) {
		return (UserSearchResult) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_PERSON_SEARCH_BY_ID)
				.setParameter(FIND_PERSON_SEARCH_BY_ID_PARAM, id)
				.setReadOnly(true)
				.uniqueResult();
	}

	@Override
	public String getFindByFirstLastNameSearchQuery() {
		return FIND_BY_FIRST_LAST_NAME_SEARCH_QUERY_NAME;
	}

	@Override
	public String[] getFindByFirstLastNameSearchParams() {
		return FIND_BY_FIRST_LAST_NAME_SEARCH_PARAMS;
	}

	@Override
	public String getFindByFirstMiddleLastNameSearchQuery() {
		return FIND_BY_FIRST_MIDDLE_LAST_NAME_SEARCH_QUERY_NAME;
	}

	@Override
	public String[] getFindByFirstMiddleLastNameSearchParams() {
		return FIND_BY_FIRST_MIDDLE_LAST_NAME_SEARCH_PARAMS;
	}

	@Override
	public String getFindPersonNameByNameSearchQuery() {
		return FIND_BY_NAME_SEARCH_CRITERIA;
	}

	@Override
	public String getFindPersonNameByNameSearchParam() {
		return FIND_BY_NAME_SEARCH_CRITERIA_PARAM;
	}

	/** {@inheritDoc} */
	@Override
	public List<UserSearchResult> findPersonNamesByUsernameSearch(
			final String username) {
		@SuppressWarnings("unchecked")
		final
		List<UserSearchResult> result = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_USERNAME_SEARCH_QUERY)
				.setParameter(FIND_BY_USERNAME_SEARCH_QUERY_PARAM, username)
				.setReadOnly(true)
				.list();
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<UserSearchResult> findUserPersonNamesByUnspecified(
			final String searchCriteria) {
		List<UserSearchResult> result = new ArrayList<UserSearchResult>();

		if (PersonRegexUtility.isUserName(searchCriteria)) {
			result = this.findPersonNamesByUsernameSearch(searchCriteria);
		} else {
			result = super.findPersonNamesByUnspecified(searchCriteria);
		}
		return result;
	}
}