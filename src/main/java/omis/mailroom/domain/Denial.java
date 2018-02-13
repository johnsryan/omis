/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.mailroom.domain;

import java.io.Serializable;
import java.util.Date;

import omis.mail.domain.Mail;

/** Mailroom denial.
 * @author Ryan JOhns
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0 */
public interface Denial extends Serializable {
	/** Gets id.
	 * @return id. */
	Long getId();
	
	/** Gets comments.
	 * @return comments. */
	String getComments();
	
	/** Gets denial date.
	 * @return date. */
	Date getDate();
	
	/** Gets returned.
	 * @return returned to sender. */
	Boolean getReturned();
	
	/** Gets mail.
	 * @return mail. */
	Mail getMail();
	
	/** Sets id.
	 * @param id - id. */
	void setId(Long id);
	
	/** Sets comments.
	 * @param comments - comments. */
	void setComments(String comments);
	
	/**
	 * Sets the date.
	 * @param date date 
	 */
	void setDate(Date date);
	
	/** Sets returned.
	 * @param returned - returned. */
	void setReturned(Boolean returned);
	
	/** Sets mail.
	 * @param mail - mail. */
	void setMail(Mail mail);
}
