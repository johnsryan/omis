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
package omis.paroleboardcondition.service.impl;

import java.util.Date;
import java.util.List;

import omis.boardhearing.domain.BoardHearing;
import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementAssociableDocument;
import omis.condition.domain.AgreementCategory;
import omis.condition.domain.AgreementNote;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionCategory;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.ConditionGroup;
import omis.condition.domain.ConditionTitle;
import omis.condition.service.delegate.AgreementAssociableDocumentDelegate;
import omis.condition.service.delegate.AgreementDelegate;
import omis.condition.service.delegate.AgreementNoteDelegate;
import omis.condition.service.delegate.ConditionClauseDelegate;
import omis.condition.service.delegate.ConditionDelegate;
import omis.condition.service.delegate.ConditionGroupDelegate;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.service.delegate.DocumentDelegate;
import omis.document.service.delegate.DocumentTagDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.offender.domain.Offender;
import omis.paroleboardcondition.domain.ParoleBoardAgreement;
import omis.paroleboardcondition.domain.ParoleBoardAgreementCategory;
import omis.paroleboardcondition.service.ParoleBoardConditionService;
import omis.paroleboardcondition.service.delegate.ParoleBoardAgreementCategoryDelegate;
import omis.paroleboardcondition.service.delegate.ParoleBoardAgreementDelegate;

/**
 * Parole Board Condition Service Implementation.
 * 
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.1 (Feb 6, 2018)
 * @since OMIS 3.0
 *
 */
