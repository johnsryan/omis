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
package omis.hearinganalysis.service;

import java.util.Date;
import java.util.List;

import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.hearinganalysis.domain.HearingAnalysisCategory;
import omis.hearinganalysis.domain.HearingAnalysisNote;
import omis.hearinganalysis.domain.HearingAnalysisTaskAssociation;
import omis.hearinganalysis.domain.ParoleHearingAnalysisTaskSource;
import omis.hearinganalysis.domain.ParoleHearingTaskGroup;
import omis.task.domain.Task;
import omis.task.domain.TaskAssignment;
import omis.task.domain.TaskParameterValue;
import omis.task.domain.TaskTemplate;
import omis.task.domain.TaskTemplateGroup;
import omis.task.domain.TaskTemplateParameterValue;
import omis.user.domain.UserAccount;

/**
 * Service for hearing analysis task.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 20, 2018)
 * @since OMIS 3.0
 */
public interface HearingAnalysisTaskService {

	/**
	 * Creates a new task.
	 * 
	 * @param controllerName controller name
	 * @param methodName method name
	 * @param description description
	 * @param sourceAccountUser source account user
	 * @param originationDate origination date
	 * @param completionDate completion date
	 * @return task
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	Task createTask(String controllerName, String methodName, 
			String description, UserAccount sourceAccountUser, 
			Date originationDate, Date completionDate) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing task.
	 * 
	 * @param task task
	 * @param controllerName controller name
	 * @param methodName method name
	 * @param description description
	 * @param sourceAccountUser source account user
	 * @param originationDate origination date
	 * @param completionDate completion date
	 * @return task
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	Task updateTask(Task task, String controllerName, String methodName, 
			String description, UserAccount sourceAccountUser, 
			Date originationDate, Date completionDate) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes an existing task.
	 * 
	 * @param task task
	 */
	void removeTask(Task task);
	
	/**
	 * Creates a new task parameter value.
	 * 
	 * @param task task
	 * @param order order
	 * @param typeName entity name
	 * @param instanceValue instance value
	 * @return task parameter value
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	TaskParameterValue createTaskParameterValue(Task task, Short order, 
			String typeName, String instanceValue) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Creates a new task assignment.
	 * 
	 * @param task task
	 * @param assigneeAccount assignee user account
	 * @param assignedDate assigned date
	 * @return task assignment
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	TaskAssignment createTaskAssignment(Task task, UserAccount assigneeAccount,
			Date assignedDate) throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing task assignment.
	 * 
	 * @param taskAssignment task assignment
	 * @param assigneeAccount assignee user account
	 * @param assignedDate assigned date
	 * @param lastInvokedDate last invoked date
	 * @return task assignment
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	TaskAssignment updateTaskAssignment(TaskAssignment taskAssignment,
			UserAccount assigneeAccount, Date assignedDate,
			Date lastInvokedDate)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes an existing task assignment.
	 * 
	 * @param taskAssignment task assignment
	 */
	void removeTaskAssignment(TaskAssignment taskAssignment);
	
	/**
	 * Creates a new hearing analysis task association.
	 * 
	 * @param task task
	 * @param hearingAnalysis hearing analysis
	 * @param taskSource parole hearing analysis task source
	 * @return hearing analysis task association
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysisTaskAssociation createHearingAnalysisTaskAssociation(
			Task task, HearingAnalysis hearingAnalysis, 
			ParoleHearingAnalysisTaskSource taskSource) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing hearing analysis task association.
	 * 
	 * @param hearingAnalysisTaskAssociation hearing analysis task association
	 * @param task task
	 * @param hearingAnalysis hearing analysis
	 * @param taskSource parole hearing analysis task source
	 * @return hearing analysis task association
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysisTaskAssociation updateHearingAnalysisTaskAssociation(
			HearingAnalysisTaskAssociation hearingAnalysisTaskAssociation, 
			Task task, HearingAnalysis hearingAnalysis) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes an existing hearing analysis task association.
	 * 
	 * @param hearingAnalysisTaskAssociation hearing analysis task association
	 */
	void removeHearingAnalysisTaskAssociation(
			HearingAnalysisTaskAssociation hearingAnalysisTaskAssociation);
	
