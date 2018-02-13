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
package omis.medicalreview.service.impl;

import java.util.Date;
import java.util.List;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.medicalreview.domain.MedicalHealthClassification;
import omis.medicalreview.domain.MedicalReview;
import omis.medicalreview.domain.MedicalReviewDocumentAssociation;
import omis.medicalreview.domain.MedicalReviewNote;
import omis.medicalreview.service.MedicalReviewService;
import omis.medicalreview.service.delegate.MedicalHealthClassificationDelegate;
import omis.medicalreview.service.delegate.MedicalReviewDelegate;
import omis.medicalreview.service.delegate.MedicalReviewDocumentAssociationDelegate;
import omis.medicalreview.service.delegate.MedicalReviewNoteDelegate;
import omis.offender.domain.Offender;

/**
 * Medical Review Service Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalReviewServiceImpl implements MedicalReviewService {
	
	private final MedicalReviewDelegate medicalReviewDelegate;
	
	private final MedicalReviewNoteDelegate medicalReviewNoteDelegate;
	
	private final MedicalReviewDocumentAssociationDelegate
			medicalReviewDocumentAssociationDelegate;
	
	private final MedicalHealthClassificationDelegate
			medicalHealthClassificationDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	/**
	 * @param medicalReviewDelegate - Medical Review Delegate
	 * @param medicalReviewNoteDelegate - Medical Review Note Delegate
	 * @param medicalReviewDocumentAssociationDelegate - Medical Review
	 * Document Association Delegate
	 * @param medicalHealthClassificationDelegate - Medical Health
	 * Classification Delegate
	 * @param documentDelegate - Document Delegate
	 * @param documentTagDelegate - Document Tag Delegate
	 */
	public MedicalReviewServiceImpl(
			final MedicalReviewDelegate medicalReviewDelegate,
			final MedicalReviewNoteDelegate medicalReviewNoteDelegate,
			final MedicalReviewDocumentAssociationDelegate
				medicalReviewDocumentAssociationDelegate,
			final MedicalHealthClassificationDelegate
				medicalHealthClassificationDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate) {
		super();
		this.medicalReviewDelegate = medicalReviewDelegate;
		this.medicalReviewNoteDelegate = medicalReviewNoteDelegate;
		this.medicalReviewDocumentAssociationDelegate =
				medicalReviewDocumentAssociationDelegate;
		this.medicalHealthClassificationDelegate =
				medicalHealthClassificationDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReview createMedicalReview(final Offender offender,
			final Date date, final String text,
			final MedicalHealthClassification healthClassification)
					throws DuplicateEntityFoundException {
		return this.medicalReviewDelegate.create(offender, date, text,
				healthClassification);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReview updateMedicalReview(final MedicalReview medicalReview,
			final Offender offender, final Date date, final String text,
			final MedicalHealthClassification healthClassification)
					throws DuplicateEntityFoundException {
		return this.medicalReviewDelegate.update(medicalReview, offender, date,
				text, healthClassification);
	}

	/**{@inheritDoc} */
	@Override
	public void removeMedicalReview(final MedicalReview medicalReview) {
		this.medicalReviewDelegate.remove(medicalReview);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewNote createMedicalReviewNote(
			final MedicalReview medicalReview,
			final String description, final Date date)
					throws DuplicateEntityFoundException {
		return this.medicalReviewNoteDelegate.create(
				medicalReview, description, date);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewNote updateMedicalReviewNote(
			final MedicalReviewNote medicalReviewNote,
			final MedicalReview medicalReview,
			final String description, final Date date)
					throws DuplicateEntityFoundException {
		return this.medicalReviewNoteDelegate.update(medicalReviewNote,
				medicalReview, description, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeMedicalReviewNote(
			final MedicalReviewNote medicalReviewNote) {
		this.medicalReviewNoteDelegate.remove(medicalReviewNote);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewDocumentAssociation
		createMedicalReviewDocumentAssociation(
			final MedicalReview medicalReview, final Document document)
					throws DuplicateEntityFoundException {
		return this.medicalReviewDocumentAssociationDelegate.create(
				medicalReview, document);
	}

	/**{@inheritDoc} */
	@Override
	public MedicalReviewDocumentAssociation
		updateMedicalReviewDocumentAssociation(
			final MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociation,
			final MedicalReview medicalReview, final Document document)
					throws DuplicateEntityFoundException {
		return this.medicalReviewDocumentAssociationDelegate.update(
				medicalReviewDocumentAssociation, medicalReview, document);
	}

	/**{@inheritDoc} */
	@Override
	public void removeMedicalReviewDocumentAssociation(
			final MedicalReviewDocumentAssociation
				medicalReviewDocumentAssociation) {
		this.medicalReviewDocumentAssociationDelegate.remove(
				medicalReviewDocumentAssociation);
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

	/**{@inheritDoc} */
	@Override
	public List<MedicalReviewNote> findMedicalReviewNotesByMedicalReview(
			final MedicalReview medicalReview) {
		return this.medicalReviewNoteDelegate.findByMedicalReview(
				medicalReview);
	}

	/**{@inheritDoc} */
	@Override
	public List<MedicalReviewDocumentAssociation>
		findReviewDocumentAssociationsByMedicalReview(
				final MedicalReview medicalReview) {
		return this.medicalReviewDocumentAssociationDelegate
				.findByMedicalReview(medicalReview);
	}

	/**{@inheritDoc} */
	@Override
	public List<MedicalHealthClassification>
		findMedicalHealthClassifications() {
		return this.medicalHealthClassificationDelegate.findAll();
	}
}
