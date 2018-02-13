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
import omis.commitstatus.exception.CommitStatusTermExistsAfterException;
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
 * Tests "update" of commit status term
 *
 * @author Yidong Li
 * @author Josh Divine
 * @version 0.0.2
 * @since OMIS 3.0
 */
@Test(groups = {"commitstatus"})
public class CommitStatusTermServiceCommitStatusTermUpdateTest
		extends AbstractHibernateTransactionalTestNGSpringContextTests {
	/* Delegates. */
	
	@Autowired
	@Qualifier("offenderDelegate")
	private OffenderDelegate offenderDelegate;
	
	@Autowired
	@Qualifier("commitStatusDelegate")
	private CommitStatusDelegate commitStatusDelegate;
	
	@Autowired
	@Qualifier("commitStatusTermDelegate")
	private CommitStatusTermDelegate commitStatusTermDelegate;
	
	/* Service to test. */
	
	@Autowired
	@Qualifier("commitStatusTermService")
	private CommitStatusTermService commitStatusTermService;
		
	/**
	 * Tests commit status update.
	 */
	@Test
	public void testCommitStatusTermUpdate() 
			throws DuplicateEntityFoundException{
		// Arrangements
		Offender originalOffender = this.offenderDelegate.createWithoutIdentity(
			"Obama", "Kevin", "Johns", "Mr.");
		CommitStatus originalStatus = this.commitStatusDelegate.create(
			"Status A", true);
		DateRange originalDateRange = new DateRange();
		Date orignalStartDate = new Date(105120000);
		Date originalEndDate = new Date(205120000);
		originalDateRange.setStartDate(orignalStartDate);
		originalDateRange.setEndDate(originalEndDate);
		CommitStatusTerm term = this.commitStatusTermDelegate.create(
				originalOffender, originalStatus, originalDateRange);
		CommitStatus newStatus = this.commitStatusDelegate.create(
			"Status B", true);
		DateRange newDateRange = new DateRange();
		Date newStartDate = new Date(5120000);
		Date newEndDate = new Date(105120000);
		newDateRange.setStartDate(newStartDate);
		newDateRange.setEndDate(newEndDate);
		
		// Action
		term = this.commitStatusTermService.update(term, newStatus, 
				newDateRange);
		
		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("offender", originalOffender)
			.addExpectedValue("status", newStatus)
			.addExpectedValue("dateRange.startDate", newStartDate)
			.addExpectedValue("dateRange.endDate", newEndDate)
			.performAssertions(term);
	}
	
	/**
	 * Tests duplicate commit status term on update.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate term exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateCommitStatusTermUpdate() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
			"Obama", "Kevin", "Johns", "Mr.");
		CommitStatus status1 = this.commitStatusDelegate.create(
			"Status A", true);
		DateRange dateRange1 = new DateRange();
		Date startDate1 = new Date(105120000);
		Date endDate1 = new Date(205120000);
		dateRange1.setStartDate(startDate1);
		dateRange1.setEndDate(endDate1);
		CommitStatusTerm term1 = this.commitStatusTermDelegate.create(offender, 
			status1, dateRange1);
		CommitStatus status2 = this.commitStatusDelegate.create(
			"Status B", true);
		DateRange dateRange2 = new DateRange();
		Date startDate2 = new Date(5120000);
		Date endDate2 = new Date(105120000);
		dateRange2.setStartDate(startDate2);
		dateRange2.setEndDate(endDate2);
		this.commitStatusTermDelegate.create(offender, status2, dateRange2);
		
		// Action
		this.commitStatusTermService.update(term1, status2, dateRange2);
	}	
	
	/**
	 * Tests commit status term exist after exception on update.
	 * 
	 * @throws CommitStatusTermExistsAfterException if term exists after
	 */
	@Test()
	public void testExistsAfterExceptionCommitStatusTermUpdate() 
		throws DuplicateEntityFoundException, CommitStatusTermExistsAfterException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity(
			"Obama", "Kevin", "Johns", "Mr.");
		CommitStatus status = this.commitStatusDelegate.create(
			"Status A", true);
		DateRange dateRange = new DateRange();
		Date startDate = new Date(105120000);
		Date endDate = new Date(205120000);
		dateRange.setStartDate(startDate);
		dateRange.setEndDate(endDate);
		CommitStatusTerm term = this.commitStatusTermService.create(offender, 
			status, dateRange);
		
		// Action
		this.commitStatusTermService.update(term, status, dateRange);
	}	
}