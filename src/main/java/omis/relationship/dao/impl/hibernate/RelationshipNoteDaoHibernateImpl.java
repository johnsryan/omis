package omis.relationship.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.relationship.dao.RelationshipNoteDao;
import omis.relationship.domain.Relationship;
import omis.relationship.domain.RelationshipNote;
import omis.relationship.domain.RelationshipNoteCategory;

/**
 * Hibernate implementation of data access object for relationship notes.
 * 
 * @author Stephen Abson
 * @version 0.0.1 (Nov 13, 2017)
 * @since OMIS 3.0
 */
public class RelationshipNoteDaoHibernateImpl
		extends GenericHibernateDaoImpl<RelationshipNote>
		implements RelationshipNoteDao {

	/* Query names. */
	
	private static final String FIND_BY_RELATIONSHIP_QUERY_NAME
			= "findRelationshipNotesByRelationship";

	private static final String FIND_QUERY_NAME
			= "findRelationshipNote";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
			= "findRelationshipNoteExcluding";
	
	private static final String DELETE_BY_RELATIONSHIP_QUERY_NAME
			= "deleteRelationshipNotesByRelationship";
	
	private static final String COUNT_BY_RELATIONSHIP_QUERY_NAME
			= "countRelationshipNotesByRelationship";
	
	/* Parameters. */
	
	private static final String RELATIONSHIP_PARAM_NAME = "relationship";
	
	private static final String CATEGORY_PARAM_NAME = "category";
	
	private static final String VALUE_PARAM_NAME = "value";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String EXCLUDED_PARAM_NAME = "excludedNotes";
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of data access object for
	 * relationship notes.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public RelationshipNoteDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */

	/** {@inheriDoc} */
	@Override
	public List<RelationshipNote> findByRelationship(
			final Relationship relationship) {
		@SuppressWarnings("unchecked")
		List<RelationshipNote> notes = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.list();
		return notes;
	}

	/** {@inheriDoc} */
	@Override
	public RelationshipNote find(
			final Relationship relationship,
			final RelationshipNoteCategory category,
			final String value,
			final Date date) {
		RelationshipNote note = (RelationshipNote) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(VALUE_PARAM_NAME, value)
				.setTimestamp(DATE_PARAM_NAME, date)
				.uniqueResult();
		return note;
	}

	/** {@inheriDoc} */
	@Override
	public RelationshipNote findExcluding(
			final Relationship relationship,
			final RelationshipNoteCategory category,
			final String value,
			final Date date,
			final RelationshipNote... excludedNotes) {
		RelationshipNote note = (RelationshipNote) this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.setParameter(CATEGORY_PARAM_NAME, category)
				.setParameter(VALUE_PARAM_NAME, value)
				.setTimestamp(DATE_PARAM_NAME, date)
				.setParameterList(EXCLUDED_PARAM_NAME, excludedNotes)
				.uniqueResult();
		return note;
	}

	/** {@inheritDoc} */
	@Override
	public int removeByRelationship(final Relationship relationship) {
		return this.getSessionFactory().getCurrentSession()
				.getNamedQuery(DELETE_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.executeUpdate();
	}

	/** {@inheritDoc} */
	@Override
	public long countByRelationship(final Relationship relationship) {
		return (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.uniqueResult();
	}
}