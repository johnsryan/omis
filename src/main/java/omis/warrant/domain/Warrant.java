package omis.warrant.domain;

import java.math.BigDecimal;
import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.jail.domain.Jail;
import omis.offender.domain.Offender;
import omis.person.domain.Person;

/**
 * Warrant.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (May 8, 2017)
 *@since OMIS 3.0
 *
 */
public interface Warrant extends Creatable, Updatable {
	
	/**
	 * Returns the ID of the Warrant
	 * @return ID
	 */
	public Long getId();
	
	/**
	 * Sets the ID of the Warrant
	 * @param id - Long
	 */
	public void setId(Long id);
	
	/**
	 * Returns the Offender for the Warrant
	 * @return offender - Offender
	 */
	public Offender getOffender();
	
	/**
	 * Sets the Offender for the Warrant
	 * @param offender - Offender
	 */
	public void setOffender(Offender offender);
	
	/**
	 * Returns the Date for the Warrant
	 * @return date - Date
	 */
	public Date getDate();
	
	/**
	 * Sets the Date for the Warrant
	 * @param date - Date
	 */
	public void setDate(Date date);
	
	/**
	 * Returns the Addressee for the Warrant
	 * @return addressee - String
	 */
	public String getAddressee();
	
	/**
	 * Sets the Addressee for the Warrant
	 * @param addressee - String
	 */
	public void setAddressee(String addressee);
	
	/**
	 * Returns the IssuedBy for the Warrant
	 * @return issuedBy - Person
	 */
	public Person getIssuedBy();
	
	/**
	 * Sets the IssuedBy for the Warrant
	 * @param issuedBy - Person
	 */
	public void setIssuedBy(Person issuedBy);
	
	/**
	 * Returns the Bondable for the Warrant
	 * @return bondable - Boolean
	 */
	public Boolean getBondable();
	
	/**
	 * Sets the Bondable for the Warrant
	 * @param bondable - Boolean
	 */
	public void setBondable(Boolean bondable);
	
	/**
	 * Returns the BondRecommendation for the Warrant
	 * @return bondRecommendation - BigDecimal
	 */
	public BigDecimal getBondRecommendation();
	
	/**
	 * Sets the BondRecommendation for the Warrant
	 * @param bondRecommendation - BigDecimal
	 */
	public void setBondRecommendation(BigDecimal bondRecommendation);
	
	/**
	 * Returns the WarrantReason for the Warrant
	 * @return warrantReason - WarrantReasonCategory
	 */
	public WarrantReasonCategory getWarrantReason();
	
	/**
	 * Sets the WarrantReason for the Warrant
	 * @param warrantReason - WarrantReasonCategory
	 */
	public void setWarrantReason(
			WarrantReasonCategory warrantReason);
	/**
	 * Returns holding jail.
	 * 
	 * @return holding jail
	 */
	public Jail getHoldingJail();
	
	/**
	 * Sets holding jail.
	 * 
	 * @param holdingJail holding jail
	 */
	public void setHoldingJail(Jail holdingJail);
	
	/** Compares {@code this} and {@code obj} for equality.
	 * <p>
	 * Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} */
	@Override
	boolean equals(Object obj);
	
	/** Returns a hash code for {@code this}.
	 * <p>
	 * Any mandatory property of {@code this} may be used in the hash code. If
	 * a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null} */
	@Override
	int hashCode();
}
