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
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.exception.OffenderNotUnderSupervisionException;
import omis.supervision.exception.OffenderSupervisedByOrganizationException;
import omis.supervision.exception.PlacementTermChangeReasonExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
import omis.supervision.service.ChangeSupervisoryOrganizationService;
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
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"placement", "service"})
public class ChangeSupervisoryOrganizationServiceChangeTests
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
	@Qualifier("placementTermChangeReasonDelegate")
	private PlacementTermChangeReasonDelegate placementTermChangeReasonDelegate;
	
	@Autowired
	@Qualifier("supervisoryOrganizationTermDelegate")
	private SupervisoryOrganizationTermDelegate 
			supervisoryOrganizationTermDelegate;
	
	@Autowired
	@Qualifier("placementTermDelegate")
	private PlacementTermDelegate placementTermDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	/* Service to test. */
	
	@Autowired
	@Qualifier("changeSupervisoryOrganizationService")
	private ChangeSupervisoryOrganizationService 
			changeSupervisoryOrganizationService;
	
	/* Test methods. */
	
	/**
	 * Tests the change placement term method.
	 * 
	 * @throws OffenderNotUnderSupervisionException thrown if offender is not 
	 * currently under supervision
	 * @throws OffenderSupervisedByOrganizationException thrown if the 
	 * supervisory organization matches the currently supervised organization
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws PlacementTermChangeReasonExistsException if placement term
	 * exists 
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists
	 * @throws CorrectionalStatusExistsException if correctional status exists 
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test
	public void testChange()
			throws OffenderNotUnderSupervisionException, 
				OffenderSupervisedByOrganizationException,
				SupervisoryOrganizationExistsException,
				PlacementTermChangeReasonExistsException,
				SupervisoryOrganizationTermExistsException,
				CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Bob", null);
		SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
						null);
		PlacementTermChangeReason changeReason = 
				this.placementTermChangeReasonDelegate.create("Reason", 
						(short) 1, true, true);
		Date effectiveDate = this.parseDateText("01/01/2017");
		DateRange dateRange = new DateRange(effectiveDate, null);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm =
				this.supervisoryOrganizationTermDelegate.create(offender, 
						dateRange, supervisoryOrganization);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		CorrectionalStatusTerm correctionalStatusTerm = 
				this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		Boolean locked = false;
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				changeReason, null, locked);
		SupervisoryOrganization newSuperOrg = 
				this.supervisoryOrganizationDelegate.create("NewSuperOrg", 
						"NSO", null);
		
		// Action
		PlacementTerm placementTerm = this.changeSupervisoryOrganizationService
				.change(offender, newSuperOrg, changeReason, 
						effectiveDate);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue(
					"supervisoryOrganizationTerm.supervisoryOrganization", 
					newSuperOrg)
			.addExpectedValue("correctionalStatusTerm", correctionalStatusTerm)
			.addExpectedValue("locked", locked)
			.addExpectedValue("startChangeReason", changeReason)
			.addExpectedValue("endChangeReason", null)
			.performAssertions(placementTerm);
	}
	
	/**
	 * Tests the {@code OffenderNotUnderSupervisionException} is thrown.
	 * 
	 * @throws OffenderNotUnderSupervisionException thrown if offender is not 
	 * currently under supervision
	 * @throws OffenderSupervisedByOrganizationException thrown if the 
	 * supervisory organization matches the currently supervised organization
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws PlacementTermChangeReasonExistsException if placement term
	 * change reason exists 
	 */
	@Test(expectedExceptions = {OffenderNotUnderSupervisionException.class})
	public void testOffenderNotUnderSupervisionException()
			throws OffenderNotUnderSupervisionException, 
				OffenderSupervisedByOrganizationException,
				SupervisoryOrganizationExistsException,
				PlacementTermChangeReasonExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Bob", null);
		SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
						null);
		PlacementTermChangeReason changeReason = 
				this.placementTermChangeReasonDelegate.create("Reason", 
						(short) 1, true, true);
		Date effectiveDate = this.parseDateText("01/01/2017");
		
		// Action
		this.changeSupervisoryOrganizationService.change(offender, 
				supervisoryOrganization, changeReason, effectiveDate);
	}
	
	/**
	 * Tests the {@code OffenderSupervisedByOrganizationException} is thrown.
	 * 
	 * @throws OffenderNotUnderSupervisionException thrown if offender is not 
	 * currently under supervision
	 * @throws OffenderSupervisedByOrganizationException thrown if the 
	 * supervisory organization matches the currently supervised organization
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization exists
	 * @throws PlacementTermChangeReasonExistsException if placement term
	 * change reason exists 
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists 
	 * @throws CorrectionalStatusExistsException if correctional status
	 * exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists 
	 * @throws PlacementTermExistsException if placement term exists 
	 */
	@Test(expectedExceptions = 
			{OffenderSupervisedByOrganizationException.class})
	public void testOffenderSupervisedByOrganizationException() 
			throws OffenderNotUnderSupervisionException,
				OffenderSupervisedByOrganizationException,
				SupervisoryOrganizationExistsException,
				PlacementTermChangeReasonExistsException,
				SupervisoryOrganizationTermExistsException,
				CorrectionalStatusExistsException,
				CorrectionalStatusTermExistsException,
				PlacementTermExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Bob", null);
		SupervisoryOrganization supervisoryOrganization =
				this.supervisoryOrganizationDelegate.create("SuperOrg", "SO", 
						null);
		PlacementTermChangeReason changeReason = 
				this.placementTermChangeReasonDelegate.create("Reason", 
						(short) 1, true, true);
		Date effectiveDate = this.parseDateText("01/01/2017");
		DateRange dateRange = new DateRange(effectiveDate, null);
		SupervisoryOrganizationTerm supervisoryOrganizationTerm =
				this.supervisoryOrganizationTermDelegate.create(offender, 
						dateRange, supervisoryOrganization);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		CorrectionalStatusTerm correctionalStatusTerm = 
				this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		this.placementTermDelegate.create(offender, dateRange, 
				supervisoryOrganizationTerm, correctionalStatusTerm, 
				changeReason, null, false);
		
		// Action
		this.changeSupervisoryOrganizationService.change(offender, 
				supervisoryOrganization, changeReason, effectiveDate);
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
