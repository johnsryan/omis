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
package omis.travelpermit.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Travel permit summary.
 *
 * @author Sheronda Vaughn
 * @version 0.1.0 (May 21, 2018)
 * @since OMIS 3.0
 */
public class TravelPermitSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long id;
	private final String offenderLastName;
	private final String offenderFirstName;
	private final String offenderMiddleName;
	private final String offenderSuffix;
	private final Integer offenderNumber;
	private final Date startDate;
	private final Date endDate;
	private final String purpose;
	private final String otherTravelerPersons;
	private final String otherTravelerRelationships;
	private final String destinationName;
	private final Long destinationTelephoneNumber;
	private final String destinationAddressValue;
	private final String destinationCityName;
	private final String destinationStateName;
	private final String destinationStateAbbreviation;
	private final String destinationCountryName;
	private final String destinationCountryAbbreviation;
	private final String destinationZipCodeValue;
	private final String destinationZipCodeExtension;
	private final String periodicityName;
	private final Date issueDate;
	private final Long issuerId;
	private final String issuerLastName;
	private final String issuerFirstName;
	private final String issuerMiddleName;
	private final String issuerSuffix;
	private final String issuerUsername;
	private final String transportationNumber;
	private final String transportationDescription;
	private final String transportationMethodName;
	private final Long createdById;
	private final String createdByUsername;
	private final String createdByLastName;
	private final String createdByFirstName;
	private final String createdByMiddleName;
	private final String createdBySuffix;

	/**
	 * Instantiates an implementation of Travel Permit Summary.
	 */
	public TravelPermitSummary(final Long id, final String offenderLastName,
			final String offenderFirstName, final String offenderMiddleName, 
			final String offenderSuffix, final Integer offenderNumber,
			final Date startDate, final Date endDate, final String purpose,
			final String otherTravelerPersons, 
			final String otherTravelerRelationships, 
			final String destinationName,
			final Long destinationTelephoneNumber, 
			final String destinationAddressValue, 
			final String destinationCityName, final String destinationStateName,
			final String destinationStateAbbreviation, 
			final String destinationCountryName, 
			final String destinationCountryAbbreviation, 
			final String destinationZipCodeValue, 
			final String destinationZipCodeExtension, 
			final String periodicityName, final Date issueDate, 
			final Long issuerId, final String issuerLastName, 
			final String issuerFirstName, final String issuerMiddleName, 
			final String issuerSuffix, final String issuerUsername, 
			final String transportationNumber, 
			final String transportationDescription, 
			final String transportationMethodName, 
			final Long createdById, final String createdByUsername,
			final String createdByLastName, final String createdByFirstName,
			final String createdByMiddleName, final String createdBySuffix) {
		
		this.id = id;
		this.offenderLastName = offenderLastName;
		this.offenderFirstName = offenderFirstName;
		this.offenderMiddleName = offenderMiddleName;
		this.offenderSuffix = offenderSuffix;
		this.offenderNumber = offenderNumber;
		this.startDate = startDate;
		this.endDate = endDate;
		this.purpose = purpose;
		this.otherTravelerPersons = otherTravelerPersons;
		this.otherTravelerRelationships = otherTravelerRelationships;
		this.destinationName = destinationName;
		this.destinationTelephoneNumber = destinationTelephoneNumber;
		this.destinationAddressValue = destinationAddressValue;
		this.destinationCityName = destinationCityName;
		this.destinationStateName = destinationStateName;
		this.destinationStateAbbreviation = destinationStateAbbreviation;
		this.destinationCountryName = destinationCountryName;
		this.destinationCountryAbbreviation = destinationCountryAbbreviation;
		this.destinationZipCodeValue = destinationZipCodeValue;
		this.destinationZipCodeExtension = destinationZipCodeExtension;
		this.periodicityName = periodicityName;
		this.issueDate = issueDate;
		this.issuerId = issuerId;
		this.issuerLastName = issuerLastName;
		this.issuerFirstName = issuerFirstName;
		this.issuerMiddleName = issuerMiddleName;
		this.issuerSuffix = issuerSuffix;
		this.issuerUsername = issuerUsername;
		this.transportationNumber = transportationNumber;
		this.transportationDescription = transportationDescription;
		this.transportationMethodName = transportationMethodName;
		this.createdById = createdById;
		this.createdByUsername = createdByUsername;
		this.createdByLastName = createdByLastName;
		this.createdByFirstName = createdByFirstName;
		this.createdByMiddleName = createdByMiddleName;
		this.createdBySuffix = createdBySuffix;
	}
	
	/**
	 * Returns the travel permit ID.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Returns the offender last name.
	 *
	 * @return the offenderLastName
	 */
	public String getOffenderLastName() {
		return this.offenderLastName;
	}

	/**
	 * Returns the offender first name.	 
	 *
	 * @return the offenderFirstName
	 */
	public String getOffenderFirstName() {
		return this.offenderFirstName;
	}

	/**
	 * Returns the offender middle name.
	 *
	 * @return the offenderMiddleName
	 */
	public String getOffenderMiddleName() {
		return this.offenderMiddleName;
	}

	/**
	 * Returns the offender suffix.
	 *
	 * @return the offenderSuffix
	 */
	public String getOffenderSuffix() {
		return this.offenderSuffix;
	}

	/**
	 * Returns the offender number.
	 *
	 * @return the offenderNumber
	 */
	public Integer getOffenderNumber() {
		return this.offenderNumber;
	}

	/**
	 * Returns the start date.
	 *
	 * @return the startDate
	 */
	public Date getStartDate() {
		return this.startDate;
	}

	/**
	 * Returns the end date.
	 *
	 * @return the endDate
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * Returns the purpose.
	 *
	 * @return the purpose
	 */
	public String getPurpose() {
		return this.purpose;
	}

	/**
	 * Returns the other traveler persons.
	 *
	 * @return the otherTravelerPersons
	 */
	public String getOtherTravelerPersons() {
		return this.otherTravelerPersons;
	}

	/**
	 * Returns the other traveler relationships.
	 *
	 * @return the otherTravelerRelationships
	 */
	public String getOtherTravelerRelationships() {
		return this.otherTravelerRelationships;
	}

	/**
	 * Returns the destination name.
	 *
	 * @return the destinationName
	 */
	public String getDestinationName() {
		return this.destinationName;
	}

	/**
	 * Returns the destination telephone number.
	 *
	 * @return the destinationTelephoneNumber
	 */
	public Long getDestinationTelephoneNumber() {
		return this.destinationTelephoneNumber;
	}

	/**
	 * Returns the destination address value.
	 *
	 * @return the destinationAddressValue
	 */
	public String getDestinationAddressValue() {
		return this.destinationAddressValue;
	}

	/**
	 * Returns the destination city name.
	 *
	 * @return the destinationCityName
	 */
	public String getDestinationCityName() {
		return this.destinationCityName;
	}

	/**
	 * Returns the destination state name.
	 *
	 * @return the destinationStateName
	 */
	public String getDestinationStateName() {
		return this.destinationStateName;
	}

	/**
	 * Returns the destination state abbreviation.
	 *
	 * @return the destinationStateAbbreviation
	 */
	public String getDestinationStateAbbreviation() {
		return this.destinationStateAbbreviation;
	}

	/**
	 * Return the destination country name.
	 *
	 * @return the destinationCountryName
	 */
	public String getDestinationCountryName() {
		return this.destinationCountryName;
	}

	/**
	 * Returns the destination country abbreviation.
	 *
	 * @return the destinationCountryAbbreviation
	 */
	public String getDestinationCountryAbbreviation() {
		return this.destinationCountryAbbreviation;
	}

	/**
	 * Returns the destination zip code value.
	 *
	 * @return the destinationZipCodeValue
	 */
	public String getDestinationZipCodeValue() {
		return this.destinationZipCodeValue;
	}

	/**
	 * Returns destination zip code extension.
	 *
	 * @return the destinationZipCodeExtension
	 */
	public String getDestinationZipCodeExtension() {
		return this.destinationZipCodeExtension;
	}

	/**
	 * Returns the periodicity name.
	 *
	 * @return the periodicityName
	 */
	public String getPeriodicityName() {
		return this.periodicityName;
	}

	/**
	 * Returns the issue date.
	 *
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return this.issueDate;
	}

	/**
	 * Return the issuer ID.
	 *
	 * @return the issuerId
	 */
	public Long getIssuerId() {
		return this.issuerId;
	}

	/**
	 * Returns issuer last name.
	 *
	 * @return the issuerLastName
	 */
	public String getIssuerLastName() {
		return this.issuerLastName;
	}

	/**
	 * Returns the issuer first name.
	 *
	 * @return the issuerFirstName
	 */
	public String getIssuerFirstName() {
		return this.issuerFirstName;
	}

	/**
	 * Returns the issuer middle name.
	 *
	 * @return the issuerMiddleName
	 */
	public String getIssuerMiddleName() {
		return this.issuerMiddleName;
	}

	/**
	 * Returns the issuer suffix.
	 *
	 * @return the issuerSuffix
	 */
	public String getIssuerSuffix() {
		return this.issuerSuffix;
	}

	/**
	 * Returns the issuer user name.
	 *
	 * @return the issuerUsername
	 */
	public String getIssuerUsername() {
		return this.issuerUsername;
	}

	/**
	 * Returns the transportation number.
	 *
	 * @return the transportationNumber
	 */
	public String getTransportationNumber() {
		return this.transportationNumber;
	}

	/**
	 * Returns the transportation description.
	 *
	 * @return the transportationDescription
	 */
	public String getTransportationDescription() {
		return this.transportationDescription;
	}

	/**
	 * Returns the transportation method name.
	 *
	 * @return the transportationMethodName
	 */
	public String getTransportationMethodName() {
		return this.transportationMethodName;
	}

	/**
	 * Returns the created by ID.
	 *
	 * @return the createdById
	 */
	public Long getcreatedById() {
		return this.createdById;
	}

	/**
	 * Returns the created by username.
	 *
	 * @return the createdByUsername
	 */
	public String getCreatedByUsername() {
		return this.createdByUsername;
	}

	/**
	 * Returns the created by last name.
	 *
	 * @return the createdByLastName
	 */
	public String getCreatedByLastName() {
		return this.createdByLastName;
	}

	/**
	 * Returns the created by first name.
	 *
	 * @return the createdByFirstName
	 */
	public String getCreatedByFirstName() {
		return this.createdByFirstName;
	}

	/**
	 * Returns the created by middle name.
	 *
	 * @return the createdByMiddleName
	 */
	public String getCreatedByMiddleName() {
		return this.createdByMiddleName;
	}

	/**
	 * Returns the created by suffix.
	 *
	 * @return the createdBySuffix
	 */
	public String getCreatedBySuffix() {
		return this.createdBySuffix;
	}	
}