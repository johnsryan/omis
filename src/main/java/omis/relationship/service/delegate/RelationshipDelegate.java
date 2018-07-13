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
package omis.relationship.service.delegate;

import java.util.List;

import omis.audit.AuditComponentRetriever;
import omis.audit.domain.CreationSignature;
import omis.instance.factory.InstanceFactory;
import omis.person.domain.Person;
import omis.relationship.dao.RelationshipDao;
import omis.relationship.domain.Relationship;
import omis.relationship.exception.ReflexiveRelationshipException;
import omis.relationship.exception.RelationshipExistsException;

/**
 * Delegate for relationships.
 *
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.0.1 (Sep 1, 2015)
 * @since OMIS 3.0
 */
public class RelationshipDelegate {
	
	/* Resources. */
	
	private final RelationshipDao relationshipDao;
	
	private final InstanceFactory<Relationship> relationshipInstanceFactory;
	
	private final AuditComponentRetriever auditComponentRetriever;

	/**
	 * Instantiates delegate for relationships.
	 * 
	 * @param relationshipDao data access object for relationships
	 * @param relationshipInstanceFactory instance factory for relationships
	 * @param auditComponentRetriever audit component retriever
	 */
	public RelationshipDelegate(
			final RelationshipDao relationshipDao,
			final InstanceFactory<Relationship> relationshipInstanceFactory,
			final AuditComponentRetriever auditComponentRetriever) {
		this.relationshipDao = relationshipDao;
		this.relationshipInstanceFactory = relationshipInstanceFactory;
		this.auditComponentRetriever = auditComponentRetriever;
	}
	
	/**
	 * Creates relationship.
	 * 
	 * @param firstPerson first person
	 * @param secondPerson second person
	 * @return relationship
	 * @throws RelationshipExistsException if relationship exists
	 * @throws ReflexiveRelationshipException if first and second person are
	 * equal
	 * @throws ReflexiveRelationshipException 
	 */
	public Relationship create(
			final Person firstPerson, final Person secondPerson)
				throws RelationshipExistsException, 
				ReflexiveRelationshipException {
		if (this.relationshipDao.findByPeople(
				firstPerson, secondPerson) != null) {
			throw new RelationshipExistsException("Relationship exists");
		}
		return this.createWithoutDuplicateCheck(firstPerson, secondPerson);
	}

	/**
	 * Returns relationship.
	 * 
	 * @param firstPerson first person
	 * @param secondPerson second person
	 * @return relationship
	 */
	public Relationship find(
			final Person firstPerson, final Person secondPerson) {
		return this.relationshipDao.findByPeople(firstPerson, secondPerson);
	}
	
	/**
	 * Returns relationship.
	 * 
	 * <p>If relationship does not exist, create and return new relationship.
	 * 
	 * <p>The method is provided to avoid redundant look ups of relationships
	 * when creating a relationship that does not exist for use as a property
	 * of an new instance of an associated entity (typically extending
	 * {@code RelationshipAssociable). 
	 * 
	 * @param firstPerson first person
	 * @param secondPerson second person
	 * @return relationship; never {@code null}
	 * @throws ReflexiveRelationshipException if first and second person are
	 * equal
	 */
	public Relationship findOrCreate(
			final Person firstPerson, final Person secondPerson)
				throws ReflexiveRelationshipException {
		Relationship relationship = this.relationshipDao.findByPeople(
				firstPerson, secondPerson);
		if (relationship != null) {
			return relationship;
		} else {
			return this.createWithoutDuplicateCheck(firstPerson, secondPerson);
		}
	}
	
	/* Helper methods. */
	
	// Creates relationship without duplicate check
	private Relationship createWithoutDuplicateCheck(
			final Person firstPerson, final Person secondPerson)
				throws ReflexiveRelationshipException {
		if (firstPerson.equals(secondPerson)) {
			throw new ReflexiveRelationshipException(
					"Persons in relationship are equal");
		}
		Relationship relationship = this.relationshipInstanceFactory
				.createInstance();
		relationship.setCreationSignature(new CreationSignature(
				this.auditComponentRetriever.retrieveUserAccount(),
				this.auditComponentRetriever.retrieveDate()));
		relationship.setFirstPerson(firstPerson);
		relationship.setSecondPerson(secondPerson);
		return this.relationshipDao.makePersistent(relationship);
	}
	
	/**
	 * Remove relationship.
	 * 
	 * @param relationship relationship	
	 */
	public void remove(final Relationship relationship) {
		this.relationshipDao.makeTransient(relationship);
	}

	/**
	 * Returns relationships for the specified person.
	 * 
	 * @param person person
	 * @return list of relationships
	 */
	public List<Relationship> findByPerson(final Person person) {
		return this.relationshipDao.findByPerson(person);
	}
}