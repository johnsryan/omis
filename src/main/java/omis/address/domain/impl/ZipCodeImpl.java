package omis.address.domain.impl;

import omis.address.domain.ZipCode;
import omis.region.domain.City;

/**
 * Zip code implementation.
 * 
 * @author Jason Nelson
 * @author Kelly Churchill
 * @author Joel Norris
 * @author Stephen Abson
 * @version 0.1.0
 * @since OMIS 3.0
 */
public class ZipCodeImpl implements ZipCode {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String value;
	private String extension;
	private City city;
	private Boolean valid;
	
	/** Instantiates an implementation of zip code. */
	public ZipCodeImpl() {
		// do nothing
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
	public String getValue() {
		return this.value;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setValue(final String value) {
		this.value = value;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getExtension() {
		return this.extension;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setExtension(final String extension) {
		this.extension = extension;
	}
	
	/** {@inheritDoc} */
	@Override
	public City getCity() {
		return this.city;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCity(final City city) {
		this.city = city;
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof ZipCode)) {
			return false;
		}
		ZipCode that = (ZipCode) obj;
		
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required");
		}
		
		if (!this.getValue().equals(that.getValue())) {
			return false;
		}
		
		if (this.getExtension() != null) {
			if (!this.getExtension().equals(that.getExtension())) {
				return false;
			}
		} else if (that.getExtension() != null) {
            return false;
		}
		
		if (this.getCity() == null) {
			throw new IllegalStateException("City required");
		}

		if (!this.getCity().equals(that.getCity())) {
			return false;
		}
		return true;
	}
	
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required");
		}
		if (this.getCity() == null) {
			throw new IllegalStateException("City required");
		}
		int hashCode = 14;	
		hashCode = 29 * hashCode + this.getValue().hashCode();
		hashCode = 29 * hashCode + this.getCity().hashCode();
		if (this.getExtension() != null) {
			hashCode = 29 * hashCode + this.getExtension().hashCode();
		}
		return hashCode;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		final String cityName;
		final String stateName;
		final String countryName;
		if (this.getCity() != null) {
			cityName = this.getCity().getName();
			if (this.getCity().getState() != null) {
				stateName = this.getCity().getState().getName();
				if (this.getCity().getState().getCountry() != null) {
					countryName = this.getCity().getState().getCountry()
							.getName();
				} else {
					countryName = null;
				}
			} else {
				stateName = null;
				countryName = null;
			}
		} else {
			cityName = null;
			stateName = null;
			countryName = null;
		}
		return String.format("#%d: %s %s %s-%s, %s", this.getId(),
				cityName, stateName, this.getValue(), this.getExtension(),
				countryName);
	}
}