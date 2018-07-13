package omis.visitation.dao.impl.hibernate;

import java.util.Date;
import java.util.List;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.datatype.DateRange;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.visitation.dao.VisitationAssociationDao;
import omis.visitation.domain.VisitationAssociation;

import org.hibernate.SessionFactory;

/**
 * Visitation Association data access object hibernate implementation.
 * 
 * @author Joel Norris
 * @author Yidong Li
 * @author Stephen Abson
 * @version 0.1.2 (June 1, 2016)
 * @since OMIS 3.0
 */
public class VisitationAssociationDaoHibernateImpl 
	extends GenericHibernateDaoImpl<VisitationAssociation>
	implements VisitationAssociationDao {

	/* Query names. */
	
	private static final String FIND_VISITATION_ASSOCIATION_QUERY_NAME
		= "findVisitationAssociation";
	
	private static final String 
	FIND_SPECIAL_VISIT_VISITATION_ASSOCIATIONS_BY_OFFENDER_QUERY_NAME
		=  "findSpecialVisitVisitationAssociationsByOffender";
	
	private static final String 
	FIND_APPROVED_VISITATION_ASSOCIATIONS_QUERY_NAME_BY_OFFENDER 
		= "findApprovedVisitationAssociationsByOffender";
	
	private static final String 
	FIND_VISITATION_ASSOCIATIONS_BY_OFFENDER_QUERY_NAME
		= "findVisitationAssociationByOffender";
	
	private static final String 
	FIND_VISITATION_ASSOCIATION_BY_DATE_RANGE_QUERY_NAME
		= "findVisitationAssociationByDateRange";
	
	private static final String 
	FIND_VISITATION_ASSOCIATION_EXCLUDING_QUERY_NAME
		= "findVisitationAssociationExcluding";
	
	private static final String FIND_VISITATION_ASSOCIATION_ON_DATE
		= "findVisitationAssociaitonOnDate";
	
	private static final String
	FIND_VISITABLE_VISITATION_ASSOCIATIONS_QUERY_NAME
		= "findVisitableVisitationAssociations";
	
	private static final String FIND_DATE_RANGE_OVERLAP_QUERY_NAME
		= "findVisitationAssociationDateRangeOverlap";
	
	private static final String FIND_DATE_RANGE_OVERLAP_EXCLUDING_QUERY_NAME
		= "findVisitationAssociationDateRangeOverlapExcluding";
	
	private static final String FIND_BY_PERSON_QUERY_NAME
		= "findVisitationAssociationsByPerson";
	
	private static final String COUNT_BY_RELATIONSHIP_QUERY_NAME
		= "countVisitationAssociationsByRelationship";

	private static final String DELETE_BY_RELATIONSHIP_QUERY_NAME
		= "deleteVisitationAssociationsByRelationship";
	
	/* Parameter names. */
	
	private static final String RELATIONSHIP_PARAMETER_NAME = "relationship";
	
	private static final String START_DATE_PARAMETER_NAME = "startDate";
	
	private static final String END_DATE_PARAMETER_NAME = "endDate";
	
	private static final String DATE_PARAMETER_NAME = "date";
	
	private static final String OFFENDER_PARAMETER_NAME = "offender";
	
	private static final String VISITATION_ASSOCIATION_PARAM_NAME 
		= "visitationAssociation";
	
	private static final String START_DATE_PARAM_NAME = "startDate";
	private static final String END_DATE_PARAM_NAME = "endDate";
	private static final String RELATIONSHIP_PARAM_NAME = "relationship";
	private static final String PERSON_PARAM_NAME = "person";
	
	/**
	 * Instantiates a data access object for visitation association with 
	 * the specified session factory and entity name.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public VisitationAssociationDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociation> findVisitationAssociationByOffender(
			final Offender offender, final Date date) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociation> visitationAssociations = 
				getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_VISITATION_ASSOCIATIONS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setTimestamp(DATE_PARAMETER_NAME, date)
				.list();
		
		return visitationAssociations;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociation> 
	findApprovedVisitationAssociationsByOffender(final Offender offender,
			final Date date) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociation> visitationAssociations = 
				getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
			FIND_APPROVED_VISITATION_ASSOCIATIONS_QUERY_NAME_BY_OFFENDER)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setTimestamp(DATE_PARAMETER_NAME, date)
				.list();
		
		return visitationAssociations;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociation> 
	findSpecialVisitVisitationAssociationsByOffender(final Offender offender,
			final Date date) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociation> visitationAssociations = 
				getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
			FIND_SPECIAL_VISIT_VISITATION_ASSOCIATIONS_BY_OFFENDER_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setDate(DATE_PARAMETER_NAME, date)
				.list();
		
		return visitationAssociations;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation find(Relationship relationship, Date startDate) {
		VisitationAssociation visitationAssociationFound = 
				(VisitationAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_VISITATION_ASSOCIATION_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAMETER_NAME, relationship)
				.setDate(START_DATE_PARAMETER_NAME, startDate)
				.uniqueResult();
		return visitationAssociationFound;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation findByDateRange(final Relationship relationship, 
			final Date startDate, final Date endDate) {
		VisitationAssociation visitationAssociationFound = 
				(VisitationAssociation) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_VISITATION_ASSOCIATION_BY_DATE_RANGE_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAMETER_NAME, relationship)
				.setDate(START_DATE_PARAMETER_NAME, startDate)
				.setDate(END_DATE_PARAMETER_NAME, endDate)
				.uniqueResult();
		return visitationAssociationFound;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation findExcluding(
			VisitationAssociation association, Relationship relationship,
			Date startDate) {
		VisitationAssociation visitationAssociation = (VisitationAssociation)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_VISITATION_ASSOCIATION_EXCLUDING_QUERY_NAME)
				.setParameter(VISITATION_ASSOCIATION_PARAM_NAME, association)
				.setParameter(RELATIONSHIP_PARAMETER_NAME, relationship)
				.setDate(START_DATE_PARAMETER_NAME, startDate)
				.uniqueResult();
		return visitationAssociation;
	}

	/** {@inheritDoc} */
	@Override
	public VisitationAssociation findOnDate(final Relationship relationship,
			final Date date) {
		VisitationAssociation visitationAssociation = (VisitationAssociation)
				this.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_VISITATION_ASSOCIATION_ON_DATE)
				.setParameter(RELATIONSHIP_PARAMETER_NAME, relationship)
				.setDate(DATE_PARAMETER_NAME, date)
				.uniqueResult();
		return visitationAssociation;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociation> findVisitableVisitationAssociations(
			Offender offender, Date date) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociation> associations = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(
						FIND_VISITABLE_VISITATION_ASSOCIATIONS_QUERY_NAME)
				.setParameter(OFFENDER_PARAMETER_NAME, offender)
				.setDate(DATE_PARAMETER_NAME, date)
				.list();
		return associations;
	}
	
	/** {@inheritDoc} */
	@Override
	public long findDateRangeOverLap(
		Relationship relationship, DateRange dateRange) {
		long overlaps = (long) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_DATE_RANGE_OVERLAP_QUERY_NAME)
			.setDate(START_DATE_PARAM_NAME, dateRange.getStartDate())
			.setDate(END_DATE_PARAM_NAME, dateRange.getEndDate())
			.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
			.uniqueResult();
		return overlaps;
	}

	/** {@inheritDoc} */
	@Override
	public long findDateRangeOverLapExcluding(Relationship relationship, DateRange dateRange,
			VisitationAssociation association) {
		long overlaps = (long) this.getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(FIND_DATE_RANGE_OVERLAP_EXCLUDING_QUERY_NAME)
			.setDate(START_DATE_PARAM_NAME, dateRange.getStartDate())
			.setDate(END_DATE_PARAM_NAME, dateRange.getEndDate())
			.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
			.setParameter(VISITATION_ASSOCIATION_PARAM_NAME, association)
			.uniqueResult();
		return overlaps;
	}

	/** {@inheritDoc} */
	@Override
	public List<VisitationAssociation> findByPerson(Person person) {
		@SuppressWarnings("unchecked")
		List<VisitationAssociation> associations = this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_BY_PERSON_QUERY_NAME)
				.setParameter(PERSON_PARAM_NAME, person)
				.list();
		return associations;
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

	/** {@inheritDoc} */
	@Override
	public int removeByRelationship(final Relationship relationship) {
		return this.getSessionFactory().getCurrentSession()
				.getNamedQuery(DELETE_BY_RELATIONSHIP_QUERY_NAME)
				.setParameter(RELATIONSHIP_PARAM_NAME, relationship)
				.executeUpdate();
	}
}