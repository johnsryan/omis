package omis.residence.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.residence.dao.NonResidenceTermDao;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceStatus;

/**
 * Hibernate implementation of non residence term data access object.
 * 
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 19, 2015)
 * @since  OMIS 3.0
 */
public class NonResidenceTermDaoHibernateImpl 
				extends GenericHibernateDaoImpl<NonResidenceTerm> 
				implements NonResidenceTermDao {

	/* Queries. */
	private static final String FIND_NON_RESIDENCE_TERM_EXCLUDING_QUERY_NAME
		= "findNonResidenceTermExcluding";
	
	private static final String FIND_NON_RESIDENCE_TERM_QUERY_NAME
		= "findNonResidenceTerm";
	
	private static final String 
		FIND_LOCATIONS_WITHIN_DATE_RANGE_EXCLUDING_QUERY_NAME
		= "findLocationsWithinDateRangeExcluding";
	
	private static final String FIND_LOCATIONS_WITHIN_DATE_RANGE_QUERY_NAME
		= "findLocationsWithinDateRange";
	
	private static final String 
		FIND_NON_RESIDENCE_TERMS_BY_DATE_RANGE_QUERY_NAME 
		= "findNonResidenceTermsByDateRange";
	
	private static final String 
		FIND_NON_RESIDENCE_TERMS_BY_DATE_RANGE_EXCLUDING_QUERY_NAME
		= "findNonResidenceTermsByDateRangeExcluding";

	private static final String FIND_ASSOCIATED_NON_RESIDENCE_TERM
		= "findNonResidenceTermsAssociatedWithPerson";
	
	private static final String FIND_BY_PERSON_ON_DATE_QUERY_NAME
		= "findNonResidenceTermsByPersonOnDate";
	
	/* Parameters. */
	private static final String PERSON_PARAMETER_NAME = "person";
	
	private static final String START_DATE_PARAMETER_NAME = "startDate";
	
	private static final String END_DATE_PARAMETER_NAME = "endDate";
	
	private static final String NON_RESIDENCE_TERM_PARAMETER_NAME 
		= "nonResidenceTerm";
	
	private static final String STATUS_PARAMETER_NAME = "status";
	
	private static final String LOCATION_PARAMETER_NAME = "location";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String STATE_PARAM_NAME = "state";
	
	private static final String CITY_PARAM_NAME = "city";
	
	/* Property names. */
	private static final String LOCATION_PROPERTY_NAME = "location";

	private static final String STATE_PROPERTY_NAME = "state";
	
	private static final String CITY_PROPERTY_NAME = "city";
		
	/**
	 * Instantiates a hibernate implementation of the data access object for 
	 * non residence term.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public NonResidenceTermDaoHibernateImpl(final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm findExcluding(final Person person, 
			final DateRange dateRange, final Location location, 
			final State state, final City city, final ResidenceStatus status, 
			final NonResidenceTerm nonResidenceTerm) {
		NonResidenceTerm term = (NonResidenceTerm) 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_NON_RESIDENCE_TERM_EXCLUDING_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
				.setParameter(LOCATION_PARAMETER_NAME, location, 
						this.getEntityPropertyType(LOCATION_PROPERTY_NAME))
				.setParameter(STATE_PARAM_NAME, state, 
						this.getEntityPropertyType(STATE_PROPERTY_NAME))
				.setParameter(CITY_PARAM_NAME, city, 
						this.getEntityPropertyType(CITY_PROPERTY_NAME))
				.setParameter(STATUS_PARAMETER_NAME, status)
				.setParameter(NON_RESIDENCE_TERM_PARAMETER_NAME, 
						nonResidenceTerm)
				.uniqueResult();
		return term;
	}

	/** {@inheritDoc} */
	@Override
	public NonResidenceTerm find(final Person person, 
			final DateRange dateRange, final Location location, 
			final State state, final City city, final ResidenceStatus status) {
		NonResidenceTerm nonResidenceTerm = (NonResidenceTerm) 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_NON_RESIDENCE_TERM_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
				.setParameter(LOCATION_PARAMETER_NAME, location, 
						this.getEntityPropertyType(LOCATION_PROPERTY_NAME))
				.setParameter(STATE_PARAM_NAME, state, 
						this.getEntityPropertyType(STATE_PROPERTY_NAME))
				.setParameter(CITY_PARAM_NAME, city, 
						this.getEntityPropertyType(CITY_PROPERTY_NAME))
				.setParameter(STATUS_PARAMETER_NAME, status)
				.uniqueResult();
		return nonResidenceTerm;
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findWithinDateRangeExcluding(final Person person,
			final DateRange dateRange, 
			final NonResidenceTerm nonResidenceTerm) {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
						FIND_LOCATIONS_WITHIN_DATE_RANGE_EXCLUDING_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
				.setParameter(NON_RESIDENCE_TERM_PARAMETER_NAME, 
						nonResidenceTerm)
				.list();
		return locations;
	}

	/** {@inheritDoc} */
	@Override
	public List<Location> findWithinDateRange(final Person person, 
			final DateRange dateRange) {
		@SuppressWarnings("unchecked")
		List<Location> locations = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_LOCATIONS_WITHIN_DATE_RANGE_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())	
				.list();
		return locations;
	}

	/** {@inheritDoc} */
	@Override
	public List<NonResidenceTerm> findNonResidenceTermByPersonAndDateRange(
			final Person person, final DateRange dateRange) {
		@SuppressWarnings("unchecked")
		List<NonResidenceTerm> nonResidenceTerms = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_NON_RESIDENCE_TERMS_BY_DATE_RANGE_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
				.list();
		return nonResidenceTerms;
	}

	/** {@inheritDoc} */
	@Override
	public List<NonResidenceTerm> 
		findNonResidenceTermByPersonAndDateRangeExcluding(
			final Person person, final DateRange dateRange, 
			final NonResidenceTerm nonResidenceTerm) {
		@SuppressWarnings("unchecked")
		List<NonResidenceTerm> nonResidenceTerms = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
					FIND_NON_RESIDENCE_TERMS_BY_DATE_RANGE_EXCLUDING_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
				.setParameter(
						NON_RESIDENCE_TERM_PARAMETER_NAME, nonResidenceTerm)
				.list();
		return nonResidenceTerms;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<NonResidenceTerm> findAssociatedNonResidenceTerms(
			final Person person, final DateRange dateRange) {
		@SuppressWarnings("unchecked")
		List<NonResidenceTerm> nonResidenceTerms = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_ASSOCIATED_NON_RESIDENCE_TERM)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						DateRange.getStartDate(dateRange))
				.setTimestamp(END_DATE_PARAMETER_NAME,
						DateRange.getEndDate(dateRange))
				.list();
		return nonResidenceTerms;
	}

	/** {@inheritDoc} */
	@Override
	public List<NonResidenceTerm> findByPersonAndDate(final Person person, final Date date) {
		@SuppressWarnings("unchecked")
		List<NonResidenceTerm> terms = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_PERSON_ON_DATE_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setDate(DATE_PARAM_NAME, date)
				.list();
		return terms;
	}
}