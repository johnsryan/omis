package omis.travelpermit.dao.impl.hibernate;

import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.travelpermit.dao.TravelMethodDao;
import omis.travelpermit.domain.TravelMethod;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for travel method.
 * 
 * @author Yidong Li
 * @version 0.1.0 (Aug 18, 2016)
 * @since OMIS 3.0
 */
public class TravelMethodDaoHibernateImpl
		extends GenericHibernateDaoImpl<TravelMethod>
		implements TravelMethodDao {
	/* Queries. */
	private static final String FIND_EXISTING_TRAVEL_METHODS_QUERY_NAME
	= "findExistingTravelMethods";
	
	/* Parameters. */
	/*private static final String OffENDER_PARAMETER_NAME = "offender";
	private static final String CATEGORY_PARAMETER_NAME = "category";
	private static final String ASSIGNED_DATE_PARAMETER_NAME = "assignedDate";
	private static final String EXCLUDED_WORK_ASSIGNMENT_PARAMETER_NAME 
		= "excludedWorkAssignment";*/
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * travel method.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public TravelMethodDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TravelMethod> findTravelMethods(){
		@SuppressWarnings("unchecked")
		List<TravelMethod> travelMethods = (List<TravelMethod>) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_EXISTING_TRAVEL_METHODS_QUERY_NAME)
			.list();
		return travelMethods;
	}
}