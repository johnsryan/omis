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
package omis.hearing.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import omis.hearing.domain.HearingStatusCategory;


/**
 * Resolution Form.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @author Ryan Johns
 * @version 0.1.4 (July 23, 2018)
 * @since OMIS 3.0
 */
public class ResolutionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<ViolationItem> violationItems = new ArrayList<ViolationItem>();
	
	private List<UserAttendanceItem> userAttendanceItems =
			new ArrayList<UserAttendanceItem>();
	
	private HearingStatusCategory category;
	
	private Date date;
	
	private Date time;
	
	private String statusDescription;
	
	private Boolean inAttendance;
	
	private Boolean groupEdit;
	
	/**
	 * 
	 */
	public ResolutionForm() {
	}

	/**
	 * Returns the violationItems.
	 * @return violationItems - List<ViolationItem>
	 */
	public List<ViolationItem> getViolationItems() {
		return violationItems;
	}

	/**
	 * Sets the violationItems.
	 * @param violationItems - List<ViolationItem>
	 */
	public void setViolationItems(final List<ViolationItem> violationItems) {
		this.violationItems = violationItems;
	}
	
	/**
	 * Returns the user attendance items.
	 * 
	 * @return user attendance items
	 */
	public List<UserAttendanceItem> getUserAttendanceItems() {
		return this.userAttendanceItems;
	}

	/**
	 * Sets the user attendance items.
	 * 
	 * @param userAttendanceItems user attendance items
	 */
	public void setUserAttendanceItems(
			final List<UserAttendanceItem> userAttendanceItems) {
		this.userAttendanceItems = userAttendanceItems;
	}
	
	/**
	 * Returns the category.
	 * @return category - HearingStatusCategory
	 */
	public HearingStatusCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category - HearingStatusCategory
	 */
	public void setCategory(final HearingStatusCategory category) {
		this.category = category;
	}

	/**
	 * Returns the date.
	 * @return date - Date
	 */
	public Date getDate() {
		return date;
	}
	
	/** Returns the time.
	 * @return time - time */
	public Date getTime() {
		return this.time;
	}

	/**
	 * Sets the date.
	 * @param date - Date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}
	
	/** Sets the time.
	 * @param time - time. */
	public void setTime(final Date time) {
		this.time = time;
	}

	/**
	 * Returns the statusDescription.
	 * @return statusDescription - String
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * Sets the statusDescription.
	 * @param statusDescription - String
	 */
	public void setStatusDescription(final String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
	/**
	 * Returns the groupEdit.
	 * @return groupEdit - Boolean
	 */
	public Boolean getGroupEdit() {
		return groupEdit;
	}

	/**
	 * Sets the groupEdit.
	 * @param groupEdit - Boolean
	 */
	public void setGroupEdit(final Boolean groupEdit) {
		this.groupEdit = groupEdit;
	}
//	
	/**
	 * @return the inAttendance
	 */
	public Boolean getInAttendance() {
		return inAttendance;
	}

	/**
	 * @param inAttendance the inAttendance to set
	 */
	public void setInAttendance(final Boolean inAttendance) {
		this.inAttendance = inAttendance;
	}
	
}
