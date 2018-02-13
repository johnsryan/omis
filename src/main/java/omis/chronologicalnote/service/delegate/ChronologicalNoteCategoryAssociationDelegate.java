/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.chronologicalnote.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.chronologicalnote.dao.ChronologicalNoteCategoryAssociationDao;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryAssociation;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryAssociationExistsException;
import omis.instance.factory.InstanceFactory;

/**
 * Chronological note association delegate.
 * 
 * @author Joel Norris
 * @version 0.1.0 (January 30, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteCategoryAssociationDelegate {

	/* Data access objects. */
	private ChronologicalNoteCategoryAssociationDao chronologicalNoteCategoryAssociationDao;
	
	/*Instance factories. */
	private InstanceFactory<ChronologicalNoteCategoryAssociation>
	chronologicalNoteCategoryAssociationInstanceFactory;
	
	/* Component retrievers. */
	private AuditComponentRetriever auditComponentRetriever;
	
	/**
	 * Instantiates a chronological note category association delegate with the specified data access object
	 * and instance factory.
	 * 
	 * @param chronologicalNoteCategoryAssociationDao chronological note category association data access object
	 * @param chronologicalNoteCategoryAssociationInstanceFactory chronological note category association instance factory
	 */
	public ChronologicalNoteCategoryAssociationDelegate(
			final ChronologicalNoteCategoryAssociationDao chronologicalNoteCategoryAssociationDao,
			final InstanceFactory<ChronologicalNoteCategoryAssociation>
			chronologicalNoteCategoryAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.chronologicalNoteCategoryAssociationDao = chronologicalNoteCategoryAssociationDao;
		this.chronologicalNoteCategoryAssociationInstanceFactory = chronologicalNoteCategoryAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new chronological note category association with the specified chronological note and
	 * chronological note category.
	 * 
	 * @param note chronological note
	 * @param category chronological note category
	 * @return newly created chronological note category association
	 * @throws ChronologicalNoteCategoryAssociationExistsException Thrown when a duplicate chronological note category association is found
	 */
	public ChronologicalNoteCategoryAssociation create(final ChronologicalNote note, final ChronologicalNoteCategory category)
			throws ChronologicalNoteCategoryAssociationExistsException {
		if (this.chronologicalNoteCategoryAssociationDao.find(note, category) != null) {
			throw new ChronologicalNoteCategoryAssociationExistsException("Association already exists between category and note");
		}
		ChronologicalNoteCategoryAssociation association = this.chronologicalNoteCategoryAssociationInstanceFactory.createInstance();
		association.setNote(note);
		association.setCategory(category);
		association.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		return this.chronologicalNoteCategoryAssociationDao.makePersistent(association);
	}
	
	/**
	 * Removes the association that exists for the specified chronological note and chronological note category.
	 * 
	 * @param note chronological note
	 * @param category chronological note category
	 * @throws IllegalArgumentException Thrown when no association exists for the specified chronological note and
	 * chronological note category.
	 */
	public void remove(final ChronologicalNote note, final ChronologicalNoteCategory category) throws IllegalArgumentException {
		ChronologicalNoteCategoryAssociation association = this.chronologicalNoteCategoryAssociationDao.find(note, category);
		if (association == null) {
			throw new IllegalArgumentException("No association for specified note and category exists");
		}
		this.chronologicalNoteCategoryAssociationDao.makeTransient(association);
	}
	
	public List<ChronologicalNoteCategory> findAssociatedCategories(final ChronologicalNote note) {
		return this.chronologicalNoteCategoryAssociationDao.findAssociatedCategories(note);
	}
}