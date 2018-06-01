<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (Feb 26, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.incident.msgs.incident">
	<ul>
		<sec:authorize access="hasRole('INCIDENT_STATEMENT_EDIT') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/incident/statement/edit.html?incidentStatement=${incidentStatement.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('INCIDENT_STATEMENT_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/incident/statement/remove.html?incidentStatement=${incidentStatement.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('INCIDENT_STATEMENT_REPORT_VIEW') or hasRole('ADMIN')">
			<li>
				<a href="${pageContext.request.contextPath}/incident/statement/incidentStatementReport.html?incidentStatement=${incidentStatement.id}&reportFormat=PDF" class="newTab adobeReportLink visibleLinkLabel"><fmt:message key="submittedIncidentStatementReportLinkLabel"/></a>
			</li>
		</sec:authorize>
	</ul>
</fmt:bundle>