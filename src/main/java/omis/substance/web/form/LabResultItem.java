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
package omis.substance.web.form;

import java.io.Serializable;

import omis.substance.domain.Substance;
import omis.substancetest.domain.SubstanceTestResultValue;

/**
 * Crime Lab Result Item.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
public class LabResultItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private SubstanceTestResultValue substanceTestResultValue;
	
	private Substance substance;
	
	private Boolean process;
	
	/**
	 * Instantiates a default instance of lab result item.
	 */
	public LabResultItem() {
		//Default constructor.
	}
	
	/**
	 * Instantiates a lab result item with the specified value, substance,
	 * and whether process applies.
	 * 
	 * @param substanceTestResultValue substance test result value
	 * @param substance substance
	 * @param process process
	 */
	public LabResultItem(
			final SubstanceTestResultValue substanceTestResultValue,
			final Substance substance, final Boolean process) {
		this.substanceTestResultValue = substanceTestResultValue;
		this.substance = substance;
		this.process = process;
	}

	/**
	 * Returns the substance test result option of the crime lab result item.
	 * 
	 * @return substance test result value
	 */
	public SubstanceTestResultValue getSubstanceTestResultValue() {
		return this.substanceTestResultValue;
	}

	/**
	 * Sets the substance test result option of the crime lab result item.
	 * 
	 * @param substanceTestResultvalue substance test result value
	 */
	public void setSubstanceTestResultValue(
			final SubstanceTestResultValue substanceTestResultOption) {
		this.substanceTestResultValue = substanceTestResultOption;
	}

	/**
	 * Returns the substance of the crime lab result option.
	 * @return substance
	 */
	public Substance getSubstance() {
		return this.substance;
	}

	/**
	 * Sets the substance of the crime lab result option.
	 * @param substance substance
	 */
	public void setSubstance(final Substance substance) {
		this.substance = substance;
	}

	/**
	 * Returns the process value of the crime lab result option.
	 * @return process
	 */
	public Boolean getProcess() {
		return this.process;
	}

	/**
	 * Sets the process value of the crime lab result option.
	 * @param process process
	 */
	public void setProcess(final Boolean process) {
		this.process = process;
	}
}