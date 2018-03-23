package omis.offenderrelationship.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.web.form.AddressFields;
import omis.audit.domain.VerificationMethod;
import omis.contact.web.form.PoBoxFields;
import omis.offenderrelationship.web.controller.OffenderRelationshipAddressOperation;
import omis.person.web.form.PersonFields;
import omis.user.domain.UserAccount;

/**
 * Edit relationships form.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (Jan 26, 2016)
 * @since OMIS 3.0
 */
public class EditRelationshipsForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private PersonFields personFields;
	private AddressFields addressFields;
	private PoBoxFields poBoxFields;
	private OffenderRelationshipAddressOperation addressOperation;
	private Address address;
	private String addressQuery;
	private List<TelephoneNumberItem> telephoneNumberItems
		= new ArrayList<TelephoneNumberItem>();
	private List<OnlineAccountContactItem> onlineAccountContactItems
		= new ArrayList<OnlineAccountContactItem>();
	private List<OffenderRelationshipNoteItem> noteItems
		= new ArrayList<OffenderRelationshipNoteItem>();
	private UserAccount verifiedByUserAccount;
	private Date verificationDate;
	private Boolean verified;
	private VerificationMethod verificationMethod;
	private Boolean enterAddress;
	private Boolean enterPoBox;
	private boolean validateSocialSecurityNumber;
	
	/**
	 * Instantiates a default instance of edit relationships form.
	 */
	public EditRelationshipsForm() {
		//Default constructor.
	}

	/**
	 * Returns person fields.
	 * 
	 * @return person fields.
	 */
	public PersonFields getPersonFields() {
		return this.personFields;
	}

	/**
	 * Sets person fields.
	 * 
	 * @param personFields person fields
	 */
	public void setPersonFields(final PersonFields personFields) {
		this.personFields = personFields;
	}

	/**
	 * Returns address fields.
	 * 
	 * @return address fields
	 */
	public AddressFields getAddressFields() {
		return this.addressFields;
	}

	/**
	 * Sets address fields.
	 * 
	 * @param addressFields address fields
	 */
	public void setAddressFields(final AddressFields addressFields) {
		this.addressFields = addressFields;
	}	
	
	/**
	 * Returns po box fields.
	 * 
	 * @return po box fields
	 */
	public PoBoxFields getPoBoxFields() {
		return this.poBoxFields;
	}

	/**
	 * Sets po box fields.
	 * 
	 * @param poBoxFields po box fields
	 */
	public void setPoBoxFields(final PoBoxFields poBoxFields) {
		this.poBoxFields = poBoxFields;
	}
	
	/**
	 * Returns addressOperation.
	 * 
	 * @return addressOperatiion address operation
	 */
	public OffenderRelationshipAddressOperation getAddressOperation() {
		return this.addressOperation;
	}

	/**
	 * Sets addressOperation.
	 * 
	 * @param addressOperation address operation
	 */
	public void setAddressOperation(
		final OffenderRelationshipAddressOperation addressOperation) {
		this.addressOperation = addressOperation;
	}
	
	/**
	 * Returns address.
	 * 
	 * @return address
	 */
	public Address getAddress() {
		return this.address;
	}

	/**
	 * Sets address.
	 * 
	 * @param address address
	 */
	public void setAddress(final Address address) {
		this.address = address;
	}
	
	/**
	 * Returns addressQuery.
	 * 
	 * @return address query
	 */
	public String getAddressQuery() {
		return this.addressQuery;
	}

	/**
	 * Sets address query.
	 * 
	 * @param addressQuery address query
	 */
	public void setAddressQuery(final String addressQuery) {
		this.addressQuery = addressQuery;
	}
	
	/**
	 * @return the telephoneNumberItems
	 */
	public List<TelephoneNumberItem> getTelephoneNumberItems() {
		return this.telephoneNumberItems;
	}

	/**
	 * @param telephoneNumberItems telephoneNumberItems
	 */
	public void setTelephoneNumberItems(
		final	List<TelephoneNumberItem> telephoneNumberItems) {
		this.telephoneNumberItems = telephoneNumberItems;
	}
	
	/**
	 * Returns the online account contact items.
	 * 
	 * @return the onlineAccountContactItems
	 */
	public List<OnlineAccountContactItem> getOnlineAccountContactItems() {
		return this.onlineAccountContactItems;
	}

	/**
	 * Sets the online account contact items.
	 * 
	 * @param onlineAccountContactItems online account contact items
	 */
	public void setOnlineAccountContactItems(
		final	List<OnlineAccountContactItem> onlineAccountContactItems) {
		this.onlineAccountContactItems = onlineAccountContactItems;
	}
	
	/**
	 * Sets note items.
	 * 
	 * @param noteItems note items
	 */
	public void setNoteItems(
			final List<OffenderRelationshipNoteItem> noteItems) {
		this.noteItems = noteItems;
	}
	
	/**
	 * Returns note items.
	 * 
	 * @return note items
	 */
	public List<OffenderRelationshipNoteItem> getNoteItems() {
		return this.noteItems;
	}
	
	/**
	 * Returns the VerifiedByUserAccount.
	 * 
	 * @return VerifiedByUserAccount VerifiedByUserAccount
	 */
	public UserAccount getVerifiedByUserAccount() {
		return verifiedByUserAccount;
	}

	/**
	 * Sets verifiedByUserAccount.
	 * 
	 * @param verifiedByUserAccount verifiedByUserAccount
	 */
	public void setVerifiedByUserAccount(final UserAccount 
		verifiedByUserAccount) {
		this.verifiedByUserAccount = verifiedByUserAccount;
	}

	/**
	 * Returns the VerificationDate.
	 * 
	 * @return VerificationDate VerificationDate
	 */
	public Date getVerificationDate() {
		return verificationDate;
	}

	/**
	 * Sets verificationDate.
	 * 
	 * @param verificationDate verificationDate
	 */
	public void setVerificationDate(final Date verificationDate) {
		this.verificationDate = verificationDate;
	}

	/**
	 * Returns verified.
	 * 
	 * @return verified verified
	 */
	public Boolean getVerified() {
		return verified;
	}

	/**
	 * Sets  verified.
	 * 
	 * @param  verified  verified
	 */
	public void setVerified(final Boolean verified) {
		this.verified = verified;
	}

	/**
	 * Returns verificationMethod.
	 * 
	 * @return verificationMethod verification method
	 */
	public VerificationMethod getVerificationMethod() {
		return verificationMethod;
	}

	/**
	 * Sets  verificationMethod.
	 * 
	 * @param  verificationMethod verification method
	 */
	public void setVerificationMethod(final VerificationMethod 
		verificationMethod) {
		this.verificationMethod = verificationMethod;
	}
	
	/**
	 * Returns enterAddress.
	 * 
	 * @return enterAddress enter address
	 */
	public Boolean getEnterAddress() {
		return enterAddress;
	}

	/**
	 * Sets  enterAddress.
	 * 
	 * @param  enterAddress enter address
	 */
	public void setEnterAddress(final Boolean enterAddress) {
		this.enterAddress = enterAddress;
	}
	
	/**
	 * Returns enterPoBox.
	 * 
	 * @return enterPoBox enter PoBox
	 */
	public Boolean getEnterPoBox() {
		return enterPoBox;
	}

	/**
	 * Sets  enterPoBox.
	 * 
	 * @param  enterPoBox enter PoBox
	 */
	public void setEnterPoBox(final Boolean enterPoBox) {
		this.enterPoBox = enterPoBox;
	}
	
	/**
	 * Returns whether to validate social security number.
	 * 
	 * @return whether to validate social security number
	 */
	public boolean getValidateSocialSecurityNumber() {
		return this.validateSocialSecurityNumber;
	}

	/**
	 * Sets whether to validate social security number.
	 * 
	 * @param validateSocialSecurityNumber whether to validate social security
	 * number
	 */
	public void setValidateSocialSecurityNumber(
			final boolean validateSocialSecurityNumber) {
		this.validateSocialSecurityNumber = validateSocialSecurityNumber;
	}	
}