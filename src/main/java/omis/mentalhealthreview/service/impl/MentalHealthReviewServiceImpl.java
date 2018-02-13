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
package omis.mentalhealthreview.service.impl;

import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.mentalhealthreview.domain.MentalHealthNote;
import omis.mentalhealthreview.domain.MentalHealthReview;
import omis.mentalhealthreview.domain.MentalHealthReviewDocumentAssociation;
import omis.mentalhealthreview.service.MentalHealthReviewService;
import omis.mentalhealthreview.service.delegate.MentalHealthNoteDelegate;
import omis.mentalhealthreview.service.delegate.MentalHealthReviewDelegate;
import omis.mentalhealthreview.service.delegate.MentalHealthReviewDocumentAssociationDelegate;

/**
 * Implementation for mental health review service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 1, 2018)
 * @since OMIS 3.0
 */
public class MentalHealthReviewServiceImpl 
		implements MentalHealthReviewService {

	/* Delegates. */
	
	private final MentalHealthReviewDelegate mentalHealthReviewDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	private final MentalHealthReviewDocumentAssociationDelegate 
			mentalHealthReviewDocumentAssociationDelegate;
	
	private final MentalHealthNoteDelegate mentalHealthNoteDelegate;
	
	public MentalHealthReviewServiceImpl(
			final MentalHealthReviewDelegate mentalHealthReviewDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate,
			final MentalHealthReviewDocumentAssociationDelegate
					mentalHealthReviewDocumentAssociationDelegate,
			final MentalHealthNoteDelegate mentalHealthNoteDelegate) {
		this.mentalHealthReviewDelegate = mentalHealthReviewDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
		this.mentalHealthReviewDocumentAssociationDelegate = 
				mentalHealthReviewDocumentAssociationDelegate;
		this.mentalHealthNoteDelegate = mentalHealthNoteDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public MentalHealthReview createMentalHealthReview(
			final Date date, final String text, final Offender offender) 
					throws DuplicateEntityFoundException {
		return this.mentalHealthReviewDelegate.create(date, text, offender);
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthReview updateMentalHealthReview(
			final MentalHealthReview mentalHealthReview, 
			final Date date, final String text) 
					throws DuplicateEntityFoundException {
		return this.mentalHealthReviewDelegate.update(mentalHealthReview, date, 
				text, mentalHealthReview.getOffender());
	}

	/** {@inheritDoc} */
	@Override
	public void removeMentalHealthReview(
			final MentalHealthReview mentalHealthReview) {
		this.mentalHealthReviewDelegate.remove(mentalHealthReview);
	}

	/** {@inheritDoc} */
	@Override
	public Document createDocument(final Date date, final String title, 
			final String filename, final String fileExtension) 
					throws DuplicateEntityFoundException {
		return this.documentDelegate.create(date, filename, fileExtension, 
				title);
	}

	/** {@inheritDoc} */
	@Override
	public Document updateDocument(final Document document, final Date date, 
			final String title) throws DuplicateEntityFoundException {
		return this.documentDelegate.update(document, title, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeDocument(final Document document) {
		this.documentDelegate.remove(document);
	}

	/** {@inheritDoc} */
	@Override
	public DocumentTag createDocumentTag(final String name, 
			final Document document) throws DuplicateEntityFoundException {
		return this.documentTagDelegate.tagDocument(document, name);
	}

	/** {@inheritDoc} */
	@Override
	public DocumentTag updateDocumentTag(final DocumentTag documentTag, 
			final String name) throws DuplicateEntityFoundException {
		return this.documentTagDelegate.update(documentTag, name);
	}

	/** {@inheritDoc} */
	@Override
	public void removeDocumentTag(final DocumentTag documentTag) {
		this.documentTagDelegate.removeTag(documentTag);
	}

	/** {@inheritDoc} */
	@Override
	public List<DocumentTag> findDocumentTagsByDocument(
			final Document document) {
		return this.documentTagDelegate.findByDocument(document);
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthReviewDocumentAssociation 
			createMentalHealthReviewDocumentAssociation(final Document document,
					final MentalHealthReview mentalHealthReview) 
							throws DuplicateEntityFoundException {
		return this.mentalHealthReviewDocumentAssociationDelegate.create(
				document, mentalHealthReview);
	}

	/** {@inheritDoc} */
	@Override
	public void removeMentalHealthReviewDocumentAssociation(
			final MentalHealthReviewDocumentAssociation 
					mentalHealthReviewDocumentAssociation) {
		this.mentalHealthReviewDocumentAssociationDelegate.remove(
				mentalHealthReviewDocumentAssociation);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<MentalHealthReviewDocumentAssociation> 
			findMentalHealthReviewDocumentAssociationsByMentalHealthReview(
					final MentalHealthReview mentalHealthReview) {
		return this.mentalHealthReviewDocumentAssociationDelegate
				.findByMentalHealthReview(mentalHealthReview);
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthNote createMentalHealthReviewNote(
			final MentalHealthReview mentalHealthReview, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.mentalHealthNoteDelegate.create(mentalHealthReview, date, 
				description);
	}

	/** {@inheritDoc} */
	@Override
	public MentalHealthNote updateMentalHealthReviewNote(
			final MentalHealthNote mentalHealthNote, final String description,
			final Date date) throws DuplicateEntityFoundException {
		return this.mentalHealthNoteDelegate.update(mentalHealthNote, 
				mentalHealthNote.getMentalHealthReview(), date, description);
	}

	/** {@inheritDoc} */
	@Override
	public void removeMentalHealthReviewNote(
			final MentalHealthNote mentalHealthNote) {
		this.mentalHealthNoteDelegate.remove(mentalHealthNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<MentalHealthNote> 
			findMentalHealthReviewNotesByMentalHealthReview(
					final MentalHealthReview mentalHealthReview) {
		return this.mentalHealthNoteDelegate.findByMentalHealthReview(
				mentalHealthReview);
	}
}