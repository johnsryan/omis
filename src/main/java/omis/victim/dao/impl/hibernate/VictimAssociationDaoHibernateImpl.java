package omis.victim.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.victim.dao.VictimAssociationDao;
import omis.victim.domain.VictimAssociation;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for victim associations.
 *
 * @author Stephen Abson
 * @version 0.0.1 (May 20, 2015)
 * @since OMIS 3.0
 */
public class VictimAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<VictimAssociation>
		implements VictimAssociationDao {
	
	/* Query names. */
	
	private static final String FIND_QUERY_NAME
		= "findVictimAssociation";
	
	private static final String FIND_EXCLUDING_QUERY_NAME
		= "findVictimAssociationExcluding";
	
	private static final String FIND_BY_OFFENDER_QUERY_NAME
		= "findVictimAssociationsByOffender";
	
	private static final String FIND_BY_VICTIM_QUERY_NAME
		= "findVictimAssociationsByVictim";
	
	private static final String FIND_VICTIM_ASSOCIATION_ON_DATE_QUERY_NAME
		= "findVictimAssociationOnDate";
	
	private static final String DELETE_BY_RELATIONSHIP_QUERY_NAME
		= "deleteVictimAssociationsByRelationship";

	private static final String COUNT_BY_RELATIONSHIP_QUERY_NAME
		= "countVictimAssociationsByRelationship";
	
	/* Parameter names. */

	private static final String OFFENDER_PARAM_NAME = "offender";
	
	private static final String VICTIM_PARAM_NAME = "victim";
	
	private static final String RELATIONSHIP_PARAM_NAME = "relationship";
	
	private static final String EXCLUDED_ASSOCIATIONS_PARAM_NAME
		= "excludedAssociations";
	
	private static final String DATE_PARAM_NAME = "date";
	
	/* Constructors. */
	
	/**
	 * Instantiates Hibernate implementation of data access object for victim
	 * associations.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public VictimAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/* Method implementations. */
	
	/** {@inheritDoc} */
	@Override
	public VictimAssociation find(final Relationship relationship) {
		VictimAssociation association = (VictimAssociation)
			this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public VictimAssociation findExcluding(final Relationship relationship,
			final VictimAssociation... excludedAssociations) {
		VictimAssociation association = (VictimAssociation)
			this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.setParameterList(EXCLUDED_ASSOCIATIONS_PARAM_NAME,
						excludedAssociations)
				.uniqueResult();
		return association;
	}

	/** {@inheritDoc} */
	@Override
	public List<VictimAssociation> findByOffender(final Offender offender) {
		@SuppressWarnings("unchecked")
		List<VictimAssociation> associations = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAM_NAME, offender).list();
		return associations;
	}

	/** {@inheritDoc} */
	@Override
	public List<VictimAssociation> findByVictim(final Person victim) {
		@SuppressWarnings("unchecked")
		List<VictimAssociation> associations = this.getSessionFactory()
				.getCurrentSession().getNamedQuery(FIND_BY_VICTIM_QUERY_NAME)
				.setParameter(VICTIM_PARAM_NAME, victim).list();
		return associations;
	}

	/** {@inheritDoc} */
	@Override
	public VictimAssociation findOnDate(final Relationship relationship,
			final Date date) {
		VictimAssociation victimAssociation = (VictimAssociation)
				this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VICTIM_ASSOCIATION_ON_DATE_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.setDate(DATE_PARAM_NAME, date)
				.uniqueResult();
		return victimAssociation;
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
		long count = (Long) this.getSessionFactory().getCurrentSession()
				.getNamedQuery(COUNT_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.uniqueResult();
		return count;
	}
}