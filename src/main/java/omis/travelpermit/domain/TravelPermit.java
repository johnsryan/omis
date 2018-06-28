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
package omis.travelpermit.domain;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.offender.domain.OffenderAssociable;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;

/**
 * Travel permit.
 * @author Yidong Li
 * @version 0.1.0 (May 17, 2018)
 * @since OMIS 3.0
 */
public interface TravelPermit
		extends Updatable, Creatable, OffenderAssociable  {
	/**
	 * Sets the ID.
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns the ID.
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets offender.
	 * 
	 * @param offender offender
	 */
	void setOffender(Offender offender);
	
	/**
	 * Returns offender.
	 * 
	 * @return offender
	 */
	Offender getOffender();
	
	/**
	 * Sets the dateRange.
	 * @param dateRange date range
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns dateRange.
	 * @return dateRange date range
	 */
	DateRange getDateRange();
	
	/**
	 * Sets the purpose.
	 * @param purpose purpose
	 */
	void setPurpose(String purpose);
	
	/**
	 * Returns purpose.
	 * @return purpose purpose date range
	 */
	String getPurpose();
	
	/**
	 * Sets other travellers.
	 * @param otherTravellers other travellers
	 */
	void setOtherTravellers(OtherTravelers otherTravellers);
	
	/**
	 * Returns OtherTravellers.
	 * @return otherTravellers other travellers
	 */
	OtherTravelers getOtherTravellers();
	
	/**
	 * Sets destination destination.
	 * @param destination destination
	 */
	void setDestination(TravelDestination destination);
	
	/**
	 * Returns destination destination.
	 * @return destination destination
	 */
	TravelDestination getDestination();
	
	/**
	 * Sets periodicity periodicity.
	 * @param periodicity periodicity
	 */
	void setPeriodicity(TravelPermitPeriodicity periodicity);
	
	/**
	 * Returns periodicity periodicity.
	 * @return periodicity periodicity
	 */
	TravelPermitPeriodicity getPeriodicity();
	
	/**
	 * Sets issuance issuance.
	 * @param issuance issuance
	 */
	void setIssuance(TravelPermitIssuance issuance);
	
	/**
	 * Returns issuance issuance.
	 * @return issuance issuance
	 */
	TravelPermitIssuance getIssuance();
	
	/**
	 * Sets transportation transportation.
	 * @param issuance issuance
	 */
	void setTransportation(TravelTransportation transportation);
	
	/**
	 * Returns transportation transportation.
	 * @return transportation transportation
	 */
	TravelTransportation getTransportation();
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);
	
	/**
	 * Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}