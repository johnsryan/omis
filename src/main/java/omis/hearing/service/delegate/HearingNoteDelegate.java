package omis.hearing.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.dao.HearingNoteDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingNote;
import omis.hearing.exception.HearingNoteExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * HearingNoteDelegate.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Dec 27, 2016)
 *@since OMIS 3.0
 *
 */
public class HearingNoteDelegate {
	private static final String HEARING_NOTE_EXISTS_FOUND_MSG =
			"Hearing Note already exists for specified hearing with given date"
			+ " and description";
	
	private final HearingNoteDao hearingNoteDao;
	
	private final InstanceFactory<HearingNote> 
		hearingNoteInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for HearingNoteDelegate
	 * @param hearingNoteDao
	 * @param hearingNoteInstanceFactory
	 * @param auditComponentRetriever
	 */
	public HearingNoteDelegate(
			final HearingNoteDao hearingNoteDao,
			final InstanceFactory<HearingNote> 
				hearingNoteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.hearingNoteDao = hearingNoteDao;
		this.hearingNoteInstanceFactory = hearingNoteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a hearing note with specified parameters
	 * @param hearing
	 * @param description - String
	 * @param date
	 * @return newly created hearing note
	 * @throws DuplicateEntityFoundException - when Hearing Note already exists
	 * for specified hearing with given date and description
	 */
	public HearingNote create(final Hearing hearing, final String description,
			final Date date) throws HearingNoteExistsException{
		if(this.hearingNoteDao.find(hearing, description, date) != null){
			throw new HearingNoteExistsException(HEARING_NOTE_EXISTS_FOUND_MSG);
		}
		
		HearingNote hearingNote = 
				this.hearingNoteInstanceFactory.createInstance();
		
		hearingNote.setHearing(hearing);
		hearingNote.setDescription(description);
		hearingNote.setDate(date);
		hearingNote.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		hearingNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.hearingNoteDao.makePersistent(hearingNote);
	}
	
	/**
	 * Updates a hearing note with specified parameters
	 * @param hearingNote - HearingNote to update
	 * @param description - String
	 * @param date
	 * @return updated hearing note
	 * @throws DuplicateEntityFoundException - when Hearing Note already exists
	 * for specified hearing with given date and description
	 */
	public HearingNote update(final HearingNote hearingNote,
			final String description, final Date date)
			throws HearingNoteExistsException{
		if(this.hearingNoteDao.findExcluding(
				hearingNote.getHearing(), description, date,
				hearingNote) != null){
			throw new HearingNoteExistsException(HEARING_NOTE_EXISTS_FOUND_MSG);
		}
		
		hearingNote.setDescription(description);
		hearingNote.setDate(date);
		hearingNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.hearingNoteDao.makePersistent(hearingNote);
	}
	
	/**
	 * Removes a HearingNote
	 * @param hearingNote - HearingNote to remove
	 */
	public void remove(final HearingNote hearingNote){
		this.hearingNoteDao.makeTransient(hearingNote);
	}
	
	/**
	 * Returns a list of all HearingNotes by hearing
	 * @param hearing
	 * @return list of all HearingNotes by hearing
	 */
	public List<HearingNote> findAllByHearing(final Hearing hearing){
		return this.hearingNoteDao.findByHearing(hearing);
	}
	
}
