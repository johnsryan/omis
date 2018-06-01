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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.hearing.domain.HearingCategory;
import omis.hearing.domain.HearingStatusCategory;
import omis.hearing.domain.LocationType;
import omis.location.domain.Location;
import omis.user.domain.UserAccount;

/**
 * Hearing Form.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.4 (May 15, 2018)
 * @since OMIS 3.0
 */
public class HearingForm {
	
	private HearingCategory category;
	
	private LocationType locationType;
	
	private Location location;
	
	private Date date;
	
	private Date time;
	
	private HearingStatusCategory status;
	
	private UserAccount officer;
	
	private List<HearingNoteItem> hearingNoteItems =
			new ArrayList<HearingNoteItem>();
	
	private List<InfractionItem> infractionItems =
			new ArrayList<InfractionItem>();
	
	/**
	 * 
	 */
	public HearingForm() {
	}

	/**
	 * Returns the category.
	 * @return category - HearingCategory
	 */
	public HearingCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the category.
	 * @param category - HearingCategory
	 */
	public void setCategory(final HearingCategory category) {
		this.category = category;
	}

	/**
	 * Returns the locationType.
	 * @return locationType - LocationType
	 */
	public LocationType getLocationType() {
		return this.locationType;
	}

	/**
	 * Sets the locationType.
	 * @param locationType - LocationType
	 */
	public void setLocationType(final LocationType locationType) {
		this.locationType = locationType;
	}

	/**
	 * Returns the location.
	 * @return location - Location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Sets the location.
	 * @param location - Location
	 */
	public void setLocation(final Location location) {
		this.location = location;
	}

	/**
	 * Returns the date.
	 * @return date - Date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 * @param date - Date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the time.
	 *
	 * @return time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 *
	 * @param time time
	 */
	public void setTime(final Date time) {
		this.time = time;
	}

	/**
	 * Returns the status.
	 * @return status - HearingStatusCategory
	 */
	public HearingStatusCategory getStatus() {
		return this.status;
	}

	/**
	 * Sets the status.
	 * @param status - HearingStatusCategory
	 */
	public void setStatus(final HearingStatusCategory status) {
		this.status = status;
	}

	/**
	 * Returns the officer.
	 * @return officer user account
	 */
	public UserAccount getOfficer() {
		return this.officer;
	}

	/**
	 * Sets the officer.
	 * @param officer user account
	 */
	public void setOfficer(final UserAccount officer) {
		this.officer = officer;
	}

	/**
	 * Returns the hearingNoteItems.
	 * @return hearingNoteItems - List<HearingNoteItem>
	 */
	public List<HearingNoteItem> getHearingNoteItems() {
		return this.hearingNoteItems;
	}

	/**
	 * Sets the hearingNoteItems.
	 * @param hearingNoteItems - List<HearingNoteItem>
	 */
	public void setHearingNoteItems(
			final List<HearingNoteItem> hearingNoteItems) {
		this.hearingNoteItems = hearingNoteItems;
	}

	/**
	 * Returns the infractionItems.
	 * @return infractionItems - List<InfractionItem>
	 */
	public List<InfractionItem> getInfractionItems() {
		return this.infractionItems;
	}

	/**
	 * Sets the infractionItems.
	 * @param infractionItems - List<InfractionItem>
	 */
	public void setInfractionItems(final List<InfractionItem> infractionItems) {
		this.infractionItems = infractionItems;
	}
}
