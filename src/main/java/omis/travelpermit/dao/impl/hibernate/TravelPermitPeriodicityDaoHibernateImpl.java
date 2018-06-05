package omis.travelpermit.dao.impl.hibernate;

import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.travelpermit.dao.TravelPermitPeriodicityDao;
import omis.travelpermit.domain.TravelPermitPeriodicity;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for travel permits.
 * 
 * @author Yidong Li
 * @author Joel NOrris
 * @version 0.1.2 (June 06, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitPeriodicityDaoHibernateImpl
		extends GenericHibernateDaoImpl<TravelPermitPeriodicity>
		implements TravelPermitPeriodicityDao {
	
	/* Queries. */
	
	private static final String 
	FIND_EXISTING_TRAVEL_PERMIT_PERIODICITIES_QUERY_NAME
		= "findTravelPermitPeriodicities";
	private static final String FIND_PERIODICITY_QUERY_NAME
		= "findTravelPermitPeriodicity";

	/* Parameters. */
	private static final String NAME_PARAM_NAME = "name";
	
	/* Constructor. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * travel permit periodicity.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public TravelPermitPeriodicityDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<TravelPermitPeriodicity> findAllPeriodicities(){
		@SuppressWarnings("unchecked")
		List<TravelPermitPeriodicity> travelMethods = (List<TravelPermitPeriodicity>) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXISTING_TRAVEL_PERMIT_PERIODICITIES_QUERY_NAME)
			.list();
		return travelMethods;
	}

	/** {@inheritDoc} */
	@Override
	public TravelPermitPeriodicity find(final String name) {
		TravelPermitPeriodicity periodicity = (TravelPermitPeriodicity) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_PERIODICITY_QUERY_NAME)
				.setParameter(NAME_PARAM_NAME, name)
				.uniqueResult();
		return periodicity;
	}
}