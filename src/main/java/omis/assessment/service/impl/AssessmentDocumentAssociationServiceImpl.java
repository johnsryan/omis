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
package omis.assessment.service.impl;

import java.util.Date;
import java.util.List;
import omis.assessment.domain.AssessmentDocumentAssociation;
import omis.assessment.service.AssessmentDocumentAssociationService;
import omis.assessment.service.delegate.AssessmentDocumentAssociationDelegate;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Document Association Service Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 8, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentDocumentAssociationServiceImpl
		implements AssessmentDocumentAssociationService {
	
	private final AssessmentDocumentAssociationDelegate
		assessmentDocumentAssociationDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	/**
	 * @param assessmentDocumentAssociationDelegate - Assessment Document
	 * Association Delegate
	 * @param documentDelegate - Document Delegate
	 * @param documentTagDelegate - Document Tag Delegate
	 */
	public AssessmentDocumentAssociationServiceImpl(
			final AssessmentDocumentAssociationDelegate
				assessmentDocumentAssociationDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate) {
		super();
		this.assessmentDocumentAssociationDelegate =
				assessmentDocumentAssociationDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentDocumentAssociation createAssessmentDocumentAssociation(
			final Document document, final Date date,
			final AdministeredQuestionnaire administeredQuestionnaire)
			throws DuplicateEntityFoundException {
		return this.assessmentDocumentAssociationDelegate.create(
				document, date, administeredQuestionnaire);
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentDocumentAssociation updateAssessmentDocumentAssociation(
			final AssessmentDocumentAssociation assessmentDocumentAssociation,
			final Document document, final Date date,
			final AdministeredQuestionnaire administeredQuestionnaire)
			throws DuplicateEntityFoundException {
		return this.assessmentDocumentAssociationDelegate.update(
				assessmentDocumentAssociation, document, date,
				administeredQuestionnaire);
	}

	/**{@inheritDoc} */
	@Override
	public void removeAssessmentDocumentAssociation(
			final AssessmentDocumentAssociation assessmentDocumentAssociation) {
		this.assessmentDocumentAssociationDelegate.remove(
				assessmentDocumentAssociation);
	}

	/**{@inheritDoc} */
	@Override
	public Document createDocument(final Date documentDate,
			final String filename, final String fileExtension,
			final String title)
					throws DuplicateEntityFoundException {
		return this.documentDelegate.create(documentDate, filename,
				fileExtension, title);
	}

	/**{@inheritDoc} */
	@Override
	public Document updateDocument(final Document document,
			final Date date, final String title)
					throws DuplicateEntityFoundException {
		return this.documentDelegate.update(document, title, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocument(final Document document) {
		this.documentDelegate.remove(document);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag createDocumentTag(
			final Document document, final String name)
					throws DuplicateEntityFoundException {
		return this.documentTagDelegate.tagDocument(document, name);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag updateDocumentTag(
			final DocumentTag documentTag, final String name)
					throws DuplicateEntityFoundException {
		return this.documentTagDelegate.update(documentTag, name);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocumentTag(final DocumentTag documentTag) {
		this.documentTagDelegate.removeTag(documentTag);
	}

	/**{@inheritDoc} */
	@Override
	public List<DocumentTag> findDocumentTagsByDocument(
			final Document document) {
		return this.documentTagDelegate.findByDocument(document);
	}

}
