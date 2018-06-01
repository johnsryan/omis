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
package omis.presentenceinvestigation.service;

import java.util.Date;
import java.util.List;

import omis.court.domain.Court;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.exception.DuplicateEntityFoundException;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelay;
import omis.presentenceinvestigation.domain.PresentenceInvestigationDelayCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequestNote;
import omis.user.domain.UserAccount;

/** Service for presentence investigation request related operations.
 * @author Ryan Johns
 * @author Annie Wahl
 * @author Josh Divine
 * @version 0.1.3 (May 15, 2018)
 * @since OMIS 3.0
 */
public interface PresentenceInvestigationRequestService {
	
	/** Creates a presentence investigation request.
	 * @param assignedUser - assignedUser.
	 * @param requestDate - request date.
	 * @param expectedCompletionDate - expected completion date.
	 * @param docket - docket.
	 * @param sentenceDate - sentence date.
	 * @param category - PresentenceInvestigationCategory
	 * @param submissionDate - submission date.
	 * @return presentence investigation request. 
	 * @throws DuplicateEntityFoundException - when presentence investigation
	 * request exists with given docket. */
	 public PresentenceInvestigationRequest create(
			 UserAccount assignedUser, Date requestDate, 
			 Date expectedCompletionDate, Docket docket, 
			 Date sentenceDate, PresentenceInvestigationCategory category, 
			 Date submissionDate) throws DuplicateEntityFoundException;
	 
	 /** Updates an existing presentence investigation request.\
	 * @param presentenceInvestigationRequest - presentence investigation 
	 * request.
	 * @param assignedUser - assigned user.
	 * @param requestDate - request date.
	 * @param expectedCompletionDate - expected completion date.
	 * @param completionDate - completion date.
	 * @param docket - docket.
	 * @param sentenceDate - sentence date.
	 * @param category - PresentenceInvestigationCategory
	 * @param submissionDate - submission date.
	 * @return presentence investigation request.
	 * @throws DuplicateEntityFoundException - when presentence investigation
	 * request exists for given docket. */
	public PresentenceInvestigationRequest update(
			PresentenceInvestigationRequest 
				presentenceInvestigationRequest,
			UserAccount assignedUser, Date requestDate, 
			Date expectedCompletionDate, Docket docket, Date sentenceDate, 
			PresentenceInvestigationCategory category, Date submissionDate) 
					throws DuplicateEntityFoundException;
	
	/** Removes a presentence investigation request.
	 * @param presentenceInvestigationRequest - presentence investigation 
	 * request. */
	public void remove(PresentenceInvestigationRequest 
			presentenceInvestigationRequest);
	
	/**
	 * Creates a Person with the specified properties
	 * @param lastName - String
	 * @param firstName - String
	 * @param middleName - String
	 * @param suffix - String
	 * @return Newly created Person
	 * @throws DuplicateEntityFoundException - When a Person already exists with
	 * the same Name and Identity
	 */
	public Person createPerson(String lastName, String firstName,
			String middleName, String suffix)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a Person with the specified properties
	 * @param person - Person to update
	 * @param lastName - String
	 * @param firstName - String
	 * @param middleName - String
	 * @param suffix - String
	 * @return Updated Person
	 * @throws DuplicateEntityFoundException - When a Person already exists with
	 * the same Name and Identity
	 */
	public Person updatePerson(Person person, String lastName, String firstName,
			String middleName, String suffix)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a Person
	 * @param person - Person to remove
	 */
	public void removePerson(Person person);
	
	/**
	 * Creates a Docket with the specified properties
	 * @param person - Person
	 * @param court - Court
	 * @param value - String
	 * @return Newly created Docket
	 * @throws DocketExistsException - When a Docket already exists
	 * with the specified properties
	 */
	public Docket createDocket(Person person, Court court, String value)
					throws DocketExistsException;
	
	/**
	 * Updates a Docket with the specified properties
	 * @param docket - Docket to update
	 * @param court - Court
	 * @param value - String
	 * @return Updated Docket
	 * @throws DocketExistsException - When a Docket already exists with
	 * the specified properties
	 */
	public Docket updateDocket(Docket docket, Court court, String value)
					throws DocketExistsException;
	
	/**
	 * Removes a Docket
	 * @param docket - Docket to remove
	 */
	public void removeDocket(Docket docket);
	
