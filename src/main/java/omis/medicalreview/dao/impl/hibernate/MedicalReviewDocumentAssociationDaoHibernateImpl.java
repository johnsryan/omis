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

import java.util.List;
import org.hibernate.SessionFactory;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.document.domain.Document;
import omis.medicalreview.dao.MedicalReviewDocumentAssociationDao;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;

/**
 * Medical Review Document Association DAO Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewDocumentAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<MedicalReviewDocumentAssociation>
		implements MedicalReviewDocumentAssociationDao {
	
	private static final String FIND_QUERY_NAME =
			"findMedicalReviewDocumentAssociation";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findMedicalReviewDocumentAssociationExcluding";
	
	private static final String FIND_BY_MEDICAL_REVIEW_QUERY_NAME =
			"findMedicalReviewDocumentAssociationsByMedicalReview";
	
	private static final String MEDICAL_REVIEW_PARAM_NAME = "medicalReview";
	
	private static final String DOCUMENT_PARAM_NAME = "document";
	
	private static final String MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_PARAM_NAME =
			"medicalReviewDocumentAssociation";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String entity name
	 */
	public MedicalReviewDocumentAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewDocumentAssociation find(
			final MedicalReview medicalReview, final Document document) {
		MedicalReviewDocumentAssociation association =
				(MedicalReviewDocumentAssociation) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReview)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.uniqueResult();
		
		return association;
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewDocumentAssociation findExcluding(
			final MedicalReview medicalReview, final Document document,
			final MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociationExcluding) {
		MedicalReviewDocumentAssociation association =
				(MedicalReviewDocumentAssociation) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReview)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.setParameter(MEDICAL_REVIEW_DOCUMENT_ASSOCIATION_PARAM_NAME,
						medicalReviewDocumentAssociationExcluding)
				.uniqueResult();
		
		return association;
	}

	/**{@inheritDoc} */
	@Override
	public List<MedicalReviewDocumentAssociation> findByMedicalReview(
			final MedicalReview medicalReview) {
		@SuppressWarnings("unchecked")
		List<MedicalReviewDocumentAssociation> associations =
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_MEDICAL_REVIEW_QUERY_NAME)
				.setParameter(MEDICAL_REVIEW_PARAM_NAME, medicalReview)
				.list();
		
		return associations;
	}

}
