<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.presentenceinvestigation.msgs.presentenceInvestigationHome">
<div id="requestHeader">
	<fieldset>
		<legend><fmt:message key="presentenceInvestigationDetailsTitle"/></legend>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="courtCaseLabel"/>
			</label>
			<c:out value="${summary.docketValue}"/>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="requestDateLabel"/>
			</label>
			<span class="detail">
				<fmt:formatDate value="${summary.requestDate}" pattern="MM/dd/yyyy" />
			</span>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="expectedCompletionDateLabel"/>
			</label>
			<span class="detail">
				<fmt:formatDate value="${summary.expectedCompletionDate}" pattern="MM/dd/yyyy" />
			</span>
		</span>
		<span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="assignedUserLabel"/>
			</label>
			<c:out value="${summary.assignedUserLastName}, ${summary.assignedUserFirstName} (${summary.assignedUserUserName})"/>
		</span>
		<%-- <span class="fieldGroup">
			<label class="fieldLabel detailsLabel">
				<fmt:message key="submissionDateLabel"/>
			</label>
			<c:out value="${summary.submissionDate}"/>
		</span> --%>
	</fieldset>
</div>
</fmt:bundle>
