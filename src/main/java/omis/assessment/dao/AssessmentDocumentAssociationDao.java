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
package omis.assessment.dao;

import java.util.List;

import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.dao.GenericDao;
import omis.document.domain.Document;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Document Association Data Access Object.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public interface AssessmentDocumentAssociationDao
		extends GenericDao<AssessmentDocumentAssociation> {
	
	/**
	 * Returns an Assessment Document Association with the specified properties.
	 * 
	 * @param document - Document
	 * @return Assessment Document Association with the specified properties.
	 */
	AssessmentDocumentAssociation find(Document document);
	
	/**
	 * Returns an Assessment Document Association with the specified properties
	 * excluding the specified Assessment Document Association.
	 * 
	 * @param document - Document
	 * @param assessmentDocumentAssociationExcluded - Assessment Document
	 * Association to exclude from search
	 * @return Assessment Document Association with the specified properties
	 * excluding the specified Assessment Document Association.
	 */
	AssessmentDocumentAssociation findExcluding(Document document,
			AssessmentDocumentAssociation
				assessmentDocumentAssociationExcluded);
	
	/**
	 * Returns a list of Assessment Document Associations by the specified
	 * Administered Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List of Assessment Document Associations by the specified
	 * Administered Questionnaire.
	 */
	List<AssessmentDocumentAssociation> findByAdministeredQuestionnaire(
			AdministeredQuestionnaire administeredQuestionnaire);
	
}
