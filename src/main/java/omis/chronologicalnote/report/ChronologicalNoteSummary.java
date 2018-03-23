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
package omis.chronologicalnote.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Chronological note summary.
 * 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jan 30, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteSummary implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Long id;
	private final Date date;
	private final String title;
	private final String narrative;
	private final List<String> categoryNames;
	private final String updateUserLastName;
	private final String updateUserFirstName;
	private final String updateUserAccountName;
	private final Date updateDate;

	/* Services. */
	@Autowired
	@Qualifier("chronologicalNoteReportService")
	private ChronologicalNoteReportService chronologicalNoteReportService;
	
	/**
	 * Instantiates a chronologocal note summary with the
	 * specified properties.
	 * 
	 * @param id id
	 * @param date date
	 * @param narrative narrative
	 * @param updateUserLastName updateUserLastName
	 * @param updateUserFirstName updateUserFirstName
	 * @param updateUserAccountName updateUserAccountName
	 * @param updateDate updateDate
	 */
	public ChronologicalNoteSummary(final Long id, final Date date,
		final String title, final String narrative,
		final String updateUserLastName,
		final String updateUserFirstName,
		final String updateUserAccountName, final Date updateDate) {
		this.id = id;
		this.title = title;
		this.narrative = narrative;
		this.date = date;
		this.updateUserAccountName = updateUserAccountName;
		this.updateUserFirstName = updateUserFirstName;
		this.updateUserLastName = updateUserLastName;
		this.updateDate = updateDate;
		List<String> newCategoryNames = new ArrayList<String>();
		this.categoryNames = newCategoryNames;
	}
	
	/**
	 * Instantiates a chronologocal note summary  with the
	 * specified properties.
	 * 
	 * @param id id
	 * @param categoryNames category names
	 * @param date date
	 * @param narrative narrative
	 * @param updateUserLastName updateUserLastName
	 * @param updateUserFirstName updateUserFirstName
	 * @param updateUserAccountName updateUserAccountName
	 * @param updateDate updateDate
	 */
	public ChronologicalNoteSummary(final Long id,
		final List<String> categoryNames, final Date date,
		final String title, final String narrative,
		final String updateUserLastName,
		final String updateUserFirstName,
		final String updateUserAccountName, final Date updateDate) {
		this.id = id;
		this.title = title;
		this.narrative = narrative;
		this.date = date;
		this.updateUserAccountName = updateUserAccountName;
		this.updateUserFirstName = updateUserFirstName;
		this.updateUserLastName = updateUserLastName;
		this.updateDate = updateDate;
		this.categoryNames = categoryNames;
	}

	/**
	 * Returns the id.
	 * 
	 * @return id id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Returns the date.
	 * 
	 * @return date date
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * Returns the title.
	 *
	 *
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Returns update user account name.
	 * 
	 * @return updateUserAccountName update user account name
	 */
	public String getupdateUserAccountName() {
		return this.updateUserAccountName;
	}

	/**
	 * Returns update user first name.
	 * 
	 * @return updateUserFirstName update user first name
	 */
	public String getUpdateUserFirstName() {
		return this.updateUserFirstName;
	}
	
	/**
	 * Returns update user last name.
	 * 
	 * @return updateUserLastName update user last name
	 */
	public String getupdateUserLastName() {
		return this.updateUserLastName;
	}
	
	/**
	 * Returns the update date.
	 * 
	 * @return updateDate update date
	 */
	public Date getUpdateDate() {
		return this.updateDate;
	}
	
	/**
	 * Returns narrative.
	 * 
	 * @return narrative narrative
	 */
	public String getNarrative() {
		return this.narrative;
	}
	
	/**
	 * Returns category names.
	 * 
	 * @return categoryNames category names
	 */
	public List<String> getCategoryNames() {
		return this.categoryNames;
	}
}