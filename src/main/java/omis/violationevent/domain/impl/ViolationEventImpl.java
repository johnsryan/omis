package omis.violationevent.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.component.Event;

/**
 * ViolationEventImpl.java
 * 
 *@author Annie Jacques
 *@author Ryan Johns 
 *@version 0.1.1 (May 10, 2018)
 *@since OMIS 3.0
 *
 */
public class ViolationEventImpl implements ViolationEvent {

	private static final long serialVersionUID = 1L;

	private Offender offender;
	
	private Organization jurisdiction;
	
	private Event event;
	
	private ViolationEventCategory category;
	
	private Long id;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	/**
	 * Default Constructor for ViolationEventImpl
	 */
	public ViolationEventImpl() {
	}
	
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
	public void setOffender(final Offender offender) {
		this.offender = offender;
	}

	/**{@inheritDoc} */
	@Override
	public Offender getOffender() {
		return this.offender;
	}

	/**{@inheritDoc} */
	@Override
	public Organization getJurisdiction() {
		return this.jurisdiction;
	}

	/**{@inheritDoc} */
	@Override
	public void setJurisdiction(final Organization jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	/**{@inheritDoc} */
	@Override
	public Event getEvent() {
		return this.event;
	}

	/**{@inheritDoc} */
	@Override
	public void setEvent(final Event event) {
		this.event = event;
	}

	/**{@inheritDoc} */
	@Override
	public ViolationEventCategory getCategory() {
		return this.category;
	}

	/**{@inheritDoc} */
	@Override
	public void setCategory(final ViolationEventCategory category) {
		this.category = category;
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean equals(final Object obj){
		if(this == obj){
			return true;
		}
		if(!(obj instanceof ViolationEvent)){
			return false;
		}
		
		ViolationEvent that = (ViolationEvent) obj;
		
		if(this.getOffender() == null){
			throw new IllegalStateException("Offender required.");
		}
		if(this.getJurisdiction() == null){
			throw new IllegalStateException("Jurisdiction required.");
		}
		if(this.getEvent().getDate() == null){
			throw new IllegalStateException("Event Date required.");
		}
		if(this.getEvent().getDetails() == null){
			throw new IllegalStateException("Event Details required.");
		}
		if(this.getCategory() == null){
			throw new IllegalStateException("Category required.");
		}
		
		if(!this.getOffender().equals(that.getOffender())){
			return false;
		}
		if(!this.getJurisdiction().equals(that.getJurisdiction())){
			return false;
		}
		if(!this.getEvent().getDate().equals(that.getEvent().getDate())){
			return false;
		}
		if(!this.getEvent().getDetails().equals(that.getEvent().getDetails())){
			return false;
		}
		if(!this.getCategory().equals(that.getCategory())){
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
		if(this.getJurisdiction() == null){
			throw new IllegalStateException("Jurisdiction required.");
		}
		if(this.getEvent().getDate() == null){
			throw new IllegalStateException("Event Date required.");
		}
		if(this.getEvent().getDetails() == null){
			throw new IllegalStateException("Event Details required.");
		}
		if(this.getCategory() == null){
			throw new IllegalStateException("Category required.");
		}
		
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getOffender().hashCode();
		hashCode = 29 * hashCode + this.getJurisdiction().hashCode();
		hashCode = 29 * hashCode + this.getEvent().getDate().hashCode();
		hashCode = 29 * hashCode + this.getEvent().getDetails().hashCode();
		hashCode = 29 * hashCode + this.getCategory().hashCode();
		
		return hashCode;
	}
	
}
