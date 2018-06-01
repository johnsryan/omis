package omis.person.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.datatype.DateRange;
import omis.person.dao.AlternativeIdentityAssociationDao;
import omis.person.domain.AlternativeIdentityAssociation;
import omis.person.domain.AlternativeIdentityCategory;
import omis.person.domain.Person;
import omis.person.domain.PersonIdentity;

/**
 * Hibernate implementation of data access object for alternative identity
 * associations.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Nov 22, 2013)
 * @since OMIS 3.0
 */
public class AlternativeIdentityAssociationDaoHibernateImpl
		extends GenericHibernateDaoImpl<AlternativeIdentityAssociation>
		implements AlternativeIdentityAssociationDao {

	/* Query names. */
	
	private static final String FIND_BY_PERSON_QUERY_NAME
		= "findAlternativeIdentityAssociationsByPerson";
	
	private static final String FIND_BY_PERSON_ON_DATE_QUERY_NAME
		= "findAlternativeIdentityAssociationsByPersonOnDate";
	
	private static final String FIND_ALTERNATIVE_IDENTITY_ASSOCIATION_QUERY_NAME
		= "findAlternativeIdentityAssociation";
	
	private static final String 
	FIND_ALTERNATIVE_IDENTITY_ASSOCIATION_EXCLUDING_QUERY_NAME
		= "findAlternativeIdentityAssociationExcluding";
	
	/* Parameter names. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String DATE_PARAM_NAME = "date";
	
	private static final String IDENTITY_PARAM_NAME = "identity";
	
	private static final String ALTERNATIVE_IDENTITY_CATEGORY_PARAM_NAME
		= "category";
	
	private static final String ASSOCIATION_PARAM_NAME = "association";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	
	private static final String END_DATE_PARAM_NAME = "endDate";
	
	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * alternative identity associations.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public AlternativeIdentityAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}
	
	/** {@inheritDoc} */
	@Override
	public List<AlternativeIdentityAssociation> findByPerson(
			final Person person) {
		@SuppressWarnings("unchecked")
		List<AlternativeIdentityAssociation> associations
			= this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.list();
		return associations;
	}

	/** {@inheritDoc} */
	@Override
	public List<AlternativeIdentityAssociation> findByPersonOnDate(
			final Person person, final Date date) {
		@SuppressWarnings("unchecked")
		List<AlternativeIdentityAssociation> associations
			= this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_PERSON_ON_DATE_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setParameter(DATE_PARAM_NAME, date)
				.list();
		return associations;
	}

	/** {@inheritDoc} */
	@Override
	public AlternativeIdentityAssociation find(final PersonIdentity identity,
			final AlternativeIdentityCategory category, 
			final DateRange dateRange) {
		AlternativeIdentityAssociation association = 
				(AlternativeIdentityAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_ALTERNATIVE_IDENTITY_ASSOCIATION_QUERY_NAME)
				.setParameter(IDENTITY_PARAM_NAME, identity)
				.setParameter(ALTERNATIVE_IDENTITY_CATEGORY_PARAM_NAME, 
						category)
				.setTimestamp(START_DATE_PARAM_NAME,
						DateRange.getStartDate(dateRange))
				.setTimestamp(END_DATE_PARAM_NAME,
						DateRange.getEndDate(dateRange))
				.uniqueResult();
		return association;
	}
	
	/** {@inheritDoc} */
	@Override
	public AlternativeIdentityAssociation findExcluding (
			final AlternativeIdentityAssociation association, 
			final PersonIdentity identity,
			final AlternativeIdentityCategory category, 
			final DateRange dateRange) {
		AlternativeIdentityAssociation resultAssociation = 
				(AlternativeIdentityAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
					FIND_ALTERNATIVE_IDENTITY_ASSOCIATION_EXCLUDING_QUERY_NAME)
				.setParameter(ASSOCIATION_PARAM_NAME, association)
				.setParameter(IDENTITY_PARAM_NAME, identity)
				.setParameter(ALTERNATIVE_IDENTITY_CATEGORY_PARAM_NAME, 
						category)
				.setTimestamp(START_DATE_PARAM_NAME,
						DateRange.getStartDate(dateRange))
				.setTimestamp(END_DATE_PARAM_NAME,
						DateRange.getEndDate(dateRange))
				.uniqueResult();
		return resultAssociation;
	}
}