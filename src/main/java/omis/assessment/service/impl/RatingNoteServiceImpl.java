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

import omis.assessment.domain.RatingNote;
import omis.assessment.service.RatingNoteService;
import omis.assessment.service.delegate.RatingNoteDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Implementation of rating note service.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 14, 2018)
 * @since OMIS 3.0
 */
public class RatingNoteServiceImpl implements RatingNoteService {

	/* Delegates. */
	
	private final RatingNoteDelegate ratingNoteDelegate;
	
	/**
	 * Instantiates a rating note service implementation with the specified 
	 * delegates.
	 */
	public RatingNoteServiceImpl(final RatingNoteDelegate ratingNoteDelegate) {
		this.ratingNoteDelegate = ratingNoteDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public RatingNote createRatingNote(final Date date, 
			final String description,
			final AdministeredQuestionnaire administeredQuestionnaire) 
					throws DuplicateEntityFoundException {
		return this.ratingNoteDelegate.create(date, description, 
				administeredQuestionnaire);
	}

	/** {@inheritDoc} */
	@Override
	public RatingNote updateRatingNote(final RatingNote ratingNote, 
			final Date date, final String description) 
					throws DuplicateEntityFoundException {
		return this.ratingNoteDelegate.update(ratingNote, date, description, 
				ratingNote.getAdministeredQuestionnaire());
	}

	/** {@inheritDoc} */
	@Override
	public void removeRatingNote(final RatingNote ratingNote) {
		this.ratingNoteDelegate.remove(ratingNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<RatingNote> findRatingNotesByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.ratingNoteDelegate
				.findRatingNotesByAdministeredQuestionnaire(
						administeredQuestionnaire);
	}
}