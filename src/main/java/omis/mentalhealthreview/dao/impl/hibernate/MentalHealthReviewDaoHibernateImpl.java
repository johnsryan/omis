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
package omis.mentalhealthreview.dao.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.mentalhealthreview.dao.MentalHealthReviewDao;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.offender.domain.Offender;

/**
 * Hibernate implementation of the mental health review data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewDaoHibernateImpl 
		extends GenericHibernateDaoImpl<MentalHealthReview>
		implements MentalHealthReviewDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findMentalHealthReview";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findMentalHealthReviewExcluding";
	
	/* Parameters. */
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String TEXT_PARAM_NAME = "text";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String EXCLUDED_PARAM_NAME = 
			"excludedMentalHealthReview";
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  mental health review.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public MentalHealthReviewDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthReview find(final Date date, final String text, 
			final Offender offender) {
		MentalHealthReview mentalHealthReview = (MentalHealthReview) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(TEXT_PARAM_NAME, text)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.uniqueResult();
		return mentalHealthReview;
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthReview findExcluding(
			final Date date, final String text, final Offender offender,
			final MentalHealthReview excludedMentalHealthReview) {
		MentalHealthReview mentalHealthReview = (MentalHealthReview) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(DATE_PARAM_NAME, date)
				.setParameter(TEXT_PARAM_NAME, text)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(EXCLUDED_PARAM_NAME, excludedMentalHealthReview)
				.uniqueResult();
		return mentalHealthReview;
	}
}