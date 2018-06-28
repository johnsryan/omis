package omis.task.domain;

import java.io.Serializable;
import java.util.Date;

import omis.user.domain.UserAccount;

/**
 * Task assignment.
 *
 * @author Stephen Abson
 * @author Annie Wahl
 * @version 0.0.2
 * @since OMIS 3.0
 */
public interface TaskAssignment
		extends Serializable {

	/**
	 * Sets ID.
	 * 
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Returns ID.
	 * 
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets task.
	 * 
	 * @param task task
	 */
	void setTask(Task task);
	
	/**
	 * Returns task.
	 * 
	 * @return task
	 */
	Task getTask();
	
	/**
	 * Sets account of assignee.
	 * 
	 * @param assigneeAccount account of assignee
	 */
	void setAssigneeAccount(UserAccount assigneeAccount);
	
	/**
	 * Returns account of assignee.
	 * 
	 * @return account of assignee
	 */
	UserAccount getAssigneeAccount();
	
	/**
	 * Sets assignee date.
	 * 
	 * @param assignedDate assigned date
	 */
	void setAssignedDate(Date assignedDate);
	
	/**
	 * Return assigned date.
	 * 
	 * @return assigned date
	 */
	Date getAssignedDate();
	
	/**
	 * Returns the LastInvokedDate for the Task Assignment.
	 * @return lastInvokedDate - last invoked date
	 */
	Date getLastInvokedDate();
	
	/**
	 * Sets the LastInvokedDate for the Task Assignment.
	 * @param lastInvokedDate - last invoked date
	 */
	void setLastInvokedDate(Date lastInvokedDate);
	
	/**
	 * Compares {@code this} and {@code obj} for equality.
	 * 
	 * <p>Any mandatory property may be used in the comparison. If a  mandatory
	 * property of {@code this} that is used in the comparison is {@code null}
	 * an {@code IllegalStateException} will be thrown.
	 * 
	 * @param obj reference object with which to compare {@code this}
	 * @return {@code true} if {@code this} and {@code obj} are equal;
	 * {@code false} otherwise
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the comparison is {@code null} 
	 */
	@Override
	boolean equals(Object obj);
	
	/**
	 * Returns a hash code for {@code this}.
	 * 
	 * <p>Any mandatory property of {@code this} may be used in the hash code.
	 * If a mandatory property that is used in the hash code is {@code null} an
	 * {@code IllegalStateException} will be thrown.
	 * 
	 * @return hash code
	 * @throws IllegalStateException if a mandatory property of {@code this}
	 * that is used in the hash code is {@code null}
	 */
	@Override
	int hashCode();
}