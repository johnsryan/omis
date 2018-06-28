package omis.employment.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.employment.dao.EmploymentNoteDao;
import omis.employment.domain.EmploymentNote;
import omis.employment.domain.EmploymentTerm;
import omis.employment.exception.EmploymentNoteExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Employment note delegate.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Dec 13, 2017)
 * @since OMIS 3.0
 */
public class EmploymentNoteDelegate {

	/* Instance factories. */
	
	private final InstanceFactory<EmploymentNote> employmentNoteInstanceFactory;
	
	/* DAOs. */
	
	private final EmploymentNoteDao employmentNoteDao;
	
	/* Audit component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	/**
	 * Instantiates delegate for employment note.
	 * 
	 * @param employmentNoteInstanceFactory instance factory for
	 * employment notes
	 * @param employmentNoteDao data access object for employment notes.
	 */
	
	public EmploymentNoteDelegate(
			final EmploymentNoteDao employmentNoteDao,
			final InstanceFactory<EmploymentNote> employmentNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.employmentNoteDao = employmentNoteDao;
		this.employmentNoteInstanceFactory = employmentNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
		}
	
	/** 
	 * Creates an employment note.
	 * 
	 * @param term employment term
	 * @param date date
	 * @param value value
	 * @return employment note 
	 * @throws EmploymentNoteExistsException if duplicate entity exists 
	 */
	public EmploymentNote create(final EmploymentTerm term, final Date date, 
			final String value) throws EmploymentNoteExistsException {
		if (this.employmentNoteDao.find(term, date, value) != null) {
			throw new EmploymentNoteExistsException(
					"Duplicate employment note found.");
		}
		EmploymentNote employmentNote 
			= this.employmentNoteInstanceFactory.createInstance();
		employmentNote.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		employmentNote.setEmploymentTerm(term);
			this.populateEmploymentNote(employmentNote, date, value);
		return this.employmentNoteDao.makePersistent(employmentNote);
	}

	/** 
	 * Updates an existing employment note.
	 * 
	 * @param note employment note
	 * @param date date
	 * @param value value
	 * @return employment note 
	 * @throws EmploymentNoteExistsException if duplicate entity exists 
	 */
	public EmploymentNote update(final EmploymentNote note, final Date date, 
			final String value) throws EmploymentNoteExistsException {
		if (this.employmentNoteDao.findExcluding(note.getEmploymentTerm(), date, 
				value, note) != null) {
			throw new EmploymentNoteExistsException(
					"Duplicate employment note found.");
		}
		this.populateEmploymentNote(note, date, value);
		return this.employmentNoteDao.makePersistent(note);
	}
	
	/**
	 * Removes an employment note.
	 * 
	 * @param note note
	 */
	public void remove(final EmploymentNote note) {
		this.employmentNoteDao.makeTransient(note);
	}
	
	/**
	 * Find employment notes.
	 * 
	 * @param term employment term
	 * @return list of employment notes
	 */
	public List<EmploymentNote> findNotes(final EmploymentTerm term) {
		return this.employmentNoteDao.findNotes(term);
	}

	// Populates an employment note
	private EmploymentNote populateEmploymentNote(
			final EmploymentNote employmentNote, 
			final Date date, 
			final String value) {
		employmentNote.setDate(date);
		employmentNote.setValue(value);
		employmentNote.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return employmentNote;
		}

	}
