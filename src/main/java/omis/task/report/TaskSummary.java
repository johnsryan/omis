package omis.task.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Summary of task.
 *
 * @author Stephen Abson
 * @author Annie Wahl
 * @version 0.0.2
 * @since OMIS 3.0
 */
public class TaskSummary
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Long id;
	
	private final String controllerName;
	
	private final String methodName;
	
	private final String description;
	
	private final String sourceUserLastName;
	
	private final String sourceUserFirstName;
	
	private final String sourceUserMiddleName;
	
	private final String sourceUserSuffix;
	
	private final String sourceUsername;
	
	private final Date originationDate;
	
	private final Date completionDate;
	
	private final Boolean invoked;
	
	/**
	 * Instantiates summary of task.
	 * 
	 * @param id ID
	 * @param controllerName controller name
	 * @param methodName method name
	 * @param description description
	 * @param sourceUserLastName last name of source user
	 * @param sourceUserFirstName first name of source user
	 * @param sourceUserMiddleName middle name of source user
	 * @param sourceUserSuffix suffix of source user
	 * @param sourceUsername username of source user
	 * @param originationDate date of origination
	 * @param completionDate date of completion
	 */
	public TaskSummary(final Long id, final String controllerName,
			final String methodName, final String description,
			final String sourceUserLastName, final String sourceUserFirstName,
			final String sourceUserMiddleName, final String sourceUserSuffix,
			final String sourceUsername, final Date originationDate,
			final Date completionDate) {
		this.id = id;
		this.controllerName = controllerName;
		this.methodName = methodName;
		this.description = description;
		this.sourceUserLastName = sourceUserLastName;
		this.sourceUserFirstName = sourceUserFirstName;
		this.sourceUserMiddleName = sourceUserMiddleName;
		this.sourceUserSuffix = sourceUserSuffix;
		this.sourceUsername = sourceUsername;
		this.originationDate = originationDate;
		this.completionDate = completionDate;
		this.invoked = null;
	}
	
	/**
	 * Instantiates summary of task.
	 * 
	 * @param id ID
	 * @param controllerName controller name
	 * @param methodName method name
	 * @param description description
	 * @param sourceUserLastName last name of source user
	 * @param sourceUserFirstName first name of source user
	 * @param sourceUserMiddleName middle name of source user
	 * @param sourceUserSuffix suffix of source user
	 * @param sourceUsername username of source user
	 * @param originationDate date of origination
	 * @param completionDate date of completion
	 */
	public TaskSummary(final Long id, final String controllerName,
			final String methodName, final String description,
			final String sourceUserLastName, final String sourceUserFirstName,
			final String sourceUserMiddleName, final String sourceUserSuffix,
			final String sourceUsername, final Date originationDate,
			final Date completionDate, final Boolean invoked) {
		this.id = id;
		this.controllerName = controllerName;
		this.methodName = methodName;
		this.description = description;
		this.sourceUserLastName = sourceUserLastName;
		this.sourceUserFirstName = sourceUserFirstName;
		this.sourceUserMiddleName = sourceUserMiddleName;
		this.sourceUserSuffix = sourceUserSuffix;
		this.sourceUsername = sourceUsername;
		this.originationDate = originationDate;
		this.completionDate = completionDate;
		this.invoked = invoked;
	}
	
	/**
	 * Returns ID.
	 * 
	 * @return ID
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * Returns controller name.
	 * 
	 * @return controller name
	 */
	public String getControllerName() {
		return this.controllerName;
	}
	
	/**
	 * Returns method name.
	 * 
	 * @return method name
	 */
	public String getMethodName() {
		return this.methodName;
	}
	
	/**
	 * Returns description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Returns last name of source user.
	 * 
	 * @return last name of source user
	 */
	public String getSourceUserLastName() {
		return this.sourceUserLastName;
	}
	
	/**
	 * Returns first name of source user.
	 * 
	 * @return first name of source user
	 */
	public String getSourceUserFirstName() {
		return this.sourceUserFirstName;
	}
	
	/**
	 * Returns middle name of source user.
	 * 
	 * @return middle name of source user
	 */
	public String getSourceUserMiddleName() {
		return this.sourceUserMiddleName;
	}
	
	/**
	 * Returns suffix of source user.
	 * 
	 * @return suffix of source user
	 */
	public String getSourceUserSuffix() {
		return this.sourceUserSuffix;
	}
	
	/**
	 * Returns username of source user.
	 * 
	 * @return username of source user
	 */
	public String getSourceUsername() {
		return this.sourceUsername;
	}
	
	/**
	 * Returns origination date.
	 * 
	 * @return origination date
	 */
	public Date getOriginationDate() {
		return this.originationDate;
	}
	
	/**
	 * Returns completion date.
	 * 
	 * @return completion date
	 */
	public Date getCompletionDate() {
		return this.completionDate;
	}

	/**
	 * Returns the invoked.
	 * @return invoked - Boolean invoked
	 */
	public Boolean getInvoked() {
		return this.invoked;
	}
}