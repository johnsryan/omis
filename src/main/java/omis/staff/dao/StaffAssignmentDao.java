package omis.staff.dao;

import java.util.Date;
import java.util.List;

import omis.dao.GenericDao;
import omis.location.domain.Location;
import omis.person.domain.Person;
import omis.staff.domain.StaffAssignment;
import omis.supervision.domain.SupervisoryOrganization;

/**
 * Data access object for staff assignments.
 * 
 * @author Stephen Abson
 * @version 0.1.0 (Oct 21, 2013)
 * @since OMIS 3.0
 */
public interface StaffAssignmentDao
		extends GenericDao<StaffAssignment> {

	/**
	 * Returns staff assignments to the supervisory organization on the date.
	 * 
	 * @param supervisoryOrganization supervisory organization
	 * @param date date
	 * @return staff assignments to supervisory organization on date
	 */
	List<StaffAssignment> findBySupervisoryOrganizationOnDate(
			SupervisoryOrganization supervisoryOrganization, Date date);
	
	/**
	 * Returns staff assignments on the date.
	 * 
	 * @param date date
	 * @return staff assignments on date
	 */
	List<StaffAssignment> findOnDate(Date date);
	
	/**
	 * Returns staff members on date.
	 * 
	 * @param date date
	 * @return staff members on date
	 */
	List<Person> findStaffMembersOnDate(Date date);
	
	/**
	 * Returns correctional supervisors on date.
	 * 
	 * @param date date
	 * @return supervisors on date
	 */
	List<Person> findSupervisoryStaffMembersOnDate(Date date);
	
	/**
	 * Returns the staff assignment for the specified person on the specified
	 * date with the specified supervisory organization.
	 * 
	 * @param date date
	 * @param person person
	 * @param organization supervisory organization
	 * @return staff assignment; {@code null} if no staff assignment is found
	 */
	StaffAssignment findOnDate(Date date, Person person, 
			SupervisoryOrganization organization);
	
	/**
	 * Finds the staff assignment matching the specified information.
	 * 
	 * @param staffMember staff member
	 * @param supervisoryOrganization supervisory organization
	 * @param location location
	 * @param startDate start date
	 * @param endDate end date
	 * @return staff assignment
	 */
	StaffAssignment find(Person staffMember, 
			SupervisoryOrganization supervisoryOrganization, Location location, 
			Date startDate, Date endDate);
	
	/**
	 * Finds the staff assignment matching the specified information, excluding 
	 * the specified staff assignment.
	 * 
	 * @param staffMember staff member
	 * @param supervisoryOrganization supervisory organization
	 * @param location location
	 * @param startDate start date
	 * @param endDate end date
	 * @param excludedStaffAssignment excluded staff assignment
	 * @return staff assignment
	 */
	StaffAssignment findExcluding(Person staffMember, 
			SupervisoryOrganization supervisoryOrganization, Location location, 
			Date startDate, Date endDate, 
			StaffAssignment excludedStaffAssignment);
}