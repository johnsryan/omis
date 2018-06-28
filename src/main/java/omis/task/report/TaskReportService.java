package omis.task.report;

import java.util.List;

import omis.user.domain.UserAccount;

/**
 * Report service for tasks.
 *
 * @author Stephen Abson
 * @version 0.0.1
 * @since OMIS 3.0
 */
public interface TaskReportService {

	/**
	 * Summarizes tasks by source account username.
	 * 
	 * @param username source account username
	 * @return summaries of tasks by source account username
	 */
	List<TaskSummary> summarizeBySourceAccountUsername(String username);
	
	/**
	 * @param assigneeAccount
	 * @return
	 */
	Long findUninvokedTaskCountByAssigneeUserAccount(
			UserAccount assigneeAccount);
}