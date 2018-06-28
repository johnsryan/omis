package omis.task.service.delegate;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.instance.factory.InstanceFactory;
import omis.task.dao.TaskAssignmentDao;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.user.domain.UserAccount;

/**
 * Delegate for task assignments.
 *
 * @author Stephen Abson
 * @author Annie Wahl
 * @version 0.0.2
 * @since OMIS 3.0
 */
public class TaskAssignmentDelegate {

	/* Data access objects. */
	
	private final InstanceFactory<TaskAssignment> taskAssignmentInstanceFactory;
	
	private final TaskAssignmentDao taskAssignmentDao;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for task assignments.
	 * 
	 * @param taskAssignmentInstanceFactory task assignment instance factory
	 * @param taskAssignmentDao data access object for task assignments
	 */
	public TaskAssignmentDelegate(
			final InstanceFactory<TaskAssignment> taskAssignmentInstanceFactory,
			final TaskAssignmentDao taskAssignmentDao) {
		this.taskAssignmentInstanceFactory = taskAssignmentInstanceFactory;
		this.taskAssignmentDao = taskAssignmentDao;
	}
	
	/* Methods. */
	
	/**
	 * Returns assignments by task.
	 * 
	 * @param task task
	 * @return assignments by task
	 */
	public List<TaskAssignment> findByTask(final Task task) {
		return this.taskAssignmentDao.findByTask(task);
	}
	
	/**
	 * Creates task assignment.
	 * 
	 * @param task task
	 * @param assignedDate assigned date
	 * @param assigneeAccount assignee account
	 * @return created task assignment
	 * @throws DuplicateEntityFoundException if task assignment exists
	 */
	public TaskAssignment create(
			final Task task,
			final Date assignedDate,
			final UserAccount assigneeAccount)
				throws DuplicateEntityFoundException {
		if (this.taskAssignmentDao.find(
				task, assigneeAccount, assignedDate) != null) {
			throw new DuplicateEntityFoundException("Task assignment exists");
		}
		TaskAssignment taskAssignment = this.taskAssignmentInstanceFactory
				.createInstance();
		taskAssignment.setTask(task);
		this.populate(taskAssignment, assignedDate, assigneeAccount);
		return this.taskAssignmentDao.makePersistent(taskAssignment);
	}
	
	/**
	 * Updates task assignment.
	 * 
	 * @param taskAssignment task assignment to update
	 * @param assignedDate assigned date
	 * @param assigneeAccount assignee account
	 * @param lastInvokedDate last invoked date
	 * @return updated task assignment
	 * @throws DuplicateEntityFoundException if task assignment exists
	 */
	public TaskAssignment update(
			final TaskAssignment taskAssignment,
			final Date assignedDate,
			final UserAccount assigneeAccount,
			final Date lastInvokedDate)
				throws DuplicateEntityFoundException {
		if (this.taskAssignmentDao.findExcluding(
				taskAssignment.getTask(), assigneeAccount, assignedDate,
				taskAssignment) != null) {
			throw new DuplicateEntityFoundException("Task assignment exists");
		}
		taskAssignment.setLastInvokedDate(lastInvokedDate);
		this.populate(taskAssignment, assignedDate, assigneeAccount);
		return this.taskAssignmentDao.makePersistent(taskAssignment);
	}
	
	/**
	 * Removes task assignment.
	 * 
	 * @param taskAssignment task assignment to remove
	 */
	public void remove(final TaskAssignment taskAssignment) {
		this.taskAssignmentDao.makeTransient(taskAssignment);
	}
	
	/* Helper methods. */
	
	// Populates task assignment
	private void populate(
			final TaskAssignment taskAssignment,
			final Date assignedDate,
			final UserAccount assigneeAccount) {
		taskAssignment.setAssignedDate(assignedDate);
		taskAssignment.setAssigneeAccount(assigneeAccount);
	}
}