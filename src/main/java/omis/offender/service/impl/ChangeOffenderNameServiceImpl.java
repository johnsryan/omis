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
package omis.offender.service.impl;

import java.util.Date;
import java.util.List;

import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.domain.Offender;
import omis.offender.service.ChangeOffenderNameService;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.AlternativeNameCategory;
import omis.person.domain.PersonName;
import omis.person.domain.Suffix;
import omis.person.service.delegate.AlternativeNameAssociationDelegate;
import omis.person.service.delegate.AlternativeNameCategoryDelegate;
import omis.person.service.delegate.PersonNameDelegate;
import omis.person.service.delegate.SuffixDelegate;

/**
 * ChangeOffenderNameServiceImpl.java
 * 
 *@author Annie Jacques 
 *@version 0.1.0 (Mar 8, 2017)
 *@since OMIS 3.0
 *
 */
public class ChangeOffenderNameServiceImpl implements ChangeOffenderNameService {
	
	private final AlternativeNameAssociationDelegate
			alternativeNameAssociationDelegate;
	
	private final PersonNameDelegate personNameDelegate;
	
	private final OffenderDelegate offenderDelegate;
	
	private final AlternativeNameCategoryDelegate
			alternativeNameCategoryDelegate;
	
	private final SuffixDelegate suffixDelegate;
	
	

	/**
	 * @param alternativeNameAssociationDelegate
	 * @param personNameDelegate
	 * @param offenderDelegate
	 * @param alternativeNameCategoryDelegate
	 * @param suffixDelegate
	 */
	public ChangeOffenderNameServiceImpl(
			final AlternativeNameAssociationDelegate
					alternativeNameAssociationDelegate,
			final PersonNameDelegate personNameDelegate,
			final OffenderDelegate offenderDelegate,
			final AlternativeNameCategoryDelegate alternativeNameCategoryDelegate,
			final SuffixDelegate suffixDelegate) {
		this.alternativeNameAssociationDelegate =
				alternativeNameAssociationDelegate;
		this.personNameDelegate = personNameDelegate;
		this.offenderDelegate = offenderDelegate;
		this.alternativeNameCategoryDelegate = alternativeNameCategoryDelegate;
		this.suffixDelegate = suffixDelegate;
	}

	/**{@inheritDoc} */
	@Override
	public Offender change(
			final Offender offender, final String lastName,
			final String firstName, final String middleName, final String suffix,
			final Date effectiveDate,
			final AlternativeNameCategory previousNameCategory)
					throws DuplicateEntityFoundException {
		
		PersonName previousName = offender.getName();
		
		this.alternativeNameAssociationDelegate.create(previousName,
				new DateRange(null, effectiveDate), previousNameCategory);
		
		PersonName newName = this.personNameDelegate
				.create(offender, lastName, firstName, middleName, suffix);
		
		return this.offenderDelegate.updateOffender(offender, newName,
				offender.getIdentity());
	}

	/**{@inheritDoc} */
	@Override
	public List<AlternativeNameCategory> findAlternativeNameCategories() {
		return this.alternativeNameCategoryDelegate.findAll();
	}

	/**{@inheritDoc} */
	@Override
	public List<Suffix> findSuffixes() {
		return this.suffixDelegate.findAll();
	}

}
