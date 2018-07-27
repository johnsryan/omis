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
package omis.hearing.domain.component;

import java.io.Serializable;
import java.util.Date;
import omis.condition.domain.Condition;
import omis.disciplinaryCode.domain.DisciplinaryCode;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.person.domain.Person;

/**
 * Resolution.
 * 
 * @author Annie Wahl 
 * @author Josh Divine
 * @version 0.1.3 (Jul 17, 2018)
 * @since OMIS 3.0
 */
public class Resolution implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date date;
	
	private String decision;
	
	private String reason;
	
	private Person authority;
	
	private Date appealDate;
	
	private DispositionCategory disposition;
	
	private ResolutionClassificationCategory category;
	
	private DisciplinaryCode adjustedCode;
	
	private Condition adjustedCondition;
	
	/**
	 * 
	 */
	public Resolution() {
	}

	/**
	 * Instantiates a resolution with all properties.
	 *  
	 * @param date date
	 * @param decision decision
	 * @param reason reason
	 * @param authority authority
	 * @param appealDate appeal date
	 * @param disposition disposition category
	 * @param category resolution classification category
	 */
	public Resolution(final Date date, final String decision, 
			final String reason, final Person authority, final Date appealDate, 
			final DispositionCategory disposition, 
			final ResolutionClassificationCategory category) {
		this.date = date;
		this.decision = decision;
		this.reason = reason;
		this.authority = authority;
		this.appealDate = appealDate;
		this.disposition = disposition;
		this.category = category;
	}
	
	/**
	 * Returns the date.
	 * @return date - Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date - Date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the decision.
	 * 
	 * @return decision
	 */
	public String getDecision() {
		return decision;
	}

	/**
	 * Sets the decision.
	 * 
	 * @param decision decision
	 */
	public void setDecision(final String decision) {
		this.decision = decision;
	}

	/**
	 * Returns the reason.
	 * @return reason - String
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 * @param reason - String
	 */
	public void setReason(final String reason) {
		this.reason = reason;
	}
	
	/**
	 * Returns the authority.
	 * @return authority - Person
	 */
	public Person getAuthority() {
		return authority;
	}

	/**
	 * Sets the authority.
	 * @param authority - Person
	 */
	public void setAuthority(final Person authority) {
		this.authority = authority;
	}
	
	/**
	 * Returns the appeal date.
	 * @return appealDate - Date of appeal
	 */
	public Date getAppealDate() {
		return appealDate;
	}

	/**
	 * Sets the appeal date.
	 * @param appealDate - Date appeal
	 */
	public void setAppealDate(final Date appealDate) {
		this.appealDate = appealDate;
	}

	/**
	 * Returns the disposition.
	 * @return disposition - DispositionCategory
	 */
	public DispositionCategory getDisposition() {
		return disposition;
	}

	/**
	 * Sets the disposition.
	 * @param disposition - DispositionCategory
	 */
	public void setDisposition(final DispositionCategory disposition) {
		this.disposition = disposition;
	}

	/**
	 * Returns the category.
	 * @return category - ResolutionClassificationCategory
	 */
	public ResolutionClassificationCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category - ResolutionClassificationCategory
	 */
	public void setCategory(final ResolutionClassificationCategory category) {
		this.category = category;
	}

	/**
	 * @return the adjustedCode
	 */
	public DisciplinaryCode getAdjustedCode() {
		return this.adjustedCode;
	}

	/**
	 * @param adjustedCode the adjustedCode to set
	 */
	public void setAdjustedCode(final DisciplinaryCode adjustedCode) {
		this.adjustedCode = adjustedCode;
	}

	/**
	 * @return the adjustedCondition
	 */
	public Condition getAdjustedCondition() {
		return this.adjustedCondition;
	}

	/**
	 * @param adjustedCondition the adjustedCondition to set
	 */
	public void setAdjustedCondition(final Condition adjustedCondition) {
		this.adjustedCondition = adjustedCondition;
	}
}
