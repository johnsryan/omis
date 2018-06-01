<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (Aug 29, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.workassignment.msgs.workAssignment">
	<ul>
		<sec:authorize access="hasRole('WORK_ASSIGNMENT_CREATE') or hasRole('ADMIN')">
			<li>
				<a class="createLink" href="${pageContext.request.contextPath}/workAssignment/create.html?offender=${offender.id}">
					<span class="visibleLinkLabel">
						<fmt:message key="createWorkAssignmentLabel"/>
					</span>
				</a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/workAssignment/offenderWorkAssignmentHistoryReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab adobeReportLink"><fmt:message key="offenderWorkAssignmentHistoryReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>		
		<sec:authorize access="hasRole('WORK_ASSIGNMENT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/workAssignment/workAssignmentsListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="workAssignmentsListingReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>