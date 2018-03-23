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

import omis.assessment.dao.RatingCategoryDao;
import omis.assessment.domain.RatingCategory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.questionnaire.domain.QuestionnaireType;

/**
 * Hibernate implementation of the rating category data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 26, 2018)
 * @since OMIS 3.0
 */
public class RatingCategoryDaoHibernateImpl 
		extends GenericHibernateDaoImpl<RatingCategory>
		implements RatingCategoryDao {

	/* Queries. */
	
	private final static String FIND_QUERY_NAME = "findRatingCategory";
	
	private final static String FIND_EXCLUDING_QUERY_NAME = 
			"findRatingCategoryExcluding";
	
	private final static String FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME = 
			"findRatingCategoryByAdministeredQuestionnaire";
	
	/* Parameters. */
	
	private final static String DESCRIPTION_PARAM_NAME = "description";
	
	private final static String VALID_PARAM_NAME = "valid";
	
	private final static String QUESTIONNAIRE_TYPE_PARAM_NAME = 
			"questionnaireType";
	
	private final static String EXCLUDED_RATING_CATEGORY_PARAM_NAME = 
			"excludedRatingCategory";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * rating category.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public RatingCategoryDaoHibernateImpl(final SessionFactory sessionFactory, 
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public RatingCategory find(final String description, final Boolean valid) {
		RatingCategory ratingCategory = (RatingCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(VALID_PARAM_NAME, valid)
				.uniqueResult();
		return ratingCategory;
	}

	/** {@inheritDoc} */
	@Override
	public RatingCategory findExcluding(final String description, 
			final Boolean valid, final RatingCategory excludedRatingCategory) {
		RatingCategory ratingCategory = (RatingCategory) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setParameter(VALID_PARAM_NAME, valid)
				.setParameter(EXCLUDED_RATING_CATEGORY_PARAM_NAME, 
						excludedRatingCategory)
				.uniqueResult();
		return ratingCategory;
	}

	/** {@inheritDoc} */
	@Override
	public List<RatingCategory> findByQuestionnaireType(
			final QuestionnaireType questionnaireType) {
		@SuppressWarnings("unchecked")
		List<RatingCategory> categories = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(QUESTIONNAIRE_TYPE_PARAM_NAME, questionnaireType)
				.list();
		return categories;
	}
}