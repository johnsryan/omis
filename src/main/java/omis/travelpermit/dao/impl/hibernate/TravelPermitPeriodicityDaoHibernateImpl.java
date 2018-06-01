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
 * @version 0.1.0 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public class TravelPermitPeriodicityDaoHibernateImpl
		extends GenericHibernateDaoImpl<TravelPermitPeriodicity>
		implements TravelPermitPeriodicityDao {
	/* Queries. */
	private static final String 
	FIND_EXISTING_TRAVEL_PERMIT_PERIODICITIES_QUERY_NAME
	= "findTravelPermitPeriodicities";

	/* Parameters. */
	/*private static final String OffENDER_PARAMETER_NAME = "offender";
	private static final String CATEGORY_PARAMETER_NAME = "category";
	private static final String ASSIGNED_DATE_PARAMETER_NAME = "assignedDate";
	private static final String EXCLUDED_WORK_ASSIGNMENT_PARAMETER_NAME 
		= "excludedWorkAssignment";*/
	
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
}