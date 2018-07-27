package omis.media.dao.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.media.dao.PhotoDao;
import omis.media.domain.Photo;

/**
 * Hibernate entity configurable data access object for photos.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 8, 2013)
 * @since OMIS 3.0
 */
public class PhotoDaoHibernateImpl
		extends GenericHibernateDaoImpl<Photo>
		implements PhotoDao {
	
	/* Query names. */
	
	private static final String FIND_PHOTO_QUERY_NAME = "findPhotoByFilename";
	private static final String FIND_NEXT_PHOTO_FILENAME = "findNextPhotoFilename";
	
	/* Parameter names. */
	
	private static final String FILENAME_PARAM_NAME = "filename";

	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * photos with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public PhotoDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public String findNextFilenameDistinguisher() {
		return getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_NEXT_PHOTO_FILENAME)
				.uniqueResult().toString();
	}

	/** {@inheritDoc} */
	@Override
	public Photo find(String filename) {
		Photo photo = (Photo) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_PHOTO_QUERY_NAME)
				.setParameter(FILENAME_PARAM_NAME, filename)
				.uniqueResult();
		return photo;
	}
}