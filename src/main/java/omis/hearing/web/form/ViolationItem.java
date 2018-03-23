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

import java.util.Date;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.report.ViolationSummary;
import omis.person.domain.Person;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;

/**
 * Violation Item.
 * 
 *@author Annie Wahl
 *@version 0.1.1 (Feb 28, 2018)
 *@since OMIS 3.0
 *
 */
public class ViolationItem {
	
	private ViolationSummary summary;
	
	private ConditionViolation conditionViolation;
	
	private DisciplinaryCodeViolation disciplinaryCodeViolation;
	
	private Infraction infraction;
	
	private Date date;
	
	private String decision;
	
	private String reason;
	
	private String sanction;
	
	private Person authority;
	
	private Date appealDate;
	
	private DispositionCategory disposition;
	
	private InfractionPlea plea;
	
	/**
	 * 
	 */
	public ViolationItem() {
	}

	/**
	 * @param summary - Violation Summary
	 * @param conditionViolation - Condition Violation
	 */
	public ViolationItem(final ViolationSummary summary,
			final ConditionViolation conditionViolation) {
		this.summary = summary;
		this.conditionViolation = conditionViolation;
		this.disciplinaryCodeViolation = null;
		this.infraction = null;
	}

	/**
	 * @param summary - Violation Summary
	 * @param disciplinaryCodeViolation - Disciplinary Code Violation
	 */
	public ViolationItem(
			final ViolationSummary summary,
			final DisciplinaryCodeViolation disciplinaryCodeViolation) {
		this.summary = summary;
		this.disciplinaryCodeViolation = disciplinaryCodeViolation;
		this.conditionViolation = null;
		this.infraction = null;
	}
	
	/**
	 * @param summary - Violation Summary
	 * @param conditionViolation - Condition Violation
	 * @param infraction - Infraction
	 */
	public ViolationItem(final ViolationSummary summary,
			final ConditionViolation conditionViolation,
			final Infraction infraction) {
		this.summary = summary;
		this.conditionViolation = conditionViolation;
		this.disciplinaryCodeViolation = null;
		this.infraction = infraction;
	}

	/**
	 * @param summary - Violation Summary
	 * @param disciplinaryCodeViolation - Disciplinary code Violation
	 * @param infraction - Infraction
	 */
	public ViolationItem(
			final ViolationSummary summary,
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final Infraction infraction) {
		this.summary = summary;
		this.disciplinaryCodeViolation = disciplinaryCodeViolation;
		this.conditionViolation = null;
		this.infraction = infraction;
	}
	
	/**
	 * Returns the infraction.
	 * @return infraction - Infraction
	 */
	public Infraction getInfraction() {
		return infraction;
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
	 * @return decision - String
	 */
	public String getDecision() {
		return decision;
	}

	/**
	 * Sets the decision.
	 * @param decision - String
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
	 * Returns the sanction.
	 * @return sanction - String
	 */
	public String getSanction() {
		return sanction;
	}

	/**
	 * Sets the sanction.
	 * @param sanction - String
	 */
	public void setSanction(final String sanction) {
		this.sanction = sanction;
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
	 * Returns the summary.
	 * @return summary - ViolationSummary
	 */
	public ViolationSummary getSummary() {
		return summary;
	}

	/**
	 * Returns the conditionViolation.
	 * @return conditionViolation - ConditionViolation
	 */
	public ConditionViolation getConditionViolation() {
		return conditionViolation;
	}

	/**
	 * Returns the disciplinaryCodeViolation.
	 * @return disciplinaryCodeViolation - DisciplinaryCodeViolation
	 */
	public DisciplinaryCodeViolation getDisciplinaryCodeViolation() {
		return disciplinaryCodeViolation;
	}

	/**
	 * Sets the summary.
	 * @param summary - ViolationSummary
	 */
	public void setSummary(
			final ViolationSummary summary) {
		this.summary = summary;
	}

	/**
	 * Sets the conditionViolation.
	 * @param conditionViolation - ConditionViolation
	 */
	public void setConditionViolation(
			final ConditionViolation conditionViolation) {
		this.conditionViolation = conditionViolation;
	}

	/**
	 * Sets the disciplinaryCodeViolation.
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 */
	public void setDisciplinaryCodeViolation(
			final DisciplinaryCodeViolation disciplinaryCodeViolation) {
		this.disciplinaryCodeViolation = disciplinaryCodeViolation;
	}

	/**
	 * Sets the infraction.
	 * @param infraction - Infraction
	 */
	public void setInfraction(final Infraction infraction) {
		this.infraction = infraction;
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
	 * @param appealDate - Date of appeal
	 */
	public void setAppealDate(final Date appealDate) {
		this.appealDate = appealDate;
	}

	/**
	 * Returns the plea.
	 * @return plea - InfractionPlea
	 */
	public InfractionPlea getPlea() {
		return this.plea;
	}

	/**
	 * Sets the plea.
	 * @param plea - InfractionPlea
	 */
	public void setPlea(final InfractionPlea plea) {
		this.plea = plea;
	}
}
