package omis.presentenceinvestigation.service.impl;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskAssociation;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskGroup;
import omis.presentenceinvestigation.domain.PresentenceInvestigationTaskSource;
import omis.presentenceinvestigation.service.PresentenceInvestigationTaskService;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationTaskAssociationDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationTaskGroupDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationTaskSourceDelegate;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.domain.TaskParameterValue;
import omis.task.domain.TaskTemplate;
import omis.task.domain.TaskTemplateGroup;
import omis.task.domain.TaskTemplateParameterValue;
import omis.task.service.delegate.TaskAssignmentDelegate;
import omis.task.service.delegate.TaskDelegate;
import omis.task.service.delegate.TaskParameterValueDelegate;
import omis.task.service.delegate.TaskTemplateParameterValueDelegate;
import omis.user.domain.UserAccount;

/**
 * Presentence Investigation Task Service Implementation.
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Jun 11, 2017)
 *@since OMIS 3.0
 *
 */
public class PresentenceInvestigationTaskServiceImpl
		implements PresentenceInvestigationTaskService {

	private final PresentenceInvestigationTaskSourceDelegate
		presentenceInvestigationTaskSourceDelegate;
	
	private final PresentenceInvestigationTaskAssociationDelegate
		presentenceInvestigationTaskAssociationDelegate;
	
	private final PresentenceInvestigationTaskGroupDelegate
		presentenceInvestigationTaskGroupDelegate;
	
	private final TaskDelegate taskDelegate;
	
	private final TaskAssignmentDelegate taskAssignmentDelegate;
	
	private final TaskParameterValueDelegate taskParameterValueDelegate;
	
	private final TaskTemplateParameterValueDelegate
		taskTemplateParameterValueDelegate;
	
	/**
	 * @param presentenceInvestigationTaskGroupAssociationDelegate
	 * @param presentenceInvestigationTaskAssociationDelegate
	 * @param presentenceInvestigationTaskGroupDelegate
	 * @param taskDelegate
	 * @param taskAssignmentDelegate
	 * @param taskParameterValueDelegate
	 * @param taskTemplateDelegate
	 */
	public PresentenceInvestigationTaskServiceImpl(
			final PresentenceInvestigationTaskSourceDelegate
				presentenceInvestigationTaskSourceDelegate,
			final PresentenceInvestigationTaskAssociationDelegate
				presentenceInvestigationTaskAssociationDelegate,
			final PresentenceInvestigationTaskGroupDelegate
				presentenceInvestigationTaskGroupDelegate,
			final TaskDelegate taskDelegate,
			final TaskAssignmentDelegate taskAssignmentDelegate,
			final TaskParameterValueDelegate taskParameterValueDelegate,
			 final TaskTemplateParameterValueDelegate
				taskTemplateParameterValueDelegate) {
		this.presentenceInvestigationTaskSourceDelegate =
				presentenceInvestigationTaskSourceDelegate;
		this.presentenceInvestigationTaskAssociationDelegate =
				presentenceInvestigationTaskAssociationDelegate;
		this.presentenceInvestigationTaskGroupDelegate =
				presentenceInvestigationTaskGroupDelegate;
		this.taskDelegate = taskDelegate;
		this.taskAssignmentDelegate = taskAssignmentDelegate;
		this.taskParameterValueDelegate = taskParameterValueDelegate;
		this.taskTemplateParameterValueDelegate =
				taskTemplateParameterValueDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public List<TaskTemplateGroup> findPresentenceInvestigationTaskGroups() {
		return this.presentenceInvestigationTaskGroupDelegate
				.findPresentenceInvestigationTaskGroups();
	}

	/**{@inheritDoc} */
	@Override
	public List<TaskParameterValue> findTaskParameterValuesByTask(
			final Task task) {
		return this.taskParameterValueDelegate.findByTask(task);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<TaskAssignment> findTaskAssignmentsByTask(final Task task) {
		return this.taskAssignmentDelegate.findByTask(task);
	}

	/**{@inheritDoc} */
	@Override
	public List<TaskTemplateParameterValue>
		findTaskTemplateParameterValuesByTaskTemplate(
				final TaskTemplate taskTemplate) {
		return this.taskTemplateParameterValueDelegate.findByTemplate(
				taskTemplate);
	}
	
	/**{@inheritDoc} */
	@Override
	public TaskParameterValue
		findTaskParameterValueByTaskTemplateParameterValueAndTask(
				final TaskTemplateParameterValue templateParameterValue,
				final Task task) {
		return this.taskParameterValueDelegate
				.findByTaskTemplateParameterValueAndTask(
						templateParameterValue, task);
	}
	
	/**{@inheritDoc} */
	@Override
	public PresentenceInvestigationTaskGroup
		findPresentenceInvestigationTaskGroupByPresentenceInvestigationCategory(
				PresentenceInvestigationCategory presentenceInvestigationCategory) {
		return this.presentenceInvestigationTaskGroupDelegate
				.findByPresentenceInvestigationCategory(
						presentenceInvestigationCategory);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<PresentenceInvestigationTaskAssociation>
		findPresentenceInvestigationTasksByPresentenceInvestigationRequest(
				PresentenceInvestigationRequest presentenceInvestigationRequest) {
		return this.presentenceInvestigationTaskAssociationDelegate
				.findTasksByPresentenceInvestigationRequest(
						presentenceInvestigationRequest);
	}
	
	/**{@inheritDoc} */
	@Override
	public List<PresentenceInvestigationTaskSource>
		findPresentenceInvestigationTaskSourceByTaskTemplateGroup(
				TaskTemplateGroup group) {
		return this.presentenceInvestigationTaskSourceDelegate
				.findByTaskTemplateGroup(group);
	}
	
	/**{@inheritDoc} */
	@Override
	public Task
		findTaskByTaskTemplateAndPresentenceInvestigationRequest(
				TaskTemplate taskTemplate,
				PresentenceInvestigationRequest presentenceInvestigationRequest) {
		return this.presentenceInvestigationTaskAssociationDelegate
				.findTaskByTaskTemplateAndPresentenceInvestigationRequest(
						taskTemplate, presentenceInvestigationRequest);
	}
	
	/**{@inheritDoc} */
	@Override
	public Task createTask(final String controllerName,
			final String methodName,
			final String description,
			final UserAccount sourceAccount,
			final Date originationDate,
			final Date completionDate) throws DuplicateEntityFoundException {
		return this.taskDelegate.create(controllerName, methodName, description,
				sourceAccount, originationDate, completionDate);
	}

	/**{@inheritDoc} */
	@Override
	public Task updateTask(final Task task,
			final String controllerName,
			final String methodName,
			final String description,
			final UserAccount sourceAccount,
			final Date originationDate,
			final Date completionDate) throws DuplicateEntityFoundException {
		return this.taskDelegate.update(task, controllerName, methodName,
				description, sourceAccount, originationDate, completionDate);
	}

	/**{@inheritDoc} */
	@Override
	public void removeTask(final Task task) {
		this.taskDelegate.remove(task);
	}

	/**{@inheritDoc} */
	@Override
	public TaskParameterValue createTaskParameterValue(final Task task,
			final String typeName, final String instanceValue, 
			final Short order) throws DuplicateEntityFoundException {
		return this.taskParameterValueDelegate.create(task, typeName, 
				instanceValue, order);
	}

	/**{@inheritDoc} */
	@Override
	public TaskParameterValue updateTaskParameterValue(
			final TaskParameterValue taskParameterValue,
			final String typeName, final String instanceValue, final Short order)
					throws DuplicateEntityFoundException {
		return this.taskParameterValueDelegate.update(taskParameterValue, 
				typeName, instanceValue, order);
	}

	/**{@inheritDoc} */
	@Override
	public void
		removeTaskParameterValue(final TaskParameterValue taskParameterValue) {
		this.taskParameterValueDelegate.remove(taskParameterValue);
	}

	/**{@inheritDoc} */
	@Override
	public TaskAssignment createTaskAssignment(final Task task,
			final Date assignedDate, final UserAccount assigneeAccount)
					throws DuplicateEntityFoundException {
		return this.taskAssignmentDelegate.create(
				task, assignedDate, assigneeAccount);
	}

	/**{@inheritDoc} */
	@Override
	public TaskAssignment updateTaskAssignment(
			final TaskAssignment taskAssignment,
			final Date assignedDate, final UserAccount assigneeAccount,
			final Date lastInvokedDate)
					throws DuplicateEntityFoundException {
		return this.taskAssignmentDelegate.update(
				taskAssignment, assignedDate, assigneeAccount, lastInvokedDate);
	}

	/**{@inheritDoc} */
	@Override
	public void removeTaskAssignment(final TaskAssignment taskAssignment) {
		this.taskAssignmentDelegate.remove(taskAssignment);
	}
	
	/**{@inheritDoc} */
	@Override
	public PresentenceInvestigationTaskAssociation
		createPresentenceInvestigationTaskAssociation(final Task task,
				final PresentenceInvestigationRequest
					presentenceInvestigationRequest,
				final PresentenceInvestigationTaskSource taskSource)
						throws DuplicateEntityFoundException {
		return this.presentenceInvestigationTaskAssociationDelegate.create(
				task, presentenceInvestigationRequest, taskSource);
	}

	/**{@inheritDoc} */
	@Override
	public PresentenceInvestigationTaskAssociation
		updatePresentenceInvestigationTaskAssociation(
				final PresentenceInvestigationTaskAssociation
					presentenceInvestigationTaskAssociation,
				final Task task,
				final PresentenceInvestigationRequest
					presentenceInvestigationRequest,
				final PresentenceInvestigationTaskSource taskSource)
						throws DuplicateEntityFoundException {
		return this.presentenceInvestigationTaskAssociationDelegate.update(
				presentenceInvestigationTaskAssociation, task,
				presentenceInvestigationRequest, taskSource);
	}

	/**{@inheritDoc} */
	@Override
	public void removePresentenceInvestigationTaskAssociation(
			final PresentenceInvestigationTaskAssociation
				presentenceInvestigationTaskAssociation) {
		this.presentenceInvestigationTaskAssociationDelegate.remove(
				presentenceInvestigationTaskAssociation);
	}
}
