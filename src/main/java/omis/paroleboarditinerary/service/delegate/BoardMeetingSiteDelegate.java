package omis.paroleboarditinerary.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.facility.domain.Unit;
import omis.instance.factory.InstanceFactory;
import omis.location.domain.Location;
import omis.paroleboarditinerary.dao.BoardMeetingSiteDao;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;

/**
 * Board meeting site delegate.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 10, 2018)
 * @since OMIS 3.0
 */
public class BoardMeetingSiteDelegate {

	/* Data access objects. */
	
	private final BoardMeetingSiteDao boardMeetingSiteDao;

	/* Instance factories. */
	
	private final InstanceFactory<BoardMeetingSite> 
			boardMeetingSiteInstanceFactory;
	
	/* Component Retrievers. */
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/** Instantiates an implementation of parole board itinerary note delegate 
	 * with the specified date access object and instance factory.
	 * 
	 * @param boardMeetingSiteDao board meeting site data access object
	 * @param boardMeetingSiteInstanceFactory board meeting site instance 
	 * factory
	 * @param auditComponentRetriever audit component retriever
	 */
	public BoardMeetingSiteDelegate(
			final BoardMeetingSiteDao boardMeetingSiteDao,
			final InstanceFactory<BoardMeetingSite> 
					boardMeetingSiteInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.boardMeetingSiteDao = boardMeetingSiteDao;
		this.boardMeetingSiteInstanceFactory = boardMeetingSiteInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new board meeting site.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @param location location
	 * @param unit unit
	 * @param date date
	 * @param order order
	 * @return board meeting site
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public BoardMeetingSite create(final ParoleBoardItinerary boardItinerary, 
			final Location location, final Unit unit, final Date date, 
			final Integer order) throws DuplicateEntityFoundException {
		if (this.boardMeetingSiteDao.find(boardItinerary, location, date, order)
				!= null) {
			throw new DuplicateEntityFoundException(
					"Board meeting site already exists.");
		}
		BoardMeetingSite meetingSite = this.boardMeetingSiteInstanceFactory
				.createInstance();
		meetingSite.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
		populateBoardMeetingSite(meetingSite, boardItinerary, location, unit, 
				date, order);
		return this.boardMeetingSiteDao.makePersistent(meetingSite);
	}

	/**
	 * Updates an existing board meeting site.
	 * 
	 * @param meetingSite board meeting site
	 * @param boardItinerary parole board itinerary
	 * @param location location
	 * @param unit unit
	 * @param date date
	 * @param order order
	 * @return board meeting site
	 * @throws DuplicateEntityFoundException if duplicate entity exists
	 */
	public BoardMeetingSite update(final BoardMeetingSite meetingSite, 
			final ParoleBoardItinerary boardItinerary, final Location location, 
			final Unit unit, final Date date, final Integer order) 
					throws DuplicateEntityFoundException {
		if (this.boardMeetingSiteDao.findExcluding(boardItinerary, location, 
				date, order, meetingSite) != null) {
			throw new DuplicateEntityFoundException(
					"Board meeting site already exists.");
		}
		populateBoardMeetingSite(meetingSite, boardItinerary, location, unit, 
				date, order);
		return this.boardMeetingSiteDao.makePersistent(meetingSite);
	}
	
	/**
	 * Removes the specified board meeting site.
	 * 
	 * @param meetingSite board meeting site
	 */
	public void remove(final BoardMeetingSite meetingSite) {
		this.boardMeetingSiteDao.makeTransient(meetingSite);
	}
	
	/**
	 * Returns a list of board meeting sites that match the specified parole 
	 * board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 * @return list of board meeting sites
	 */
	public List<BoardMeetingSite> findBoardMeetingSitesByBoardItinerary(
			final ParoleBoardItinerary boardItinerary) {
		return this.boardMeetingSiteDao
				.findBoardMeetingSitesByBoardItinerary(boardItinerary);
	}
	
	// Populates a board meeting site
	private void populateBoardMeetingSite(
			final BoardMeetingSite meetingSite, 
			final ParoleBoardItinerary boardItinerary, final Location location, 
			final Unit unit, final Date date, final Integer order) {
		meetingSite.setBoardItinerary(boardItinerary);
		meetingSite.setLocation(location);
		meetingSite.setUnit(unit);
		meetingSite.setDate(date);
		meetingSite.setOrder(order);
		meetingSite.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(), 
				this.auditComponentRetriever.retrieveDate()));
	}
}
