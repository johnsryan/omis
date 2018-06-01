package omis.travelpermit.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.travelpermit.dao.TravelPermitNoteDao;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitNote;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for travel permit note.
 * 
 * @author Yidong Li
 * @version 0.1.0 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public class TravelPermitNoteDaoHibernateImpl
		extends GenericHibernateDaoImpl<TravelPermitNote>
		implements TravelPermitNoteDao {
	/* Queries. */
	private static final String FIND_EXISTING_TRAVEL_PERMIT_NOTE_QUERY_NAME
	= "findExistingTravelPermitNote";
	private static final String 
	FIND_EXCLUDED_EXISTING_TRAVEL_PERMIT_NOTE_QUERY_NAME
	= "findExcludedExistingTravelPermitNote";
	private static final String 
	FIND_EXISTING_TRAVEL_PERMIT_NOTE_BY_PERMIT_QUERY_NAME
	= "findExistingTravelPermitNoteByPermit";
	/* Parameters. */
	private static final String VALUE_PARAMETER_NAME = "value";
	private static final String PERMIT_PARAMETER_NAME = "permit";
	private static final String DATE_PARAMETER_NAME = "date";
	private static final String NOTE_PARAMETER_NAME = "note";
		
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * travel permit note.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public TravelPermitNoteDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public TravelPermitNote findExistingTravelPermitNote(
		final TravelPermit permit, final Date date, final String value) {
		TravelPermitNote travelPermitNote;
		travelPermitNote = (TravelPermitNote) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXISTING_TRAVEL_PERMIT_NOTE_QUERY_NAME)
			.setParameter(PERMIT_PARAMETER_NAME, permit)
			.setParameter(DATE_PARAMETER_NAME, date)
			.setParameter(VALUE_PARAMETER_NAME, value)
			.uniqueResult();
		return travelPermitNote;
	}
	
	/** {@inheritDoc} */
	@Override
	public TravelPermitNote findExcludedExistingTravelPermitNote(
		final TravelPermitNote note, final Date date, final String value){
		TravelPermitNote travelPermitNote;
		travelPermitNote = (TravelPermitNote) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXCLUDED_EXISTING_TRAVEL_PERMIT_NOTE_QUERY_NAME)
			.setParameter(NOTE_PARAMETER_NAME, note)
			.setParameter(DATE_PARAMETER_NAME, date)
			.setParameter(VALUE_PARAMETER_NAME, value)
			.uniqueResult();
		return travelPermitNote;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelPermitNote> findNotes(final TravelPermit permit){
		@SuppressWarnings("unchecked")
		List<TravelPermitNote> travelPermitNotes = (List<TravelPermitNote>) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXISTING_TRAVEL_PERMIT_NOTE_BY_PERMIT_QUERY_NAME)
			.setParameter(PERMIT_PARAMETER_NAME, permit)
			.list();
		return travelPermitNotes;
	}
}