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
package omis.offender.dao.impl.hibernate;

import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.dao.OffenderDao;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.person.domain.PersonIdentity;
import omis.person.domain.PersonName;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of offender data access object.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @version 0.1.5 (Sept 16, 2014)
 * @since OMIS 3.0
 */
public class OffenderDaoHibernateImpl
		extends GenericHibernateDaoImpl<Offender>
		implements OffenderDao {

	/* Query names. */
	
	private static final String FIND_OFFENDER_BY_IDENTITY_AND_NAME_QUERY_NAME 
		= "findOffenderByIdentityAndName";
	
	private static final String FIND_OFFENDER_BY_OFFENDER_NUMBER_QUERY_NAME
		= "findOffenderByOffenderNumber";
	
	private static final String FIND_OFFENDERS_BY_NAME_QUERY_NAME
		= "findOffendersByName";
	
	private static final String FIND_NEXT_OFFENDER_NUMBER_QUERY_NAME 
		= "findNextOffenderNumber";
	
	private static final String FIND_OFFENDER_BY_ID_QUERY_NAME 
		= "findOffenderById";
	
	private static final String EXPUNGE_OFFENDER_QUERY_NAME 
		= "expungeOffender";
	
	private static final String INSERT_OFFENDER_QUERY_NAME = "insertOffender";
	
	/* Parameter names. */
	
	private static final String IDENTITY_PARAMETER_NAME = "identity";
	
	private static final String PERSON_NAME_PARAMETER_NAME = "personName";
	
	// TODO: Do not reference ID - rather, compare entities in the query - SA
	private static final String ID_PARAMETER_NAME = "id";
	
	private static final String OFFENDER_ID_PARAMETER_NAME = "offenderId";

	private static final String OFFENDER_NUMBER_PARAMETER_NAME
		= "offenderNumber";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * offenders with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public OffenderDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isOffenderNumberValid(final Integer offenderNumber) {
		return getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_OFFENDER_BY_OFFENDER_NUMBER_QUERY_NAME)
				.setParameter(OFFENDER_NUMBER_PARAMETER_NAME, offenderNumber)
				.uniqueResult() != null;
	}

	/** {@inheritDoc} */
	@Override
	public List<Offender> findOffenderByName(final String firstName,
			final String lastName) {
		@SuppressWarnings("unchecked")
		List<Offender> offenders = getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_OFFENDERS_BY_NAME_QUERY_NAME)
				.list();
		return offenders;
	}

	/** {@inheritDoc} */
	@Override
	public Offender findByOffenderNumber(final Integer offenderNumber) {
		Offender offender = (Offender) getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_OFFENDER_BY_OFFENDER_NUMBER_QUERY_NAME)
				.setParameter(OFFENDER_NUMBER_PARAMETER_NAME, offenderNumber)
				.uniqueResult();
		return offender;
	}

	/** {@inheritDoc} */
	@Override
	public Integer findNextOffenderNumber() {
		Integer nextOffenderNumber = Integer.valueOf(getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_NEXT_OFFENDER_NUMBER_QUERY_NAME)
				.uniqueResult().toString());
		return nextOffenderNumber;
	}

	/** {@inheritDoc} */
	@Override
	public Offender findAsOffender(final Person person) {
		return (Offender) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_OFFENDER_BY_ID_QUERY_NAME)
				.setParameter(ID_PARAMETER_NAME, person.getId())
			.uniqueResult();
	}
	
	/** {@inheritDoc} */
	@Override
	public void expunge(final Offender offender) {
		this.getSessionFactory().getCurrentSession()
			.getNamedQuery(EXPUNGE_OFFENDER_QUERY_NAME)
			.setParameter(OFFENDER_ID_PARAMETER_NAME, offender.getId())
			.executeUpdate();
	}

	/** {@inheritDoc} */
	@Override
	public List<Offender> findOffenderByIdentityAndName(
			final PersonIdentity identity, final PersonName name) {
		@SuppressWarnings("unchecked")
		List<Offender> offenders = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_OFFENDER_BY_IDENTITY_AND_NAME_QUERY_NAME)
				.setParameter(IDENTITY_PARAMETER_NAME, identity)
				.setParameter(PERSON_NAME_PARAMETER_NAME, name)
				.list();
		return offenders;
	}

	/** {@inheritDoc} */
	@Override
	public Offender convertPerson(
			final Person person, final Integer offenderNumber) {
		Long id = person.getId();
		this.getSessionFactory().getCurrentSession()
				.getNamedQuery(INSERT_OFFENDER_QUERY_NAME)
				.setParameter(ID_PARAMETER_NAME, id)
				.setParameter(OFFENDER_NUMBER_PARAMETER_NAME, offenderNumber)
				.executeUpdate();
		this.getSessionFactory().getCurrentSession().evict(person);
		return this.findById(id, false);
	}
}