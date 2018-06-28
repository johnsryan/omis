package omis.presentenceinvestigation.service;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskAssociation;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskGroup;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskSource;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.domain.TaskParameterValue;
import omis.task.domain.TaskTemplate;
import omis.task.domain.TaskTemplateGroup;
import omis.task.domain.TaskTemplateParameterValue;
import omis.user.domain.UserAccount;

/**
 * Presentence Investigation Task Service Service.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Jun 11, 2017)
 *@since OMIS 3.0
 *
 */
public interface PresentenceInvestigationTaskService {
	
	/**
	 * Returns a PresentenceInvestigationTaskGroup with the specified
	 * PresentenceInvestigationCategory
	 * @param presentenceInvestigationCategory - PresentenceInvestigationCategory
	 * @return PresentenceInvestigationTaskGroup with the specified
	 * PresentenceInvestigationCategory
	 */
	public PresentenceInvestigationTaskGroup
		findPresentenceInvestigationTaskGroupByPresentenceInvestigationCategory(
			PresentenceInvestigationCategory presentenceInvestigationCategory);
	
	/**
	 * Returns a list of PresentenceInvestigationTaskSources found by specified
	 * TaskTemplateGroup
	 * @param group - TaskTemplateGroup
	 * @return List of PresentenceInvestigationTaskSources found by specified
	 * TaskTemplateGroup
	 */
	public List<PresentenceInvestigationTaskSource>
		findPresentenceInvestigationTaskSourceByTaskTemplateGroup(
				TaskTemplateGroup group);
	
	/**
	 * Returns a list of PresentenceInvestigation TaskTemplateGroups
	 * @return List of PresentenceInvestigation TaskTemplateGroups
	 */
	public List<TaskTemplateGroup> findPresentenceInvestigationTaskGroups();
	
	/**
	 * Returns parameter values by task.
	 * 
	 * @param task task
	 * @return parameter values by task
	 */
	public List<TaskParameterValue> findTaskParameterValuesByTask(Task task);
	
	/**
	 * Returns assignments by task.
	 * 
	 * @param task task
	 * @return assignments by task
	 */
	public List<TaskAssignment> findTaskAssignmentsByTask(Task task);
	
	/**
	 * Returns a PresentenceInvestigationTaskAssociation found with specified
	 * TaskTemplate and PresentenceInvestigationRequest
	 * @param taskTemplate - TaskTemplate
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @return PresentenceInvestigationTaskAssociation found with specified
	 * TaskTemplate and PresentenceInvestigationRequest
	 */
	public Task
		findTaskByTaskTemplateAndPresentenceInvestigationRequest(
			TaskTemplate taskTemplate,
			PresentenceInvestigationRequest presentenceInvestigationRequest);
	
	/**
	 * Returns a list of Presentence Investigation Tasks found by specified
	 * PresentenceInvestigationRequest
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @return List of Presentence Investigation Tasks found by specified
	 * PresentenceInvestigationRequest
	 */
	public List<PresentenceInvestigationTaskAssociation>
		findPresentenceInvestigationTasksByPresentenceInvestigationRequest(
			PresentenceInvestigationRequest presentenceInvestigationRequest);
	
	/**
	 * Returns parameter values by template.
	 * 
	 * @param template template
	 * @return parameter values by template 
	 */
	public List<TaskTemplateParameterValue>
		findTaskTemplateParameterValuesByTaskTemplate(TaskTemplate taskTemplate);
	

	/**
	 * Returns a TaskParameterValue found with specified
	 * TaskTemplateParameterValue and Task
	 * @param taskTemplateParameterValue - TaskTemplateParameterValue
	 * @param task - Task
	 * @return TaskParameterValue found with specified
	 * TaskTemplateParameterValue and Task
	 */
	public TaskParameterValue
		findTaskParameterValueByTaskTemplateParameterValueAndTask(
				TaskTemplateParameterValue templateParameterValue, Task task);
	
	/**
	 * Creates task.
	 * 
	 * @param controllerName controller name
	 * @param methodName method name
	 * @param description description
	 * @param sourceAccount source user account
	 * @param originationDate origination date
	 * @param completionDate completion date
	 * @return created task
	 * @throws DuplicateEntityFoundException if task exists
	 */
	public Task createTask(
			String controllerName, String methodName,
			String description, UserAccount sourceAccount,
			Date originationDate, Date completionDate)
				throws DuplicateEntityFoundException;
	
