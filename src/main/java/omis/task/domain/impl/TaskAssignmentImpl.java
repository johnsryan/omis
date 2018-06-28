package omis.task.domain.impl;

import java.util.Date;

import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.user.domain.UserAccount;

/**
 * Implementation of task assignment.
 *
 * @author Stephen Abson
 * @author Annie Wahl
 * @version 0.0.2
 * @since OMIS 3.0
 */
public class TaskAssignmentImpl
		implements TaskAssignment {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Task task;
	
	private UserAccount assigneeAccount;
	
	private Date assignedDate;
	
	private Date lastInvokedDate;
	
	/** Instantiates implementation of task assignment. */
	public TaskAssignmentImpl() {
		// Default instantiation
	}
	
	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return this.id;
	}

	/** {@inheritDoc} */
	@Override
	public void setTask(final Task task) {
		this.task = task;
	}

	/** {@inheritDoc} */
	@Override
	public Task getTask() {
		return this.task;
	}

	/** {@inheritDoc} */
	@Override
	public void setAssigneeAccount(final UserAccount assigneeAccount) {
		this.assigneeAccount = assigneeAccount;
	}

	/** {@inheritDoc} */
	@Override
	public UserAccount getAssigneeAccount() {
		return this.assigneeAccount;
	}

	/** {@inheritDoc} */
	@Override
	public void setAssignedDate(final Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	/** {@inheritDoc} */
	@Override
	public Date getAssignedDate() {
		return this.assignedDate;
	}
	
	/**{@inheritDoc} */
	@Override
	public Date getLastInvokedDate() {
		return this.lastInvokedDate;
	}
	
	/**{@inheritDoc} */
	@Override
	public void setLastInvokedDate(final Date lastInvokedDate) {
		this.lastInvokedDate = lastInvokedDate;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TaskAssignment)) {
			return false;
		}
		TaskAssignment that = (TaskAssignment) obj;
		if (this.getTask() == null) {
			throw new IllegalStateException("Task required");
		}
		if (!this.getTask().equals(that.getTask())) {
			return false;
		}
		if (this.getAssigneeAccount() == null) {
			throw new IllegalStateException("Assignee account required");
		}
		if (!this.getAssigneeAccount().equals(that.getAssigneeAccount())) {
			return false;
		}
		if (this.getAssignedDate() == null) {
			throw new IllegalStateException("Assigned date required");
		}
		if (!this.getAssignedDate().equals(that.getAssignedDate())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int hashCode = 14;
		if (this.getTask() == null) {
			throw new IllegalStateException("Task required");
		}
		if (this.getAssigneeAccount() == null) {
			throw new IllegalStateException("Assignee account required");
		}
		if (this.getAssignedDate() == null) {
			throw new IllegalStateException("Assigned date required");
		}
		hashCode = 29 * hashCode + this.getTask().hashCode();
		hashCode = 29 * hashCode + this.getAssigneeAccount().hashCode();
		hashCode = 29 * hashCode + this.getAssignedDate().hashCode();
		return hashCode;
	}
}