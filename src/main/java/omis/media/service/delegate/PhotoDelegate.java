package omis.media.service.delegate;

import java.util.Date;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.instance.factory.InstanceFactory;
import omis.media.dao.PhotoDao;
import omis.media.domain.Photo;
import omis.media.exception.PhotoExistsException;

/**
 * Photo delegate.
 * 
 * @author Joel Norris
 * @version 0.1.0 (July 9, 2018)
 * @since OMIS 3.0
 */
public class PhotoDelegate {

	/* Data access objects. */
	private PhotoDao photoDao;
	
	/* Instance factories. */
	private InstanceFactory<Photo> photoInstanceFactory;
	
	/* Helpers. */
	private AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	public PhotoDelegate(final PhotoDao photoDao,
			final AuditComponentRetriever auditComponentRetriever,
			final InstanceFactory<Photo> photoInstanceFactory) {
		this.photoDao = photoDao;
		this.auditComponentRetriever = auditComponentRetriever;
		this.photoInstanceFactory = photoInstanceFactory;
	}
	
	/**
	 * Creates a photo with the specified filename and date.
	 * 
	 * @param filename filename
	 * @param date date
	 * @return newly created photo
	 * @throws PhotoExistsException Thrown when a duplicate photo is found.
	 */
	public Photo create(final String filename, final Date date)
			throws PhotoExistsException{
		if (this.photoDao.find(filename) != null) {
			throw new PhotoExistsException("Photo exists.");
		}
		Photo photo = photoInstanceFactory.createInstance();
		photo.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		photo.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		photo.setDate(date);
		photo.setFilename(filename);
		return this.photoDao.makePersistent(photo);
	}
	
	/**
	 * Removes the specified photo.
	 * 
	 * @param photo photo
	 */
	public void remove(final Photo photo) {
		this.photoDao.makeTransient(photo);
	}
}