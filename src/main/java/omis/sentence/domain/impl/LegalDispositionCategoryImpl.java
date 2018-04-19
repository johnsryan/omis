package omis.sentence.domain.impl;

import omis.sentence.domain.LegalDispositionCategory;

/**
 * Implementation of legal disposition category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Apr 3, 2017)
 * @since OMIS 3.0
 */
public class LegalDispositionCategoryImpl implements LegalDispositionCategory {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String name;
	
	private Boolean valid;
	
	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return this.valid;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof LegalDispositionCategory)) {
			return false;
		}
		LegalDispositionCategory that = (LegalDispositionCategory) obj;
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		int hashCode = 14;
		hashCode = 26 * hashCode + this.getName().hashCode();
		return hashCode;
	}
	
	/**
	 * Returns string representation of {@code this} including name.
	 * 
	 * @return string representation of {@code this} including name
	 */
	@Override
	public String toString() {
		return String.format("#%d - %s", this.getId(), this.getName());
	}
}
