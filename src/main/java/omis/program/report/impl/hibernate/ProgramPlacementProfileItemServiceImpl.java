package omis.program.report.impl.hibernate;

import java.util.Date;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.program.report.ProgramPlacementProfileItemService;

public class ProgramPlacementProfileItemServiceImpl 
	implements ProgramPlacementProfileItemService {

	private static final String 
		FIND_PROGRAM_PLACEMENTS_BY_OFFENDER_ON_DATE_QUERY_NAME 
			= "findProgramPlacementsByOffenderOnDate";
	
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private final SessionFactory sessionFactory;

	/**
	 * Constructor.
	 * 
	 * @param sessionFactory session factory
	 */
	public ProgramPlacementProfileItemServiceImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean findProgramPlacementExistenceByOffenderOnDate(
			final Offender offender, final Date effectiveDate) {
		return !this.sessionFactory.getCurrentSession()
				.getNamedQuery(
					FIND_PROGRAM_PLACEMENTS_BY_OFFENDER_ON_DATE_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(DATE_PARAM_NAME, effectiveDate)
				.list().isEmpty();
	}
}
