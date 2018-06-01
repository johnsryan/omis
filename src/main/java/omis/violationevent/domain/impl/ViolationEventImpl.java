/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.violationevent.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.facility.domain.Unit;
import omis.offender.domain.Offender;
import omis.organization.domain.Organization;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.domain.component.Event;

/**
 * Implementation of violation event.
 * 
 * @author Annie Wahl
 * @author Ryan Johns 
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public class ViolationEventImpl implements ViolationEvent {

	private static final long serialVersionUID = 1L;

	private Offender offender;
	
	private Organization jurisdiction;
	
	private Event event;
	
	private ViolationEventCategory category;
	
	private Long id;
	
	private Unit unit;
	
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
	
	/** {@inheritDoc} */
	@Override
	public Unit getUnit() {
		return unit;
	}

	/** {@inheritDoc} */
	@Override
	public void setUnit(final Unit unit) {
		this.unit = unit;
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
		if (this.getJurisdiction() != null) {
			if(!this.getJurisdiction().equals(that.getJurisdiction())){
				return false;
			}
		} else {
			if (that.getJurisdiction() != null) {
				return false;
			}
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
		if (this.getUnit() != null) {
			if (!this.getUnit().equals(that.getUnit())) {
				return false;
			}
		} else {
			if (that.getUnit() != null) {
				return false;
			}
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if(this.getOffender() == null){
			throw new IllegalStateException("Offender required.");
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
		if (this.getJurisdiction() != null) {
			hashCode = 29 * hashCode + this.getJurisdiction().hashCode();
		}
		hashCode = 29 * hashCode + this.getEvent().getDate().hashCode();
		hashCode = 29 * hashCode + this.getEvent().getDetails().hashCode();
		hashCode = 29 * hashCode + this.getCategory().hashCode();
		
		return hashCode;
	}
}