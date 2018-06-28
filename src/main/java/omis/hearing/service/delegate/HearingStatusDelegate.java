package omis.hearing.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.dao.HearingStatusDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.HearingStatus;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.exception.HearingStatusExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * HearingStatusDelegate.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Apr 18, 2017)
 *@since OMIS 3.0
 *
 */
public class HearingStatusDelegate {
	
	private static final String HEARING_STATUS_EXISTS_MSG =
			"HearingStatus already exists with given Date and Category for specified Hearing.";
	
	private final HearingStatusDao hearingStatusDao;
	
	private final InstanceFactory<HearingStatus> 
		hearingStatusInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for HearingStatusDelegate
	 * @param hearingStatusDao
	 * @param hearingStatusInstanceFactory
	 * @param auditComponentRetriever
	 */
	public HearingStatusDelegate(
			final HearingStatusDao hearingStatusDao,
			final InstanceFactory<HearingStatus> 
				hearingStatusInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.hearingStatusDao = hearingStatusDao;
		this.hearingStatusInstanceFactory = hearingStatusInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a HearingStatus with the specified properties
	 * @param hearing - Hearing
	 * @param description - String
	 * @param date - Date
	 * @param category - HearingStatusCategory
	 * @return Newly Created HearingStatus
	 * @throws DuplicateEntityFoundException - When a HearingStatus already
	 * exists with specified Date and Category for given Hearing
	 */
	public HearingStatus create(final Hearing hearing, final String description,
			final Date date, final HearingStatusCategory category)
					throws HearingStatusExistsException{
		if(this.hearingStatusDao.find(hearing, date, category) != null){
			throw new HearingStatusExistsException(HEARING_STATUS_EXISTS_MSG);
		}
		
		HearingStatus hearingStatus = 
				this.hearingStatusInstanceFactory.createInstance();
		
		hearingStatus.setCategory(category);
		hearingStatus.setDate(date);
		hearingStatus.setDescription(description);
		hearingStatus.setHearing(hearing);
		hearingStatus.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		hearingStatus.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.hearingStatusDao.makePersistent(hearingStatus);
	}
	
	/**
	 * Updates a HearingStatus with the specified properties
	 * @param hearingStatus - HearingStatus to update
	 * @param description - String
	 * @param date - Date
	 * @param category - HearingStatusCategory
	 * @return Updated HearingStatus
	 * @throws DuplicateEntityFoundException - When a HearingStatus already
	 * exists with specified Date and Category for given Hearing
	 */
	public HearingStatus update(final HearingStatus hearingStatus,
			final String description, final Date date,
			final HearingStatusCategory category)
					throws HearingStatusExistsException{
		if(this.hearingStatusDao.findExcluding(hearingStatus.getHearing(), date,
				category, hearingStatus) != null){
			throw new HearingStatusExistsException(HEARING_STATUS_EXISTS_MSG);
		}
		
		hearingStatus.setCategory(category);
		hearingStatus.setDate(date);
		hearingStatus.setDescription(description);
		hearingStatus.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.hearingStatusDao.makePersistent(hearingStatus);
	}
	
	/**
	 * Removes a HearingStatus
	 * @param hearingStatus - HearingStatus to remove
	 */
	public void remove(final HearingStatus hearingStatus){
		this.hearingStatusDao.makeTransient(hearingStatus);
	}
	
	/**
	 * Returns the HearingStatus with the most recent Date for specified Hearing
	 * @param hearing - Hearing
	 * @return HearingStatus with the most recent Date for specified Hearing
	 */
	public HearingStatus findLatestByHearing(final Hearing hearing){
		return this.hearingStatusDao.findLatestByHearing(hearing);
	}
	
	/**
	 * Returns a list of all HearingStatuses for specified Hearing
	 * @param hearing - Hearing
	 * @return List of all HearingStatuses for specified Hearing
	 */
	public List<HearingStatus> findByHearing(final Hearing hearing){
		return this.hearingStatusDao.findByHearing(hearing);
	}
	
}
