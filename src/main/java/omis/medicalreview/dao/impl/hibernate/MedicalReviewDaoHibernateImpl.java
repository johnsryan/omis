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
package omis.medicalreview.dao.impl.hibernate;

import java.util.Date;
import org.hibernate.SessionFactory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.medicalreview.dao.MedicalReviewDao;
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.offender.domain.Offender;

/**
 * Medical Review DAO Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewDaoHibernateImpl extends
		GenericHibernateDaoImpl<MedicalReview> implements MedicalReviewDao {
	
	private static final String FIND_QUERY_NAME = "findMedicalReview";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findMedicalReviewExcluding";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String HEALTH_CLASSIFICATION_PARAM_NAME =
			"healthClassification";
	
	private static final String MEDICAL_REVIEW_PARAM_NAME = "medicalReview";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String entity name
	 */
	public MedicalReviewDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/**{@inheritDoc} */
	@Override
	public MedicalReview find(final Offender offender, final Date date,
			final MedicalHealthClassification healthClassification) {
		MedicalReview medicalReview = (MedicalReview) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(HEALTH_CLASSIFICATION_PARAM_NAME,
						healthClassification)
				.uniqueResult();
		
		return medicalReview;
	}
	
	/**{@inheritDoc} */
	@Override
	public MedicalReview findExcluding(final Offender offender, final Date date,
			final MedicalHealthClassification healthClassification,
			final MedicalReview medicalReviewExcluding) {
		MedicalReview medicalReview = (MedicalReview) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(HEALTH_CLASSIFICATION_PARAM_NAME,
						healthClassification)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReviewExcluding)
				.uniqueResult();
		
		return medicalReview;
	}
	
}
