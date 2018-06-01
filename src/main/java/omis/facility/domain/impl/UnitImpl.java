/**
 * 
 */
package omis.facility.domain.impl;

import omis.facility.domain.Complex;
import omis.facility.domain.Facility;
import omis.facility.domain.Unit;

/**
 * Unit implementation.
 * 
 * @author Joel Norris 
 * @version 0.1.0 (Feb, 07 2013)
 * @since OMIS 3.0
 */
public class UnitImpl implements Unit {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private Facility facility;
	
	private Complex complex;
	
	private String abbreviation;
	
	private Boolean valid;
	
	/** Instantiates a default unit implementation. */
	public UnitImpl() {
		//Empty constructor
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public Facility getFacility() {
		return this.facility;
	}

	/** {@inheritDoc} */
	@Override
	public void setFacility(final Facility facility) {
		this.facility = facility;
	}

	/** {@inheritDoc} */
	@Override
	public Complex getComplex() {
		return this.complex;
	}

	/** {@inheritDoc} */
	@Override
	public void setComplex(final Complex complex) {
		this.complex = complex;
	}

	/** {@inheritDoc} */
	@Override
	public String getAbbreviation() {
		return this.abbreviation;
	}

	/** {@inheritDoc} */
	@Override
	public void setAbbreviation(final String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValid() {
		return this.valid;
	}

	/** {@inheritDoc} */
	@Override
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Unit)) {
			return false;
		}
		
		Unit that = (Unit) o;
		
		if (this.getName() == null) {
			throw new IllegalStateException("Name required");
		}
		if (!this.getName().equals(that.getName())) {
			return false;
		}
		if (this.getFacility() == null) {
			throw new IllegalStateException("Facility required");
		}
		if (!this.getFacility().equals(that.getFacility())) {
			return false;
		}
		if (this.getComplex() != null) {
			if (!this.getComplex().equals(that.getComplex())) {
				return false;
			}
		} else if (that.getComplex() != null) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getName() == null) {
			throw new IllegalStateException("Unit Name required");
		}
		if (this.getFacility() == null) {
			throw new IllegalStateException("Facility required");
		}
		
		int hashCode = 14;
		
		hashCode = 29 * hashCode + this.getName().hashCode();
		hashCode = 29 * hashCode + this.getFacility().hashCode();
		if (this.getComplex() != null) {
			hashCode = 29 * hashCode + this.getComplex().hashCode();
		}
		
		return hashCode;
	}
}