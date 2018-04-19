package omis.stg.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.stg.domain.SecurityThreatGroupActivity;
import omis.stg.domain.SecurityThreatGroupActivityNote;

/**
 * Data access object for security threat group activity note.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Apr 10, 2018)
 * @since OMIS 3.0
 */
public interface SecurityThreatGroupActivityNoteDao
		extends GenericDao<SecurityThreatGroupActivityNote> {

	/**
	 * Returns a list of security threat group activity notes.
	 * 
	 * @param activity security threat group activity 
	 * @return list of security threat group activity notes
	 */
	List<SecurityThreatGroupActivityNote> 
		findNotes(SecurityThreatGroupActivity activity);
	
	/**
	 * Finds security threat group activity notes excluding the specified 
	 * activity note.
	 * 
	 * @param activity security threat group activity 
	 * @param date date
	 * @param value value
	 * @param excludedNote excluded security threat group activity note
	 * @return security threat group activity note
	 */
	SecurityThreatGroupActivityNote findExcluding(
			SecurityThreatGroupActivity activity, Date date, String value,
			SecurityThreatGroupActivityNote excludedNote);
	
	/**
	 * Finds security threat group activity note.
	 * 
	 * @param activity security threat group activity 
	 * @param date date
	 * @param value value
	 * @return security threat group activity note
	 */
	SecurityThreatGroupActivityNote findNote(
			SecurityThreatGroupActivity activity, Date date, String value);
}
