package omis.sentence.domain.impl;

import omis.sentence.domain.SentenceCategory;
import omis.sentence.domain.TermRequirement;

/**
 * Implementation of sentence category.
 * 
 * @author Josh Divine
 * @version 0.1.0 (Jan 26, 2017)
 * @since OMIS 3.0
 */
public class SentenceCategoryImpl implements SentenceCategory {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String name;
	
	private TermRequirement prisonRequirement;
	
	private TermRequirement probationRequirement;
	
	private TermRequirement deferredRequirement;
	
	
	/** {@inheritDoc} */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public void setPrisonRequirement(
			TermRequirement prisonRequirement) {
		this.prisonRequirement = prisonRequirement;
	}

	/** {@inheritDoc} */
	@Override
	public TermRequirement getPrisonRequirement() {
		return this.prisonRequirement;
	}

	/** {@inheritDoc} */
	@Override
	public void setProbationRequirement(
			TermRequirement probationRequirement) {
		this.probationRequirement = probationRequirement;
	}

	/** {@inheritDoc} */
	@Override
	public TermRequirement getProbationRequirement() {
		return this.probationRequirement;
	}

	/** {@inheritDoc} */
	@Override
	public void setDeferredRequirement(
			TermRequirement deferredRequirement) {
		this.deferredRequirement = deferredRequirement;
	}

	/** {@inheritDoc} */
	@Override
	public TermRequirement getDeferredRequirement() {
		return this.deferredRequirement;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SentenceCategory)) {
			return false;
		}
		SentenceCategory that = (SentenceCategory) obj;
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
