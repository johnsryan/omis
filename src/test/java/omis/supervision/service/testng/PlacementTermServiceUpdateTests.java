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
package omis.supervision.service.testng;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.testng.annotations.Test;

import omis.address.domain.Address;
import omis.address.domain.BuildingCategory;
import omis.address.domain.ZipCode;
import omis.address.service.delegate.AddressDelegate;
import omis.address.service.delegate.AddressUnitDesignatorDelegate;
import omis.address.service.delegate.StreetSuffixDelegate;
import omis.address.service.delegate.ZipCodeDelegate;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.country.domain.Country;
import omis.country.service.delegate.CountryDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.location.service.delegate.LocationDelegate;
import omis.locationterm.domain.LocationTerm;
import omis.locationterm.service.delegate.LocationTermDelegate;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.region.domain.City;
import omis.region.domain.State;
import omis.region.service.delegate.CityDelegate;
import omis.region.service.delegate.StateDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementStatus;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermLockedException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.service.PlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.DateManipulator;
import omis.util.PropertyValueAsserter;

/**
 * Tests placement term service.
 *
 * @author Josh Divine 
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"supervision", "service"})
public class PlacementTermServiceUpdateTests 
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationDelegate")
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("placementTermChangeReasonDelegate")
	private PlacementTermChangeReasonDelegate placementTermChangeReasonDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	@Autowired
	@Qualifier("locationTermDelegate")
	private LocationTermDelegate locationTermDelegate;
	
	@Autowired
	@Qualifier("locationDelegate")
	private LocationDelegate locationDelegate;
	

	@Autowired
	@Qualifier("addressDelegate")
	private AddressDelegate addressDelegate;
	
	@Autowired
	@Qualifier("streetSuffixDelegate")
	private StreetSuffixDelegate streetSuffixDelegate;
	
	@Autowired
	@Qualifier("addressUnitDesignatorDelegate")
	private AddressUnitDesignatorDelegate addressUnitDesignatorDelegate;
	
	@Autowired
	@Qualifier("countryDelegate")
	private CountryDelegate countryDelegate;
	
	@Autowired
	@Qualifier("cityDelegate")
	private CityDelegate cityDelegate;
	
	@Autowired
	@Qualifier("stateDelegate")
	private StateDelegate stateDelegate;
	
	@Autowired
	@Qualifier("zipCodeDelegate")
	private ZipCodeDelegate zipCodeDelegate;
	
	
	/* Services. */
	
	@Autowired
	@Qualifier("placementTermService")
	private PlacementTermService placementTermService;
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/**
	 * Tests for overlapping correctional status terms
	 * 
	 * @throws SupervisoryOrganizationTermConflictException 
	 * @throws CorrectionalStatusTermConflictException 
	 * @throws DuplicateEntityFoundException 
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	@Test(expectedExceptions = {CorrectionalStatusTermConflictException.class})
	public void testCorrectionalStatusTermConflictException() throws 
		DuplicateEntityFoundException, CorrectionalStatusTermConflictException, 
		SupervisoryOrganizationTermConflictException,
		PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("12/03/2013");
		Date endDate = this.parseDateText("12/04/2014");
		DateRange dateRange = new DateRange(startDate, endDate);

		this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
	
		Date secondStartDate = this.parseDateText("12/02/2012");
		Date secondEndDate = this.parseDateText("12/03/2013");
		DateRange secondDateRange = new DateRange(
				secondStartDate, secondEndDate);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, 
					secondDateRange, correctionalStatus);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
					secondDateRange, supervisoryOrganization);
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, secondDateRange, 
					supervisoryOrganizationTerm, correctionalStatusTerm, 
					changeReason, null, false);
		
		PlacementStatus updatedStatus = PlacementStatus.PLACED;
		DateRange updatedStatusDate = null;
		
		// Attempt to set end date of correctional status term of placement
		// term to this should cause conflict exception
		Date conflictingEndDate = this.parseDateText("06/04/2014");
		
		// Action
		this.placementTermService.update(placementTerm,
				conflictingEndDate, updatedStatus,
				updatedStatusDate, changeReason, null);
	}
	
	/**
	 * Tests that {@code CorrectionalStatusTermConflictException} is thrown when
	 * a placement term's end date is set to {@code null} using
	 * {@code PlacementTermServce#update(...)}, causing it to conflict with a
	 * future correctional status term.
	 * 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists (asserted)
	 * @throws DuplicateEntityFoundException if duplicate entities are found
	 * @throws PlacementTermLockedException  if placement term is locked when
	 * update is attempted
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists
	 */
	@Test(expectedExceptions = {CorrectionalStatusTermConflictException.class})
	public void
	testCorrectionalStatusTermConflictExceptionWithNullConflictingEndDate()
			throws CorrectionalStatusTermConflictException,
				DuplicateEntityFoundException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermLockedException {
		
		// Arrangements - secure Blofeld in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRI", null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 2, true);
		DateRange secureDateRange = new DateRange(
				this.parseDateText("12/02/2012"),
				this.parseDateText("12/03/2013"));
		SupervisoryOrganizationTerm prisonOrganizationTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, secureDateRange, prison);
		CorrectionalStatusTerm secureStatusTerm
			= this.correctionalStatusTermDelegate.create(
					blofeld, secureDateRange, secure);
		PlacementTerm placementTerm = this.placementTermDelegate
				.create(blofeld, secureDateRange,
						prisonOrganizationTerm, secureStatusTerm,
						null, null, false);
		
		// More arrangements - parole Blofeld
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		this.correctionalStatusTermDelegate.create(
					blofeld,
					new DateRange(
							this.parseDateText("12/03/2013"),
							this.parseDateText("12/04/2014")),
					parole);
		
		// Action - updates secure placement term with null end date
		// This should conflict with parole status term and be caught in
		// service layer check
		this.placementTermService.update(placementTerm, null,
				PlacementStatus.PLACED, null, null, null);
	}
	
	/**
	 * Tests that {@code CorrectionalStatusTermConflictException} is thrown when
	 * a placement term's end date is set to a date using
	 * {@code PlacementTermServce#update()}, causing it to conflict with a
	 * future correctional status term that has a {@code null} end date.
	 * 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists (asserted)
	 * @throws DuplicateEntityFoundException if duplicate entities are found
	 * @throws PlacementTermLockedException  if placement term is locked when
	 * update is attempted
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists
	 */
	@Test(expectedExceptions = {CorrectionalStatusTermConflictException.class})
	public void
	testCorrectionalStatusTermConflictExceptionNullOriginalEndDate()
			throws DuplicateEntityFoundException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermLockedException {
		
		// Arrangements - place Largo at a pre-release
		Offender largo = this.offenderDelegate
				.createWithoutIdentity("Largo", "Emilio", null, null);
		DateRange preReleaseDateRange = new DateRange(
				this.parseDateText("12/02/2012"),
				this.parseDateText("12/03/2013"));
		SupervisoryOrganization preRelease
			= this.supervisoryOrganizationDelegate.create(
					"Pre-release", "PRC", null);
		SupervisoryOrganizationTerm preReleaseOrganizationTerm
			= this.supervisoryOrganizationTermDelegate
				.create(largo, preReleaseDateRange, preRelease);
		CorrectionalStatus altSecure = this.correctionalStatusDelegate
				.create("Alt-secure", "ALT", true, (short) 1, false);
		CorrectionalStatusTerm altStatusTerm
			= this.correctionalStatusTermDelegate
				.create(largo, preReleaseDateRange, altSecure);
		PlacementTerm placementTerm = this.placementTermDelegate
				.create(largo, preReleaseDateRange, preReleaseOrganizationTerm,
						altStatusTerm, null, null, false);
		
		// More arrangements - parole Largo
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		this.correctionalStatusTermDelegate.create(largo,
				new DateRange(this.parseDateText("12/03/2013"), null), parole);
		
		// Action - updates pre-release placement term with end date that's
		// after parole start date
		// This should conflict with parole status term and be caught in
		//service layer check
		this.placementTermService.update(placementTerm,
				this.parseDateText("06/04/2014"), PlacementStatus.PLACED,
				null, null, null);
	}
	
	/**
	 * Tests for overlapping supervisory organization terms
	 * 
	 * @throws DuplicateEntityFoundException
	 * @throws CorrectionalStatusTermConflictException
	 * @throws SupervisoryOrganizationTermConflictException
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	@Test(expectedExceptions = 
		{SupervisoryOrganizationTermConflictException.class})
	public void testSupervisoryOrganizationTermConflictException() 
			throws DuplicateEntityFoundException, 
			CorrectionalStatusTermConflictException, 
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("12/02/2012");
		Date endDate = this.parseDateText("12/03/2013");
		DateRange dateRange = new DateRange(startDate, endDate);
		this.supervisoryOrganizationTermDelegate.create(offender, dateRange, 
				supervisoryOrganization);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		Date secondStartDate = this.parseDateText("12/01/2011");
		Date secondEndDate = this.parseDateText("12/02/2012");
		DateRange secondDateRange
			= new DateRange(secondStartDate, secondEndDate);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, 
					secondDateRange, correctionalStatus);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
					secondDateRange, supervisoryOrganization);
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, secondDateRange, 
					supervisoryOrganizationTerm, correctionalStatusTerm, 
					changeReason, null, false);
		
		PlacementStatus updatedStatus = PlacementStatus.PLACED;
		DateRange updatedStatusDate = null;
		
		Date conflictingEndDate = this.parseDateText("06/06/2013");
		
		// Action
		this.placementTermService.update(placementTerm, 
				conflictingEndDate, updatedStatus, updatedStatusDate,
				changeReason, null);
	}
	
	/**
	 * Tests that {@code SupervisoryOrganizationTermConflictException} is thrown
	 * when a placement term's end date is set to {@code null} using
	 * {@code PlacementTermService#update(...)}, causing it to conflict with a
	 * future supervisory organization term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities are found
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists (asserted)
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	@Test(expectedExceptions
			= {SupervisoryOrganizationTermConflictException.class})
	public void
	testSupervisoryOrganizationTermConflictExceptionWithNullConflictingEndDate()
			throws DuplicateEntityFoundException,
			CorrectionalStatusTermConflictException,
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		
		// Arrangements - secure Blofeld in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange secureDateRange = new DateRange(
				this.parseDateText("12/02/2012"),
				this.parseDateText("12/03/2013"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonOrganizationTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, secureDateRange, prison);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureStatusTerm
			= this.correctionalStatusTermDelegate.create(
					blofeld, secureDateRange, secure);
		PlacementTerm placementTerm = this.placementTermDelegate
				.create(blofeld, secureDateRange, prisonOrganizationTerm,
						secureStatusTerm, null, null, false);
		
		// More arrangements - supervise Blofeld at P&P office after secure
		// placement
		SupervisoryOrganization pnpOffice
			= this.supervisoryOrganizationDelegate
				.create("P&P Office", "PNP", null);
		this.supervisoryOrganizationTermDelegate.create(blofeld,
				new DateRange(
						this.parseDateText("12/03/2013"),
						this.parseDateText("12/04/2014")), pnpOffice);
		
		// Action - updates secure placement term with null end date
		// This should conflict with P&P supervisory organization term and be
		// caught in service layer check
		this.placementTermService.update(
				placementTerm, null, PlacementStatus.PLACED, null, null, null);
	}
	
	/**
	 * Tests that {@code SupervisoryOrganizationTermConflictException} is thrown
	 * when a placement term's end date is set to a date using
	 * {@code PlacementTermService#update(...)}, causing it to conflict with a
	 * future correctional status that has a {@code null} end date.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist (asserted)
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	@Test(expectedExceptions = {
			SupervisoryOrganizationTermConflictException.class})
	public void
	testSupervisoryOrganizationTermConflictExceptionWithNullOriginalEndDate()
			throws DuplicateEntityFoundException,
			CorrectionalStatusTermConflictException,
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		
		// Arrangements - place Dr No at a treatment center
		Offender drNo = this.offenderDelegate
				.createWithoutIdentity("No", "Julius", null, null);
		DateRange treatmentCenterDateRange = new DateRange(
				this.parseDateText("12/02/2012"),
				this.parseDateText("12/03/2013"));
		SupervisoryOrganization treatmentCenter
			= this.supervisoryOrganizationDelegate
				.create("Treatment Center", "TC", null);
		SupervisoryOrganizationTerm treatmentCenterStay
			= this.supervisoryOrganizationTermDelegate
				.create(drNo, treatmentCenterDateRange, treatmentCenter);
		CorrectionalStatus altSecure
			= this.correctionalStatusDelegate.create(
					"Alt-Secure", "ALT", true, (short) 1, true);
		CorrectionalStatusTerm treatmentCenterStatusTerm
			= this.correctionalStatusTermDelegate
				.create(drNo, treatmentCenterDateRange, altSecure);
		PlacementTerm placementTerm = this.placementTermDelegate
				.create(drNo, treatmentCenterDateRange, treatmentCenterStay,
						treatmentCenterStatusTerm, null, null, false);
		
		// More arrangements - parole Dr No
		SupervisoryOrganization pnpOffice = this.supervisoryOrganizationDelegate
				.create("P&P Office", "PNP", null);
		this.supervisoryOrganizationTermDelegate.create(drNo,
				new DateRange(
						this.parseDateText("12/03/2013"), null),
				pnpOffice);
		
		// Action - updates treatment center placement term with end date that
		// after parole start date
		// This should conflict with parole status term and be caugh in
		// service layer check
		this.placementTermService.update(placementTerm,
				this.parseDateText("06/04/2014"), PlacementStatus.PLACED,
				null, null, null);
	}
	
	/**
	 * Tests supervisory organization updates correctly
	 * 
	 * @throws DuplicateEntityFoundException
	 * @throws CorrectionalStatusTermConflictException
	 * @throws SupervisoryOrganizationTermConflictException
	 * @throws PlacementTermConflictException
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	public void testUpdateSupervisoryOrganization() 
			throws DuplicateEntityFoundException, 
			CorrectionalStatusTermConflictException, 
			SupervisoryOrganizationTermConflictException, 
			PlacementTermConflictException,
			PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
		
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, dateRange,
				supervisoryOrganizationTerm, correctionalStatusTerm,  
				changeReason, null, false);

		PlacementStatus status = PlacementStatus.PLACED;
		DateRange statusDate = null;
		
		// Action
		placementTerm = this.placementTermService.update(placementTerm, 
				DateRange.getEndDate(dateRange), status, statusDate,
				changeReason, null);
		
		assertValues(placementTerm, supervisoryOrganization, 
				correctionalStatus, dateRange, changeReason, null);
	}
	
	/**
	 * Tests end date updates correctly
	 * 
	 * @throws DuplicateEntityFoundException
	 * @throws CorrectionalStatusTermConflictException
	 * @throws SupervisoryOrganizationTermConflictException
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	public void testUpdateDateRange() 
			throws DuplicateEntityFoundException, 
			CorrectionalStatusTermConflictException, 
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
		
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, dateRange, 
					supervisoryOrganizationTerm, correctionalStatusTerm, 
					changeReason, null, false);
		
		Date newEndDate = this.parseDateText("12/01/2012");
		DateRange newDateRange = new DateRange(
				DateRange.getStartDate(dateRange), newEndDate);
		
		PlacementStatus status = PlacementStatus.PLACED;
		DateRange statusDate = null;
		
		// Action
		placementTerm = this.placementTermService.update(placementTerm, 
				newEndDate, status, statusDate,	changeReason, null);
		
		assertValues(placementTerm, supervisoryOrganization, 
				correctionalStatus, newDateRange, changeReason, null);
	}
	
	/**
	 * Tests start change reason updates correctly
	 * 
	 * @throws DuplicateEntityFoundException
	 * @throws CorrectionalStatusTermConflictException
	 * @throws SupervisoryOrganizationTermConflictException
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	public void testUpdateStartChangeReason() 
			throws DuplicateEntityFoundException, 
			CorrectionalStatusTermConflictException, 
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
		
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, dateRange, 
					supervisoryOrganizationTerm, correctionalStatusTerm,
					changeReason, null, false);
		
		PlacementTermChangeReason newChangeReason = this.createChangeReason(
				"New Change Reason", (short) 2, true, true);
		
		PlacementStatus status = PlacementStatus.PLACED;
		DateRange statusDate = null;
		
		// Action
		placementTerm = this.placementTermService.update(placementTerm, 
				DateRange.getEndDate(dateRange), status, statusDate,
				newChangeReason, null);
		
		assertValues(placementTerm, supervisoryOrganization, 
				correctionalStatus, dateRange, newChangeReason, null);
	}
	
	/**
	 * Tests end change reason updates correctly
	 * 
	 * @throws DuplicateEntityFoundException
	 * @throws CorrectionalStatusTermConflictException
	 * @throws SupervisoryOrganizationTermConflictException
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	public void testUpdateEndChangeReason() 
			throws DuplicateEntityFoundException, 
			CorrectionalStatusTermConflictException, 
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
		
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, dateRange, 
					supervisoryOrganizationTerm, correctionalStatusTerm, 
					changeReason, null, false);
		
		PlacementTermChangeReason newChangeReason = this.createChangeReason(
				"New Change Reason", (short) 2, true, true);

		PlacementStatus status = PlacementStatus.PLACED;
		DateRange statusDate = null;
		
		// Action
		placementTerm = this.placementTermService.update(placementTerm, 
				DateRange.getEndDate(dateRange), status, statusDate,
				changeReason, newChangeReason);
		
		assertValues(placementTerm, supervisoryOrganization, 
				correctionalStatus, dateRange, changeReason, 
				newChangeReason);
	}
	
	/**
	 * Tests that when a placement status is set to escaped that the underlying 
	 * location terms end date is set to the escapee status start date. 
	 * 
	 * @throws DuplicateEntityFoundException
	 * @throws CorrectionalStatusTermConflictException
	 * @throws SupervisoryOrganizationTermConflictException
	 * @throws PlacementTermLockedException if placement term is locked
	 */
	@Test
	public void testUpdateEscapedStatus() 
			throws DuplicateEntityFoundException, 
			CorrectionalStatusTermConflictException, 
			SupervisoryOrganizationTermConflictException,
			PlacementTermLockedException {
		// Arrangements
		Offender offender = this.createOffender();
		
		SupervisoryOrganization supervisoryOrganization 
			= this.createSupervisoryOrganization("SuperOrg", "SO");
		
		CorrectionalStatus correctionalStatus = this.createCorrectionalStatus(
				"SECURE", "SEC", true, (short) 1);
		
		Date startDate = this.parseDateText("09/12/2001");
		Date endDate = this.parseDateText("12/03/2012");
		DateRange dateRange = new DateRange(startDate, endDate);
		
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
				dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		PlacementTermChangeReason changeReason = this.createChangeReason(
				"Sentence Imposed, Initial Status", (short) 1, true, true);
		
		PlacementTerm placementTerm 
			= this.placementTermDelegate.create(offender, dateRange, 
					supervisoryOrganizationTerm, correctionalStatusTerm, 
					changeReason, null, false);
		
		PlacementStatus status = PlacementStatus.ESCAPED;
		Date escapeStartDate = this.parseDateText("03/03/2010");
		DateRange statusDate = new DateRange(escapeStartDate, null);
		
		Country unitedStates = this.countryDelegate.create("United States", 
				"US", true);
		State mt = this.stateDelegate.create("Montana", "MT", unitedStates, 
				true, true);
		City helena = this.cityDelegate.create("Helena", true, mt, 
				unitedStates);
		ZipCode mt59602 = this.zipCodeDelegate.create(helena, "59602", null, 
				true);
		Address address = this.addressDelegate.findOrCreate("1000", null, null, 
				BuildingCategory.APARTMENT,  mt59602);
		Location location = this.locationDelegate.create(
				supervisoryOrganization, null, address);
		this.locationTermDelegate.create(offender, location, startDate, endDate, 
				false);
		
		// Action
		placementTerm = this.placementTermService.update(placementTerm, 
				DateRange.getEndDate(dateRange), status, statusDate,
				changeReason, null);
		
		// Assert
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("supervisoryOrganizationTerm", 
					supervisoryOrganizationTerm)
			.addExpectedValue("correctionalStatusTerm", correctionalStatusTerm)
			.addExpectedValue("locked", false)
			.addExpectedValue("startChangeReason", changeReason)
			.addExpectedValue("endChangeReason", null)
			.performAssertions(placementTerm);
		// The update method updates the underlying location terms end date, 
		// the following ensures that actually happened. The date manipulator is 
		// called to ensure we find the location term that was active prior to 
		// the escape date
		DateManipulator searchDate = new DateManipulator(escapeStartDate);
		searchDate.changeDate(-1);
		LocationTerm locationTerm = this.locationTermDelegate
				.findByOffenderOnDate(offender, searchDate.getDate());
		assert locationTerm != null : "Location term was not found";
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.endDate", escapeStartDate)
			.performAssertions(locationTerm);
	}
	
	/**
	 * Test that updates to locked placement terms are prevented
	 * 
	 * @throws PlacementTermLockedException if placement term is locked
	 * (assertion)
	 * @throws DuplicateEntityFoundException if placement term exists
	 * @throws SupervisoryOrganizationTermConflictException if supervisory
	 * organization terms conflict
	 * @throws CorrectionalStatusTermConflictException if correctional status
	 * terms conflict 
	 */
	@Test(expectedExceptions = {PlacementTermLockedException.class})
	public void testPlacementTermLockedException()
			throws PlacementTermLockedException,
					DuplicateEntityFoundException,
					CorrectionalStatusTermConflictException,
					SupervisoryOrganizationTermConflictException {
		
		// Arrangements
		Offender offender = this.createOffender();
		SupervisoryOrganization paroleOffice
			= this.createSupervisoryOrganization(
					"City Parole Office", "CiParO");
		CorrectionalStatus paroleStatus
			= this.createCorrectionalStatus(
					"Parole", "PAR", false, (short) 1);
		PlacementTermChangeReason startOfSupervisionStatus
			= this.createChangeReason("Start of Supervision", (short) 1,
					true, false);
		DateRange dateRange = new DateRange(
				this.parseDateText("05/23/2011"), null);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
					dateRange, paroleOffice);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
					paroleStatus);
		PlacementTerm placementTerm = this.placementTermDelegate
				.create(offender, dateRange, supervisoryOrganizationTerm,
						correctionalStatusTerm, startOfSupervisionStatus, null,
						true);
		
		// Action
		this.placementTermService.update(placementTerm,
				this.parseDateText("07/23/2013"),
				placementTerm.getStatus(),
				placementTerm.getStatusDateRange(),
				placementTerm.getStartChangeReason(),
				placementTerm.getEndChangeReason());
	}
	
	/* Helper methods. */
	
	private void assertValues(PlacementTerm placementTerm, 
			SupervisoryOrganization supervisoryOrganization,
			CorrectionalStatus correctionalStatus, DateRange dateRange, 
			PlacementTermChangeReason startChangeReason, 
			PlacementTermChangeReason endChangeReason)
	{
		assert supervisoryOrganization.equals(placementTerm
			.getSupervisoryOrganizationTerm().getSupervisoryOrganization()) 
			: String.format("Wrong supervisory organization: %s found; %s "
				+ "expected", placementTerm.getSupervisoryOrganizationTerm()
				.getSupervisoryOrganization().getName(), 
				supervisoryOrganization.getName());
		assert correctionalStatus.equals(placementTerm
			.getCorrectionalStatusTerm().getCorrectionalStatus()) : String
			.format("Wrong correctional status: %s found; %s expected", 
				placementTerm.getCorrectionalStatusTerm()
				.getCorrectionalStatus().getName(), 
				correctionalStatus.getName());
		assert dateRange.equals(placementTerm.getDateRange()) : String.format(
			"Wrong date range: %s Found; %s expected", placementTerm
			.getDateRange().toString(), dateRange.toString());
		assert startChangeReason.equals(placementTerm.getStartChangeReason()) :
			String.format("Wrong start change reason: %s found; %s expected", 
				placementTerm.getStartChangeReason().getName(), 
				startChangeReason.getName());
		if (endChangeReason == null || placementTerm.getEndChangeReason() 
				== null) {
			assert endChangeReason == null && 
				placementTerm.getEndChangeReason() == null : String.format(
					"Mismatch end change reason nulls: %s found; %s expected", 
					placementTerm.getEndChangeReason() == null ? "null" : 
					"not null", endChangeReason == null ? "null" : "not null");
		} else {
			assert endChangeReason.equals(placementTerm.getEndChangeReason()) :
				String.format("Wrong end change reason: %s found; %s expected", 
					placementTerm.getEndChangeReason().getName(), 
					endChangeReason.getName());	
		}
	}
	
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
	
	// Creates an offender
	private Offender createOffender() {
		return this.offenderDelegate.createWithoutIdentity("Blofeld", "Ernst", 
				null, null);
	}
	
	// Creates a supervisory organization
	private SupervisoryOrganization createSupervisoryOrganization(
			final String name, final String alias) 
			throws DuplicateEntityFoundException {
		return this.supervisoryOrganizationDelegate.create(name, alias, null);
	}
	
	// Creates a correctional status
	private CorrectionalStatus createCorrectionalStatus(final String name, 
			final String abbreviation, final boolean locationRequired, 
			final short sortOrder) 
			throws DuplicateEntityFoundException {
		return this.correctionalStatusDelegate.create(name, abbreviation, 
				locationRequired, sortOrder, true);
	}
	
	// Creates a placement term change reason
	private PlacementTermChangeReason createChangeReason(final String name, 
			final short sortOrder, final boolean validStartReason, 
			final boolean validEndReason) 
			throws DuplicateEntityFoundException {
		return this.placementTermChangeReasonDelegate.create(name, sortOrder, 
				validStartReason, validEndReason);
	}
}
