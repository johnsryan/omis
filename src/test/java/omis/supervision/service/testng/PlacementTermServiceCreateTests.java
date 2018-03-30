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
import omis.supervision.domain.PlacementTerm;
import omis.supervision.domain.PlacementTermChangeReason;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.PlacementTermConflictException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermConflictException;
import omis.supervision.service.PlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create placement terms.
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
		
		// Action
		PlacementTerm placementTerm = this.placementTermService
				.create(offender, supervisoryOrganization, correctionalStatus,
						dateRange, startChangeReason, endChangeReason);
		
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
					supervisoryOrganization)
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