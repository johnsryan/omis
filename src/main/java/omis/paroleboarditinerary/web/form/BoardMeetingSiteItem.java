package omis.paroleboarditinerary.web.form;

import java.io.Serializable;
import java.util.Date;

import omis.facility.domain.Unit;
import omis.location.domain.Location;
import omis.paroleboarditinerary.domain.BoardMeetingSite;

/**
 * Board meeting site item.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 10, 2018)
 * @since OMIS 3.0
 */
public class BoardMeetingSiteItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private BoardMeetingSite boardMeetingSite;
	
	private Date date;
	
	private Location location;
	
	private Unit unit;
	
	private Integer order;
	
	private BoardMeetingSiteItemOperation operation;
	
	/** Instantiates a default note on a parole board itinerary form. */
	public BoardMeetingSiteItem() {
		// Default instantiation
	}

	/**
	 * Returns the board meeting site.
	 * 
	 * @return board meeting site
	 */
	public BoardMeetingSite getBoardMeetingSite() {
		return boardMeetingSite;
	}

	/**
	 * Sets the board meeting site.
	 * 
	 * @param boardMeetingSite board meeting site
	 */
	public void setBoardMeetingSite(final BoardMeetingSite boardMeetingSite) {
		this.boardMeetingSite = boardMeetingSite;
	}

	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Returns the location.
	 * 
	 * @return location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location location
	 */
	public void setLocation(final Location location) {
		this.location = location;
	}
	
	/**
	 * Returns the unit.
	 *
	 * @return unit
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * Sets the unit.
	 *
	 * @param unit unit
	 */
	public void setUnit(final Unit unit) {
		this.unit = unit;
	}

	/**
	 * Returns the order.
	 * 
	 * @return order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 * 
	 * @param order order
	 */
	public void setOrder(final Integer order) {
		this.order = order;
	}

	/**
	 * Returns the board meeting site item operation.
	 * 
	 * @return board meeting site item operation
	 */
	public BoardMeetingSiteItemOperation getOperation() {
		return operation;
	}

	/**
	 * Sets the board meeting site item operation.
	 * 
	 * @param operation board meeting site item operation
	 */
	public void setOperation(
			final BoardMeetingSiteItemOperation operation) {
		this.operation = operation;
	}
}
