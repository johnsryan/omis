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
package omis.paroleboardcondition.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.condition.domain.Agreement;
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
import omis.paroleboardcondition.service.ParoleBoardConditionService;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update conditions.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
@Test
public class ParoleBoardConditionServiceUpdateConditionTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	private ConditionClauseDelegate conditionClauseDelegate;
	
	@Autowired
	private ConditionTitleDelegate conditionTitleDelegate;
	
	@Autowired
	private OffenderDelegate offenderDelegate;

	@Autowired
	private AgreementDelegate agreementDelegate;
	
	@Autowired
	private ConditionDelegate conditionDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("paroleBoardConditionService")
	private ParoleBoardConditionService paroleBoardConditionService;

	/* Test methods. */

	/**
	 * Tests the update of the condition clause for a condition.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateConditionConditionClause() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/12/2017"), 
				this.parseDateText("05/15/2019"), null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Condition Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);
		String clause = "Santa Clause";
		ConditionCategory category = ConditionCategory.STANDARD;
		Boolean waived = false;
		Condition condition = this.conditionDelegate.create(agreement, clause, 
				conditionClause, category, waived);
		ConditionClause newConditionClause = this.conditionClauseDelegate
				.create("New Condition Clause Description", conditionTitle, 
						true);
		
		// Action
		condition = this.paroleBoardConditionService.updateCondition(condition, 
				newConditionClause, clause, category, waived);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("agreement", agreement)
			.addExpectedValue("clause", clause)
			.addExpectedValue("conditionClause", newConditionClause)
			.addExpectedValue("category", category)
			.addExpectedValue("waived", waived)
			.performAssertions(condition);
	}
	
	/**
	 * Tests the update of the category for a condition.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateConditionCategory() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/12/2017"), 
				this.parseDateText("05/15/2019"), null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Condition Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);
		String clause = "Santa Clause";
		ConditionCategory category = ConditionCategory.STANDARD;
		Boolean waived = false;
		Condition condition = this.conditionDelegate.create(agreement, clause, 
				conditionClause, category, waived);
		ConditionCategory newCategory = ConditionCategory.PREREQUISITE;
		
		// Action
		condition = this.paroleBoardConditionService.updateCondition(condition, 
				conditionClause, clause, newCategory, waived);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("agreement", agreement)
			.addExpectedValue("clause", clause)
			.addExpectedValue("conditionClause", conditionClause)
			.addExpectedValue("category", newCategory)
			.addExpectedValue("waived", waived)
			.performAssertions(condition);
	}
	
	/**
	 * Tests the update of the waived flag for a condition.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateConditionWaived() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/12/2017"), 
				this.parseDateText("05/15/2019"), null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Condition Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);
		String clause = "Santa Clause";
		ConditionCategory category = ConditionCategory.STANDARD;
		Boolean waived = false;
		Condition condition = this.conditionDelegate.create(agreement, clause, 
				conditionClause, category, waived);
		Boolean newWaived = true;

		// Action
		condition = this.paroleBoardConditionService.updateCondition(condition, 
				conditionClause, clause, category, newWaived);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("agreement", agreement)
			.addExpectedValue("clause", clause)
			.addExpectedValue("conditionClause", conditionClause)
			.addExpectedValue("category", category)
			.addExpectedValue("waived", newWaived)
			.performAssertions(condition);
	}
	
	/**
	 * Tests the update of the clause for a condition.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateConditionClause() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/12/2017"), 
				this.parseDateText("05/15/2019"), null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Condition Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);
		String clause = "Santa Clause";
		ConditionCategory category = ConditionCategory.STANDARD;
		Boolean waived = false;
		Condition condition = this.conditionDelegate.create(agreement, clause, 
				conditionClause, category, waived);
		String newClause = "New Clause";

		// Action
		condition = this.paroleBoardConditionService.updateCondition(condition, 
				conditionClause, newClause, category, waived);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("agreement", agreement)
			.addExpectedValue("clause", newClause)
			.addExpectedValue("conditionClause", conditionClause)
			.addExpectedValue("category", category)
			.addExpectedValue("waived", waived)
			.performAssertions(condition);
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
		Offender offender = this.offenderDelegate.createWithoutIdentity("Wayne", 
				"Bruce", "Alen", null);
		Agreement agreement = this.agreementDelegate.create(offender, 
				this.parseDateText("05/12/2017"), 
				this.parseDateText("05/15/2019"), null, null);
		ConditionTitle conditionTitle = this.conditionTitleDelegate
				.create("Condition Title");
		ConditionClause conditionClause = this.conditionClauseDelegate
				.create("Condition Clause Description", conditionTitle, true);
		String clause = "Santa Clause";
		ConditionCategory category = ConditionCategory.STANDARD;
		Boolean waived = false;
		this.conditionDelegate.create(agreement, clause, conditionClause, 
				category, waived);
		String secondClause = "Second Clause";
		Condition condition = this.conditionDelegate.create(agreement, 
				secondClause, conditionClause, category, waived);

		// Action
		condition = this.paroleBoardConditionService.updateCondition(condition, 
				conditionClause, clause, category, waived);
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