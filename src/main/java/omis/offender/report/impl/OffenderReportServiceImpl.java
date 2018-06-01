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
package omis.offender.report.impl;

import java.util.Date;

import omis.demographics.domain.Sex;
import omis.offender.dao.OffenderDao;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.person.domain.Person;

/**
 * Implementation of offender report services.
 * 
 * @author Stephen Abson
 * @author Ryan Johns
 * @version 0.1.2 (Feb 2, 2016)
 * @since OMIS 3.0
 */
public class OffenderReportServiceImpl
				implements OffenderReportService {
	
	private final OffenderDao offenderDao;
	/**
	 * Instantiates an implementation of offender report service.
	 * 
	 * @param offenderDao data access object for offenders.*/
	public OffenderReportServiceImpl(
			final OffenderDao offenderDao) {
		this.offenderDao = offenderDao;
	}

	/** {@inheritDoc} */
	@Override
	public OffenderSummary summarizeOffender(final Offender offender) {
		// Retrieves offender name and identity information
		final Long id = offender.getId();
		final Integer offenderNumber = offender.getOffenderNumber();
		final String lastName = offender.getName().getLastName();
		final String firstName = offender.getName().getFirstName();
		final String middleName = offender.getName().getMiddleName();
		final String suffix = offender.getName().getSuffix();
		final Sex sex;
		final Date birthDate;
		final Integer socialSecurityNumber;
		
		if (offender.getIdentity() != null) {
			sex = offender.getIdentity().getSex();
			birthDate = offender.getIdentity().getBirthDate();
			socialSecurityNumber  =
					offender.getIdentity().getSocialSecurityNumber();
		} else {
			sex = null;
			birthDate = null;
			socialSecurityNumber = null;
		}
		
		// Create summary instance with values
		OffenderSummary offenderSummary = new OffenderSummary() {
			
			private static final long serialVersionUID = 1L;

			/** {@inheritDoc} */
			@Override
			public Long getId() {
				return id;
			}

			/** {@inheritDoc} */
			@Override
			public Integer getOffenderNumber() {
				return offenderNumber;
			}

			/** {@inheritDoc} */
			@Override
			public String getLastName() {
				return lastName;
			}

			/** {@inheritDoc} */
			@Override
			public String getFirstName() {
				return firstName;
			}
			
			/** {@inheritDoc} */
			@Override
			public String getMiddleName() {
				return middleName;
			}

			/** {@inheritDoc} */
			@Override
			public String getSuffix() {
				return suffix;
			}
			
			/** {@inheritDoc} */
			@Override
			public Sex getSex() {
				return sex;
			}
			
			/** {@inheritDoc} */
			@Override
			public Date getBirthDate() {
				return birthDate;
			}
			
			/** {@inheritDoc} */
			@Override
			public Integer getSocialSecurityNumber() {
				return socialSecurityNumber;
			}
		};
		return offenderSummary;
	}

	/** {@inheritDoc} */
	@Override
	public OffenderSummary summarizeOffender(
					final Offender offender, final Date date) {
		final Long id = offender.getId();
		final Integer offenderNumber = offender.getOffenderNumber();
		final String lastName = offender.getName().getLastName();
		final String firstName = offender.getName().getFirstName();
		final String middleName = offender.getName().getMiddleName();
		final String suffix = offender.getName().getSuffix();
		final Sex sex;
		final Date birthDate;
		final Integer socialSecurityNumber; 
		
		if (offender.getIdentity() != null) {
			sex = offender.getIdentity().getSex();
			birthDate = offender.getIdentity().getBirthDate();
			socialSecurityNumber =
					offender.getIdentity().getSocialSecurityNumber();
		} else {
			sex = null;
			birthDate = null;
			socialSecurityNumber = null;
		}
		
		// Create summary instance with values
		OffenderSummary offenderSummary = new OffenderSummary() {
			private static final long serialVersionUID = 1L;
				
			/** {@inheritDoc} */
			@Override
			public Long getId() {
				return id;
			}

			/** {@inheritDoc} */
			@Override
			public Integer getOffenderNumber() {
				return offenderNumber;
			}

			/** {@inheritDoc} */
			@Override
			public String getLastName() {
				return lastName;
			}

			/** {@inheritDoc} */
			@Override
			public String getFirstName() {
				return firstName;
			}
			
			/** {@inheritDoc} */
			@Override
			public String getMiddleName() {
				return middleName;
			}

			/** {@inheritDoc} */
			@Override
			public String getSuffix() {
				return suffix;
			}
			
			/** {@inheritDoc} */
			@Override
			public Sex getSex() {
				return sex;
			}
			
			/** {@inheritDoc} */
			@Override
			public Date getBirthDate() {
				return birthDate;
			}
			
			/** {@inheritDoc} */
			@Override
			public Integer getSocialSecurityNumber() {
				return socialSecurityNumber;
			}
		};
		return offenderSummary;
	}

	/** {@inheritDoc} */
	@Override
	public OffenderSummary summarizeIfOffender(
					final Person person) {
		if (this.offenderDao.findAsOffender(person) != null) {
			return this.summarizeOffender((Offender) person);
		} else {
			return null;
		}
	}
}