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
package omis.assessment.report;

import java.io.Serializable;

/**
 * Assessment Document Summary.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentDocumentSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Long assessmentDocumentAssociationId;
	
	private final Long documentId;
	
	private final String documentTitle;
	
	private final String updatedByFirstName;
	
	private final String updatedByLastName;

	/**
	 * @param assessmentDocumentAssociationId - Long
	 * @param documentId - Long
	 * @param documentTitle - String
	 * @param updatedByFirstName - String
	 * @param updatedByLastName - String
	 */
	public AssessmentDocumentSummary(
			final Long assessmentDocumentAssociationId,
			final Long documentId,
			final String documentTitle, final String updatedByFirstName,
			final String updatedByLastName) {
		this.assessmentDocumentAssociationId = assessmentDocumentAssociationId;
		this.documentId = documentId;
		this.documentTitle = documentTitle;
		this.updatedByFirstName = updatedByFirstName;
		this.updatedByLastName = updatedByLastName;
	}
	
	/**
	 * Returns the assessmentDocumentAssociationId.
	 * @return assessmentDocumentAssociationId - Long
	 */
	public Long getAssessmentDocumentAssociationId() {
		return this.assessmentDocumentAssociationId;
	}
	
	/**
	 * Returns the documentId.
	 * @return documentId - Long
	 */
	public Long getDocumentId() {
		return this.documentId;
	}

	/**
	 * Returns the documentTitle.
	 * @return documentTitle - String
	 */
	public String getDocumentTitle() {
		return this.documentTitle;
	}

	/**
	 * Returns the updatedByFirstName.
	 * @return updatedByFirstName - String
	 */
	public String getUpdatedByFirstName() {
		return this.updatedByFirstName;
	}

	/**
	 * Returns the updatedByLastName.
	 * @return updatedByLastName - String
	 */
	public String getUpdatedByLastName() {
		return this.updatedByLastName;
	}
}
