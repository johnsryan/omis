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

import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.service.PlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create placement terms.
 *
 * <p>Verifies documented side effects of invoking method to create placement
 * terms.
 * 
 * <p>See {@link PlacementTermService#create}.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"placement", "service"})
public class PlacementTermServiceCreateTests
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Property editor factory. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	/* Service delegates. */
	
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
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate
	supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	/* Service to test. */
	
	@Autowired
	@Qualifier("placementTermService")
	private PlacementTermService placementTermService;
	
	/* Test methods. */
	
	/**
	 * Tests creation of placement term.
	 * 
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws DuplicateEntityFoundException if other entities exist
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if supervisory
	 * organization term conflicts
	 * @throws CorrectionalStatusTermConflictException if correctional status
	 * term conflicts 
	 */
	public void testCreation()
			throws PlacementTermExistsException,
				DuplicateEntityFoundException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - creates Blofeld, parole status and office and reasons
		Offender offender = this.offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", null, null);
		SupervisoryOrganization supervisoryOrganization 
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "PAROFF", null);
		CorrectionalStatus correctionalStatus
			= this.correctionalStatusDelegate.create(
					"Parole", "PAR", false, (short) 1, true);
		DateRange dateRange = new DateRange(
				this.parseDateText("03/15/2012"),
				this.parseDateText("03/21/2017"));
		PlacementTermChangeReason startChangeReason
			= this.placementTermChangeReasonDelegate
				.create("Start of Supervision", (short) 1, true, false);
		PlacementTermChangeReason endChangeReason
			= this.placementTermChangeReasonDelegate
				.create("End of Supervision", (short) 2, true, false);
		
		// Action - places Blofeld in on parole at a parole office
		PlacementTerm placementTerm = this.placementTermService
				.create(offender, supervisoryOrganization, correctionalStatus,
						dateRange, startChangeReason, endChangeReason);
		
		// Assertions - verifies that Blofeld is placed
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("startChangeReason", startChangeReason)
			.addExpectedValue("endChangeReason", endChangeReason)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					correctionalStatus)
			.addExpectedValue("supervisoryOrganizationTerm"
						+ ".supervisoryOrganization",
					supervisoryOrganization)
			.performAssertions(placementTerm);
	}
	
	/**
	 * Tests creation without supervisory organization.
	 * 
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists
	 * @throws PlacementTermConflictException if conflicting placement term
	 * exists
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 */
	public void testCreationWithoutSupervisoryOrganization()
			throws PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException,
				DuplicateEntityFoundException {
		
		// Arrangements - creates Blofeld and correctional status
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		
		// Action - places Blofeld on secure status without a supervisory
		// organization
		PlacementTerm placementTerm = this.placementTermService
				.create(offender, null, secure, dateRange, null, null);
		
		// Asserts that Blofeld is secure without a supervisory organization
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					secure)
			.addExpectedValue("supervisoryOrganizationTerm", null)
			.performAssertions(placementTerm);
	}
	
	/**
	 * Tests that an existing placement term without an end date is ended with
	 * the start date of the new placement term.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if conflicting placement terms
	 * exist
	 */
	public void testEndExistingPlacementTermWithoutEndDateOnCreation()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld securely, at prison
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate.create(
					offender, dateRange, secure);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, dateRange, prison);
		PlacementTerm existingPlacementTerm
			= this.placementTermDelegate.create(
				offender, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - places offender on alt-secure status
		CorrectionalStatus altSecure = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 1, true);
		SupervisoryOrganization preRelease
			= this.supervisoryOrganizationDelegate
				.create("PreRelease", "PRE", null);
		DateRange altDateRange = new DateRange(
				this.parseDateText("12/12/2014"), null);
		this.placementTermService
			.create(offender, preRelease, altSecure, altDateRange, null, null);
		
		// Asserts that original secure term is unaffected
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", dateRange.getStartDate())
			.addExpectedValue("dateRange.endDate", altDateRange.getStartDate())
			.addExpectedValue("offender", offender)
			.addExpectedValue(
					"supervisoryOrganizationTerm.supervisoryOrganization",
					prison)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					secure)
			.performAssertions(existingPlacementTerm);
	}
	
	/**
	 * Tests that an existing placement term with an end date has its end date
	 * adjusted to that start date of the new placement term when the end date
	 * is after the start date.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if conflicting placement terms
	 * exist
	 */
	public void testEndExistingPlacementTermWithEndDateOnCreation()
				throws DuplicateEntityFoundException,
					PlacementTermExistsException,
					CorrectionalStatusTermConflictException,
					SupervisoryOrganizationTermConflictException,
					PlacementTermConflictException {
		
		// Arrangements - places Blofeld securely, at prison
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"),
				this.parseDateText("12/12/2015"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate.create(
					offender, dateRange, secure);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, dateRange, prison);
		PlacementTerm existingPlacementTerm
			= this.placementTermDelegate.create(
				offender, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - places offender on alt-secure status
		CorrectionalStatus altSecure = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 1, true);
		SupervisoryOrganization preRelease
			= this.supervisoryOrganizationDelegate
				.create("PreRelease", "PRE", null);
		DateRange altDateRange = new DateRange(
				this.parseDateText("12/12/2014"), null);
		this.placementTermService
			.create(offender, preRelease, altSecure, altDateRange, null, null);
		
		// Asserts that original secure term is unaffected
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate", dateRange.getStartDate())
			.addExpectedValue("dateRange.endDate", altDateRange.getStartDate())
			.addExpectedValue("offender", offender)
			.addExpectedValue(
					"supervisoryOrganizationTerm.supervisoryOrganization",
					prison)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					secure)
			.performAssertions(existingPlacementTerm);
	}
	
	/**
	 * Tests that when a correctional status term with a matching correctional
	 * status and date range exists, it is used for the placement term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	public void testMatchingCorrectionalStatusTermIsUsed()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - puts Blofeld on parole status but don't place him
		// Creates parole office
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("05/21/2012"),
				this.parseDateText("05/21/2017"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		CorrectionalStatusTerm paroleTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, dateRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "POFF", null);
		
		// Action - place Blofeld on parole at the parole office
		PlacementTerm placementTerm = this.placementTermService
				.create(blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that matching correctional status term was used
		assert placementTerm.getCorrectionalStatusTerm().equals(paroleTerm)
			: "Matching correctional status term not used";
	}
	
	/**
	 * Tests that when a supervisory organization term with a matching
	 * supervisory organization and date range exists, its used for the
	 * placement term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	public void testMatchingSupervisoryOrganizationTermIsUsed()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld at parole office but does not put him
		// on parole status
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/31/2008"),
				this.parseDateText("12/31/2018"));
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "POFF", null);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, dateRange, paroleOffice);
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		
		// Action - places offender on parole at parole office
		PlacementTerm placementTerm = this.placementTermService
				.create(blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that matching supervisory organization term was used
		assert placementTerm.getSupervisoryOrganizationTerm()
			.equals(paroleOfficeTerm)
			: "Matching supervisory organization term not used";
	}
	
	/**
	 * Tests that overlapping supervisory organization term is left unchanged
	 * when supervisory organization is not specified.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist 
	 * @throws PlacementTermConflictException if conflicting placement terms
	 * exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	public void
	testCreationWithoutSupervisoryOrganizationLeavesExistingUnchanged()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld securely, at prison
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate.create(
					offender, dateRange, secure);
		SupervisoryOrganization prison
			= this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(offender, dateRange, prison);
		this.placementTermDelegate.create(
				offender, dateRange, prisonTerm, secureTerm, null, null, false);
		
		// Action - places offender on alt-secure status
		CorrectionalStatus altSecure = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 1, true);
		DateRange altDateRange = new DateRange(
				this.parseDateText("12/12/2014"), null);
		this.placementTermService
			.create(offender, null, altSecure, altDateRange, null, null);
		
		// Asserts that original secure term is unaffected
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("offender", offender)
			.addExpectedValue("supervisoryOrganization", prison)
			.performAssertions(prisonTerm);
	}
	
	/**
	 * Tests that attempts to create duplicate placement terms are prevented
	 * when supervisory organization is {@code null}.
	 *  
	 * @throws PlacementTermExistsException if placement term exists - asserted
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional statuses exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organizations exist
	 * @throws PlacementTermConflictException if conflicting placement
	 * terms exist
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 */
	// Disabled as current implementation ends existing placement term of
	// which new one is a duplicate. This should be resolved by dropping the
	// correctional status and supervisory organization terms from the business
	// key of placement term and performing the check before existing placement
	// terms are adjusted.
	@Test(enabled = false,
			expectedExceptions = {PlacementTermExistsException.class})
	public void
	testPlacementTermExistsExceptionWithoutSupervisoryOrganization()
			throws PlacementTermExistsException,
			CorrectionalStatusTermConflictException,
			SupervisoryOrganizationTermConflictException,
			PlacementTermConflictException,
			DuplicateEntityFoundException {
	
		// Arrangements - creates Blofeld, places securely
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange dateRange = new DateRange(
				this.parseDateText("12/12/2012"), null);
		CorrectionalStatus secureStatus
			= this.correctionalStatusDelegate.create(
					"Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(offender, dateRange, secureStatus);
		this.placementTermDelegate.create(offender, dateRange, null,
				secureTerm, null, null, false);
		
		// Action - attempts to create duplicate placement term
		this.placementTermService
			.create(offender, null, secureStatus, dateRange, null, null);
	}
	
	/**
	 * Tests that an existing correctional status term with a matching
	 * correctional status occuring earlier but finishing during a placement
	 * term is lengthened and used for placement.
	 * 
	 * <p>Start date must remain the same.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	public void testEarlierMatchingCorrectionalStatusTermIsLengthened()
				throws DuplicateEntityFoundException,
					PlacementTermExistsException,
					CorrectionalStatusTermConflictException,
					SupervisoryOrganizationTermConflictException,
					PlacementTermConflictException {
		
		// Arrangements - places Blofled on parole correctional status but
		// not at parole office
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange statusRange = new DateRange(
				this.parseDateText("12/21/2001"),
				this.parseDateText("12/21/2011"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		CorrectionalStatusTerm paroleTerm = this.correctionalStatusTermDelegate
				.create(blofeld, statusRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "POFF", null);
		
		// Action - places Blofeld at parole office
		DateRange dateRange = new DateRange(
				this.parseDateText("12/31/2005"),
				this.parseDateText("12/31/2015"));
		this.placementTermService.create(
				blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that start date is unaffected but end date is lengthened
		// of correctional status term
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(statusRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(dateRange))
			.performAssertions(paroleTerm);
	}
	
	/**
	 * Tests that an existing correctional status term with a matching
	 * correctional status lasting later but starting during a placement
	 * term is lengthened and used for placement.
	 * 
	 * <p>End date must remain the same.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist 
	 * @throws PlacementTermConflictException if conflicting placement terms
	 * exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	public void testLaterMatchingCorretionalStatusTermIsLengthened()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld on correctional status but not at
		// parole office
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange statusRange = new DateRange(
				this.parseDateText("12/21/2001"),
				this.parseDateText("12/21/2011"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		CorrectionalStatusTerm paroleTerm = this.correctionalStatusTermDelegate
				.create(blofeld, statusRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "POFF", null);
		
		// Action - places Blofeld at parole office
		DateRange dateRange = new DateRange(
				this.parseDateText("12/21/1996"),
				this.parseDateText("12/21/2006"));
		this.placementTermService.create(
				blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that start date is lengthened of correctional status term
		// and that end date is unaffected
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(dateRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(statusRange))
			.performAssertions(paroleTerm);
	}
	
	/**
	 * Tests that an existing supervisory organization term with a matching
	 * supervisory organization occuring earlier but finishing during a
	 * placement term is lengthened and used for placement.
	 * 
	 * <p>Start date must remain the same.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists 
	 * @throws PlacementTermExistsException if placement term exists
	 */
	public void testEarlierMatchingSupervisoryOrganizationTermIsLengthened()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld at a parole office but not on parole
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange paroleOfficeRange = new DateRange(
				this.parseDateText("12/21/2001"),
				this.parseDateText("12/21/2011"));
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "POFF", null);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, paroleOfficeRange, paroleOffice);
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		
		// Action - places offender on parole
		DateRange dateRange = new DateRange(
				this.parseDateText("12/31/2005"),
				this.parseDateText("12/31/2015"));
		this.placementTermService.create(
				blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that start date is unaffected but end date is lengthened
		// of supervisory organization term
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(paroleOfficeRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(dateRange))
			.performAssertions(paroleOfficeTerm);
	}
	
	/**
	 * Tests that an existing supervisory organization term with a matching
	 * supervisory organization lasting later but starting during a placement
	 * term is lengthened and used for placement.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if conflicting placement
	 * terms exist
	 */
	public void testLaterMatchingSupervisoryOrganizationTermIsLengthened()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld at parole office but not on
		// correctional status
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange officeRange = new DateRange(
				this.parseDateText("12/21/2001"),
				this.parseDateText("12/21/2011"));
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "PAR", null);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate.create(
					blofeld, officeRange, paroleOffice);
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		
		// Action - places Blofeld
		DateRange dateRange = new DateRange(
				this.parseDateText("12/21/1996"),
				this.parseDateText("12/21/2006"));
		this.placementTermService.create(
				blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that start date is lengthened of supervisory organization
		// term and end date is unaffected
		PropertyValueAsserter.create()
			.addExpectedValue("dateRange.startDate",
					DateRange.getStartDate(dateRange))
			.addExpectedValue("dateRange.endDate",
					DateRange.getEndDate(officeRange))
			.performAssertions(paroleOfficeTerm);
	}
	
	/**
	 * Tests that an earlier correctional status term ending
	 * during the placement term but with a mismatching correctional status
	 * is ended and a new correctional status term began for use by the
	 * placement term.
	 *  
	 * @throws DuplicateEntityFoundException if entities exist 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists 
	 * @throws PlacementTermExistsException if placement term exists
	 */
	public void testEarlierMismatchingCorrectionalStatusTermIsEnded()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - secures Blofeld
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange secureRange = new DateRange(
				this.parseDateText("06/30/2005"),
				this.parseDateText("06/30/2015"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, secureRange, secure);
		
		// Action - places Blofeld on parole during secure term
		DateRange dateRange = new DateRange(
				this.parseDateText("06/30/2010"),
				this.parseDateText("06/30/2020"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "PAOFF", null);
		PlacementTerm placementTerm = this.placementTermService.create(
				offender, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that a different correctional status term to secure is
		// used
		assert !placementTerm.getCorrectionalStatusTerm().equals(secureTerm)
			: "Term with mismatching correctional status term is used";

		// Asserts that original secure term was ended on start date of
		// placement term
		assert DateRange.getEndDate(secureTerm.getDateRange())
				.equals(DateRange.getStartDate(dateRange))
			: "Incorrect end date of original, mismatching status term";
	}
	
	/**
	 * Tests that a later correctional status term starting during the
	 * placement term but with a mismatching correctional status
	 * is adjusted to start when the placement term is finished and that
	 * a new correctional status term is used by the placement term.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status terms exist
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if conflicting placement terms
	 * exist
	 */
	public void testLaterMatchingCorrectionalStatusTermIsStartedLater()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - secures Blofeld
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange secureRange = new DateRange(
				this.parseDateText("12/21/2010"),
				this.parseDateText("12/21/2020"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, secureRange, secure);
		
		// Action - places Blofeld on Parole during secure term
		DateRange dateRange = new DateRange(
				this.parseDateText("12/21/2005"),
				this.parseDateText("12/21/2015"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "PAROFF", null);
		PlacementTerm placementTerm = this.placementTermService
			.create(blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that different correctional status term to secure is used
		assert !placementTerm.getCorrectionalStatusTerm().equals(secureTerm)
			: "Term with mismatching correctional status term is used";
		
		// Asserts that original secure term start date was adjusted to
		// end date of placement term
		assert DateRange.getStartDate(secureTerm.getDateRange())
			.equals(DateRange.getEndDate(dateRange))
			: "Incorrect start date of original, mismatching status term";
	}
	
	/**
	 * Tests that an earlier supervisory organization term ending
	 * during placement term but with a mismatching supervisory organization
	 * is ended and a new supervisory organization term began for use by the
	 * placement term
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status exists 
	 * @throws PlacementTermExistsException if placement term exists
	 */
	public void testEarlierMismatchingSupervisoryOrganizationTermIsEnded()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - supervises Blofeld by prison
		Offender offender = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange prisonRange = new DateRange(
				this.parseDateText("05/31/2000"),
				this.parseDateText("05/29/2010"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					offender, prisonRange, prison);
		
		// Action - paroles Blofeld during secure term
		DateRange dateRange = new DateRange(
				this.parseDateText("03/21/2005"),
				this.parseDateText("12/30/2015"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "POF", null);
		PlacementTerm placementTerm = this.placementTermService
				.create(offender, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that a different supervisory organization was ended on start
		// date of placement term
		assert !placementTerm.getSupervisoryOrganizationTerm()
				.equals(prisonTerm)
			: "Term with mismatching supervisory organization term is used";
				
		// Asserts that original prison term was ended on start date of
		// placement term
		assert DateRange.getEndDate(prisonTerm.getDateRange()).equals(
				DateRange.getStartDate(dateRange))
			: "Incorrect end date of original, mismatching organization term";
	}
	
	/**
	 * Tests that a later supervisory organization term starting during the
	 * placement term but with a mismatching supervisory organization
	 * is adjusted to start when the placement term is finished and that a new
	 * supervisory organization term is used by the placement term. 
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entities exist
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization terms exist
	 * @throws PlacementTermConflictException if conflicting placement
	 * terms exist
	 */
	public void testLaterMatchingSupervisoryOrganizationTermIsStartedLater()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld at prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange secureRange = new DateRange(
				this.parseDateText("12/21/2010"),
				this.parseDateText("12/21/2020"));
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, secureRange, prison);
		
		// Action - places Blofeld on Parole during prison placement
		DateRange dateRange = new DateRange(
				this.parseDateText("12/21/2005"),
				this.parseDateText("12/21/2015"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", true, (short) 1, true);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "PROFF", null);
		PlacementTerm placementTerm = this.placementTermService.create(
				blofeld, paroleOffice, parole, dateRange, null, null);
		
		// Asserts that different supervisory organization term to prison
		// is used
		assert !placementTerm.getSupervisoryOrganizationTerm()
				.equals(prisonTerm)
			: "Term with mismatching supervisory organization is used";
				
		// Asserts that original prison term start date was adjusted to
		// end date of placement term
		assert DateRange.getStartDate(prisonTerm.getDateRange())
				.equals(DateRange.getEndDate(dateRange))
			: "Incorrect start date of original, mismatching organization term";
	}
	
	/**
	 * Tests that {@code PlacementTermConflictException} is thrown when a
	 * conflicting placement term exists entirely within created placement term.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 * @throws PlacementTermConflictException if conflicting placement term
	 * exists - asserted
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test(expectedExceptions = {PlacementTermConflictException.class})
	public void testPlacementTermConflict()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - secures Blofeld at prison
		Offender offender = offenderDelegate.createWithoutIdentity(
				"Blofeld", "Ernst", "Stavro", null);
		DateRange prisonRange = new DateRange(
				this.parseDateText("07/23/2005"),
				this.parseDateText("06/21/2015"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm = this.correctionalStatusTermDelegate
				.create(offender, prisonRange, secure);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate.create(
					offender, prisonRange, prison);
		this.placementTermDelegate.create(offender, prisonRange, prisonTerm,
				secureTerm, null, null, false);
		
		// Action - places Blofeld on parole
		DateRange dateRange = new DateRange(
				this.parseDateText("12/21/2000"),
				this.parseDateText("01/12/2020"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 1, true);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate
				.create("Parole Office", "POF", null);
		this.placementTermService.create(
				offender, paroleOffice, parole, dateRange, null, null);
	}
	
	/**
	 * Tests that {@code PlacementTermConflictException} is thrown when a
	 * conflicting placement term exists entirely within created placement term.
	 * 
	 * <p>A placement term exists that overlaps the start date, this should be
	 * adjust to not conflict with the created placement term. The conflict
	 * should occur due to another existing placement within the date range
	 * of the created one.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 * @throws PlacementTermConflictException if conflicting placement term
	 * exists - asserted
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test(expectedExceptions = {PlacementTermConflictException.class})
	public void testPlacementTermConflictWithStartOverlap()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange prisonRange = new DateRange(
				this.parseDateText("06/30/2000"),
				this.parseDateText("06/30/2010"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, prisonRange, secure);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, prisonRange, prison);
		this.placementTermDelegate.create(
			blofeld, prisonRange, prisonTerm, secureTerm, null, null, false);
		
		// More arrangements - place Blofeld on parole
		DateRange paroleRange = new DateRange(
				this.parseDateText("06/30/2010"),
				this.parseDateText("06/30/2015"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 2, true);
		CorrectionalStatusTerm paroleTerm = this.correctionalStatusTermDelegate
				.create(blofeld, paroleRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "POFF", null);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, paroleRange, paroleOffice);
		this.placementTermDelegate.create(
				blofeld, paroleRange, paroleOfficeTerm, paroleTerm, null, null,
				false);
		
		// Action - places Blofeld on alt placement during prison term and
		// ending after parole placement
		DateRange altRange = new DateRange(
				this.parseDateText("06/31/2005"),
				this.parseDateText("06/31/2020"));
		CorrectionalStatus alt = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 3, true);
		SupervisoryOrganization prerelease
			= this.supervisoryOrganizationDelegate
				.create("Prerelease", "PRER", null);
		this.placementTermService.create(
				blofeld, prerelease, alt, altRange, null, null);
	}
	
	/**
	 * Tests that {@code PlacementTermConflictException} is thrown when a
	 * conflicting placement term exists entirely within created placement term.
	 * 
	 * <p>A placement term exists that overlaps the end date, this should be
	 * adjust to not conflict with the created placement term. The conflict
	 * should occur due to another existing placement within the date range
	 * of the created one.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 * @throws PlacementTermConflictException if conflicting placement term
	 * exists - asserted
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test(expectedExceptions = {PlacementTermConflictException.class})
	public void testPlacementTermConflictWithEndOverlap()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange prisonRange = new DateRange(
				this.parseDateText("06/31/2010"),
				this.parseDateText("06/31/2015"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, prisonRange, secure);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, prisonRange, prison);
		this.placementTermDelegate.create(
			blofeld, prisonRange, prisonTerm, secureTerm, null, null, false);
		
		// More arrangements - place Blofeld on parole
		DateRange paroleRange = new DateRange(
				this.parseDateText("06/31/2015"),
				this.parseDateText("06/31/2025"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 2, true);
		CorrectionalStatusTerm paroleTerm = this.correctionalStatusTermDelegate
				.create(blofeld, paroleRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "POFF", null);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, paroleRange, paroleOffice);
		this.placementTermDelegate.create(
				blofeld, paroleRange, paroleOfficeTerm, paroleTerm, null, null,
				false);
		
		// Action - places Blofeld on alt placement during prison term and
		// ending after parole placement
		DateRange altRange = new DateRange(
				this.parseDateText("06/31/2005"),
				this.parseDateText("06/31/2020"));
		CorrectionalStatus alt = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 3, true);
		SupervisoryOrganization prerelease
			= this.supervisoryOrganizationDelegate
				.create("Prerelease", "PRER", null);
		this.placementTermService.create(
				blofeld, prerelease, alt, altRange, null, null);
	}
	
	/**
	 * Tests that {@code PlacementTermConflictException} is thrown when a
	 * conflicting placement term exists entirely within created placement term.
	 * 
	 * <p>A placement term exists that overlaps the start date, another the end
	 * date, these should be adjust to not conflict with the created placement
	 * term. The conflict should occur due to another existing placement within
	 * the date range of the created one.
	 * 
	 * @throws DuplicateEntityFoundException if entities exist 
	 * @throws PlacementTermConflictException if conflicting placement term
	 * exists - asserted
	 * @throws SupervisoryOrganizationTermConflictException if conflicting
	 * supervisory organization term exists 
	 * @throws CorrectionalStatusTermConflictException if conflicting
	 * correctional status term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	// Disabled as correctional status (and supervisory organization) check
	// is before placement term check in implementation - SA
	@Test(expectedExceptions = {PlacementTermConflictException.class},
			enabled = true)
	public void testPlacementTermConflictWithStartAndEndOverlap()
			throws DuplicateEntityFoundException,
				PlacementTermExistsException,
				CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException {
		
		// Arrangements - places Blofeld in prison
		Offender blofeld = this.offenderDelegate
				.createWithoutIdentity("Blofeld", "Ernst", "Stavro", null);
		DateRange prisonRange = new DateRange(
				this.parseDateText("06/30/2000"),
				this.parseDateText("06/30/2010"));
		CorrectionalStatus secure = this.correctionalStatusDelegate
				.create("Secure", "SEC", true, (short) 1, true);
		CorrectionalStatusTerm secureTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, prisonRange, secure);
		SupervisoryOrganization prison = this.supervisoryOrganizationDelegate
				.create("Prison", "PRSN", null);
		SupervisoryOrganizationTerm prisonTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, prisonRange, prison);
		this.placementTermDelegate.create(
			blofeld, prisonRange, prisonTerm, secureTerm, null, null, false);
		
		// More arrangements - place Blofeld on parole
		DateRange paroleRange = new DateRange(
				this.parseDateText("06/30/2010"),
				this.parseDateText("06/30/2015"));
		CorrectionalStatus parole = this.correctionalStatusDelegate
				.create("Parole", "PAR", false, (short) 2, true);
		CorrectionalStatusTerm paroleTerm = this.correctionalStatusTermDelegate
				.create(blofeld, paroleRange, parole);
		SupervisoryOrganization paroleOffice
			= this.supervisoryOrganizationDelegate.create(
					"Parole Office", "PAFF", null);
		SupervisoryOrganizationTerm paroleOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, paroleRange, paroleOffice);
		this.placementTermDelegate.create(
				blofeld, paroleRange, paroleOfficeTerm, paroleTerm, null, null,
				false);
		
		// More arrangements - place Blofeld on probation
		DateRange probationRange = new DateRange(
				this.parseDateText("06/30/2015"),
				this.parseDateText("06/30/2025"));
		CorrectionalStatus probation = this.correctionalStatusDelegate
				.create("Probation", "PRO", false, (short) 3, true);
		CorrectionalStatusTerm probationTerm
			= this.correctionalStatusTermDelegate
				.create(blofeld, probationRange, probation);
		SupervisoryOrganization probationOffice
			= this.supervisoryOrganizationDelegate.create(
					"Probation Office", "PRFF", null);
		SupervisoryOrganizationTerm probationOfficeTerm
			= this.supervisoryOrganizationTermDelegate
				.create(blofeld, probationRange, probationOffice);
		this.placementTermDelegate.create(
				blofeld, probationRange, probationOfficeTerm, probationTerm,
				null, null, false);
		
		// Action - places Blofeld on alt placement during prison term and
		// ending after parole placement
		DateRange altRange = new DateRange(
				this.parseDateText("06/30/2005"),
				this.parseDateText("06/30/2020"));
		CorrectionalStatus alt = this.correctionalStatusDelegate
				.create("Alt-Secure", "ALT", true, (short) 4, true);
		SupervisoryOrganization prerelease
			= this.supervisoryOrganizationDelegate
				.create("Prerelease", "PRER", null);
		this.placementTermService.create(
				blofeld, prerelease, alt, altRange, null, null);
	}
	
	/* Helper methods. */
	
	// Parses date text
	private Date parseDateText(final String dateText) {
		CustomDateEditor customEditor = this.customDateEditorFactory
				.createCustomDateOnlyEditor(true);
		customEditor.setAsText(dateText);
		return (Date) customEditor.getValue();
	}
}