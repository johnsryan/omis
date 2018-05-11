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

import java.beans.PropertyEditor;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import omis.locationterm.exception.LocationTermConflictException;
import omis.locationterm.exception.LocationTermExistsAfterException;
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
 * Tests administrative transfer to location such as prison, assessment center
 * or hospital from a community (probation, parole) placement.
 * 
 * <p>Administrative transfers are location changes - this test class targets
 * {@code LocationTermService#create()}.
 * 
 * <p>Placement - correctional status and supervisory organization - should
 * remain the same.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (May 2, 2018)
 * @since OMIS 3.0
 * @see AdministrativeTransferFromCommunityReturnScenarioTests
 * @see AdministrativeTransferFromFacilityScenarioTests
 * @see AdministrativeTransferFromFacilityReturnScenarioTests
 */
@Test(groups = {"location", "service", "scenario"})
public class AdministrativeTransferFromCommunityScenarioTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Property editors. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
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
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;	
	
	/* Unit tests. */
	
	/**
	 * Tests administrative transfer to hospital while placed.
	 * 
	 * <p>Asserts that location is changed by correctional status but
	 * placement (correctional status and supervisory organization) remain
	 * the same.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws OffenderNotUnderSupervisionException if offender is not placed
	 * @throws LocationTermExistsAfterException if location terms exist after
	 * @throws LocationTermConflictException if location terms exist before
	 */
	public void testWhilePlaced()
			throws DuplicateEntityFoundException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Arrangements - places Blofeld on parole
		Offender blofeld = this.createBlofeld();
		DateRange placementRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2017"));
		CorrectionalStatus parole = this.createParole();
		SupervisoryOrganization pnpOffice = this.createPnpOffice();
		CorrectionalStatusTerm paroleTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, placementRange, parole);
		SupervisoryOrganizationTerm pnpOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, placementRange, pnpOffice);
		this.placementTermDelegate.create(blofeld, placementRange,
				pnpOfficeTerm, paroleTerm, null, null, false);
		
		// Action
		Location hospitalLocation = this.createHospitalLocation();
		Date hospitalStartDate = this.parseDateText("12/12/2013");
		DateRange hospitalDateRange = new DateRange(
				hospitalStartDate, null);
		this.locationTermService.create(
				blofeld, hospitalLocation, hospitalDateRange);
		
		// Assertions
		CorrectionalStatus correctionalStatusOnHospitalStartDate
			= this.correctionalStatusTermDelegate
					.findForOffenderOnDate(blofeld, hospitalStartDate)
					.getCorrectionalStatus();
		assert correctionalStatusOnHospitalStartDate.equals(parole)
			: String.format("Wrong correctional status: %s expected; %s found",
					parole, correctionalStatusOnHospitalStartDate);
		SupervisoryOrganization supervisoryOrganizationOnStartDate
			= this.supervisoryOrganizationTermDelegate
				.findByOffenderOnEffectiveDate(blofeld, hospitalStartDate)
					.getSupervisoryOrganization();
		assert supervisoryOrganizationOnStartDate.equals(pnpOffice)
			: String.format(
					"Wrong supervisory organization: %s expected; %s found",
					pnpOffice, supervisoryOrganizationOnStartDate);
		Location locationOnStartDate = this.locationTermDelegate
				.findByOffenderOnDate(blofeld, hospitalStartDate)
					.getLocation();
		assert hospitalLocation.equals(hospitalLocation)
			: String.format("Wrong location: %s expected; %s found",
					hospitalLocation, locationOnStartDate);
	}
	
	/**
	 * Verifies that administrative transfers while not placed are prevented by
	 * a {@code OffenderNotUnderSupervisionException}.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities are found
	 * @throws LocationTermConflictException if conflicting location terms
	 * exist
	 * @throws LocationTermExistsAfterException if location terms exist after
	 * @throws OffenderNotUnderSupervisionException if offender is not placed
	 * - expected
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testWhileNotPlaced()
			throws DuplicateEntityFoundException,
				LocationTermConflictException,
				LocationTermExistsAfterException,
				OffenderNotUnderSupervisionException {
		
		// Action
		Offender blofeld = this.createBlofeld();
		Location hospitalLocation = this.createHospitalLocation();
		DateRange hospitalDateRange = new DateRange(
					this.parseDateText("12/12/2013"),
					null);
		this.locationTermService.create(
				blofeld, hospitalLocation, hospitalDateRange);
	}
	
	// Returns Blofeld
	private Offender createBlofeld() {
		return this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
	}
	
	// Returns parole status
	private CorrectionalStatus createParole()
			throws DuplicateEntityFoundException {
		return this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
	}
	
	// Returns P&P office
	private SupervisoryOrganization createPnpOffice()
			throws DuplicateEntityFoundException {
		return this.supervisoryOrganizationDelegate
				.create("P&P Office", "POFF", null);
	}
	
	// Returns hospital location
	private Location createHospitalLocation()
			throws DuplicateEntityFoundException {
		Organization hospital
			= this.organizationDelegate.create("Hospital", "HOSP", null);
		Country canada = this.countryDelegate
				.create("Canada", "CA", true);
		City calgary = this.cityDelegate.create("Calgary", true, null, canada);
		ZipCode zipCode = this.zipCodeDelegate
				.create(calgary, "T1X 0L3", null, true);
		Address address = this.addressDelegate
				.findOrCreate("100 1ST ST", null, null, null, zipCode);
		Location hospitalLocation = this.locationDelegate
				.create(hospital, null, address);
		return hospitalLocation;
	}
	
	// Parse date text
	private Date parseDateText(final String dateText) {
		PropertyEditor propertyEditor
			= this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		propertyEditor.setAsText(dateText);
		return (Date) propertyEditor.getValue();
	}
}