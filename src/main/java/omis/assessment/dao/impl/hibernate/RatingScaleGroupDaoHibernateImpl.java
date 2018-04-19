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
package omis.assessment.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.assessment.dao.RatingScaleGroupDao;
import omis.assessment.domain.RatingCategory;
import omis.assessment.domain.RatingScaleGroup;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Hibernate implementation of the rating scale group data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 5, 2018)
 * @since OMIS 3.0
 */
public class RatingScaleGroupDaoHibernateImpl 
		extends GenericHibernateDaoImpl<RatingScaleGroup>
		implements RatingScaleGroupDao {
	
	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findRatingScaleGroup";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findRatingScaleGroupExcluding";
	private final static String 
			FIND_BY_ADMINISTERED_QUESTIONNAIRE_AND_RATING_CATEGORY_QUERY_NAME = 
			"findRatingScaleGroupsByAdministeredQuestionnaireAndRatingCategory";
	
	/* Parameters. */
	
	private final static String NAME_PARAM_NAME = "name";
	
	private final static String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME = 
			"administeredQuestionnaire";
	
	private final static String RATING_CATEGORY_PARAM_NAME = "ratingCategory";
	
	private final static String EXCLUDED_RATING_CATEGORY_PARAM_NAME = 
			"excludedRatingScaleGroup";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * rating scale group.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public RatingScaleGroupDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public RatingScaleGroup find(final String name) {
		RatingScaleGroup ratingScaleGroup = (RatingScaleGroup) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return ratingScaleGroup;
	}

	/** {@inheritDoc} */
	@Override
	public RatingScaleGroup findExcluding(final String name, 
			final RatingScaleGroup excludedRatingScaleGroup) {
		RatingScaleGroup ratingScaleGroup = (RatingScaleGroup) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.setParameter(EXCLUDED_RATING_CATEGORY_PARAM_NAME, 
						excludedRatingScaleGroup)
				.uniqueResult();
		return ratingScaleGroup;
	}

	/** {@inheritDoc} */
	@Override
	public List<RatingScaleGroup> 
			findByAdministeredQuestionnaireAndRatingCategory(
					final AdministeredQuestionnaire administeredQuestionnaire, 
					final RatingCategory ratingCategory) {
		@SuppressWarnings("unchecked")
		List<RatingScaleGroup> ratingScaleGroups = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_BY_ADMINISTERED_QUESTIONNAIRE_AND_RATING_CATEGORY_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME, 
						administeredQuestionnaire)
				.setParameter(RATING_CATEGORY_PARAM_NAME, ratingCategory)
				.list();
		return ratingScaleGroups;
	}
}