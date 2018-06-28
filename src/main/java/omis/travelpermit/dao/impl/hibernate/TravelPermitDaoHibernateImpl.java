package omis.travelpermit.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;
import omis.travelpermit.dao.TravelPermitDao;
import omis.travelpermit.domain.TravelPermit;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for travel permit.
 * 
 * @author Yidong Li
 * @version 0.1.0 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public class TravelPermitDaoHibernateImpl
		extends GenericHibernateDaoImpl<TravelPermit>
		implements TravelPermitDao {
	/* Queries. */
	private static final String FIND_EXISTING_TRAVEL_PERMIT_QUERY_NAME
		= "findExistingTravelPermit";
	private static final String  FIND_EXCLUDED_EXISTING_TRAVEL_PERMIT_QUERY_NAME
		= "findExcludedExistingTravelPermit";
	private static final String  FIND_EXISTING_TRAVEL_PERMIT_BY_OFFENDER_QUERY_NAME
	= "findExistingTravelPermitByOffender";
	
	/* Parameters. */
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	private static final String START_DATE_PARAMETER_NAME = "startDate";
	private static final String PERMIT_PARAMETER_NAME = "permit";
		
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * travel permit.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public TravelPermitDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public TravelPermit findExistingTravelPermit(final Offender offender,
		final Date startDate) {
		TravelPermit travelPermit;
		travelPermit = (TravelPermit) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXISTING_TRAVEL_PERMIT_QUERY_NAME)
			.setParameter(OFFENDER_PARAMETER_NAME, offender)
			.setParameter(START_DATE_PARAMETER_NAME, startDate)
			.uniqueResult();
		return travelPermit;
	}
	
	/** {@inheritDoc} */
	@Override
	public TravelPermit findExcludedExistingTravelPermit(
		final TravelPermit permit, final Offender offender,
		final Date startDate) {
		TravelPermit travelPermit;
		travelPermit = (TravelPermit) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXCLUDED_EXISTING_TRAVEL_PERMIT_QUERY_NAME)
			.setParameter(PERMIT_PARAMETER_NAME, permit)
			.setParameter(START_DATE_PARAMETER_NAME, startDate)
			.setParameter(OFFENDER_PARAMETER_NAME, offender)
			.uniqueResult();
		return travelPermit;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelPermit> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<TravelPermit> travelPermits = (List<TravelPermit>) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXISTING_TRAVEL_PERMIT_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAMETER_NAME, offender)
			.list();
		return travelPermits;
	}
}