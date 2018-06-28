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
package omis.hearing.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.exception.DuplicateEntityFoundException;
import omis.hearing.dao.InfractionDao;
import omis.hearing.domain.Hearing;
import omis.hearing.domain.Infraction;
import omis.hearing.domain.InfractionPlea;
import omis.hearing.domain.component.Resolution;
import omis.hearing.exception.InfractionExistsException;
import omis.instance.factory.InstanceFactory;
import omis.violationevent.domain.ConditionViolation;
import omis.violationevent.domain.DisciplinaryCodeViolation;

/**
 * Infraction Delegate.
 * 
 *@author Annie Wahl
 *@version 0.1.1 (Feb 28, 2018)
 *@since OMIS 3.0
 *
 */
public class InfractionDelegate {
	
	
	private static final String INFRACTION_EXISTS_FOUND_MSG =
			"Infraction already exists with given Hearing, ConditionViolation, "
			+ "and DisciplinaryCodeViolation.";
	
	private final InfractionDao infractionDao;
	
	private final InstanceFactory<Infraction> 
		infractionInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Constructor for Infraction Delegate.
	 * @param infractionDao - Infraction DAO
	 * @param infractionInstanceFactory - Infraction Instance Factory
	 * @param auditComponentRetriever - Audit Component Retriever
	 */
	public InfractionDelegate(
			final InfractionDao infractionDao,
			final InstanceFactory<Infraction> 
				infractionInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.infractionDao = infractionDao;
		this.infractionInstanceFactory = infractionInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates a new Infraction with the specified properties.
	 * @param hearing - Hearing
	 * @param conditionViolation - Condition Violation
	 * @param disciplinaryCodeViolation - Disciplinary Code Violation
	 * @param resolution - Resolution
	 * @param plea - Infraction Plea
	 * @return Newly created Infraction
	 * @throws DuplicateEntityFoundException - When an Infraction already exists
	 * with given Hearing, ConditionViolation, and DisciplinaryCodeViolation.
	 * @throws DuplicateEntityFoundException
	 */
	public Infraction create(final Hearing hearing,
			final ConditionViolation conditionViolation,
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final Resolution resolution, final InfractionPlea plea)
					throws InfractionExistsException {
		if (this.infractionDao.find(hearing, conditionViolation,
				disciplinaryCodeViolation) != null) {
			throw new InfractionExistsException(INFRACTION_EXISTS_FOUND_MSG);
		}
		
		Infraction infraction = 
				this.infractionInstanceFactory.createInstance();
		
		
		infraction.setConditionViolation(conditionViolation);
		infraction.setDisciplinaryCodeViolation(disciplinaryCodeViolation);
		infraction.setResolution(resolution);
		infraction.setHearing(hearing);
		infraction.setPlea(plea);
		infraction.setCreationSignature(
				new CreationSignature(
						this.auditComponentRetriever.retrieveUserAccount(), 
						this.auditComponentRetriever.retrieveDate()));
		infraction.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.infractionDao.makePersistent(infraction);
	}
	
	/**
	 * Updates an Infraction with the specified properties.
	 * @param infraction - Infraction to update
	 * @param conditionViolation - ConditionViolation
	 * @param disciplinaryCodeViolation - DisciplinaryCodeViolation
	 * @param resolution - Resolution
	 * @param plea - Infraction Plea
	 * @return Updated Infraction
	 * @throws DuplicateEntityFoundException - When an Infraction already exists
	 * with given Hearing, ConditionViolation, and DisciplinaryCodeViolation.
	 */
	public Infraction update(final Infraction infraction,
			final ConditionViolation conditionViolation,
			final DisciplinaryCodeViolation disciplinaryCodeViolation,
			final Resolution resolution, final InfractionPlea plea)
					throws InfractionExistsException {
		if (this.infractionDao.findExcluding(infraction.getHearing(),
				conditionViolation, disciplinaryCodeViolation,
				infraction) != null) {
			throw new InfractionExistsException(INFRACTION_EXISTS_FOUND_MSG);
		}
		
		infraction.setConditionViolation(conditionViolation);
		infraction.setDisciplinaryCodeViolation(disciplinaryCodeViolation);
		infraction.setResolution(resolution);
		infraction.setPlea(plea);
		infraction.setUpdateSignature(
				new UpdateSignature(
						this.auditComponentRetriever.retrieveUserAccount(),
						this.auditComponentRetriever.retrieveDate()));
		
		return this.infractionDao.makePersistent(infraction);
	}
	
	/**
	 * Removes an Infraction.
	 * @param infraction - Infraction to remove
	 */
	public void remove(final Infraction infraction) {
		this.infractionDao.makeTransient(infraction);
	}
	
	/**
	 * Returns a list of Infractions by specified Hearing.
	 * @param hearing - Hearing
	 * @return List of Infractions by specified Hearing
	 */
	public List<Infraction> findByHearing(final Hearing hearing) {
		return this.infractionDao.findByHearing(hearing);
	}
	
}
