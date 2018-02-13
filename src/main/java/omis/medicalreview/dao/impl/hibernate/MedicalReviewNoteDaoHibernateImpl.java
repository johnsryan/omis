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
import java.util.List;
import org.hibernate.SessionFactory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.medicalreview.dao.MedicalReviewNoteDao;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewNote;

/**
 * Medical Review Note DAO Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewNoteDaoHibernateImpl
		extends GenericHibernateDaoImpl<MedicalReviewNote>
		implements MedicalReviewNoteDao {
	
	private static final String FIND_QUERY_NAME = "findMedicalReviewNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findMedicalReviewNoteExcluding";
	
	private static final String FIND_BY_MEDICAL_REVIEW_QUERY_NAME =
			"findMedicalReviewNotesByMedicalReview";
	
	private static final String MEDICAL_REVIEW_PARAM_NAME = "medicalReview";
	
	private static final String DESCRIPTION_PARAM_NAME = "description";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String MEDICAL_REVIEW_NOTE_PARAM_NAME =
			"medicalReviewNote";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String entity name
	 */
	public MedicalReviewNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewNote find(final MedicalReview medicalReview,
			final String description, final Date date) {
		MedicalReviewNote medicalReviewNote = (MedicalReviewNote)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReview)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setTimestamp(DATE_PARAM_NAME, date)
				.uniqueResult();
		
		return medicalReviewNote;
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewNote findExcluding(final MedicalReview medicalReview,
			final String description, final Date date,
			final MedicalReviewNote medicalReviewNoteExcluding) {
		MedicalReviewNote medicalReviewNote = (MedicalReviewNote)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReview)
				.setParameter(DESCRIPTION_PARAM_NAME, description)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(MEDICAL_REVIEW_NOTE_PARAM_NAME,
						medicalReviewNoteExcluding)
				.uniqueResult();
		
		return medicalReviewNote;
	}

	/**{@inheritDoc} */
	@Override
	public List<MedicalReviewNote> findByMedicalReview(
			final MedicalReview medicalReview) {
		@SuppressWarnings("unchecked")
		List<MedicalReviewNote> notes = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_MEDICAL_REVIEW_QUERY_NAME)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReview)
				.list();
		
		return notes;
	}

}
