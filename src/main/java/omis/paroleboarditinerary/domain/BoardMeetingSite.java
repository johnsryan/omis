package omis.paroleboarditinerary.domain;

import java.util.Date;

import omis.audit.domain.Creatable;
import omis.audit.domain.Updatable;
import omis.facility.domain.Unit;
import omis.location.domain.Location;

/**
 * Board meeting site.
 *
 * @author Josh Divine
 * @version 0.1.1 (Apr 10, 2018)
 * @since OMIS 3.0
 */
public interface BoardMeetingSite extends Creatable, Updatable {

	/**
	 * Sets the ID of the board meeting site.
	 *
	 * @param id ID
	 */
	void setId(Long id);
	
	/**
	 * Return the ID of the board meeting site.
	 *
	 * @return ID
	 */
	Long getId();
	
	/**
	 * Sets the parole board itinerary.
	 * 
	 * @param boardItinerary parole board itinerary
	 */
	void setBoardItinerary(ParoleBoardItinerary boardItinerary);
	
	/**
	 * Returns the parole board itinerary.
	 * 
	 * @return parole board itinerary
	 */
	ParoleBoardItinerary getBoardItinerary();
	
	/**
	 * Sets the location.
	 * 
	 * @param location location
	 */
	void setLocation(Location location);
	
	/**
	 * Returns the location.
	 * 
	 * @return location
	 */
	Location getLocation();
	
	/**
	 * Sets the date.
	 * 
	 * @param date date
	 */
	void setDate(Date date);
	
	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	Date getDate();
	
	/**
	 * Sets the order.
	 * 
	 * @param order order
	 */
	void setOrder(Integer order);
	
	/**
	 * Returns the order.
	 * 
	 * @return order
	 */
	Integer getOrder();
	
	/**
	 * Sets the unit.
	 * 
	 * @param unit unit
	 */
	void setUnit(Unit unit);
	
	/**
	 * Returns the unit.
	 * 
	 * @return unit
	 */
	Unit getUnit();
}