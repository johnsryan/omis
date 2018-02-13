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
package omis.parolereview.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.document.domain.Document;
import omis.parolereview.dao.ParoleReviewDocumentAssociationDao;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;

/**
 * Hibernate implementation of the parole review document association data 
 * access object.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewDocumentAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<ParoleReviewDocumentAssociation>
		implements ParoleReviewDocumentAssociationDao {


	/* Queries. */
	
	private static final String FIND_QUERY_NAME = 
			"findParoleReviewDocumentAssociation";
	
	private static final String FIND_BY_PAROLE_REVIEW_QUERY_NAME = 
			"findParoleReviewDocumentAssociationsByParoleReview";
	
	/* Parameters. */
	
	private static final String DOCUMENT_PARAM_NAME = "document";
	
	private static final String PAROLE_REVIEW_PARAM_NAME = 
			"paroleReview";
	
	
	/** Instantiates a hibernate implementation of the data access object for 
	 *  parole review document association.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public ParoleReviewDocumentAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
		// TODO Auto-generated constructor stub
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReviewDocumentAssociation find(final Document document, 
			final ParoleReview paroleReview) {
		ParoleReviewDocumentAssociation documentAssociation = 
				(ParoleReviewDocumentAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.setParameter(PAROLE_REVIEW_PARAM_NAME, paroleReview)
				.uniqueResult();
		return documentAssociation;
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleReviewDocumentAssociation> findByParoleReview(
			final ParoleReview paroleReview) {
		@SuppressWarnings("unchecked")
		List<ParoleReviewDocumentAssociation> documentAssociations = this
		.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_PAROLE_REVIEW_QUERY_NAME)
				.setParameter(PAROLE_REVIEW_PARAM_NAME, paroleReview)
				.list();
		return documentAssociations;
	}
}