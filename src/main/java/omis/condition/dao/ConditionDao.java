package omis.condition.dao;

import java.util.Date;
import java.util.List;

import omis.condition.domain.Agreement;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.Condition;
import omis.dao.GenericDao;
import omis.offender.domain.Offender;

/**
 * Condition Dao.
 * 
 * @author Jonny Santy
 * @author Trevor Isles
 * @author Annie Jacques
 * @version 0.1.2 (May 21, 2017)
 * @since OMIS 3.0
 */
public interface ConditionDao extends GenericDao<Condition>{
	
	/**
	 * Returns a Condition found with the specified properties
	 * @param conditionClause - ConditionClause
	 * @param clause - String
	 * @param agreement - Agreement
	 * @return Condition found with the specified properties
	 */
	Condition find(ConditionClause conditionClause, String clause,
			Agreement agreement);
	
	/**
	 * Returns a list of Conditions with the specified Agreement
	 * @param agreement - Agreement
	 * @return List of Conditions with the specified Agreement
	 */
	List<Condition> findByAgreement(
			Agreement agreement);
	
	/**
	 * Returns a Condition found with the specified properties excluding
	 * specified Condition
	 * @param condition - Condition to exclude from the search
	 * @param conditionClause - ConditionClause
	 * @param clause - String
	 * @param agreement - Agreement
	 * @return Condition found with the specified properties excluding
	 * specified Condition
	 */
	Condition findExcluding(Condition condition, ConditionClause conditionClause,
			String clause, Agreement agreement);
	
	/**
	 * Finds and returns a list of Conditions for an offender on specified date
	 * @param offender - Offender
	 * @param effectiveDate - Date
	 * @return list of Conditions for an offender on specified date
	 */
	List<Condition> findByOffenderAndEffectiveDate(Offender offender,
			Date effectiveDate);
	
	/**
	 * Returns a list of Waived Conditions by specified agreement
	 * @param agreement - Agreement
	 * @return List of Waived Conditions by specified agreement
	 */
	List<Condition> findWaivedByAgreement(Agreement agreement);

	/**
	 * Finds conditions for the specified condition clause, offender, and effective date.
	 * 
	 * @param clause condition clause
	 * @param offender offender
	 * @param effectiveDate effective date
	 * @return list of conditions
	 */
	List<Condition> findByConditionClauseAndOffenderOnDate(ConditionClause clause, Offender offender,
			Date effectiveDate);
}
