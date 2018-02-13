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
package omis.medicalreview.service.delegate;
import java.util.List;
import omis.instance.factory.InstanceFactory;
import omis.medicalreview.dao.MedicalHealthClassificationDao;
import omis.medicalreview.domain.MedicalHealthClassification;

/**
 * Medical Health Classification Delegate.
 * 
 *@author Annie Wahl 
 *@version 0.1.0 (Jan 31, 2018)
 *@since OMIS 3.0
 *
 */
public class MedicalHealthClassificationDelegate {
	
	private final MedicalHealthClassificationDao medicalHealthClassificationDao;
	
	private final InstanceFactory<MedicalHealthClassification>
		medicalHealthClassificationInstanceFactory;

	/**
	 * Contructor for MedicalHealthClassificationDelegate.
	 * @param medicalHealthClassificationDao - Medical Health Classification DAO
	 * @param medicalHealthClassificationInstanceFactory - Medical Health
	 * Classification Instance Factory
	 */
	public MedicalHealthClassificationDelegate(
			final MedicalHealthClassificationDao medicalHealthClassificationDao,
			final InstanceFactory<MedicalHealthClassification>
				medicalHealthClassificationInstanceFactory) {
		this.medicalHealthClassificationDao = medicalHealthClassificationDao;
		this.medicalHealthClassificationInstanceFactory =
				medicalHealthClassificationInstanceFactory;
	}
	
	/**
	 * Returns a list of all valid Medical Health Classifications.
	 * @return List of all valid Medical Health Classifications
	 */
	public List<MedicalHealthClassification> findAll() {
		return this.medicalHealthClassificationDao.findAll();
	}
	
	/**
	* Creates a MedicalHealthClassification for use in unit tests.
	* @param name - String name
	* @param valid - Boolean valid
	* @return medicalHealthClassification
	*/
	public MedicalHealthClassification create(final String name,
			final Boolean valid) {
		MedicalHealthClassification medicalHealthClassification =
			this.medicalHealthClassificationInstanceFactory.createInstance();
		medicalHealthClassification.setName(name);
		medicalHealthClassification.setValid(valid);
		
		return this.medicalHealthClassificationDao.makePersistent(
				medicalHealthClassification);
	}
	
	
}
