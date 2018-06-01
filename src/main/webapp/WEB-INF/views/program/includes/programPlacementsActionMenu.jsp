<%--
  - Action menu for program placements.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.program.msgs.program" var="programBundle"/>
<ul>
	<c:if test="${not empty offender}">
		<sec:authorize access="hasRole('PROGRAM_PLACEMENT_CREATE') or hasRole('ADMIN')">
			<li><a class="createLink" href="${pageContext.request.contextPath}/program/create.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="createProgramPlacementLink" bundle="${programBundle}"/></span></a></li>
		</sec:authorize>
	</c:if>
	<sec:authorize access="hasRole('PROGRAM_PLACEMENT_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/program/programPlacementListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="programPlacementListingReportLinkLabel" bundle="${programBundle}"/></a>
			</li>
		</c:if>
	</sec:authorize>
	<c:if test="${not empty programPlacement}">
		<sec:authorize access="hasRole('PROGRAM_PLACEMENT_VIEW') or hasRole('ADMIN')">
			<li><a class="viewEditLink" href="${pageContext.request.contextPath}/program/edit.html?programPlacement=${programPlacement.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditProgramPlacementLink" bundle="${programBundle}"/></span></a></li>
		</sec:authorize>
	</c:if>
	<c:if test="${not empty programPlacement}">
		<sec:authorize access="hasRole('PROGRAM_PLACEMENT_REMOVE') or hasRole('ADMIN')">
			<li><a class="removeLink" href="${pageContext.request.contextPath}/program/remove.html?programPlacement=${programPlacement.id}"><span class="visibleLinkLabel"><fmt:message key="removeProgramPlacementLink" bundle="${programBundle}"/></span></a></li>
		</sec:authorize>
	</c:if>
	<sec:authorize access="hasRole('PROGRAM_PLACEMENT_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty programPlacement}">
			<li>
				<a href="${pageContext.request.contextPath}/program/programPlacementDetailsReport.html?programPlacement=${programPlacement.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="programPlacementDetailsReportLinkLabel" bundle="${programBundle}"/></a>
			</li>
		</c:if>
	</sec:authorize>
</ul>