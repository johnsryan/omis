package omis.sentence.domain.component;

import java.io.Serializable;

import omis.sentence.domain.Sentence;
import omis.sentence.domain.SentenceConnectionClassification;

/**
 * Sentence connection. 
 * 
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.1.0 (Apr 18, 2017)
 * @since OMIS 3.0
 */
public class SentenceConnection
	implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Sentence sentence;
	
	private SentenceConnectionClassification classification;
	
	/** Instantiates a default association of sentence connection. */
	public SentenceConnection() {
		// Default instantiation
	}
	
	/**
	 * Instantiates association of sentence connection.
	 * 
	 * @param sentence sentence
	 * @param classification classification
	 */
	public SentenceConnection(final Sentence sentence, 
			final SentenceConnectionClassification classification) {
		this.classification = classification;
		this.sentence = sentence;
	}

	/**
	 * Creates initial connection.
	 * 
	 * @return initial connection
	 */
	public static SentenceConnection createInitial() {
		return new SentenceConnection(
				null, SentenceConnectionClassification.INITIAL);
	}

	/**
	 * Creates concurrent connection.
	 * 
	 * @return concurrent connection
	 */
	public static SentenceConnection createConcurrent() {
		return new SentenceConnection(
				null, SentenceConnectionClassification.CONCURRENT);
	}
	
	/**
	 * Creates consecutive connection.
	 * 
	 * <p>Consecutive sentence is required.
	 * 
	 * @param consecutiveSentence consecutive sentence
	 * @return consecutive connection
	 */
	public static SentenceConnection createConsecutive(
			final Sentence consecutiveSentence) {
		if (consecutiveSentence == null) {
			throw new IllegalArgumentException("Sentence required");
		}
		return new SentenceConnection(
				consecutiveSentence, SentenceConnectionClassification
					.CONSECUTIVE);
	}
	
	/**
	 * Returns the connected sentence.
	 * 
	 * @return connected sentence
	 */
	public Sentence getSentence() {
		return sentence;
	}

	/**
	 * Sets the connected sentence.
	 * 
	 * @param sentence connected sentence
	 */
	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	/**
	 * Returns the sentence connection classification.
	 * 
	 * @return sentence connection classification
	 */
	public SentenceConnectionClassification getClassification() {
		return classification;
	}

	/**
	 * Sets the sentence connection classification.
	 * 
	 * @param classification sentence connection classification
	 */
	public void setClassification(
			SentenceConnectionClassification classification) {
		this.classification = classification;
	}
	
	/**
	 * Returns string representation of {@code this} including classification
	 * and sentence.
	 * 
	 * @return string representation of {@code this}
	 */
	@Override
	public String toString() {
		return String.format("%s - %s",
				this.getClassification(), this.getSentence());
	}
}
