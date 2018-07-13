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

package omis.victim.service.delegate;

import java.util.Date;
import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.audit.domain.UpdateSignature;
import omis.instance.factory.InstanceFactory;
import omis.offender.domain.Offender;
import omis.person.domain.Person;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.victim.dao.VictimAssociationDao;
import omis.victim.domain.VictimAssociation;
import omis.victim.domain.component.VictimAssociationFlags;
import omis.victim.exception.VictimExistsException;

/**
 * Delegate for victim associations.
 *
 * @author Stephen Abson
 * @author Yidong Li
 * @author Sheronda Vaughn
 * @version 0.0.1 (May 20, 2015)
 * @since OMIS 3.0
 */
public class VictimAssociationDelegate {

	/* Resources. */
	
	private final VictimAssociationDao victimAssociationDao;
	
	private final InstanceFactory<VictimAssociation>
	victimAssociationInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;
	
	/* Constructors. */
	
	/**
	 * Instantiates delegate for victim associations.
	 * 
	 * @param victimAssociationDao data access object for victim associations
	 * @param victimAssociationInstanceFactory instance factory for victim
	 * associations
	 * @param auditComponentRetriever audit component retriever
	 */
	public VictimAssociationDelegate(
			final VictimAssociationDao victimAssociationDao,
			final InstanceFactory<VictimAssociation>
				victimAssociationInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.victimAssociationDao = victimAssociationDao;
		this.victimAssociationInstanceFactory
			= victimAssociationInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/* Methods. */
	
	/**
	 * Creates victim association.
	 * 
	 * @param relationship relationship
	 * @param registeredDate date registered
	 * @param packetSent whether packet is sent 
	 * @param packetSendDate date packet is sent
	 * @param flags flags
	 * @return new victim association
	 * @throws VictimExistsException if victim association exists
	 * @throws ReflexiveRelationshipException if offender equals victim
	 */
	public VictimAssociation create(final Relationship relationship,
			final Date registeredDate, final Boolean packetSent,
			final Date packetSendDate,
			final VictimAssociationFlags flags)
					throws VictimExistsException {
		if (this.victimAssociationDao.find(relationship) != null) {
			throw new VictimExistsException(
					"Victim association exists");
		}
		VictimAssociation victimAssociation
			= this.victimAssociationInstanceFactory.createInstance();
		victimAssociation.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		victimAssociation.setRelationship(relationship);
		this.populateVictimAssociation(victimAssociation,
				registeredDate, packetSent, packetSendDate, flags);
		return this.victimAssociationDao.makePersistent(victimAssociation);
	}
	
	/**
	 * Updates victim association.
	 * 
	 * @param association victim association
	 * @param registeredDate date registered
	 * @param packetSent whether packet is sent
	 * @param packetSendDate date packet is sent
	 * @param flags flags
	 * @return updated victim association
	 * @throws VictimExistsException if victim association exists
	 */
	public VictimAssociation update(final VictimAssociation association,
			final Date registeredDate, final Boolean packetSent,
			final Date packetSendDate, final VictimAssociationFlags flags)
				throws VictimExistsException {
		if (this.victimAssociationDao.findExcluding(
				association.getRelationship(), association) != null) {
			throw new VictimExistsException(
					"Victim association exists");
		}
		this.populateVictimAssociation(association,
				registeredDate, packetSent, packetSendDate, flags);
		return victimAssociationDao.makePersistent(association);
	}
	
	/**
	 * Finds victim associations by offender.
	 * 
	 * @param offender offender by which to find victim associations 
	 * @return victim associations by offender
	 */
	public List<VictimAssociation> findByOffender(final Offender offender) {
		return this.victimAssociationDao.findByOffender(offender);
	}
	
	/**
	 * Finds associations by victim.
	 * 
	 * @param victim victim
	 * @return associations by victim
	 */
	public List<VictimAssociation> findByVictim(final Person victim) {
		return this.victimAssociationDao.findByVictim(victim);
	}
	
	/**
	 * Removes victim association.
	 * 
	 * @param association victim association to remove
	 */
	public void remove(final VictimAssociation association) {
		this.victimAssociationDao.makeTransient(association);
	}
	
	/**
	 * Returns count of victim associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return count of victim associations by relationship
	 */
	public long countByRelationship(final Relationship relationship) {
		return this.victimAssociationDao.countByRelationship(relationship);
	}
	
	/**
	 * Removes victim associations by relationship.
	 * 
	 * @param relationship relationship
	 * @return number of victim associations removed by relationship
	 */
	public int removeByRelationship(final Relationship relationship) {
		return this.victimAssociationDao.removeByRelationship(relationship);
	}
	
	/* Helper methods. */
	
	// Populates victim association
	private void populateVictimAssociation(
			final VictimAssociation association,
			final Date registeredDate,
			final Boolean packetSent, final Date packetSentDate,
			final VictimAssociationFlags flags) {
		association.setUpdateSignature(new UpdateSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		association.setRegisterDate(registeredDate);
		association.setPacketSent(packetSent);
		association.setPacketSendDate(packetSentDate);
		association.setFlags(flags);
	}
}