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
package omis.locationterm.service.testng;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.exception.LocationTermLockedException;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.service.delegate.CityDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests return from administrative transfer to location such as prison,
 * assessment center or hospital from a community (probation, parole) placement.
 * 
 * <p>Administrative transfer returns are changes to existing location terms -
 * uses {@code LocationTermService#update()}.
 * 
 * <p>Placement - correctional status and supervisory organization - should
 * remain the same.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (May 2, 2018)
 * @since OMIS 3.0
 * @see AdministrativeTransferFromCommunityScenarioTests
 * @see AdministrativeTransferFromFacilityScenarioTests
 * @see AdministrativeTransferFromFacilityReturnScenarioTests
 */
@Test(groups = {"location", "service", "scenario"})
public class AdministrativeTransferFromCommunityReturnScenarioTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Property editors. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	/* Delegates. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate
	supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("locationTermDelegate")
	private LocationTermDelegate locationTermDelegate;
	
	/* Service. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Unit tests. */
	
	/**
	 * Tests return from administrative transfer to hospital while placed.
	 * 
	 * <p>Asserts that location is ended but placmenet (correctional status
	 * and supervisory organization) remain the same.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws OffenderNotUnderSupervisionException if offender is not placed 
	 * @throws LocationTermLockedException if location term is locked
	 * @throws LocationTermExistsAfterException if conflicting location terms
	 * exist after location term
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist 
	 */
	public void testWhilePlaced()
			throws DuplicateEntityFoundException,
				OffenderNotUnderSupervisionException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				LocationTermLockedException {
		
		// Arrangements - placed Blofeld, transfers administratively to hospital
		Offender blofeld = this.createBlofeld();
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2017"));
		CorrectionalStatus parole = this.createParole();
		SupervisoryOrganization paroleOffice
			= this.createParoleOffice();
		CorrectionalStatusTerm paroleTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, placementRange, parole);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, placementRange, paroleOffice);
		this.placementTermDelegate.create(
				blofeld, placementRange, paroleOfficeTerm, paroleTerm, null,
				null, false);
		Location hospitalLocation = this.createHospitalLocation();
		Date hospitalStartDate = this.parseDateText("12/12/2013");
		LocationTerm hospitalTerm = this.locationTermDelegate
				.create(blofeld, hospitalLocation, hospitalStartDate,
						null, false);
		
		// Action - returns Blofeld from hospital
		Date hospitalEndDate = this.parseDateText("12/12/2014");
		hospitalTerm = this.locationTermService
				.update(hospitalTerm, hospitalLocation, new DateRange(
						hospitalStartDate, hospitalEndDate));
		
		// Assertions
		CorrectionalStatus correctionalStatusOnHospitalEndDate
			= this.correctionalStatusTermDelegate
				.findForOffenderOnDate(blofeld, hospitalEndDate)
					.getCorrectionalStatus();
		assert correctionalStatusOnHospitalEndDate.equals(parole)
			: String.format("Wrong correctional status: %s expected; %s found",
					parole, correctionalStatusOnHospitalEndDate);
		SupervisoryOrganization supervisoryOrganizationOnEndDate
			= this.supervisoryOrganizationTermDelegate
				.findByOffenderOnEffectiveDate(blofeld, hospitalEndDate)
					.getSupervisoryOrganization();
		assert supervisoryOrganizationOnEndDate.equals(paroleOffice)
			: String.format(
					"Wrong supervisory organization: %s expected; %s found",
					paroleOffice, supervisoryOrganizationOnEndDate);
		LocationTerm locationTermOnEndDate
			= this.locationTermDelegate
				.findByOffenderOnDate(blofeld, hospitalEndDate);
		assert locationTermOnEndDate == null
			: String.format("Wrong location: no location expected; %s found",
					locationTermOnEndDate.getLocation());
	}
	
	/**
	 * Verifies that attempts to return an offender from an administrative
	 * transfer is prevented by a {@code OffenderNotUnderSupervisionException}
	 * while the offender is not placed.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist
	 * @throws LocationTermExistsAfterException if location term exists
	 * after
	 * @throws LocationTermLockedException if location term is locked
	 * @throws OffenderNotUnderSupervisionException if offender is not
	 * placed - asserted
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testWhileNotPlaced()
			throws DuplicateEntityFoundException,
					LocationTermExistsException,
					LocationTermConflictException,
					LocationTermExistsAfterException,
					LocationTermLockedException,
					OffenderNotUnderSupervisionException {
		
		// Arrangements - locates Blofeld at hospital
		Offender blofeld = this.createBlofeld();
		Location hospitalLocation = this.createHospitalLocation();
		Date startDate = this.parseDateText("12/12/2013");
		LocationTerm locationTerm = this.locationTermDelegate
				.create(blofeld, hospitalLocation, startDate, null, false);
		
		// Action - attempts to end location term
		Date endDate = this.parseDateText("12/12/2018");
		this.locationTermService.update(locationTerm, hospitalLocation,
				new DateRange(startDate, endDate));
	}
	
	/* Helper methods. */

	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customDateEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customDateEditor.setAsText(dateText);
		return (Date) customDateEditor.getValue();
	}
	
	// Returns Blofeld
	private Offender createBlofeld() {
		return this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
	}
	
	// Returns Parole status
	private CorrectionalStatus createParole()
			throws DuplicateEntityFoundException {
		return this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
	}
	
	// Returns Parole office
	private SupervisoryOrganization createParoleOffice()
			throws DuplicateEntityFoundException {
		return this.supervisoryOrganizationDelegate
				.create("Parole Office", "PAROFF", null);
	}
	
	// Returns hospital location
	private Location createHospitalLocation()
			throws DuplicateEntityFoundException {
		Organization hospital = this.organizationDelegate
				.create("Hospital", "HOS", null);
		Country mexico = this.countryDelegate
				.create("Mexico", "MEX", true);
		City mexicoCity = this.cityDelegate
				.create("Mexico City", true, null, mexico);
		ZipCode mexicoCity00810 = this.zipCodeDelegate
				.create(mexicoCity, "00810", null, true);
		Address address = this.addressDelegate
				.findOrCreate("100 100 ST", null, null, null, mexicoCity00810);
		return this.locationDelegate.create(hospital, null, address);
	}
}