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
package omis.chronologicalnote.report.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.report.ChronologicalNoteReportService;
import omis.chronologicalnote.report.ChronologicalNoteSummary;
import omis.offender.domain.Offender;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of report service for chronological notes.
 *
 * @author Yidong Li
 * @version 0.0.1 (Jan 30, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteReportServiceHibernateImpl
		implements ChronologicalNoteReportService {
	/* Query names. */
	private static final String FIND_BY_OFFENDER_QUERY_NAME
		= "findChronologicalNoteSummaryByOffender";
	private static final String FIND_CATEGORIES_QUERY_NAME = "findCategories";
	private static final String FIND_CATEGORY_NAMES_BY_NOTE_ID_QUERY_NAME
		= "findCategoryNamesByNoteId";
	private static final String 
		FIND_CATEGORY_NAMES_BY_NOTE_ID_AND_CATEGORIES_QUERY_NAME
		= "findCategoryNamesByNoteIdAndCategories";
	
	/* Parameter names. */
	private static final String OFFENDER_PARAM_NAME = "offender";
	private static final String CATEGORIES_PARAM_NAME = "categories";
	private static final String NOTE_ID_PARAM_NAME = "noteId";
	
	/* Resources. */
	private final SessionFactory sessionFactory;

	/* Constructors. */
	/**
	 * Instantiates an implementation of report service.
	 * 
	 * @param sessionFactory session factory
	 */
	public ChronologicalNoteReportServiceHibernateImpl(
			final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteSummary> findByOffender(
		final Offender offender) {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.list();
		for (ChronologicalNoteSummary summary : summaries) {
			summary.getCategoryNames().addAll(this.findCategoryNamesByNoteId(
				summary.getId()));
		}
		return summaries;
	}
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChronologicalNoteSummary> findByOffenderAndCategories(
		final Offender offender, 
		final List<ChronologicalNoteCategory> categories) {
			List<ChronologicalNoteSummary> summaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.list();
		if(categories!=null){
			for (ChronologicalNoteSummary summary : summaries) {
				List<String> namesList = new ArrayList<String>();
				namesList.addAll(findCategoryNamesByNoteIdAndCategories(
					summary.getId(), categories));
				if(!namesList.isEmpty()){
					summary.getCategoryNames().addAll(
					this.findCategoryNamesByNoteId(summary.getId()));
				} 
			}
		} 
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<ChronologicalNoteCategory> findCategories() {
		@SuppressWarnings("unchecked")
		List<ChronologicalNoteCategory> categories = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_CATEGORIES_QUERY_NAME)
			.list();
		return categories;
	}
	
	private List<String> findCategoryNamesByNoteId(final Long noteId) {
		@SuppressWarnings("unchecked")
		List<String> categoryNames = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(FIND_CATEGORY_NAMES_BY_NOTE_ID_QUERY_NAME)
			.setParameter(NOTE_ID_PARAM_NAME, noteId)
			.list();
		return categoryNames;
	}
	
	private List<String> findCategoryNamesByNoteIdAndCategories(
		final Long noteId, final List<ChronologicalNoteCategory> categories) {
		@SuppressWarnings("unchecked")
		List<String> categoryNames = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
				FIND_CATEGORY_NAMES_BY_NOTE_ID_AND_CATEGORIES_QUERY_NAME)
			.setParameter(NOTE_ID_PARAM_NAME, noteId)
			.setParameterList(CATEGORIES_PARAM_NAME, categories)
			.list();
		return categoryNames;
	}
}