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
package omis.violationevent.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.condition.domain.Agreement;
import omis.condition.domain.AgreementCategory;
import omis.condition.domain.Condition;
import omis.condition.domain.ConditionCategory;
import omis.condition.domain.ConditionClause;
import omis.condition.domain.ConditionTitle;
import omis.condition.service.delegate.AgreementDelegate;
import omis.condition.service.delegate.ConditionClauseDelegate;
import omis.condition.service.delegate.ConditionDelegate;
import omis.condition.service.delegate.ConditionTitleDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.supervision.domain.SupervisoryOrganization;
import omis.supervision.service.delegate.SupervisoryOrganizationDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.ViolationEvent;
import omis.violationevent.domain.ViolationEventCategory;
import omis.violationevent.service.ViolationEventService;
import omis.violationevent.service.delegate.ConditionViolationDelegate;
import omis.violationevent.service.delegate.ViolationEventDelegate;

/**
 * Tests method to create condition violations.
 *
 * @author Josh Divine
 * @version 0.1.0 (May 23, 2018)
 * @since OMIS 3.0
 */
@Test
public class ViolationEventServiceCreateConditionViolationTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private ConditionDelegate conditionDelegate;

	@Autowired
	private ViolationEventDelegate violationEventDelegate;

	@Autowired
	private OffenderDelegate offenderDelegate;

	@Autowired
	private SupervisoryOrganizationDelegate supervisoryOrganizationDelegate;
	
	@Autowired
	private AgreementDelegate agreementDelegate;
	
	@Autowired
	private ConditionTitleDelegate conditionTitleDelegate;
	
	@Autowired
	private ConditionClauseDelegate conditionClauseDelegate;
	
	@Autowired
	private ConditionViolationDelegate conditionViolationDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("violationEventService")
	private ViolationEventService violationEventService;

	/* Test methods. */

	/**
	 * Tests the creation of condition violations.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testCreateConditionViolation() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, null, date, details, category);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/01/2018"), null, "Description", 
				AgreementCategory.COURT_CASE);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Clause", conditionTitle, true);
		Condition condition = this.conditionDelegate.create(agreement, "Clause", 
				conditionClause, ConditionCategory.STANDARD, true);

		// Action
		ConditionViolation conditionViolation = this.violationEventService
				.createConditionViolation(condition, violationEvent);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("condition", condition)
			.addExpectedValue("violationEvent", violationEvent)
			.performAssertions(conditionViolation);
	}

	/**
	 * Tests {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Smith", 
				"John", "Jay", null);
		SupervisoryOrganization jurisdiction = this
				.supervisoryOrganizationDelegate.create("SuperOrg", "SO", null);
		Date date = this.parseDateText("05/23/2017");
		String details = "Details";
		ViolationEventCategory category = ViolationEventCategory.DISCIPLINARY;
		ViolationEvent violationEvent = this.violationEventDelegate.create(
				offender, jurisdiction, null, date, details, category);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/01/2018"), null, "Description", 
				AgreementCategory.COURT_CASE);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Clause", conditionTitle, true);
		Condition condition = this.conditionDelegate.create(agreement, "Clause", 
				conditionClause, ConditionCategory.STANDARD, true);
		this.conditionViolationDelegate.create(condition, violationEvent);
		
		// Action
		this.violationEventService.createConditionViolation(condition, 
				violationEvent);
	}

	// Parses date text
	private Date parseDateText(final String text) {
		try {
			return new SimpleDateFormat("MM/dd/yyyy").parse(text);
		} catch (ParseException e) {
			throw new RuntimeException("Parse error", e);
		}
	}
}