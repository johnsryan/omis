package omis.chronologicalnote.web.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Chronological note form.
 * 
 * @author Joel Norris
 * @version 0.1.0 (February 7, 2018)
 * @since OMIS 3.0
 */
public class ChronologicalNoteForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date date;
	private String narrative;
	private List<ChronologicalNoteCategoryItem> items = new ArrayList<ChronologicalNoteCategoryItem>();
	
	/**
	 * Instantiates a default instance of chronological note form.
	 */
	public ChronologicalNoteForm() {
		//Default constructor.
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