public class ParoleBoardConditionServiceImpl
		implements ParoleBoardConditionService {

	private final ParoleBoardAgreementDelegate
		paroleBoardAgreementDelegate;

	private final ParoleBoardAgreementCategoryDelegate
		paroleBoardAgreementCategoryDelegate;

	private final AgreementNoteDelegate agreementNoteDelegate;

	private final AgreementDelegate agreementDelegate;

	private final AgreementAssociableDocumentDelegate
		agreementAssociableDocumentDelegate;

	private final ConditionDelegate conditionDelegate;

	private final ConditionClauseDelegate conditionClauseDelegate;

	private final ConditionGroupDelegate conditionGroupDelegate;

	private final DocumentDelegate documentDelegate;

	private final DocumentTagDelegate documentTagDelegate;

	/**
	 * @param paroleBoardAgreementDelegate - Parole Board Agreement Delegate
	 * @param paroleBoardAgreementCategoryDelegate - Parole Board Agreement
	 * Category Delegate
	 * @param agreementNoteDelegate - Agreement Note Delegate
	 * @param agreementDelegate - Agreement Delegate
	 * @param agreementAssociableDocumentDelegate - Agreement Associable
	 * Document Delegate
	 * @param conditionDelegate - Condition Delegate
	 * @param conditionClauseDelegate - Condition Clause Delegate
	 * @param conditionGroupDelegate - Condition Group Delegate
	 * @param documentDelegate - Document Delegate
	 * @param documentTagDelegate - Document Tag Delegate
	 */
	public ParoleBoardConditionServiceImpl(
			final ParoleBoardAgreementDelegate paroleBoardAgreementDelegate,
			final ParoleBoardAgreementCategoryDelegate
				paroleBoardAgreementCategoryDelegate,
			final AgreementNoteDelegate agreementNoteDelegate,
			final AgreementDelegate agreementDelegate,
			final AgreementAssociableDocumentDelegate
				agreementAssociableDocumentDelegate,
			final ConditionDelegate conditionDelegate,
			final ConditionClauseDelegate conditionClauseDelegate,
			final ConditionGroupDelegate conditionGroupDelegate,
			final DocumentDelegate documentDelegate,
			final DocumentTagDelegate documentTagDelegate) {
		this.paroleBoardAgreementDelegate = paroleBoardAgreementDelegate;
		this.paroleBoardAgreementCategoryDelegate =
				paroleBoardAgreementCategoryDelegate;
		this.agreementNoteDelegate = agreementNoteDelegate;
		this.agreementDelegate = agreementDelegate;
		this.agreementAssociableDocumentDelegate =
				agreementAssociableDocumentDelegate;
		this.conditionDelegate = conditionDelegate;
		this.conditionClauseDelegate = conditionClauseDelegate;
		this.conditionGroupDelegate = conditionGroupDelegate;
		this.documentDelegate = documentDelegate;
		this.documentTagDelegate = documentTagDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public Agreement createAgreement(final Offender offender,
			final Date startDate, final Date endDate, final String description,
			final AgreementCategory category)
			throws DuplicateEntityFoundException {
		return this.agreementDelegate
				.create(offender, startDate, endDate, description, category);
	}

	/** {@inheritDoc} */
	@Override
	public Agreement updateAgreement(final Agreement agreement,
			final Date startDate, final Date endDate, final String description,
			final AgreementCategory category)
			throws DuplicateEntityFoundException {
		return this.agreementDelegate
				.update(agreement, startDate, endDate, description, category);
	}

	/** {@inheritDoc} */
	@Override
	public void removeAgreement(final Agreement agreement) {
		this.agreementDelegate.remove(agreement);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardAgreement createParoleBoardAgreement(
			final Agreement agreement,
			final BoardHearing boardHearing,
			final ParoleBoardAgreementCategory category)
			throws DuplicateEntityFoundException {
		return this.paroleBoardAgreementDelegate.create(agreement, boardHearing,
				null, category);
	}
	
	/** {@inheritDoc} */
	@Override
	public ParoleBoardAgreement createParoleBoardAgreement(
			final Agreement agreement,
			final HearingAnalysis hearingAnalysis,
			final ParoleBoardAgreementCategory category)
			throws DuplicateEntityFoundException {
		return this.paroleBoardAgreementDelegate.create(agreement, null,
				hearingAnalysis, category);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleBoardAgreement updateParoleBoardAgreement(
			final ParoleBoardAgreement paroleBoardAgreement,
			final ParoleBoardAgreementCategory category)
			throws DuplicateEntityFoundException {
		return this.paroleBoardAgreementDelegate.update(
				paroleBoardAgreement, paroleBoardAgreement.getAgreement(), 
				paroleBoardAgreement.getBoardHearing(), 
				paroleBoardAgreement.getHearingAnalysis(), category);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParoleBoardAgreement(
			final ParoleBoardAgreement paroleBoardAgreement) {
		this.paroleBoardAgreementDelegate.remove(paroleBoardAgreement);
	}

	/** {@inheritDoc} */
	@Override
	public AgreementNote createAgreementNote(final Date date,
			final Agreement agreement, final String description)
			throws DuplicateEntityFoundException {
		return this.agreementNoteDelegate.create(date, agreement, description);
	}

	/** {@inheritDoc} */
	@Override
	public AgreementNote updateAgreementNote(final AgreementNote agreementNote,
			final String description, final Date date)
			throws DuplicateEntityFoundException {
		return this.agreementNoteDelegate.update(
				agreementNote, description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeAgreementNote(final AgreementNote agreementNote) {
		this.agreementNoteDelegate.remove(agreementNote);
	}

	/** {@inheritDoc} */
	@Override
	public Condition createCondition(final Agreement agreement,
			final String clause, final ConditionClause conditionClause,
			final ConditionCategory category, final Boolean waived)
			throws DuplicateEntityFoundException {
		return this.conditionDelegate.create(agreement, clause,
				conditionClause, category, waived);
	}

	/** {@inheritDoc} */
	@Override
	public Condition updateCondition(final Condition condition,
			final ConditionClause conditionClause, final String clause,
			final ConditionCategory category, final Boolean waived)
			throws DuplicateEntityFoundException {
		return this.conditionDelegate.update(condition, conditionClause, clause,
				category, waived);
	}

	/** {@inheritDoc} */
	@Override
	public void removeCondition(final Condition condition) {
		this.conditionDelegate.remove(condition);
	}

	/** {@inheritDoc} */
	@Override
	public AgreementAssociableDocument createAgreementAssociableDocument(
			final Agreement agreement, final Document document)
			throws DuplicateEntityFoundException {
		return this.agreementAssociableDocumentDelegate
				.create(agreement, document);
	}

	/** {@inheritDoc} */
	@Override
	public void removeAgreementAssociableDocument(
			final AgreementAssociableDocument agreementAssociableDocument) {
		this.agreementAssociableDocumentDelegate
				.remove(agreementAssociableDocument);
	}

	/** {@inheritDoc} */
	@Override
	public Document createDocument(final Date documentDate,
			final String filename, final String fileExtension,
			final String title) throws DuplicateEntityFoundException {
		return this.documentDelegate.create(documentDate, filename,
				fileExtension, title);
	}

	/** {@inheritDoc} */
	@Override
	public Document updateDocument(final Document document,
			final Date documentDate, final String title)
			throws DuplicateEntityFoundException {
		return this.documentDelegate.update(document, title, documentDate);
	}

	/** {@inheritDoc} */
	@Override
	public void removeDocument(final Document document) {
		this.documentDelegate.remove(document);
	}

	/** {@inheritDoc} */
	@Override
	public DocumentTag createDocumentTag(final Document document,
			final String name) throws DuplicateEntityFoundException {
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
	public List<ConditionClause> findConditionClausesByConditionGroup(
			final ConditionGroup conditionGroup) {
		return this.conditionClauseDelegate
				.findConditionClausesByConditionGroup(conditionGroup);
	}

	/** {@inheritDoc} */
	@Override
	public List<ConditionGroup> findUnusedByAgreement(
			final Agreement agreement) {
		return this.conditionGroupDelegate.findUnusedByAgreement(agreement);
	}

	/** {@inheritDoc} */
	@Override
	public List<ConditionGroup> findUsedByAgreement(final Agreement agreement) {
		return this.conditionGroupDelegate.findUsedByAgreement(agreement);
	}

	/** {@inheritDoc} */
	@Override
	public ConditionClause findConditionClauseByConditionTitle(
			final ConditionTitle conditionTitle) {
		return this.conditionClauseDelegate
				.findByConditionTitle(conditionTitle);
	}

	/** {@inheritDoc} */
	@Override
	public List<ConditionClause> findConditionClausesByCategory(
			final ParoleBoardAgreementCategory category) {
		return this.conditionClauseDelegate
				.findByParoleBoardAgreementCategory(category);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleBoardAgreementCategory>
			findAllParoleBoardAgreementCategories() {
		return this.paroleBoardAgreementCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<Condition> findConditionsByAgreement(
			final Agreement agreement) {
		return this.conditionDelegate.findByAgreement(agreement);
	}

	/** {@inheritDoc} */
	@Override
	public List<AgreementNote> findAgreementNotesByAgreement(
			final Agreement agreement) {
		return this.agreementNoteDelegate.findByAgreement(agreement);
	}

	/** {@inheritDoc} */
	@Override
	public List<AgreementAssociableDocument>
			findAgreementAssociableDocumentsByAgreement(
					final Agreement agreement) {
		return this.agreementAssociableDocumentDelegate
				.findByAgreement(agreement);
	}
}