	/**
	 * Returns a list of task template parameter values by the specified task 
	 * template.
	 * 
	 * @param taskTemplate task template
	 * @return list of task template parameter values
	 */
	List<TaskTemplateParameterValue> 
			findTaskTemplateParameterValuesByTaskTemplate(
					TaskTemplate taskTemplate);
	
	/**
	 * Returns a list of parole hearing analysis task sources for the specified 
	 * task template group.
	 * 
	 * @param taskTemplateGroup task template group
	 * @return list of parole hearing analysis task sources
	 */
	List<ParoleHearingAnalysisTaskSource> 
			findParoleHearingTaskSourcesByTaskTemplateGroup(
					TaskTemplateGroup taskTemplateGroup);
	
	/**
	 * Returns a list of task parameter values for the specified task.
	 * 
	 * @param task task
	 * @return list of task parameter values
	 */
	List<TaskParameterValue> findTaskParameterValuesByTask(Task task);
	
	/**
	 * Returns a list of task assignments for the specified task.
	 * 
	 * @param task task
	 * @return list of task assignments
	 */
	List<TaskAssignment> findTaskAssignmentsByTask(Task task);
	
	/**
	 * Returns the task for the specified task template and hearing analysis.
	 * 
	 * @param taskTemplate task template
	 * @param hearingAnalysis hearing analysis
	 * @return task
	 */
	Task findTaskByTaskTemplateAndHearingAnalysis(TaskTemplate taskTemplate,
			HearingAnalysis hearingAnalysis);
	
	/**
	 * Returns a list of hearing analysis task associations for the specified 
	 * hearing analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of hearing analysis task associations
	 */
	List<HearingAnalysisTaskAssociation> 
			findHearingAnalysisTasksByHearingAnalysis(
					HearingAnalysis hearingAnalysis);
	
	/**
	 * Returns the task parameter value for the specified task template 
	 * parameter value and task.
	 * 
	 * @param taskTemplateParameterValue task template parameter value
	 * @param task task
	 * @return task parameter value
	 */
	TaskParameterValue findTaskParameterValueByTaskTemplateParameterValueAndTask(
			TaskTemplateParameterValue taskTemplateParameterValue, Task task);
	
	/**
	 * Returns the parole hearing task group for the specified hearing analysis 
	 * category.
	 * 
	 * @param hearingAnalysisCategory hearing analysis category
	 * @return parole hearing task group
	 */
	ParoleHearingTaskGroup findParoleHearingTaskGroupByHearingAnalysisCategory(
			HearingAnalysisCategory hearingAnalysisCategory);

	/**
	 * Returns the user account for the specified user name.
	 * 
	 * @param username user name
	 * @return user account
	 */
	UserAccount findUserAccountByUsername(String username);
	
	/**
	 * Creates a new hearing analysis note.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param description description
	 * @param date date
	 * @return hearing analysis note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysisNote createHearingAnalysisNote(
			HearingAnalysis hearingAnalysis, String description, Date date) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing hearing analysis note.
	 * 
	 * @param hearingAnalysisNote hearing analysis note
	 * @param description description
	 * @param date date
	 * @return hearing analysis note
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	HearingAnalysisNote updateHearingAnalysisNote(
			HearingAnalysisNote hearingAnalysisNote, String description, 
			Date date) throws DuplicateEntityFoundException;
	
	/**
	 * Removes a hearing analysis note.
	 * 
	 * @param hearingAnalysisNote hearing analysis note
	 */
	void removeHearingAnalysisNote(HearingAnalysisNote hearingAnalysisNote);
	
	/**
	 * Returns a list of hearing analysis notes for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of hearing analysis notes
	 */
	List<HearingAnalysisNote> findHearingAnalysisNotesByHearingAnalysis(
			HearingAnalysis hearingAnalysis);
}