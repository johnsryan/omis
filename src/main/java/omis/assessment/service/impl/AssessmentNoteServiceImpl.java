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

import omis.assessment.domain.AssessmentNote;
import omis.assessment.service.AssessmentNoteService;
import omis.assessment.service.delegate.AssessmentNoteDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Note Service Implementation.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 15, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentNoteServiceImpl implements AssessmentNoteService {
	
	private final AssessmentNoteDelegate assessmentNoteDelegate;
	
	/**
	 * @param assessmentNoteDelegate - Assessment Note Delegate
	 */
	public AssessmentNoteServiceImpl(
			final AssessmentNoteDelegate assessmentNoteDelegate) {
		this.assessmentNoteDelegate = assessmentNoteDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentNote createAssessmentNote(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final String description, final Date date)
					throws DuplicateEntityFoundException {
		return this.assessmentNoteDelegate.create(administeredQuestionnaire,
				description, date);
	}

	/**{@inheritDoc} */
	@Override
	public AssessmentNote updateAssessmentNote(
			final AssessmentNote assessmentNote,
			final AdministeredQuestionnaire administeredQuestionnaire,
			final String description, final Date date)
					throws DuplicateEntityFoundException {
		return this.assessmentNoteDelegate.update(assessmentNote,
				administeredQuestionnaire, description, date);
	}

	/**{@inheritDoc} */
	@Override
	public void removeAssessmentNote(final AssessmentNote assessmentNote) {
		this.assessmentNoteDelegate.remove(assessmentNote);
	}

	/**{@inheritDoc} */
	@Override
	public List<AssessmentNote> findAssessmentNotesByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.assessmentNoteDelegate.findByAdministeredQuestionnaire(
				administeredQuestionnaire);
	}

}
