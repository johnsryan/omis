package omis.stg.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.stg.dao.SecurityThreatGroupActivityNoteDao;
import omis.stg.domain.SecurityThreatGroupActivity;
import omis.stg.domain.SecurityThreatGroupActivityNote;

/**
 * Security threat group affiliation note.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Apr 10, 2018)
 * @since OMIS 3.0
 */
public class SecurityThreatGroupActivityNoteDelegate {

	/* Instance factories. */
	private final InstanceFactory<SecurityThreatGroupActivityNote> 
		activityNoteInstanceFactory;
	
	/* DAOs. */
	private final SecurityThreatGroupActivityNoteDao activityNoteDao;
	
	/* Audit component retriever. */
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	/** 
	 * Instantiates delegate for security threat group activity note.
	 * 
	 * @param securityThreatGroupActivityNoteInstanceFactory security threat 
	 * group activity note instance factory
	 * @param securityThreatGroupActivityNoteDao security threat group activity 
	 * note data access object 
	 * @param auditComponentRetriever audit component retriever
	 */
	public SecurityThreatGroupActivityNoteDelegate(
			final InstanceFactory<SecurityThreatGroupActivityNote> 
				activityNoteInstanceFactory,
			final SecurityThreatGroupActivityNoteDao activityNoteDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.activityNoteInstanceFactory = activityNoteInstanceFactory;
		this.activityNoteDao = activityNoteDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/** 
	 * Creates a security threat group activity note.
	 * 
	 * @param activity security threat group activity
	 * @param date date
	 * @param value value
	 * @return security threat group activity note
	 * @throws DuplicateEntityFoundException when an activity note already 
	 * exists
	 */
	public SecurityThreatGroupActivityNote addNote(
			final SecurityThreatGroupActivity activity, 
			final Date date,
			final String value)
		throws DuplicateEntityFoundException {
		if (this.activityNoteDao.findNote(activity, date, value) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate security threat group activity note found");
		}
		SecurityThreatGroupActivityNote activityNote = 
			this.activityNoteInstanceFactory.createInstance();
				activityNote.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
			this.populateSecurityThreatGroupActivityNote(activityNote, activity, 
					date, value);
		return this.activityNoteDao.makePersistent(activityNote);
	}
	
	/**
	 * Updates a security threat group activity note.
	 * 
	 * @param note security threat group activity note
	 * @param date date
	 * @param value value
	 * @return security threat group activity note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public SecurityThreatGroupActivityNote updateNote(
			final SecurityThreatGroupActivityNote note, final Date date, 
			final String value) throws DuplicateEntityFoundException {
		if (this.activityNoteDao.findExcluding(note.getActivity(), date, value, 
				note) != null) {
			throw new DuplicateEntityFoundException(
					"Duplicate security threat group activity note found.");
		}
		this.populateSecurityThreatGroupActivityNote(note, note.getActivity(),
				date, value);
		return this.activityNoteDao.makePersistent(note);
	}

	/** 
	 * Removes a security threat group activity.
	 * 
	 * @param note security threat group activity note
	 */
	public void remove(final SecurityThreatGroupActivityNote note) {
		this.activityNoteDao.makeTransient(note);
	}
	
	/**
	 * Returns a list of security threat group activity notes.
	 * 
	 * @return list of security threat group activity notes
	 */
	public List<SecurityThreatGroupActivityNote> findNotes(
			SecurityThreatGroupActivity activity) {
		return this.activityNoteDao.findNotes(activity);
	}
	
	// Populates the specified security threat group activity.
	private void populateSecurityThreatGroupActivityNote(
			final SecurityThreatGroupActivityNote note,
			final SecurityThreatGroupActivity activity,
			final Date date, 
			final String value) {
		note.setActivity(activity);
		note.setDate(date);
		note.setValue(value);
		note.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
	}
}