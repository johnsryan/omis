package omis.media.dao;

import omis.dao.GenericDao;
import omis.media.domain.Photo;

/**
 * Data access object for photos.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.1 (July 9, 2018)
 * @since OMIS 3.0
 */
public interface PhotoDao
		extends GenericDao<Photo> {
	
	/**
	 * Returns the photo with the specified filename.
	 * 
	 * @param filename filename
	 * @return photo
	 */
	Photo find(String filename);

	/**
	 * Returns the next database generated filename distinguisher for photos.
	 * 
	 * <p>An implementation may not use database generated distinguisher for
	 * photo filenames. In such an implementation, an
	 * {@code UnsupportedOperationException} will be thrown.
	 * 
	 * @return next database generated filename distinguisher
	 * @throws UnsupportedOperationException if the implementation does not
	 * support database generated filename distinguisher for photos
	 */
	String findNextFilenameDistinguisher();
}