	/**
	 * Creates a PresentenceInvestigationRequestNote with the specified properties
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @param description - String
	 * @param date - Date
	 * @return Newly created PresentenceInvestigationRequestNote
	 * @throws DuplicateEntityFoundException - When a
	 * PresentenceInvestigationRequestNote already exists with the given Date
	 * and Description for the specified PresentenceInvestigationRequest
	 */
	public PresentenceInvestigationRequestNote
		createPresentenceInvestigationRequestNote(
			PresentenceInvestigationRequest presentenceInvestigationRequest,
			String description, Date date)
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates a PresentenceInvestigationRequestNote with the specified properties
	 * @param presentenceInvestigationRequestNote -
	 * PresentenceInvestigationRequestNote to update
	 * @param description - String
	 * @param date - Date
	 * @return Updated PresentenceInvestigationRequestNote
	 * @throws DuplicateEntityFoundException - When a
	 * PresentenceInvestigationRequestNote already exists with the given Date
	 * and Description for the specified PresentenceInvestigationRequest
	 */
	public PresentenceInvestigationRequestNote
		updatePresentenceInvestigationRequestNote(
			PresentenceInvestigationRequestNote
				presentenceInvestigationRequestNote,
			String description, Date date)
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes a PresentenceInvestigationRequestNote
	 * @param presentenceInvestigationRequestNote -
	 * PresentenceInvestigationRequestNote to remove
	 */
	public void removePresentenceInvestigationRequestNote(
			PresentenceInvestigationRequestNote
				presentenceInvestigationRequestNote);
	
	/**
	 * Returns a list of PresentenceInvestigationRequestNotes found with the
	 * specified PresentenceInvestigationRequest
	 * @param presentenceInvestigationRequest - PresentenceInvestigationRequest
	 * @return List of PresentenceInvestigationRequestNotes found with the
	 * specified PresentenceInvestigationRequest
	 */
	public List<PresentenceInvestigationRequestNote>
		findPresentenceInvestigationRequestNotesByPresentenceInvestigationRequest(
			PresentenceInvestigationRequest presentenceInvestigationRequest);
	
	/**
	 * Returns a list of all Suffixes
	 * @return List of Suffixes
	 */
	public List<Suffix> findSuffixes();
	
	/**
	 * Returns a list of all Courts
	 * @return List of Courts
	 */
	public List<Court> findCourts();
	
	/**
	 * Returns whether person is an offender.
	 * 
	 * @param person person
	 * @return whether person is an offender
	 */
	public Boolean isOffender(Person person);
	
	/**
	 * Finds user account by user name.
	 *
	 * @param userName user name
	 * @return user account name
	 */
	UserAccount findUserAccountByUsername(String username);
	
	/**
	 * Returns a list of all PresentenceInvestigationCategories
	 * @return List of all PresentenceInvestigationCategories
	 */
	public List<PresentenceInvestigationCategory>
			findAllPresentenceInvestigationCategories();

	/**
	 * Creates a new presentence investigation delay.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @param date date
	 * @param reason presentence investigation delay category
	 * @return presentence investigation delay
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	PresentenceInvestigationDelay createPresentenceInvestigationDelay(
			PresentenceInvestigationRequest presentenceInvestigationRequest, 
			Date date, PresentenceInvestigationDelayCategory reason) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Updates an existing presentence investigation delay.
	 * 
	 * @param presentenceInvestigationDelay presentence investigation delay
	 * @param date date
	 * @param reason presentence investigation delay category
	 * @return presentence investigation delay
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	PresentenceInvestigationDelay updatePresentenceInvestigationDelay(
			PresentenceInvestigationDelay presentenceInvestigationDelay, 
			Date date, PresentenceInvestigationDelayCategory reason) 
					throws DuplicateEntityFoundException;
	
	/**
	 * Removes the specified presentence investigation delay.
	 * 
	 * @param presentenceInvestigationDelay presentence investigation delay
	 */
	void removePresentenceInvestigationDelay(
			PresentenceInvestigationDelay presentenceInvestigationDelay);
	
	/**
	 * Returns a list of presentence investigation delay categories.
	 *  
	 * @return list of presentence investigation delay categories
	 */
	List<PresentenceInvestigationDelayCategory> 
			findPresentenceInvestigationDelayCategories();
	
	/**
	 * Returns a list of presentence investigation delays for the specified 
	 * presentence investigation request.
	 * 
	 * @param presentenceInvestigationRequest presentence investigation request
	 * @return list of presentence investigation delays
	 */
	List<PresentenceInvestigationDelay> findPresentenceInvestigationDelays(
			PresentenceInvestigationRequest presentenceInvestigationRequest);
}