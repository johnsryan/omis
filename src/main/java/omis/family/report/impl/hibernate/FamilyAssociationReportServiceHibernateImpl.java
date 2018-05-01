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
package omis.family.report.impl.hibernate;

import java.util.List;
import java.util.ArrayList;

import omis.family.domain.FamilyAssociation;
import omis.family.report.FamilyAssociationReportService;
import omis.family.report.FamilyAssociationSummary;
import omis.offender.domain.Offender;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of offender family association report service.
 * 
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @author Josh Divine
 * @version 0.1.1 (Feb 15, 2018)
 * @since OMIS 3.0
 */
public class FamilyAssociationReportServiceHibernateImpl
		implements FamilyAssociationReportService {

	/* Queries */
	private static final String 
		FIND_FAMILY_ASSOCIATION_SUMMARY_BY_OFFENDER_QUERY_NAME 
		= "findFamilyAssociationSummaryByOffender";
	
	private static final String 
		COUNT_FAMILY_ASSOCIATION_BY_OFFENDER_FAMILY_MEMBER_QUERY_NAME
		= "countFamilyAssociationByOffenderFamilyMember";
	
	private static final String 
		FIND_FAMILY_ASSOCIATION_BY_OFFENDER_FAMILY_MEMBER_QUERY_NAME
		= "findFamilyAssociationByOffenderFamilyMember";
	
	/* Parameters */
	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String FAMILY_MEMBER_PARAM_NAME = "familyMember";
	
	private final OffenderDelegate offenderDelegate;
	
	private SessionFactory sessionFactory;
		
	/* Constructors. */
	
	/**
	 * Instantiates an Hibernate implementation of offender family association 
	 * report service.
	 * @param sessionFactory session factory
	 * @param offenderDelegate offenderDelegate
	 */
	public FamilyAssociationReportServiceHibernateImpl(final SessionFactory
		sessionFactory, final OffenderDelegate offenderDelegate) {
		this.sessionFactory = sessionFactory;
		this.offenderDelegate = offenderDelegate;
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override 
	public List<FamilyAssociationSummary> findByOffender(
		final Offender offender) {
		List<FamilyAssociationSummary> summaries
			= new ArrayList<FamilyAssociationSummary>();
		@SuppressWarnings("unchecked")
		List<FamilyAssociationSummary> internalSummaries = this.sessionFactory
			.getCurrentSession()
			.getNamedQuery(
					FIND_FAMILY_ASSOCIATION_SUMMARY_BY_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_PARAM_NAME, offender)
			.setReadOnly(true)
			.list();
		summaries.addAll(internalSummaries); 
		return summaries;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean familyAssociationExists(
			final Offender offender, final Person familyMember) {
		final Boolean query = (Boolean) this.sessionFactory.getCurrentSession()
				.getNamedQuery(
						COUNT_FAMILY_ASSOCIATION_BY_OFFENDER_FAMILY_MEMBER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(FAMILY_MEMBER_PARAM_NAME, familyMember)
				.uniqueResult();
		return query;
	}

	/** {@inheritDoc} */
	@Override
	public FamilyAssociation findFamilyAssociation(
			final Offender offender, final Person familyMember) {
		FamilyAssociation association = (FamilyAssociation) this.sessionFactory
				.getCurrentSession()
				.getNamedQuery(
						FIND_FAMILY_ASSOCIATION_BY_OFFENDER_FAMILY_MEMBER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender)
				.setParameter(FAMILY_MEMBER_PARAM_NAME, familyMember)
				.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean isOffender(final Person person) {
		return this.offenderDelegate.isOffender(person);
	}
}