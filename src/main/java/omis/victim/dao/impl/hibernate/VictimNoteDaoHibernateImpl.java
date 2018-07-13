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
package omis.victim.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.victim.dao.VictimNoteDao;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.VictimNote;
import omis.victim.domain.VictimNoteCategory;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for victim notes.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Jul 23, 2015)
 * @since OMIS 3.0
 */
public class VictimNoteDaoHibernateImpl
		extends GenericHibernateDaoImpl<VictimNote>
		implements VictimNoteDao {

	/* Query names. */
	
	private static final String FIND_QUERY_NAME = "findVictimNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findVictimNoteExcluding";
	
	private static final String FIND_BY_VICTIM_QUERY_NAME
		= "findVictimNotesByVictim";
	
	private static final String FIND_BY_ASSOCIATION_QUERY_NAME
		= "findVictimNotesByAssociation";
	
	private static final String COUNT_BY_ASSOCIATION_QUERY_NAME
		= "countVictimNotesByAssociation";
	
	private static final String DELETE_BY_ASSOCIATION_QUERY_NAME
		= "deleteVictimNotesByAssociation";
	
	private static final String COUNT_BY_VICTIM_QUERY_NAME
		= "countVictimNotesByVictim";
	
	private static final String DELETE_BY_RELATIONSHIP_QUERY_NAME
		= "deleteVictimNotesByRelationship";

	/* Parameter names. */
	
	private static final String VICTIM_PARAM_NAME = "victim";

	private static final String CATEGORY_PARAM_NAME = "category";

	private static final String DATE_PARAM_NAME = "date";

	private static final String EXCLUDED_NOTES_PARAM_NAME = "excludedNotes";

	private static final String ASSOCIATION_PARAM_NAME = "association";
	
	private static final String VALUE_PARAM_NAME = "value";

	/* Property names. */
	
	private static final String ASSOCIATION_PROPERTY_NAME = "association";

	private static final String RELATIONSHIP_PARAM_NAME = "relationship";
	
	/* Constructors. */

	/**
	 * Instantiates Hibernate implementation of data access object for victim
	 * notes.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public VictimNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public VictimNote find(
			final Person victim, final VictimNoteCategory category,
			final Date date, final String value) {
		VictimNote note = (VictimNote) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(VALUE_PARAM_NAME, value)
				.uniqueResult();
		return note;
	}

	/** {@inheritDoc} */
	@Override
	public VictimNote findExcluding(
			final Person victim, final VictimNoteCategory category,
			final Date date, final String value, 
			final VictimNote... excludedNotes) {
		VictimNote note = (VictimNote) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameter(VALUE_PARAM_NAME, value)
				.setParameterList(EXCLUDED_NOTES_PARAM_NAME, excludedNotes)
				.uniqueResult();
		return note;
	}

	/** {@inheritDoc} */
	@Override
	public List<VictimNote> findByVictim(final Person victim) {
		@SuppressWarnings("unchecked")
		List<VictimNote> notes = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_VICTIM_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim).list();
		return notes;
	}

	/** {@inheritDoc} */
	@Override
	public List<VictimNote> findByAssociation(
			final VictimAssociation association) {
		@SuppressWarnings("unchecked")
		List<VictimNote> notes = this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_ASSOCIATION_QUERY_NAME)
				.setParameter(ASSOCIATION_PARAM_NAME, association,
						this.getEntityPropertyType(ASSOCIATION_PROPERTY_NAME))
				.list();
		return notes;
	}

	/** {@inheritDoc} */
	@Override
	public long countByAssociation(final VictimAssociation association) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
			.getNamedQuery(COUNT_BY_ASSOCIATION_QUERY_NAME)
				.setParameter(ASSOCIATION_PARAM_NAME, association)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public int removeByAssociation(
			final VictimAssociation association) {
		return this.getSessionFactory().getCurrentSession()
				.getNamedQuery(DELETE_BY_ASSOCIATION_QUERY_NAME)
				.setParameter(ASSOCIATION_PARAM_NAME, association)
				.executeUpdate();
	}

	/** {@inheritDoc} */
	@Override
	public long countByVictim(final Person victim) {
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_BY_VICTIM_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim)
				.uniqueResult();
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public int removeByRelationship(final Relationship relationship) {
		return this.getSessionFactory().getCurrentSession()
				.getNamedQuery(DELETE_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.executeUpdate();
	}
}