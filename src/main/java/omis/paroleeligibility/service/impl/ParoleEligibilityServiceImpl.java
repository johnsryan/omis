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
package omis.paroleeligibility.service.impl;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.paroleeligibility.domain.AppearanceCategory;
import omis.paroleeligibility.domain.EligibilityStatusCategory;
import omis.paroleeligibility.domain.EligibilityStatusReason;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.domain.ParoleEligibilityNote;
import omis.paroleeligibility.domain.component.ParoleEligibilityStatus;
import omis.paroleeligibility.service.ParoleEligibilityService;
import omis.paroleeligibility.service.delegate.AppearanceCategoryDelegate;
import omis.paroleeligibility.service.delegate.EligibilityStatusReasonDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.paroleeligibility.service.delegate.ParoleEligibilityNoteDelegate;

/**
 * Implementation of service for parole eligibility service.
 *
 * @author Trevor Isles
 * @author Annie Wahl
 * @version 0.1.1 (May 23, 2018)
 * @since OMIS 3.0
 */
public class ParoleEligibilityServiceImpl implements ParoleEligibilityService {
	
	private final ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	private final EligibilityStatusReasonDelegate 
		eligibilityStatusReasonDelegate;
	
	private final AppearanceCategoryDelegate appearanceCategoryDelegate;
	
	private final ParoleEligibilityNoteDelegate paroleEligibilityNoteDelegate;
	
	
	public ParoleEligibilityServiceImpl(
			final ParoleEligibilityDelegate paroleEligibilityDelegate,
			final EligibilityStatusReasonDelegate 
				eligibilityStatusReasonDelegate,
			final AppearanceCategoryDelegate appearanceCategoryDelegate,
			final ParoleEligibilityNoteDelegate paroleEligibilityNoteDelegate) {
		this.paroleEligibilityDelegate = paroleEligibilityDelegate;
		this.eligibilityStatusReasonDelegate = eligibilityStatusReasonDelegate;
		this.appearanceCategoryDelegate = appearanceCategoryDelegate;
		this.paroleEligibilityNoteDelegate = paroleEligibilityNoteDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEligibility create(final Offender offender,
			final Date hearingEligibilityDate, 
			final AppearanceCategory appearanceCategory,
			final EligibilityStatusCategory statusCategory, 
			final Date statusDate, 
			final EligibilityStatusReason statusReason,
			final String statusComment, 
			final Date reviewDate) 
					throws DuplicateEntityFoundException {
		return this.paroleEligibilityDelegate.create(offender,
				hearingEligibilityDate, reviewDate, new ParoleEligibilityStatus(
				statusDate,	statusComment, statusCategory, statusReason), 
				appearanceCategory); 
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEligibility update(
			final ParoleEligibility paroleEligibility,
			final Date hearingEligibilityDate,
			final AppearanceCategory appearanceCategory, 
			final EligibilityStatusCategory statusCategory, 
			final Date statusDate,
			final EligibilityStatusReason statusReason, 
			final String statusComment, 
			final Date reviewDate)
					throws DuplicateEntityFoundException {
		return this.paroleEligibilityDelegate.update(paroleEligibility, 
				paroleEligibility.getOffender(), hearingEligibilityDate,
				reviewDate, new ParoleEligibilityStatus(statusDate, 
				statusComment, statusCategory, statusReason), 
				appearanceCategory); 
	}

	/** {@inheritDoc} */
	@Override
	public void remove(ParoleEligibility paroleEligibility) {
		this.paroleEligibilityDelegate.remove(paroleEligibility); 
	}
	

	/** {@inheritDoc} */
	@Override
	public ParoleEligibilityNote createParoleEligibilityNote(
			final ParoleEligibility paroleEligibility, 
			final Date date,
			final String description) 
					throws DuplicateEntityFoundException {
		return this.paroleEligibilityNoteDelegate.create(paroleEligibility,
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleEligibilityNote updateParoleEligibilityNote(
			final ParoleEligibilityNote paroleEligibilityNote,
			final Date date,
			final String description) 
					throws DuplicateEntityFoundException {
		return this.paroleEligibilityNoteDelegate.update(
				paroleEligibilityNote, description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeParoleEligiblityNote(
			final ParoleEligibilityNote paroleEligibilityNote) {
		this.paroleEligibilityNoteDelegate.remove(paroleEligibilityNote);
	}
	

	/** {@inheritDoc} */
	@Override
	public List<ParoleEligibilityNote> 
		findParoleEligibilityNotesByParoleEligibility(
			ParoleEligibility paroleEligibility) {
		return this.paroleEligibilityNoteDelegate
			.findParoleEligibilityNotesByParoleEligibility(paroleEligibility);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<EligibilityStatusReason> findEligibilityStatusReasons() {
		return this.eligibilityStatusReasonDelegate
				.findEligibilityStatusReasons();
	}
	
	/** {@inheritDoc} */
	@Override
	public List<AppearanceCategory> findAppearanceCategories() {
		return this.appearanceCategoryDelegate.findAppearanceCategories();
	}

	/**{@inheritDoc} */
	@Override
	public List<EligibilityStatusReason>
		findEligibilityStatusReasonsByEligibilityStatusCategory(
			final EligibilityStatusCategory statusCategory) {
		return this.eligibilityStatusReasonDelegate
				.findByStatusCategory(statusCategory);
	}

	/**{@inheritDoc} */
	@Override
	public List<ParoleEligibility> findParoleEligibilitiesByOffenderAfterDate(
			final Offender offender, final Date date) {
		return this.paroleEligibilityDelegate.findByOffenderAfterDate(
				offender, date);
	}
}
