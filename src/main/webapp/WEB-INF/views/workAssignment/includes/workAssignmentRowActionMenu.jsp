<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.workassignment.msgs.workAssignment">
	<ul>
		<sec:authorize access="hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('WORK_ASSIGNMENT_EDIT') or hasRole('ADMIN')">
		<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/workAssignment/edit.html?workAssignment=${workAssignment.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" /></span></a>
		</li>
		</sec:authorize> 
		<sec:authorize access="hasRole('WORK_ASSIGNMENT_REMOVE') or hasRole('ADMIN')">
		<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/workAssignment/remove.html?workAssignment=${workAssignment.id}&offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="removeLink" /></span></a>
		</li>
		</sec:authorize>	
		<sec:authorize access="hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty workAssignment}">
			<li>
				<a href="${pageContext.request.contextPath}/workAssignment/workAssignmentsDetailsReport.html?workAssignment=${workAssignment.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="workAssignmentsDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
	</sec:authorize>
	</ul>
</fmt:bundle>