package omis.hearing.domain.component;
import java.io.Serializable;
import java.util.Date;
import omis.hearing.domain.DispositionCategory;
import omis.hearing.domain.ResolutionClassificationCategory;
import omis.person.domain.Person;

/**
 * Resolution.
 * 
 *@author Annie Wahl 
 *@version 0.1.1 (Apr 17, 2018)
 *@since OMIS 3.0
 *
 */
public class Resolution implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Date date;
	
	private String descision;
	
	private String reason;
	
	private Person authority;
	
	private Date appealDate;
	
	private DispositionCategory disposition;
	
	private ResolutionClassificationCategory category;
	
	/**
	 * 
	 */
	public Resolution() {
	}

	/**
	 * Returns the date.
	 * @return date - Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date - Date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the descision.
	 * @return descision - String
	 */
	public String getDescision() {
		return descision;
	}

	/**
	 * Sets the descision.
	 * @param descision - String
	 */
	public void setDescision(final String descision) {
		this.descision = descision;
	}

	/**
	 * Returns the reason.
	 * @return reason - String
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 * @param reason - String
	 */
	public void setReason(final String reason) {
		this.reason = reason;
	}
	
	/**
	 * Returns the authority.
	 * @return authority - Person
	 */
	public Person getAuthority() {
		return authority;
	}

	/**
	 * Sets the authority.
	 * @param authority - Person
	 */
	public void setAuthority(final Person authority) {
		this.authority = authority;
	}
	
	/**
	 * Returns the appeal date.
	 * @return appealDate - Date of appeal
	 */
	public Date getAppealDate() {
		return appealDate;
	}

	/**
	 * Sets the appeal date.
	 * @param appealDate - Date appeal
	 */
	public void setAppealDate(final Date appealDate) {
		this.appealDate = appealDate;
	}

	/**
	 * Returns the disposition.
	 * @return disposition - DispositionCategory
	 */
	public DispositionCategory getDisposition() {
		return disposition;
	}

	/**
	 * Sets the disposition.
	 * @param disposition - DispositionCategory
	 */
	public void setDisposition(final DispositionCategory disposition) {
		this.disposition = disposition;
	}

	/**
	 * Returns the category.
	 * @return category - ResolutionClassificationCategory
	 */
	public ResolutionClassificationCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category - ResolutionClassificationCategory
	 */
	public void setCategory(final ResolutionClassificationCategory category) {
		this.category = category;
	}
	
	

}
