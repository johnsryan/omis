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
package omis.violationevent.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.facility.domain.Unit;
import omis.offender.domain.OffenderAssociable;
import omis.organization.domain.Organization;
import omis.violationevent.domain.component.Event;

/**
 * Violation event.
 * 
 * @author Annie Wahl
 * @author Ryan Johns 
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
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
	
	/**
	 * Returns the unit.
	 * 
	 * @return unit
	 */
	Unit getUnit();
	
	/**
	 * Sets the unit.
	 * 
	 * @param unit unit
	 */
	void setUnit(Unit unit);
	
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
