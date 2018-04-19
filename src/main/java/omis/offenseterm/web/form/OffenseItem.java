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
package omis.offenseterm.web.form;

import java.io.Serializable;

import omis.conviction.domain.Conviction;
import omis.conviction.web.form.ConvictionFields;
import omis.sentence.domain.Sentence;
import omis.sentence.web.form.SentenceFields;
import omis.sentence.web.form.SentenceOperation;

/**
 * Offense item.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class OffenseItem
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private OffenseItemOperation operation;
	
	private Boolean expanded;
	
	private OffenseItemConnection connection;
	
	private Conviction conviction;
	
	private ConvictionFields convictionFields = new ConvictionFields();
	
	private SentenceOperation sentenceOperation;
	
	private Sentence sentence;
	
	private SentenceFields sentenceFields = new SentenceFields();
	
	/** Instantiates conviction item. */
	public OffenseItem() {
		// Default instantiation
	}

	/**
	 * Sets operation.
	 * 
	 * @param operation operation
	 */
	public void setOperation(final OffenseItemOperation operation) {
		this.operation = operation;
	}
	
	/**
	 * Returns operation.
	 * 
	 * @return operation
	 */
	public OffenseItemOperation getOperation() {
		return this.operation;
	}
	
	/**
	 * Sets whether expanded.
	 * 
	 * @param expanded whether expanded
	 */
	public void setExpanded(final Boolean expanded) {
		this.expanded = expanded;
	}
	
	/**
	 * Returns whether expanded;
	 * 
	 * @return whether expanded
	 */
	public Boolean getExpanded() {
		return this.expanded;
	}
	
	/**
	 * Sets whether consecutive or concurrent.
	 * 
	 * @param connection whether consecutive or concurrent
	 */
	public void setConnection(
			final OffenseItemConnection connection) {
		this.connection = connection;
	}
	
	/**
	 * Returns whether consecutive or concurrent.
	 * 
	 * @return whether consecutive or concurrent
	 */
	public OffenseItemConnection getConnection() {
		return this.connection;
	}
	
	/**
	 * Sets conviction.
	 * 
	 * @param conviction conviction
	 */
	public void setConviction(final Conviction conviction) {
		this.conviction = conviction;
	}
	
	/**
	 * Returns conviction.
	 * 
	 * @return conviction
	 */
	public Conviction getConviction() {
		return this.conviction;
	}
	
	/**
	 * Sets fields for convictions.
	 * 
	 * @param convictionFields fields for convictions
	 */
	public void setConvictionFields(
			final ConvictionFields convictionFields) {
		this.convictionFields = convictionFields;
	}
	
	/**
	 * Returns fields for convictions.
	 * 
	 * @return fields for convictions
	 */
	public ConvictionFields getConvictionFields() {
		return this.convictionFields;
	}

	/**
	 * Sets sentence operation.
	 * 
	 * @param sentenceOperation sentence operation
	 */
	public void setSentenceOperation(
			final SentenceOperation sentenceOperation) {
		this.sentenceOperation = sentenceOperation;
	}
	
	/**
	 * Returns sentence operation.
	 * 
	 * @return sentence operation
	 */
	public SentenceOperation getSentenceOperation() {
		return this.sentenceOperation;
	}
	
	/**
	 * Sets sentence.
	 * 
	 * @param sentence sentence
	 */
	public void setSentence(final Sentence sentence) {
		this.sentence = sentence;
	}
	
	/**
	 * Returns sentence.
	 * 
	 * @return sentence
	 */
	public Sentence getSentence() {
		return this.sentence;
	}
	
	/**
	 * Sets fields for sentencing.
	 * 
	 * @param sentenceFields fields for sentencing
	 */
	public void setSentenceFields(
			final SentenceFields sentenceFields) {
		this.sentenceFields = sentenceFields;
	}
	
	/**
	 * Returns fields for sentencing.
	 * 
	 * @return fields for sentencing
	 */
	public SentenceFields getSentenceFields() {
		return this.sentenceFields;
	}
	
	/**
	 * Returns string representation of {@code this} including operation,
	 * connection, conviction, sentence operation and sentence.
	 * 
	 * @return string representation of {@code this}
	 */
	@Override
	public String toString() {
		return String.format(
				"%s - %s [conviction - %s] %s [sentence - %s]",
				this.getOperation(), this.getConnection(), this.getConviction(),
				this.getSentenceOperation(), this.getSentence());
	}
}