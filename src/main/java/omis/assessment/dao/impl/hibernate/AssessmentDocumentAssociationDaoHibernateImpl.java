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
import omis.assessment.dao.AssessmentDocumentAssociationDao;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.document.domain.Document;
import omis.questionnaire.domain.AdministeredQuestionnaire;
import org.hibernate.SessionFactory;

/**
 * Assessment Document Association DAO Hibernate Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentDocumentAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<AssessmentDocumentAssociation>
		implements AssessmentDocumentAssociationDao {
	
	private static final String FIND_QUERY_NAME =
			"findAssessmentDocumentAssociation";
	
	private static final String FIND_EXCLUDING_QUERY_NAME =
			"findAssessmentDocumentAssociationExcluding";
	
	private static final String FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME =
			"findAssessmentDocumentAssociationsByAdministeredQuestionnaire";
	
	private static final String DOCUMENT_PARAM_NAME = "document";
	
	private static final String ADMINISTERED_QUESTIONNAIRE_PARAM_NAME =
			"administeredQuestionnaire";
	
	private static final String ASSESSMENT_DOCUMENT_ASSOCIATION_PARAM_NAME =
			"assessmentDocumentAssociation";
	
	/**
	 * @param sessionFactory - Session Factory
	 * @param entityName - String Entity Name
	 */
	public AssessmentDocumentAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	/**{@inheritDoc} */
	@Override
	public AssessmentDocumentAssociation find(final Document document) {
		AssessmentDocumentAssociation assessmentDocumentAssociation =
				(AssessmentDocumentAssociation) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.uniqueResult();
		
		return assessmentDocumentAssociation;
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentDocumentAssociation findExcluding(final Document document,
			final AssessmentDocumentAssociation
				assessmentDocumentAssociationExcluded) {
		AssessmentDocumentAssociation assessmentDocumentAssociation =
				(AssessmentDocumentAssociation) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(DOCUMENT_PARAM_NAME, document)
				.setParameter(ASSESSMENT_DOCUMENT_ASSOCIATION_PARAM_NAME,
						assessmentDocumentAssociationExcluded)
				.uniqueResult();
		
		return assessmentDocumentAssociation;
	}
	
	/**{@inheritDoc} */
	@Override
	public List<AssessmentDocumentAssociation> findByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		@SuppressWarnings("unchecked")
		List<AssessmentDocumentAssociation> associations =
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_ADMINISTERED_QUESTIONNAIRE_QUERY_NAME)
				.setParameter(ADMINISTERED_QUESTIONNAIRE_PARAM_NAME,
						administeredQuestionnaire)
				.list();
		
		return associations;
	}
}
