package omis.travelpermit.domain.component;
/*
 *  OMIS - Offender Management Information System
 *  Copyright (C) 2011 - 2017 State of Montana
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.Serializable;

/**
 * Other Travelers.
 * 
 * @author Yidong Li
 * @author Joel Norris
 * @author Sheronda Vaughn
 * @version 0.1.1 (June 6, 2018)
 * @since OMIS 3.0 
 */
public class OtherTravelers implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String persons;
	private String relationships;
	
	/** Instantiates a default instance of other travelers. */
	public OtherTravelers() {
		//Default constructor.
	}
	
	/**
	 * Instantiates an other travelers with the specified persons and relationships.
	 * 
	 * @param persons persons
	 * @param relationships relationships
	 */
	public OtherTravelers(final String persons, final String relationships) {
		this.persons = persons;
		this.relationships = relationships;
	}
	
	/**
	 * Returns persons.
	 * 
	 * @return persons
	 */
	public String getPersons() {
		return this.persons;
	}

	/**
	 * Sets persons.
	 * 
	 * @param persons persons
	 */
	public void setPersons(final String persons) {
		this.persons = persons;
	}
	
	/**
	 * Returns relationships.
	 * 
	 * @return relationships
	 */
	public String getRelationships() {
		return this.relationships;
	}

	/**
	 * Sets relationships. 
	 * 
	 * @param Relationships relationships
	 */
	public void setRelationships(final String Relationships) {
		this.relationships = Relationships;
	}
}