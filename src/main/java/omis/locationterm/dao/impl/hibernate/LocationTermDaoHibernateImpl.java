package omis.locationterm.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.locationterm.dao.LocationTermDao;
import omis.locationterm.domain.LocationTerm;
import omis.offender.domain.Offender;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Hibernate implementation of data access object for location terms.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 8, 2013)
 * @since OMIS 3.0
 */
public class LocationTermDaoHibernateImpl
		extends GenericHibernateDaoImpl<LocationTerm>
		implements LocationTermDao {

	/* Query names. */
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME
		= "findLocationTermsByOffender";
	
	private static final String FIND_QUERY_NAME = "findLocationTerm";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findLocationTermExcluding";
	
	private static final String 
		FIND_LOCATION_TERM_BY_OFFENDER_ON_DATE_QUERY_NAME
		= "findLocationTermByOffenderOnDate";
	
	private static final String COUNT_QUERY_NAME
		= "countLocationTermsForOffenderBetweenDates";

	private static final String COUNT_EXCLUDING_QUERY_NAME
		= "countLocationTermsForOffenderBetweenDatesExcluding";
	
	private static final String COUNT_AFTER_DATE_EXCLUDING_QUERY_NAME
	= "countLocationTermsForOffenderAfterDateExcluding";
	
	private static final String
	FIND_BY_SUPERVISORY_ORGANIZATION_BETWEEN_DATES_QUERY_NAME
		= "findLocationTermForSupervisoryOrganizationBetweenDates";
	
	private static final String FIND_FOR_OFFENDER_BETWEEN_DATES_QUERY_NAME
		= "findLocationTermsByOffenderBetweenDates";
	
	private static final String FIND_FOR_OFFENDER_WITH_START_DATE_QUERY_NAME
		= "findLocationTermForOffenderWithStartDate";
	
	/* Parameters. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	private static final String EXCLUDED_LOCATION_TERMS_PARAM_NAME
		= "excludedLocationTerms";

	private static final String EXCLUDED_LOCATION_TERM_PARAM_NAME
		= "excludedLocationTerm";
	
	private static final String DATE_PARAM_NAME = "date";

	private static final String SUPERVISORY_ORGANIZATION_PARAM_NAME
		= "supervisoryOrganization";
	
	/* Constructor. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * location terms with specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public LocationTermDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<LocationTerm> locationTerms
			= this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender).list();
		return locationTerms;
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm find(final Offender offender, final Date startDate,
			final Date endDate) {
		LocationTerm locationTerm = (LocationTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findExcluding(final Offender offender,
			final Date startDate, final Date endDate,
			final LocationTerm... excludedLocationTerms) {
		LocationTerm locationTerm = (LocationTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setParameterList(EXCLUDED_LOCATION_TERMS_PARAM_NAME,
						excludedLocationTerms).uniqueResult();
		return locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findByOffenderOnDate(final Offender offender, 
			final Date date) {
		LocationTerm locationTerm = (LocationTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_LOCATION_TERM_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(DATE_PARAM_NAME, date)
				.uniqueResult();
		return locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public long count(final Offender offender, final Date startDate,
			final Date endDate) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public long countExcluding(final Offender offender, final Date startDate,
			final Date endDate, final LocationTerm... excludedLocationTerms) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate)
				.setParameterList(EXCLUDED_LOCATION_TERMS_PARAM_NAME,
						excludedLocationTerms)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findBySupervisoryOrganizationBetweenDates(
			final SupervisoryOrganization supervisoryOrganization,
			final Offender offender, final Date startDate, final Date endDate) {
		@SuppressWarnings("unchecked")
		List<LocationTerm> terms = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
					FIND_BY_SUPERVISORY_ORGANIZATION_BETWEEN_DATES_QUERY_NAME)
				.setParameter(SUPERVISORY_ORGANIZATION_PARAM_NAME,
						supervisoryOrganization)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate).list();
		return terms;
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findByOffenderBetweenDates(
			final Offender offender, final Date startDate,
			final Date endDate) {
		@SuppressWarnings("unchecked")
		List<LocationTerm> terms = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(
					FIND_FOR_OFFENDER_BETWEEN_DATES_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.setTimestamp(END_DATE_PARAM_NAME, endDate).list();
		return terms;
	}

	/** {@inheritDoc} */
	@Override
	public long countAfterDateExcluding(Offender offender, Date startDate, 
			LocationTerm excludedLocationTerm) {
		long count = (long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_AFTER_DATE_EXCLUDING_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(START_DATE_PARAM_NAME, startDate)
				.setParameter(EXCLUDED_LOCATION_TERM_PARAM_NAME, 
						excludedLocationTerm).uniqueResult();
		return count;
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm findWithStartDate(
			final Offender offender, final Date startDate) {
		LocationTerm locationTerm = (LocationTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_FOR_OFFENDER_WITH_START_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setTimestamp(START_DATE_PARAM_NAME, startDate)
				.uniqueResult();
		return locationTerm;
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm endLocationTerm(
			final Offender offender, final Date effectiveDate) {
		
		// TODO - remove this method once legacy functionality is no longer
		// required - SA
		throw new UnsupportedOperationException(
				"Ending location term not supported - update location term"
					+ " instead");
	}
}