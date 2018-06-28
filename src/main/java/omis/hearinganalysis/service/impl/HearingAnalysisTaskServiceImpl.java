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
package omis.hearinganalysis.service.impl;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.domain.HearingAnalysisNote;
import omis.hearinganalysis.domain.HearingAnalysisTaskAssociation;
import omis.hearinganalysis.domain.ParoleHearingAnalysisTaskSource;
import omis.hearinganalysis.domain.ParoleHearingTaskGroup;
import omis.hearinganalysis.service.HearingAnalysisTaskService;
import omis.hearinganalysis.service.delegate.HearingAnalysisNoteDelegate;
import omis.hearinganalysis.service.delegate.HearingAnalysisTaskAssociationDelegate;
import omis.hearinganalysis.service.delegate.ParoleHearingAnalysisTaskSourceDelegate;
import omis.hearinganalysis.service.delegate.ParoleHearingTaskGroupDelegate;
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
import omis.user.service.delegate.UserAccountDelegate;

/**
 * Implementation of service for hearing analysis task.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public class HearingAnalysisTaskServiceImpl 
		implements HearingAnalysisTaskService {

	/* Delegates. */
	
	private final TaskDelegate taskDelegate;
	
	private final TaskParameterValueDelegate taskParameterValueDelegate;
	
	private final TaskAssignmentDelegate taskAssignmentDelegate;
	
	private final TaskTemplateParameterValueDelegate 
			taskTemplateParameterValueDelegate;
	
	private final HearingAnalysisTaskAssociationDelegate
			hearingAnalysisTaskAssociationDelegate;
	
	private final ParoleHearingAnalysisTaskSourceDelegate
			paroleHearingAnalysisTaskSourceDelegate;
	
	private final ParoleHearingTaskGroupDelegate paroleHearingTaskGroupDelegate;
	
	private final UserAccountDelegate userAccountDelegate;
	
	private final HearingAnalysisNoteDelegate hearingAnalysisNoteDelegate;
	
	/**
	 * Instantiates a hearing analysis task service implementation with the 
	 * specified delegates.
	 * 
	 * @param taskDelegate task delegate
	 * @param taskParameterValueDelegate task parameter value delegate
	 * @param taskAssignmentDelegate task assignment delegate
	 * @param taskTemplateParameterValueDelegate task template parameter value 
	 * delegate
	 * @param hearingAnalysisTaskAssociationDelegate hearing analysis task 
	 * association delegate
	 * @param paroleHearingAnalysisTaskSourceDelegate parole hearing analysis 
	 * task source delegate
	 * @param paroleHearingTaskGroupDelegate parole hearing task group delegate
	 * @param userAccountDelegate user account delegate
	 * @param hearingAnalysisNoteDelegate hearing analysis note delegate
	 */
	public HearingAnalysisTaskServiceImpl(final TaskDelegate taskDelegate,
			final TaskParameterValueDelegate taskParameterValueDelegate,
			final TaskAssignmentDelegate taskAssignmentDelegate,
			final TaskTemplateParameterValueDelegate 
					taskTemplateParameterValueDelegate,
			final HearingAnalysisTaskAssociationDelegate
					hearingAnalysisTaskAssociationDelegate,
			final ParoleHearingAnalysisTaskSourceDelegate
					paroleHearingAnalysisTaskSourceDelegate,
			final ParoleHearingTaskGroupDelegate paroleHearingTaskGroupDelegate,
			final UserAccountDelegate userAccountDelegate,
			final HearingAnalysisNoteDelegate hearingAnalysisNoteDelegate) {
		this.taskDelegate = taskDelegate;
		this.taskParameterValueDelegate = taskParameterValueDelegate;
		this.taskAssignmentDelegate = taskAssignmentDelegate;
		this.taskTemplateParameterValueDelegate = 
				taskTemplateParameterValueDelegate;
		this.hearingAnalysisTaskAssociationDelegate = 
				hearingAnalysisTaskAssociationDelegate;
		this.paroleHearingAnalysisTaskSourceDelegate = 
				paroleHearingAnalysisTaskSourceDelegate;
		this.paroleHearingTaskGroupDelegate = paroleHearingTaskGroupDelegate;
		this.userAccountDelegate = userAccountDelegate;
		this.hearingAnalysisNoteDelegate = hearingAnalysisNoteDelegate;
	}
	
	/** {@inheritDoc} */
	@Override
	public Task createTask(final String controllerName, final String methodName, 
			final String description, final UserAccount sourceAccountUser,
			final Date originationDate, final Date completionDate) 
					throws DuplicateEntityFoundException {
		return this.taskDelegate.create(controllerName, methodName, description, 
				sourceAccountUser, originationDate, completionDate);
	}

	/** {@inheritDoc} */
	@Override
	public Task updateTask(final Task task, final String controllerName, 
			final String methodName, final String description,
			final UserAccount sourceAccountUser, final Date originationDate, 
			final Date completionDate) throws DuplicateEntityFoundException {
		return this.taskDelegate.update(task, controllerName, methodName, 
				description, sourceAccountUser, originationDate, completionDate);
	}

	/** {@inheritDoc} */
	@Override
	public void removeTask(final Task task) {
		this.taskDelegate.remove(task);
	}

	/** {@inheritDoc} */
	@Override
	public TaskParameterValue createTaskParameterValue(final Task task, 
			final Short order, final String typeName, 
			final String instanceValue) 
					throws DuplicateEntityFoundException {
		return this.taskParameterValueDelegate.create(task, typeName, 
				instanceValue, order);
	}

	/** {@inheritDoc} */
	@Override
	public TaskAssignment createTaskAssignment(final Task task, 
			final UserAccount assigneeAccount, final Date assignedDate) 
					throws DuplicateEntityFoundException {
		return this.taskAssignmentDelegate.create(task, assignedDate, 
				assigneeAccount);
	}

	/** {@inheritDoc} */
	@Override
	public TaskAssignment updateTaskAssignment(
			final TaskAssignment taskAssignment, 
			final UserAccount assigneeAccount, final Date assignedDate,
			final Date lastInvokedDate) 
					throws DuplicateEntityFoundException {
		return this.taskAssignmentDelegate.update(taskAssignment, assignedDate, 
				assigneeAccount, lastInvokedDate);
	}

	/** {@inheritDoc} */
	@Override
	public void removeTaskAssignment(final TaskAssignment taskAssignment) {
		this.taskAssignmentDelegate.remove(taskAssignment);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisTaskAssociation createHearingAnalysisTaskAssociation(
			final Task task, final HearingAnalysis hearingAnalysis, 
			final ParoleHearingAnalysisTaskSource taskSource) 
					throws DuplicateEntityFoundException {
		return this.hearingAnalysisTaskAssociationDelegate.create(task, 
				hearingAnalysis, taskSource);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisTaskAssociation updateHearingAnalysisTaskAssociation(
			final HearingAnalysisTaskAssociation hearingAnalysisTaskAssociation,
			final Task task, final HearingAnalysis hearingAnalysis) 
					throws DuplicateEntityFoundException {
		return this.hearingAnalysisTaskAssociationDelegate.update(
				hearingAnalysisTaskAssociation, task, hearingAnalysis);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearingAnalysisTaskAssociation(
			final HearingAnalysisTaskAssociation hearingAnalysisTaskAssociation) {
		this.hearingAnalysisTaskAssociationDelegate.remove(
				hearingAnalysisTaskAssociation);
	}

	/** {@inheritDoc} */
	@Override
	public List<TaskTemplateParameterValue> 
			findTaskTemplateParameterValuesByTaskTemplate(
					final TaskTemplate taskTemplate) {
		return this.taskTemplateParameterValueDelegate.findByTemplate(
				taskTemplate);
	}

	/** {@inheritDoc} */
	@Override
	public List<ParoleHearingAnalysisTaskSource> 
			findParoleHearingTaskSourcesByTaskTemplateGroup(
					final TaskTemplateGroup taskTemplateGroup) {
		return this.paroleHearingAnalysisTaskSourceDelegate
				.findByTaskTemplateGroup(taskTemplateGroup);
	}

	/** {@inheritDoc} */
	@Override
	public List<TaskParameterValue> findTaskParameterValuesByTask(
			final Task task) {
		return this.taskParameterValueDelegate.findByTask(task);
	}

	/** {@inheritDoc} */
	@Override
	public List<TaskAssignment> findTaskAssignmentsByTask(final Task task) {
		return this.taskAssignmentDelegate.findByTask(task);
	}

	/** {@inheritDoc} */
	@Override
	public Task findTaskByTaskTemplateAndHearingAnalysis(
			final TaskTemplate taskTemplate, 
			final HearingAnalysis hearingAnalysis) {
		return this.hearingAnalysisTaskAssociationDelegate
				.findTaskByTaskTemplateAndHearingAnalysis(taskTemplate, 
						hearingAnalysis);
	}

	/** {@inheritDoc} */
	@Override
	public List<HearingAnalysisTaskAssociation> 
			findHearingAnalysisTasksByHearingAnalysis(
					final HearingAnalysis hearingAnalysis) {
		return this.hearingAnalysisTaskAssociationDelegate
				.findByHearingAnalysis(hearingAnalysis);
	}

	/** {@inheritDoc} */
	@Override
	public TaskParameterValue 
			findTaskParameterValueByTaskTemplateParameterValueAndTask(
					final TaskTemplateParameterValue taskTemplateParameterValue, 
					final Task task) {
		return this.taskParameterValueDelegate
				.findByTaskTemplateParameterValueAndTask(
						taskTemplateParameterValue, task);
	}

	/** {@inheritDoc} */
	@Override
	public ParoleHearingTaskGroup 
			findParoleHearingTaskGroupByHearingAnalysisCategory(
					final HearingAnalysisCategory hearingAnalysisCategory) {
		return this.paroleHearingTaskGroupDelegate
				.findByHearingAnalysisCategory(hearingAnalysisCategory);
	}

	/** {@inheritDoc} */
	@Override
	public UserAccount findUserAccountByUsername(final String username) {
		return this.userAccountDelegate.findByUsername(username);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisNote createHearingAnalysisNote(
			final HearingAnalysis hearingAnalysis, final String description, 
			final Date date) throws DuplicateEntityFoundException {
		return this.hearingAnalysisNoteDelegate.create(hearingAnalysis, 
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public HearingAnalysisNote updateHearingAnalysisNote(
			final HearingAnalysisNote hearingAnalysisNote, 
			final String description, final Date date) 
					throws DuplicateEntityFoundException {
		return this.hearingAnalysisNoteDelegate.update(hearingAnalysisNote, 
				description, date);
	}

	/** {@inheritDoc} */
	@Override
	public void removeHearingAnalysisNote(
			final HearingAnalysisNote hearingAnalysisNote) {
		this.hearingAnalysisNoteDelegate.remove(hearingAnalysisNote);
	}

	/** {@inheritDoc} */
	@Override
	public List<HearingAnalysisNote> findHearingAnalysisNotesByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		return this.hearingAnalysisNoteDelegate.findByHearingAnalysis(
				hearingAnalysis);
	}
}