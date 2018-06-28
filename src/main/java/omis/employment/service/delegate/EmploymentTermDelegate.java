package omis.employment.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.audit.domain.VerificationSignature;
import omis.datatype.DateRange;
import omis.employment.dao.EmploymentTermDao;
import omis.employment.domain.Employer;
import omis.employment.domain.EmploymentChangeReason;
import omis.employment.domain.EmploymentTerm;
import omis.employment.domain.component.Job;
import omis.employment.exception.EmploymentExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;

/**
 * Delegate for managing employment term.
 *
 * @author Yidong
 * @author Josh Divine
 * @version 0.0.2 (Dec 13, 2017)
 * @since OMIS 3.0
 */
public class EmploymentTermDelegate {
	/* Resources. */
	private final EmploymentTermDao employmentTermDao;
	private final InstanceFactory<EmploymentTerm> employmentTermInstanceFactory;
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for managing employment term.
	 * 
	 * @param employmentTermDao data access object for employment term
	 * @param employmentTermInstanceFactory employment term instance factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public EmploymentTermDelegate(
		final EmploymentTermDao employmentTermDao,
		final InstanceFactory<EmploymentTerm> employmentTermInstanceFactory,
		final AuditComponentRetriever auditComponentRetriever) {
		this.employmentTermDao = employmentTermDao;
		this.employmentTermInstanceFactory = employmentTermInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}

	/* Methods. */
	
	/**
	 * Creates employment term.
	 * 
	 * @param offender offender
	 * @param dateRange date range
	 * @param job job
	 * @param convictedOfEmployerTheft, if this person is convicted of theft
	 * @param endReason the reason of termination of employment 
	 * @return created employer
	 * @throws EmploymentExistsException if duplicate entity exists
	 */
	public EmploymentTerm create(final Offender offender, 
		final DateRange dateRange, final Job job, 
		final Boolean convictedOfEmployerTheft, 
		final EmploymentChangeReason endReason, 
		final VerificationSignature	verificationSignature) 
				throws EmploymentExistsException {
		if (this.employmentTermDao.find(offender, job.getEmployer(), dateRange,
				job, endReason) != null) {
			throw new EmploymentExistsException(
					"Employment term already exists.");
		}
		EmploymentTerm employmentTerm = this.employmentTermInstanceFactory
				.createInstance();
		employmentTerm.setOffender(offender);
		employmentTerm.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		populateEmploymentTerm(employmentTerm, dateRange, job, 
				convictedOfEmployerTheft, endReason, verificationSignature);
		return this.employmentTermDao.makePersistent(employmentTerm);
	}

	/**
	 * Updates employment term.
	 * 
	 * @param employmentTerm employment term to be updated
	 * @param dateRange date range
	 * @param job job
	 * @param convictedOfEmployerTheft if ever convicted of employer theft
	 * @param endReason employment end reason
	 * @param verificationSignature verification signature
	 * @return updated employment term
	 * @throws EmploymentExistsException if duplicate entity exists
	 */
	public EmploymentTerm update(final EmploymentTerm employmentTerm, 
		final DateRange dateRange, final Job job, 
		final Boolean convictedOfEmployerTheft,	
		final EmploymentChangeReason endReason, 
		final VerificationSignature	verificationSignature) 
				throws EmploymentExistsException {
		if (this.employmentTermDao.findExcluding(employmentTerm, dateRange,
				job, endReason) != null) {
			throw new EmploymentExistsException(
					"Employment term already exists.");
		}
		populateEmploymentTerm(employmentTerm, dateRange, job, 
				convictedOfEmployerTheft, endReason, verificationSignature);
		return this.employmentTermDao.makePersistent(employmentTerm);
	}
	
	/**
	 * Changes an employment term by employer.
	 * 
	 * @param employmentTerm employment term to be updated
	 * @param employer employer
	 * @return updated employment term
	 * @throws EmploymentExistsException if duplicate entity exists
	 */
	public EmploymentTerm change(final EmploymentTerm employmentTerm, 
		final Employer employer) throws EmploymentExistsException {
		if (this.employmentTermDao.findEmploymentTermByEmployer(employmentTerm, 
				employer) != null) {
			throw new EmploymentExistsException(
					"Employment term already exists.");
		}
		employmentTerm.getJob().setEmployer(employer);
		return this.employmentTermDao.makePersistent(employmentTerm);
	}
	
	/**
	 * Removes an employment term.
	 * 
	 * @param employmentTerm employment term
	 */
	public void remove(final EmploymentTerm employmentTerm) {
		this.employmentTermDao.makeTransient(employmentTerm);
	}
	
	/**
	 * Find employment terms by offender.
	 * 
	 * @param offender offender
	 * @return list of employment terms
	 */
	public List<EmploymentTerm> findByOffender(final Offender offender) {
		return this.employmentTermDao.findByOffender(offender);
	}
	
	// Populates an employment term
	private void populateEmploymentTerm(final EmploymentTerm employmentTerm, 
			final DateRange dateRange, final Job job,
			final Boolean convictedOfEmployerTheft, 
			final EmploymentChangeReason endReason,
			final VerificationSignature verificationSignature) {
		employmentTerm.setDateRange(dateRange);
		employmentTerm.setConvictedOfEmployerTheft(convictedOfEmployerTheft);
		employmentTerm.setEndReason(endReason);
		employmentTerm.setVerificationSignature(verificationSignature);
		employmentTerm.setJob(job);
		employmentTerm.setUpdateSignature(new UpdateSignature(
			this.auditComponentRetriever.retrieveUserAccount(),
			this.auditComponentRetriever.retrieveDate()));
	}
}