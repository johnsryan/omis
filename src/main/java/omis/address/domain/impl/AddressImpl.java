package omis.address.domain.impl;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;


/**
 * Address implementation.
 * 
 * @author Jason Nelson
 * @author Kelly Churchill
 * @author Joel Norris
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.1 (May 27, 2015)
 * @since OMIS 3.0
 */
public class AddressImpl implements Address {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String value;
	
	private ZipCode zipCode;
	
	private BuildingCategory buildingCategory;
	
	private UpdateSignature updateSignature;
	
	private CreationSignature creationSignature;
	
	private String designator;
	
	private String coordinates;

	/** Instantiates an implementation of address. */
	public AddressImpl() {
		// Default instantiation
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
	public ZipCode getZipCode() {
		return this.zipCode;
	}

	/** {@inheritDoc} */
	@Override
	public void setZipCode(final ZipCode zipCode) {
		this.zipCode = zipCode;
	}

	/** {@inheritDoc} */
	@Override
	public void setBuildingCategory(final BuildingCategory buildingCategory) {
		this.buildingCategory = buildingCategory;
	}

	/** {@inheritDoc} */
	@Override
	public BuildingCategory getBuildingCategory() {
		return this.buildingCategory;
	}
	
	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(
			final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDesignator(final String designator) {
		this.designator = designator;		
	}

	/** {@inheritDoc} */
	@Override
	public String getDesignator() {
		return this.designator;
	}
	

	/** {@inheritDoc} */
	@Override
	public void setCoordinates(final String coordinates) {
		this.coordinates = coordinates;
	}

	/** {@inheritDoc} */
	@Override
	public String getCoordinates() {
		return this.coordinates;
	}	
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Address)) {
			return false;
		}
		Address that = (Address) obj;
		
		if (this.getValue() == null) {
			throw new IllegalStateException("Value required");
		}
		if (!this.getValue().equals(that.getValue())) {
			return false;
		}
 		if (this.getZipCode() == null) {
			throw new IllegalStateException("Zip Code required");
		}
		if (!this.getZipCode().equals(that.getZipCode())) {
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
		if (this.getZipCode() == null) {
			throw new IllegalStateException("Zip Code required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getValue().hashCode();
		hashCode = 29 * hashCode + this.getZipCode().hashCode();
		return hashCode;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		final String zipCodeText;
		if (this.getZipCode() != null) {
			final String cityName;
			final String stateAbbreviation;
			if (this.getZipCode().getCity() != null) {
				cityName = this.getZipCode().getCity().getName();
				if (this.getZipCode().getCity().getState() != null) {
					stateAbbreviation = this.getZipCode().getCity().getState()
							.getAbbreviation();
				} else {
					stateAbbreviation = null;
				}
			} else {
				cityName = null;
				stateAbbreviation = null;
			}
			zipCodeText = String.format("%s, %s %s-%s",
					cityName, stateAbbreviation, zipCode.getValue(),
					zipCode.getExtension());
		} else {
			zipCodeText = null;
		}
		return String.format("#%d: %s, %s", this.getId(), this.getValue(),
				zipCodeText);
	}
}