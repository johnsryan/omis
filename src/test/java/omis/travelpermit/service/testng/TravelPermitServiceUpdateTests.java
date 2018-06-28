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
package omis.travelpermit.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.travelpermit.domain.TravelMethod;
import omis.travelpermit.domain.TravelPermit;
import omis.travelpermit.domain.TravelPermitPeriodicity;
import omis.travelpermit.domain.component.OtherTravelers;
import omis.travelpermit.domain.component.TravelDestination;
import omis.travelpermit.domain.component.TravelPermitIssuance;
import omis.travelpermit.domain.component.TravelTransportation;
import omis.travelpermit.exception.TravelPermitExistsException;
import omis.travelpermit.exception.TravelPermitPeriodicityExistsException;
import omis.travelpermit.service.TravelPermitService;
import omis.travelpermit.service.delegate.TravelMethodDelegate;
import omis.travelpermit.service.delegate.TravelPermitDelegate;
import omis.travelpermit.service.delegate.TravelPermitPeriodicityDelegate;
import omis.user.service.delegate.UserAccountDelegate;

/**
 * Tests method to update travel permit.
 *
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jun 7, 2018)
 * @since OMIS 3.0
 */
@Test(groups = {"travelPermit", "service"})
public class TravelPermitServiceUpdateTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("travelPermitDelegate")
	private TravelPermitDelegate travelPermitDelegate;

	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("travelPermitPeriodicityDelegate")
	private TravelPermitPeriodicityDelegate travelPermitPeriodicityDelegate;
	
	@Autowired
	@Qualifier("travelMethodDelegate")
	private TravelMethodDelegate travelMethodDelegate;
	
	@Autowired
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate userAccountDelegate;

	/* Services. */

	@Autowired
	@Qualifier("travelPermitService")
	private TravelPermitService travelPermitService;

	/* Test methods. */

	@Test
	public void testUpdate() throws TravelPermitExistsException, 
		TravelPermitPeriodicityExistsException {
		// Arrangements		
		String purpose = "purpose";
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2018"), 
				this.parseDateText("12/01/2018"));
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"OLast", "OFirst", "OMiddle", null);
		TravelPermitPeriodicity periodicity 
			= this.travelPermitPeriodicityDelegate.create(
				"New travel periodicity name", (short) 0);;
		TravelPermitIssuance issuance = new TravelPermitIssuance(
				this.parseDateText("01/01/2018"),
				this.userAccountDelegate.findByUsername("AUDIT"));
		TravelMethod method = this.travelMethodDelegate.create(
				"Travel Method Name", true, "Description Name",
				"Number Name", (short) 0);
		TravelTransportation transportation = new TravelTransportation(
				"123-ABC", "Transportation description", method);
		TravelDestination destination = new TravelDestination(
				"Travel Destination Name", null, null, null, null, null);
		this.travelPermitDelegate.createTravelPermit(offender, periodicity, 
				issuance, transportation, destination, dateRange, purpose, 
				null);
		OtherTravelers otherTravelers = new OtherTravelers(
				"person", "relationships");		
		DateRange newDateRange = new DateRange(this.parseDateText("06/01/2018"), 
				this.parseDateText("06/30/2018"));
		TravelPermitPeriodicity newPeriodicity 
			= this.travelPermitPeriodicityDelegate.create(
				"Different travel periodicity name", (short) 0);		
		TravelPermit travelPermit = this.travelPermitDelegate
				.createTravelPermit(offender, newPeriodicity, issuance, 
						transportation, destination, newDateRange, purpose, 
						otherTravelers);
		TravelDestination newDestination = new TravelDestination(
				"New Travel Destination Name", null, null, null, null, null);
		
		// Action
		this.travelPermitService.update(
				travelPermit, purpose, newDateRange, offender, newPeriodicity, 
				issuance, transportation, newDestination, otherTravelers);
	}

	@Test(expectedExceptions = {TravelPermitExistsException.class})
	public void testTravelPermitExistsException() 
			throws TravelPermitExistsException, 
			TravelPermitPeriodicityExistsException {
		// Arrangements
		String purpose = "purpose";
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2018"), 
				this.parseDateText("12/01/2018"));
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"OLast", "OFirst", "OMiddle", null);
		TravelPermitPeriodicity periodicity 
			= this.travelPermitPeriodicityDelegate.create(
				"New travel periodicity name", (short) 0);
		TravelPermitIssuance issuance = new TravelPermitIssuance(
				this.parseDateText("01/01/2018"), 
				this.userAccountDelegate.findByUsername("AUDIT"));
		TravelMethod method = this.travelMethodDelegate.create(
				"Travel Method Name", true, "Description Name",
				"Number Name", (short) 0);
		TravelTransportation transportation = new TravelTransportation(
				"123-ABC", "Transportation description", method);
		TravelDestination destination = new TravelDestination(
				"Travel Destination Name", null, null, null, null, null);
		this.travelPermitDelegate.createTravelPermit(offender, periodicity, 
				issuance, transportation, destination, dateRange, purpose, 
				null);		
		OtherTravelers otherTravelers = new OtherTravelers(
				"person", "relationships");		
		DateRange newDateRange = new DateRange(this.parseDateText("06/01/2018"), 
				this.parseDateText("06/30/2018"));
		TravelPermitPeriodicity newPeriodicity 
			= this.travelPermitPeriodicityDelegate.create(
				"Different travel periodicity name", (short) 0);
		TravelDestination newDestination = new TravelDestination(
				"New Travel Destination Name", null, null, null, null, null);
		TravelPermit travelPermit = this.travelPermitDelegate
				.createTravelPermit(offender, newPeriodicity, issuance, 
						transportation, newDestination, newDateRange, purpose, 
						otherTravelers);
				
		// Action
		this.travelPermitService.update(travelPermit, purpose, dateRange, 
				offender, periodicity, issuance, transportation, 
				destination, otherTravelers);
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