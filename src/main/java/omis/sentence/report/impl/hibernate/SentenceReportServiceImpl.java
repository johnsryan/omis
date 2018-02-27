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
package omis.sentence.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.conviction.domain.Conviction;
import omis.person.domain.Person;
import omis.sentence.report.SentenceSummary;
import omis.sentence.report.SentenceReportService;

/**
 * Sentence report service implementation.
 * 
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class SentenceReportServiceImpl 
	implements SentenceReportService {

	/* Query names. */
	
	private static final String SUMMARIZE_SENTENCES_QUERY_NAME = 
			"summarizeSentences";

	private static final String 
		 SUMMARIZE_INACTIVE_SENTENCES_BY_CONVICTION_QUERY_NAME = 
		 		"summarizeInactiveSentencesByConviction";

	private static final String SUMMARIZE_ACTIVE_SENTENCES_QUERY_NAME = 
			"summarizeActiveSentences";

	/* Parameters. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String CONVICTION_PARAM_NAME = "conviction";
	
	/* Resources. */
	
	private final SessionFactory sessionFactory;
	
	/**
	 * Instantiates an instance of sentence summary report service with the
	 * specified session factory.
	 * @param sessionFactory session factory.
	 */
	public SentenceReportServiceImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<SentenceSummary> summarizeSentences(final Person person) {
		@SuppressWarnings("unchecked")
		List<SentenceSummary> sentences = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_SENTENCES_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setReadOnly(true)
				.list();
		return sentences;
	}

	/** {@inheritDoc} */
	@Override
	public List<SentenceSummary> summarizeInactiveSentencesByConviction(
			final Conviction conviction) {
		@SuppressWarnings("unchecked")
		List<SentenceSummary> sentences = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						SUMMARIZE_INACTIVE_SENTENCES_BY_CONVICTION_QUERY_NAME)
				.setParameter(CONVICTION_PARAM_NAME, conviction)
				.setReadOnly(true)
				.list();
		return sentences;
	}

	/** {@inheritDoc} */
	@Override
	public List<SentenceSummary> summarizeActiveSentences(Person person) {
		@SuppressWarnings("unchecked")
		List<SentenceSummary> sentences = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(SUMMARIZE_ACTIVE_SENTENCES_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setReadOnly(true)
				.list();
		return sentences;
	}
}