	/**
	 * Updates task.
	 * 
	 * @param task task
	 * @param controllerName controller name
	 * @param methodName method name
	 * @param description description
	 * @param sourceAccount source user account
	 * @param originationDate origination date
	 * @param completionDate completion date
	 * @return updated task
	 * @throws DuplicateEntityFoundException if task exists
	 */
	public Task updateTask(
			Task task, String controllerName,
			String methodName, String description,
			UserAccount sourceAccount, Date originationDate,
			Date completionDate)
				throws DuplicateEntityFoundException;
	
	public void removeTask(Task task);
	
	/**
	 * Creates task parameter value.
	 * 
	 * @param task task
	 * @param typeName type name
	 * @param instanceValue instance value
	 * @param order order
	 * @return created task parameter value
	 * @throws DuplicateEntityFoundException if task parameter value exists
	 */
	public TaskParameterValue createTaskParameterValue(
			Task task, String typeName, String instanceValue, Short order)
				throws DuplicateEntityFoundException;
	
	/**
	 * Updates task parameter value.
	 * 
	 * @param taskParameterValue task parameter value
	 * @param typeName type name
	 * @param instanceValue instance value
	 * @param order order
	 * @return updated task parameter value
	 * @throws DuplicateEntityFoundException if task parameter value exists
	 */
	public TaskParameterValue updateTaskParameterValue(
			TaskParameterValue taskParameterValue,
			String typeName, String instanceValue, Short order)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes task parameter value.
	 * 
	 * @param taskParameterValue task parameter value to remove
	 */
	public void removeTaskParameterValue(TaskParameterValue taskParameterValue);
	
	/**
	 * Creates task assignment.
	 * 
	 * @param task task
	 * @param assignedDate assigned date
	 * @param assigneeAccount assignee account
	 * @return created task assignment
	 * @throws DuplicateEntityFoundException if task assignment exists
	 */
	public TaskAssignment createTaskAssignment(
			Task task, Date assignedDate,
			UserAccount assigneeAccount)
				throws DuplicateEntityFoundException;
	
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
	public TaskAssignment updateTaskAssignment(
			TaskAssignment taskAssignment, Date assignedDate,
			UserAccount assigneeAccount, Date lastInvokedDate)
				throws DuplicateEntityFoundException;
	
	/**
	 * Removes task assignment.
	 * 
	 * @param taskAssignment task assignment to remove
	 */
	public void removeTaskAssignment(TaskAssignment taskAssignment);
	
	/**
	 * Creates a PresentenceInvestigationTaskAssociation with the specified
	 * properties
	 * @param task - Task
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @param taskSource - PresentenceInvestigationTaskSource
	 * @return Newly created PresentenceInvestigationTaskAssociation
	 * @throws DuplicateEntityFoundException - When a
	 * PresentenceInvestigationTaskAssociation already exists with the specified
	 * properties
	 */
	public PresentenceInvestigationTaskAssociation
		createPresentenceInvestigationTaskAssociation(Task task,
			PresentenceInvestigationRequest presentenceInvestigationRequest,
			PresentenceInvestigationTaskSource taskSource)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a PresentenceInvestigationTaskAssociation with the specified
	 * properties
	 * @param presentenceInvestigationTaskAssociation -
	 * PresentenceInvestigationTaskAssociation to update
	 * @param task - Task
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @param taskSource - PresentenceInvestigationTaskSource
	 * @return Updated PresentenceInvestigationTaskAssociation
	 * @throws DuplicateEntityFoundException - When another
	 * PresentenceInvestigationTaskAssociation already exists with the specified
	 * properties
	 */
	public PresentenceInvestigationTaskAssociation
		updatePresentenceInvestigationTaskAssociation(
			PresentenceInvestigationTaskAssociation
				presentenceInvestigationTaskAssociation, Task task,
				PresentenceInvestigationRequest presentenceInvestigationRequest,
				PresentenceInvestigationTaskSource taskSource)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified PresentenceInvestigationTaskAssociation
	 * @param presentenceInvestigationTaskAssociation -
	 * PresentenceInvestigationTaskAssociation to remove
	 */
	public void removePresentenceInvestigationTaskAssociation(
			PresentenceInvestigationTaskAssociation
				presentenceInvestigationTaskAssociation);

}
