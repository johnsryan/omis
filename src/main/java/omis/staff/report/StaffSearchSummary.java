package omis.staff.report;

import java.io.Serializable;

/**
 * Staff search summary.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (Mar 29, 2016)
 * @since OMIS 3.0
 */

public class StaffSearchSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long staffId;
	
	private String staffMemberFirstName;
	
	private String staffMemberMiddleName;
	
	private String staffMemberLastName;
	
	private String staffMemberSuffixName;
		
	private String staffMemberTitle;
	
	private String organizationName;
	
	private String organizationDivision;
	
	private Long telephoneNumber;
	
	private String emailAddress;
		
	private Boolean active;

	/**
	 * Constructor.
	 * 
	 * @param staffId staff id
	 * @param staffMemberFirstName staff member first name
	 * @param staffMemberMiddleName staff member middle name
	 * @param staffMemberLastName staff member last name
	 * @param staffMemberTitle staff member title
	 * @param organizationName organization name
	 * @param active active
	 */
	public StaffSearchSummary(final Long staffId, 
			final String staffMemberFirstName, 
			final String staffMemberMiddleName, 
			final String staffMemberLastName,
			final String staffMemberSuffixName,
			final String staffMemberTitle,
			final String organizationName, 
			final String organizationDivision, 
			final Long telephoneNumber,
			final String emailAddress, final Boolean active) {
		this.staffId = staffId;
		this.staffMemberFirstName = staffMemberFirstName;
		this.staffMemberMiddleName = staffMemberMiddleName;
		this.staffMemberLastName = staffMemberLastName;
		this.staffMemberSuffixName = staffMemberSuffixName;
		this.staffMemberTitle = staffMemberTitle;
		this.organizationName = organizationName;
		this.organizationDivision = organizationDivision;
		this.telephoneNumber = telephoneNumber;
		this.emailAddress = emailAddress;
		this.active = active;
	}

	/**
	 * Gets the staff ID.
	 * 
	 * @return the staff ID
	 */
	public Long getStaffId() {
		return this.staffId;
	}

	/**
	 * Gets the staff member first name.
	 * 
	 * @return the staff member first name
	 */
	public String getStaffMemberFirstName() {
		return this.staffMemberFirstName;
	}

	/**
	 * Gets the staff member middle name. 
	 * 
	 * @return the staff member middle name
	 */
	public String getStaffMemberMiddleName() {
		return this.staffMemberMiddleName;
	}

	/**
	 * Gets the staff member last name.
	 * 
	 * @return the staff member last nName
	 */
	public String getStaffMemberLastName() {
		return this.staffMemberLastName;
	}

	/**
	 * Gets the staff member suffix name.
	 *
	 * @return the staff member suffix name
	 */
	public String getStaffMemberSuffixName() {
		return this.staffMemberSuffixName;
	}

	/**
	 * Gets the staff member title.
	 * 
	 * @return the staff member title
	 */
	public String getStaffMemberTitle() {
		return this.staffMemberTitle;
	}

	/**
	 * Gets the organization name.
	 * 
	 * @return the organization name
	 */
	public String getOrganizationName() {
		return this.organizationName;
	}

	/**
	 * Gets the active boolean.
	 * 
	 * @return the active
	 */
	public Boolean getActive() {
		return this.active;
	}

	/**
	 * Gets the organization division.
	 * 
	 * @return the organization division
	 */
	public String getOrganizationDivision() {
		return this.organizationDivision;
	}

	/**
	 * Sets the organization division.  
	 * @param organizationDivision organization division
	 */
	public void setOrganizationDivision(String organizationDivision) {
		this.organizationDivision = organizationDivision;
	}

	/**
	 * Gets the telephone number.
	 * 
	 * @return the telephone number
	 */
	public Long getTelephoneNumber() {
		return this.telephoneNumber;
	}

	/**
	 * Sets the telephone number. 
	 * 
	 * @param telephoneNumber telephone number
	 */
	public void setTelephoneNumber(Long telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * Gets the email address.
	 * 
	 * @return the email address
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 * Sets the email address.
	 * 
	 * @param emailAddress email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}	
}