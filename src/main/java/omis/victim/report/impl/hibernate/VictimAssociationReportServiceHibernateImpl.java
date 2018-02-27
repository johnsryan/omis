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
package omis.victim.report.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.victim.domain.VictimAssociation;
import omis.victim.report.VictimAssociationReportService;
import omis.victim.report.VictimAssociationSummary;
import omis.victim.report.VictimDocumentAssociationSummary;
import omis.victim.service.delegate.VictimNoteDelegate;

/**
 * Hibernate implementation of victim report service.
 *
 * @author Stephen Abson
 * @author Josh Divine
 * @version 0.0.2 (Feb 14, 2018)
 * @since OMIS 3.0
 */
public class VictimAssociationReportServiceHibernateImpl
		implements VictimAssociationReportService {

	/* Query names */
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME
		= "findVictimSummaryAssociationsByOffender";
	
	private static final String SUMMARIZE_QUERY_NAME
		= "summarizeVictimAssociation";
	
	private static final String FIND_BY_VICTIM_QUERY_NAME
		= "findVictimSummaryAssociationsByVictim";
	
	private static final String FIND_DOCUMENT_BY_VICTIM_QUERY_NAME 
		= "findDocumentAssociationSummariesByVictim";
	
	/* Parameter names */
	
	private static final String OFFENDER_PARAM_NAME = "offender";

	private static final String VICTIM_PARAM_NAME = "victim";
	
	private static final String VICTIM_ASSOCIATION_PARAM_NAME 
		= "victimAssociation";
	
	/* Resources */
	
	private final SessionFactory sessionFactory;
	
	private final VictimNoteDelegate victimNoteDelegate;
	
	private final OffenderDelegate offenderDelegate;
	
	/* Constructors */
	
	/**
	 * Instantiates Hibernate implementation of victim report service. 
	 * 
	 * @param sessionFactory session factory
	 * @param delegate for victim notes
	 */
	public VictimAssociationReportServiceHibernateImpl(
			final SessionFactory sessionFactory,
			final VictimNoteDelegate victimNoteDelegate,
			final OffenderDelegate offenderDelegate) {
		this.sessionFactory = sessionFactory;
		this.victimNoteDelegate = victimNoteDelegate;
		this.offenderDelegate = offenderDelegate;
	}

	/* Method implementations */
	
	/** {@inheritDoc} */
	@Override
	public List<VictimAssociationSummary> findSummariesByOffender(
			final Offender offender) {
		@SuppressWarnings("unchecked")
		List<VictimAssociationSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setReadOnly(true)
				.list();
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public VictimAssociationSummary summarizeVictimAssociation(
			final VictimAssociation victimAssociation) {
		VictimAssociationSummary summary =
				(VictimAssociationSummary) this.sessionFactory
				.getCurrentSession().getNamedQuery(
						SUMMARIZE_QUERY_NAME)
				.setParameter(VICTIM_ASSOCIATION_PARAM_NAME, victimAssociation)
				.setReadOnly(true)
				.uniqueResult();
		return summary;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<VictimAssociationSummary> findSummariesByVictim(
			final Person victim) {
		@SuppressWarnings("unchecked")
		List<VictimAssociationSummary> summaries = this.sessionFactory
				.getCurrentSession().getNamedQuery(FIND_BY_VICTIM_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim)
				.setReadOnly(true)
				.list();
		return summaries;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<VictimDocumentAssociationSummary> 
		findDocumentAssociationSummariesByVictim(final Person victim) {
		@SuppressWarnings("unchecked")
		List<VictimDocumentAssociationSummary> documentSummaries = this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(FIND_DOCUMENT_BY_VICTIM_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim)
				.setReadOnly(true)
				.list();
		return documentSummaries;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNotes(final VictimAssociation victimAssociation) {
		return this.victimNoteDelegate
				.countByAssociation(victimAssociation) > 0;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isOffender(final Person person) {
		return this.offenderDelegate.isOffender(person);
	}
}