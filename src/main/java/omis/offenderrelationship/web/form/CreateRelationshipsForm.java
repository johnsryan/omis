package omis.offenderrelationship.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.web.form.AddressFields;
import omis.audit.domain.VerificationMethod;
import omis.contact.web.form.PoBoxFields;
import omis.family.web.form.FamilyAssociationFields;
import omis.offenderrelationship.web.controller.OffenderRelationshipAddressOperation;
import omis.person.web.form.PersonFields;
import omis.user.domain.UserAccount;
import omis.victim.web.form.VictimFields;
import omis.visitation.web.form.VisitationAssociationFields;

/**
 * Create relationships form.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.0 (May 5, 2016)
 * @since OMIS 3.0
 */
public class CreateRelationshipsForm implements Serializable {
	
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
	
	private FamilyAssociationFields familyAssociationFields;
	
	private VictimFields victimFields;
	
	private VisitationAssociationFields visitationAssociationFields;
	
	private UserAccount verifiedByUserAccount;
	
	private Date verificationDate;
	
	private Boolean verified;
	
	private VerificationMethod verificationMethod;
	
	private Boolean createFamilyMember;
	
	private Boolean createVictim;
	
	private Boolean createVisitor;
	
	private Boolean enterAddress;
	
	private Boolean enterPoBox;
	
	/**
	 * Instantiates a default instance of create relationships form.
	 */
	public CreateRelationshipsForm() {
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
			final List<OnlineAccountContactItem> onlineAccountContactItems) {
		this.onlineAccountContactItems = onlineAccountContactItems;
	}
	
	/**
	 * Sets note items.
	 * 
	 * @param noteItems note items
	 */
	public void setNoteItems(
			final List<OffenderRelationshipNoteItem> noteItems) {
		this.noteItems  = noteItems;
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
	 * Returns the family association fields.
	 * 
	 * @return the familyAssociationFields family association fields
	 */
	public FamilyAssociationFields getFamilyAssociationFields() {
		return this.familyAssociationFields;
	}

	/**
	 * Sets the family association fields.
	 * 
	 * @param familyAssociationFields family association fields
	 */
	public void setFamilyAssociationFields(
		final FamilyAssociationFields  familyAssociationFields) {
		this.familyAssociationFields = familyAssociationFields;
	}
	
	/**
	 * Returns the victim fields.
	 * 
	 * @return the victimFields victim fields
	 */
	public VictimFields getVictimFields() {
		return this.victimFields;
	}

	/**
	 * Sets the victim fields.
	 * 
	 * @param victimFields victim fields
	 */
	public void setVictimFields(final VictimFields  victimFields) {
		this.victimFields = victimFields;
	}
	
	/**
	 * Returns the visitation association fields.
	 * 
	 * @return the visitationAssociationFields visitation association fields
	 */
	public VisitationAssociationFields getVisitationAssociationFields() {
		return this.visitationAssociationFields;
	}

	/**
	 * Sets the visitation association fields.
	 * 
	 * @param visitationAssociationFields visitation association fields
	 */
	public void setVisitationAssociationFields(
		final	VisitationAssociationFields  visitationAssociationFields) {
		this.visitationAssociationFields = visitationAssociationFields;
	}
	
	/**
	 * Returns Verified User Account.
	 * 
	 * @return VerifiedByUserAccount verified user account
	 */
	public UserAccount getVerifiedByUserAccount() {
		return verifiedByUserAccount;
	}

	/**
	 * Sets the  Verified User Account.
	 * 
	 * @param verifiedByUserAccount verified user account
	 */
	public void setVerifiedByUserAccount(final UserAccount 
		verifiedByUserAccount) {
		this.verifiedByUserAccount = verifiedByUserAccount;
	}

	/**
	 * Returns Verification date.
	 * 
	 * @return VerificationDate Verification date
	 */
	public Date getVerificationDate() {
		return verificationDate;
	}

	/**
	 * Sets the Verification date.
	 * 
	 * @param verificationDate Verification date
	 */
	public void setVerificationDate(final Date verificationDate) {
		this.verificationDate = verificationDate;
	}

	/**
	 * Returns verified or not.
	 * 
	 * @return verified verified or not
	 */
	public Boolean getVerified() {
		return verified;
	}

	/**
	 * Sets verified or not.
	 * 
	 * @param verified verified or not
	 */
	public void setVerified(final Boolean verified) {
		this.verified = verified;
	}

	/**
	 * Returns verification method.
	 * 
	 * @return verificationMethod verification method
	 */
	public VerificationMethod getVerificationMethod() {
		return verificationMethod;
	}

	/**
	 * Sets verificationMethod.
	 * 
	 * @param verificationMethod verification method
	 */
	public void setVerificationMethod(final VerificationMethod 
		verificationMethod) {
		this.verificationMethod = verificationMethod;
	}
	
	/**
	 * Returns CreateFamilyMember.
	 * 
	 * @return CreateFamilyMember create family member
	 */
	public Boolean getCreateFamilyMember() {
		return createFamilyMember;
	}

	/**
	 * Sets CreateFamilyMember.
	 * 
	 * @param createFamilyMember Create Family Member
	 */
	public void setCreateFamilyMember(final Boolean createFamilyMember) {
		this.createFamilyMember = createFamilyMember;
	}
	
	/**
	 * Returns CreateVictim.
	 * 
	 * @return CreateVictim Create Victim
	 */
	public Boolean getCreateVictim() {
		return createVictim;
	}

	/**
	 * Sets CreateVictim.
	 * 
	 * @param createVictim Create Victim
	 */
	public void setCreateVictim(final Boolean createVictim) {
		this.createVictim = createVictim;
	}
	
	/**
	 * Returns CreateVisitor.
	 * 
	 * @return CreateVisitor Create Visitor
	 */
	public Boolean getCreateVisitor() {
		return createVisitor;
	}

	/**
	 * Sets CreateVisitor.
	 * 
	 * @param createVisitor Create Visitor
	 */
	public void setCreateVisitor(final Boolean createVisitor) {
		this.createVisitor = createVisitor;
	}
	
	/**
	 * Returns EnterAddress.
	 * 
	 * @return E\enterAddress Enter Address
	 */
	public Boolean getEnterAddress() {
		return enterAddress;
	}

	/**
	 * Sets EnterAddress.
	 * 
	 * @param enterAddress enter address
	 */
	public void setEnterAddress(final Boolean enterAddress) {
		this.enterAddress = enterAddress;
	}
	
	/**
	 * Returns EnterPoBox.
	 * 
	 * @return enterPoBox Enter PoBox
	 */
	public Boolean getEnterPoBox() {
		return enterPoBox;
	}

	/**
	 * Sets enterPoBox.
	 * 
	 * @param enterPoBox enter PoBox
	 */
	public void setEnterPoBox(final Boolean enterPoBox) {
		this.enterPoBox = enterPoBox;
	}
}