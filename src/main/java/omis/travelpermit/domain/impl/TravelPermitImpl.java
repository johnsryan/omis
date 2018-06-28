/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.domain.impl;

import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;

/**
 * Implementation of travel permit.
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.0 (May 17, 2018)
 * @since OMIS 3.0 
 */
public class TravelPermitImpl implements TravelPermit {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Offender offender;
	private DateRange dateRange;
	private String purpose;
	private OtherTravelers otherTravellers;
	private TravelDestination destination;
	private TravelPermitPeriodicity periodicity;
	private TravelPermitIssuance issuance;
	private TravelTransportation transportation;
	private UpdateSignature updateSignature;
	private CreationSignature creationSignature;
	
	/** Constructor. */
	public TravelPermitImpl() {
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
	public Offender getOffender() {
		return this.offender;
	}

	/** {@inheritDoc} */
	@Override
	public void setOffender(final Offender offender) {
		this.offender = offender;
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
	public String getPurpose() {
		return this.purpose;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setPurpose(final String purpose) {
		this.purpose=purpose;
	}

	@Override
	public void setOtherTravellers(final OtherTravelers otherTravellers) {
		this.otherTravellers = otherTravellers;
		
	}

	@Override
	public OtherTravelers getOtherTravellers() {
		return this.otherTravellers;
	}

	@Override
	public void setDestination(final TravelDestination travelDestination) {
		this.destination = travelDestination;
	}

	@Override
	public TravelDestination getDestination() {
		return this.destination;
	}
	
	@Override
	public void setPeriodicity(final TravelPermitPeriodicity periodicity) {
		this.periodicity = periodicity;
	}

	@Override
	public TravelPermitPeriodicity getPeriodicity() {
		return this.periodicity;
	}
	
	@Override
	public void setIssuance(final TravelPermitIssuance issuance) {
		this.issuance = issuance;
	}

	@Override
	public TravelPermitIssuance getIssuance() {
		return this.issuance;
	}
	
	@Override
	public void setTransportation(TravelTransportation transportation) {
		this.transportation = transportation;
	}

	@Override
	public TravelTransportation getTransportation() {
		return this.transportation;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TravelPermit)) {
			return false;
		}
		
		TravelPermit that = (TravelPermit) obj;
		
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required.");
		}
		if (this.getDateRange().getStartDate() == null) {
			throw new IllegalStateException("Start date required.");
		}	
		if (this.getOffender().equals(that.getOffender())) {
			return true;
		}
		if (this.dateRange.getStartDate().equals(that.getDateRange().getStartDate())) {
			return true;
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getOffender() == null) {
			throw new IllegalStateException("Offender required.");
		}
		if (this.getDateRange().getStartDate() == null) {
			throw new IllegalStateException("Start date required.");
		} 
		
		int hashCode = 14;
		
		hashCode += 29 * this.getOffender().hashCode();
		hashCode += 29 * this.getDateRange().getStartDate().hashCode();
		return hashCode;
	}

	@Override
	public void setCreationSignature(final CreationSignature creationSignature) {
		this.creationSignature = creationSignature;
		
	}

	@Override
	public CreationSignature getCreationSignature() {
		return this.creationSignature;
	}

	@Override
	public void setUpdateSignature(final UpdateSignature updateSignature) {
		this.updateSignature = updateSignature;
		
	}

	@Override
	public UpdateSignature getUpdateSignature() {
		return this.updateSignature;
	}
}