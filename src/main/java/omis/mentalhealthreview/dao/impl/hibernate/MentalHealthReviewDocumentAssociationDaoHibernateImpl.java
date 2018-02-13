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

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.document.domain.Document;
import omis.mentalhealthreview.dao.MentalHealthReviewDocumentAssociationDao;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;

/**
 * Hibernate implementation of the mental health review document association 
 * data access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewDocumentAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<MentalHealthReviewDocumentAssociation>
		implements MentalHealthReviewDocumentAssociationDao {


	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findMentalHealthReviewDocumentAssociation";
	
	private static final String FIND_BY_MENTAL_HEALTH_REVIEW_QUERY_NAME = 
			"findMentalHealthReviewDocumentAssociationsByMentalHealthReview";
	
	/* Parameters. */
	
	private static final String DOCUMENT_PARAM_NAME = "document";
	
	private static final String MENTAL_HEALTH_REVIEW_PARAM_NAME = 
			"mentalHealthReview";
	
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  mental health review document association.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public MentalHealthReviewDocumentAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
		// TODO Auto-generated constructor stub
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthReviewDocumentAssociation find(final Document document, 
			final MentalHealthReview mentalHealthReview) {
		MentalHealthReviewDocumentAssociation documentAssociation = 
				(MentalHealthReviewDocumentAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.setParameter(MENTAL_HEALTH_REVIEW_PARAM_NAME, 
						mentalHealthReview)
				.uniqueResult();
		return documentAssociation;
	}

	/** {@inheritDoc} */
	@Override
	public List<MentalHealthReviewDocumentAssociation> findByMentalHealthReview(
			final MentalHealthReview mentalHealthReview) {
		@SuppressWarnings("unchecked")
		List<MentalHealthReviewDocumentAssociation> documentAssociations = this
		.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_MENTAL_HEALTH_REVIEW_QUERY_NAME)
				.setParameter(MENTAL_HEALTH_REVIEW_PARAM_NAME, 
						mentalHealthReview)
				.list();
		return documentAssociations;
	}
}