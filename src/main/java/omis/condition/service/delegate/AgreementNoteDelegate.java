package omis.condition.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.condition.dao.AgreementNoteDao;
import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementNote;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Agreement Note Delegate.
 * 
 * @author Jonny Santy
 * @author Trevor Isles
 * @author Annie Jacques
 * @author Josh Divine
 * @version 0.1.2 (Feb 7, 2018)
 * @since OMIS 3.0
 */
public class AgreementNoteDelegate {

	/* Instance factories. */
	
	private static final String DUPLICATE_AGREEMENT_NOTE_MSG = "Agreement Note "
			+ "exists";

	private final InstanceFactory<AgreementNote> agreementNoteFactory;
	
	/* DAOs. */
	
	private final AgreementNoteDao agreementNoteDao;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	/**
	 * Instantiates delegate for caution descriptions.
	 * 
	 * @param cautionDescriptionInstanceFactory instance factory for
	 * caution descriptions
	 * @param cautionDescriptionDao data access object for caution descriptions
	 */
	public AgreementNoteDelegate(
			final InstanceFactory<AgreementNote> agreementNoteInstanceFactory,
			final AgreementNoteDao agreementNoteDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.agreementNoteFactory = agreementNoteInstanceFactory;
		this.agreementNoteDao = agreementNoteDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/** Creates an Agreement Note.
	 * @param date - Date
	 * @param agreement - agreement.
	 * @param description - Description.
	 * @return Agreement Note. 
	 * @throws DuplicateEntityFoundException - when Agreement Note
	 *  already exists. */
	public AgreementNote create(final Date date, final Agreement agreement,
			final String description)
				throws DuplicateEntityFoundException {
		if(this.agreementNoteDao.find(date, description, agreement) 
				!= null) {
			throw new DuplicateEntityFoundException(
					DUPLICATE_AGREEMENT_NOTE_MSG);
		}
		AgreementNote agreementNote
			= this.agreementNoteFactory.createInstance();

		agreementNote.setAgreement(agreement);
		agreementNote.setDescription(description);
		agreementNote.setDate(date);
		agreementNote.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		agreementNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		return this.agreementNoteDao.makePersistent(agreementNote);
	 }
	 
	 
	/** Updates an existing Agreement Note.
	 * @param agreementNote - Agreement Note.
	 * @param description - description.
	 * @param date - Date
	 * @return Agreement Note. 
	 * @throws DuplicateEntityFoundException - when Agreement Note already exists.
	 */
	public AgreementNote update(final AgreementNote agreementNote,
			final String description, final Date date)
				throws DuplicateEntityFoundException {
		if(this.agreementNoteDao.findExcluding(agreementNote, date, description, 
				agreementNote.getAgreement()) != null) {
			throw new DuplicateEntityFoundException(
					DUPLICATE_AGREEMENT_NOTE_MSG);
		}
		agreementNote.setDescription(description);
		agreementNote.setDate(date);
		agreementNote.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		return this.agreementNoteDao.makePersistent(agreementNote);
		
	}
	
	/** Removes a Agreement Note.
	 * @param AgreementNote - agreementNote
	 */
	public void remove(final AgreementNote agreementNote){
		this.agreementNoteDao.makeTransient(agreementNote);
	}
	
	/**
	 * Returns a List of AgreementNotes with the specified Agreement
	 * @param agreement - Agreement
	 * @return List of AgreementNotes with the specified Agreement
	 */
	public List<AgreementNote> findByAgreement(final Agreement agreement){
		return this.agreementNoteDao.findByAgreement(agreement);
	}
}
