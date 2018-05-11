package omis.violationevent.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.offender.domain.OffenderAssociable;
import omis.organization.domain.Organization;
import omis.violationevent.domain.component.Event;

/**
 * ViolationEvent.java
 * 
 *@author Annie Jacques
 *@author Ryan Johns 
 *@version 0.1.1 (May 10, 2018)
 *@since OMIS 3.0
 *
 */
public interface ViolationEvent
	extends OffenderAssociable, Creatable, Updatable {
	
	/**
	 * Returns the ID of the ViolationEvent
	 * @return ID
	 */
	public Long getId();
	
	/**
	 * Sets the ID of the ViolationEvent
	 * @param id - Long
	 */
	public void setId(Long id);
	
	/**
	 * Returns the Jurisdiction of the ViolationEvent
	 * @return jurisdiction - Organization
	 */
	public Organization getJurisdiction();
	
	/**
	 * Sets the jurisdiction of the ViolationEvent
	 * @param jurisdiction - Organization
	 */
	public void setJurisdiction(Organization jurisdiction);
	
	/**
	 * Returns the Event for the ViolationEvent
	 * @return Event - Event
	 */
	public Event getEvent();
	
	/**
	 * Sets the Event for the ViolationEvent
	 * @param Event - Event
	 */
	public void setEvent(Event event);
	
	/**
	 * Returns the ViolationEventCategory for the ViolationEvent
	 * @return Category - ViolationEventCategory
	 */
	public ViolationEventCategory getCategory();
	
	/**
	 * Sets the ViolationEventCategory for the ViolationEvent
	 * @param Category - ViolationEventCategory
	 */
	public void setCategory(ViolationEventCategory category);
	
	/** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	int hashCode();
}
