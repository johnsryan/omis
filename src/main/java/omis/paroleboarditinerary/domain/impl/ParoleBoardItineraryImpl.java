package omis.paroleboarditinerary.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardlocation.domain.ParoleBoardLocation;

/**
 * Implementation of parole board itinerary.
 * 
 * @author Josh Divine
 * @author Annie Wahl
 * @version 0.1.1 (Jan 23, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryImpl implements ParoleBoardItinerary {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private CreationSignature creationSignature;
	
	private UpdateSignature updateSignature;
	
	private DateRange dateRange;
	
	private ParoleBoardLocation paroleBoardLocation;
	
	/** 
	 * Instantiates an implementation of parole board itinerary. 
	 */
	public ParoleBoardItineraryImpl() {
		// Default constructor.
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
		return creationSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public UpdateSignature getUpdateSignature() {
		return updateSignature;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setDateRange(final DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/** {@inheritDoc} */
	@Override
	public DateRange getDateRange() {
		return dateRange;
	}
	
	/**{@inheritDoc} */
	@Override
	public ParoleBoardLocation getParoleBoardLocation() {
		return this.paroleBoardLocation;
	}
	
	/**{@inheritDoc} */
	@Override
	public void setParoleBoardLocation(
			final ParoleBoardLocation paroleBoardLocation) {
		this.paroleBoardLocation = paroleBoardLocation;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParoleBoardItinerary)) {
			return false;
		}
		ParoleBoardItinerary that = (ParoleBoardItinerary) obj;
		if (this.getParoleBoardLocation() == null) {
			throw new IllegalStateException("Parole Board Location required");
		}
		if (!this.getParoleBoardLocation().equals(
				that.getParoleBoardLocation())) {
			return false;
		}
		if (DateRange.getStartDate(this.getDateRange()) == null) {
			throw new IllegalStateException("Start date required");
		}
		if (!DateRange.getStartDate(this.getDateRange()).equals(
				DateRange.getStartDate(that.getDateRange()))) {
			return false;
		}
		if (DateRange.getEndDate(this.getDateRange()) == null) { 
			if (DateRange.getEndDate(that.getDateRange()) != null) {
				return false;
			}
		} else {
			if (!DateRange.getEndDate(this.getDateRange()).equals(
					DateRange.getEndDate(that.getDateRange()))) {
				return false;
			}
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getParoleBoardLocation() == null) {
			throw new IllegalStateException("Parole Board Location required");
		}
		if (DateRange.getStartDate(this.dateRange) == null) {
			throw new IllegalStateException("Start date required");
		}
		int hashCode = 14;
		hashCode = 29 * hashCode + this.getParoleBoardLocation().hashCode();
		hashCode = 29 * hashCode + DateRange.getStartDate(this.dateRange)
				.hashCode();
		if (DateRange.getEndDate(this.dateRange) != null) {
			hashCode = 29 * hashCode + DateRange.getEndDate(this.dateRange)
					.hashCode();
		}
		return hashCode;
	}
}
