package omis.warrant.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.jail.domain.Jail;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantArrest;

/**
 * Warrant arrest.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantArrestImpl implements WarrantArrest {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Warrant warrant;
	
	private Date date;
	
	private Jail jail;
	
	private Date determinationDeadline;
	
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
	public Date getDate() {
		return this.date;
	}

	/**{@inheritDoc} */
	@Override
	public void setDate(final Date date) {
		this.date = date;
	}

	/**{@inheritDoc} */
	@Override
	public Jail getJail() {
		return this.jail;
	}

	/**{@inheritDoc} */
	@Override
	public void setJail(final Jail jail) {
		this.jail = jail;
	}
	
	/**{@inheritDoc} */
	@Override
	public Date getDeterminationDeadline() {
		return this.determinationDeadline;
	}

	/**{@inheritDoc} */
	@Override
	public void setDeterminationDeadline(final Date determinationDeadline) {
		this.determinationDeadline = determinationDeadline;
	}

	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj){
		if(this == obj){
			return true;
		}
		if(!(obj instanceof WarrantArrest)){
			return false;
		}
		
		WarrantArrest that = (WarrantArrest) obj;
		
		if(this.getDate() == null){
			throw new IllegalStateException("Date required.");
		}
		if(this.getJail() == null){
			throw new IllegalStateException("Jail required.");
		}
		if(this.getWarrant() == null){
			throw new IllegalStateException("Warrant required.");
		}
		
		if(!this.getWarrant().equals(that.getWarrant())){
			return false;
		}
		
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if(this.getDate() == null){
			throw new IllegalStateException("Date required.");
		}
		if(this.getJail() == null){
			throw new IllegalStateException("Jail required.");
		}
		if(this.getWarrant() == null){
			throw new IllegalStateException("Warrant required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getDate().hashCode();
		hashCode = 29 * hashCode + this.getJail().hashCode();
		hashCode = 29 * hashCode + this.getWarrant().hashCode();
		
		return hashCode;
	}
}
