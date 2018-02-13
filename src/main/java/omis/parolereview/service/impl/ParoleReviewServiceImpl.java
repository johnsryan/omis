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
package omis.parolereview.service.impl;

import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.parolereview.domain.ParoleEndorsementCategory;
import omis.parolereview.domain.ParoleReview;
import omis.parolereview.domain.ParoleReviewDocumentAssociation;
import omis.parolereview.domain.ParoleReviewNote;
import omis.parolereview.domain.StaffRoleCategory;
import omis.parolereview.service.ParoleReviewService;
import omis.parolereview.service.delegate.ParoleEndorsementCategoryDelegate;
import omis.parolereview.service.delegate.ParoleReviewDelegate;
import omis.parolereview.service.delegate.ParoleReviewDocumentAssociationDelegate;
import omis.parolereview.service.delegate.ParoleReviewNoteDelegate;
import omis.parolereview.service.delegate.StaffRoleCategoryDelegate;
import omis.staff.domain.StaffAssignment;

/**
 * Implementation for parole review service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 29, 2018)
 * @since OMIS 3.0
 */
public class ParoleReviewServiceImpl implements ParoleReviewService {

	/* Delegates. */
	
	private final ParoleReviewDelegate paroleReviewDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	private final ParoleReviewDocumentAssociationDelegate 
			paroleReviewDocumentAssociationDelegate;
	
	private final ParoleReviewNoteDelegate paroleReviewNoteDelegate;
	
	private final ParoleEndorsementCategoryDelegate 
			paroleEndorsementCategoryDelegate;
	
	private final StaffRoleCategoryDelegate staffRoleCategoryDelegate;
	
	/**
	 * Instantiates a parole review service implementation with the specified 
	 * delegates.
	 * 
	 * @param paroleReviewDelegate parole review delegate
	 * @param documentDelegate document delegate
	 * @param documentTagDelegate document tag delegate
	 * @param paroleReviewDocumentAssociationDelegate parole review document 
	 * association delegate
	 * @param paroleReviewNoteDelegate parole review note delegate
	 * @param paroleEndorsementCategoryDelegate parole endorsement category 
	 * delegate
	 * @param staffRoleCategoryDelegate staff role category delegate
	 */
	public ParoleReviewServiceImpl(
			final ParoleReviewDelegate paroleReviewDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate,
			final ParoleReviewDocumentAssociationDelegate
					paroleReviewDocumentAssociationDelegate,
			final ParoleReviewNoteDelegate paroleReviewNoteDelegate,
			final ParoleEndorsementCategoryDelegate 
					paroleEndorsementCategoryDelegate,
			final StaffRoleCategoryDelegate staffRoleCategoryDelegate) {
		this.paroleReviewDelegate = paroleReviewDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
		this.paroleReviewDocumentAssociationDelegate = 
				paroleReviewDocumentAssociationDelegate;
		this.paroleReviewNoteDelegate = paroleReviewNoteDelegate;
		this.paroleEndorsementCategoryDelegate = 
				paroleEndorsementCategoryDelegate;
		this.staffRoleCategoryDelegate = staffRoleCategoryDelegate;
	}
	
	/**{@inheritDoc} */
	@Override
	public ParoleReview createParoleReview(
			final StaffAssignment staffAssignment, final Date date, 
			final String text, final Offender offender,
			final ParoleEndorsementCategory endorsement,
			final StaffRoleCategory staffRole) 
					throws DuplicateEntityFoundException {
		return this.paroleReviewDelegate.create(staffAssignment, date, 
				text, offender, endorsement, staffRole);
	}

	/**{@inheritDoc} */
	@Override
	public ParoleReview updateParoleReview(final ParoleReview paroleReview, 
			final StaffAssignment staffAssignment, final Date date, 
			final String text, final ParoleEndorsementCategory endorsement,
			final StaffRoleCategory staffRole) 
					throws DuplicateEntityFoundException {
		return this.paroleReviewDelegate.update(paroleReview, 
				staffAssignment, date, text, paroleReview.getOffender(), 
				endorsement, staffRole);
	}

	/**{@inheritDoc} */
	@Override
	public void removeParoleReview(final ParoleReview paroleReview) {
		this.paroleReviewDelegate.remove(paroleReview);
	}

	/**{@inheritDoc} */
	@Override
	public Document createDocument(final Date date, final String title, 
			final String filename, final String fileExtension) 
					throws DuplicateEntityFoundException {
		return this.documentDelegate.create(date, filename, fileExtension, 
				title);
	}

	/**{@inheritDoc} */
	@Override
	public Document updateDocument(final Document document, final Date date, 
			final String title) throws DuplicateEntityFoundException {
		return this.documentDelegate.update(document, title, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeDocument(final Document document) {
		this.documentDelegate.remove(document);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag createDocumentTag(final String name, 
			final Document document) throws DuplicateEntityFoundException {
		return this.documentTagDelegate.tagDocument(document, name);
	}

	/**{@inheritDoc} */
	@Override
	public DocumentTag updateDocumentTag(final DocumentTag documentTag, 
			final String name) throws DuplicateEntityFoundException {
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
	public ParoleReviewDocumentAssociation 
			createParoleReviewDocumentAssociation(final Document document,
					final ParoleReview paroleReview) 
							throws DuplicateEntityFoundException {
		return this.paroleReviewDocumentAssociationDelegate.create(
				document, paroleReview);
	}

	/**{@inheritDoc} */
	@Override
	public void removeParoleReviewDocumentAssociation(
			final ParoleReviewDocumentAssociation 
					paroleReviewDocumentAssociation) {
		this.paroleReviewDocumentAssociationDelegate.remove(
				paroleReviewDocumentAssociation);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<ParoleReviewDocumentAssociation> 
			findParoleReviewDocumentAssociationsByParoleReview(
					final ParoleReview paroleReview) {
		return this.paroleReviewDocumentAssociationDelegate
				.findByParoleReview(paroleReview);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReviewNote createParoleReviewNote(
			final ParoleReview paroleReview, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		return this.paroleReviewNoteDelegate.create(paroleReview, date, 
				description);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleReviewNote updateParoleReviewNote(
			final ParoleReviewNote paroleReviewNote, 
			final ParoleReview paroleReview, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		return this.paroleReviewNoteDelegate.update(paroleReviewNote, 
				paroleReview, date, description);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParoleReviewNote(
			final ParoleReviewNote paroleReviewNote) {
		this.paroleReviewNoteDelegate.remove(paroleReviewNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleEndorsementCategory> findParoleEndorsementCategories() {
		return this.paroleEndorsementCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<StaffRoleCategory> findStaffRoleCategories() {
		return this.staffRoleCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleReviewNote> findParoleReviewNotesByParoleReview(
			final ParoleReview paroleReview) {
		return this.paroleReviewNoteDelegate.findByParoleReview(paroleReview);
	}
}