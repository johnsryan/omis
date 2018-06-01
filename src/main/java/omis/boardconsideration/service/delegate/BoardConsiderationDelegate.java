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
package omis.boardconsideration.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.boardconsideration.dao.BoardConsiderationDao;
import omis.boardconsideration.domain.BoardConsideration;
import omis.boardconsideration.domain.BoardConsiderationCategory;
import omis.exception.DuplicateEntityFoundException;
import omis.hearinganalysis.domain.HearingAnalysis;
import omis.instance.factory.InstanceFactory;

/**
 * Board consideration delegate.
 * 
 * @author Josh Divine
 * @version 0.1.0 (May 29, 2018)
 * @since OMIS 3.0
 */
public class BoardConsiderationDelegate {

	/* Data access objects. */
	
	private final BoardConsiderationDao boardConsiderationDao;

	/* Instance factories. */
	
	private final InstanceFactory<BoardConsideration> 
			boardConsiderationInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates an implementation of board consideration delegate with 
	 * the specified data access object, instance factory and audit component 
	 * retriever.
	 * 
	 * @param boardConsiderationDao board consideration data access object
	 * @param boardConsiderationInstanceFactory board consideration instance 
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public BoardConsiderationDelegate(
			final BoardConsiderationDao boardConsiderationDao,
			final InstanceFactory<BoardConsideration> 
					boardConsiderationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.boardConsiderationDao = boardConsiderationDao;
		this.boardConsiderationInstanceFactory = 
				boardConsiderationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new board consideration.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @param title title
	 * @param description description
	 * @param category board consideration category
	 * @param accepted accepted
	 * @return board consideration
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public BoardConsideration create(final HearingAnalysis hearingAnalysis, 
			final String title, final String description, 
			final BoardConsiderationCategory category, final Boolean accepted) 
					throws DuplicateEntityFoundException {
		if (this.boardConsiderationDao.find(hearingAnalysis, title) != null) {
			throw new DuplicateEntityFoundException(
					"Board consideration already exists.");
		}
		BoardConsideration boardConsideration = this
				.boardConsiderationInstanceFactory.createInstance();
		boardConsideration.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		populateBoardConsideration(boardConsideration, hearingAnalysis, title, 
				description, category, accepted);
		return this.boardConsiderationDao.makePersistent(boardConsideration);
	}
	
	/**
	 * Updates an existing board consideration.
	 * 
	 * @param boardConsideration board consideration
	 * @param hearingAnalysis hearing analysis
	 * @param title title
	 * @param description description
	 * @param category board consideration category
	 * @param accepted accepted
	 * @return board consideration
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public BoardConsideration update(final BoardConsideration boardConsideration,
			final HearingAnalysis hearingAnalysis, final String title, 
			final String description, final BoardConsiderationCategory category,
			final Boolean accepted) throws DuplicateEntityFoundException {
		if (this.boardConsiderationDao.findExcluding(hearingAnalysis, title, 
				boardConsideration) != null) {
			throw new DuplicateEntityFoundException(
					"Board consideration already exists.");
		}
		populateBoardConsideration(boardConsideration, hearingAnalysis, title, 
				description, category, accepted);
		return this.boardConsiderationDao.makePersistent(boardConsideration);
	}

	/**
	 * Removes the specified board consideration.
	 * 
	 * @param boardConsideration board consideration
	 */
	public void remove(
			final BoardConsideration boardConsideration) {
		this.boardConsiderationDao.makeTransient(boardConsideration);
	}
	
	/**
	 * Returns a list of board considerations for the specified hearing 
	 * analysis.
	 * 
	 * @param hearingAnalysis hearing analysis
	 * @return list of board considerations
	 */
	public List<BoardConsideration> findByHearingAnalysis(
			final HearingAnalysis hearingAnalysis) {
		return this.boardConsiderationDao.findByHearingAnalysis(hearingAnalysis);
	}
	
	// Populates a board consideration
	private void populateBoardConsideration(
			final BoardConsideration boardConsideration,
			final HearingAnalysis hearingAnalysis, final String title, 
			final String description, final BoardConsiderationCategory category,
			final Boolean accepted) {
		boardConsideration.setHearingAnalysis(hearingAnalysis);
		boardConsideration.setTitle(title);
		boardConsideration.setDescription(description);
		boardConsideration.setCategory(category);
		boardConsideration.setAccepted(accepted);
		boardConsideration.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}