package omis.relationship.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.instance.factory.InstanceFactory;
import omis.relationship.dao.RelationshipNoteDao;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;
import omis.relationship.exception.RelationshipNoteExistsException;

/**
 * Delegate for relationship notes.
 * 
 * @author Stephen Abson
 * @author Yidong Li
 * @version 0.0.1 (Nov 17, 2017)
 * @since OMIS 3.0
 */
public class RelationshipNoteDelegate {
	
	/* Instance factory. */
	
	private final InstanceFactory<RelationshipNote>
	relationshipNoteInstanceFactory;
	
	/* Data access object. */
	
	private final RelationshipNoteDao relationshipNoteDao;
	
	/* Component retriever. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	/**
	 * Delegate for relationship notes.
	 * 
	 * @param relationshipNoteInstanceFactory instance factory for relationship
	 * notes
	 * @param relationshipNoteDao data access object for relationship notes
	 */
	public RelationshipNoteDelegate(
			final InstanceFactory<RelationshipNote>
				relationshipNoteInstanceFactory,
			final RelationshipNoteDao relationshipNoteDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.relationshipNoteInstanceFactory = relationshipNoteInstanceFactory;
		this.relationshipNoteDao = relationshipNoteDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/* Methods. */
	
	/**
	 * Creates relationship note.
	 * 
	 * @param relationship relationship.
	 * @param category category
	 * @param value value
	 * @param date date
	 * @return created relationship note
	 * @throws RelationshipNoteExistsException if relationship note exists
	 */
	public RelationshipNote create(
			final Relationship relationship,
			final RelationshipNoteCategory category,
			final String value,
			final Date date)
				throws RelationshipNoteExistsException {
		if (this.relationshipNoteDao.find(
				relationship, category, value, date) != null) {
			throw new RelationshipNoteExistsException(
					"Relationship note exists");
		}
		RelationshipNote note = this.relationshipNoteInstanceFactory
				.createInstance();
		note.setRelationship(relationship);
		note.setCategory(category);
		note.setDate(date);
		note.setValue(value);
		note.setCreationSignature(new CreationSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		note.setUpdateSignature(new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
		return this.relationshipNoteDao.makePersistent(note);
	}
	
	/**
	 * Updates relationship note.
	 * 
	 * @param note relationship note to update
	 * @param category category
	 * @param value value
	 * @param date date
	 * @return updated relationship note
	 * @throws RelationshipNoteExistsException if relationship note exists
	 */
	public RelationshipNote update(
			final RelationshipNote note,
			final RelationshipNoteCategory category,
			final String value,
			final Date date)
				throws RelationshipNoteExistsException {
		if (this.relationshipNoteDao.findExcluding(note.getRelationship(),
				category, value, date, note) != null) {
			throw new RelationshipNoteExistsException(
					"Relationship note exists");
		}
		this.populate(note, category, value, date);
		return this.relationshipNoteDao.makePersistent(note);
	}
	
	/**
	 * Removes relationship note.
	 * 
	 * @param note relationship note to remove
	 */
	public void remove(final RelationshipNote note) {
		this.relationshipNoteDao.makeTransient(note);
	}
	
	/**
	 * Removes notes by relationship.
	 * 
	 * @param relationship relationship by which to remove notes
	 * @return count of notes removed
	 */
	public int removeByRelationship(final Relationship relationship) {
		return this.relationshipNoteDao.removeByRelationship(relationship);
	}
	
	/**
	 * Returns notes by relationship.
	 * 
	 * @param relationship relationship by which to return notes
	 * @return notes by relationship
	 */
	public List<RelationshipNote> findByRelationship(
			final Relationship relationship) {
		return this.relationshipNoteDao.findByRelationship(relationship);
	}
	
	/* Helper methods. */
	
	// Populates relationship note
	private void populate(final RelationshipNote note,
			final RelationshipNoteCategory category, final String value,
			final Date date) {
		note.setCategory(category);
		note.setValue(value);
		note.setDate(date);
		note.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
	}

	/**
	 * Returns count of notes by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of notes by relationship
	 */
	public long countByRelationship(Relationship relationship) {
		return this.relationshipNoteDao.countByRelationship(relationship);
	}
}