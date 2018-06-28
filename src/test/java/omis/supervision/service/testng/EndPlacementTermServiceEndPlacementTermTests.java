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
import omis.locationterm.exception.LocationTermExistsException;
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
import omis.supervision.exception.PlacementTermChangeReasonExistsException;
import omis.supervision.exception.PlacementTermExistsException;
import omis.supervision.exception.SupervisoryOrganizationExistsException;
import omis.supervision.exception.SupervisoryOrganizationTermExistsException;
import omis.supervision.service.EndPlacementTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.supervision.service.delegate.PlacementTermChangeReasonDelegate;
import omis.supervision.service.delegate.PlacementTermDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.supervision.service.delegate.SupervisoryOrganizationTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to end placement terms.
 *
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"placement", "service"})
public class EndPlacementTermServiceEndPlacementTermTests
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
	@Qualifier("endPlacementTermService")
	private EndPlacementTermService endPlacementTermService;
	
	/* Test methods. */
	
	/**
	 * Tests the end placement term method.
	 * 
	 * @throws SupervisoryOrganizationExistsException if supervisory
	 * organization term exists
	 * @throws PlacementTermChangeReasonExistsException if placement term
	 * change reason exists 
	 * @throws SupervisoryOrganizationTermExistsException if supervisory
	 * organization term exists 
	 * @throws CorrectionalStatusExistsException if correctional status exists
	 * @throws CorrectionalStatusTermExistsException if correctional status
	 * term exists
	 * @throws PlacementTermExistsException if placement term exists
	 * @throws LocationTermExistsException if location term exists
	 */
	@Test
	public void testEndPlacementTerm()
				throws SupervisoryOrganizationExistsException,
					PlacementTermChangeReasonExistsException,
					SupervisoryOrganizationTermExistsException,
					CorrectionalStatusExistsException,
					CorrectionalStatusTermExistsException,
					PlacementTermExistsException,
					LocationTermExistsException {
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
		PlacementTerm placementTerm = this.placementTermDelegate.create(
				offender, dateRange, supervisoryOrganizationTerm, 
				correctionalStatusTerm, changeReason, null, locked);
		PlacementTermChangeReason endChangeReason = 
				this.placementTermChangeReasonDelegate.create("EndReason", 
						(short) 2, false, true);
		Date endDate = this.parseDateText("01/01/2018");
		dateRange = new DateRange(effectiveDate, endDate);
		
		// Action
		placementTerm = this.endPlacementTermService.endPlacementTerm(
				placementTerm, endDate, endChangeReason);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("supervisoryOrganizationTerm", 
					supervisoryOrganizationTerm)
			.addExpectedValue("correctionalStatusTerm", correctionalStatusTerm)
			.addExpectedValue("locked", locked)
			.addExpectedValue("startChangeReason", changeReason)
			.addExpectedValue("endChangeReason", endChangeReason)
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
