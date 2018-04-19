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
package omis.boardhearingdocument.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.boardhearing.domain.BoardHearing;
import omis.boardhearing.domain.BoardHearingAssociableDocument;
import omis.boardhearing.service.BoardHearingDocumentService;
import omis.boardhearing.service.delegate.BoardHearingAssociableDocumentDelegate;
import omis.boardhearing.service.delegate.BoardHearingDelegate;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.document.domain.Document;
import omis.document.service.delegate.DocumentDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.organization.domain.Organization;
import omis.organization.service.delegate.OrganizationDelegate;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.service.delegate.ParoleBoardItineraryDelegate;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardlocation.service.delegate.ParoleBoardLocationDelegate;
import omis.paroleeligibility.domain.ParoleEligibility;
import omis.paroleeligibility.service.delegate.ParoleEligibilityDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests the method to update board hearing associable documents.
 * 
 * @author Trevor Isles
 * @author Josh Divine
 * @version 0.1.3 (Apr 18, 2018)
 * @since OMIS 3.0
 */
@Test
public class BoardHearingAssociableDocumentUpdateTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("paroleEligibilityDelegate")
	private ParoleEligibilityDelegate paroleEligibilityDelegate;
	
	@Autowired
	private LocationDelegate locationDelegate;
	
	@Autowired
	@Qualifier("boardHearingDelegate")
	private BoardHearingDelegate boardHearingDelegate;
	
	@Autowired
	@Qualifier("documentDelegate")
	private DocumentDelegate documentDelegate;
	
	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	private CountryDelegate countryDelegate;
	
	@Autowired
	private StateDelegate stateDelegate;
	
	@Autowired
	private CityDelegate cityDelegate;
	
	@Autowired
	private ZipCodeDelegate zipCodeDelegate;
	
	@Autowired
	private ParoleBoardItineraryDelegate paroleBoardItineraryDelegate;
	
	@Autowired
	private ParoleBoardLocationDelegate paroleBoardLocationDelegate;
	
	@Autowired
	@Qualifier("organizationDelegate")
	private OrganizationDelegate organizationDelegate;
	
	@Autowired
	@Qualifier("boardHearingAssociableDocumentDelegate")
	private BoardHearingAssociableDocumentDelegate 
		boardHearingAssociableDocumentDelegate; 

	/* Services. */

	@Autowired
	@Qualifier("boardHearingDocumentService")
	private BoardHearingDocumentService boardHearingDocumentService;

	/* Test methods. */

	/**
	 * Test the update of board hearing associable documents.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateBoardHearingAssociableDocument() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		BoardHearing boardHearing = this.boardHearingDelegate.create(itinerary, 
				null, paroleEligibility, null, null, false);
		Document document = this.documentDelegate.create(
				this.parseDateText("01/01/2018"), "filename", "fileextension", 
				"title");
		
		BoardHearingAssociableDocument boardHearingAssociableDocument 
				= this.boardHearingAssociableDocumentDelegate
				.create(boardHearing, document);

		// Action
		boardHearingAssociableDocument = this.boardHearingDocumentService
				.updateBoardHearingAssociableDocument(
					boardHearingAssociableDocument, boardHearing, document);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("boardHearing", boardHearing)
			.addExpectedValue("document", document)
			.performAssertions(boardHearingAssociableDocument);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Jay", null);
		Country country = this.countryDelegate.create(
				"Country", "USA", true);
		State state = this.stateDelegate.create(
				"State", "ST", country, true, true);
		City city = this.cityDelegate.create(
				"City", true, state, country);
		ZipCode zipCode = this.zipCodeDelegate.create(
				city, "12345", null, true);
		Address address = this.addressDelegate.findOrCreate("123", "321",
				null, null, zipCode);
		Organization organization = this.organizationDelegate.create(
				"Organization", "org", null);
		Location location = this.locationDelegate.create(organization,
				new DateRange(this.parseDateText("01/01/2001"),
						this.parseDateText("01/01/2020")), address);
		ParoleBoardLocation paroleBoardLocation =
				this.paroleBoardLocationDelegate.create(location, true);
		ParoleBoardItinerary itinerary =
				this.paroleBoardItineraryDelegate.create(paroleBoardLocation,
						true, this.parseDateText("01/01/2015"), 
						this.parseDateText("01/01/2015"));
		ParoleEligibility paroleEligibility = this.paroleEligibilityDelegate
				.create(offender, this.parseDateText("01/01/2018"), null, null,
						null);
		BoardHearing boardHearing = this.boardHearingDelegate.create(itinerary, 
				null, paroleEligibility, null, null, false);
		Document document = this.documentDelegate.create(
				this.parseDateText("01/01/2018"), "filename", "fileextension", 
				"title");
		this.boardHearingAssociableDocumentDelegate.create(boardHearing, 
				document);
		Document secondDocument = this.documentDelegate.create(
				this.parseDateText("01/01/2018"), "filename2", "fileextension", 
				"title2");
		BoardHearingAssociableDocument boardHearingAssociableDocument = this
				.boardHearingAssociableDocumentDelegate.create(boardHearing, 
						secondDocument);
		
		// Action
		this.boardHearingDocumentService.updateBoardHearingAssociableDocument(
				boardHearingAssociableDocument, boardHearing, document);
	}

	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}

}
