package omis.paroleboarditinerary.web.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardmember.domain.ParoleBoardMember;

/**
 * Parole board itinerary form.
 *
 * @author Josh Divine
 * @author Annie Wahl 
 * @version 0.1.1 (Jan 23, 2018)
 * @since OMIS 3.0
 */
public class ParoleBoardItineraryForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date startDate;
	
	private Date endDate;
	
	private ParoleBoardLocation paroleBoardLocation;
	
	private OnsiteCategory onsite;
	
	private ParoleBoardMember boardMember1;

	private ParoleBoardMember boardMember2;

	private ParoleBoardMember boardMember3;

	private ParoleBoardMember boardMemberAlternate;
	
	private List<BoardMeetingSiteItem> boardMeetingSiteItems;
	
	private List<ParoleBoardItineraryNoteItem> boardItineraryNoteItems;
	
	/**
	 * Instantiates a default parole board itinerary form. 
	 */
	public ParoleBoardItineraryForm() {
		// Default instantiation
	}

	/**
	 * Returns the start date.
	 * 
	 * @return start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate start date
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns the end date.
	 * 
	 * @return end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 * 
	 * @param endDate end date
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the parole board location.
	 * 
	 * @return paroleBoardLocation - Parole Board Location
	 */
	public ParoleBoardLocation getParoleBoardLocation() {
		return this.paroleBoardLocation;
	}

	/**
	 * Sets the parole board location.
	 * 
	 * @param paroleBoardLocation - Parole Board Location
	 */
	public void setParoleBoardLocation(
			final ParoleBoardLocation paroleBoardLocation) {
		this.paroleBoardLocation = paroleBoardLocation;
	}

	/**
	 * Returns the on-site.
	 *
	 * @return on-site
	 */
	public OnsiteCategory getOnsite() {
		return onsite;
	}

	/**
	 * Sets the on-site.
	 *
	 * @param onsite on-site
	 */
	public void setOnsite(OnsiteCategory onsite) {
		this.onsite = onsite;
	}

	/**
	 * Returns the first board attendee.
	 * 
	 * @return first board attendee
	 */
	public ParoleBoardMember getBoardMember1() {
		return boardMember1;
	}

	/**
	 * Sets the first board attendee.
	 * 
	 * @param boardMember first board attendee
	 */
	public void setBoardMember1(final ParoleBoardMember boardMember) {
		this.boardMember1 = boardMember;
	}

	/**
	 * Returns the second board attendee.
	 *  
	 * @return second board attendee
	 */
	public ParoleBoardMember getBoardMember2() {
		return boardMember2;
	}

	/**
	 * Sets the second board attendee.
	 * 
	 * @param boardMember second board attendee
	 */
	public void setBoardMember2(final ParoleBoardMember boardMember) {
		this.boardMember2 = boardMember;
	}

	/**
	 * Returns the third board attendee.
	 *  
	 * @return third board attendee
	 */
	public ParoleBoardMember getBoardMember3() {
		return boardMember3;
	}

	/**
	 * Sets the third board attendee.
	 * 
	 * @param boardMember third board attendee
	 */
	public void setBoardMember3(final ParoleBoardMember boardMember) {
		this.boardMember3 = boardMember;
	}

	/**
	 * Returns the alternate board attendee.
	 *  
	 * @return the boardMemberAlternate
	 */
	public ParoleBoardMember getBoardMemberAlternate() {
		return boardMemberAlternate;
	}

	/**
	 * Sets the alternate board attendee.
	 * 
	 * @param boardMemberAlternate alternate board attendee
	 */
	public void setBoardMemberAlternate(
			final ParoleBoardMember boardMemberAlternate) {
		this.boardMemberAlternate = boardMemberAlternate;
	}

	/**
	 * Returns the board meeting site items.
	 * 
	 * @return board meeting site items
	 */
	public List<BoardMeetingSiteItem> getBoardMeetingSiteItems() {
		return boardMeetingSiteItems;
	}

	/**
	 * Sets the board meeting site items.
	 * 
	 * @param boardMeetingSiteItems board meeting site items
	 */
	public void setBoardMeetingSiteItems(
			final List<BoardMeetingSiteItem> boardMeetingSiteItems) {
		this.boardMeetingSiteItems = boardMeetingSiteItems;
	}

	/**
	 * Returns the parole board itinerary note items.
	 * 
	 * @return parole board itinerary note items
	 */
	public List<ParoleBoardItineraryNoteItem> getBoardItineraryNoteItems() {
		return boardItineraryNoteItems;
	}

	/**
	 * Sets the parole board itinerary note items.
	 * 
	 * @param boardItineraryNoteItems parole board itinerary note items
	 */
	public void setBoardItineraryNoteItems(
			final List<ParoleBoardItineraryNoteItem> boardItineraryNoteItems) {
		this.boardItineraryNoteItems = boardItineraryNoteItems;
	}
}
