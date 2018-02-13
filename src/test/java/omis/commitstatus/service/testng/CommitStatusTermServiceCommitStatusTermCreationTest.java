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
 * Tests commit status term creation 
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2
 * @since OMIS 3.0
 */
@Test(groups = {"commitstatus"})
public class CommitStatusTermServiceCommitStatusTermCreationTest
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
	 * Tests the creation of commit status term.
	 */
	@Test
	public void testCommitStatusTermCreation() 
			throws DuplicateEntityFoundException {
		// Arrangement
		Offender offender = this.offenderDelegate.createWithoutIdentity("Obama",
			"Kevin", "Johns", "Mr.");
		CommitStatus status = this.commitStatusDelegate.create("Status A", true);
		DateRange dateRange = new DateRange();
		Date startDate = new Date(105120000);
		Date endDate = new Date(205120000);
		dateRange.setStartDate(startDate);
		dateRange.setEndDate(endDate);
		
		// Action
		CommitStatusTerm term = this.commitStatusTermService.create(offender, 
			status, dateRange);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", offender)
			.addExpectedValue("status", status)
			.addExpectedValue("dateRange.startDate", startDate)
			.addExpectedValue("dateRange.endDate", endDate)
			.performAssertions(term);
	}
	
	/**
	 * Tests duplicate commit status term on creation.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate term exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateCommitStatusCreate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("asfd",
			"tjuty", "abedb", "Mr.");
		CommitStatus status;
		status = this.commitStatusDelegate.create("ahrt", true);
		DateRange dateRange = new DateRange();
		Date startDate = new Date(10000000);
		Date endDate = new Date(20000000);
		dateRange.setStartDate(startDate);
		dateRange.setEndDate(endDate);
		this.commitStatusTermDelegate.create(offender, status, dateRange);
		
		// Action
		this.commitStatusTermService.create(offender, status, dateRange);
	}
}