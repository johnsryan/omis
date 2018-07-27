package omis.organization.domain.impl;

import omis.organization.domain.Organization;

/**
 * Implementation of organization.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Mar 5, 2013)
 * @since OMIS 3.0
 */
public class OrganizationImpl
		implements Organization {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String alias;
	
	private Organization parent;
	
	/** Instantiates a default organization. */
	public OrganizationImpl() {
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
	public void setName(final String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setAlias(final String alias) {
		this.alias = alias;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getAlias() {
		return this.alias;
	}

	/** {@inheritDoc} */
	@Override
	public void setParent(final Organization parent) {
		this.parent = parent;
	}

	/** {@inheritDoc} */
	@Override
	public Organization getParent() {
		return this.parent;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Organization)) {
			return false;
		}
		Organization that = (Organization) obj;
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
		hashCode = 29 * hashCode + this.getName().hashCode();
		return hashCode;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		String parentName;
		if (this.getParent() != null) {
			parentName = this.getParent().getName();
		} else {
			parentName = null;
		}
		return String.format("#%d: %s - %s [parent: %d]", this.getId(),
				this.getName(), this.getAlias(), parentName);
	}
}