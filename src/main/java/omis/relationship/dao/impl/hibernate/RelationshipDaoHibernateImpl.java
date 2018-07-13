package omis.relationship.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.person.domain.Person;
import omis.relationship.dao.RelationshipDao;
import omis.relationship.domain.Relationship;

/**
 * Hibernate implementation of data access object for relationships.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Feb 21, 2013)
 * @since OMIS 3.0
 */
public class RelationshipDaoHibernateImpl
		extends GenericHibernateDaoImpl<Relationship>
		implements RelationshipDao {

	/* Query names. */
	
	private static final String FIND_BY_PEOPLE_QUERY_NAME = "findRelationshipByPeople";
	private static final String FIND_BY_PERSON_QUERY_NAME = "findRelationshipsByPerson";
	
	/* Parameter names. */
	
	private static final String FIRST_PERSON_PARAM_NAME = "firstPerson";
	private static final String SECOND_PERSON_PARAM_NAME = "secondPerson";
	private static final String PERSON_PARAM_NAME = "person";
	
	/* Constructor. */
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * relationships with the specified resources.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public RelationshipDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public Relationship findByPeople(final Person firstPerson,
			final Person secondPerson) {
		Relationship relationship = (Relationship) getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_PEOPLE_QUERY_NAME)
				.setParameter(FIRST_PERSON_PARAM_NAME, firstPerson)
				.setParameter(SECOND_PERSON_PARAM_NAME, secondPerson)
				.uniqueResult();
		return relationship;
	}

	/** {@inheritDoc} */
	@Override
	public List<Relationship> findByPerson(Person person) {
		@SuppressWarnings("unchecked")
		List<Relationship> relationships = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.list();
		return relationships;
	}
}