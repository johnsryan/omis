package omis.warrant.web.form;

import omis.condition.domain.ConditionClause;
import omis.warrant.domain.WarrantCauseViolation;

/**
 * Warrant cause violation item.
 * 
 *@author Annie Jacques
 *@author Joel Norris
 *@version 0.1.1 (April 20, 2018)
 *@since OMIS 3.0
 *
 */
public class WarrantCauseViolationItem {
	
	private WarrantCauseViolation warrantCauseViolation;
	
	private ConditionClause conditionClause;
	
	private String description;
	
	private WarrantItemOperation itemOperation;
	
	/**
	 * 
	 */
	public WarrantCauseViolationItem() {
	}

	/**
	 * Returns the warrantCauseViolation
	 * @return warrantCauseViolation - WarrantCauseViolation
	 */
	public WarrantCauseViolation getWarrantCauseViolation() {
		return warrantCauseViolation;
	}

	/**
	 * Sets the warrantCauseViolation
	 * @param warrantCauseViolation - WarrantCauseViolation
	 */
	public void setWarrantCauseViolation(
			final WarrantCauseViolation warrantCauseViolation) {
		this.warrantCauseViolation = warrantCauseViolation;
	}
	
	/**
	 * Returns condition clause.
	 * 
	 * @return condition clause
	 */
	public ConditionClause getConditionClause() {
		return this.conditionClause;
	}

	/**
	 * Sets condition clause.
	 * 
	 * @param conditionClause condition clause
	 */
	public void setConditionClause(final ConditionClause conditionClause) {
		this.conditionClause = conditionClause;
	}

	/**
	 * Returns the description
	 * @return description - String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 * @param description - String
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Returns the itemOperation
	 * @return itemOperation - WarrantItemOperation
	 */
	public WarrantItemOperation getItemOperation() {
		return itemOperation;
	}

	/**
	 * Sets the itemOperation
	 * @param itemOperation - WarrantItemOperation
	 */
	public void setItemOperation(final WarrantItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
}
