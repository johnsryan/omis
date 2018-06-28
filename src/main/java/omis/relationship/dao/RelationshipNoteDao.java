package omis.relationship.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;

/**
 * Data access object for relationship notes.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Nov 7, 2017)
 * @since OMIS 3.0
 */
public interface RelationshipNoteDao
		extends GenericDao<RelationshipNote> {

	/**
	 * Returns notes by relationship.
	 * 
	 * @param relationship relationship
	 * @return notes by relationship
	 */
	List<RelationshipNote> findByRelationship(Relationship relationship);

	/**
	 * Returns relationship note.
	 * 
	 * @param relationship relationship
	 * @param category category
	 * @param value value
	 * @param date date
	 * @return relationship note
	 */
	RelationshipNote find(Relationship relationship,
			RelationshipNoteCategory category, String value, Date date);

	/**
	 * Removes notes by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of notes removed
	 */
	int removeByRelationship(Relationship relationship);
	
	/**
	 * Returns relationship note with notes excluded
	 * 
	 * @param relationship relationship
	 * @param category category
	 * @param value value
	 * @param date date
	 * @param excludedNotes notes to exclude
	 * @return relationship note with notes excluded
	 */
	RelationshipNote findExcluding(Relationship relationship,
			RelationshipNoteCategory category, String value, Date date,
			RelationshipNote... excludedNotes);

	/**
	 * Returns count of relationship notes by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of relationship notes by relationship
	 */
	long countByRelationship(Relationship relationship);
}