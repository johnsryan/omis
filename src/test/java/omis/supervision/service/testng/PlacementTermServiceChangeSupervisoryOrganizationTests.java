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
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.supervision.domain.CorrectionalStatus;
import omis.supervision.domain.CorrectionalStatusTerm;
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.domain.SupervisoryOrganizationTerm;
import omis.supervision.exception.CorrectionalStatusExistsException;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.PlacementTermChangeReasonExistsException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermExistsAfterException;
import omis.supervision.exception.PlacementTermExistsBeforeException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
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
 * Tests method to change supervisory organization.
 *
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"placement", "service"})
public class PlacementTermServiceChangeSupervisoryOrganizationTests
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
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	/* Service to test. */
	
	@Autowired
	@Qualifier("placementTermService")
	private PlacementTermService placementTermService;
	
	/* Test methods. */
	
	/**
	 * Tests creation of placement term.
	 * 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if supervisory
	 * organization term conflicts
	 * @throws CorrectionalStatusTermConflictException if correctional status
	 * term conflicts 
	 * @throws PlacementTermExistsBeforeException if term exists and start date 
	 * is null
	 * @throws PlacementTermExistsAfterException if term exists after start date 
	 * and end date is null
	 * @throws OffenderNotUnderSupervisionException if offender not under 
	 * supervision
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusExistsException if correctional status exists 
	 * @throws PlacementTermChangeReasonExistsException if placement term
	 * change reason exists
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists 
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	public void testChangeSupervisoryOrganization()
			throws CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException, 
				OffenderNotUnderSupervisionException, 
				PlacementTermExistsAfterException, 
				PlacementTermExistsBeforeException,
				SupervisoryOrganizationExistsException,
				CorrectionalStatusExistsException,
				PlacementTermChangeReasonExistsException,
				SupervisoryOrganizationTermExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException {
		// Arrangements
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
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
					dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
					correctionalStatus);
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				startChangeReason, endChangeReason, false);
		SupervisoryOrganization newOrg = this.supervisoryOrganizationDelegate
				.create("NewOrg", "NO", null);
		
		// Action
		PlacementTerm placementTerm = this.placementTermService
				.changeSupervisoryOrganization(offender, newOrg, dateRange, 
						startChangeReason, endChangeReason);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("startChangeReason", startChangeReason)
			.addExpectedValue("endChangeReason", endChangeReason)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					correctionalStatus)
			.addExpectedValue("supervisoryOrganizationTerm"
						+ ".supervisoryOrganization",
					newOrg)
			.performAssertions(placementTerm);
	}
	
	/**
	 * Tests {@code OffenderNotUnderSupervisionException} is thrown.
	 * 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if supervisory
	 * organization term conflicts
	 * @throws CorrectionalStatusTermConflictException if correctional status
	 * term conflicts 
	 * @throws PlacementTermExistsBeforeException if term exists and start date 
	 * is null
	 * @throws PlacementTermExistsAfterException if term exists after start date 
	 * and end date is null
	 * @throws OffenderNotUnderSupervisionException if offender not under 
	 * supervision
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusExistsException if correctional status exists 
	 * @throws PlacementTermChangeReasonExistsException if placement term
	 * change reason exists
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists 
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testOffenderNotUnderSupervisionException()
			throws CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException, 
				OffenderNotUnderSupervisionException, 
				PlacementTermExistsAfterException, 
				PlacementTermExistsBeforeException,
				SupervisoryOrganizationExistsException,
				CorrectionalStatusExistsException,
				PlacementTermChangeReasonExistsException,
				SupervisoryOrganizationTermExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException {
		// Arrangements
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
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
					dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
					correctionalStatus);
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				startChangeReason, endChangeReason, false);
		SupervisoryOrganization newOrg = this.supervisoryOrganizationDelegate
				.create("NewOrg", "NO", null);
		DateRange newDateRange = new DateRange(null, 
				this.parseDateText("03/21/2017"));
		
		// Action
		PlacementTerm placementTerm = this.placementTermService
				.changeSupervisoryOrganization(offender, newOrg, newDateRange, 
						startChangeReason, endChangeReason);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", newDateRange)
			.addExpectedValue("startChangeReason", startChangeReason)
			.addExpectedValue("endChangeReason", endChangeReason)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					correctionalStatus)
			.addExpectedValue("supervisoryOrganizationTerm"
						+ ".supervisoryOrganization",
					newOrg)
			.performAssertions(placementTerm);
	}
	
	/**
	 * Tests {@code PlacementTermExistsAfterException} is thrown.
	 * 
	 * @throws PlacementTermConflictException if placement term conflicts
	 * @throws SupervisoryOrganizationTermConflictException if supervisory
	 * organization term conflicts
	 * @throws CorrectionalStatusTermConflictException if correctional status
	 * term conflicts 
	 * @throws PlacementTermExistsBeforeException if term exists and start date 
	 * is null
	 * @throws PlacementTermExistsAfterException if term exists after start date 
	 * and end date is null
	 * @throws OffenderNotUnderSupervisionException if offender not under 
	 * supervision
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws CorrectionalStatusExistsException if correctional status exists 
	 * @throws PlacementTermChangeReasonExistsException if placement term exists
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test(expectedExceptions = {PlacementTermExistsAfterException.class})
	public void testPlacementTermExistsAfterException()
			throws CorrectionalStatusTermConflictException,
				SupervisoryOrganizationTermConflictException,
				PlacementTermConflictException, 
				OffenderNotUnderSupervisionException, 
				PlacementTermExistsAfterException, 
				PlacementTermExistsBeforeException,
				SupervisoryOrganizationExistsException,
				CorrectionalStatusExistsException,
				PlacementTermChangeReasonExistsException,
				SupervisoryOrganizationTermExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException {
		// Arrangements
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
		SupervisoryOrganizationTerm supervisoryOrganizationTerm 
			= this.supervisoryOrganizationTermDelegate.create(offender, 
					dateRange, supervisoryOrganization);
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermDelegate.create(offender, dateRange, 
					correctionalStatus);
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				startChangeReason, endChangeReason, false);
		dateRange = new DateRange(this.parseDateText("03/21/2017"),
				this.parseDateText("10/10/2020"));
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				startChangeReason, endChangeReason, false);
		SupervisoryOrganization newOrg = this.supervisoryOrganizationDelegate
				.create("NewOrg", "NO", null);
		DateRange newDateRange = new DateRange(this.parseDateText("03/15/2012"), 
				null);
		
		// Action
		PlacementTerm placementTerm = this.placementTermService
				.changeSupervisoryOrganization(offender, newOrg, newDateRange, 
						startChangeReason, endChangeReason);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", newDateRange)
			.addExpectedValue("startChangeReason", startChangeReason)
			.addExpectedValue("endChangeReason", endChangeReason)
			.addExpectedValue("correctionalStatusTerm.correctionalStatus",
					correctionalStatus)
			.addExpectedValue("supervisoryOrganizationTerm"
						+ ".supervisoryOrganization",
					newOrg)
			.performAssertions(placementTerm);
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