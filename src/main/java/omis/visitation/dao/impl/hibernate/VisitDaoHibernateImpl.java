package omis.visitation.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.facility.domain.Facility;
import omis.offender.domain.Offender;
import omis.visitation.dao.VisitDao;
import omis.visitation.domain.Visit;
import omis.visitation.domain.VisitationAssociation;

import org.hibernate.SessionFactory;

/**
 * Visit DAO Hibernate Impl.
 * 
 * @author Joel Norris
 * @version 0.1.1 (Sept 23, 2014)
 * @since OMIS 3.0
 */
public class VisitDaoHibernateImpl 
	extends GenericHibernateDaoImpl<Visit>
	implements VisitDao {
	
	/* Query names. */
	
	private static final String FIND_VISITS_BY_OFFENDER 
		="findVisitsByOffender";
	
	private static final String FIND_VISIT_QUERY_NAME = "findVisit";
	
	private static final String FIND_VISIT_EXCLUDING_QUERY_NAME 
		= "findVisitExcluding";
	
	private static final String FIND_VISITS_IN_RANGE_QUERY_NAME
		= "findVisitsInRange";
	
	private static final String FIND_VISITS_IN_RANGE_EXCLUDING_QUERY_NAME
		= "findVisitsInRangeExcluding";
	
	private static final String FIND_VISITS_BY_OFFENDER_ON_DATE_QUERY_NAME
		= "findVisitsByOffenderOnDate";
	
	private static final String
	FIND_OFFENDERS_WITH_VISITS_BY_FACILITY_ON_DATE_QUERY_NAME
		= "findOffendersWithVisitsByFacilityOnDate";
	
	private static final String FIND_VISITS_BY_ASSOCIATION_QUERY_NAME
		= "findVisitsByVisitationAssociation";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String VISITATION_ASSOCIATION_PARAMETER_NAME
		= "visitationAssociation";
	
	private static final String VISIT_PARAMETER_NAME = "visit";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String LOCATION_PARAM_NAME = "location";
	
	/**
	 * Instantiates a data access object for visit with the specified 
	 * session factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public VisitDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<Visit> findVisitByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<Visit> visits = getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISITS_BY_OFFENDER)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.list();
		return visits;
	}

	/** {@inheritDoc} */
	@Override
	public Visit findVisit(final VisitationAssociation visitationAssociation,
			final Date startDate) {
		Visit visitFound = (Visit) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISIT_QUERY_NAME)
				.setParameter(VISITATION_ASSOCIATION_PARAMETER_NAME, 
						visitationAssociation)
				.setParameter(START_DATE_PARAM_NAME, startDate)
				.uniqueResult();
		return visitFound;
	}

	/** {@inheritDoc} */
	@Override
	public Visit findVisitExcluding(final Visit visit, 
			final VisitationAssociation association, final Date startDate, 
			final Date endDate) {
		Visit visitFound = (Visit) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISIT_EXCLUDING_QUERY_NAME)
				.setParameter(VISIT_PARAMETER_NAME, visit)
				.setParameter(VISITATION_ASSOCIATION_PARAMETER_NAME, 
						association)
				.setParameter(START_DATE_PARAM_NAME, startDate)
				.uniqueResult();
		return visitFound;
	}

	/** {@inheritDoc} */
	@Override
	public List<Visit> findVisitsInRange(
			final VisitationAssociation visitationAssociation,
			final Date startDate, final Date endDate) {
		System.out.println("SD:" + startDate + " ED:" + endDate);
		@SuppressWarnings("unchecked")
		List<Visit> visits = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISITS_IN_RANGE_QUERY_NAME)
				.setParameter(VISITATION_ASSOCIATION_PARAMETER_NAME,
						visitationAssociation)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.list();
		return visits;
	}

	/** {@inheritDoc} */
	@Override
	public List<Visit> findVisitsInRangeExcluding(
			final VisitationAssociation association, final Date startDate, 
			final Date endDate, final Visit visit) {
		@SuppressWarnings("unchecked")
		List<Visit> visits = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISITS_IN_RANGE_EXCLUDING_QUERY_NAME)
				.setParameter(VISIT_PARAMETER_NAME, visit)
				.setParameter(VISITATION_ASSOCIATION_PARAMETER_NAME,
						association)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.list();
		return visits;
	}

	/** {@inheritDoc} */
	@Override
	public List<Visit> findVisitsByOffenderOnDate(final Offender offender,
			final Date date) {
		@SuppressWarnings("unchecked")
		List<Visit> visits = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISITS_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setDate(DATE_PARAM_NAME, date)
				.list();
		return visits;
	}

	/** {@inheritDoc} */
	@Override
	public List<Offender> findOffendersWithVisitsByFacilityOnDate(Facility facility, Date date) {
		@SuppressWarnings("unchecked")
		List<Offender> offenders = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_OFFENDERS_WITH_VISITS_BY_FACILITY_ON_DATE_QUERY_NAME)
				.setParameter(LOCATION_PARAM_NAME, facility.getLocation())
				.setDate(DATE_PARAM_NAME, date)
				.list();
		return offenders;
	}

	/** {@inheritDoc} */
	@Override
	public List<Visit> findVisitsByAssociation(VisitationAssociation association) {
		@SuppressWarnings("unchecked")
		List<Visit> visits = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISITS_BY_ASSOCIATION_QUERY_NAME)
				.setParameter(VISITATION_ASSOCIATION_PARAMETER_NAME, association)
				.list();
		return visits;
	}
}