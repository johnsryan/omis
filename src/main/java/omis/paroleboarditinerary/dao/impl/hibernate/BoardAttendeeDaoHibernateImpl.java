package omis.paroleboarditinerary.dao.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import omis.dao.impl.hibernate.GenericHibernateDaoImpl;
import omis.paroleboarditinerary.dao.BoardAttendeeDao;
import omis.paroleboarditinerary.domain.AttendeeRoleCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboardmember.domain.ParoleBoardMember;

/**
 * Hibernate implementation of the board attendee data access object.
 *
 * @author Josh Divine
 * @version 0.1.0 (Nov 20, 2017)
 * @since OMIS 3.0
 */
public class BoardAttendeeDaoHibernateImpl 
		extends GenericHibernateDaoImpl<BoardAttendee>
		implements BoardAttendeeDao {

	/* Queries. */
	
	private static final String FIND_QUERY_NAME = "findBoardAttendee";
	
	private static final String FIND_EXCLUDING_QUERY_NAME = 
			"findBoardAttendeeExcluding";
	
	private static final String FIND_BY_BOARD_ITINERARY_PARAM_NAME =
			"findBoardAttendeesByBoardItinerary";
	
	private static final String FIND_ALTERNATE_BY_BOARD_ITINERARY_PARAM_NAME =
			"findBoardAttendeeAlternateByBoardItinerary";
	
	/* Parameters. */
	
	private static final String BOARD_ITINERARY_PARAM_NAME = "boardItinerary";
	
	private static final String BOARD_MEMBER_PARAM_NAME = "boardMember";
	
	private static final String NUMBER_PARAM_NAME = "number";
	
	private static final String ROLE_PARAM_NAME = "role";
	
	private static final String EXCLUDED_BOARD_ATTENDEE_PARAM_NAME = 
			"excludedBoardAttendee";

	/** Instantiates a hibernate implementation of the data access object for 
	 *  board attendee.
	 * 
	 * @param sessionFactory session factory
	 * @param entityName entity name
	 */
	public BoardAttendeeDaoHibernateImpl(
			final SessionFactory sessionFactory, final String entityName) {
		super(sessionFactory, entityName);
	}

	/** {@inheritDoc} */
	@Override
	public BoardAttendee find(final ParoleBoardItinerary boardItinerary, 
			final ParoleBoardMember boardMember, final Long number, 
			final AttendeeRoleCategory role) {
		BoardAttendee attendee = (BoardAttendee) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_QUERY_NAME)
				.setParameter(BOARD_ITINERARY_PARAM_NAME, boardItinerary)
				.setParameter(BOARD_MEMBER_PARAM_NAME, boardMember)
				.setParameter(NUMBER_PARAM_NAME, number)
				.setParameter(ROLE_PARAM_NAME, role)
				.uniqueResult();
		return attendee;
	}

	/** {@inheritDoc} */
	@Override
	public BoardAttendee findExcluding(
			final ParoleBoardItinerary boardItinerary, 
			final ParoleBoardMember boardMember, final Long number, 
			final AttendeeRoleCategory role, 
			final BoardAttendee excludedAttendee) {
		BoardAttendee attendee = (BoardAttendee) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_EXCLUDING_QUERY_NAME)
				.setParameter(BOARD_ITINERARY_PARAM_NAME, boardItinerary)
				.setParameter(BOARD_MEMBER_PARAM_NAME, boardMember)
				.setParameter(NUMBER_PARAM_NAME, number)
				.setParameter(ROLE_PARAM_NAME, role)
				.setParameter(EXCLUDED_BOARD_ATTENDEE_PARAM_NAME, 
						excludedAttendee)
				.uniqueResult();
		return attendee;
	}

	/** {@inheritDoc} */
	@Override
	public List<BoardAttendee> findBoardAttendeesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		@SuppressWarnings("unchecked")
		List<BoardAttendee> attendees = (List<BoardAttendee>) this
				.getSessionFactory().getCurrentSession()
				.getNamedQuery(FIND_BY_BOARD_ITINERARY_PARAM_NAME)
				.setParameter(BOARD_ITINERARY_PARAM_NAME, boardItinerary)
				.list();
		return attendees;
	}

	/** {@inheritDoc} */
	@Override
	public BoardAttendee findBoardAlternateAttendeeByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		BoardAttendee attendee = (BoardAttendee) this.getSessionFactory()
				.getCurrentSession()
				.getNamedQuery(FIND_ALTERNATE_BY_BOARD_ITINERARY_PARAM_NAME)
				.setParameter(BOARD_ITINERARY_PARAM_NAME, boardItinerary)
				.uniqueResult();
		return attendee;
	}


}
