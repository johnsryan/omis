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
package omis.boardhearing.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Schedule Hearings Form.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jun 28, 2018)
 *@since OMIS 3.0
 *
 */
public class ScheduleHearingsForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Boolean scheduleAsGroup;
	
	private List<BoardHearingItem> boardHearingItems =
			new ArrayList<BoardHearingItem>();
	
	/**
	 * Instantiates Schedule Hearings Form.
	 */
	public ScheduleHearingsForm() {
	}

	/**
	 * Returns the scheduleAsGroup.
	 * @return scheduleAsGroup - schedule as group
	 */
	public Boolean getScheduleAsGroup() {
		return this.scheduleAsGroup;
	}

	/**
	 * Sets the scheduleAsGroup.
	 * @param scheduleAsGroup - schedule as group
	 */
	public void setScheduleAsGroup(final Boolean scheduleAsGroup) {
		this.scheduleAsGroup = scheduleAsGroup;
	}

	/**
	 * Returns the Board Hearing Items.
	 * @return boardHearingItems - List of Board Hearing Items
	 */
	public List<BoardHearingItem> getBoardHearingItems() {
		return this.boardHearingItems;
	}

	/**
	 * Sets the Board Hearing Items.
	 * @param boardHearingItems - List of Board Hearing Items
	 */
	public void setBoardHearingItems(
			final List<BoardHearingItem> boardHearingItems) {
		this.boardHearingItems = boardHearingItems;
	}
	
	
}
