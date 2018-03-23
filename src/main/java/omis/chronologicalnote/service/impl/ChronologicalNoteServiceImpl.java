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
package omis.chronologicalnote.service.impl;

import java.util.Date;
import java.util.List;

import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroupTemplate;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryTemplate;
import omis.chronologicalnote.exception.ChronologicalNoteCategoryAssociationExistsException;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryAssociationDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryGroupDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryGroupTemplateDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteCategoryTemplateDelegate;
import omis.chronologicalnote.service.delegate.ChronologicalNoteDelegate;
import omis.offender.domain.Offender;

/**
 * Chronological note service implmentation.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @verison 0.1.0 (February 1, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteServiceImpl 
		implements ChronologicalNoteService {
	
	/* Delegates */
	
	private ChronologicalNoteDelegate chronologicalNoteDelegate;
	private ChronologicalNoteCategoryAssociationDelegate
	chronologicalNoteCategoryAssociationDelegate;
	private ChronologicalNoteCategoryDelegate chronologicalNoteCategoryDelegate;
	private ChronologicalNoteCategoryGroupDelegate
	chronologicalNoteCategoryGroupDelegate;
	private ChronologicalNoteCategoryTemplateDelegate
	chronologicalNoteCategoryTemplateDelegate;
	private ChronologicalNoteCategoryGroupTemplateDelegate
	chronologicalNoteCategoryGroupTemplateDelegate;
	
	/**
	 * Instantiates a chronological note service with the specified delegates.
	 * 
	 * @param chronologicalNoteDelegate chronological note delegate
	 * @param chronologicalNoteCategoryAssociationDelegate chronological note
	 * category association delegate
	 * @param chronologicalNoteCategoryDelegate chronological note category
	 * delegate
	 * @param chronologicalNoteCategoryGroupDelegate chronological note category
	 * group delegate
	 * @param chronologicalNoteCategoryTemplateDelegate chronological note
	 * category template delegate
	 * @param chronologicalNoteCategoryGroupTemplateDelegate chronological note
	 * category group template delegate
	 */
	public ChronologicalNoteServiceImpl(final ChronologicalNoteDelegate
			chronologicalNoteDelegate,
			final ChronologicalNoteCategoryAssociationDelegate
			chronologicalNoteCategoryAssociationDelegate,
			final ChronologicalNoteCategoryDelegate
			chronologicalNoteCategoryDelegate,
			final ChronologicalNoteCategoryGroupDelegate
			chronologicalNoteCategoryGroupDelegate,
			final ChronologicalNoteCategoryTemplateDelegate
			chronologicalNoteCategoryTemplateDelegate,
			final ChronologicalNoteCategoryGroupTemplateDelegate
			chronologicalNoteCategoryGroupTemplateDelegate) {
		this.chronologicalNoteDelegate = chronologicalNoteDelegate;
		this.chronologicalNoteCategoryAssociationDelegate
		= chronologicalNoteCategoryAssociationDelegate;
		this.chronologicalNoteCategoryDelegate
		= chronologicalNoteCategoryDelegate;
		this.chronologicalNoteCategoryGroupDelegate
		= chronologicalNoteCategoryGroupDelegate;
		this.chronologicalNoteCategoryTemplateDelegate
		= chronologicalNoteCategoryTemplateDelegate;
		this.chronologicalNoteCategoryGroupTemplateDelegate
		= chronologicalNoteCategoryGroupTemplateDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public ChronologicalNote create(final Date date, final Offender offender,
			final String title, final String narrative)
			throws ChronologicalNoteExistsException {
		return this.chronologicalNoteDelegate.create(date, offender, 
				title, narrative);
	}

	/** {@inheritDoc} */
	@Override
	public ChronologicalNote update(final ChronologicalNote note,
			final Date date, final String title, final String narrative)
			throws ChronologicalNoteExistsException {
		return this.chronologicalNoteDelegate.update(note, date, title, 
				narrative);
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final ChronologicalNote note) {
		this.chronologicalNoteDelegate.remove(note);
	}

	/** {@inheritDoc} */
	@Override
	public void associateCategory(final ChronologicalNote note,
			final ChronologicalNoteCategory category) {
		try {
			this.chronologicalNoteCategoryAssociationDelegate.create(note,
					category);
		} catch (ChronologicalNoteCategoryAssociationExistsException exception) 
		{
			throw new IllegalArgumentException(
			"No association exists between specified note and category");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void dissociateCategory(final ChronologicalNote note,
			final ChronologicalNoteCategory category) {
		this.chronologicalNoteCategoryAssociationDelegate.remove(note,
				category);
	}

	/** {@inheritDoc} */
	@Deprecated
	@Override
	public List<ChronologicalNoteCategory> findCategories() {
		return this.chronologicalNoteCategoryDelegate.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findCategoriesByGroup(
		final ChronologicalNoteCategoryGroup group) {
		return this.chronologicalNoteCategoryDelegate.findCategoriesByGroup(
			group);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategoryGroup> findGroups() {
		return this.chronologicalNoteCategoryGroupDelegate.findAll();
	}
	
	/** {@inheritDoc} */
	@Override
	public ChronologicalNoteCategoryTemplate findCategoryTemplate(
		final ChronologicalNoteCategory category) {
		return this.chronologicalNoteCategoryTemplateDelegate
		.findCategoryTemplateByCategory(category);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategoryGroupTemplate> findTemplatesByGroup(
			final ChronologicalNoteCategoryGroup group) {
		return this.chronologicalNoteCategoryGroupTemplateDelegate
				.findByGroup(group);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findAssociatedCategories(
			final ChronologicalNote note) {
		return this.chronologicalNoteCategoryAssociationDelegate
				.findAssociatedCategories(note);
	}

	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategoryGroup> findCategoryGroups() {
		return this.chronologicalNoteCategoryGroupDelegate.findAll();
	}
}