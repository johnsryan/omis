package omis.residence.domain.impl;

import omis.address.domain.Address;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.audit.domain.VerificationSignature;
import omis.datatype.DateRange;
import omis.person.domain.Person;
import omis.residence.domain.ResidenceCategory;
import omis.residence.domain.ResidenceStatus;
import omis.residence.domain.ResidenceTerm;

/**
 * Implementation of residence term. 
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Sep 19, 2014)
 * @since OMIS 3.0
 */
public class ResidenceTermImpl
		implements ResidenceTerm {

	private static final long serialVersionUID = 1L;
	
	private CreationSignature creationSignature;

	private UpdateSignature updateSignature;

	private Long id;

	private Person person;

	private DateRange dateRange;

	private ResidenceStatus status;

	private Address address;

	private VerificationSignature verificationSignature;

	private ResidenceCategory category;

	private Boolean confirmed;
	
	private String notes;
	
	/** Instantiates an implementation of residence status term. */
	public ResidenceTermImpl() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCreationSignature(
			final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(
			final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
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
	public void setPerson(final Person person) {
		this.person = person;
	}

	/** {@inheritDoc} */
	@Override
	public Person getPerson() {
		return this.person;
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
	public void setStatus(final ResidenceStatus status) {
		this.status = status;
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceStatus getStatus() {
		return this.status;
	}
	
	/** {@inheritDoc} */
	@Override
	public Address getAddress() {
		return this.address;
	}

	/** {@inheritDoc} */
	@Override
	public void setAddress(final Address address) {
		this.address = address;		
	}

	/** {@inheritDoc} */
	@Override
	public VerificationSignature getVerificationSignature() {
		return this.verificationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setVerificationSignature(
			final VerificationSignature verficationSignature) {
		this.verificationSignature = verficationSignature;
		
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceCategory getCategory() {
		return this.category;
	}

	/** {@inheritDoc} */
	@Override
	public void setCategory(final ResidenceCategory category) {
		this.category = category;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getConfirmed() {
		return this.confirmed;
	}

	/** {@inheritDoc} */
	@Override
	public void setConfirmed(final Boolean confirmed) {
		this.confirmed = confirmed;
	}

	/** {@inheritDoc} */
	@Override
	public String getNotes() {
		return this.notes;
	}

	/** {@inheritDoc} */
	@Override
	public void setNotes(final String notes) {
		this.notes = notes;
	}	
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ResidenceTerm)) {
			return false;
		}
		ResidenceTerm that = (ResidenceTerm) obj;
		if (this.getPerson() == null) {
			throw new IllegalStateException("Person required");
		}
		if (!this.getPerson().equals(that.getPerson())) {
			return false;
		}
		if (this.getAddress() == null) {
			throw new IllegalStateException("Address required");
		}
		if (!this.getConfirmed().equals(that.getConfirmed())) {
			return false;
		}
		if (this.getDateRange() != null) {
			if (that.getDateRange() != null) {	
				if (this.getDateRange().getStartDate() != null) {
					if (!this.getDateRange().getStartDate().equals(
							that.getDateRange().getStartDate())) {
						return false;
					}
				} else if (that.getDateRange().getStartDate() != null) {
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
		if (this.getPerson() == null) {
			throw new IllegalStateException("Person required");
		}
		if (this.getAddress() == null) {
			throw new IllegalStateException("Address required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getPerson().hashCode();
		hashCode = 29 * hashCode + this.getAddress().hashCode();
		if (this.getDateRange() != null) {
			if (this.getDateRange().getStartDate() != null) {
				hashCode = 29 * hashCode + this.getDateRange()
						.getStartDate().hashCode();
			}
		}
		return hashCode;
	}	
}