package omis.chronologicalnote.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Chronological note form.
 * 
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.0 (February 7, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private Date date;
	private Date dateTime;
	private String narrative;
	private List<ChronologicalNoteCategoryItem> items = new ArrayList<ChronologicalNoteCategoryItem>();
	
	/**
	 * Instantiates a default instance of chronological note form.
	 */
	public ChronologicalNoteForm() {
		//Default constructor.
	}

	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	/**
	 * Returns date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets date.
	 * 
	 * @param date date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}
	
	/**
	 * Returns date time.
	 * 
	 * @return time
	 */
	public Date getDateTime() {
		return this.dateTime;
	}

	/**
	 * Sets date time.
	 * 
	 * @param dateTime date time
	 */
	public void setDateTime(final Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Returns narrative.
	 * 
	 * @return narrative
	 */
	public String getNarrative() {
		return this.narrative;
	}

	/**
	 * Sets narrative.
	 * 
	 * @param narrative narrative
	 */
	public void setNarrative(final String narrative) {
		this.narrative = narrative;
	}

	/**
	 * Returns chronological note category items.
	 * 
	 * @return list of chronological note category items
	 */
	public List<ChronologicalNoteCategoryItem> getItems() {
		return this.items;
	}

	/**
	 * Sets chronological note category items.
	 * 
	 * @param items chronological note category items
	 */
	public void setItems(final List<ChronologicalNoteCategoryItem> items) {
		this.items = items;
	}
}
