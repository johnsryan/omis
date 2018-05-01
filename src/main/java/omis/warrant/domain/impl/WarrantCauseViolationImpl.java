package omis.warrant.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.condition.domain.ConditionClause;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantCauseViolation;

/**
 * Warrant cause violation.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantCauseViolationImpl implements WarrantCauseViolation {
	
	private static final long serialVersionUID = 1L;
	
	private ConditionClause conditionClause;
	
	private String description;
	
	private Warrant warrant;
	
	private Long id;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**{@inheritDoc} */
	@Override
	public void setCreationSignature(final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/**{@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/**{@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/**{@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/**{@inheritDoc} */
	@Override
	public Warrant getWarrant() {
		return this.warrant;
	}

	/**{@inheritDoc} */
	@Override
	public void setWarrant(final Warrant warrant) {
		this.warrant = warrant;
	}
	
	/**{@inheritDoc} */
	@Override
	public ConditionClause getConditionClause() {
		return this.conditionClause;
	}

	/**{@inheritDoc} */
	@Override
	public void setConditionClause(final ConditionClause conditionClause) {
		this.conditionClause = conditionClause;
	}
	
	/**{@inheritDoc} */
	@Override
	public String getDescription() {
		return this.description;
	}
	
	/**{@inheritDoc} */
	@Override
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj){
		if(this == obj){
			return true;
		}
		if(!(obj instanceof WarrantCauseViolation)){
			return false;
		}
		
		WarrantCauseViolation that = (WarrantCauseViolation) obj;
		
		if(this.getWarrant() == null){
			throw new IllegalStateException("Warrant required.");
		}
		if(this.getConditionClause() == null){
			throw new IllegalStateException("ConditionClause required.");
		}
		
		if(!this.getWarrant().equals(that.getWarrant())){
			return false;
		}

		if(!this.getConditionClause().equals(that.getConditionClause())){
			return false;
		}
		
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if(this.getWarrant() == null){
			throw new IllegalStateException("Warrant required.");
		}
		if(this.getConditionClause() == null){
			throw new IllegalStateException("ConditionClause required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getWarrant().hashCode();
		hashCode = 29 * hashCode + this.getConditionClause().hashCode();
		
		return hashCode;
	}
}
