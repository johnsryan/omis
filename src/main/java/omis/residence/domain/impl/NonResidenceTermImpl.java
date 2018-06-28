package omis.residence.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.audit.domain.VerificationSignature;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.person.domain.Person;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.residence.domain.NonResidenceTerm;
import omis.residence.domain.ResidenceStatus;

/**
 * Implementation of non residence term.
 * 
 * @author Sheronda Vaughn
 * @version 0.1.0 (Feb 19, 2015)
 * @since  OMIS 3.0
 */
public class NonResidenceTermImpl 
		implements NonResidenceTerm {

	private static final long serialVersionUID = 1L;
	
	private UpdateSignature updateSignature;
	
	private CreationSignature creationSignature;
	
	private Long id;
	
	private Person person;
	
	private DateRange dateRange;
	
	private Location location;
	
	private State state;
	
	private City city;
	
	private String notes;
	
	private VerificationSignature verificationSignature;
	
	private ResidenceStatus status;

	private Boolean confirmed;	
	
	/** Instantiates an implementation of non residence status term. */
	public NonResidenceTermImpl() {
		// Default instantiation
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
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
	public Person getPerson() {
		return this.person;
	}

	/** {@inheritDoc} */
	@Override
	public void setPerson(final Person person) {
		this.person = person;
	}

	/** {@inheritDoc} */
	@Override
	public DateRange getDateRange() {
		return this.dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public void setDateRange(final DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public Location getLocation() {
		return this.location;
	}

	/** {@inheritDoc} */
	@Override
	public void setLocation(final Location location) {
		this.location = location;
	}

	/** {@inheritDoc} */
	@Override
	public State getState() {
		return this.state;
	}

	/** {@inheritDoc} */
	@Override
	public void setState(final State state) {
		this.state = state;
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
	public VerificationSignature getVerificationSignature() {
		return this.verificationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setVerificationSignature(
			final VerificationSignature verificationSignature) {
		this.verificationSignature = verificationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public ResidenceStatus getStatus() {
		return this.status;
	}

	/** {@inheritDoc} */
	@Override
	public void setStatus(final ResidenceStatus status) {
		this.status = status;
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NonResidenceTerm)) {
			return false;
		}
		NonResidenceTerm that = (NonResidenceTerm) obj;
		if (this.getPerson() == null) {
			throw new IllegalStateException("Person required.");			
		}
		if (!this.getPerson().equals(that.getPerson())) {
			return false;
		}	
		if (this.getStatus() == null) {
			throw new IllegalStateException("Status required.");			
		}
		if (!this.getStatus().equals(that.getStatus())) {
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
		if (this.getLocation() != null) {
			if (!this.getLocation().equals(that.getLocation())) {
				return false;
			}
		} else if (that.getLocation() != null) {
			return false;
		}		
		if (this.getState() != null) {
			if (!this.getState().equals(that.getState())) {
				return false;
			}
		} else if (that.getState() != null) {
			return false;
		}
		if (this.getCity() != null) {
			if (!this.getCity().equals(that.getCity())) {
			return false;
			}
		} else if (that.getCity() != null) {
			return false;
		}
		return true;
	}
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getPerson() == null) {
			throw new IllegalStateException("Person required.");			
		}
		if (this.getStatus() == null) {
			throw new IllegalStateException("Status required.");
		}
		
		int hashCode = 14;
		
		hashCode = 29 * hashCode + this.getPerson().hashCode();
		hashCode = 29 * hashCode + this.getStatus().hashCode();
		if (this.getDateRange() != null) {
			if (this.getDateRange().getStartDate() != null) {
				hashCode = 29 * hashCode + this.getDateRange()
						.getStartDate().hashCode();
			}
		}
		if (this.getLocation() != null) {
			hashCode = 29 * hashCode + this.getLocation().hashCode();
		}
		if (this.getState() != null) {
			hashCode = 29 * hashCode + this.getState().hashCode();
		}
		if (this.getCity() != null) {
			hashCode = 29 * hashCode + this.getCity().hashCode();
		}
		return hashCode;
	}
}