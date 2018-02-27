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
package omis.hearinganalysis.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Hearing analysis home form.
 *
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisHomeForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<HearingAnalysisTaskItem> analysisTaskItems = new ArrayList<>();
	
	private List<HearingAnalysisTaskItem> planningTaskItems = new ArrayList<>();
	
	private List<HearingAnalysisNoteItem> hearingAnalysisNoteItems;
	
	/**
	 * Instantiates a default hearing analysis home form.
	 */
	public HearingAnalysisHomeForm() {
		// Default instantiation
	}

	/**
	 * Returns the list of analysis related task items.
	 * 
	 * @return list of analysis related task items
	 */
	public List<HearingAnalysisTaskItem> getAnalysisTaskItems() {
		return analysisTaskItems;
	}

	/**
	 * Sets the list of analysis related task items.
	 * 
	 * @param analysisTaskItems analysis related task items
	 */
	public void setAnalysisTaskItems(
			final List<HearingAnalysisTaskItem> analysisTaskItems) {
		this.analysisTaskItems = analysisTaskItems;
	}

	/**
	 * Returns the list of planning related task items.
	 * 
	 * @return list of planning related task items
	 */
	public List<HearingAnalysisTaskItem> getPlanningTaskItems() {
		return planningTaskItems;
	}

	/**
	 * Sets the list of planning related task items.
	 * 
	 * @param planningTaskItems planning related task items
	 */
	public void setPlanningTaskItems(
			final List<HearingAnalysisTaskItem> planningTaskItems) {
		this.planningTaskItems = planningTaskItems;
	}
	
	/**
	 * Returns the list of hearing analysis note items.
	 * 
	 * @return list of hearing analysis note items
	 */
	public List<HearingAnalysisNoteItem> getHearingAnalysisNoteItems() {
		return hearingAnalysisNoteItems;
	}

	/**
	 * Sets the list of hearing analysis note items.
	 *  
	 * @param hearingAnalysisNoteItems list of hearing analysis note items
	 */
	public void setHearingAnalysisNoteItems(
			final List<HearingAnalysisNoteItem> hearingAnalysisNoteItems) {
		this.hearingAnalysisNoteItems = hearingAnalysisNoteItems;
	}
}
