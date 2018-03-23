package omis.residence.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.address.domain.Address;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.residence.dao.ResidenceTermDao;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;

/**
 * Hibernate implementation of the residence term data access object.
 * 
 * @author Sheronda Vaughn
 * @author Joel Norris
 * @version 0.1.1 (May 12, 2015)
 * @since  OMIS 3.0
 */
public class ResidenceTermDaoHibernateImpl 
				extends GenericHibernateDaoImpl<ResidenceTerm>
				implements ResidenceTermDao {

	/* Queries. */
	
	private static final String FIND_EXCLUDING_QUERY_NAME 
		= "findResidenceTermExcluding";
	
	private static final String 
		FIND_LOCATIONS_WITHIN_DATE_RANGE_EXCLUDING_QUERY_NAME
		= "findRTLocationsWithinDateRangeExcluding";
	
	private static final String FIND_LOCATIONS_WITHIN_DATE_RANGE_QUERY_NAME
		= "findRTLocationsWithinDateRange";
	
	private static final String FIND_RESIDENCE_TERM_QUERY_NAME
		= "findResidenceTerm";
	
	private static final String 
		FIND_RESIDENCE_TERMS_BY_PERSON_QUERY_NAME
		= "findResidenceTermsByPerson";
	
	private static final String 
		FIND_RESIDENCE_TERMS_BY_PERSON_EXCLUDING_QUERY_NAME
		= "findResidenceTermsByPersonExcluding";
	
	private static final String 
		FIND_RESIDENCE_BY_PERSON_ON_DATE_QUERY_NAME
		= "findResidenceTermByPersonOnDate";
	
	private static final String FIND_RESIDENCE_TERM_BY_EFFECTIVE_DATE
		= "findResidenceTermByEffectiveDate";
	
	private static final String
		FIND_RESIDENCE_TERM_ASSOCIATED_WITH_PERSON_QUERY_NAME
		= "findResidenceTermsAssociatedWithPerson";
	
	private static final String FIND_RESIDENCE_TERMS_BY_OFFENDER 
		= "findResidenceTermsByOffender";
	
	/* Parameters. */
	
	private static final String PERSON_PARAMETER_NAME = "person";
	
	private static final String START_DATE_PARAMETER_NAME = "startDate";
	
	private static final String END_DATE_PARAMETER_NAME = "endDate";
	
	private static final String ADDRESS_PARAMETER_NAME = "address";
	
	private static final String RESIDENCE_TERM_PARAMETER_NAME = "residenceTerm";
	
	private static final String RESIDENCE_CATEGORY_PARAMETER_NAME = "category";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";
	
	private static final String STATUS_PARAMETER_NAME = "status";
	
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	/**
	* Instantiates an hibernate implementation of the data access object for 
	* residence term.
	* 
	* @param sessionFactory session factory
	* @param entityName entity name
	*/
	public ResidenceTermDaoHibernateImpl(final SessionFactory sessionFactory,
				final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public ResidenceTerm find(final Person person, final DateRange dateRange, 
			final Address address) {
		ResidenceTerm residenceTerm = 
				(ResidenceTerm) this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_RESIDENCE_TERM_QUERY_NAME)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setTimestamp(START_DATE_PARAMETER_NAME, 
					DateRange.getStartDate(dateRange))
			.setTimestamp(END_DATE_PARAMETER_NAME,
					DateRange.getEndDate(dateRange))
			.setParameter(ADDRESS_PARAMETER_NAME, address)
			.uniqueResult();
		return residenceTerm;
	}
	
	/**{@inheritDoc} */
	@Override
	public ResidenceTerm findExcluding(final Person person, 
			final DateRange dateRange, final Address address, 
			final ResidenceTerm residenceTerm) {
		ResidenceTerm term = (ResidenceTerm) 
				this.getSessionFactory().getCurrentSession()
			.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setTimestamp(START_DATE_PARAMETER_NAME, 
					DateRange.getStartDate(dateRange))
			.setTimestamp(END_DATE_PARAMETER_NAME, 
					DateRange.getEndDate(dateRange))
			.setParameter(ADDRESS_PARAMETER_NAME, address)
			.setParameter(RESIDENCE_TERM_PARAMETER_NAME, residenceTerm)
			.uniqueResult();
		return term;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findWithinDateRangeExcluding(final Person person,
			final DateRange dateRange, final ResidenceTerm residenceTerm) {
		@SuppressWarnings("unchecked")
	List<Location> locations = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
						FIND_LOCATIONS_WITHIN_DATE_RANGE_EXCLUDING_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						DateRange.getStartDate(dateRange))
				.setTimestamp(END_DATE_PARAMETER_NAME, 
						DateRange.getEndDate(dateRange))
				.setParameter(RESIDENCE_TERM_PARAMETER_NAME, residenceTerm)
				.list();
		return locations;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<Location> findWithinDateRange(final Person person, 
			final DateRange dateRange) {
		@SuppressWarnings("unchecked")
	List<Location> locations = this.getSessionFactory().getCurrentSession()
			.getNamedQuery(
					FIND_LOCATIONS_WITHIN_DATE_RANGE_QUERY_NAME)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setTimestamp(START_DATE_PARAMETER_NAME, 
					DateRange.getStartDate(dateRange))
			.setTimestamp(END_DATE_PARAMETER_NAME, 
					DateRange.getEndDate(dateRange))
			.list();
		return locations;
	}	
	
	/** {@inheritDoc} */
	@Override
	public List<ResidenceTerm> findResidenceTermsByPerson(
			final Person person, final DateRange dateRange) {
		@SuppressWarnings("unchecked")
	List<ResidenceTerm> residenceTerms = this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_RESIDENCE_TERMS_BY_PERSON_QUERY_NAME)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setTimestamp(START_DATE_PARAMETER_NAME, 
					DateRange.getStartDate(dateRange))
			.setTimestamp(END_DATE_PARAMETER_NAME, 
					DateRange.getEndDate(dateRange))
			.list();
		return residenceTerms;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ResidenceTerm> findResidenceTermsByPersonExcluding(
			final Person person, final ResidenceTerm residenceTerm,
			final Address address, final DateRange dateRange) {
		@SuppressWarnings("unchecked")
	List<ResidenceTerm> residenceTerms = this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(
					FIND_RESIDENCE_TERMS_BY_PERSON_EXCLUDING_QUERY_NAME)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setParameter(RESIDENCE_TERM_PARAMETER_NAME, residenceTerm)
			.setParameter(ADDRESS_PARAMETER_NAME, address)
			.setTimestamp(START_DATE_PARAMETER_NAME, dateRange.getStartDate())
			.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
			.list();
		return residenceTerms;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ResidenceTerm> findAssociatedResidenceTerms(final Person person,
			final DateRange dateRange) {
		@SuppressWarnings("unchecked")
		List<ResidenceTerm> residenceTerms = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_RESIDENCE_TERM_ASSOCIATED_WITH_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAMETER_NAME, person)	
				.setTimestamp(START_DATE_PARAMETER_NAME, 
						dateRange.getStartDate())
				.setTimestamp(END_DATE_PARAMETER_NAME, dateRange.getEndDate())
				.list();
		return residenceTerms;
	}
	
	/** {@inheritDoc} */
	@Override
	public ResidenceTerm findByPersonAndDate(final Person person, 
			final Date date, final ResidenceCategory category) {
		ResidenceTerm primaryResidence = (ResidenceTerm) 
			this.getSessionFactory().getCurrentSession()
			.getNamedQuery(
					FIND_RESIDENCE_BY_PERSON_ON_DATE_QUERY_NAME)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setParameter(RESIDENCE_CATEGORY_PARAMETER_NAME, category)
			.setDate(DATE_PARAM_NAME, date)
			.uniqueResult();
		return primaryResidence;
	}
	
	/** {@inheritDoc} */
	@Override
	public ResidenceTerm findOnDate(final Person person, 
			final Date effectiveDate, final ResidenceStatus status, 
			final ResidenceCategory category) {
		ResidenceTerm residenceTerm = (ResidenceTerm) this.getSessionFactory()
			.getCurrentSession().getNamedQuery(
					FIND_RESIDENCE_TERM_BY_EFFECTIVE_DATE)
			.setDate(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			.setParameter(PERSON_PARAMETER_NAME, person)
			.setParameter(STATUS_PARAMETER_NAME, status)
			.setParameter(RESIDENCE_CATEGORY_PARAMETER_NAME, category)
			.uniqueResult();				
		return residenceTerm;
	}

	/** {@inheritDoc} */
	@Override
	public List<ResidenceTerm> findResidenceTermsByOffender(
			final Offender offender, final Date date) {
		@SuppressWarnings("unchecked")
		List<ResidenceTerm> residenceTerms = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_RESIDENCE_TERMS_BY_OFFENDER)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date).list();
		return residenceTerms;
	}	
}