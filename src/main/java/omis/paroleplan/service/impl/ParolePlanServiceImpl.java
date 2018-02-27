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
package omis.paroleplan.service.impl;

import java.util.Date;
import java.util.List;

import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleplan.domain.ParolePlan;
import omis.paroleplan.domain.ParolePlanDocumentAssociation;
import omis.paroleplan.domain.ParolePlanNote;
import omis.paroleplan.service.ParolePlanService;
import omis.paroleplan.service.delegate.ParolePlanDelegate;
import omis.paroleplan.service.delegate.ParolePlanDocumentAssociationDelegate;
import omis.paroleplan.service.delegate.ParolePlanNoteDelegate;
import omis.staff.domain.StaffAssignment;

/**
 * Implementation for parole plan service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Feb 13, 2018)
 * @since OMIS 3.0
 */
public class ParolePlanServiceImpl implements ParolePlanService {

	/* Delegates. */
	
	private final ParolePlanDelegate parolePlanDelegate;
	
	private final ParolePlanDocumentAssociationDelegate 
			parolePlanDocumentAssociationDelegate;
	
	private final ParolePlanNoteDelegate parolePlanNoteDelegate;
	
	private final DocumentDelegate documentDelegate;
	
	private final DocumentTagDelegate documentTagDelegate;
	
	/**
	 * Instantiates a parole plan service implementation with the specified 
	 * delegates.
	 * 
	 * @param parolePlanDelegate parole plan delegate
	 * @param parolePlanDocumentAssociationDelegate parole plan document 
	 * association delegate
	 * @param parolePlanNoteDelegate parole plan note delegate
	 * @param documentDelegate document delegate
	 * @param documentTagDelegate document tag delegate
	 */
	public ParolePlanServiceImpl(final ParolePlanDelegate parolePlanDelegate,
			final ParolePlanDocumentAssociationDelegate 
					parolePlanDocumentAssociationDelegate,
			final ParolePlanNoteDelegate parolePlanNoteDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate) {
		this.parolePlanDelegate = parolePlanDelegate;
		this.parolePlanDocumentAssociationDelegate = 
				parolePlanDocumentAssociationDelegate;
		this.parolePlanNoteDelegate = parolePlanNoteDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public ParolePlan createParolePlan(
			final ParoleEligibility paroleEligibility, 
			final StaffAssignment evaluator, final String evaluationDescription, 
			final String vocationalPlan, final String residencePlan, 
			final String treatmentPlan) throws DuplicateEntityFoundException {
		return this.parolePlanDelegate.create(paroleEligibility, evaluator, 
				evaluationDescription, vocationalPlan, residencePlan, 
				treatmentPlan);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlan updateParolePlan(final ParolePlan parolePlan, 
			final StaffAssignment evaluator, final String evaluationDescription,
			final String vocationalPlan, final String residencePlan, 
			final String treatmentPlan) throws DuplicateEntityFoundException {
		return this.parolePlanDelegate.update(parolePlan, 
				parolePlan.getParoleEligibility(), evaluator, 
				evaluationDescription, vocationalPlan, residencePlan, 
				treatmentPlan);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParolePlan(final ParolePlan parolePlan) {
		this.parolePlanDelegate.remove(parolePlan);
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
	public ParolePlanDocumentAssociation createParolePlanDocumentAssociation(
			final ParolePlan parolePlan, final Document document) 
					throws DuplicateEntityFoundException {
		return this.parolePlanDocumentAssociationDelegate.create(parolePlan, 
				document);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParolePlanDocumentAssociation(
			final ParolePlanDocumentAssociation parolePlanDocumentAssociation) {
		this.parolePlanDocumentAssociationDelegate.remove(
				parolePlanDocumentAssociation);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlanNote createParolePlanNote(final ParolePlan parolePlan, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.parolePlanNoteDelegate.create(parolePlan, description, 
				date);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlanNote updateParolePlanNote(
			final ParolePlanNote parolePlanNote, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		return this.parolePlanNoteDelegate.update(parolePlanNote, 
				parolePlanNote.getParolePlan(), description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParolePlanNote(final ParolePlanNote parolePlanNote) {
		this.parolePlanNoteDelegate.remove(parolePlanNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParolePlanDocumentAssociation> 
			findParolePlanDocumentAssociationsByParolePlan(
					final ParolePlan parolePlan) {
		return this.parolePlanDocumentAssociationDelegate.findByParolePlan(
				parolePlan);
	}

	/** {@inheritDoc} */
	@Override
	public List<DocumentTag> findDocumentTagsByDocument(
			final Document document) {
		return this.documentTagDelegate.findByDocument(document);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParolePlanNote> findParolePlanNotesByParolePlan(
			final ParolePlan parolePlan) {
		return this.parolePlanNoteDelegate.findByParolePlan(parolePlan);
	}

	/** {@inheritDoc} */
	@Override
	public ParolePlan findParolePlanByParoleEligibility(
			final ParoleEligibility paroleEligibility) {
		return this.parolePlanDelegate.findByParoleEligibility(
				paroleEligibility);
	}

}
