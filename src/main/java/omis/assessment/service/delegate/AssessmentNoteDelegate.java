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
import omis.assessment.dao.AssessmentNoteDao;
import omis.assessment.domain.AssessmentNote;
import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.questionnaire.domain.AdministeredQuestionnaire;

/**
 * Assessment Note Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Mar 15, 2018)
 *@since OMIS 3.0
 *
 */
public class AssessmentNoteDelegate {
	
	private static final String DUPLICATE_ENTITY_FOUND_MSG =
			"Assessment Note already exists with the given date and description"
			+ " for the specified Administered Questionnaire.";
	
	private final AssessmentNoteDao assessmentNoteDao;
	
	private final InstanceFactory<AssessmentNote> 
		assessmentNoteInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for AssessmentNoteDelegate.
	 * @param assessmentNoteDao - Assessment Note Dao
	 * @param assessmentNoteInstanceFactory - Assessment Note Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public AssessmentNoteDelegate(
			final AssessmentNoteDao assessmentNoteDao,
			final InstanceFactory<AssessmentNote> 
				assessmentNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.assessmentNoteDao = assessmentNoteDao;
		this.assessmentNoteInstanceFactory = assessmentNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates the specified Assessment Note with the given properties.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param description - String description
	 * @param date - Date
	 * @return Newly created Assessment Note
	 * @throws DuplicateEntityFoundException - When another Assessment Note
	 * already exists with the given properties.
	 */
	public AssessmentNote create(
			final AdministeredQuestionnaire administeredQuestionnaire,
			final String description, final Date date)
				throws DuplicateEntityFoundException {
		if (this.assessmentNoteDao.find(administeredQuestionnaire,
				description, date) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		AssessmentNote assessmentNote = 
				this.assessmentNoteInstanceFactory.createInstance();
		
		assessmentNote.setAdministeredQuestionnaire(administeredQuestionnaire);
		assessmentNote.setDescription(description);
		assessmentNote.setDate(date);
		assessmentNote.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		assessmentNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.assessmentNoteDao.makePersistent(assessmentNote);
	}
	
	/**
	 * Updates the specified Assessment Note with the given properties.
	 * 
	 * @param assessmentNote - Assessment Note to update.
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @param description - String description
	 * @param date - Date
	 * @return Updated Assessment Note
	 * @throws DuplicateEntityFoundException - When another Assessment Note
	 * already exists with the given properties.
	 */
	public AssessmentNote update(final AssessmentNote assessmentNote,
			final AdministeredQuestionnaire administeredQuestionnaire,
			final String description, final Date date)
				throws DuplicateEntityFoundException {
		if (this.assessmentNoteDao.findExcluding(administeredQuestionnaire,
				description, date, assessmentNote) != null) {
			throw new DuplicateEntityFoundException(DUPLICATE_ENTITY_FOUND_MSG);
		}
		
		assessmentNote.setAdministeredQuestionnaire(administeredQuestionnaire);
		assessmentNote.setDescription(description);
		assessmentNote.setDate(date);
		assessmentNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.assessmentNoteDao.makePersistent(assessmentNote);
	}
	
	/**
	 * Removes the specified Assessment Note.
	 * 
	 * @param assessmentNote - Assessment Note to remove.
	 */
	public void remove(final AssessmentNote assessmentNote) {
		this.assessmentNoteDao.makeTransient(assessmentNote);
	}
	
	/**
	 * Returns a list a Assessment Notes for the specified Administered
	 * Questionnaire.
	 * 
	 * @param administeredQuestionnaire - Administered Questionnaire
	 * @return List a Assessment Notes for the specified Administered
	 * Questionnaire.
	 */
	public List<AssessmentNote> findByAdministeredQuestionnaire(
			final AdministeredQuestionnaire administeredQuestionnaire) {
		return this.assessmentNoteDao.findByAdministeredQuestionnaire(
				administeredQuestionnaire);
	}
	
}
