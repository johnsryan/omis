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
package omis.violationevent.web.form;

import java.util.Date;
import java.util.List;

import omis.facility.domain.Unit;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Violation event form.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.2 (May 23, 2018)
 * @since OMIS 3.0
 */
public class ViolationEventForm {
	
	private SupervisoryOrganization jurisdiction;
	
	private JurisdictionFilter jurisdictionFilter;
	
	private Date eventDate;
	
	private String eventDetails;
	
	private Unit unit;
	
	private List<DisciplinaryCodeViolationItem> disciplinaryCodeViolationItems;
	
	private List<ConditionViolationItem> conditionViolationItems;
	
	private List<ViolationEventNoteItem> violationEventNoteItems;
	
	private List<ViolationEventDocumentItem> violationEventDocumentItems;
	
	/**
	 * Default Constructor for ViolationEventForm 
	 */
	public ViolationEventForm() {
	}

	
	/**
	 * Returns the jurisdiction
	 * @return jurisdiction - SupervisoryOrganization
	 */
	public SupervisoryOrganization getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the jurisdiction
	 * @param jurisdiction - SupervisoryOrganization
	 */
	public void setJurisdiction(final SupervisoryOrganization jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
	
	/**
	 * Returns the unit.
	 *
	 * @return unit
	 */
	public Unit getUnit() {
		return unit;
	}


	/**
	 * Sets the unit.
	 *
	 * @param unit unit
	 */
	public void setUnit(final Unit unit) {
		this.unit = unit;
	}


	/**
	 * Returns the jurisdictionFilter
	 * @return jurisdictionFilter - JurisdictionFilter
	 */
	public JurisdictionFilter getJurisdictionFilter() {
		return jurisdictionFilter;
	}

	/**
	 * Sets the jurisdictionFilter
	 * @param jurisdictionFilter - JurisdictionFilter
	 */
	public void setJurisdictionFilter(
			final JurisdictionFilter jurisdictionFilter) {
		this.jurisdictionFilter = jurisdictionFilter;
	}

	/**
	 * Returns the eventDate
	 * @return eventDate - Date
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * Sets the eventDate
	 * @param eventDate - Date
	 */
	public void setEventDate(final Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * Returns the eventDetails
	 * @return eventDetails - String
	 */
	public String getEventDetails() {
		return eventDetails;
	}

	/**
	 * Sets the eventDetails
	 * @param eventDetails - String
	 */
	public void setEventDetails(final String eventDetails) {
		this.eventDetails = eventDetails;
	}

	/**
	 * Returns the disciplinaryCodeViolationItems
	 * @return disciplinaryCodeViolationItems -
	 * 	 List<DisciplinaryCodeViolationItem>
	 */
	public List<DisciplinaryCodeViolationItem> getDisciplinaryCodeViolationItems() {
		return disciplinaryCodeViolationItems;
	}

	/**
	 * Sets the disciplinaryCodeViolationItems
	 * @param disciplinaryCodeViolationItems -
	 * 	 List<DisciplinaryCodeViolationItem>
	 */
	public void setDisciplinaryCodeViolationItems(
			final List<DisciplinaryCodeViolationItem>
			disciplinaryCodeViolationItems) {
		this.disciplinaryCodeViolationItems = disciplinaryCodeViolationItems;
	}
	
	/**
	 * Returns the conditionViolationItems
	 * @return conditionViolationItems - List<ConditionViolationItem>
	 */
	public List<ConditionViolationItem> getConditionViolationItems() {
		return conditionViolationItems;
	}

	/**
	 * Sets the conditionViolationItems
	 * @param conditionViolationItems - List<ConditionViolationItem>
	 */
	public void setConditionViolationItems(
			final List<ConditionViolationItem> conditionViolationItems) {
		this.conditionViolationItems = conditionViolationItems;
	}

	/**
	 * Returns the violationEventNoteItems
	 * @return violationEventNoteItems - List<ViolationEventNoteItem>
	 */
	public List<ViolationEventNoteItem> getViolationEventNoteItems() {
		return violationEventNoteItems;
	}

	/**
	 * Sets the violationEventNoteItems
	 * @param violationEventNoteItems - List<ViolationEventNoteItem>
	 */
	public void setViolationEventNoteItems(final List<ViolationEventNoteItem>
			violationEventNoteItems) {
		this.violationEventNoteItems = violationEventNoteItems;
	}

	/**
	 * Returns the violationEventDocumentItems
	 * @return violationEventDocumentItems - List<ViolationEventDocumentItem>
	 */
	public List<ViolationEventDocumentItem> getViolationEventDocumentItems() {
		return violationEventDocumentItems;
	}

	/**
	 * Sets the violationEventDocumentItems
	 * @param violationEventDocumentItems - List<ViolationEventDocumentItem>
	 */
	public void setViolationEventDocumentItems(
			final List<ViolationEventDocumentItem> violationEventDocumentItems) {
		this.violationEventDocumentItems = violationEventDocumentItems;
	}
	
	
	
	
}
