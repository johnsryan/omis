package omis.location.domain;

import java.io.Serializable;

import omis.address.domain.Address;
import omis.datatype.DateRange;
import omis.organization.domain.Organization;

/**
 * Location from which an organization operates.
 * @author Stephen Abson
 * @version 0.1.0 (Feb 19, 2013)
 * @since OMIS 3.0
 */
public interface Location
		extends Serializable {

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
	 * Sets the organization.
	 * @param organization organization
	 */
	void setOrganization(Organization organization);
	
	/**
	 * Returns the organization.
	 * @return organization
	 */
	Organization getOrganization();
	
	/**
	 * Sets the address.
	 * @param address address
	 */
	void setAddress(Address address);
	
	/**
	 * Returns the address.
	 * @return address
	 */
	Address getAddress();
	
	/**
	 * Sets the range of dates during which the organization operates from
	 * a location.
	 * @param dateRange range of dates during which organization operates
	 * from location
	 */
	void setDateRange(DateRange dateRange);
	
	/**
	 * Returns the range of dates during which the organization operates from
	 * a location.
	 * @return range of dates during which organization operates from location
	 */
	DateRange getDateRange();
	
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
	
	/**
	 * Returns string representation of {@code this} including organization,
	 * address and start date.
	 * 
	 * @return string representation of {@code this} including organization,
	 * address and start date
	 */
	@Override
	String toString();
}