package omis.chronologicalnote.domain.impl;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryTemplate;

/**
 * Implementation of template for chronological note categories.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Mar 2, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryTemplateImpl
		implements ChronologicalNoteCategoryTemplate {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private ChronologicalNoteCategory category;
	
	private String text;

	/**
	 * Instantiates implementation of template for chronological note
	 * categories.
	 */
	public ChronologicalNoteCategoryTemplateImpl() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setCategory(final ChronologicalNoteCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategory getCategory() {
		return this.category;
	}

	/** {@inheritDoc} */
	@Override
	public void setText(final String text) {
		this.text = text;
	}

	/** {@inheritDoc} */
	@Override
	public String getText() {
		return this.text;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChronologicalNoteCategoryTemplate)) {
			return false;
		}
		ChronologicalNoteCategoryTemplate that
			= (ChronologicalNoteCategoryTemplate) obj;
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required");
		}
		if (!this.getCategory().equals(that.getCategory())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getCategory() == null) {
			throw new IllegalStateException("Category required");
		}
		return (14 * 29) + this.getCategory().hashCode();
	}
	
	/**
	 * Returns string representation of {@code this} containing category and
	 * text.
	 * 
	 * @return string representation of {@code this} containing category
	 * and text 
	 */
	@Override
	public String toString() {
		return String.format("#%d [%s] \"%s\"",
				this.getId(), this.getCategory(), this.getText());
	}
}