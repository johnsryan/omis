package omis.warrant.report.impl.hibernate;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.warrant.report.WarrantProfileItemReportService;

/**
 * Warrant profile item report service hibernate implementation.
 * 
 * @author Joel Norris
 * @version 0.1.0 (April 3, 2018)
 * @since OMIS 3.0
 */
public class WarrantProfileItemReportServiceHibernateImpl
	implements WarrantProfileItemReportService {

	/* Query names. */
	
	private static final String COUNT_WARRANTS_QUERY_NAME = "countWarrantsByOffender";
	
	/* Parameter names. */
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	/* Helpers. */
	
	private SessionFactory sessionFactory;
	
	/**
	 * Creates a warrant profile item report service with the specified session factory.
	 * 
	 * @param sessionFactory session factory
	 */
	public WarrantProfileItemReportServiceHibernateImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Integer countWarrantsByOffender(Offender offender) {
		return ((Long) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(COUNT_WARRANTS_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.uniqueResult()).intValue();
	}
}
