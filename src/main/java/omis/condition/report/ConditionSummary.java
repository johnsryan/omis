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
package omis.condition.report;

import java.io.Serializable;

/**
 * Condition Summary Implementation.
 * 
 * @author Jonny Santy
 * @author Trevor Isles
 * @author Annie Jacques
 * @author Josh Divine
 * @version 0.1.2 (Feb 6, 2018)
 * @since OMIS 3.0
 */
public class ConditionSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final Long conditionId;
	private final String conditionTitle;
	private final String conditionClause;
	private final Boolean active;
	private final String category;
	private final Boolean waived;
	
	/**
	 * Instantiates summary of Conditions.
	 * @param conditionId The condition ID.
	 * @param conditionTitle the Title of the Condition Summary.
	 * @param conditionClause the clause of the Condition Summary.
	 * @param active whether this condition summary is within the Imposed
	 * Condition's date range.
	 */
	public ConditionSummary(final Long conditionId, final String conditionTitle,
			final String conditionClause,final Boolean active,
			final String category, final Boolean waived) {
		this.conditionId = conditionId;
		this.conditionTitle = conditionTitle;
		this.conditionClause = conditionClause;
		this.active = active;
		this.category = category;
		this.waived = waived;
	}
	
	/**
	 * Returns the conditionId
	 * @return conditionId - Long
	 */
	public Long getConditionId() {
		return conditionId;
	}


	/**
	 * Returns the conditionTitle
	 * @return conditionTitle - String
	 */
	public String getConditionTitle() {
		return conditionTitle;
	}


	/**
	 * Returns the conditionClause
	 * @return conditionClause - String
	 */
	public String getConditionClause() {
		return conditionClause;
	}


	/**
	 * Returns the active
	 * @return active - Boolean
	 */
	public Boolean getActive() {
		return active;
	}


	/**
	 * Returns the category
	 * @return category - String
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Returns the waived
	 * @return waived - Boolean
	 */
	public Boolean getWaived() {
		return waived;
	}
	
}
