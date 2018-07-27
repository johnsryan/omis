package omis.location.domain.impl;

import omis.address.domain.Address;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.organization.domain.Organization;

/**
 * Implementation of location.
 * 
 * @author Stephen Abson
 * @version 0.1.1 (July 30, 2015)
 * @since OMIS 3.0
 */
public class LocationImpl implements Location {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Organization organization;

	private Address address;

	private DateRange dateRange;

	/** Instantiates a default location. */
	public LocationImpl() {
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
	public void setOrganization(final Organization organization) {
		this.organization = organization;
	}

	/** {@inheritDoc} */
	@Override
	public Organization getOrganization() {
		return this.organization;
	}

	/** {@inheritDoc} */
	@Override
	public void setAddress(final Address address) {
		this.address = address;
	}

	/** {@inheritDoc} */
	@Override
	public Address getAddress() {
		return this.address;
	}

	/** {@inheritDoc} */
	@Override
	public void setDateRange(final DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public DateRange getDateRange() {
		return this.dateRange;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Location)) {
			return false;
		}
		Location that = (Location) obj;
		if (this.getAddress() == null) {
			throw new IllegalStateException("Address required");
		}
		if (!this.getAddress().equals(that.getAddress())) {
			return false;
		}
		if (this.getOrganization() == null) {
			throw new IllegalStateException("Organization required");
		}
		if (!this.getOrganization().equals(that.getOrganization())) {
			return false;
		}
		if(this.getDateRange() != null) {
			if (that.getDateRange() != null) {
				if (this.getDateRange().getStartDate() != null) {
					if (!this.getDateRange().getStartDate().equals(
							that.getDateRange().getStartDate())) {
						return false;
					}
				} else if (that.getDateRange().getStartDate() != null) {
					return false;
				}
				if (this.getDateRange().getEndDate() != null) {
					if (!this.getDateRange().getEndDate().equals(
							that.getDateRange().getEndDate())) {
						return false;
					}
				} else if (that.getDateRange().getEndDate() != null) {
					return false;
				}
			} else {
				return false;
			}
		} else if (that.getDateRange() != null) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getAddress() == null) {
			throw new IllegalStateException("Address required");
		}
		if (this.getOrganization() == null) {
			throw new IllegalStateException("Organization required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getAddress().hashCode();
		hashCode = 29 * hashCode + this.getOrganization().hashCode();
		if (this.getDateRange() != null) {
			if (this.getDateRange().getStartDate() != null) {
				hashCode = 31 * hashCode
					+ this.getDateRange().getStartDate().hashCode();
			}
			if (this.getDateRange().getEndDate() != null) {
				hashCode = 33 * hashCode
					+ this.getDateRange().getEndDate().hashCode();
			}
		}
		return hashCode;
	}
	
	/** {@inheritDoc} */
	public String toString() {
		final String organizationName;
		if (this.getOrganization() != null) {
			organizationName = this.getOrganization().getName();
		} else {
			organizationName = null;
		}
		final String addressText;
		if (this.getAddress() != null) {
			final String cityName;
			final String stateAbbreviation;
			final String zipCodeValue;
			final String zipCodeExtension;
			if (this.getAddress().getZipCode() != null) {
				if (this.getAddress().getZipCode().getCity() != null) {
					cityName = this.getAddress().getZipCode().getCity()
							.getName();
					if (this.getAddress().getZipCode().getCity()
							.getState() != null) {
						stateAbbreviation = this.getAddress().getZipCode()
								.getCity().getState().getAbbreviation();
					} else {
						stateAbbreviation = null;
					}
				} else {
					cityName = null;
					stateAbbreviation = null;
				}
				zipCodeValue = this.getAddress().getZipCode().getValue();
				zipCodeExtension
					= this.getAddress().getZipCode().getExtension();
			} else {
				cityName = null;
				stateAbbreviation = null;
				zipCodeValue = null;
				zipCodeExtension = null;
			}
			addressText = String.format("%s, %s, %s %s-%s",
					this.getAddress().getValue(), cityName, stateAbbreviation,
					zipCodeValue, zipCodeExtension);
		} else {
			addressText = null;
		}
		return String.format("#%d: %s - %s, %s", this.getId(),
				organizationName, addressText,
				DateRange.getStartDate(this.getDateRange()));
	}
}