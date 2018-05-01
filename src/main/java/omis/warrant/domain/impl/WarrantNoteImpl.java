package omis.warrant.domain.impl;

import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantNote;

/**
 * Warrant note implementation.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantNoteImpl implements WarrantNote {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Warrant warrant;
	
	private Date date;
	
	private String value;
	
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
	public String getValue() {
		return this.value;
	}

	/**{@inheritDoc} */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj){
		if(this == obj){
			return true;
		}
		if(!(obj instanceof WarrantNote)){
			return false;
		}
		
		WarrantNote that = (WarrantNote) obj;
		
		if(this.getWarrant() == null){
			throw new IllegalStateException("Warrant required.");
		}
		if(this.getDate() == null){
			throw new IllegalStateException("Date required.");
		}
		if(this.getValue() == null){
			throw new IllegalStateException("Note required.");
		}
		
		if(!this.getWarrant().equals(that.getWarrant())){
			return false;
		}
		if(!this.getDate().equals(that.getDate())){
			return false;
		}
		if(!this.getValue().equals(that.getValue())){
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
		if(this.getDate() == null){
			throw new IllegalStateException("Date required.");
		}
		if(this.getValue() == null){
			throw new IllegalStateException("Value required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getWarrant().hashCode();
		hashCode = 29 * hashCode + this.getDate().hashCode();
		hashCode = 29 * hashCode + this.getValue().hashCode();
		
		return hashCode;
	}
}
