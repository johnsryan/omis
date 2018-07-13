package omis.offenderrelationship.report;

import java.util.Date;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.report.AddressSummarizable;
import omis.address.report.delegate.AddressSummaryDelegate;
import omis.contact.domain.TelephoneNumber;
import omis.contact.domain.TelephoneNumberCategory;
import omis.contact.report.TelephoneNumberSummarizable;
import omis.contact.report.delegate.TelephoneNumberSummaryDelegate;
import omis.demographics.domain.Sex;
import omis.offender.domain.Offender;
import omis.person.domain.Person;

/**
 * Summary of offender relationship.
 *
 * @author Stephen Abson
 * @version 0.0.1 (Jun 30, 2015)
 * @since OMIS 3.0
 */
public class OffenderRelationshipSummary
		implements AddressSummarizable, TelephoneNumberSummarizable {

	private static final long serialVersionUID = 1L;
	
	private final Long id;
	
	private final String lastName;
	
	private final String firstName;
	
	private final String middleName;
	
	private final String suffix;
	
	private final Date birthDate;
	
	private final Sex sex;
	
	private final Integer socialSecurityNumber;
	
	private final Boolean offender;
	
	private final Integer offenderNumber;
	
	private final Long offenderAssociationCount;
	
	private final Long criminalAssociationCount;
	
	private final Long familyMemberCount;
	
	private final Long victimCount;
	
	private final Long visitorCount;
	
	private final Boolean address;
	
	private final AddressSummaryDelegate addressSummaryDelegate;
	
	private final Boolean telephoneNumber;
	
	private final TelephoneNumberSummaryDelegate telephoneNumberSummaryDelegate;
	
	private final Long relationshipCount;
	
	/**
	 * Instantiates an offender relationship summary.
	 * 
	 * @param relation relation
	 * @param offender relation as offender if relation is an offender
	 * @param offenderAssociationCount count of associated offenders
	 * @param criminalAssociationCount count of associated criminals
	 * @param familyMemberCount count of family member
	 * @param victimCount count of victims
	 * @param visitorCount count of visitors
	 * @param address address
	 * @param telephoneNumber telephone number
	 */
	public OffenderRelationshipSummary(
			final Person relation,
			final Offender offender,
			final Long offenderAssociationCount,
			final Long criminalAssociationCount,
			final Long familyMemberCount,
			final Long victimCount,
			final Long visitorCount,
			final Address address,
			final TelephoneNumber telephoneNumber,
			final Long relationshipCount) {
		this.id = relation.getId();
		this.lastName = relation.getName().getLastName();
		this.firstName = relation.getName().getFirstName();
		this.middleName = relation.getName().getMiddleName();
		this.suffix = relation.getName().getSuffix();
		if (relation.getIdentity() != null) {
			this.birthDate = relation.getIdentity().getBirthDate();
			this.sex = relation.getIdentity().getSex();
			this.socialSecurityNumber = relation.getIdentity()
					.getSocialSecurityNumber();
		} else {
			this.birthDate = null;
			this.sex = null;
			this.socialSecurityNumber = null;
		}
		if (offender != null) {
			this.offender = true;
			this.offenderNumber = offender.getOffenderNumber();
		} else {
			this.offender = false;
			this.offenderNumber = null;
		}

		this.offenderAssociationCount = offenderAssociationCount;
		this.criminalAssociationCount = criminalAssociationCount;
		this.familyMemberCount = familyMemberCount;
		this.victimCount = victimCount;
		this.visitorCount = visitorCount;
		this.addressSummaryDelegate = new AddressSummaryDelegate(address);
		if (address != null) {
			this.address = true;
		} else {
			this.address = false;
		}
		this.telephoneNumberSummaryDelegate
			= new TelephoneNumberSummaryDelegate(telephoneNumber);
		if (telephoneNumber != null) {
			this.telephoneNumber = true;
		} else {
			this.telephoneNumber = false;
		}
		this.relationshipCount = relationshipCount;
	}
	
	/**
	 * Instantiates an implementation of OffenderRelationshipSummary.
	 * 
	 * @param id ID
	 * @param lastName last name
	 * @param firstName first name
	 * @param middleName middle name
	 * @param suffix suffix
	 * @param birthDate birth date
	 * @param sex sex
	 * @param socialSecurityNumber social security number
	 * @param offenderNumber offender number
	 * @param offenderAssociationCount count of associated offenders
	 * @param criminalAssociationCount count of associated criminals
	 * @param familyMemberCount count of family member
	 * @param victimCount count of victims
	 * @param visitorCount count of visitors
	 * @param address address
	 * @param telephoneNumber telephone number
	 */
	public OffenderRelationshipSummary(
			final Long id, final String lastName, final String firstName, 
			final String middleName, final String suffix, final Date birthDate,
			final Sex sex, final Integer socialSecurityNumber,
			final Integer offenderNumber,
			final Long offenderAssociationCount,
			final Long criminalAssociationCount,
			final Long familyMemberCount,
			final Long victimCount,
			final Long visitorCount,
			final Address address,
			final TelephoneNumber telephoneNumber,
			final Long relationshipCount) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.suffix = suffix;
		this.birthDate = birthDate;
		this.sex = sex;
		this.socialSecurityNumber = socialSecurityNumber;		
		if (offenderNumber != null) {
			this.offender = true;
			this.offenderNumber = offenderNumber;
		} else {
			this.offender = false;
			this.offenderNumber = null;
		}
		this.offenderAssociationCount = offenderAssociationCount;
		this.criminalAssociationCount = criminalAssociationCount;
		this.familyMemberCount = familyMemberCount;
		this.victimCount = victimCount;
		this.visitorCount = visitorCount;
		this.addressSummaryDelegate = new AddressSummaryDelegate(address);
		if (address != null) {
			this.address = true;
		} else {
			this.address = false;
		}
		this.telephoneNumberSummaryDelegate
			= new TelephoneNumberSummaryDelegate(telephoneNumber);
		if (telephoneNumber != null) {
			this.telephoneNumber = true;
		} else {
			this.telephoneNumber = false;
		}
		this.relationshipCount = relationshipCount;
	}

	/**
	 * Returns ID.
	 * 
	 * @return ID
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Returns last name.
	 * 
	 * @return last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Returns first name.
	 * 
	 * @return first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Returns middle name.
	 * 
	 * @return middle name
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * Returns suffix.
	 * 
	 * @return suffix
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Returns birth date.
	 * 
	 * @return birth date
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * Returns sex.
	 * 
	 * @return sex
	 */
	public Sex getSex() {
		return this.sex;
	}

	/**
	 * Returns social security number.
	 * 
	 * @return social security number
	 */
	public Integer getSocialSecurityNumber() {
		return this.socialSecurityNumber;
	}

	/**
	 * Returns whether relation is an offender.
	 * 
	 * @return whether relation is an offender
	 */
	public Boolean getOffender() {
		return this.offender;
	}

	/**
	 * Returns offender number of relation if relation is an offender. If
	 * relation is not an offender return {@code null}.
	 * 
	 * @return offender number of relation if relation is an offender;
	 * {@code null} otherwise
	 */
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}

	/**
	 * Returns count of associated offenders.
	 * 
	 * @return count of associated offenders
	 */
	public Long getOffenderAssociationCount() {
		return this.offenderAssociationCount;
	}

	/**
	 * Returns count of associated criminals.
	 * 
	 * @return count of associated criminals 
	 */
	public Long getCriminalAssociationCount() {
		return this.criminalAssociationCount;
	}

	/**
	 * Returns count of associated family members.
	 * 
	 * @return count of associated family members
	 */
	public Long getFamilyMemberCount() {
		return this.familyMemberCount;
	}

	/**
	 * Returns count of associated victims.
	 * 
	 * @return count of associated victims
	 */
	public Long getVictimCount() {
		return this.victimCount;
	}

	/**
	 * Returns count of associated visitors. 
	 * 
	 * @return count of associated visitors
	 */
	public Long getVisitorCount() {
		return this.visitorCount;
	}
	
	/**
	 * Returns whether relation has telephone number.
	 * 
	 * @return whether relation has telephone number
	 */
	public Boolean getTelephoneNumber() {
		return this.telephoneNumber;
	}

	/**
	 * Returns relationship count.
	 * 
	 * @return relationship count
	 */
	public Long getRelationshipCount() {
		return this.relationshipCount;
	}

	/** {@inheritDoc} */
	@Override
	public Long getTelephoneNumberValue() {
		return this.telephoneNumberSummaryDelegate.getValue();
	}

	/** {@inheritDoc} */
	@Override
	public Integer getTelephoneNumberExtension() {
		return this.telephoneNumberSummaryDelegate.getExtension();
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getTelephoneNumberPrimary() {
		return this.telephoneNumberSummaryDelegate.getPrimary();
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getTelephoneNumberActive() {
		return this.telephoneNumberSummaryDelegate.getActive();
	}

	/** {@inheritDoc} */
	@Override
	public TelephoneNumberCategory getTelephoneNumberCategory() {
		return this.telephoneNumberSummaryDelegate.getCategory();
	}

	/**
	 * Returns whether relation has a mailing address.
	 * 
	 * @return whether relation has a mailing address
	 */
	public Boolean getAddress() {
		return this.address;
	}
	
	/** {@inheritDoc} */
	@Override
	public String getAddressValue() {
		return this.addressSummaryDelegate.getValue();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressDesignator() {
		return this.addressSummaryDelegate.getDesignator();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressCoordinates() {
		return this.addressSummaryDelegate.getCoordinates();
	}

	/** {@inheritDoc} */
	@Override
	public BuildingCategory getAddressCategory() {
		return this.addressSummaryDelegate.getCategory();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressCityName() {
		return this.addressSummaryDelegate.getCityName();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressStateName() {
		return this.addressSummaryDelegate.getStateName();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressStateAbbreviation() {
		return this.addressSummaryDelegate.getStateAbbreviation();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressZipCodeValue() {
		return this.addressSummaryDelegate.getZipCodeValue();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressZipCodeExtension() {
		return this.addressSummaryDelegate.getZipCodeExtension();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressCountryName() {
		return this.addressSummaryDelegate.getCountryName();
	}

	/** {@inheritDoc} */
	@Override
	public String getAddressCountryAbbreviation() {
		return this.addressSummaryDelegate.getCountryAbbreviation();
	}
}