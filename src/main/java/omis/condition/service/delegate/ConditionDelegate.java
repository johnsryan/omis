package omis.condition.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.condition.dao.ConditionDao;
import omis.condition.domain.Agreement;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;

/**
 * condition Delegate
 * 
 * @author Jonny Santy
 * @author Trevor Isles
 * @author Annie Jacques
 * @version 0.1.1 (May 21, 2017)
 * @since OMIS 3.0
 */
public class ConditionDelegate {

	/* Instance factories. */
	

	private static final String DUPLICATE_CONDITION_MSG = "Duplicate "
			+ "condition exists";

	private final InstanceFactory<Condition>
	conditionFactory;
	
	/* DAOs. */
	
	private final ConditionDao conditionDao;
	
	/* Audit Component */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructor. */
	
	/**
	 * Instantiates delegate for caution descriptions.
	 * 
	 * @param cautionDescriptionInstanceFactory instance factory for
	 * caution descriptions
	 * @param cautionDescriptionDao data access object for caution descriptions
	 */
	public ConditionDelegate(
			final InstanceFactory<Condition> conditionInstanceFactory,
			final ConditionDao conditionDao,
			final AuditComponentRetriever auditComponentRetriever) {
		this.conditionFactory = conditionInstanceFactory;
		this.conditionDao = conditionDao;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a Condition with the specified properties
	 * @param agreement - agreement.
	 * @param clause - clause.
	 * @param conditionClause - conditionClause.
	 * @param category - ConditionCategory
	 * @param waived - Boolean
	 * @return condition. 
	 * @throws DuplicateEntityFoundException - when condition already exists. */
	public Condition create(final Agreement agreement, final String clause,
			final ConditionClause conditionClause,
			final ConditionCategory category, final Boolean waived)
					throws DuplicateEntityFoundException {
		if(this.conditionDao.find(conditionClause, clause, agreement) != null) {
			throw new DuplicateEntityFoundException(
					DUPLICATE_CONDITION_MSG);
		}
			
		Condition condition = this.conditionFactory.createInstance();
		
		condition.setWaived(waived);
		condition.setCategory(category);
		condition.setAgreement(agreement);
		condition.setClause(clause);
		condition.setConditionClause(conditionClause);
		CreationSignature creationSignature = (
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		condition.setCreationSignature(creationSignature);
		UpdateSignature updateSignature = (
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		condition.setUpdateSignature(updateSignature);
		
		return this.conditionDao.makePersistent(condition);
	}
	
	/**
	 * Updates a Condition with the specified propertied
	 * @param condition - Condition to update
	 * @param conditionClause - ConditionClause
	 * @param agreement - Agreement
	 * @param clause - String
	 * @param category - ConditionCategory
	 * @param waived - Boolean
	 * @return Updated Condition
	 * @throws DuplicateEntityFoundException - when condition already exists
	 */
	public Condition update(final Condition condition,
			final ConditionClause conditionClause,
			final String clause,
			final ConditionCategory category, final Boolean waived)
					throws DuplicateEntityFoundException{
		if(this.conditionDao
				.findExcluding(condition, conditionClause, clause,
						condition.getAgreement())!= null) {
			throw new DuplicateEntityFoundException
				(DUPLICATE_CONDITION_MSG);
		}
		condition.setClause(clause);
		condition.setWaived(waived);
		condition.setCategory(category);
		condition.setConditionClause(conditionClause);
		UpdateSignature updateSignature = (
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		condition.setUpdateSignature(updateSignature);
		return this.conditionDao.makePersistent(condition);
	}
	
	/** Removes a condition.
	 * @param condition - Condition to remove.
	 */
	public void remove(final Condition condition){
		this.conditionDao.makeTransient(condition);
	}
	
	/**
	 * Returns a List of Conditions found by specified Agreement
	 * @param agreement - Agreement
	 * @return List of Conditions found by specified Agreement
	 */
	public List<Condition> findByAgreement(final Agreement agreement) {
		return this.conditionDao.findByAgreement(agreement);
	}
	
	/**
	 * Returns a list of Waived Conditions by specified agreement
	 * @param agreement - Agreement
	 * @return List of Waived Conditions by specified agreement
	 */
	public List<Condition> findWaivedByAgreement(final Agreement agreement){
		return this.conditionDao.findWaivedByAgreement(agreement);
	}
	
	/**
	 * Finds and returns a list of Conditions for an offender on specified date
	 * @param offender - Offender
	 * @param effectiveDate - Date
	 * @return list of Conditions for an offender on specified date
	 */
	public List<Condition> findByOffenderAndEffectiveDate(
			final Offender offender, final Date effectiveDate){
		return this.conditionDao.findByOffenderAndEffectiveDate(
				offender, effectiveDate);
	}
	
	/**
	 * Finds conditions for the specified condition clause, offender, and effective date.
	 * 
	 * @param clause condition clause
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return list of conditions
	 */
	public List<Condition> findByConditionClauseAndOffenderOnDate(final ConditionClause clause,
			final Offender offender, final Date effectiveDate) {
		return this.conditionDao.findByConditionClauseAndOffenderOnDate(clause, offender, effectiveDate);
	}
}
