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
import omis.supervision.exception.CorrectionalStatusExistsException;
import omis.supervision.exception.CorrectionalStatusTermConflictException;
import omis.supervision.exception.CorrectionalStatusTermExistsException;
import omis.supervision.service.CorrectionalStatusTermService;
import omis.supervision.service.delegate.CorrectionalStatusDelegate;
import omis.supervision.service.delegate.CorrectionalStatusTermDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to create correctional status terms.
 *
 * @author Josh Divine
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test(groups = {"placement", "service"})
public class CorrectionalStatusTermServiceCreateTests
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
	@Qualifier("correctionalStatusDelegate")
	private CorrectionalStatusDelegate correctionalStatusDelegate;
	
	@Autowired
	@Qualifier("correctionalStatusTermDelegate")
	private CorrectionalStatusTermDelegate correctionalStatusTermDelegate;
	
	/* Service to test. */
	
	@Autowired
	@Qualifier("correctionalStatusTermService")
	private CorrectionalStatusTermService correctionalStatusTermService;
	
	/* Test methods. */
	
	/**
	 * Tests the creation of a correctional status term.
	 * 
	 * @throws CorrectionalStatusTermExistsException thrown if correctional
	 * status term exists
	 * @throws CorrectionalStatusTermConflictException thrown if date range 
	 * conflicts with existing date ranges
	 * @throws CorrectionalStatusExistsException thrown if correctional status
	 * exists
	 */
	@Test
	public void testCreate()
			throws CorrectionalStatusTermExistsException, 
				CorrectionalStatusTermConflictException, CorrectionalStatusExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Bob", null);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2018")); 
		
		// Action
		CorrectionalStatusTerm correctionalStatusTerm 
			= this.correctionalStatusTermService.create(offender, 
					correctionalStatus, dateRange);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("dateRange", dateRange)
			.addExpectedValue("correctionalStatus", correctionalStatus)
			.performAssertions(correctionalStatusTerm);
	}
	
	/**
	 * Tests the {@code CorrectionalStatusTermExistsException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException thrown if correctional
	 * status term already exists - asserted
	 * @throws CorrectionalStatusTermConflictException thrown if date range 
	 * conflicts with existing date ranges
	 * @throws CorrectionalStatusExistsException thrown if correctional status
	 * exists
	 */
	@Test(expectedExceptions = {CorrectionalStatusTermExistsException.class})
	public void testDuplicateEntityFoundException() 
			throws CorrectionalStatusTermExistsException, 
				CorrectionalStatusTermConflictException,
				CorrectionalStatusExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Bob", null);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2018")); 
		this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		
		// Action
		this.correctionalStatusTermService.create(offender, correctionalStatus, 
				dateRange);
	}
	
	/**
	 * Tests the {@code CorrectionalStatusTermConflictException} is thrown.
	 * 
	 * @throws CorrectionalStatusTermExistsException thrown if correctional
	 * status term exists
	 * @throws CorrectionalStatusTermConflictException thrown if date range 
	 * conflicts with existing date ranges
	 * @throws CorrectionalStatusExistsException thrown if correctional status
	 * exists
	 */
	@Test(expectedExceptions = {CorrectionalStatusTermConflictException.class})
	public void testCorrectionalStatusTermConflictException() 
			throws CorrectionalStatusTermExistsException, 
				CorrectionalStatusTermConflictException,
				CorrectionalStatusExistsException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith",
				"John", "Bob", null);
		CorrectionalStatus correctionalStatus = this.correctionalStatusDelegate
				.create("Status", "S", false, (short) 1, true);
		DateRange dateRange = new DateRange(this.parseDateText("01/01/2017"), 
				this.parseDateText("01/01/2018")); 
		this.correctionalStatusTermDelegate.create(offender, dateRange, 
				correctionalStatus);
		dateRange = new DateRange(this.parseDateText("06/01/2017"), 
				this.parseDateText("06/01/2018"));
		
		// Action
		this.correctionalStatusTermService.create(offender, correctionalStatus, 
				dateRange);
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
