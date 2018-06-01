/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.travelpermit.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.web.form.AddressFields;
import omis.country.domain.Country;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.user.domain.UserAccount;


/** Form object for travel permit.
 * @author: Yidong Li
 * @version 0.1.1 (May 21, 2018)
 * @since OMIS 3.0 */
public class TravelPermitForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private TravelPermitPeriodicity periodicity;
	private Date issueDate;
	private UserAccount issuer;
	private String issuerInput;
	private Date startDate;
	private Date endDate;
	private String name;
	private Long phoneNumber;
	private String destinationOption;
	private String addressOption;
	private AddressFields addressFields;
	private Address address;
	private String addressQuery;
	private Country partialAddressCountry;
	private State partialAddressState;
	private City partialAddressCity;
	private ZipCode partialAddressZipCode;
	private String tripPurpose;
	private String transportMethod;
	private String vehicleInfo;
	private String plateNumber;
	private String flightNumber;
	private String busNumber;
	private String trainNumber;
	private String person;
	private String relation;
	private List<TravelPermitNoteItem> travelPermitNoteItems 
	= new ArrayList<TravelPermitNoteItem>();
	private Boolean newCity;
	private Boolean newZipCode;
	private String newCityName;
	private String newZipCodeName;
	private TravelMethod travelMethod;
	
	/** Instantiates a travel permit form. */
	public TravelPermitForm() {
		// Default instantiation
	}
	
	public TravelPermitPeriodicity getPeriodicity() {
		return this.periodicity;
	}
	public void setIssueDate(final Date issueDate) {
		this.issueDate = issueDate;
	}
	 
	public Date getIssueDate() {
		return this.issueDate;
	}
	public void setIssuer(final UserAccount issuer) {
		this.issuer = issuer;
	}
	public UserAccount getIssuer() {
		return this.issuer;
	}
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
	public Date getStartDate() {
		return this.startDate;
	}
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}
	public Date getEndDate() {
		return this.endDate;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setPhoneNumber(final Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getphoneNumber() {
		return this.phoneNumber;
	}
	public void setDestinationOption(final String destinationOption) {
		this.destinationOption = destinationOption;
	}
	public String getDestinationOption() {
		return this.destinationOption;
	}
	public void setAddressOption(final String addressOption) {
		this.addressOption = addressOption;
	}
	public String getAddressOption() {
		return this.addressOption;
	}
	public void setAddressFields(final AddressFields addressFields) {
		this.addressFields = addressFields;
	}
	public AddressFields getAddressFields() {
		return this.addressFields;
	}
	public void setAddress(final Address address) {
		this.address = address;
	}
	public Address getAddress() {
		return this.address;
	}
	public void setAddressQuery(final String addressQuery) {
		this.addressQuery = addressQuery;
	}
	public String getAddressQuery() {
		return this.addressQuery;
	}
	public void setPartialAddressState(final State partialAddressState) {
		this.partialAddressState = partialAddressState;
	}
	public State getPartialAddressState() {
		return this.partialAddressState;
	}
	public void setPartialAddressCity(final City partialAddress) {
		this.partialAddressCity = partialAddress;
	}
	public City getPartialAddressCity() {
		return this.partialAddressCity;
	}
	public void setPartialAddressZipCode(final ZipCode partialAddressZipCode) {
		this.partialAddressZipCode = partialAddressZipCode;
	}
	public ZipCode getPartialAddressZipCode() {
		return this.partialAddressZipCode;
	}
	public void setTripPurpose(final String tripPurpose) {
		this.tripPurpose = tripPurpose;
	}
	public String getTripPurpose() {
		return this.tripPurpose;
	}
	public void setTransportMethod(final String transportMethod) {
		this.transportMethod = transportMethod;
	}
	public String getTransportMethod() {
		return this.transportMethod;
	}
	public void setVehicleInfo(final String vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}
	public String getVehicleInfo() {
		return this.vehicleInfo;
	}
	public void setPlateNumber(final String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getPlateNumber() {
		return this.plateNumber;
	}
	public void setFlightNumber(final String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getFlightNumber() {
		return this.flightNumber;
	}
	public void setBusNumber(final String busNumber) {
		this.busNumber = busNumber;
	}
	public String getBusNumber() {
		return this.busNumber;
	}
	public void setTrainNumber(final String trainNumber) {
		this.trainNumber = trainNumber;
	}
	public String getTrainNumber() {
		return this.trainNumber;
	}
	public void setAccompaniedBy(final String person) {
		this.person = person;
	}
	public String getPerson() {
		return this.person;
	}
	public void setRelation(final String relation) {
		this.relation = relation;
	}
	public String getRelation() {
		return this.relation;
	}
	public void setTravelPermitNoteItems(
		final List<TravelPermitNoteItem> travelPermitNoteItems) {
		this.travelPermitNoteItems = travelPermitNoteItems;
	}
	public List<TravelPermitNoteItem> getTravelPermitNoteItems() {
		return this.travelPermitNoteItems;
	}
	public void setNewCity(final Boolean newCity) {
		this.newCity = newCity;
	}
	public Boolean getNewCity() {
		return this.newCity;
	}
	public void setNewZipCode(final Boolean newZipCode) {
		this.newZipCode = newZipCode;
	}
	public Boolean getNewZipCode() {
		return this.newZipCode;
	}
	public void setNewCityName(final String newCityName) {
		this.newCityName = newCityName;
	}
	public String getNewCityName() {
		return this.newCityName;
	}
	public void setNewZipCodeName(final String newZipCodeName) {
		this.newZipCodeName = newZipCodeName;
	}
	public String getNewZipCodeName() {
		return this.newZipCodeName;
	}	
	public void setPartialAddressCountry(final Country partialAddressCountry) {
		this.partialAddressCountry = partialAddressCountry;
	}
	public Country getPartialAddressCountry() {
		return this.partialAddressCountry;
	}
	public void setIssuerInput(final String issuerInput) {
		this.issuerInput = issuerInput;
	}
	public String getIssuerInput() {
		return this.issuerInput;
	}
	public void setTravelMethod(final TravelMethod travelMethod) {
		this.travelMethod = travelMethod;
	}
	public TravelMethod getTravelMethod() {
		return this.travelMethod;
	}
}