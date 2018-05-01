package omis.warrant.domain.impl;

import java.math.BigDecimal;
import java.util.Date;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.jail.domain.Jail;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.warrant.domain.Warrant;
import omis.warrant.domain.WarrantReasonCategory;

/**
 * WarrantImpl.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public class WarrantImpl implements Warrant {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Offender offender;
	
	private Date date;
	
	private String addressee;
	
	private Person issuedBy;
	
	private Boolean bondable;
	
	private BigDecimal bondRecommendation;
	
	private WarrantReasonCategory warrantReason;
	
	private Jail holdingJail;
	
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
	public Offender getOffender() {
		return this.offender;
	}

	/**{@inheritDoc} */
	@Override
	public void setOffender(final Offender offender) {
		this.offender = offender;
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
	public String getAddressee() {
		return this.addressee;
	}

	/**{@inheritDoc} */
	@Override
	public void setAddressee(final String addressee) {
		this.addressee = addressee;
	}

	/**{@inheritDoc} */
	@Override
	public Person getIssuedBy() {
		return this.issuedBy;
	}

	/**{@inheritDoc} */
	@Override
	public void setIssuedBy(final Person issuedBy) {
		this.issuedBy = issuedBy;
	}

	/**{@inheritDoc} */
	@Override
	public Boolean getBondable() {
		return this.bondable;
	}

	/**{@inheritDoc} */
	@Override
	public void setBondable(final Boolean bondable) {
		this.bondable = bondable;
	}

	/**{@inheritDoc} */
	@Override
	public BigDecimal getBondRecommendation() {
		return this.bondRecommendation;
	}

	/**{@inheritDoc} */
	@Override
	public void setBondRecommendation(final BigDecimal bondRecommendation) {
		this.bondRecommendation = bondRecommendation;
	}

	/**{@inheritDoc} */
	@Override
	public WarrantReasonCategory getWarrantReason() {
		return this.warrantReason;
	}

	/**{@inheritDoc} */
	@Override
	public void setWarrantReason(
			final WarrantReasonCategory warrantReason) {
		this.warrantReason = warrantReason;
	}
	
	/**{@inheritDoc} */
	@Override
	public Jail getHoldingJail() {
		return this.holdingJail;
	}

	/**{@inheritDoc} */
	@Override
	public void setHoldingJail(final Jail holdingJail) {
		this.holdingJail = holdingJail;
	}

	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj){
		if(this == obj){
			return true;
		}
		if(!(obj instanceof Warrant)){
			return false;
		}
		
		Warrant that = (Warrant) obj;
		
		if(this.getOffender() == null){
			throw new IllegalStateException("Offender required.");
		}
		if(this.getDate() == null){
			throw new IllegalStateException("Date required.");
		}
		if(this.getAddressee() == null){
			throw new IllegalStateException("Addressee required.");
		}
		if(this.getIssuedBy() == null){
			throw new IllegalStateException("IssuedBy required.");
		}
		if(this.getBondable() == null){
			throw new IllegalStateException("Bondable required.");
		}
		if(this.getWarrantReason() == null){
			throw new IllegalStateException("WarrantReason required.");
		}
		
		if(!this.getOffender().equals(that.getOffender())){
			return false;
		}
		if(!this.getDate().equals(that.getDate())){
			return false;
		}
		
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if(this.getOffender() == null){
			throw new IllegalStateException("Offender required.");
		}
		if(this.getDate() == null){
			throw new IllegalStateException("Date required.");
		}
		if(this.getAddressee() == null){
			throw new IllegalStateException("Addressee required.");
		}
		if(this.getIssuedBy() == null){
			throw new IllegalStateException("IssuedBy required.");
		}
		if(this.getBondable() == null){
			throw new IllegalStateException("Bondable required.");
		}
		if(this.getWarrantReason() == null){
			throw new IllegalStateException("WarrantReason required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		hashCode = 29 * hashCode + this.getDate().hashCode();
		
		return hashCode;
	}
	
}
