package omis.person.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.country.domain.Country;
import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.demographics.domain.Sex;
import omis.person.dao.PersonIdentityDao;
import omis.person.domain.Person;
import omis.person.domain.PersonIdentity;
import omis.region.domain.City;

import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of data access object for person identities.
 * 
 * @author Stephen Abson
 * @author Joel Norris
 * @author Yidong Li
 * @version 0.1.1 (June 1, 2015)
 * @since OMIS 3.0
 */
public class PersonIdentityDaoHibernateImpl
		extends GenericHibernateDaoImpl<PersonIdentity>
		implements PersonIdentityDao {

	/* Query names. */
	
	private static final String FIND_ALTERNATIVE_IDENTITIES_QUERY_NAME
			= "findAlternativePersonIdentities";
	
	private static final String FIND_IDENTITY_QUERY_NAME = "findPersonIdentity";
	
	private static final String FIND_IDENTITY_EXCLUDING_QUERY_NAME
		= "findPersonIdentityExcluding";
	
	private static final String FIND_IDENTITY_BY_PERSON_QUERY_NAME 
		= "findPersonIdentityByPerson";
	
	/* Parameter names. */
	
	private static final String PERSON_PARAM_NAME = "person";
	
	private static final String BIRTH_DATE_PARAM_NAME = "birthDate";
	
	private static final String BIRTH_CITY_PARAM_NAME = "birthCity";
	
	private static final String BIRTH_COUNTRY_PARAM_NAME = "birthCountry";
	
	private static final String PERSON_IDENTITY_PARAM_NAME = "personIdentity";
	
	private static final String SOCIAL_SECURITY_NUMBER_PARAM_NAME 
		= "socialSecurityNumber";
	
	private static final String SEX_PARAM_NAME = "sex";

	/**
	 * Instantiates an Hibernate implementation of data access object for
	 * person identities.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public PersonIdentityDaoHibernateImpl(
			final SessionFactory sessionFactory,
			final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<PersonIdentity> findAlternativeIdentities(final Person person) {
		@SuppressWarnings("unchecked")
		List<PersonIdentity> personIdentities = (List<PersonIdentity>)
				this.getSessionFactory()
				.getCurrentSession().getNamedQuery(
						FIND_ALTERNATIVE_IDENTITIES_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.list();
		return personIdentities;
	}

	/** {@inheritDoc} */
	@Override
	public PersonIdentity find(final Person person, final Date birthDate, 
			final City birthCity, final Country birthCountry, 
			final Integer socialSecurityNumber, final Sex sex) {
		PersonIdentity identity = (PersonIdentity) 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_IDENTITY_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.setParameter(SOCIAL_SECURITY_NUMBER_PARAM_NAME, socialSecurityNumber)
				.setDate(BIRTH_DATE_PARAM_NAME, birthDate)
				.setParameter(BIRTH_CITY_PARAM_NAME, birthCity)
				.setParameter(BIRTH_COUNTRY_PARAM_NAME, birthCountry)
				.setParameter(SEX_PARAM_NAME, sex)
				.uniqueResult();
		return identity;
	}

	/** {@inheritDoc} */
	@Override
	public PersonIdentity findExcluding(final PersonIdentity personIdentity,
			final Integer socialSecurityNumber, final Date birthDate, 
			final City birthCity, final Country birthCountry, final Sex sex) {
		PersonIdentity identity = (PersonIdentity) 
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_IDENTITY_EXCLUDING_QUERY_NAME)
				.setParameter(PERSON_IDENTITY_PARAM_NAME, personIdentity)
				.setParameter(PERSON_PARAM_NAME, personIdentity.getPerson())
				.setParameter(SOCIAL_SECURITY_NUMBER_PARAM_NAME, socialSecurityNumber)
				.setDate(BIRTH_DATE_PARAM_NAME, birthDate)
				.setParameter(BIRTH_CITY_PARAM_NAME, birthCity)
				.setParameter(BIRTH_COUNTRY_PARAM_NAME, birthCountry)
				.setParameter(SEX_PARAM_NAME, sex)
				.uniqueResult();
		return identity;
	}
	
	/** {@inheritDoc} */
	@Override
	public PersonIdentity findByPerson(final Person person) {
		PersonIdentity identity = (PersonIdentity) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_IDENTITY_BY_PERSON_QUERY_NAME)
			.setParameter(PERSON_PARAM_NAME, person)
			.uniqueResult();
		return identity;
	}
}