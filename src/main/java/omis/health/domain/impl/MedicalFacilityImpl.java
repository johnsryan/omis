package omis.health.domain.impl;

import omis.health.domain.MedicalFacility;
import omis.location.domain.Location;

/**
 * Implementation of medical facility.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Jun 23, 2014)
 * @since OMIS 3.0
 */
public class MedicalFacilityImpl
		implements MedicalFacility {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String name;

	private String abbreviation;

	private Location location;

	private Boolean active;
	
	private Boolean hospital;
	
	private Long telephoneNumber;
	
	/** Instantiates an implementation of medical center. */
	public MedicalFacilityImpl() {
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
	public void setAbbreviation(final String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/** {@inheritDoc} */
	@Override
	public String getAbbreviation() {
		return this.abbreviation;
	}

	/** {@inheritDoc} */
	@Override
	public void setLocation(final Location location) {
		this.location = location;
	}

	/** {@inheritDoc} */
	@Override
	public Location getLocation() {
		return this.location;
	}

	/** {@inheritDoc} */
	@Override
	public void setActive(final Boolean active) {
		this.active = active;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getActive() {
		return this.active;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setHospital(final Boolean hospital) {
		this.hospital = hospital;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getHospital() {
		return this.hospital;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setTelephoneNumber(Long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/** {@inheritDoc} */
	@Override
	public Long getTelephoneNumber() {
		return this.telephoneNumber;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof MedicalFacility)) {
			return false;
		}
		MedicalFacility that = (MedicalFacility) obj;
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
		hashCode = hashCode * 29 + this.getName().hashCode();
		return hashCode;
	}
}