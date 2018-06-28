package omis.employment.service.delegate;

import java.util.List;

import omis.employment.dao.EmploymentChangeReasonDao;
import omis.employment.domain.EmploymentChangeReason;
import omis.employment.exception.EmploymentChangeReasonExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;

/**
 * Employment change reason delegate.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.1 (Dec 13, 2017)
 * @since OMIS 3.0
 */
public class EmploymentChangeReasonDelegate {
	
	/* Instance factories. */
	private final InstanceFactory<EmploymentChangeReason>
	employmentChangeReasonInstanceFactory;	
	
	/* DAOs. */

	private final EmploymentChangeReasonDao employmentChangeReasonDao;
	
	/* Constructor. */

	/**
	 * Instantiates delegate for employment change reasons.
	 * 
	 * @param employmentChangeReasonInstanceFactory instance factory for
	 * employment change reasons
	 * @param employmentChangrReasonDao data access object for employment
	 * change reasons
	 */
	public EmploymentChangeReasonDelegate (
			final EmploymentChangeReasonDao employmentChangeReasonDao,
			final InstanceFactory<EmploymentChangeReason> 
			employmentChangeReasonInstanceFactory) {
		this.employmentChangeReasonDao = employmentChangeReasonDao;
		this.employmentChangeReasonInstanceFactory 
			= employmentChangeReasonInstanceFactory;
	}

	/** 
	 * Creates an employment change reason.
	 * 
	 * @param name name
	 * @param sortOrder sort order
	 * @param valid valid
	 * @return employment change reason 
	 * @throws EmploymentChangeReasonExistsException if duplicate entity exists 
	 */
	public EmploymentChangeReason create(final String name, 
			final Short sortOrder, final Boolean valid) 
				throws EmploymentChangeReasonExistsException {
		if (this.employmentChangeReasonDao.find(name)!= null) {
			throw new EmploymentChangeReasonExistsException(
					"Duplicate employment change reason found.");
		}
		EmploymentChangeReason employmentChangeReason = this
				.employmentChangeReasonInstanceFactory.createInstance();
		populateEmploymentChangeReason(employmentChangeReason, name, sortOrder, 
				valid);
		return this.employmentChangeReasonDao.makePersistent(
				employmentChangeReason);
	}

	/** 
	 * Creates an employment change reason.
	 * 
	 * @param employmentChangeReason employment change reason
	 * @param name name
	 * @param sortOrder sort order
	 * @param valid valid
	 * @return employment change reason 
	 * @throws EmploymentChangeReasonExistsException if duplicate entity exists 
	 */
	public EmploymentChangeReason create(
			final EmploymentChangeReason employmentChangeReason, 
			final String name, final Short sortOrder, final Boolean valid) 
				throws EmploymentChangeReasonExistsException {
		if (this.employmentChangeReasonDao.findExcluding(name, 
				employmentChangeReason)!= null) {
			throw new EmploymentChangeReasonExistsException(
					"Duplicate employment change reason found.");
		}
		populateEmploymentChangeReason(employmentChangeReason, name, sortOrder, 
				valid);
		return this.employmentChangeReasonDao.makePersistent(
				employmentChangeReason);
	}

	/**
	 * Removes an employment change reason.
	 * 
	 * @param employmentChangeReason employment change reason
	 */
	public void remove(final EmploymentChangeReason employmentChangeReason) {
		this.employmentChangeReasonDao.makeTransient(employmentChangeReason);
	}
	
	/**
	 * Returns a list of employment change reasons.
	 * 
	 * @return list of employment change reasons
	 */
	public List<EmploymentChangeReason> findEmploymentChangeReasons() {
		return this.employmentChangeReasonDao.findEmploymentChangeReasons();
	}
	
	// Populates an employment change reason
	private void populateEmploymentChangeReason(
			final EmploymentChangeReason employmentChangeReason, 
			final String name, final Short sortOrder, final Boolean valid) {
		employmentChangeReason.setName(name);
		employmentChangeReason.setSortOrder(sortOrder);
		employmentChangeReason.setValid(valid);
	}
}
