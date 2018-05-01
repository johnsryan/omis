/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.victim.report;

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
import omis.person.domain.PersonIdentity;
import omis.person.domain.PersonName;
import omis.victim.domain.VictimAssociation;

/**
 * Summary of a victim association.
 * 
 * <p>A victim must have an association with an offender. This summarizes one
 * such association. 
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 8, 2015)
 * @since OMIS 3.0
 */
public class VictimAssociationSummary
		implements AddressSummarizable, TelephoneNumberSummarizable {
	
	private static final long serialVersionUID = 1L;
	
	private final Long id;
	
	private final String lastName;
	
	private final String firstName;
	
	private final String middleName;
	
	private final String suffix;
	
	private final Sex sex;
	
	private final Date birthDate;
	
	private final Long offenderId;
	
	private final String offenderLastName;
	
	private final String offenderFirstName;
	
	private final String offenderMiddleName;
	
	private final Integer offenderNumber;
	
	private final Long associationId;
	
	private final Date registerDate;
	
	private final Boolean packetSent;
	
	private final Date packetSendDate;
	
	private final Boolean victim;
	
	private final Boolean docRegistered;
	
	private final Boolean business;
	
	private final Boolean vineRegistered;
	
	private final Boolean address;
	
	private final AddressSummaryDelegate addressSummaryDelegate;
	
	private final Boolean telephoneNumber;
	
	private final TelephoneNumberSummaryDelegate telephoneNumberSummaryDelegate;
	
	private final Long noteCount;
	
	private final Boolean victimOffender;
	
	private final Integer victimOffenderNumber;
	
	/**
	 * Instantiates victim summary.
	 * 
	 * @param association victim association
	 * @param address address
	 * @param telephoneNumber telephone number
	 * @param noteCount note count
	 */
	public VictimAssociationSummary(
			final VictimAssociation association,
			final Address address,
			final TelephoneNumber telephoneNumber,
			final Long noteCount,
			final Offender victimOffender) {
		Person victim = association.getRelationship().getSecondPerson();
		this.id = victim.getId();
		PersonName victimName = victim.getName();
		this.lastName = victimName.getLastName();
		this.firstName = victimName.getFirstName();
		this.middleName = victimName.getMiddleName();
		this.suffix = victimName.getSuffix();
		PersonIdentity victimIdentity = victim.getIdentity();
		if (victimIdentity != null) {
			this.sex = victimIdentity.getSex();
			this.birthDate = victimIdentity.getBirthDate();
		} else {
			this.sex = null;
			this.birthDate = null;
		}
		Offender offender = (Offender) association.getRelationship()
				.getFirstPerson();
		this.offenderId = offender.getId();
		PersonName offenderName = offender.getName();
		this.offenderLastName = offenderName.getLastName();
		this.offenderFirstName = offenderName.getFirstName();
		this.offenderMiddleName = offenderName.getMiddleName();
		this.offenderNumber = offender.getOffenderNumber();
		this.associationId = association.getId();
		this.registerDate = association.getRegisterDate();
		this.packetSent = association.getPacketSent();
		this.packetSendDate = association.getPacketSendDate();
		if (association.getFlags() != null) {
			this.victim = association.getFlags().getVictim();
			this.docRegistered = association.getFlags().getDocRegistered();
			this.business = association.getFlags().getBusiness();
			this.vineRegistered = association.getFlags().getVineRegistered();
		} else {
			this.victim = null;
			this.docRegistered = null;
			this.business = null;
			this.vineRegistered = null;
		}
		this.address = address != null;
		this.addressSummaryDelegate = new AddressSummaryDelegate(address);
		this.telephoneNumber = telephoneNumber != null;
		this.telephoneNumberSummaryDelegate
			= new TelephoneNumberSummaryDelegate(telephoneNumber);
		this.noteCount = noteCount;
		
		if (victimOffender != null) {
			this.victimOffender = true;
			this.victimOffenderNumber = victimOffender.getOffenderNumber();
		} else {
			this.victimOffender = false;
			this.victimOffenderNumber = null;
		}
	}
	
	/**
	 * Second constructor
	 */
	public VictimAssociationSummary(final String lastName, 
			final String firstName, final String middleName, 
			final String suffix, final Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.suffix = suffix;
		this.addressSummaryDelegate = new AddressSummaryDelegate(address);
		this.id = null;
		this.sex = null;
		this.birthDate = null;
		this.offenderId = null;
		this.offenderLastName = null;
		this.offenderFirstName = null;
		this.offenderMiddleName = null;
		this.offenderNumber = null;
		this.associationId = null;
		this.registerDate = null;
		this.packetSent = null;
		this.packetSendDate = null;
		this.victim = null;
		this.docRegistered = null;
		this.business = null;
		this.vineRegistered = null;
		this.telephoneNumber = null;
		this.telephoneNumberSummaryDelegate = null;
		this.noteCount = null;
		this.address = null;
		this.victimOffender = null;
		this.victimOffenderNumber = null;
	}

	/**
	 * Returns ID of victim.
	 * 
	 * @return ID of victim
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Returns last name of victim.
	 * 
	 * @return last name of victim
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Returns first name of victim.
	 * 
	 * @return first name of victim
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Returns middle name of victim.
	 * 
	 * @return middle name of victim
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * Returns suffix of victim.
	 *
	 * @return the suffix
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Returns sex of victim.
	 * 
	 * @return sex of victim
	 */
	public Sex getSex() {
		return this.sex;
	}

	/**
	 * Returns birth date of victim.
	 * 
	 * @return birth date of victim
	 */
	public Date getBirthDate() {
		return this.birthDate;
	}

	/**
	 * Returns ID of offender.
	 * 
	 * @return ID of offender
	 */
	public Long getOffenderId() {
		return this.offenderId;
	}

	/**
	 * Returns last name of offender.
	 * 
	 * @return last name of offender
	 */
	public String getOffenderLastName() {
		return this.offenderLastName;
	}

	/**
	 * Returns first name of offender.
	 * 
	 * @return first name of offender
	 */
	public String getOffenderFirstName() {
		return this.offenderFirstName;
	}

	/**
	 * Returns middle name of offender.
	 * 
	 * @return middle name of offender
	 */
	public String getOffenderMiddleName() {
		return this.offenderMiddleName;
	}

	/**
	 * Returns offender number.
	 * 
	 * @return offender number
	 */
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}

	/**
	 * Returns ID of association of victim to offender.
	 * 
	 * @return ID of association of victim to offender
	 */
	public Long getAssociationId() {
		return this.associationId;
	}

	/**
	 * Returns register date.
	 * 
	 * @return register date
	 */
	public Date getRegisterDate() {
		return this.registerDate;
	}
	
	/**
	 * Returns whether packet was sent.
	 * 
	 * @return whether packet was sent
	 */
	public Boolean getPacketSent() {
		return this.packetSent;
	}

	/**
	 * Returns date packet was sent.
	 * 
	 * @return date packet was sent
	 */
	public Date getPacketSentDate() {
		return this.packetSendDate;
	}

	/**
	 * Returns whether "victim".
	 * 
	 * @return whether "victim"
	 */
	public Boolean getVictim() {
		return this.victim;
	}

	/**
	 * Returns whether DOC registered.
	 * 
	 * @return whether DOC registered
	 */
	public Boolean getDocRegistered() {
		return this.docRegistered;
	}

	/**
	 * Returns whether "business".
	 * 
	 * @return whether "business"
	 */
	public Boolean getBusiness() {
		return this.business;
	}

	/**
	 * Returns whether VINE registered.
	 * 
	 * @return whether VINE registered
	 */
	public Boolean getVineRegistered() {
		return this.vineRegistered;
	}

	/**
	 * Returns whether telephone number is associated.
	 * 
	 * @return whether telephone number is associated
	 */
	public Boolean getTelephoneNumber() {
		return telephoneNumber;
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
	 * Returns whether address is associated.
	 * 
	 * @return whether address is associated
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
	
	/**
	 * Returns note count.
	 * 
	 * @return note count
	 */
	public Long getNoteCount() {
		return this.noteCount;
	}

	/**
	 * Returns victim as offender.
	 *
	 * @return the victimOffender victim offender
	 */
	public Boolean getVictimOffender() {
		return this.victimOffender;
	}

	/**
	 * Return victim as offender number.
	 *
	 * @return the victimOffenderNumber victim offender number
	 */
	public Integer getVictimOffenderNumber() {
		return this.victimOffenderNumber;
	}
}