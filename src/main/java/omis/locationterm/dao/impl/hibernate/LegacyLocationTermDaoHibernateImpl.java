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
 * Legacy Hibernate implementation of data access object for location terms.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class LegacyLocationTermDaoHibernateImpl
		extends GenericHibernateDaoImpl<LocationTerm>
		implements LocationTermDao {

	private static final String FIND_BY_OFFENDER_ON_DATE_QUERY_NAME
		= "findLegacyLocationTermByOffenderOnDate";
	
	private static final String OFFENDER_ID_PARAM_NAME = "offenderId";

	private static final String EFFECTIVE_DATE_PARAM_NAME = "effectiveDate";

	private static final String END_LEGACY_LOCATION_QUERY_NAME
		= "endLegacyLocation";
	
	/**
	 * Instantiates legacy Hibernate implementation of data access object for
	 * location terms.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public LegacyLocationTermDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findByOffender(final Offender offender) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm find(final Offender offender,
			final Date startDate, final Date endDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findExcluding(final Offender offender,
			final Date startDate, final Date endDate,
			final LocationTerm... excludedLocationTerms) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findByOffenderOnDate(
			final Offender offender, final Date date) {
		LocationTerm locationTerm = (LocationTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_ID_PARAM_NAME, offender.getId())
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, date)
				.setReadOnly(true)
				.uniqueResult();
		return locationTerm;
	}

	/** {@inheritDoc} */
	@Override
	public long count(
			final Offender offender, final Date startDate, final Date endDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public long countExcluding(
			final Offender offender, final Date startDate, final Date endDate,
			final LocationTerm... excludedLocationTerms) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findBySupervisoryOrganizationBetweenDates(
			final SupervisoryOrganization supervisoryOrganization,
			final Offender offender, final Date startDate, final Date endDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public List<LocationTerm> findByOffenderBetweenDates(
			final Offender offender, final Date startDate, Date endDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/** {@inheritDoc} */
	@Override
	public long countAfterDateExcluding(final Offender offender,
			final Date startDate, final LocationTerm excludedLocationTerm) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/** {@inheritDoc} */
	@Override
	public LocationTerm endLocationTerm(
			final Offender offender, final Date effectiveDate) {
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(END_LEGACY_LOCATION_QUERY_NAME)
			.setParameter(OFFENDER_ID_PARAM_NAME, offender.getId())
			.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
			.executeUpdate();
		return (LocationTerm) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_ID_PARAM_NAME, offender.getId())
				.setTimestamp(EFFECTIVE_DATE_PARAM_NAME, effectiveDate)
				.setReadOnly(true)
				.uniqueResult();
	}

	/** {@inheritDoc} */
	@Override
	public LocationTerm findWithStartDate(
			final Offender offender, final Date startDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}