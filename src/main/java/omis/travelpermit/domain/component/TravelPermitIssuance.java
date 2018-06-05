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
import java.util.Date;

import omis.user.domain.UserAccount;

/**
 * Implementation of travel permits issuance.
 * @author Yidong Li
 * @author Joel Norris
 * @version 0.1.1 (June 06, 2018)
 * @since OMIS 3.0 
 */
public class TravelPermitIssuance implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Date date;
	private UserAccount issuer;
	
	/* Constructors. */
	
	/**
	 * Instantiates a default instance of travel permit issuance.
	 */
	public TravelPermitIssuance() {
		//Default constructor.
	}
	
	/**
	 * Instantiates a travel permit issuance with the specified date and issuer.
	 * 
	 * @param date date
	 * @param issuer user account
	 */
	public TravelPermitIssuance(Date date, UserAccount issuer) {
		this.date = date;
		this.issuer = issuer;
	}
	
	/**
	 * Returns the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return this.date;
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
	 * Returns the issuer {@code UserAccount}.
	 * 
	 * @return issuer
	 */
	public UserAccount getIssuer() {
		return this.issuer;
	}

	/**
	 * Sets the issuer {@code UserAccount}.
	 * 
	 * @param issuer user account
	 */
	public void setIssuer(final UserAccount issuer) {
		this.issuer = issuer;
	}
}