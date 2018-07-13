package omis.offenderrelationship.service.impl;

import java.util.List;

import omis.family.service.delegate.FamilyAssociationDelegate;
import omis.offenderrelationship.service.OffenderRelationRemovalService;
import omis.person.domain.Person;
import omis.person.service.delegate.PersonDelegate;
import omis.relationship.domain.Relationship;
import omis.relationship.service.delegate.RelationshipDelegate;
import omis.victim.service.delegate.VictimAssociationDelegate;
import omis.visitation.service.delegate.VisitationAssociationDelegate;

/**
 * Offender relation removal service implementation.
 * 
 * @author Joel Norris
 * @version 0.1.0 (June 19, 2018)
 * @since OMIS 3.0
 */
public class OffenderRelationRemovalServiceImpl implements OffenderRelationRemovalService {

	private PersonDelegate personDelegate;
	private RelationshipDelegate relationshipDelegate;
	private VisitationAssociationDelegate visitationAssociationDelegate;
	private VictimAssociationDelegate victimAssociationDelegate;
	private FamilyAssociationDelegate familyAssociationDelegate;
	//private CriminalAssociationDelegate criminalAssociationDelegate;
	
	/**
	 * Instantiates an offender relation removal service with the specified delegates.
	 * 
	 * @param personDelegate person delegate
	 * @param relationshipDelegate relationship delegate
	 * @param visitationAssociationDelegate visitation association delegate
	 * @param victimAssociationDelegate victim association delegate
	 * @param familyAssociationDelegate family association delegate
	 */
	public OffenderRelationRemovalServiceImpl(final PersonDelegate personDelegate,
			final RelationshipDelegate relationshipDelegate,
			final VisitationAssociationDelegate visitationAssociationDelegate,
			final VictimAssociationDelegate victimAssociationDelegate,
			final FamilyAssociationDelegate familyAssociationDelegate) {
		this.personDelegate = personDelegate;
		this.relationshipDelegate = relationshipDelegate;
		this.visitationAssociationDelegate = visitationAssociationDelegate;
		this.victimAssociationDelegate = victimAssociationDelegate;
		this.familyAssociationDelegate = familyAssociationDelegate;
	}

	/** {@inheritDoc} */
	@Override
	public void remove(final Person person) throws IllegalArgumentException {
		if (!this.visitationAssociationDelegate.findByPerson(person).isEmpty()) {
			throw new IllegalArgumentException("Visitation Association exists");
		} else if (!this.victimAssociationDelegate.findByVictim(person).isEmpty()) {
			throw new IllegalArgumentException("Victim Association exists");
		} else if (!this.familyAssociationDelegate.findByPerson(person).isEmpty()) {
			throw new IllegalArgumentException("Family Association exists");
		} else {
			List<Relationship> relationships = this.relationshipDelegate.findByPerson(person);
			for (Relationship relationship: relationships) {
				this.relationshipDelegate.remove(relationship);
			}
			this.personDelegate.remove(person);
		}
	}
}