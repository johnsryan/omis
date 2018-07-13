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
import omis.address.exception.ZipCodeExistsException;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.exception.CountryExistsException;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.location.domain.Location;
import omis.location.exception.LocationExistsException;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.domain.LocationTermChangeAction;
import omis.locationterm.exception.LocationTermChangeActionAssociationExistsException;
import omis.locationterm.exception.LocationTermChangeActionExistsException;
import omis.locationterm.exception.LocationTermExistsException;
import omis.locationterm.service.LocationTermService;
import omis.locationterm.service.delegate.LocationTermChangeActionAssociationDelegate;
import omis.locationterm.service.delegate.LocationTermChangeActionDelegate;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.exception.OrganizationExistsException;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.exception.CityExistsException;
import omis.region.exception.StateExistsException;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;

/**
 * Tests for method to association change action with location term of
 * service for location terms.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Jul 9, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"locationTerm", "service"})
public class LocationTermServiceAssociateChangeActionTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Service delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("locationTermDelegate")
	private LocationTermDelegate locationTermDelegate;
	
	@Autowired
	@Qualifier("locationTermChangeActionDelegate")
	private LocationTermChangeActionDelegate locationTermChangeActionDelegate;
	
	@Autowired
	@Qualifier("locationTermChangeActionAssociationDelegate")
	private LocationTermChangeActionAssociationDelegate
	locationTermChangeActionAssociationDelegate;
	
	/* Services. */
	
	@Autowired
	@Qualifier("locationTermService")
	private LocationTermService locationTermService;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;

	/* Test methods. */
	
	/**
	 * Tests association of change action to location term.
	 * 
	 * @throws OrganizationExistsException if organization exists
	 * @throws CityExistsException if city exists
	 * @throws ZipCodeExistsException if ZIP code exists
	 * @throws LocationExistsException if location exists
	 * @throws LocationTermExistsException if location term exists
	 * @throws LocationTermChangeActionExistsException if location term
	 * change action exists
	 * @throws LocationTermChangeActionAssociationExistsException if location
	 * term change action association exists
	 * @throws CountryExistsException if country exists
	 * @throws StateExistsException if State exists
	 */
	public void testAssociation()
			throws OrganizationExistsException,
				CityExistsException,
				ZipCodeExistsException,
				LocationExistsException,
				LocationTermExistsException,
				LocationTermChangeActionExistsException,
				LocationTermChangeActionAssociationExistsException,
				CountryExistsException,
				StateExistsException {
		
		// Arrangements - locates offender in hospital
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Grant", "Donald", null, null);
		Organization hospitalOrganization = this.organizationDelegate
				.create("Hospital", "HOSP", null);
		Country homeCountry = this.countryDelegate
				.create("United States", "US", true);
		State homeState = this.stateDelegate
				.create("Montana", "MT", homeCountry, true, true);
		City helena = this.cityDelegate.create("Helena", true, homeState,
				homeState.getCountry());
		ZipCode mt59601 = this.zipCodeDelegate
				.create(helena, "59601", null, true);
		Address address = this.addressDelegate
				.findOrCreate("1000 1ST ST #1000", null, null, null, mt59601);
		Location hospital = this.locationDelegate.create(hospitalOrganization,
				new DateRange(this.parseDateText("12/12/2012"), null), address);
		LocationTerm locationTerm = this.locationTermDelegate
				.create(offender, hospital, this.parseDateText("12/12/2012"),
						null, false);
		
		// Action - associates change action
		LocationTermChangeAction gotoHospital
			= this.locationTermChangeActionDelegate.create("Goto Hospital");
		this.locationTermService.associateChangeAction(
				locationTerm, gotoHospital);
		
		// Asserts that change action is associated with location term
		assert this.locationTermChangeActionAssociationDelegate
			.findByLocationTerm(locationTerm).getChangeAction()
				.equals(gotoHospital)
			: "Wrong change action";
	}
	
	// Returns parsed text as date
	private Date parseDateText(final String dateText) {
		PropertyEditor propertyEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		propertyEditor.setAsText(dateText);
		return (Date) propertyEditor.getValue();
	}
}