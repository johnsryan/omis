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
package omis.commitstatus.service.testng;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.commitstatus.domain.CommitStatus;
import omis.commitstatus.domain.CommitStatusTerm;
import omis.commitstatus.service.CommitStatusTermService;
import omis.commitstatus.service.delegate.CommitStatusDelegate;
import omis.commitstatus.service.delegate.CommitStatusTermDelegate;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests commit status term creation with auto ending 
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2
 * @since OMIS 3.0
 */
@Test(groups = {"commitstatus"})
public class CommitStatusTermServiceCommitStatusTermCreationWithAutoEndingTest
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	
	/* Delegates. */
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("commitStatusTermDelegate")
	private CommitStatusTermDelegate commitStatusTermDelegate;
	
	@Autowired
	@Qualifier("commitStatusDelegate")
	private CommitStatusDelegate commitStatusDelegate;
	
	/* Service to test. */
	@Autowired
	@Qualifier("commitStatusTermService")
	private CommitStatusTermService commitStatusTermService;
	
	/**
	 * Tests the creation of commit status term with auto ending.
	 * @throws DuplicateEntityFoundException 
	 */
	@Test
	public void testCommitStatusTermCreationWithAutoEnding() 
		throws DuplicateEntityFoundException{
		// Create the first term with end date is null
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
			"Kevin", "Johns", "Mr.");
		CommitStatus status1 = this.commitStatusDelegate.create("Status A", 
				true);
		DateRange dateRange1 = new DateRange();
		Date startDate1= new Date(105120000);
		dateRange1.setStartDate(startDate1);
		CommitStatusTerm term1 = this.commitStatusTermService.create(offender, 
			status1, dateRange1);
		// Create the properties to prepare the creation of the second term whose 
		// start date is behind the start date of the first created term and its 
		// end date is null  
		CommitStatus status2 = this.commitStatusDelegate.create("Status b", 
				true);
		DateRange dateRange2 = new DateRange();
		Date startDate2= new Date(1205120000);
		dateRange2.setStartDate(startDate2);
		
		// Create the second term
		CommitStatusTerm term2 = this.commitStatusTermService.create(offender, 
			status2, dateRange2);
		
		// Assertions
		// Check the 2nd term. Based on the successful test conducted in 
		// CommitStatusTermServiceCommitStatusTermCreationTest the creation of 
		// 1st term (lines 55 - 62) is assumed to be successful
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("status", status1)
			.addExpectedValue("dateRange.startDate", startDate1)
			.addExpectedValue("dateRange.endDate", startDate2)
			.performAssertions(term1);
		
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("status", status2)
			.addExpectedValue("dateRange.startDate", startDate2)
			.addExpectedValue("dateRange.endDate", null)
			.performAssertions(term2);
	}
}