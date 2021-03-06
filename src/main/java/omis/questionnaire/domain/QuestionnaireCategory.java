package omis.questionnaire.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;

/**
 * QuestionnaireCategory.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Sep 7, 2016)
 *@since OMIS 3.0
 *
 */
public interface QuestionnaireCategory extends Creatable, Updatable {
	
	/** Gets id.
	 * @return id. */
	public Long getId();
	
	/**
	 * Returns the description
	 * @return description - String
	 */
	public String getDescription();
	
	/**
	 * Returns valid
	 * @return valid - Boolean
	 */
	public Boolean getValid();
	
	/**
	 * Returns source
	 * @return source - string
	 */
	public String getSource();
	
	/** Sets id.
	 * @param id - id. */
	public void setId(Long id);
	
	/**
	 * Sets the description
	 * @param description - String
	 */
	public void setDescription(String description);
	
	/**
	 * Sets valid
	 * @param valid - Boolean
	 */
	public void setValid(Boolean valid);
	
	/**
	 * Sets the source
	 * @param source - String
	 */
	public void setSource(String source);
	
	/** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	int hashCode();
	
}
