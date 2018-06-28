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
package omis.hearinganalysis.service.testng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.service.HearingAnalysisTaskService;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.service.delegate.TaskAssignmentDelegate;
import omis.task.service.delegate.TaskDelegate;
import omis.testng.AbstractHibernateTransactionalTestNGSpringContextTests;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;
import omis.util.PropertyValueAsserter;

/**
 * Tests method to update task assignments.
 *
 * @author Josh Divine
 * @version 0.0.1
 * @since OMIS 3.0
 */
public class HearingAnalysisTaskServiceUpdateTaskAssignmentTests
	extends AbstractHibernateTransactionalTestNGSpringContextTests {

	/* Delegates. */

	@Autowired
	@Qualifier("userAccountDelegate")
	private UserAccountDelegate userAccountDelegate;
	
	@Autowired
	@Qualifier("personDelegate")
	private PersonDelegate personDelegate;
	
	@Autowired
	@Qualifier("taskDelegate")
	private TaskDelegate taskDelegate;
	
	@Autowired
	@Qualifier("taskAssignmentDelegate")
	private TaskAssignmentDelegate taskAssignmentDelegate;
	
	/* Services. */

	@Autowired
	@Qualifier("hearingAnalysisTaskService")
	private HearingAnalysisTaskService hearingAnalysisTaskService;

	/* Test methods. */

	/**
	 * Tests the update of the assignee account for a task assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateTaskAssignmentAssigneeAccount() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person user = this.personDelegate.create("Smith", "John", "Jay", null);
		UserAccount sourceAccount = this.userAccountDelegate.create(user, 
				"Username", "password", null, 0, true);
		Task task = this.taskDelegate.create("controllerName", "methodName", 
				"description", sourceAccount, this.parseDateText("01/01/2018"), 
				this.parseDateText("01/02/2018"));
		Person secondUser = this.personDelegate.create("Bob", "Jim", null, 
				null);
		UserAccount assigneeAccount = this.userAccountDelegate.create(
				secondUser, "Username2", "password", null, 0, true);;
		Date assignedDate = this.parseDateText("01/01/2018");
		TaskAssignment taskAssignment = this.taskAssignmentDelegate.create(task, 
				assignedDate, assigneeAccount);
		Person newUser = this.personDelegate.create("Bob", "Jimbo", null, 
				null);
		UserAccount newAssigneeAccount = this.userAccountDelegate.create(
				newUser, "Username3", "password", null, 0, true);
		Date lastInvokedDate = this.parseDateText("01/03/2018");
		
		// Action
		taskAssignment = this.hearingAnalysisTaskService.updateTaskAssignment(
				taskAssignment, newAssigneeAccount, assignedDate,
				lastInvokedDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("task", task)
			.addExpectedValue("assigneeAccount", newAssigneeAccount)
			.addExpectedValue("assignedDate", assignedDate)
			.addExpectedValue("lastInvokedDate", lastInvokedDate)
			.performAssertions(taskAssignment);
	}
	
	/**
	 * Tests the update of the assigned date for a task assignment.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test
	public void testUpdateTaskAssignment() throws DuplicateEntityFoundException {
		// Arrangements
		Person user = this.personDelegate.create("Smith", "John", "Jay", null);
		UserAccount sourceAccount = this.userAccountDelegate.create(user, 
				"Username", "password", null, 0, true);
		Task task = this.taskDelegate.create("controllerName", "methodName", 
				"description", sourceAccount, this.parseDateText("01/01/2018"), 
				this.parseDateText("01/02/2018"));
		Person secondUser = this.personDelegate.create("Bob", "Jim", null, 
				null);
		UserAccount assigneeAccount = this.userAccountDelegate.create(
				secondUser, "Username2", "password", null, 0, true);;
		Date assignedDate = this.parseDateText("01/01/2018");
		TaskAssignment taskAssignment = this.taskAssignmentDelegate.create(task, 
				assignedDate, assigneeAccount);
		Date newAssignedDate = this.parseDateText("01/02/2018");
		Date lastInvokedDate = this.parseDateText("01/03/2018");
		
		// Action
		taskAssignment = this.hearingAnalysisTaskService.updateTaskAssignment(
				taskAssignment, assigneeAccount, newAssignedDate,
				lastInvokedDate);

		// Assertions
		PropertyValueAsserter.create()
			.addExpectedValue("task", task)
			.addExpectedValue("assigneeAccount", assigneeAccount)
			.addExpectedValue("assignedDate", newAssignedDate)
			.addExpectedValue("lastInvokedDate", lastInvokedDate)
			.performAssertions(taskAssignment);
	}

	/**
	 * Test that {@code DuplicateEntityFoundException} is thrown.
	 * 
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	@Test(expectedExceptions = {DuplicateEntityFoundException.class})
	public void testDuplicateEntityFoundException() 
			throws DuplicateEntityFoundException {
		// Arrangements
		Person user = this.personDelegate.create("Smith", "John", "Jay", null);
		UserAccount sourceAccount = this.userAccountDelegate.create(user, 
				"Username", "password", null, 0, true);
		Task task = this.taskDelegate.create("controllerName", "methodName", 
				"description", sourceAccount, this.parseDateText("01/01/2018"), 
				this.parseDateText("01/02/2018"));
		Person secondUser = this.personDelegate.create("Bob", "Jim", null, 
				null);
		UserAccount assigneeAccount = this.userAccountDelegate.create(
				secondUser, "Username2", "password", null, 0, true);;
		Date assignedDate = this.parseDateText("01/01/2018");
		this.taskAssignmentDelegate.create(task, assignedDate, assigneeAccount);
		Date secondAssignedDate = this.parseDateText("01/02/2018");
		TaskAssignment taskAssignment = this.taskAssignmentDelegate.create(task, 
				secondAssignedDate, assigneeAccount);
		
		// Action
		taskAssignment = this.hearingAnalysisTaskService.updateTaskAssignment(
				taskAssignment, assigneeAccount, assignedDate, new Date());
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