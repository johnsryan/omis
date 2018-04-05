package omis.family.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.web.form.AddressFields;
import omis.audit.domain.VerificationMethod;
import omis.contact.web.form.PoBoxFields;
import omis.family.domain.FamilyAssociationCategory;
import omis.family.web.controller.FamilyAddressOperation;
import omis.offenderrelationship.web.form.OffenderRelationshipNoteItem;
import omis.person.web.form.PersonFields;
import omis.user.domain.UserAccount;

/**
 * Family association fields.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.1.0 (Jun 17, 2015)
 * @since OMIS 3.0
 */
public class FamilyAssociationForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private PersonFields personFields;
	private BuildingCategory homeType;
	private AddressFields addressFields;
	private String secondAddressDesignator;
	private PoBoxFields poBoxFields;
	private List<FamilyAssociationTelephoneNumberItem> 
		familyAssociationTelephoneNumberItems 
		= new ArrayList<FamilyAssociationTelephoneNumberItem>();
	private List<FamilyAssociationOnlineAccountItem> 
		familyAssociationOnlineAccountItems 
		= new ArrayList<FamilyAssociationOnlineAccountItem>();
	private Date startDate;
	private Date endDate;
	private FamilyAssociationCategory category;
	private Date marriageDate;
	private Date divorceDate;
	private Boolean emergencyContact;
	private Boolean dependent;
	private Boolean cohabitant;
	private List<OffenderRelationshipNoteItem> familyAssociationNoteItems 
		= new ArrayList<OffenderRelationshipNoteItem>();
	private FamilyAddressOperation addressOperation;
	private Address address;
	private String addressQuery;
	private UserAccount verifiedByUserAccount;
	private Date verificationDate;
	private Boolean verified;
	private VerificationMethod verificationMethod;
	private Boolean enterAddress;
	private Boolean enterPoBox;
	private boolean validateSocialSecurityNumber;
	
	/**
	 * Instantiates a default instance of family association fields.
	 */
	public FamilyAssociationForm() {
		//Default constructor.
	}

	/**
	 * Gets the person fields.
	 *
	 * @return the personFields
	 */
	public PersonFields getPersonFields() {
		return this.personFields;
	}

	/**
	 * Sets the person fields.
	 *
	 * @param personFields person fields
	 */
	public void setPersonFields(final PersonFields personFields) {
		this.personFields = personFields;
	}

	/**
	 * Gets the building category home type.
	 *
	 * @return the homeType
	 */
	public BuildingCategory getHomeType() {
		return this.homeType;
	}

	/**
	 * Sets the building category home type.
	 *
	 * @param homeType home type
	 */
	public void setHomeType(final BuildingCategory homeType) {
		this.homeType = homeType;
	}

	/**
	 * Gets the address fields.
	 *
	 * @return the addressFields
	 */
	public AddressFields getAddressFields() {
		return this.addressFields;
	}

	/**
	 * Sets the address fields. 
	 *
	 * @param addressFields address fields
	 */
	public void setAddressFields(final AddressFields addressFields) {
		this.addressFields = addressFields;
	}

	/**
	 * Gets the second address designator.
	 *
	 * @return the secondAddressDesignator
	 */
	public String getSecondAddressDesignator() {
		return this.secondAddressDesignator;
	}

	/**
	 * Sets the second address designator.
	 *
	 * @param secondAddressDesignator second address designator
	 */
	public void setSecondAddressDesignator(
			final String secondAddressDesignator) {
		this.secondAddressDesignator = secondAddressDesignator;
	}

	/**
	 * Gets the P O box fields.
	 *
	 * @return the poBoxFields
	 */
	public PoBoxFields getPoBoxFields() {
		return this.poBoxFields;
	}

	/**
	 * Sets the P O box fields.
	 *
	 * @param poBoxFields P O Box fields
	 */
	public void setPoBoxFields(final PoBoxFields poBoxFields) {
		this.poBoxFields = poBoxFields;
	}

	/**
	 * Gets the family association telephone number items.
	 *
	 * @return the familyAssociationTelephoneNumberItems
	 */
	public List<FamilyAssociationTelephoneNumberItem> 
		getFamilyAssociationTelephoneNumberItems() {
		return this.familyAssociationTelephoneNumberItems;
	}

	/**
	 * Sets the family association telephone number items.
	 *
	 * @param familyAssociationTelephoneNumberItems family association
	 * telephone number items
	 */
	public void setFamilyAssociationTelephoneNumberItems(
			final List<FamilyAssociationTelephoneNumberItem> 
			familyAssociationTelephoneNumberItems) {
		this.familyAssociationTelephoneNumberItems 
			= familyAssociationTelephoneNumberItems;
	}

	/**
	 * Gets the family association onlin account items.
	 *
	 * @return the familyAssociationOnlineAccountItems
	 */
	public List<FamilyAssociationOnlineAccountItem> 
		getFamilyAssociationOnlineAccountItems() {
		return this.familyAssociationOnlineAccountItems;
	}

	/**
	 * Sets the family association online account items.
	 *
	 * @param familyAssociationOnlineAccountItems family association
	 * online account items
	 */
	public void setFamilyAssociationOnlineAccountItems(
			final List<FamilyAssociationOnlineAccountItem> 
			familyAssociationOnlineAccountItems) {
		this.familyAssociationOnlineAccountItems 
			= familyAssociationOnlineAccountItems;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the startDate
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate start date
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate end date
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the family association category.
	 *
	 * @return the category
	 */
	public FamilyAssociationCategory getCategory() {
		return this.category;
	}

	/**
	 * Sets the family association category.
	 *
	 * @param category category
	 */
	public void setCategory(final FamilyAssociationCategory category) {
		this.category = category;
	}

	/**
	 * Gets the marriage date.
	 *
	 * @return the marriageDate
	 */
	public Date getMarriageDate() {
		return this.marriageDate;
	}

	/**
	 * Sets the marriage date.
	 *
	 * @param marriageDate marriage date
	 */
	public void setMarriageDate(final Date marriageDate) {
		this.marriageDate = marriageDate;
	}

	/**
	 * Gets the divorce date.
	 *
	 * @return the divorceDate
	 */
	public Date getDivorceDate() {
		return this.divorceDate;
	}

	/**
	 * Sets the divorce date.
	 *
	 * @param divorceDate divorce date
	 */
	public void setDivorceDate(final Date divorceDate) {
		this.divorceDate = divorceDate;
	}

	/**
	 * Gets the emergency contact.
	 *
	 * @return the emergencyContact
	 */
	public Boolean getEmergencyContact() {
		return this.emergencyContact;
	}

	/**
	 * Sets the emergency contact.
	 *
	 * @param emergencyContact emergency contact
	 */
	public void setEmergencyContact(final Boolean emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	/**
	 * Gets the dependent.
	 *
	 * @return the dependent
	 */
	public Boolean getDependent() {
		return this.dependent;
	}

	/**
	 * Sets the dependent.
	 *
	 * @param dependent dependent
	 */
	public void setDependent(final Boolean dependent) {
		this.dependent = dependent;
	}

	/**
	 * Gets the cohabitant.
	 *
	 * @return the cohabitant
	 */
	public Boolean getCohabitant() {
		return this.cohabitant;
	}

	/**
	 * Sets the cohabitant.
	 *
	 * @param cohabitant cohabitant
	 */
	public void setCohabitant(final Boolean cohabitant) {
		this.cohabitant = cohabitant;
	}

	/**
	 * Gets the family association note items.
	 *
	 * @return the familyAssociationNoteItems
	 */
	public List<OffenderRelationshipNoteItem> getFamilyAssociationNoteItems() {
		return this.familyAssociationNoteItems;
	}

	/**
	 * Sets the family association note items.
	 *
	 * @param familyAssociationNoteItems family association note items
	 */
	public void setFamilyAssociationNoteItems(
		final List<OffenderRelationshipNoteItem> familyAssociationNoteItems) {
		this.familyAssociationNoteItems = familyAssociationNoteItems;
	}

	/**
	 * Gets the family address operation.
	 *
	 * @return the addressOperation
	 */
	public FamilyAddressOperation getAddressOperation() {
		return this.addressOperation;
	}

	/**
	 * Sets the family address operation.
	 *
	 * @param addressOperation address operation
	 */
	public void setAddressOperation(
			final FamilyAddressOperation addressOperation) {
		this.addressOperation = addressOperation;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public Address getAddress() {
		return this.address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address address
	 */
	public void setAddress(final Address address) {
		this.address = address;
	}

	/**
	 * Gets the address query.
	 *
	 * @return the addressQuery
	 */
	public String getAddressQuery() {
		return this.addressQuery;
	}

	/**
	 * Sets the address query.
	 *
	 * @param addressQuery address query
	 */
	public void setAddressQuery(final String addressQuery) {
		this.addressQuery = addressQuery;
	}

	/**
	 * Gets the verified by user account.
	 *
	 * @return the verifiedByUserAccount
	 */
	public UserAccount getVerifiedByUserAccount() {
		return this.verifiedByUserAccount;
	}

	/**
	 * Sets the verified by user account.
	 *
	 * @param verifiedByUserAccount verified by user account
	 */
	public void setVerifiedByUserAccount(
			final UserAccount verifiedByUserAccount) {
		this.verifiedByUserAccount = verifiedByUserAccount;
	}

	/**
	 * Gets the verification date.
	 *
	 * @return the verificationDate
	 */
	public Date getVerificationDate() {
		return this.verificationDate;
	}

	/**
	 * Sets the verification date.
	 *
	 * @param verificationDate verification date
	 */
	public void setVerificationDate(final Date verificationDate) {
		this.verificationDate = verificationDate;
	}

	/**
	 * Gets the verified.
	 *
	 * @return the verified
	 */
	public Boolean getVerified() {
		return this.verified;
	}

	/**
	 * Sets the verified.
	 *
	 * @param verified verified
	 */
	public void setVerified(final Boolean verified) {
		this.verified = verified;
	}

	/**
	 * Gets the verification method.
	 *
	 * @return the verificationMethod
	 */
	public VerificationMethod getVerificationMethod() {
		return this.verificationMethod;
	}

	/**
	 * Sets the verification method.
	 *
	 * @param verificationMethod verification method
	 */
	public void setVerificationMethod(
			final VerificationMethod verificationMethod) {
		this.verificationMethod = verificationMethod;
	}

	/**
	 * Gets the enter address.
	 *
	 * @return the enterAddress
	 */
	public Boolean getEnterAddress() {
		return this.enterAddress;
	}

	/**
	 * Sets the enter address.
	 *
	 * @param enterAddress enter address
	 */
	public void setEnterAddress(final Boolean enterAddress) {
		this.enterAddress = enterAddress;
	}

	/**
	 * Gets the enter P O Box.
	 *
	 * @return the enterPoBox
	 */
	public Boolean getEnterPoBox() {
		return this.enterPoBox;
	}

	/**
	 * Sets the enter P O Box.
	 *
	 * @param enterPoBox enter P O Box
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