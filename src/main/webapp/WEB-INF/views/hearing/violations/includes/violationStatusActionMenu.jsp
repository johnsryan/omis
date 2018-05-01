<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.hearing.msgs.hearing">
	<ul>
		<sec:authorize access="hasRole('HEARING_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/hearing/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listHearingsLink"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_EVENT_LIST') or hasRole('ADMIN')">
			<li>
				<a class="listLink" href="${pageContext.request.contextPath}/violationEvent/list.html?offender=${offender.id}"><span class="visibleLinkLabel"><fmt:message key="listViolationEventsLink"/></span></a>
			</li>
		</sec:authorize>
				<sec:authorize access="hasRole('VIOLATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/violations/violationStatusReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="violationStatusReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('VIOLATION_VIEW') or hasRole('ADMIN')">
		<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/hearing/violations/violationSummaryOffenderReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="violationSummaryOffenderReportLinkLabel"/></a>
			</li>
		</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>