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
package omis.presentenceinvestigation.web.form;

import java.util.Date;

import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;

/**
 * Presentence investigation delay item.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 23, 2018)
 * @since OMIS 3.0
 */
public class PresentenceInvestigationDelayItem {
	
	private PresentenceInvestigationDelay presentenceInvestigationDelay;
	
	private Date date;
	
	private PresentenceInvestigationDelayCategory reason;
	
	private PresentenceInvestigationItemOperation itemOperation;
	
	/**
	 * 
	 */
	public PresentenceInvestigationDelayItem() {
	}

	/**
	 * Returns the presentence investigation delay.
	 * 
	 * @return presentence investigation delay
	 */
	public PresentenceInvestigationDelay
			getPresentenceInvestigationDelay() {
		return presentenceInvestigationDelay;
	}

	/**
	 * Sets the presentence investigation delay.
	 * 
	 * @param presentenceInvestigationDelay presentence investigation delay
	 */
	public void setPresentenceInvestigationDelay(
			final PresentenceInvestigationDelay presentenceInvestigationDelay) {
		this.presentenceInvestigationDelay = presentenceInvestigationDelay;
	}

	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the reason.
	 * 
	 * @return presentence investigation delay category
	 */
	public PresentenceInvestigationDelayCategory getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 * 
	 * @param reason presentence investigation delay category
	 */
	public void setReason(final PresentenceInvestigationDelayCategory reason) {
		this.reason = reason;
	}
	
	/**
	 * Returns the item operation.
	 * 
	 * @return item operation
	 */
	public PresentenceInvestigationItemOperation getItemOperation() {
		return itemOperation;
	}

	/**
	 * Sets the item operation.
	 * 
	 * @param itemOperation item operation
	 */
	public void setItemOperation(
			final PresentenceInvestigationItemOperation itemOperation) {
		this.itemOperation = itemOperation;
	}
}