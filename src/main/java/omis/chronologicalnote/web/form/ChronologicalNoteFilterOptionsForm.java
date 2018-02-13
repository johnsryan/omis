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
package omis.chronologicalnote.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;

/** Form object for chronological note list screen.
 * @author: Yidong Li
 * @version 0.1.1 (Feb 2, 2018)
 * @since OMIS 3.0 */
public class ChronologicalNoteFilterOptionsForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<ChronologicalNoteCategory> categories 
		= new ArrayList<ChronologicalNoteCategory>();
	
	/** Instantiates a chronological note form. */
	public ChronologicalNoteFilterOptionsForm() {
		// Default instantiation
	}
	
	/**
	 * Get categories.
	 * @return categories
	 */
	public List<ChronologicalNoteCategory> getCategories() {
		return this.categories;
	}
	
	/**
	 * Set categories.
	 * @param categories categories
	 */
	public void setCategories(
		final List<ChronologicalNoteCategory> categories) {
		this.categories = categories;
	}
}	
