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
package omis.assessment.service.delegate;

import java.util.Date;
import java.util.List;

import omis.assessment.dao.AssessmentCategoryOverrideNoteDao;
import omis.assessment.domain.AssessmentCategoryOverride;
import omis.assessment.domain.AssessmentCategoryOverrideNote;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Assessment category override note delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Mar 12, 2018)
 * @since OMIS 3.0
 */
public class AssessmentCategoryOverrideNoteDelegate {

	/* Data access objects. */
	
	private final AssessmentCategoryOverrideNoteDao 
			assessmentCategoryOverrideNoteDao;

	/* Instance factories. */
	
	private final InstanceFactory<AssessmentCategoryOverrideNote> 
			assessmentCategoryOverrideNoteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;

	/** Instantiates an implementation of assessment category override note 
	 * delegate with the specified date access object and instance factory.
	 * 
	 * @param assessmentCategoryOverrideNoteDao assessment category override 
	 * note data access object
	 * @param assessmentCategoryOverrideNoteInstanceFactory assessment category 
	 * override note instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public AssessmentCategoryOverrideNoteDelegate(
			final AssessmentCategoryOverrideNoteDao 
					assessmentCategoryOverrideNoteDao,
			final InstanceFactory<AssessmentCategoryOverrideNote> 
					assessmentCategoryOverrideNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.assessmentCategoryOverrideNoteDao = 
				assessmentCategoryOverrideNoteDao;
		this.assessmentCategoryOverrideNoteInstanceFactory = 
				assessmentCategoryOverrideNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new assessment category override note.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @param description description
	 * @param date date
	 * @return assessment category override note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryOverrideNote create(
			final AssessmentCategoryOverride assessmentCategoryOverride, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryOverrideNoteDao.find(
				assessmentCategoryOverride, description, date) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category override note already exists.");
		}
		AssessmentCategoryOverrideNote assessmentCategoryOverrideNote = this
				.assessmentCategoryOverrideNoteInstanceFactory.createInstance();
		populateAssessmentCategoryOverrideNote(assessmentCategoryOverrideNote, 
				description, date);
		assessmentCategoryOverrideNote.setAssessmentCategoryOverride(
				assessmentCategoryOverride);
		assessmentCategoryOverrideNote.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		return this.assessmentCategoryOverrideNoteDao.makePersistent(
				assessmentCategoryOverrideNote);
	}

	/**
	 * Updates the specified assessment category override note.
	 * 
	 * @param assessmentCategoryOverrideNote assessment category override note
	 * @param description description
	 * @param date date
	 * @return assessment category override note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public AssessmentCategoryOverrideNote update(
			final AssessmentCategoryOverrideNote assessmentCategoryOverrideNote, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		if (this.assessmentCategoryOverrideNoteDao.findExcluding(
				assessmentCategoryOverrideNote.getAssessmentCategoryOverride(), 
				description, date, assessmentCategoryOverrideNote) != null) {
			throw new DuplicateEntityFoundException(
					"Assessment category override note already exists.");
		}
		populateAssessmentCategoryOverrideNote(assessmentCategoryOverrideNote, 
				description, date);
		return this.assessmentCategoryOverrideNoteDao.makePersistent(
				assessmentCategoryOverrideNote);
	}

	/**
	 * Removes the specified assessment category override note.
	 * 
	 * @param assessmentCategoryOverrideNote assessment category override note
	 */
	public void remove(
			final AssessmentCategoryOverrideNote assessmentCategoryOverrideNote) {
		this.assessmentCategoryOverrideNoteDao.makeTransient(
				assessmentCategoryOverrideNote);
	}

	/**
	 * Returns a list of assessment category override notes for the specified 
	 * assessment category override.
	 * 
	 * @param assessmentCategoryOverride assessment category override
	 * @return list of assessment category override notes
	 */
	public List<AssessmentCategoryOverrideNote> 
			findByAssessmentCategoryOverride(
					final AssessmentCategoryOverride assessmentCategoryOverride) {
		return this.assessmentCategoryOverrideNoteDao
				.findByAssessmentCategoryOverride(assessmentCategoryOverride);
	}

	// Populates a assessment category override note
	private void populateAssessmentCategoryOverrideNote(
			final AssessmentCategoryOverrideNote assessmentCategoryOverrideNote, 
			final String description, final Date date) {
		assessmentCategoryOverrideNote.setDate(date);
		assessmentCategoryOverrideNote.setDescription(description);
		assessmentCategoryOverrideNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}